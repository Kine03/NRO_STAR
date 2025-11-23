package nro.models.boss.chill;

import java.util.Random;
import nro.models.boss.*;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.Util;

/**
 *
 * @author 💖 Trần Lại 💖
 * @copyright 💖 GirlkuN 💖
 *
 */
public class Chill2 extends FutureBoss {

    public Chill2() {
        super(BossFactory.CHILL2, BossData.CHILL2);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {
        pl.pointSb += 1;
        TaskService.gI().checkDoneTaskKillBoss(pl, this);
        ItemMap itemMap = null;
        int x = this.location.x;
        int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);

        int[] itemDoTL = new int[]{555, 556, 557, 558, 559, 560, 563, 565, 567};
        int[] gangTL = new int[]{562, 564, 566};

        int randomDoTL = new Random().nextInt(itemDoTL.length);
        int randomGangTL = new Random().nextInt(gangTL.length);

        if (Util.isTrue(8, 100)) {
            itemMap = Util.ratiItem(this.zone, gangTL[randomGangTL], 1, x, y, pl.id);
        } else if (Util.isTrue(20, 100)) {
            itemMap = Util.ratiItem(this.zone, itemDoTL[randomDoTL], 1, x, y, pl.id);
        }else if (Util.isTrue(20, 100)) {
            itemMap = Util.ratiItem(this.zone, 1503, 1, x, y, pl.id);
        }  else {
            if (Util.isTrue(50, 100)) {
                itemMap = new ItemMap(this.zone, 15, 1, x, y, pl.id);
            } else {
                itemMap = new ItemMap(this.zone, 16, 1, x, y, pl.id);

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

    @Override
    public void leaveMap() {
        BossManager.gI().getBossById(BossFactory.CHILL).setJustRest();
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }
}
