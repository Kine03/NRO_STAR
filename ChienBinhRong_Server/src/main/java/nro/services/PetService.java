package nro.services;

import nro.consts.ConstPlayer;
import nro.models.player.Pet;
import nro.models.player.Player;
import nro.utils.SkillUtil;
import nro.utils.Util;
import nro.consts.ConstPet;

/**
 *
 * @Build by Arriety
 *
 */
public class PetService {

    private static PetService i;

    public static PetService gI() {
        if (i == null) {
            i = new PetService();
        }
        return i;
    }

  public void createNormalPet(Player player, int gender, byte... limitPower) {
    try {
        createNewPet(player, ConstPet.NORMAL, (byte) gender);
        if (limitPower != null && limitPower.length == 1) {
            player.pet.nPoint.limitPower = limitPower[0];
            player.pet.nPoint.initPowerLimit();
        }
        Service.getInstance().chatJustForMe(player, player.pet, "Xin hãy thu nhận làm đệ tử");
    } catch (Exception e) {
        e.printStackTrace();
    }
}

//    public void createNormalPet(Player player, byte... limitPower) {
//        new Thread(() -> {
//            try {
//                createNewPet(player, ConstPet.NORMAL);
//                if (limitPower != null && limitPower.length == 1) {
//                    player.pet.nPoint.limitPower = limitPower[0];
//                }
//                Thread.sleep(1000);
//                Service.getInstance().chatJustForMe(player, player.pet, "Xin hãy thu nhận làm đệ tử");
//            } catch (Exception e) {
//            }
//        }).start();
//    }
 public void createMabuPet(Player player, byte... limitPower) {
    try {
        createNewPet(player, ConstPet.MABU);
        if (limitPower != null && limitPower.length == 1) {
            player.pet.nPoint.limitPower = limitPower[0];
            player.pet.nPoint.initPowerLimit();
        }
        Service.getInstance().chatJustForMe(player, player.pet, "Oa oa oa...");
    } catch (Exception e) {
        e.printStackTrace();
    }
}

public void createMabuPet(Player player, int gender, byte... limitPower) {
    try {
        createNewPet(player, ConstPet.MABU, (byte) gender);
        if (limitPower != null && limitPower.length == 1) {
            player.pet.nPoint.limitPower = limitPower[0];
            player.pet.nPoint.initPowerLimit();
        }
        Service.getInstance().chatJustForMe(player, player.pet, "Oa oa oa...");
    } catch (Exception e) {
        e.printStackTrace();
    }
}
  public void createBerusPet(Player player, int gender, byte... limitPower) {
    try {
        createNewPet(player, ConstPet.BILL, (byte) gender);
        if (limitPower != null && limitPower.length == 1) {
            player.pet.nPoint.limitPower = limitPower[0];
        }
        Service.getInstance().chatJustForMe(player, player.pet, "Thần hủy diệt hiện thân tất cả quỳ xuống...");
    } catch (Exception e) {
        e.printStackTrace();
    }
}

public void createPetFide(Player player, int gender, byte... limitPower) {
    try {
        createNewPet(player, ConstPet.FIDE_NHI, (byte) gender);
        if (limitPower != null && limitPower.length == 1) {
            player.pet.nPoint.limitPower = limitPower[0];
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

public void createPetMabuHan(Player player, int gender, byte... limitPower) {
    try {
        createNewPet(player, ConstPet.MABU_HAN, (byte) gender);
        if (limitPower != null && limitPower.length == 1) {
            player.pet.nPoint.limitPower = limitPower[0];
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}


public void createBlackPet(Player player, int gender, byte... limitPower) {
    try {
        createNewPet(player, ConstPet.SUPER, (byte) gender);
        if (limitPower != null && limitPower.length == 1) {
            player.pet.nPoint.limitPower = limitPower[0];
        }
        Service.getInstance().chatJustForMe(player, player.pet, "huuuuuuuuuuu...");
    } catch (Exception e) {
        e.printStackTrace();
    }
}


 public void createMabuNhi(Player player, int gender, byte... limitPower) {
    try {
        createNewPet(player, ConstPet.MABU_NHI, (byte) gender);
        if (limitPower != null && limitPower.length == 1) {
            player.pet.nPoint.limitPower = limitPower[0];
        }
        Service.getInstance().chatJustForMe(player, player.pet, "Bư bư");
    } catch (Exception e) {
        e.printStackTrace();
    }
}

public void createBILLCON(Player player, int gender, byte... limitPower) {
    try {
        createNewPet(player, ConstPet.BILL_CON, (byte) gender);
        if (limitPower != null && limitPower.length == 1) {
            player.pet.nPoint.limitPower = limitPower[0];
        }
        Service.getInstance().chatJustForMe(player, player.pet, "Hiazzzz...");
    } catch (Exception e) {
        e.printStackTrace();
    }
}

 public void createCell(Player player, int gender, byte... limitPower) {
    try {
        createNewPet(player, ConstPet.CELL, (byte) gender);
        if (limitPower != null && limitPower.length == 1) {
            player.pet.nPoint.limitPower = limitPower[0];
        }
        Service.getInstance().chatJustForMe(player, player.pet, "Hiazzzz...");
    } catch (Exception e) {
        e.printStackTrace();
    }
}
 
public void createVidelPet(Player player, int gender, byte... limitPower) {
    try {
        createNewPet(player, ConstPet.VIDEL, (byte) gender);
        if (limitPower != null && limitPower.length == 1) {
            player.pet.nPoint.limitPower = limitPower[0];
        }
        Service.getInstance().chatJustForMe(player, player.pet, "Ta sẽ đem hạnh phúc đến Noel này...");
        Service.getInstance().sendThongBao(player, "Bạn đã nhận được Đệ Tử Videl");
    } catch (Exception e) {
        e.printStackTrace();
    }
}

public void createWhisPet(Player player, int gender, byte... limitPower) {
    try {
        createNewPet(player, ConstPet.WHIS, (byte) gender);
        if (limitPower != null && limitPower.length == 1) {
            player.pet.nPoint.limitPower = limitPower[0];
        }
        Service.getInstance().chatJustForMe(player, player.pet, "Chào thằng sư phụ nha...");
        Service.getInstance().sendThongBao(player, "Chúc mừng bạn đã đổi được con đệ mất dạy nhất hành tinh");
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    public void changeNormalPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createNormalPet(player, gender, limitPower);
    }

    public void changeMabuPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createMabuPet(player, gender, limitPower);
    }

    public void changeBillPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createBerusPet(player, gender, limitPower);
    }

    public void changeCellPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createCell(player, gender, limitPower);
    }

    public void changeVidelPet(Player player, int gender) {
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createVidelPet(player, gender);
    }

    public void changebilconPet(Player player, int gender) {
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createBILLCON(player, gender);
    }

    public void changeSuperPet(Player player, int gender) {
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createBlackPet(player, gender);
    }

    public void changeNamePet(Player player, String name) {
    if (!InventoryService.gI().existItemBag(player, 400)) {
        Service.getInstance().sendThongBao(player, "Bạn cần thẻ đặt tên đệ tử, mua tại Santa");
        return;
    } else if (Util.haveSpecialCharacter(name)) {
        Service.getInstance().sendThongBao(player, "Tên không được chứa ký tự đặc biệt");
        return;
    } else if (name.length() > 10) {
        Service.getInstance().sendThongBao(player, "Tên quá dài");
        return;
    }

    // Xóa pet ra khỏi map tạm thời (nếu có)
    MapService.gI().exitMap(player.pet);

    // Gán lại tên mới
    player.pet.name = "$" + name.toLowerCase().trim();

    // Trừ 1 thẻ đổi tên
    InventoryService.gI().subQuantityItemsBag(
        player,
        InventoryService.gI().findItemBagByTemp(player, 400),
        1
    );

    // Gửi tin nhắn xác nhận
    Service.getInstance().chatJustForMe(player, player.pet, "Cảm ơn sư phụ đã đặt cho con tên " + name);
}

    private int[] getDataPetNormal() {
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 105) * 10; //hp
        petData[1] = Util.nextInt(40, 105) * 10; //mp
        petData[2] = Util.nextInt(20, 45); //dame
        petData[3] = Util.nextInt(9, 50); //def
        petData[4] = Util.nextInt(0, 2); //crit
        return petData;
    }

    private int[] getDataPetMabu() {
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 105) * 20; //hp
        petData[1] = Util.nextInt(40, 105) * 20; //mp
        petData[2] = Util.nextInt(50, 120); //dame
        petData[3] = Util.nextInt(9, 50); //def
        petData[4] = Util.nextInt(0, 2); //crit
        return petData;
    }

    private int[] getDataPetBerus() {
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 115) * 20; //hp
        petData[1] = Util.nextInt(40, 115) * 20; //mp
        petData[2] = Util.nextInt(70, 140); //dame
        petData[3] = Util.nextInt(9, 50); //def
        petData[4] = Util.nextInt(0, 2); //crit
        return petData;
    }

