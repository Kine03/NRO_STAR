package nro.services.func;

import nro.consts.ConstItem;
import nro.consts.ConstNpc;
import nro.lib.RandomCollection;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.npc.Npc;
import nro.models.npc.NpcManager;
import nro.models.player.Player;
import nro.server.ServerLog;
import nro.server.ServerNotify;
import nro.server.io.Message;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.RewardService;
import nro.services.Service;
import nro.utils.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import nro.consts.ConstCombine;
import static nro.consts.ConstCombine.REMOVE_OPTION;
import nro.models.player.Inventory;

/**
 * @author 💖 Trần Lại 💖
 * @copyright 💖 GirlkuN 💖
 */
public class CombineServiceNew {

    private static final int COST_DOI_VE_DOI_DO_HUY_DIET = 500000000;
    private static final int COST_DAP_DO_KICH_HOAT = 500000000;
    private static final int COST_DAP_DO_KICH_HOAT_VIP = 1000000000;
    private static final int COST_DOI_MANH_KICH_HOAT = 500000000;
    private static final int COST_GIA_HAN_CAI_TRANG = 500000000;
    private static final int COST = 500000000;

    private static final int TIME_COMBINE = 500;
    private static final byte MAX_SAO_CAI_TRANG = 7;
    private static final byte MAX_LEVEL_PET = 8;
    private static final byte MAX_STAR_ITEM = 8;
    private static final byte MAX_LEVEL_ITEM = 7;

    private static final byte OPEN_TAB_COMBINE = 0;
    private static final byte REOPEN_TAB_COMBINE = 1;
    private static final byte COMBINE_SUCCESS = 2;
    private static final byte COMBINE_FAIL = 3;
    private static final byte COMBINE_CHANGE_OPTION = 4;
    private static final byte COMBINE_DRAGON_BALL = 5;
    public static final byte OPEN_ITEM = 6;

    public static final int EP_SAO_TRANG_BI = 500;
    public static final int PHA_LE_HOA_CAI_TRANG = 527;
    public static final int PHA_LE_HOA_TRANG_BI = 501;
    public static final int CHUYEN_HOA_TRANG_BI = 502;
    public static final int DOI_VE_HUY_DIET = 503;
    public static final int DAP_SET_KICH_HOAT = 504;
    public static final int DOI_MANH_KICH_HOAT = 505;
    public static final int NANG_CAP_SKH = 5287;

    public static final int NANG_CAP_VAT_PHAM = 506;

    public static final int NANG_CAP_BONG_TAI = 507;
    public static final int MO_CHI_SO_BONG_TAI = 519;
    public static final int NANG_CAP_BONG_TAI_VO_CUC = 528;
    public static final int MO_CHI_SO_BONG_TAI_VO_CUC = 529;

    public static final int LAM_PHEP_NHAP_DA = 508;
    public static final int NHAP_NGOC_RONG = 509;
    public static final int NHAP_NGOC_RONG_TRANH = 5099;
    public static final int CHE_TAO_DO_THIEN_SU = 510;
    public static final int DAP_SET_KICH_HOAT_CAO_CAP = 511;
    public static final int GIA_HAN_CAI_TRANG = 512;
    public static final int NANG_CAP_DO_THIEN_SU = 513;
    public static final int PHA_LE_HOA_TRANG_BI_X10 = 514;
    public static final int AN_TRANG_BI = 5177;

    public static final int NANG_CHAN_MENH = 540;
    public static final int PHAP_SU_HOA = 541;
    public static final int TAY_PHAP_SU = 542;
    public static final int NANG_CAP_PET = 543;
    public static final int MO_NOI_TAI_ITEM = 544;
    public static final int CUONG_HOA_LO_SPL = 545;
    public static final int NANG_PET = 546;
    // START _ SÁCH TUYỆT KỸ //
    public static final int GIAM_DINH_SACH = 515;
    public static final int TAY_SACH = 516;
    public static final int NANG_CAP_SACH_TUYET_KY = 517;
    public static final int PHUC_HOI_SACH = 518;
    public static final int PHAN_RA_SACH = 520;
    // END _ SÁCH TUYỆT KỸ //s

    // START _ PHA LÊ HÓA NEW //
    public static final int NANG_CAP_SAO_PHA_LE = 521;
    public static final int DANH_BONG_SAO_PHA_LE = 522;
    public static final int CUONG_HOA_LO_SAO_PHA_LE = 523;
    public static final int TAO_DA_HEMATILE = 524;
    // END _ PHA LÊ HÓA NEW //

    // START _ CHUYEN HOA TRANG BI //
    public static final int CHUYEN_HOA_BANG_VANG = 525;
    public static final int CHUYEN_HOA_BANG_NGOC = 526;

    // END _ CHUYEN HOA TRANG BI //
    private static final int GOLD_MOCS_BONG_TAI = 500_000_000;
    private static final int Gem_MOCS_BONG_TAI = 250;

    private static final int GOLD_BONG_TAI2 = 5_000_000;
    private static final int GEM_BONG_TAI2 = 20;

    private static final int GOLD_PHAP_SU_HOA = 500_000_000;
    private static final int GOLD_BONG_TAI = 500_000_000;
    private static final int GEM_BONG_TAI = 5_000;
    private static final int RATIO_BONG_TAI = 15;
    private static final int RATIO_NANG_CAP = 22;

    private final Npc baHatMit;
    private final Npc whis;

    private static CombineServiceNew i;

    public CombineServiceNew() {
        this.baHatMit = NpcManager.getNpc(ConstNpc.BA_HAT_MIT);
        this.whis = NpcManager.getNpc(ConstNpc.WHIS);
    }

    public static CombineServiceNew gI() {
        if (i == null) {
            i = new CombineServiceNew();
        }
        return i;
    }

    /**
     * Mở tab đập đồ
     *
     * @param player
     * @param type kiểu đập đồ
     */
    public void openTabCombine(Player player, int type) {
        player.combineNew.setTypeCombine(type);
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(OPEN_TAB_COMBINE);
            msg.writer().writeUTF(getTextInfoTabCombine(type));
            msg.writer().writeUTF(getTextTopTabCombine(type));
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

//    public int calculateChiSO1(Item trangbiChuyenHoa, int levelTrangBi, int soLanRotCap) {
//        
//        int chiSO1_trangBiCanChuyenHoa = 0;
//        
//        int chiSO_DaTangCap = trangbiChuyenHoa.itemOptions.get(0).param + (trangbiChuyenHoa.itemOptions.get(0).param * (levelTrangBi * 10 / 100));
//
//        if (soLanRotCap != 0) {
//            chiSO1_trangBiCanChuyenHoa = chiSO_DaTangCap - chiSO_DaTangCap * ( soLanRotCap * 10 / 100);
//        } else {
//            chiSO1_trangBiCanChuyenHoa = chiSO_DaTangCap;
//        }
//        
//        System.out.println(chiSO1_trangBiCanChuyenHoa);
//        
//        return chiSO1_trangBiCanChuyenHoa;
//        
//    }
    private float getRationangbt(int lvbt) { // tile dap do chi hat mit
        return 50f;
    }

    /**
     * Hiển thị thông tin đập đồ
     *
     * @param player
     */
    public void showInfoCombine(Player player, int[] index) {
        player.combineNew.clearItemCombine();
        if (index.length > 0) {
            for (int i = 0; i < index.length; i++) {
                player.combineNew.itemsCombine.add(player.inventory.itemsBag.get(index[i]));
            }
        }
        switch (player.combineNew.typeCombine) {
            case AN_TRANG_BI:
                if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                    if (player.combineNew.itemsCombine.size() == 2) {
                        Item item = player.combineNew.itemsCombine.get(0);
                        Item dangusac = player.combineNew.itemsCombine.get(1);
                        if (isTrangBiAn(item)) {
                            if (item != null && item.isNotNullItem() && dangusac != null && dangusac.isNotNullItem() && (dangusac.template.id == 1232 || dangusac.template.id == 1233 || dangusac.template.id == 1234) && dangusac.quantity >= 99) {
                                String npcSay = item.template.name + "\n|2|";
                                for (ItemOption io : item.itemOptions) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                                npcSay += "|1|Con có muốn biến trang bị " + item.template.name + " thành\n"
                                        + "trang bị Ấn không?\b|4|Đục là lên\n"
                                        + "|7|Cần 99 " + dangusac.template.name;
                                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Làm phép", "Từ chối");
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn chưa bỏ đủ vật phẩm !!!", "Đóng");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm này không thể hóa ấn", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần bỏ đủ vật phẩm yêu cầu", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống", "Đóng");
                }
                break;
            case LAM_PHEP_NHAP_DA:
                if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                    if (player.combineNew.itemsCombine.size() == 1) {
                        Item item = player.combineNew.itemsCombine.get(0);
                        if (item != null && item.isNotNullItem()) {
                            int itemId = item.template.id;

                            // Xử lý item ID 225 với số lượng x99 để tạo ID ngẫu nhiên từ 220 đến 224
                            if (itemId == 225 && item.quantity >= 99) {
                                String npcSay = "|2|Con có muốn biến x99 " + item.template.name + " thành\n" + "1 viên đá ngẫu nhiên\n"
                                        + "|7|Cần 99 " + item.template.name;
                                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Làm phép",
                                        "Từ chối");
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        "Cần x99 mảnh đá vụn để thực hiện nâng cấp", "Đóng");
                            }
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần x99 mảnh đá vụn để thực hiện nâng cấp", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống",
                            "Đóng");
                }
                break;
            case NANG_PET:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    Item item1 = player.combineNew.itemsCombine.get(1);
                    Item item2 = player.combineNew.itemsCombine.get(2);
                    if (isTrangBiGod(item1) && ispet(item) && isdanspro(item2)) {
                        int star = 0;
                        for (ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 72) {
                                star = io.param;
                                break;
                            }
                        }
                        if (star < MAX_LEVEL_PET) {
                            player.combineNew.goldCombine = 20000;
                            player.combineNew.ngusacCombine = getngusacKhamDa(star);
                            player.combineNew.ratioCombine = getRatioPhaLeHoa2(star);

                            String npcSay = "Con có muốn nâng cấp pet :\n" + item.template.name + " không \n";
                            if (star == 0) {
                                npcSay += "|7|Tỉ lệ thành công: 80%" + "\n";
                            }
                            if (star == 1) {
                                npcSay += "|7|Tỉ lệ thành công: 70%" + "\n";
                            }
                            if (star == 2) {
                                npcSay += "|7|Tỉ lệ thành công: 60%" + "\n";
                            }
                            if (star == 3) {
                                npcSay += "|7|Tỉ lệ thành công: 50%" + "\n";
                            }
                            if (star == 4) {
                                npcSay += "|7|Tỉ lệ thành công: 40%" + "\n";
                            }
                            if (star == 5) {
                                npcSay += "|7|Tỉ lệ thành công: 30%" + "\n";
                            }
                            if (star == 6) {
                                npcSay += "|7|Tỉ lệ thành công: 20%" + "\n";
                            }
                            if (star == 7) {
                                npcSay += "|7|Tỉ lệ thành công: 10%" + "\n";
                            }

                            if (item2 != null && item2.quantity >= 25) {
                                npcSay += "|1|Cần "
                                        + "20000 hồng ngọc";
                                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                        "Nâng cấp");

                            } else {
                                npcSay += "Còn thiếu nguyên liệu";
                                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Trang bị đã quá cấp", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy cho pet + 1 món đồ thần + 25 đá ngu sac pro theo đúng thứ tự", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không có vật phẩm để nâng cấp", "Đóng");
                }
                break;
            case PHA_LE_HOA_CAI_TRANG:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    if (isItemCaiTrang(item)) {
                        int star = 0;
                        Item hoa = null;
                        for (ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 107) {
                                star = io.param;
                                break;
                            }
                        }
                        for (Item it : player.combineNew.itemsCombine) {
                            if (it.isNotNullItem()) {
                                switch (it.template.id) {
                                    case 1502:
                                        hoa = it;
                                        break;
                                }
                            }
                        }
                        if (star < MAX_SAO_CAI_TRANG) {
                            int ruby = 10_000;
                            String npcSay = item.template.name + "\n|2|";
                            for (ItemOption io : item.itemOptions) {
                                if (io.optionTemplate.id != 102) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            if (hoa == null) {
                                npcSay += "|1|Cần x1 Đá Thánh";
                                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay,
                                        "Đóng");
                            } else if (player.inventory.ruby < ruby) {
                                npcSay += "thiếu 10k Hồng Ngọc";
                                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            } else {
                                npcSay += "|1|Tỉ lệ thành công 50%\nCần " + Util.numberToMoney(ruby) + " ruby";
                                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                        "Nâng cấp\ncần 10k Hồng Ngọc");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Vật phẩm đã đạt tối đa sao pha lê", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm này không thể đục lỗ",
                                "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy hãy chọn 1 cải trang để pha lê hóa",
                            "Đóng");
                }
                break;
            case CUONG_HOA_LO_SPL:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item trangBi = null;
                    Item duiduc = null;
                    Item Hematite = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (isTrangBiPhaLeHoa(item)) {
                            trangBi = item;
                        } else if (item.template.id == 1500) {
                            duiduc = item;
                        } else if (item.template.id == 1499) {
                            Hematite = item;
                        }
                    }
                    int star = 0;
                    int star2 = 0;//sao pha lê đã ép
                    if (trangBi != null && duiduc != null && duiduc.quantity >= 2 && Hematite != null && Hematite.quantity >= 20) {
                        for (ItemOption io : trangBi.itemOptions) {
                            if (io.optionTemplate.id == 102) {
                                star = io.param;
                            }
                        }
                        for (ItemOption io : trangBi.itemOptions) {
                            if (io.optionTemplate.id == 234) {
                                star2 = io.param;
                            }
                        }
                        if (star > 5 && star2 == 0) {
                            player.combineNew.goldCombine = 500000000;
                            player.combineNew.ratioCombine = 80;

                            String npcSay = trangBi.template.name + "\n|2|";
                            for (ItemOption io : trangBi.itemOptions) {
                                if (io.optionTemplate.id != 234) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            if (player.inventory.ruby < player.combineNew.gemCombine) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không đủ 500tr vàng!!", "Đóng");
                                return;
                            }
                            npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Cường hóa\ncần " + player.combineNew.goldCombine + " vàng");
                        } else if (star > 6 && star2 == 7) {
                            player.combineNew.goldCombine = 500000000;
                            player.combineNew.ratioCombine = 50;
                            String npcSay = trangBi.template.name + "\n|2|";
                            for (ItemOption io : trangBi.itemOptions) {
                                if (io.optionTemplate.id == 234) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            if (player.inventory.ruby < player.combineNew.gemCombine) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không đủ 500tr vàng!!", "Đóng");
                                return;
                            }
                            npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Cường hóa\ncần " + player.combineNew.goldCombine + " vàng");
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Đã cường hóa tối đa", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 trang bị đã ép 6 sao pha lê và 20 Đá Hematite và 2 Dùi đục", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 trang bị đã ép 6 sao pha lê và 20 Đá Hematite và 2 Dùi đục", "Đóng");
                }
                break;
            case REMOVE_OPTION:
                if (!Objects.isNull(checkItemCanCombine(player))) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, "Xác nhận thực hiện", "Nâng cấp", "Đóng");
                }
                break;
            case MO_NOI_TAI_ITEM:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item uchiha = null;
                    Item datime = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.type == 5) {
                            uchiha = item;
                        } else if (item.template.id == 1402) {
                            datime = item;
                        }
                    }
                    player.combineNew.gemCombine = 500;
                    if (uchiha != null && datime != null) {
                        if (player.combineNew.gemCombine < 500) {
                            Service.getInstance().sendThongBaoOK(player, "Không đủ hồng ngọc");
                            return;
                        }
                        String npcSay = "|2|Mở nội tại cần\n";
                        npcSay += "Cần Cải Trang \n"
                                + "Cần 1 Đá Thời Gian \n"
                                + "Cần " + player.combineNew.gemCombine + " Hồng Ngọc ?\n";
                        baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                "Nâng cấp", "Từ chối");
                    } else {
                        Service.getInstance().sendThongBaoOK(player, "Cần cải trang x1 và x1 đá thời gian");
                    }
                } else {
                    Service.getInstance().sendThongBaoOK(player, "Cần cải trang x1 và x1 đá thời gian");
                }
                break;
            case NANG_CAP_PET:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    Item item1 = player.combineNew.itemsCombine.get(1);
                    Item item2 = player.combineNew.itemsCombine.get(2);
                    if (isTrangBiGod(item1) && isLinhThu(item) && isdalua(item2)) {
                        int star = 0;
                        for (ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 72) {
                                star = io.param;
                                break;
                            }
                        }
                        if (star < MAX_LEVEL_PET) {
                            player.combineNew.goldCombine = 5000;
                            player.combineNew.ngusacCombine = getngusacKhamDa(star);
                            player.combineNew.ratioCombine = getRatioPhaLeHoa2(star);

                            String npcSay = "Con có muốn nâng cấp linh thú :\n" + item.template.name + " không \n";
                            if (star == 0) {
                                npcSay += "|7|Tỉ lệ thành công: 80%" + "\n";
                            }
                            if (star == 1) {
                                npcSay += "|7|Tỉ lệ thành công: 70%" + "\n";
                            }
                            if (star == 2) {
                                npcSay += "|7|Tỉ lệ thành công: 60%" + "\n";
                            }
                            if (star == 3) {
                                npcSay += "|7|Tỉ lệ thành công: 50%" + "\n";
                            }
                            if (star == 4) {
                                npcSay += "|7|Tỉ lệ thành công: 40%" + "\n";
                            }
                            if (star == 5) {
                                npcSay += "|7|Tỉ lệ thành công: 30%" + "\n";
                            }
                            if (star == 6) {
                                npcSay += "|7|Tỉ lệ thành công: 20%" + "\n";
                            }
                            if (star == 7) {
                                npcSay += "|7|Tỉ lệ thành công: 10%" + "\n";
                            }
                            Item dangusac = InventoryService.gI().findItemBagByTemp(player, 457);

                            if (dangusac != null && dangusac.quantity >= player.combineNew.ngusacCombine && item2 != null && item2.quantity >= 10) {
                                npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.ngusacCombine) + " Thỏi vàng \n"
                                        + "5000 hồng ngọc";
                                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                        "Nâng cấp");
//                        if (player.combineNew.goldCombine <= player.inventory.ruby ) {
//                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
//                                    "Nâng cấp\ncần " + player.combineNew.goldCombine + " hồng ngọc " );

                            } else {
                                npcSay += "Còn thiếu nguyên liệu";
                                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Trang bị đã quá cấp", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy cho linh thú 1 món đồ thần, đá lửa theo đúng thứ tự", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không có vật phẩm để nâng cấp", "Đóng");
                }
                break;
            case PHAP_SU_HOA:
                if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                    if (player.combineNew.itemsCombine.size() == 2) {
                        Item itemOption = null;
                        Item daPhapSu = null;
                        for (Item item : player.combineNew.itemsCombine) {
                            if (item.isNotNullItem()) {
                                if (isTrangBiPhapsu(item)) {
                                    itemOption = item;
                                } else if (item.template.id == 1379) {
                                    daPhapSu = item;
                                }
                            }
                        }

                        if (daPhapSu == null || daPhapSu.quantity <= 10) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu pháp sư", "Đóng");
                            return;
                        }
                        int star = 0;
                        if (itemOption != null && itemOption.itemOptions != null) {
                            for (ItemOption io2 : itemOption.itemOptions) {
                                if (io2.optionTemplate.id == 205) {
                                    star = io2.param;
                                    break;
                                }
                            }
                        }

                        if (isTrangBiPhapsu(itemOption)) {
                            if (itemOption != null && itemOption.isNotNullItem() && daPhapSu != null && daPhapSu.isNotNullItem() && daPhapSu.template.id == 1379 && daPhapSu.quantity >= 3) {

                                player.combineNew.ratioCombine = getRatioPhapSuHoa(star);
                                String npcSay = "|1|Con có muốn biến trang bị " + itemOption.template.name + " thành\n"
                                        + "trang bị Pháp sư hóa không?\n"
                                        + "|2|Sau khi nâng cấp sẽ được thêm chỉ số Pháp sư HP, KI hoặc SĐ\n"
                                        + "|7|Cần 500 Triệu vàng và 10 " + daPhapSu.template.name + "\n"
                                        + "|2|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%";
                                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Làm phép", "Từ chối");

                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn chưa bỏ đủ vật phẩm !!!", "Đóng");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm này không thể hóa ấn hoặc có HSD", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần bỏ đủ vật phẩm yêu cầu", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống", "Đóng");
                }
                break;
            case TAY_PHAP_SU:
                if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                    if (player.combineNew.itemsCombine.size() == 2) {
                        Item itemOption = null;
                        Item buaPhapSu = null;
                        for (Item item : player.combineNew.itemsCombine) {
                            if (item.isNotNullItem()) {
                                if (isTrangBiPhapsu(item)) {
                                    itemOption = item;
                                } else if (item.template.id == 1380) {
                                    buaPhapSu = item;
                                }
                            }
                        }

                        if (buaPhapSu == null || buaPhapSu.quantity < 0) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu bùa pháp sư", "Đóng");
                            return;
                        }
                        if (isTrangBiPhapsu(itemOption)) {
                            if (itemOption != null && itemOption.isNotNullItem() && buaPhapSu != null && buaPhapSu.isNotNullItem() && buaPhapSu.template.id == 1380 && buaPhapSu.quantity > 0) {
                                String npcSay = itemOption.template.name + "\n|2|";
                                for (ItemOption io : itemOption.itemOptions) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                                npcSay += "|1|Con có muốn tẩy trang bị " + itemOption.template.name + " về\n"
                                        + "lúc chưa Pháp sư hóa không?\n"
                                        + "|7|Cần 1 " + buaPhapSu.template.name;
                                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Làm phép", "Từ chối");
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn chưa bỏ đủ vật phẩm !!!", "Đóng");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm này không thể thực hiện", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần bỏ đủ vật phẩm yêu cầu", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống", "Đóng");
                }
                break;
            case NANG_CHAN_MENH:
                if (player.combineNew.itemsCombine.size() != 3) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta chân mệnh và x30 Đá cam, x2 Đá lửa", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item chanmenh = null;
                    Item da = null;
                    Item daLua = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.isNotNullItem()) {
                            if (item.template.type == 35) {
                                chanmenh = item;
                            } else if (item.template.id == 1450) {
                                da = item;
                            } else if (item.template.id == 1452) {
                                daLua = item;
                            }
                        }
                    }
                    if (chanmenh == null) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu chân mệnh", "Đóng");
                        return;
                    }
                    if (da == null || da.quantity < 30) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đá cam", "Đóng");
                        return;
                    }
                    if (daLua == null || da.quantity < 2) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đá lửa", "Đóng");
                        return;
                    }
                    if (chanmenh.template.id >= 1415) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Đã đạt cấp tối đa!!!", "Đóng");
                        return;
                    }
                    player.combineNew.ratioCombine = (float) getTileNangHonHoan(chanmenh.template.id);
                    String npcSay = "|2|Ngươi muốn nâng cấp chân mệnh của mình sao?\n|7|"
                            + "Hãy đưa ta đủ nguyên liệu ta sẽ làm cho nó mạnh hơn\n"
                            + "|1|Cần x30 đá cam \n"
                            + "|1|Cần x2 đá lửa \n"
                            + "|1|Tỉ lệ thành công: " + player.combineNew.ratioCombine + " %";

                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                            npcSay, "Nâng cấp", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cất đi con ta không thèm", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break;
            case EP_SAO_TRANG_BI:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item trangBi = null;
                    Item daPhaLe = null;

                    for (Item item : player.combineNew.itemsCombine) {
                        if (isTrangBiPhaLeHoa2(item)) {
                            trangBi = item;
                        } else {
                            daPhaLe = item;
                        }
                    }

                    if (trangBi == null || daPhaLe == null) {
                        baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 trang bị có lỗ sao pha lê và\n1 loại đá pha lê siêu cấp or nrđ phù hợp để ép vào", "Đóng");
                        break;
                    }

                    int star = 0;
                    int starEmpty = 0;
                    int level_216 = 0;
                    ItemOption optionLevel_216 = null;

                    for (ItemOption io : trangBi.itemOptions) {
                        if (io.optionTemplate.id == 102) {
                            star = io.param;
                        } else if (io.optionTemplate.id == 107) {
                            starEmpty = io.param;
                        } else if (io.optionTemplate.id == 234) {
                            level_216 = io.param;
                            optionLevel_216 = io;
                        }
                    }

                    int id = daPhaLe.template.id;
                    int type = daPhaLe.template.type;

                    boolean isDa14_20 = id >= 14 && id <= 20;
                    boolean isDa807_813 = id >= 807 && id <= 813;
                    boolean isType30 = type == 30;

                    boolean hopLe = false;

                    if (star < 6) {
                        hopLe = isDa14_20 || isType30;
                    } else if (star == 6) {
                        hopLe = level_216 >= 7 && (isDa807_813 || isType30);
                    } else if (star == 7) {
                        hopLe = level_216 >= 8 && (isDa807_813 || isType30);
                    }

                    if (!hopLe) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Không thể ép sao với đá hoặc cấp cường hóa hiện tại", "Đóng");
                        return;
                    }

                    if (star >= starEmpty || star >= 8) {
                        baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Đã đạt tối đa hoặc không còn lỗ sao pha lê", "Đóng");
                        break;
                    }

                    player.combineNew.gemCombine = getGemEpSao(star);
                    String npcSay = trangBi.template.name + "\n|2|";
                    for (ItemOption io : trangBi.itemOptions) {
                        if (io.optionTemplate.id != 102) {
                            npcSay += io.getOptionString() + "\n";
                        }
                    }

                    String optName;
                    if (isDa807_813) {
                        optName = ItemService.gI().getItemOptionTemplate(getOptionDaPhaLe2(daPhaLe)).name
                                .replaceAll("#", getParamDaPhaLe2(daPhaLe) + "");
                    } else {
                        optName = ItemService.gI().getItemOptionTemplate(getOptionDaPhaLe(daPhaLe)).name
                                .replaceAll("#", getParamDaPhaLe(daPhaLe) + "");
                    }

                    npcSay += "|7|" + optName + "\n";
                    npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.gemCombine) + " ngọc";

                    baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                            "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                } else {
                    baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 trang bị có lỗ sao pha lê và\n1 loại đá pha lê siêu cấp or nrđ phù hợp để ép vào", "Đóng");
                }
                break;
            case PHA_LE_HOA_TRANG_BI:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    if (isTrangBiPhaLeHoa(item)) {
                        int star = 0;
                        int param = 0;
                        for (ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 107) {
                                star = io.param;
                                break;
                            }
                        }
                        if (star < MAX_STAR_ITEM) {
                            player.combineNew.goldCombine = getGoldPhaLeHoa(star);
                            player.combineNew.gemCombine = getGemPhaLeHoa(star);
                            player.combineNew.ratioCombine = getRatioPhaLeHoa(star);

                            String npcSay = "|2|" + item.template.name + "\n";
                            for (ItemOption io : item.itemOptions) {
                                if (io.optionTemplate.id != 102 && io.optionTemplate.id != 107) {
                                    npcSay += "|0|" + io.getOptionString() + "\n";
                                }
                                if (io.optionTemplate.id == 107) {
                                    npcSay += "|1|" + io.getOptionString() + "\n";
                                }
                            }
                            npcSay += "|2|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                            npcSay += "|2|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";

                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\n1 ngọc\nx100 lần", "Nâng cấp\n1 ngọc\nx10 lần", "Nâng cấp\n1 ngọc", "Từ chối");

                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Vật phẩm đã đạt tối đa sao pha lê", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm này không thể đục lỗ",
                                "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy hãy chọn 1 vật phẩm để pha lê hóa",
                            "Đóng");
                }
                break;
            case CHE_TAO_DO_THIEN_SU:
                if (player.combineNew.itemsCombine.size() == 0) {
                    return;
                }
                if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 5) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isCongthucVip()).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Công thức Vip", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 9999).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Mảnh đồ thiên sứ", "Đóng");
                        return;
                    }
