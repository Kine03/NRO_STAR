package nro.services;

import nro.models.item.ItemOptionTemplate;
import nro.models.item.ItemTemplate;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.shop.ItemShop;
import nro.server.Manager;
import nro.utils.TimeUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import nro.models.kygui.ConsignmentItem;
import nro.models.player.Player;
import nro.utils.Util;

/**
 * @author 💖 Trần Lại 💖
 * @copyright 💖 GirlkuN 💖
 */
public class ItemService {

    private static ItemService i;

    public static ItemService gI() {
        if (i == null) {
            i = new ItemService();
        }
        return i;
    }

    public Item createItemNull() {
        Item item = new Item();
        return item;
    }

    public Item createItemFromItemShop(ItemShop itemShop) {
        Item item = new Item();
        item.template = itemShop.temp;
        item.quantity = 1;
        item.content = item.getContent();
        item.info = item.getInfo();
        for (ItemOption io : itemShop.options) {
            item.itemOptions.add(new ItemOption(io));
        }
        return item;
    }

    public Item copyItem(Item item) {
        Item it = new Item();
        it.itemOptions = new ArrayList<>();
        it.template = item.template;
        it.info = item.info;
        it.content = item.content;
        it.quantity = item.quantity;
        it.createTime = item.createTime;
        for (ItemOption io : item.itemOptions) {
            it.itemOptions.add(new ItemOption(io));
        }
        return it;
    }


    public Item createNewItem(short tempId) {
        return createNewItem(tempId, 1);
    }

    public Item createNewItem(short tempId, int quantity) {
        Item item = new Item();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.createTime = System.currentTimeMillis();

        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }
    
    public Item createItemFromItemMap(ItemMap itemMap) {
        Item item = createNewItem(itemMap.itemTemplate.id, itemMap.quantity);
        item.itemOptions = itemMap.options;
        return item;
    }

    public ItemOptionTemplate getItemOptionTemplate(int id) {
        return Manager.ITEM_OPTION_TEMPLATES.get(id);
    }

    public ItemTemplate getTemplate(int id) {
        return Manager.ITEM_TEMPLATES.get(id);
    }

    public boolean isItemActivation(Item item) {
        return false;
    }
    public Item createItemSetKichHoat(int tempId, int quantity) {
        Item item = new Item();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.itemOptions = createItemNull().itemOptions;
        item.createTime = System.currentTimeMillis();
        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }
    public Item otpKH(short tempId) {
        return otpKH(tempId, 1);
    }
    public Item otpKH(short tempId, int quantity) {
        Item item = new Item();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.createTime = System.currentTimeMillis();
        switch (item.template.type) {
            case 0:
                item.itemOptions.add(new ItemOption(47, Util.nextInt(1, 4)));
                break;
            case 1:
                item.itemOptions.add(new ItemOption(6, Util.nextInt(150, 200)));
                break;
            case 2:
                item.itemOptions.add(new ItemOption(0, Util.nextInt(7, 29)));
                break;
            case 3:
                item.itemOptions.add(new ItemOption(7, Util.nextInt(2, 12)));
                break;
            case 4:
                item.itemOptions.add(new ItemOption(14, 1));
                break;
            default:
                break;
        }
        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }
    public Item otptl(short tempId) {
        return otptl(tempId, 1);
    }

    public Item otphd(short tempId) {
        return otphd(tempId, 1);
    }

    public Item otpts(short tempId) {
        return otpts(tempId, 1);
    }

    public Item otpts(short tempId, int quantity) {
        Item item = new Item();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.createTime = System.currentTimeMillis();
        if (item.template.type == 0) {
            item.itemOptions.add(new ItemOption(21, 80));
            item.itemOptions.add(new ItemOption(47, Util.nextInt(2000, 2500)));
        }
        if (item.template.type == 1) {
            item.itemOptions.add(new ItemOption(21, 80));
            item.itemOptions.add(new ItemOption(22, Util.nextInt(150, 200)));
        }
        if (item.template.type == 2) {
            item.itemOptions.add(new ItemOption(21, 80));
            item.itemOptions.add(new ItemOption(0, Util.nextInt(13000, 18000)));
        }
        if (item.template.type == 3) {
            item.itemOptions.add(new ItemOption(21, 80));
            item.itemOptions.add(new ItemOption(23, Util.nextInt(150, 200)));
        }
        if (item.template.type == 4) {
            item.itemOptions.add(new ItemOption(21, 80));
            item.itemOptions.add(new ItemOption(14, Util.nextInt(20, 25)));
        }
        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }

