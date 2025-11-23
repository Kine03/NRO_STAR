package nro.models.kygui;

import lombok.Getter;
import nro.dialog.ConfirmDialog;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.player.Inventory;
import nro.models.player.Player;
import nro.server.io.Message;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.NpcService;
import nro.services.Service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;
import nro.consts.ConstItem;

/**
 * @debug Arriety
 */
public class ConsignmentShop {

    /**
     *
     */
    private static final ConsignmentShop INSTANCE = new ConsignmentShop();

    private static final byte CONSIGN = 0;

    private static final byte CCANCEL_CONSIGN = 1;

    private static final byte GET_MONEY = 2;

    private static final byte BUY = 3;

    private static final byte NEXT_PAGE = 4;

    private static final byte UP_TOP = 5;

    public static ConsignmentShop getInstance() {
        return INSTANCE;
    }

    @Getter
    private List<ConsignmentItem> list = new ArrayList<>();

    private Map<Long, ConsignmentItem> mapItemsExpired = new HashMap<>();

    public String[] tabName = {"Trang bị", "Phụ kiện", "Hỗ trợ", "Linh tinh", ""};

    public void handler(Player player, Message m) {
        try {
            DataInputStream dis = m.reader();
            byte action = dis.readByte();
            switch (action) {
                case CONSIGN: {
                    short itemID = dis.readShort();
                    byte monneyType = dis.readByte();
                    int money = dis.readInt();
                    int quantity = 0;
                    if (player.isVersionAbove(222)) {
                        quantity = dis.readInt();
                    } else {
                        quantity = dis.readByte();
                    }
                    consign(player, itemID, monneyType, money, quantity);
                }
                break;
                case BUY: {
                    short itemID = dis.readShort();
                    byte monneyType = dis.readByte();
                    int money = dis.readInt();
                    buy(player, itemID, monneyType, money);
                }
                break;
                case GET_MONEY: {
                    short itemID = dis.readShort();
                    getMoney(player, itemID);
                }
                break;
                case CCANCEL_CONSIGN: {
                    short itemID = dis.readShort();
                    cancelConsign(player, itemID);
                }
                break;
                case NEXT_PAGE: {
                    byte tab = dis.readByte();
                    byte page = dis.readByte();
                    nextPage(player, tab, page);
                }
                break;
                case UP_TOP: {
                    short itemID = dis.readShort();
                    upTop(player, itemID);
                }
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void upTop(Player player, short itemID) {
        ConfirmDialog confirmDialog = new ConfirmDialog("Bạn có muốn đưa vật phẩm này của bản thân lên trang đầu?\nYêu cầu 50 hồng ngọc.", () -> {
            if (player.inventory.ruby < 50) {
                Service.getInstance().sendThongBao(player, "Bạn không đủ hồng ngọc");
                show(player);
                return;
            }
            ConsignmentItem consignmentItem = findItemConsign(player.id, itemID);
            if (!player.getSession().actived) {
                Service.getInstance().sendThongBao(player, "Bạn chưa phải là thành viên của NRS");
                show(player);
                return;
            }
            if (consignmentItem == null || consignmentItem.getSold() == 1) {
                Service.getInstance().sendThongBao(player, "Vật phẩm không tồn tại hoặc đã được bán");
                return;
            }
            if (consignmentItem.isUpTop()) {
                Service.getInstance().sendThongBao(player, "Vật phẩm này đã up top rồi");
                return;
            }
            if (player.inventory.ruby < 0) {
                Service.getInstance().sendThongBao(player, "Bạn không đủ hồng ngọc");
                return;
            }
            player.inventory.subRuby(50);
            Service.getInstance().sendMoney(player);
            consignmentItem.setUpTop(true);
            Service.getInstance().sendThongBao(player, "Vật phẩm " + consignmentItem.template.name + " của bạn đã up top thành công");
            show(player);
        });
        confirmDialog.show(player);
    }

    private void cancelConsign(Player player, short itemID) {
        ConsignmentItem item = findItemConsign(player.id, itemID);
        if (item == null) {
            Service.getInstance().sendThongBao(player, "không tìm thấy vật phẩm");
            show(player);
            return;
        }
        if (item.getSold() == 1) {
            Service.getInstance().sendThongBao(player, "Vật phẩm đã đã được bán");
            show(player);
            return;
        }
        // Tạo lại item đúng kiểu
        Item itemReturn = ItemService.gI().createNewItem(item.getId(), item.quantity);
        itemReturn.quantity = item.quantity;
        itemReturn.itemOptions.addAll(item.itemOptions);

        // Thêm vào túi
        InventoryService.gI().addItemBag(player, itemReturn, 9999);
        InventoryService.gI().sendItemBags(player);
//        InventoryService.gI().addItemBag(player, item, item.quantity);
//        InventoryService.gI().sendItemBags(player);
//        Service.getInstance().sendThongBao(player, "Add Vật phẩm " + itemReturn.template.name + " với số lượng " + itemReturn.quantity);
        Service.getInstance().sendThongBao(player, "Hủy kí gửi thành công");
        removeItem(item);
        show(player);
    }

    public void addItem(ConsignmentItem item) {
        synchronized (list) {
            list.add(item);
        }
    }

    public void addExpiredItem(ConsignmentItem item) {
        mapItemsExpired.put(item.getConsignorID(), item);
    }

    public void removeItem(ConsignmentItem item) {
        synchronized (list) {
            list.remove(item);
        }
    }

    private void consign(Player player, short itemID, byte monneyType, int money, int quantity) {
        Item thoivang = ItemService.gI().createNewItem((short) 457);
        if (player.playerTask.taskMain.id < 20) {
            Service.getInstance().sendThongBao(player, "Bạn phải hoàn thành nhiệm vụ Tiểu đội sát thủ");
            show(player);
            return;
        }
        if (thoivang == null && thoivang.quantity < 1) {
            Service.getInstance().sendThongBao(player, "Phí kí gửi là 1 Thỏi Vàng");
            return;
        }
        if (player.nPoint.power < 50_000_000_000L) {
            Service.getInstance().sendThongBao(player, "Bạn chưa đủ 50 tỷ sức mạnh");
            show(player);
            return;
        }
        if (!player.getSession().actived) {
            Service.getInstance().sendThongBao(player, "Bạn chưa phải là thành viên của Ngọc rồng star");
            show(player);
            return;
        }
        if (quantity < 0 || quantity > 9999) {
            Service.getInstance().sendThongBao(player, "Chỉ có thể kí gửi tối đa x9999");
            return;
        }
        if (money <= 2 || money > 2000000000) {
            Service.getInstance().sendThongBao(player, "Chỉ có thể ký gửi từ 2 thỏi vàng trở lên");
            return;
        }
        if (findItemConsign(player.id, itemID) != null) {
            Service.getInstance().sendThongBao(player, "Không thể kí gửi nhiều vật phẩm giống nhau");
            return;
        }
        Item item = InventoryService.gI().findItem(player, itemID, quantity);
        if (item == null) {
            Service.getInstance().sendThongBao(player, "Không tìm thấy vật phẩm");
            return;
        }
        for (ItemOption io : item.itemOptions) {
            if (io.optionTemplate.id == 30) {
                Service.getInstance().sendThongBao(player, "Vật phẩm không thể giao dịch không thể ký gửi");
                return;
            }
        }

        ConsignmentItem consignmentItem = ItemService.gI().convertToConsignmentItem(item);
        if (monneyType == 0) {
            consignmentItem.setPriceGold(money);
        } else {
            consignmentItem.setPriceGem(money);
        }
        consignmentItem.createTime = System.currentTimeMillis();
        consignmentItem.setConsignID(getMaxId() + 1);
        consignmentItem.setConsignorID(player.id);
        consignmentItem.setTab(getTabByType(consignmentItem.template.type));
        consignmentItem.quantity = quantity;
        addItem(consignmentItem);
        InventoryService.gI().subQuantityItemsBag(player, item, quantity);
        InventoryService.gI().sendItemBags(player);
        Service.getInstance().sendMoney(player);
        show(player);
        Service.getInstance().sendThongBao(player, "Kí gửi vật phẩm thành công");
    }

    public int getMaxId() {
        try {
            List<Integer> id = new ArrayList<>();
            list.stream().filter((it) -> (it != null)).forEachOrdered((it) -> {
                id.add(it.getConsignID());
            });
            return Collections.max(id);
        } catch (Exception e) {
            return 0;
        }
    }

    public ConsignmentItem findItemConsign(long consignerID, short itemID) {
        for (ConsignmentItem consignmentItem : list) {
            if (consignmentItem.getConsignorID() == consignerID && consignmentItem.template.id == itemID) {
                return consignmentItem;
            }
        }
        return null;
    }

    private List<ConsignmentItem> getItemConsignByTab(Player player, byte tab, int... max) {
        List<ConsignmentItem> items = new ArrayList<>();
        List<ConsignmentItem> listSort = new ArrayList<>();
        List<ConsignmentItem> listSort2 = new ArrayList<>();
        for (ConsignmentItem item : list) {
            if (item != null && item.getTab() == tab && item.getSold() == 0 && item.getConsignorID() != player.id) {// isSold || 1 là bán : 0 là chưa bán
                items.add(item);
            }
        }
        Collections.sort(items, (item1, item2) -> Boolean.compare(item2.template.isUpToUp, item1.template.isUpToUp));
        switch (max.length) {
            case 2: {
                int startIndex = Math.min(max[0], items.size());
                int endIndex = Math.min(max[1], items.size());
                listSort.addAll(items.subList(startIndex, endIndex));
                break;
            }
            case 1: {
                int endIndex = Math.min(max[0], items.size());
                listSort.addAll(items.subList(0, endIndex));
                break;
            }
            default:
                listSort.addAll(items);
                break;
        }

        for (ConsignmentItem item : listSort) {
            if (item != null) {
                listSort2.add(item);
            }
        }
        return listSort2;
    }

    private List<ConsignmentItem> getItemCanConsign(Player player) {
        List<ConsignmentItem> items = new ArrayList<>();
        list.stream().filter((it) -> (it != null && it.getConsignorID() == player.id)).forEachOrdered((it) -> {
            items.add(it);
        });

        player.inventory.itemsBag.stream().filter(item -> item.isNotNullItem()).forEach(item -> {
            item.itemOptions.forEach(itemOption -> {
                if (itemOption.optionTemplate.id == 31) {
                    if (itemOption.param >= 32767) {
                        itemOption.param = 32767;
                    }
                }
            });
        });
        player.inventory.itemsBag.stream().filter((item)
                -> (item.isNotNullItem()
                && item.template.id != ConstItem.THOI_VANG
                && canConsign(item.template.type)
                && item.canConsign())).forEachOrdered((it) -> {
            ConsignmentItem consignmentItem = ItemService.gI().convertToConsignmentItem(it);
            consignmentItem.setConsignorID(-1);
            consignmentItem.setTab((byte) 4);
            consignmentItem.setPriceGem(-1);
            consignmentItem.setPriceGold(-1);
            consignmentItem.setSold(0);
            items.add(consignmentItem);
        });
        return items;
    }

    private boolean canConsign(int type) {
        return (type < 5 && type >= 0) || type == 12 || type == 33 || type == 29 || type == 27 || type == 11 || type == 5;
    }

    private byte getTabByType(byte type) {
        byte tab = -1;
        if (type >= 0 && type <= 2) {
            tab = 0;
        } else if (type >= 3 && type <= 4) {
            tab = 1;
        } else if (type == 29 || type == 33 || type == 12) {
            tab = 2;
        } else {
            tab = 3;
        }
        return tab;
    }

    public void buy(Player player, short itemID, byte monneyType, int money) {
        for (ConsignmentItem item : list) {
            Item thoivang = InventoryService.gI().findItemBagByTemp(player, (short) 457);
            if (item.getConsignID() == itemID && monneyType == monneyType && money == money) {
                if (player.playerTask.taskMain.id < 24) {
                    Service.getInstance().sendThongBao(player, "Bạn phải hoàn thành nhiệm vụ Xên hoàn thiện");
                    show(player);
                    return;
                }
                if (player.nPoint.power < 100_000_000_000L) {
                    Service.getInstance().sendThongBao(player, "Bạn chưa đủ 100 tỷ sức mạnh !!");
                    show(player);
                    return;
                }
                if (!player.getSession().actived) {
                    Service.getInstance().sendThongBao(player, "Bạn chưa phải là thành viên của Ngọc rồng star");
                    show(player);
                    return;
                }
                Inventory inventory = player.inventory;
                if (item.getConsignorID() == player.id) {
                    Service.getInstance().sendThongBao(player, "Không thể mua vật phẩm bản thân đăng bán");
                    show(player);
                    return;
                }
                if (monneyType == 0) {
                    if (thoivang == null) {
                        Service.getInstance().sendThongBao(player, "Bạn còn thiếu " + (money - thoivang.quantity) + "Thỏi Vàng");
                        show(player);
                        return;
                    } else if (thoivang.quantity < money) {
                        Service.getInstance().sendThongBao(player, "Bạn còn thiếu " + (money - thoivang.quantity) + "Thỏi Vàng");
                        show(player);
                        return;
                    }
                }
                InventoryService.gI().subQuantityItemsBag(player, thoivang, money);
                InventoryService.gI().addItemBag(player, item, 999);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                Service.getInstance().sendThongBao(player, "Mua vật phẩm thành công");
                item.setSold(1);
                show(player);
            }
        }
    }

    public void getMoney(Player player, short itemID) {
        for (ConsignmentItem item : list) {
            if (item.template.id == itemID && item.getConsignorID() == player.id && item.getSold() == 1) {
                Item thoivang = ItemService.gI().createNewItem((short) 457);
                if (item.getPriceGold() > 1) {
                    thoivang.quantity = item.getPriceGold() - 1; // số Đồng Coin Brics = số Đồng Coin Brics - 1 Đồng Coin Brics phí
                    InventoryService.gI().addItemBag(player, thoivang, 9999);
                }
                removeItem(item);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                Service.getInstance().sendThongBao(player, "Nhận thành công phí ký gửi là 1 thỏi vàng");
                show(player);
                return;
            }
        }
    }

    public void nextPage(Player player, byte tab, int page) {
        Message msg = new Message(-100);
        try {
            int maxPage = (byte) (list.size() / 20 > 0 ? list.size() / 20 : 1);
            DataOutputStream ds = msg.writer();
            ds.writeByte(tab);
            ds.writeByte(maxPage);
            ds.writeByte(page);
            List<ConsignmentItem> list = getItemConsignByTab(player, tab, (byte) (page * 20), (byte) (page * 20 + 20));
            for (ConsignmentItem item : list) {
                ds.writeShort(item.template.id);
                ds.writeShort(item.template.id);
                ds.writeInt(item.getPriceGold());
                ds.writeInt(item.getPriceGem());

                ds.writeByte(0);

                if (player.isVersionAbove(222)) {
                    ds.writeInt(item.quantity);
                } else {
                    ds.writeByte(item.quantity);
                }
                ds.writeByte(item.getConsignorID() == player.id ? 0 : 1); // isMe
                ds.writeByte(item.itemOptions.size());
                for (ItemOption option : item.itemOptions) {
                    ds.writeByte(option.optionTemplate.id);
                    ds.writeShort(option.param);
                }
                ds.writeByte(0);
                ds.writeByte(0);
            }
            showItemCanConsign(player, ds);
            ds.flush();
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show(Player player) {
        Message msg = new Message(-44);
        try {
            int tabLength = tabName.length;
            int maxPage = (byte) (list.size() / 20 > 0 ? list.size() / 20 : 1);
            DataOutputStream ds = msg.writer();
            ds.writeByte(2);
            ds.writeByte(tabLength);
            for (byte i = 0; i < tabLength - 1; i++) {
                List<ConsignmentItem> list = getItemConsignByTab(player, i);
                ds.writeUTF(tabName[i]);
                ds.writeByte(maxPage); // max page
                ds.writeByte(list.size());
                for (ConsignmentItem item : list) {
//                    System.err.println("ConsignID " + item.getConsignID());
                    ds.writeShort(item.template.id);
                    ds.writeShort(item.getConsignID());
                    ds.writeInt(item.getPriceGold());
                    ds.writeInt(item.getPriceGem());
                    ds.writeByte(0);
                    if (player.isVersionAbove(222)) {
                        ds.writeInt(item.quantity);
                    } else {
                        ds.writeByte(item.quantity);
                    }
                    ds.writeByte(item.getConsignorID() == player.id ? 0 : 1); // isMe
                    ds.writeByte(item.itemOptions.size());
                    for (ItemOption option : item.itemOptions) {
                        ds.writeByte(option.optionTemplate.id);
                        ds.writeShort(option.param);
                    }
                    ds.writeByte(0);
                    ds.writeByte(0);
                }
            }
            showItemCanConsign(player, ds);
            ds.flush();
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showItemCanConsign(Player player, DataOutputStream ds) throws IOException {
        List<ConsignmentItem> items = getItemCanConsign(player);
        ds.writeUTF("");
        ds.writeByte(0); // max page
        ds.writeByte(items.size());
        for (ConsignmentItem item : items) {
            ds.writeShort(item.template.id);
            ds.writeShort(item.template.id);
            ds.writeInt(item.getPriceGold());
            ds.writeInt(item.getPriceGem());
            if (item.getConsignorID() == -1) {
                ds.writeByte(0);
            } else if (item.getSold() == 1) {
                ds.writeByte(2);
            } else {
                ds.writeByte(1);
            }
            if (player.isVersionAbove(222)) {
                ds.writeInt(item.quantity);
            } else {
                ds.writeByte(item.quantity);
            }
            ds.writeByte(item.getConsignorID() == player.id ? 0 : 1); // isMe
            ds.writeByte(item.itemOptions.size());
            for (ItemOption option : item.itemOptions) {
                ds.writeByte(option.optionTemplate.id);
                ds.writeShort(option.param);
            }
            ds.writeByte(0);
            ds.writeByte(0);
        }
    }

    public int getDaysExpried(Long createTime) {
        long now = System.currentTimeMillis();
        long elapsedTimeMillis = now - createTime;
        long elapsedDays = elapsedTimeMillis / (24 * 60 * 60 * 1000);
        return (int) elapsedDays;
    }

    public void showExpiringItems(Player player) {
        if (mapItemsExpired.containsKey(player.id)) {
            StringBuilder sb = new StringBuilder();
            sb.append("|1|Danh sách vật phẩm sắp hết hạn:\n\n");
            for (Map.Entry<Long, ConsignmentItem> entry : mapItemsExpired.entrySet()) {
                ConsignmentItem item = entry.getValue();
                sb.append("- ").append(item.template.name).append("\n");
            }
            sb.append("Vật phẩm sẽ bị xóa nếu quá hạn 2 ngày");
            NpcService.gI().createMenuConMeo(player, -1, -1, sb.toString(), "OK");
            return;
        }
        Service.getInstance().sendThongBao(player, "Không có vật phẩm nào sắp hết hạn kí gửi");
    }

    public void sendExpirationNotification(Player player) {
        if (mapItemsExpired.containsKey(player.id)) {
            Service.getInstance().sendThongBao(player, "Bạn có vật phẩm sắp hết hạn đang kí gửi tại siêu thị");
        }
    }
}