//                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() < 1 || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() < 1) {
//                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đá nâng cấp", "Đóng");
//                        return;
//                    }
//                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() < 1 || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() < 1) {
//                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đá may mắn", "Đóng");
//                        return;
//                    }
                    Item mTS = null, daNC = null, daMM = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.isNotNullItem()) {
                            if (item.isManhTS()) {
                                mTS = item;
                            } else if (item.isdanangcapDoTs()) {
                                daNC = item;
                            } else if (item.isDamayman()) {
                                daMM = item;
                            }
                        }
                    }
                    int tilemacdinh = 35;
                    int tilenew = tilemacdinh;
//                    if (daNC != null) {
//                        tilenew += (daNC.template.id - 1073) * 10;                     
//                    }

                    String npcSay = "|1|Chế tạo " + player.combineNew.itemsCombine.stream().filter(Item::isManhTS).findFirst().get().typeNameManh() + " Thiên sứ "
                            + player.combineNew.itemsCombine.stream().filter(Item::isCongthucVip).findFirst().get().typeHanhTinh() + "\n"
                            + "|1|Mạnh hơn trang bị Hủy Diệt từ 20% đến 35%"
                            + "\n|2|Mảnh ghép " + mTS.quantity + "/9999(Thất bại -999 mảnh ghép)";
                    if (daNC != null) {
                        npcSay += "\n|2|Đá nâng cấp " + player.combineNew.itemsCombine.stream().filter(Item::isdanangcapDoTs).findFirst().get().typeDanangcap()
                                + " (+" + (daNC.template.id - 1073) + "0% tỉ lệ thành công)";
                    }
                    if (daMM != null) {
                        npcSay += "\n|2|Đá may mắn " + player.combineNew.itemsCombine.stream().filter(Item::isDamayman).findFirst().get().typeDaMayman()
                                + " (+" + (daMM.template.id - 1078) + "0% tỉ lệ tối đa các chỉ số)";
                    }
                    if (daNC != null) {
                        tilenew += (daNC.template.id - 1073) * 10;
                        npcSay += "\n|2|Tỉ lệ thành công: " + tilenew + "%";
                    } else {
                        npcSay += "\n|2|Tỉ lệ thành công: " + tilemacdinh + "%";
                    }
                    npcSay += "\n|2|Phí nâng cấp: 10 tỉ vàng";
                    if (player.inventory.gold < 10_000_000_000L) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn không đủ vàng", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                            npcSay, "Đồng ý", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 4) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không đủ nguyên liệu", "Đóng");
                }
                break;

            // START _ SÁCH TUYỆT KỸ //
            case GIAM_DINH_SACH:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item sachTuyetKy = null;
                    Item buaGiamDinh = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (issachTuyetKy(item)) {
                            sachTuyetKy = item;
                        } else if (item.template.id == 1283) {
                            buaGiamDinh = item;
                        }
                    }
                    if (InventoryService.gI().getCountEmptyBag(player) < 1) {
                        Service.getInstance().sendThongBaoOK(player, "Hành trang đã đầy");
                        return;
                    }
                    if (player.inventory.ruby < 5000) {
                        Service.getInstance().sendThongBaoOK(player, "Không đủ 5k hồng ngọc ");
                        return;
                    }
                    if (sachTuyetKy != null && buaGiamDinh != null) {

                        String npcSay = "|1|" + sachTuyetKy.getName() + "\n";
                        npcSay += "|2|" + buaGiamDinh.getName() + " " + buaGiamDinh.quantity + "/1";
                        baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                "Giám định\n 5000 hồng ngọc", "Từ chối");
                    } else {
                        Service.getInstance().sendThongBaoOK(player, "Cần Sách Tuyệt Kỹ và bùa giám định");
                        return;
                    }
                } else {
                    Service.getInstance().sendThongBaoOK(player, "Cần Sách Tuyệt Kỹ và bùa giám định");
                    return;
                }
                break;

            case CHUYEN_HOA_BANG_VANG:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item trangBiGoc = player.combineNew.itemsCombine.get(0);
                    Item trangBiCanChuyenHoa = player.combineNew.itemsCombine.get(1);

                    int goldChuyenHoa = 2_000_000_000;

                    int levelTrangBi = 0;
                    int soLanRotCap = 0;
                    int chiSO1_trangBiCanChuyenHoa = 0;

                    for (ItemOption io : trangBiGoc.itemOptions) {
                        if (io.optionTemplate.id == 72) {
                            levelTrangBi = io.param;
                        } else if (io.optionTemplate.id == 232) {
                            soLanRotCap += io.param;
                        }
                    }

                    // START Tính chỉ số nhân với số cấp và trừ với số lần rớt cấp //
                    int chisogoc = trangBiCanChuyenHoa.itemOptions.get(0).param;

                    chisogoc += chisogoc * (levelTrangBi * 0.1);

                    chisogoc -= chisogoc * (soLanRotCap * 0.1);
                    // END Tính chỉ số nhân với số cấp và trừ với số lần rớt cấp //

                    boolean trangBi_daNangCap_daPhaLeHoa = false;

                    for (int so = 0; so < trangBiCanChuyenHoa.itemOptions.size(); so++) {
                        if (trangBiCanChuyenHoa.itemOptions.get(so).optionTemplate.id == 72 || trangBiCanChuyenHoa.itemOptions.get(so).optionTemplate.id == 102) {
                            trangBi_daNangCap_daPhaLeHoa = true;
                            break;
                        }
                    }

                    if (!isTrangBiGoc(trangBiGoc)) {
                        Service.getInstance().sendThongBaoOK(player, "Trang bị phải từ bậc lưỡng long, Jean hoặc Zelot trở lên");
                        return;
                    } else if (levelTrangBi < 4) {
                        Service.getInstance().sendThongBaoOK(player, "Trang bị gốc có cấp từ [+4]");
                        return;
                    } else if (!isTrangBiChuyenHoa(trangBiCanChuyenHoa)) {
                        Service.getInstance().sendThongBaoOK(player, "Trang bị phải từ bậc lưỡng long, Jean hoặc Zelot trở lên");
                        return;
                    } else if (trangBi_daNangCap_daPhaLeHoa) {
                        Service.getInstance().sendThongBaoOK(player, "Trang bị nhập thể phải chưa nâng cấp và pha lê hóa trang bị");
                        return;
                    } else if (!isCheckTrungTypevsGender(trangBiGoc, trangBiCanChuyenHoa)) {
                        Service.getInstance().sendThongBaoOK(player, "Trang bị gốc và Trang bị nhập thể phải cùng loại và hành tinh");
                        return;
                    } else {
                        String NpcSay = "|2|Hiện tại " + trangBiCanChuyenHoa.getName() + "\n";
                        if (trangBiCanChuyenHoa.itemOptions != null) {
                            for (ItemOption io : trangBiCanChuyenHoa.itemOptions) {
                                if (io.optionTemplate.id != 72) {
                                    NpcSay += "|0|" + io.getOptionString() + "\n";
                                }
                            }
                        }
                        NpcSay += "|2|Sau khi nâng cấp (+" + levelTrangBi + ")\n";
                        for (ItemOption io : trangBiCanChuyenHoa.itemOptions) {
                            if (io.optionTemplate.id != 72) {
                                if (io.optionTemplate.id == 0 || io.optionTemplate.id == 47 || io.optionTemplate.id == 6 || io.optionTemplate.id == 7 || io.optionTemplate.id == 14 || io.optionTemplate.id == 22 || io.optionTemplate.id == 23) {
                                    NpcSay += "|1|" + io.getOptionString(chisogoc) + "\n";
                                } else {
                                    NpcSay += "|1|" + io.getOptionString() + "\n";
                                }
                            }
                        }
                        for (ItemOption io : trangBiGoc.itemOptions) {
                            if (io.optionTemplate.id != 72 && io.optionTemplate.id != 102 && io.optionTemplate.id != 107 && io.optionTemplate.id != 0 && io.optionTemplate.id != 47 && io.optionTemplate.id != 6 && io.optionTemplate.id != 7 && io.optionTemplate.id != 14 && io.optionTemplate.id != 22 && io.optionTemplate.id != 23) {
                                NpcSay += io.getOptionString() + "\n";
                            } else {
                            }
                        }
                        NpcSay += "Chuyển qua tất cả sao pha lê\n";
                        NpcSay += "|2|Cần 2 tỷ vàng";
                        baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, NpcSay,
                                "Nâng cấp\n2 tỷ\nvàng", "Từ chối");
                    }
                } else {
                    Service.getInstance().sendThongBaoOK(player, "Cần 1 trang bị có cấp từ [+4] và 1 trang bị không có cấp nhưng cao hơn 1 bậc");
                    return;
                }
                break;

            case CHUYEN_HOA_BANG_NGOC:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item trangBiGoc = player.combineNew.itemsCombine.get(0);
                    Item trangBiCanChuyenHoa = player.combineNew.itemsCombine.get(1);

                    int ngocChuyenHoa = 5_000;

                    int levelTrangBi = 0;
                    int soLanRotCap = 0;
                    int chiSO1_trangBiCanChuyenHoa = 0;

                    for (ItemOption io : trangBiGoc.itemOptions) {
                        if (io.optionTemplate.id == 72) {
                            levelTrangBi = io.param - 1;
                        } else if (io.optionTemplate.id == 232) {
                            soLanRotCap += io.param;
                        }
                    }

                    // START Tính chỉ số nhân với số cấp và trừ với số lần rớt cấp //
                    int chisogoc = trangBiCanChuyenHoa.itemOptions.get(0).param;

                    chisogoc += chisogoc * (levelTrangBi * 0.1);

                    chisogoc -= chisogoc * (soLanRotCap * 0.1);
                    // END Tính chỉ số nhân với số cấp và trừ với số lần rớt cấp //

                    boolean trangBi_daNangCap_daPhaLeHoa = false;

                    for (int so = 0; so < trangBiCanChuyenHoa.itemOptions.size(); so++) {
                        if (trangBiCanChuyenHoa.itemOptions.get(so).optionTemplate.id == 72 || trangBiCanChuyenHoa.itemOptions.get(so).optionTemplate.id == 102) {
                            trangBi_daNangCap_daPhaLeHoa = true;
                            break;
                        }
                    }

                    if (!isTrangBiGoc(trangBiGoc)) {
                        Service.getInstance().sendThongBaoOK(player, "Trang bị phải từ bậc lưỡng long, Jean hoặc Zelot trở lên");
                        return;
                    } else if (levelTrangBi < 4) {
                        Service.getInstance().sendThongBaoOK(player, "Trang bị gốc có cấp từ [+4]");
                        return;
                    } else if (!isTrangBiChuyenHoa(trangBiCanChuyenHoa)) {
                        Service.getInstance().sendThongBaoOK(player, "Trang bị phải từ bậc lưỡng long, Jean hoặc Zelot trở lên");
                        return;
                    } else if (trangBi_daNangCap_daPhaLeHoa) {
                        Service.getInstance().sendThongBaoOK(player, "Trang bị nhập thể phải chưa nâng cấp và pha lê hóa trang bị");
                        return;
                    } else if (!isCheckTrungTypevsGender(trangBiGoc, trangBiCanChuyenHoa)) {
                        Service.getInstance().sendThongBaoOK(player, "Trang bị gốc và Trang bị nhập thể phải cùng loại và hành tinh");
                        return;
                    } else {
                        String NpcSay = "|2|Hiện tại " + trangBiCanChuyenHoa.getName() + "\n";
                        if (trangBiCanChuyenHoa.itemOptions != null) {
                            for (ItemOption io : trangBiCanChuyenHoa.itemOptions) {
                                if (io.optionTemplate.id != 72) {
                                    NpcSay += "|0|" + io.getOptionString() + "\n";
                                }
                            }
                        }
                        NpcSay += "|2|Sau khi nâng cấp (+" + levelTrangBi + ")\n";
                        for (ItemOption io : trangBiCanChuyenHoa.itemOptions) {
                            if (io.optionTemplate.id != 72) {
                                if (io.optionTemplate.id == 0 || io.optionTemplate.id == 47 || io.optionTemplate.id == 6 || io.optionTemplate.id == 7 || io.optionTemplate.id == 14 || io.optionTemplate.id == 22 || io.optionTemplate.id == 23) {
                                    NpcSay += "|1|" + io.getOptionString(chisogoc) + "\n";
                                } else {
                                    NpcSay += "|1|" + io.getOptionString() + "\n";
                                }
                            }
                        }
                        for (ItemOption io : trangBiGoc.itemOptions) {
                            if (io.optionTemplate.id != 72 && io.optionTemplate.id != 102 && io.optionTemplate.id != 107 && io.optionTemplate.id != 0 && io.optionTemplate.id != 47 && io.optionTemplate.id != 6 && io.optionTemplate.id != 7 && io.optionTemplate.id != 14 && io.optionTemplate.id != 22 && io.optionTemplate.id != 23) {
                                NpcSay += io.getOptionString() + "\n";
                            } else {
                            }
                        }
                        NpcSay += "Chuyển qua tất cả sao pha lê\n";
                        NpcSay += "|2|Cần 5000 ngọc";
                        baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, NpcSay,
                                "Nâng cấp\n5000\nngọc", "Từ chối");
                    }
                } else {
                    Service.getInstance().sendThongBaoOK(player, "Cần 1 trang bị có cấp từ [+4] và 1 trang bị không có cấp nhưng cao hơn 1 bậc");
                    return;
                }
                break;

            case TAY_SACH:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item sachTuyetKy = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (issachTuyetKy(item)) {
                            sachTuyetKy = item;
                        }
                    }
                    if (player.inventory.ruby < 5000) {
                        Service.getInstance().sendThongBaoOK(player, "Không đủ 5k hồng ngọc ");
                        return;
                    }
                    if (sachTuyetKy != null) {
                        String npcSay = "|2|Tẩy Sách Tuyệt Kỹ";
                        baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                "Đồng ý\n 5000 hồng ngọc", "Từ chối");
                    } else {
                        Service.getInstance().sendThongBaoOK(player, "Cần Sách Tuyệt Kỹ để tẩy");
                        return;
                    }
                } else {
                    Service.getInstance().sendThongBaoOK(player, "Cần Sách Tuyệt Kỹ để tẩy");
                    return;
                }
                break;

            case NANG_CAP_SACH_TUYET_KY:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item sachtk = null;
                    Item kimbam = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 1285 || item.template.id == 1287 || item.template.id == 1289) {
                            sachtk = item;
                        } else if (item.template.id == 1282) {
                            kimbam = item;
                        }
                    }
                    if (sachtk != null && kimbam != null && kimbam.quantity >= 10) {
                        player.combineNew.gemCombine = 5000;
                        player.combineNew.ratioCombine = 10;
                        String npcSay = "Nâng cấp " + sachtk.template.name + "\n|2|Cần 10 Kìm bấm giấy" + "\n";
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%"
                                + "\n Thành công sẽ tăng thêm 5% mỗi chỉ số đang có"
                                + "\n Riêng sức đánh và giáp chỉ tăng 3%";
                        if (player.inventory.ruby < player.combineNew.gemCombine) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không đủ 5k hồng ngọc!!", "Đóng");
                            return;
                        }
                        this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                "Nâng cấp\ncần " + player.combineNew.gemCombine + " hồng ngọc");
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Thiếu nguyên liệu rồi bạn ơi!", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Sách Tuyệt Kỹ bậc 1 và 10 Kìm bấm giấy", "Đóng");
                }
                break;
            case PHUC_HOI_SACH:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item sachTuyetKy = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (issachTuyetKy(item)) {
                            sachTuyetKy = item;
                        }
                    }
                    if (sachTuyetKy != null) {
                        String npcSay = "|2|Phục hồi " + sachTuyetKy.getName() + "\n"
                                + "Cần 10 cuốn sách cũ\n"
                                + "Phí phục hồi 10 triệu vàng";
                        baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                "Đồng ý", "Từ chối");
                    } else {
                        Service.getInstance().sendThongBaoOK(player, "Không tìm thấy vật phẩm");
                        return;
                    }
                } else {
                    Service.getInstance().sendThongBaoOK(player, "Không tìm thấy vật phẩm");
                    return;
                }
                break;
            case PHAN_RA_SACH:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item sachTuyetKy = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (issachTuyetKy(item)) {
                            sachTuyetKy = item;
                        }
                    }
                    if (sachTuyetKy != null) {
                        String npcSay = "|2|Phân rã sách\n"
                                + "Nhận lại 5 cuốn sách cũ\n"
                                + "Phí rã 10 triệu vàng";
                        baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                "Đồng ý", "Từ chối");
                    } else {
                        Service.getInstance().sendThongBaoOK(player, "Không tìm thấy vật phẩm");
                        return;
                    }
                } else {
                    Service.getInstance().sendThongBaoOK(player, "Không tìm thấy vật phẩm");
                    return;
                }
                break;

            // END _ SÁCH TUYỆT KỸ //
            case NHAP_NGOC_RONG:
                if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                    if (player.combineNew.itemsCombine.size() == 1) {
                        Item item = player.combineNew.itemsCombine.get(0);
                        if (item != null && item.isNotNullItem()) {
                            if ((item.template.id > 14 && item.template.id <= 20) && item.quantity >= 7) {
                                String npcSay = "|2|Con có muốn biến 7 " + item.template.name + " thành\n" + "1 viên "
                                        + ItemService.gI().getTemplate((short) (item.template.id - 1)).name + "\n"
                                        + "|7|Cần 7 " + item.template.name;
                                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Làm phép",
                                        "Từ chối");
                            } else if ((item.template.id == 14 && item.quantity >= 7)) {
                                String npcSay = "|2|Con có muốn biến 7 " + item.template.name + " thành\n" + "1 viên "
                                        + ItemService.gI().getTemplate((short) (925)).name + "\n" + "\n|7|Cần 7 "
                                        + item.template.name + "\n|7|Cần 500tr Vàng";
                                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Làm phép",
                                        "Từ chối");
                            } else if (item.template.id == 926 && item.quantity >= 7) {
                                String npcSay = "|2|Con có muốn biến 7 " + item.template.name + " thành\n" + "1 viên "
                                        + ItemService.gI().getTemplate((short) (925)).name + "\n" + "\n|7|Cần 7 "
                                        + item.template.name + "\n|7|Cần 500tr Vàng";
                                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Làm phép",
                                        "Từ chối");
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        "Cần 7 viên ngọc rồng 2 sao trở lên", "Đóng");
                            }
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 7 viên ngọc rồng 2 sao trở lên", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống",
                            "Đóng");
                }
                break;
            case NHAP_NGOC_RONG_TRANH:
                if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                    if (player.combineNew.itemsCombine.size() == 1) {
                        Item item = player.combineNew.itemsCombine.get(0);
                        if (item != null && item.isNotNullItem()) {
                            if ((item.template.id > 1558 && item.template.id <= 1564) && item.quantity >= 7) {
                                String npcSay = "|2|Con có muốn biến 7 " + item.template.name + " thành\n" + "1 viên "
                                        + ItemService.gI().getTemplate((short) (item.template.id - 1)).name + "\n"
                                        + "|7|Cần 7 " + item.template.name;
                                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Làm phép",
                                        "Từ chối");
                            } else if ((item.template.id == 1558 && item.quantity >= 7)) {
                                String npcSay = "|2|Con có muốn biến 7 " + item.template.name + " thành\n" + "1 viên "
                                        + ItemService.gI().getTemplate((short) (925)).name + "\n" + "\n|7|Cần 7 "
                                        + item.template.name + "\n|7|Cần 500tr Vàng";
                                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Làm phép",
                                        "Từ chối");
                            } else if (item.template.id == 926 && item.quantity >= 7) {
                                String npcSay = "|2|Con có muốn biến 7 " + item.template.name + " thành\n" + "1 viên "
                                        + ItemService.gI().getTemplate((short) (925)).name + "\n" + "\n|7|Cần 7 "
                                        + item.template.name + "\n|7|Cần 500tr Vàng";
                                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Làm phép",
                                        "Từ chối");
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        "Cần 7 viên ngọc rồng 2 sao trở lên", "Đóng");
                            }
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 7 viên ngọc rồng 2 sao trở lên", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống",
                            "Đóng");
                }
                break;
            case NANG_CAP_BONG_TAI_VO_CUC:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item bongtai = null;
                    Item manhvobt = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 921) {
                            bongtai = item;
                        } else if (item.template.id == 1130) {
                            manhvobt = item;
                        }
                    }

                    if (bongtai != null && manhvobt != null && manhvobt.quantity >= 9999) {
                        int level = 0;
                        for (ItemOption io : bongtai.itemOptions) {
                            if (io.optionTemplate.id == 72) {
                                level = io.param;
                                break;
                            }
                        }
                        int lvbt = lvbt(bongtai);
                        player.combineNew.goldCombine = 1_000_000_000;

                        int soluongMVBT = 0;
                        for (ItemOption io : manhvobt.itemOptions) {
                            if (io.optionTemplate.id == 31) {
                                soluongMVBT = io.param;
                                break;
                            }
                        }

                        String npcSay = "|2|Ngọc Vô Cực [+3]\n";
                        npcSay += "Tỉ lệ thành công: 40%" + "\n";
                        npcSay += "|7|Thành công -9999 Mảnh vỡ bông tai 3\n";
                        if (player.inventory.gold <= player.combineNew.goldCombine) {
                            npcSay += "|7|Cần 1 Tỷ vàng\n";
                        } else {
                            npcSay += "|2|Cần 1 Tỷ vàng\n";
                        }
                        npcSay += "|7|Thất bại -999 mảnh vỡ bông tai 3";

                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            if (player.combineNew.gemCombine <= player.inventory.gem) {
                                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                        "Nâng cấp", "Từ chối");
                            } else {
                                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }
                        } else {
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 bông tai Porata 2 và 9999 Mảnh vỡ bông tai", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 bông tai Porata 2 và 9999 Mảnh vỡ bông tai", "Đóng");
                }
                break;
            case MO_CHI_SO_BONG_TAI_VO_CUC:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item bongTai2 = null;
                    Item daNguSacPro = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 1015) {
                            bongTai2 = item;
                        } else if (item.template.id == 1503) {
                            daNguSacPro = item;
                        }
                    }
                    if (bongTai2 != null && daNguSacPro != null) {

                        player.combineNew.ratioCombine = RATIO_NANG_CAP;

                        String npcSay = "|2|Ngọc Vô Cực [+3]\n";

                        npcSay += "Tỉ lệ thành công: 50%" + "\n";
                        npcSay += "|2|Cần 1 Đá ngũ sắc pro\n";
                        npcSay += "|1|+1 Chỉ số ngẫu nhiên\n";
                        baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Nâng cấp", "Đóng");

                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Ngọc Vô Cực, X1 Đá ngũ sắc pro", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Ngọc Vô Cực, X1 Đá ngũ sắc pro", "Đóng");
                }
                break;
            case NANG_CAP_BONG_TAI:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item bongtai = null;
                    Item manhvobt = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 454) {
                            bongtai = item;
                        } else if (item.template.id == 933) {
                            manhvobt = item;
                        }
                    }

                    if (bongtai != null && manhvobt != null && manhvobt.quantity >= 999) {
                        int level = 0;
                        for (ItemOption io : bongtai.itemOptions) {
                            if (io.optionTemplate.id == 72) {
                                level = io.param;
                                break;
                            }
                        }
                        int lvbt = lvbt(bongtai);
                        player.combineNew.goldCombine = 200_000_000;

                        int soluongMVBT = 0;
                        for (ItemOption io : manhvobt.itemOptions) {
                            if (io.optionTemplate.id == 31) {
                                soluongMVBT = io.param;
                                break;
                            }
                        }

                        String npcSay = "|2|Bông tai Porata [+2]\n";
//                        for (ItemOption io : bongtai.itemOptions) {
//                            npcSay += io.getOptionString() + "\n";
//                        }
                        npcSay += "|7|Thành công -999 mảnh vỡ bông tai\n";
                        npcSay += "Tỉ lệ thành công: 60%" + "\n";
                        if (player.inventory.gold <= player.combineNew.goldCombine) {
                            npcSay += "|7|Cần 200 Tr vàng\n";
                        } else {
                            npcSay += "|2|Cần 200 Tr vàng\n";
                        }
                        npcSay += "|7|Thất bại -99 mảnh vỡ bông tai";

                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            if (player.combineNew.gemCombine <= player.inventory.gem) {
                                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                        "Nâng cấp\n200 Tr vàng", "Từ chối");
                            } else {
                                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }
                        } else {
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 bông tai Porata và 999 Mảnh vỡ bông tai", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 bông tai Porata và 999 Mảnh vỡ bông tai", "Đóng");
                }
                break;
            case MO_CHI_SO_BONG_TAI:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item bongTai = null;
                    Item manhHon = null;
                    Item daXanhLam = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 921) {
                            bongTai = item;
                        } else if (item.template.id == 934) {
                            manhHon = item;
                        } else if (item.template.id == 935) {
                            daXanhLam = item;
                        }
                    }
                    if (bongTai != null && manhHon != null && daXanhLam != null && manhHon.quantity >= 99) {

                        player.combineNew.ratioCombine = RATIO_NANG_CAP;

                        String npcSay = "|2|Bông tai Porata [+2]\n";

                        npcSay += "\n";

                        npcSay += "Tỉ lệ thành công: 90%" + "\n";
                        if (manhHon.quantity < 99) {
                            npcSay += "|7|Cần 99 Mảnh hồn bông tai\n";
                        } else {
                            npcSay += "Cần 99 Mảnh hồn bông tai\n";
                        }
                        npcSay += "|2|Cần 1 Đá xanh lam\n";
                        npcSay += "|1|+1 Chỉ số ngẫu nhiên\n";
                        baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Nâng cấp", "Đóng");

                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata cấp 2, X99 Mảnh hồn bông tai và 1 Đá xanh lam", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata cấp 2, X99 Mảnh hồn bông tai và 1 Đá xanh lam", "Đóng");
                }
                break;
            case NANG_CAP_SKH:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item isItemSKH = null;
                    Item isItemThanLinh = null;
                    Item daNangCap = null;

                    int levelTrangBi = 0;
                    player.combineNew.goldCombine = 2_000_000_000;
                    int goldCombie = player.combineNew.goldCombine;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (isDancskh(item)) {
                            isItemThanLinh = item;
                        }
                        if (item.template.id == 1314) {
                            daNangCap = item;
                        }
                        for (int i = 0; i < item.itemOptions.size(); i++) {
                            for (int option = 127; option <= 135; option++) {
                                if (checkHaveOption(item, i, option)) {
                                    isItemSKH = item;
                                    break;
                                }
                            }
                        }
                    }
                    if (isItemSKH != null) {
                        for (ItemOption io : isItemSKH.itemOptions) {
                            for (int option = 127; option <= 135; option++) {
                                if (io.optionTemplate.id == option) {
                                    levelTrangBi = io.param;
                                }
                            }
                        }
                    }
                    if (levelTrangBi >= 3) {
                        Service.getInstance().sendThongBaoOK(player, "SKH đã đạt level tối đa");
                        return;
                    }
                    if (daNangCap != null && isItemThanLinh != null && isItemSKH != null) {
                        String npcSay = "Vật phẩm SKH được nâng cấp: " + isItemSKH.getName() + "\n";
                        npcSay += "\n|1|Sau khi nâng cấp SKH sẽ được tăng 10% chỉ số kích hoạt\n";
                        npcSay += "\n|2|Tỷ lệ thành công: 50%";
                        npcSay += "\n|2|Cần " + Util.numberToMoney(goldCombie) + " vàng";
                        npcSay += "\n|7|Thất bại sẽ mất đồ Đá Thánh và 2 tỷ vàng";

                        baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Nâng cấp", "Từ chối");
                    } else {
                        Service.getInstance().sendThongBaoOK(player, "Cần một Đá Thánh, Đá nâng cấp kích hoạt và một món kích hoạt");
                        return;
                    }
                } else {
                    Service.getInstance().sendThongBaoOK(player, "Cần một món Đá Thánh, Đá nâng cấp kích hoạt và một món kích hoạt");
                    return;
                }
                break;
            case NANG_CAP_VAT_PHAM:
                if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 4) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 5).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ nâng cấp", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 14).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đá nâng cấp", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 987).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ nâng cấp", "Đóng");
                        break;
                    }
                    Item itemDo = null;
                    Item itemDNC = null;
                    Item itemDBV = null;
                    for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                        if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.get(j).template.id == 987) {
                                itemDBV = player.combineNew.itemsCombine.get(j);
                                continue;
                            }
                            if (player.combineNew.itemsCombine.get(j).template.type < 5) {
                                itemDo = player.combineNew.itemsCombine.get(j);
                            } else {
                                itemDNC = player.combineNew.itemsCombine.get(j);
                            }
                        }
                    }
                    if (isCoupleItemNangCapCheck(itemDo, itemDNC)) {
                        int level = 0;
                        for (ItemOption io : itemDo.itemOptions) {
                            if (io.optionTemplate.id == 72) {
                                level = io.param;
                                break;
                            }
                        }
                        if (level < MAX_LEVEL_ITEM) {
                            player.combineNew.goldCombine = getGoldNangCapDo(level);
                            player.combineNew.ratioCombine = (float) getTileNangCapDo(level);
                            player.combineNew.countDaNangCap = getCountDaNangCapDo(level);
                            player.combineNew.countDaBaoVe = (short) getCountDaBaoVe(level);
                            String npcSay = "|2|Hiện tại " + itemDo.template.name + " (+" + level + ")\n|0|";
                            for (ItemOption io : itemDo.itemOptions) {
                                if (io.optionTemplate.id != 72) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            String option = null;
                            int param = 0;
                            for (ItemOption io : itemDo.itemOptions) {
                                if (io.optionTemplate.id == 47
                                        || io.optionTemplate.id == 6
                                        || io.optionTemplate.id == 0
                                        || io.optionTemplate.id == 7
                                        || io.optionTemplate.id == 14
                                        || io.optionTemplate.id == 22
                                        || io.optionTemplate.id == 23) {
                                    option = io.optionTemplate.name;
                                    param = io.param + (io.param * 10 / 100);
                                    break;
                                }
                            }
                            npcSay += "|2|Sau khi nâng cấp (+" + (level + 1) + ")\n|7|"
                                    + option.replaceAll("#", String.valueOf(param))
                                    + "\n|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%\n"
                                    + (player.combineNew.countDaNangCap > itemDNC.quantity ? "|7|" : "|1|")
                                    + "Cần " + player.combineNew.countDaNangCap + " " + itemDNC.template.name
                                    + "\n" + (player.combineNew.goldCombine > player.inventory.gold ? "|7|" : "|1|")
                                    + "Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";

                            String daNPC = player.combineNew.itemsCombine.size() == 3 && itemDBV != null ? String.format("\nCần tốn %s đá bảo vệ", player.combineNew.countDaBaoVe) : "";
                            if ((level == 2 || level == 4 || level == 6) && !(player.combineNew.itemsCombine.size() == 3 && itemDBV != null)) {
                                npcSay += "\nNếu thất bại sẽ rớt xuống (+" + (level - 1) + ")";
                            }
                            if (player.combineNew.countDaNangCap > itemDNC.quantity) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaNangCap - itemDNC.quantity) + " " + itemDNC.template.name);
                            } else if (player.combineNew.goldCombine > player.inventory.gold) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + Util.numberToMoney((player.combineNew.goldCombine - player.inventory.gold)) + " vàng");
                            } else if (player.combineNew.itemsCombine.size() == 3 && Objects.nonNull(itemDBV) && itemDBV.quantity < player.combineNew.countDaBaoVe) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaBaoVe - itemDBV.quantity) + " đá bảo vệ");
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                        npcSay, "Nâng cấp\n" + Util.numberToMoney(player.combineNew.goldCombine) + " vàng" + daNPC, "Từ chối");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Trang bị của ngươi đã đạt cấp tối đa", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị và 1 loại đá nâng cấp", "Đóng");
                    }
                } else {
                    if (player.combineNew.itemsCombine.size() > 3) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cất đi con ta không thèm", "Đóng");
                        break;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị và 1 loại đá nâng cấp", "Đóng");
                }
                break;
            case DOI_VE_HUY_DIET:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    if (item.isNotNullItem() && item.template.id >= 555 && item.template.id <= 567) {
                        String ticketName = "Vé đổi " + (item.template.type == 0 ? "áo"
                                : item.template.type == 1 ? "quần"
                                        : item.template.type == 2 ? "găng" : item.template.type == 3 ? "giày" : "nhẫn")
                                + " hủy diệt";
                        String npcSay = "|6|Ngươi có chắc chắn muốn đổi\n|7|" + item.template.name + "\n";
                        for (ItemOption io : item.itemOptions) {
                            npcSay += "|2|" + io.getOptionString() + "\n";
                        }
                        npcSay += "|6|Lấy\n|7|" + ticketName + "\n|6|Với giá "
                                + Util.numberToMoney(COST_DOI_VE_DOI_DO_HUY_DIET) + " vàng không?";
                        if (player.inventory.gold >= COST_DOI_VE_DOI_DO_HUY_DIET) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Đổi",
                                    "Từ chối");
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Còn thiếu\n"
                                    + Util.numberToMoney(COST_DOI_VE_DOI_DO_HUY_DIET - player.inventory.gold) + " vàng",
                                    "Đóng");
                        }

                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Hãy chọn 1 trang bị thần linh ngươi muốn trao đổi", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Hãy chọn 1 trang bị thần linh ngươi muốn trao đổi", "Đóng");
                }
                break;
            case DAP_SET_KICH_HOAT:
                if (player.combineNew.itemsCombine.size() == 1 || player.combineNew.itemsCombine.size() == 2) {
                    List<Item> trangBiThanLinh = player.combineNew.itemsCombine.stream()
                            .filter(item -> item.isNotNullItem() && (item.template.id >= 555 && item.template.id <= 567))
                            .collect(Collectors.toList());
                    if (trangBiThanLinh.size() != player.combineNew.itemsCombine.size()) {
                        // Có item không phải thần linh
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Chỉ chọn trang bị thần linh", "Đóng");
                        break;
                    }

                    Item itemChinh = trangBiThanLinh.get(0); // Món đầu tiên sẽ là cơ sở đổi sang SKH

                    String npcSay = "|6|" + itemChinh.template.name + "\n";
                    if (itemChinh.itemOptions != null) {
                        for (ItemOption io : itemChinh.itemOptions) {
                            npcSay += "|2|" + io.getOptionString() + "\n";
                        }
                    }

                    int tile = (trangBiThanLinh.size() == 2) ? 100 : 50;
                    String itemNameSKH = getNameItemC0(itemChinh.template.gender, itemChinh.template.type);
                    npcSay += "Ngươi có muốn chuyển hóa thành\n";
                    npcSay += "|1|" + itemNameSKH + " (ngẫu nhiên kích hoạt)\n";
                    npcSay += "|7|Tỉ lệ thành công " + tile + "%\n";
                    npcSay += "|2|Cần " + Util.numberToMoney(COST_DAP_DO_KICH_HOAT) + " vàng";

                    if (player.inventory.gold >= COST_DAP_DO_KICH_HOAT) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                "Cần " + Util.numberToMoney(COST_DAP_DO_KICH_HOAT) + " vàng");
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay,
                                "Còn thiếu\n" + Util.numberToMoney(COST_DAP_DO_KICH_HOAT - player.inventory.gold) + " vàng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 hoặc 2 trang bị thần linh", "Đóng");
                }
                break;
            // case DOI_MANH_KICH_HOAT:
            // if (player.combineNew.itemsCombine.size() == 2 ||
            // player.combineNew.itemsCombine.size() == 3) {
            // Item nr1s = null, doThan = null, buaBaoVe = null;
            // for (Item it : player.combineNew.itemsCombine) {
            // if (it.template.id == 14) {
            // nr1s = it;
            // } else if (it.template.id == 2010) {
            // buaBaoVe = it;
            // } else if (it.template.id >= 555 && it.template.id <= 567) {
            // doThan = it;
            // }
            // }
            //
            // if (nr1s != null && doThan != null) {
            // int tile = 50;
            // String npcSay = "|6|Ngươi có muốn trao đổi\n|7|" + nr1s.template.name +
            // "\n|7|" + doThan.template.name
            // + "\n";
            // for (ItemOption io : doThan.itemOptions) {
            // npcSay += "|2|" + io.getOptionString() + "\n";
            // }
            // if (buaBaoVe != null) {
            // tile = 100;
            // npcSay += buaBaoVe.template.name
            // + "\n";
            // for (ItemOption io : buaBaoVe.itemOptions) {
            // npcSay += "|2|" + io.getOptionString() + "\n";
            // }
            // }
            //
            // npcSay += "|6|Lấy\n|7|Mảnh kích hoạt\n"
            // + "|1|Tỉ lệ " + tile + "%\n"
            // + "|6|Với giá " + Util.numberToMoney(COST_DOI_MANH_KICH_HOAT) + " vàng
            // không?";
            // if (player.inventory.gold >= COST_DOI_MANH_KICH_HOAT) {
            // this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
            // npcSay, "Đổi", "Từ chối");
            // } else {
            // this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
            // npcSay, "Còn thiếu\n"
            // + Util.numberToMoney(COST_DOI_MANH_KICH_HOAT - player.inventory.gold) + "
            // vàng", "Đóng");
            // }
            // } else {
            // this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang
            // bị thần linh và 1 viên ngọc rồng 1 sao", "Đóng");
            // }
            // } else {
            // this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang
            // bị thần linh và 1 viên ngọc rồng 1 sao", "Đóng");
            // }
            // break;
            case DAP_SET_KICH_HOAT_CAO_CAP:
                if (player.combineNew.itemsCombine.size() == 2) {
                    List<Item> items = player.combineNew.itemsCombine.stream()
                            .filter(Item::isNotNullItem)
                            .collect(Collectors.toList());

                    List<Item> itemsSKH = items.stream().filter(Item::isSKH).collect(Collectors.toList());

                    if (itemsSKH.size() != 2) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Thiếu đồ kích hoạt", "Đóng");
                        return;
                    }

                    Item itemChinh = itemsSKH.get(0); // Món chính
                    Item itemPhu = itemsSKH.get(1);   // Món phụ
                    // --- Thêm đoạn kiểm tra vị trí trùng ---
                    int viTriChinh = -1;
                    int viTriPhu = -1;
                    for (int i = 0; i < player.inventory.itemsBag.size(); i++) {
                        Item item = player.inventory.itemsBag.get(i);
                        if (item == itemChinh) {
                            viTriChinh = i;
                        }
                        if (item == itemPhu) {
                            viTriPhu = i;
                        }
                    }

                    if (viTriChinh == viTriPhu && viTriChinh != -1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Bố biết rồi nhé, Bug cái đcmm!", "Đóng");
                        return;
                    }
                    int type1 = itemChinh.template.type;
                    int type2 = itemPhu.template.type;
                    int idItemChinh = itemChinh.template.id;
                    int idItemPhu = itemPhu.template.id;
                    int genderChinh = itemChinh.template.gender;
                    int genderPhu = itemPhu.template.gender;

                    if (genderChinh != genderPhu) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Món kích hoạt phải cùng hành tinh để nâng cấp", "Đóng");
                        return;
                    }
                    if (type1 != type2) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Món kích hoạt phải cùng loại", "Đóng");
                        return;
                    }
                    int capDo = getCapDoKHVIP(genderChinh, type1, idItemChinh);
                    int capDoPhu = getCapDoKHVIP(genderPhu, type2, idItemPhu);
                    if (capDo != capDoPhu) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Hãy chọn 2 món kích hoạt ngang nhau", "Đóng");
                        return;
                    }
                    if (capDo >= 4) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Không thể nâng cấp món đồ này nữa", "Đóng");
                        return;
                    }

                    String itemNameNext = getNameIdItemKHVIP(genderChinh, type1, capDo + 1);

                    String npcSay = "2 Trang bị sẽ nâng lên " + itemNameNext
                            + " với chỉ số cao hơn\n|2|Tỷ lệ thành công: 100%\n Cần "
                            + Util.numberToMoney(COST_DAP_DO_KICH_HOAT_VIP) + " vàng";

                    if (player.inventory.gold >= COST_DAP_DO_KICH_HOAT_VIP) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Nâng cấp", "Đóng");
                    } else {
                        long thieu = COST_DAP_DO_KICH_HOAT_VIP - player.inventory.gold;
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                npcSay + "\n|1|Thiếu " + Util.numberToMoney(thieu) + " vàng", "Đóng");
                    }

                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần đúng 2 món đồ kích hoạt VIP để nâng cấp", "Đóng");
                }
                break;
            case GIA_HAN_CAI_TRANG:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item caitrang = null, vegiahan = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.isNotNullItem()) {
                            if (item.template.type == 5) {
                                caitrang = item;
                            } else if (item.template.id == 2022) {
                                vegiahan = item;
                            }
                        }
                    }
                    int expiredDate = 0;
                    boolean canBeExtend = true;
                    if (caitrang != null && vegiahan != null) {
                        for (ItemOption io : caitrang.itemOptions) {
                            if (io.optionTemplate.id == 93) {
                                expiredDate = io.param;
                            }
                            if (io.optionTemplate.id == 199) {
                                canBeExtend = false;
                            }
                        }
                        if (canBeExtend) {
                            if (expiredDate > 0) {
                                String npcSay = "|2|" + caitrang.template.name + "\n"
                                        + "Sau khi gia hạn +1 ngày \n Tỷ lệ thành công: 101% \n" + "|7|Cần 500tr vàng";
                                if (player.inventory.gold >= COST_GIA_HAN_CAI_TRANG) {
                                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                            "Gia hạn");
                                } else {
                                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay,
                                            "Còn thiếu\n"
                                            + Util.numberToMoney(player.inventory.gold - COST_GIA_HAN_CAI_TRANG)
                                            + " vàng");
                                }
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        "Cần cải trang có hạn sử dụng và thẻ gia hạn", "Đóng");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Vật phẩm này không thể gia hạn", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ta Cần cải trang có hạn sử dụng và thẻ gia hạn", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Ta Cần cải trang có hạn sử dụng và thẻ gia hạn", "Đóng");
                }
                break;
            case NANG_CAP_DO_THIEN_SU:
                if (player.combineNew.itemsCombine.size() != 2) {
                    this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta cần 1 công thức VIP và x999 Mảnh Thiên Sứ", "Đóng");
                    return;
                }
                Item ctVip = null,
                 manhTS = null;
                for (Item item : player.combineNew.itemsCombine) {
                    if (item.isNotNullItem()) {
                        if (item.isCongthucVip()) {
                            ctVip = item;
                        } else if (item.isManhTS() && item.quantity >= 999) {
                            manhTS = item;
                        }
                    }
                }
                if (player.inventory.gold < 1_000_000_000) {
                    this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn không đủ vàng (1 tỷ)", "Đóng");
                    return;
                }
                if (ctVip != null && manhTS != null) {
                    // Nội dung hiển thị menu nâng cấp
                    String npcSay = "|2|Chế tạo " + manhTS.typeNameManh() + " Thiên sứ " + ctVip.typeHanhTinh() + "\n"
                            + "|7|Mảnh ghép " + manhTS.quantity + "/999\n"
                            + "|7|Phí nâng cấp: 1 tỷ vàng";
                    this.whis.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                            npcSay, "Nâng cấp\n1 Tỷ vàng", "Từ chối");
                } else {
                    this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Công thức VIP hoặc không đủ 999 Mảnh Thiên Sứ", "Đóng");
                    return;
                }

                break;
        }
    }

    /**
     * Bắt đầu đập đồ - điều hướng từng loại đập đồ
     *
     * @param player
     */
    public void startCombine(Player player, int select) {
        if (Util.canDoWithTime(player.combineNew.lastTimeCombine, TIME_COMBINE)) {
            switch (player.combineNew.typeCombine) {
                case REMOVE_OPTION:
                    removeItemOptions(player);
                    break;
                case MO_NOI_TAI_ITEM:
                    monoitaiitem(player);
                    break;
                case NANG_CAP_PET:
                    setNangCapPet(player);
                    break;
                case NANG_PET:
                    setNangPet(player);
                    break;
                case PHAP_SU_HOA:
                    phapsuhoa(player);
                    break;
                case TAY_PHAP_SU:
                    tayphapsu(player);
                    break;
                case NANG_CHAN_MENH:
                    nangcaphonhoan(player);
                    break;
                case EP_SAO_TRANG_BI:
                    epSaoTrangBi(player);
                    break;
                case PHA_LE_HOA_CAI_TRANG:
                    phaLeHoaCaiTrang(player);
                    break;
                case PHA_LE_HOA_TRANG_BI:
                    switch (select) {
                        case 0:
                            phaLeHoaTrangBix100(player);
                            break;
                        case 1:
                            phaLeHoaTrangBix10(player);
                            break;
                        case 2:
                            phaLeHoaTrangBi(player);
                            break;
                    }
                    break;
                case NHAP_NGOC_RONG:
                    nhapNgocRong(player);
                    break;
                case NHAP_NGOC_RONG_TRANH:
                    nhapNgocRongTranh(player);
                    break;
                case NANG_CAP_VAT_PHAM:
                    nangCapVatPham(player);
                    break;
                case LAM_PHEP_NHAP_DA:
                    nhapda(player);
                    break;
                case AN_TRANG_BI:
                    antrangbi(player);
                    break;
                case NANG_CAP_SKH:
                    nangCapSKH(player);
                    break;
                case DOI_VE_HUY_DIET:
                    doiVeHuyDiet(player);
                    break;
                case DAP_SET_KICH_HOAT:
                    dapDoKichHoat(player);
                    break;

                case DAP_SET_KICH_HOAT_CAO_CAP:
                    dapDoKichHoatCaoCap(player);
                    break;
                case GIA_HAN_CAI_TRANG:
                    giaHanCaiTrang(player);
                    break;
                case NANG_CAP_DO_THIEN_SU:
                    nangCapDoThienSu(player);
                    break;
                case CHE_TAO_DO_THIEN_SU:
                    cheTaoDoTS(player);
                    break;
                case NANG_CAP_BONG_TAI:
                    nangCapBongTai(player);
                    break;
                case MO_CHI_SO_BONG_TAI:
                    moChiSoBongTai(player);
                    break;
                case NANG_CAP_BONG_TAI_VO_CUC:
                    nangCapBongTaiVoCuc(player);
                    break;
                case MO_CHI_SO_BONG_TAI_VO_CUC:
                    moChiSoBongTaiVoCuc(player);
                    break;
                case CHUYEN_HOA_BANG_VANG:
                    chuyenHoaTrangBiVang(player);
                    break;
                case CHUYEN_HOA_BANG_NGOC:
                    chuyenHoaTrangBiNgoc(player);
                    break;
                // START _ SÁCH TUYỆT KỸ //
                case GIAM_DINH_SACH:
                    giamDinhSach(player);
                    break;
                case TAY_SACH:
                    taySach(player);
                    break;
                case NANG_CAP_SACH_TUYET_KY:
                    nangCapSachTuyetKy(player);
                    break;
                case PHUC_HOI_SACH:
                    phucHoiSach(player);
                    break;
                case PHAN_RA_SACH:
                    phanRaSach(player);
                    break;
                // END _ SÁCH TUYỆT KỸ //
                case CUONG_HOA_LO_SPL:
                    CuongHoaLoSpl(player);
                    break;
            }
            player.iDMark.setIndexMenu(ConstNpc.IGNORE_MENU);
            player.combineNew.clearParamCombine();
            player.combineNew.lastTimeCombine = System.currentTimeMillis();
        }
    }

    private boolean phaLeHoaCaiTrang(Player player) {
        boolean flag = false;
        if (!player.combineNew.itemsCombine.isEmpty()) {
            Item item = player.combineNew.itemsCombine.get(0);
            int ruby = 10_000;
            if (isItemCaiTrang(item)) {
                int star = 0;
                ItemOption optionStar = null;
                ItemOption optionFailureCount = null; // Khai báo biến để lưu Option ID 250
                Item hoa = null;

                // Tìm Đá Thánh
                for (Item it : player.combineNew.itemsCombine) {
                    if (it.isNotNullItem()) {
                        if (it.template.id == 1502) {
                            hoa = it;
                            break;
                        }
                    }
                }

                // Tìm option 107 (số sao cải trang) và option 250 (số lần thất bại)
                for (ItemOption io : item.itemOptions) {
                    if (io.optionTemplate.id == 107) {
                        star = io.param;
                        optionStar = io;
                    } else if (io.optionTemplate.id == 250) {
                        optionFailureCount = io; // Tìm option ID 250 hiện có
                    }
                }

                // Kiểm tra nếu có Đá Thánh
                if (hoa != null) {
                    if (player.inventory.ruby < ruby) {
                        Service.getInstance().sendThongBao(player, "Không đủ ruby để hồng ngọc");
                        return false;
                    }

                    // Kiểm tra nếu số sao của cải trang còn chưa đạt tối đa
                    if (star < MAX_SAO_CAI_TRANG) {
                        player.inventory.ruby -= ruby;
                        InventoryService.gI().subQuantityItemsBag(player, hoa, 1);

                        // Tỉ lệ thành công 20%
                        if (Util.isTrue(20, 100)) {
                            if (optionStar == null) {
                                item.itemOptions.add(new ItemOption(107, 1));
                            } else {
                                optionStar.param++;
                            }
                            flag = true;
                            sendEffectSuccessCombine(player);
                        } else {
                            // Khi xịt, tăng số lần thất bại vào option 250
                            if (optionFailureCount == null) {
                                // Nếu chưa có option 250, thêm mới với giá trị ban đầu là 1
                                item.itemOptions.add(new ItemOption(250, 1));
                            } else {
                                // Nếu đã có, tăng giá trị param thêm 1
                                optionFailureCount.param++;
                            }
                            sendEffectFailCombine(player);
                        }

                        // Cập nhật thông tin item sau khi nâng cấp
                        InventoryService.gI().sendItemBags(player);
                        Service.getInstance().sendMoney(player);
                        reOpenItemCombine(player);
                    }
                }
            }
        }
        return flag;
    }

    private void CuongHoaLoSpl(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            int ruby = player.combineNew.gemCombine;
            if (player.inventory.ruby < ruby) {
                Service.getInstance().sendThongBao(player, "Không đủ ngọc hồng để thực hiện");
                return;
            }
            Item trangBi = null;
            Item duiduc = null;
            Item Hematite = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (isTrangBiPhaLeHoa(item)) {
                    trangBi = item;
                } else if (item.template.id == 1500) {
                    duiduc = item;
                } else if (item.template.id == 1499) {
                    Hematite = item;
                }
            }
            int star = 0; //sao pha lê đã ép
            int star2 = 0;
            if (trangBi != null && duiduc != null && duiduc.quantity >= 2 && Hematite != null && Hematite.quantity >= 20) {
                ItemOption optionStar = null;
                for (ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id == 102) {
                        star = io.param;
                        optionStar = io;
                    }
                }
                for (ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id == 234) {
                        star2 = io.param;
                        optionStar = io;
                    }
                }
                if (star > 5 && star2 == 0) {
                    player.inventory.ruby -= ruby;
                    int optionId = 234;
                    ItemOption option = null;
                    for (ItemOption io : trangBi.itemOptions) {
                        if (io.optionTemplate.id == optionId) {
                            option = io;
                            break;
                        }
                    }
                    if (option == null) {
                        trangBi.itemOptions.add(new ItemOption(234, 7));
                    }
                    InventoryService.gI().subQuantityItemsBag(player, Hematite, 20);
                    InventoryService.gI().subQuantityItemsBag(player, duiduc, 2);
                    sendEffectSuccessCombine(player);
                } else if (star > 6 && star2 == 7) {
                    player.inventory.ruby -= ruby;
                    int optionId = 234;
                    ItemOption option = null;
                    for (ItemOption io : trangBi.itemOptions) {
                        if (io.optionTemplate.id == optionId) {
                            option = io;
                            break;
                        }
                    }
                    if (option != null) {
                        option.param += 1;
                    }
                    InventoryService.gI().subQuantityItemsBag(player, Hematite, 20);
                    InventoryService.gI().subQuantityItemsBag(player, duiduc, 2);
                    sendEffectSuccessCombine(player);
                }
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void removeItemOptions(Player player) {
        Item itemCanCombine = checkItemCanCombine(player);
        if (Objects.isNull(itemCanCombine)) {
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Text 7", "Đóng");
            return;
        }
        if (player.inventory.ruby < ConstCombine.COST_REMOVE_OPTION) {
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không Đủ 20k Hồng Ngọc", "Đóng");
            return;
        }

        if (Util.isTrue(ConstCombine.RATIO_REMOVE_OPTION, 100)) {
            removeAndAddEmptyStartItem(itemCanCombine);
            this.sendEffectSuccessCombine(player);
        } else {
            this.sendEffectFailCombine(player);
        }
        player.inventory.ruby -= ConstCombine.COST_REMOVE_OPTION;
        Service.getInstance().sendMoney(player);
        InventoryService.gI().sendItemBags(player);
        reOpenItemCombine(player);
    }

    private Item removeAndAddEmptyStartItem(Item itemCanCombine) {
        int countStarBefore = itemCanCombine.getQuantityStar();
        System.out.println("countStarBefore" + countStarBefore);
        List<ItemOption> copy = new ArrayList<>(itemCanCombine.itemOptions);
        copy.removeIf(option -> ConstCombine.itemOptionsCanRemove.contains(option.optionTemplate.id));
        itemCanCombine.itemOptions = copy;
        int countStarCurrent = itemCanCombine.getQuantityStar();
        int diff = countStarBefore - countStarCurrent;
        diff = (diff == 0) ? countStarBefore : diff;

        for (int i = 0; i < diff; i++) {
            System.out.println("Ok");
            itemCanCombine.itemOptions.add(new ItemOption(102, 0));
        }
        return itemCanCombine;
    }

    private void monoitaiitem(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gem = player.combineNew.gemCombine;
            if (player.inventory.ruby < gem) {
                Service.getInstance().sendThongBao(player, "Không đủ hồng ngọc để thực hiện");
                return;
            }
            Item uchiha = null;
            Item datime = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.type == 5) {
                    uchiha = item;
                } else if (item.template.id == 1402) {
                    datime = item;
                }
            }
            if (uchiha != null && datime != null) {
                ItemOption option = null;
                for (ItemOption io : uchiha.itemOptions) {
                    if (io.optionTemplate.id == 210 || io.optionTemplate.id == 212 || io.optionTemplate.id == 213 || io.optionTemplate.id == 217 || io.optionTemplate.id == 218 || io.optionTemplate.id == 220) {
                        option = io;
                        break;
                    }
                }
                int[] nTai = new int[]{210, 212, 213, 217, 218, 220};

                int randomNoiTai = new Random().nextInt(nTai.length);
                if (option != null) {
                    uchiha.itemOptions.remove(option);
                    uchiha.itemOptions.add(new ItemOption(nTai[randomNoiTai], Util.nextInt(5, 30)));
                } else {
                    uchiha.itemOptions.add(new ItemOption(nTai[randomNoiTai], Util.nextInt(5, 30)));
                }

                InventoryService.gI().subQuantityItemsBag(player, datime, 1);
                sendEffectSuccessCombine(player);
                player.inventory.ruby -= gem;
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void setNangPet(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            long star = 0;
            int gold = 20000;

            if (player.inventory.ruby < gold) {
                Service.getInstance().sendThongBao(player, "Không đủ 20k hồng ngọc thực hiện");
                return;
            }
            Item trangBi = player.combineNew.itemsCombine.get(0);
            Item trangBiGod = player.combineNew.itemsCombine.get(1);
            Item dalua = player.combineNew.itemsCombine.get(2);
            for (Item item : player.combineNew.itemsCombine) {
                if (ispet(item)) {
                    trangBi = item;
                }
                if (isdanspro(item)) {
                    dalua = item;
                }
            }
            if (trangBi != null && isTrangBiGod(trangBiGod) && dalua != null && dalua.quantity >= 25) {
                ItemOption optionDaKham = null;
                for (ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id == 72) {
                        star = io.param;
                        optionDaKham = io;
                    }
                }
                if (star < MAX_LEVEL_PET) {
                    player.inventory.ruby -= gold;
                    int[] optionIds = {50, 77, 103, 14, 5, 101};
                    int param;
                    if (trangBiGod.template.type == 2 || trangBiGod.template.type == 4) {
                        param = 2;
                    } else {
                        param = 1;
                    }
                    ItemOption option = null;
                    for (ItemOption io : trangBi.itemOptions) {
                        for (int id : optionIds) {
                            if (io.optionTemplate.id == id) {
                                option = io;
                                break;
                            }
                        }
                        if (option != null) {
                            break;
                        }
                    }
                    //byte ratio = (optionDaKham != null && optionDaKham.param > 4) ? (byte) 1 : 1;
                    byte ratio = (optionDaKham != null && optionDaKham.param > 4) ? (byte) 1 : 1;
                    if (Util.isTrue(player.combineNew.ratioCombine, 100 * ratio)) {
                        if (option != null) {
                            for (int id : optionIds) {
                                for (ItemOption io : trangBi.itemOptions) {
                                    if (io.optionTemplate.id == id) {
                                        io.param += param;
                                        break;
                                    }
                                }
                            }
                        } else {
                            trangBi.itemOptions.add(new ItemOption(optionIds[0], param));
                            trangBi.itemOptions.add(new ItemOption(optionIds[1], param));
                            trangBi.itemOptions.add(new ItemOption(optionIds[2], param));
                            trangBi.itemOptions.add(new ItemOption(optionIds[3], param));
                            trangBi.itemOptions.add(new ItemOption(optionIds[4], param));
                            trangBi.itemOptions.add(new ItemOption(optionIds[5], param));
                        }
                        if (optionDaKham != null) {
                            optionDaKham.param++;
                        } else {
                            trangBi.itemOptions.add(new ItemOption(72, 1));
                        }
                        sendEffectSuccessCombine(player);
                        if (optionDaKham != null && optionDaKham.param >= 3) {
                            ServerNotify.gI().notify("Chúc mừng " + player.name
                                    + "thành công nâng cấp " + trangBi.template.name + " lên cấp " + optionDaKham.param);
                        }
                    } else {
                        sendEffectFailCombine(player);
                    }
                }
                InventoryService.gI().removeItemBag(player, trangBiGod);
                InventoryService.gI().subQuantityItemsBag(player, dalua, 25);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void setNangCapPet(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            long star = 0;
            int gold = 5000;
            int ngusac = player.combineNew.ngusacCombine;
            Item dangusac = InventoryService.gI().findItemBagByTemp(player, 457);
            if (dangusac.quantity < ngusac) {
                Service.getInstance().sendThongBao(player, "Không đủ Thỏi vàng để thực hiện");
                return;
            }
            if (player.inventory.ruby < gold) {
                Service.getInstance().sendThongBao(player, "Không đủ hồng ngọc thực hiện");
                return;
            }
            Item trangBi = player.combineNew.itemsCombine.get(0);
            Item trangBiGod = player.combineNew.itemsCombine.get(1);
            Item dalua = player.combineNew.itemsCombine.get(2);
            for (Item item : player.combineNew.itemsCombine) {
                if (isLinhThu(item)) {
                    trangBi = item;
                }
                if (isdalua(item)) {
                    dalua = item;
                }
            }
            if (trangBi != null && isTrangBiGod(trangBiGod) && dalua != null && dalua.quantity >= 10) {
                ItemOption optionDaKham = null;
                for (ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id == 72) {
                        star = io.param;
                        optionDaKham = io;
                    }
                }
                if (star < MAX_LEVEL_PET) {
                    player.inventory.ruby -= gold;
                    dangusac.quantity -= ngusac;
                    int[] optionIds = {50, 77, 103, 14, 5, 101};
                    int param;
                    if (trangBiGod.template.type == 2 || trangBiGod.template.type == 4) {
                        param = 2;
                    } else {
                        param = 1;
                    }
                    ItemOption option = null;
                    for (ItemOption io : trangBi.itemOptions) {
                        for (int id : optionIds) {
                            if (io.optionTemplate.id == id) {
                                option = io;
                                break;
                            }
                        }
                        if (option != null) {
                            break;
                        }
                    }
                    //byte ratio = (optionDaKham != null && optionDaKham.param > 4) ? (byte) 1 : 1;
                    byte ratio = (optionDaKham != null && optionDaKham.param > 4) ? (byte) 1 : 1;
                    if (Util.isTrue(player.combineNew.ratioCombine, 100 * ratio)) {
                        if (option != null) {
                            for (int id : optionIds) {
                                for (ItemOption io : trangBi.itemOptions) {
                                    if (io.optionTemplate.id == id) {
                                        io.param += param;
                                        break;
                                    }
                                }
                            }
                        } else {
                            trangBi.itemOptions.add(new ItemOption(optionIds[0], param));
                            trangBi.itemOptions.add(new ItemOption(optionIds[1], param));
                            trangBi.itemOptions.add(new ItemOption(optionIds[2], param));
                            trangBi.itemOptions.add(new ItemOption(optionIds[3], param));
                            trangBi.itemOptions.add(new ItemOption(optionIds[4], param));
                            trangBi.itemOptions.add(new ItemOption(optionIds[5], param));
                        }
                        if (optionDaKham != null) {
                            optionDaKham.param++;
                        } else {
                            trangBi.itemOptions.add(new ItemOption(72, 1));
                        }
                        sendEffectSuccessCombine(player);
                        if (optionDaKham != null && optionDaKham.param >= 3) {
                            ServerNotify.gI().notify("Chúc mừng " + player.name
                                    + "thành công nâng cấp " + trangBi.template.name + " lên cấp " + optionDaKham.param);
                        }
                    } else {
                        sendEffectFailCombine(player);
                    }
                }
                InventoryService.gI().removeItemBag(player, trangBiGod);
                InventoryService.gI().subQuantityItemsBag(player, dalua, 10);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void phapsuhoa(Player player) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (player.inventory.gold < GOLD_PHAP_SU_HOA) {
                Service.getInstance().sendThongBao(player, "Ngươi còn thiếu " + Util.numberToMoney(GOLD_PHAP_SU_HOA - player.inventory.gold) + " Vàng");
                return;
            }

            if (!player.combineNew.itemsCombine.isEmpty()) {
                Item itemOption = null;
                Item daPhapSu = null;
                for (Item item : player.combineNew.itemsCombine) {
                    if (item.isNotNullItem()) {
                        if (isTrangBiPhapsu(item)) {
                            itemOption = item;
                        } else if (item.template.id == 1379) {
                            daPhapSu = item;
                        }
                    }
                }

                int star = 0;
                short[] chiso = {201, 202, 203};
                byte randomDo = (byte) new Random().nextInt(chiso.length);
                int lvchiso = 0;
                int cap = 1;
                ItemOption optionStar = null;
                int check = chiso[randomDo];
                int run = 0;
                int lvcheck = 0;
                if (itemOption.itemOptions != null) {
                    for (ItemOption io : itemOption.itemOptions) {
                        if (io.optionTemplate.id == 201 || io.optionTemplate.id == 202 || io.optionTemplate.id == 203) {
                            star = io.param;
                            optionStar = io;
                            break;
                        }
                    }
                    if (itemOption != null && itemOption.itemOptions != null) {
                        for (ItemOption io2 : itemOption.itemOptions) {
                            if (io2.optionTemplate.id == 205) {
                                lvcheck = io2.param;
                                break;
                            }
                        }
                    }

                }
                if (itemOption != null && itemOption.isNotNullItem() && daPhapSu != null && daPhapSu.isNotNullItem() && (daPhapSu.template.id == 1379) && daPhapSu.quantity >= 10) {
                    player.combineNew.ratioCombine = getRatioPhapSuHoa(lvcheck);
                    if (lvcheck < 8) {
                        //Trừ vàng
                        player.inventory.gold -= GOLD_PHAP_SU_HOA;
                        Service.getInstance().sendMoney(player);
                        if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                            if (optionStar == null) {
                                itemOption.itemOptions.add(new ItemOption(205, cap));
                                if (check == 204) {
                                    itemOption.itemOptions.add(new ItemOption(check, lvchiso + 2));
                                } else {
                                    itemOption.itemOptions.add(new ItemOption(check, lvchiso + 3));
                                }
                                sendEffectSuccessCombine(player);
                                InventoryService.gI().subQuantityItemsBag(player, daPhapSu, 10);
                                InventoryService.gI().sendItemBags(player);
                                reOpenItemCombine(player);
                            } else {
                                if (itemOption.itemOptions != null) {
                                    for (ItemOption ioo : itemOption.itemOptions) {
                                        if (ioo.optionTemplate.id == 205) {
                                            ioo.param++;
                                        }
                                        if ((ioo.optionTemplate.id == 201 || ioo.optionTemplate.id == 202 || ioo.optionTemplate.id == 203) && (ioo.optionTemplate.id == check)) {
                                            if (check == 204) {
                                                ioo.param += 2;
                                            } else {
                                                ioo.param += 3;
                                            }
                                            sendEffectSuccessCombine(player);
                                            InventoryService.gI().subQuantityItemsBag(player, daPhapSu, 10);
                                            InventoryService.gI().sendItemBags(player);
                                            reOpenItemCombine(player);
                                            run = 1;
                                            break;
                                        } else {
                                            run = 2;
                                        }
                                    }
                                }
                                if (run == 2) {
                                    if (check == 197) {
                                        itemOption.itemOptions.add(new ItemOption(check, lvchiso + 2));
                                    } else {
                                        itemOption.itemOptions.add(new ItemOption(check, lvchiso + 3));
                                    }
                                    sendEffectSuccessCombine(player);
                                    InventoryService.gI().subQuantityItemsBag(player, daPhapSu, 10);
                                    InventoryService.gI().sendItemBags(player);
                                    reOpenItemCombine(player);
                                }
                            }
                        } else {
                            sendEffectFailCombine(player);
                            InventoryService.gI().subQuantityItemsBag(player, daPhapSu, 10);
                            InventoryService.gI().sendItemBags(player);
                            reOpenItemCombine(player);
                        }

                    } else {
                        Service.getInstance().sendThongBao(player, "Pháp sư hóa đã đạt cấp cao nhất !!!");
                    }
                }
            }
        }
    }

    private void tayphapsu(Player player) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (!player.combineNew.itemsCombine.isEmpty()) {
                Item itemOption = null;
                Item buaPhapSu = null;
                for (Item item : player.combineNew.itemsCombine) {
                    if (item.isNotNullItem()) {
                        if (isTrangBiPhapsu(item)) {
                            itemOption = item;
                        } else if (item.template.id == 1380) {
                            buaPhapSu = item;
                        }
                    }
                }
                ItemOption optionStar = null;

                for (ItemOption io : itemOption.itemOptions) {
                    if (io.optionTemplate.id == 201 || io.optionTemplate.id == 202 || io.optionTemplate.id == 203 || io.optionTemplate.id == 205) {
                        optionStar = io;
                        break;
                    }
                }
                if (isTrangBiPhapsu(itemOption)) {
                    if (itemOption != null && itemOption.isNotNullItem() && buaPhapSu != null && buaPhapSu.isNotNullItem() && buaPhapSu.template.id == 1380 && buaPhapSu.quantity > 0) {
                        if (optionStar == null) {
                            Service.getInstance().sendThongBao(player, "Có gì đâu mà tẩy !!!");
                        } else {

                            if (itemOption.itemOptions != null) {

                                Iterator<ItemOption> iterator = itemOption.itemOptions.iterator();
                                while (iterator.hasNext()) {
                                    ItemOption ioo = iterator.next();
                                    if (ioo.optionTemplate.id == 201 || ioo.optionTemplate.id == 202 || ioo.optionTemplate.id == 203 || ioo.optionTemplate.id == 205) {
                                        iterator.remove();
                                    }
                                }

                            }
                            //item.itemOptions.add(new Item.ItemOption(73 , 1));  
                            sendEffectSuccessCombine(player);
                            InventoryService.gI().subQuantityItemsBag(player, buaPhapSu, 1);
                            InventoryService.gI().sendItemBags(player);
                            reOpenItemCombine(player);

                        }
                    }

                } else {
                    Service.getInstance().sendThongBao(player, "Thiếu vật phẩm gòi !!!");
                }

            }
        }
    }

    private void nangcaphonhoan(Player player) {

        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 35).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu chân mệnh");
            return;
        }

        if (player.combineNew.itemsCombine.size() != 3) {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(player) < 1) {
            Service.getInstance().sendThongBao(player, "Hành trang không đủ ô trống!");
            return;
        }
        Item itemChanMenh = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 35).findFirst().get();
        Item da = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 1450).findAny().get();
        Item daLua = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 1452).findAny().get();

        if (itemChanMenh.quantity < 1) {
            Service.getInstance().sendThongBaoOK(player, "Thiếu chân mệnh!");
            return;
        }
        if (itemChanMenh.template.id >= 1415) {
            Service.getInstance().sendThongBaoOK(player, "Đã đạt cấp tối đa!!");
            return;
        }
        if (da.quantity < 30) {
            Service.getInstance().sendThongBaoOK(player, "Thiếu Đá cam");
            return;
        }
        if (da.template.id != 1450) {
            Service.getInstance().sendThongBaoOK(player, "Thiếu Đá cam");
            return;
        }
        if (daLua.quantity < 2) {
            Service.getInstance().sendThongBaoOK(player, "Thiếu Đá Lửa");
            return;
        }
        if (daLua.template.id != 1452) {
            Service.getInstance().sendThongBaoOK(player, "Thiếu Đá Lửa");
            return;
        }
        if (Util.isTrue(getTileNangHonHoan(itemChanMenh.template.id), 100)) {

            Item chanMenh = ItemService.gI().createNewItem((short) (itemChanMenh.template.id + 1));

            // Tách option 93 ra trước
            ItemOption optHSD = null;
            List<ItemOption> optionsToCopy = new ArrayList<>();
            // Tìm option 93 nếu có
            for (ItemOption opt : itemChanMenh.itemOptions) {
                if (opt.optionTemplate.id == 93) {
                    optHSD = opt;
                } else {
                    optionsToCopy.add(new ItemOption(opt.optionTemplate.id, opt.param));
                }
            }
            // Giữ nguyên toàn bộ option cũ
            chanMenh.itemOptions.addAll(optionsToCopy);

            // Ngẫu nhiên chọn 1 trong 3 option đặc biệt
            int[] optionIds = {50, 77, 103};
            int selectedId = optionIds[Util.nextInt(0, optionIds.length - 1)];

            // Tìm xem option đã có chưa
            ItemOption existed = chanMenh.itemOptions.stream()
                    .filter(io -> io.optionTemplate.id == selectedId)
                    .findFirst()
                    .orElse(null);

            if (existed != null) {
                // Nếu đã có thì cộng dồn param
                existed.param += 3;
            } else {
                // Nếu chưa có thì thêm mới
                chanMenh.itemOptions.add(new ItemOption(selectedId, 3));
            }
            // Thêm lại vào cuối nếu có
            if (optHSD != null) {
                chanMenh.itemOptions.add(optHSD);
            }
            InventoryService.gI().addItemBag(player, chanMenh, -1);
            sendEffectSuccessCombine(player);
            InventoryService.gI().subQuantityItemsBag(player, da, 30);
            InventoryService.gI().subQuantityItemsBag(player, daLua, 2);
            InventoryService.gI().subQuantityItemsBag(player, itemChanMenh, 1);

        } else {
            InventoryService.gI().subQuantityItemsBag(player, da, 30);
            InventoryService.gI().subQuantityItemsBag(player, daLua, 2);
            sendEffectFailCombine(player);

        }
        Service.getInstance().sendMoney(player);
        InventoryService.gI().sendItemBags(player);
        reOpenItemCombine(player);
        player.combineNew.itemsCombine.clear();
    }

    private void nangCapBongTaiVoCuc(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            Item bongtai = null;
            Item manhvobt3 = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 921) {
                    bongtai = item;
                } else if (item.template.id == 1130) {
                    manhvobt3 = item;
                }
            }
            if (bongtai != null && manhvobt3 != null && manhvobt3.quantity >= 9999) {
                int level = 0;
                for (ItemOption io : bongtai.itemOptions) {
                    if (io.optionTemplate.id == 72) {
                        level = io.param;
                        break;
                    }
                }
                if (level < 3) {
                    int lvbt = lvbt(bongtai);
                    if (Util.isTrue(40, 100)) {
                        bongtai.template = ItemService.gI().getTemplate(getidbtsaukhilencap(lvbt));
                        bongtai.itemOptions.clear();
                        bongtai.itemOptions.add(new ItemOption(72, lvbt + 1));
                        sendEffectSuccessCombine(player);
                        InventoryService.gI().subQuantityItemsBag(player, manhvobt3, 9999);
                    } else {
                        sendEffectFailCombine(player);
                        InventoryService.gI().subQuantityItemsBag(player, manhvobt3, 999);
                    }
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void nangCapBongTai(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            Item bongtai = null;
            Item manhvobt = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 454) {
                    bongtai = item;
                } else if (item.template.id == 933) {
                    manhvobt = item;
                }
            }
//            System.out.println("bongtai object: " + bongtai);
//            System.out.println("Combine object: " + manhvobt);
            if (bongtai != null && manhvobt != null && manhvobt.quantity >= 999) {
                int level = 0;
                for (ItemOption io : bongtai.itemOptions) {
                    if (io.optionTemplate.id == 72) {
                        level = io.param;
                        break;
                    }
                }
                if (level < 2) {
                    int lvbt = lvbt(bongtai);
                    player.inventory.gold -= gold;
                    if (Util.isTrue(60, 100)) {
                        bongtai.template = ItemService.gI().getTemplate(getidbtsaukhilencap(lvbt));
                        bongtai.itemOptions.clear();
                        bongtai.itemOptions.add(new ItemOption(72, lvbt + 1));
                        InventoryService.gI().subQuantityItemsBag(player, manhvobt, 999);
                        sendEffectSuccessCombine(player);
                    } else {
                        InventoryService.gI().subQuantityItemsBag(player, manhvobt, 99);
                        sendEffectFailCombine(player);
                    }
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void antrangbi(Player player) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (!player.combineNew.itemsCombine.isEmpty()) {
                Item item = player.combineNew.itemsCombine.get(0);
                Item dangusac = player.combineNew.itemsCombine.get(1);
                int star = 0;
                ItemOption optionStar = null;
                for (ItemOption io : item.itemOptions) {
                    if (io.optionTemplate.id == 34 || io.optionTemplate.id == 35 || io.optionTemplate.id == 35) {
                        star = io.param;
                        optionStar = io;
                        break;
                    }
                }
                if (item != null && item.isNotNullItem() && dangusac != null && dangusac.isNotNullItem() && (dangusac.template.id == 1403 || dangusac.template.id == 1404 || dangusac.template.id == 1405) && dangusac.quantity >= 99) {
                    if (optionStar == null) {
                        if (dangusac.template.id == 1403) {
                            item.itemOptions.add(new ItemOption(34, 1));
                            sendEffectSuccessCombine(player);
                        } else if (dangusac.template.id == 1404) {
                            item.itemOptions.add(new ItemOption(35, 1));
                            sendEffectSuccessCombine(player);
                        } else if (dangusac.template.id == 1405) {
                            item.itemOptions.add(new ItemOption(36, 1));
                            sendEffectSuccessCombine(player);
                        }
//                    InventoryService.gI().addItemBag(player, item, 0);
                        InventoryService.gI().subQuantityItemsBag(player, dangusac, 99);
                        InventoryService.gI().sendItemBags(player);
                        reOpenItemCombine(player);
//                    sendEffectCombineDB(player, item.template.iconID);
                    } else {
                        Service.getInstance().sendThongBao(player, "Trang bị của bạn có ấn rồi mà !!!");
                    }
                }
            }
        }
    }

    // START _ SÁCH TUYỆT KỸ
    private void giamDinhSach(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {

            Item sachTuyetKy = null;
            Item buaGiamDinh = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (issachTuyetKy(item)) {
                    sachTuyetKy = item;
                } else if (item.template.id == 1283) {
                    buaGiamDinh = item;
                }
            }
            if (sachTuyetKy != null && buaGiamDinh != null) {
                Item sachTuyetKy_2 = ItemService.gI().createNewItem((short) sachTuyetKy.template.id);
                if (checkHaveOption(sachTuyetKy, 0, 229)) {
                    int tyle = new Random().nextInt(10);
                    int rdUp = Util.nextInt(0, 7);
                    switch (rdUp) {
                        case 0:
                            sachTuyetKy_2.itemOptions.add(new ItemOption(50, Util.nextInt(5, 10)));
                            break;
                        case 1:
                            sachTuyetKy_2.itemOptions.add(new ItemOption(77, Util.nextInt(5, 10)));
                            break;
                        case 2:
                            sachTuyetKy_2.itemOptions.add(new ItemOption(103, Util.nextInt(5, 10)));
                            break;
                        case 3:
                            sachTuyetKy_2.itemOptions.add(new ItemOption(5, Util.nextInt(5, 10)));
                            break;
                        case 4:
                            sachTuyetKy_2.itemOptions.add(new ItemOption(94, Util.nextInt(5, 10)));
                            break;
                        case 5:
                            sachTuyetKy_2.itemOptions.add(new ItemOption(14, Util.nextInt(5, 10)));
                            break;

                    }
                    sendEffectSuccessCombine(player);
                    InventoryService.gI().addItemBag(player, sachTuyetKy_2, 1);
                    InventoryService.gI().subQuantityItemsBag(player, sachTuyetKy, 1);
                    InventoryService.gI().subQuantityItemsBag(player, buaGiamDinh, 1);
                    InventoryService.gI().sendItemBags(player);
                    reOpenItemCombine(player);
                } else {
                    Service.getInstance().sendThongBao(player, "Còn cái nịt mà giám");
                    return;
                }
            }
        }
    }

    private void nangCapSachTuyetKy(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int ruby = player.combineNew.gemCombine;
            if (player.inventory.ruby < ruby) {
                Service.getInstance().sendThongBao(player, "Không đủ hồng ngọc để thực hiện");
                return;
            }
            Item sachtk = null;
            Item kimkep = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 1285 || item.template.id == 1287 || item.template.id == 1289) {
                    sachtk = item;
                } else if (item.template.id == 1282) {
                    kimkep = item;
                }
            }
            if (sachtk != null && kimkep != null && kimkep.quantity >= 10) {
                player.inventory.ruby -= ruby;
                ItemOption option = null;
                ItemOption option2 = null;
                ItemOption option3 = null;
                ItemOption option4 = null;
                ItemOption option5 = null;
                ItemOption option6 = null;
                InventoryService.gI().subQuantityItemsBag(player, kimkep, 10);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {

                    for (ItemOption io : sachtk.itemOptions) {
                        if (io.optionTemplate.id == 50) {
                            option = io;
                        }
                        if (io.optionTemplate.id == 77) {
                            option2 = io;
                        }
                        if (io.optionTemplate.id == 103) {
                            option3 = io;
                        }
                        if (io.optionTemplate.id == 108) {
                            option4 = io;
                        }
                        if (io.optionTemplate.id == 94) {
                            option5 = io;
                        }
                        if (io.optionTemplate.id == 14) {
                            option6 = io;
                        }
                    }
                    if (option != null) {
                        option.param += 3;
                    }
                    if (option2 != null) {
                        option2.param += 5;
                    }
                    if (option3 != null) {
                        option3.param += 5;
                    }
                    if (option4 != null) {
                        option4.param += 5;
                    }
                    if (option5 != null) {
                        option5.param += 3;
                    }
                    if (option6 != null) {
                        option6.param += 5;
                    }
                    sachtk.template = ItemService.gI().getTemplate(sachtk.template.id + 1);
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void phucHoiSach(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item cuonSachCu = InventoryService.gI().findItemBagByTemp(player, (short) 1284);
            int goldPhanra = 10_000_000;
            Item sachTuyetKy = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (issachTuyetKy(item)) {
                    sachTuyetKy = item;
                }
            }
            if (sachTuyetKy != null) {
                int doBen = 0;
                ItemOption optionLevel = null;
                for (ItemOption io : sachTuyetKy.itemOptions) {
                    if (io.optionTemplate.id == 231) {
                        doBen = io.param;
                        optionLevel = io;
                        break;
                    }
                }
                if (cuonSachCu == null) {
                    Service.getInstance().sendThongBaoOK(player, "Cần sách tuyệt kỹ và 10 cuốn sách cũ");
                    return;
                }
                if (cuonSachCu.quantity < 10) {
                    Service.getInstance().sendThongBaoOK(player, "Cần sách tuyệt kỹ và 10 cuốn sách cũ");
                    return;
                }
                if (player.inventory.gold < goldPhanra) {
                    Service.getInstance().sendThongBao(player, "Không có tiền mà đòi phục hồi à");
                    return;
                }
                if (doBen != 1000) {
                    for (int i = 0; i < sachTuyetKy.itemOptions.size(); i++) {
                        if (sachTuyetKy.itemOptions.get(i).optionTemplate.id == 231) {
                            sachTuyetKy.itemOptions.get(i).param = 1000;
                            break;
                        }
                    }
                    player.inventory.gold -= 10_000_000;
                    InventoryService.gI().subQuantityItemsBag(player, cuonSachCu, 10);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    sendEffectSuccessCombine(player);
                    reOpenItemCombine(player);
                } else {
                    Service.getInstance().sendThongBao(player, "Còn dùng được phục hồi ăn cứt à");
                    return;
                }
            }
        }
    }

    private void phanRaSach(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item cuonSachCu = ItemService.gI().createNewItem((short) 1284, 5);
            int goldPhanra = 10_000_000;
            Item sachTuyetKy = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (issachTuyetKy(item)) {
                    sachTuyetKy = item;
                }
            }
            if (sachTuyetKy != null) {
                int luotTay = 0;
                ItemOption optionLevel = null;
                for (ItemOption io : sachTuyetKy.itemOptions) {
                    if (io.optionTemplate.id == 230) {
                        luotTay = io.param;
                        optionLevel = io;
                        break;
                    }
                }
                if (player.inventory.gold < goldPhanra) {
                    Service.getInstance().sendThongBao(player, "Không có tiền mà đòi phân rã à");
                    return;
                }
                if (luotTay == 0) {

                    player.inventory.gold -= goldPhanra;
                    InventoryService.gI().subQuantityItemsBag(player, sachTuyetKy, 1);
                    InventoryService.gI().addItemBag(player, cuonSachCu, 999);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    sendEffectSuccessCombine(player);
                    reOpenItemCombine(player);

                } else {
                    Service.getInstance().sendThongBao(player, "Còn dùng được phân rã ăn cứt à");
                    return;
                }
            }
        }
    }

    private void taySach(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item sachTuyetKy = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (issachTuyetKy(item)) {
                    sachTuyetKy = item;
                }
            }
            if (sachTuyetKy != null) {
                int luotTay = 0;
                ItemOption optionLevel = null;
                for (ItemOption io : sachTuyetKy.itemOptions) {
                    if (io.optionTemplate.id == 230) {
                        luotTay = io.param;
                        optionLevel = io;
                        break;
                    }
                }
                if (luotTay == 0) {
                    Service.getInstance().sendThongBao(player, "Còn cái nịt mà tẩy");
                    return;
                }
                Item sachTuyetKy_2 = ItemService.gI().createNewItem((short) sachTuyetKy.template.id);
                if (checkHaveOption(sachTuyetKy, 0, 229)) {
                    Service.getInstance().sendThongBao(player, "Còn cái nịt mà tẩy");
                    return;
                }
                int tyle = new Random().nextInt(10);
                for (int i = 1; i < sachTuyetKy.itemOptions.size(); i++) {
                    if (sachTuyetKy.itemOptions.get(i).optionTemplate.id == 230) {
                        sachTuyetKy.itemOptions.get(i).param -= 1;
                    }
                }
                sachTuyetKy_2.itemOptions.add(new ItemOption(229, 0));
                for (int i = 1; i < sachTuyetKy.itemOptions.size(); i++) {
                    sachTuyetKy_2.itemOptions.add(new ItemOption(sachTuyetKy.itemOptions.get(i).optionTemplate.id, sachTuyetKy.itemOptions.get(i).param));
                }
                sendEffectSuccessCombine(player);
                player.inventory.ruby -= 5000;
                Service.getInstance().sendMoney(player);
                InventoryService.gI().addItemBag(player, sachTuyetKy_2, 1);
                InventoryService.gI().subQuantityItemsBag(player, sachTuyetKy, 1);
                InventoryService.gI().sendItemBags(player);
                reOpenItemCombine(player);
            }
        }
    }

    // END _ SÁCH TUYỆT KỸ
    private short getidbtsaukhilencap(int lvbtcu) {
        switch (lvbtcu) {
            case 1:
                return 921;
            case 2:
                return 1015;
        }
        return 0;
    }

    private void moChiSoBongTaiVoCuc(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            Item bongTai3 = null;
            Item daNguSacPro = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 1015) {
                    bongTai3 = item;
                } else if (item.template.id == 1503) {
                    daNguSacPro = item;
                }
            }
            if (bongTai3 != null && daNguSacPro != null) {
                InventoryService.gI().subQuantityItemsBag(player, daNguSacPro, 1);
                if (Util.isTrue(40, 100)) {

                    bongTai3.itemOptions.clear();

                    int rdUp = Util.nextInt(0, 7);
                    switch (rdUp) {
                        case 0:
                            bongTai3.itemOptions.add(new ItemOption(50, Util.nextInt(16, 30)));
                            break;
                        case 1:
                            bongTai3.itemOptions.add(new ItemOption(77, Util.nextInt(16, 30)));
                            break;
                        case 2:
                            bongTai3.itemOptions.add(new ItemOption(103, Util.nextInt(16, 30)));
                            break;
                        case 3:
                            bongTai3.itemOptions.add(new ItemOption(108, Util.nextInt(16, 30)));
                            break;
                        case 4:
                            bongTai3.itemOptions.add(new ItemOption(94, Util.nextInt(16, 30)));
                            break;
                        case 5:
                            bongTai3.itemOptions.add(new ItemOption(14, Util.nextInt(16, 30)));
                            break;
                        case 6:
                            bongTai3.itemOptions.add(new ItemOption(80, Util.nextInt(16, 30)));
                            break;
                        case 7:
                            bongTai3.itemOptions.add(new ItemOption(81, Util.nextInt(16, 30)));
                            break;
                    }
                    bongTai3.itemOptions.add(new ItemOption(38, 0));
                    bongTai3.itemOptions.add(new ItemOption(72, 3));
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void moChiSoBongTai(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            Item bongTai = null;
            Item manhHon = null;
            Item daXanhLam = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 921) {
                    bongTai = item;
                } else if (item.template.id == 934) {
                    manhHon = item;
                } else if (item.template.id == 935) {
                    daXanhLam = item;
                }
            }
            if (bongTai != null && daXanhLam != null && manhHon.quantity >= 99) {
                InventoryService.gI().subQuantityItemsBag(player, manhHon, 99);
                InventoryService.gI().subQuantityItemsBag(player, daXanhLam, 1);
                if (Util.isTrue(80, 100)) {

                    bongTai.itemOptions.clear();

                    int rdUp = Util.nextInt(0, 7);
                    switch (rdUp) {
                        case 0:
                            bongTai.itemOptions.add(new ItemOption(50, Util.nextInt(5, 15)));
                            break;
                        case 1:
                            bongTai.itemOptions.add(new ItemOption(77, Util.nextInt(5, 15)));
                            break;
                        case 2:
                            bongTai.itemOptions.add(new ItemOption(103, Util.nextInt(5, 15)));
                            break;
                        case 3:
                            bongTai.itemOptions.add(new ItemOption(108, Util.nextInt(5, 15)));
                            break;
                        case 4:
                            bongTai.itemOptions.add(new ItemOption(94, Util.nextInt(5, 15)));
                            break;
                        case 5:
                            bongTai.itemOptions.add(new ItemOption(14, Util.nextInt(5, 15)));
                            break;
                        case 6:
                            bongTai.itemOptions.add(new ItemOption(80, Util.nextInt(5, 15)));
                            break;
                        case 7:
                            bongTai.itemOptions.add(new ItemOption(81, Util.nextInt(5, 15)));
                            break;
                    }
                    bongTai.itemOptions.add(new ItemOption(38, 0));
                    bongTai.itemOptions.add(new ItemOption(72, 2));
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void cheTaoDoTS(Player player) {
        // Công thức vip + x999 Mảnh thiên sứ + đá nâng cấp + đá may mắn
        if (player.combineNew.itemsCombine.size() < 2 || player.combineNew.itemsCombine.size() > 4) {
            Service.getInstance().sendThongBao(player, "Thiếu vật phẩm, vui lòng thêm vào");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isCongthucVip()).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Công thức Vip");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 9999).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Mảnh thiên sứ");
            return;
        }
        Item mTS = null, daNC = null, daMM = null, CtVip = null;
        for (Item item : player.combineNew.itemsCombine) {
            if (item.isNotNullItem()) {
                if (item.isManhTS()) {
                    mTS = item;
                } else if (item.isdanangcapDoTs()) {
                    daNC = item;
                } else if (item.isDamayman()) {
                    daMM = item;
                } else if (item.isCongthucVip()) {
                    CtVip = item;
                }
            }
        }
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {//check chỗ trống hành trang
            if (player.inventory.gold < 10_000_000_000L) {
                Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            player.inventory.gold -= 10_000_000_000L;

            int tilemacdinh = 35;
            int tileLucky = 5;
            if (daNC != null) {
                tilemacdinh += (daNC.template.id - 1073);
            }
            if (daMM != null) {
                tileLucky += tileLucky * (daMM.template.id - 1078);
            }
            if (Util.nextInt(0, 100) < tilemacdinh) {
                Item itemCtVip = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isCongthucVip()).findFirst().get();
                Item itemManh = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 999).findFirst().get();
                tilemacdinh = 100;

                short[][] itemIds = {{1048, 1051, 1054, 1057, 1060}, {1049, 1052, 1055, 1058, 1061}, {1050, 1053, 1056, 1059, 1062}}; // thứ tự td - 0,nm - 1, xd - 2

                Item itemTS = ItemService.gI().DoThienSu(itemIds[itemCtVip.template.gender > 2 ? player.gender : itemCtVip.template.gender][itemManh.typeIdManh()], itemCtVip.template.gender);

                if (tilemacdinh > 0) {
                    for (byte i = 0; i < itemTS.itemOptions.size(); i++) {
                        if (itemTS.itemOptions.get(i).optionTemplate.id != 0 && itemTS.itemOptions.get(i).optionTemplate.id != 0 && itemTS.itemOptions.get(i).optionTemplate.id != 21 && itemTS.itemOptions.get(i).optionTemplate.id != 14) {
                            itemTS.itemOptions.get(i).param += (itemTS.itemOptions.get(i).param);
                        }
                    }
                }
                tilemacdinh = Util.nextInt(0, 50);

                if (tilemacdinh <= tileLucky) {
                    if (tilemacdinh >= (tileLucky - 3)) {
                        tileLucky = 3;
                    } else if (tilemacdinh <= (tileLucky - 4) && tilemacdinh >= (tileLucky - 10)) {
                        tileLucky = 2;
                    } else {
                        tileLucky = 1;
                    }
                    itemTS.itemOptions.add(new ItemOption(15, tileLucky));

                    ArrayList<Integer> listOptionBonus = new ArrayList<>();
                    listOptionBonus.add(50);
                    listOptionBonus.add(77);
                    listOptionBonus.add(103);
                    listOptionBonus.add(94);
                    listOptionBonus.add(5);
                    for (int i = 0; i < tileLucky; i++) {
                        tilemacdinh = Util.nextInt(0, listOptionBonus.size() - 1);
                        itemTS.itemOptions.add(new ItemOption(listOptionBonus.get(tilemacdinh), Util.nextInt(1, 3)));
                        listOptionBonus.remove(tilemacdinh);
                    }
                }

                InventoryService.gI().addItemBag(player, itemTS, 9999);
                sendEffectSuccessCombine(player);
                if (mTS != null && daMM != null && daNC != null && CtVip != null) {
                    InventoryService.gI().subQuantityItemsBag(player, CtVip, 1);
                    InventoryService.gI().subQuantityItemsBag(player, daNC, 1);
                    InventoryService.gI().subQuantityItemsBag(player, mTS, 9999);
                    InventoryService.gI().subQuantityItemsBag(player, daMM, 1);
                } else if (CtVip != null && mTS != null) {
                    InventoryService.gI().subQuantityItemsBag(player, CtVip, 1);
                    InventoryService.gI().subQuantityItemsBag(player, mTS, 9999);
                } else if (CtVip != null && mTS != null && daNC != null) {
                    InventoryService.gI().subQuantityItemsBag(player, CtVip, 1);
                    InventoryService.gI().subQuantityItemsBag(player, mTS, 9999);
                    InventoryService.gI().subQuantityItemsBag(player, daNC, 1);
                } else if (CtVip != null && mTS != null && daMM != null) {
                    InventoryService.gI().subQuantityItemsBag(player, CtVip, 1);
                    InventoryService.gI().subQuantityItemsBag(player, mTS, 9999);
                    InventoryService.gI().subQuantityItemsBag(player, daMM, 1);
                }
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);

            } else {
                sendEffectFailCombine(player);
                if (mTS != null && daMM != null && daNC != null && CtVip != null) {

                    InventoryService.gI().subQuantityItemsBag(player, mTS, 999);

                } else if (CtVip != null && mTS != null) {

                    InventoryService.gI().subQuantityItemsBag(player, mTS, 999);
                } else if (CtVip != null && mTS != null && daNC != null) {

                    InventoryService.gI().subQuantityItemsBag(player, mTS, 999);

                } else if (CtVip != null && mTS != null && daMM != null) {

                    InventoryService.gI().subQuantityItemsBag(player, mTS, 999);

                }
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }

        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    private void nangCapDoThienSu(Player player) {
        if (player.combineNew.itemsCombine.size() != 2) {
            Service.getInstance().sendThongBao(player, "Cần 1 Công thức VIP và 999 Mảnh Thiên Sứ");
            return;
        }

        Item ctVip = null, manhTS = null;
        for (Item item : player.combineNew.itemsCombine) {
            if (item.isNotNullItem()) {
                if (item.isCongthucVip()) {
                    ctVip = item;
                } else if (item.isManhTS() && item.quantity >= 999) {
                    manhTS = item;
                }
            }
        }

        if (ctVip == null || manhTS == null) {
            Service.getInstance().sendThongBao(player, "Thiếu Công thức VIP hoặc không đủ 999 Mảnh Thiên Sứ");
            return;
        }

        if (InventoryService.gI().getCountEmptyBag(player) < 1) {
            Service.getInstance().sendThongBao(player, "Cần ít nhất 1 ô trống trong hành trang");
            return;
        }

        if (player.inventory.gold < 1_000_000_000) {
            Service.getInstance().sendThongBao(player, "Không đủ vàng (1 tỷ)");
            return;
        }

        player.inventory.gold -= 1_000_000_000;

        // Tạo item thiên sứ mới
        short[][] itemIds = {
            {1048, 1051, 1054, 1057, 1060}, // Trái Đất
            {1049, 1052, 1055, 1058, 1061}, // Namek
            {1050, 1053, 1056, 1059, 1062} // Xayda
        };

        int gender = ctVip.template.gender > 2 ? player.gender : ctVip.template.gender;
        int typeManh = manhTS.typeIdManh(); // 0, 1, 2
        Item itemTS = ItemService.gI().DoThienSu(itemIds[gender][typeManh], gender);

        // Random option đặc biệt
        int[] optionIds = {50, 77, 103};
        int optionId = optionIds[Util.nextInt(0, optionIds.length - 1)];

        int param;
        int rnd = Util.nextInt(100);
        if (rnd < 70) {
            param = Util.nextInt(1, 5); // 70%: 1-5
        } else if (rnd < 90) {
            param = Util.nextInt(6, 8); // 20%: 6-8
        } else {
            param = Util.nextInt(9, 10); // 10%: 9-10
        }

        itemTS.itemOptions.add(new ItemOption(optionId, param));

        // Thêm item vào hành trang
        InventoryService.gI().addItemBag(player, itemTS, 1);
        InventoryService.gI().subQuantityItemsBag(player, ctVip, 1);
        InventoryService.gI().subQuantityItemsBag(player, manhTS, 999);

        InventoryService.gI().sendItemBags(player);
        Service.getInstance().sendMoney(player);
        sendEffectSuccessCombine(player);
        reOpenItemCombine(player);
    }

    private void chuyenHoaTrangBiVang(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {

            Item trangBiGoc = player.combineNew.itemsCombine.get(0);
            Item trangBiCanChuyenHoa = player.combineNew.itemsCombine.get(1);

            Item trangBiCanChuyenHoa_2 = ItemService.gI().createNewItem(player.combineNew.itemsCombine.get(1).template.id);
            int goldChuyenHoa = 2_000_000_000;

            int levelTrangBi = 0;
            int soLanRotCap = 0;
            int chiSO1_trangBiCanChuyenHoa = 0;

            for (ItemOption io : trangBiGoc.itemOptions) {
                if (io.optionTemplate.id == 72) {
                    levelTrangBi = io.param;
                } else if (io.optionTemplate.id == 232) {
                    soLanRotCap += io.param;
                }
            }

            // START Tính chỉ số nhân với số cấp và trừ với số lần rớt cấp //
            int chisogoc = trangBiCanChuyenHoa.itemOptions.get(0).param;

            chisogoc += chisogoc * (levelTrangBi * 0.1);

            chisogoc -= chisogoc * (soLanRotCap * 0.1);
            // END Tính chỉ số nhân với số cấp và trừ với số lần rớt cấp //

            boolean trangBi_daNangCap_daPhaLeHoa = false;
            if (player.inventory.gold >= goldChuyenHoa) {
                if (!isTrangBiGoc(trangBiGoc)) {
                    Service.getInstance().sendThongBaoOK(player, "Trang bị phải từ bậc lưỡng long, Jean hoặc Zelot trở lên");
                    return;
                } else if (levelTrangBi < 4) {
                    Service.getInstance().sendThongBaoOK(player, "Trang bị gốc có cấp từ [+4]");
                    return;
                } else if (!isTrangBiChuyenHoa(trangBiCanChuyenHoa)) {
                    Service.getInstance().sendThongBaoOK(player, "Trang bị phải từ bậc lưỡng long, Jean hoặc Zelot trở lên");
                    return;
                } else if (trangBi_daNangCap_daPhaLeHoa) {
                    Service.getInstance().sendThongBaoOK(player, "Trang bị nhập thể phải chưa nâng cấp và pha lê hóa trang bị");
                    return;
                } else if (!isCheckTrungTypevsGender(trangBiGoc, trangBiCanChuyenHoa)) {
                    Service.getInstance().sendThongBaoOK(player, "Trang bị gốc và Trang bị nhập thể phải cùng loại và hành tinh");
                    return;
                } else {

                    trangBiCanChuyenHoa.itemOptions.get(0).param = chisogoc;

                    for (int i = 1; i < trangBiGoc.itemOptions.size(); i++) {
                        trangBiCanChuyenHoa.itemOptions.add(new ItemOption(trangBiGoc.itemOptions.get(i).optionTemplate.id, trangBiGoc.itemOptions.get(i).param));
                    }

                    for (int i = 0; i < trangBiCanChuyenHoa.itemOptions.size(); i++) {
                        trangBiCanChuyenHoa_2.itemOptions.add(new ItemOption(trangBiCanChuyenHoa.itemOptions.get(i).optionTemplate.id, trangBiCanChuyenHoa.itemOptions.get(i).param));
                    }

                    player.inventory.gold -= 2_000_000_000;
                    Service.getInstance().sendMoney(player);
                    InventoryService.gI().addItemBag(player, trangBiCanChuyenHoa_2, 1);
                    InventoryService.gI().subQuantityItemsBag(player, trangBiGoc, 1);
                    InventoryService.gI().subQuantityItemsBag(player, trangBiCanChuyenHoa, 1);
                    InventoryService.gI().sendItemBags(player);
                    reOpenItemCombine(player);
                    sendEffectSuccessCombine(player);
                }
            } else {
                Service.getInstance().sendThongBao(player, "Không có tiền mà đòi chuyển hóa cái gì?");
                return;
            }
        }
    }

    private boolean isCoupleItemNangCapCheck(Item trangBi, Item daNangCap) {
        if (trangBi != null && daNangCap != null) {
            if (trangBi.template.type == 0 && daNangCap.template.id == 223) {
                return true;
            } else if (trangBi.template.type == 1 && daNangCap.template.id == 222) {
                return true;
            } else if (trangBi.template.type == 2 && daNangCap.template.id == 224) {
                return true;
            } else if (trangBi.template.type == 3 && daNangCap.template.id == 221) {
                return true;
            } else if (trangBi.template.type == 4 && daNangCap.template.id == 220) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void chuyenHoaTrangBiNgoc(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {

            Item trangBiGoc = player.combineNew.itemsCombine.get(0);
            Item trangBiCanChuyenHoa = player.combineNew.itemsCombine.get(1);

            Item trangBiCanChuyenHoa_2 = ItemService.gI().createNewItem(player.combineNew.itemsCombine.get(1).template.id);

            int ngocChuyenHoa = 5000;

            int levelTrangBi = 0;
            int soLanRotCap = 0;
            int chiSO1_trangBiCanChuyenHoa = 0;

            for (ItemOption io : trangBiGoc.itemOptions) {
                if (io.optionTemplate.id == 72) {
                    levelTrangBi = io.param - 1;
                } else if (io.optionTemplate.id == 232) {
                    soLanRotCap += io.param;
                }
            }

            // START Tính chỉ số nhân với số cấp và trừ với số lần rớt cấp //
            int chisogoc = trangBiCanChuyenHoa.itemOptions.get(0).param;

            chisogoc += chisogoc * (levelTrangBi * 0.1);

            chisogoc -= chisogoc * (soLanRotCap * 0.1);
            // END Tính chỉ số nhân với số cấp và trừ với số lần rớt cấp //

            boolean trangBi_daNangCap_daPhaLeHoa = false;
            if (player.inventory.gem >= ngocChuyenHoa) {
                if (!isTrangBiGoc(trangBiGoc)) {
                    Service.getInstance().sendThongBaoOK(player, "Trang bị phải từ bậc lưỡng long, Jean hoặc Zelot trở lên");
                    return;
                } else if (levelTrangBi < 4) {
                    Service.getInstance().sendThongBaoOK(player, "Trang bị gốc có cấp từ [+4]");
                    return;
                } else if (!isTrangBiChuyenHoa(trangBiCanChuyenHoa)) {
                    Service.getInstance().sendThongBaoOK(player, "Trang bị phải từ bậc lưỡng long, Jean hoặc Zelot trở lên");
                    return;
                } else if (trangBi_daNangCap_daPhaLeHoa) {
                    Service.getInstance().sendThongBaoOK(player, "Trang bị nhập thể phải chưa nâng cấp và pha lê hóa trang bị");
                    return;
                } else if (!isCheckTrungTypevsGender(trangBiGoc, trangBiCanChuyenHoa)) {
                    Service.getInstance().sendThongBaoOK(player, "Trang bị gốc và Trang bị nhập thể phải cùng loại và hành tinh");
                    return;
                } else {

                    trangBiCanChuyenHoa.itemOptions.get(0).param = chisogoc;

                    for (int i = 1; i < trangBiGoc.itemOptions.size(); i++) {
                        trangBiCanChuyenHoa.itemOptions.add(new ItemOption(trangBiGoc.itemOptions.get(i).optionTemplate.id, trangBiGoc.itemOptions.get(i).param));
                    }

                    for (int i = 0; i < trangBiCanChuyenHoa.itemOptions.size(); i++) {
                        trangBiCanChuyenHoa_2.itemOptions.add(new ItemOption(trangBiCanChuyenHoa.itemOptions.get(i).optionTemplate.id, trangBiCanChuyenHoa.itemOptions.get(i).param));
                    }

                    player.inventory.gem -= ngocChuyenHoa;
                    Service.getInstance().sendMoney(player);
                    InventoryService.gI().addItemBag(player, trangBiCanChuyenHoa_2, 1);
                    InventoryService.gI().subQuantityItemsBag(player, trangBiGoc, 1);
                    InventoryService.gI().subQuantityItemsBag(player, trangBiCanChuyenHoa, 1);
                    InventoryService.gI().sendItemBags(player);
                    reOpenItemCombine(player);
                    sendEffectSuccessCombine(player);
                }
            } else {
                Service.getInstance().sendThongBao(player, "Không có tiền mà đòi chuyển hóa cái gì?");
                return;
            }
        }
    }

    private void giaHanCaiTrang(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            Item caitrang = null, vegiahan = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.isNotNullItem()) {
                    if (item.template.type == 5) {
                        caitrang = item;
                    } else if (item.template.id == 2022) {
                        vegiahan = item;
                    }
                }
            }
            if (caitrang != null && vegiahan != null) {
                if (InventoryService.gI().getCountEmptyBag(player) > 0
                        && player.inventory.gold >= COST_GIA_HAN_CAI_TRANG) {
                    ItemOption expiredDate = null;
                    boolean canBeExtend = true;
                    for (ItemOption io : caitrang.itemOptions) {
                        if (io.optionTemplate.id == 93) {
                            expiredDate = io;
                        }
                        if (io.optionTemplate.id == 199) {
                            canBeExtend = false;
                        }
                    }
                    if (canBeExtend) {
                        if (expiredDate.param > 0) {
                            player.inventory.subGold(COST_GIA_HAN_CAI_TRANG);
                            sendEffectSuccessCombine(player);
                            expiredDate.param++;
                            InventoryService.gI().subQuantityItemsBag(player, vegiahan, 1);
                            InventoryService.gI().sendItemBags(player);
                            Service.getInstance().sendMoney(player);
                            reOpenItemCombine(player);
                        }
                    }
                }
            }
        }
    }

    private void dapDoKichHoatCaoCap(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            List<Item> items = player.combineNew.itemsCombine.stream()
                    .filter(Item::isNotNullItem)
                    .collect(Collectors.toList());

            List<Item> itemsSKH = items.stream().filter(Item::isSKH).collect(Collectors.toList());

            if (itemsSKH.size() != 2) {
                Service.getInstance().sendThongBao(player, "Thiếu đồ kích hoạt");
                return;
            }

            Item itemChinh = itemsSKH.get(0);
            Item itemPhu = itemsSKH.get(1);
            // --- Thêm đoạn kiểm tra vị trí trùng ---
            int viTriChinh = -1;
            int viTriPhu = -1;
            for (int i = 0; i < player.inventory.itemsBag.size(); i++) {
                Item item = player.inventory.itemsBag.get(i);
                if (item == itemChinh) {
                    viTriChinh = i;
                }
                if (item == itemPhu) {
                    viTriPhu = i;
                }
            }

            if (viTriChinh == viTriPhu && viTriChinh != -1) {
                Service.getInstance().sendThongBao(player,"Bố biết rồi nhé, Bug cái đcmm!");
                return;
            }
            int type1 = itemChinh.template.type;
            int type2 = itemPhu.template.type;
            int idItemChinh = itemChinh.template.id;
            int idItemPhu = itemPhu.template.id;
            int genderChinh = itemChinh.template.gender;
            int genderPhu = itemPhu.template.gender;

            if (genderChinh != genderPhu) {
                Service.getInstance().sendThongBao(player, "Hai món phải cùng hành tinh");
                return;
            }
            if (type1 != type2) {
                Service.getInstance().sendThongBao(player, "Món kích hoạt phải cùng loại");
                return;
            }
            int capDo = getCapDoKHVIP(genderChinh, type1, idItemChinh);
            int capDoPhu = getCapDoKHVIP(genderPhu, type2, idItemPhu);
            if (capDo != capDoPhu) {
                Service.getInstance().sendThongBao(player, "Hãy chọn 2 món kích hoạt ngang nhau");
                return;
            }
            if (capDo >= 4) {
                Service.getInstance().sendThongBao(player, "Không thể nâng cấp món đồ này nữa");
                return;
            }

            int gender = itemChinh.template.gender;
            int type = itemChinh.template.type;

            if (InventoryService.gI().getCountEmptyBag(player) <= 0) {
                Service.getInstance().sendThongBao(player, "Hành trang đã đầy");
                return;
            }

            if (player.inventory.gold < COST_DAP_DO_KICH_HOAT) {
                Service.getInstance().sendThongBao(player, "Không đủ vàng để nâng cấp");
                return;
            }

            // Trừ vàng
            player.inventory.subGold(COST_DAP_DO_KICH_HOAT);
            Service.getInstance().sendMoney(player);

            // Tạo ID item kế tiếp
            int idNext = getTempIdItemKHVIP(gender, type, capDo + 1);
            Item item = ItemService.gI().createNewItem((short) idNext);
            //Service.getInstance().sendThongBao(player, "type Chính " + getOptionSetKichHoat(itemChinh) + " type Phu" + getOptionSetKichHoat(itemPhu));
            if (getOptionSetKichHoat(itemChinh) == getOptionSetKichHoat(itemPhu)) {
                // Gán chỉ số
                sendEffectSuccessCombine(player);
                RewardService.gI().initBaseOptionClothes(item.template.id, item.template.type, item.itemOptions);
                // Gán đúng loại set từ món chính
                for (ItemOption opt : itemChinh.itemOptions) {
                    if (OPTION_SET_KICH_HOAT.contains(opt.optionTemplate.id)) {
                        item.itemOptions.add(new ItemOption(opt.optionTemplate.id, opt.param));
                    }
                }
                for (ItemOption opt : itemChinh.itemOptions) {
                    if (OPTION_HIEU_UNG_SET.contains(opt.optionTemplate.id)) {
                        item.itemOptions.add(new ItemOption(opt.optionTemplate.id, opt.param));
                    }
                }
                item.itemOptions.add(new ItemOption(30, 7));
            } else {
                // Gán chỉ số
                sendEffectSuccessCombine(player);
                RewardService.gI().initBaseOptionClothes(item.template.id, item.template.type,
                        item.itemOptions);
                RewardService.gI().initActivationOption(
                        item.template.gender < 3 ? item.template.gender : player.gender, item.template.type,
                        item.itemOptions);
            }

            // Thêm vào hành trang
            InventoryService.gI().addItemBag(player, item, 0);

            // Trừ đồ cũ
            InventoryService.gI().subQuantityItemsBag(player, itemChinh, 1);
            InventoryService.gI().subQuantityItemsBag(player, itemPhu, 1);
            InventoryService.gI().sendItemBags(player);

            sendEffectSuccessCombine(player);
            reOpenItemCombine(player);
        }
    }

    private void doiManhKichHoat(Player player) {
        if (player.combineNew.itemsCombine.size() == 2 || player.combineNew.itemsCombine.size() == 3) {
            Item nr1s = null, doThan = null, buaBaoVe = null;
            for (Item it : player.combineNew.itemsCombine) {
                if (it.template.id == 14) {
                    nr1s = it;
                } else if (it.template.id == 2010) {
                    buaBaoVe = it;
                } else if (it.template.id >= 555 && it.template.id <= 567) {
                    doThan = it;
                }
            }
            if (nr1s != null && doThan != null) {
                if (InventoryService.gI().getCountEmptyBag(player) > 0
                        && player.inventory.gold >= COST_DOI_MANH_KICH_HOAT) {
                    player.inventory.gold -= COST_DOI_MANH_KICH_HOAT;
                    int tiLe = buaBaoVe != null ? 100 : 50;
                    if (Util.isTrue(tiLe, 100)) {
                        sendEffectSuccessCombine(player);
                        Item item = ItemService.gI().createNewItem((short) 2009);
                        item.itemOptions.add(new ItemOption(30, 0));
                        InventoryService.gI().addItemBag(player, item, 0);
                    } else {
                        sendEffectFailCombine(player);
                    }
                    InventoryService.gI().subQuantityItemsBag(player, nr1s, 1);
                    InventoryService.gI().subQuantityItemsBag(player, doThan, 1);
                    if (buaBaoVe != null) {
                        InventoryService.gI().subQuantityItemsBag(player, buaBaoVe, 1);
                    }
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
            } else {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Hãy chọn 1 trang bị thần linh và 1 viên ngọc rồng 1 sao", "Đóng");
            }
        }
    }

    private void dapDoKichHoat(Player player) {
        if (player.combineNew.itemsCombine.size() == 1 || player.combineNew.itemsCombine.size() == 2) {
            List<Item> trangBiThanLinh = player.combineNew.itemsCombine.stream()
                    .filter(item -> item.isNotNullItem() && (item.template.id >= 555 && item.template.id <= 567))
                    .collect(Collectors.toList());
            if (trangBiThanLinh.isEmpty()) {
                return; // check empty
            }
            Item itemChinh = trangBiThanLinh.get(0); // Món đầu tiên sẽ là cơ sở đổi sang SKH
            Item itemPhu = trangBiThanLinh.size() > 1 ? trangBiThanLinh.get(1) : null;
            if (itemChinh != null) {
                //check HTrang
                if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                    // Kiểm tra vàng
                    if (player.inventory.gold < COST_DAP_DO_KICH_HOAT) {
                        Service.gI().sendThongBao(player, "Không đủ vàng để đập đồ! Cần "
                                + Util.numberToMoney(COST_DAP_DO_KICH_HOAT) + " vàng");
                        return;
                    }
                    player.inventory.gold -= COST_DAP_DO_KICH_HOAT;
                    int tile = (trangBiThanLinh.size() == 2) ? 100 : 50;
                    if (Util.isTrue(tile, 100)) {
                        sendEffectSuccessCombine(player);
                        Item item = ItemService.gI()
                                .createNewItem((short) getTempIdItemC0(itemChinh.template.gender, itemChinh.template.type));
                        RewardService.gI().initBaseOptionClothes(item.template.id, item.template.type,
                                item.itemOptions);
                        RewardService.gI().initActivationOption(
                                item.template.gender < 3 ? item.template.gender : player.gender, item.template.type,
                                item.itemOptions);
                        InventoryService.gI().addItemBag(player, item, 0);
                    } else {
                        sendEffectFailCombine(player);
                    }
                    InventoryService.gI().subQuantityItemsBag(player, itemChinh, 1);
                    if (itemPhu != null) {
                        InventoryService.gI().subQuantityItemsBag(player, itemPhu, 1);
                    }
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                } else {
                    Service.gI().sendThongBao(player, "Hành trang không đủ chỗ trống!");
                    return;
                }
            }
        }
    }

    private void doiVeHuyDiet(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item item = player.combineNew.itemsCombine.get(0);
            if (item.isNotNullItem() && item.template.id >= 555 && item.template.id <= 567) {
                if (InventoryService.gI().getCountEmptyBag(player) > 0
                        && player.inventory.gold >= COST_DOI_VE_DOI_DO_HUY_DIET) {
                    player.inventory.gold -= COST_DOI_VE_DOI_DO_HUY_DIET;
                    Item ticket = ItemService.gI().createNewItem((short) (2001 + item.template.type));
                    ticket.itemOptions.add(new ItemOption(30, 0));
                    InventoryService.gI().subQuantityItemsBag(player, item, 1);
                    InventoryService.gI().addItemBag(player, ticket, 99);
                    sendEffectOpenItem(player, item.template.iconID, ticket.template.iconID);

                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void epSaoTrangBi(Player player) {
        if (player.combineNew.itemsCombine.size() != 2) {
            return;
        }

        int gem = player.combineNew.gemCombine;
        if (player.inventory.gem < gem) {
            Service.getInstance().sendThongBao(player, "Không đủ ngọc để thực hiện");
            return;
        }

        Item trangBi = null;
        Item daPhaLe = null;

        for (Item item : player.combineNew.itemsCombine) {
            if (isTrangBiPhaLeHoa2(item)) {
                trangBi = item;
            } else {
                daPhaLe = item;
            }
        }

        if (trangBi == null || daPhaLe == null) {
            return;
        }

        int star = 0;
        int starEmpty = 0;
        int capCuongHoa = 0;
        ItemOption optionStar = null;

        for (ItemOption io : trangBi.itemOptions) {
            if (io.optionTemplate.id == 102) {
                star = io.param;
                optionStar = io;
            } else if (io.optionTemplate.id == 107) {
                starEmpty = io.param;
            } else if (io.optionTemplate.id == 234) {
                capCuongHoa = io.param;
            }
        }

        int id = daPhaLe.template.id;
        int type = daPhaLe.template.type;

        boolean isDa14_20 = id >= 14 && id <= 20;
        boolean isDa807_813 = id >= 807 && id <= 813;
        boolean isType30 = type == 30;

        boolean hopLe = false;

        if (star < 6) {
            hopLe = isDa14_20 || isType30;
        } else if (star == 6) {
            hopLe = capCuongHoa >= 7 && (isDa807_813 || isType30);
        } else if (star == 7) {
            hopLe = capCuongHoa >= 8 && (isDa807_813 || isType30);
        }

        if (!hopLe || star >= starEmpty || star >= 8) {
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Không thể ép sao với đá hoặc cấp cường hóa hiện tại", "Đóng");
            return;
        }

        // Trừ ngọc
        player.inventory.gem -= gem;

        // Xác định option cần ép
        int optionId;
        int param;
        if (isDa807_813) {
            optionId = getOptionDaPhaLe2(daPhaLe);
            param = getParamDaPhaLe2(daPhaLe);
        } else {
            optionId = getOptionDaPhaLe(daPhaLe);
            param = getParamDaPhaLe(daPhaLe);
        }

        // Kiểm tra option đã tồn tại chưa
        ItemOption option = null;
        for (ItemOption io : trangBi.itemOptions) {
            if (io.optionTemplate.id == optionId) {
                option = io;
                break;
            }
        }

        if (option != null) {
            option.param += param;
        } else {
            trangBi.itemOptions.add(new ItemOption(optionId, param));
        }

        // Tăng sao
        if (optionStar != null) {
            optionStar.param++;
        } else {
            trangBi.itemOptions.add(new ItemOption(102, 1));
        }

        // Nếu là đá 807–813 mà chưa có option 233, thêm luôn
        if (isDa807_813) {
            boolean hasOption233 = false;
            for (ItemOption io : trangBi.itemOptions) {
                if (io.optionTemplate.id == 233) {
                    hasOption233 = true;
                    break;
                }
            }
            if (!hasOption233) {
                trangBi.itemOptions.add(new ItemOption(233, 0));
            }
        }

        // Trừ đá
        InventoryService.gI().subQuantityItemsBag(player, daPhaLe, 1);

        // Hiệu ứng và cập nhật
        sendEffectSuccessCombine(player);
        InventoryService.gI().sendItemBags(player);
        Service.getInstance().sendMoney(player);
        reOpenItemCombine(player);
    }

    private void phaLeHoaTrangBi(Player player) {
        if (!player.combineNew.itemsCombine.isEmpty()) {
            int gold = player.combineNew.goldCombine;
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gold < gold) {
                Service.getInstance().sendThongBao(player, "Bạn không đủ vàng, còn thiếu " + Util.numberToMoney(gold) + " vàng nữa");
                return;
            } else if (player.inventory.gem < gem) {
                Service.getInstance().sendThongBao(player, "Bạn không đủ vàng, còn thiếu " + Util.mumberToLouis(gem) + " ngọc nữa");
                return;
            }
            Item item = player.combineNew.itemsCombine.get(0);
            if (isTrangBiPhaLeHoa(item)) {
                int star = 0;
                ItemOption optionStar = null;
                ItemOption optionFailureCount = null; // Khai báo biến để lưu Option ID 250

                // Tìm option 107 (số sao pha lê) và option 250 (số lần thất bại)
                for (ItemOption io : item.itemOptions) {
                    if (io.optionTemplate.id == 107) {
                        star = io.param;
                        optionStar = io;
                    } else if (io.optionTemplate.id == 250) {
                        optionFailureCount = io; // Tìm option ID 250 hiện có
                    }
                }

                if (star < MAX_STAR_ITEM) {
                    player.inventory.gold -= gold;
                    player.inventory.subGem(gem);
                    if (Util.isTrue(player.combineNew.ratioCombine, 400)) {
                        if (optionStar == null) {
                            item.itemOptions.add(new ItemOption(107, 1));
                        } else {
                            optionStar.param++;
                        }
                        sendEffectSuccessCombine(player);
                        if (optionStar != null && optionStar.param >= 7) {
                            ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa pha lê hóa " + "thành công "
                                    + item.template.name + " lên " + optionStar.param + " sao pha lê");
                            ServerLog.logCombine(player.name, item.template.name, optionStar.param);
                        }
                    } else {
                        // Xử lý khi thất bại, cộng thêm giá trị vào option ID 250
                        if (optionFailureCount == null) {
                            // Nếu chưa có option 250, thêm mới với giá trị ban đầu là 1
                            item.itemOptions.add(new ItemOption(250, 1));
                        } else {
                            // Nếu đã có, tăng giá trị param thêm 1
                            optionFailureCount.param++;
                        }
                        sendEffectFailCombine(player);
//                        Service.getInstance().sendThongBao(player, "Xịt");
                    }
                }
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void phaLeHoaTrangBix10(Player player) {
        if (!player.combineNew.itemsCombine.isEmpty()) {
            long gold = player.combineNew.goldCombine;
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gold < (gold * 10)) {
                Service.getInstance().sendThongBao(player, "Bạn không đủ vàng, còn thiếu " + Util.numberToMoney(gold * 10) + " vàng nữa");
                return;
            } else if (player.inventory.gem < (gem * 10)) {
                Service.getInstance().sendThongBao(player, "Bạn không đủ vàng, còn thiếu " + Util.mumberToLouis(gem * 10) + " ngọc nữa");
                return;
            }
            Item item = player.combineNew.itemsCombine.get(0);
            if (isTrangBiPhaLeHoa(item)) {
                int star = 0;
                ItemOption optionStar = null;
                ItemOption optionFailureCount = null; // Khai báo biến để lưu Option ID 250
                for (ItemOption io : item.itemOptions) {
                    if (io.optionTemplate.id == 107) {
                        star = io.param;
                        optionStar = io;
                    } else if (io.optionTemplate.id == 250) {
                        optionFailureCount = io; // Tìm option ID 250
                    }
                }
                // Nếu item chưa có option ID 250, thêm mới
                if (optionFailureCount == null) {
                    optionFailureCount = new ItemOption(250, 0);
                    item.itemOptions.add(optionFailureCount);
                }
                if (star < MAX_STAR_ITEM) {
                    int failureCount = 0; // Biến đếm số lần thất bại
                    for (int i = 0; i < 10; i++) {
                        player.inventory.gold -= gold;
                        player.inventory.subGem(gem);
                        if (Util.isTrue(player.combineNew.ratioCombine, 400)) {
                            if (optionStar == null) {
                                item.itemOptions.add(new ItemOption(107, 1));
                            } else {
                                optionStar.param++;
                            }
                            sendEffectSuccessCombine(player);
                            // Cộng dồn số lần thất bại vào option 250 khi thành công
                            optionFailureCount.param += failureCount;

                            if (optionStar != null && optionStar.param >= 7) {
                                ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa pha lê hóa " + "thành công " + item.template.name + " lên " + optionStar.param + " sao pha lê");
                                ServerLog.logCombine(player.name, item.template.name, optionStar.param);
                            }
                            Service.getInstance().sendThongBao(player, "Bạn đã đập " + (i + 1) + " lần, với " + failureCount + " lần thất bại.");
                            break;
                        } else {
                            // Tăng biến đếm khi thất bại
                            failureCount++;
                            if (i == 9) {
                                sendEffectFailCombine(player);
//                                Service.getInstance().sendThongBao(player, "Xịt");
                                // Cộng toàn bộ số lần thất bại vào option 250 nếu thất bại hết 10 lần
                                optionFailureCount.param += failureCount;
                                Service.getInstance().sendThongBao(player, "Bạn đã đập " + (i + 1) + " lần, với " + failureCount + " lần thất bại.");
                            }
                        }
                    }
                }
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void phaLeHoaTrangBix100(Player player) {
        if (!player.combineNew.itemsCombine.isEmpty()) {
            long gold = player.combineNew.goldCombine;
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gold < (gold * 100)) {
                Service.getInstance().sendThongBao(player, "Bạn không đủ vàng, còn thiếu " + Util.numberToMoney(gold * 100) + " vàng nữa");
                return;
            } else if (player.inventory.gem < (gem * 100)) {
                Service.getInstance().sendThongBao(player, "Bạn không đủ vàng, còn thiếu " + Util.mumberToLouis(gem * 100) + " ngọc nữa");
                return;
            }
            Item item = player.combineNew.itemsCombine.get(0);
            if (isTrangBiPhaLeHoa(item)) {
                int star = 0;
                ItemOption optionStar = null;
                ItemOption optionFailureCount = null; // Khai báo biến để lưu Option ID 250
                for (ItemOption io : item.itemOptions) {
                    if (io.optionTemplate.id == 107) {
                        star = io.param;
                        optionStar = io;
                    } else if (io.optionTemplate.id == 250) {
                        optionFailureCount = io; // Tìm option ID 250
                    }
                }
                // Nếu item chưa có option ID 250, thêm mới
                if (optionFailureCount == null) {
                    optionFailureCount = new ItemOption(250, 0);
                    item.itemOptions.add(optionFailureCount);
                }
                if (star < MAX_STAR_ITEM) {
                    int failureCount = 0; // Biến đếm số lần thất bại
                    for (int i = 0; i < 100; i++) {
                        player.inventory.gold -= gold;
                        player.inventory.subGem(gem);
                        if (Util.isTrue(player.combineNew.ratioCombine, 400)) {
                            if (optionStar == null) {
                                item.itemOptions.add(new ItemOption(107, 1));
                            } else {
                                optionStar.param++;
                            }
                            sendEffectSuccessCombine(player);
                            // Cộng dồn số lần thất bại vào option 250 khi thành công
                            optionFailureCount.param += failureCount;

                            if (optionStar != null && optionStar.param >= 7) {
                                ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa pha lê hóa " + "thành công " + item.template.name + " lên " + optionStar.param + " sao pha lê");
                                ServerLog.logCombine(player.name, item.template.name, optionStar.param);
                            }
                            Service.getInstance().sendThongBao(player, "Bạn đã đập " + (i + 1) + " lần, với " + failureCount + " lần thất bại.");
                            break;
                        } else {
                            // Tăng biến đếm khi thất bại
                            failureCount++;
                            if (i == 99) {
                                sendEffectFailCombine(player);
//                                Service.getInstance().sendThongBao(player, "Xịt");
                                // Cộng toàn bộ số lần thất bại vào option 250 nếu thất bại hết 100 lần
                                optionFailureCount.param += failureCount;
                                Service.getInstance().sendThongBao(player, "Bạn đã đập " + (i + 1) + " lần, với " + failureCount + " lần thất bại.");
                            }
                        }
                    }
                }
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void nhapNgocRong(Player player) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (!player.combineNew.itemsCombine.isEmpty()) {
                Item item = player.combineNew.itemsCombine.get(0);
                if (item != null && item.isNotNullItem()) {
                    if ((item.template.id > 14 && item.template.id <= 20) && item.quantity >= 7) {
                        Item nr = ItemService.gI().createNewItem((short) (item.template.id - 1));
                        InventoryService.gI().addItemBag(player, nr, 0);
                        InventoryService.gI().subQuantityItemsBag(player, item, 7);
                        InventoryService.gI().sendItemBags(player);
                        reOpenItemCombine(player);
                        sendEffectCombineDB(player, item.template.iconID);
                        return;
                    }
                    if (player.inventory.gold >= 500000000) {
                        if (item.template.id == 14 && item.quantity >= 7) {
                            Item nr = ItemService.gI().createNewItem((short) (1015));
                            InventoryService.gI().addItemBag(player, nr, 0);
                            sendEffectCombineDB(player, (short) 9650);
                        } else if (item.template.id == 926 && item.quantity >= 7) {
                            Item nr = ItemService.gI().createNewItem((short) (925));
                            nr.itemOptions.add(new ItemOption(93, 70));
                            InventoryService.gI().addItemBag(player, nr, 0);
                            sendEffectCombineDB(player, item.template.iconID);
                        }
                        InventoryService.gI().subQuantityItemsBag(player, item, 7);
                        player.inventory.gold -= 500000000;
                        Service.getInstance().sendMoney(player);
                        InventoryService.gI().sendItemBags(player);
                        reOpenItemCombine(player);
                    } else {
                        Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                + Util.numberToMoney(500000000 - player.inventory.gold) + " vàng");
                    }
                }
            }
        }
    }

    private void nhapNgocRongTranh(Player player) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (!player.combineNew.itemsCombine.isEmpty()) {
                Item item = player.combineNew.itemsCombine.get(0);
                if (item != null && item.isNotNullItem()) {
                    if ((item.template.id > 1558 && item.template.id <= 1564) && item.quantity >= 7) {
                        Item nr = ItemService.gI().createNewItem((short) (item.template.id - 1));
                        InventoryService.gI().addItemBag(player, nr, 0);
                        InventoryService.gI().subQuantityItemsBag(player, item, 7);
                        InventoryService.gI().sendItemBags(player);
                        reOpenItemCombine(player);
                        sendEffectCombineDB(player, item.template.iconID);
                        return;
                    }
                    if (player.inventory.gold >= 500000000) {
                        if (item.template.id == 1558 && item.quantity >= 7) {
                            Item nr = ItemService.gI().createNewItem((short) (1015));
                            InventoryService.gI().addItemBag(player, nr, 0);
                            sendEffectCombineDB(player, (short) 9650);
                        } else if (item.template.id == 926 && item.quantity >= 7) {
                            Item nr = ItemService.gI().createNewItem((short) (925));
                            nr.itemOptions.add(new ItemOption(93, 70));
                            InventoryService.gI().addItemBag(player, nr, 0);
                            sendEffectCombineDB(player, item.template.iconID);
                        }
                        InventoryService.gI().subQuantityItemsBag(player, item, 7);
                        player.inventory.gold -= 500000000;
                        Service.getInstance().sendMoney(player);
                        InventoryService.gI().sendItemBags(player);
                        reOpenItemCombine(player);
                    } else {
                        Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                + Util.numberToMoney(500000000 - player.inventory.gold) + " vàng");
                    }
                }
            }
        }
    }

    private void nhapda(Player player) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (!player.combineNew.itemsCombine.isEmpty()) {
                Item item = player.combineNew.itemsCombine.get(0);
                if (item != null && item.isNotNullItem()) {
                    int itemId = item.template.id;

                    // Xử lý item ID 225 với số lượng x99 để tạo ID ngẫu nhiên từ 220 đến 224
                    if (itemId == 225 && item.quantity >= 99) {
                        int newItemId = 220 + (int) (Math.random() * 5); // Tạo ID ngẫu nhiên từ 220 đến 224
                        Item nr = ItemService.gI().createNewItem((short) newItemId);
                        InventoryService.gI().addItemBag(player, nr, 0);
                        InventoryService.gI().subQuantityItemsBag(player, item, 99);
                        InventoryService.gI().sendItemBags(player);
                        reOpenItemCombine(player);
                        sendEffectCombineDB(player, item.template.iconID);
                        return;
                    }

                    // Xử lý trường hợp có đủ vàng
                    if (player.inventory.gold >= 500000000) {
                        if (itemId == 14 && item.quantity >= 7) {
                            Item nr = ItemService.gI().createNewItem((short) (1015));
                            InventoryService.gI().addItemBag(player, nr, 0);
                            sendEffectCombineDB(player, (short) 9650);
                        } else if (itemId == 926 && item.quantity >= 7) {
                            Item nr = ItemService.gI().createNewItem((short) (925));
                            nr.itemOptions.add(new ItemOption(93, 70));
                            InventoryService.gI().addItemBag(player, nr, 0);
                            sendEffectCombineDB(player, item.template.iconID);
                        }
                        InventoryService.gI().subQuantityItemsBag(player, item, 7);
                        player.inventory.gold -= 500000000;
                        Service.getInstance().sendMoney(player);
                        InventoryService.gI().sendItemBags(player);
                        reOpenItemCombine(player);
                    } else {
                        Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                + Util.numberToMoney(500000000 - player.inventory.gold) + " vàng");
                    }
                }
            }
        }
    }

    private void nangCapSKH(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {

            Item SKH = null, doThan = null, daNangCap = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.isNotNullItem()) {
                    if (isDancskh(item)) {
                        doThan = item;
                    }
                    if (item.template.id == 1314) {
                        daNangCap = item;
                    }
                    for (int i = 0; i < item.itemOptions.size(); i++) {
                        for (int option = 127; option <= 135; option++) {
                            if (checkHaveOption(item, i, option)) {
                                SKH = item;
                                break;
                            }
                        }
                    }
                }
            }

            int levelTrangBi = 0;

            int paramSKH = 0;

            for (ItemOption io : SKH.itemOptions) {
                for (int option = 127; option <= 135; option++) {
                    if (io.optionTemplate.id == option) {
                        levelTrangBi = io.param;
                    }
                }
                for (int option = 136; option <= 144; option++) {
                    if (io.optionTemplate.id == option) {
                        paramSKH = io.param;
                    }
                }
            }
            //Start Combie//
            if (SKH != null && doThan != null && daNangCap != null) {
                if (player.inventory.gold >= player.combineNew.goldCombine) {
                    if (Util.isTrue(50, 100)) {
                        for (int i = 1; i < SKH.itemOptions.size(); i++) {
                            if (SKH.itemOptions.get(i).optionTemplate.id >= 127 && SKH.itemOptions.get(i).optionTemplate.id <= 135) {
                                SKH.itemOptions.get(i).param += 1;
                            }
                            if (SKH.itemOptions.get(i).optionTemplate.id >= 136 && SKH.itemOptions.get(i).optionTemplate.id <= 144) {
                                SKH.itemOptions.get(i).param += 10;
                            }
                        }
                        InventoryService.gI().subQuantityItemsBag(player, doThan, 1);
                        InventoryService.gI().subQuantityItemsBag(player, daNangCap, 1);
                        InventoryService.gI().sendItemBags(player);
                        player.inventory.gold -= 2000000000L;
                        Service.getInstance().sendMoney(player);
                        sendEffectSuccessCombine(player);
                        reOpenItemCombine(player);
                        player.combineNew.itemsCombine.clear();
                        return;
                    } else {
                        InventoryService.gI().subQuantityItemsBag(player, doThan, 1);
                        InventoryService.gI().subQuantityItemsBag(player, daNangCap, 1);
                        InventoryService.gI().sendItemBags(player);
                        player.inventory.gold -= 2000000000L;
                        Service.getInstance().sendMoney(player);
                        sendEffectFailCombine(player);
                        reOpenItemCombine(player);
                        player.combineNew.itemsCombine.clear();
                        return;
                    }
                } else {
                    Service.getInstance().sendThongBao(player, "Không đủ vàng");
                }
            } else {
                Service.getInstance().sendThongBao(player, "Không đủ vật phẩm");
            }
            //End Combie//
        }
    }

    private void nangCapVatPham(Player player) {
        if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 4) {
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 5).count() != 1) {
                return;
            }
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 14).count() != 1) {
                return;
            }
            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 987).count() != 1) {
                return;
            }
            Item itemDo = null;
            Item itemDNC = null;
            Item itemDBV = null;
            for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.get(j).template.id == 987) {
                        itemDBV = player.combineNew.itemsCombine.get(j);
                        continue;
                    }
                    if (player.combineNew.itemsCombine.get(j).template.type < 5) {
                        itemDo = player.combineNew.itemsCombine.get(j);
                    } else {
                        itemDNC = player.combineNew.itemsCombine.get(j);
                    }
                }
            }
            if (isCoupleItemNangCapCheck(itemDo, itemDNC)) {
                int countDaNangCap = player.combineNew.countDaNangCap;
                int gold = player.combineNew.goldCombine;
                short countDaBaoVe = player.combineNew.countDaBaoVe;
                if (player.inventory.gold < gold) {
                    Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                    return;
                }
                if (itemDNC.quantity < countDaNangCap) {
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 3) {
                    if (Objects.isNull(itemDBV)) {
                        return;
                    }
                    if (itemDBV.quantity < countDaBaoVe) {
                        return;
                    }
                }
                int level = 0;
                ItemOption optionLevel = null;
                for (ItemOption io : itemDo.itemOptions) {
                    if (io.optionTemplate.id == 72) {
                        level = io.param;
                        optionLevel = io;
                        break;
                    }
                }
                if (level < MAX_LEVEL_ITEM) {
                    player.inventory.gold -= gold;
                    ItemOption option = null;
                    ItemOption option2 = null;
                    for (ItemOption io : itemDo.itemOptions) {
                        if (io.optionTemplate.id == 47
                                || io.optionTemplate.id == 6
                                || io.optionTemplate.id == 0
                                || io.optionTemplate.id == 7
                                || io.optionTemplate.id == 14
                                || io.optionTemplate.id == 22
                                || io.optionTemplate.id == 23) {
                            option = io;
                        } else if (io.optionTemplate.id == 27 || io.optionTemplate.id == 28) {
                            option2 = io;
                        }
                    }
                    if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                        if (option.optionTemplate.id == 14) {
                            option.param += 1;
                        } else {
                            option.param += (option.param * 10 / 100);
                        }
                        if (option2 != null) {
                            option2.param += (option2.param * 10 / 100);
                        }
                        if (optionLevel == null) {
                            itemDo.itemOptions.add(new ItemOption(72, 1));
                        } else {
                            optionLevel.param++;
                        }
                        if (optionLevel != null && optionLevel.param >= 5) {
                            ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa nâng cấp "
                                    + "thành công " + itemDo.template.name + " lên +" + optionLevel.param);
                        }
                        sendEffectSuccessCombine(player);
                    } else {
                        if ((level == 2 || level == 4 || level == 6) && (player.combineNew.itemsCombine.size() != 3)) {
                            if (option.optionTemplate.id == 14) {
                                option.param -= 1;
                            } else {
                                option.param -= (option.param * 15 / 100);
                            }
                            if (option2 != null) {
                                option2.param -= (option2.param * 15 / 100);
                            }
                            optionLevel.param--;
                        }
                        sendEffectFailCombine(player);
                    }
                    if (player.combineNew.itemsCombine.size() == 3) {
                        InventoryService.gI().subQuantityItemsBag(player, itemDBV, countDaBaoVe);
                    }
                    InventoryService.gI().subQuantityItemsBag(player, itemDNC, player.combineNew.countDaNangCap);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    // --------------------------------------------------------------------------
    /**
     * Hiệu ứng mở item
     *
     * @param player
     */
    public void sendEffectOpenItem(Player player, short icon1, short icon2) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(OPEN_ITEM);
            msg.writer().writeShort(icon1);
            msg.writer().writeShort(icon2);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiệu ứng đập đồ thành công
     *
     * @param player
     */
    public void sendEffectSuccessCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_SUCCESS);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendeffcombine7(Player player, short iconIdWhenSucces, int idNpc) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(8);
            msg.writer().writeShort(iconIdWhenSucces);
            msg.writer().writeShort(idNpc);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    private void sendEffectCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(8);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Hiệu ứng đập đồ thất bại
     *
     * @param player
     */
    private void sendEffectFailCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_FAIL);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Gửi lại danh sách đồ trong tab combine
     *
     * @param player
     */
    public void reOpenItemCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(REOPEN_TAB_COMBINE);
            msg.writer().writeByte(player.combineNew.itemsCombine.size());
            for (Item it : player.combineNew.itemsCombine) {
                for (int j = 0; j < player.inventory.itemsBag.size(); j++) {
                    if (it == player.inventory.itemsBag.get(j)) {
                        msg.writer().writeByte(j);
                    }
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiệu ứng ghép ngọc rồng
     *
     * @param player
     * @param icon
     */
    private void sendEffectCombineDB(Player player, short icon) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_DRAGON_BALL);
            msg.writer().writeShort(icon);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    // --------------------------------------------------------------------------Ratio,
    // cost combine
    private int getRatioDaMayMan(int id) {
        switch (id) {
            case 1079:
                return 10;
            case 1080:
                return 20;
            case 1081:
                return 30;
            case 1082:
                return 40;
            case 1083:
                return 50;
        }
        return 0;
    }

    private int getCountDaBaoVe(int level) {
        return level + 1;
    }

    private int getRatioDaNangCap(int id) {
        switch (id) {
            case 1074:
                return 10;
            case 1075:
                return 20;
            case 1076:
                return 30;
            case 1077:
                return 40;
            case 1078:
                return 50;
        }
        return 0;
    }

    private int getGoldPhaLeHoa(int star) {
        switch (star) {
            case 0:
                return 5000000;
            case 1:
                return 10000000;
            case 2:
                return 20000000;
            case 3:
                return 40000000;
            case 4:
                return 60000000;
            case 5:
                return 90000000;
            case 6:
                return 120000000;
            case 7:
                return 200000000;
        }
        return 0;
    }

    private float getRatioPhaLeHoa(int star) {
        switch (star) {
            case 0:
                return 80f;
            case 1:
                return 50f;
            case 2:
                return 40f;
            case 3:
                return 30f;
            case 4:
                return 20f;
            case 5:
                return 10f;
            case 6:
                return 5f;
            case 7:
                return 2f;
//            case 8:
//                return 0.8f;
//            case 6:
//                return 0.2f;
//            case 7:
//                return 0.1f;
        }
        return 0;
    }

    private float getRatioPhapSuHoa(int star) {
        switch (star) {
            case 0:
                return 50f;
            case 1:
                return 40f;
            case 2:
                return 30f;
            case 3:
                return 20f;
            case 4:
                return 15f;
            case 5:
                return 10f;
            case 6:
                return 8f;
            case 7:
                return 5f;
//            case 8:
//                return 0.8f;
//            case 6:
//                return 0.2f;
//            case 7:
//                return 0.1f;
        }
        return 0;
    }

    private float getTileNangHonHoan(int level) {
        switch (level) {
            case 1407:
                return 50f;
            case 1408:
                return 40f;
            case 1409:
                return 30f;
            case 1410:
                return 20f;
            case 1411:
                return 15f;
            case 1412:
                return 10f;
            case 1413:
                return 8f;
            case 1414:
                return 5f;
        }
        return 0;
    }

    private int getGemPhaLeHoa(int star) {
        switch (star) {
            case 0:
                return 10;
            case 1:
                return 20;
            case 2:
                return 30;
            case 3:
                return 40;
            case 4:
                return 50;
            case 5:
                return 60;
            case 6:
                return 70;
            case 7:
                return 80;
//            case 8:
//                return 90;
        }
        return 0;
    }

    private int getGemEpSao(int star) {
        switch (star) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 5;
            case 3:
                return 10;
            case 4:
                return 25;
            case 5:
                return 50;
            case 6:
                return 100;
        }
        return 0;
    }

    private int getTileNangCapDo(int level) {
        switch (level) {
            case 0:
                return 80;
            case 1:
                return 50;
            case 2:
                return 20;
            case 3:
                return 10;
            case 4:
                return 7;
            case 5:
                return 3;
            case 6:
                return 1;
        }
        return 0;
    }

    private int getCountDaNangCapDo(int level) {
        switch (level) {
            case 0:
                return 3;
            case 1:
                return 7;
            case 2:
                return 11;
            case 3:
                return 17;
            case 4:
                return 23;
            case 5:
                return 35;
            case 6:
                return 50;
        }
        return 0;
    }

    private int lvbt(Item bongtai) {
        switch (bongtai.template.id) {
            case 454:
                return 1;
            case 921:
                return 2;
        }
        return 0;

    }

    private int getGoldNangCapDo(int level) {
        switch (level) {
            case 0:
                return 10000;
            case 1:
                return 70000;
            case 2:
                return 300000;
            case 3:
                return 1500000;
            case 4:
                return 7000000;
            case 5:
                return 23000000;
            case 6:
                return 100000000;
        }
        return 0;
    }

    private int getngusacKhamDa(int star) {
        switch (star) {
            case 0:
                return 5;
            case 1:
                return 10;
            case 2:
                return 20;
            case 3:
                return 40;
            case 4:
                return 60;
            case 5:
                return 80;
            case 6:
                return 100;
            case 7:
                return 150;
            case 8:
                return 200;
            case 9:
                return 300;
        }
        return 0;
    }

    private float getRatioPhaLeHoa2(int star) { //tile nang cap pet2
        switch (star) {
            case 0:
                return 50f;
            case 1:
                return 50f;
            case 2:
                return 20f;
            case 3:
                return 7f;
            case 4:
                return 3f;
            case 5:
                return 0.6f;
            case 6:
                return 0.45f;//1f;
            case 7:
                return 0.25f;//0.5f;
            case 8:
                return 0.5f;
            case 9:
                return 0.7f;
            case 10:
                return 0.5f;
            case 11:
                return 0.03f;
            case 12:
                return 0.1f;
        }

        return 0;
    }

    // --------------------------------------------------------------------------check
    public boolean isAngelClothes(int id) {
        if (id >= 1048 && id <= 1062) {
            return true;
        }
        return false;
    }

    public boolean isDestroyClothes(int id) {
        if (id >= 650 && id <= 662) {
            return true;
        }
        return false;
    }

    private String getTypeTrangBi(int type) {
        switch (type) {
            case 0:
                return "Áo";
            case 1:
                return "Quần";
            case 2:
                return "Găng";
            case 3:
                return "Giày";
            case 4:
                return "Nhẫn";
        }
        return "";
    }

    public boolean isManhTrangBi(Item it) {
        switch (it.template.id) {
            case 1066:
            case 1067:
            case 1068:
            case 1069:
            case 1070:
                return true;
        }
        return false;
    }

    public boolean isCraftingRecipe(int id) {
        switch (id) {
            case 1071:
            case 1072:
            case 1073:
            case 1084:
            case 1085:
            case 1086:
                return true;
        }
        return false;
    }

    public int getRatioCraftingRecipe(int id) {
        switch (id) {
            case 1071:
                return 0;
            case 1072:
                return 0;
            case 1073:
                return 0;
            case 1084:
                return 10;
            case 1085:
                return 10;
            case 1086:
                return 10;
        }
        return 0;
    }

    public boolean isUpgradeStone(int id) {
        switch (id) {
            case 1074:
            case 1075:
            case 1076:
            case 1077:
            case 1078:
                return true;
        }
        return false;
    }

    public int getRatioUpgradeStone(int id) {
        switch (id) {
            case 1074:
                return 10;
            case 1075:
                return 20;
            case 1076:
                return 30;
            case 1077:
                return 40;
            case 1078:
                return 50;
        }
        return 0;
    }

    public boolean isLuckyStone(int id) {
        switch (id) {
            case 1079:
            case 1080:
            case 1081:
            case 1082:
            case 1083:
                return true;
        }
        return false;
    }

    private int getGoldnangbt(int lvbt) {
        return GOLD_BONG_TAI2;
    }

    private int getgemdnangbt(int lvbt) {
        return GEM_BONG_TAI2;
    }

    private int getcountmvbtnangbt(int lvbt) {
        return 9999;
    }

    private boolean checkBongTaiNangCap(Item item) {
        if (item.template.id == 454) {
            return true;
        }
        return false;
    }

    public int getRatioLuckyStone(int id) {
        switch (id) {
            case 1079:
                return 10;
            case 1080:
                return 20;
            case 1081:
                return 30;
            case 1082:
                return 40;
            case 1083:
                return 50;
        }
        return 0;
    }

    private boolean isCoupleItemNangCap(Item item1, Item item2) {
        Item trangBi = null;
        Item daNangCap = null;
        if (item1 != null && item1.isNotNullItem()) {
            if (item1.template.type < 5) {
                trangBi = item1;
            } else if (item1.template.type == 14) {
                daNangCap = item1;
            }
        }
        if (item2 != null && item2.isNotNullItem()) {
            if (item2.template.type < 5) {
                trangBi = item2;
            } else if (item2.template.type == 14) {
                daNangCap = item2;
            }
        }
        if (trangBi != null && daNangCap != null) {
            if (trangBi.template.type == 0 && daNangCap.template.id == 223) {
                return true;
            } else if (trangBi.template.type == 1 && daNangCap.template.id == 222) {
                return true;
            } else if (trangBi.template.type == 2 && daNangCap.template.id == 224) {
                return true;
            } else if (trangBi.template.type == 3 && daNangCap.template.id == 221) {
                return true;
            } else if (trangBi.template.type == 4 && daNangCap.template.id == 220) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isDaPhaLe(Item item) {
        return item.template.type == 30 || (item.template.id >= 14 && item.template.id <= 20);
    }

    private boolean isDaPhaLe2(Item item) {
        return item != null && (item.template.id >= 807 && item.template.id <= 813) || (item.template.id >= 1484 && item.template.id <= 1486);
    }

    public boolean isDTL(Item item) {
        if (item.template == null) {
            return false;
        }
        if (item.template.id == 555 || item.template.id == 556 || item.template.id == 562 || item.template.id == 563 || item.template.id == 561) {
            return true;
        }
        if (item.template.id == 559 || item.template.id == 560 || item.template.id == 566 || item.template.id == 567 || item.template.id == 561) {
            return true;
        }
        if (item.template.id == 557 || item.template.id == 558 || item.template.id == 564 || item.template.id == 565 || item.template.id == 561) {
            return true;
        }
        return false;
    }

    private Item checkItemCanCombine(Player player) {
        List<Item> itemsCombine = player.combineNew.itemsCombine;

        if (itemsCombine.size() != 1) {
            this.baHatMit.createOtherMenu(player, ConstNpc.BA_HAT_MIT, "Cần 1 ô trống trong hành trang", "Đóng");
            return null;
        }
        Item itemCombine = itemsCombine.get(0);

        if (!itemCombine.isNotNullItem()) {
            this.baHatMit.createOtherMenu(player, ConstNpc.BA_HAT_MIT, "Thiếu item", "Đóng");
            return null;
        } else if (!itemCombine.itemCanRemoveOption()) {
            this.baHatMit.createOtherMenu(player, ConstNpc.BA_HAT_MIT, "Chỉ có thể tẩy trang bị: quần áo găng giày rada \nvà trang bị phải có sao pha lê ", "Đóng");
            return null;
        } else {
            if (player.inventory.ruby < ConstCombine.COST_REMOVE_OPTION) {
                this.baHatMit.createOtherMenu(player, ConstNpc.BA_HAT_MIT, "Cần 20k hồng ngọc", "Đóng");
                return null;
            } else {
                return itemCombine;
            }
        }
    }

    public boolean isDancskh(Item item) {
        if (item.template == null) {
            return false;
        }
        if (item.template.id == 1502) {
            return true;
        }
        return false;
    }

    public boolean isDanc(Item item) {
        if (item.template == null) {
            return false;
        }
        if (item.template.id == 1341) {
            return true;
        }
        return false;
    }

    private boolean isTrangBiAn(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.id >= 1048 && item.template.id <= 1062) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isTrangBiPhaLeHoa(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.type < 6 && item.template.type != 5 || item.template.type == 32) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isTrangBiPhaLeHoa2(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.type < 6 || item.template.type == 32) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isTrangBiGoc(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (isDoLuongLong(item) || isDoJean(item) || isDoZelot(item) || isDoThanXD(item) || isDoThanTD(item) || isDoThanNM(item)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isTrangBiChuyenHoa(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (isDoThanXD(item) || isDoThanTD(item) || isDoThanNM(item)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isCheckTrungTypevsGender(Item item, Item item2) {
        if (item != null && item.isNotNullItem() && item2 != null && item2.isNotNullItem()) {
            if (item.template.type == item2.template.type && item.template.gender == item2.template.gender) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isDoLuongLong(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.id == 241 || item.template.id == 253 || item.template.id == 265 || item.template.id == 277 || item.template.id == 281) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isDoZelot(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.id == 241 - 4 || item.template.id == 253 - 4 || item.template.id == 265 - 4 || item.template.id == 277 - 4 || item.template.id == 281) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isDoJean(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.id == 241 - 8 || item.template.id == 253 - 8 || item.template.id == 265 - 8 || item.template.id == 277 - 8 || item.template.id == 281) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isDoThanXD(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.id == 559 || item.template.id == 560 || item.template.id == 566 || item.template.id == 567 || item.template.id == 561) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isDoThanTD(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.id == 559 - 4 || item.template.id == 560 - 4 || item.template.id == 566 - 4 || item.template.id == 567 - 4 || item.template.id == 561) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isDoThanNM(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.id == 559 - 2 || item.template.id == 560 - 2 || item.template.id == 566 - 2 || item.template.id == 567 - 2 || item.template.id == 561) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean issachTuyetKy(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.type == 35) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean checkHaveOption(Item item, int viTriOption, int idOption) {
        if (item != null && item.isNotNullItem()) {
            if (item.itemOptions.get(viTriOption).optionTemplate.id == idOption) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private int getParamDaPhaLe(Item daPhaLe) {
        if (daPhaLe.template.type == 30) {
            return daPhaLe.itemOptions.get(0).param;
        }
        switch (daPhaLe.template.id) {
            case 20:
                return 5; // +5%hp
            case 19:
                return 5; // +5%ki
            case 18:
                return 5; // +5%hp/30s
            case 17:
                return 5; // +5%ki/30s
            case 16:
                return 3; // +3%sđ
            case 15:
                return 2; // +2%giáp
            case 14:
                return 2; // +2%né đòn
            default:
                return -1;
        }
    }

    private int getOptionDaPhaLe(Item daPhaLe) {
        if (daPhaLe.template.type == 30) {
            return daPhaLe.itemOptions.get(0).optionTemplate.id;
        }
        switch (daPhaLe.template.id) {
            case 20:
                return 77;
            case 19:
                return 103;
            case 18:
                return 80;
            case 17:
                return 81;
            case 16:
                return 50;
            case 15:
                return 94;
            case 14:
                return 108;
            default:
                return -1;
        }
    }

    private int getParamDaPhaLe2(Item daPhaLe2) {
        if (daPhaLe2.template.type == 30) {
            return daPhaLe2.itemOptions.get(0).param;
        }
        switch (daPhaLe2.template.id) {
            case 813:
                return 10; // +10%hp
            case 812:
                return 10; // +10%ki
            case 811:
                return 15; // +15%hp/30s
            case 810:
                return 15; // +15%ki/30s
            case 809:
                return 6; // +6%sđ
            case 808:
                return 9; // +9%giáp
            case 807:
                return 3; // +3%né đòn            
            default:
                return -1;
        }
    }

    private int getOptionDaPhaLe2(Item daPhaLe2) {
        if (daPhaLe2.template.type == 30) {
            return daPhaLe2.itemOptions.get(0).optionTemplate.id;
        }
        switch (daPhaLe2.template.id) {
            case 813:
                return 77;
            case 812:
                return 103;
            case 811:
                return 80;
            case 810:
                return 81;
            case 809:
                return 50;
            case 808:
                return 94;
            case 807:
                return 108;
            default:
                return -1;
        }
    }

    /**
     * Trả về id item c0
     *
     * @param gender
     * @param type
     * @return
     */
    private int getOptionSetKichHoat(Item item) {
        for (ItemOption opt : item.itemOptions) {
            if (OPTION_SET_KICH_HOAT.contains(opt.optionTemplate.id)) {
                return opt.optionTemplate.id;
            }
        }
        return -1;
    }
    private static final Set<Integer> OPTION_SET_KICH_HOAT = Set.of( 128, 129, 130, 131, 133, 135,248);
    private static final Set<Integer> OPTION_HIEU_UNG_SET = Set.of(136, 138, 140, 141, 142, 143, 249); // bạn bổ sung đúng các ID set hiệu ứng

    private int getTempIdItemKHVIP(int gender, int type, int typeItem) {
        if (type == 4) {
            switch (typeItem) {
                case 0:
                    return 184;
                case 1:
                    return 186;
                case 2:
                    return 279;
                case 3:
                    return 281;
                case 4:
                    return 561;
            }
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        switch (typeItem) {
                            case 0:
                                return 136;
                            case 1:
                                return 138;
                            case 2:
                                return 230;
                            case 3:
                                return 232;
                            case 4:
                                return 555;
                        }
                        break;
                    case 1:
                        switch (typeItem) {
                            case 0:
                                return 140;
                            case 1:
                                return 142;
                            case 2:
                                return 242;
                            case 3:
                                return 244;
                            case 4:
                                return 556;
                        }
                        break;
                    case 2:
                        switch (typeItem) {
                            case 0:
                                return 144;
                            case 1:
                                return 146;
                            case 2:
                                return 254;
                            case 3:
                                return 256;
                            case 4:
                                return 562;
                        }
                        break;
                    case 3:
                        switch (typeItem) {
                            case 0:
                                return 148;
                            case 1:
                                return 150;
                            case 2:
                                return 266;
                            case 3:
                                return 268;
                            case 4:
                                return 563;
                        }
                        break;
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        switch (typeItem) {
                            case 0:
                                return 152;
                            case 1:
                                return 154;
                            case 2:
                                return 234;
                            case 3:
                                return 236;
                            case 4:
                                return 557;
                        }
                        break;
                    case 1:
                        switch (typeItem) {
                            case 0:
                                return 156;
                            case 1:
                                return 158;
                            case 2:
                                return 246;
                            case 3:
                                return 248;
                            case 4:
                                return 558;
                        }
                        break;
                    case 2:
                        switch (typeItem) {
                            case 0:
                                return 160;
                            case 1:
                                return 162;
                            case 2:
                                return 258;
                            case 3:
                                return 260;
                            case 4:
                                return 564;
                            case 5:
                                return 659;
                        }
                        break;
                    case 3:
                        switch (typeItem) {
                            case 0:
                                return 164;
                            case 1:
                                return 166;
                            case 2:
                                return 270;
                            case 3:
                                return 272;
                            case 4:
                                return 565;
                        }
                        break;
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        switch (typeItem) {
                            case 0:
                                return 168;
                            case 1:
                                return 170;
                            case 2:
                                return 238;
                            case 3:
                                return 240;
                            case 4:
                                return 559;
                        }
                        break;
                    case 1:
                        switch (typeItem) {
                            case 0:
                                return 172;
                            case 1:
                                return 174;
                            case 2:
                                return 250;
                            case 3:
                                return 252;
                            case 4:
                                return 560;
                        }
                        break;
                    case 2:
                        switch (typeItem) {
                            case 0:
                                return 176;
                            case 1:
                                return 178;
                            case 2:
                                return 262;
                            case 3:
                                return 264;
                            case 4:
                                return 566;
                        }
                        break;
                    case 3:
                        switch (typeItem) {
                            case 0:
                                return 180;
                            case 1:
                                return 182;
                            case 2:
                                return 274;
                            case 3:
                                return 276;
                            case 4:
                                return 567;
                        }
                        break;
                }
                break;
        }
        return -1;
    }

    private String getNameIdItemKHVIP(int gender, int type, int typeItem) {
        if (type == 4) {
            switch (typeItem) {
                case 0:
                    return "Rada cấp 5";
                case 1:
                    return "Rada cấp 7";
                case 2:
                    return "Rada cấp 9";
                case 3:
                    return "Rada cấp 11";
                case 4:
                    return "Nhẫn Thần Linh";
            }
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        switch (typeItem) {
                            case 0:
                                return "Áo vải Kame";
                            case 1:
                                return "Áo võ Kame";
                            case 2:
                                return "Áo bạc Goku";
                            case 3:
                                return "Áo da Calic";
                            case 4:
                                return "Áo Thần Linh";
                        }
                        break;
                    case 1:
                        switch (typeItem) {
                            case 0:
                                return "Quần vải Kame";
                            case 1:
                                return "Quần võ Kame";
                            case 2:
                                return "Quần bạc Goku";
                            case 3:
                                return "Quần da Calic";
                            case 4:
                                return "Quần Thần Linh";
                        }
                        break;
                    case 2:
                        switch (typeItem) {
                            case 0:
                                return "Găng vải Kame";
                            case 1:
                                return "Găng võ Kame";
                            case 2:
                                return "Găng bạc Goku";
                            case 3:
                                return "Găng da Calic";
                            case 4:
                                return "Găng Thần Linh";
                        }
                        break;
                    case 3:
                        switch (typeItem) {
                            case 0:
                                return "Giày nhựa Kame";
                            case 1:
                                return "Giày võ Kame";
                            case 2:
                                return "Giày bạc Goku";
                            case 3:
                                return "Giày da Calic";
                            case 4:
                                return "Giày Thần Linh";
                        }
                        break;
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        switch (typeItem) {
                            case 0:
                                return "Áo choàn len";
                            case 1:
                                return "Áo vải Pico";
                            case 2:
                                return "Áo sắt Tron";
                            case 3:
                                return "Áo Bạc Zealot";
                            case 4:
                                return "Áo Thần Namếc";
                        }
                        break;
                    case 1:
                        switch (typeItem) {
                            case 0:
                                return "Quần len cứng";
                            case 1:
                                return "Quần vải cứng Pico";
                            case 2:
                                return "Quần sắt Tron";
                            case 3:
                                return "Quần Bạc Zealot";
                            case 4:
                                return "Quần Thần Namếc";
                        }
                        break;
                    case 2:
                        switch (typeItem) {
                            case 0:
                                return "Găng len cứng";
                            case 1:
                                return "Găng vải Pico";
                            case 2:
                                return "Găng sắt Tron";
                            case 3:
                                return "Găng Bạc Zealot";
                            case 4:
                                return "Găng Thần Namếc";
                        }
                        break;
                    case 3:
                        switch (typeItem) {
                            case 0:
                                return "Giày nhựa cứng";
                            case 1:
                                return "Giày da Pico";
                            case 2:
                                return "Giày sắt Tron";
                            case 3:
                                return "Giày Bạc Zealot";
                            case 4:
                                return "Giày Thần Namếc";
                        }
                        break;
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        switch (typeItem) {
                            case 0:
                                return "Áo giáp bạc";
                            case 1:
                                return "Áo lông Xayda";
                            case 2:
                                return "Áo lông đỏ";
                            case 3:
                                return "Áo Kaio";
                            case 4:
                                return "Áo Thần Xayda";
                        }
                        break;
                    case 1:
                        switch (typeItem) {
                            case 0:
                                return "Quần giáp bạc";
                            case 1:
                                return "Quần lông Xayda";
                            case 2:
                                return "Quần lông đỏ";
                            case 3:
                                return "Quần Kaio";
                            case 4:
                                return "Quần Thần Xayda";
                        }
                        break;
                    case 2:
                        switch (typeItem) {
                            case 0:
                                return "Găng giáp bạc";
                            case 1:
                                return "Găng lông Xayda";
                            case 2:
                                return "Găng lông đỏ";
                            case 3:
                                return "Găng Kaio";
                            case 4:
                                return "Găng Thần Xayda";
                        }
                        break;
                    case 3:
                        switch (typeItem) {
                            case 0:
                                return "Giày giáp bạc";
                            case 1:
                                return "Giày lông Xayda";
                            case 2:
                                return "Giày lông đỏ";
                            case 3:
                                return "Giày Kaio";
                            case 4:
                                return "Giày Thần Xayda";
                        }
                        break;
                }
                break;
        }
        return "";
    }

    private int getCapDoKHVIP(int gender, int type, int idItem) {
        if (type == 4) {
            switch (idItem) {
                case 184:
                    return 0;
                case 186:
                    return 1;
                case 279:
                    return 2;
                case 281:
                    return 3;
                case 561:
                    return 4;
            }
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        switch (idItem) {
                            case 136:
                                return 0;
                            case 138:
                                return 1;
                            case 230:
                                return 2;
                            case 232:
                                return 3;
                            case 555:
                                return 4;
                        }
                        break;
                    case 1:
                        switch (idItem) {
                            case 140:
                                return 0;
                            case 142:
                                return 1;
                            case 242:
                                return 2;
                            case 244:
                                return 3;
                            case 556:
                                return 4;
                        }
                        break;
                    case 2:
                        switch (idItem) {
                            case 144:
                                return 0;
                            case 146:
                                return 1;
                            case 254:
                                return 2;
                            case 256:
                                return 3;
                            case 562:
                                return 4;
                            case 657:
                                return 5;
                        }
                        break;
                    case 3:
                        switch (idItem) {
                            case 148:
                                return 0;
                            case 150:
                                return 1;
                            case 266:
                                return 2;
                            case 268:
                                return 3;
                            case 563:
                                return 4;
                        }
                        break;
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        switch (idItem) {
                            case 152:
                                return 0;
                            case 154:
                                return 1;
                            case 234:
                                return 2;
                            case 236:
                                return 3;
                            case 557:
                                return 4;
                        }
                        break;
                    case 1:
                        switch (idItem) {
                            case 156:
                                return 0;
                            case 158:
                                return 1;
                            case 246:
                                return 2;
                            case 248:
                                return 3;
                            case 558:
                                return 4;
                        }
                        break;
                    case 2:
                        switch (idItem) {
                            case 160:
                                return 0;
                            case 162:
                                return 1;
                            case 258:
                                return 2;
                            case 260:
                                return 3;
                            case 564:
                                return 4;
                        }
                        break;
                    case 3:
                        switch (idItem) {
                            case 164:
                                return 0;
                            case 166:
                                return 1;
                            case 270:
                                return 2;
                            case 272:
                                return 3;
                            case 565:
                                return 4;
                        }
                        break;
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        switch (idItem) {
                            case 168:
                                return 0;
                            case 170:
                                return 1;
                            case 238:
                                return 2;
                            case 240:
                                return 3;
                            case 559:
                                return 4;
                        }
                        break;
                    case 1:
                        switch (idItem) {
                            case 172:
                                return 0;
                            case 174:
                                return 1;
                            case 250:
                                return 2;
                            case 252:
                                return 3;
                            case 560:
                                return 4;
                        }
                        break;
                    case 2:
                        switch (idItem) {
                            case 176:
                                return 0;
                            case 178:
                                return 1;
                            case 262:
                                return 2;
                            case 264:
                                return 3;
                            case 566:
                                return 4;
                        }
                        break;
                    case 3:
                        switch (idItem) {
                            case 180:
                                return 0;
                            case 182:
                                return 1;
                            case 274:
                                return 2;
                            case 276:
                                return 3;
                            case 567:
                                return 4;
                        }
                        break;
                }
                break;
        }
        return -1;
    }

    private int getTempIdItemC0(int gender, int type) {
        if (type == 4) {
            return 12;
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        return 0;
                    case 1:
                        return 6;
                    case 2:
                        return 21;
                    case 3:
                        return 27;
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        return 1;
                    case 1:
                        return 7;
                    case 2:
                        return 22;
                    case 3:
                        return 28;
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        return 2;
                    case 1:
                        return 8;
                    case 2:
                        return 23;
                    case 3:
                        return 29;
                }
                break;
        }
        return -1;
    }

    // Trả về tên đồ c0
    private String getNameItemC0(int gender, int type) {
        if (type == 4) {
            return "Rada cấp 1";
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        return "Áo vải 3 lỗ";
                    case 1:
                        return "Quần vải đen";
                    case 2:
                        return "Găng thun đen";
                    case 3:
                        return "Giầy nhựa";
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        return "Áo sợi len";
                    case 1:
                        return "Quần sợi len";
                    case 2:
                        return "Găng sợi len";
                    case 3:
                        return "Giầy sợi len";
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        return "Áo vải thô";
                    case 1:
                        return "Quần vải thô";
                    case 2:
                        return "Găng vải thô";
                    case 3:
                        return "Giầy vải thô";
                }
                break;
        }
        return "";
    }

    public boolean isTrangBiPhapsu(Item item) {
        if (item != null && item.isNotNullItem()) {
            if ((item.template.type == 72 || item.template.type == 5 || item.template.type == 11) && !item.isTrangBiHSD()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isTrangBiGod(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.id >= 555 && item.template.id <= 567) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isdalua(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.id == 1452) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isLinhThu(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.type == 72) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isdanspro(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.id == 1503) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean ispet(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.type == 21) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isItemCaiTrang(Item it) {
        return it.template.type == 5;
    }

    // --------------------------------------------------------------------------Text
    // tab combine
    private String getTextTopTabCombine(int type) {
        switch (type) {
            case CUONG_HOA_LO_SPL:
                return "Ta sẽ cường hóa\n lỗ sao pha lê của ngươi";
            case MO_NOI_TAI_ITEM:
                return "Ta sẽ phù phép\ncho ngươi\nCó những chỉ số đẹp";
            case NANG_CAP_PET:
                return "Ta sẽ phù phép\ncho linh thú của ngươi\nCó những chỉ số đẹp";
            case NANG_PET:
                return "Ta sẽ phù phép\ncho linh thú của ngươi\nCó những chỉ số đẹp";
            case PHAP_SU_HOA:
                return "Pháp sư hóa trang bị\nTa sẽ phù phép cho trang bị\ncủa ngươi trở lên mạnh mẽ";
            case TAY_PHAP_SU:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở về lúc chưa 'Pháp sư hóa'";
            case NANG_CHAN_MENH:
                return "Ta sẽ phù phép cho ngươi chân mệnh thành hồn hoàn cao cấp!";
            case EP_SAO_TRANG_BI:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở lên mạnh mẽ";
            case PHA_LE_HOA_CAI_TRANG:
                return "Ta sẽ giúp ngươi làm điều đó =)))";
            case PHA_LE_HOA_TRANG_BI:
            case PHA_LE_HOA_TRANG_BI_X10:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở thành trang bị pha lê";
            case NHAP_NGOC_RONG:
                return "Ta sẽ phù phép\ncho 7 viên Ngọc Rồng\nthành 1 viên Ngọc Rồng cấp cao";
            case NHAP_NGOC_RONG_TRANH:
                return "Ta sẽ phù phép\ncho 7 viên Ngọc Rồng\nthành 1 viên Ngọc Rồng cấp cao";
            case CHE_TAO_DO_THIEN_SU:
                return "Whis Đã Ban phép cho ta";
            // START_ SÁCH TUYỆT KỸ //
            case GIAM_DINH_SACH:
                return "Ta sẽ phù phép\ngiám định sách đó cho ngươi";
            case TAY_SACH:
                return "Ta sẽ phù phép\ntẩy sách đó cho ngươi";
            case NANG_CAP_SACH_TUYET_KY:
                return "Ta sẽ phù phép\nnâng cấp Sách Tuyệt Kỹ cho ngươi";
            case PHUC_HOI_SACH:
                return "Ta sẽ phù phép\nphục hồi sách cho ngươi";
            case PHAN_RA_SACH:
                return "Ta sẽ phù phép\nphân rã sách cho ngươi";
            // END _ SÁCH TUYỆT KỸ //
            case REMOVE_OPTION:
                return "Ta sẽ giúp ngươi làm điều đó";
            case NANG_CAP_VAT_PHAM:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở lên mạnh mẽ";
            case NANG_CAP_SKH:
                return "Ta sẽ nâng cấp\ncho trang bị của ngươi\ntrở lên mạnh mẽ";
            case DOI_VE_HUY_DIET:
                return "Ta sẽ đưa ngươi 1 vé đổi đồ\nhủy diệt, đổi lại ngươi phải đưa ta\n 1 món đồ thần linh tương ứng";
            case DAP_SET_KICH_HOAT:
                return "Ta sẽ giúp ngươi chuyển hóa\n1 món đồ thần linh\nthành 1 món đồ kích hoạt";
            // case DOI_MANH_KICH_HOAT:
            // return "Ta sẽ giúp ngươi biến hóa\nviên ngọc 1 sao và 1 món đồ\nthần linh
            // thành mảnh kích hoạt";
            case DAP_SET_KICH_HOAT_CAO_CAP:
                return "Ta sẽ giúp ngươi chuyển hóa\n2 món đồ kích hoạt giống nhau\nthành 1 món đồ kích hoạt cao cấp";
            case GIA_HAN_CAI_TRANG:
                return "Ta sẽ phù phép\n cho trang bị của mi\n thêm hạn sử dụng";
            case NANG_CAP_DO_THIEN_SU:
                return "Nâng cấp\n trang bị thiên sứ";
            case NANG_CAP_BONG_TAI:
                return "Ta sẽ phù phép\ncho bông tai Porata của ngươi\nthành cấp 2";
            case MO_CHI_SO_BONG_TAI:
                return "Ta sẽ phù phép\ncho bông tai Porata cấp 2 của ngươi\ncó 1 chỉ số ngẫu nhiên";
            case NANG_CAP_BONG_TAI_VO_CUC:
                return "Ta sẽ phù phép\ncho bông tai Porata của ngươi\nthành Ngọc Vô Cực";
            case MO_CHI_SO_BONG_TAI_VO_CUC:
                return "Ta sẽ phù phép\ncho Ngọc Vô Cực của ngươi\ncó 1 chỉ số ngẫu nhiên";
            case CHUYEN_HOA_BANG_NGOC:
            case CHUYEN_HOA_BANG_VANG:
                return "Lưu ý trang bị mới\nphải hơn trang bị gốc\n1 bậc";

            // START _ NEW PHA LÊ HÓA //
            case NANG_CAP_SAO_PHA_LE:
                return "Ta sẽ phù phép\nnâng cấp Sao Pha Lê\nthành cấp 2";
            case DANH_BONG_SAO_PHA_LE:
                return "Đánh bóng\nSao pha lê cấp 2";
            case CUONG_HOA_LO_SAO_PHA_LE:
                return "Cường hóa\nÔ Sao Pha lê";
            case LAM_PHEP_NHAP_DA:
                return "Ta sẽ phù phép\ncho x99 mảnh đá vụn\nthành 1 viên Đá cấp cao";
            case TAO_DA_HEMATILE:
                return "Ta sẽ phù phép\n"
                        + "tạo đá Hematite";
            case AN_TRANG_BI:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở thành trang bị Ấn";
            // END _ NEW PHA LÊ HÓA //
            default:
                return "";
        }
    }

    private String getTextInfoTabCombine(int type) {
        switch (type) {
            case CUONG_HOA_LO_SPL:
                return "vào hành trang\nChọn 1 trang bị 6 sao trở lên \nChọn 20 đá Hematite và 2 Dùi đục để nâng cấp\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case MO_NOI_TAI_ITEM:
                return "Hãy bỏ x1 cải trang \nvà x1 đá thời gian vào\n sau đó ấn nâng cấp\n các nội tại có thể mở ra\n"
                        + "+%s Galick, Tự Sát, Dragon, Demon, Makankosappo, Liên Hoàn";
            case NANG_CAP_PET:
                return "Vào hành trang\n"
                        + "Chọn linh thú, trang bị thần linh và x10 đá lửa\n"
                        + "Sau đó chọn 'Nâng cấp'\n"
                        + "Ngươi sẽ nhận được điều bất ngờ!!\n"
                        + "Chúc nhà ngươi may mắn.";
            case NANG_PET:
                return "Vào hành trang\n"
                        + "Chọn pet + trang bị thần linh + x25 đá lửa\n"
                        + "Sau đó chọn 'Nâng cấp'\n"
                        + "Ngươi sẽ nhận được điều bất ngờ!!\n"
                        + "Chúc nhà ngươi may mắn.";
            case AN_TRANG_BI:
                return "Vào hành trang\n"
                        + "Chọn 1 Trang bị THIÊN SỨ và 99 mảnh Ấn\n"
                        + "Sau đó chọn 'Làm phép'\n"
                        + "-Tinh ấn (5 món +15%HP)\n"
                        + "-Nhật ấn (5 món +15%KI\n"
                        + "-Nguyệt ấn (5 món +10%SD)";
            case PHAP_SU_HOA:
                return "Vào hành trang\nChọn trang bị\n(Cải trang, Linh thú, Phụ kiện đeo)\nChọn loại đá Pháp Sư\nSau đó chọn 'Nâng câp'";
            case TAY_PHAP_SU:
                return "Vào hành trang\nChọn trang bị\n(Cải trang, Linh thú, Phụ kiện đeo\n'đã Pháp sư hóa')\nChọn Bùa Tẩy Pháp Sư\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case NANG_CHAN_MENH:
                return "Hãy bỏ chân mệnh và x30 đá \ncam, x2 đá lửa vào\nsau đó ấn 'Nâng cấp'";
            case EP_SAO_TRANG_BI:
                return "Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa) có ô đặt sao pha lê\nChọn loại sao pha lê\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case PHA_LE_HOA_CAI_TRANG:
                return "Con hãy đưa cho ta 1 Cải trang bất kì\nVà x1 Đá Thánh";
            case PHA_LE_HOA_TRANG_BI:
                return "Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\nSau đó chọn 'Nâng cấp'";
            case PHA_LE_HOA_TRANG_BI_X10:
                return "Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\nSau đó chọn 'Nâng cấp'\n Khi nâng cấp thành công hoặc đủ 5 lần thì sẽ dừng lại";
            case NHAP_NGOC_RONG:
                return "Vào hành trang\nChọn 7 viên ngọc cùng sao\nSau đó chọn 'Làm phép'";
            case NHAP_NGOC_RONG_TRANH:
                return "Vào hành trang\nChọn 7 viên ngọc cùng sao\nSau đó chọn 'Làm phép'";
            case CHE_TAO_DO_THIEN_SU:
                return "Cần 1 công thức vip\nMảnh trang bị tương ứng\n"
                        + "Số Lượng\n"
                        + "9999 (có thể thêm)\nĐá nâng cấp (tùy chọn) để tăng tỉ lệ chế tạo\n"
                        + "Đá may mắn (tùy chọn) để tăng tỉ lệ các chỉ số cơ bản và chỉ số ẩn\n"
                        + "Sau đó chọn 'Nâng cấp'";
            // START_ SÁCH TUYỆT KỸ //
            case REMOVE_OPTION:
                return "Hãy bỏ trang bị \ncần tẩy sao vào\n sau đó ấn nâng cấp";
            case GIAM_DINH_SACH:
                return "Vào hành trang chọn\n1 sách cần giám định";
            case TAY_SACH:
                return "Vào hành trang chọn\n1 sách cần tẩy";
            case NANG_CAP_SACH_TUYET_KY:
                return "Vào hành trang chọn\nSách Tuyệt Kỹ 1 cần nâng cấp và 10 Kìm bấm giấy";
            case PHUC_HOI_SACH:
                return "Vào hành trang chọn\nCác Sách Tuyệt Kỹ cần phục hồi";
            case PHAN_RA_SACH:
                return "Vào hành trang chọn\n1 sách cần phân rã";
            // END _ SÁCH TUYỆT KỸ //
            case NANG_CAP_SKH:
                return "Vào hành trang chọn\n1 món kích hoạt, một Đá Thánh và\nmột đá nâng cấp SKH";
            case NANG_CAP_VAT_PHAM:
                return "Vào hành trang\nChọn trang bị\n(Áo,quần,găng,giày hoặc rada)\nChọn loại đá để nâng cấp\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case DOI_VE_HUY_DIET:
                return "Vào hành trang\nChọn món đồ thần linh tương ứng\n(Áo, quần, găng, giày hoặc nhẫn)\nSau đó chọn 'Đổi'";
            case DAP_SET_KICH_HOAT:
                return "Vào hành trang\nChọn món đồ thần linh tương ứng\n(Áo, quần, găng, giày hoặc nhẫn)\n(Có thể thêm 1 món đồ thần linh bất kỳ để tăng tỉ lệ)"
                        + "\nLưu ý: Món đầu tiên ra kích hoạt tương ứng\nSau đó chọn 'Đập'";
            // case DOI_MANH_KICH_HOAT:
            // return "Vào hành trang\nChọn món đồ thần linh tương ứng\n(Áo, quần, găng,
            // giày hoặc nhẫn)\nSau đó chọn 'Đổi'";
            case DAP_SET_KICH_HOAT_CAO_CAP:
                return "Vào hành trang\nChọn 2 đồ kích hoạt giống nhau\n(Áo, quần, găng, giày hoặc nhẫn)\nSau đó chọn 'Đập'";
            case GIA_HAN_CAI_TRANG:
                return "Vào hành trang \n Chọn cải trang có hạn sử dụng \n Chọn thẻ gia hạn \n Sau đó chọn gia hạn";
            case NANG_CAP_DO_THIEN_SU:
                return "C?n 1 công thức vip\n"
                        + "Theo hành tinh\n"
                        + "x999 Mảnh trang bị mảnh\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case NANG_CAP_BONG_TAI:
                return "Vào hành trang\nChọn bông tai Porata\nChọn mảnh bông tai để nâng cấp, số lượng\n999 cái\nSau đó chọn 'Nâng cấp'";
            case MO_CHI_SO_BONG_TAI:
                return "Vào hành trang\nChọn bông tai Porata\nChọn mảnh hồn bông tai số lượng 99 cái\nvà đá xanh lam để nâng cấp\nSau đó chọn 'Nâng cấp'";
            case NANG_CAP_BONG_TAI_VO_CUC:
                return "Vào hành trang\nChọn bông tai Porata 2\nChọn mảnh bông tai 3 để nâng cấp, số lượng\n9999 cái\nSau đó chọn 'Nâng cấp'";
            case MO_CHI_SO_BONG_TAI_VO_CUC:
                return "Vào hành trang\nChọn Ngọc Vô Cực\nChọn đá ngũ sắc pro để nâng cấp\nSau đó chọn 'Nâng cấp'";
            case CHUYEN_HOA_BANG_NGOC:
            case CHUYEN_HOA_BANG_VANG:
                return "Vào hành trang\nChọn trang bị gốc\n(Áo,quần,găng,giày hoặc rada)\ntừ cấp[+4] trở lên\nChọn tiếp trang bị mới\nchưa nâng cấp cần nhập thể\nsau đó chọn 'Nâng cấp'";
            // START _ NEW PHA LÊ HÓA //
            case NANG_CAP_SAO_PHA_LE:
                return "Vào hành trang\nChọn đá Hematite\n Chọn loại sao pha lê (cấp 1)\nSau đó chọn 'Nâng cấp'";
            case DANH_BONG_SAO_PHA_LE:
                return "Vào hành trang\nChọn loại sao pha lê cấp 2 có từ 2 viên trở\nlên\nChọn 1 loại đá mài\nSau đó chọn 'Đánh bóng'";
            case CUONG_HOA_LO_SAO_PHA_LE:
                return "Vào hành trang\n"
                        + "Chọn trang bị có Ô sao thứ 8 trở lên chưa\n"
                        + "cường hóa\n"
                        + "Chọn đá Hematite\n"
                        + "Chọn dùi đục\n"
                        + "Sau đó chọn 'Cường hóa'";
            case TAO_DA_HEMATILE:
                return "Vào hành trang\n"
                        + "Chọn 5 sao pha lê cấp 2 cùng màu\n"
                        + "Chọn 'Tạo đá Hematite'";
            case LAM_PHEP_NHAP_DA:
                return "Vào hành trang\nChọn x99 mảnh đá vụn\nSau đó chọn 'Làm phép'";
            // END _ NEW PHA LÊ HÓA //
            default:
                return "";
        }
    }

}