    public Item otphd(short tempId, int quantity) {
        Item item = new Item();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.createTime = System.currentTimeMillis();
        if (item.template.type == 0) {
            item.itemOptions.add(new ItemOption(21, 80));
            item.itemOptions.add(new ItemOption(47, Util.nextInt(2000, 2500)));
        }
        if (item.template.type == 1) {
            item.itemOptions.add(new ItemOption(21, 80));
            item.itemOptions.add(new ItemOption(22, Util.nextInt(95, 110)));
        }
        if (item.template.type == 2) {
            item.itemOptions.add(new ItemOption(21, 80));
            item.itemOptions.add(new ItemOption(0, Util.nextInt(8800, 10000)));
        }
        if (item.template.type == 3) {
            item.itemOptions.add(new ItemOption(21, 80));
            item.itemOptions.add(new ItemOption(23, Util.nextInt(95, 110)));
        }
        if (item.template.type == 4) {
            item.itemOptions.add(new ItemOption(21, 80));
            item.itemOptions.add(new ItemOption(14, Util.nextInt(18, 22)));
        }
        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }

    public Item otptl(short tempId, int quantity) {
        Item item = new Item();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.createTime = System.currentTimeMillis();
        if (item.template.type == 0) {
            item.itemOptions.add(new ItemOption(21, 16));
            item.itemOptions.add(new ItemOption(47, Util.nextInt(2000, 2500)));
        }
        if (item.template.type == 1) {
            item.itemOptions.add(new ItemOption(21, 16));
            item.itemOptions.add(new ItemOption(22, Util.nextInt(45, 65)));
        }
        if (item.template.type == 2) {
            item.itemOptions.add(new ItemOption(21, 16));
            item.itemOptions.add(new ItemOption(0, Util.nextInt(3800, 4200)));
        }
        if (item.template.type == 3) {
            item.itemOptions.add(new ItemOption(21, 16));
            item.itemOptions.add(new ItemOption(23, Util.nextInt(45, 46)));
        }
        if (item.template.type == 4) {
            item.itemOptions.add(new ItemOption(21, 16));
            item.itemOptions.add(new ItemOption(14, Util.nextInt(15, 18)));
        }
        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }
    public void setthiensu(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1323);
        Item ao = ItemService.gI().otpts((short) 1048);
        Item quan = ItemService.gI().otpts((short) 1051);
        Item gang = ItemService.gI().otpts((short) 1054);
        Item giay = ItemService.gI().otpts((short) 1057);
        Item nhan = ItemService.gI().otpts((short) 1060);
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 1);
            InventoryService.gI().addItemBag(player, quan, 1);
            InventoryService.gI().addItemBag(player, gang, 1);
            InventoryService.gI().addItemBag(player, giay, 1);
            InventoryService.gI().addItemBag(player, nhan, 1);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set hủy diệt  ");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
//    }
    }

    public void setthiensu1(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1323);
        Item ao = ItemService.gI().otpts((short) 1049);
        Item quan = ItemService.gI().otpts((short) 1052);
        Item gang = ItemService.gI().otpts((short) 1055);
        Item giay = ItemService.gI().otpts((short) 1058);
        Item nhan = ItemService.gI().otpts((short) 1061);
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 1);
            InventoryService.gI().addItemBag(player, quan, 1);
            InventoryService.gI().addItemBag(player, gang, 1);
            InventoryService.gI().addItemBag(player, giay, 1);
            InventoryService.gI().addItemBag(player, nhan, 1);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set hủy diệt  ");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
//    }
    }

    public void setthiensu2(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1323);
        Item ao = ItemService.gI().otpts((short) 1050);
        Item quan = ItemService.gI().otpts((short) 1053);
        Item gang = ItemService.gI().otpts((short) 1056);
        Item giay = ItemService.gI().otpts((short) 1059);
        Item nhan = ItemService.gI().otpts((short) 1062);
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 1);
            InventoryService.gI().addItemBag(player, quan, 1);
            InventoryService.gI().addItemBag(player, gang, 1);
            InventoryService.gI().addItemBag(player, giay, 1);
            InventoryService.gI().addItemBag(player, nhan, 1);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set hủy diệt  ");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
//    }
    }

    public void sethuydiet(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1322);
        Item ao = ItemService.gI().otphd((short) 650);
        Item quan = ItemService.gI().otphd((short) 651);
        Item gang = ItemService.gI().otphd((short) 657);
        Item giay = ItemService.gI().otphd((short) 658);
        Item nhan = ItemService.gI().otphd((short) 656);
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 1);
            InventoryService.gI().addItemBag(player, quan, 1);
            InventoryService.gI().addItemBag(player, gang, 1);
            InventoryService.gI().addItemBag(player, giay, 1);
            InventoryService.gI().addItemBag(player, nhan, 1);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set hủy diệt  ");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
