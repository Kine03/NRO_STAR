package nro.models.boss.cold;

import java.util.Random;
import nro.consts.ConstItem;
import nro.models.boss.*;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.RewardService;
import nro.services.Service;
import nro.utils.Util;

/**
 * @author 💖 Trần Lại 💖
 * @copyright 💖 GirlkuN 💖
 */
public class Cooler2 extends FutureBoss {

    public Cooler2() {
        super(BossFactory.COOLER2, BossData.COOLER2);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {
        pl.pointSb += 1;
        ItemMap itemMap = null;
        int x = this.location.x;
        int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);

        int[] itemDoThuong = new int[]{241, 253, 265, 277, 237, 249, 261, 273, 233, 245, 257, 269, 281};
        int[] NRs = new int[]{16, 15};
        int[] itemDoTL = new int[]{555, 556, 557, 558, 559, 560, 563, 565, 567};
        int[] gangTL = new int[]{562, 564, 566};

        int randomDoThuong = new Random().nextInt(itemDoThuong.length);
        int randomNR = new Random().nextInt(NRs.length);
        int randomDoTL = new Random().nextInt(itemDoTL.length);
        int randomGangTL = new Random().nextInt(gangTL.length);

        if (Util.isTrue(5, 100)) {
            itemMap = Util.ratiItem(this.zone, gangTL[randomGangTL], 1, x, y, pl.id);
        } else if (Util.isTrue(15, 100)) {
            itemMap = Util.ratiItem(this.zone, itemDoTL[randomDoTL], 1, x, y, pl.id);
        } else {
            if (Util.isTrue(50, 100)) {
                itemMap = new ItemMap(this.zone, NRs[randomNR], 1, x, y, pl.id);
            } else {
                itemMap = Util.ratiItemDoThuong(this.zone, itemDoThuong[randomDoThuong], 1, x, y, pl.id);

            }
        }

        Service.getInstance().dropItemMap(this.zone, itemMap);
    }

    @Override
    public void idle() {

    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {

        textTalkAfter = new String[]{"Ta đã giấu hết ngọc rồng rồi, các ngươi tìm vô ích hahaha"};
    }

//    @Override
//    public void leaveMap() {
//        BossManager.gI().getBossById(BossFactory.COOLER).setJustRest();
//        super.leaveMap();
//        BossManager.gI().removeBoss(this);
//    }
    @Override
    public void leaveMap() {
        Boss cooler3 = BossFactory.createBoss(BossFactory.FILE_VANG);
        cooler3.zone = this.zone;
        this.setJustRestToFuture();
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }
}
