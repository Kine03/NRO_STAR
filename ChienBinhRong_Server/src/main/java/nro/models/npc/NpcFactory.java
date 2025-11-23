package nro.models.npc;

import nro.services.func.minigame.ChonAiDay_Gem;
import nro.services.func.minigame.ChonAiDay_Ruby;
import nro.services.func.minigame.ChonAiDay_Gold;
import nro.attr.Attribute;
import nro.attr.AttributeManager;
import nro.consts.*;
import nro.dialog.ConfirmDialog;
import nro.dialog.MenuDialog;
import nro.jdbc.daos.PlayerDAO;
import nro.lib.RandomCollection;
import nro.models.boss.Boss;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.boss.event.EscortedBoss;
import nro.models.boss.event.Qilin;
import nro.models.clan.Clan;
import nro.models.clan.ClanMember;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.item.ItemTemplate;
import nro.models.map.ItemMap;
import nro.models.map.Map;
import nro.models.map.SantaCity;
import nro.models.map.Zone;
import nro.models.map.DaiHoiVoThuat.DHVT23Service;
import nro.models.map.dungeon.SnakeRoad;
import nro.models.map.dungeon.zones.ZSnakeRoad;
import nro.models.map.mabu.MabuWar;
import nro.models.map.phoban.DoanhTrai;
import nro.models.map.war.BlackBallWar;
import nro.models.map.war.NamekBallWar;
import nro.models.player.Inventory;
import nro.models.player.NPoint;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.noti.NotiManager;
import nro.server.Maintenance;
import nro.server.Manager;
import nro.server.ServerManager;
import nro.server.io.Message;
import nro.services.*;
import nro.services.func.*;
import nro.utils.Log;
import nro.utils.SkillUtil;
import nro.utils.TimeUtil;
import nro.utils.Util;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import nro.manager.SieuHangManager;
import nro.models.boss.BossData;
import nro.models.boss.Potaufeu.Boss_NhanBan;
import nro.models.boss.mabu_war.Goku_Tang5;
import nro.models.boss.mapoffline.Boss_MrPôPô;
import nro.models.boss.mapoffline.Boss_ThanMeo;
import nro.models.boss.mapoffline.Boss_ThuongDe;
import nro.models.boss.mapoffline.Boss_Whis;
import nro.models.boss.mapoffline.Boss_Yanjiro;
import nro.models.boss.mapoffline.NPC_ToSuKaio;
import static nro.models.item.ItemTime.TEXT_NHIEM_VU_HANG_NGAY;
import nro.models.kygui.ConsignmentShop;
import nro.models.map.DaiHoiVoThuat.DaiHoiVoThuatManager;
import nro.models.map.DaiHoiVoThuat.DaiHoiVoThuatService;
import nro.models.map.VoDaiSinhTu.VoDaiSinhTuService;
import nro.models.map.mabu.MabuWar14h;
import nro.models.map.phoban.BanDoKhoBau;
import nro.models.map.phoban.KhiGas;
import nro.models.npc.specialnpc.TrungLinhThu;
import nro.models.phuban.DragonNamecWar.TranhNgoc;
import nro.models.phuban.DragonNamecWar.TranhNgocService;
import nro.sendEff.SendEffect;
import nro.server.Controller;

import static nro.server.Manager.*;
import nro.server.TaiXiu;
import static nro.services.func.CombineServiceNew.CHE_TAO_DO_THIEN_SU;
import static nro.services.func.CombineServiceNew.NANG_CAP_BONG_TAI;
import static nro.services.func.CombineServiceNew.NANG_CAP_SKH;
import static nro.services.func.Input.ADD_ITEM;
import static nro.services.func.Input.NUMERIC;
import static nro.services.func.SummonDragon.*;

/**
 * @author 💖 Trần Lại 💖
 * @copyright 💖 GirlkuN 💖
 */
public class NpcFactory {

    private static boolean nhanVang = true;
    private static boolean nhanDeTu = true;

    // playerid - object
    public static final java.util.Map<Long, Object> PLAYERID_OBJECT = new HashMap<Long, Object>();

    private NpcFactory() {

    }

    public static Npc createNPC(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        Npc npc = null;
        try {
            switch (tempId) {
                case ConstNpc.xinbato:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOP_xinbato, 0, -1);
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOP_xinbato, 0, -1);
                            }
                        }
                    };
                    break;
                case ConstNpc.TORIBOT:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOP_TORIBOT, 0, -1);
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOP_TORIBOT, 0, -1);
                            }
                        }
                    };
                    break;

                case ConstNpc.TRUNG_THU:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            ShopService.gI().openShopSpecial(player, this, ConstNpc.TRUNGTHU, 0, -1);
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                ShopService.gI().openShopSpecial(player, this, ConstNpc.TRUNGTHU, 0, -1);
                            }
                        }
                    };
                    break;
                case ConstNpc.NGO_KHONG:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
//                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Chu mi nga", "Tặng quả\nHồng đào\nChín");
                            super.openBaseMenu(player);
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                int itemNeed = ConstItem.QUA_HONG_DAO_CHIN;
                                Item item = InventoryService.gI().findItemBagByTemp(player, itemNeed);
                                if (item != null) {
                                    RandomCollection<Integer> rc = Manager.HONG_DAO_CHIN;
                                    int itemID = rc.next();
                                    int x = cx + Util.nextInt(-50, 50);
                                    int y = player.zone.map.yPhysicInTop(x, cy - 24);
                                    int quantity = 1;
                                    if (itemID == ConstItem.HONG_NGOC) {
                                        quantity = Util.nextInt(1, 2);
                                    }

                                    InventoryService.gI().subQuantityItemsBag(player, item, 1);
                                    InventoryService.gI().sendItemBags(player);
                                    ItemMap itemMap = new ItemMap(player.zone, itemID, quantity, x, y, player.id);
                                    Service.getInstance().dropItemMap(player.zone, itemMap);
                                    npcChat(player.zone, "Xie xie");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Không tìm thấy!");
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.DUONG_TANG:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (this.mapId == MapName.LANG_ARU || this.mapId == 7 || this.mapId == 14 || this.mapId == 0) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "A mi phò phò, thí chủ hãy giúp giải cứu đồ đệ của bần tăng đang bị phong ấn tại ngũ hành sơn.",
                                        "Đồng ý", "Từ chối");
                            }
                            if (this.mapId == MapName.NGU_HANH_SON_3) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "A mi phò phò, thí chủ hãy thu thập bùa 'giải khai phong ấn', mỗi chữ 10 cái.",
                                        "Về\nLàng Aru", "Từ chối");
                            }
                            if (this.mapId == MapName.NGU_HANH_SON) {
//                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
//                                        "A mi phò phò, thí chủ hãy thu thập bùa 'giải khai phong ấn', mỗi chữ 10 cái.",
//                                        "Đổi đào chín", "Giải phong ấn", "Từ chối");
                                super.openBaseMenu(player);
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == MapName.LANG_ARU || this.mapId == 7 || this.mapId == 14 || this.mapId == 0) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                if (!Manager.gI().getGameConfig().isOpenPrisonPlanet()) {
                                                    Service.getInstance().sendThongBao(player,
                                                            "Lối vào ngũ hành sơn chưa mở");
                                                    return;
                                                }
                                                Zone zone = MapService.gI().getZoneJoinByMapIdAndZoneId(player, 124, 0);
                                                if (zone != null) {
                                                    player.location.x = 100;
                                                    player.location.y = 384;
                                                    MapService.gI().goToMap(player, zone);
                                                    Service.getInstance().clearMap(player);
                                                    zone.mapInfo(player);
                                                    player.zone.loadAnotherToMe(player);
                                                    player.zone.load_Me_To_Another(player);
                                                }
                                                // Service.getInstance().sendThongBao(player, "Lối vào ngũ hành sơn chưa
                                                // mở");
                                                break;
                                        }
                                    }
                                }
                                if (this.mapId == MapName.NGU_HANH_SON_3) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                Zone zone = MapService.gI().getZoneJoinByMapIdAndZoneId(player, 0, 0);
                                                if (zone != null) {
                                                    player.location.x = 454;
                                                    player.location.y = 432;
                                                    MapService.gI().goToMap(player, zone);
                                                    Service.getInstance().clearMap(player);
                                                    zone.mapInfo(player);
                                                    player.zone.loadAnotherToMe(player);
                                                    player.zone.load_Me_To_Another(player);
                                                }
                                                break;
                                        }
                                    }
                                }
                                if (this.mapId == MapName.NGU_HANH_SON) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                // Đổi đào
                                                Item item = InventoryService.gI().findItemBagByTemp(player,
                                                        ConstItem.QUA_HONG_DAO);
                                                if (item == null || item.quantity < 10) {
                                                    npcChat(player,
                                                            "Cần 10 quả đào xanh để đổi lấy đào chín từ bần tăng.");
                                                    return;
                                                }
                                                if (InventoryService.gI().getCountEmptyBag(player) == 0) {
                                                    npcChat(player, "Túi đầy rồi kìa.");
                                                    return;
                                                }
                                                Item newItem = ItemService.gI()
                                                        .createNewItem((short) ConstItem.QUA_HONG_DAO_CHIN, 1);
                                                InventoryService.gI().subQuantityItemsBag(player, item, 10);
                                                InventoryService.gI().addItemBag(player, newItem, 0);
                                                InventoryService.gI().sendItemBags(player);
                                                npcChat(player,
                                                        "Ta đã đổi cho thí chủ rồi đó, hãy mang cho đệ tử ta đi nào.");
                                                break;

                                            case 1:
                                                // giải phong ấn
                                                if (InventoryService.gI().getCountEmptyBag(player) == 0) {
                                                    npcChat(player, "Túi đầy rồi kìa.");
                                                    return;
                                                }
                                                int[] itemsNeed = {ConstItem.CHU_GIAI, ConstItem.CHU_KHAI,
                                                    ConstItem.CHU_PHONG, ConstItem.CHU_AN};
                                                List<Item> items = InventoryService.gI().getListItem(player, itemsNeed)
                                                        .stream().filter(i -> i.quantity >= 10)
                                                        .collect(Collectors.toList());
                                                boolean[] flags = new boolean[4];
                                                for (Item i : items) {
                                                    switch ((int) i.template.id) {
                                                        case ConstItem.CHU_GIAI:
                                                            flags[0] = true;
                                                            break;

                                                        case ConstItem.CHU_KHAI:
                                                            flags[1] = true;
                                                            break;

                                                        case ConstItem.CHU_PHONG:
                                                            flags[2] = true;
                                                            break;

                                                        case ConstItem.CHU_AN:
                                                            flags[3] = true;
                                                            break;
                                                    }
                                                }
                                                for (int i = 0; i < flags.length; i++) {
                                                    if (!flags[i]) {
                                                        ItemTemplate template = ItemService.gI()
                                                                .getTemplate(itemsNeed[i]);
                                                        npcChat("Thí chủ còn thiếu x10 " + template.name);
                                                        return;
                                                    }
                                                }

                                                for (Item i : items) {
                                                    InventoryService.gI().subQuantityItemsBag(player, i, 10);
                                                }

                                                RandomCollection<Integer> rc = new RandomCollection<>();
                                                rc.add(10, ConstItem.CAI_TRANG_TON_NGO_KHONG_DE_TU);
                                                rc.add(10, ConstItem.CAI_TRANG_BAT_GIOI_DE_TU);
                                                rc.add(50, ConstItem.GAY_NHU_Y);
                                                switch (player.gender) {
                                                    case ConstPlayer.TRAI_DAT:
                                                        rc.add(30, ConstItem.CAI_TRANG_TON_NGO_KHONG);
                                                        break;

                                                    case ConstPlayer.NAMEC:
                                                        rc.add(30, ConstItem.CAI_TRANG_TON_NGO_KHONG_545);
                                                        break;

                                                    case ConstPlayer.XAYDA:
                                                        rc.add(30, ConstItem.CAI_TRANG_TON_NGO_KHONG_546);
                                                        break;
                                                }
                                                int itemID = rc.next();
                                                Item nItem = ItemService.gI().createNewItem((short) itemID);
                                                boolean all = itemID == ConstItem.CAI_TRANG_TON_NGO_KHONG_DE_TU
                                                        || itemID == ConstItem.CAI_TRANG_BAT_GIOI_DE_TU
                                                        || itemID == ConstItem.CAI_TRANG_TON_NGO_KHONG
                                                        || itemID == ConstItem.CAI_TRANG_TON_NGO_KHONG_545
                                                        || itemID == ConstItem.CAI_TRANG_TON_NGO_KHONG_546;
                                                if (all) {
                                                    nItem.itemOptions.add(new ItemOption(50, Util.nextInt(20, 35)));
                                                    nItem.itemOptions.add(new ItemOption(77, Util.nextInt(20, 35)));
                                                    nItem.itemOptions.add(new ItemOption(103, Util.nextInt(20, 35)));
                                                    nItem.itemOptions.add(new ItemOption(94, Util.nextInt(5, 10)));
                                                    nItem.itemOptions.add(new ItemOption(100, Util.nextInt(10, 20)));
                                                    nItem.itemOptions.add(new ItemOption(101, Util.nextInt(10, 20)));
                                                }
                                                if (itemID == ConstItem.CAI_TRANG_TON_NGO_KHONG
                                                        || itemID == ConstItem.CAI_TRANG_TON_NGO_KHONG_545
                                                        || itemID == ConstItem.CAI_TRANG_TON_NGO_KHONG_546) {
                                                    nItem.itemOptions.add(new ItemOption(80, Util.nextInt(5, 15)));
                                                    nItem.itemOptions.add(new ItemOption(81, Util.nextInt(5, 15)));
                                                    nItem.itemOptions.add(new ItemOption(106, 0));
                                                } else if (itemID == ConstItem.CAI_TRANG_TON_NGO_KHONG_DE_TU
                                                        || itemID == ConstItem.CAI_TRANG_BAT_GIOI_DE_TU) {
                                                    nItem.itemOptions.add(new ItemOption(197, 0));
                                                }
                                                if (all) {
                                                    if (Util.isTrue(499, 500)) {
                                                        nItem.itemOptions.add(new ItemOption(93, Util.nextInt(3, 30)));
                                                    }
                                                } else if (itemID == ConstItem.GAY_NHU_Y) {
                                                    RandomCollection<Integer> rc2 = new RandomCollection<>();
                                                    rc2.add(60, 30);
                                                    rc2.add(30, 90);
                                                    rc2.add(10, 365);
                                                    nItem.itemOptions.add(new ItemOption(93, rc2.next()));
                                                }
                                                InventoryService.gI().addItemBag(player, nItem, 0);
                                                InventoryService.gI().sendItemBags(player);
                                                npcChat(player.zone,
                                                        "A mi phò phò, đa tạ thí chủ tương trợ, xin hãy nhận món quà mọn này, bần tăng sẽ niệm chú giải thoát cho Ngộ Không");
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.TAPION:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 19) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Ác quỷ truyền thuyết Hirudegarn\nđã thoát khỏi phong ấn ngàn năm\nHãy giúp tôi chế ngự nó",
                                            "OK", "Từ chối");
                                }
                                if (this.mapId == 126) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Tôi sẽ đưa bạn về", "OK",
                                            "Từ chối");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 19) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                SantaCity santaCity = (SantaCity) MapService.gI().getMapById(126);
                                                if (santaCity != null) {
//                                                    if (!santaCity.isOpened() || santaCity.isClosed()) {
//                                                        Service.getInstance().sendThongBao(player,
//                                                                "Hẹn gặp bạn lúc 22h mỗi ngày");
//                                                        return;
//                                                    }
                                                    santaCity.enter(player);
                                                } else {
                                                    Service.getInstance().sendThongBao(player, "Có lỗi xảy ra!");
                                                }
                                                break;
                                        }
                                    }
                                }
                                if (this.mapId == 126) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                SantaCity santaCity = (SantaCity) MapService.gI().getMapById(126);
                                                if (santaCity != null) {
                                                    santaCity.leave(player);
                                                } else {
                                                    Service.getInstance().sendThongBao(player, "Có lỗi xảy ra!");
                                                }
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;
                case 77:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 0 || this.mapId == 7 || this.mapId == 14 || this.mapId == 176) {
                                    if (Manager.gI().demTimeSuKien2() != 0) {
                                        this.createOtherMenu(player, ConstNpc.MENU_DUA_TOP,
                                                "|2|Sự kiện đua TOP Test chào mừng khai mở máy chủ Ngọc Rồng Star\n"
                                                + "diễn ra từ " + Manager.timeStartDuaTop + " đến " + Manager.timeEndDuaTop + "\n"
                                                + "Nhận thưởng vào " + Manager.timeEndNhanGiai + "\n"
                                                + "Giải thưởng khủng chưa từng có, xem chi tiết tại diễn đàn, fanpage\n"
                                                + "|7|Thời gian diễn ra: "
                                                + Manager.demTimeSuKien(), "Top\nSức mạnh", "Top\nĐại gia", "Top\nNhiệm Vụ", "Từ chối");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.MENU_DUA_TOP,
                                                "|2|Sự kiện đua TOP chào mừng khai mở máy chủ Ngọc Rồng Star\n"
                                                + "diễn ra từ " + Manager.timeStartDuaTop + " đến " + Manager.timeEndDuaTop + "\n"
                                                + "Nhận thưởng vào " + Manager.timeEndNhanGiai + "\n"
                                                + "Giải thưởng khủng chưa từng có, xem chi tiết tại diễn đàn, fanpage\n"
                                                + "|7|Thời gian diễn ra: "
                                                + Manager.demTimeSuKien(), "Top\nSức mạnh", "Top\nĐại gia", "Top\nNhiệm Vụ", "Từ chối");
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DUA_TOP) {
                                    if (Manager.gI().demTimeSuKien2() > 0) {
                                        switch (select) {
                                            case 0:
                                                Service.getInstance().showTopPower(player);
                                                break;
                                            case 1:
                                                Service.getInstance().showTopRichMan(player);
                                                break;
//                                            case 2:
//                                                Service.getInstance().showTopSD(player);
//                                                break;
//                                            case 3:
//                                                Service.getInstance().showTopHP(player);
//                                                break;
//                                            case 4:
//                                                Service.getInstance().showTopKI(player);
//                                                break;
                                            case 2:
                                                Service.getInstance().showTopTask(player);
                                                break;
                                        }
                                    } else {
                                        switch (select) {
                                            case 0:
                                                Service.getInstance().showTopPower(player);
                                                break;
                                            case 1:
                                                Service.getInstance().showTopRichMan(player);
                                                break;
//                                            case 2:
//                                                Service.getInstance().showTopSD(player);
//                                                break;
//                                            case 3:
//                                                Service.getInstance().showTopHP(player);
//                                                break;
//                                            case 4:
//                                                Service.getInstance().showTopKI(player);
//                                                break;
                                            case 2:
                                                Service.getInstance().showTopTask(player);
                                                break;
//                                            case 6:
//                                                ShopService.gI().openBoxItemReward(player);
//                                                break;
                                        }
                                    }

                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.DUA_HAU:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (player.LinhThuEgg != null) {
                                    player.LinhThuEgg.sendLinhThuEgg();
                                    if (player.LinhThuEgg.getSecondDone() != 0) {
                                        this.createOtherMenu(player, ConstNpc.NOT_NHAN_DUA, "Trứng linh thú đang tu luyện\n"
                                                + "Hãy sử dụng Hồn Linh Thú để giúp tu luyện nhanh hơn nhé\n"
                                                + "Cứ x100 Hồn Linh Thú và nước sẽ giảm đc 1 ngày tu luyện",
                                                "Chưa tu\n luyện xong", "Hỗ Trợ\n Tu Luyện", "Đóng");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.CAN_THU_HOACH_DUA, "Trứng linh thú đã tu luyện xong",
                                                "Nở Trứng", "Đóng");
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.NOT_NHAN_DUA -> {
                                        switch (select) {
                                            case 0 -> {
                                                Service.getInstance().sendThongBao(player, "Chưa tu luyện xong");
                                            }
                                            case 1 -> {
                                                this.createOtherMenu(player, ConstNpc.BON_PHAN, "Hãy giúp trứng tu luyện nhanh hơn",
                                                        "x100", "Đóng");
                                            }
                                        }
                                    }
                                    case ConstNpc.CAN_THU_HOACH_DUA -> {
                                        switch (select) {
                                            case 0 -> {
                                                player.LinhThuEgg.openEgg();
                                                int[] idpet = {2053, 2054, 2055, 2056, 2057, 2058, 2059};
                                                int[] option = {50, 77, 103, 5, 14, 5, 14};
                                                Item Pet = ItemService.gI().createNewItem((short) idpet[Util.nextInt(0, idpet.length - 1)]);
                                                Pet.itemOptions.add(new ItemOption((short) option[Util.nextInt(0, idpet.length - 1)], Util.nextInt(5, 12)));
                                                InventoryService.gI().addItemBag(player, Pet, 9999);
                                                Service.getInstance().sendThongBao(player, "Bạn đã nhận được linh thú");
                                                InventoryService.gI().sendItemBags(player);
                                                TrungLinhThu.createLinhThuEgg(player);
                                            }
                                        }
                                    }
                                    case ConstNpc.BON_PHAN -> {
                                        Item hlt = InventoryService.gI().findItemBag(player, 1317);
                                        switch (select) {
                                            case 0 -> {
                                                if (hlt != null && hlt.quantity >= 100) {
                                                    player.LinhThuEgg.timeDone -= 86400000L;
                                                    player.LinhThuEgg.sendLinhThuEgg();
                                                    InventoryService.gI().subQuantityItemsBag(player, hlt, 100);
                                                    InventoryService.gI().sendItemBags(player);
                                                    Service.getInstance().sendThongBao(player, "Bạn đã giúp tu luyện thành công");
                                                } else {
                                                    Service.getInstance().sendThongBao(player, "Bạn không đủ Hồn linh thú");
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    };
                case ConstNpc.MR_POPO:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 0 || this.mapId == 176) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Thượng đế vừa phát hiện 1 loại khí đang âm thầm\n"
                                            + "hủy diệt mọi mầm sống trên Trái Đất,\n"
                                            + "nó được gọi là Destron Gas.\n"
                                            + "Ta sẽ đưa các cậu đến nơi ấy, các cậu sẵn sàng chưa?",
                                            "Thông tin\nChi tiết", "Top 100\nBang hội",
                                            "Thành tích\nBang", "OK", "Từ chối");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 0 || this.mapId == 176) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:// Thông tin chi tiết
                                                NpcService.gI().createTutorial(player, avartar, "Chúng ta gặp rắc rối rồi\b"
                                                        + "Thượng Đế nói với tôi rằng có 1 loại khí\bgọi là Destron Gas, thứ này không thuộc về nơi đây\n"
                                                        + "Nó tích tụ trên Trái Đất\bvà nó sẽ hủy diệt mọi mô tế bào sống\b"
                                                        + "Có tất cả 4 địa điểm mà Thượng Đế bảo tôi nói với cậu\bCậu có thể đến kiểm tra...\n"
                                                        + "Đầu tiên là Thành phố Santa tọa lạc ở phía Tây nam của thủ đô ở Viễn Đông.\n"
                                                        + "Thứ hai là gần Kim Tự Tháp ở vùng Sa Mạc viễn tây của thủ đô phía Bắc\n"
                                                        + "Thứ ba Vùng Đất Băng Giá ở Phương Bắc xa xôi\n"
                                                        + "Thứ tư là Hành tinh Bóng Tối đang che phủ một phần địa cầu\bCậu đã hiểu rõ chưa?");
                                                break;
                                            case 1:// Top 100 bang hội
                                                Service.gI().showTopClanKhiGas(player);
                                                break;
                                            case 2:// Thành tích Bang
                                                Service.gI().showMyTopClanKhiGas(player);
                                                break;
                                            case 3: //OK
                                                if (player.clan != null) {
//                                                    if (player.clanMember.getNumDateFromJoinTimeToToday() < 2) {
//                                                        Service.getInstance().sendThongBao(player,
//                                                                "Phải tham gia bang hội ít nhất 2 ngày mới có thể tham gia!");
//                                                        return;
//                                                    }
                                                    if (player.clan.isLeader(player)) {
                                                        if (player.clan.khiGas != null) {
                                                            this.createOtherMenu(player, ConstNpc.MENU_OPENED_KGHD,
                                                                    "Bang hội của cậu đang tham gia Destron Gas cấp độ 110\n"
                                                                    + "cậu có muốn đi cùng họ không? (" + Util.convertSecondsToTime2((System.currentTimeMillis() - player.clan.khiGas.lastTimeOpen) / 1000) + ")", "Đồng ý", "Từ chối");
                                                        } else {
                                                            Input.gI().createFormChooseLevelKhiGas(player);
                                                        }
                                                    } else {
                                                        Service.getInstance().sendThongBao(player, "Chức năng chỉ dành cho bang chủ");
                                                    }
                                                }
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCEPT_GO_TO_KGHD) {
                                        switch (select) {
                                            case 0:
                                                KhiGasHuyDietService.gI().openKhiGas(player, Byte.parseByte(String.valueOf(PLAYERID_OBJECT.get(player.id))));
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_KGHD) {
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().goToKhiGas(player);
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    };

                    break;
                case ConstNpc.LY_TIEU_NUONG_1:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
//                            if (canOpenNpc(player)) {
                            createOtherMenu(player, ConstNpc.BASE_MENU, "Mini game.", "Chọn ai đây", "Con Số\nMay Mắn", "Đóng");
                            return;
//                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            String time = ((ChonAiDay_Gold.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
                            if (((ChonAiDay_Gold.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) < 0) {
                                ChonAiDay_Gold.gI().lastTimeEnd = System.currentTimeMillis() + 300000;
                            }
                            String time2 = ((ChonAiDay_Ruby.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
                            if (((ChonAiDay_Ruby.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) < 0) {
                                ChonAiDay_Ruby.gI().lastTimeEnd = System.currentTimeMillis() + 300000;
                            }
                            String time3 = ((ChonAiDay_Gem.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
                            if (((ChonAiDay_Gem.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) < 0) {
                                ChonAiDay_Gem.gI().lastTimeEnd = System.currentTimeMillis() + 300000;
                            }
//                            if (canOpenNpc(player)) {
//                                if (this.mapId == 5) {
                            if (player.iDMark.isBaseMenu()) {
                                switch (select) {

//                                    case 1: // Con số may mắn vàng
//                                        xửLýLựaChọnMiniGame_Gold(player);
//                                        break;
                                    case 1:
                                        xửLýLựaChọnMiniGame(player);
                                        break;
                                    case 0: // chọn ai đây
                                        createOtherMenu(player, ConstNpc.CHON_AI_DAY, "Trò chơi Chọn Ai Đây đang được diễn ra, nếu bạn tin tưởng mình đang tràn đầy "
                                                + "may mắn thì có thể tham gia thử", "Thể lệ", "Chọn\nVàng", "Chọn\nhồng ngọc", "Chọn\nngọc xanh");
                                        break;
                                }
                            } else if (player.iDMark.getIndexMenu() == ConstNpc.CON_SO_MAY_MAN_NGOC_XANH) {
                                xửLýConSoMayManNgocXanh(player, select);
                            } else if (player.iDMark.getIndexMenu() == ConstNpc.CON_SO_MAY_MAN_VANG) {
                                xửLýConSoMayManVang(player, select);
                            } else if (player.iDMark.getIndexMenu() == ConstNpc.CHON_AI_DAY) {
                                xửLýChonAiDay(player, select, time);
                            } else if (player.iDMark.getIndexMenu() == ConstNpc.CHON_AI_DAY_VANG) {
                                xửLýChonAiDayVang(player, select, time);
                            } else if (player.iDMark.getIndexMenu() == ConstNpc.CHON_AI_DAY_HONG_NGOC) {
                                xửLýChonAiDayRuby(player, select, time2);
                            } else if (player.iDMark.getIndexMenu() == ConstNpc.CHON_AI_DAY_NGOC) {
                                xửLýChonAiDayGem(player, select, time3);
                            } else if (player.iDMark.getIndexMenu() == ConstNpc.UPDATE_CHON_AI_DAY_NGOC) {
                                switch (select) {
                                    case 0:
                                        createOtherMenu(player, ConstNpc.UPDATE_CHON_AI_DAY_NGOC, "Thời gian từ 8h đến hết 21h59 hằng ngày\n"
                                                + "Mỗi lượt được chọn 10 con số từ 0 đến 99\n"
                                                + "Thời gian mỗi lượt là 5 phút", "Cập nhật", "Đóng");
                                        break;
                                }
                            }
                        }
//                            }
//                        }

                        // Thêm các phương thức mới để xử lý logic cho mỗi trường hợp
                        private void xửLýLựaChọnMiniGame(Player player) {
                            LocalTime thoi_gian_hien_tai = LocalTime.now();
                            int gio = thoi_gian_hien_tai.getHour();
                            int phut = thoi_gian_hien_tai.getMinute();
                            String plWin = MiniGame.gI().MiniGame_S1.result_name;
                            String KQ = MiniGame.gI().MiniGame_S1.result + "";
                            String Money = MiniGame.gI().MiniGame_S1.money + "";
                            String count = MiniGame.gI().MiniGame_S1.players.size() + "";
                            String second = MiniGame.gI().MiniGame_S1.second + "";
                            String number = MiniGame.gI().MiniGame_S1.strNumber((int) player.id);
                            StringBuilder previousResults = new StringBuilder("");
                            if (MiniGame.gI().MiniGame_S1.dataKQ_CSMM != null && !MiniGame.gI().MiniGame_S1.dataKQ_CSMM.isEmpty()) {
                                int maxResultsToShow = Math.min(10, MiniGame.gI().MiniGame_S1.dataKQ_CSMM.size());
                                for (int i = MiniGame.gI().MiniGame_S1.dataKQ_CSMM.size() - maxResultsToShow; i < MiniGame.gI().MiniGame_S1.dataKQ_CSMM.size(); i++) {
                                    previousResults.append(MiniGame.gI().MiniGame_S1.dataKQ_CSMM.get(i));
                                    if (i < MiniGame.gI().MiniGame_S1.dataKQ_CSMM.size() - 1) {
                                        previousResults.append(",");
                                    }
                                }
                            }

                            String npcSay = ""
                                    + "Kết quả giải trước: " + KQ + "\n"
                                    + (previousResults.toString() != "" ? previousResults.toString() + "\n" : "")
                                    + "Tổng giải thưởng: " + Money + " thỏi vàng\n"
                                    + "<" + second + ">giây\n"
                                    + (number != "" ? "Các số bạn chọn: " + number : "");
                            String[] Menus = {
                                "Cập nhật",
                                "1 Số\n 1 thỏi vàng",
                                "Ngẫu nhiên\n1 số lẻ\n 1 thỏi vàng",
                                "Ngẫu nhiên\n1 số chẵn\n 1 thỏi vàng",
                                "Hướng\ndẫn\nthêm",
                                "Đóng"
                            };
                            createOtherMenu(player, ConstNpc.CON_SO_MAY_MAN_NGOC_XANH, npcSay, Menus);
                            return;
                        }

                        // Thêm các phương thức mới để xử lý logic cho mỗi trường hợp
                        private void xửLýLựaChọnMiniGame_Gold(Player player) {
                            LocalTime thoi_gian_hien_tai = LocalTime.now();
                            int gio = thoi_gian_hien_tai.getHour();
                            int phut = thoi_gian_hien_tai.getMinute();
                            String plWin = MiniGame.gI().MiniGame_S1.result_name;
                            String KQ = MiniGame.gI().MiniGame_S1.result + "";
                            String Money = Util.mumberToLouis(MiniGame.gI().MiniGame_S1.gold) + "";
                            String count = MiniGame.gI().MiniGame_S1.players.size() + "";
                            String second = MiniGame.gI().MiniGame_S1.second + "";
                            String number = MiniGame.gI().MiniGame_S1.strNumber((int) player.id);
                            StringBuilder previousResults = new StringBuilder("");
                            if (MiniGame.gI().MiniGame_S1.dataKQ_CSMM != null && !MiniGame.gI().MiniGame_S1.dataKQ_CSMM.isEmpty()) {
                                int maxResultsToShow = Math.min(10, MiniGame.gI().MiniGame_S1.dataKQ_CSMM.size());
                                for (int i = MiniGame.gI().MiniGame_S1.dataKQ_CSMM.size() - maxResultsToShow; i < MiniGame.gI().MiniGame_S1.dataKQ_CSMM.size(); i++) {
                                    previousResults.append(MiniGame.gI().MiniGame_S1.dataKQ_CSMM.get(i));
                                    if (i < MiniGame.gI().MiniGame_S1.dataKQ_CSMM.size() - 1) {
                                        previousResults.append(",");
                                    }
                                }
                            }

                            String npcSay = ""
                                    + "Kết quả giải trước: " + KQ + "\n"
                                    + (previousResults.toString() != "" ? previousResults.toString() + "\n" : "")
                                    + "Tổng giải thưởng: " + Money + " thỏi vàng\n"
                                    + "<" + second + ">giây\n"
                                    + (number != "" ? "Các số bạn chọn: " + number : "");
                            String[] Menus = {
                                "Cập nhật",
                                "1 Số\n 1 thỏi vàng",
                                "Ngẫu nhiên\n1 số lẻ\n 1 thỏi vàng",
                                "Ngẫu nhiên\n1 số chẵn\n 1 thỏi vàng",
                                "Hướng\ndẫn\nthêm",
                                "Đóng"
                            };
                            createOtherMenu(player, ConstNpc.CON_SO_MAY_MAN_VANG, npcSay, Menus);
                            return;
                        }

                        private void xửLýConSoMayManNgocXanh(Player player, int select) {
                            switch (select) {
                                case 0: // cập nhật
                                    xửLýLựaChọnMiniGame(player);
                                    break;
                                case 1: // chọn 1 số
                                    Input.gI().createFormConSoMayMan_Gem(player);
                                    break;
                                case 2: // chọn 1 số lẻ
                                    MiniGame.gI().MiniGame_S1.ramdom1SoLe(player, 1);
                                    break;
                                case 3: // chọn 1 số chẵn
                                    MiniGame.gI().MiniGame_S1.ramdom1SoChan(player, 1);
                                    break;
                                case 4:
                                    createOtherMenu(player, 1, "Thời gian từ 8h đến hết 21h59 hằng ngày\n"
                                            + "Mỗi lượt được chọn 10 con số từ 0 đến 99\n"
                                            + "Thời gian mỗi lượt là 5 phút.", "Đồng ý");
                                    break;
                            }
                        }

                        private void xửLýConSoMayManVang(Player player, int select) {
                            switch (select) {
                                case 0: // cập nhật
                                    xửLýLựaChọnMiniGame_Gold(player);
                                    break;
                                case 1: // chọn 1 số
                                    Input.gI().createFormConSoMayMan_Gold(player);
                                    break;
                                case 2: // chọn 1 số lẻ
                                    MiniGame.gI().MiniGame_S1.ramdom1SoLe(player, 0);
                                    break;
                                case 3: // chọn 1 số chẵn
                                    MiniGame.gI().MiniGame_S1.ramdom1SoChan(player, 0);
                                    break;
                                case 4:
                                    createOtherMenu(player, 1, "Thời gian từ 8h đến hết 21h59 hằng ngày\n"
                                            + "Mỗi lượt được chọn 10 con số từ 0 đến 99\n"
                                            + "Thời gian mỗi lượt là 5 phút.", "Đồng ý");
                                    break;
                            }
                        }

                        private void xửLýChonAiDay(Player player, int select, String time) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(player, ConstNpc.IGNORE_MENU, "Mỗi lượt chơi có 6 giải thưởng\n"
                                            + "Được chọn tối đa 10 lần mỗi giải\n"
                                            + "Thời gian 1 lượt chọn là 5 phút\n"
                                            + "Khi hết giờ, hệ thống sẽ ngẫu nhiên chọn ra 1 người may mắn\n"
                                            + "của từng giải và trao thưởng.\n"
                                            + "Lưu ý: Nếu tham gia bằng Ngọc Xanh hoặc Hồng ngọc thì người thắng sẽ nhận thưởng là hồng ngọc.", "OK");
                                    break;
                                case 1:
                                    createOtherMenu(player, ConstNpc.CHON_AI_DAY_VANG, "Tổng giải thường: " + Util.numberToMoney(ChonAiDay_Gold.gI().goldNormar) + " vàng, cơ hội trúng của bạn là: " + player.percentGold(0) + "%\n"
                                            + "Tổng giải VIP: " + Util.numberToMoney(ChonAiDay_Gold.gI().goldVip) + " vàng, cơ hội trúng của bạn là: " + player.percentGold(1) + "%\n"
                                            + "Thời gian còn lại: " + time, "Cập nhập", "Thường\n1 triệu\nvàng", "VIP\n10 triệu\nvàng", "Đóng");
                                    break;
                                case 2:
                                    createOtherMenu(player, ConstNpc.CHON_AI_DAY_HONG_NGOC, "Tổng giải thường: " + Util.numberToMoney(ChonAiDay_Ruby.gI().rubyNormar) + " hồng ngọc, cơ hội trúng của bạn là: " + player.percentRuby(0) + "%\nTổng giải VIP: " + Util.numberToMoney(ChonAiDay_Ruby.gI().rubyVip) + " hồng ngọc, cơ hội trúng của bạn là: " + player.percentRuby(1) + "%\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n10 hồng\nngọc", "VIP\n100 hồng\nngọc", "Đóng");
                                    break;
                                case 3:
                                    createOtherMenu(player, ConstNpc.CHON_AI_DAY_NGOC, "Tổng giải thường: " + Util.numberToMoney(ChonAiDay_Gem.gI().gemNormar) + " hồng ngọc, cơ hội trúng của bạn là: " + player.percentGem(0) + "%\nTổng giải VIP: " + Util.numberToMoney(ChonAiDay_Gem.gI().gemVip) + " hồng ngọc, cơ hội trúng của bạn là: " + player.percentGem(1) + "%\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n10 ngọc\nxanh", "VIP\n100 ngọc\nxanh", "Đóng");
                                    break;
                            }
                        }

                        private void xửLýChonAiDayVang(Player player, int select, String time) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(player, ConstNpc.CHON_AI_DAY_VANG, "Tổng giải thường: " + Util.numberToMoney(ChonAiDay_Gold.gI().goldNormar) + " vàng, cơ hội trúng của bạn là: " + player.percentGold(0) + "%\nTổng giải VIP: " + Util.numberToMoney(ChonAiDay_Gold.gI().goldVip) + " vàng, cơ hội trúng của bạn là: " + player.percentGold(1) + "%\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n1 triệu\nvàng", "VIP\n10 triệu\nvàng", "Đóng");
                                    break;
                                case 1:
                                    xửLýThuong1TrieuVang(player);
                                    break;
                                case 2:
                                    xửLýVIP10TrieuVang(player);
                                    break;
                            }
                        }

                        private void xửLýChonAiDayRuby(Player player, int select, String time) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(player, ConstNpc.CHON_AI_DAY_HONG_NGOC, "Tổng giải thường: " + Util.numberToMoney(ChonAiDay_Ruby.gI().rubyNormar) + " hồng ngọc, cơ hội trúng của bạn là: " + player.percentRuby(0) + "%\nTổng giải VIP: " + Util.numberToMoney(ChonAiDay_Ruby.gI().rubyVip) + " hồng ngọc, cơ hội trúng của bạn là: " + player.percentRuby(1) + "%\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n10 hồng\nngọc", "VIP\n100 hồng\nngọc", "Đóng");
                                    break;
                                case 1:
                                    xửLýThuong10HongNgoc(player);
                                    break;
                                case 2:
                                    xửLýVIP100HongNgoc(player);
                                    break;
                            }
                        }

                        private void xửLýChonAiDayGem(Player player, int select, String time) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(player, ConstNpc.CHON_AI_DAY_NGOC, "Tổng giải thường: " + Util.numberToMoney(ChonAiDay_Gem.gI().gemNormar) + " hồng ngọc, cơ hội trúng của bạn là: " + player.percentGem(0) + "%\nTổng giải VIP: " + Util.numberToMoney(ChonAiDay_Gem.gI().gemVip) + " hồng ngọc, cơ hội trúng của bạn là: " + player.percentGem(1) + "%\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n10 ngọc\nxanh", "VIP\n100 ngọc\nxanh", "Đóng");
                                    break;
                                case 1:
                                    xửLýThuong10NgocXanh(player);
                                    break;
                                case 2:
                                    xửLýVIP100NgocXanh(player);
                                    break;
                            }
                        }

                        // Thêm các phương thức mới để xử lý logic cho mỗi trường hợp
                        private void xửLýThuong1TrieuVang(Player player) {
                            try {
                                String time = ((ChonAiDay_Gold.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
                                if (((ChonAiDay_Gold.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) < 0) {
                                    ChonAiDay_Gold.gI().lastTimeEnd = System.currentTimeMillis() + 300000;
                                }
                                if (player.inventory.gold >= 1_000_000) {
                                    player.inventory.gold -= 1_000_000;
                                    Service.gI().sendMoney(player);
                                    player.goldNormar += 1_000_000;
                                    ChonAiDay_Gold.gI().goldNormar += 1_000_000;
                                    ChonAiDay_Gold.gI().addPlayerNormar(player);
                                    createOtherMenu(player, ConstNpc.CHON_AI_DAY_VANG, "Tổng giải thường: " + Util.numberToMoney(ChonAiDay_Gold.gI().goldNormar) + " vàng, cơ hội trúng của bạn là: " + player.percentGold(0) + "%\nTổng giải VIP: " + Util.numberToMoney(ChonAiDay_Gold.gI().goldVip) + " vàng, cơ hội trúng của bạn là: " + player.percentGold(1) + "%\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n1 triệu\nvàng", "VIP\n10 triệu\nvàng", "Đóng");
                                } else {
                                    Service.gI().sendThongBao(player, "Bạn không đủ vàng");
                                }
                            } catch (Exception ex) {
                                System.out.println("Lỗi CHON_AI_DAY_VANG");
                            }
                        }

                        private void xửLýVIP10TrieuVang(Player player) {
                            try {
                                String time = ((ChonAiDay_Gold.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
                                if (((ChonAiDay_Gold.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) < 0) {
                                    ChonAiDay_Gold.gI().lastTimeEnd = System.currentTimeMillis() + 300000;
                                }
                                if (player.inventory.gold >= 10_000_000) {
                                    player.inventory.gold -= 10_000_000;
                                    Service.gI().sendMoney(player);
                                    player.goldVIP += 10_000_000;
                                    ChonAiDay_Gold.gI().goldVip += 10_000_000;
                                    ChonAiDay_Gold.gI().addPlayerVIP(player);
                                    createOtherMenu(player, ConstNpc.CHON_AI_DAY_VANG, "Tổng giải thường: " + Util.numberToMoney(ChonAiDay_Gold.gI().goldNormar) + " vàng, cơ hội trúng của bạn là: " + player.percentGold(0) + "%\nTổng giải VIP: " + Util.numberToMoney(ChonAiDay_Gold.gI().goldVip) + " vàng, cơ hội trúng của bạn là: " + player.percentGold(1) + "%\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n1 triệu\nvàng", "VIP\n10 triệu\nvàng", "Đóng");
                                } else {
                                    Service.gI().sendThongBao(player, "Bạn không đủ vàng");
                                }
                            } catch (Exception ex) {
                                System.out.println("Lỗi CHON_AI_DAY_VANG VIP");
                            }
                        }

                        // Thêm các phương thức mới để xử lý logic cho mỗi trường hợp
                        private void xửLýThuong10HongNgoc(Player player) {
                            try {
                                String time = ((ChonAiDay_Ruby.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
                                if (((ChonAiDay_Ruby.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) < 0) {
                                    ChonAiDay_Ruby.gI().lastTimeEnd = System.currentTimeMillis() + 300000;
                                }
                                if (player.inventory.ruby >= 10) {
                                    player.inventory.ruby -= 10;
                                    Service.gI().sendMoney(player);
                                    player.rubyNormar += 10;
                                    ChonAiDay_Ruby.gI().rubyNormar += 10;
                                    ChonAiDay_Ruby.gI().addPlayerNormar(player);
                                    createOtherMenu(player, ConstNpc.CHON_AI_DAY_HONG_NGOC, "Tổng giải thường: " + Util.numberToMoney(ChonAiDay_Ruby.gI().rubyNormar) + " hồng ngọc, cơ hội trúng của bạn là: " + player.percentRuby(0) + "%\nTổng giải VIP: " + Util.numberToMoney(ChonAiDay_Ruby.gI().rubyVip) + " hồng ngọc, cơ hội trúng của bạn là: " + player.percentRuby(1) + "%\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n10 hồng\nngọc", "VIP\n100 hồng\nngọc", "Đóng");
                                } else {
                                    Service.gI().sendThongBao(player, "Bạn không đủ hồng ngọc");
                                }
                            } catch (Exception ex) {
                                System.out.println("Lỗi CHON_AI_DAY_HONG_NGOC");
                            }
                        }

                        private void xửLýVIP100HongNgoc(Player player) {
                            try {
                                String time = ((ChonAiDay_Ruby.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
                                if (((ChonAiDay_Ruby.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) < 0) {
                                    ChonAiDay_Ruby.gI().lastTimeEnd = System.currentTimeMillis() + 300000;
                                }
                                if (player.inventory.ruby >= 100) {
                                    player.inventory.ruby -= 100;
                                    Service.gI().sendMoney(player);
                                    player.rubyVIP += 100;
                                    ChonAiDay_Ruby.gI().rubyVip += 100;
                                    ChonAiDay_Ruby.gI().addPlayerVIP(player);
                                    createOtherMenu(player, ConstNpc.CHON_AI_DAY_HONG_NGOC, "Tổng giải thường: " + Util.numberToMoney(ChonAiDay_Ruby.gI().rubyNormar) + " hồng ngọc, cơ hội trúng của bạn là: " + player.percentRuby(0) + "%\nTổng giải VIP: " + Util.numberToMoney(ChonAiDay_Ruby.gI().rubyVip) + " hồng ngọc, cơ hội trúng của bạn là: " + player.percentRuby(1) + "%\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n10 hồng\nngọc", "VIP\n100 hồng\nngọc", "Đóng");
                                } else {
                                    Service.gI().sendThongBao(player, "Bạn không đủ hồng ngọc");
                                }
                            } catch (Exception ex) {
                                System.out.println("Lỗi CHON_AI_DAY_HONG_NGOC VIP");
                            }
                        }

                        // Thêm các phương thức mới để xử lý logic cho mỗi trường hợp
                        private void xửLýThuong10NgocXanh(Player player) {
                            try {
                                String time = ((ChonAiDay_Gem.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
                                if (((ChonAiDay_Gem.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) < 0) {
                                    ChonAiDay_Gem.gI().lastTimeEnd = System.currentTimeMillis() + 300000;
                                }
                                if (player.inventory.gem >= 10) {
                                    player.inventory.gem -= 10;
                                    Service.gI().sendMoney(player);
                                    player.gemNormar += 10;
                                    ChonAiDay_Gem.gI().gemNormar += 10;
                                    ChonAiDay_Gem.gI().addPlayerNormar(player);
                                    createOtherMenu(player, ConstNpc.CHON_AI_DAY_NGOC, "Tổng giải thường: " + Util.numberToMoney(ChonAiDay_Gem.gI().gemNormar) + " hồng ngọc, cơ hội trúng của bạn là: " + player.percentGem(0) + "%\nTổng giải VIP: " + Util.numberToMoney(ChonAiDay_Gem.gI().gemVip) + " hồng ngọc, cơ hội trúng của bạn là: " + player.percentGem(1) + "%\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n10 ngọc\nxanh", "VIP\n100 ngọc\nxanh", "Đóng");
                                } else {
                                    Service.gI().sendThongBao(player, "Bạn không đủ ngọc xanh");
                                }
                            } catch (Exception ex) {
                                System.out.println("Lỗi CHON_AI_DAY_NGOC_XANH");
                            }
                        }

                        private void xửLýVIP100NgocXanh(Player player) {
                            try {
                                String time = ((ChonAiDay_Gem.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
                                if (((ChonAiDay_Gem.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) < 0) {
                                    ChonAiDay_Gem.gI().lastTimeEnd = System.currentTimeMillis() + 300000;
                                }
                                if (player.inventory.gem >= 100) {
                                    player.inventory.gem -= 100;
                                    Service.gI().sendMoney(player);
                                    player.gemVIP += 100;
                                    ChonAiDay_Gem.gI().gemVip += 100;
                                    ChonAiDay_Gem.gI().addPlayerVIP(player);
                                    createOtherMenu(player, ConstNpc.CHON_AI_DAY_NGOC, "Tổng giải thường: " + Util.numberToMoney(ChonAiDay_Gem.gI().gemNormar) + " hồng ngọc, cơ hội trúng của bạn là: " + player.percentGem(0) + "%\nTổng giải VIP: " + Util.numberToMoney(ChonAiDay_Gem.gI().gemVip) + " hồng ngọc, cơ hội trúng của bạn là: " + player.percentGem(1) + "%\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n10 ngọc\nxanh", "VIP\n100 ngọc\nxanh", "Đóng");
                                } else {
                                    Service.gI().sendThongBao(player, "Bạn không đủ ngọc xanh");
                                }
                            } catch (Exception ex) {
                                System.out.println("Lỗi CHON_AI_DAY_NGOC_XANH VIP");
                            }
                        }
                    };
                    break;

                case ConstNpc.QUY_LAO_KAME:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Con muốn hỏi gì nào?", "Nói\nchuyện", "Đổi điểm\nDanh hiệu");
                                }
                            }
                            return;
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0: // Nói chuyện
                                            if (player.clan != null) {
                                                if (player.clan.isLeader(player)) {
                                                    this.createOtherMenu(player, ConstNpc.MENU_NOI_CHUYEN,
                                                            "Chào con, ta rất vui khi gặp con\n"
                                                            + "Con muốn làm gì nào ?\n",
                                                            "Nhiệm vụ\n", "Học\nKỹ năng\n", "Về khu\nvực bang\n", "Giải tán\nBang hội",
                                                            "Kho báu\ndưới biển");
                                                } else {
                                                    this.createOtherMenu(player, ConstNpc.MENU_NOI_CHUYEN,
                                                            "Chào con, ta rất vui khi gặp con\n"
                                                            + "Con muốn làm gì nào ?\n",
                                                            "Nhiệm vụ\n", "Học\nKỹ năng\n", "Về khu\nvực bang\n",
                                                            "Kho báu\ndưới biển");
                                                }
                                            } else {
                                                this.createOtherMenu(player, ConstNpc.MENU_NOI_CHUYEN,
                                                        "Chào con, ta rất vui khi gặp con\n"
                                                        + "Con muốn làm gì nào ?\n",
                                                        "Nhiệm vụ\n", "Học\nKỹ năng\n");
                                            }
                                            break;
                                        case 1: //đổi danh hiệu sự kiện
                                            this.createOtherMenu(player, ConstNpc.MENU_OPENED_SUKIEN_DOIDIEM,
                                                    "Ta có tổng cộng 8 danh hiệu cho con\n"
                                                    + "1: Cao thủ siêu hạng: SĐ: 9% HP: 8% KI: 8% HSD 3-7 Ngày\n"
                                                    + "2: Thánh đập đồ 7+: SĐ: 8% HP: 9% KI: 9% HSD 3-7 Ngày\n"
                                                    + "3: Nông dân chăm chỉ: SĐ: 10% HP: 7% KI: 7% HSD 3-7 Ngày\n"
                                                    + "4: Trùm săn boss: SĐ: 7% HP: 10% KI: 10% HSD 3-7 Ngày\n"
                                                    + "5: Ông thần ve chai: SĐ: 11% HP: 6% KI: 6% HSD 3-7 Ngày\n"
                                                    + "6: Trùm ước rồng: SĐ: 6% HP: 11% KI: 11% HSD 3-7 Ngày\n"
                                                    + "7: Bị móc sạch túi: SĐ: 12% HP: 5% KI: 5% HSD 3-7 Ngày\n"
                                                    + "8: Đại gia mới nhú: SĐ: 5% HP: 12% KI: 12% HSD 3-7 Ngày\n"
                                                    + "Điểm sự kiện hiện tại của con là: " + player.diem_skien + " điểm\n"
                                                    + "Con muốn đổi cái nào ?\n",
                                                    "Danh hiệu 1\n50 điểm", "Danh hiệu 2\n50 điểm",
                                                    "Danh hiệu 3\n100 điểm", "Danh hiệu 4\n100 điểm",
                                                    "Danh hiệu 5\n150 điểm", "Danh hiệu 6\n150 điểm",
                                                    "Danh hiệu 7\n200 điểm", "Danh hiệu 8\n200 điểm");
                                            break;
//                                        case 1:
//                                            if (player.inventory.gold >= 100_000_000) {
//                                                Skill skill;
//                                                for (int i = 0; i < player.playerSkill.skills.size(); i++) {
//                                                    skill = player.playerSkill.skills.get(i);
//                                                    skill.lastTimeUseThisSkill = System.currentTimeMillis() - (long) skill.coolDown;
//                                                }
//                                                Service.getInstance().sendTimeSkill(player);
//                                                player.inventory.gold -= 100_000_000;
//                                                Service.getInstance().sendMoney(player);
//                                            } else {
//                                                Service.getInstance().sendThongBao(player, "Bạn không đủ vàng");
//                                                return;
//                                            }
//                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_SUKIEN_DOIDIEM) {
                                    switch (select) {
                                        case 0: {
                                            if (InventoryService.gI().getCountEmptyBag(player) == 0) {
                                                this.npcChat(player,
                                                        "Con phải có ít nhất 1 ô trống trong hành trang ta mới đưa cho con được");
                                                return;
                                            }
                                            if (player.diem_skien >= 50) {
                                                Item itemCaoThu = ItemService.gI()
                                                        .createNewItem((short) ConstItem.CAO_THU_SIEU_HANG);
                                                itemCaoThu.itemOptions.add(new ItemOption(50, 9));
                                                itemCaoThu.itemOptions.add(new ItemOption(77, 8));
                                                itemCaoThu.itemOptions.add(new ItemOption(103, 8));
                                                itemCaoThu.itemOptions.add(new ItemOption(93, Util.nextInt(3, 7)));

                                                InventoryService.gI().sendItemBags(player);
                                                itemCaoThu.quantity = 1;
                                                player.diem_skien -= 50;
                                                InventoryService.gI().addItemBag(player, itemCaoThu, 0);
                                                InventoryService.gI().sendItemBags(player);
                                                Service.getInstance().sendThongBao(player,
                                                        "Bạn nhận được " + itemCaoThu.template.name);
                                            } else {
                                                Service.getInstance().sendThongBao(player,
                                                        "Con không đủ điểm để nhận. Cần 50 điểm");
                                            }

                                        }
                                        break;
                                        case 1: {
                                            if (InventoryService.gI().getCountEmptyBag(player) == 0) {
                                                this.npcChat(player,
                                                        "Con phải có ít nhất 1 ô trống trong hành trang ta mới đưa cho con được");
                                                return;
                                            }
                                            if (player.diem_skien >= 50) {
                                                Item itemCaoThu = ItemService.gI()
                                                        .createNewItem((short) ConstItem.THANH_DAP_DO_7);
                                                itemCaoThu.itemOptions.add(new ItemOption(50, 8));
                                                itemCaoThu.itemOptions.add(new ItemOption(77, 9));
                                                itemCaoThu.itemOptions.add(new ItemOption(103, 9));
                                                itemCaoThu.itemOptions.add(new ItemOption(93, Util.nextInt(3, 7)));
                                                itemCaoThu.quantity = 1;
                                                player.diem_skien -= 50;
                                                InventoryService.gI().addItemBag(player, itemCaoThu, 0);
                                                InventoryService.gI().sendItemBags(player);
                                                Service.getInstance().sendThongBao(player,
                                                        "Bạn nhận được " + itemCaoThu.template.name);
                                            } else {
                                                Service.getInstance().sendThongBao(player,
                                                        "Con không đủ điểm để nhận. Cần 50 điểm");
                                            }

                                        }
                                        break;
                                        case 2: {
                                            if (InventoryService.gI().getCountEmptyBag(player) == 0) {
                                                this.npcChat(player,
                                                        "Con phải có ít nhất 1 ô trống trong hành trang ta mới đưa cho con được");
                                                return;
                                            }
                                            if (player.diem_skien >= 100) {
                                                Item itemCaoThu = ItemService.gI()
                                                        .createNewItem((short) ConstItem.NONG_DAN_CHAM_CHI);
                                                itemCaoThu.itemOptions.add(new ItemOption(50, 10));
                                                itemCaoThu.itemOptions.add(new ItemOption(77, 7));
                                                itemCaoThu.itemOptions.add(new ItemOption(103, 7));
                                                itemCaoThu.itemOptions.add(new ItemOption(93, Util.nextInt(3, 7)));
                                                itemCaoThu.quantity = 1;
                                                player.diem_skien -= 100;
                                                InventoryService.gI().addItemBag(player, itemCaoThu, 0);
                                                InventoryService.gI().sendItemBags(player);
                                                Service.getInstance().sendThongBao(player,
                                                        "Bạn nhận được " + itemCaoThu.template.name);
                                            } else {
                                                Service.getInstance().sendThongBao(player,
                                                        "Con không đủ điểm để nhận. Cần 100 điểm");
                                            }

                                        }
                                        break;
                                        case 3: {
                                            if (InventoryService.gI().getCountEmptyBag(player) == 0) {
                                                this.npcChat(player,
                                                        "Con phải có ít nhất 1 ô trống trong hành trang ta mới đưa cho con được");
                                                return;
                                            }
                                            if (player.diem_skien >= 100) {
                                                Item itemCaoThu = ItemService.gI()
                                                        .createNewItem((short) ConstItem.TRUM_SAN_BOSS);
                                                itemCaoThu.itemOptions.add(new ItemOption(50, 7));
                                                itemCaoThu.itemOptions.add(new ItemOption(77, 10));
                                                itemCaoThu.itemOptions.add(new ItemOption(103, 10));
                                                itemCaoThu.itemOptions.add(new ItemOption(93, Util.nextInt(3, 7)));
                                                itemCaoThu.quantity = 1;
                                                player.diem_skien -= 100;
                                                InventoryService.gI().addItemBag(player, itemCaoThu, 0);
                                                InventoryService.gI().sendItemBags(player);
                                                Service.getInstance().sendThongBao(player,
                                                        "Bạn nhận được " + itemCaoThu.template.name);
                                            } else {
                                                Service.getInstance().sendThongBao(player,
                                                        "Con không đủ điểm để nhận. Cần 100 điểm");
                                            }

                                        }
                                        break;
                                        case 4: {
                                            if (InventoryService.gI().getCountEmptyBag(player) == 0) {
                                                this.npcChat(player,
                                                        "Con phải có ít nhất 1 ô trống trong hành trang ta mới đưa cho con được");
                                                return;
                                            }
                                            if (player.diem_skien >= 150) {
                                                Item itemCaoThu = ItemService.gI()
                                                        .createNewItem((short) ConstItem.ONG_THAN_VE_CHAI);
                                                itemCaoThu.itemOptions.add(new ItemOption(50, 11));
                                                itemCaoThu.itemOptions.add(new ItemOption(77, 6));
                                                itemCaoThu.itemOptions.add(new ItemOption(103, 6));
                                                itemCaoThu.itemOptions.add(new ItemOption(93, Util.nextInt(3, 7)));
                                                itemCaoThu.quantity = 1;
                                                player.diem_skien -= 150;
                                                InventoryService.gI().addItemBag(player, itemCaoThu, 0);
                                                InventoryService.gI().sendItemBags(player);
                                                Service.getInstance().sendThongBao(player,
                                                        "Bạn nhận được " + itemCaoThu.template.name);

                                            } else {
                                                Service.getInstance().sendThongBao(player,
                                                        "Con không đủ điểm để nhận. Cần 150 điểm");
                                            }
                                        }
                                        break;

                                        case 5: {
                                            if (InventoryService.gI().getCountEmptyBag(player) == 0) {
                                                this.npcChat(player,
                                                        "Con phải có ít nhất 1 ô trống trong hành trang ta mới đưa cho con được");
                                                return;
                                            }
                                            if (player.diem_skien >= 150) {
                                                Item itemCaoThu = ItemService.gI()
                                                        .createNewItem((short) ConstItem.TRUM_UOC_RONG);
                                                itemCaoThu.itemOptions.add(new ItemOption(50, 6));
                                                itemCaoThu.itemOptions.add(new ItemOption(77, 11));
                                                itemCaoThu.itemOptions.add(new ItemOption(103, 11));
                                                itemCaoThu.itemOptions.add(new ItemOption(93, Util.nextInt(3, 7)));
                                                itemCaoThu.quantity = 1;
                                                player.diem_skien -= 150;
                                                InventoryService.gI().addItemBag(player, itemCaoThu, 0);
                                                InventoryService.gI().sendItemBags(player);
                                                Service.getInstance().sendThongBao(player,
                                                        "Bạn nhận được " + itemCaoThu.template.name);
                                            } else {
                                                Service.getInstance().sendThongBao(player,
                                                        "Con không đủ điểm để nhận. Cần 150 điểm");
                                            }
                                        }
                                        break;

                                        case 6: {
                                            if (InventoryService.gI().getCountEmptyBag(player) == 0) {
                                                this.npcChat(player,
                                                        "Con phải có ít nhất 1 ô trống trong hành trang ta mới đưa cho con được");
                                                return;
                                            }
                                            if (player.diem_skien >= 200) {
                                                Item itemCaoThu = ItemService.gI()
                                                        .createNewItem((short) ConstItem.BI_MOC_SACH_TUI);
                                                itemCaoThu.itemOptions.add(new ItemOption(50, 12));
                                                itemCaoThu.itemOptions.add(new ItemOption(77, 5));
                                                itemCaoThu.itemOptions.add(new ItemOption(103, 5));
                                                itemCaoThu.itemOptions.add(new ItemOption(93, Util.nextInt(3, 7)));
                                                itemCaoThu.quantity = 1;
                                                player.diem_skien -= 200;
                                                InventoryService.gI().addItemBag(player, itemCaoThu, 0);
                                                InventoryService.gI().sendItemBags(player);
                                                Service.getInstance().sendThongBao(player,
                                                        "Bạn nhận được " + itemCaoThu.template.name);
                                            } else {
                                                Service.getInstance().sendThongBao(player,
                                                        "Con không đủ điểm để nhận. Cần 200 điểm");
                                            }
                                        }
                                        break;
                                        case 7: {
                                            if (InventoryService.gI().getCountEmptyBag(player) == 0) {
                                                this.npcChat(player,
                                                        "Con phải có ít nhất 1 ô trống trong hành trang ta mới đưa cho con được");
                                                return;
                                            }
                                            if (player.diem_skien >= 200) {
                                                Item itemCaoThu = ItemService.gI()
                                                        .createNewItem((short) ConstItem.DAI_GIA_MOI_NHU);
                                                itemCaoThu.itemOptions.add(new ItemOption(50, 5));
                                                itemCaoThu.itemOptions.add(new ItemOption(77, 12));
                                                itemCaoThu.itemOptions.add(new ItemOption(103, 12));
                                                itemCaoThu.itemOptions.add(new ItemOption(93, Util.nextInt(3, 7)));
                                                itemCaoThu.quantity = 1;
                                                player.diem_skien -= 200;
                                                InventoryService.gI().addItemBag(player, itemCaoThu, 0);
                                                InventoryService.gI().sendItemBags(player);
                                                Service.getInstance().sendThongBao(player,
                                                        "Bạn nhận được " + itemCaoThu.template.name);
                                            } else {
                                                Service.getInstance().sendThongBao(player,
                                                        "Con không đủ điểm để nhận. Cần 200 điểm");
                                            }
                                        }
                                        break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NOI_CHUYEN) {
                                    if (player.clan != null) {
                                        if (player.clan.isLeader(player)) {
                                            switch (select) {
                                                case 0:// nhiệm vụ
                                                    NpcService.gI().createTutorial(player, avartar, "Nhiệm vụ hiện tại của con: " + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).name);
                                                    break;
                                                case 1:// có phí
                                                    ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOP_QUY_LAO_KAME_2, 2, -1);
                                                    break;
                                                case 2:
                                                    if (player.clan == null) {
                                                        Service.getInstance().sendThongBao(player, "Chưa có bang hội");
                                                        return;
                                                    }
                                                    ChangeMapService.gI().changeMap(player, player.clan.getClanArea(), 910, 190);
                                                    break;
                                                case 3: // Giải tán bang hội
                                                    if (player.clan.isLeader(player)) {
                                                        this.createOtherMenu(player, ConstNpc.ACP_GIAI_TAN_BANG,
                                                                "Con có chắc chắn giải tán bang hội không?",
                                                                "Đồng ý", "Từ chối");
                                                    }
//                                                     this.npcChat(player, "Tắt rồi mày ơi");
                                                    break;
                                                case 4:
                                                    if (player.clan != null) {
                                                        if (player.clan.banDoKhoBau != null) {
                                                            this.createOtherMenu(player, ConstNpc.MENU_OPENED_DBKB,
                                                                    "Bang hội của con đang đi tìm kho báu dưới biển cấp độ "
                                                                    + player.clan.banDoKhoBau.level
                                                                    + "\nCon có muốn đi theo không?",
                                                                    "Đồng ý", "Từ chối");
                                                        } else {
                                                            this.createOtherMenu(player, ConstNpc.MENU_OPEN_DBKB,
                                                                    "Đây là bản đồ kho báu hải tặc tí hon\nCác con cứ yên tâm lên đường\n"
                                                                    + "Ở đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
                                                                    "Top\nBang hội", "Thành tích\nBang", "Chọn\ncấp độ", "Từ chối");
                                                        }
                                                    } else {
                                                        this.npcChat(player, "Con phải có bang hội ta mới có thể cho con đi");
                                                    }
                                                    break;
                                            }
                                        } else {
                                            switch (select) {
                                                case 0:// nhiệm vụ
                                                    NpcService.gI().createTutorial(player, avartar, "Nhiệm vụ hiện tại của con: " + player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).name);
                                                    break;
                                                case 1:// có phí
                                                    ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOP_QUY_LAO_KAME_2, 2, -1);
                                                    break;
                                                case 2:
                                                    if (player.clan == null) {
                                                        Service.getInstance().sendThongBao(player, "Chưa có bang hội");
                                                        return;
                                                    }
                                                    ChangeMapService.gI().changeMap(player, player.clan.getClanArea(), 910, 190);
                                                    break;
                                                case 3:
                                                    if (player.clan != null) {
                                                        if (player.clan.banDoKhoBau != null) {
                                                            this.createOtherMenu(player, ConstNpc.MENU_OPENED_DBKB,
                                                                    "Bang hội của con đang đi tìm kho báu dưới biển cấp độ "
                                                                    + player.clan.banDoKhoBau.level
                                                                    + "\nCon có muốn đi theo không?",
                                                                    "Đồng ý", "Từ chối");
                                                        } else {
                                                            this.createOtherMenu(player, ConstNpc.MENU_OPEN_DBKB,
                                                                    "Đây là bản đồ kho báu hải tặc tí hon\nCác con cứ yên tâm lên đường\n"
                                                                    + "Ở đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
                                                                    "Top\nBang hội", "Thành tích\nBang", "Chọn\ncấp độ", "Từ chối");
                                                        }
                                                    } else {
                                                        this.npcChat(player, "Con phải có bang hội ta mới có thể cho con đi");
                                                    }
                                                    break;
                                            }
                                        }
                                    } else {
                                        switch (select) {
                                            case 0:// nhiệm vụ
                                                NpcService.gI().createTutorial(player, avartar, player.playerTask.taskMain.name);
                                                break;
                                            case 1:// có phí
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOP_QUY_LAO_KAME_2, 2, -1);
                                                break;
                                            case 2:
                                                if (player.clan != null) {
                                                    if (player.clan.banDoKhoBau != null) {
                                                        this.createOtherMenu(player, ConstNpc.MENU_OPENED_DBKB,
                                                                "Bang hội của con đang đi tìm kho báu dưới biển cấp độ "
                                                                + player.clan.banDoKhoBau.level
                                                                + "\nCon có muốn đi theo không?",
                                                                "Đồng ý", "Từ chối");
                                                    } else {
                                                        this.createOtherMenu(player, ConstNpc.MENU_OPEN_DBKB,
                                                                "Đây là bản đồ kho báu hải tặc tí hon\nCác con cứ yên tâm lên đường\n"
                                                                + "Ở đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
                                                                "Top\nBang hội", "Thành tích\nBang", "Chọn\ncấp độ", "Từ chối");
                                                    }
                                                } else {
                                                    this.npcChat(player, "Con phải có bang hội ta mới có thể cho con đi");
                                                }
                                                break;
                                        }
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.ACP_GIAI_TAN_BANG) {
                                    switch (select) {
                                        case 0:
                                            Input.gI().createFormGiaiTanBang(player);
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_SUKIEN) {
                                    openMenuSuKien(player, this, tempId, select);
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_DBKB) {
                                    switch (select) {
                                        case 0:
                                            ChangeMapService.gI().goToDBKB(player);
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_DBKB) {
                                    switch (select) {
                                        case 0:// Top bang hội
                                            Service.gI().showTopClanBDKB(player);
                                            break;
                                        case 1:// Thách tích bang
                                            Service.getInstance().showMyTopClanBDKB(player);
                                            break;
                                        case 2:
                                            if (player.isAdmin()
                                                    || player.nPoint.power >= BanDoKhoBau.POWER_CAN_GO_TO_DBKB) {
                                                Input.gI().createFormChooseLevelBDKB(player);
                                            } else {
                                                this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                                        + Util.numberToMoney(BanDoKhoBau.POWER_CAN_GO_TO_DBKB));
                                            }
                                            break;
                                    }

                                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCEPT_GO_TO_BDKB) {
                                    switch (select) {
                                        case 0:
                                            BanDoKhoBauService.gI().openBanDoKhoBau(player, Byte.parseByte(String.valueOf(PLAYERID_OBJECT.get(player.id))));
                                            break;
                                    }

                                } else if (player.iDMark.getIndexMenu() == ConstNpc.ESCORT_QILIN_MENU) {
                                    switch (select) {
                                        case 0: {
                                            if (InventoryService.gI().getCountEmptyBag(player) == 0) {
                                                this.npcChat(player,
                                                        "Con phải có ít nhất 1 ô trống trong hành trang ta mới đưa cho con được");
                                                return;
                                            }
                                            EscortedBoss escortedBoss = player.getEscortedBoss();
                                            if (escortedBoss != null) {
                                                escortedBoss.stopEscorting();
                                                Item item = ItemService.gI()
                                                        .createNewItem((short) ConstItem.CAPSULE_TET_2022);
                                                item.quantity = 1;
                                                InventoryService.gI().addItemBag(player, item, 0);
                                                InventoryService.gI().sendItemBags(player);
                                                Service.getInstance().sendThongBao(player,
                                                        "Bạn nhận được " + item.template.name);
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.TRUONG_LAO_GURU:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            Item mcl = InventoryService.gI().findItemBagByTemp(player, ConstTranhNgocNamek.ITEM_TRANH_NGOC);
                            int slMCL = (mcl == null) ? 0 : mcl.quantity;
                            if (canOpenNpc(player)) {
                                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Ngọc rồng Namếc đang bị 2 thế lực tranh giành\nHãy tham gia để lấy lại nó",
                                            "Tham gia", "Từ chối");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.BASE_MENU:
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().changeMap(player, 221, -1, 635, 100);
                                                break;

                                        }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.VUA_VEGETA:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                EscortedBoss escortedBoss = player.getEscortedBoss();
//                                if (escortedBoss != null && escortedBoss instanceof Qilin) {
//                                    this.createOtherMenu(player, ConstNpc.ESCORT_QILIN_MENU,
//                                            "Ah con đã tìm thấy lân con thất lạc của ta\nTa sẽ thưởng cho con 1 viên Capsule Tết 2023.",
//                                            "Đồng ý", "Từ chối");
//                                } else {
                                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                    super.openBaseMenu(player);
                                }
//                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.getIndexMenu() == ConstNpc.ESCORT_QILIN_MENU) {
                                    if (select == 0) {
                                        if (InventoryService.gI().getCountEmptyBag(player) == 0) {
                                            this.npcChat(player, "Con phải có ít nhất 1 ô trống trong hành trang ta mới đưa cho con được");
                                            return;
                                        }
                                        EscortedBoss escortedBoss = player.getEscortedBoss();
                                        if (escortedBoss != null) {
                                            escortedBoss.stopEscorting();
                                            Item item = ItemService.gI().createNewItem((short) ConstItem.CAPSULE_TET_2022);
                                            item.quantity = 1;
                                            InventoryService.gI().addItemBag(player, item, 0);
                                            InventoryService.gI().sendItemBags(player);
                                            Service.gI().sendThongBao(player, "Bạn nhận được " + item.template.name);
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;

                case ConstNpc.ONG_GOHAN:
                case ConstNpc.ONG_MOORI:
                case ConstNpc.ONG_PARAGUS:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Con có " + player.DiemGt + " điểm giới thiệu \nHãy truy cập trang chủ Ngọc Rồng Star để tham gia sự kiện nhé",
                                            "Mã Quà Tặng", "Nhận vàng", "Hỗ Trợ NV", "Đổi Điểm Giới Thiệu", "Mở Thành Viên", "Từ chối");

                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:
                                            Input.gI().createFormGiftCode(player);
                                            break;
                                        case 1:
                                            this.createOtherMenu(player, ConstNpc.MENU_NAP_TIEN,
                                                    "Số dư của con là: " + Util.mumberToLouis(player.soDuVND) + " VND dùng để nạp qua đơn vị khác\n"
                                                    + "Ta đang giữ giúp con " + Util.mumberToLouis(player.soThoiVang) + " thỏi vàng",
                                                    "Đổi thỏi vàng", "Nhận\nThỏi vàng",
                                                    "Đổi Hồng Ngọc", "Nhận\nNgọc Xanh\n(Miễn phí)",
                                                    "Nhận mốc\nNạp", "Đóng");
                                            return;
                                        case 2: {
                                            if (TaskService.gI().getIdTask(player) == ConstTask.TASK_18_0 || TaskService.gI().getIdTask(player) == ConstTask.TASK_18_1) {
                                                player.playerTask.taskMain.id = 18;
                                                player.playerTask.taskMain.index = 2;
                                                TaskService.gI().sendTaskMain(player);
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Chỉ hỗ trợ nhiệm vụ DHVT.");
                                            }
                                            if (TaskService.gI().getIdTask(player) == ConstTask.TASK_19_0 || TaskService.gI().getIdTask(player) == ConstTask.TASK_19_1) {
                                                player.playerTask.taskMain.id = 19;
                                                player.playerTask.taskMain.index = 2;
                                                TaskService.gI().sendTaskMain(player);
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Chỉ hỗ trợ nhiệm vụ Trung Úy Trắng.");
                                            }
                                            if (TaskService.gI().getIdTask(player) == ConstTask.TASK_16_0) {
                                                player.playerTask.taskMain.id = 16;
                                                player.playerTask.taskMain.index = 1;
                                                TaskService.gI().sendTaskMain(player);
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Chỉ hỗ trợ nhiệm vụ Thách đấu 10 người.");
                                            }
                                            if (TaskService.gI().getIdTask(player) == ConstTask.TASK_29_0) {
                                                player.playerTask.taskMain.id = 29;
                                                player.playerTask.taskMain.index = 1;
                                                TaskService.gI().sendTaskMain(player);
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Chỉ hỗ trợ nhiệm vụ Nâng 10k sđg.");
                                            }
                                        }
                                        break;
//                                        case 3:
//                                            if (player.pet == null) {
//                                                PetService.gI().createNormalPet(player, Util.nextInt(0, 2));
//                                                Service.getInstance().sendThongBao(player, "Con vừa nhận được đệ tử! Hãy chăm sóc nó nhé");
//                                            } else {
//                                                this.npcChat(player, "Đã có đệ tử rồi mà!");
//                                            }
//                                            break;
                                        case 3:
                                            if (player.DiemGt <= 0) {
                                                Service.gI().sendThongBao(player, "Bạn không đủ điểm để đổi item, hãy truy cập trang chủ để biết thông tin sự kiện");
                                                return;
                                            } else {

                                                //open
                                                Item ct = ItemService.gI().createNewItem((short) 1329, 1);
                                                ct.itemOptions.add(new ItemOption(50, 20));
                                                ct.itemOptions.add(new ItemOption(101, 30));
                                                ct.itemOptions.add(new ItemOption(95, 10));
                                                ct.itemOptions.add(new ItemOption(96, 10));

                                                Item tv = ItemService.gI().createNewItem((short) 2011, 50);

                                                Item buax5 = ItemService.gI().createNewItem((short) 1326, 3);
                                                buax5.itemOptions.add(new ItemOption(30, 1));

                                                Item cn2 = ItemService.gI().createNewItem((short) 1150, 10);
                                                cn2.itemOptions.add(new ItemOption(30, 1));

                                                Item bk2 = ItemService.gI().createNewItem((short) 1151, 10);
                                                bk2.itemOptions.add(new ItemOption(30, 1));

                                                Item bh2 = ItemService.gI().createNewItem((short) 1152, 10);
                                                bh2.itemOptions.add(new ItemOption(30, 1));

                                                PlayerDAO.subReferral_points(player, 1);
                                                player.DiemGt -= 1;
                                                InventoryService.gI().addItemBag(player, ct, 1);
                                                InventoryService.gI().addItemBag(player, tv, 9999);
                                                InventoryService.gI().addItemBag(player, buax5, 9999);
                                                InventoryService.gI().addItemBag(player, cn2, 9999);
                                                InventoryService.gI().addItemBag(player, bk2, 9999);
                                                InventoryService.gI().addItemBag(player, bh2, 9999);
                                                InventoryService.gI().sendItemBags(player);
                                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + ct.getName());
                                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + tv.getName());
                                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + buax5.getName());
                                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + cn2.getName());
                                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + bk2.getName());
                                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + bh2.getName());

                                            }
                                            break;
                                        case 4:
                                            if (player.thanhVien == false) {
                                                if (player.soDuVND >= 10000) {
                                                    player.thanhVien = true;
                                                    player.soDuVND -= 10000;
                                                    PlayerDAO.subVndBar(player, 10000);
                                                    PlayerDAO.moThanhVien(player);
                                                    Service.getInstance().sendThongBao(player, "Chúc mừng bạn đã mở thành viên thành công!");
                                                } else {
                                                    Service.gI().sendThongBao(player, "Bạn không đủ số dư để mở thành viên");
                                                }
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn đã mở thành viên rồi");
                                            }
                                            break;
                                    }
                                }
                                if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NAP_TIEN) {
                                    switch (select) {
                                        case 0: // Nạp vàng
                                            this.createOtherMenu(player, ConstNpc.MENU_DOI_VANG,
                                                    "Ta sẽ tạm giữ giúp con\n"
                                                    + "Nếu con cần dùng tới hãy quay lại đây gặp ta!",
                                                    "10.000\n30 Thỏi\nvàng", "20.000\n60 Thỏi\nvàng",
                                                    "30.000\n125 Thỏi\nvàng", "50.000\n160 Thỏi\nvàng",
                                                    "100.000\n320 Thỏi\nvàng", "200.000\n640 Thỏi\nvàng",
                                                    "500.000\n1.600 Thỏi\nvàng", "1.000.000\n3.200 Thỏi\nvàng");
                                            return;
                                        case 1: // Nhận thỏi vàng
                                            Input.gI().createFormNhanThoiVang(player);
                                            break;
                                        case 4:
                                            createOtherMenu(player, ConstNpc.NHAN_QUA,
                                                    "Xin chào\n|2|Hiện tại con đã nạp : " + player.getSession().tongnap + " VNĐ"
                                                    + "\n|5|Đã nhận quà mốc nạp: " + (player.mocnap > 0 ? Util.format(player.mocnap) + " VNĐ" : "Hiện chưa nhận mốc nạp nào")
                                                    + "\n|-1|Con muốn nhận phần thường nào?",
                                                    "Nhận Quà\nMốc Nạp");
                                            break;
                                        case 2:
                                            Input.gI().createFormTradeRuby(player);
                                            break;
                                        case 3:
                                            if (player.inventory.gem >= 10000000) {
                                                Service.getInstance().sendThongBao(player, "Tiêu bớt ngọc xanh đi bạn ơi");
                                            } else {
                                                player.inventory.gem += 100000;
                                                Service.getInstance().sendMoney(player);
                                            }
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.NHAN_QUA) {
                                    switch (select) {
                                        case 0:
                                            createOtherMenu(player, ConstNpc.MOC_NAP,
                                                    "Xin chào"
                                                    + "\n|2|Hiện tại con đã nạp được: " + Util.format(player.getSession().tongnap) + " VNĐ"
                                                    + "\n|-1|Con hãy chọn mốc nạp muốn nhận?",
                                                    "Xem Mốc\n20K VNĐ", "Xem Mốc\n50K VNĐ", "Xem Mốc\n100K VNĐ", "Xem Mốc\n200K VNĐ", "Xem Mốc\n500K VNĐ");

                                            break;
                                        case 1:
                                            NpcService.gI().createTutorial(player, avartar, "Chức năng đang được phát triển");
//                                            createOtherMenu(player, ConstNpc.MOC_BOS,
//                                                    "Xin chào"
//                                                    + "\n|2|Hiện tại con có: " + player.pointSb + " điểm Săn Boss"
//                                                    + "\n|-1|Con hãy chọn mốc thưởng săn boss muốn nhận?",
//                                                    "Xem Mốc\n100 ĐIỂM", "Xem Mốc\n200 ĐIỂM", "Xem Mốc\n300 ĐIỂM",
//                                                    "Xem Mốc\n500 ĐIỂM", "Xem Mốc\n800 ĐIỂM", "Xem Mốc\n1000 ĐIỂM");

                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.MOC_NAP) {
                                    switch (select) {
                                        case 0:
                                            createOtherMenu(player, ConstNpc.MOC_NAP1,
                                                    "|7|Phần Thưởng Mốc Nạp 20.000 VNĐ Gồm:"
                                                    + "\n|2|- x5 item 2 mỗi loại"
                                                    + "\n- x2 Bình X5 TNSM"
                                                    + "\n- x20 thỏi vàng"
                                                    + "\n|7|Bạn có mún nhận khum?",
                                                    "Nhận Ngay", "Khum Hong cóa tiền");

                                            break;
                                        case 1:
                                            createOtherMenu(player, ConstNpc.MOC_NAP2,
                                                    "|7|Phần Thưởng Mốc Nạp 50.000 VNĐ"
                                                    + "\n|2|- x10 item 2 mỗi loại"
                                                    + "\n|2|- x10 3 sao"
                                                    + "\n|2|- x80 thỏi vàng"
                                                    + "\n- giáp luyện tập cấp 4 10% hp và ki"
                                                    + "\n|7|Bạn có mún nhận khum?",
                                                    "Nhận Ngay", "Khum Hong cóa tiền");
                                            break;
                                        case 2:
                                            createOtherMenu(player, ConstNpc.MOC_NAP3,
                                                    "|7|Phần Thưởng Mốc Nạp 100.000 VNĐ:"
                                                    + "\n|2|- x1 thẻ đổi tên"
                                                    + "\n- x10 3 sao"
                                                    + "\n- x10 item 2 mỗi loại"
                                                    + "\n- x5 Bình x5 tnsm"
                                                    + "\n- x50 đá bảo vệ"
                                                    + "\n- x5 hộp linh thú"
                                                    + "\n|7|Bạn có mún nhận khum?",
                                                    "Nhận Ngay", "Khum Hong cóa tiền");
                                            break;
                                        case 3:
                                            createOtherMenu(player, ConstNpc.MOC_NAP4,
                                                    "|7|Phần Thưởng Mốc Nạp 200.000 VNĐ:"
                                                    + "\n|2|- x150 thỏi vàng"
                                                    + "\n- x10 Hộp hit"
                                                    + "\n- x20 item 2 mỗi loại"
                                                    + "\n- x10 hộp quà sự kiện"
                                                    + "\n- x100 đá bảo vệ"
                                                    + "\n- x3 bộ ngọc rồng băng"
                                                    + "\n- x3 bộ ngọc rồng"
                                                    + "\n|7|Bạn có mún nhận khum?",
                                                    "Nhận Ngay", "Khum Hong cóa tiền");
                                            break;
                                        case 4:
                                            createOtherMenu(player, ConstNpc.MOC_NAP5,
                                                    "|7|Phần Thưởng Mốc Nạp 500.000 VNĐ:"
                                                    + "\n|2|- x300 thỏi vàng"
                                                    + "\n- x30 Hộp hit"
                                                    + "\n- x50 item 2 mỗi loại"
                                                    + "\n- x20 hộp linh thú"
                                                    + "\n- x20 hộp quà sự kiện"
                                                    + "\n- x10 bộ ngọc rồng băng"
                                                    + "\n- x999 đá bảo cam"
                                                    + "\n- x600 đá pháp sư"
                                                    + "\n|7|Bạn có mún nhận khum?",
                                                    "Nhận Ngay", "Khum Hong cóa tiền");
                                            break;
//                                        case 5:
//                                            createOtherMenu(player, ConstNpc.MOC_NAP1,
//                                                    "|7|Phần Thưởng Mốc Nạp 1.500.000 VNĐ Tuần 1 Gồm:"
//                                                    + "\n|2|- 1000TV"
//                                                    + "\n- x50 đá thánh"
//                                                    + "\n- x30 đá cam"
//                                                    + "\n- x30 đá lửa"
//                                                    + "\n- x50 vé ngũ hành sơn"
//                                                    + "\n|7| Bạn có mún nhận khum?",
//                                                    "Nhận Ngay", "Khum Hong cóa tiền");
//                                            break;
//                                        case 6:
//                                            createOtherMenu(player, ConstNpc.MOC_NAP1,
//                                                    "|7|Phần Thưởng Mốc Nạp 2.000.000 VNĐ Tuần 1 Gồm:"
//                                                    + "\n|2|- 1700TV"
//                                                    + "\n- x99 đá thánh"
//                                                    + "\n- x99 đá cam"
//                                                    + "\n- x99 đá lửa"
//                                                    + "\n- x99 vé ngũ hành sơn"
//                                                    + "\n|7| Bạn có mún nhận khum?",
//                                                    "Nhận Ngay", "Khum Hong cóa tiền");
//                                        case 7:
//                                            createOtherMenu(player, ConstNpc.MOC_NAP1,
//                                                    "|7|Phần Thưởng Mốc Nạp 5.000.000 VNĐ Tuần 1 Gồm:"
//                                                    + "\n|2|- Cải trang Nakroth Thứ nguyên vệ thần(40%cs all)"
//                                                    + "\n- x999 đồng brics"
//                                                    + "\n|7| Bạn có mún nhận khum?",
//                                                    "Nhận Ngay", "Khum Hong cóa tiền");
//                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.MOC_BOS) {
                                    switch (select) {
                                        case 0:
                                            createOtherMenu(player, ConstNpc.MOC_NAP1,
                                                    "|7|Phần Thưởng Mốc Diệt Boss Đạt 100 ĐiMOC_NAP1ểm Gồm:"
                                                    + "\n|2|- 55 Đá Hoàng Kim"
                                                    + "\n- 111 Đá Thức Tỉnh"
                                                    + "\n- 20 Thỏi Vàng"
                                                    + "\n- 5 Máy Dò Boss"
                                                    + "\n|7|Bạn có mún nhận khum?",
                                                    "Nhận Ngay", "Đéo");

                                            break;
                                        case 1:
                                            createOtherMenu(player, ConstNpc.MOC_BOS1,
                                                    "|7|Phần Thưởng Mốc Diệt Boss Đạt 200 Điểm Gồm:"
                                                    + "\n|2|- 111 Đá Hoàng Kim"
                                                    + "\n- 222 Đá Thức Tỉnh"
                                                    + "\n- 30 Thỏi Vàng"
                                                    + "\n- 5 Máy Dò Boss"
                                                    + "\n|7| Bạn có mún nhận khum?",
                                                    "Nhận Ngay", "Đéo");
                                            break;
                                        case 2:
                                            createOtherMenu(player, ConstNpc.MOC_BOS1,
                                                    "|7|Phần Thưởng Mốc Diệt Boss Đạt 300 Điểm Gồm:"
                                                    + "\n|2|- 161 Đá Hoàng Kim"
                                                    + "\n- 333 Đá Thức Tỉnh"
                                                    + "\n- 50 Thỏi Vàng"
                                                    + "\n- 5 Máy Dò Boss"
                                                    + "\n|7| Bạn có mún nhận khum?",
                                                    "Nhận Ngay", "Đéo");
                                            break;
                                        case 3:
                                            createOtherMenu(player, ConstNpc.MOC_BOS1,
                                                    "|7|Phần Thưởng Mốc Diệt Boss Đạt 500 Điểm Gồm:"
                                                    + "\n|2|- 252 Đá Hoàng Kim"
                                                    + "\n- 555 Đá Thức Tỉnh"
                                                    + "\n- 150 Thỏi Vàng"
                                                    + "\n- 15 Máy Dò Boss"
                                                    + "\n|7| Bạn có mún nhận khum?",
                                                    "Nhận Ngay", "Đéo");
                                            break;
                                        case 4:
                                            createOtherMenu(player, ConstNpc.MOC_BOS1,
                                                    "|7|Phần Thưởng Mốc Diệt Boss Đạt 800 Điểm Gồm:"
                                                    + "\n|2|- 444 Đá Hoàng Kim"
                                                    + "\n- 888 Đá Thức Tỉnh"
                                                    + "\n- 300 Thỏi Vàng"
                                                    + "\n- 15 Máy Dò Boss"
                                                    + "\n|7| Bạn có mún nhận khum?",
                                                    "Nhận Ngay", "Đéo");
                                            break;
                                        case 5:
                                            createOtherMenu(player, ConstNpc.MOC_BOS1,
                                                    "|7|Phần Thưởng Mốc Diệt Boss Đạt 1000 Điểm Gồm:"
                                                    + "\n|2|- 499 Đá Hoàng Kim"
                                                    + "\n- 999 Đá Thức Tỉnh"
                                                    + "\n- 500 Thỏi Vàng"
                                                    + "\n- 15 Máy Dò Boss"
                                                    + "\n|7| Bạn có mún nhận khum?",
                                                    "Nhận Ngay", "Đéo");
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.MOC_NAP1) {
                                    switch (select) {
                                        case 0:
//                                            UseItem.gI().ComfirmMocNap(player);
                                            Item itemqua;
                                            Item itemqua1;
                                            Item itemqua2;
                                            Item itemqua3;
                                            Item itemqua4;
                                            Item itemqua5;

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
                                                } else {
                                                    Service.getInstance().sendThongBao(player, "Bạn Chưa Đủ Điều Kiện Nhận Mốc Nạp Này Hoặc đã Nhận rồi!!");
                                                }
                                            } catch (Exception e) {
                                            }
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.MOC_NAP2) {
                                    switch (select) {
                                        case 0:
                                            Item itemqua;
                                            Item itemqua1;
                                            Item itemqua2;
                                            Item itemqua3;
                                            Item itemqua4;
                                            Item itemqua5;
                                            Item itemqua6;
                                            try {
                                                int time = 5;

                                                if (player.getSession().tongnap >= 50000 && player.mocnap == 20000) {
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
                                                    itemqua4.itemOptions.add(new ItemOption(9, 0));
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
                                                    Service.getInstance().sendThongBao(player, "Bạn Chưa Đủ Điều Kiện Nhận Mốc Nạp Này Hoặc đã Nhận rồi!");
                                                }
                                            } catch (Exception e) {
                                            }
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.MOC_NAP3) {
                                    switch (select) {
                                        case 0:
                                            Item itemqua;
                                            Item itemqua1;
                                            Item itemqua2;
                                            Item itemqua3;
                                            Item itemqua4;
                                            Item itemqua5;
                                            Item itemqua6;
                                            Item itemqua7;
                                            Item itemqua8;
                                            try {
                                                int time = 5;

                                                if (player.getSession().tongnap >= 100000 && player.mocnap == 50000) {
                                                    if (InventoryService.gI().getCountEmptyBag(player) < 9) {
                                                        Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 9 ô trống hành trang");
                                                        return;
                                                    }
                                                    Service.getInstance().sendThongBao(player, "Tiến Hành Nhận\nMốc Nạp 100K\nSau " + time + " Giây!");
                                                    while (time > 0) {
                                                        time--;
                                                        Thread.sleep(1000);
                                                        Service.getInstance().sendThongBao(player, "|7|" + time);
                                                    }
                                                    player.mocnap = 100000;//100k
                                                    itemqua = ItemService.gI().createNewItem((short) 1150, 10);//cuồng nộ 2
                                                    itemqua1 = ItemService.gI().createNewItem((short) 1151, 10);//bổ khí 2
                                                    itemqua2 = ItemService.gI().createNewItem((short) 1152, 10);//bổ huyết 2
                                                    itemqua3 = ItemService.gI().createNewItem((short) 1153, 10);//giáp 2
                                                    itemqua4 = ItemService.gI().createNewItem((short) 1326, 5);//bùa x5

                                                    itemqua5 = ItemService.gI().createNewItem((short) 16, 10);//3sao
                                                    itemqua6 = ItemService.gI().createNewItem((short) 987, 50);//đá bảo vệ
                                                    itemqua7 = ItemService.gI().createNewItem((short) 1504, 1);//thẻ đổi tên
                                                    itemqua8 = ItemService.gI().createNewItem((short) 1376, 5);//Hộp linh thú

                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua1.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua2.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua3.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua4.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua4.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua5.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua6.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua7.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua8.template.name);

                                                    InventoryService.gI().addItemBag(player, itemqua, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua1, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua2, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua3, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua4, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua5, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua6, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua7, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua8, 9999);

                                                    InventoryService.gI().sendItemBags(player);
                                                } else {
                                                    Service.getInstance().sendThongBao(player, "Bạn Chưa Đủ Điều Kiện Nhận Mốc Nạp Này Hoặc đã Nhận rồi!");
                                                }
                                            } catch (Exception e) {
                                            }
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.MOC_NAP4) {
                                    switch (select) {
                                        case 0:
                                            Item itemqua;
                                            Item itemqua1;
                                            Item itemqua2;
                                            Item itemqua3;
                                            Item itemqua4;
                                            Item itemqua5;
                                            Item itemqua6;
                                            Item itemqua7;
                                            Item itemqua8;
                                            Item itemqua9;
                                            Item itemqua10;
                                            Item itemqua11;
                                            Item itemqua12;
                                            Item itemqua13;
                                            Item itemqua14;
                                            Item itemqua15;
                                            Item itemqua16;
                                            Item itemqua17;
                                            Item itemqua18;
                                            Item itemqua19;
                                            Item itemqua20;
                                            Item itemqua21;
                                            try {
                                                int time = 5;

                                                if (player.getSession().tongnap >= 200000 && player.mocnap == 100000) {
                                                    if (InventoryService.gI().getCountEmptyBag(player) < 21) {
                                                        Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 21 ô trống hành trang");
                                                        return;
                                                    }
                                                    Service.getInstance().sendThongBao(player, "Tiến Hành Nhận\nMốc Nạp 200K\nSau " + time + " Giây!");
                                                    while (time > 0) {
                                                        time--;
                                                        Thread.sleep(1000);
                                                        Service.getInstance().sendThongBao(player, "|7|" + time);
                                                    }
                                                    player.mocnap = 200000;//200k
                                                    itemqua = ItemService.gI().createNewItem((short) 1150, 20);//cuồng nộ 2
                                                    itemqua1 = ItemService.gI().createNewItem((short) 1151, 20);//bổ khí 2
                                                    itemqua2 = ItemService.gI().createNewItem((short) 1152, 20);//bổ huyết 2
                                                    itemqua3 = ItemService.gI().createNewItem((short) 1153, 20);//giáp 2
                                                    itemqua4 = ItemService.gI().createNewItem((short) 1328, 10);//hòm hit
                                                    itemqua5 = ItemService.gI().createNewItem((short) 1389, 10);//hop quà sk
                                                    itemqua6 = ItemService.gI().createNewItem((short) 457, 150);//tv
                                                    itemqua7 = ItemService.gI().createNewItem((short) 987, 100);//đá bảo vệ
                                                    itemqua8 = ItemService.gI().createNewItem((short) 14, 3);//1sao
                                                    itemqua9 = ItemService.gI().createNewItem((short) 15, 3);//2s
                                                    itemqua10 = ItemService.gI().createNewItem((short) 16, 3);//3s
                                                    itemqua11 = ItemService.gI().createNewItem((short) 17, 3);//4s
                                                    itemqua12 = ItemService.gI().createNewItem((short) 18, 3);//5s
                                                    itemqua13 = ItemService.gI().createNewItem((short) 19, 3);//6s
                                                    itemqua14 = ItemService.gI().createNewItem((short) 20, 3);//7s
                                                    itemqua15 = ItemService.gI().createNewItem((short) 925, 3);//1saob
                                                    itemqua16 = ItemService.gI().createNewItem((short) 926, 3);//2saob
                                                    itemqua17 = ItemService.gI().createNewItem((short) 927, 3);//3saob
                                                    itemqua18 = ItemService.gI().createNewItem((short) 928, 3);//4saob
                                                    itemqua19 = ItemService.gI().createNewItem((short) 929, 3);//5saob
                                                    itemqua20 = ItemService.gI().createNewItem((short) 930, 3);//6saob
                                                    itemqua21 = ItemService.gI().createNewItem((short) 931, 3);//7saob

                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua1.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua2.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua3.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua4.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua4.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua5.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua6.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua7.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua8.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua9.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua10.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua11.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua12.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua13.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua14.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua15.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua16.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua17.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua18.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua19.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua20.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua21.template.name);

                                                    InventoryService.gI().addItemBag(player, itemqua, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua1, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua2, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua3, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua4, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua5, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua6, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua7, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua8, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua9, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua10, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua11, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua12, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua13, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua14, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua15, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua16, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua17, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua18, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua19, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua20, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua21, 9999);

                                                    InventoryService.gI().sendItemBags(player);
                                                } else {
                                                    Service.getInstance().sendThongBao(player, "Bạn Chưa Đủ Điều Kiện Nhận Mốc Nạp Này Hoặc đã Nhận rồi!");
                                                }
                                            } catch (Exception e) {
                                            }
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.MOC_NAP5) {
                                    switch (select) {
                                        case 0:
                                            Item itemqua;
                                            Item itemqua1;
                                            Item itemqua2;
                                            Item itemqua3;
                                            Item itemqua4;
                                            Item itemqua5;
                                            Item itemqua6;
                                            Item itemqua7;
                                            Item itemqua8;
                                            Item itemqua9;
                                            Item itemqua10;
                                            Item itemqua11;
                                            Item itemqua12;
                                            Item itemqua13;
                                            Item itemqua14;
                                            Item itemqua15;
                                            Item itemqua16;
                                            try {
                                                int time = 5;

                                                if (player.getSession().tongnap >= 500000 && player.mocnap == 200000) {
                                                    if (InventoryService.gI().getCountEmptyBag(player) < 17) {
                                                        Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 17 ô trống hành trang");
                                                        return;
                                                    }
                                                    Service.getInstance().sendThongBao(player, "Tiến Hành Nhận\nMốc Nạp 500K\nSau " + time + " Giây!");
                                                    while (time > 0) {
                                                        time--;
                                                        Thread.sleep(1000);
                                                        Service.getInstance().sendThongBao(player, "|7|" + time);
                                                    }
                                                    player.mocnap = 500000;//500k
                                                    itemqua = ItemService.gI().createNewItem((short) 1150, 50);//cuồng nộ 2
                                                    itemqua1 = ItemService.gI().createNewItem((short) 1151, 50);//bổ khí 2
                                                    itemqua2 = ItemService.gI().createNewItem((short) 1152, 50);//bổ huyết 2
                                                    itemqua3 = ItemService.gI().createNewItem((short) 1153, 50);//giáp 2
                                                    itemqua4 = ItemService.gI().createNewItem((short) 1376, 20);//hòm linh thú
                                                    itemqua5 = ItemService.gI().createNewItem((short) 1389, 20);//hop quà sk
                                                    itemqua6 = ItemService.gI().createNewItem((short) 457, 300);//tv
                                                    itemqua7 = ItemService.gI().createNewItem((short) 1450, 999);//đá cam
                                                    itemqua8 = ItemService.gI().createNewItem((short) 1379, 600);//đá pháp sư
                                                    itemqua9 = ItemService.gI().createNewItem((short) 925, 10);//1saob
                                                    itemqua10 = ItemService.gI().createNewItem((short) 926, 10);//2saob
                                                    itemqua11 = ItemService.gI().createNewItem((short) 927, 10);//3saob
                                                    itemqua12 = ItemService.gI().createNewItem((short) 928, 10);//4saob
                                                    itemqua13 = ItemService.gI().createNewItem((short) 929, 10);//5saob
                                                    itemqua14 = ItemService.gI().createNewItem((short) 930, 10);//6saob
                                                    itemqua15 = ItemService.gI().createNewItem((short) 931, 10);//7saob
                                                    itemqua16 = ItemService.gI().createNewItem((short) 1328, 30);//hit

                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua1.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua2.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua3.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua4.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua4.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua5.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua6.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua7.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua8.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua9.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua10.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua11.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua12.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua13.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua14.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua15.template.name);
                                                    Service.getInstance().sendThongBao(player, "Bạn Đã Nhận Được " + itemqua16.template.name);

                                                    InventoryService.gI().addItemBag(player, itemqua, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua1, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua2, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua3, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua4, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua5, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua6, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua7, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua8, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua9, 99999);
                                                    InventoryService.gI().addItemBag(player, itemqua10, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua11, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua12, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua13, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua14, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua15, 9999);
                                                    InventoryService.gI().addItemBag(player, itemqua16, 9999);

                                                    InventoryService.gI().sendItemBags(player);
                                                } else {
                                                    Service.getInstance().sendThongBao(player, "Bạn Chưa Đủ Điều Kiện Nhận Mốc Nạp Này Hoặc đã Nhận rồi!");
                                                }
                                            } catch (Exception e) {
                                            }
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.MOC_BOS1) {
                                    switch (select) {
                                        case 0:
                                            UseItem.gI().ComfirmMocSb(player);
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_MO_THANH_VIEN) {
                                    if (select == 0) {
                                        if (player.thanhVien == false) {
                                            if (player.soDuVND >= 10000) {
                                                Item thoivang = ItemService.gI().createNewItem((short) 457, 10);
                                                player.thanhVien = true;
                                                player.soDuVND -= 10000;
                                                PlayerDAO.subVndBar(player, 10000);
                                                PlayerDAO.moThanhVien(player);
                                                InventoryService.gI().addItemBag(player, thoivang, 99999);
                                                InventoryService.gI().sendItemBags(player);
                                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + thoivang.getName());
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn không đủ số dư để mở thành viên");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn đã mở thành viên rồi");
                                        }

                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DOI_VANG) {
                                    switch (select) {
                                        case 0:
                                            processThoiVangPurchase(player, 10_000, 30);
                                            break;
                                        case 1:
                                            processThoiVangPurchase(player, 20_000, 60);
                                            break;
                                        case 2:
                                            processThoiVangPurchase(player, 30_000, 125);
                                            break;
                                        case 3:
                                            processThoiVangPurchase(player, 50_000, 160);
                                            break;
                                        case 4:
                                            processThoiVangPurchase(player, 100_000, 320);
                                            break;
                                        case 5:
                                            processThoiVangPurchase(player, 200_000, 640);
                                            break;
                                        case 6:
                                            processThoiVangPurchase(player, 500_000, 1600);
                                            break;
                                        case 7:
                                            processThoiVangPurchase(player, 1_000_000, 3200);
                                            break;
                                    }
                                }
                            }
                        }
                    };
                    break;

                case ConstNpc.BUNMA:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                    if (player.gender == ConstPlayer.TRAI_DAT) {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                "Cậu cần trang bị gì cứ đến chỗ tôi nhé", "Cửa\nhàng");
                                    } else {
                                        NpcService.gI().createTutorial(player, this.avartar, "Xin lỗi cưng, chị chỉ bán đồ cho người Trái Đất");
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:// Shop
                                            this.openShopWithGender(player, ConstNpc.SHOP_BUNMA_QK_0, 0);
                                            break;
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.DENDE:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                    if (player.isHoldNamecBall) {
                                        this.createOtherMenu(player, ConstNpc.ORTHER_MENU,
                                                "Ô,ngọc rồng Namek,anh thật may mắn,nếu tìm đủ 7 viên ngọc có thể triệu hồi Rồng Thần Namek,",
                                                "Gọi rồng", "Từ chối");
                                    } else {
                                        if (player.gender == ConstPlayer.NAMEC) {
                                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                    "Anh cần trang bị gì cứ đến chỗ em nhé", "Cửa\nhàng");
                                        } else {
                                            NpcService.gI().createTutorial(player, this.avartar, "Xin lỗi anh, em chỉ bán đồ cho dân tộc Namếc");
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:// Shop
                                            this.openShopWithGender(player, ConstNpc.SHOP_DENDE_0, 0);
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.ORTHER_MENU) {
                                    NamekBallWar.gI().summonDragon(player, this);
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.APPULE:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                    if (player.gender == ConstPlayer.XAYDA) {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                "Ngươi cần trang bị gì cứ đến chỗ ta nhé", "Cửa\nhàng");
                                    } else {
                                        NpcService.gI().createTutorial(player, this.avartar, "Về hành tinh hạ đẳng của ngươi mà mua đồ cùi nhé. Tại đây ta chỉ bán đồ cho người Xayda thôi");
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:// Shop

                                            this.openShopWithGender(player, ConstNpc.SHOP_APPULE_0, 0);

                                            break;
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.DR_DRIEF:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player pl) {
                            if (canOpenNpc(pl)) {
                                if (this.mapId == 84) {
                                    this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                            "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                            pl.gender == ConstPlayer.TRAI_DAT ? "Đến\nTrái Đất"
                                                    : pl.gender == ConstPlayer.NAMEC ? "Đến\nNamếc" : "Đến\nXayda");
                                } else if (this.mapId == 153) {
//                                    Clan clan = pl.clan;
//                                    ClanMember cm = pl.clanMember;
//                                    if (cm.role == Clan.LEADER) {
//                                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
//                                                "Cần 1000 capsule bang [đang có " + clan.clanPoint
//                                                + " capsule bang] để nâng cấp bang hội lên cấp "
//                                                + (clan.level++) + "\n"
//                                                + "+1 tối đa số lượng thành viên",
//                                                "Về\nĐảoKame", "Góp " + cm.memberPoint + " capsule", "Nâng cấp",
//                                                "Từ chối");
//                                    } else {
//                                        this.createOtherMenu(pl, ConstNpc.BASE_MENU, "Bạn đang có " + cm.memberPoint
//                                                + " capsule bang,bạn có muốn đóng góp toàn bộ cho bang hội của mình không ?",
//                                                "Về\nĐảoKame", "Đồng ý", "Từ chối");
//                                    }
                                    super.openBaseMenu(pl);
                                    return;
                                } else if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                                    if (pl.playerTask.taskMain.id == 7) {
                                        NpcService.gI().createTutorial(pl, this.avartar,
                                                "Hãy lên đường cứu đứa bé nhà tôi\n"
                                                + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                                    } else {
                                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                                "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                                "Đến\nNamếc", "Đến\nXayda", "Siêu thị");
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 84) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 24, -1, -1);
                                } else if (mapId == 153) {
                                    if (select == 0) {
                                        ChangeMapService.gI().changeMap(player, ConstMap.DAO_KAME, -1, 638, 624);
                                        return;
                                    }
                                    Clan clan = player.clan;
                                    ClanMember cm = player.clanMember;
                                    if (select == 1) {
                                        player.clan.clanPoint += cm.memberPoint;
                                        cm.clanPoint += cm.memberPoint;
                                        cm.memberPoint = 0;
                                        Service.getInstance().sendThongBao(player, "Đóng góp thành công");
                                    } else if (select == 2 && cm.role == Clan.LEADER) {
                                        if (clan.level >= 5) {
                                            Service.getInstance().sendThongBao(player,
                                                    "Bang hội của bạn đã đạt cấp tối đa");
                                            return;
                                        }
                                        if (clan.clanPoint < 1000) {
                                            Service.getInstance().sendThongBao(player, "Không đủ capsule");
                                            return;
                                        }
                                        clan.level++;
                                        clan.maxMember++;
                                        clan.clanPoint -= 1000;
                                        Service.getInstance().sendThongBao(player,
                                                "Bang hội của bạn đã được nâng cấp lên cấp " + clan.level);
                                    }
                                } else if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:
                                            ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                            break;
                                        case 1:
                                            ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                            break;
                                        case 2:
                                            ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                            break;
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.CARGO:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player pl) {
                            if (canOpenNpc(pl)) {
                                if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                                    if (pl.playerTask.taskMain.id == 7) {
                                        NpcService.gI().createTutorial(pl, this.avartar,
                                                "Hãy lên đường cứu đứa bé nhà tôi\n"
                                                + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                                    } else {
                                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                                "Tàu vũ trụ Namếc tuy cũ nhưng tốc độ không hề kém bất kỳ loại tàu nào khác. Cậu muốn đi đâu?",
                                                "Đến\nTrái Đất", "Đến\nXayda", "Siêu thị");
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:
                                            ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                            break;
                                        case 1:
                                            ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                            break;
                                        case 2:
                                            ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                            break;
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.CUI:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {

                        private final int COST_FIND_BOSS = 20000000;

                        @Override
                        public void openBaseMenu(Player pl) {
                            if (canOpenNpc(pl)) {
                                if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                                    if (pl.playerTask.taskMain.id == 7) {
                                        NpcService.gI().createTutorial(pl, this.avartar,
                                                "Hãy lên đường cứu đứa bé nhà tôi\n"
                                                + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                                    } else {
                                        if (this.mapId == 19) {

                                            int taskId = TaskService.gI().getIdTask(pl);
                                            switch (taskId) {
                                                case ConstTask.TASK_21_0:
                                                    this.createOtherMenu(pl, ConstNpc.MENU_FIND_KUKU,
                                                            "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                            "Đến chỗ\nKuku\n(" + Util.numberToMoney(COST_FIND_BOSS)
                                                            + " vàng)",
                                                            "Đến Cold", "Đến\nNappa", "Từ chối");
                                                    break;
                                                case ConstTask.TASK_21_1:
                                                    this.createOtherMenu(pl, ConstNpc.MENU_FIND_MAP_DAU_DINH,
                                                            "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                            "Đến chỗ\nMập đầu đinh\n("
                                                            + Util.numberToMoney(COST_FIND_BOSS) + " vàng)",
                                                            "Đến Cold", "Đến\nNappa", "Từ chối");
                                                    break;
                                                case ConstTask.TASK_21_2:
                                                    this.createOtherMenu(pl, ConstNpc.MENU_FIND_RAMBO,
                                                            "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                            "Đến chỗ\nRambo\n(" + Util.numberToMoney(COST_FIND_BOSS)
                                                            + " vàng)",
                                                            "Đến Cold", "Đến\nNappa", "Từ chối");
                                                    break;
                                                default:
                                                    this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                                            "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                            "Đến Cold", "Đến\nNappa", "Từ chối");

                                                    break;
                                            }
                                        } else if (this.mapId == 68) {
                                            this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                                    "Ngươi muốn về Thành Phố Vegeta", "Đồng ý", "Từ chối");
                                        } else {
                                            this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                                    "Tàu vũ trụ Xayda sử dụng công nghệ mới nhất, có thể đưa ngươi đi bất kỳ đâu, chỉ cần trả tiền là được.",
                                                    "Đến\nTrái Đất", "Đến\nNamếc", "Siêu thị");
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 26) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                                break;
                                            case 1:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                                break;
                                            case 2:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                                break;
                                        }
                                    }
                                }
                                if (this.mapId == 19) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                                break;
                                            case 1:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_KUKU) {
                                        switch (select) {
                                            case 0:
                                                Boss boss = BossManager.gI().getBossById(BossFactory.KUKU);
                                                if (boss != null && !boss.isDie()) {
                                                    if (player.inventory.gold >= COST_FIND_BOSS) {
                                                        player.inventory.gold -= COST_FIND_BOSS;
                                                        ChangeMapService.gI().changeMap(player, boss.zone,
                                                                boss.location.x, boss.location.y);
                                                        Service.getInstance().sendMoney(player);
                                                    } else {
                                                        Service.getInstance().sendThongBao(player,
                                                                "Không đủ vàng, còn thiếu "
                                                                + Util.numberToMoney(
                                                                        COST_FIND_BOSS - player.inventory.gold)
                                                                + " vàng");
                                                    }
                                                }
                                                break;
                                            case 1:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                                break;
                                            case 2:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_MAP_DAU_DINH) {
                                        switch (select) {
                                            case 0:
                                                Boss boss = BossManager.gI().getBossById(BossFactory.MAP_DAU_DINH);
                                                if (boss != null && !boss.isDie()) {
                                                    if (player.inventory.gold >= COST_FIND_BOSS) {
                                                        player.inventory.gold -= COST_FIND_BOSS;
                                                        ChangeMapService.gI().changeMap(player, boss.zone,
                                                                boss.location.x, boss.location.y);
                                                        Service.getInstance().sendMoney(player);
                                                    } else {
                                                        Service.getInstance().sendThongBao(player,
                                                                "Không đủ vàng, còn thiếu "
                                                                + Util.numberToMoney(
                                                                        COST_FIND_BOSS - player.inventory.gold)
                                                                + " vàng");
                                                    }
                                                }
                                                break;
                                            case 1:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                                break;
                                            case 2:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_RAMBO) {
                                        switch (select) {
                                            case 0:
                                                Boss boss = BossManager.gI().getBossById(BossFactory.RAMBO);
                                                if (boss != null && !boss.isDie()) {
                                                    if (player.inventory.gold >= COST_FIND_BOSS) {
                                                        player.inventory.gold -= COST_FIND_BOSS;
                                                        ChangeMapService.gI().changeMap(player, boss.zone,
                                                                boss.location.x, boss.location.y);
                                                        Service.getInstance().sendMoney(player);
                                                    } else {
                                                        Service.getInstance().sendThongBao(player,
                                                                "Không đủ vàng, còn thiếu "
                                                                + Util.numberToMoney(
                                                                        COST_FIND_BOSS - player.inventory.gold)
                                                                + " vàng");
                                                    }
                                                }
                                                break;
                                            case 1:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                                break;
                                            case 2:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                                break;
                                        }
                                    }
                                }
                                if (this.mapId == 68) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 19, -1, 1100);
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.SANTA:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "Xin chào, ta có một số vật phẩm đặt biệt cậu có muốn xem không?",
                                        "Cửa hàng",
                                        "Mở rộng\nHành trang\nRương đồ",
                                        "Nhập mã\n quà tặng",
                                        "Tiệm\nHớt tóc");
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 5 || this.mapId == 13 || this.mapId == 20) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0: // shop
                                                this.openShopWithGender(player, ConstNpc.SHOP_SANTA_0, 0);
                                                break;
                                            case 1:
                                                this.openShopWithGender(player, ConstNpc.SHOP_SANTA_2, 2);
                                                break;
                                            case 2: // giftcode
                                                Input.gI().createFormGiftCode(player);
                                                break;

                                            case 3: // tiệm hớt tóc
                                                this.openShopWithGender(player, ConstNpc.SHOP_SANTA_1, 1);
                                                break;

                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.URON:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player pl) {
                            if (canOpenNpc(pl)) {
                                this.openShopWithGender(pl, ConstNpc.SHOP_URON_0, 0);
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {

                            }
                        }
                    };
                    break;
                case ConstNpc.BA_HAT_MIT:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {

                        @Override
                        public void openBaseMenu(Player player) {
                            Item bongTai = InventoryService.gI().findItemBagByTemp(player, (short) 454);
                            Item bongTaiCap2 = InventoryService.gI().findItemBagByTemp(player, (short) 921);
                            if (canOpenNpc(player)) {
                                if (this.mapId == 5) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ngươi tìm ta có việc gì?",
                                            "Chức năng\nPha lê",
                                            //                                            "Võ đài\nSinh tử",
                                            //                                            "Nâng Sét\n kích hoạt",
                                            //                                            "Trang Bị\n Thiên Sứ",
                                            //                                            "Nâng cấp\n chân quang",
                                            //                                           "Pháp sư hoá\ntrang bị",
                                            //                                           "Tẩy\npháp sư",
                                            //                                            "Luyện\nlinh thú",

                                            "Pháp sư \nTrang bị",
                                            "Mở nội tại\n cải trang");
//                                            "Nâng cấp \npet theo sau",
//                                            "Đến Địa Ngục");
                                } else if (this.mapId == 112) {
                                    if (player.DoneVoDaiBaHatMit == 1) {
                                        this.createOtherMenu(player, ConstNpc.NHAN_QUA_VO_DAI, "Đây là phẩn thưởng của con.", "1 vệ tinh\n bất kì", "1 bùa 1h\n bất kỳ");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ngươi muốn đăng ký thi đấu võ đài?\n"
                                                + "nhiều phần thưởng giá trị đang đợi ngươi đó", "Top 100", "Đồng ý\n0 ngọc", "Từ chối", "Về\nđảo rùa");
                                    }
                                } else if (this.mapId == 217) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ngươi tìm ta có việc gì?",
                                            "Nâng cấp\nChân quang");
                                } else if (this.mapId == 213) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ngươi tìm ta có việc gì?",
                                            "Nâng cấp\ntrang bị\nkích hoạt", "Từ chối");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ngươi tìm ta có việc gì?",
                                            "Cửa hàng\n Bùa", "Nâng cấp\n Vật phẩm", "Nâng Cấp\nBông Tai", "Làm phép\nNhập đá", "Nhập\nNgọc Rồng");

                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            Item bongTai = InventoryService.gI().findItemBagByTemp(player, (short) 454);
                            Item bongTaiCap2 = InventoryService.gI().findItemBagByTemp(player, (short) 921);
                            if (canOpenNpc(player)) {
                                if (this.mapId == 217) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CHAN_MENH);
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                                        switch (player.combineNew.typeCombine) {
                                            case CombineServiceNew.NANG_CHAN_MENH:
                                                if (select == 0) {
                                                    CombineServiceNew.gI().startCombine(player, select);
                                                }
                                                break;
                                        }
                                    }
                                } else if (this.mapId == 213) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.DAP_SET_KICH_HOAT_CAO_CAP);
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                                        switch (player.combineNew.typeCombine) {
                                            case CombineServiceNew.DAP_SET_KICH_HOAT_CAO_CAP:
                                                if (select == 0) {
                                                    CombineServiceNew.gI().startCombine(player, select);
                                                }
                                                break;
                                        }
                                    }
                                } else if (this.mapId == 5) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                createOtherMenu(player, 3333,
                                                        "Ta có thể giúp gì cho ngươi",
                                                        "Ép sao\ntrang bị",
                                                        "Pha lê\nhóa\ntrang bị",
                                                        //                                                        "Pha lê\nhóa\nCải Trang",
                                                        "Cường hóa\nLỗ sao pha lê");
                                                break;
//                                            case 3:
//                                                createOtherMenu(player, ConstNpc.MENU_THIEN_SU, "Ta sẽ nâng trang bị thiên sứ của người\nlên một tầm cao mới hoàn toàn khác",
//                                                        "Nâng Cấp\nThiên Sứ",
//                                                        "Nâng Cấp\nTrang Bị\nẤn");
//                                                break;
//                                            case 1:
//                                                createOtherMenu(player, ConstNpc.MENU_CHUYEN_HOA_SKH, "Ta sẽ nâng trang bị hủy diệt của người\nlên một tầm cao mới hoàn toàn khác",
//                                                        "Nâng cấp\nSKH thường",
//                                                        //"Up chỉ số\n set kích hoạt",
//                                                        "Nâng cấp\nSKH VIP");
//                                                break;
//                                            case 1:
//                                                ChangeMapService.gI().changeMap(player, 112, -1, 55, 408);
//                                                break;
//                                            case 3:
//                                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHE_TAO_DO_THIEN_SU);
//                                                break;
//                                            case 1:
//                                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CHAN_MENH);
//                                                break;
//                                            case 5:
//                                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.PHAP_SU_HOA);
//                                                break;
//                                            case 6:
//                                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.TAY_PHAP_SU);
//                                                break;
//                                            case 5:
//                                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_PET);
//                                                break;
//                                            
                                            case 1:
                                                this.createOtherMenu(player, ConstNpc.MENU_PHAP_SU,
                                                        "Nâng cấp trang bị pháp sư giúp tăng ngẫu nhiên các chỉ số HP, KI, SĐ\n"
                                                        + "ngoài ra ngươi cũng có thể xóa dòng bóng tối khi có Bùa pháp sư.\n"
                                                        + "Ngươi muốn ta giúp gì?", "Nâng cấp\nPháp sư", "Xóa dòng\nPháp sư");
                                                break;
                                            case 2:
                                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_NOI_TAI_ITEM);
                                                break;
//                                            case 8:
//                                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_PET);
//                                                break;
//                                            case 9:
//                                                ChangeMapService.gI().changeMap(player, 208, -1, 705, 432);
//                                                break;
                                            }
                                    } else if (player.iDMark.getIndexMenu() == 3333) {
                                        switch (select) {
                                            case 0:
                                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.EP_SAO_TRANG_BI);
                                                break;
                                            case 1:
                                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.PHA_LE_HOA_TRANG_BI);
                                                break;

                                            case 2:
                                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CUONG_HOA_LO_SPL);
                                                break;
//                                            case 3:
//                                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.PHA_LE_HOA_CAI_TRANG);
//                                                break;

                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CHUYEN_HOA_SKH) {
                                        switch (select) {
                                            case 0:
                                                if (!player.setClothes.godClothes) {
                                                    this.npcChat(player, "Con phải mặc đủ 5 món Thần Linh");
                                                    return;
                                                }
                                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.DAP_SET_KICH_HOAT);
                                                break;

                                            case 1: // NANG CAP SAO PHA LE
                                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.DAP_SET_KICH_HOAT_CAO_CAP);
                                                break;
//                                            case 2: // NANG CAP SAO PHA LE
//                                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_SKH);
//                                                break;
                                            }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_THIEN_SU) {
                                        switch (select) {
                                            case 0:
                                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHE_TAO_DO_THIEN_SU);
                                                break;
                                            case 1: // NANG CAP SAO PHA LE
                                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.AN_TRANG_BI);
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHA_LE_HOA_TRANG_BI) {
                                        switch (select) {
                                            case 0:
                                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.PHA_LE_HOA_TRANG_BI);
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CHUYEN_HOA_TRANG_BI) {
                                        switch (select) {
                                            case 0:
                                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHUYEN_HOA_BANG_VANG);
                                                break;
                                            case 1:
                                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHUYEN_HOA_BANG_NGOC);
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CHUYEN_HOA_TRANG_BI) {
                                        switch (select) {
                                            case 0:
                                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHUYEN_HOA_BANG_VANG);
                                                break;
                                            case 1:
                                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHUYEN_HOA_BANG_NGOC);
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHAP_SU) {
                                        switch (select) {
                                            case 0:
                                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.PHAP_SU_HOA);
                                                break;
                                            case 1:
                                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.TAY_PHAP_SU);
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                                        switch (player.combineNew.typeCombine) {
                                            case CombineServiceNew.EP_SAO_TRANG_BI:
                                            case CombineServiceNew.PHA_LE_HOA_TRANG_BI:
                                            case CombineServiceNew.DOI_VE_HUY_DIET:
                                            case CombineServiceNew.DAP_SET_KICH_HOAT:
//                                            case CombineServiceNew.DAP_SET_KICH_HOAT_CAO_CAP:
                                            case CombineServiceNew.CHUYEN_HOA_BANG_VANG:
                                            case CombineServiceNew.CHUYEN_HOA_BANG_NGOC:
                                            case CombineServiceNew.GIA_HAN_CAI_TRANG:
                                            case CombineServiceNew.NANG_CAP_SKH:
                                            case CombineServiceNew.CHE_TAO_DO_THIEN_SU:
//                                            case CombineServiceNew.NANG_CHAN_MENH:
                                            case CombineServiceNew.PHAP_SU_HOA:
                                            case CombineServiceNew.TAY_PHAP_SU:
                                            case CombineServiceNew.NANG_CAP_PET:
                                            case CombineServiceNew.MO_NOI_TAI_ITEM:
                                            case CombineServiceNew.CUONG_HOA_LO_SPL:
                                            case CombineServiceNew.NANG_PET:
                                            case ConstCombine.REMOVE_OPTION:
                                            case CombineServiceNew.PHA_LE_HOA_CAI_TRANG:
                                            case CombineServiceNew.AN_TRANG_BI:
                                                CombineServiceNew.gI().startCombine(player, select);
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_CAP_DO_TS) {
                                        if (select == 0) {
                                            CombineServiceNew.gI().startCombine(player, select);
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstCombine.REMOVE_OPTION) {
                                        switch (select) {
                                            case 0:
                                                CombineServiceNew.gI().openTabCombine(player,
                                                        ConstCombine.REMOVE_OPTION);
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.ORTHER_MENU) {
                                        switch (select) {
                                            case 0:
                                                CombineServiceNew.gI().openTabCombine(player,
                                                        CombineServiceNew.DAP_SET_KICH_HOAT);
                                                break;
                                            case 1:
                                                CombineServiceNew.gI().openTabCombine(player,
                                                        CombineServiceNew.DAP_SET_KICH_HOAT_CAO_CAP);
                                                break;

                                        }
                                    }
//                                    else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
//                                        switch (player.combineNew.typeCombine) {
//                                            case CombineServiceNew.NANG_CHAN_MENH:
//                                                if (select == 0) {
//                                                    CombineServiceNew.gI().startCombine(player, select);
//                                                }
//                                                break;
//                                        }
//                                    }
                                } else if (this.mapId == 112) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:// Top 100 gì đó đéo biết

                                                break;
                                            case 1:// xác nhận lên võ đài
                                                VoDaiSinhTuService.gI().startChallenge(player);
                                                break;
                                            case 2:// từ chối

                                                break;
                                            case 3:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 1156);
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.NHAN_QUA_VO_DAI) {
                                        switch (select) {
                                            case 0:
                                                if (player.DoneVoDaiBaHatMit == 1) {
                                                    player.DoneVoDaiBaHatMit = 0;
                                                    Item vetinh = ItemService.gI().createNewItem((short) Util.nextInt(342, 345), 1);
                                                    InventoryService.gI().addItemBag(player, vetinh, 9999);
                                                    InventoryService.gI().sendItemBags(player);
                                                    Service.getInstance().sendMoney(player);
                                                    Service.getInstance().sendThongBao(player, "Bạn đã nhận được 1 vệ tinh ngẫu nhiên");
                                                    break;
                                                } else {
                                                    Service.getInstance().sendThongBao(player, "Bạn đã nhận phần thưởng này rồi");
                                                }
                                                break;
                                            case 1:
                                                if (player.DoneVoDaiBaHatMit == 1) {
                                                    player.DoneVoDaiBaHatMit = 0;
                                                    Item vetinh = ItemService.gI().createNewItem((short) Util.nextInt(1150, 1153), 1);
                                                    InventoryService.gI().addItemBag(player, vetinh, 9999);
                                                    InventoryService.gI().sendItemBags(player);
                                                    Service.getInstance().sendMoney(player);
                                                } else {
                                                    Service.getInstance().sendThongBao(player, "Bạn đã nhận rồi");
                                                }
                                                break;
                                        }
                                    }
                                } else if (this.mapId == 42 || this.mapId == 43 || this.mapId == 44 || this.mapId == 84) { // BA_HAT_MIT_BUA
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0: // shop bùa
                                                createOtherMenu(player, ConstNpc.MENU_OPTION_SHOP_BUA,
                                                        "Bùa của ta rất lợi hại, nhìn ngươi yếu đuối thế này, chắc muốn mua bùa để "
                                                        + "mạnh mẽ à, mua không ta bán cho, xài rồi lại thích cho mà xem.",
                                                        "Bùa\n1 giờ", "Bùa\n8 giờ", "Bùa\n1 tháng",
                                                        "Đóng");
                                                break;
                                            case 1: // nâng cấp vật phẩm
                                                CombineServiceNew.gI().openTabCombine(player,
                                                        CombineServiceNew.NANG_CAP_VAT_PHAM);
                                                break;
                                            case 2: // bông tai
                                                createOtherMenu(player, ConstNpc.MENU_PORATA,
                                                        "Nâng cấp bông tai đi nào!",
                                                        "Nâng cấp\nBông tai", "Mở chỉ số\nBông tai\nCấp 2", "Nâng cấp\nNgọc vô cực",
                                                        "Mở chỉ số\nNgọc vô cực");
//                                                createOtherMenu(player, ConstNpc.MENU_PORATA,
//                                                        "Nâng cấp bông tai đi nào!",
//                                                        "Nâng cấp\nBông tai", "Mở chỉ số\nBông tai\nCấp 2");
                                                break;
                                            case 3: //Làm phép nhập đá
                                                CombineServiceNew.gI().openTabCombine(player,
                                                        CombineServiceNew.LAM_PHEP_NHAP_DA);
                                                break;
                                            case 4:// 
                                                CombineServiceNew.gI().openTabCombine(player,
                                                        CombineServiceNew.NHAP_NGOC_RONG);
                                                break;
                                        }

                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.DOI_SACH_TUYET_KY) {
                                        switch (select) {
                                            case 0:
                                                Item cuonSachCu = InventoryService.gI().findItemBagByTemp(player, 1284);
                                                Item kimBam = InventoryService.gI().findItemBagByTemp(player, 1282);

                                                short baseValue = 1287;
                                                short genderModifier = (player.gender == 0) ? -2 : ((player.gender == 2) ? 2 : (short) 0);

                                                Item sachTuyetKy = ItemService.gI().createNewItem((short) (baseValue + genderModifier));

                                                if (Util.isTrue(20, 100)) {

                                                    sachTuyetKy.itemOptions.add(new ItemOption(229, 0));
                                                    sachTuyetKy.itemOptions.add(new ItemOption(21, 40));
                                                    sachTuyetKy.itemOptions.add(new ItemOption(30, 0));
                                                    sachTuyetKy.itemOptions.add(new ItemOption(87, 1));
                                                    sachTuyetKy.itemOptions.add(new ItemOption(230, 5));
                                                    sachTuyetKy.itemOptions.add(new ItemOption(231, 1000));
                                                    try { // send effect susscess
                                                        Message msg = new Message(-81);
                                                        msg.writer().writeByte(0);
                                                        msg.writer().writeUTF("test");
                                                        msg.writer().writeUTF("test");
                                                        msg.writer().writeShort(tempId);
                                                        player.sendMessage(msg);
                                                        msg.cleanup();
                                                        msg = new Message(-81);
                                                        msg.writer().writeByte(1);
                                                        msg.writer().writeByte(2);
                                                        msg.writer().writeByte(InventoryService.gI().getIndexBag(player, kimBam));
                                                        msg.writer().writeByte(InventoryService.gI().getIndexBag(player, cuonSachCu));
                                                        player.sendMessage(msg);
                                                        msg.cleanup();
                                                        msg = new Message(-81);
                                                        msg.writer().writeByte(7);
                                                        msg.writer().writeShort(sachTuyetKy.template.iconID);
                                                        msg.writer().writeShort(-1);
                                                        msg.writer().writeShort(-1);
                                                        msg.writer().writeShort(-1);
                                                        player.sendMessage(msg);
                                                        msg.cleanup();
                                                    } catch (Exception e) {
                                                    }
                                                    InventoryService.gI().addItemList(player.inventory.itemsBag, sachTuyetKy, 1);
                                                    InventoryService.gI().subQuantityItemsBag(player, cuonSachCu, 10);
                                                    InventoryService.gI().subQuantityItemsBag(player, kimBam, 1);
                                                    InventoryService.gI().sendItemBags(player);
                                                    return;
                                                } else {
                                                    try { // send effect faile
                                                        Message msg = new Message(-81);
                                                        msg.writer().writeByte(0);
                                                        msg.writer().writeUTF("test");
                                                        msg.writer().writeUTF("test");
                                                        msg.writer().writeShort(tempId);
                                                        player.sendMessage(msg);
                                                        msg.cleanup();
                                                        msg = new Message(-81);
                                                        msg.writer().writeByte(1);
                                                        msg.writer().writeByte(2);
                                                        msg.writer().writeByte(InventoryService.gI().getIndexBag(player, kimBam));
                                                        msg.writer().writeByte(InventoryService.gI().getIndexBag(player, cuonSachCu));
                                                        player.sendMessage(msg);
                                                        msg.cleanup();
                                                        msg = new Message(-81);
                                                        msg.writer().writeByte(8);
                                                        msg.writer().writeShort(-1);
                                                        msg.writer().writeShort(-1);
                                                        msg.writer().writeShort(-1);
                                                        player.sendMessage(msg);
                                                        msg.cleanup();
                                                    } catch (Exception e) {
                                                    }
                                                    InventoryService.gI().subQuantityItemsBag(player, cuonSachCu, 5);
                                                    InventoryService.gI().subQuantityItemsBag(player, kimBam, 1);
                                                    InventoryService.gI().sendItemBags(player);
                                                }
                                                return;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.DONG_THANH_SACH_CU) {
                                        switch (select) {
                                            case 0:
                                                Item trangSachCu = InventoryService.gI().findItemBagByTemp(player, 1291);
                                                Item biaSach = InventoryService.gI().findItemBagByTemp(player, 1281);
                                                Item cuonSachCu = ItemService.gI().createNewItem((short) 1284);
                                                if (Util.isTrue(20, 100)) {
                                                    cuonSachCu.itemOptions.add(new ItemOption(30, 0));

                                                    try { // send effect susscess

                                                        Message msg = new Message(-81);
                                                        msg.writer().writeByte(0);
                                                        msg.writer().writeUTF("test");
                                                        msg.writer().writeUTF("test");
                                                        msg.writer().writeShort(tempId);
                                                        player.sendMessage(msg);
                                                        msg.cleanup();

                                                        msg = new Message(-81);
                                                        msg.writer().writeByte(1);
                                                        msg.writer().writeByte(2);
                                                        msg.writer().writeByte(InventoryService.gI().getIndexBag(player, trangSachCu));
                                                        msg.writer().writeByte(InventoryService.gI().getIndexBag(player, biaSach));
                                                        player.sendMessage(msg);
                                                        msg.cleanup();

                                                        msg = new Message(-81);
                                                        msg.writer().writeByte(7);
                                                        msg.writer().writeShort(cuonSachCu.template.iconID);
                                                        player.sendMessage(msg);
                                                        msg.cleanup();
                                                    } catch (Exception e) {
                                                    }

                                                    InventoryService.gI().addItemList(player.inventory.itemsBag, cuonSachCu, 99);
                                                    InventoryService.gI().subQuantityItemsBag(player, trangSachCu, 9999);
                                                    InventoryService.gI().subQuantityItemsBag(player, biaSach, 1);
                                                    InventoryService.gI().sendItemBags(player);
                                                    return;
                                                } else {
                                                    try { // send effect faile
                                                        Message msg = new Message(-81);
                                                        msg.writer().writeByte(0);
                                                        msg.writer().writeUTF("test");
                                                        msg.writer().writeUTF("test");
                                                        msg.writer().writeShort(tempId);
                                                        player.sendMessage(msg);
                                                        msg.cleanup();

                                                        msg = new Message(-81);
                                                        msg.writer().writeByte(1);
                                                        msg.writer().writeByte(2);
                                                        msg.writer().writeByte(InventoryService.gI().getIndexBag(player, biaSach));
                                                        msg.writer().writeByte(InventoryService.gI().getIndexBag(player, trangSachCu));
                                                        player.sendMessage(msg);
                                                        msg.cleanup();

                                                        msg = new Message(-81);
                                                        msg.writer().writeByte(8);
                                                        player.sendMessage(msg);
                                                        msg.cleanup();
                                                    } catch (Exception e) {
                                                    }
                                                    InventoryService.gI().subQuantityItemsBag(player, trangSachCu, 99);
                                                    InventoryService.gI().subQuantityItemsBag(player, biaSach, 1);
                                                    InventoryService.gI().sendItemBags(player);
                                                }
                                                return;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_SHOP_BUA) {
                                        switch (select) {
                                            case 0:
                                                ShopService.gI().openShopBua(player, ConstNpc.SHOP_BA_HAT_MIT_0, 0);
                                                break;
                                            case 1:
                                                ShopService.gI().openShopBua(player, ConstNpc.SHOP_BA_HAT_MIT_1, 1);
                                                break;
                                            case 2:
                                                ShopService.gI().openShopBua(player, ConstNpc.SHOP_BA_HAT_MIT_2, 2);
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PORATA) {
                                        switch (select) {
                                            case 0:
                                                CombineServiceNew.gI().openTabCombine(player,
                                                        CombineServiceNew.NANG_CAP_BONG_TAI);
                                                break;
                                            case 1:
                                                CombineServiceNew.gI().openTabCombine(player,
                                                        CombineServiceNew.MO_CHI_SO_BONG_TAI);
                                                break;
                                            case 2:
                                                CombineServiceNew.gI().openTabCombine(player,
                                                        CombineServiceNew.NANG_CAP_BONG_TAI_VO_CUC);
                                                break;
                                            case 3:
                                                CombineServiceNew.gI().openTabCombine(player,
                                                        CombineServiceNew.MO_CHI_SO_BONG_TAI_VO_CUC);
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                                        switch (player.combineNew.typeCombine) {
                                            case CombineServiceNew.NANG_CAP_VAT_PHAM:
                                                if (select == 0) {
                                                    player.iDMark.isUseTuiBaoVeNangCap = false;
                                                    CombineServiceNew.gI().startCombine(player, select);
                                                } else if (select == 1) {
                                                    player.iDMark.isUseTuiBaoVeNangCap = true;
                                                    CombineServiceNew.gI().startCombine(player, select);
                                                }
                                                break;
                                            case CombineServiceNew.NANG_CAP_BONG_TAI:
                                            case CombineServiceNew.MO_CHI_SO_BONG_TAI:
                                            case CombineServiceNew.NANG_CAP_BONG_TAI_VO_CUC:
                                            case CombineServiceNew.MO_CHI_SO_BONG_TAI_VO_CUC:
                                            case CombineServiceNew.LAM_PHEP_NHAP_DA:
                                            case CombineServiceNew.NHAP_NGOC_RONG:
                                            //START _ SÁCH TUYỆT KỸ//
                                            case CombineServiceNew.GIAM_DINH_SACH:
                                            case CombineServiceNew.TAY_SACH:
                                            case CombineServiceNew.NANG_CAP_SACH_TUYET_KY:
                                            case CombineServiceNew.PHUC_HOI_SACH:
                                            case CombineServiceNew.PHAN_RA_SACH:
                                                //END _ SÁCH TUYỆT KỸ//
                                                CombineServiceNew.gI().startCombine(player, select);
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.RUONG_DO:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {

                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                InventoryService.gI().sendItemBox(player);
                                InventoryService.gI().openBox(player);
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {

                            }
                        }
                    };
                    break;
                case ConstNpc.DAU_THAN:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                player.magicTree.openMenuTree();
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                TaskService.gI().checkDoneTaskConfirmMenuNpc(player, this, (byte) select);
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.MAGIC_TREE_NON_UPGRADE_LEFT_PEA:
                                        if (select == 0) {
                                            player.magicTree.harvestPea();
                                        } else if (select == 1) {
                                            if (player.magicTree.level == 10) {
                                                player.magicTree.fastRespawnPea();
                                            } else {
                                                player.magicTree.showConfirmUpgradeMagicTree();
                                            }
                                        } else if (select == 2) {
                                            player.magicTree.fastRespawnPea();
                                        }
                                        break;
                                    case ConstNpc.MAGIC_TREE_NON_UPGRADE_FULL_PEA:
                                        if (select == 0) {
                                            player.magicTree.harvestPea();
                                        } else if (select == 1) {
                                            player.magicTree.showConfirmUpgradeMagicTree();
                                        }
                                        break;
                                    case ConstNpc.MAGIC_TREE_CONFIRM_UPGRADE:
                                        if (select == 0) {
                                            player.magicTree.upgradeMagicTree();
                                        }
                                        break;
                                    case ConstNpc.MAGIC_TREE_UPGRADE:
                                        if (select == 0) {
                                            player.magicTree.fastUpgradeMagicTree();
                                        } else if (select == 1) {
                                            player.magicTree.showConfirmUnuppgradeMagicTree();
                                        }
                                        break;
                                    case ConstNpc.MAGIC_TREE_CONFIRM_UNUPGRADE:
                                        if (select == 0) {
                                            player.magicTree.unupgradeMagicTree();
                                        }
                                        break;
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.CALICK:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {

                        private void changeMap_CaLich() {
                            if (this.mapId != 102) {
                                this.map.npcs.remove(this);
                                Map map = MapService.gI().getMapForCalich();
                                this.mapId = map.mapId;
                                this.cx = Util.nextInt(100, map.mapWidth - 100);
                                this.cy = map.yPhysicInTop(this.cx, 0);
                                this.map = map;
                                this.map.npcs.add(this);
                            }
                        }

                        @Override
                        public void openBaseMenu(Player player) {
                            player.iDMark.setIndexMenu(ConstNpc.BASE_MENU);
                            if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                            }
                            if (TaskService.gI().getIdTask(player) < ConstTask.TASK_20_0) {
                                Service.getInstance().hideWaitDialog(player);
                                Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                                return;
                            }
                            if (this.mapId == 102) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU, "Chào chú, cháu có thể giúp gì?",
                                        "Kể\nChuyện", "Quay về\nQuá khứ");
                            } else {
                                changeMap_CaLich();
                                this.createOtherMenu(player, ConstNpc.BASE_MENU, "Chào chú, cháu có thể giúp gì?",
                                        "Kể\nChuyện", "Đi đến\nTương lai", "Từ chối");
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (this.mapId == 102) {
                                if (player.iDMark.isBaseMenu()) {
                                    if (select == 0) {
                                        // kể chuyện
                                        NpcService.gI().createTutorial(player, this.avartar, ConstNpc.CALICK_KE_CHUYEN);
                                    } else if (select == 1) {
                                        // về quá khứ
                                        ChangeMapService.gI().goToQuaKhu(player);
                                    }
                                }
                            } else if (player.iDMark.isBaseMenu()) {
                                if (select == 0) {
                                    // kể chuyện
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.CALICK_KE_CHUYEN);
                                } else if (select == 1) {
                                    // đến tương lai
                                    // changeMap();
                                    if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_20_0) {
                                        ChangeMapService.gI().goToTuongLai(player);
                                    }
                                } else {
                                    Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                                }
                            }
                        }
                    };
                    break;

                case ConstNpc.JACO:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {

                        @Override
                        public void openBaseMenu(Player player) {
                            player.iDMark.setIndexMenu(ConstNpc.BASE_MENU);
                            if (this.mapId == 24) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU, "Gô Tên, Calích và Monaka đang gặp chuyện ở hành tinh Potaufeu\nHãy đến đó ngay", "Đến\nPotaufeu", "Từ chối");
                            } else {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU, "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?", "Đến\nTrái Đất", "Đến\nNamếc", "Đến\nXayda", "Từ chối");
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 24) {
                                    if (player.iDMark.isBaseMenu()) {
                                        if (select == 0) {
                                            // đến potaufeu
                                            ChangeMapService.gI().goToPotaufeu(player);
                                        }
                                    }
                                } else if (this.mapId == 139) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                                break;
                                            case 1:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                                break;
                                            case 2:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;

                case ConstNpc.POTAGE:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {

                        @Override
                        public void openBaseMenu(Player player) {
                            player.iDMark.setIndexMenu(ConstNpc.BASE_MENU);
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Hãy giúp ta đánh bại bản sao\nNgươi chỉ có 5 phút để hạ hắn\nPhần thưởng cho ngươi là 1 bình Commeson",
                                    "Hướng\ndẫn\nthêm", "OK", "Từ chối");
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:
                                            NpcService.gI().createTutorial(player, avartar, "Thứ bị phong ấn tại đây là vũ khí có tên Commeson\b"
                                                    + "được tạo ra nhằm bảo vệ cho hành tinh Potaufeu\b"
                                                    + "Tuy nhiên nó đã tàn phá mọi thứ trong quá khứ\n"
                                                    + "Khiến cư dân Potaufeu niêm phong nó với cái giá\b phải trả là mạng sống của họ\b Ta, Potage là người duy nhất sống sót\b"
                                                    + "và ta đã bảo vệ phong ấn hơn một trăm năm.\n"
                                                    + "Tuy nhiên bọn xâm lược Gryll đã đến và giải thoát Commeson\b"
                                                    + "Hãy giúp ta tiêu diệt bản sao do Commeson tạo ra\b"
                                                    + "và niêm phong Commeson một lần và mãi mãi");
                                            break;
                                        case 1:// gọi nhân bản
                                            if (player.zone.getBosses().size() != 0) {
                                                this.createOtherMenu(player, 251003, "Đang có 1 nhân bản của " + player.zone.getBosses().get(0).name + " hãy chờ kết quả trận đấu", "OK");
                                                return;
                                            }
                                            if (!player.itemTime.doneDanhNhanBan) {
                                                player.itemTime.isDanhNhanBan = true;
                                                player.itemTime.lasttimeDanhNhanBan = System.currentTimeMillis();

                                                ItemTimeService.gI().sendAllItemTime(player);
                                                List<Skill> skillList = new ArrayList<>();
                                                for (byte i = 0; i < player.playerSkill.skills.size(); i++) {
                                                    Skill skill = player.playerSkill.skills.get(i);
                                                    if (skill.point > 0) {
                                                        skillList.add(skill);
                                                    }
                                                }
                                                int[][] skillTemp = new int[skillList.size()][5];
                                                for (byte i = 0; i < skillList.size(); i++) {
                                                    Skill skill = skillList.get(i);
                                                    if (skill.point > 0) {
                                                        skillTemp[i][0] = skill.template.id;
                                                        skillTemp[i][1] = skill.point;
                                                        skillTemp[i][2] = skill.coolDown;
                                                    }
                                                }

                                                BossData bossdataa = BossData.builder()
                                                        .name(player.name)
                                                        .gender(player.gender)
                                                        .typeDame(Boss.DAME_NORMAL)
                                                        .typeHp(Boss.HP_NORMAL)
                                                        .dame(player.nPoint.hpMax / 10)
                                                        .hp(new int[][]{{player.nPoint.dame * 10}})
                                                        .outfit(new short[]{player.getHead(), player.getBody(), player.getLeg(), player.getFlagBag(), player.getAura(), player.getEffFront()})
                                                        .skillTemp(skillTemp)
                                                        .secondsRest(BossData._0_GIAY)
                                                        .build();

                                                try {
                                                    Boss_NhanBan dt = new Boss_NhanBan(Util.createIdDuongTank((int) ((byte) player.id)), bossdataa, player.zone, player.location.x, player.location.y, (int) player.id);
                                                } catch (Exception ex) {
                                                    Logger.getLogger(NpcFactory.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                                break;
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Hãy chờ đến ngày mai");
                                            }
                                    }
                                }
                            }
                        }
                    };
                    break;

                case ConstNpc.THAN_MEO_KARIN:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (mapId == ConstMap.THAP_KARIN) {
                                    player.thachDauNPC = 0;
                                    if (player.zone instanceof ZSnakeRoad) {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                "Hãy cầm lấy hai hạt đậu cuối cùng ở đây\nCố giữ mình nhé "
                                                + player.name,
                                                "Cảm ơn\nsư phụ");
                                    } else if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                        if (player.doneThachDauThanMeo == 0) {
                                            this.createOtherMenu(player, ConstNpc.THACH_DAU_THAN_MEO, "Muốn chiến thắng Tàu Pảy Pảy phải đánh bại được ta đã", "Đăng ký\n tập\n tự động", "Nhiệm vụ", "Tập luyện\n với\n Thần Mèo", "Thách đấu\nThần Mèo");
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                Boss boss = BossManager.gI().getBossById((int) ((int) -251003 - player.id) - 300000);
                                if (mapId == ConstMap.THAP_KARIN) {
                                    switch (player.iDMark.getIndexMenu()) {
                                        case ConstNpc.BASE_MENU:
                                            if (player.zone instanceof ZSnakeRoad) {
                                                switch (select) {
                                                    case 0:
                                                        player.setInteractWithKarin(true);
                                                        Service.getInstance().sendThongBao(player,
                                                                "Hãy mau bay xuống chân tháp Karin");
                                                        break;
                                                }
                                            } else {
                                                switch (select) {
                                                    case 1: // tập luyện với Thần mèo
                                                        createOtherMenu(player, ConstNpc.COFIRM_LUYEN_TAP_THAN_MEO, "Con có chắc chắn muốn tập luyện ?\nTập luyện với ta sẽ tăng 20 sức mạnh mỗi phút", "Đồng ý\nluyện tập", "Không\nđồng ý");
                                                        break;
                                                }
                                            }
                                            break;
                                        case ConstNpc.COFIRM_LUYEN_TAP_THAN_MEO:
                                            switch (select) {
                                                case 0:

                                                    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
                                                    Runnable task = () -> {
                                                        hide_npc(player, 18, 0);
                                                        scheduler.shutdown();
                                                    };
                                                    scheduler.schedule(task, 1, TimeUnit.SECONDS);

                                                    try {
                                                        Boss_ThanMeo dt = new Boss_ThanMeo(Util.createIdDuongTank((int) ((byte) player.id)), BossData.THAN_MEO, player.zone, this.cx, this.cy, (int) player.id);
                                                    } catch (Exception ex) {
                                                        Logger.getLogger(NpcFactory.class.getName()).log(Level.SEVERE, null, ex);
                                                    }
                                                    PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.PK_PVP);
                                                    player.zone.load_Me_To_Another(player);
                                                    break;
                                                default:
                                                    break;
                                            }
                                            break;
                                        case ConstNpc.COFIRM_LUYEN_TAP_YAJIRO:
                                            switch (select) {
                                                case 0:
                                                    player.activeYajiro = 1;
                                                    PlayerService.gI().changeAndSendTypePK(boss, ConstPlayer.PK_PVP);
                                                    PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.PK_PVP);
                                                    break;
                                                default:
                                                    break;
                                            }
                                            break;
                                        case ConstNpc.THACH_DAU_THAN_MEO:
                                            switch (select) {
                                                case 2: // luyện tập với thần mèo
                                                    createOtherMenu(player, ConstNpc.COFIRM_LUYEN_TAP_THAN_MEO, "Con có chắc chắn muốn tập luyện ?\nTập luyện với ta sẽ tăng 20 sức mạnh mỗi phút", "Đồng ý\nluyện tập", "Không\nđồng ý");
                                                    break;
                                                case 3:
                                                    createOtherMenu(player, ConstNpc.COFIRM_THACH_DAU_THAN_MEO, "Con có chắc chắn muốn thách đấu ?\n"
                                                            + "Nếu thắng ta sẽ được tập với Yajirô, tăng 40 sức mạnh mỗi phút", "Đồng ý\ngiao đấu", "Không\nđồng ý");
                                                    break;
                                            }
                                            break;
                                        case ConstNpc.COFIRM_THACH_DAU_THAN_MEO:
                                            switch (select) {
                                                case 0:
                                                    try {
                                                    Boss_ThanMeo dt = new Boss_ThanMeo(Util.createIdDuongTank((int) ((byte) player.id)), BossData.THAN_MEO, player.zone, this.cx, this.cy, (int) player.id);
                                                } catch (Exception ex) {
                                                    Logger.getLogger(NpcFactory.class.getName()).log(Level.SEVERE, null, ex);
                                                }

                                                ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
                                                Runnable task = () -> {
                                                    hide_npc(player, 18, 0);
                                                    scheduler.shutdown();
                                                };
                                                scheduler.schedule(task, 1, TimeUnit.SECONDS);

                                                PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.PK_PVP);
                                                player.thachDauNPC = 1;
                                                player.zone.load_Me_To_Another(player);
                                                break;
                                                default:
                                                    break;
                                            }
                                            break;
                                        case ConstNpc.THACH_DAU_YAJIRO:
                                            switch (select) {
                                                case 1:// luyện tập với Yajirô
                                                    createOtherMenu(player, ConstNpc.COFIRM_LUYEN_TAP_YAJIRO, "Con có chắc chắn muốn tập luyện ?\nTập luyện với Yajirô sẽ tăng 40 sức mạnh mỗi phút", "Đồng ý\nluyện tập", "Không\nđồng ý");
                                                    break;
                                                case 2:// thách đấu với Yajirô
                                                    createOtherMenu(player, ConstNpc.COFIRM_THACH_DAU_YAJIRO, "Con có chắc chắn muốn thách đấu ?\n"
                                                            + "Nếu thắng được Yajirô, con sẽ được học võ với người mạnh hơn để tăng đến 80 sức mạnh mỗi phút", "Đồng ý\ngiao đấu", "Không\nđồng ý");
                                                    break;
                                            }
                                        case ConstNpc.COFIRM_THACH_DAU_YAJIRO:
                                            switch (select) {
                                                case 0:
                                                    player.activeYajiro = 1;
                                                    PlayerService.gI().changeAndSendTypePK(boss, ConstPlayer.PK_PVP);
                                                    PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.PK_PVP);
                                                    player.thachDauNPC = 1;
                                                    break;
                                                default:
                                                    break;
                                            }
                                            break;
                                    }
                                }
                            }
                        }
                    };
                    break;

                case ConstNpc.THUONG_DE:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {

                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 45) {
//                                    if (player.doneThachDauPoPo == 0) {
//                                        this.createOtherMenu(player, ConstNpc.THACH_DAU_POPO, "Pôpô là đệ tử của ta, luyện tập với Pôpô con sẽ có thêm nhiều kinh nghiệm đánh bại được Pôpô ta sẽ dạy võ công cho con", "Đăng ký\ntập\ntự động", "Tập luyện\nvới\nMr.Pôpô", "Thách đấu\nMr.Pôpô", "Quay ngọc\nmay mắn");
//                                    }
//                                    if (player.doneThachDauPoPo == 1 && player.doneThachDauThuongDe == 0) {
//                                        this.createOtherMenu(player, ConstNpc.THACH_DAU_THUONG_DE, "Từ nay con sẽ là đệ tử của ta. Ta sẽ truyền cho con tất cả tuyệt kĩ", "Đăng ký\ntập\nTự động", "Tập luyện\nvới\nThượng Đế", "Thách đấu\nThượng Đế", "Quay ngọc\nMay mắn");
//                                    }
//                                    if (player.doneThachDauPoPo == 1 && player.doneThachDauThuongDe == 1) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Con đã mạnh hơn ta, ta sẽ chỉ đường cho con đến Kaio để gặp thần Vũ\nTrụ Phương Bắc\nNgài là thần cai quản vũ trụ này, hãy theo ngài ấy học võ công",
                                            "Đến\nKaio");
//                                    }
                                } else if (player.zone instanceof ZSnakeRoad) {
                                    if (mapId == ConstMap.CON_DUONG_RAN_DOC) {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Hãy lắm lấy tay ta mau",
                                                "Về thần điện");
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            Boss boss = BossManager.gI().getBossById(Util.createIdDuongTank((int) player.id) - 200000);
                            if (canOpenNpc(player)) {
                                if (this.mapId == 45) {
                                    switch (player.iDMark.getIndexMenu()) {
                                        case ConstNpc.BASE_MENU:
                                            switch (select) {
//                                                case 0: // Đăng ký tập tự động
//
//                                                    break;
//                                                case 1: // luyện tập với popo
//                                                    player.activeYajiro = 1;
//                                                    PlayerService.gI().changeAndSendTypePK(boss, ConstPlayer.PK_PVP);
//                                                    PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.PK_PVP);
//                                                    player.thachDauNPC = 1;
//                                                    break;
//                                                case 2: // tập luyện với thượng đế
//                                                    ChangeMapService.gI().changeMap(player, 49, 0, 384, 440);
//                                                    try {
//                                                        Boss_ThuongDe dt = new Boss_ThuongDe(Util.createIdDuongTank((int) player.id), BossData.THUONG_DE, player.zone, this.cx, this.cy, (int) player.id);
//                                                    } catch (Exception ex) {
//                                                        Logger.getLogger(NpcFactory.class.getName()).log(Level.SEVERE, null, ex);
//                                                    }
//                                                    PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.PK_PVP);
//                                                    player.zone.load_Me_To_Another(player);
//                                                    break;
                                                case 0: // Đến kaio
                                                    ChangeMapService.gI().changeMapBySpaceShip(player, 48, -1, 354);
                                                    break;
//                                                case 4:// Vòng quay may mắn
//                                                    this.createOtherMenu(player, ConstNpc.MENU_CHOOSE_LUCKY_ROUND,
//                                                            "Con có thể chọn từ 1 đến 7 viên\n"
//                                                            + "giá mỗi viên là 5 triệu vàng.\n"
//                                                            + "Ưu tiên dùng vé quay trước.", "Vòng quay\nVàng",
//                                                            "Rương phụ\nĐang có "
//                                                            + (player.inventory.itemsBoxCrackBall.size()
//                                                            - InventoryService.gI().getCountEmptyListItem(
//                                                                    player.inventory.itemsBoxCrackBall))
//                                                            + "\nmón", "Đóng");
//                                                    break;
                                            }
                                            break;
                                        case ConstNpc.MENU_CHOOSE_LUCKY_ROUND:
                                            switch (select) {
                                                case 0:
                                                    LuckyRoundService.gI().openCrackBallUI(player,
                                                            LuckyRoundService.USING_GOLD);
                                                    break;
                                                case 1:
                                                    ShopService.gI().openBoxItemLuckyRound(player);
                                                    break;
                                            }
                                            break;
                                        case ConstNpc.THACH_DAU_POPO:
                                            switch (select) {
                                                case 0:// đăng ký tập luyện tự động

                                                    break;
                                                case 1:// luyện tập pôpô
                                                    player.activeYajiro = 1;
                                                    PlayerService.gI().changeAndSendTypePK(boss, ConstPlayer.PK_PVP);
                                                    PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.PK_PVP);
                                                    player.thachDauNPC = 1;
                                                    break;
                                                case 2:// thách đầu pôpô
                                                    player.thachDauNPC = 1;
                                                    player.activeYajiro = 1;
                                                    PlayerService.gI().changeAndSendTypePK(boss, ConstPlayer.PK_PVP);
                                                    PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.PK_PVP);
                                                    player.thachDauNPC = 1;
                                                    break;
                                                case 3:
                                                    this.createOtherMenu(player, ConstNpc.MENU_CHOOSE_LUCKY_ROUND,
                                                            "Con có thể chọn từ 1 đến 7 viên\n"
                                                            + "giá mỗi viên là 4 ngọc hoặc 5 triệu vàng.\n"
                                                            + "Ưu tiên dùng vé quay trước.", "Vòng quay\nVàng", "Vòng quay\nMay mắn",
                                                            "Rương phụ\nĐang có "
                                                            + (player.inventory.itemsBoxCrackBall.size()
                                                            - InventoryService.gI().getCountEmptyListItem(
                                                                    player.inventory.itemsBoxCrackBall))
                                                            + "\nmón", "Đóng");
                                                    break;
                                            }
                                            break;
                                        case ConstNpc.THACH_DAU_THUONG_DE:
                                            switch (select) {
                                                case 0:
                                                    break;
                                                case 1:
                                                    ChangeMapService.gI().changeMap(player, 49, 0, 384, 440);
                                                    try {
                                                        Boss_ThuongDe dt = new Boss_ThuongDe(Util.createIdDuongTank((int) player.id), BossData.THUONG_DE, player.zone, this.cx, this.cy, (int) player.id);
                                                    } catch (Exception ex) {
                                                        Logger.getLogger(NpcFactory.class.getName()).log(Level.SEVERE, null, ex);
                                                    }
                                                    PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.PK_PVP);
                                                    player.zone.load_Me_To_Another(player);
                                                    break;
                                                case 2:
                                                    ChangeMapService.gI().changeMap(player, 49, 0, 384, 440);
                                                    player.thachDauNPC = 1;
                                                    try {
                                                        Boss_ThuongDe dt = new Boss_ThuongDe(Util.createIdDuongTank((int) player.id), BossData.THUONG_DE, player.zone, this.cx, this.cy, (int) player.id);
                                                    } catch (Exception ex) {
                                                        Logger.getLogger(NpcFactory.class.getName()).log(Level.SEVERE, null, ex);
                                                    }
                                                    PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.PK_PVP);
                                                    player.zone.load_Me_To_Another(player);
                                                    break;
                                                case 3:
                                                    this.createOtherMenu(player, ConstNpc.MENU_CHOOSE_LUCKY_ROUND,
                                                            "Con có thể chọn từ 1 đến 7 viên\n"
                                                            + "giá mỗi viên là 4 ngọc hoặc 5 triệu vàng.\n"
                                                            + "Ưu tiên dùng vé quay trước.", "Vòng quay\nVàng", "Vòng quay\nMay mắn",
                                                            "Rương phụ\nĐang có "
                                                            + (player.inventory.itemsBoxCrackBall.size()
                                                            - InventoryService.gI().getCountEmptyListItem(
                                                                    player.inventory.itemsBoxCrackBall))
                                                            + "\nmón", "Đóng");
                                                    break;
                                            }
                                            break;
                                    }
                                } else if (player.zone instanceof ZSnakeRoad) {
                                    if (mapId == ConstMap.CON_DUONG_RAN_DOC) {
                                        ZSnakeRoad zroad = (ZSnakeRoad) player.zone;
                                        if (zroad.isKilledAll()) {
                                            SnakeRoad road = (SnakeRoad) zroad.getDungeon();
                                            ZSnakeRoad egr = (ZSnakeRoad) road.find(ConstMap.THAN_DIEN);
                                            egr.enter(player, 360, 408);
                                            Service.getInstance().sendThongBao(player, "Hãy xuống gặp thần mèo Karin");
                                        } else {
                                            Service.getInstance().sendThongBao(player,
                                                    "Hãy tiêu diệt hết quái vật ở đây!");
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.THAN_VU_TRU:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 48) {
                                    if (player.doneThachDauBubbles == 0) {
                                        this.createOtherMenu(player, ConstNpc.THACH_DAU_BUBBLES, "Thượng đế đưa ngươi đến đây, chắc muốn ta dạy võ chứ gì\n"
                                                + "Bắt được con khỉ Bubbles rồi hãy tính", "Đăng ký\ntập\ntự động", "Tập luyện\nvới\nBubbles", "Thách đấu\nBubbles", "Di chuyển");
                                    } else if (player.doneThachDauThuongDe == 0) {
                                        this.createOtherMenu(player, ConstNpc.THACH_DAU_THAN_VU_TRU, "Ta là Thần Vũ Trụ Phương Bắc cai quản khu vực bắc vũ trụ\n"
                                                + "nếu thắng được ta, ngươi sẽ được đến\n"
                                                + "Lành Đại Kaio, nơi ở của Thần Linh", "Đăng ký\ntập\ntự động", "Tập luyện\nvới\nThần Vũ Trụ", "Thách đấu\nThần Vũ Trụ", "Di chuyển");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Con mạnh nhất phía bắc vũ trụ này rồi đấy\n"
                                                + "nhưng ngoài vũ trụ bao la kia vẫn có những kẻ mạnh hơn nhìu\n"
                                                + "con cần phải luyện tập để mạnh hơn nữa", "Đăng ký\ntập\ntự động", "Tập luyện\nvới\nBubbles", "Tập luyện\nvới\nThần Vũ Trụ", "Di chuyển");
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 48) {
                                    switch (player.iDMark.getIndexMenu()) {
                                        case ConstNpc.BASE_MENU:
                                            switch (select) {
                                                case 0:// tập tự động

                                                    break;
                                                case 1:// tập luyện với bubbles

                                                    break;
                                                case 2:// tập luyện với thần vũ trụ

                                                    break;
                                                case 3:
                                                    this.createOtherMenu(player, ConstNpc.MENU_DI_CHUYEN,
                                                            "Con muốn đi đâu?", "Về\nthần điện", "Thánh địa\nKaio",
                                                            "Con\nđường\nrắn độc", "Từ chối");
                                                    break;
                                            }
                                            break;
                                        case ConstNpc.THACH_DAU_BUBBLES:
                                            switch (select) {
                                                case 0:// tập tự động

                                                    break;
                                                case 1:// tập luyện với bubbles

                                                    break;
                                                case 2:// tập luyện với thần vũ trụ

                                                    break;
                                                case 3:
                                                    this.createOtherMenu(player, ConstNpc.MENU_DI_CHUYEN,
                                                            "Con muốn đi đâu?", "Về\nthần điện", "Thánh địa\nKaio",
                                                            "Con\nđường\nrắn độc", "Từ chối");
                                                    break;
                                            }
                                            break;

                                        case ConstNpc.THACH_DAU_THAN_VU_TRU:
                                            switch (select) {
                                                case 0:// tập tự động

                                                    break;
                                                case 1:// tập luyện với bubbles

                                                    break;
                                                case 2:// tập luyện với thần vũ trụ

                                                    break;
                                                case 3:
                                                    this.createOtherMenu(player, ConstNpc.MENU_DI_CHUYEN,
                                                            "Con muốn đi đâu?", "Về\nthần điện", "Thánh địa\nKaio",
                                                            "Con\nđường\nrắn độc", "Từ chối");
                                                    break;
                                            }
                                            break;

                                        case ConstNpc.MENU_DI_CHUYEN:
                                            switch (select) {
                                                case 0:
                                                    ChangeMapService.gI().changeMapBySpaceShip(player, 45, -1, 354);
                                                    break;
                                                case 1:
                                                    ChangeMapService.gI().changeMap(player, 50, -1, 318, 336);
                                                    break;
                                                case 2:
                                                    // con đường rắn độc
                                                    if (player.clan != null) {
                                                        Calendar calendar = Calendar.getInstance();
                                                        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//                                                    if (!(dayOfWeek == Calendar.MONDAY
//                                                            || dayOfWeek == Calendar.WEDNESDAY
//                                                            || dayOfWeek == Calendar.FRIDAY
//                                                            || dayOfWeek == Calendar.SUNDAY)) {
//                                                        Service.getInstance().sendThongBao(player,
//                                                                "Chỉ mở vào thứ 2, 4, 6, CN hàng tuần!");
//                                                        return;
//                                                    }
                                                        if (player.clanMember.getNumDateFromJoinTimeToToday() < 2) {
                                                            Service.getInstance().sendThongBao(player,
                                                                    "Phải tham gia bang hội ít nhất 2 ngày mới có thể tham gia!");
                                                            return;
                                                        }
                                                        if (player.clan.snakeRoad == null) {
                                                            this.createOtherMenu(player, ConstNpc.MENU_CHON_CAP_DO,
                                                                    "Hãy mau trở về bằng con đường rắn độc\nbọn Xayda đã đến Trái Đất",
                                                                    "Chọn\ncấp độ", "Từ chối");
                                                        } else {
                                                            if (player.clan.snakeRoad.isClosed()) {
                                                                Service.getInstance().sendThongBao(player,
                                                                        "Bang hội đã hết lượt tham gia!");
                                                            }
                                                        }
                                                    } else {
                                                        Service.getInstance().sendThongBao(player,
                                                                "Chỉ dành cho những người trong bang hội!");
                                                    }
                                                    break;

                                            }
                                            break;
                                        case ConstNpc.MENU_CHON_CAP_DO:
                                            switch (select) {
                                                case 0:
                                                    Input.gI().createFormChooseLevelCDRD(player);
                                                    break;
                                            }
                                            break;
                                    }
                                }
                            }
                        }

                    };
                    break;
                case ConstNpc.TO_SU_KAIO:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
//                                if (this.mapId == 50) {
//                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
//                                            "Cắc cùm cum");
//                                } else {
                                super.openBaseMenu(player);
                                return;
//                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 50) {
                                    switch (select) {
                                        case 0:
                                            NPC_ToSuKaio toSukaio = new NPC_ToSuKaio();
                                            toSukaio.NPC_ToSuKaio(player);
                                            hide_npc(player, 43, 0);
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.DOC_NHAN:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 57) {
                                    if (player.zone.isCheckKilledAll(57) && !player.clan.doanhTrai.isHaveDoneDoanhTrai) {
                                        player.clan.doanhTrai.isHaveDoneDoanhTrai = true;
                                        player.clan.doanhTrai.lastTimeDoneDoanhTrai = System.currentTimeMillis();
                                        player.clan.doanhTrai.DropNgocRong();
                                        Service.getInstance().sendThongBao(player, "Trại Độc Nhãn đã bị tiêu diệt, bạn có 5 phút để tìm kiếm viên ngọc rồng 4 sao trước khi phi thuyền đến đón");
                                        NpcService.gI().createTutorial(player, avartar, "Ta chịu thua, nhưng các ngươi đừng có mong lấy được ngọc của ta\b"
                                                + "ta đã giấu ngọc 4 sao và 1 đống 7 sao trong doanh trại này\b"
                                                + "Các ngươi chỉ có 5 phút đi tìm, đố các ngươi tìm ra hahaha");
                                    } else {
                                        NpcService.gI().createTutorial(player, avartar, "hãy tiêu diệt hết quái");
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                        }
                    };
                    break;
                case ConstNpc.KIBIT:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 50) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                            "Đến\nKaio", "Từ chối");
                                } else {
                                    super.openBaseMenu(player);
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 50) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().changeMap(player, 48, -1, 354, 240);
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;

                case ConstNpc.TRONG_TAI:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                int turn = SieuHangManager.GetFreeTurn(player);
                                if (turn == 0) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đại hội võ thuật Siêu hạng\ndiễn ra 24/7 kể cả ngày lễ và chủ nhật\nHãy thi đấu ngay để khẳng định đẳng cấp của mình nhé",
                                            "Top 100\nCao thủ",
                                            "Hướng\ndẫn\nthêm",
                                            "Ưu tiên\nđấu ngay",
                                            "Tạo bản sao siêu hạng",
                                            "Về\nĐại Hội\nVõ Thuật");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đại hội võ thuật Siêu hạng\ndiễn ra 24/7 kể cả ngày lễ và chủ nhật\nHãy thi đấu ngay để khẳng định đẳng cấp của mình nhé",
                                            "Top 100\nCao thủ",
                                            "Hướng\ndẫn\nthêm",
                                            "Miễn phí\nCòn " + turn + " vé",
                                            "Ưu tiên\nđấu ngay",
                                            "Lưu\ntrạng thái\nchiến đấu",
                                            "Về\nĐại Hội\nVõ Thuật");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                int turn = SieuHangManager.GetFreeTurn(player);
                                if (turn == 0 && select >= 2) {
                                    select++;
                                }
                                switch (select) {
                                    case 0: {
                                        SieuHangService.ShowTop(player, 0);
                                        break;
                                    }
                                    case 1: {
                                        NpcService.gI().createTutorial(player, ConstNpc.TRONG_TAI, -1,
                                                "Giải đấu thể hiện đẳng cấp thực sự\bCác trận đấu diễn ra liên tục bất kể ngày đêm\bBạn hãy tham gia thi đấu để nâng hạng\bvà nhận giải thưởng khủng nhé\nCơ cấu giải thưởng như sau\b(chốt và trao giải ngẫu nhiên từ 20h-23h mỗi ngày)\bTop 1 thưởng 100 ngọc\bTop 2-10 thưởng 20 ngọc\bTop 11-100 thưởng 5 ngọc\bTop 101-1000 thưởng 1 ngọc\nMỗi ngày các bạn được tặng 1 vé tham dự miễn phí\b(tích lũy tối đa 3 vé) khi thua sẽ mất đi 1 vé\bKhi hết vé bạn phải trả 1 ngọc để đấu tiếp\b(trừ ngọc khi trận đấu kết thúc)\nBạn không thể thi đấu với đấu thủ\bcó hạng thấp hơn mình\bChúc bạn may mắn, chào đoàn kết và quyết thắng");
                                        break;
                                    }
                                    case 2: {
                                        if (turn <= 0) {
                                            Service.getInstance().sendThongBao(player, "Bạn đã hết lượt miễn phí");
                                        } else {
                                            SieuHangService.ShowTop(player, 1);
                                        }
                                        break;
                                    }
                                    case 3: {
                                        SieuHangService.ShowTop(player, 1);
                                        break;
                                    }
                                    case 4: {
                                        Timestamp lastModifiedTime = SieuHangManager.GetLastTimeCreateClone(player);

                                        if (lastModifiedTime != null) {
                                            Timestamp currentTime = new Timestamp(System.currentTimeMillis());

                                            long millisecondsDifference = currentTime.getTime() - lastModifiedTime.getTime();
                                            int minutesDifference = (int) (millisecondsDifference / (60 * 1000));

                                            if (minutesDifference > 5) {
                                                SieuHangManager.CreateClone(player);
                                                Service.getInstance().sendThongBao(player, "Tạo bản sao thành công");
                                            } else {
                                                Service.getInstance().sendThongBao(player, "5p mới có thể lưu bản sao 1 lần");
                                            }
                                        }
                                        break;
                                    }
                                    case 5: {
                                        ChangeMapService.gI().changeMapNonSpaceship(player, ConstMap.DAI_HOI_VO_THUAT, 576, 307);
                                        break;
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.OSIN:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 50) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                            "Đến\nKaio", "Đến\nhành tinh\nBill", "Từ chối");
                                } else if (this.mapId == 183) {
                                    long now = System.currentTimeMillis();
                                    if (now > MabuWar.TIME_OPEN && now < MabuWar.TIME_CLOSE) {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                "Bây giờ tôi sẽ bí mật...\n đuổi theo 2 tên đồ tể... \n"
                                                + "Quý vị nào muốn đi theo thì xin mời !",
                                                "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                                    } else if (MabuWar14h.gI().isTimeMabuWar()) {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đại chiến Ma Bư 2 giờ đã mở, "
                                                + "ngươi có muốn tham gia không?",
                                                "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                "Bây giờ tôi sẽ bí mật...\n đuổi theo 2 tên đồ tể... \n"
                                                + "Quý vị nào muốn đi theo thì xin mời !",
                                                "Ok");
                                    }
                                } else if (this.mapId == 154) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                            "Về thánh địa", "Đến\nhành tinh\nngục tù", "Từ chối");
                                } else if (this.mapId == 127) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                            "về nhà", "Từ chối");
                                } else if (this.mapId == 213) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                            "Cửa Hàng\nPháp Sư", "về nhà", "Từ chối");
                                } else if (this.mapId == 155) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                            "Quay về", "Từ chối");
//                                } else if (MapService.gI().isMapMabuWar(this.mapId)) {
//                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
//                                            "Đừng vội xem thường Babyđây,ngay đến cha hắn là thần ma đạo sĩ\n"
//                                            + "Bibiđây khi còn sống cũng phải sợ hắn đấy",
//                                            "Giải trừ\nphép thuật\n50Tr Vàng",
//                                            player.zone.map.mapId != 120 ? "Xuống\nTầng Dưới" : "Rời\nKhỏi đây");
//                                } else if (MabuWar14h.gI().isTimeMabuWar()) {
//                                    createOtherMenu(player, ConstNpc.BASE_MENU, "Ta sẽ phù hộ cho ngươi bằng nguồn sức mạnh của Thần Kaiô"
//                                            + "\n+1 triệu HP, +1 triệu MP, +10k Sức đánh"
//                                            + "\nLưu ý: sức mạnh sẽ biến mất khi ngươi rời khỏi đây",
//                                            "Phù hộ\n55 hồng ngọc", "Từ chối", "Về\nĐại Hội\nVõ Thuật");
                                } else if (this.mapId == 44) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                            "Hành Tinh\nPháp Sư", "Từ chối");
                                } else {
                                    super.openBaseMenu(player);
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 50) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().changeMap(player, 48, -1, 354, 240);
                                                break;
                                            case 1:
                                                ChangeMapService.gI().changeMap(player, 154, -1, 200, 312);
                                                break;
                                        }
                                    }
                                } else if (this.mapId == 183) {
                                    long cc = System.currentTimeMillis();
                                    if (cc > MabuWar.TIME_OPEN && cc < MabuWar.TIME_CLOSE) {

                                        if (select == 0) {
                                            NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_MA_BU);
                                        } else if (select == 1) {
                                            if (!player.getSession().actived) {
                                                Service.getInstance().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
                                            } else {
                                                ChangeMapService.gI().changeMap(player, 114, -1, 318, 336);
                                            }
                                        }

                                    } else if (MabuWar14h.gI().isTimeMabuWar()) {
                                        if (select == 0) {
                                            NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_MA_BU);
                                        } else if (select == 1) {
                                            ChangeMapService.gI().changeMap(player, 127, -1, 318, 336);
                                        }
                                    }
                                } else if (this.mapId == 154) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().changeMap(player, 50, -1, 318, 336);
                                                break;
                                            case 1:
                                                if (!Manager.gI().getGameConfig().isOpenPrisonPlanet()) {
                                                    Service.getInstance().sendThongBao(player,
                                                            "Lối vào hành tinh ngục tù chưa mở");
                                                    return;
                                                }
                                                if (player.nPoint.power < 80000000000L) {
                                                    Service.getInstance().sendThongBao(player,
                                                            "Yêu cầu tối thiếu 80tỷ sức mạnh");
                                                    return;
                                                }
                                                ChangeMapService.gI().changeMap(player, 155, -1, 111, 792);
                                                break;
                                        }
                                    }
                                } else if (this.mapId == 127) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
                                                break;
                                        }
                                    }
                                } else if (this.mapId == 213) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOP_OSIN, 2,
                                                        player.gender);
                                                return;
                                            case 1:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
                                                break;
                                        }
                                    }
                                } else if (this.mapId == 155) {
                                    if (player.iDMark.isBaseMenu()) {
                                        if (select == 0) {
                                            ChangeMapService.gI().changeMap(player, 154, -1, 200, 312);
                                        }
                                    }
                                } else if (this.mapId == 44) {
                                    if (player.iDMark.isBaseMenu()) {
                                        if (select == 0) {
                                            ChangeMapService.gI().changeMap(player, 213, -1, 128, 10);
                                        }
                                    }
                                } else if (MapService.gI().isMapMabuWar(this.mapId)) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                if (player.inventory.getGold() >= 50000000) {
                                                    Service.getInstance().changeFlag(player, 9);
                                                    player.inventory.subGold(50000000);

                                                } else {
                                                    Service.getInstance().sendThongBao(player, "Không đủ vàng");
                                                }
                                                break;
                                            case 1:
                                                if (player.zone.map.mapId == 120) {
                                                    ChangeMapService.gI().changeMapBySpaceShip(player,
                                                            player.gender + 21, -1, 250);
                                                }
                                                if (player.cFlag == 9) {
                                                    if (player.getPowerPoint() >= 20) {
                                                        if (!(player.zone.map.mapId == 119)) {
                                                            int idMapNextFloor = player.zone.map.mapId == 115
                                                                    ? player.zone.map.mapId + 2
                                                                    : player.zone.map.mapId + 1;
                                                            ChangeMapService.gI().changeMap(player, idMapNextFloor, -1,
                                                                    354, 240);
                                                        } else {
                                                            Zone zone = MabuWar.gI().getMapLastFloor(120);
                                                            if (zone != null) {
                                                                ChangeMapService.gI().changeMap(player, zone, 354, 240);
                                                            } else {
                                                                Service.getInstance().sendThongBao(player,
                                                                        "Trận đại chiến đã kết thúc, tàu vận chuyển sẽ đưa bạn về nhà");
                                                            }
                                                        }
                                                        player.resetPowerPoint();
                                                        player.sendMenuGotoNextFloorMabuWar = false;
                                                        Service.getInstance().sendPowerInfo(player, "%",
                                                                player.getPowerPoint());
                                                        if (Util.isTrue(1, 30)) {
                                                            player.inventory.ruby += 1;
                                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                                            Service.getInstance().sendThongBao(player,
                                                                    "Bạn nhận được 1 Hồng Ngọc");
                                                        } else {
                                                            Service.getInstance().sendThongBao(player,
                                                                    "Bạn đen vô cùng luôn nên không nhận được gì cả");
                                                        }
                                                    } else {
                                                        this.npcChat(player,
                                                                "Ngươi cần có đủ điểm để xuống tầng tiếp theo");
                                                    }
                                                    break;
                                                } else {
                                                    this.npcChat(player,
                                                            "Ngươi đang theo phe Babiđây,Hãy qua bên đó mà thể hiện");
                                                }
                                        }
                                    } else if (MabuWar14h.gI().isTimeMabuWar()) {
                                        switch (select) {
                                            case 0:
                                                if (player.effectSkin.isPhuHo) {
                                                    this.npcChat("Con đã mang trong mình sức mạnh của thần Kaiô!");
                                                    return;
                                                }
                                                if (player.inventory.ruby < 55) {
                                                    Service.getInstance().sendThongBao(player, "Bạn không đủ hồng ngọc");
                                                } else {
                                                    player.inventory.ruby -= 55;
                                                    player.effectSkin.isPhuHo = true;
                                                    Service.getInstance().point(player);
                                                    this.npcChat("Ta đã phù hộ cho con hãy giúp ta tiêu diệt Mabư!");
                                                }
                                                break;
                                            case 2:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 52, -1, 250);
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.BABIDAY:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (MapService.gI().isMapMabuWar(this.mapId)) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Đừng vội xem thường Babyđây,ngay đến cha hắn là thần ma đạo sĩ\n"
                                            + "Bibiđây khi còn sống cũng phải sợ hắn đấy",
                                            "Yểm bùa\n50Tr Vàng",
                                            player.zone.map.mapId != 120 ? "Xuống\nTầng Dưới" : "Rời\nKhỏi đây");
                                } else {
                                    super.openBaseMenu(player);
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (MapService.gI().isMapMabuWar(this.mapId)) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                if (player.inventory.getGold() >= 50000000) {
                                                    Service.getInstance().changeFlag(player, 10);
                                                    player.inventory.subGold(50000000);
                                                } else {
                                                    Service.getInstance().sendThongBao(player, "Không đủ vàng");
                                                }
                                                break;
                                            case 1:
                                                if (player.zone.map.mapId == 120) {
                                                    ChangeMapService.gI().changeMapBySpaceShip(player,
                                                            player.gender + 21, -1, 250);
                                                }
                                                if (player.cFlag == 10) {
                                                    if (player.getPowerPoint() >= 20) {
                                                        if (!(player.zone.map.mapId == 119)) {
                                                            int idMapNextFloor = player.zone.map.mapId == 115
                                                                    ? player.zone.map.mapId + 2
                                                                    : player.zone.map.mapId + 1;
                                                            ChangeMapService.gI().changeMap(player, idMapNextFloor, -1,
                                                                    354, 240);
                                                        } else {
                                                            Zone zone = MabuWar.gI().getMapLastFloor(120);
                                                            if (zone != null) {
                                                                ChangeMapService.gI().changeMap(player, zone, 354, 240);
                                                            } else {
                                                                Service.getInstance().sendThongBao(player,
                                                                        "Trận đại chiến đã kết thúc, tàu vận chuyển sẽ đưa bạn về nhà");
                                                                ChangeMapService.gI().changeMapBySpaceShip(player,
                                                                        player.gender + 21, -1, 250);
                                                            }
                                                        }
                                                        player.resetPowerPoint();
                                                        player.sendMenuGotoNextFloorMabuWar = false;
                                                        Service.getInstance().sendPowerInfo(player, "TL",
                                                                player.getPowerPoint());
                                                        if (Util.isTrue(1, 30)) {
                                                            player.inventory.ruby += 1;
                                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                                            Service.getInstance().sendThongBao(player,
                                                                    "Bạn nhận được 1 Hồng Ngọc");
                                                        } else {
                                                            Service.getInstance().sendThongBao(player,
                                                                    "Bạn đen vô cùng luôn nên không nhận được gì cả");
                                                        }
                                                    } else {
                                                        this.npcChat(player,
                                                                "Ngươi cần có đủ điểm để xuống tầng tiếp theo");
                                                    }
                                                    break;
                                                } else {
                                                    this.npcChat(player,
                                                            "Ngươi đang theo phe Ôsin,Hãy qua bên đó mà thể hiện");
                                                }
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.LINH_CANH:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (player.clan == null) {
                                    this.createOtherMenu(player, ConstNpc.MENU_KHONG_CHO_VAO_DT,
                                            "Chỉ tiếp các bang hội, miễn tiếp khách vãng lai", "Đóng");
                                } else if (player.clan.getMembers().size() < 5) {
//                                } else if (player.clan.getMembers().size() < 1) {
                                    this.createOtherMenu(player, ConstNpc.MENU_KHONG_CHO_VAO_DT,
                                            "Bang hội phải có ít nhất 5 thành viên mới có thể mở", "Đóng");
                                } else {
                                    ClanMember clanMember = player.clan.getClanMember((int) player.id);
                                    if (player.nPoint.dameg < 1_000) {
                                        NpcService.gI().createTutorial(player, avartar,
                                                "Bạn phải đạt 1k sức đánh gốc");
                                        return;
                                    }
                                    int days = (int) (((System.currentTimeMillis() / 1000) - clanMember.joinTime) / 60 / 60 / 24);
                                    if (days < 1) {
                                        NpcService.gI().createTutorial(player, avartar,
                                                "Chỉ những thành viên gia nhập bang hội tối thiểu 1 ngày mới có thể tham gia");
                                        return;
                                    }
                                    if (!player.clan.haveGoneDoanhTrai && player.clan.timeOpenDoanhTrai != 0) {
                                        createOtherMenu(player, ConstNpc.MENU_VAO_DT,
                                                "Bang hội của ngươi đang đánh trại độc nhãn\n" + "Thời gian còn lại là "
                                                + TimeUtil.getSecondLeft(player.clan.timeOpenDoanhTrai,
                                                        DoanhTrai.TIME_DOANH_TRAI / 1000)
                                                + ". Ngươi có muốn tham gia không?",
                                                "Tham gia", "Không", "Hướng\ndẫn\nthêm");
                                    } else {
                                        List<Player> plSameClans = new ArrayList<>();
                                        List<Player> playersMap = player.zone.getPlayers();
                                        synchronized (playersMap) {
                                            for (Player pl : playersMap) {
                                                if (!pl.equals(player) && pl.clan != null
                                                        && pl.clan.id == player.clan.id && pl.location.x >= 1285
                                                        && pl.location.x <= 1645) {
                                                    plSameClans.add(pl);
                                                }

                                            }
                                        }
//                                        if (plSameClans.size() >= 0) {
                                        if (plSameClans.size() >= 2) {
                                            if (!player.isAdmin() && player.clanMember
                                                    .getNumDateFromJoinTimeToToday() < DoanhTrai.DATE_WAIT_FROM_JOIN_CLAN) {
                                                createOtherMenu(player, ConstNpc.MENU_KHONG_CHO_VAO_DT,
                                                        "Bang hội chỉ cho phép những người ở trong bang trên 1 ngày. Hẹn ngươi quay lại vào lúc khác",
                                                        "OK", "Hướng\ndẫn\nthêm");
                                            } else if (player.clan.haveGoneDoanhTrai) {
                                                createOtherMenu(player, ConstNpc.MENU_KHONG_CHO_VAO_DT,
                                                        "Bang hội của ngươi đã đi trại lúc "
                                                        + Util.formatTime(player.clan.timeOpenDoanhTrai)
                                                        + " hôm nay. Người mở\n" + "("
                                                        + player.clan.playerOpenDoanhTrai.name
                                                        + "). Hẹn ngươi quay lại vào ngày mai",
                                                        "OK", "Hướng\ndẫn\nthêm");

                                            } else {
                                                createOtherMenu(player, ConstNpc.MENU_CHO_VAO_DT,
                                                        "Hôm nay bang hội của ngươi chưa vào trại lần nào. Ngươi có muốn vào\n"
                                                        + "không?\nĐể vào, ta khuyên ngươi nên có 3-4 người cùng bang đi cùng",
                                                        "Vào\n(miễn phí)", "Không", "Hướng\ndẫn\nthêm");
                                            }
                                        } else {
                                            createOtherMenu(player, ConstNpc.MENU_KHONG_CHO_VAO_DT,
                                                    "Ngươi phải có ít nhất 2 đồng đội cùng bang đứng gần mới có thể\nvào\n"
                                                    + "tuy nhiên ta khuyên ngươi nên đi cùng với 3-4 người để khỏi chết.\n"
                                                    + "Hahaha.",
                                                    "OK", "Hướng\ndẫn\nthêm");
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 27) {
                                    switch (player.iDMark.getIndexMenu()) {
                                        case ConstNpc.MENU_KHONG_CHO_VAO_DT:
                                            if (select == 1) {
                                                NpcService.gI().createTutorial(player, this.avartar,
                                                        ConstNpc.HUONG_DAN_DOANH_TRAI);
                                            }
                                            break;
                                        case ConstNpc.MENU_CHO_VAO_DT:
                                            switch (select) {
                                                case 0:
                                                    DoanhTraiService.gI().openDoanhTrai(player);
                                                    break;
                                                case 2:
                                                    NpcService.gI().createTutorial(player, this.avartar,
                                                            ConstNpc.HUONG_DAN_DOANH_TRAI);
                                                    break;
                                            }
                                            break;
                                        case ConstNpc.MENU_VAO_DT:
                                            switch (select) {
                                                case 0:
                                                    ChangeMapService.gI().changeMap(player, 53, 0, 35, 432);
                                                    break;
                                                case 2:
                                                    NpcService.gI().createTutorial(player, this.avartar,
                                                            ConstNpc.HUONG_DAN_DOANH_TRAI);
                                                    break;
                                            }
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.QUA_TRUNG:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {

                        private final int COST_AP_TRUNG_NHANH = 1000000000;

                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                player.mabuEgg.sendMabuEgg();
                                if (player.mabuEgg.getSecondDone() != 0) {
                                    this.createOtherMenu(player, ConstNpc.CAN_NOT_OPEN_EGG, "Hãy thu thập năng lượng\nbằng cách làm nhiệm vụ hằng ngày\ntại Bò Mộng từ mức độ khó trở lên\nđể trứng mau nở nhé.",
                                            "Nở trứng\nnhanh\n1 tỷ vàng", "Hủy bỏ\ntrứng", "Đóng");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.CAN_OPEN_EGG, "Hãy thu thập năng lượng\nbằng cách làm nhiệm vụ hằng ngày\ntại Bò Mộng từ mức độ khó trở lên\nđể trứng mau nở nhé.", "Nở",
                                            "Hủy bỏ\ntrứng", "Đóng");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.CAN_NOT_OPEN_EGG:
                                        switch (select) {
                                            case 0:
                                                player.mabuEgg.timeDone = 0;
                                                player.inventory.gold -= 1000000000;
                                                Service.getInstance().sendMoney(player);
                                                Service.getInstance().sendThongBao(player, "Đã nở trứng nhanh thành công");
                                                break;
                                            case 1:
                                                this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_EGG,
                                                        "Bạn có chắc chắn muốn hủy bỏ trứng Mabư?", "Đồng ý", "Từ chối");
                                                break;
                                        }
                                        break;
                                    case ConstNpc.CAN_OPEN_EGG:
                                        switch (select) {
                                            case 0:
                                                this.createOtherMenu(player, ConstNpc.CONFIRM_OPEN_EGG,
                                                        "Bạn có chắc thay thế đệ tử hiện tại bằng Đệ tử Mabư",
                                                        "Thay thế", "Từ chối");
                                                break;
                                            case 1:
                                                this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_EGG,
                                                        "Bạn có chắc chắn muốn hủy bỏ trứng Mabư?", "Đồng ý",
                                                        "Từ chối");
                                                break;
                                        }
                                        break;
                                    case ConstNpc.CONFIRM_OPEN_EGG:
                                        switch (select) {
                                            case 0:
                                                player.mabuEgg.openEgg(player.gender);
                                                break;
                                            default:
                                                break;
                                        }
                                        break;
                                    case ConstNpc.CONFIRM_DESTROY_EGG:
                                        if (select == 0) {
                                            player.mabuEgg.destroyEgg();
                                        }
                                        break;
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.QUOC_VUONG:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {

                        @Override
                        public void openBaseMenu(Player player) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Con muốn nâng giới hạn sức mạnh cho bản thân hay đệ tử?",
                                    "Bản thân", "Đệ tử",
                                    "Từ chối");
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:
                                            if (player.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                                this.createOtherMenu(player, ConstNpc.OPEN_POWER_MYSEFT,
                                                        "Ta sẽ truền năng lượng giúp con mở giới hạn sức mạnh của bản thân lên "
                                                        + Util.numberToMoney(player.nPoint.getPowerNextLimit()),
                                                        "Nâng\ngiới hạn\nsức mạnh",
                                                        "Nâng ngay\n"
                                                        + Util.numberToMoney(
                                                                OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER)
                                                        + " vàng",
                                                        "Đóng");
                                            } else {
                                                this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                        "Sức mạnh của con đã đạt tới giới hạn", "Đóng");
                                            }
                                            break;
                                        case 1:
                                            if (player.pet != null) {
                                                if (player.pet.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                                    this.createOtherMenu(player, ConstNpc.OPEN_POWER_PET,
                                                            "Ta sẽ truền năng lượng giúp con mở giới hạn sức mạnh của đệ tử lên "
                                                            + Util.numberToMoney(
                                                                    player.pet.nPoint.getPowerNextLimit()),
                                                            "Nâng ngay\n" + Util.numberToMoney(
                                                                    OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER)
                                                            + " vàng",
                                                            "Đóng");
                                                } else {
                                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                            "Sức mạnh của đệ con đã đạt tới giới hạn", "Đóng");
                                                }
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                                            }
                                            // giới hạn đệ tử
                                            break;
//                                        case 2:
//                                            this.createOtherMenu(player, ConstNpc.MENU_CHUYENSINH,
//                                                    "|8| -- CHUYỂN SINH --"
//                                                    + "\n|3|Sức Mạnh Hiện Tại: \n"
//                                                    + Util.format(player.nPoint.power)
//                                                    + "\n|5| ----------------"
//                                                    + "\n Bạn sẽ được tái sinh ở một hành tinh khác bất kì"
//                                                    + "\n Các chiêu thức sẽ về cấp 1, Sức mạnh về 1 triệu 5"
//                                                    + "\n|1| Tái sinh càng nhiều SĐ,HP,KI càng cao"
//                                                    + "\n ----------------"
//                                                    + "\n|7| Yêu Cầu:"
//                                                    + "\n|2| Sức mạnh đạt 90 Tỷ"
//                                                    + "\n Có Skill " + player.tenskill9(player.gender)
//                                                    + "\n ----------------"
//                                                    + "\n|6| Có tỉ lệ thất bại !"
//                                                    + "\n Thất bại sẽ trừ đi Thỏi vàng và Giảm 10 Tỷ Sức mạnh",
//                                                    "Chuyển sinh", "Thông tin\nchuyển sinh",
//                                                    "Đóng");
//                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CHUYENSINH) {
                                    switch (select) {
                                        case 0:
                                            int tvang = 0;
                                            if (player.chuyensinh <= 10) {
                                                tvang = 2;
                                            }
                                            if (player.chuyensinh <= 20 && player.chuyensinh > 10) {
                                                tvang = 3;
                                            }
                                            if (player.chuyensinh > 20 && player.chuyensinh <= 30) {
                                                tvang = 5;
                                            }
                                            if (player.chuyensinh > 30 && player.chuyensinh <= 50) {
                                                tvang = 8;
                                            }
                                            if (player.chuyensinh > 50 && player.chuyensinh <= 60) {
                                                tvang = 20;
                                            }
                                            if (player.chuyensinh > 60 && player.chuyensinh <= 72) {
                                                tvang = 50;
                                            }
                                            this.createOtherMenu(player, ConstNpc.CHUYENSINH,
                                                    "|7|CHUYỂN SINH"
                                                    + "\n\n|5|Bạn đang chuyển sinh : " + player.chuyensinh
                                                    + " \nCấp tiếp theo với tỉ lệ : " + (100 - player.chuyensinh * 2)
                                                    + "% \n Mức giá chuyển sinh : " + tvang + " Thỏi vàng \n\n|7|Bạn có muốn chuyển sinh ?",
                                                    "Đồng ý", "Từ chối");
                                            break; // 
                                        case 1:
                                            int hp = 0,
                                             dame = 0;
                                            if (player.chuyensinh > 0) {
                                                if (player.chuyensinh <= 10) {
                                                    dame += (1750) * player.chuyensinh;
                                                    hp += (15650) * player.chuyensinh;
                                                }
                                                if (player.chuyensinh <= 20 && player.chuyensinh > 10) {
                                                    dame += (3350) * (player.chuyensinh);
                                                    hp += (30750) * (player.chuyensinh);
                                                }
                                                if (player.chuyensinh > 20 && player.chuyensinh <= 30) {
                                                    dame += (4950) * (player.chuyensinh);
                                                    hp += (45875) * (player.chuyensinh);
                                                }
                                                if (player.chuyensinh > 30) {
                                                    dame += (6000) * (player.chuyensinh);
                                                    hp += (60000) * (player.chuyensinh);
                                                }
                                            }
                                            Service.getInstance().sendThongBaoOK(player, "Bạn đang cấp chuyển sinh: " + player.chuyensinh
                                                    + "\n HP : +" + Util.format(hp) + "\n KI : +" + Util.format(hp) + "\n Sức đánh : +" + Util.format(dame));
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.CHUYENSINH) {
                                    if (player.chuyensinh >= 72) {
                                        npcChat(player, "|7| Cấp Chuyển sinh đạt MAX là 72 Cấp");
                                        return;
                                    }
                                    if (player.playerSkill.skills.get(7).point == 0) {
                                        npcChat(player, "|7|Yêu cầu phải học kỹ năng " + player.tenskill9(player.gender));
                                        return;
                                    }
                                    if (player.nPoint.power < 90_000_000_000L) {
                                        npcChat(player, "|7|Bạn chưa đủ sức mạnh yêu cầu để Chuyển sinh");
                                    } else {
                                        Item thoivang = null;
                                        try {
                                            thoivang = InventoryService.gI().findItemBagByTemp(player, 457);
                                        } catch (Exception e) {
                                        }
                                        int tvang = 0;
                                        if (player.chuyensinh <= 10) {
                                            tvang = 2;
                                        }
                                        if (player.chuyensinh <= 20 && player.chuyensinh > 10) {
                                            tvang = 3;
                                        }
                                        if (player.chuyensinh > 20 && player.chuyensinh <= 30) {
                                            tvang = 5;
                                        }
                                        if (player.chuyensinh > 30 && player.chuyensinh <= 50) {
                                            tvang = 8;
                                        }
                                        if (player.chuyensinh > 50 && player.chuyensinh <= 60) {
                                            tvang = 20;
                                        }
                                        if (player.chuyensinh > 60 && player.chuyensinh <= 72) {
                                            tvang = 50;
                                        }
                                        if (thoivang == null || thoivang.quantity < tvang) {
                                            npcChat(player, "Bạn chưa đủ Thỏi vàng để chuyển sinh");
                                            return;
                                        }
                                        int percent = (player.chuyensinh <= 45) ? (100 - (player.chuyensinh) * 2) : 10;
                                        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                                            if (player.inventory.itemsBody.get(0).quantity < 1
                                                    && player.inventory.itemsBody.get(1).quantity < 1
                                                    && player.inventory.itemsBody.get(2).quantity < 1
                                                    && player.inventory.itemsBody.get(3).quantity < 1
                                                    && player.inventory.itemsBody.get(4).quantity < 1) {
                                                if (Util.nextInt(0, 100) < (percent)) {
                                                    InventoryService.gI().subQuantityItemsBag(player, thoivang, tvang);
                                                    player.gender += 1;
                                                    player.nPoint.power = 1_500_000;
                                                    player.chuyensinh++;
                                                    if (player.gender > 2) {
                                                        player.gender = 0;
                                                    }
                                                    short[] headtd = {30, 31, 64};
                                                    short[] headnm = {9, 29, 32};
                                                    short[] headxd = {27, 28, 6};
                                                    player.playerSkill.skills.clear();
                                                    for (Skill skill : player.playerSkill.skills) {
                                                        skill.point = 1;
                                                    }
                                                    int[] skillsArr = player.gender == 0 ? new int[]{0, 1, 6, 9, 10, 20, 22, 24, 19}
                                                            : player.gender == 1 ? new int[]{2, 3, 7, 11, 12, 17, 18, 26, 19}
                                                            : new int[]{4, 5, 8, 13, 14, 21, 23, 25, 19};
                                                    for (int i = 0; i < skillsArr.length; i++) {
                                                        player.playerSkill.skills.add(SkillUtil.createSkill(skillsArr[i], 1));
                                                    }
                                                    player.playerIntrinsic.intrinsic = IntrinsicService.gI().getIntrinsicById(0);
                                                    player.playerIntrinsic.intrinsic.param1 = 0;
                                                    player.playerIntrinsic.intrinsic.param2 = 0;
                                                    player.playerIntrinsic.countOpen = 0;
                                                    switch (player.gender) {
                                                        case 0:
                                                            player.head = headtd[Util.nextInt(headtd.length)];
                                                            break;
                                                        case 1:
                                                            player.head = headnm[Util.nextInt(headnm.length)];
                                                            break;
                                                        case 2:
                                                            player.head = headxd[Util.nextInt(headxd.length)];
                                                            break;
                                                        default:
                                                            break;
                                                    }
                                                    npcChat(player, "|1|Chuyển sinh thành công \n cấp hiện tại :" + player.chuyensinh);
                                                    Service.getInstance().player(player);
                                                    player.zone.loadAnotherToMe(player);
                                                    player.zone.load_Me_To_Another(player);
                                                    Service.getInstance().sendFlagBag(player);
                                                    Service.getInstance().Send_Caitrang(player);
                                                    PlayerService.gI().sendInfoHpMpMoney(player);
                                                    Service.getInstance().point(player);
                                                    Service.getInstance().Send_Info_NV(player);
                                                    InventoryService.gI().sendItemBags(player);
                                                    Service.getInstance().sendMoney(player);
                                                } else {
                                                    npcChat(player, "|7|Chuyển sinh thất bại \n cấp hiện tại :" + player.chuyensinh);
                                                    player.nPoint.power -= 10_000_000_000L;
                                                    InventoryService.gI().subQuantityItemsBag(player, thoivang, tvang);
                                                    Service.getInstance().point(player);
                                                    Service.getInstance().Send_Info_NV(player);
                                                    InventoryService.gI().sendItemBags(player);
                                                    Service.getInstance().sendMoney(player);
                                                }
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Tháo hết 5 món đầu đang mặc ra nha");
                                            }
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Balo đầy");
                                        }
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_MYSEFT) {
                                    switch (select) {
                                        case 0:
                                            OpenPowerService.gI().openPowerBasic(player);
                                            break;
                                        case 1:
                                            if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
                                                if (OpenPowerService.gI().openPowerSpeed(player)) {
                                                    player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                                                    Service.getInstance().sendMoney(player);
                                                }
                                            } else {
                                                Service.getInstance().sendThongBao(player,
                                                        "Bạn không đủ vàng để mở, còn thiếu " + Util.numberToMoney(
                                                                (OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER
                                                                - player.inventory.gold))
                                                        + " vàng");
                                            }
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_PET) {
                                    if (select == 0) {
                                        if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
                                            if (OpenPowerService.gI().openPowerSpeed(player.pet)) {
                                                player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                                                Service.getInstance().sendMoney(player);
                                            }
                                        } else {
                                            Service.getInstance().sendThongBao(player,
                                                    "Bạn không đủ vàng để mở, còn thiếu " + Util
                                                            .numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER
                                                                    - player.inventory.gold))
                                                    + " vàng");
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.BUNMA_TL:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu bé muốn mua gì nào?",
                                            "Cửa hàng", "Đóng");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    if (select == 0) {
                                        ShopService.gI().openShopNormal(player, this, ConstNpc.SHOP_BUNMA_TL_0, 0,
                                                player.gender);
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.RONG_OMEGA:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                BlackBallWar.gI().setTime();
                                if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                                    try {
                                        long now = System.currentTimeMillis();
                                        if (now > BlackBallWar.TIME_OPEN && now < BlackBallWar.TIME_CLOSE) {
                                            this.createOtherMenu(player, ConstNpc.MENU_OPEN_BDW,
                                                    "Đường đến với ngọc rồng sao đen đã mở, "
                                                    + "ngươi có muốn tham gia không?",
                                                    "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                                        } else {
                                            String[] optionRewards = new String[7];
                                            int index = 0;
                                            for (int i = 0; i < 7; i++) {
                                                if (player.rewardBlackBall.timeOutOfDateReward[i] > System
                                                        .currentTimeMillis()) {
                                                    optionRewards[index] = "Nhận thưởng\n" + (i + 1) + " sao";
                                                    index++;
                                                }
                                            }
                                            if (index != 0) {
                                                String[] options = new String[index + 1];
                                                for (int i = 0; i < index; i++) {
                                                    options[i] = optionRewards[i];
                                                }
                                                options[options.length - 1] = "Từ chối";
                                                this.createOtherMenu(player, ConstNpc.MENU_REWARD_BDW,
                                                        "Ngươi có một vài phần thưởng ngọc " + "rồng sao đen đây!",
                                                        options);
                                            } else {
                                                this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_BDW,
                                                        "Ta có thể giúp gì cho ngươi?", "Hướng dẫn", "Từ chối");
                                            }
                                        }
                                    } catch (Exception ex) {
                                        Log.error("Lỗi mở menu rồng Omega");
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.MENU_REWARD_BDW:
                                        player.rewardBlackBall.getRewardSelect((byte) select);
                                        break;
                                    case ConstNpc.MENU_OPEN_BDW:
                                        if (select == 0) {
                                            NpcService.gI().createTutorial(player, this.avartar,
                                                    ConstNpc.HUONG_DAN_BLACK_BALL_WAR);
                                        } else if (select == 1) {
                                            player.iDMark.setTypeChangeMap(ConstMap.CHANGE_BLACK_BALL);
                                            ChangeMapService.gI().openChangeMapTab(player);
                                        }
                                        break;
                                    case ConstNpc.MENU_NOT_OPEN_BDW:
                                        if (select == 0) {
                                            NpcService.gI().createTutorial(player, this.avartar,
                                                    ConstNpc.HUONG_DAN_BLACK_BALL_WAR);
                                        }
                                        break;
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.RONG_1S:
                case ConstNpc.RONG_2S:
                case ConstNpc.RONG_3S:
                case ConstNpc.RONG_4S:
                case ConstNpc.RONG_5S:
                case ConstNpc.RONG_6S:
                case ConstNpc.RONG_7S:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (player.isHoldBlackBall) {
                                    this.createOtherMenu(player, ConstNpc.MENU_PHU_HP, "Ta có thể giúp gì cho ngươi?",
                                            "Phù hộ", "Từ chối");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_OPTION_GO_HOME,
                                            "Ta có thể giúp gì cho ngươi?", "Về nhà", "Từ chối");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHU_HP) {
                                    if (select == 0) {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPTION_PHU_HP,
                                                "Ta sẽ giúp ngươi tăng HP lên mức kinh hoàng, ngươi chọn đi",
                                                "x3 HP\n" + Util.numberToMoney(BlackBallWar.COST_X3) + " thỏi vàng",
                                                "x5 HP\n" + Util.numberToMoney(BlackBallWar.COST_X5) + " thỏi vàng",
                                                "x7 HP\n" + Util.numberToMoney(BlackBallWar.COST_X7) + " thỏi vàng",
                                                "Từ chối");
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_GO_HOME) {
                                    if (select == 0) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PHU_HP) {
                                    switch (select) {
                                        case 0:
                                            BlackBallWar.gI().xHPKI(player, BlackBallWar.X3);
                                            break;
                                        case 1:
                                            BlackBallWar.gI().xHPKI(player, BlackBallWar.X5);
                                            break;
                                        case 2:
                                            BlackBallWar.gI().xHPKI(player, BlackBallWar.X7);
                                            break;
                                        case 3:
                                            this.npcChat(player, "Để ta xem ngươi trụ được bao lâu");
                                            break;
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.NPC_64:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                createOtherMenu(player, ConstNpc.BASE_MENU, "Ngươi muốn xem thông tin gì?",
                                        "Top\nsức mạnh", "Top\nTổng Nạp", "Top Sức Đánh", "Top HP", "Top KI", "Top\nNhiệm Vụ", "Đóng");
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:
                                            Service.getInstance().showTopPower(player);
                                            break;
                                        case 1:
                                            Service.getInstance().showTopRichMan(player);
                                            break;
                                        case 2:
                                            Service.getInstance().showTopSD(player);
                                            break;
                                        case 3:
                                            Service.getInstance().showTopHP(player);
                                            break;
                                        case 4:
                                            Service.getInstance().showTopKI(player);
                                            break;
                                        case 5:
                                            Service.getInstance().showTopTask(player);
                                            break;
                                        default:
                                            Service.getInstance().sendThongBao(player, "Lựa chọn không hợp lệ");
                                            break;
                                    }
                                }
                            }
                        }
                    };
                    break;

                case ConstNpc.BILL:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {

                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 48) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đói bụng quá...ngươi mang cho ta 99 phần đồ ăn,\n"
                                            + "ta sẽ cho ngươi một món đồ Hủy Diệt.\n"
                                            + "Nếu tâm trạng ta vui ngươi có thể nhận được trang bị tăng đến 15%", "OK", "Từ chối");
                                } else {
                                    super.openBaseMenu(player);
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (this.mapId) {
                                    case 48:
                                        if (player.iDMark.isBaseMenu()) {
                                            switch (select) {
                                                case 0:
                                                    if (player.setClothes.godClothes) {
                                                        ShopService.gI().openShopBillHuyDiet(player, ConstNpc.SHOP_BILL_HUY_DIET_0, 0);
                                                    } else {
                                                        Service.getInstance().sendThongBao(player,
                                                                "Yêu cầu có đủ trang bị thần linh");
                                                    }
                                                    break;
                                            }
                                        }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.VADOS:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            Item mcl = InventoryService.gI().findItemBagByTemp(player, ConstTranhNgocNamek.ITEM_TRANH_NGOC);
                            int slMCL = (mcl == null) ? 0 : mcl.quantity;
                            if (canOpenNpc(player)) {
                                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Ngọc rồng Namếc đang bị 2 thế lực tranh giành\nHãy chọn cấp độ tham gia tùy theo sức mạnh bản thân",
                                            "Tham gia", "Đổi điểm\nThưởng\n[" + player.diem_tranh_ngoc + "]", "Từ chối");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                int cost = 2000;
                                int returnCost = 1800;
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.BASE_MENU:

                                        switch (select) {
                                            case 0:
                                                if (TranhNgoc.gI().isTimeRegisterWar()) {
                                                    if (player.iDMark.getTranhNgoc() == -1) {
                                                        this.createOtherMenu(player, ConstNpc.REGISTER_TRANH_NGOC,
                                                                "Ngọc rồng Namếc đang bị 2 thế lực tranh giành\n"
                                                                + "Hãy chọn cấp độ tham gia tùy theo sức mạnh bản thân\n"
                                                                + "Lệ phí tham của ngươi là " + cost + " hồng ngọc - Win sẽ hoàn còn Lose sẽ mất\n"
                                                                + "Ngẫu nhiên phe Cadic hoặc Fide\n"
                                                                + "Số lượng người đăng ký: "
                                                                + (TranhNgoc.gI().getPlayersCadic().size() + TranhNgoc.gI().getPlayersFide().size()) + "\n"
                                                                + "Lúc " + TranhNgoc.HOUR_OPEN + " Sẽ bắt đầu khởi tranh Ngọc Rồng Namek",
                                                                "Đăng ký\nngay", "Đóng");
                                                    } else {
                                                        this.createOtherMenu(player, ConstNpc.LOG_OUT_TRANH_NGOC,
                                                                "Ngọc rồng Namếc đang bị 2 thế lực tranh giành\nHãy chọn cấp độ tham gia tùy theo sức mạnh bản thân\nNgươi sẽ được hoàn " + returnCost + " hồng ngọc\n"
                                                                + "Số lượng người đăng ký:: " + (TranhNgoc.gI().getPlayersCadic().size() + TranhNgoc.gI().getPlayersFide().size()),
                                                                "Hủy\nĐăng Ký", "Đóng");
                                                    }
                                                    return;
                                                }
                                                Service.gI().sendPopUpMultiLine(player, 0, 5074, "Sự kiện sẽ mở đăng ký vào lúc " + TranhNgoc.HOUR_REGISTER + ":" + TranhNgoc.MIN_REGISTER + "\nSự kiện sẽ bắt đầu vào " + TranhNgoc.HOUR_OPEN + ":" + TranhNgoc.MIN_OPEN + " và kết thúc vào " + TranhNgoc.HOUR_CLOSE + ":" + TranhNgoc.HOUR_CLOSE);
                                                break;
                                            case 1:
//                                                this.createOtherMenu(player, ConstNpc.DOI_DIEM_THUONG,
//                                                        "Đổi ngẫu nhiên NRO Tranh Namek 1 - 7 sao\nThu thập đủ 7 viên để ước",
//                                                        "Đổi 1 vé\nNRO Tranh", "Đóng");
                                                break;
//                                            case 2:
//                                                this.npcChat(player, "Chức Năng Đang Được Update!");
////                                Service.gI().showListTop(player, Manager.topDauThan);
//                                                break;
//
//                                            case 3:
//                                                CombineServiceNew.gI().openTabCombine(player,
//                                                        CombineServiceNew.NHAP_NGOC_RONG_TRANH);
//                                                break;
                                        }
                                        break;
                                    case ConstNpc.REGISTER_TRANH_NGOC:
                                        switch (select) {
                                            case 0:
                                                if (!player.getSession().actived) {
                                                    Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này!");
                                                    return;
                                                }
                                                if (player.inventory.ruby <= cost) {
                                                    Service.gI().sendThongBao(player, "Ngươi không đủ " + cost + " hồng ngọc");
                                                    return;
                                                }
                                                if ((TranhNgoc.gI().getPlayersCadic().size() + TranhNgoc.gI().getPlayersFide().size()) >= 20) {
                                                    Service.gI().sendThongBao(player, "Số lượng người chơi đã đủ!");
                                                    return;
                                                }
                                                if (player.nPoint.power < 80000000000L) {
                                                    Service.getInstance().sendThongBao(player,
                                                            "Yêu cầu tối thiếu 80tỷ sức mạnh");
                                                    return;
                                                }
                                                byte countPhe = TranhNgoc.gI().getBalancedPhe();
                                                player.iDMark.setTranhNgoc(countPhe);
                                                if (countPhe == 1) {
                                                    TranhNgoc.gI().addPlayersCadic(player);
                                                } else {
                                                    TranhNgoc.gI().addPlayersFide(player);
                                                }
                                                player.inventory.ruby -= cost;
                                                InventoryService.gI().sendItemBody(player);
                                                Service.gI().sendThongBao(player, "Đăng ký thành công");
                                                break;
                                        }
                                        break;
                                    case ConstNpc.LOG_OUT_TRANH_NGOC:
                                        if (select == 0) {
                                            player.iDMark.setTranhNgoc((byte) -1);
                                            TranhNgoc.gI().removePlayersCadic(player);
                                            TranhNgoc.gI().removePlayersFide(player);
                                            player.inventory.ruby += returnCost;
                                            InventoryService.gI().sendItemBody(player);
                                            Service.gI().sendThongBao(player, "Hủy đăng ký thành công");
                                        }
                                        break;
                                    case ConstNpc.DOI_DIEM_THUONG:
                                        if (select == 0) {
                                            Item veNroTranh = InventoryService.gI().findItemBagByTemp(player, ConstTranhNgocNamek.ITEM_TRANH_NGOC);
                                            if (veNroTranh.quantity <= 0) {
                                                int itemId = Util.nextInt(1558, 1564); // Gọi hàm random trong khoảng
                                                Item nroTranh = ItemService.gI().createNewItem((short) itemId);
                                                InventoryService.gI().addItemBag(player, nroTranh, 1);//add nro vào player
                                                InventoryService.gI().sendItemBags(player);
                                                InventoryService.gI().subQuantityItemsBag(player, veNroTranh, 1);//trừ vé nro tranh
                                                Service.gI().sendThongBao(player, "Bạn đã nhận được " + nroTranh.template.name);
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn không đủ vé đổi thưởng!");
                                            }

                                        }
                                        break;
                                }
                            } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                                switch (player.combineNew.typeCombine) {
                                    case CombineServiceNew.NHAP_NGOC_RONG_TRANH:
                                        CombineServiceNew.gI().startCombine(player, select);
                                        break;
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.WHIS:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            switch (mapId) {
                                case 48:
//                                    this.createOtherMenu(player, ConstNpc.WHIS, "Ta là Whis được Đại thiên sứ cử xuống Trái đất để thu thập lại các trang bị Thần linh bị kẻ xấu đánh cắp."
//                                            + " Ta sẽ bạn lại cho ngươi trang bị kích hoạt trong truyền thuyết nếu ngươi giao cho ta trang bị Thần linh.", 
//                                            "Hiến tế\nThần linh", "Hướng\ndẫn", "Đóng");
                                    super.openBaseMenu(player);
                                    return;
                                case 154:
//                                    createOtherMenu(player, ConstNpc.WHIS, "Thử đánh ta xem nào.\n"
//                                            + "Ngươi còn 1 lượt nữa cơ mà.", "Chế Tạo", "Học\ntuyệt kỹ", "Top 100", "[LV:" + player.levelKillWhis + "]");
//                                    return;
                                    createOtherMenu(player, ConstNpc.WHIS, "Ta sẽ giúp ngươi chế tạo trang bị thiên sứ", "Chế Tạo");
                                    return;
                                case 19:
                                    createOtherMenu(player, ConstNpc.WHIS, "Ta xuống đây để đưa ngươi đi.",
                                            "Vùng Đất Thần Linh", "Khu Vực\nChân Mệnh", "Hành Tinh\nRồng Đen", "Đóng");
                                    return;
                                case 217:
                                    createOtherMenu(player, ConstNpc.WHIS, "Đây là Khu Vực Chân Mệnh, Nơi để tìm kho báu quý hiếm, Ngươi định làm gì?.",
                                            "Cửa hàng\nChân Mệnh", "Đổi Chân Mệnh\nThiên Tử", "Về Nhà");
                                    return;
                                case 5:
                                    createOtherMenu(player, ConstNpc.WHIS, "Ta là Whis được Đại Thiên Sứ cử xuống Trái Đất để thu thập các trang bị thần bị kẻ xấu đánh cắp.\n"
                                            + "Ta sẽ ban lại cho ngươi trang bị kích hoạt trong truyền thuyết nếu ngươi cho ta trang bị Thần Linh",
                                            "Hiến tế Thần Linh");
                                    return;
                            }

                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.WHIS:
                                        switch (mapId) {
                                            case 154:
                                                switch (select) {
                                                    case 0: // nói chuyện => chế tạo đồ thiên thứ
                                                        CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_DO_THIEN_SU);
                                                        break;
//                                                    case 1: // Học tuyệt kỹ
//                                                        Item biKipTuyetKy = InventoryService.gI().findItemBagByTemp(player, (short) 1229);
//                                                        if (biKipTuyetKy != null && biKipTuyetKy.quantity >= 9999 && player.inventory.gold >= 10_000_000 && player.inventory.ruby >= 50000) {
//                                                            int skillID = player.gender == 0 ? 24 : player.gender == 1 ? 26 : 25;
//                                                            Skill newSkill = SkillUtil.createSkill(skillID, 1);
//                                                            String npcSay = "|1|Qua sẽ dạy ngươi tuyệt kỹ " + newSkill.template.name + "\n";
//                                                            npcSay += "|2|" + biKipTuyetKy.getName() + " " + biKipTuyetKy.quantity + "/9999\n";
//                                                            npcSay += "Giá vàng: 10.000.000\n";
//                                                            npcSay += "Giá hồng ngọc: 50k";
//                                                            createOtherMenu(player, ConstNpc.HOC_TUYET_KY, npcSay, "Đồng ý", "Từ chối");
//                                                            return;
//                                                        } else {
//                                                            int skillID = player.gender == 0 ? 24 : player.gender == 1 ? 26 : 25;
//                                                            Skill newSkill = SkillUtil.createSkill(skillID, 1);
//                                                            String npcSay = "|1|Qua sẽ dạy ngươi tuyệt kỹ " + newSkill.template.name + " 1\n";
//                                                            if (biKipTuyetKy == null || biKipTuyetKy.quantity < 9999) {
//                                                                if (biKipTuyetKy == null) {
//                                                                    npcSay += "|7|Bí kíp tuyệt kỹ" + " " + "0/9999\n";
//                                                                } else {
//                                                                    npcSay += "|7|Bí kíp tuyệt kỹ" + " " + biKipTuyetKy.quantity + "/9999\n";
//                                                                }
//                                                            } else {
//                                                                npcSay += "|2|" + biKipTuyetKy.getName() + " " + biKipTuyetKy.quantity + "/9999\n";
//                                                            }
//                                                            if (player.inventory.gold < 10_000_000) {
//                                                                npcSay += "|7|Giá vàng: 10.000.000\n";
//                                                            } else {
//                                                                npcSay += "|2|Giá vàng: 10.000.000\n";
//                                                            }
//                                                            if (player.inventory.ruby < 50000) {
//                                                                npcSay += "|7|Giá ngọc: 50000";
//                                                            } else {
//                                                                npcSay += "|2|Giá ngọc: 50000";
//                                                            }
//                                                            createOtherMenu(player, ConstNpc.HOC_TUYET_KY_2, npcSay, "Từ chối");
//                                                            return;
//                                                        }
//
//                                                    case 2: // Top đánh NPC whis
//                                                        Service.getInstance().showToplevelWhis(player);
//                                                        break;
//                                                    case 3:// khiêu chiến NPC whis
//                                                        player.lastTimeSwapWhis = System.currentTimeMillis();
//                                                        PlayerService.gI().savePlayer(player);
//
//                                                        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
//
//                                                        Runnable task = () -> {
//                                                            hide_npc(player, (int) ConstNpc.WHIS, 0);
//                                                            PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.PK_PVP);
//                                                            PlayerService.gI().playerMove(player, 485, 360);
//                                                            PlayerService.gI().setPos(player, 488, 360, 55);
//                                                            scheduler.shutdown();
//                                                        };
//                                                        scheduler.schedule(task, 1, TimeUnit.SECONDS);
//
//                                                        try {
//                                                            Boss_Whis dt = new Boss_Whis(Util.createIdDuongTank((int) player.id), BossData.WHIS_NPC, player.zone, this.cx, this.cy, player);
//                                                        } catch (Exception ex) {
//                                                            Logger.getLogger(NpcFactory.class.getName()).log(Level.SEVERE, null, ex);
//                                                        }
//                                                        player.zone.load_Me_To_Another(player);
                                                }
                                                return;
                                            case 5:
                                                switch (select) {
                                                    case 0:
                                                        createOtherMenu(player, 799455479, "Ngươi muốn hiến tế cho Bản thân hay Đệ tử", "Cho\nBản thân", "Cho\nĐệ tử", "Đóng");
                                                        return;
                                                    case 1:
                                                        NpcService.gI().createTutorial(player, avartar, "Ta là Whis được Đại thiên sứ cử xuống Trái đất để thu thập lại trang bị Thần linh\bbị kẻ xấu đánh cắp\n"
                                                                + "Hãy đi tiêu diệt kẻ xấu để giành lại trang bị Thần linh bị đánh cắp\n"
                                                                + "Hiến tế cho ta trang bị Thần linh, Ta sẽ ban cho ngươi trang bị kích hoạt tương ứng trong truyền thuyết\n"
                                                                + "Yêu cầu khi hiến tế:\b- Bản thân đang sử dụng trang bị Thần linh\b- Vàng trong hành trang: 2 tỷ vàng\b(không giới hạn số trang bị Thần linh trong 1 lần hiến tế)");
                                                        return;
                                                }
                                            case 19:
                                                switch (select) {
                                                    case 0:
                                                        if (player.playerTask.taskMain.id <= 30) {
                                                            Service.getInstance().sendThongBao(player, "Hãy hoàn thành nhiệm vụ 31 để tiếp tục");
                                                        } else {
                                                            ChangeMapService.gI().changeMapBySpaceShip(player, 212, -1, -1);
                                                        }
                                                        return;
                                                    case 1:
                                                        ChangeMapService.gI().changeMap(player, 217, -1, 100, 500);
                                                        return;
                                                    case 2:
                                                        ChangeMapService.gI().changeMap(player, 220, -1, 535, 20);
                                                        return;
                                                }
                                            case 217:
                                                switch (select) {
                                                    case 0:
                                                        ShopService.gI().openShopWhisThienSu(player, ConstNpc.SHOP_WHIS_THIEN_SU, 0);
                                                        return;

                                                    case 1:
                                                        createOtherMenu(player, ConstNpc.MENU_CHON_DA, "Ngươi sưu tầm được đủ nguyên liệu nào rồi?",
                                                                "Đá Lửa", "Đá Cam");
                                                        return;
                                                    case 2:
                                                        ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 525);
                                                        return;
//                                                    case 3:
//                                                        SendEffect.getInstance().send_danh_hieu(player, 647, 1, 5, 5, 5, 1);
//                                                        return;
//                                                    case 4:
//                                                        SendEffect.getInstance().send_danh_hieu(player, 646, 1, 5, 5, 5, 2);
//                                                        return;
//                                                    case 5:
//                                                        SendEffect.getInstance().send_danh_hieu(player, 356, 1, 5, 5, 5, 3);
//                                                        return;
                                                }
//                                            case 5:
//                                                switch (select) {
//                                                    case 0:
//                                                        boolean hasGodItem = false;
//
//                                                        for (Item item : player.inventory.itemsBody) {
//                                                            if (item != null && item.template != null) {
//                                                                if (item.template.id >= 555 && item.template.id <= 567) {
//                                                                    hasGodItem = true;
//                                                                    break;
//                                                                }
//                                                            }
//                                                        }
//
//                                                        if (!hasGodItem) {
//                                                            NpcService.gI().createTutorial(player, avartar, "Khi nào ngươi mặc trang bị thần linh thì tới đây nói chuyện tiếp");
//                                                            return;
//                                                        }
//                                                        createOtherMenu(player, ConstNpc.HIEN_TE_SU_PHU, "Danh sách vật phẩm hiến tế cho Whis:"
//                                                                + "\n1. Áo Thần Linh\n2. Quần Thần Linh\n3. Găng Thần Linh\n 4. Giày Thần Linh\n 5. Nhẫn Thần Linh\n"
//                                                                + "Ngươi sẽ nhận lại 5 trang bị kích hoạt tương ứng trong truyền thuyết\nHiến Tế (2 tỷ vàng)",
//                                                                "Hiến tế\nSư Phụ", "Hiến tế\nĐệ Tử");
//                                                        return;
//                                                }
                                        }
                                    case ConstNpc.HOC_TUYET_KY:
                                        switch (select) {
                                            case 0:
                                                Item biKipTuyetKy = InventoryService.gI().findItemBagByTemp(player, (short) 1229);
                                                int skillID = player.gender == 0 ? 24 : player.gender == 1 ? 26 : 25;
                                                Skill newSkill = SkillUtil.createSkill(skillID, 1);
                                                try {
                                                    Message msg = Service.getInstance().messageSubCommand((byte) 23);
                                                    msg.writer().writeShort(newSkill.skillId);
                                                    player.sendMessage(msg);
                                                    msg.cleanup();
                                                } catch (IOException e) {
                                                }
                                                try { // send effect susscess
                                                    Message msg = new Message(-81);
                                                    msg.writer().writeByte(0);
                                                    msg.writer().writeUTF("test");
                                                    msg.writer().writeUTF("test");
                                                    msg.writer().writeShort(tempId);
                                                    player.sendMessage(msg);
                                                    msg.cleanup();

                                                    msg = new Message(-81);
                                                    msg.writer().writeByte(1);
                                                    msg.writer().writeByte(2);
                                                    msg.writer().writeByte(InventoryService.gI().getIndexBag(player, biKipTuyetKy));
                                                    msg.writer().writeByte(-1);
                                                    player.sendMessage(msg);
                                                    msg.cleanup();

                                                    msg = new Message(-81);
                                                    msg.writer().writeByte(7);
                                                    msg.writer().writeShort(newSkill.template.iconId);
                                                    player.sendMessage(msg);
                                                    msg.cleanup();
                                                } catch (Exception e) {
                                                }
                                                Inventory inv = player.inventory;
                                                InventoryService.gI().subQuantityItemsBag(player, biKipTuyetKy, 9999);
                                                InventoryService.gI().sendItemBags(player);
                                                inv.subGold(10_000_000);
                                                inv.subRuby(50000);
                                                SkillUtil.setSkill(player, newSkill);
                                        }
                                        return;
                                    case 799455479:
                                        switch (select) {
                                            case 0:
//                                                if (!player.getSession().actived) {
//                                                    NpcService.gI().createTutorial(player, avartar, "Truy cập Trang chủ Nro9.Pro để mở Thành viên");
//                                                    return;
//                                                }
                                                int gender = player.gender;
                                                List<Integer> ao = Arrays.asList(555, 557, 559);
                                                List<Integer> quan = Arrays.asList(556, 558, 560);
                                                List<Integer> gang = Arrays.asList(562, 564, 566);
                                                List<Integer> giay = Arrays.asList(563, 565, 567);
                                                int nhan = 561;

                                                boolean dieuKien1 = player.inventory.itemsBody.get(0).isNotNullItem();
                                                boolean dieuKien2 = player.inventory.itemsBody.get(1).isNotNullItem();
                                                boolean dieuKien3 = player.inventory.itemsBody.get(2).isNotNullItem();
                                                boolean dieuKien4 = player.inventory.itemsBody.get(3).isNotNullItem();
                                                boolean dieuKien5 = player.inventory.itemsBody.get(4).isNotNullItem();

                                                boolean dieuKien1_1 = dieuKien1 && (player.inventory.itemsBody.get(0).template.id == ao.get(gender));
                                                boolean dieuKien2_1 = dieuKien2 && (player.inventory.itemsBody.get(1).template.id == quan.get(gender));
                                                boolean dieuKien3_1 = dieuKien3 && (player.inventory.itemsBody.get(2).template.id == gang.get(gender));
                                                boolean dieuKien4_1 = dieuKien4 && (player.inventory.itemsBody.get(3).template.id == giay.get(gender));
                                                boolean dieuKien5_1 = dieuKien5 && (player.inventory.itemsBody.get(4).template.id == nhan);

                                                boolean condition1 = dieuKien1_1;
                                                boolean condition2 = dieuKien2_1;
                                                boolean condition3 = dieuKien3_1;
                                                boolean condition4 = dieuKien4_1;
                                                boolean condition5 = dieuKien5_1;

                                                if (condition1 || condition2 || condition3 || condition4 || condition5) {
                                                    String npcsay = "Danh sách hiến tế cho Whis:\n";
                                                    int i = 1;
                                                    if (condition1) {
                                                        npcsay += i + ". " + player.inventory.itemsBody.get(0).template.name + "\n";
                                                        i++;
                                                    }
                                                    if (condition2) {
                                                        npcsay += i + ". " + player.inventory.itemsBody.get(1).template.name + "\n";
                                                        i++;
                                                    }
                                                    if (condition3) {
                                                        npcsay += i + ". " + player.inventory.itemsBody.get(2).template.name + "\n";
                                                        i++;
                                                    }
                                                    if (condition4) {
                                                        npcsay += i + ". " + player.inventory.itemsBody.get(3).template.name + "\n";
                                                        i++;
                                                    }
                                                    if (condition5) {
                                                        npcsay += i + ". " + player.inventory.itemsBody.get(4).template.name + "\n";
                                                        i++;
                                                    }

                                                    npcsay += "Ngươi sẽ nhận lại một trang bị kích hoạt tương ứng trong truyền thuyết.";

                                                    createOtherMenu(player, ConstNpc.HIEN_TE_SU_PHU, npcsay, "Hiến tế\n(2 tỷ vàng)", "Đóng");
                                                    return;
                                                } else {
                                                    NpcService.gI().createTutorial(player, avartar, "Khi nào ngươi mặc trang bị thần linh thì tới đây ta nói chuyện tiếp");
                                                    break;
                                                }

                                            case 1:

//                                                if (!player.getSession().actived) {
//                                                    NpcService.gI().createTutorial(player, avartar, "Truy cập Trang chủ Nro9.Pro để mở Thành viên");
//                                                    return;
//                                                }
                                                if (player.pet == null) {
                                                    NpcService.gI().createTutorial(player, avartar, "Ngươi cần phải có Đệ tử trước khi thực hiện");
                                                    return;
                                                }

                                                int gender_detu = player.pet.gender;

                                                List<Integer> ao2 = Arrays.asList(555, 557, 559);
                                                List<Integer> quan2 = Arrays.asList(556, 558, 560);
                                                List<Integer> gang2 = Arrays.asList(562, 564, 566);
                                                List<Integer> giay2 = Arrays.asList(563, 565, 567);
                                                int nhan2 = 561;

                                                boolean dieuKien12 = player.pet.inventory.itemsBody.get(0).isNotNullItem();
                                                boolean dieuKien22 = player.pet.inventory.itemsBody.get(1).isNotNullItem();
                                                boolean dieuKien32 = player.pet.inventory.itemsBody.get(2).isNotNullItem();
                                                boolean dieuKien42 = player.pet.inventory.itemsBody.get(3).isNotNullItem();
                                                boolean dieuKien52 = player.pet.inventory.itemsBody.get(4).isNotNullItem();

                                                boolean dieuKien1_12 = dieuKien12 && (player.pet.inventory.itemsBody.get(0).template.id == ao2.get(gender_detu));
                                                boolean dieuKien2_12 = dieuKien22 && (player.pet.inventory.itemsBody.get(1).template.id == quan2.get(gender_detu));
                                                boolean dieuKien3_12 = dieuKien32 && (player.pet.inventory.itemsBody.get(2).template.id == gang2.get(gender_detu));
                                                boolean dieuKien4_12 = dieuKien42 && (player.pet.inventory.itemsBody.get(3).template.id == giay2.get(gender_detu));
                                                boolean dieuKien5_12 = dieuKien52 && (player.pet.inventory.itemsBody.get(4).template.id == nhan2);

                                                boolean condition12 = dieuKien1_12;
                                                boolean condition22 = dieuKien2_12;
                                                boolean condition32 = dieuKien3_12;
                                                boolean condition42 = dieuKien4_12;
                                                boolean condition52 = dieuKien5_12;

                                                if (condition12 || condition22 || condition32 || condition42 || condition52) {
                                                    String npcsay = "Danh sách hiến tế cho Whis:\n";
                                                    int i = 1;
                                                    if (condition12) {
                                                        npcsay += i + ". " + player.pet.inventory.itemsBody.get(0).template.name + "\n";
                                                        i++;
                                                    }
                                                    if (condition22) {
                                                        npcsay += i + ". " + player.pet.inventory.itemsBody.get(1).template.name + "\n";
                                                        i++;
                                                    }
                                                    if (condition32) {
                                                        npcsay += i + ". " + player.pet.inventory.itemsBody.get(2).template.name + "\n";
                                                        i++;
                                                    }
                                                    if (condition42) {
                                                        npcsay += i + ". " + player.pet.inventory.itemsBody.get(3).template.name + "\n";
                                                        i++;
                                                    }
                                                    if (condition52) {
                                                        npcsay += i + ". " + player.pet.inventory.itemsBody.get(4).template.name + "\n";
                                                        i++;
                                                    }

                                                    npcsay += "Ngươi sẽ nhận lại một trang bị kích hoạt tương ứng trong truyền thuyết.";

                                                    createOtherMenu(player, ConstNpc.HIEN_TE_DE_TU, npcsay, "Hiến tế\n(2 tỷ vàng)", "Đóng");
                                                    return;
                                                } else {
                                                    NpcService.gI().createTutorial(player, avartar, "Khi nào đệ tử ngươi mặc trang bị thần linh thì tới đây ta nói chuyện tiếp");
                                                    break;
                                                }
                                        }

                                    case ConstNpc.HIEN_TE_SU_PHU:
                                        int i = 0;
                                        int gender = player.gender;
                                        List<Integer> ao = Arrays.asList(555, 557, 559);
                                        List<Integer> quan = Arrays.asList(556, 558, 560);
                                        List<Integer> gang = Arrays.asList(562, 564, 566);
                                        List<Integer> giay = Arrays.asList(563, 565, 567);

                                        List<Integer> aoSKH = Arrays.asList(555, 557, 559);
                                        List<Integer> quanSKH = Arrays.asList(556, 558, 560);
                                        List<Integer> gangSKH = Arrays.asList(562, 564, 566);
                                        List<Integer> giaySKH = Arrays.asList(563, 565, 567);
                                        int rada = 12;

                                        int[][] options = {{128, 129, 127}, {130, 131, 132}, {133, 135, 134}};
//                                                int skhId = ItemService.gI().randomSKHId((byte) 0);

                                        short itemId;

                                        int nhan = 561;

                                        boolean dieuKien1 = player.inventory.itemsBody.get(0).isNotNullItem();
                                        boolean dieuKien2 = player.inventory.itemsBody.get(1).isNotNullItem();
                                        boolean dieuKien3 = player.inventory.itemsBody.get(2).isNotNullItem();
                                        boolean dieuKien4 = player.inventory.itemsBody.get(3).isNotNullItem();
                                        boolean dieuKien5 = player.inventory.itemsBody.get(4).isNotNullItem();

                                        boolean dieuKien1_1 = dieuKien1 && (player.inventory.itemsBody.get(0).template.id == ao.get(gender));
                                        boolean dieuKien2_1 = dieuKien2 && (player.inventory.itemsBody.get(1).template.id == quan.get(gender));
                                        boolean dieuKien3_1 = dieuKien3 && (player.inventory.itemsBody.get(2).template.id == gang.get(gender));
                                        boolean dieuKien4_1 = dieuKien4 && (player.inventory.itemsBody.get(3).template.id == giay.get(gender));
                                        boolean dieuKien5_1 = dieuKien5 && (player.inventory.itemsBody.get(4).template.id == nhan);

                                        boolean condition1 = dieuKien1_1;
                                        boolean condition2 = dieuKien2_1;
                                        boolean condition3 = dieuKien3_1;
                                        boolean condition4 = dieuKien4_1;
                                        boolean condition5 = dieuKien5_1;

                                        if (condition1 || condition2 || condition3 || condition4 || condition5) {

                                            if (player.inventory.gold < 2_000_000_000) {
                                                NpcService.gI().createTutorial(player, avartar, "Ngươi còn thiếu " + Util.numberToMoney(2_000_000_000 - player.inventory.gold) + " Vàng");
                                                return;
                                            }
                                            player.inventory.gold -= 2_000_000_000;
                                            Service.getInstance().sendMoney(player);

                                            if (condition1) { // ÁO
                                                Item ao2 = ItemService.gI().createNewItem((short) gender);

                                                InventoryService.gI().removeItem(player.inventory.itemsBody, 0);

                                                Random random = new Random();
                                                int tyle = random.nextInt(100);
                                                ao2.itemOptions.add(new ItemOption(47, 3));

                                                if (tyle < 30) {
                                                    if (gender == 0) {
                                                        ao2.itemOptions.add(new ItemOption(129, 1));
                                                        ao2.itemOptions.add(new ItemOption(141, 100));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else if (gender == 1) {
                                                        ao2.itemOptions.add(new ItemOption(131, 1));
                                                        ao2.itemOptions.add(new ItemOption(143, 150));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else {
                                                        ao2.itemOptions.add(new ItemOption(135, 1));
                                                        ao2.itemOptions.add(new ItemOption(138, 100));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    }
                                                } else if (tyle < 60) {
                                                    if (gender == 0) {
                                                        ao2.itemOptions.add(new ItemOption(128, 1));
                                                        ao2.itemOptions.add(new ItemOption(140, 300));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else if (gender == 1) {
                                                        ao2.itemOptions.add(new ItemOption(130, 1));
                                                        ao2.itemOptions.add(new ItemOption(142, 100));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else {
                                                        ao2.itemOptions.add(new ItemOption(133, 1));
                                                        ao2.itemOptions.add(new ItemOption(136, 150));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    }

                                                } else {
                                                    ao2.itemOptions.add(new ItemOption(248, 1));
                                                    ao2.itemOptions.add(new ItemOption(249, 1));
                                                    ao2.itemOptions.add(new ItemOption(30, 1));

                                                }
                                                InventoryService.gI().addItemBody(player, ao2);
                                                InventoryService.gI().sendItemBody(player);
                                                i++;
                                            }

                                            if (condition2) { // QUẦN
                                                Item ao2 = ItemService.gI().createNewItem((short) (gender + 6));
                                                InventoryService.gI().removeItem(player.inventory.itemsBody, 1);
//                                                      ao2.template.id = (short) (gender + 6);
                                                List<ItemOption> optionsToRemove = new ArrayList<>(); // Danh sách các ItemOption cần xóa
                                                for (ItemOption itopt : ao2.itemOptions) {
                                                    int optionId = itopt.optionTemplate.id;
                                                    if (optionId >= 0 && optionId <= 233) {
                                                        optionsToRemove.add(itopt);
                                                    }
                                                }

                                                Random random = new Random();
                                                int tyle = random.nextInt(100);

                                                ao2.itemOptions.removeAll(optionsToRemove);
                                                ao2.itemOptions.add(new ItemOption(6, 20));

                                                if (tyle < 30) {
                                                    if (gender == 0) {
                                                        ao2.itemOptions.add(new ItemOption(129, 1));
                                                        ao2.itemOptions.add(new ItemOption(141, 100));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else if (gender == 1) {
                                                        ao2.itemOptions.add(new ItemOption(131, 1));
                                                        ao2.itemOptions.add(new ItemOption(143, 150));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else {
                                                        ao2.itemOptions.add(new ItemOption(135, 1));
                                                        ao2.itemOptions.add(new ItemOption(138, 100));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    }
                                                } else if (tyle < 60) {
                                                    if (gender == 0) {
                                                        ao2.itemOptions.add(new ItemOption(128, 1));
                                                        ao2.itemOptions.add(new ItemOption(140, 300));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else if (gender == 1) {
                                                        ao2.itemOptions.add(new ItemOption(130, 1));
                                                        ao2.itemOptions.add(new ItemOption(142, 100));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else {
                                                        ao2.itemOptions.add(new ItemOption(133, 1));
                                                        ao2.itemOptions.add(new ItemOption(136, 150));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    }

                                                } else {
                                                    ao2.itemOptions.add(new ItemOption(248, 1));
                                                    ao2.itemOptions.add(new ItemOption(249, 1));
                                                    ao2.itemOptions.add(new ItemOption(30, 1));
                                                }
                                                InventoryService.gI().addItemBody(player, ao2);
                                                InventoryService.gI().sendItemBody(player);
                                                Service.getInstance().Send_Caitrang(player);
                                                Service.getInstance().Send_Info_NV(player);
                                                i++;
                                            }
                                            if (condition3) { // GĂNG

                                                Item ao2 = ItemService.gI().createNewItem((short) (gender + 21));
                                                InventoryService.gI().removeItem(player.inventory.itemsBody, 2);

                                                List<ItemOption> optionsToRemove = new ArrayList<>(); // Danh sách các ItemOption cần xóa
                                                for (ItemOption itopt : ao2.itemOptions) {
                                                    int optionId = itopt.optionTemplate.id;
                                                    if (optionId >= 0 && optionId <= 233) {
                                                        optionsToRemove.add(itopt);
                                                    }
                                                }

                                                Random random = new Random();
                                                int tyle = random.nextInt(100);

                                                ao2.itemOptions.removeAll(optionsToRemove);
                                                ao2.itemOptions.add(new ItemOption(0, 5));

                                                if (tyle < 30) {
                                                    if (gender == 0) {
                                                        ao2.itemOptions.add(new ItemOption(129, 1));
                                                        ao2.itemOptions.add(new ItemOption(141, 100));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else if (gender == 1) {
                                                        ao2.itemOptions.add(new ItemOption(131, 1));
                                                        ao2.itemOptions.add(new ItemOption(143, 150));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else {
                                                        ao2.itemOptions.add(new ItemOption(135, 1));
                                                        ao2.itemOptions.add(new ItemOption(138, 100));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    }
                                                } else if (tyle < 60) {
                                                    if (gender == 0) {
                                                        ao2.itemOptions.add(new ItemOption(128, 1));
                                                        ao2.itemOptions.add(new ItemOption(140, 300));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else if (gender == 1) {
                                                        ao2.itemOptions.add(new ItemOption(130, 1));
                                                        ao2.itemOptions.add(new ItemOption(142, 100));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else {
                                                        ao2.itemOptions.add(new ItemOption(133, 1));
                                                        ao2.itemOptions.add(new ItemOption(136, 150));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    }

                                                } else {
                                                    ao2.itemOptions.add(new ItemOption(248, 1));
                                                    ao2.itemOptions.add(new ItemOption(249, 1));
                                                    ao2.itemOptions.add(new ItemOption(30, 1));
                                                }
                                                InventoryService.gI().addItemBody(player, ao2);
                                                InventoryService.gI().sendItemBody(player);
                                                Service.getInstance().Send_Caitrang(player);
                                                Service.getInstance().Send_Info_NV(player);
                                                i++;
                                            }
                                            if (condition4) { // GIẦY

                                                Item ao2 = ItemService.gI().createNewItem((short) (gender + 27));
                                                InventoryService.gI().removeItem(player.inventory.itemsBody, 3);

                                                List<ItemOption> optionsToRemove = new ArrayList<>(); // Danh sách các ItemOption cần xóa
                                                for (ItemOption itopt : ao2.itemOptions) {
                                                    int optionId = itopt.optionTemplate.id;
                                                    if (optionId >= 0 && optionId <= 233) {
                                                        optionsToRemove.add(itopt);
                                                    }
                                                }

                                                Random random = new Random();
                                                int tyle = random.nextInt(100);

                                                ao2.itemOptions.removeAll(optionsToRemove);
                                                ao2.itemOptions.add(new ItemOption(7, 10));

                                                if (tyle < 30) {
                                                    if (gender == 0) {
                                                        ao2.itemOptions.add(new ItemOption(129, 1));
                                                        ao2.itemOptions.add(new ItemOption(141, 100));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else if (gender == 1) {
                                                        ao2.itemOptions.add(new ItemOption(131, 1));
                                                        ao2.itemOptions.add(new ItemOption(143, 150));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else {
                                                        ao2.itemOptions.add(new ItemOption(135, 1));
                                                        ao2.itemOptions.add(new ItemOption(138, 100));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    }
                                                } else if (tyle < 60) {
                                                    if (gender == 0) {
                                                        ao2.itemOptions.add(new ItemOption(128, 1));
                                                        ao2.itemOptions.add(new ItemOption(140, 300));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else if (gender == 1) {
                                                        ao2.itemOptions.add(new ItemOption(130, 1));
                                                        ao2.itemOptions.add(new ItemOption(142, 100));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else {
                                                        ao2.itemOptions.add(new ItemOption(133, 1));
                                                        ao2.itemOptions.add(new ItemOption(136, 150));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    }

                                                } else {
                                                    ao2.itemOptions.add(new ItemOption(248, 1));
                                                    ao2.itemOptions.add(new ItemOption(249, 1));
                                                    ao2.itemOptions.add(new ItemOption(30, 1));
                                                }
                                                InventoryService.gI().addItemBody(player, ao2);
                                                InventoryService.gI().sendItemBody(player);
                                                Service.getInstance().Send_Caitrang(player);
                                                Service.getInstance().Send_Info_NV(player);
                                                i++;
                                            }
                                            if (condition5) { // RADA
                                                Item ao2 = ItemService.gI().createNewItem((short) 12);
                                                InventoryService.gI().removeItem(player.inventory.itemsBody, 4);

                                                List<ItemOption> optionsToRemove = new ArrayList<>(); // Danh sách các ItemOption cần xóa
                                                for (ItemOption itopt : ao2.itemOptions) {
                                                    int optionId = itopt.optionTemplate.id;
                                                    if (optionId >= 0 && optionId <= 233) {
                                                        optionsToRemove.add(itopt);
                                                    }
                                                }

                                                Random random = new Random();
                                                int tyle = random.nextInt(100);

                                                ao2.itemOptions.removeAll(optionsToRemove);
                                                ao2.itemOptions.add(new ItemOption(14, 1));

                                                if (tyle < 30) {
                                                    if (gender == 0) {
                                                        ao2.itemOptions.add(new ItemOption(129, 1));
                                                        ao2.itemOptions.add(new ItemOption(141, 100));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else if (gender == 1) {
                                                        ao2.itemOptions.add(new ItemOption(131, 1));
                                                        ao2.itemOptions.add(new ItemOption(143, 150));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else {
                                                        ao2.itemOptions.add(new ItemOption(135, 1));
                                                        ao2.itemOptions.add(new ItemOption(138, 100));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    }
                                                } else if (tyle < 60) {
                                                    if (gender == 0) {
                                                        ao2.itemOptions.add(new ItemOption(128, 1));
                                                        ao2.itemOptions.add(new ItemOption(140, 300));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else if (gender == 1) {
                                                        ao2.itemOptions.add(new ItemOption(130, 1));
                                                        ao2.itemOptions.add(new ItemOption(142, 100));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else {
                                                        ao2.itemOptions.add(new ItemOption(133, 1));
                                                        ao2.itemOptions.add(new ItemOption(136, 150));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    }

                                                } else {
                                                    ao2.itemOptions.add(new ItemOption(248, 1));
                                                    ao2.itemOptions.add(new ItemOption(249, 1));
                                                    ao2.itemOptions.add(new ItemOption(30, 1));
                                                }
                                                InventoryService.gI().addItemBody(player, ao2);
                                                InventoryService.gI().sendItemBody(player);
                                                Service.getInstance().Send_Caitrang(player);
                                                Service.getInstance().Send_Info_NV(player);
                                                i++;
                                            }
                                            NpcService.gI().createTutorial(player, avartar, "Ba la ca ... ca ca ca... Um ba mi xa ki... ca ca...\n"
                                                    + "Na na ca ca... la la... sa da ma ta ro bu ki....\n"
                                                    + "....................\n"
                                                    + "Bạn vừa hiến tế thành công cho Whis " + i + " trang bị Thần linh và nhận được " + i + " trang bị kích hoạt trong truyền thuyết.");

                                            InventoryService.gI().sendItemBody(player);
                                            Service.getInstance().Send_Caitrang(player);
                                            Service.getInstance().Send_Info_NV(player);
                                            break;
                                        }
                                        break;
                                    case ConstNpc.HIEN_TE_DE_TU:
                                        int iPet = 0;
                                        int genderPet = player.gender;
                                        List<Integer> aoPet = Arrays.asList(555, 557, 559);
                                        List<Integer> quanPet = Arrays.asList(556, 558, 560);
                                        List<Integer> gangPet = Arrays.asList(562, 564, 566);
                                        List<Integer> giayPet = Arrays.asList(563, 565, 567);

                                        List<Integer> aoSKHPet = Arrays.asList(555, 557, 559);
                                        List<Integer> quanSKHPet = Arrays.asList(556, 558, 560);
                                        List<Integer> gangSKHPet = Arrays.asList(562, 564, 566);
                                        List<Integer> giaySKHPet = Arrays.asList(563, 565, 567);
                                        int radaPet = 12;

                                        int[][] optionsPet = {{128, 129, 127}, {130, 131, 132}, {133, 135, 134}};
//                                                int skhId = ItemService.gI().randomSKHId((byte) 0);

                                        short itemIdPet;

                                        int nhanPet = 561;

                                        boolean dieuKienPet1 = player.pet.inventory.itemsBody.get(0).isNotNullItem();
                                        boolean dieuKienPet2 = player.pet.inventory.itemsBody.get(1).isNotNullItem();
                                        boolean dieuKienPet3 = player.pet.inventory.itemsBody.get(2).isNotNullItem();
                                        boolean dieuKienPet4 = player.pet.inventory.itemsBody.get(3).isNotNullItem();
                                        boolean dieuKienPet5 = player.pet.inventory.itemsBody.get(4).isNotNullItem();

                                        boolean dieuKienPet1_1 = dieuKienPet1 && (player.pet.inventory.itemsBody.get(0).template.id == aoPet.get(genderPet));
                                        boolean dieuKienPet2_1 = dieuKienPet2 && (player.pet.inventory.itemsBody.get(1).template.id == quanPet.get(genderPet));
                                        boolean dieuKienPet3_1 = dieuKienPet3 && (player.pet.inventory.itemsBody.get(2).template.id == gangPet.get(genderPet));
                                        boolean dieuKienPet4_1 = dieuKienPet4 && (player.pet.inventory.itemsBody.get(3).template.id == giayPet.get(genderPet));
                                        boolean dieuKienPet5_1 = dieuKienPet5 && (player.pet.inventory.itemsBody.get(4).template.id == nhanPet);

                                        boolean conditionPet1 = dieuKienPet1_1;
                                        boolean conditionPet2 = dieuKienPet2_1;
                                        boolean conditionPet3 = dieuKienPet3_1;
                                        boolean conditionPet4 = dieuKienPet4_1;
                                        boolean conditionPet5 = dieuKienPet5_1;

                                        if (conditionPet1 || conditionPet2 || conditionPet3 || conditionPet4 || conditionPet5) {

                                            if (player.inventory.gold < 2_000_000_000) {
                                                NpcService.gI().createTutorial(player, avartar, "Ngươi còn thiếu " + Util.numberToMoney(2_000_000_000 - player.inventory.gold) + " Vàng");
                                                return;
                                            }
                                            player.inventory.gold -= 2_000_000_000;
                                            Service.getInstance().sendMoney(player);

                                            if (conditionPet1) { // ÁO
                                                Item ao2 = ItemService.gI().createNewItem((short) genderPet);

                                                InventoryService.gI().removeItem(player.pet.inventory.itemsBody, 0);

                                                Random random = new Random();
                                                int tyle = random.nextInt(100);
                                                ao2.itemOptions.add(new ItemOption(47, 3));

                                                if (tyle < 30) {
                                                    if (genderPet == 0) {
                                                        ao2.itemOptions.add(new ItemOption(129, 1));
                                                        ao2.itemOptions.add(new ItemOption(141, 100));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else if (genderPet == 1) {
                                                        ao2.itemOptions.add(new ItemOption(131, 1));
                                                        ao2.itemOptions.add(new ItemOption(143, 150));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else {
                                                        ao2.itemOptions.add(new ItemOption(135, 1));
                                                        ao2.itemOptions.add(new ItemOption(138, 100));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    }
                                                } else if (tyle < 60) {
                                                    if (genderPet == 0) {
                                                        ao2.itemOptions.add(new ItemOption(128, 1));
                                                        ao2.itemOptions.add(new ItemOption(140, 300));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else if (genderPet == 1) {
                                                        ao2.itemOptions.add(new ItemOption(130, 1));
                                                        ao2.itemOptions.add(new ItemOption(142, 100));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else {
                                                        ao2.itemOptions.add(new ItemOption(133, 1));
                                                        ao2.itemOptions.add(new ItemOption(136, 150));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    }

                                                } else {
                                                    ao2.itemOptions.add(new ItemOption(248, 1));
                                                    ao2.itemOptions.add(new ItemOption(249, 1));
                                                    ao2.itemOptions.add(new ItemOption(30, 1));
                                                }
                                                InventoryService.gI().addItemBody(player.pet, ao2);
                                                InventoryService.gI().sendItemBody(player.pet);
                                                iPet++;
                                            }

                                            if (conditionPet2) { // QUẦN
                                                Item ao2 = ItemService.gI().createNewItem((short) (genderPet + 6));
                                                InventoryService.gI().removeItem(player.pet.inventory.itemsBody, 1);
//                                                      ao2.template.id = (short) (gender + 6);
                                                List<ItemOption> optionsToRemove = new ArrayList<>(); // Danh sách các ItemOption cần xóa
                                                for (ItemOption itopt : ao2.itemOptions) {
                                                    int optionId = itopt.optionTemplate.id;
                                                    if (optionId >= 0 && optionId <= 233) {
                                                        optionsToRemove.add(itopt);
                                                    }
                                                }

                                                Random random = new Random();
                                                int tyle = random.nextInt(100);

                                                ao2.itemOptions.removeAll(optionsToRemove);
                                                ao2.itemOptions.add(new ItemOption(6, 20));

                                                if (tyle < 30) {
                                                    if (genderPet == 0) {
                                                        ao2.itemOptions.add(new ItemOption(129, 1));
                                                        ao2.itemOptions.add(new ItemOption(141, 100));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else if (genderPet == 1) {
                                                        ao2.itemOptions.add(new ItemOption(131, 1));
                                                        ao2.itemOptions.add(new ItemOption(143, 150));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else {
                                                        ao2.itemOptions.add(new ItemOption(135, 1));
                                                        ao2.itemOptions.add(new ItemOption(138, 100));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    }
                                                } else if (tyle < 60) {
                                                    if (genderPet == 0) {
                                                        ao2.itemOptions.add(new ItemOption(128, 1));
                                                        ao2.itemOptions.add(new ItemOption(140, 300));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else if (genderPet == 1) {
                                                        ao2.itemOptions.add(new ItemOption(130, 1));
                                                        ao2.itemOptions.add(new ItemOption(142, 100));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else {
                                                        ao2.itemOptions.add(new ItemOption(133, 1));
                                                        ao2.itemOptions.add(new ItemOption(136, 150));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    }

                                                } else {
                                                    ao2.itemOptions.add(new ItemOption(248, 1));
                                                    ao2.itemOptions.add(new ItemOption(249, 1));
                                                    ao2.itemOptions.add(new ItemOption(30, 1));
                                                }
                                                InventoryService.gI().addItemBody(player.pet, ao2);
                                                InventoryService.gI().sendItemBody(player.pet);
                                                Service.getInstance().Send_Caitrang(player.pet);
                                                Service.getInstance().Send_Info_NV(player.pet);
                                                iPet++;
                                            }
                                            if (conditionPet3) { // GĂNG

                                                Item ao2 = ItemService.gI().createNewItem((short) (genderPet + 21));
                                                InventoryService.gI().removeItem(player.pet.inventory.itemsBody, 2);

                                                List<ItemOption> optionsToRemove = new ArrayList<>(); // Danh sách các ItemOption cần xóa
                                                for (ItemOption itopt : ao2.itemOptions) {
                                                    int optionId = itopt.optionTemplate.id;
                                                    if (optionId >= 0 && optionId <= 233) {
                                                        optionsToRemove.add(itopt);
                                                    }
                                                }

                                                Random random = new Random();
                                                int tyle = random.nextInt(100);

                                                ao2.itemOptions.removeAll(optionsToRemove);
                                                ao2.itemOptions.add(new ItemOption(0, 5));

                                                if (tyle < 30) {
                                                    if (genderPet == 0) {
                                                        ao2.itemOptions.add(new ItemOption(129, 1));
                                                        ao2.itemOptions.add(new ItemOption(141, 100));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else if (genderPet == 1) {
                                                        ao2.itemOptions.add(new ItemOption(131, 1));
                                                        ao2.itemOptions.add(new ItemOption(143, 150));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else {
                                                        ao2.itemOptions.add(new ItemOption(135, 1));
                                                        ao2.itemOptions.add(new ItemOption(138, 100));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    }
                                                } else if (tyle < 60) {
                                                    if (genderPet == 0) {
                                                        ao2.itemOptions.add(new ItemOption(128, 1));
                                                        ao2.itemOptions.add(new ItemOption(140, 300));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else if (genderPet == 1) {
                                                        ao2.itemOptions.add(new ItemOption(130, 1));
                                                        ao2.itemOptions.add(new ItemOption(142, 100));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else {
                                                        ao2.itemOptions.add(new ItemOption(133, 1));
                                                        ao2.itemOptions.add(new ItemOption(136, 150));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    }

                                                } else {
                                                    ao2.itemOptions.add(new ItemOption(248, 1));
                                                    ao2.itemOptions.add(new ItemOption(249, 1));
                                                    ao2.itemOptions.add(new ItemOption(30, 1));
                                                }
                                                InventoryService.gI().addItemBody(player.pet, ao2);
                                                InventoryService.gI().sendItemBody(player.pet);
                                                Service.getInstance().Send_Caitrang(player.pet);
                                                Service.getInstance().Send_Info_NV(player.pet);
                                                iPet++;
                                            }
                                            if (conditionPet4) { // GIẦY

                                                Item ao2 = ItemService.gI().createNewItem((short) (genderPet + 27));
                                                InventoryService.gI().removeItem(player.pet.inventory.itemsBody, 3);

                                                List<ItemOption> optionsToRemove = new ArrayList<>(); // Danh sách các ItemOption cần xóa
                                                for (ItemOption itopt : ao2.itemOptions) {
                                                    int optionId = itopt.optionTemplate.id;
                                                    if (optionId >= 0 && optionId <= 233) {
                                                        optionsToRemove.add(itopt);
                                                    }
                                                }

                                                Random random = new Random();
                                                int tyle = random.nextInt(100);

                                                ao2.itemOptions.removeAll(optionsToRemove);
                                                ao2.itemOptions.add(new ItemOption(7, 10));

                                                if (tyle < 30) {
                                                    if (genderPet == 0) {
                                                        ao2.itemOptions.add(new ItemOption(129, 1));
                                                        ao2.itemOptions.add(new ItemOption(141, 100));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else if (genderPet == 1) {
                                                        ao2.itemOptions.add(new ItemOption(131, 1));
                                                        ao2.itemOptions.add(new ItemOption(143, 150));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else {
                                                        ao2.itemOptions.add(new ItemOption(135, 1));
                                                        ao2.itemOptions.add(new ItemOption(138, 100));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    }
                                                } else if (tyle < 60) {
                                                    if (genderPet == 0) {
                                                        ao2.itemOptions.add(new ItemOption(128, 1));
                                                        ao2.itemOptions.add(new ItemOption(140, 300));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else if (genderPet == 1) {
                                                        ao2.itemOptions.add(new ItemOption(130, 1));
                                                        ao2.itemOptions.add(new ItemOption(142, 100));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else {
                                                        ao2.itemOptions.add(new ItemOption(133, 1));
                                                        ao2.itemOptions.add(new ItemOption(136, 150));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    }

                                                } else {
                                                    ao2.itemOptions.add(new ItemOption(248, 1));
                                                    ao2.itemOptions.add(new ItemOption(249, 1));
                                                    ao2.itemOptions.add(new ItemOption(30, 1));
                                                }
                                                InventoryService.gI().addItemBody(player.pet, ao2);
                                                InventoryService.gI().sendItemBody(player.pet);
                                                Service.getInstance().Send_Caitrang(player.pet);
                                                Service.getInstance().Send_Info_NV(player.pet);
                                                iPet++;
                                            }
                                            if (conditionPet5) { // RADA
                                                Item ao2 = ItemService.gI().createNewItem((short) 12);
                                                InventoryService.gI().removeItem(player.pet.inventory.itemsBody, 4);

                                                List<ItemOption> optionsToRemove = new ArrayList<>(); // Danh sách các ItemOption cần xóa
                                                for (ItemOption itopt : ao2.itemOptions) {
                                                    int optionId = itopt.optionTemplate.id;
                                                    if (optionId >= 0 && optionId <= 233) {
                                                        optionsToRemove.add(itopt);
                                                    }
                                                }

                                                Random random = new Random();
                                                int tyle = random.nextInt(100);

                                                ao2.itemOptions.removeAll(optionsToRemove);
                                                ao2.itemOptions.add(new ItemOption(14, 1));

                                                if (tyle < 30) {
                                                    if (genderPet == 0) {
                                                        ao2.itemOptions.add(new ItemOption(129, 1));
                                                        ao2.itemOptions.add(new ItemOption(141, 100));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else if (genderPet == 1) {
                                                        ao2.itemOptions.add(new ItemOption(131, 1));
                                                        ao2.itemOptions.add(new ItemOption(143, 150));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else {
                                                        ao2.itemOptions.add(new ItemOption(135, 1));
                                                        ao2.itemOptions.add(new ItemOption(138, 100));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    }
                                                } else if (tyle < 60) {
                                                    if (genderPet == 0) {
                                                        ao2.itemOptions.add(new ItemOption(128, 1));
                                                        ao2.itemOptions.add(new ItemOption(140, 300));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else if (genderPet == 1) {
                                                        ao2.itemOptions.add(new ItemOption(130, 1));
                                                        ao2.itemOptions.add(new ItemOption(142, 100));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    } else {
                                                        ao2.itemOptions.add(new ItemOption(133, 1));
                                                        ao2.itemOptions.add(new ItemOption(136, 150));
                                                        ao2.itemOptions.add(new ItemOption(30, 1));
                                                    }

                                                } else {
                                                    ao2.itemOptions.add(new ItemOption(248, 1));
                                                    ao2.itemOptions.add(new ItemOption(249, 1));
                                                    ao2.itemOptions.add(new ItemOption(30, 1));
                                                }
                                                InventoryService.gI().addItemBody(player.pet, ao2);
                                                InventoryService.gI().sendItemBody(player.pet);
                                                Service.getInstance().Send_Caitrang(player.pet);
                                                Service.getInstance().Send_Info_NV(player.pet);
                                                iPet++;
                                            }
                                            NpcService.gI().createTutorial(player, avartar, "Ba la ca ... ca ca ca... Um ba mi xa ki... ca ca...\n"
                                                    + "Na na ca ca... la la... sa da ma ta ro bu ki....\n"
                                                    + "....................\n"
                                                    + "Bạn vừa hiến tế thành công cho Whis " + iPet + " trang bị Thần linh và nhận được " + iPet + " trang bị kích hoạt trong truyền thuyết.");

                                            InventoryService.gI().sendItemBody(player.pet);
                                            Service.getInstance().Send_Caitrang(player.pet);
                                            Service.getInstance().Send_Info_NV(player.pet);
                                            break;
                                        }
                                        break;
                                    case ConstNpc.MENU_START_COMBINE:
                                        switch (player.combineNew.typeCombine) {
                                            case CombineServiceNew.NANG_CAP_DO_THIEN_SU -> {
                                                if (select == 0) {
                                                    CombineServiceNew.gI().startCombine(player, select);
                                                }
                                            }
                                        }
                                        break;
                                    case ConstNpc.MENU_CHON_DA:
                                        switch (select) {
                                            case 0:
                                                createOtherMenu(player, ConstNpc.DOI_CHAN_MENH_LUA, "Ngươi cần tìm đủ số lượng Đá Cam để có thể đổi lấy Chân Mệnh Thiên Tử với công thức như sau:"
                                                        + "\nCần x9 Đá Lửa để đổi lấy Chân Mệnh Tiên Tử 2-5 ngày"
                                                        + "\nCần x99 Đá Lửa để đổi lấy Chân Mệnh Tiên Tử vĩnh viễn",
                                                        "2-5 ngày\n(-4 Tỷ vàng)", "Vĩnh viễn\n(-8 Tỷ vàng)");
                                                return;
                                            case 1:
                                                createOtherMenu(player, ConstNpc.DOI_CHAN_MENH_CAM, "Ngươi cần tìm đủ số lượng Đá Cam để có thể đổi lấy Chân Mệnh Thiên Tử với công thức như sau:"
                                                        + "\nCần x99 Đá Cam để đổi lấy Chân Mệnh Tiên Tử 2-5 ngày"
                                                        + "\nCần x999 Đá Cam để đổi lấy Chân Mệnh Tiên Tử vĩnh viễn",
                                                        "2-5 ngày\n(-4 Tỷ vàng)", "Vĩnh viễn\n(-8 Tỷ vàng)");
                                                return;

                                        }
                                        break;
                                    case ConstNpc.DOI_CHAN_MENH_LUA:
                                        switch (select) {
                                            case 0:
                                                doiChanMenh(player, 1452, 9, 4_000_000_000L, false); // false = ngày 2-5
                                                break;
                                            case 1:
                                                doiChanMenh(player, 1452, 99, 8_000_000_000L, true); //vĩnh viễn
                                                break;
                                        }
                                        break;
                                    case ConstNpc.DOI_CHAN_MENH_CAM:
                                        switch (select) {
                                            case 0:
                                                doiChanMenh(player, 1450, 99, 4_000_000_000L, false); // false = ngày 2-5
                                                break;
                                            case 1:
                                                doiChanMenh(player, 1450, 999, 8_000_000_000L, true); //vĩnh viễn
                                                break;
                                        }
                                        break;
                                }
                            }
                        }
                    };
                    break;

                case ConstNpc.BO_MONG:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                    if (this.mapId == 47 || this.mapId == 84) {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Xin chào, cậu muốn tôi giúp gì?",
                                                "Nhiệm vụ\nhàng ngày", "Mã quà tặng", "Nhận ngọc\nmiễn phí", "Từ chối");
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 47 || this.mapId == 84) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                if (player.playerTask.sideTask.template != null) {
                                                    String npcSay = "Nhiệm vụ hiện tại: "
                                                            + player.playerTask.sideTask.getName() + " ("
                                                            + player.playerTask.sideTask.getLevel() + ")"
                                                            + "\nHiện tại đã hoàn thành: "
                                                            + player.playerTask.sideTask.count + "/"
                                                            + player.playerTask.sideTask.maxCount + " ("
                                                            + player.playerTask.sideTask.getPercentProcess()
                                                            + "%)\nSố nhiệm vụ còn lại trong ngày: "
                                                            + player.playerTask.sideTask.leftTask + "/"
                                                            + ConstTask.MAX_SIDE_TASK;
                                                    this.createOtherMenu(player, ConstNpc.MENU_OPTION_PAY_SIDE_TASK,
                                                            npcSay, "Trả nhiệm\nvụ", "Hủy nhiệm\nvụ");
                                                } else {
                                                    this.createOtherMenu(player, ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK,
                                                            "Tôi có vài nhiệm vụ theo cấp bậc, "
                                                            + "sức cậu có thể làm được cái nào?",
                                                            "Dễ", "Bình thường", "Khó", "Siêu khó", "Từ chối");
                                                }
                                                break;

                                            case 1:
                                                Input.gI().createFormGiftCode(player);
                                                break;
                                            case 2:
                                                TaskService.gI().checkDoneAchivements(player);
                                                TaskService.gI().sendAchivement(player);
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK) {
                                        switch (select) {
                                            case 0:
                                            case 1:
                                            case 2:
                                            case 3:
                                                TaskService.gI().changeSideTask(player, (byte) select);
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PAY_SIDE_TASK) {
                                        switch (select) {
                                            case 0:
                                                TaskService.gI().paySideTask(player);
                                                break;
                                            case 1:
                                                TaskService.gI().removeSideTask(player);
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.GOKU_SSJ:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 80) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Xin chào, tôi có thể giúp gì cho cậu?", "Tới hành tinh\nYardart",
                                            "Từ chối");
                                } else if (this.mapId == 131) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Xin chào, tôi có thể giúp gì cho cậu?", "Quay về", "Từ chối");
                                } else {
                                    super.openBaseMenu(player);
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.BASE_MENU:
                                        if (this.mapId == 80) {
                                            // if (select == 0) {
                                            // if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_24_0) {
                                            // ChangeMapService.gI().changeMapBySpaceShip(player, 160, -1, 168);
                                            // } else {
                                            // this.npcChat(player, "Xin lỗi, tôi chưa thể đưa cậu tới nơi đó lúc
                                            // này...");
                                            // }
                                            // } else
                                            if (select == 0) {
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 131, -1, 940);
                                            }
                                        } else if (this.mapId == 131) {
                                            if (select == 0) {
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 80, -1, 870);
                                            }
                                        }
                                        break;
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.GOKU_SSJ_2:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 133) {
                                    Item biKiep = InventoryService.gI().findItem(player.inventory.itemsBag, 590);
                                    int soLuong = (biKiep != null) ? biKiep.quantity : 0;

                                    if (soLuong >= 10000) {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Bạn đang có " + soLuong
                                                + " bí kiếp.\n"
                                                + "Hãy kiếm đủ 10000 bí kiếp tôi sẽ dạy bạn cách dịch chuyển tức thời của người Yardart",
                                                "Học dịch\nchuyển", "Đóng");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Bạn đang có " + soLuong
                                                + " bí kiếp.\n"
                                                + "Hãy kiếm đủ 10000 bí kiếp tôi sẽ dạy bạn cách dịch chuyển tức thời của người Yardart",
                                                "Đóng");
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player) && this.mapId == 133) {
                                Item biKiep = InventoryService.gI().findItem(player.inventory.itemsBag, 590);
                                int soLuong = (biKiep != null) ? biKiep.quantity : 0;

                                if (soLuong >= 10000 && InventoryService.gI().getCountEmptyBag(player) > 0) {
                                    Item yardart = ItemService.gI().createNewItem((short) (player.gender + 592));
                                    yardart.itemOptions.add(new ItemOption(47, 400));
                                    yardart.itemOptions.add(new ItemOption(108, 10));
                                    InventoryService.gI().addItemBag(player, yardart, 0);
                                    InventoryService.gI().subQuantityItemsBag(player, biKiep, 10000);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendThongBao(player, "Bạn vừa nhận được trang phục tộc Yardart");
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.CADIC:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 184) {
                                    if (player.iDMark.getTranhNgoc() == 2) {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                "Cút!Ta không nói chuyện với sinh vật hạ đẳng", "Đóng");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                "Hãy mang ngọc rồng về cho ta", "Đưa ngọc", "Đóng");
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (this.mapId == 184) {
                                switch (select) {
                                    case 0:
                                        if (player.iDMark.getTranhNgoc() == 1 && player.isHoldNamecBallTranhDoat) {
                                            if (!Util.canDoWithTime(player.lastTimePickItem, 60000)) {
                                                Service.gI().sendThongBao(player, "Vui lòng đợi "
                                                        + ((player.lastTimePickItem + 60000 - System.currentTimeMillis()) / 1000)
                                                        + " giây để có thể trả");
                                                return;
                                            }
                                            TranhNgocService.getInstance().dropBallTranhNro(player, (byte) 1);
                                            player.zone.pointCadic++;
                                            if (player.zone.pointCadic > ConstTranhNgocNamek.MAX_POINT) {
                                                player.zone.pointCadic = ConstTranhNgocNamek.MAX_POINT;
                                            }
                                            TranhNgocService.getInstance().sendUpdatePoint(player);
                                        }
                                        break;
                                    case 1:
                                        break;
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.FIDE:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 184) {
                                    if (player.iDMark.getTranhNgoc() == 1) {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đi đi cu!! Chém giờ", "Đóng");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                "Hãy mang ngọc rồng về cho ta", "Đưa ngọc", "Đóng");
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (this.mapId == 184) {
                                switch (select) {
                                    case 0:
                                        if (player.iDMark.getTranhNgoc() == 2 && player.isHoldNamecBallTranhDoat) {
                                            if (!Util.canDoWithTime(player.lastTimePickItem, 60000)) {
                                                Service.gI().sendThongBao(player, "Vui lòng đợi "
                                                        + ((player.lastTimePickItem + 60000 - System.currentTimeMillis()) / 1000)
                                                        + " giây để có thể trả");
                                                return;
                                            }
                                            TranhNgocService.getInstance().dropBallTranhNro(player, (byte) 2);
                                            player.zone.pointFide++;
                                            if (player.zone.pointFide > ConstTranhNgocNamek.MAX_POINT) {
                                                player.zone.pointFide = ConstTranhNgocNamek.MAX_POINT;
                                            }
                                            TranhNgocService.getInstance().sendUpdatePoint(player);
                                        }
                                        break;
                                    case 1:
                                        break;
                                }
                            }
                        }
                    };
                    break;

                case ConstNpc.GHI_DANH:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        String[] menuselect = new String[]{};

                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == ConstMap.DAI_HOI_VO_THUAT) {
                                    int crrHOUR = TimeUtil.getCurrHour();
                                    if (DaiHoiVoThuatManager.gI().openDHVT && (System.currentTimeMillis() <= DaiHoiVoThuatManager.gI().tOpenDHVT)) {
                                        String nameDH = DaiHoiVoThuatManager.gI().nameRoundDHVT();
                                        this.createOtherMenu(player, ConstNpc.DAI_HOI_VO_THUAT, "Chào mừng bạn đến với đại hội võ thuật\n"
                                                + "Giải " + nameDH + " đang có " + DaiHoiVoThuatManager.gI().lstIDPlayers.size() + " người đăng ký thi đấu\n" + DaiHoiVoThuatService.gI().textDaiHoi(player.nPoint.power), "Thông tin\nChi tiết", "Đăng kí", "Giải\nSiêu Hạng", "Đại Hội\nVõ Thuật\nLần thứ\n23");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đã hết hạn đăng ký thi đấu, xin vui lòng chờ đến giải sau" + DaiHoiVoThuatManager.gI().timeDHVTnext(crrHOUR) + "\n" + DaiHoiVoThuatService.gI().textDaiHoi(player.nPoint.power), new String[]{"Thông tin\bChi tiết", "Giải\nSiêu Hạng", "Đại Hội\nVõ Thuật\nLần thứ\n23", "Ðóng"});
                                    }
                                } else if (this.mapId == ConstMap.DAI_HOI_VO_THUAT_129) {
                                    int goldchallenge = player.goldChallenge;
                                    if (player.levelWoodChest == 0) {
                                        menuselect = new String[]{
                                            "Hướng\ndẫn\nthêm",
                                            "Thi đấu\n" + player.gemChallenge + " ngọc",
                                            "Thi đấu\n" + Util.numberToMoney(goldchallenge) + "\nvàng",
                                            "Về\nĐại Hội\nVõ Thuật"};
                                    } else {
                                        menuselect = new String[]{
                                            "Hướng\ndẫn\nthêm",
                                            "Thi đấu\n" + player.gemChallenge + " ngọc",
                                            "Thi đấu\n" + Util.numberToMoney(goldchallenge) + "\nvàng",
                                            "Nhận\nthưởng\n Rương Cấp " + player.levelWoodChest,
                                            "Về\nĐại Hội\nVõ Thuật"};
                                    }
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Đại hội võ thuật lần thứ 23\n"
                                            + "Diễn ra bất kể ngày đêm, ngày nghỉ, ngày lễ\n"
                                            + "Phần thưởng vô cùng quý giá\n"
                                            + "Nhanh chóng tham gia nào",
                                            menuselect, "Từ chối");
                                } else if (this.mapId == 213) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Sự kiện chào mừng khai mở máy chủ Ngọc Rồng\n"
                                            + "Điểm của bạn: " + player.diemmaydam
                                            + "\nTham gia để nhận thưởng!", "Top 100\nMáy Đấm", "Nhận Thưởng", "Đóng");
                                } else {
                                    super.openBaseMenu(player);
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.DAI_HOI_VO_THUAT:
                                        switch (select) {
                                            case 0:
                                                NpcService.gI().createTutorial(player, avartar, "Lịch thi đấu trong ngày\b Giải Nhi đồng: 8,14,18h\b Giải Siêu cấp 1: 9,13,19h\b Giải Siêu cấp 2: 10,15,20h\b Giải Siêu cấp 3: 11,16,21h\b Giải Ngoại hạng: 12,17,22,23h\n"
                                                        + "Giải thưởng khi thắng mỗi vòng\b Giải Nhi đồng: 2 ngọc\b Giải Siêu cấp 1: 4 ngọc\b Giải Siêu cấp 2: 6 ngọc\b Giải Siêu cấp 3: 8 ngọc\b Giải Ngoại hạng: 10.000 vàng\b Vô địch: 5 viên đá nâng cấp\n"
                                                        + "Lệ phí đăng ký các giải đấu\b Giải Nhi đồng: 2 ngọc\b Giải Siêu cấp 1: 4 ngọc\b Giải Siêu cấp 2: 6 ngọc\b Giải Siêu cấp 3: 8 ngọc\b Giải Ngoại hạng: 10.000 vàng\n"
                                                        + "Vui lòng đến đúng giờ để đăng ký thi đấu");
                                                break;
                                            case 1:
                                                this.createOtherMenu(player, ConstNpc.DANG_KY_DAI_HOI_VO_THUAT, "Hiện đang có giải đấu " + DaiHoiVoThuatManager.gI().nameRoundDHVT() + " bạn có muốn đăng ký không?", "Giải\n " + DaiHoiVoThuatManager.gI().nameRoundDHVT() + "\n(" + DaiHoiVoThuatManager.gI().costRoundDHVT() + ")", "Từ chối");
                                                break;
                                            case 2:
//                                                NpcService.gI().createTutorial(player, avartar, "Chức năng đang được phát triển");
                                                ChangeMapService.gI().changeMapNonSpaceship(player, 180, 377, 360);
                                                break;
                                            case 3:
                                                ChangeMapService.gI().changeMapNonSpaceship(player, 181, 382, 395);
                                                break;
                                        }
                                        return;
                                    case ConstNpc.DANG_KY_DAI_HOI_VO_THUAT:
                                        switch (select) {
                                            case 0:
                                                if (DaiHoiVoThuatService.gI().canRegisDHVT(player.nPoint.power)) {
                                                    if (DaiHoiVoThuatManager.gI().lstIDPlayers.size() < 256) {
                                                        if (DaiHoiVoThuatManager.gI().typeDHVT == (byte) 5 && player.inventory.gold >= 10000) {
                                                            if (DaiHoiVoThuatManager.gI().isAssignDHVT(player.id)) {
                                                                Service.getInstance().sendThongBao(player, "Bạn đã đăng ký tham gia đại hội võ thuật rồi");
                                                            } else {
                                                                player.inventory.gold -= 10000;
                                                                Service.getInstance().sendMoney(player);
                                                                Service.getInstance().sendThongBao(player, "Bạn đã đăng ký thành công, nhớ có mặt tại đây trước giờ thi đấu");
                                                                DaiHoiVoThuatManager.gI().lstIDPlayers.add(player.id);
                                                            }
                                                        } else if (DaiHoiVoThuatManager.gI().typeDHVT > (byte) 0 && DaiHoiVoThuatManager.gI().typeDHVT < (byte) 5 && player.inventory.gem >= (int) (2 * DaiHoiVoThuatManager.gI().typeDHVT)) {
                                                            if (DaiHoiVoThuatManager.gI().isAssignDHVT(player.id)) {
                                                                Service.getInstance().sendThongBao(player, "Bạn đã đăng ký tham gia đại hội võ thuật rồi");
                                                            } else {
                                                                player.inventory.gem -= (int) (2 * DaiHoiVoThuatManager.gI().typeDHVT);
                                                                Service.getInstance().sendMoney(player);
                                                                Service.getInstance().sendThongBao(player, "Bạn đã đăng ký thành công, nhớ có mặt tại đây trước giờ thi đấu");
                                                                DaiHoiVoThuatManager.gI().lstIDPlayers.add(player.id);
                                                            }
                                                        } else {
                                                            Service.getInstance().sendThongBao(player, "Không đủ vàng ngọc để đăng ký thi đấu");
                                                        }
                                                    } else {
                                                        Service.getInstance().sendThongBao(player, "Hiện tại đã đạt tới số lượng người đăng ký tối đa, xin hãy chờ đến giải sau");
                                                    }

                                                } else {
                                                    NpcService.gI().createTutorial(player, avartar, DaiHoiVoThuatService.gI().textDaiHoi2(player.nPoint.power));
                                                }
                                        }
                                        return;
                                    case ConstNpc.MENU_NHAN_RUONG:
                                        switch (select) {
                                            case 0:
                                                if (!player.receivedWoodChest) {
                                                    if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                                                        Item it = ItemService.gI()
                                                                .createNewItem((short) ConstItem.RUONG_GO);
                                                        it.itemOptions.add(new ItemOption(72, player.levelWoodChest));
                                                        it.createTime = System.currentTimeMillis();
                                                        InventoryService.gI().addItemBag(player, it, 0);
                                                        InventoryService.gI().sendItemBags(player);
                                                        NpcService.gI().createMenuConMeo(player, 251020003, -1, "Bạn nhận được\n"
                                                                + "|1|Rương gỗ\n"
                                                                + "|2|Giấu bên trong nhiều vật phẩm quý giá", "OK");
                                                        player.receivedWoodChest = true;
                                                        player.levelWoodChest = 0;
                                                        return;
                                                    } else {
                                                        this.npcChat(player, "Hành trang đã đầy");
                                                    }
                                                } else {
                                                    Service.getInstance().sendThongBao(player,
                                                            "Mỗi ngày chỉ có thể nhận rương báu 1 lần");
                                                }
                                                break;
                                        }
                                        break;
                                    case ConstNpc.BASE_MENU:
                                        if (this.mapId == ConstMap.DAI_HOI_VO_THUAT) {
                                            if (player.iDMark.isBaseMenu()) {
                                                switch (select) {
                                                    case 0:
                                                        NpcService.gI().createTutorial(player, avartar, "Lịch thi đấu trong ngày\b Giải Nhi đồng: 8,14,18h\b Giải Siêu cấp 1: 9,13,19h\b Giải Siêu cấp 2: 10,15,20h\b Giải Siêu cấp 3: 11,16,21h\b Giải Ngoại hạng: 12,17,22,23h\n"
                                                                + "Giải thưởng khi thắng mỗi vòng\b Giải Nhi đồng: 2 ngọc\b Giải Siêu cấp 1: 4 ngọc\b Giải Siêu cấp 2: 6 ngọc\b Giải Siêu cấp 3: 8 ngọc\b Giải Ngoại hạng: 10.000 vàng\b Vô địch: 5 viên đá nâng cấp\n"
                                                                + "Lệ phí đăng ký các giải đấu\b Giải Nhi đồng: 2 ngọc\b Giải Siêu cấp 1: 4 ngọc\b Giải Siêu cấp 2: 6 ngọc\b Giải Siêu cấp 3: 8 ngọc\b Giải Ngoại hạng: 10.000 vàng\n"
                                                                + "Vui lòng đến đúng giờ để đăng ký thi đấu");
                                                        break;
                                                    case 1:
//                                                        NpcService.gI().createTutorial(player, avartar, "Chức năng đang được phát triển");
                                                        ChangeMapService.gI().changeMapNonSpaceship(player, 180, 377, 360);
                                                        break;
                                                    case 2:
                                                        ChangeMapService.gI().changeMapNonSpaceship(player, 181, 382, 395);
                                                        break;
                                                }
                                            }
                                        } else if (this.mapId == 213) {
                                            switch (select) {
                                                case 0:
                                                    Service.getInstance().Topmaydam(player);
                                                    break;
                                                case 1:
                                                    ShopService.gI().openBoxItemReward(player);
                                                    break;
                                            }
                                        } else if (this.mapId == ConstMap.DAI_HOI_VO_THUAT_129) {
                                            int goldchallenge = player.goldChallenge;
                                            if (player.levelWoodChest == 0) {
                                                switch (select) {
                                                    case 0:
                                                        NpcService.gI().createTutorial(player, avartar, "Đại hội quy tụ nhiều cao thủ như Jacky Chun, Thiên Xin Hăng, Tàu Bảy Bảy...\bPhần thưởng là 1 rương gỗ chứa nhiều vật phẩm giá trị\bKhi hạ được 1 đối thủ, phần thưởng sẽ nâng lên 1 cấp\bRương càng cao cấp, vật phẩm trong đó càng giá trị hơn\n"
                                                                + "Mỗi ngày bạn chỉ được nhận 1 phần thưởng\bBạn hãy cố gắng hết sức mình để\b nhận phần thưởng xứng đáng nhất nhé");
                                                        break;
                                                    case 1:
                                                        if (!player.receivedWoodChest) {
                                                            if (InventoryService.gI().finditemWoodChest(player)) {
                                                                if (player.inventory.getGem() >= player.gemChallenge) {
                                                                    DHVT23Service.gI().startChallenge(player);
                                                                    player.inventory.subGem(player.gemChallenge);
                                                                    PlayerService.gI().sendInfoHpMpMoney(player);
                                                                    player.goldChallenge += 50000;
                                                                    player.gemChallenge += 1;
                                                                } else {
                                                                    Service.getInstance().sendThongBao(player,
                                                                            "Không đủ ngọc, còn thiếu "
                                                                            + Util.numberToMoney(player.gemChallenge
                                                                                    - player.inventory.gem)
                                                                            + " ngọc nữa");
                                                                }
                                                            } else {
                                                                Service.getInstance().sendThongBao(player,
                                                                        "Hãy mở rương báu vật trước");
                                                            }
                                                        } else {
                                                            Service.getInstance().sendThongBao(player,
                                                                    "Hãy chờ đến ngày mai");
                                                        }
                                                        break;
                                                    case 2:
                                                        if (!player.receivedWoodChest) {
                                                            if (InventoryService.gI().finditemWoodChest(player)) {
                                                                if (player.inventory.getGold() >= goldchallenge) {
                                                                    DHVT23Service.gI().startChallenge(player);
                                                                    player.inventory.subGold(goldchallenge);
                                                                    PlayerService.gI().sendInfoHpMpMoney(player);
                                                                    player.goldChallenge += 50000;
                                                                    player.gemChallenge += 1;
                                                                } else {
                                                                    Service.getInstance().sendThongBao(player,
                                                                            "Không đủ vàng, còn thiếu "
                                                                            + Util.numberToMoney(goldchallenge
                                                                                    - player.inventory.gold)
                                                                            + " vàng nữa");
                                                                }
                                                            } else {
                                                                Service.getInstance().sendThongBao(player,
                                                                        "Hãy mở rương báu vật trước");
                                                            }
                                                            break;
                                                        } else {
                                                            Service.getInstance().sendThongBao(player,
                                                                    "Hãy chờ đến ngày mai");
                                                        }
                                                        break;
                                                    case 3:
                                                        ChangeMapService.gI().changeMapNonSpaceship(player, ConstMap.DAI_HOI_VO_THUAT, 382, 336);
                                                        break;
                                                }
                                            } else {
                                                switch (select) {
                                                    case 0:
                                                        NpcService.gI().createTutorial(player, avartar, "Đại hội quy tụ nhiều cao thủ như Jacky Chun, Thiên Xin Hăng, Tàu Bảy Bảy...\bPhần thưởng là 1 rương gỗ chứa nhiều vật phẩm giá trị\bKhi hạ được 1 đối thủ, phần thưởng sẽ nâng lên 1 cấp\bRương càng cao cấp, vật phẩm trong đó càng giá trị hơn\n"
                                                                + "Mỗi ngày bạn chỉ được nhận 1 phần thưởng\bBạn hãy cố gắng hết sức mình để\b nhận phần thưởng xứng đáng nhất nhé");
                                                        break;
                                                    case 1:
                                                        if (!player.receivedWoodChest) {
                                                            if (InventoryService.gI().finditemWoodChest(player)) {
                                                                if (player.inventory.getGem() >= player.gemChallenge) {
                                                                    DHVT23Service.gI().startChallenge(player);
                                                                    player.inventory.subGem(player.gemChallenge);
                                                                    PlayerService.gI().sendInfoHpMpMoney(player);
                                                                    player.goldChallenge += 50000;
                                                                    player.gemChallenge += 1;
                                                                } else {
                                                                    Service.getInstance().sendThongBao(player,
                                                                            "Không đủ ngọc, còn thiếu "
                                                                            + Util.numberToMoney(player.gemChallenge
                                                                                    - player.inventory.gem)
                                                                            + " ngọc nữa");
                                                                }
                                                            } else {
                                                                Service.getInstance().sendThongBao(player,
                                                                        "Hãy mở rương báu vật trước");
                                                            }
                                                        } else {
                                                            Service.getInstance().sendThongBao(player,
                                                                    "Hãy chờ đến ngày mai");
                                                        }

                                                        break;
                                                    case 2:
                                                        if (!player.receivedWoodChest) {
                                                            if (InventoryService.gI().finditemWoodChest(player)) {
                                                                if (player.inventory.getGold() >= goldchallenge) {
                                                                    DHVT23Service.gI().startChallenge(player);
                                                                    player.inventory.subGold(goldchallenge);
                                                                    PlayerService.gI().sendInfoHpMpMoney(player);
                                                                    player.goldChallenge += 2000000;
                                                                } else {
                                                                    Service.getInstance().sendThongBao(player,
                                                                            "Không đủ vàng, còn thiếu "
                                                                            + Util.numberToMoney(goldchallenge
                                                                                    - player.inventory.gold)
                                                                            + " vàng");
                                                                }
                                                            } else {
                                                                Service.getInstance().sendThongBao(player,
                                                                        "Hãy mở rương báu vật trước");
                                                            }
                                                        } else {
                                                            Service.getInstance().sendThongBao(player,
                                                                    "Hãy chờ đến ngày mai");
                                                        }
                                                        break;
                                                    case 3:
                                                        createOtherMenu(player, ConstNpc.MENU_NHAN_RUONG, "Phần thưởng của bạn đang ở cấp " + player.levelWoodChest + " / 12\n"
                                                                + "Mỗi ngày chỉ được nhận phần thưởng 1 lần\n"
                                                                + "bạn có chắc sẽ nhận phần thưởng ngay bây giờ?",
                                                                "OK", "Từ chối");
                                                        break;
                                                    case 4:
                                                        ChangeMapService.gI().changeMapNonSpaceship(player, ConstMap.DAI_HOI_VO_THUAT, 576, 307);
                                                        break;
                                                }
                                            }
                                        }
                                        break;
                                }
                            }
                        }
                    };
                    break;
//                case ConstNpc.SU_KIEN:
//                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
//                        @Override
//                        public void openBaseMenu(Player player) {
//                            if (canOpenNpc(player)) {
//                                if (this.mapId == 5) {
//                                    switch (ConstNpc.SU_KIEN_ALL) {
//                                        case 1:
//                                            this.createOtherMenu(player, SU_KIEN.MUAHE, "|7|-Sự Kiện Mùa Hè-\n"
//                                                    + "Chỉ Có Thể Up Vật Phẩm Tại Các Map Sau : Nam Kame\n"
//                                                    + "Yêu Cầu : Mang trên người 'Quần Đi Biển'\n\n"
//                                                    + "|1| x999 Sao Biển: Danh Hiệu 'Tuổi Thơ', Tỉ Lệ Vĩnh Viễn (10% SĐ HP Ki)\n"
//                                                    + " x99 Con Cua: Đột Phá Sức Mạnh Trái Đất (x1)\n"
//                                                    + " x99 Vỏ Sò: Đột Phá Sức Mạnh Xayda (x1)\n"
//                                                    + " x99 Vỏ Ốc: Đột Phá Sức Mạnh Namec (x1)\n",
//                                                    "Đổi\nSao Biển", "Đổi\nCon Cua", "Đổi\nVỏ Sò", "Đổi\nVỏ Ốc", "Đóng");
//                                            break;
//                                        case 2:
//                                            this.createOtherMenu(player, SU_KIEN.HUNGVUONG, "|7|-Sự Kiện Hùng Vương-\n"
//                                                    + "Có Thể Up Vật Phẩm Tại Tất Cả Các Map\n"
//                                                    + "Yêu Cầu : Mang trên người (Cải Trang Mị Nương)\n"
//                                                    + "\n|2|Dâng Dưa Hấu Để Đổi Lấy Điểm Sự Kiện"
//                                                    + "\n|2|Sử Dụng Điểm Để Đổi Công Thức Chế Tạo Đồ Thiên Sứ"
//                                                    + "\n|2|Sử Dụng Điểm Để Để Thử Vận May Nhận Item SC"
//                                                    + "\n|2|Sử Dụng Điểm Để Đổi Cải Trang Vip (Có Khả Năng Vĩnh Viễn)"
//                                                    + "\n|2|Sử Dụng Điểm Để Thử Vận May Nhận Ngọc Rồng",
//                                                    "Dâng\nDưa Hấu", "Xem Điểm\nSự Kiện", "Đổi\nCông Thức", "Đổi\nItem SC", "Đổi\nCải Trang", "Thử\nVận May", "Từ chối");
//                                            break;
//                                        case 3:
////                                this.createOtherMenu(player, SU_KIEN.TRUNGTHU, "|7| SỰ KIỆN TRUNG THU"
////                                        + "\n\n|2|Nguyên liệu cần nấu bánh Trung thu"
////                                        + "\n|-1|- Bánh Hạt sen : 99 Hạt sen + 50 Bột nếp + 2 Mồi lửa"
////                                        + "\n|-1|- Bánh Đậu xanh : 99 Đậu xanh + 50 Bột nếp + 2 Mồi lửa"
////                                        + "\n|-1|- Bánh Thập cẩm : 99 Hạt sen + 99 Đậu xanh + 99 Bột nếp + 5 Mồi lửa"
////                                        + "\n|7|Làm bánh sẽ tốn phí 2Ty Vàng/lần"
////                                        + "\n\n|1|Điểm sự kiện : " + Util.format(player.NguHanhSonPoint) + " Điểm",
////                                        "Thể lệ", "Làm bánh", "Đổi điểm\nTrung thu");
//                                            break;
//                                        case 4:
//                                            this.createOtherMenu(player, SU_KIEN.HLWEEN,
//                                                    "Sự kiện Halloween chính thức tại VŨ TRỤ NGỌC RỒNG\n"
//                                                    + "Chuẩn bị x10 nguyên liệu Kẹo, Bánh Quy, Bí ngô để đổi Giỏ Kẹo cho ta nhé\n"
//                                                    + "Nguyên Liệu thu thập bằng cách đánh quái tại các hành tinh được chỉ định\n"
//                                                    + "Tích lũy 3 Giỏ Kẹo +  3 Vé mang qua đây ta sẽ cho con 1 Hộp Ma Quỷ\n"
//                                                    + "Tích lũy 3 Giỏ Kẹo, 3 Hộp Ma Quỷ + 3 Vé \nmang qua đây ta sẽ cho con 1 hộp quà thú vị.",
//                                                    "Đổi\nGiỏ Kẹo", "Đổi Hộp\nMa Quỷ", "Đổi Hộp\nQuà Halloween",
//                                                    "Từ chối");
//                                            break;
//                                        case 5:
////                                this.createOtherMenu(player, SU_KIEN.NHAGIAO,
////                                        "Sự kiện 20/11 chính thức tại VŨ TRỤ NGỌC RỒNG\n"
////                                        + "Số điểm hiện tại của bạn là : " + player.inventory.event
////                                        + "\nTổng số hoa đã tặng " + player.inventory.event + "/999"
////                                        + "\nToàn bộ máy chủ được nhân đôi số vàng rơi ra từ quái,thời gian còn lại "
////                                        + "5" + " phút."
////                                        + "\nKhi tặng đủ 999 bông hoa toàn bộ máy chủ được nhân đôi số vàng rơi ra từ quái trong 60 phút",
////                                        "Tặng 1\n Bông hoa", "Tặng\n10 Bông", "Tặng\n99 Bông",
////                                        "Đổi\nHộp quà");
//                                            break;
//                                        case 6:
//                                            this.createOtherMenu(player, SU_KIEN.NOEL,
//                                                    "Sự kiên giáng sinh VŨ TRỤ NGỌC RỒNG"
//                                                    + "\nKhi đội mũ len bất kì đánh quái sẽ có cơ hội nhận được kẹo giáng sinh"
//                                                    + "\nĐem 99 kẹo giáng sinh tới đây để đổi 1 Vớ,tất giáng sinh"
//                                                    + "\nChúc bạn một mùa giáng sinh vui vẻ",
//                                                    "Đổi\nTất giáng sinh");
//                                            break;
//                                        case 7:
////                                this.createOtherMenu(player, SU_KIEN.TET,
////                                        "Mừng Ngày Tết Nguyên Đán VŨ TRỤ NGỌC RỒNG"
////                                        + "\nBạn đang có: " + player.inventory.event + " điểm sự kiện"
////                                        + "\n" + (ConstEvent.TONGSOBANH >= 500 ? "|7|HIỆN TẠI ĐANG X2 EXP TRÊN TOÀN MÁY CHỦ\nTHỜI GIAN CÒN: " + Util.tinhgio(ConstEvent.X2) : "|7|Tổng số bánh server: " + ConstEvent.TONGSOBANH
////                                                + "\nKhi số bánh nấu đạt đủ 500 bánh toàn máy chủ sẽ được X2 EXP")
////                                        + "\n|-1|Chúc bạn năm mới dui dẻ",
////                                        "Nhận Lìxì", "Đổi Điểm\nSự Kiện", "Cửa Hàng\nSự Kiện", "Nhận Quần\nHoa Văn");
//                                            break;
//                                        case 8:
////                                this.createOtherMenu(player, SU_KIEN.PHUNU,
////                                        "Sự kiện 8/3 chính thức tại VŨ TRỤ NGỌC RỒNG"
////                                        + "\nBạn đang có: " + player.inventory.event
////                                        + " điểm sự kiện\nChúc bạn chơi game dui dẻ",
////                                        "Tặng 1\n Bông hoa", "Tặng\n10 Bông", "Tặng\n99 Bông", "Đổi Capsule");
//                                            break;
//                                        default:
//                                            Service.gI().sendThongBaoFromAdmin(player, "|7|Hiện chưa mở sự kiện nào!");
//                                            break;
//                                    }
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void confirmMenu(Player player, int select) {
//                            if (canOpenNpc(player)) {
//                                if (this.mapId == 5) {
//                                    switch (player.iDMark.getIndexMenu()) {
//                                        case SU_KIEN.HLWEEN:
//                                            switch (select) {
//                                                case 0:
//                                                    if (InventoryService.gI().getCountEmptyBag(player) > 0) {
//                                                        Item keo = InventoryService.gI().finditemnguyenlieuKeo(player);
//                                                        Item banh = InventoryService.gI().finditemnguyenlieuBanh(player);
//                                                        Item bingo = InventoryService.gI().finditemnguyenlieuBingo(player);
//
//                                                        if (keo != null && banh != null && bingo != null) {
//                                                            Item GioBingo = ItemService.gI().createNewItem((short) 2016, 1);
//
//                                                            // - Số item sự kiện có trong rương
//                                                            InventoryService.gI().subQuantityItemsBag(player, keo, 10);
//                                                            InventoryService.gI().subQuantityItemsBag(player, banh, 10);
//                                                            InventoryService.gI().subQuantityItemsBag(player, bingo, 10);
//
//                                                            GioBingo.itemOptions.add(new ItemOption(74, 0));
//                                                            InventoryService.gI().addItemBag(player, GioBingo, 0);
//                                                            InventoryService.gI().sendItemBags(player);
//                                                            Service.getInstance().sendThongBao(player, "Đổi quà sự kiện thành công");
//                                                        } else {
//                                                            Service.getInstance().sendThongBao(player,
//                                                                    "Vui lòng chuẩn bị x10 Nguyên Liệu Kẹo, Bánh Quy, Bí Ngô để đổi vật phẩm sự kiện");
//                                                        }
//                                                    } else {
//                                                        Service.getInstance().sendThongBao(player, "Hành trang đầy.");
//                                                    }
//                                                    break;
//                                                case 1:
//                                                    if (InventoryService.gI().getCountEmptyBag(player) > 0) {
//                                                        Item ve = InventoryService.gI().finditemnguyenlieuVe(player);
//                                                        Item giokeo = InventoryService.gI().finditemnguyenlieuGiokeo(player);
//
//                                                        if (ve != null && giokeo != null) {
//                                                            Item Hopmaquy = ItemService.gI().createNewItem((short) 2017, 1);
//                                                            // - Số item sự kiện có trong rương
//                                                            InventoryService.gI().subQuantityItemsBag(player, ve, 3);
//                                                            InventoryService.gI().subQuantityItemsBag(player, giokeo, 3);
//
//                                                            Hopmaquy.itemOptions.add(new ItemOption(74, 0));
//                                                            InventoryService.gI().addItemBag(player, Hopmaquy, 0);
//                                                            InventoryService.gI().sendItemBags(player);
//                                                            Service.getInstance().sendThongBao(player, "Đổi quà sự kiện thành công");
//                                                        } else {
//                                                            Service.getInstance().sendThongBao(player,
//                                                                    "Vui lòng chuẩn bị x3 Vé đổi Kẹo và x3 Giỏ kẹo để đổi vật phẩm sự kiện");
//                                                        }
//                                                    } else {
//                                                        Service.getInstance().sendThongBao(player, "Hành trang đầy.");
//                                                    }
//                                                    break;
//                                                case 2:
//                                                    if (InventoryService.gI().getCountEmptyBag(player) > 0) {
//                                                        Item ve = InventoryService.gI().finditemnguyenlieuVe(player);
//                                                        Item giokeo = InventoryService.gI().finditemnguyenlieuGiokeo(player);
//                                                        Item hopmaquy = InventoryService.gI().finditemnguyenlieuHopmaquy(player);
//
//                                                        if (ve != null && giokeo != null && hopmaquy != null) {
//                                                            Item HopQuaHLW = ItemService.gI().createNewItem((short) 2012, 1);
//                                                            // - Số item sự kiện có trong rương
//                                                            InventoryService.gI().subQuantityItemsBag(player, ve, 3);
//                                                            InventoryService.gI().subQuantityItemsBag(player, giokeo, 3);
//                                                            InventoryService.gI().subQuantityItemsBag(player, hopmaquy, 3);
//
//                                                            HopQuaHLW.itemOptions.add(new ItemOption(74, 0));
//                                                            HopQuaHLW.itemOptions.add(new ItemOption(30, 0));
//                                                            InventoryService.gI().addItemBag(player, HopQuaHLW, 0);
//                                                            InventoryService.gI().sendItemBags(player);
//                                                            Service.getInstance().sendThongBao(player,
//                                                                    "Đổi quà hộp quà sự kiện Halloween thành công");
//                                                        } else {
//                                                            Service.getInstance().sendThongBao(player,
//                                                                    "Vui lòng chuẩn bị x3 Hộp Ma Quỷ, x3 Vé đổi Kẹo và x3 Giỏ kẹo để đổi vật phẩm sự kiện");
//                                                        }
//                                                    } else {
//                                                        Service.getInstance().sendThongBao(player, "Hành trang đầy.");
//                                                    }
//                                                    break;
//                                            }
//                                            break;
//                                    }
//                                }
//                            }
//                        }
//                    };
//                    break;
                case ConstNpc.NOI_BANH:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "Xin chào " + player.name + "\nTôi là nồi nấu bánh\nTôi có thể giúp gì cho bạn",
                                        "Làm\nBánh Tét", "Làm\nBánh Chưng", getMenuLamBanh(player, 0),
                                        getMenuLamBanh(player, 1), "Đổi Hộp\nQuà Tết");
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.BASE_MENU:
                                        switch (select) {
                                            case 0:
                                                Item thitBaChi = InventoryService.gI().findItem(player,
                                                        ConstItem.THIT_BA_CHI, 99);
                                                Item gaoNep = InventoryService.gI().findItem(player, ConstItem.GAO_NEP,
                                                        99);
                                                Item doXanh = InventoryService.gI().findItem(player, ConstItem.DO_XANH,
                                                        99);
                                                Item laChuoi = InventoryService.gI().findItem(player,
                                                        ConstItem.LA_CHUOI, 99);
                                                if (thitBaChi != null && gaoNep != null && doXanh != null
                                                        && laChuoi != null) {
                                                    InventoryService.gI().subQuantityItemsBag(player, thitBaChi, 99);
                                                    InventoryService.gI().subQuantityItemsBag(player, gaoNep, 99);
                                                    InventoryService.gI().subQuantityItemsBag(player, doXanh, 99);
                                                    InventoryService.gI().subQuantityItemsBag(player, laChuoi, 99);
                                                    Item banhtet = ItemService.gI()
                                                            .createNewItem((short) ConstItem.BANH_TET_2023);
                                                    banhtet.itemOptions.add(new ItemOption(74, 0));
                                                    InventoryService.gI().addItemBag(player, banhtet, 0);
                                                    InventoryService.gI().sendItemBags(player);
                                                    Service.getInstance().sendThongBao(player,
                                                            "Bạn nhận được Bánh Tét");
                                                } else {
                                                    Service.getInstance().sendThongBao(player, "Không đủ nguyên liệu");
                                                }
                                                break;
                                            case 1:
                                                Item thitHeo1 = InventoryService.gI().findItem(player,
                                                        ConstItem.THIT_HEO_2023, 99);
                                                Item gaoNep1 = InventoryService.gI().findItem(player, ConstItem.GAO_NEP,
                                                        99);
                                                Item doXanh1 = InventoryService.gI().findItem(player, ConstItem.DO_XANH,
                                                        99);
                                                Item laDong1 = InventoryService.gI().findItem(player,
                                                        ConstItem.LA_DONG_2023, 99);
                                                if (thitHeo1 != null && gaoNep1 != null && doXanh1 != null
                                                        && laDong1 != null) {
                                                    InventoryService.gI().subQuantityItemsBag(player, thitHeo1, 99);
                                                    InventoryService.gI().subQuantityItemsBag(player, gaoNep1, 99);
                                                    InventoryService.gI().subQuantityItemsBag(player, doXanh1, 99);
                                                    InventoryService.gI().subQuantityItemsBag(player, laDong1, 99);
                                                    Item banhChung = ItemService.gI()
                                                            .createNewItem((short) ConstItem.BANH_CHUNG_2023);
                                                    banhChung.itemOptions.add(new ItemOption(74, 0));
                                                    InventoryService.gI().addItemBag(player, banhChung, 0);
                                                    InventoryService.gI().sendItemBags(player);
                                                    Service.getInstance().sendThongBao(player,
                                                            "Bạn nhận được Bánh Chưng");
                                                } else {
                                                    Service.getInstance().sendThongBao(player, "Không đủ nguyên liệu");
                                                }
                                                break;
                                            case 2:
                                                if (!player.event.isCookingTetCake()) {
                                                    Item banhTet2 = InventoryService.gI().findItem(player,
                                                            ConstItem.BANH_TET_2023, 1);
                                                    Item phuGiaTaoMau2 = InventoryService.gI().findItem(player,
                                                            ConstItem.PHU_GIA_TAO_MAU, 1);
                                                    Item giaVi2 = InventoryService.gI().findItem(player,
                                                            ConstItem.GIA_VI_TONG_HOP, 1);

                                                    if (banhTet2 != null && phuGiaTaoMau2 != null && giaVi2 != null) {
                                                        InventoryService.gI().subQuantityItemsBag(player, banhTet2, 1);
                                                        InventoryService.gI().subQuantityItemsBag(player, phuGiaTaoMau2,
                                                                1);
                                                        InventoryService.gI().subQuantityItemsBag(player, giaVi2, 1);
                                                        InventoryService.gI().sendItemBags(player);
                                                        player.event.setTimeCookTetCake(300);
                                                        player.event.setCookingTetCake(true);
                                                        Service.getInstance().sendThongBao(player,
                                                                "Bắt đầu nấu bánh,thời gian nấu bánh là 5 phút");
                                                    } else {
                                                        Service.getInstance().sendThongBao(player,
                                                                "Không đủ nguyên liệu");
                                                    }
                                                } else if (player.event.isCookingTetCake()
                                                        && player.event.getTimeCookTetCake() == 0) {
                                                    Item cake = ItemService.gI()
                                                            .createNewItem((short) ConstItem.BANH_TET_CHIN, 1);
                                                    cake.itemOptions.add(new ItemOption(77, 20));
                                                    cake.itemOptions.add(new ItemOption(103, 20));
                                                    cake.itemOptions.add(new ItemOption(74, 0));
                                                    InventoryService.gI().addItemBag(player, cake, 0);
                                                    InventoryService.gI().sendItemBags(player);
                                                    player.event.setCookingTetCake(false);
                                                    player.event.addEventPoint(1);
                                                    Service.getInstance().sendThongBao(player,
                                                            "Bạn nhận được Bánh Tét (đã chính) và 1 điểm sự kiện");
                                                }
                                                break;
                                            case 3:
                                                if (!player.event.isCookingChungCake()) {
                                                    Item banhChung3 = InventoryService.gI().findItem(player,
                                                            ConstItem.BANH_CHUNG_2023, 1);
                                                    Item phuGiaTaoMau3 = InventoryService.gI().findItem(player,
                                                            ConstItem.PHU_GIA_TAO_MAU, 1);
                                                    Item giaVi3 = InventoryService.gI().findItem(player,
                                                            ConstItem.GIA_VI_TONG_HOP, 1);

                                                    if (banhChung3 != null && phuGiaTaoMau3 != null && giaVi3 != null) {
                                                        InventoryService.gI().subQuantityItemsBag(player, banhChung3,
                                                                1);
                                                        InventoryService.gI().subQuantityItemsBag(player, phuGiaTaoMau3,
                                                                1);
                                                        InventoryService.gI().subQuantityItemsBag(player, giaVi3, 1);
                                                        InventoryService.gI().sendItemBags(player);
                                                        player.event.setTimeCookChungCake(300);
                                                        player.event.setCookingChungCake(true);
                                                        Service.getInstance().sendThongBao(player,
                                                                "Bắt đầu nấu bánh,thời gian nấu bánh là 5 phút");
                                                    } else {
                                                        Service.getInstance().sendThongBao(player,
                                                                "Không đủ nguyên liệu");
                                                    }
                                                } else if (player.event.isCookingChungCake()
                                                        && player.event.getTimeCookChungCake() == 0) {
                                                    Item cake = ItemService.gI()
                                                            .createNewItem((short) ConstItem.BANH_CHUNG_CHIN, 1);
                                                    cake.itemOptions.add(new ItemOption(50, 20));
                                                    cake.itemOptions.add(new ItemOption(5, 15));
                                                    cake.itemOptions.add(new ItemOption(74, 0));
                                                    InventoryService.gI().addItemBag(player, cake, 0);
                                                    InventoryService.gI().sendItemBags(player);
                                                    player.event.setCookingChungCake(false);
                                                    player.event.addEventPoint(1);
                                                    Service.getInstance().sendThongBao(player,
                                                            "Bạn nhận được Bánh Chưng (đã chín) và 1 điểm sự kiện");
                                                }
                                                break;
                                            case 4:
                                                Item tetCake = InventoryService.gI().findItem(player,
                                                        ConstItem.BANH_TET_CHIN, 5);
                                                Item chungCake = InventoryService.gI().findItem(player,
                                                        ConstItem.BANH_CHUNG_CHIN, 5);
                                                if (chungCake != null && tetCake != null) {
                                                    Item hopQua = ItemService.gI()
                                                            .createNewItem((short) ConstItem.HOP_QUA_TET_2023, 1);
                                                    hopQua.itemOptions.add(new ItemOption(30, 0));
                                                    hopQua.itemOptions.add(new ItemOption(74, 0));

                                                    InventoryService.gI().subQuantityItemsBag(player, tetCake, 5);
                                                    InventoryService.gI().subQuantityItemsBag(player, chungCake, 5);
                                                    InventoryService.gI().addItemBag(player, hopQua, 0);
                                                    InventoryService.gI().sendItemBags(player);
                                                    Service.getInstance().sendThongBao(player,
                                                            "Bạn nhận được Hộp quà tết");
                                                } else {
                                                    Service.getInstance().sendThongBao(player,
                                                            "Không đủ nguyên liệu để đổi");
                                                }
                                                break;
                                        }
                                        break;
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.KING_FURY:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "Cửa hàng của chúng tôi chuyên mua bán hàng hiệu, hàng độc\n"
                                        + "Cám ơn bạn đã ghé thăm.", "Hướng\ndẫn\nthêm", "Mua bán\nKý gửi\nSự kiện", "Từ chối");
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.BASE_MENU:
                                        switch (select) {
                                            case 0:
                                                NpcService.gI().createTutorial(player, avartar, "Cửa hàng chuyên nhận ký gửi mua bán vật phẩm\b"
                                                        + "Chỉ với 1 ngọc và 5% phí ký gửi\b"
                                                        + "Giá trị ký gửi 100k-1 Tỉ vàng hoặc 2-2k ngọc\b"
                                                        + "Một người bán, vạn người mua, mại dô, mại dô");
                                                break;
                                            case 1:
                                                Service.getInstance().sendThongBao(player, "Đang trong quá trình update");
//                                                player.isShopKiGuiSuKien = true;
//                                                KiGuiShop.getInstance().show(player);
                                                return;
                                        }
                                        break;
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.CUA_HANG_KY_GUI:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "Cửa hàng chúng tôi chuyên mua bán hàng hiệu, hàng độc, cảm ơn bạn đã ghé thăm.",
                                        "Hướng\ndẫn\nthêm", "Mua bán", "Danh sách\nHết Hạn", "Hủy");
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.BASE_MENU:
                                        switch (select) {
                                            case 0:
                                                Service.getInstance().sendPopUpMultiLine(player, tempId, avartar, "Cửa hàng chuyên nhận ký gửi mua bán vật phẩm\bGiá trị ký gửi 10k-200Tr vàng hoặc 2-2k ngọc\bMột người bán, vạn người mua, mại dô, mại dô");
                                                break;
                                            case 1:
                                                ConsignmentShop.getInstance().show(player);
                                                break;
                                            case 2:
                                                ConsignmentShop.getInstance().showExpiringItems(player);
                                                break;
                                        }
                                        break;
                                }
                            }
                        }
                    };
                    break;
                default:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                super.openBaseMenu(player);
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                // ShopService.gI().openShopNormal(player, this, ConstNpc.SHOP_BUNMA_TL_0, 0,
                                // player.gender);
                            }
                        }
                    };
            }
        } catch (Exception e) {
            Log.error(NpcFactory.class,
                    e, "Lỗi load npc");
        }
        return npc;
    }
// girlkun75-mark

    public static void createNpcRongThieng() {
        Npc npc = new Npc(-1, -1, -1, -1, ConstNpc.RONG_THIENG, -1) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.IGNORE_MENU:

                        break;
                    case ConstNpc.SHENRON_CONFIRM:
                        if (select == 0) {
                            SummonDragon.gI().confirmWish();
                        } else if (select == 1) {
                            SummonDragon.gI().reOpenShenronWishes(player);
                        }
                        break;
                    case ConstNpc.SHENRON_1_1:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_1
                                && select == SHENRON_1_STAR_WISHES_1.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_2, SHENRON_SAY,
                                    SHENRON_1_STAR_WISHES_2);
                            break;
                        }
                    case ConstNpc.SHENRON_1_2:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_2
                                && select == SHENRON_1_STAR_WISHES_2.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_1, SHENRON_SAY,
                                    SHENRON_1_STAR_WISHES_1);
                            break;
                        }
                    case ConstNpc.BLACK_SHENRON:
                        if (player.iDMark.getIndexMenu() == ConstNpc.BLACK_SHENRON
                                && select == BLACK_SHENRON_WISHES.length) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.BLACK_SHENRON, BLACK_SHENRON_SAY,
                                    BLACK_SHENRON_WISHES);
                            break;
                        }
                    case ConstNpc.ICE_SHENRON:
                        if (player.iDMark.getIndexMenu() == ConstNpc.ICE_SHENRON
                                && select == ICE_SHENRON_WISHES.length) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.ICE_SHENRON, ICE_SHENRON_SAY,
                                    ICE_SHENRON_WISHES);
                            break;
                        }
                    default:
                        SummonDragon.gI().showConfirmShenron(player, player.iDMark.getIndexMenu(), (byte) select);
                        break;
                }
            }
        };
    }

    public static void createNpcConMeo() {
        Npc npc = new Npc(-1, -1, -1, -1, ConstNpc.CON_MEO, 351) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case 1002:
                        switch (select) {
                            case 0:
                                Item itemd1 = InventoryService.gI().findItemBagByTemp(player, 1328);
                                int[] hdTraiDat = UseItem.itemHD[0];
                                for (int i = 0; i < 5; i++) {
                                    int id = hdTraiDat[i];
                                    Item dhd = UseItem.itemdoHD(id);
                                    InventoryService.gI().addItemBag(player, dhd, 0);

                                }
                                InventoryService.gI().subQuantityItemsBag(player, itemd1, 1);
                                InventoryService.gI().sendItemBags(player);
                                break;
                            case 1:
                                Item itemd2 = InventoryService.gI().findItemBagByTemp(player, 1328);
                                int[] hdTNM = UseItem.itemHD[1];
                                for (int i = 0; i < 5; i++) {
                                    int id = hdTNM[i];
                                    Item dhd = UseItem.itemdoHD(id);
                                    InventoryService.gI().addItemBag(player, dhd, 0);
                                }
                                InventoryService.gI().subQuantityItemsBag(player, itemd2, 1);
                                InventoryService.gI().sendItemBags(player);
                                break;
                            case 2:
                                Item itemd3 = InventoryService.gI().findItemBagByTemp(player, 1328);
                                int[] hdxd = UseItem.itemHD[2];
                                for (int i = 0; i < 5; i++) {
                                    int id = hdxd[i];
                                    Item dhd = UseItem.itemdoHD(id);
                                    InventoryService.gI().addItemBag(player, dhd, 0);

                                }
                                InventoryService.gI().subQuantityItemsBag(player, itemd3, 1);
                                InventoryService.gI().sendItemBags(player);
                                break;
                        }
                        return;
                    case ConstNpc.MENU_OPTION_USE_ITEM1105:
                        switch (select) {
                            case 0:
                                IntrinsicService.gI().sattd(player);
                                break;
                            case 1:
                                IntrinsicService.gI().satnm(player);
                                break;
                            case 2:
                                IntrinsicService.gI().setxd(player);
                                break;
                            default:
                                break;
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM1281:
                        switch (select) {
                            case 0:
                                IntrinsicService.gI().sattdkh(player);
                                break;
                            case 1:
                                IntrinsicService.gI().satnmkh(player);
                                break;
                            case 2:
                                IntrinsicService.gI().setxdkh(player);
                                break;
                            default:
                                break;
                        }
                        break;
                    case ConstNpc.menutdkh:
                        switch (select) {
                            case 0: {// set songoku
                                try {
                                    ItemService.gI().setSongokutl(player);

                                } catch (Exception ex) {
                                    Logger.getLogger(NpcFactory.class
                                            .getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            break;
                            case 1:// set kaioken
                                try {
                                ItemService.gI().setKaioKentl(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 2:// set thenxin hang
                                   try {
                                ItemService.gI().setThenXinHangtl(player);
                            } catch (Exception e) {
                            }
                            break;
                        }
                        break;
                    case ConstNpc.menunmkh:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().setLienHoantl(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setPicolotl(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setPikkoroDaimaotl(player);
                            } catch (Exception e) {
                            }
                            break;
                        }
                        break;

                    case ConstNpc.menuxdkh:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().setKakarottl(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setCadictl(player);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setNappatl(player);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM568:
                        switch (select) {
                            case 0: {// Hành tinh Trái đất
                                try {
                                    UseItem.gI().openMabu(player, 0);

                                } catch (Exception e) {
                                    Logger.getLogger(NpcFactory.class
                                            .getName()).log(Level.SEVERE, null, e);
                                }
                            }
                            break;
                            case 1:// Hành tinh Namec
                                try {
                                UseItem.gI().openMabu(player, 1);

                            } catch (Exception e) {
                                Logger.getLogger(NpcFactory.class
                                        .getName()).log(Level.SEVERE, null, e);
                            }
                            break;
                            case 2:// Hành tinh Xayda
                                try {
                                UseItem.gI().openMabu(player, 2);

                            } catch (Exception e) {
                                Logger.getLogger(NpcFactory.class
                                        .getName()).log(Level.SEVERE, null, e);
                            }
                            break;
                            default:
                                break;
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM1458:
                        switch (select) {
                            case 0: {// Hành tinh Trái đất
                                try {
                                    UseItem.gI().openbillcon(player, 0);

                                } catch (Exception e) {
                                    Logger.getLogger(NpcFactory.class
                                            .getName()).log(Level.SEVERE, null, e);
                                }
                            }
                            break;
                            case 1:// Hành tinh Namec
                                try {
                                UseItem.gI().openbillcon(player, 1);

                            } catch (Exception e) {
                                Logger.getLogger(NpcFactory.class
                                        .getName()).log(Level.SEVERE, null, e);
                            }
                            break;
                            case 2:// Hành tinh Xayda
                                try {
                                UseItem.gI().openbillcon(player, 2);

                            } catch (Exception e) {
                                Logger.getLogger(NpcFactory.class
                                        .getName()).log(Level.SEVERE, null, e);
                            }
                            break;
                            default:
                                break;
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM1280:
                        switch (select) {
                            case 0: {// Trái đất
                                try {
                                    ItemService.gI().settraidat(player);

                                } catch (Exception e) {
                                    Logger.getLogger(NpcFactory.class
                                            .getName()).log(Level.SEVERE, null, e);
                                }
                            }
                            break;
                            case 1:// Namec
                                try {
                                ItemService.gI().setnamec(player);

                            } catch (Exception e) {
                                Logger.getLogger(NpcFactory.class
                                        .getName()).log(Level.SEVERE, null, e);
                            }
                            break;
                            case 2:// Xayda
                                   try {
                                ItemService.gI().setxayda(player);

                            } catch (Exception e) {
                                Logger.getLogger(NpcFactory.class
                                        .getName()).log(Level.SEVERE, null, e);
                            }
                            break;
                            default:
                                break;
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM1282:
                        switch (select) {
                            case 0: {// set songoku
                                try {
                                    ItemService.gI().sethuydiet(player);

                                } catch (Exception e) {
                                    Logger.getLogger(NpcFactory.class
                                            .getName()).log(Level.SEVERE, null, e);
                                }
                            }
                            break;
                            case 1:// set kaioken
                                try {
                                ItemService.gI().sethuydiet1(player);

                            } catch (Exception e) {
                                Logger.getLogger(NpcFactory.class
                                        .getName()).log(Level.SEVERE, null, e);
                            }
                            break;
                            case 2:// set thenxin hang
                                   try {
                                ItemService.gI().sethuydiet2(player);

                            } catch (Exception e) {
                                Logger.getLogger(NpcFactory.class
                                        .getName()).log(Level.SEVERE, null, e);
                            }
                            break;
                            default:
                                break;
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM1283:
                        switch (select) {
                            case 0: {// set songoku
                                try {
                                    ItemService.gI().setthiensu(player);

                                } catch (Exception e) {
                                    Logger.getLogger(NpcFactory.class
                                            .getName()).log(Level.SEVERE, null, e);
                                }
                            }
                            break;
                            case 1:// set kaioken
                                try {
                                ItemService.gI().setthiensu1(player);

                            } catch (Exception e) {
                                Logger.getLogger(NpcFactory.class
                                        .getName()).log(Level.SEVERE, null, e);
                            }
                            break;
                            case 2:// set thenxin hang
                                   try {
                                ItemService.gI().setthiensu2(player);

                            } catch (Exception e) {
                                Logger.getLogger(NpcFactory.class
                                        .getName()).log(Level.SEVERE, null, e);
                            }
                            break;
                            default:
                                break;
                        }
                        break;
                    case ConstNpc.menutd:
                        switch (select) {
                            case 0: {// set songoku
                                try {
                                    ItemService.gI().setSongoku(player);

                                } catch (Exception ex) {
                                    Logger.getLogger(NpcFactory.class
                                            .getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            break;
                            case 1:// set kaioken
                                try {
                                ItemService.gI().setKaioKen(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 2:// set thenxin hang
                                   try {
                                ItemService.gI().setThenXinHang(player);
                            } catch (Exception e) {
                            }
                            break;
                        }
                        break;
                    case ConstNpc.menunm:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().setLienHoan(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setPicolo(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setPikkoroDaimao(player);
                            } catch (Exception e) {
                            }
                            break;
                        }
                        break;

                    case ConstNpc.menuxd:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().setKakarot(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setCadic(player);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setNappa(player);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                        break;
                    case ConstNpc.CONFIRM_DIALOG:
                        ConfirmDialog confirmDialog = player.getConfirmDialog();
                        if (confirmDialog != null) {
                            if (confirmDialog instanceof MenuDialog menu) {
                                menu.getRunable().setIndexSelected(select);
                                menu.run();
                                return;
                            }
                            if (select == 0) {
                                confirmDialog.run();
                            } else {
                                confirmDialog.cancel();
                            }
                            player.setConfirmDialog(null);
                        }
                        break;
                    case 25100303:
                        switch (select) {
                            case 0:
                                PlayerDAO.saveMaBaoVe(player, player.MaBaoVe_TamThoi);
                                PlayerDAO.Bat_Tat_MaBaoVe(player, select);
                                player.MaBaoVe = player.MaBaoVe_TamThoi;
                                player.isUseMaBaoVe = true;
                                Service.getInstance().sendThongBao(player, "Kích hoạt thành công, tài khoản đang được bảo vệ");
                                PlayerService.gI().savePlayer(player);
                                break;
                            case 1:
                                break;
                        }
                        break;
                    case 25100304:
                        switch (select) {
                            case 0:
                                PlayerDAO.Bat_Tat_MaBaoVe(player, 1);
                                player.isUseMaBaoVe = false;
                                PlayerService.gI().savePlayer(player);
                                Service.getInstance().sendThongBao(player, "Chức năng bảo vệ tài khoản đang tắt");
                                break;
                            case 1:
                                break;
                        }
                        break;
                    case 25100305:
                        switch (select) {
                            case 0:
                                PlayerDAO.Bat_Tat_MaBaoVe(player, 0);
                                player.isUseMaBaoVe = true;
                                PlayerService.gI().savePlayer(player);
                                Service.getInstance().sendThongBao(player, "Tài khoản đang được bảo vệ");
                                break;
                            case 1:
                                break;
                        }
                        break;
                    case ConstNpc.HOP_QUA_THAN_LINH:

                        Item aotl_td = ItemService.gI().createNewItem((short) 555);
                        Item aotl_nm = ItemService.gI().createNewItem((short) 557);
                        Item aotl_xd = ItemService.gI().createNewItem((short) 559);

                        aotl_td.itemOptions.add(new ItemOption(47, 800 + new Random().nextInt(200)));

                        aotl_nm.itemOptions.add(new ItemOption(47, 900 + new Random().nextInt(100)));

                        aotl_xd.itemOptions.add(new ItemOption(47, 950 + new Random().nextInt(200)));

                        aotl_td.itemOptions.add(new ItemOption(21, 18)); // ycsm 18 tỉ
                        aotl_nm.itemOptions.add(new ItemOption(21, 18)); // ycsm 18 tỉ
                        aotl_xd.itemOptions.add(new ItemOption(21, 18)); // ycsm 18 tỉ

                        aotl_td.itemOptions.add(new ItemOption(30, 1)); // ycsm 18 tỉ
                        aotl_nm.itemOptions.add(new ItemOption(30, 1)); // ycsm 18 tỉ
                        aotl_xd.itemOptions.add(new ItemOption(30, 1)); // ycsm 18 tỉ

                        Item quantl_td = ItemService.gI().createNewItem((short) 556);
                        Item quantl_nm = ItemService.gI().createNewItem((short) 558);
                        Item quantl_xd = ItemService.gI().createNewItem((short) 560);

                        quantl_td.itemOptions.add(new ItemOption(22, 47 + new Random().nextInt(5)));
                        quantl_td.itemOptions.add(new ItemOption(27, (47 + new Random().nextInt(5)) * 1000 * 15 / 100));

                        quantl_nm.itemOptions.add(new ItemOption(22, 45 + new Random().nextInt(5)));
                        quantl_nm.itemOptions.add(new ItemOption(27, (45 + new Random().nextInt(5)) * 1000 * 15 / 100));

                        quantl_xd.itemOptions.add(new ItemOption(22, 42 + new Random().nextInt(8)));
                        quantl_xd.itemOptions.add(new ItemOption(27, (42 + new Random().nextInt(8)) * 1000 * 15 / 100));

                        quantl_td.itemOptions.add(new ItemOption(21, 18)); // ycsm 18 tỉ
                        quantl_nm.itemOptions.add(new ItemOption(21, 18)); // ycsm 18 tỉ
                        quantl_xd.itemOptions.add(new ItemOption(21, 18)); // ycsm 18 tỉ

                        quantl_td.itemOptions.add(new ItemOption(30, 1)); // ycsm 18 tỉ
                        quantl_nm.itemOptions.add(new ItemOption(30, 1)); // ycsm 18 tỉ
                        quantl_xd.itemOptions.add(new ItemOption(30, 1)); // ycsm 18 tỉ

                        Item gangtl_td = ItemService.gI().createNewItem((short) 562);
                        Item gangtl_nm = ItemService.gI().createNewItem((short) 564);
                        Item gangtl_xd = ItemService.gI().createNewItem((short) 566);

                        gangtl_td.itemOptions.add(new ItemOption(0, 3500 + new Random().nextInt(1200)));
                        gangtl_nm.itemOptions.add(new ItemOption(0, 3300 + new Random().nextInt(1100)));
                        gangtl_xd.itemOptions.add(new ItemOption(0, 3500 + new Random().nextInt(1400)));

                        gangtl_td.itemOptions.add(new ItemOption(21, 18)); // ycsm 18 tỉ
                        gangtl_nm.itemOptions.add(new ItemOption(21, 18)); // ycsm 18 tỉ
                        gangtl_xd.itemOptions.add(new ItemOption(21, 18)); // ycsm 18 tỉ

                        gangtl_td.itemOptions.add(new ItemOption(30, 1)); // ycsm 18 tỉ
                        gangtl_nm.itemOptions.add(new ItemOption(30, 1)); // ycsm 18 tỉ
                        gangtl_xd.itemOptions.add(new ItemOption(30, 1)); // ycsm 18 tỉ

                        Item giaytl_td = ItemService.gI().createNewItem((short) 563);
                        Item giaytl_nm = ItemService.gI().createNewItem((short) 565);
                        Item giaytl_xd = ItemService.gI().createNewItem((short) 567);

                        giaytl_td.itemOptions.add(new ItemOption(23, 42 + new Random().nextInt(5)));
                        giaytl_nm.itemOptions.add(new ItemOption(23, 47 + new Random().nextInt(5)));
                        giaytl_xd.itemOptions.add(new ItemOption(23, 45 + new Random().nextInt(4)));

                        giaytl_td.itemOptions.add(new ItemOption(28, (42 + new Random().nextInt(5)) * 1000 * 15 / 100));
                        giaytl_nm.itemOptions.add(new ItemOption(28, (47 + new Random().nextInt(5)) * 1000 * 15 / 100));
                        giaytl_xd.itemOptions.add(new ItemOption(28, (45 + new Random().nextInt(4)) * 1000 * 15 / 100));

                        giaytl_td.itemOptions.add(new ItemOption(21, 18)); // ycsm 18 tỉ
                        giaytl_nm.itemOptions.add(new ItemOption(21, 18)); // ycsm 18 tỉ
                        giaytl_xd.itemOptions.add(new ItemOption(21, 18)); // ycsm 18 tỉ

                        giaytl_td.itemOptions.add(new ItemOption(30, 1)); // ycsm 18 tỉ
                        giaytl_nm.itemOptions.add(new ItemOption(30, 1)); // ycsm 18 tỉ
                        giaytl_xd.itemOptions.add(new ItemOption(30, 1)); // ycsm 18 tỉ

                        Item nhan = ItemService.gI().createNewItem((short) 561);

                        nhan.itemOptions.add(new ItemOption(14, 14 + new Random().nextInt(4)));
                        nhan.itemOptions.add(new ItemOption(21, 18)); // ycsm 18 tỉ

                        nhan.itemOptions.add(new ItemOption(30, 1)); // ycsm 18 tỉ

                        Item HopQuaThanLinh = InventoryService.gI().findItemBagByTemp(player, 1280);

                        switch (select) {

                            case 0:
                                if (InventoryService.gI().getCountEmptyBag(player) < 5) {
                                    Service.getInstance().sendThongBao(player, "Cần 5 ô hành trang mới có thể mở!!!");
                                    return;
                                }
                                InventoryService.gI().addItemBag(player, aotl_td, 1);
                                InventoryService.gI().addItemBag(player, quantl_td, 1);
                                InventoryService.gI().addItemBag(player, gangtl_td, 1);
                                InventoryService.gI().addItemBag(player, giaytl_td, 1);
                                InventoryService.gI().addItemBag(player, nhan, 1);
                                InventoryService.gI().subQuantityItemsBag(player, HopQuaThanLinh, 1);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được 1 set thần linh trái đất");
                                return;
                            case 1:
                                if (InventoryService.gI().getCountEmptyBag(player) < 5) {
                                    Service.getInstance().sendThongBao(player, "Cần 5 ô hành trang mới có thể mở!!!");
                                    return;
                                }

                                InventoryService.gI().addItemBag(player, aotl_nm, 1);
                                InventoryService.gI().addItemBag(player, quantl_nm, 1);
                                InventoryService.gI().addItemBag(player, gangtl_nm, 1);
                                InventoryService.gI().addItemBag(player, giaytl_nm, 1);
                                InventoryService.gI().addItemBag(player, nhan, 1);
                                InventoryService.gI().subQuantityItemsBag(player, HopQuaThanLinh, 1);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được 1 set thần linh namek");
                                InventoryService.gI().sendItemBags(player);
                                return;
                            case 2:
                                if (InventoryService.gI().getCountEmptyBag(player) < 5) {
                                    Service.getInstance().sendThongBao(player, "Cần 5 ô hành trang mới có thể mở!!!");
                                    return;
                                }

                                InventoryService.gI().addItemBag(player, aotl_xd, 1);
                                InventoryService.gI().addItemBag(player, quantl_xd, 1);
                                InventoryService.gI().addItemBag(player, gangtl_xd, 1);
                                InventoryService.gI().addItemBag(player, giaytl_xd, 1);
                                InventoryService.gI().addItemBag(player, nhan, 1);
                                InventoryService.gI().subQuantityItemsBag(player, HopQuaThanLinh, 1);
                                InventoryService.gI().sendItemBags(player);

                                Service.getInstance().sendThongBao(player, "Bạn nhận được 1 set thần linh xayda");
                                return;
                        }
                        return;
                    case ConstNpc.UP_TOP_ITEM:

                        break;
                    case ConstNpc.RUONG_GO:
                        int size = player.textRuongGo.size();
                        if (size > 0) {
                            String menuselect = "OK [" + (size - 1) + "]";
                            if (size == 1) {
                                menuselect = "OK";
                            }
                            NpcService.gI().createMenuConMeo(player, ConstNpc.RUONG_GO, -1,
                                    player.textRuongGo.get(size - 1), menuselect);
                            player.textRuongGo.remove(size - 1);
                        }
                        break;
                    case ConstNpc.MENU_MABU_WAR:
                        if (select == 0) {
                            if (player.zone.finishMabuWar) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
                            } else if (player.zone.map.mapId == 119) {
                                Zone zone = MabuWar.gI().getMapLastFloor(120);
                                if (zone != null) {
                                    ChangeMapService.gI().changeMap(player, zone, 354, 240);
                                } else {
                                    Service.getInstance().sendThongBao(player,
                                            "Trận đại chiến đã kết thúc, tàu vận chuyển sẽ đưa bạn về nhà");
                                    ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
                                }
                            } else {
                                int idMapNextFloor = player.zone.map.mapId == 115 ? player.zone.map.mapId + 2
                                        : player.zone.map.mapId + 1;
                                ChangeMapService.gI().changeMap(player, idMapNextFloor, -1, 354, 240);
                            }
                            player.resetPowerPoint();
                            player.sendMenuGotoNextFloorMabuWar = false;
                            Service.getInstance().sendPowerInfo(player, "TL", player.getPowerPoint());
                            if (Util.isTrue(1, 30)) {
                                player.inventory.ruby += 1;
                                PlayerService.gI().sendInfoHpMpMoney(player);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được 1 Hồng Ngọc");
                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Bạn đen vô cùng luôn nên không nhận được gì cả");
                            }
                        }
                        break;
                    case ConstNpc.IGNORE_MENU:

                        break;
                    case ConstNpc.MAKE_MATCH_PVP:
                        // PVP_old.gI().sendInvitePVP(player, (byte) select);
                        PVPServcice.gI().sendInvitePVP(player, (byte) select);
                        break;
                    case ConstNpc.MAKE_FRIEND:
                        if (select == 0) {
                            Object playerId = PLAYERID_OBJECT.get(player.id);
                            if (playerId != null) {
                                FriendAndEnemyService.gI().acceptMakeFriend(player,
                                        Integer.parseInt(String.valueOf(playerId)));
                            }
                        }
                        break;
                    case ConstNpc.REVENGE:
                        if (select == 0) {
                            PVPServcice.gI().acceptRevenge(player);
                        }
                        break;
                    case ConstNpc.TUTORIAL_SUMMON_DRAGON:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        }
                        break;
                    case ConstNpc.SUMMON_SHENRON:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        } else if (select == 1) {
                            SummonDragon.gI().summonShenron(player);
                        }
                        break;
                    case ConstNpc.SUMMON_BLACK_SHENRON:
                        if (select == 0) {
                            SummonDragon.gI().summonBlackShenron(player);
                        }
                        break;
                    case ConstNpc.SUMMON_ICE_SHENRON:
                        if (select == 0) {
                            SummonDragon.gI().summonIceShenron(player);
                        }
                        break;
                    case ConstNpc.INTRINSIC:
                        if (select == 0) {
                            IntrinsicService.gI().showAllIntrinsic(player);
                        } else if (select == 1) {
                            IntrinsicService.gI().showConfirmOpen(player);
                        } else if (select == 2) {
                            IntrinsicService.gI().showConfirmOpenVip(player);
                        }
                        break;
                    case ConstNpc.INTRINSIC2:
                        if (select == 0) {
                            IntrinsicService.gI().showAllIntrinsic(player);
                        } else if (select == 1) {
                            IntrinsicService.gI().showConfirmOpenVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_OPEN_INTRINSIC:
                        if (select == 0) {
                            IntrinsicService.gI().open(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_OPEN_INTRINSIC_VIP:
                        if (select == 0) {
                            IntrinsicService.gI().openVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_LEAVE_CLAN:
                        if (select == 0) {
                            ClanService.gI().leaveClan(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_NHUONG_PC:
                        if (select == 0) {
                            ClanService.gI().phongPc(player, (int) PLAYERID_OBJECT.get(player.id));
                        }
                        break;
                    case ConstNpc.BAN_PLAYER:
                        if (select == 0) {
                            PlayerService.gI().banPlayer((Player) PLAYERID_OBJECT.get(player.id));
                            Service.getInstance().sendThongBao(player,
                                    "Ban người chơi " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                        }
                        break;
                    case ConstNpc.BUFF_PET:
                        if (select == 0) {
                            Player pl = (Player) PLAYERID_OBJECT.get(player.id);
                            if (pl.pet == null) {
                                PetService.gI().createNormalPet(pl, ConstPet.NORMAL);
                                Service.getInstance().sendThongBao(player, "Phát đệ tử cho "
                                        + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                            }
                        }
                        break;
                    case ConstNpc.TAIXIU:
                        String time = ((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
                        if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && player.goldTai == 0 && player.goldXiu == 0 && TaiXiu.gI().baotri == false) {
                            switch (select) {
                                case 0:
                                    int ketqua = TaiXiu.gI().z + TaiXiu.gI().y + TaiXiu.gI().x;
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TAIXIU, 11039, "\n|7|---Trò chơi may mắn---\n"
                                            + "\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z + " " + (ketqua >= 10 ? "Tài" : "Xỉu")
                                            + "\n|1|Kết quả kì trước" + "\n"
                                            + "|3| " + TaiXiu.gI().tongHistoryString
                                            + "\n\n|1|Tổng Cược TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Thỏi vàng"
                                            + "\n\n|1|Tổng Cược XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Thỏi vàng\n"
                                            + "\n|5|Đếm ngược: " + time, "Cập nhập", "Cược\n'Tài'", "Cược\n'Xỉu' ", "Đóng");
                                    break;
                                case 1:
                                    if (player.thanhVien) {
                                        Input.gI().TAI_taixiu(player);
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Bạn hãy mở thành viên để chơi nhé!");
                                    }
                                    break;
                                case 2:
                                    if (player.thanhVien) {
                                        Input.gI().XIU_taixiu(player);
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Bạn hãy mở thành viên để chơi nhé!");
                                    }
                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && player.goldTai > 0 && TaiXiu.gI().baotri == false) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(player, ConstNpc.TAIXIU, "\n|7|---Trò chơi may mắn---\n"
                                            + "\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|1|Tổng nhà 'Tài'=> " + Util.format(TaiXiu.gI().goldTai) + " Thỏi vàng"
                                            + "\n\n|1|Tổng nhà 'Xỉu'=> " + Util.format(TaiXiu.gI().goldXiu) + " Thỏi vàng\n"
                                            + "\n|5|Thời gian còn lại: " + time, "Cập nhập", "Cược\n'Tài'", "Cược\n'Xỉu' ", "Đóng");
                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && player.goldXiu > 0 && TaiXiu.gI().baotri == false) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(player, ConstNpc.TAIXIU, "\n|7|---Trò chơi may mắn---\n"
                                            + "\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|1|Tổng nhà 'Tài'=> " + Util.format(TaiXiu.gI().goldTai) + " Thỏi vàng"
                                            + "\n\n|1|Tổng nhà 'Xỉu'=> " + Util.format(TaiXiu.gI().goldXiu) + " Thỏi vàng\n"
                                            + "\n|5|Thời gian còn lại: " + time, "Cập nhập", "Cược\n'Tài'", "Cược\n'Xỉu' ", "Đóng");
                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && player.goldTai > 0 && TaiXiu.gI().baotri == true) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(player, ConstNpc.TAIXIU, "\n|7|---Trò chơi may mắn---\n"
                                            + "\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|1|Tổng nhà 'Tài'=> " + Util.format(TaiXiu.gI().goldTai) + " Thỏi vàng"
                                            + "\n\n|1|Tổng nhà 'Xỉu'=> " + Util.format(TaiXiu.gI().goldXiu) + " Thỏi vàng\n"
                                            + "\n|5|Thời gian còn lại: " + time, "Cập nhập", "Cược\n'Tài'", "Cược\n'Xỉu' ", "Đóng");
                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && player.goldXiu > 0 && TaiXiu.gI().baotri == true) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(player, ConstNpc.TAIXIU, "\n|7|---Trò chơi may mắn---\n"
                                            + "\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|1|Tổng nhà 'Tài'=> " + Util.format(TaiXiu.gI().goldTai) + " Thỏi vàng"
                                            + "\n\n|1|Tổng nhà 'Xỉu'=> " + Util.format(TaiXiu.gI().goldXiu) + " Thỏi vàng\n"
                                            + "\n|5|Thời gian còn lại: " + time, "Cập nhập", "Cược\n'Tài'", "Cược\n'Xỉu' ", "Đóng");
                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && player.goldXiu == 0 && player.goldTai == 0 && TaiXiu.gI().baotri == true) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(player, ConstNpc.TAIXIU, "\n|7|---Trò chơi may mắn---\n"
                                            + "\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|1|Tổng nhà 'Tài'=> " + Util.format(TaiXiu.gI().goldTai) + " Thỏi vàng"
                                            + "\n\n|1|Tổng nhà 'Xỉu'=> " + Util.format(TaiXiu.gI().goldXiu) + " Thỏi vàng\n"
                                            + "\n|5|Thời gian còn lại: " + time, "Cập nhập", "Cược\n'Tài'", "Cược\n'Xỉu' ", "Đóng");
                                    break;
                            }
                        }
                        break;

                    case ConstNpc.MENU_ADMIN:
                        switch (select) {
                            case 0:
                                for (int i = 14; i <= 20; i++) {
                                    Item item = ItemService.gI().createNewItem((short) i);
                                    InventoryService.gI().addItemBag(player, item, 0);
                                }
                                InventoryService.gI().sendItemBags(player);
                                break;
                            case 1:
                                if (player.pet == null) {
                                    PetService.gI().createNormalPet(player, ConstPet.NORMAL);
                                } else {
                                    PetService.gI().changeMabuPet(player, Util.nextInt(ConstPet.NORMAL, ConstPet.BILL_CON));
                                }
                                break;
                            case 2:
                                Maintenance.gI().start(60);
                                break;
                            case 3:
                                Input.gI().createFormFindPlayer(player);
                                break;
                            case 4:
                                this.createOtherMenu(player, ConstNpc.CALL_BOSS,
                                        "Chọn Boss?", "Full Cụm\nANDROID", "BLACK", "BROLY", "Cụm\nCell",
                                        "Cụm\nĐịa Ngục", "DOREMON", "FIDE", "FIDE\nBlack", "Cụm\nGINYU", "Cụm\nNAPPA", "NGỤC\nTÙ", "List boss");
                                break;

                        }
                        break;
                    case ConstNpc.CALL_BOSS:
                        switch (select) {
                            case 0:
                                BossFactory.createBoss(BossFactory.ANDROID_13);
                                BossFactory.createBoss(BossFactory.ANDROID_14);
                                BossFactory.createBoss(BossFactory.ANDROID_15);
                                BossFactory.createBoss(BossFactory.ANDROID_19);
                                BossFactory.createBoss(BossFactory.ANDROID_20);
                                BossFactory.createBoss(BossFactory.KINGKONG);
                                BossFactory.createBoss(BossFactory.PIC);
                                BossFactory.createBoss(BossFactory.POC);
                                break;
                            case 1:
                                BossFactory.createBoss(BossFactory.BLACKGOKU);
                                break;
                            case 2:
                                BossFactory.createBoss(BossFactory.BROLY);
                                break;
                            case 3:
                                BossFactory.createBoss(BossFactory.XEN_BO_HUNG_1);
                                break;
                            case 4:
                                BossFactory.createBoss(BossFactory.DOGDIANGUC);
                                BossFactory.createBoss(BossFactory.PIKKON);
                                break;
                            case 5:
                                Service.getInstance().sendThongBao(player, "Chua duoc update");
                                break;
                            case 6:
                                BossFactory.createBoss(BossFactory.FIDE_DAI_CA_1);
                                break;
                            case 7:
                                Service.getInstance().sendThongBao(player, "Coming sooonn");
                                break;
                            case 8:
                                BossFactory.createBoss(BossFactory.TIEU_DOI_TRUONG);
                                break;
                            case 9:
                                BossFactory.createBoss(BossFactory.KUKU);
                                BossFactory.createBoss(BossFactory.MAP_DAU_DINH);
                                BossFactory.createBoss(BossFactory.RAMBO);
                                break;
                            case 10:
                                BossFactory.createBoss(BossFactory.CUMBER);
                                break;
                            case 11:
                                BossManager.gI().showListBoss(player);
                                break;
                        }
                        break;
                    case ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND:
                        if (select == 0) {
                            for (int i = 0; i < player.inventory.itemsBoxCrackBall.size(); i++) {
                                player.inventory.itemsBoxCrackBall.set(i, ItemService.gI().createItemNull());
                            }
                            Service.getInstance().sendThongBao(player, "Đã xóa hết vật phẩm trong rương");
                        }
                        break;
                    case ConstNpc.MENU_FIND_PLAYER:
                        Player p = (Player) PLAYERID_OBJECT.get(player.id);
                        if (p != null) {
                            switch (select) {
                                case 0:
                                    if (p.zone != null) {
                                        ChangeMapService.gI().changeMapYardrat(player, p.zone, p.location.x,
                                                p.location.y);
                                    }
                                    break;
                                case 1:
                                    if (p.zone != null) {
                                        ChangeMapService.gI().changeMap(p, player.zone, player.location.x,
                                                player.location.y);
                                    }
                                    break;
                                case 2:
                                    if (p != null) {
                                        Input.gI().createFormChangeName(player);
                                    }
                                    break;
                                case 3:
                                    if (p != null) {
                                        String[] selects = new String[]{"Đồng ý", "Hủy"};
                                        NpcService.gI().createMenuConMeo(player, ConstNpc.BAN_PLAYER, -1,
                                                "Bạn có chắc chắn muốn ban " + p.name, selects, p);
                                    }
                                    break;
                            }
                        }
                        break;
                }
            }
        };
    }

    public static void processGemPurchase(Player player, int requiredVndBar, int gemAmount) {
        if (player.soDuVND >= requiredVndBar) {
            player.inventory.gem += gemAmount;
            player.soDuVND -= requiredVndBar;
            PlayerDAO.subVndBar(player, requiredVndBar);
            Service.getInstance().sendMoney(player);
            Service.getInstance().sendThongBao(player, "Bạn có thêm " + Util.mumberToLouis(gemAmount) + " ngọc xanh");
        } else {
            Service.getInstance().sendThongBao(player, "Bạn không đủ số dư");
        }
    }

    public static void processThoiVangPurchase(Player player, int requiredVndBar, int gemAmount) {
        if (player.soDuVND >= requiredVndBar) {
            player.soDuVND -= requiredVndBar;
            player.soThoiVang += gemAmount;
            PlayerDAO.subVndBar(player, requiredVndBar);
            PlayerDAO.addGoldBar(player, gemAmount);
            Service.getInstance().sendThongBao(player, "Bạn có thêm " + Util.mumberToLouis(gemAmount) + " thỏi vàng");
            int soHop = 0;
            switch (requiredVndBar) {
                case 20000:
//                    soHop = 1;
                    break;
                case 30000:
//                    soHop = 1;
                    break;
                case 50000:
//                    soHop = 3;
                    break;
                case 100000:
//                    soHop = 6;
                    break;
                case 200000:
//                    soHop = 12;
                    break;
                case 500000:
//                    soHop = 30;
                case 1000000:
//                    soHop = 60;
            }
//            Item hopThoiKhong = ItemService.gI().createNewItem((short) 1318, soHop);
//            InventoryService.gI().addItemBag(player, hopThoiKhong, 9999);
//            InventoryService.gI().sendItemBags(player);
//            Service.getInstance().sendThongBao(player, "Bạn nhận được " + soHop + " " + hopThoiKhong.getName());
            return;

        } else {
            Service.getInstance().sendThongBao(player, "Bạn không đủ số dư");
        }
    }

    public static void openMenuSuKien(Player player, Npc npc, int tempId, int select) {
        switch (Manager.EVENT_SEVER) {
            case 0:
                break;
            case 1:// hlw
                switch (select) {
                    case 0:
                        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                            Item keo = InventoryService.gI().finditemnguyenlieuKeo(player);
                            Item banh = InventoryService.gI().finditemnguyenlieuBanh(player);
                            Item bingo = InventoryService.gI().finditemnguyenlieuBingo(player);

                            if (keo != null && banh != null && bingo != null) {
                                Item GioBingo = ItemService.gI().createNewItem((short) 2016, 1);

                                // - Số item sự kiện có trong rương
                                InventoryService.gI().subQuantityItemsBag(player, keo, 10);
                                InventoryService.gI().subQuantityItemsBag(player, banh, 10);
                                InventoryService.gI().subQuantityItemsBag(player, bingo, 10);

                                GioBingo.itemOptions.add(new ItemOption(74, 0));
                                InventoryService.gI().addItemBag(player, GioBingo, 0);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendThongBao(player, "Đổi quà sự kiện thành công");
                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Vui lòng chuẩn bị x10 Nguyên Liệu Kẹo, Bánh Quy, Bí Ngô để đổi vật phẩm sự kiện");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                        }
                        break;
                    case 1:
                        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                            Item ve = InventoryService.gI().finditemnguyenlieuVe(player);
                            Item giokeo = InventoryService.gI().finditemnguyenlieuGiokeo(player);

                            if (ve != null && giokeo != null) {
                                Item Hopmaquy = ItemService.gI().createNewItem((short) 2017, 1);
                                // - Số item sự kiện có trong rương
                                InventoryService.gI().subQuantityItemsBag(player, ve, 3);
                                InventoryService.gI().subQuantityItemsBag(player, giokeo, 3);

                                Hopmaquy.itemOptions.add(new ItemOption(74, 0));
                                InventoryService.gI().addItemBag(player, Hopmaquy, 0);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendThongBao(player, "Đổi quà sự kiện thành công");
                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Vui lòng chuẩn bị x3 Vé đổi Kẹo và x3 Giỏ kẹo để đổi vật phẩm sự kiện");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                        }
                        break;
                    case 2:
                        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                            Item ve = InventoryService.gI().finditemnguyenlieuVe(player);
                            Item giokeo = InventoryService.gI().finditemnguyenlieuGiokeo(player);
                            Item hopmaquy = InventoryService.gI().finditemnguyenlieuHopmaquy(player);

                            if (ve != null && giokeo != null && hopmaquy != null) {
                                Item HopQuaHLW = ItemService.gI().createNewItem((short) 2012, 1);
                                // - Số item sự kiện có trong rương
                                InventoryService.gI().subQuantityItemsBag(player, ve, 3);
                                InventoryService.gI().subQuantityItemsBag(player, giokeo, 3);
                                InventoryService.gI().subQuantityItemsBag(player, hopmaquy, 3);

                                HopQuaHLW.itemOptions.add(new ItemOption(74, 0));
                                HopQuaHLW.itemOptions.add(new ItemOption(30, 0));
                                InventoryService.gI().addItemBag(player, HopQuaHLW, 0);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendThongBao(player,
                                        "Đổi quà hộp quà sự kiện Halloween thành công");
                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Vui lòng chuẩn bị x3 Hộp Ma Quỷ, x3 Vé đổi Kẹo và x3 Giỏ kẹo để đổi vật phẩm sự kiện");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                        }
                        break;
                }
                break;
            case 2:// 20/11
                switch (select) {
                    case 3:
                        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                            int evPoint = player.event.getEventPoint();
                            if (evPoint >= 999) {
                                Item HopQua = ItemService.gI().createNewItem((short) 2021, 1);
                                player.event.setEventPoint(evPoint - 999);

                                HopQua.itemOptions.add(new ItemOption(74, 0));
                                HopQua.itemOptions.add(new ItemOption(30, 0));
                                InventoryService.gI().addItemBag(player, HopQua, 0);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được Hộp Quà Teacher Day");
                            } else {
                                Service.getInstance().sendThongBao(player, "Cần 999 điểm tích lũy để đổi");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                        }
                        break;
                    // case 4:
                    // ShopService.gI().openShopSpecial(player, npc, ConstNpc.SHOP_HONG_NGOC, 0,
                    // -1);
                    // break;
                    default:
                        int n = 0;
                        switch (select) {
                            case 0:
                                n = 1;
                                break;
                            case 1:
                                n = 10;
                                break;
                            case 2:
                                n = 99;
                                break;
                        }

                        if (n > 0) {
                            Item bonghoa = InventoryService.gI().finditemBongHoa(player, n);
                            if (bonghoa != null) {
                                int evPoint = player.event.getEventPoint();
                                player.event.setEventPoint(evPoint + n);
                                ;
                                InventoryService.gI().subQuantityItemsBag(player, bonghoa, n);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + n + " điểm sự kiện");
                                int pre;
                                int next;
                                String text = null;
                                AttributeManager am = ServerManager.gI().getAttributeManager();
                                switch (tempId) {
                                    case ConstNpc.THAN_MEO_KARIN:
                                        pre = EVENT_COUNT_THAN_MEO / 999;
                                        EVENT_COUNT_THAN_MEO += n;
                                        next = EVENT_COUNT_THAN_MEO / 999;
                                        if (pre != next) {
                                            am.setTime(ConstAttribute.TNSM, 3600);
                                            text = "Toàn bộ máy chủ tăng được 20% TNSM cho đệ tử khi đánh quái trong 60 phút.";
                                        }
                                        break;

                                    case ConstNpc.QUY_LAO_KAME:
                                        pre = EVENT_COUNT_QUY_LAO_KAME / 999;
                                        EVENT_COUNT_QUY_LAO_KAME += n;
                                        next = EVENT_COUNT_QUY_LAO_KAME / 999;
                                        if (pre != next) {
                                            am.setTime(ConstAttribute.VANG, 3600);
                                            text = "Toàn bộ máy chủ được tăng 100% vàng từ quái trong 60 phút.";
                                        }
                                        break;

                                    case ConstNpc.THUONG_DE:
                                        pre = EVENT_COUNT_THUONG_DE / 999;
                                        EVENT_COUNT_THUONG_DE += n;
                                        next = EVENT_COUNT_THUONG_DE / 999;
                                        if (pre != next) {
                                            am.setTime(ConstAttribute.KI, 3600);
                                            text = "Toàn bộ máy chủ được tăng 20% KI trong 60 phút.";
                                        }
                                        break;

                                    case ConstNpc.THAN_VU_TRU:
                                        pre = EVENT_COUNT_THAN_VU_TRU / 999;
                                        EVENT_COUNT_THAN_VU_TRU += n;
                                        next = EVENT_COUNT_THAN_VU_TRU / 999;
                                        if (pre != next) {
                                            am.setTime(ConstAttribute.HP, 3600);
                                            text = "Toàn bộ máy chủ được tăng 20% HP trong 60 phút.";
                                        }
                                        break;

                                    case ConstNpc.BILL:
                                        pre = EVENT_COUNT_THAN_HUY_DIET / 999;
                                        EVENT_COUNT_THAN_HUY_DIET += n;
                                        next = EVENT_COUNT_THAN_HUY_DIET / 999;
                                        if (pre != next) {
                                            am.setTime(ConstAttribute.SUC_DANH, 3600);
                                            text = "Toàn bộ máy chủ được tăng 20% Sức đánh trong 60 phút.";
                                        }
                                        break;
                                }
                                if (text != null) {
                                    Service.getInstance().sendThongBaoAllPlayer(text);
                                }

                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Cần ít nhất " + n + " bông hoa để có thể tặng");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Cần ít nhất " + n + " bông hoa để có thể tặng");
                        }
                }
                break;
            case 3:
                if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                    Item keogiangsinh = InventoryService.gI().finditemKeoGiangSinh(player);

                    if (keogiangsinh != null && keogiangsinh.quantity >= 99) {
                        Item tatgiangsinh = ItemService.gI().createNewItem((short) 649, 1);
                        // - Số item sự kiện có trong rương
                        InventoryService.gI().subQuantityItemsBag(player, keogiangsinh, 99);

                        tatgiangsinh.itemOptions.add(new ItemOption(74, 0));
                        tatgiangsinh.itemOptions.add(new ItemOption(30, 0));
                        InventoryService.gI().addItemBag(player, tatgiangsinh, 0);
                        InventoryService.gI().sendItemBags(player);
                        Service.getInstance().sendThongBao(player, "Bạn nhận được Tất,vớ giáng sinh");
                    } else {
                        Service.getInstance().sendThongBao(player,
                                "Vui lòng chuẩn bị x99 kẹo giáng sinh để đổi vớ tất giáng sinh");
                    }
                } else {
                    Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                }
                break;
            case 4:
                switch (select) {
                    case 0:
                        if (!player.event.isReceivedLuckyMoney()) {
                            Calendar cal = Calendar.getInstance();
                            int day = cal.get(Calendar.DAY_OF_MONTH);
                            if (day >= 22 && day <= 24) {
                                Item goldBar = ItemService.gI().createNewItem((short) ConstItem.THOI_VANG,
                                        Util.nextInt(1, 3));
                                player.inventory.ruby += Util.nextInt(10, 30);
                                goldBar.quantity = Util.nextInt(1, 3);
                                InventoryService.gI().addItemBag(player, goldBar, 99999);
                                InventoryService.gI().sendItemBags(player);
                                PlayerService.gI().sendInfoHpMpMoney(player);
                                player.event.setReceivedLuckyMoney(true);
                                Service.getInstance().sendThongBao(player,
                                        "Nhận lì xì thành công,chúc bạn năm mới dui dẻ");
                            } else if (day > 24) {
                                Service.getInstance().sendThongBao(player, "Hết tết rồi còn đòi lì xì");
                            } else {
                                Service.getInstance().sendThongBao(player, "Đã tết đâu mà đòi lì xì");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Bạn đã nhận lì xì rồi");
                        }
                        break;
                    case 1:
                        ShopService.gI().openShopNormal(player, npc, ConstNpc.SHOP_SU_KIEN_TET, 1, -1);
                        break;
                }
                break;
            case ConstEvent.SU_KIEN_8_3:
                switch (select) {
                    case 3:
                        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                            int evPoint = player.event.getEventPoint();
                            if (evPoint >= 999) {
                                Item capsule = ItemService.gI().createNewItem((short) 2052, 1);
                                player.event.setEventPoint(evPoint - 999);

                                capsule.itemOptions.add(new ItemOption(74, 0));
                                capsule.itemOptions.add(new ItemOption(30, 0));
                                InventoryService.gI().addItemBag(player, capsule, 0);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được Capsule Hồng");
                            } else {
                                Service.getInstance().sendThongBao(player, "Cần 999 điểm tích lũy để đổi");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                        }
                        break;
                    default:
                        int n = 0;
                        switch (select) {
                            case 0:
                                n = 1;
                                break;
                            case 1:
                                n = 10;
                                break;
                            case 2:
                                n = 99;
                                break;
                        }

                        if (n > 0) {
                            Item bonghoa = InventoryService.gI().finditemBongHoa(player, n);
                            if (bonghoa != null) {
                                int evPoint = player.event.getEventPoint();
                                player.event.setEventPoint(evPoint + n);
                                InventoryService.gI().subQuantityItemsBag(player, bonghoa, n);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + n + " điểm sự kiện");
                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Cần ít nhất " + n + " bông hoa để có thể tặng");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Cần ít nhất " + n + " bông hoa để có thể tặng");
                        }
                }
                break;
        }
    }

    public static void doiChanMenh(Player player, int daId, int soLuong, long vang, boolean vinhVien) {
        Item da = InventoryService.gI().findItemBagByTemp(player, (short) daId);
        if (da == null || da.quantity < soLuong) {
            Service.getInstance().sendThongBao(player, "Thiếu nguyên liệu");
            return;
        }

        if (InventoryService.gI().getCountEmptyBag(player) <= 1) {
            Service.getInstance().sendThongBao(player, "Hành trang không đủ ô trống!");
            return;
        }

        if (player.inventory.gold < vang) {
            Service.getInstance().sendThongBao(player, "Không đủ vàng");
            return;
        }

        player.inventory.gold -= vang;
        Service.getInstance().sendMoney(player);

        Item chanMenh = ItemService.gI().createNewItem((short) 1407);

        // Option 1: Tấn công, HP+, KI+
        int[] optionChinh = {0, 6, 7};
        int opChinh = optionChinh[Util.nextInt(0, optionChinh.length - 1)];
        int valueChinh = (opChinh == 0) ? Util.nextInt(400, 2000) : Util.nextInt(8000, 20000);
        chanMenh.itemOptions.add(new ItemOption(opChinh, valueChinh));

        // Option 2: %SĐ, %HP, %KI
        int[] optionPhanTram = {50, 77, 103};
        int opPhanTram = optionPhanTram[Util.nextInt(0, optionPhanTram.length - 1)];
        int valuePhanTram = Util.nextInt(1, 7);
        chanMenh.itemOptions.add(new ItemOption(opPhanTram, valuePhanTram));

        if (!vinhVien) {
            // Option 3: thêm dòng 93 ngẫu nhiên 2-5
            chanMenh.itemOptions.add(new ItemOption(93, Util.nextInt(2, 5)));
        }
        InventoryService.gI().addItemBag(player, chanMenh, 0);
        InventoryService.gI().subQuantityItemsBag(player, da, soLuong);
        InventoryService.gI().sendItemBags(player);

        Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + chanMenh.template.name);
    }

    public static String getMenuSuKien(int id) {
        switch (id) {
            case ConstEvent.KHONG_CO_SU_KIEN:
                return "Chưa có\n Sự Kiện";
            case ConstEvent.SU_KIEN_HALLOWEEN:
                return "Sự Kiện\nHaloween";
            case ConstEvent.SU_KIEN_20_11:
                return "Sự Kiện\n 20/11";
            case ConstEvent.SU_KIEN_NOEL:
                return "Sự Kiện\n Giáng Sinh";
            case ConstEvent.SU_KIEN_TET:
                return "Sự Kiện\n Tết Nguyên\nĐán 2023";
            case ConstEvent.SU_KIEN_8_3:
                return "Sự Kiện\n 8/3";
        }
        return "Chưa có\n Sự Kiện";
    }

    public static String getMenuLamBanh(Player player, int type) {
        switch (type) {
            case 0:// bánh tét
                if (player.event.isCookingTetCake()) {
                    int timeCookTetCake = player.event.getTimeCookTetCake();
                    if (timeCookTetCake == 0) {
                        return "Lấy bánh";
                    } else if (timeCookTetCake > 0) {
                        return "Đang nấu\nBánh Tét\nCòn " + TimeUtil.secToTime(timeCookTetCake);
                    }
                } else {
                    return "Nấu\nBánh Tét";
                }
                break;
            case 1:// bánh chưng
                if (player.event.isCookingChungCake()) {
                    int timeCookChungCake = player.event.getTimeCookChungCake();
                    if (timeCookChungCake == 0) {
                        return "Lấy bánh";
                    } else if (timeCookChungCake > 0) {
                        return "Đang nấu\nBánh Chưng\nCòn " + TimeUtil.secToTime(timeCookChungCake);
                    }
                } else {
                    return "Nấu\nBánh Chưng";
                }
                break;
        }
        return "";
    }

}