//    }
    }

    public void sethuydiet1(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1322);
        Item ao = ItemService.gI().otphd((short) 652);
        Item quan = ItemService.gI().otphd((short) 653);
        Item gang = ItemService.gI().otphd((short) 659);
        Item giay = ItemService.gI().otphd((short) 660);
        Item nhan = ItemService.gI().otphd((short) 656);
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 1);
            InventoryService.gI().addItemBag(player, quan, 1);
            InventoryService.gI().addItemBag(player, gang, 1);
            InventoryService.gI().addItemBag(player, giay, 1);
            InventoryService.gI().addItemBag(player, nhan, 1);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set hủy diệt  ");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
//    }
    }

    public void sethuydiet2(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1322);
        Item ao = ItemService.gI().otphd((short) 654);
        Item quan = ItemService.gI().otphd((short) 655);
        Item gang = ItemService.gI().otphd((short) 661);
        Item giay = ItemService.gI().otphd((short) 662);
        Item nhan = ItemService.gI().otphd((short) 656);
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 1);
            InventoryService.gI().addItemBag(player, quan, 1);
            InventoryService.gI().addItemBag(player, gang, 1);
            InventoryService.gI().addItemBag(player, giay, 1);
            InventoryService.gI().addItemBag(player, nhan, 1);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set hủy diệt  ");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
