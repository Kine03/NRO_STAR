package nro.services.func;

import java.util.ArrayList;
import nro.consts.*;
import nro.dialog.MenuDialog;
import nro.dialog.MenuRunable;
import nro.event.Event;
import nro.lib.RandomCollection;
import nro.manager.MiniPetManager;
import nro.manager.NamekBallManager;
import nro.manager.PetFollowManager;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.item.MinipetTemplate;
import nro.models.map.*;
import nro.models.map.dungeon.zones.ZSnakeRoad;
import nro.models.map.war.NamekBallWar;
import nro.models.player.Inventory;
import nro.models.player.MiniPet;
import nro.models.player.PetFollow;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.server.Client;
import nro.server.Manager;
import nro.server.io.Message;
import nro.server.io.Session;
import nro.services.*;
import nro.utils.Log;
import nro.utils.SkillUtil;
import nro.utils.TimeUtil;
import nro.utils.Util;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;
import nro.jdbc.daos.PlayerDAO;
import nro.models.boss.BossManager;
import nro.models.map.zeno.ZenoWar21h;
import nro.models.npc.specialnpc.TrungLinhThu;
import nro.sendEff.SendEffect;

/**
 * @author 💖 Trần Lại 💖
 * @copyright 💖 GirlkuN 💖
 */
public class UseItem {

    private static final int ITEM_BOX_TO_BODY_OR_BAG = 0;
    private static final int ITEM_BAG_TO_BOX = 1;
    private static final int ITEM_BODY_TO_BOX = 3;
    private static final int ITEM_BAG_TO_BODY = 4;
    private static final int ITEM_BODY_TO_BAG = 5;
    private static final int ITEM_BAG_TO_PET_BODY = 6;
    private static final int ITEM_BODY_PET_TO_BAG = 7;

    private static final byte DO_USE_ITEM = 0;
    private static final byte DO_THROW_ITEM = 1;
    private static final byte ACCEPT_THROW_ITEM = 2;
    private static final byte ACCEPT_USE_ITEM = 3;

    private static UseItem instance;
    private static final Logger logger = Logger.getLogger(UseItem.class);

    private UseItem() {

    }

    public static UseItem gI() {
        if (instance == null) {
            instance = new UseItem();
        }
        return instance;
    }

    public void getItem(Session session, Message msg) {
        Player player = session.player;
        TransactionService.gI().cancelTrade(player);
        try {
            int type = msg.reader().readByte();
            int index = msg.reader().readByte();
            switch (type) {
                case ITEM_BOX_TO_BODY_OR_BAG:
                    InventoryService.gI().itemBoxToBodyOrBag(player, index);
                    TaskService.gI().checkDoneTaskGetItemBox(player);
                    break;
                case ITEM_BAG_TO_BOX:
                    InventoryService.gI().itemBagToBox(player, index);
                    break;
                case ITEM_BODY_TO_BOX:
                    InventoryService.gI().itemBodyToBox(player, index);
                    break;
                case ITEM_BAG_TO_BODY:
                    InventoryService.gI().itemBagToBody(player, index);
                    break;
                case ITEM_BODY_TO_BAG:
                    InventoryService.gI().itemBodyToBag(player, index);
                    break;
                case ITEM_BAG_TO_PET_BODY:
                    InventoryService.gI().itemBagToPetBody(player, index);
                    break;
                case ITEM_BODY_PET_TO_BAG:
                    InventoryService.gI().itemPetBodyToBag(player, index);
                    break;
            }
            player.setClothes.setup();
            if (player.pet != null) {
                player.pet.setClothes.setup();
            }
            player.setClanMember();
            PlayerService.gI().sendPetFollow(player);
            Service.getInstance().point(player);
        } catch (Exception e) {
            Log.error(UseItem.class, e);

        }
    }

    public void doItem(Player player, Message _msg) {
        TransactionService.gI().cancelTrade(player);
        Message msg;
        try {
            byte type = _msg.reader().readByte();
            int where = _msg.reader().readByte();
            int index = _msg.reader().readByte();

            switch (type) {
                case DO_USE_ITEM:
                    if (player != null && player.inventory != null) {
                        if (index != -1) {
                            if (index >= 0 && index < player.inventory.itemsBag.size()) {
                                Item item = player.inventory.itemsBag.get(index);
                                if (item.isNotNullItem()) {
                                    if (item.template.type == 21) {
                                        MinipetTemplate temp = MiniPetManager.gI().findByID(item.getId());
                                        if (temp == null) {
                                            System.err.println("khong tim thay minipet id: " + item.getId());
                                        }
                                        MiniPet.callMiniPet(player, item.template.id);
                                        InventoryService.gI().itemBagToBody(player, index);
                                        break;
                                    }
                                    if (item.template.type == 22) {
                                        msg = new Message(-43);
                                        msg.writer().writeByte(type);
                                        msg.writer().writeByte(where);
                                        msg.writer().writeByte(index);
                                        msg.writer().writeUTF("Bạn có muốn dùng " + player.inventory.itemsBag.get(index).template.name + "?");
                                        player.sendMessage(msg);
                                        msg.cleanup();
                                    } else if (item.template.type == 7) {
                                        msg = new Message(-43);
                                        msg.writer().writeByte(type);
                                        msg.writer().writeByte(where);
                                        msg.writer().writeByte(index);
                                        msg.writer().writeUTF("Bạn chắc chắn học " + player.inventory.itemsBag.get(index).template.name + "?");
                                        player.sendMessage(msg);
                                    } else if (player.isVersionAbove(220) && item.template.type == 23 || item.template.type == 24 || item.template.type == 11 || item.template.type == 35) {
                                        InventoryService.gI().itemBagToBody(player, index);
                                    } else if (item.template.id == 401) {
                                        msg = new Message(-43);
                                        msg.writer().writeByte(type);
                                        msg.writer().writeByte(where);
                                        msg.writer().writeByte(index);
                                        msg.writer().writeUTF("Sau khi đổi đệ sẽ mất toàn bộ trang bị trên người đệ tử nếu chưa tháo");
                                        player.sendMessage(msg);
                                    } else if (item.template.id == 1457) {
                                        msg = new Message(-43);
                                        msg.writer().writeByte(type);
                                        msg.writer().writeByte(where);
                                        msg.writer().writeByte(index);
                                        msg.writer().writeUTF("Sau khi đổi đệ sẽ mất toàn bộ trang bị trên người đệ tử nếu chưa tháo");
                                        player.sendMessage(msg);
                                    } else if (item.template.id == 1459) {
                                        msg = new Message(-43);
                                        msg.writer().writeByte(type);
                                        msg.writer().writeByte(where);
                                        msg.writer().writeByte(index);
                                        msg.writer().writeUTF("Sau khi đổi đệ sẽ mất toàn bộ trang bị trên người đệ tử nếu chưa tháo");
                                        player.sendMessage(msg);
                                    } else if (item.template.type == 72) {
                                        PetFollow pet = PetFollowManager.gI().findByID(item.getId());
                                        player.setPetFollow(pet);
                                        InventoryService.gI().itemBagToBody(player, index);
                                        PlayerService.gI().sendPetFollow(player);
                                    } else {
                                        useItem(player, item, index);
                                    }
                                }
                            }
                        } else {
                            InventoryService.gI().eatPea(player);
                        }
                    }
                    break;
                case DO_THROW_ITEM:
                    if (!(player.zone.map.mapId == 21 || player.zone.map.mapId == 22 || player.zone.map.mapId == 23)) {
                        Item item = null;
                        if (where == 0) {
                            if (index >= 0 && index < player.inventory.itemsBody.size()) {
                                item = player.inventory.itemsBody.get(index);
                            }
                        } else {
                            if (index >= 0 && index < player.inventory.itemsBag.size()) {
                                item = player.inventory.itemsBag.get(index);
                            }
                        }
                        if (item != null && item.isNotNullItem()) {
                            msg = new Message(-43);
                            msg.writer().writeByte(type);
                            msg.writer().writeByte(where);
                            msg.writer().writeByte(index);
                            msg.writer().writeUTF("Bạn chắc chắn muốn vứt " + item.template.name + "?");
                            player.sendMessage(msg);
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                    }
                    break;
                case ACCEPT_THROW_ITEM:
                    if (player.isUseMaBaoVe) {
                        Service.getInstance().sendThongBao(player, "Chức năng bảo vệ đã được bật. Bạn vui lòng kiểm tra lại");
                        return;
                    }
                    InventoryService.gI().throwItem(player, where, index);
                    break;
                case ACCEPT_USE_ITEM:
                    if (index >= 0 && index < player.inventory.itemsBag.size()) {
                        Item item = player.inventory.itemsBag.get(index);
                        if (item.isNotNullItem()) {
                            useItem(player, item, index);
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            Log.error(UseItem.class, e);
        }
    }

    public void useSatellite(Player player, Item item) {
        Satellite satellite = null;
        if (player.zone != null) {
            int count = player.zone.getSatellites().size();
            if (count < 3) {
                switch (item.template.id) {
                    case ConstItem.VE_TINH_TRI_LUC:
                        satellite = new SatelliteMP(player.zone, ConstItem.VE_TINH_TRI_LUC, player.location.x, player.location.y, player);
                        break;

                    case ConstItem.VE_TINH_TRI_TUE:
                        satellite = new SatelliteExp(player.zone, ConstItem.VE_TINH_TRI_TUE, player.location.x, player.location.y, player);
                        break;

                    case ConstItem.VE_TINH_PHONG_THU:
                        satellite = new SatelliteDefense(player.zone, ConstItem.VE_TINH_PHONG_THU, player.location.x, player.location.y, player);
                        break;

                    case ConstItem.VE_TINH_SINH_LUC:
                        satellite = new SatelliteHP(player.zone, ConstItem.VE_TINH_SINH_LUC, player.location.x, player.location.y, player);
                        break;
                }
                if (satellite != null) {
                    InventoryService.gI().subQuantityItemsBag(player, item, 1);
                    Service.getInstance().dropItemMapForMe(player, satellite);
                    Service.getInstance().dropItemMap(player.zone, satellite);
                }
            } else {
                Service.getInstance().sendThongBaoOK(player, "Số lượng vệ tinh có thể đặt trong khu vực đã đạt mức tối đa.");
            }
        }
    }

    private void useItem(Player pl, Item item, int indexBag) {
        if (pl.isUseMaBaoVe) {
            Service.getInstance().sendThongBao(pl, "Chức năng bảo vệ đã được bật. Bạn vui lòng kiểm tra lại");
            return;
        }
        if (Event.isEvent() && Event.getInstance().useItem(pl, item)) {
            return;
        }
        if (item.template.strRequire <= pl.nPoint.power) {
            int type = item.getType();
            switch (type) {
                case 6:
                    InventoryService.gI().eatPea(pl);
                    break;
                case 33:
                    RadaService.getInstance().useItemCard(pl, item);
                    break;
                case 22:
                    useSatellite(pl, item);
                    break;
                case 97:
                case 99:
                    InventoryService.gI().itemBagToBody(pl, indexBag);
//                    Service.getInstance().sendEffPlayer(pl);

                    break;
                default:
                    switch (item.template.id) {
                        case ConstItem.GOI_10_RADA_DO_NGOC:
                            findNamekBall(pl, item);
                            break;
                        case 2052:
                            capsule8thang3(pl, item);
                            break;
                        case 1460:
                            nanglvdetu(pl, item);
                            break;
                        case 1319://vải thô
                            UseItem.gI().hopQuaKichHoat(pl, item);
                            break;
                        case 1320://skh tl ngẫu nhiên
                            UseItem.gI().hopQuaThanLinh(pl, item);
                            break;
                        case 1321://skh chọn
                            UseItem.gI().hopQuaThanLinhskh(pl, item);
                            break;
                        case 1322://skh hd chọn
                            UseItem.gI().hopQuaHuyDiet(pl, item);
                            break;
                        case 1323:// skh ts chọn
                            UseItem.gI().hopQuaThienSu(pl, item);
                            break;
                        case ConstItem.CAPSULE_THOI_TRANG_30_NGAY:
                            capsuleThoiTrang(pl, item);
                            break;
                        case 457:
                            Input.gI().createFormBanThoiVang(pl);
                            break;
                        case 1520:
                            Input.gI().createFormBanBrics(pl);
                            break;
                        case 2039:
                            openboxsukien(pl, item, ConstEvent.SU_KIEN_TET);
                            break;
                        //////
                        case 1312: //hộp quà tân thủ
                            if (InventoryService.gI().getCountEmptyBag(pl) < 12) {
                                Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 12 ô trống hành trang");
                                return;
                            }
                            int itemId3 = 454;
                            Item PhanQua3 = ItemService.gI().createNewItem(((short) itemId3));

                            int itemId6 = 380;
                            Item PhanQua6 = ItemService.gI().createNewItem(((short) itemId6));
                            PhanQua6.quantity = 10;

                            int itemId11 = 441;
                            Item PhanQua11 = ItemService.gI().createNewItem(((short) itemId11));
                            PhanQua11.itemOptions.add(new ItemOption(95, 5));
                            PhanQua11.quantity = 10;

                            int itemId12 = 442;
                            Item PhanQua12 = ItemService.gI().createNewItem(((short) itemId12));
                            PhanQua12.itemOptions.add(new ItemOption(96, 5));
                            PhanQua12.quantity = 10;

                            int itemId13 = 443;
                            Item PhanQua13 = ItemService.gI().createNewItem(((short) itemId13));
                            PhanQua13.itemOptions.add(new ItemOption(97, 5));
                            PhanQua13.quantity = 10;

                            int itemId17 = 447;
                            Item PhanQua17 = ItemService.gI().createNewItem(((short) itemId17));
                            PhanQua17.itemOptions.add(new ItemOption(101, 5));
                            PhanQua17.quantity = 50;

                            pl.inventory.gold += 10000000000L;
                            pl.inventory.gem += 1000000;
                            pl.inventory.ruby += 4000;

//                        InventoryService.gI().addItemBag(pl, PhanQua2, 0);
//                        Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua2.Name());
                            InventoryService.gI().addItemBag(pl, PhanQua3, 0);
                            Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua3.Name());
                            if (pl.gender == 0) {
                                Item PhanQua5 = ItemService.gI().createNewItem(((short) 0));
                                PhanQua5.itemOptions.add(new ItemOption(47, 3));
                                PhanQua5.itemOptions.add(new ItemOption(107, 3));

                                Item PhanQua7 = ItemService.gI().createNewItem(((short) 6));
                                PhanQua7.itemOptions.add(new ItemOption(6, 20));
                                PhanQua7.itemOptions.add(new ItemOption(27, 3));
                                PhanQua7.itemOptions.add(new ItemOption(107, 3));

                                Item PhanQua8 = ItemService.gI().createNewItem(((short) 21));
                                PhanQua8.itemOptions.add(new ItemOption(0, 5));
                                PhanQua8.itemOptions.add(new ItemOption(107, 3));

                                Item PhanQua9 = ItemService.gI().createNewItem(((short) 27));
                                PhanQua9.itemOptions.add(new ItemOption(7, 5));
                                PhanQua9.itemOptions.add(new ItemOption(28, 3));
                                PhanQua9.itemOptions.add(new ItemOption(107, 3));

                                Item PhanQua10 = ItemService.gI().createNewItem(((short) 12));
                                PhanQua10.itemOptions.add(new ItemOption(14, 1));
                                PhanQua10.itemOptions.add(new ItemOption(107, 3));

                                Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua5.Name());
                                Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua7.Name());
                                Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua8.Name());
                                Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua9.Name());
                                Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua10.Name());
                                InventoryService.gI().addItemBag(pl, PhanQua5, 0);
                                InventoryService.gI().addItemBag(pl, PhanQua7, 0);
                                InventoryService.gI().addItemBag(pl, PhanQua8, 0);
                                InventoryService.gI().addItemBag(pl, PhanQua9, 0);
                                InventoryService.gI().addItemBag(pl, PhanQua10, 0);
                                InventoryService.gI().sendItemBags(pl);
                            }
                            if (pl.gender == 1) {
                                Item PhanQua5 = ItemService.gI().createNewItem(((short) 1));
                                PhanQua5.itemOptions.add(new ItemOption(47, 3));
                                PhanQua5.itemOptions.add(new ItemOption(107, 3));

                                Item PhanQua7 = ItemService.gI().createNewItem(((short) 7));
                                PhanQua7.itemOptions.add(new ItemOption(6, 20));
                                PhanQua7.itemOptions.add(new ItemOption(27, 3));
                                PhanQua7.itemOptions.add(new ItemOption(107, 3));

                                Item PhanQua8 = ItemService.gI().createNewItem(((short) 22));
                                PhanQua8.itemOptions.add(new ItemOption(0, 5));
                                PhanQua8.itemOptions.add(new ItemOption(107, 3));

                                Item PhanQua9 = ItemService.gI().createNewItem(((short) 28));
                                PhanQua9.itemOptions.add(new ItemOption(7, 5));
                                PhanQua9.itemOptions.add(new ItemOption(28, 3));
                                PhanQua9.itemOptions.add(new ItemOption(107, 3));

                                Item PhanQua10 = ItemService.gI().createNewItem(((short) 12));
                                PhanQua10.itemOptions.add(new ItemOption(14, 1));
                                PhanQua10.itemOptions.add(new ItemOption(107, 3));
                                Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua5.Name());
                                Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua7.Name());
                                Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua8.Name());
                                Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua9.Name());
                                Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua10.Name());
                                InventoryService.gI().addItemBag(pl, PhanQua5, 0);
                                InventoryService.gI().addItemBag(pl, PhanQua7, 0);
                                InventoryService.gI().addItemBag(pl, PhanQua8, 0);
                                InventoryService.gI().addItemBag(pl, PhanQua9, 0);
                                InventoryService.gI().addItemBag(pl, PhanQua10, 0);
                                InventoryService.gI().sendItemBags(pl);
                            }
                            if (pl.gender == 2) {
                                Item PhanQua5 = ItemService.gI().createNewItem(((short) 2));
                                PhanQua5.itemOptions.add(new ItemOption(47, 3));
                                PhanQua5.itemOptions.add(new ItemOption(107, 3));

                                Item PhanQua7 = ItemService.gI().createNewItem(((short) 8));
                                PhanQua7.itemOptions.add(new ItemOption(6, 20));
                                PhanQua7.itemOptions.add(new ItemOption(27, 3));
                                PhanQua7.itemOptions.add(new ItemOption(107, 3));

                                Item PhanQua8 = ItemService.gI().createNewItem(((short) 23));
                                PhanQua8.itemOptions.add(new ItemOption(0, 5));
                                PhanQua8.itemOptions.add(new ItemOption(107, 3));

                                Item PhanQua9 = ItemService.gI().createNewItem(((short) 29));
                                PhanQua9.itemOptions.add(new ItemOption(7, 5));
                                PhanQua9.itemOptions.add(new ItemOption(28, 3));
                                PhanQua9.itemOptions.add(new ItemOption(107, 3));

                                Item PhanQua10 = ItemService.gI().createNewItem(((short) 12));
                                PhanQua10.itemOptions.add(new ItemOption(14, 1));
                                PhanQua10.itemOptions.add(new ItemOption(107, 3));

                                Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua5.Name());
                                Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua7.Name());
                                Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua8.Name());
                                Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua9.Name());
                                Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua10.Name());
                                InventoryService.gI().addItemBag(pl, PhanQua5, 0);
                                InventoryService.gI().addItemBag(pl, PhanQua7, 0);
                                InventoryService.gI().addItemBag(pl, PhanQua8, 0);
                                InventoryService.gI().addItemBag(pl, PhanQua9, 0);
                                InventoryService.gI().addItemBag(pl, PhanQua10, 0);
                                InventoryService.gI().sendItemBags(pl);
                            }
                            InventoryService.gI().addItemBag(pl, PhanQua6, 0);
                            Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua6.Name());
                            InventoryService.gI().addItemBag(pl, PhanQua11, 0);
                            Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua11.Name());
                            InventoryService.gI().addItemBag(pl, PhanQua12, 0);
                            Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua12.Name());
                            InventoryService.gI().addItemBag(pl, PhanQua13, 0);
                            Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua13.Name());
                            InventoryService.gI().addItemBag(pl, PhanQua17, 0);
                            Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua17.Name());
                            Service.gI().sendThongBao(pl, "Bạn nhận được 2 Tỷ\nvàng");
                            Service.gI().sendThongBao(pl, "Bạn nhận được 1\nTriệu ngọc xanh");
                            InventoryService.gI().sendItemBags(pl);
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            Service.gI().sendMoney(pl);
                            break;
                        ////

                        case 570:
                            openWoodChest(pl, item);
                            break;
