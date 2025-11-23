package bosscuatao;

import java.util.Random;
import nro.consts.ConstItem;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.boss.FutureBoss;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.Util;

/**
 * @author 💖 Trần Lại 💖
 * @copyright 💖 GirlkuN 💖
 */
public class zaiko extends FutureBoss {

    public zaiko() {
        super(BossFactory.ZAIKO, BossData.ZAIKO);
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
        int[] ctVIP = new int[]{1084, 1085, 1086};

        int randomDoTL = new Random().nextInt(itemDoTL.length);
        int randomGangTL = new Random().nextInt(gangTL.length);
        int randomCTVIP = new Random().nextInt(ctVIP.length);

        if (Util.isTrue(5, 100)) {
            itemMap = Util.ratiItem(this.zone, 561, 1, x, y, pl.id);
        } else if (Util.isTrue(10, 100)) {
            itemMap = Util.ratiItem(this.zone, gangTL[randomGangTL], 1, x, y, pl.id);
        } else if (Util.isTrue(15, 100)) {
            itemMap = new ItemMap(this.zone, 1127, 1, x, y, pl.id);
        } else if (Util.isTrue(20, 100)) {
            itemMap = Util.ratiItem(this.zone, itemDoTL[randomDoTL], 1, x, y, pl.id);
        } else if (Util.isTrue(25, 100)) {
            itemMap = Util.ratiItem(this.zone, ctVIP[randomCTVIP], 1, x, y, pl.id);
        } else if (Util.isTrue(30, 100)) {
            itemMap = Util.ratiItem(this.zone, 1503, 1, x, y, pl.id);
        } else {
            itemMap = new ItemMap(this.zone, 15, 1, x, y, pl.id);
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
        textTalkMidle = new String[]{"Đưa ngọc rồng cho ta"};

    }

    @Override
    public void leaveMap() {

        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }

    @Override
    public synchronized int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (isDie()) {
            return 0;
        }

        // Kiểm tra xuyên giáp và giảm sát thương một nửa
        damage /= 2;

        // Kiểm tra miss nếu không xuyên giáp
        if (!piercing && Util.isTrue(0, 100)) {
            chat("Xí hụt");
            return 0;
        }

        // Trừ HP và kiểm tra trạng thái chết
        this.nPoint.subHP(damage);
        if (this.isDie()) {
            rewards(plAtt);
            die();
        }
        return damage;
    }
}