//    }
    }

    public void settraidat(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1320);
        Item ao = ItemService.gI().otptl((short) 555);
        Item quan = ItemService.gI().otptl((short) 556);
        Item gang = ItemService.gI().otptl((short) 562);
        Item giay = ItemService.gI().otptl((short) 563);
        Item nhan = ItemService.gI().otptl((short) 561);
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 1);
            InventoryService.gI().addItemBag(player, quan, 1);
            InventoryService.gI().addItemBag(player, gang, 1);
            InventoryService.gI().addItemBag(player, giay, 1);
            InventoryService.gI().addItemBag(player, nhan, 1);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set thần linh");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setnamec(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1320);
        Item ao = ItemService.gI().otptl((short) 557);
        Item quan = ItemService.gI().otptl((short) 558);
        Item gang = ItemService.gI().otptl((short) 564);
        Item giay = ItemService.gI().otptl((short) 565);
        Item nhan = ItemService.gI().otptl((short) 561);
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 1);
            InventoryService.gI().addItemBag(player, quan, 1);
            InventoryService.gI().addItemBag(player, gang, 1);
            InventoryService.gI().addItemBag(player, giay, 1);
            InventoryService.gI().addItemBag(player, nhan, 1);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set thần linh");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setxayda(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1320);
        Item ao = ItemService.gI().otptl((short) 559);
        Item quan = ItemService.gI().otptl((short) 560);
        Item gang = ItemService.gI().otptl((short) 566);
        Item giay = ItemService.gI().otptl((short) 567);
        Item nhan = ItemService.gI().otptl((short) 561);
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 1);
            InventoryService.gI().addItemBag(player, quan, 1);
            InventoryService.gI().addItemBag(player, gang, 1);
            InventoryService.gI().addItemBag(player, giay, 1);
            InventoryService.gI().addItemBag(player, nhan, 1);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set thần linh");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setSongokutl(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1321);
        Item ao = ItemService.gI().otptl((short) 555);
        Item quan = ItemService.gI().otptl((short) 556);
        Item gang = ItemService.gI().otptl((short) 562);
        Item giay = ItemService.gI().otptl((short) 563);
        Item nhan = ItemService.gI().otptl((short) 561);
        ao.itemOptions.add(new ItemOption(129, 0));//129
        quan.itemOptions.add(new ItemOption(129, 0));
        gang.itemOptions.add(new ItemOption(129, 0));
        giay.itemOptions.add(new ItemOption(129, 0));
        nhan.itemOptions.add(new ItemOption(129, 0));
        ao.itemOptions.add(new ItemOption(141, 100));
        quan.itemOptions.add(new ItemOption(141, 100));
        gang.itemOptions.add(new ItemOption(141, 100));
        giay.itemOptions.add(new ItemOption(141, 100));
        nhan.itemOptions.add(new ItemOption(141, 100));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, nhan, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set Kích hoạt Kame");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setThenXinHangtl(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1321);
        Item ao = ItemService.gI().otptl((short) 555);
        Item quan = ItemService.gI().otptl((short) 556);
        Item gang = ItemService.gI().otptl((short) 562);
        Item giay = ItemService.gI().otptl((short) 563);
        Item nhan = ItemService.gI().otptl((short) 561);
        ao.itemOptions.add(new ItemOption(127, 0));
        quan.itemOptions.add(new ItemOption(127, 0));
        gang.itemOptions.add(new ItemOption(127, 0));
        giay.itemOptions.add(new ItemOption(127, 0));
        nhan.itemOptions.add(new ItemOption(127, 0));
        
        ao.itemOptions.add(new ItemOption(139, 100));
        quan.itemOptions.add(new ItemOption(139, 100));
        gang.itemOptions.add(new ItemOption(139, 100));
        giay.itemOptions.add(new ItemOption(139, 100));
        nhan.itemOptions.add(new ItemOption(139, 100));
        
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, nhan, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set Kích hoạt Thên Xin Hăng");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setKaioKentl(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1321);
        Item ao = ItemService.gI().otptl((short) 555);
        Item quan = ItemService.gI().otptl((short) 556);
        Item gang = ItemService.gI().otptl((short) 562);
        Item giay = ItemService.gI().otptl((short) 563);
        Item nhan = ItemService.gI().otptl((short) 561);
        ao.itemOptions.add(new ItemOption(128, 0));
        quan.itemOptions.add(new ItemOption(128, 0));
        gang.itemOptions.add(new ItemOption(128, 0));
        giay.itemOptions.add(new ItemOption(128, 0));
        nhan.itemOptions.add(new ItemOption(128, 0));
        
        ao.itemOptions.add(new ItemOption(140, 300));
        quan.itemOptions.add(new ItemOption(140, 300));
        gang.itemOptions.add(new ItemOption(140, 300));
        giay.itemOptions.add(new ItemOption(140, 300));
        nhan.itemOptions.add(new ItemOption(140, 300));
        
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, nhan, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set Kích hoạt Kaioken");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setSongoku(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1319);
        Item ao = ItemService.gI().otpKH((short) 0);
        Item quan = ItemService.gI().otpKH((short) 6);
        Item gang = ItemService.gI().otpKH((short) 21);
        Item giay = ItemService.gI().otpKH((short) 27);
        Item rd = ItemService.gI().otpKH((short) 12);
        ao.itemOptions.add(new ItemOption(129, 0));//129
        quan.itemOptions.add(new ItemOption(129, 0));
        gang.itemOptions.add(new ItemOption(129, 0));
        giay.itemOptions.add(new ItemOption(129, 0));
        rd.itemOptions.add(new ItemOption(129, 0));
        
        ao.itemOptions.add(new ItemOption(141, 100));
        quan.itemOptions.add(new ItemOption(141, 100));
        gang.itemOptions.add(new ItemOption(141, 100));
        giay.itemOptions.add(new ItemOption(141, 100));
        rd.itemOptions.add(new ItemOption(141, 100));
        
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        rd.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, rd, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set Kích hoạt Kame");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setThenXinHang(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1319);
        Item ao = ItemService.gI().otpKH((short) 0);
        Item quan = ItemService.gI().otpKH((short) 6);
        Item gang = ItemService.gI().otpKH((short) 21);
        Item giay = ItemService.gI().otpKH((short) 27);
        Item rd = ItemService.gI().otpKH((short) 12);
        ao.itemOptions.add(new ItemOption(127, 0));
        quan.itemOptions.add(new ItemOption(127, 0));
        gang.itemOptions.add(new ItemOption(127, 0));
        giay.itemOptions.add(new ItemOption(127, 0));
        rd.itemOptions.add(new ItemOption(127, 0));
        
        ao.itemOptions.add(new ItemOption(139, 100));
        quan.itemOptions.add(new ItemOption(139, 100));
        gang.itemOptions.add(new ItemOption(139, 100));
        giay.itemOptions.add(new ItemOption(139, 100));
        rd.itemOptions.add(new ItemOption(139, 100));
        
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        rd.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, rd, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set Kích hoạt Thên Xin Hăng");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setKaioKen(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1319);
        Item ao = ItemService.gI().otpKH((short) 0);
        Item quan = ItemService.gI().otpKH((short) 6);
        Item gang = ItemService.gI().otpKH((short) 21);
        Item giay = ItemService.gI().otpKH((short) 27);
        Item rd = ItemService.gI().otpKH((short) 12);
        ao.itemOptions.add(new ItemOption(128, 0));
        quan.itemOptions.add(new ItemOption(128, 0));
        gang.itemOptions.add(new ItemOption(128, 0));
        giay.itemOptions.add(new ItemOption(128, 0));
        rd.itemOptions.add(new ItemOption(128, 0));
        
        ao.itemOptions.add(new ItemOption(140, 100));
        quan.itemOptions.add(new ItemOption(140, 100));
        gang.itemOptions.add(new ItemOption(140, 100));
        giay.itemOptions.add(new ItemOption(140, 100));
        rd.itemOptions.add(new ItemOption(140, 100));
        
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        rd.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, rd, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set Kích hoạt Kaioken");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setLienHoantl(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1321);
        Item ao = ItemService.gI().otptl((short) 557);
        Item quan = ItemService.gI().otptl((short) 558);
        Item gang = ItemService.gI().otptl((short) 564);
        Item giay = ItemService.gI().otptl((short) 565);
        Item nhan = ItemService.gI().otptl((short) 561);
        ao.itemOptions.add(new ItemOption(131, 0));
        quan.itemOptions.add(new ItemOption(131, 0));
        gang.itemOptions.add(new ItemOption(131, 0));
        giay.itemOptions.add(new ItemOption(131, 0));
        nhan.itemOptions.add(new ItemOption(131, 0));
        
        ao.itemOptions.add(new ItemOption(143, 100));
        quan.itemOptions.add(new ItemOption(143, 100));
        gang.itemOptions.add(new ItemOption(143, 100));
        giay.itemOptions.add(new ItemOption(143, 100));
        nhan.itemOptions.add(new ItemOption(143, 100));
        
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, nhan, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set Kích hoạt Liên hoàn");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setPicolotl(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1321);
        Item ao = ItemService.gI().otptl((short) 557);
        Item quan = ItemService.gI().otptl((short) 558);
        Item gang = ItemService.gI().otptl((short) 564);
        Item giay = ItemService.gI().otptl((short) 565);
        Item nhan = ItemService.gI().otptl((short) 561);
        ao.itemOptions.add(new ItemOption(130, 0));
        quan.itemOptions.add(new ItemOption(130, 0));
        gang.itemOptions.add(new ItemOption(130, 0));
        giay.itemOptions.add(new ItemOption(130, 0));
        nhan.itemOptions.add(new ItemOption(130, 0));

        ao.itemOptions.add(new ItemOption(142, 100));
        quan.itemOptions.add(new ItemOption(142, 100));
        gang.itemOptions.add(new ItemOption(142, 100));
        giay.itemOptions.add(new ItemOption(142, 100));
        nhan.itemOptions.add(new ItemOption(142, 100));
        
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, nhan, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set Kích hoạt Picolo");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setPikkoroDaimaotl(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1321);
        Item ao = ItemService.gI().otptl((short) 557);
        Item quan = ItemService.gI().otptl((short) 558);
        Item gang = ItemService.gI().otptl((short) 564);
        Item giay = ItemService.gI().otptl((short) 565);
        Item nhan = ItemService.gI().otptl((short) 561);
        ao.itemOptions.add(new ItemOption(132, 0));
        quan.itemOptions.add(new ItemOption(132, 0));
        gang.itemOptions.add(new ItemOption(132, 0));
        giay.itemOptions.add(new ItemOption(132, 0));
        nhan.itemOptions.add(new ItemOption(132, 0));

        ao.itemOptions.add(new ItemOption(144, 100));
        quan.itemOptions.add(new ItemOption(144, 100));
        gang.itemOptions.add(new ItemOption(144, 100));
        giay.itemOptions.add(new ItemOption(144, 100));
        nhan.itemOptions.add(new ItemOption(144, 100));
        
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, nhan, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set Kích hoạt Picolo");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setLienHoan(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1319);
        Item ao = ItemService.gI().otpKH((short) 1);
        Item quan = ItemService.gI().otpKH((short) 7);
        Item gang = ItemService.gI().otpKH((short) 22);
        Item giay = ItemService.gI().otpKH((short) 28);
        Item rd = ItemService.gI().otpKH((short) 12);
        ao.itemOptions.add(new ItemOption(131, 0));
        quan.itemOptions.add(new ItemOption(131, 0));
        gang.itemOptions.add(new ItemOption(131, 0));
        giay.itemOptions.add(new ItemOption(131, 0));
        rd.itemOptions.add(new ItemOption(131, 0));

        ao.itemOptions.add(new ItemOption(143, 100));
        quan.itemOptions.add(new ItemOption(143, 100));
        gang.itemOptions.add(new ItemOption(143, 100));
        giay.itemOptions.add(new ItemOption(143, 100));
        rd.itemOptions.add(new ItemOption(143, 100));
        
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        rd.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, rd, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set Kích hoạt Liên hoàn");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setPicolo(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1319);
        Item ao = ItemService.gI().otpKH((short) 1);
        Item quan = ItemService.gI().otpKH((short) 7);
        Item gang = ItemService.gI().otpKH((short) 22);
        Item giay = ItemService.gI().otpKH((short) 28);
        Item rd = ItemService.gI().otpKH((short) 12);
        ao.itemOptions.add(new ItemOption(130, 0));
        quan.itemOptions.add(new ItemOption(130, 0));
        gang.itemOptions.add(new ItemOption(130, 0));
        giay.itemOptions.add(new ItemOption(130, 0));
        rd.itemOptions.add(new ItemOption(130, 0));

        ao.itemOptions.add(new ItemOption(142, 100));
        quan.itemOptions.add(new ItemOption(142, 100));
        gang.itemOptions.add(new ItemOption(142, 100));
        giay.itemOptions.add(new ItemOption(142, 100));
        rd.itemOptions.add(new ItemOption(142, 100));
        
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        rd.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, rd, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set Kích hoạt Picolo");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setPikkoroDaimao(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1319);
        Item ao = ItemService.gI().otpKH((short) 1);
        Item quan = ItemService.gI().otpKH((short) 7);
        Item gang = ItemService.gI().otpKH((short) 22);
        Item giay = ItemService.gI().otpKH((short) 28);
        Item rd = ItemService.gI().otpKH((short) 12);
        ao.itemOptions.add(new ItemOption(132, 0));
        quan.itemOptions.add(new ItemOption(132, 0));
        gang.itemOptions.add(new ItemOption(132, 0));
        giay.itemOptions.add(new ItemOption(132, 0));
        rd.itemOptions.add(new ItemOption(132, 0));

        ao.itemOptions.add(new ItemOption(144, 100));
        quan.itemOptions.add(new ItemOption(144, 100));
        gang.itemOptions.add(new ItemOption(144, 100));
        giay.itemOptions.add(new ItemOption(144, 100));
        rd.itemOptions.add(new ItemOption(144, 100));
        
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        rd.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, rd, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set Kích hoạt Picolo");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setKakarottl(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1321);
        Item ao = ItemService.gI().otptl((short) 559);
        Item quan = ItemService.gI().otptl((short) 560);
        Item gang = ItemService.gI().otptl((short) 566);
        Item giay = ItemService.gI().otptl((short) 567);
        Item nhan = ItemService.gI().otptl((short) 561);
        ao.itemOptions.add(new ItemOption(133, 0));
        quan.itemOptions.add(new ItemOption(133, 0));
        gang.itemOptions.add(new ItemOption(133, 0));
        giay.itemOptions.add(new ItemOption(133, 0));
        nhan.itemOptions.add(new ItemOption(133, 0));

        ao.itemOptions.add(new ItemOption(136, 100));
        quan.itemOptions.add(new ItemOption(136, 100));
        gang.itemOptions.add(new ItemOption(136, 100));
        giay.itemOptions.add(new ItemOption(136, 100));
        nhan.itemOptions.add(new ItemOption(136, 100));
        
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, nhan, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set Kích hoạt Kakarot");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setCadictl(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1321);
        Item ao = ItemService.gI().otptl((short) 559);
        Item quan = ItemService.gI().otptl((short) 560);
        Item gang = ItemService.gI().otptl((short) 566);
        Item giay = ItemService.gI().otptl((short) 567);
        Item nhan = ItemService.gI().otptl((short) 561);
        ao.itemOptions.add(new ItemOption(134, 0));
        quan.itemOptions.add(new ItemOption(134, 0));
        gang.itemOptions.add(new ItemOption(134, 0));
        giay.itemOptions.add(new ItemOption(134, 0));
        nhan.itemOptions.add(new ItemOption(134, 0));

        ao.itemOptions.add(new ItemOption(137, 101));
        quan.itemOptions.add(new ItemOption(137, 101));
        gang.itemOptions.add(new ItemOption(137, 101));
        giay.itemOptions.add(new ItemOption(137, 101));
        nhan.itemOptions.add(new ItemOption(137, 101));
        
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, nhan, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set Kích hoạt Ca đíc");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setNappatl(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1321);
        Item ao = ItemService.gI().otptl((short) 559);
        Item quan = ItemService.gI().otptl((short) 560);
        Item gang = ItemService.gI().otptl((short) 566);
        Item giay = ItemService.gI().otptl((short) 567);
        Item nhan = ItemService.gI().otptl((short) 561);
        ao.itemOptions.add(new ItemOption(135, 0));
        quan.itemOptions.add(new ItemOption(135, 0));
        gang.itemOptions.add(new ItemOption(135, 0));
        giay.itemOptions.add(new ItemOption(135, 0));
        nhan.itemOptions.add(new ItemOption(135, 0));

        ao.itemOptions.add(new ItemOption(138, 80));
        quan.itemOptions.add(new ItemOption(138, 80));
        gang.itemOptions.add(new ItemOption(138, 80));
        giay.itemOptions.add(new ItemOption(138, 80));
        nhan.itemOptions.add(new ItemOption(138, 80));
        
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, nhan, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set Kích hoạt Nappa");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setKakarot(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1319);
        Item ao = ItemService.gI().otpKH((short) 2);
        Item quan = ItemService.gI().otpKH((short) 8);
        Item gang = ItemService.gI().otpKH((short) 23);
        Item giay = ItemService.gI().otpKH((short) 29);
        Item rd = ItemService.gI().otpKH((short) 12);
        ao.itemOptions.add(new ItemOption(133, 0));
        quan.itemOptions.add(new ItemOption(133, 0));
        gang.itemOptions.add(new ItemOption(133, 0));
        giay.itemOptions.add(new ItemOption(133, 0));
        rd.itemOptions.add(new ItemOption(133, 0));

        ao.itemOptions.add(new ItemOption(136, 100));
        quan.itemOptions.add(new ItemOption(136, 100));
        gang.itemOptions.add(new ItemOption(136, 100));
        giay.itemOptions.add(new ItemOption(136, 100));
        rd.itemOptions.add(new ItemOption(136, 100));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        rd.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, rd, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set Kích hoạt Kakarot");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setCadic(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1319);
        Item ao = ItemService.gI().otpKH((short) 2);
        Item quan = ItemService.gI().otpKH((short) 8);
        Item gang = ItemService.gI().otpKH((short) 23);
        Item giay = ItemService.gI().otpKH((short) 29);
        Item rd = ItemService.gI().otpKH((short) 12);
        ao.itemOptions.add(new ItemOption(134, 0));
        quan.itemOptions.add(new ItemOption(134, 0));
        gang.itemOptions.add(new ItemOption(134, 0));
        giay.itemOptions.add(new ItemOption(134, 0));
        rd.itemOptions.add(new ItemOption(134, 0));

        ao.itemOptions.add(new ItemOption(137, 1));
        quan.itemOptions.add(new ItemOption(137, 1));
        gang.itemOptions.add(new ItemOption(137, 1));
        giay.itemOptions.add(new ItemOption(137, 1));
        rd.itemOptions.add(new ItemOption(137, 1));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        rd.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, rd, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set Kích hoạt Ca đíc");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setNappa(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1319);
        Item ao = ItemService.gI().otpKH((short) 2);
        Item quan = ItemService.gI().otpKH((short) 8);
        Item gang = ItemService.gI().otpKH((short) 23);
        Item giay = ItemService.gI().otpKH((short) 29);
        Item rd = ItemService.gI().otpKH((short) 12);
        ao.itemOptions.add(new ItemOption(135, 0));
        quan.itemOptions.add(new ItemOption(135, 0));
        gang.itemOptions.add(new ItemOption(135, 0));
        giay.itemOptions.add(new ItemOption(135, 0));
        rd.itemOptions.add(new ItemOption(135, 0));

        ao.itemOptions.add(new ItemOption(138, 80));
        quan.itemOptions.add(new ItemOption(138, 80));
        gang.itemOptions.add(new ItemOption(138, 80));
        giay.itemOptions.add(new ItemOption(138, 80));
        rd.itemOptions.add(new ItemOption(138, 80));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        rd.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, rd, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set Kích hoạt Nappa");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }
    public Item DoThienSu(int itemId, int gender) {
        Item dots = createItemSetKichHoat(itemId, 1);
        List<Integer> ao = Arrays.asList(1048, 1049, 1050);
        List<Integer> quan = Arrays.asList(1051, 1052, 1053);
        List<Integer> gang = Arrays.asList(1054, 1055, 1056);
        List<Integer> giay = Arrays.asList(1057, 1058, 1059);
        List<Integer> nhan = Arrays.asList(1060, 1061, 1062);
        //áo
        if (ao.contains(itemId)) {
            dots.itemOptions.add(new ItemOption(47, Util.highlightsItem(gender == 2, new Random().nextInt(1201) + 2800))); // áo từ 2800-4000 giáp
        }
        //quần
        if (Util.isTrue(60, 100)) {
            if (quan.contains(itemId)) {
                dots.itemOptions.add(new ItemOption(22, Util.highlightsItem(gender == 0, new Random().nextInt(11) + 120))); // hp 120k-130k
            }
        } else {
            if (quan.contains(itemId)) {
                dots.itemOptions.add(new ItemOption(22, Util.highlightsItem(gender == 0, new Random().nextInt(51) + 130))); // hp 130-180k 15%
            }
        }
        //găng
        if (Util.isTrue(60, 100)) {
            if (gang.contains(itemId)) {
                dots.itemOptions.add(new ItemOption(0, Util.highlightsItem(gender == 2, new Random().nextInt(1650) + 11000))); // 11000-18600
            }
        } else {
            if (gang.contains(itemId)) {
                dots.itemOptions.add(new ItemOption(0, Util.highlightsItem(gender == 2, new Random().nextInt(1800) + 12000))); // gang 15% 12-19k -xayda 12k1
            }
        }
        //giày
        if (Util.isTrue(60, 100)) {
            if (giay.contains(itemId)) {
                dots.itemOptions.add(new ItemOption(23, Util.highlightsItem(gender == 1, new Random().nextInt(21) + 120))); // ki 90-110k
            }
        } else {
            if (giay.contains(itemId)) {
                dots.itemOptions.add(new ItemOption(23, Util.highlightsItem(gender == 1, new Random().nextInt(21) + 130))); // ki 110-130k
            }
        }

        if (nhan.contains(itemId)) {
            dots.itemOptions.add(new ItemOption(14, Util.highlightsItem(gender == 1, new Random().nextInt(3) + 18))); // nhẫn 18-20%
            
        }
        dots.itemOptions.add(new ItemOption(21, 100));
        dots.itemOptions.add(new ItemOption(30, 1));
        return dots;
    }

    public int getPercentTrainArmor(Item item) {
        if (item != null) {
            switch (item.template.id) {
                case 529:
                case 534:
                    return 10;
                case 530:
                case 535:
                    return 20;
                case 531:
                case 536:
                    return 30;
                case 1517:
                    return 40;    
                default:
                    return 0;
            }
        } else {
            return 0;
        }
    }

    public boolean isTrainArmor(Item item) {
        if (item != null) {
            switch (item.template.id) {
                case 529:
                case 534:
                case 530:
                case 535:
                case 531:
                case 536:
                case 1517:    
                    return true;
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    public boolean isOutOfDateTime(Item item) {
        long now = System.currentTimeMillis();
        if (item != null) {
            for (ItemOption io : item.itemOptions) {
                if (io.optionTemplate.id == 93) {
                    int dayPass = (int) TimeUtil.diffDate(new Date(), new Date(item.createTime), TimeUtil.DAY);
                    if (dayPass != 0) {
                        io.param -= dayPass;
                        if (io.param <= 0) {
                            return true;
                        } else {
                            item.createTime = System.currentTimeMillis();
                        }
                    }
                } else if (io.optionTemplate.id == 196) {
                    long e = io.param * 1000L;
                    if (e <= now) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public ConsignmentItem createNewConsignmentItem(short tempId, int quantity) {
        ConsignmentItem item = new ConsignmentItem();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.createTime = System.currentTimeMillis();
        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }
    public ConsignmentItem convertToConsignmentItem(Item item) {
        ConsignmentItem it = new ConsignmentItem();
        it.itemOptions = new ArrayList<>();
        it.template = item.template;
        it.info = item.info;
        it.content = item.content;
        it.quantity = item.quantity;
        it.createTime = item.createTime;
        for (ItemOption io : item.itemOptions) {
            it.itemOptions.add(new ItemOption(io));
        }
        it.setPriceGold(-1);
        it.setPriceGem(-1);
        return it;
    }
    
    
    public List<ItemOption> getListOptionItemShop(short id) {
        List<ItemOption> list = new ArrayList<>();
        Manager.SHOPS.forEach(shop -> shop.tabShops.forEach(tabShop -> tabShop.itemShops.forEach(itemShop -> {
            if (itemShop.temp.id == id && list.size() == 0) {
                list.addAll(itemShop.options);
            }
        })));
        return list;
    }
    public boolean isItemNoLimitQuantity(int id) {// item k giới hạn số lượng
        if (id >= 1066 && id <= 1070) {// mảnh trang bị thiên sứ
            return true;
        }
        return false;
    }
}