//                        case 1401:
//                            SendEffect.getInstance().send_danh_hieu(pl, 645, 1, 5, 5, 5,1);
//                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
//                            Service.gI().point(pl);
//                            Service.getInstance().sendThongBaoAllPlayer("|7|"
//                                    + "Người chơi: " + pl.name+ " Đã đạt top 1 máy chủ");
//                            break;
//                        case 1402:
//                            SendEffect.getInstance().send_danh_hieu(pl, 218, 1, 5, 5, 5,2);
//                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
//                            Service.gI().point(pl);
//                            break;
                        case 1307:
                            SendEffect.getInstance().send_danh_hieu(pl, 647, 1, 5, 5, 5, 3);
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            Service.gI().point(pl);
                            break;
                        case 648:
                            openboxsukien(pl, item, 3);
                            break;
                        case 1327:
                            noicomTanThu(pl, item);
                            break;
                        case 1328:
                            ruongHit(pl, item);

                            break;

                        case 1376:
                            hoplinhthu(pl, item);
                            break;
                        case 1377:
                            hoppet(pl, item);
                            break;
                        case 2100:
                        case 1446:
                            ruongvanbay(pl, item);
                            break;
                        case 1457:
                            changePetcell(pl, item);
                            break;
                        case 1557:
                            if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
                                EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 67, 2, 1, pl.location.x, pl.location.y, 5);
                                EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 67, 2, 1, pl.location.x - 24, pl.location.y, 5);
                                EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 67, 2, 1, pl.location.x - 48, pl.location.y, 5);
                                EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 67, 2, 1, pl.location.x, pl.location.y, 5);
                                EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 67, 2, 1, pl.location.x - 24, pl.location.y, 5);
                                EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 67, 2, 1, pl.location.x - 48, pl.location.y, 5);
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1); // trừ item khi sử dụng
                                int rdHN = 10;
                                pl.inventory.ruby += rdHN;
                                Service.getInstance().sendThongBao(pl, "vừa thả đèn trời và bạn được cộng " + rdHN + " hồng ngọc.");
                                Service.getInstance().sendMoney(pl);

                                pl.diem_skien++;
                                Service.getInstance().sendThongBao(pl, "Bạn đã nhận được 1 điểm sự kiện vì thả đèn trời");
                                ruongquaticknap(pl, item);
                            } else {
                                Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
                            }
                            break;
                        case 1579:
                            EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 64, 2, 1, pl.location.x, pl.location.y, 5);
                            EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 64, 2, 1, pl.location.x - 24, pl.location.y, 5);
                            EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 64, 2, 1, pl.location.x - 48, pl.location.y, 5);
                            EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 64, 2, 1, pl.location.x, pl.location.y, 5);
                            EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 64, 2, 1, pl.location.x - 24, pl.location.y, 5);
                            EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 64, 2, 1, pl.location.x - 48, pl.location.y, 5);
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);

                            int sodu1000 = 1_000_000;
                            pl.soDuVND += sodu1000;
                            PlayerDAO.subVndBarCong(pl, 1_000_000);
                            Service.getInstance().sendThongBao(pl, Util.mumberToLouis(sodu1000) + " Coin vào số dư của mình.");
                            Service.getInstance().sendMoney(pl);

                            break;
                        case 1580:
                            EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 64, 2, 1, pl.location.x, pl.location.y, 5);
                            EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 64, 2, 1, pl.location.x - 24, pl.location.y, 5);
                            EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 64, 2, 1, pl.location.x - 48, pl.location.y, 5);
                            EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 64, 2, 1, pl.location.x, pl.location.y, 5);
                            EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 64, 2, 1, pl.location.x - 24, pl.location.y, 5);
                            EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 64, 2, 1, pl.location.x - 48, pl.location.y, 5);
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            int sodu800 = 800_000;
                            pl.soDuVND += sodu800;
                            PlayerDAO.subVndBarCong(pl, 800_000);
                            Service.getInstance().sendThongBao(pl, Util.mumberToLouis(sodu800) + " Coin vào số dư của mình.");
                            Service.getInstance().sendMoney(pl);

                            break;
                        case 1581:
                            EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 64, 2, 1, pl.location.x, pl.location.y, 5);
                            EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 64, 2, 1, pl.location.x - 24, pl.location.y, 5);
                            EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 64, 2, 1, pl.location.x - 48, pl.location.y, 5);
                            EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 64, 2, 1, pl.location.x, pl.location.y, 5);
                            EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 64, 2, 1, pl.location.x - 24, pl.location.y, 5);
                            EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 64, 2, 1, pl.location.x - 48, pl.location.y, 5);
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);

                            int sodu500 = 500_000;
                            pl.soDuVND += sodu500;
                            PlayerDAO.subVndBarCong(pl, 500_000);
                            Service.getInstance().sendThongBao(pl, Util.mumberToLouis(sodu500) + " Coin vào số dư của mình.");
                            Service.getInstance().sendMoney(pl);

                            break;
                        case 1582:
                            EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 64, 2, 1, pl.location.x, pl.location.y, 5);
                            EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 64, 2, 1, pl.location.x - 24, pl.location.y, 5);
                            EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 64, 2, 1, pl.location.x - 48, pl.location.y, 5);
                            EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 64, 2, 1, pl.location.x, pl.location.y, 5);
                            EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 64, 2, 1, pl.location.x - 24, pl.location.y, 5);
                            EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 64, 2, 1, pl.location.x - 48, pl.location.y, 5);
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);

                            int sodu300 = 300_000;
                            pl.soDuVND += sodu300;
                            PlayerDAO.subVndBarCong(pl, 300_000);
                            Service.getInstance().sendThongBao(pl, Util.mumberToLouis(sodu300) + " Coin vào số dư của mình.");
                            Service.getInstance().sendMoney(pl);

                            break;
                        case 1583:
                            EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 64, 2, 1, pl.location.x, pl.location.y, 5);
                            EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 64, 2, 1, pl.location.x - 24, pl.location.y, 5);
                            EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 64, 2, 1, pl.location.x - 48, pl.location.y, 5);
                            EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 64, 2, 1, pl.location.x, pl.location.y, 5);
                            EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 64, 2, 1, pl.location.x - 24, pl.location.y, 5);
                            EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 64, 2, 1, pl.location.x - 48, pl.location.y, 5);
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);

                            int sodu200 = 200_000;
                            pl.soDuVND += sodu200;
                            PlayerDAO.subVndBarCong(pl, 200_000);
                            Service.getInstance().sendThongBao(pl, Util.mumberToLouis(sodu200) + " Coin vào số dư của mình.");
                            Service.getInstance().sendMoney(pl);

                            break;
                        case 1584:
                            EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 64, 2, 1, pl.location.x, pl.location.y, 5);
                            EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 64, 2, 1, pl.location.x - 24, pl.location.y, 5);
                            EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 64, 2, 1, pl.location.x - 48, pl.location.y, 5);
                            EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 64, 2, 1, pl.location.x, pl.location.y, 5);
                            EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 64, 2, 1, pl.location.x - 24, pl.location.y, 5);
                            EffectMapService.gI().sendEffectMapToAllInMap(pl.zone, 64, 2, 1, pl.location.x - 48, pl.location.y, 5);
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);

                            int sodu100 = 100_000;
                            pl.soDuVND += sodu100;
                            PlayerDAO.subVndBarCong(pl, 100_000);
                            Service.getInstance().sendThongBao(pl, Util.mumberToLouis(sodu100) + " Coin vào số dư của mình.");
                            Service.getInstance().sendMoney(pl);

                            break;
                        case 568:
                            UseItem.gI().openChonHTDeTu(pl, item);
                            break;
                        case 1458:
                            UseItem.gI().openChonHTDeTuBill(pl, item);
                            break;
                        case 1459:
                            openMabuNhi(pl, item);
                            break;
                        case 1401:
                            TrungLinhThu(pl, item);
                            break;
                        case 1378:
                            hopcaitrang(pl, item);
                            break;
                        case 1389:
                            hopsukien(pl, item);
                            break;
                        case 1451:
                            thoigianglt(pl, item);
                            break;
                        case 992:
                            ChangeMapService.gI().goToPrimaryForest(pl);
                            break;
                        case 1127:
                            if (ZenoWar21h.gI().isTimeZenoWar()) {
                                Item item1127 = InventoryService.gI().findItemBag(pl, 1127);
                                if (item1127 != null && item1127.quantity >= 1) {
                                    // Trừ item
                                    InventoryService.gI().subQuantityItemsBag(pl, item1127, 1);
                                    ChangeMapService.gI().goToZenoLand(pl);
                                } else {
                                    Service.getInstance().sendThongBao(pl, "Bạn không có Vé Vùng Đất Zeno");
                                }

                            } else {
                                Service.getInstance().sendThongBao(pl, "Chỉ có thể vào Vùng Đất Zeno từ 21h đến 23h");
                            }
                            break;
                        case 2023:
                            if (!pl.getSession().actived) {
                                Service.getInstance().sendThongBao(pl, "Vui lòng kích hoạt tài khoản để có thể sử dụng");
                                return;
                            }
                            Input.gI().createFormTangRuby(pl);
                            break;
                        case 2006: //phiếu cải trang hải tặc
                        case 2007: //phiếu cải trang hải tặc
                        case 2008: //phiếu cải trang hải tặc
                            openPhieuCaiTrangHaiTac(pl, item);
                            break;
                        case 2012: //Hop Qua Kich Hoat
                            openboxsukien(pl, item, 1);
                            break;
                        case 2020: //phiếu cải trang 20/10
                            openbox2010(pl, item);
                            break;
                        case 1449: //cskb
                            maydoboss(pl);
                            Item item1449 = InventoryService.gI().findItemBag(pl, 1449);
                            InventoryService.gI().subQuantityItemsBag(pl, item1449, 1);
                            break;
                        case 2021:
                            openboxsukien(pl, item, 2);
                            break;
                        case 211: //nho tím
                        case 212: //nho xanh
                            eatGrapes(pl, item);
                            break;
                        case 380: //cskb
                            openCSKB(pl, item);
                            break;
                        case 381: //cuồng nộ
                        case 382: //bổ huyết
                        case 383: //bổ khí
                        case 384: //giáp xên
                        case 385: //ẩn danh
                        case 379: //máy dò                        
                        case 663: //bánh pudding
                        case 664: //xúc xíc
                        case 665: //kem dâu
                        case 666: //mì ly
                        case 667: //sushi
                        case ConstItem.BANH_CHUNG_CHIN:
                        case ConstItem.BANH_TET_CHIN:
                        case ConstItem.CUONG_NO_2:
                        case ConstItem.BO_HUYET_2:
                        case ConstItem.GIAP_XEN_BO_HUNG_2:
                        case ConstItem.BO_KHI_2:
                        case ConstItem.BINH_CHUA_COMMESON:
                        case 1326:
                        case 1416:
                        case 1518:
                        case 1420:
                        case 1128: //nhẫn ma pháp
                        case 1129: //nhẫn linh giới
                        case 2082: //SMTA
                            useItemTime(pl, item);
                            break;
                        case 521: //tdlt
                            useTDLT(pl, item);
                            break;
                        case 454: //bông tai
                            usePorata(pl);
                            break;
                        case 921: //bông tai 2
                            usePorata2(pl);
                            break;
                        case 1015: //bông tai vô cực
                            usePorataVoCuc(pl);
                            break;
                        case 1280: //hôp thần linh thường
                            NpcService.gI().createMenuConMeo(pl, ConstNpc.HOP_QUA_THAN_LINH, 9840,
                                    "Đây là hộp quà đua top open.",
                                    "Trái đất", "Namek", "Xayda");
                            break;
                        case 193: //gói 10 viên capsule
                            if (pl.iDMark.getTranhNgoc() == 1 || pl.iDMark.getTranhNgoc() == 2) {
                                Service.gI().sendThongBao(pl, "Bạn đang đăng ký Tranh Ngọc Rồng nên không thể thực hiện!");
                                return;
                            }
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                        case 194: //capsule đặc biệt
                            if (pl.iDMark.getTranhNgoc() == 1 || pl.iDMark.getTranhNgoc() == 2) {
                                Service.gI().sendThongBao(pl, "Bạn đang đăng ký Tranh Ngọc Rồng nên không thể thực hiện!");
                                return;
                            }
                            openCapsuleUI(pl);
                            break;

                        case 1504:
                            Input.gI().createFormChangeName(pl);
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            InventoryService.gI().sendItemBags(pl);
                            break;
                        case 401: //đổi đệ tử
                            changePet(pl, item);
                            break;
                        case 402: //sách nâng chiêu 1 đệ tử
                        case 403: //sách nâng chiêu 2 đệ tử
                        case 404: //sách nâng chiêu 3 đệ tử
                        case 759: //sách nâng chiêu 4 đệ tử
                        case 1324:
                            upSkillPet(pl, item);
                            break;
                        case 1417:
                        case 1418:
                        case 1419:
                            this.useItemThaySkill(pl, item);
                            break;
                        case ConstItem.CAPSULE_TET_2022:
                            openCapsuleTet2022(pl, item);
                            break;

                        default:
                            switch (item.template.type) {
                                case 7: //sách học, nâng skill
                                    learnSkill(pl, item);
                                    break;
                                case 12: //ngọc rồng các loại
//                                Service.getInstance().sendThongBaoOK(pl, "Bảo trì tính năng.");
                                    controllerCallRongThan(pl, item);
                                    break;
                                case 11: //item flag bag
                                    useItemChangeFlagBag(pl, item);
                                    break;
                            }
                    }
            }
            InventoryService.gI().sendItemBags(pl);
        } else {
            Service.getInstance().sendThongBaoOK(pl, "Sức mạnh không đủ yêu cầu");
        }
    }

    private void findNamekBall(Player pl, Item item) {
        List<Integer> usedMaps = new ArrayList<>(); // Danh sách các Map đã sử dụng
        int randomtoado = 0;

        List<NamekBall> balls = NamekBallManager.gI().getList();
        StringBuffer sb = new StringBuffer();
        for (NamekBall namekBall : balls) {
            Map m = namekBall.zone.map;
            int z = namekBall.zone.zoneId;
            if (pl.zone.map.mapId == m.mapId) {
                if (pl.zone.zoneId == z) {
                    sb.append(namekBall.getIndex() + 1).append(" Sao: " + "(" + Util.nextInt(80, 200) + "m) ").append(namekBall.getHolderName() == null ? "" : namekBall.getHolderName()).append("đây" + "\n");
                } else {
                    sb.append(namekBall.getIndex() + 1).append(" Sao: " + "(" + Util.nextInt(80, 200) + "m) ").append(namekBall.getHolderName() == null ? "" : namekBall.getHolderName()).append("đây kv " + z + "\n");
                }
            } else {
                sb.append(namekBall.getIndex() + 1).append(" Sao: " + "(" + Util.nextInt(80, 200) + "m) ").append(m.mapName).append(namekBall.getHolderName() == null ? "" : namekBall.getHolderName()).append("\n");
            }
        }
        final int star = Util.nextInt(0, 6);
        final NamekBall ball = NamekBallManager.gI().findByIndex(star);
        final Inventory inventory = pl.inventory;
        MenuDialog menu = new MenuDialog(sb.toString(), new String[]{"Ok", "Đóng"}, new MenuRunable() {
            @Override
            public void run() {
                switch (getIndexSelected()) {
                    case 0:
                        break;
                    case 1:
                        break;
                }
                if (pl.isHoldNamecBall) {
                    NamekBallWar.gI().dropBall(pl);
                }
            }
        });
        menu.show(pl);
        InventoryService.gI().sendItemBags(pl);
        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
    }

    private int getRandomItem6() {
        int[] weights = {18, 18, 18, 18, 18, 10};
        int totalWeight = 100;
        int random = Util.nextInt(1, totalWeight + 1);
        int sum = 0;

        for (int i = 0; i < weights.length; i++) {
            sum += weights[i];
            if (random <= sum) {
                return i + 1;
            }
        }
        return 1; // Mặc định trả về case 1 nếu có lỗi xảy ra
    }

    private void ruongquaticknap(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            int randomItem = getRandomItem6();

            // Tạo item tương ứng với số ngẫu nhiên đã chọn
            switch (randomItem) {
                case 1:
                    //Ct kirin lân
                    Item gokuVoCuc = ItemService.gI().createNewItem((short) 1549, 1);
                    gokuVoCuc.itemOptions.add(new ItemOption(50, Util.nextInt(15, 25)));
                    gokuVoCuc.itemOptions.add(new ItemOption(77, Util.nextInt(15, 25)));
                    gokuVoCuc.itemOptions.add(new ItemOption(103, Util.nextInt(15, 25)));

                    if (Util.nextInt(1, 10000) <= 9999) { // Tỷ lệ thêm là 99,99%
                        gokuVoCuc.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                    }
                    InventoryService.gI().addItemBag(pl, gokuVoCuc, 1); // 1 là số ngày hạn sử dụng
                    Service.getInstance().sendThongBao(pl, "Bạn vừa nhận được " + gokuVoCuc.template.name);
                    break;
                case 2:
                    // 4sao
                    Item nro4sao = ItemService.gI().createNewItem((short) 17);
                    int quantity4sao = 1;
                    nro4sao.quantity = quantity4sao;
                    InventoryService.gI().addItemBag(pl, nro4sao, 9999); // Không đặt hạn sử dụng
                    Service.getInstance().sendThongBao(pl, "Bạn nhận được  x" + quantity4sao + " " + nro4sao.template.name);
                    break;

                case 3:
                    // Pet Thỏ ngọc
                    Item vegetaBlue2 = ItemService.gI().createNewItem((short) 1551);
                    vegetaBlue2.itemOptions.add(new ItemOption(50, Util.nextInt(5, 10)));
                    vegetaBlue2.itemOptions.add(new ItemOption(77, Util.nextInt(5, 10)));
                    vegetaBlue2.itemOptions.add(new ItemOption(103, Util.nextInt(5, 10)));

                    if (Util.nextInt(1, 10000) <= 9999) { // Tỷ lệ thêm là 99,99%
                        vegetaBlue2.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                    }
                    InventoryService.gI().addItemBag(pl, vegetaBlue2, 1); // 1 là số ngày hạn sử dụng
                    Service.getInstance().sendThongBao(pl, "Bạn vừa nhận được " + vegetaBlue2.template.name);
                    break;
                case 4:
                    // Chụy Hằng
                    Item da1 = ItemService.gI().createNewItem((short) 1555, 1);
                    da1.itemOptions.add(new ItemOption(50, Util.nextInt(10, 20)));
                    da1.itemOptions.add(new ItemOption(77, Util.nextInt(10, 20)));
                    da1.itemOptions.add(new ItemOption(103, Util.nextInt(10, 20)));
                    da1.itemOptions.add(new ItemOption(101, 30));
                    if (Util.nextInt(1, 10000) <= 9999) { // Tỷ lệ thêm là 99,99%
                        da1.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                    }
                    InventoryService.gI().addItemBag(pl, da1, 1);

                    Service.getInstance().sendThongBao(pl, "Bạn vừa nhận được " + da1.template.name);

                    break;
                case 5:
                    // thỏi vàng
                    Item tv = ItemService.gI().createNewItem((short) 457);
                    int quantity3 = 1;
                    tv.quantity = quantity3;
                    InventoryService.gI().addItemBag(pl, tv, 9999);
                    Service.getInstance().sendThongBao(pl, "Bạn nhận được  x" + quantity3 + " " + tv.template.name);
                    break;
                case 6:
                    // vật phẩm lồng đèn
                    Item longden = ItemService.gI().createNewItem((short) 1556);
                    longden.itemOptions.add(new ItemOption(50, Util.nextInt(5, 10)));
                    longden.itemOptions.add(new ItemOption(77, Util.nextInt(5, 10)));
                    longden.itemOptions.add(new ItemOption(103, Util.nextInt(5, 10)));
                    longden.itemOptions.add(new ItemOption(5, Util.nextInt(5, 10)));
                    if (Util.nextInt(1, 10000) <= 9999) { // Tỷ lệ thêm là 99,99%
                        longden.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                    }
                    InventoryService.gI().addItemBag(pl, longden, 1);
                    Service.getInstance().sendThongBao(pl, "Bạn vừa nhận được " + longden.template.name);
                    break;

            }

            // Sau khi thêm item, cập nhật túi đồ của người chơi
//            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void capsuleThoiTrang(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) Util.nextInt(ConstItem.CAI_TRANG_GOKU_THOI_TRANG, ConstItem.CAI_TRANG_CA_DIC_THOI_TRANG));
            it.itemOptions.add(new ItemOption(50, 30));
            it.itemOptions.add(new ItemOption(77, 30));
            it.itemOptions.add(new ItemOption(103, 30));
            it.itemOptions.add(new ItemOption(106, 0));
            InventoryService.gI().addItemBag(pl, it, 0);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            short icon1 = item.template.iconID;
            short icon2 = it.template.iconID;
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon1, icon2);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }

    }

    private void openCapsuleTet2022(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) == 0) {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
            return;
        }
        RandomCollection<Integer> rdItemID = new RandomCollection<>();
        rdItemID.add(1, ConstItem.PHAO_HOA);
        rdItemID.add(1, ConstItem.CAY_TRUC);
        rdItemID.add(1, ConstItem.NON_HO_VANG);
        if (pl.gender == 0) {
            rdItemID.add(1, ConstItem.NON_TRAU_MAY_MAN);
            rdItemID.add(1, ConstItem.NON_CHUOT_MAY_MAN);
        } else if (pl.gender == 1) {
            rdItemID.add(1, ConstItem.NON_TRAU_MAY_MAN_847);
            rdItemID.add(1, ConstItem.NON_CHUOT_MAY_MAN_755);
        } else {
            rdItemID.add(1, ConstItem.NON_TRAU_MAY_MAN_848);
            rdItemID.add(1, ConstItem.NON_CHUOT_MAY_MAN_756);
        }
        rdItemID.add(1, ConstItem.CAI_TRANG_HO_VANG);
        rdItemID.add(1, ConstItem.HO_MAP_VANG);