    private int[] getDataPetVidel() {
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 120) * 20; //hp
        petData[1] = Util.nextInt(40, 120) * 20; //mp
        petData[2] = Util.nextInt(20, 150); //dame
        petData[3] = Util.nextInt(9, 50); //def
        petData[4] = Util.nextInt(0, 2); //crit
        return petData;
    }

    private void createNewPet(Player player, byte typePet, byte... gender) {
        int[] data = new int[0];
        int petLevel = 0;
        Pet pet = new Pet(player);
        pet.nPoint.power = 1_500_000;
        pet.lever = player.lvpet;
        switch (typePet) {
            case ConstPet.NORMAL: {
                data = getDataPetNormal();
                pet.name = "$Đệ tử";
                pet.nPoint.power = 2_000;
                pet.typePet = ConstPet.NORMAL;
                break;
            }
            case ConstPet.MABU: {
                data = getDataPetMabu();
                pet.name = "$Mabư";
                pet.typePet = ConstPet.MABU;
                break;
            }
            case ConstPet.FIDE_NHI: {
                data = getDataPetMabu();
                pet.name = "$Fide Nhí";
                pet.typePet = ConstPet.FIDE_NHI;
                break;
            }
            case ConstPet.MABU_HAN: {
                data = getDataPetMabu();
                pet.name = "$Mabư Han";
                pet.typePet = ConstPet.MABU_HAN;
                break;
            }
            case ConstPet.BILL: {
                data = getDataPetBerus();
                pet.name = "$Berus";
                pet.typePet = ConstPet.BILL;
                break;
            }
            case ConstPet.SUPER: {
                data = getDataPetBerus();
                pet.name = "$Black Goku";
                pet.typePet = ConstPet.SUPER;
                break;
            }
            case ConstPet.VIDEL: {
                data = getDataPetVidel();
                pet.name = "$Videl";
                pet.typePet = ConstPet.VIDEL;
                break;
            }
            case ConstPet.WHIS: {
                data = getDataPetVidel();
                pet.name = "$Cell Kid";
                pet.typePet = ConstPet.WHIS;
                break;
            }
            case ConstPet.CELL: {
                data = getDataPetVidel();
                pet.name = "$Cell";
                pet.typePet = ConstPet.CELL;
                petLevel = 1;
                break;
            }
            case ConstPet.MABU_NHI: {
                data = getDataPetVidel();
                pet.name = "$Mabư Kid";
                pet.typePet = ConstPet.MABU_NHI;
                break;
            }
            case ConstPet.BILL_CON: {
                data = getDataPetVidel();
                pet.name = "$Mini Berus";
                pet.typePet = ConstPet.BILL_CON;
                if (player.pet != null && player.pet.typePet == ConstPet.MABU_NHI) {
                    petLevel = player.pet.getLever();
                } else {
                    petLevel = 1;
                }
                break;
            }
        }
        pet.gender = (gender != null && gender.length != 0) ? gender[0] : (byte) Util.nextInt(0, 2);
        pet.id = -player.id;
        pet.nPoint.stamina = 1000;
        pet.nPoint.maxStamina = 1000;
        pet.nPoint.hpg = data[0];
        pet.nPoint.mpg = data[1];
        pet.nPoint.dameg = data[2];
        pet.nPoint.defg = data[3];
        pet.nPoint.critg = data[4];

        for (int i = 0; i < 8; i++) {
            pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
        }
//        pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(Util.nextInt(0, 2) * 2, 17), 1));
        if (Util.isTrue(80, 100)) {
            pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
        } else {
            pet.playerSkill.skills.add(SkillUtil.createSkill(17, 1));
        }
//        pet.playerSkill.skills.add(SkillUtil.createSkill(17, 1));
//        if (petLevel != 0) {
//            pet.playerSkill.skills.add(SkillUtil.createSkill(1, 1));
//        }
        if (petLevel != 0) {
            pet.setLever(petLevel);
        }
        for (int i = 0; i < 4; i++) {
            pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
        }
        pet.nPoint.calPoint();
        pet.nPoint.setFullHpMp();

        player.pet = pet;
    }

    //--------------------------------------------------------------------------
}