//        rdItemID.add(2, ConstItem.SAO_PHA_LE);
//        rdItemID.add(2, ConstItem.SAO_PHA_LE_442);
//        rdItemID.add(2, ConstItem.SAO_PHA_LE_443);
//        rdItemID.add(2, ConstItem.SAO_PHA_LE_444);
//        rdItemID.add(2, ConstItem.SAO_PHA_LE_445);
//        rdItemID.add(2, ConstItem.SAO_PHA_LE_446);
//        rdItemID.add(2, ConstItem.SAO_PHA_LE_447);
        rdItemID.add(2, ConstItem.DA_LUC_BAO);
        rdItemID.add(2, ConstItem.DA_SAPHIA);
        rdItemID.add(2, ConstItem.DA_TITAN);
        rdItemID.add(2, ConstItem.DA_THACH_ANH_TIM);
        rdItemID.add(2, ConstItem.DA_RUBY);
        rdItemID.add(3, ConstItem.VANG_190);
        int itemID = rdItemID.next();
        Item newItem = ItemService.gI().createNewItem((short) itemID);
        if (newItem.template.type == 9) {
            newItem.quantity = Util.nextInt(10, 50) * 1000000;
        } else if (newItem.template.type == 14 || newItem.template.type == 30) {
            newItem.quantity = 10;
        } else {
            switch (itemID) {
                case ConstItem.CAY_TRUC: {
                    RandomCollection<ItemOption> rdOption = new RandomCollection<>();
                    rdOption.add(2, new ItemOption(77, 15));//%hp
                    rdOption.add(2, new ItemOption(103, 15));//%hp
                    rdOption.add(1, new ItemOption(50, 15));//%hp
                    newItem.itemOptions.add(rdOption.next());
                }
                break;

                case ConstItem.HO_MAP_VANG: {
                    newItem.itemOptions.add(new ItemOption(77, Util.nextInt(10, 20)));
                    newItem.itemOptions.add(new ItemOption(103, Util.nextInt(10, 20)));
                    newItem.itemOptions.add(new ItemOption(50, Util.nextInt(10, 20)));
                }
                break;

                case ConstItem.NON_HO_VANG:
                case ConstItem.CAI_TRANG_HO_VANG:
                case ConstItem.NON_TRAU_MAY_MAN:
                case ConstItem.NON_TRAU_MAY_MAN_847:
                case ConstItem.NON_TRAU_MAY_MAN_848:
                case ConstItem.NON_CHUOT_MAY_MAN:
                case ConstItem.NON_CHUOT_MAY_MAN_755:
                case ConstItem.NON_CHUOT_MAY_MAN_756:
                    newItem.itemOptions.add(new ItemOption(77, 30));
                    newItem.itemOptions.add(new ItemOption(103, 30));
                    newItem.itemOptions.add(new ItemOption(50, 30));
                    break;
            }
            RandomCollection<Integer> rdDay = new RandomCollection<>();
            rdDay.add(6, 3);
            rdDay.add(3, 7);
            rdDay.add(1, 15);
            int day = rdDay.next();
            newItem.itemOptions.add(new ItemOption(93, day));
        }
        short icon1 = item.template.iconID;
        short icon2 = newItem.template.iconID;
        if (newItem.template.type == 9) {
            Service.getInstance().sendThongBao(pl, "Bạn nhận được " + Util.numberToMoney(newItem.quantity) + " " + newItem.template.name);
        } else if (newItem.quantity == 1) {
            Service.getInstance().sendThongBao(pl, "Bạn nhận được " + newItem.template.name);
        } else {
            Service.getInstance().sendThongBao(pl, "Bạn nhận được x" + newItem.quantity + " " + newItem.template.name);
        }
        CombineServiceNew.gI().sendEffectOpenItem(pl, icon1, icon2);
        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
        InventoryService.gI().addItemBag(pl, newItem, 99);
        InventoryService.gI().sendItemBags(pl);
    }

    private int randClothes(int level) {
        return ConstItem.LIST_ITEM_CLOTHES[Util.nextInt(0, 2)][Util.nextInt(0, 4)][level - 1];
    }

    private void nanglvdetu(Player player, Item item) {
        if (player.lvpet > 6) {
            Service.getInstance().sendThongBao(player, "Đã đạt cấp tối đa");
            return;
        }
        if (player.pet.getLever() > 6) {
            Service.getInstance().sendThongBaoOK(player, "Viên đá chỉ giúp bạn nâng lên lever 5\nNếu bạn muốn nâng cấp nữa hãy bay đến\ngặp Tổ Sư Kaio để nâng cấp tiếp");
            return;
        }
        player.pet.setLever(player.pet.getLever() + 1);
        player.lvpet += 1;
        InventoryService.gI().sendItemBags(player);
        Service.getInstance().sendThongBao(player, "Chúc mừng bạn đã nâng cấp Thành Công");
        InventoryService.gI().subQuantityItemsBag(player, item, 1);
        Service.getInstance().player(player);
        player.zone.load_Me_To_Another(player);
        player.zone.loadAnotherToMe(player);
        Service.getInstance().point(player);
        Service.getInstance().Send_Caitrang(player);
    }

    private void openWoodChest(Player pl, Item item) {
        int time = (int) TimeUtil.diffDate(new Date(), new Date(item.createTime), TimeUtil.DAY);
        if (time != 0) {
            Item itemReward = null;
            int param = item.itemOptions.get(0).param;
            int gold = 0;
            int[] listItem = {441, 442, 443, 444, 445, 446, 447, 220, 221, 222, 223, 224, 225};
            int[] listClothesReward;
            int[] listItemReward;
            String text = "Bạn nhận được\n";
            if (param < 8) {
                gold = 100000 * param;
                listClothesReward = new int[]{randClothes(param)};
                listItemReward = Util.pickNRandInArr(listItem, 3);
            } else if (param < 10) {
                gold = 250000 * param;
                listClothesReward = new int[]{randClothes(param), randClothes(param)};
                listItemReward = Util.pickNRandInArr(listItem, 4);
            } else {
                gold = 500000 * param;
                listClothesReward = new int[]{randClothes(param), randClothes(param), randClothes(param)};
                listItemReward = Util.pickNRandInArr(listItem, 5);
                int ruby = Util.nextInt(1, 5);
                pl.inventory.ruby += ruby;
                pl.textRuongGo.add(text + "|1| " + ruby + " Hồng Ngọc");
            }
            for (var i : listClothesReward) {
                itemReward = ItemService.gI().createNewItem((short) i);
                RewardService.gI().initBaseOptionClothes(itemReward.template.id, itemReward.template.type, itemReward.itemOptions);
                RewardService.gI().initStarOption(itemReward, new RewardService.RatioStar[]{new RewardService.RatioStar((byte) 1, 1, 2), new RewardService.RatioStar((byte) 2, 1, 3), new RewardService.RatioStar((byte) 3, 1, 4), new RewardService.RatioStar((byte) 4, 1, 5),});
                InventoryService.gI().addItemBag(pl, itemReward, 0);
                pl.textRuongGo.add(text + itemReward.getInfoItem());
            }
            for (var i : listItemReward) {
                itemReward = ItemService.gI().createNewItem((short) i);
                RewardService.gI().initBaseOptionSaoPhaLe(itemReward);
                itemReward.quantity = Util.nextInt(1, 5);
                InventoryService.gI().addItemBag(pl, itemReward, 0);
                pl.textRuongGo.add(text + itemReward.getInfoItem());
            }
            if (param == 11) {
                itemReward = ItemService.gI().createNewItem((short) ConstItem.MANH_NHAN);
                itemReward.quantity = Util.nextInt(1, 3);
                InventoryService.gI().addItemBag(pl, itemReward, 0);
                pl.textRuongGo.add(text + itemReward.getInfoItem());
            }
            NpcService.gI().createMenuConMeo(pl, ConstNpc.RUONG_GO, -1, "Bạn nhận được\n|1|+" + Util.numberToMoney(gold) + " vàng", "OK [" + pl.textRuongGo.size() + "]");
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            pl.inventory.addGold(gold);
            InventoryService.gI().sendItemBags(pl);
            PlayerService.gI().sendInfoHpMpMoney(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Vì bạn quên không lấy chìa nên cần đợi 24h để bẻ khóa");
        }
    }

    private void useItemChangeFlagBag(Player player, Item item) {
        switch (item.template.id) {
            case 865: //kiem Z
                if (!player.effectFlagBag.useKiemz) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useKiemz = !player.effectFlagBag.useKiemz;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 994: //vỏ ốc
                break;
            case 995: //cây kem
                break;
            case 996: //cá heo
                break;
            case 997: //con diều
                break;
            case 998: //diều rồng
                if (!player.effectFlagBag.useDieuRong) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useDieuRong = !player.effectFlagBag.useDieuRong;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 999: //mèo mun
                if (!player.effectFlagBag.useMeoMun) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useMeoMun = !player.effectFlagBag.useMeoMun;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 1000: //xiên cá
                if (!player.effectFlagBag.useXienCa) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useXienCa = !player.effectFlagBag.useXienCa;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 1001: //phóng heo
                if (!player.effectFlagBag.usePhongHeo) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.usePhongHeo = !player.effectFlagBag.usePhongHeo;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 954:
                if (!player.effectFlagBag.useHoaVang) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useHoaVang = !player.effectFlagBag.useHoaVang;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 955:
                if (!player.effectFlagBag.useHoaHong) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useHoaHong = !player.effectFlagBag.useHoaHong;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 852:
                if (!player.effectFlagBag.useGayTre) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useGayTre = !player.effectFlagBag.useGayTre;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 1575:
                if (!player.effectFlagBag.useCanhacquythienthan) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useCanhacquythienthan = !player.effectFlagBag.useCanhacquythienthan;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
        }
        SendEffect.getInstance().removeTitle(player);
        Service.getInstance().point(player);
        Service.getInstance().sendFlagBag(player);
    }

    private void thoigianglt(Player pl, Item item) {
        if (pl.nPoint.wearingTrainArmor) {
            Item trainArmor = pl.inventory.trainArmor;
            if (trainArmor != null) {
                int trainArmorID = trainArmor.template.id;
                int maxTime = 0;
                switch (trainArmorID) {
                    case 529:
                    case 534:
                        maxTime = 100;
                        break;
                    case 530:
                    case 535:
                        maxTime = 1000;
                        break;
                    case 531:
                    case 536:
                        maxTime = 10000;
                        break;
                    case 1517:
                        maxTime = 20000;
                        break;
                }
                for (ItemOption io : trainArmor.itemOptions) {
                    if (io.optionTemplate.id == 9) {
                        if (io.param < maxTime) {
                            io.param += 10;
                            InventoryService.gI().sendItemBody(pl);
                        } else {
                            Service.getInstance().sendThongBao(pl, "Thời gian đã đạt giới hạn");
                        }
                        break;
                    }
                }
            }
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Chưa mặc giáp luyện tập");
        }
    }

    private void changePet(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender + 1;
            if (gender > 2) {
                gender = 0;
            }
            PetService.gI().changeNormalPet(player, gender);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }

    public void hoplinhthu(Player pl, Item it) {
        if (InventoryService.gI().getCountEmptyBag(pl) >= 1) {
            int[] lt = {1428, 1436, 2105, 2107, 1467, 1466, 1465, 1434, 1433, 1424};
            Item item = ItemService.gI().createNewItem((short) lt[Util.nextInt(0, lt.length - 1)]);
            item.itemOptions.add(new ItemOption(50, Util.nextInt(5, 8)));
            item.itemOptions.add(new ItemOption(77, Util.nextInt(5, 8)));
            item.itemOptions.add(new ItemOption(103, Util.nextInt(5, 8)));
            int date = Util.nextInt(1, 100);
            if (date <= 30) {
                item.itemOptions.add(new ItemOption(93, Util.nextInt(4, 7)));
            } else if (date <= 95) {
                item.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
            } else {

            }
            InventoryService.gI().addItemBag(pl, item, 0);
            InventoryService.gI().subQuantityItemsBag(pl, it, 1);
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().sendThongBao(pl, "Bạn vừa nhận được " + item.template.name);
        } else {
            Service.getInstance().sendThongBao(pl, "Cần tối thiểu 1 ô trống để mở");
        }
    }

    public void ruongvanbay(Player pl, Item it) {
        if (InventoryService.gI().getCountEmptyBag(pl) >= 1) {
            int[] pet = {1437, 1438, 1439, 1440, 1441, 1442, 1443, 1444, 1445};
            Item item = ItemService.gI().createNewItem((short) pet[Util.nextInt(0, pet.length - 1)]);
            item.itemOptions.add(new ItemOption(50, Util.nextInt(2, 6)));
            item.itemOptions.add(new ItemOption(77, Util.nextInt(2, 6)));
            item.itemOptions.add(new ItemOption(103, Util.nextInt(2, 6)));
            int date = Util.nextInt(1, 100);
            if (date <= 30) {
                item.itemOptions.add(new ItemOption(93, Util.nextInt(4, 7)));
            } else if (date <= 95) {
                item.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
            } else {

            }
            InventoryService.gI().addItemBag(pl, item, 0);
            InventoryService.gI().subQuantityItemsBag(pl, it, 1);
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().sendThongBao(pl, "Bạn vừa nhận được " + item.template.name);
        } else {
            Service.getInstance().sendThongBao(pl, "Cần tối thiểu 1 ô trống để mở");
        }
    }

    public void ruongHit(Player pl, Item it) {
        if (InventoryService.gI().getCountEmptyBag(pl) >= 1) {
            Item item = ItemService.gI().createNewItem((short) 884);

            int chance = Util.nextInt(1, 100); // Random từ 1 đến 100
            int value;
            if (chance <= 10) {
                value = Util.nextInt(81, 100);
            } else if (chance <= 30) {
                value = Util.nextInt(51, 80);
            } else {
                value = Util.nextInt(40, 50);
            }

            item.itemOptions.add(new ItemOption(50, Util.nextInt(5, 15)));
            item.itemOptions.add(new ItemOption(5, value));
            item.itemOptions.add(new ItemOption(14, Util.nextInt(5, 10)));
            int date = Util.nextInt(1, 100);
            if (date <= 30) {
                item.itemOptions.add(new ItemOption(93, Util.nextInt(4, 7)));
            } else if (date <= 90) {
                item.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
            } else {

            }
            InventoryService.gI().addItemBag(pl, item, 0);
            InventoryService.gI().subQuantityItemsBag(pl, it, 1);
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().sendThongBao(pl, "Bạn vừa nhận được " + item.template.name);
        } else {
            Service.getInstance().sendThongBao(pl, "Cần tối thiểu 1 ô trống để mở");
        }
    }

    public void hoppet(Player pl, Item it) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 1) {
            int[] pet = {1363, 1364, 1365, 1366, 1367, 1368, 1369, 1370, 1371, 1372, 1373, 1374, 1375, 1359, 1360, 1361};
            Item item = ItemService.gI().createNewItem((short) pet[Util.nextInt(0, pet.length - 1)]);
            item.itemOptions.add(new ItemOption(50, Util.nextInt(3, 10)));
            item.itemOptions.add(new ItemOption(77, Util.nextInt(3, 10)));
            item.itemOptions.add(new ItemOption(103, Util.nextInt(3, 10)));
            int date = Util.nextInt(1, 100);
            if (date <= 30) {
                item.itemOptions.add(new ItemOption(93, Util.nextInt(4, 7)));
            } else if (date <= 95) {
                item.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
            } else {

            }
            InventoryService.gI().addItemBag(pl, item, 0);
            InventoryService.gI().subQuantityItemsBag(pl, it, 1);
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().sendThongBao(pl, "Bạn vừa nhận được " + item.template.name);
        } else {
            Service.getInstance().sendThongBao(pl, "Cần tối thiểu 1 ô trống để mở");
        }
    }

    public void hopsukien(Player pl, Item it) {
        if (InventoryService.gI().getCountEmptyBag(pl) >= 1) {
            int[] ct = {1397, 1398, 1392, 1393, 2103, 2104, 2115, 2117, 2119, 2120, 2123, 2124, 2125, 1575};
            Item item = ItemService.gI().createNewItem((short) ct[Util.nextInt(0, ct.length - 1)]);
            item.itemOptions.add(new ItemOption(50, Util.nextInt(1, 5)));
            item.itemOptions.add(new ItemOption(77, Util.nextInt(1, 5)));
            item.itemOptions.add(new ItemOption(103, Util.nextInt(1, 5)));
            int date = Util.nextInt(1, 100);
            if (date <= 30) {
                item.itemOptions.add(new ItemOption(93, Util.nextInt(4, 7)));
            } else if (date <= 95) {
                item.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
            } else {

            }
            InventoryService.gI().addItemBag(pl, item, 0);
            InventoryService.gI().subQuantityItemsBag(pl, it, 1);
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().sendThongBao(pl, "Bạn vừa nhận được " + item.template.name);
        } else {
            Service.getInstance().sendThongBao(pl, "Cần tối thiểu 1 ô trống để mở");
        }
    }

    public void hopcaitrang(Player pl, Item it) {
        if (InventoryService.gI().getCountEmptyBag(pl) >= 1) {
            // Danh sách cải trang ban đầu
            int[] allCT = {1339, 2098, 2099, 1521, 2094, 2089, 1348};
            List<Integer> ctList = new ArrayList<>();

            // Lọc theo loại nhân vật
            for (int id : allCT) {
                if (pl.gender == 1 && (id == 2098 || id == 2099)) {
                    continue; // loại 1 bỏ 2098, 2099
                }
                if ((pl.gender == 0 || pl.gender == 2) && id == 1339) {
                    continue; // loại 0, 2 bỏ 1339
                }
                ctList.add(id);
            }

            // Random cải trang
            Item item = ItemService.gI().createNewItem(
                    (short) (int) ctList.get(Util.nextInt(0, ctList.size() - 1))
            );

            int chance = Util.nextInt(1, 100); // Random từ 1 đến 100
            if (item.template.id != 1348) {
                if (chance <= 20) {
                    item.itemOptions.add(new ItemOption(50, Util.nextInt(28, 35)));
                    item.itemOptions.add(new ItemOption(77, Util.nextInt(28, 35)));
                    item.itemOptions.add(new ItemOption(103, Util.nextInt(28, 35)));
                } else {
                    item.itemOptions.add(new ItemOption(50, Util.nextInt(15, 27)));
                    item.itemOptions.add(new ItemOption(77, Util.nextInt(15, 27)));
                    item.itemOptions.add(new ItemOption(103, Util.nextInt(15, 27)));
                }
            } else {
                if (chance <= 20) {
                    item.itemOptions.add(new ItemOption(50, Util.nextInt(28, 35)));
                    item.itemOptions.add(new ItemOption(77, Util.nextInt(28, 35)));
                    item.itemOptions.add(new ItemOption(103, Util.nextInt(28, 35)));
                    item.itemOptions.add(new ItemOption(101, Util.nextInt(21, 25)));

                } else {
                    item.itemOptions.add(new ItemOption(50, Util.nextInt(15, 27)));
                    item.itemOptions.add(new ItemOption(77, Util.nextInt(15, 27)));
                    item.itemOptions.add(new ItemOption(103, Util.nextInt(15, 27)));
                    item.itemOptions.add(new ItemOption(101, Util.nextInt(10, 20)));
                }
            }

            int date = Util.nextInt(1, 100);
            if (date <= 30) {
                item.itemOptions.add(new ItemOption(93, Util.nextInt(4, 7)));
            } else if (date <= 95) {
                item.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
            } else {

            }

            InventoryService.gI().addItemBag(pl, item, 0);
            InventoryService.gI().subQuantityItemsBag(pl, it, 1);
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().sendThongBao(pl, "Bạn vừa nhận được " + item.template.name);
        } else {
            Service.getInstance().sendThongBao(pl, "Cần tối thiểu 1 ô trống để mở");
        }
    }

    public void noicomTanThu(Player pl, Item it) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 14) {
            int gender = pl.gender;
            int[] id = {gender, 6 + gender, 21 + gender, 27 + gender, 12, 381, 382, 383, 384, 194, 454};
            int[] soluong = {1, 1, 1, 1, 1, 40, 40, 40, 40, 1, 1};
            int[] option = {73, 73, 73, 73, 73, 73, 73, 73, 73, 73, 73};
            int[] param = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            int arrLength = id.length - 1;

            for (int i = 0; i < arrLength; i++) {
                if (i < 5) {
                    Item item = ItemService.gI().createNewItem((short) id[i]);
                    RewardService.gI().initBaseOptionClothes(item.template.id, item.template.type, item.itemOptions);
                    item.itemOptions.add(new ItemOption(248, 0));
                    item.itemOptions.add(new ItemOption(249, 0));
                    item.itemOptions.add(new ItemOption(30, 0));
                    InventoryService.gI().addItemBag(pl, item, 0);
                } else {
                    Item item = ItemService.gI().createNewItem((short) id[i]);
                    item.quantity = soluong[i];
                    item.itemOptions.add(new ItemOption(option[i], param[i]));
                    InventoryService.gI().addItemBag(pl, item, 0);
                }
            }

            int[] idpet = {884, 1348, 2128};
            Item item = ItemService.gI().createNewItem((short) idpet[Util.nextInt(0, idpet.length - 1)]);
            item.itemOptions.add(new ItemOption(50, 25));
            item.itemOptions.add(new ItemOption(77, 25));
            item.itemOptions.add(new ItemOption(103, 25));
            InventoryService.gI().addItemBag(pl, item, 0);

            InventoryService.gI().subQuantityItemsBag(pl, it, 1);
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().sendThongBao(pl, "Chúc bạn chơi game vui vẻ");
        } else {
            Service.getInstance().sendThongBao(pl, "Cần tối thiểu 14 ô trống để nhận thưởng");
        }
    }

    private void openbox2010(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {17, 16, 15, 675, 676, 677, 678, 679, 680, 681, 580, 581, 582};
            int[][] gold = {{5000, 20000}};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;

            Item it = ItemService.gI().createNewItem(temp[index]);

            if (temp[index] >= 15 && temp[index] <= 17) {
                it.itemOptions.add(new ItemOption(73, 0));

            } else if (temp[index] >= 580 && temp[index] <= 582 || temp[index] >= 675 && temp[index] <= 681) { // cải trang

                it.itemOptions.add(new ItemOption(77, Util.nextInt(20, 30)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(20, 30)));
                it.itemOptions.add(new ItemOption(50, Util.nextInt(20, 30)));
                it.itemOptions.add(new ItemOption(95, Util.nextInt(5, 15)));
                it.itemOptions.add(new ItemOption(96, Util.nextInt(5, 15)));

                if (Util.isTrue(1, 200)) {
                    it.itemOptions.add(new ItemOption(74, 0));
                } else {
                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                }

            } else {
                it.itemOptions.add(new ItemOption(73, 0));
            }
            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;

            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);

            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void capsule8thang3(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {17, 16, 15, 675, 676, 677, 678, 679, 680, 681, 580, 581, 582, 1154, 1155, 1156, 860, 1041, 1042, 1043, 1103, 1104, 1105, 1106, 954, 955};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;

            Item it = ItemService.gI().createNewItem(temp[index]);

            if (Util.isTrue(30, 100)) {
                int ruby = Util.nextInt(1, 5);
                pl.inventory.ruby += ruby;
                CombineServiceNew.gI().sendEffectOpenItem(pl, item.template.iconID, (short) 7743);
                PlayerService.gI().sendInfoHpMpMoney(pl);
                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                InventoryService.gI().sendItemBags(pl);
                Service.getInstance().sendThongBao(pl, "Bạn nhận được " + ruby + " Hồng Ngọc");
                return;
            }
            if (it.template.type == 5) { // cải trang

                it.itemOptions.add(new ItemOption(50, Util.nextInt(20, 35)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(20, 35)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(20, 35)));
                it.itemOptions.add(new ItemOption(117, Util.nextInt(10, 20)));

            } else if (it.template.id == 954 || it.template.id == 955) {
                it.itemOptions.add(new ItemOption(50, Util.nextInt(10, 20)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(10, 20)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(10, 20)));
            }

            if (it.template.type == 5 || it.template.id == 954 || it.template.id == 955) {
                if (Util.isTrue(1, 200)) {
                    it.itemOptions.add(new ItemOption(74, 0));
                } else {
                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                }
            }
            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;

            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);

            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private boolean TrungLinhThu(Player pl, Item item) {
        Item hlt = InventoryService.gI().findItemBag(pl, 1402);
        if (hlt != null && hlt.quantity >= 200) {
            if (pl.LinhThuEgg == null) {
                TrungLinhThu.createLinhThuEgg(pl);
                Service.getInstance().sendThongBaoOK(pl, "Bạn vừa ấp trứng ở nhà");
                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                InventoryService.gI().subQuantityItemsBag(pl, hlt, 200);
            }
            return true;
        } else {
            Service.getInstance().sendThongBaoOK(pl, "Bạn không đủ 200 đá thời ");
        }
        return false;
    }

    public void openMabu(Player pl, int gender) {
        Item trungBu = InventoryService.gI().findItem(pl.inventory.itemsBag, 568);
        if (pl.pet == null) {
            Service.getInstance().sendThongBao(pl, "Yêu cầu bạn phải có đệ tử");
            return;
        }
        // Kiểm tra hành trang đệ có đồ không
        for (Item i : pl.pet.inventory.itemsBody) {
            if (i != null && i.template != null && i.template.id != -1) {
                Service.gI().sendThongBao(pl, "Vui lòng tháo hết trang bị của đệ tử trước khi đổi!");
                return;
            }
        }
//        if (pl.pet.typePet != ConstPet.WHIS) { 
//            Service.getInstance().sendThongBao(pl, "Yêu cầu bạn phải có đệ tử Cell nhí");
//            return;
//        }
//        if (pl.pet.nPoint.power < 60_000_000_000L) {
//            Service.getInstance().sendThongBao(pl, "Yêu cầu bạn phải 60 tỷ sức mạnh");
//            return;
//        }
        byte limitPower = pl.pet.nPoint.limitPower;
        if (pl.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            pl.pet.unFusion();
        }
        pl.pet.dispose();
        MapService.gI().exitMap(pl.pet);
        pl.pet = null;
        PetService.gI().createMabuPet(pl, gender, limitPower);
        InventoryService.gI().subQuantityItemsBag(pl, trungBu, 1);
        InventoryService.gI().sendItemBags(pl);
    }

    private void openMabuNhi(Player pl, Item item) {
//        if (pl.pet == null) {
//            Service.getInstance().sendThongBao(pl, "Yêu cầu bạn phải có đệ tử Cell nhí");
//            return;
//        }
//        if (pl.pet.typePet != ConstPet.WHIS) {
//            Service.getInstance().sendThongBao(pl, "Yêu cầu bạn phải có đệ tử Cell nhí");
//            return;
//        }
//        if (pl.pet.nPoint.power < 100_000_000_000L) {
//            Service.getInstance().sendThongBao(pl, "Yêu cầu bạn phải có đệ tử Cell nhí và 100 tỷ sức mạnh");
//            return;
//        }
        if (pl.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            pl.pet.unFusion();
        }
        pl.pet.dispose();
        MapService.gI().exitMap(pl.pet);
        pl.pet = null;
        PetService.gI().createMabuNhi(pl, pl.gender);
        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
    }

    public void openbillcon(Player player, int gender) {
        Item trungBill = InventoryService.gI().findItem(player.inventory.itemsBag, 1458);
        if (player.pet == null || (player.pet.typePet != ConstPet.MABU && player.pet.typePet != ConstPet.BILL_CON)) {
            Service.getInstance().sendThongBao(player, "Yêu cầu bạn phải có đệ tử Bư");
            return;
        }
        // Kiểm tra hành trang đệ có đồ không
        for (Item i : player.pet.inventory.itemsBody) {
            if (i != null && i.template != null && i.template.id != -1) {
                Service.gI().sendThongBao(player, "Vui lòng tháo hết trang bị của đệ tử trước khi đổi!");
                return;
            }
        }
        if (player.pet.typePet != ConstPet.BILL_CON && player.pet.nPoint.power < 60_000_000_000L) {
            Service.getInstance().sendThongBao(player, "Yêu cầu đệ tử bạn phải 60 tỷ sức mạnh");
            return;
        }
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        player.pet.dispose();
        MapService.gI().exitMap(player.pet);
        player.pet = null;
        PetService.gI().createBILLCON(player, gender, limitPower);
        InventoryService.gI().subQuantityItemsBag(player, trungBill, 1);
        InventoryService.gI().sendItemBags(player);
    }

    private void changePetcell(Player player, Item item) {
//       if (player.pet.nPoint.power <= 80_000_000_000l) {
//               Service.getInstance().sendThongBao(player, "Cần đệ trên 80 tỷ");
//                   return;
//       }
        if (player.pet == null) {
            PetService.gI().createWhisPet(player, player.gender);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
            return;
        }
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        PetService.gI().createWhisPet(player, player.gender);
        InventoryService.gI().subQuantityItemsBag(player, item, 1);
    }

    public boolean maydoboss(Player pl) {
        try {
            BossManager.gI().showListBossMember(pl);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public void openboxsukien(Player pl, Item item, int idsukien) {
        try {
            switch (idsukien) {
                case 1:
                    if (Manager.EVENT_SEVER == idsukien) {
                        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
                            short[] temp = {16, 15, 865, 999, 1000, 1001, 739, 742, 743};
                            int[][] gold = {{5000, 20000}};
                            byte index = (byte) Util.nextInt(0, temp.length - 1);
                            short[] icon = new short[2];
                            icon[0] = item.template.iconID;

                            Item it = ItemService.gI().createNewItem(temp[index]);

                            if (temp[index] >= 15 && temp[index] <= 16) {
                                it.itemOptions.add(new ItemOption(73, 0));

                            } else if (temp[index] == 865) {

                                it.itemOptions.add(new ItemOption(30, 0));

                                if (Util.isTrue(1, 30)) {
                                    it.itemOptions.add(new ItemOption(93, 365));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 999) { // mèo mun
                                it.itemOptions.add(new ItemOption(77, 15));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(1, 50)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 1000) { // xiên cá
                                it.itemOptions.add(new ItemOption(103, 15));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(1, 50)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 1001) { // Phóng heo
                                it.itemOptions.add(new ItemOption(50, 15));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(1, 50)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }

                            } else if (temp[index] == 739) { // cải trang Billes

                                it.itemOptions.add(new ItemOption(77, Util.nextInt(30, 40)));
                                it.itemOptions.add(new ItemOption(103, Util.nextInt(30, 40)));
                                it.itemOptions.add(new ItemOption(50, Util.nextInt(30, 45)));

                                if (Util.isTrue(1, 100)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }

                            } else if (temp[index] == 742) { // cải trang Caufila

                                it.itemOptions.add(new ItemOption(77, Util.nextInt(30, 40)));
                                it.itemOptions.add(new ItemOption(103, Util.nextInt(30, 40)));
                                it.itemOptions.add(new ItemOption(50, Util.nextInt(30, 45)));

                                if (Util.isTrue(1, 100)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 743) { // chổi bay
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(1, 50)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }

                            } else {
                                it.itemOptions.add(new ItemOption(73, 0));
                            }
                            InventoryService.gI().addItemBag(pl, it, 0);
                            icon[1] = it.template.iconID;

                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            InventoryService.gI().sendItemBags(pl);

                            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
                        } else {
                            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
                        }
                        break;
                    } else {
                        Service.getInstance().sendThongBao(pl, "Sự kiện đã kết thúc");
                    }
                case ConstEvent.SU_KIEN_20_11:
                    if (Manager.EVENT_SEVER == idsukien) {
                        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
                            short[] temp = {16, 15, 1039, 954, 955, 710, 711, 1040, 2023, 999, 1000, 1001};
                            byte index = (byte) Util.nextInt(0, temp.length - 1);
                            short[] icon = new short[2];
                            icon[0] = item.template.iconID;
                            Item it = ItemService.gI().createNewItem(temp[index]);
                            if (temp[index] >= 15 && temp[index] <= 16) {
                                it.itemOptions.add(new ItemOption(73, 0));
                            } else if (temp[index] == 1039) {
                                it.itemOptions.add(new ItemOption(50, 10));
                                it.itemOptions.add(new ItemOption(77, 10));
                                it.itemOptions.add(new ItemOption(103, 10));
                                it.itemOptions.add(new ItemOption(30, 0));
                                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                            } else if (temp[index] == 954) {
                                it.itemOptions.add(new ItemOption(50, 15));
                                it.itemOptions.add(new ItemOption(77, 15));
                                it.itemOptions.add(new ItemOption(103, 15));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(79, 80)) {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 955) {
                                it.itemOptions.add(new ItemOption(50, 20));
                                it.itemOptions.add(new ItemOption(77, 20));
                                it.itemOptions.add(new ItemOption(103, 20));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(79, 80)) {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 710) {//cải trang quy lão kame
                                it.itemOptions.add(new ItemOption(50, 22));
                                it.itemOptions.add(new ItemOption(77, 20));
                                it.itemOptions.add(new ItemOption(103, 20));
                                it.itemOptions.add(new ItemOption(194, 0));
                                it.itemOptions.add(new ItemOption(160, 35));
                                if (Util.isTrue(99, 100)) {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 711) { // cải trang jacky chun
                                it.itemOptions.add(new ItemOption(50, 23));
                                it.itemOptions.add(new ItemOption(77, 21));
                                it.itemOptions.add(new ItemOption(103, 21));
                                it.itemOptions.add(new ItemOption(195, 0));
                                it.itemOptions.add(new ItemOption(160, 50));
                                if (Util.isTrue(99, 100)) {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 1040) {
                                it.itemOptions.add(new ItemOption(50, 10));
                                it.itemOptions.add(new ItemOption(77, 10));
                                it.itemOptions.add(new ItemOption(103, 10));
                                it.itemOptions.add(new ItemOption(30, 0));
                                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                            } else if (temp[index] == 2023) {
                                it.itemOptions.add(new ItemOption(30, 0));
                            } else if (temp[index] == 999) { // mèo mun
                                it.itemOptions.add(new ItemOption(77, 15));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(1, 50)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 1000) { // xiên cá
                                it.itemOptions.add(new ItemOption(103, 15));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(1, 50)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 1001) { // Phóng heo
                                it.itemOptions.add(new ItemOption(50, 15));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(1, 50)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else {
                                it.itemOptions.add(new ItemOption(73, 0));
                            }
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            icon[1] = it.template.iconID;
                            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
                            InventoryService.gI().addItemBag(pl, it, 0);
                            int ruby = Util.nextInt(1, 5);
                            pl.inventory.ruby += ruby;
                            InventoryService.gI().sendItemBags(pl);
                            PlayerService.gI().sendInfoHpMpMoney(pl);
                            Service.getInstance().sendThongBao(pl, "Bạn được tặng kèm " + ruby + " Hồng Ngọc");
                        } else {
                            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
                        }
                    } else {
                        Service.getInstance().sendThongBao(pl, "Sự kiện đã kết thúc");
                    }
                    break;
                case ConstEvent.SU_KIEN_NOEL:
                    if (Manager.EVENT_SEVER == idsukien) {
                        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
                            int spl = Util.nextInt(441, 445);
                            int dnc = Util.nextInt(220, 224);
                            int nr = Util.nextInt(16, 18);
                            int nrBang = Util.nextInt(926, 931);

                            if (Util.isTrue(5, 90)) {
                                int ruby = Util.nextInt(1, 3);
                                pl.inventory.ruby += ruby;
                                CombineServiceNew.gI().sendEffectOpenItem(pl, item.template.iconID, (short) 7743);
                                PlayerService.gI().sendInfoHpMpMoney(pl);
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                InventoryService.gI().sendItemBags(pl);
                                Service.getInstance().sendThongBao(pl, "Bạn nhận được " + ruby + " Hồng Ngọc");
                            } else {
                                int[] temp = {spl, dnc, nr, nrBang, 387, 390, 393, 821, 822, 746, 380, 999, 1000, 1001, 936, 2022};
                                byte index = (byte) Util.nextInt(0, temp.length - 1);
                                short[] icon = new short[2];
                                icon[0] = item.template.iconID;
                                Item it = ItemService.gI().createNewItem((short) temp[index]);

                                if (temp[index] >= 441 && temp[index] <= 443) {// sao pha le
                                    it.itemOptions.add(new ItemOption(temp[index] - 346, 5));
                                    it.quantity = 10;
                                } else if (temp[index] >= 444 && temp[index] <= 445) {
                                    it.itemOptions.add(new ItemOption(temp[index] - 346, 3));
                                    it.quantity = 10;
                                } else if (temp[index] >= 220 && temp[index] <= 224) { // da nang cap
                                    it.quantity = 10;
                                } else if (temp[index] >= 387 && temp[index] <= 393) { // mu noel do
                                    it.itemOptions.add(new ItemOption(50, Util.nextInt(30, 40)));
                                    it.itemOptions.add(new ItemOption(77, Util.nextInt(30, 40)));
                                    it.itemOptions.add(new ItemOption(103, Util.nextInt(30, 40)));
                                    it.itemOptions.add(new ItemOption(80, Util.nextInt(10, 20)));
                                    it.itemOptions.add(new ItemOption(106, 0));
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                                    it.itemOptions.add(new ItemOption(199, 0));
                                } else if (temp[index] == 936) { // tuan loc
                                    it.itemOptions.add(new ItemOption(50, Util.nextInt(5, 10)));
                                    it.itemOptions.add(new ItemOption(77, Util.nextInt(5, 10)));
                                    it.itemOptions.add(new ItemOption(103, Util.nextInt(5, 10)));
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(3, 30)));
                                } else if (temp[index] == 822) { //cay thong noel
                                    it.itemOptions.add(new ItemOption(50, Util.nextInt(10, 20)));
                                    it.itemOptions.add(new ItemOption(77, Util.nextInt(10, 20)));
                                    it.itemOptions.add(new ItemOption(103, Util.nextInt(10, 20)));
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(3, 30)));
                                    it.itemOptions.add(new ItemOption(30, 0));
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else if (temp[index] == 746) { // xe truot tuyet
                                    it.itemOptions.add(new ItemOption(74, 0));
                                    it.itemOptions.add(new ItemOption(30, 0));
                                    if (Util.isTrue(99, 100)) {
                                        it.itemOptions.add(new ItemOption(93, Util.nextInt(30, 360)));
                                    }
                                } else if (temp[index] == 999) { // mèo mun
                                    it.itemOptions.add(new ItemOption(77, 15));
                                    it.itemOptions.add(new ItemOption(74, 0));
                                    it.itemOptions.add(new ItemOption(30, 0));
                                    if (Util.isTrue(99, 100)) {
                                        it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                    }
                                } else if (temp[index] == 1000) { // xiên cá
                                    it.itemOptions.add(new ItemOption(103, 15));
                                    it.itemOptions.add(new ItemOption(74, 0));
                                    it.itemOptions.add(new ItemOption(30, 0));
                                    if (Util.isTrue(99, 100)) {
                                        it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                    }
                                } else if (temp[index] == 1001) { // Phóng heo
                                    it.itemOptions.add(new ItemOption(50, 15));
                                    it.itemOptions.add(new ItemOption(74, 0));
                                    it.itemOptions.add(new ItemOption(30, 0));
                                    if (Util.isTrue(99, 100)) {
                                        it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                    }
                                } else if (temp[index] == 2022 || temp[index] == 821) {
                                    it.itemOptions.add(new ItemOption(30, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(73, 0));
                                }
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                icon[1] = it.template.iconID;
                                CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
                                InventoryService.gI().addItemBag(pl, it, 0);
                                InventoryService.gI().sendItemBags(pl);
                            }
                        } else {
                            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
                        }
                    } else {
                        Service.getInstance().sendThongBao(pl, "Sự kiện đã kết thúc");
                    }
                    break;
                case ConstEvent.SU_KIEN_TET:
                    if (Manager.EVENT_SEVER == idsukien) {
                        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
                            short[] icon = new short[2];
                            icon[0] = item.template.iconID;
                            RandomCollection<Integer> rd = Manager.HOP_QUA_TET;
                            int tempID = rd.next();
                            Item it = ItemService.gI().createNewItem((short) tempID);
                            if (it.template.type == 11) {//FLAGBAG
                                it.itemOptions.add(new ItemOption(50, Util.nextInt(5, 20)));
                                it.itemOptions.add(new ItemOption(77, Util.nextInt(5, 20)));
                                it.itemOptions.add(new ItemOption(103, Util.nextInt(5, 20)));
                            } else if (tempID >= 1159 && tempID <= 1161) {
                                it.itemOptions.add(new ItemOption(50, Util.nextInt(20, 30)));
                                it.itemOptions.add(new ItemOption(77, Util.nextInt(20, 30)));
                                it.itemOptions.add(new ItemOption(103, Util.nextInt(20, 30)));
                                it.itemOptions.add(new ItemOption(106, 0));
                            } else if (tempID == ConstItem.CAI_TRANG_SSJ_3_WHITE) {
                                it.itemOptions.add(new ItemOption(50, Util.nextInt(30, 40)));
                                it.itemOptions.add(new ItemOption(77, Util.nextInt(30, 40)));
                                it.itemOptions.add(new ItemOption(103, Util.nextInt(30, 40)));
                                it.itemOptions.add(new ItemOption(5, Util.nextInt(10, 25)));
                                it.itemOptions.add(new ItemOption(104, Util.nextInt(5, 15)));
                            }
                            int type = it.template.type;
                            if (type == 5 || type == 11) {// cải trang & flagbag
                                if (Util.isTrue(199, 200)) {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                                it.itemOptions.add(new ItemOption(199, 0));//KHÔNG THỂ GIA HẠN
                            } else if (type == 23) {// thú cưỡi
                                if (Util.isTrue(199, 200)) {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 5)));
                                }
                            }
                            if (tempID >= ConstItem.MANH_AO && tempID <= ConstItem.MANH_GANG_TAY) {
                                it.quantity = Util.nextInt(5, 15);
                            } else {
                                it.itemOptions.add(new ItemOption(74, 0));
                            }
                            icon[1] = it.template.iconID;
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
                            InventoryService.gI().addItemBag(pl, it, 0);
                            InventoryService.gI().sendItemBags(pl);
                            break;
                        } else {
                            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
                        }
                    } else {
                        Service.getInstance().sendThongBao(pl, "Sự kiện đã kết thúc");
                    }
                    break;
            }
        } catch (Exception e) {
            logger.error("Lỗi mở hộp quà", e);
        }
    }

    private void openboxkichhoat(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {76, 188, 189, 190, 441, 442, 447, 2010, 2009, 865, 938, 939, 940, 16, 17, 18, 19, 20, 946, 947, 948, 382, 383, 384, 385};
            int[][] gold = {{5000, 20000}};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            if (index <= 3 && index >= 0) {
                pl.inventory.addGold(Util.nextInt(gold[0][0], gold[0][1]));
                PlayerService.gI().sendInfoHpMpMoney(pl);
                icon[1] = 930;
            } else {

                Item it = ItemService.gI().createNewItem(temp[index]);
                if (temp[index] == 441) {
                    it.itemOptions.add(new ItemOption(95, 5));
                } else if (temp[index] == 442) {
                    it.itemOptions.add(new ItemOption(96, 5));
                } else if (temp[index] == 447) {
                    it.itemOptions.add(new ItemOption(101, 5));
                } else if (temp[index] >= 2009 && temp[index] <= 2010) {
                    it.itemOptions.add(new ItemOption(30, 0));
                } else if (temp[index] == 865) {
                    it.itemOptions.add(new ItemOption(30, 0));
                    if (Util.isTrue(1, 20)) {
                        it.itemOptions.add(new ItemOption(93, 365));
                    } else {
                        it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                    }
                } else if (temp[index] >= 938 && temp[index] <= 940) {
                    it.itemOptions.add(new ItemOption(77, 35));
                    it.itemOptions.add(new ItemOption(103, 35));
                    it.itemOptions.add(new ItemOption(50, 35));
                    if (Util.isTrue(1, 50)) {
                        it.itemOptions.add(new ItemOption(116, 0));
                    } else {
                        it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                    }
                } else if (temp[index] >= 946 && temp[index] <= 948) {
                    it.itemOptions.add(new ItemOption(77, 35));
                    it.itemOptions.add(new ItemOption(103, 35));
                    it.itemOptions.add(new ItemOption(50, 35));
                    if (Util.isTrue(1, 20)) {
                        it.itemOptions.add(new ItemOption(93, 365));
                    } else {
                        it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                    }
                } else {
                    it.itemOptions.add(new ItemOption(73, 0));
                }
                InventoryService.gI().addItemBag(pl, it, 0);
                icon[1] = it.template.iconID;

            }
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);

            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void openPhieuCaiTrangHaiTac(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item ct = ItemService.gI().createNewItem((short) Util.nextInt(618, 626));
            ct.itemOptions.add(new ItemOption(147, 3));
            ct.itemOptions.add(new ItemOption(77, 3));
            ct.itemOptions.add(new ItemOption(103, 3));
            ct.itemOptions.add(new ItemOption(149, 0));
            if (item.template.id == 2006) {
                ct.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
            } else if (item.template.id == 2007) {
                ct.itemOptions.add(new ItemOption(93, Util.nextInt(7, 30)));
            }
            InventoryService.gI().addItemBag(pl, ct, 0);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, item.template.iconID, ct.template.iconID);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void eatGrapes(Player pl, Item item) {
        int percentCurrentStatima = pl.nPoint.stamina * 100 / pl.nPoint.maxStamina;
        if (percentCurrentStatima > 50) {
            Service.getInstance().sendThongBao(pl, "Thể lực vẫn còn trên 50%");
            return;
        } else if (item.template.id == 211) {
            pl.nPoint.stamina = pl.nPoint.maxStamina;
            Service.getInstance().sendThongBao(pl, "Thể lực của bạn đã được hồi phục 100%");
        } else if (item.template.id == 212) {
            pl.nPoint.stamina += (pl.nPoint.maxStamina * 20 / 100);
            Service.getInstance().sendThongBao(pl, "Thể lực của bạn đã được hồi phục 20%");
        }
        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
        InventoryService.gI().sendItemBags(pl);
        PlayerService.gI().sendCurrentStamina(pl);
    }

    private void openCSKB(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {76, 188, 189, 190, 381, 382, 383, 384, 385};
            int[][] gold = {{5000, 20000}};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            if (index <= 3) {
                pl.inventory.addGold(Util.nextInt(gold[0][0], gold[0][1]));
                PlayerService.gI().sendInfoHpMpMoney(pl);
                icon[1] = 930;
            } else {
                Item it = ItemService.gI().createNewItem(temp[index]);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryService.gI().addItemBag(pl, it, 0);
                icon[1] = it.template.iconID;
            }
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);

            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void useItemTime(Player pl, Item item) {
        boolean updatePoint = false;
        switch (item.template.id) {
            case 382: //bổ huyết
                if (pl.itemTime.isUseBoHuyet2) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimeBoHuyet = System.currentTimeMillis();
                pl.itemTime.isUseBoHuyet = true;
                updatePoint = true;
                break;
            case 383: //bổ khí
                if (pl.itemTime.isUseBoKhi2) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimeBoKhi = System.currentTimeMillis();
                pl.itemTime.isUseBoKhi = true;
                updatePoint = true;
                break;
            case 384: //giáp xên
                if (pl.itemTime.isUseGiapXen2) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimeGiapXen = System.currentTimeMillis();
                pl.itemTime.isUseGiapXen = true;
                updatePoint = true;
                break;
            case 381: //cuồng nộ
                if (pl.itemTime.isUseCuongNo2) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimeCuongNo = System.currentTimeMillis();
                pl.itemTime.isUseCuongNo = true;
                updatePoint = true;
                break;
            case 385: //ẩn danh
                pl.itemTime.lastTimeAnDanh = System.currentTimeMillis();
                pl.itemTime.isUseAnDanh = true;
                break;
            case ConstItem.BO_HUYET_2: //bổ huyết 2
                if (pl.itemTime.isUseBoHuyet) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimeBoHuyet2 = System.currentTimeMillis();
                pl.itemTime.isUseBoHuyet2 = true;
                updatePoint = true;
                break;
            case ConstItem.BO_KHI_2: //bổ khí 2
                if (pl.itemTime.isUseBoKhi) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimeBoKhi2 = System.currentTimeMillis();
                pl.itemTime.isUseBoKhi2 = true;
                updatePoint = true;
                break;
            case ConstItem.GIAP_XEN_BO_HUNG_2: //giáp xên 2
                if (pl.itemTime.isUseGiapXen) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimeGiapXen2 = System.currentTimeMillis();
                pl.itemTime.isUseGiapXen2 = true;
                updatePoint = true;
                break;
            case ConstItem.CUONG_NO_2: //cuồng nộ 2
                if (pl.itemTime.isUseCuongNo) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimeCuongNo2 = System.currentTimeMillis();
                pl.itemTime.isUseCuongNo2 = true;
                updatePoint = true;
                break;
            case 379: //máy dò
                pl.itemTime.lastTimeUseMayDo = System.currentTimeMillis();
                pl.itemTime.isUseMayDo = true;
                break;
            case 663: //bánh pudding
            case 664: //xúc xíc
            case 665: //kem dâu
            case 666: //mì ly
            case 667: //sushi
                pl.itemTime.lastTimeEatMeal = System.currentTimeMillis();
                pl.itemTime.isEatMeal = true;
                ItemTimeService.gI().removeItemTime(pl, pl.itemTime.iconMeal);
                pl.itemTime.iconMeal = item.template.iconID;
                updatePoint = true;
                break;
//            case ConstItem.BANH_CHUNG_CHIN:
//                pl.itemTime.lastTimeBanhChung = System.currentTimeMillis();
//                pl.itemTime.isUseBanhChung = true;
//                updatePoint = true;
//                break;
            case ConstItem.BANH_TET_CHIN:
                pl.itemTime.lastTimeBanhTet = System.currentTimeMillis();
                pl.itemTime.isUseBanhTet = true;
                updatePoint = true;
                break;
            case 1326:
                pl.itemTime.lastTimebuax5 = System.currentTimeMillis();
                pl.itemTime.isUsebuax5 = true;
                updatePoint = true;
                break;
            case 1416:
                pl.itemTime.lastTimebuax2dt = System.currentTimeMillis();
                pl.itemTime.isUsebuax2dt = true;
                updatePoint = true;
                break;
            case 1518:
                pl.itemTime.lastTime4la = System.currentTimeMillis();
                pl.itemTime.isUse4la = true;
                updatePoint = true;
                break;
            case ConstItem.BINH_CHUA_COMMESON:
                pl.itemTime.lastTimebinhcommeson = System.currentTimeMillis();
                pl.itemTime.isUsebinhcommeson = true;
                updatePoint = true;
                break;
//            case 1420:
//                pl.itemTime.lastTimeCuongbao = System.currentTimeMillis();
//                pl.itemTime.isUseCuongbao = true;
//                updatePoint = true;
//                break;
            case 1128: //Tinh thạch pháp sư
                pl.itemTime.lastTimeBanhChung = System.currentTimeMillis();
                pl.itemTime.isUseBanhChung = true;
                updatePoint = true;
                break;
            case 1129: //Hỏa ngọc cam
                pl.itemTime.lastTimeCuongbao = System.currentTimeMillis();
                pl.itemTime.isUseCuongbao = true;
                updatePoint = true;
                break;
            case 2082: //Sức mạnh tiềm ẩn
                if (pl.gender != 1) {
                    Service.getInstance().sendThongBao(pl, "Chỉ người Namek mới có thể mở Sức mạnh tiềm ẩn");
                    return;
                }
                pl.itemTime.lastTimeSMTA = System.currentTimeMillis();
                pl.itemTime.isUseSMTA = true;
                updatePoint = true;
                break;
        }
        if (updatePoint) {
            Service.getInstance().point(pl);
        }
        ItemTimeService.gI().sendAllItemTime(pl);
        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
        InventoryService.gI().sendItemBags(pl);
    }

    private void useItemThaySkill(Player pl, Item item) {
        switch (item.template.id) {
            case 1417:
                if (pl.pet != null) {
                    if (pl.pet.playerSkill.skills.get(1).skillId != -1) {
                        pl.pet.openSkill2();
                        if (pl.pet.playerSkill.skills.get(2).skillId != -1) {
                            pl.pet.openSkill3();
                        }
                    } else {
                        Service.getInstance().sendThongBao(pl, "Ít nhất đệ tử ngươi phải có chiêu 2 chứ!");
                        return;
                    }
                } else {
                    Service.getInstance().sendThongBao(pl, "Ngươi làm gì có đệ tử?");
                    return;
                }
                break;
            case 1418:
                if (pl.pet != null) {
                    if (pl.pet.playerSkill.skills.get(2).skillId != -1) {
                        pl.pet.openSkill3();
                        if (pl.pet.playerSkill.skills.get(3).skillId != -1) {
                            pl.pet.openSkill4();
                        }
                    } else {
                        Service.getInstance().sendThongBao(pl, "Ít nhất đệ tử ngươi phải có chiêu 3 chứ!");
                        return;
                    }
                } else {
                    Service.getInstance().sendThongBao(pl, "Ngươi làm gì có đệ tử?");
                    return;
                }
                break;
            case 1419:
                if (pl.pet != null) {
                    if (pl.pet.playerSkill.skills.get(3).skillId != -1) {
                        pl.pet.openSkill4();
                        if (pl.pet.playerSkill.skills.get(4).skillId != -1) {
                            pl.pet.openSkill5();
                        }
                    } else {
                        Service.getInstance().sendThongBao(pl, "Ít nhất đệ tử ngươi phải có chiêu 4 chứ!");
                        return;
                    }
                } else {
                    Service.getInstance().sendThongBao(pl, "Ngươi làm gì có đệ tử?");
                    return;
                }
                break;

        }
        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
        InventoryService.gI().sendItemBags(pl);
    }

    private void controllerCallRongThan(Player pl, Item item) {
        int tempId = item.template.id;
        if (tempId >= SummonDragon.NGOC_RONG_1_SAO && tempId <= SummonDragon.NGOC_RONG_7_SAO) {
            switch (tempId) {
                case SummonDragon.NGOC_RONG_1_SAO:
                case SummonDragon.NGOC_RONG_2_SAO:
                case SummonDragon.NGOC_RONG_3_SAO:
                    SummonDragon.gI().openMenuSummonShenron(pl, (byte) (tempId - 13), SummonDragon.DRAGON_SHENRON);
                    break;
                default:
                    NpcService.gI().createMenuConMeo(pl, ConstNpc.TUTORIAL_SUMMON_DRAGON, -1, "Bạn chỉ có thể gọi rồng từ ngọc 3 sao, 2 sao, 1 sao", "Hướng\ndẫn thêm\n(mới)", "OK");
                    break;
            }
//        } else if (tempId == SummonDragon.NGOC_RONG_SIEU_CAP) {
//            SummonDragon.gI().openMenuSummonShenron(pl, (byte) 1015, SummonDragon.DRAGON_BLACK_SHENRON);
        } else if (tempId >= SummonDragon.NGOC_RONG_BANG[0] && tempId <= SummonDragon.NGOC_RONG_BANG[6]) {
            switch (tempId) {
                case 925:
                    SummonDragon.gI().openMenuSummonShenron(pl, (byte) 925, SummonDragon.DRAGON_ICE_SHENRON);
                    break;
                default:
                    Service.getInstance().sendThongBao(pl, "Bạn chỉ có thể gọi rồng băng từ ngọc 1 sao");
                    break;
            }
        }
    }

    private void learnSkill(Player pl, Item item) {
        Message msg;
        try {
            if (item.template.gender == pl.gender || item.template.gender == 3) {
                String[] subName = item.template.name.split("");
                byte level = Byte.parseByte(subName[subName.length - 1]);
                Skill curSkill = SkillUtil.getSkillByItemID(pl, item.template.id);
                if (curSkill.point == 7) {
                    Service.getInstance().sendThongBao(pl, "Kỹ năng đã đạt tối đa!");
                } else {
                    if (curSkill.point == 0) {
                        if (level == 1) {
                            curSkill = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
                            SkillUtil.setSkill(pl, curSkill);
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            msg = Service.getInstance().messageSubCommand((byte) 23);
                            msg.writer().writeShort(curSkill.skillId);
                            pl.sendMessage(msg);
                            msg.cleanup();
                        } else {
                            Skill skillNeed = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
                            Service.getInstance().sendThongBao(pl, "Vui lòng học " + skillNeed.template.name + " cấp " + skillNeed.point + " trước!");
                        }
                    } else {
                        if (curSkill.point + 1 == level) {
                            curSkill = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
                            //System.out.println(curSkill.template.name + " - " + curSkill.point);
                            SkillUtil.setSkill(pl, curSkill);
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            msg = Service.getInstance().messageSubCommand((byte) 62);
                            msg.writer().writeShort(curSkill.skillId);
                            pl.sendMessage(msg);
                            msg.cleanup();
                        } else {
                            Service.getInstance().sendThongBao(pl, "Vui lòng học " + curSkill.template.name + " cấp " + (curSkill.point + 1) + " trước!");
                        }
                    }
                    InventoryService.gI().sendItemBags(pl);
                }
            } else {
                Service.getInstance().sendThongBao(pl, "Không thể thực hiện");

            }
        } catch (Exception e) {
            Log.error(UseItem.class, e);
        }
    }

    private void useTDLT(Player pl, Item item) {
        if (pl.itemTime.isUseTDLT) {
            ItemTimeService.gI().turnOffTDLT(pl, item);
        } else {
            ItemTimeService.gI().turnOnTDLT(pl, item);
        }
    }

    private void usePorata(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == 4) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusion(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void usePorata2(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == 4 || pl.fusion.typeFusion == 6 || pl.fusion.typeFusion == 10 || pl.fusion.typeFusion == 12) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        } else if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
            pl.pet.fusion2(true);
        } else {
            pl.pet.unFusion();
        }
    }

    private void usePorataVoCuc(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == 4) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        } else if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
            pl.pet.fusion3(true);
        } else {
            pl.pet.unFusion();
        }
    }

    private void openCapsuleUI(Player pl) {
        if (pl.isHoldNamecBall) {
            NamekBallWar.gI().dropBall(pl);
            Service.getInstance().sendFlagBag(pl);
        }
        pl.iDMark.setTypeChangeMap(ConstMap.CHANGE_CAPSULE);
        ChangeMapService.gI().openChangeMapTab(pl);
    }

    public void choseMapCapsule(Player pl, int index) {
        int zoneId = -1;
        if (index < 0 || index >= pl.mapCapsule.size()) {
            return;
        }
        Zone zoneChose = pl.mapCapsule.get(index);
        if (index != 0 || zoneChose.map.mapId == 21 || zoneChose.map.mapId == 22 || zoneChose.map.mapId == 23) {
            if (!(pl.zone != null && pl.zone instanceof ZSnakeRoad)) {
                pl.mapBeforeCapsule = pl.zone;
            } else {
                pl.mapBeforeCapsule = null;
            }
        } else {
            zoneId = pl.mapBeforeCapsule != null ? pl.mapBeforeCapsule.zoneId : -1;
            pl.mapBeforeCapsule = null;
        }
        ChangeMapService.gI().changeMapBySpaceShip(pl, pl.mapCapsule.get(index).map.mapId, zoneId, -1);
    }

    private void upSkillPet(Player pl, Item item) {
        if (pl.pet == null) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
            return;
        }
        try {
            switch (item.template.id) {
                case 402: //skill 1
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 0)) {
                        Service.getInstance().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 403: //skill 2
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 1)) {
                        Service.getInstance().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 404: //skill 3
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 2)) {
                        Service.getInstance().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 759: //skill 4
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 3)) {
                        Service.getInstance().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 1324://skill 5
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 4)) {
                        Service.getInstance().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ Tuyệt Kỹ của con lên cấp rồi");
                        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
            }
        } catch (Exception e) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        }
    }

    public static int[][] itemHD = {{650, 651, 657, 658, 656}, {652, 653, 659, 660, 656}, {654, 655, 661, 662, 656}};

    public static Item itemdoHD(int itemId) {
        Item item = ItemService.gI().createItemSetKichHoat(itemId, 1);
        if (item != null) {
            item.itemOptions.addAll(ItemService.gI().getListOptionItemShop((short) itemId));
            item.itemOptions.add(new ItemOption(30, 1));
        }
        return item;
    }

    private void hopQuaKichHoat(Player player, Item item) {
        NpcService.gI().createMenuConMeo(player,
                ConstNpc.MENU_OPTION_USE_ITEM1105, -1, "Chọn hành tinh của cậu đi",
                "Set trái đất",
                "Set namec",
                "Set xayda",
                "Từ chổi");
    }

    private void openChonHTDeTu(Player player, Item item) {
        NpcService.gI().createMenuConMeo(player,
                ConstNpc.MENU_OPTION_USE_ITEM568, -1, "Chọn hành tinh của đệ tử",
                "Trái đất",
                "Namec",
                "Xayda",
                "Từ chổi");
    }

    private void openChonHTDeTuBill(Player player, Item item) {
        NpcService.gI().createMenuConMeo(player,
                ConstNpc.MENU_OPTION_USE_ITEM1458, -1, "Chọn hành tinh của đệ tử",
                "Trái đất",
                "Namec",
                "Xayda",
                "Từ chổi");
    }

    private void hopQuaThanLinh(Player player, Item item) {
        NpcService.gI().createMenuConMeo(player,
                ConstNpc.MENU_OPTION_USE_ITEM1280, -1, "Chọn hành tinh của cậu đi",
                "Set trái đất",
                "Set namec",
                "Set xayda",
                "Từ chổi");
    }

    private void hopQuaThanLinhskh(Player player, Item item) {
        NpcService.gI().createMenuConMeo(player,
                ConstNpc.MENU_OPTION_USE_ITEM1281, -1, "Chọn hành tinh của cậu đi",
                "Set trái đất",
                "Set namec",
                "Set xayda",
                "Từ chổi");
    }

    private void hopQuaHuyDiet(Player player, Item item) {
        NpcService.gI().createMenuConMeo(player,
                ConstNpc.MENU_OPTION_USE_ITEM1282, -1, "Chọn hành tinh của cậu đi",
                "Set trái đất",
                "Set namec",
                "Set xayda",
                "Từ chổi");
    }

    private void hopQuaThienSu(Player player, Item item) {
        NpcService.gI().createMenuConMeo(player,
                ConstNpc.MENU_OPTION_USE_ITEM1283, -1, "Chọn hành tinh của cậu đi",
                "Set trái đất",
                "Set namec",
                "Set xayda",
                "Từ chổi");
    }

    public void ComfirmMocNap(Player player) {

        Item itemqua;
        Item itemqua1;
        Item itemqua2;
        Item itemqua3;
        Item itemqua4;
        Item itemqua5;
        Item itemqua6;
        try {
            int time = 5;
            if (player.getSession().tongnap >= 20000 && player.mocnap == 0) {
                if (InventoryService.gI().getCountEmptyBag(player) < 6) {
                    Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 6 ô trống hành trang");
                    return;
                }
                Service.getInstance().sendThongBao(player, "Tiến Hành Nhận\nMốc Nạp 20K\nSau " + time + " Giây!");
                while (time > 0) {
                    time--;
                    Thread.sleep(1000);
                    Service.getInstance().sendThongBao(player, "|7|" + time);
                }
                player.mocnap = 20000;//20k
                itemqua = ItemService.gI().createNewItem((short) 1150, 5);//cuồng nộ 2
                itemqua1 = ItemService.gI().createNewItem((short) 1151, 5);//bổ khí 2
                itemqua2 = ItemService.gI().createNewItem((short) 1152, 5);//bổ huyết 2
                itemqua3 = ItemService.gI().createNewItem((short) 1153, 5);//giáp 2
                itemqua4 = ItemService.gI().createNewItem((short) 1326, 3);//bùa x5
                itemqua5 = ItemService.gI().createNewItem((short) 457, 20);//tv

                Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua.template.name);
                Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua1.template.name);
                Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua2.template.name);
                Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua3.template.name);
                Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua4.template.name);
                Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua5.template.name);

                InventoryService.gI().addItemBag(player, itemqua, 9999);
                InventoryService.gI().addItemBag(player, itemqua1, 9999);
                InventoryService.gI().addItemBag(player, itemqua2, 9999);
                InventoryService.gI().addItemBag(player, itemqua3, 9999);
                InventoryService.gI().addItemBag(player, itemqua4, 9999);
                InventoryService.gI().addItemBag(player, itemqua5, 9999);

                InventoryService.gI().sendItemBags(player);
            } else if (player.getSession().tongnap >= 50000 && player.mocnap == 20000) {
                if (InventoryService.gI().getCountEmptyBag(player) < 7) {
                    Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 7 ô trống hành trang");
                    return;
                }
                Service.getInstance().sendThongBao(player, "Tiến Hành Nhận\nMốc Nạp 50K\nSau " + time + " Giây!");
                while (time > 0) {
                    time--;
                    Thread.sleep(1000);
                    Service.getInstance().sendThongBao(player, "|7|" + time);
                }
                player.mocnap = 50000;//50k
                itemqua = ItemService.gI().createNewItem((short) 1150, 10);//cuồng nộ 2
                itemqua1 = ItemService.gI().createNewItem((short) 1151, 10);//bổ khí 2
                itemqua2 = ItemService.gI().createNewItem((short) 1152, 10);//bổ huyết 2
                itemqua3 = ItemService.gI().createNewItem((short) 1153, 10);//giáp 2
                itemqua4 = ItemService.gI().createNewItem((short) 1517, 1);//giáp luyện tập c4
                itemqua4.itemOptions.add(new ItemOption(77, 10));
                itemqua4.itemOptions.add(new ItemOption(103, 10));
                itemqua5 = ItemService.gI().createNewItem((short) 16, 10);//3sao
                itemqua6 = ItemService.gI().createNewItem((short) 457, 80);//tv

                Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua.template.name);
                Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua1.template.name);
                Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua2.template.name);
                Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua3.template.name);
                Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua4.template.name);
                Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua4.template.name);
                Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua5.template.name);
                Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua6.template.name);

                InventoryService.gI().addItemBag(player, itemqua, 9999);
                InventoryService.gI().addItemBag(player, itemqua1, 9999);
                InventoryService.gI().addItemBag(player, itemqua2, 9999);
                InventoryService.gI().addItemBag(player, itemqua3, 9999);
                InventoryService.gI().addItemBag(player, itemqua4, 9999);
                InventoryService.gI().addItemBag(player, itemqua5, 9999);
                InventoryService.gI().addItemBag(player, itemqua6, 9999);

                InventoryService.gI().sendItemBags(player);
            } else {
                Service.getInstance().sendThongBao(player, "Bạn Chưa Đủ Điều Kiện Nhận Mốc Nạp Này!");
            }
        } catch (Exception e) {
        }

    }

    public void ComfirmMocSb(Player player) {
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            Item itemqua;
            Item itemqua1;
            Item itemqua2;
            Item itemqua3;
            try {
                int time = 5;
                if (player.pointSb >= 100 && player.mocsanboss == 0) {
                    Service.getInstance().sendThongBao(player, "Tiến Hành Nhận\nMốc 100 Điểm Boss\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.getInstance().sendThongBao(player, "|7|" + time);
                    }
                    player.mocsanboss = 100;
                    itemqua = ItemService.gI().createNewItem((short) 457, 20);
                    itemqua.itemOptions.add(new ItemOption(30, 1));
                    itemqua1 = ItemService.gI().createNewItem((short) 1318, 55);
                    itemqua2 = ItemService.gI().createNewItem((short) 1262, 111);
                    itemqua3 = ItemService.gI().createNewItem((short) 1296, 5);
                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua.template.name);
                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua1.template.name);
                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua2.template.name);
                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua3.template.name);
                    InventoryService.gI().addItemBag(player, itemqua, 9999);
                    InventoryService.gI().addItemBag(player, itemqua1, 9999);
                    InventoryService.gI().addItemBag(player, itemqua2, 9999);
                    InventoryService.gI().addItemBag(player, itemqua3, 9999);
                    InventoryService.gI().sendItemBags(player);
                } else if (player.pointSb >= 200 && player.mocsanboss == 100) {
                    Service.getInstance().sendThongBao(player, "Tiến Hành Nhận\nMốc 200 Điểm Boss\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.getInstance().sendThongBao(player, "|7|" + time);
                    }
                    player.mocsanboss = 200;
                    itemqua = ItemService.gI().createNewItem((short) 457, 30);
                    itemqua.itemOptions.add(new ItemOption(30, 1));
                    itemqua1 = ItemService.gI().createNewItem((short) 1318, 111);
                    itemqua2 = ItemService.gI().createNewItem((short) 1262, 222);
                    itemqua3 = ItemService.gI().createNewItem((short) 1296, 5);
                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua.template.name);
                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua1.template.name);
                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua2.template.name);
                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua3.template.name);
                    InventoryService.gI().addItemBag(player, itemqua, 9999);
                    InventoryService.gI().addItemBag(player, itemqua1, 9999);
                    InventoryService.gI().addItemBag(player, itemqua2, 9999);
                    InventoryService.gI().addItemBag(player, itemqua3, 9999);
                    InventoryService.gI().sendItemBags(player);
                } else if (player.pointSb >= 300 && player.mocsanboss == 200) {
                    Service.getInstance().sendThongBao(player, "Tiến Hành Nhận\nMốc 300 Điểm Boss\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.getInstance().sendThongBao(player, "|7|" + time);
                    }
                    player.mocsanboss = 300;
                    itemqua = ItemService.gI().createNewItem((short) 457, 50);
                    itemqua.itemOptions.add(new ItemOption(30, 1));
                    itemqua1 = ItemService.gI().createNewItem((short) 1318, 151);
                    itemqua2 = ItemService.gI().createNewItem((short) 1262, 333);
                    itemqua3 = ItemService.gI().createNewItem((short) 1296, 5);
                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua.template.name);
                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua1.template.name);
                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua2.template.name);
                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua3.template.name);
                    InventoryService.gI().addItemBag(player, itemqua, 9999);
                    InventoryService.gI().addItemBag(player, itemqua1, 9999);
                    InventoryService.gI().addItemBag(player, itemqua2, 9999);
                    InventoryService.gI().addItemBag(player, itemqua3, 9999);
                    InventoryService.gI().sendItemBags(player);
                } else if (player.pointSb >= 500 && player.mocsanboss == 300) {
                    Service.getInstance().sendThongBao(player, "Tiến Hành Nhận\nMốc 500 Điểm Boss\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.getInstance().sendThongBao(player, "|7|" + time);
                    }
                    player.mocsanboss = 500;
                    itemqua = ItemService.gI().createNewItem((short) 457, 150);
                    itemqua.itemOptions.add(new ItemOption(30, 1));
                    itemqua1 = ItemService.gI().createNewItem((short) 1318, 252);
                    itemqua2 = ItemService.gI().createNewItem((short) 1262, 555);
                    itemqua3 = ItemService.gI().createNewItem((short) 1296, 15);
                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua.template.name);
                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua1.template.name);
                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua2.template.name);
                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua3.template.name);
                    InventoryService.gI().addItemBag(player, itemqua, 9999);
                    InventoryService.gI().addItemBag(player, itemqua1, 9999);
                    InventoryService.gI().addItemBag(player, itemqua2, 9999);
                    InventoryService.gI().addItemBag(player, itemqua3, 9999);
                    InventoryService.gI().sendItemBags(player);
                } else if (player.pointSb >= 800 && player.mocsanboss == 500) {
                    Service.getInstance().sendThongBao(player, "Tiến Hành Nhận\nMốc 800 Điểm Boss\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.getInstance().sendThongBao(player, "|7|" + time);
                    }
                    player.mocsanboss = 800;
                    itemqua = ItemService.gI().createNewItem((short) 457, 300);
                    itemqua.itemOptions.add(new ItemOption(30, 1));
                    itemqua1 = ItemService.gI().createNewItem((short) 1318, 444);
                    itemqua2 = ItemService.gI().createNewItem((short) 1262, 888);
                    itemqua3 = ItemService.gI().createNewItem((short) 1296, 20);
                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua.template.name);
                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua1.template.name);
                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua2.template.name);
                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua3.template.name);
                    InventoryService.gI().addItemBag(player, itemqua, 9999);
                    InventoryService.gI().addItemBag(player, itemqua1, 9999);
                    InventoryService.gI().addItemBag(player, itemqua2, 9999);
                    InventoryService.gI().addItemBag(player, itemqua3, 9999);
                    InventoryService.gI().sendItemBags(player);
                } else if (player.pointSb >= 1000 && player.mocsanboss == 800) {
                    Service.getInstance().sendThongBao(player, "Tiến Hành Nhận\nMốc 1.000 Điểm Boss\nSau " + time + " Giây!");
                    while (time > 0) {
                        time--;
                        Thread.sleep(1000);
                        Service.getInstance().sendThongBao(player, "|7|" + time);
                    }
                    player.mocsanboss = 1000;
                    itemqua = ItemService.gI().createNewItem((short) 457, 500);
                    itemqua.itemOptions.add(new ItemOption(30, 1));
                    itemqua1 = ItemService.gI().createNewItem((short) 1318, 499);
                    itemqua2 = ItemService.gI().createNewItem((short) 1262, 999);
                    itemqua3 = ItemService.gI().createNewItem((short) 1296, 30);
                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua.template.name);
                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua1.template.name);
                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua2.template.name);
                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua3.template.name);
                    InventoryService.gI().addItemBag(player, itemqua, 9999);
                    InventoryService.gI().addItemBag(player, itemqua1, 9999);
                    InventoryService.gI().addItemBag(player, itemqua2, 9999);
                    InventoryService.gI().addItemBag(player, itemqua3, 9999);
                    InventoryService.gI().sendItemBags(player);
                } else {
                    Service.getInstance().sendThongBao(player, "Bạn Chưa Đủ Điều Kiện Nhận Mốc Điểm Săn Boss Này!");
                }
            } catch (Exception e) {
            }
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }
}
