package nro.models.boss.NgucTu;

import java.util.Random;
import nro.consts.ConstItem;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.FutureBoss;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.RewardService;
import nro.services.Service;
import nro.utils.Util;

/**
 * @author 💖 Nothing 💖
 */
public class Cumber extends FutureBoss {

    public Cumber() {
        super(BossFactory.CUMBER, BossData.CUMBER);
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

        int[] itemDoTL = new int[]{555, 556, 557, 558, 559, 560, 563, 565, 567};
        int[] gangTL = new int[]{562, 564, 566};

        int randomDoTL = new Random().nextInt(itemDoTL.length);
        int randomGangTL = new Random().nextInt(gangTL.length);

        if (Util.isTrue(8, 100)) {
            itemMap = Util.ratiItem(this.zone, gangTL[randomGangTL], 1, x, y, pl.id);
        } else if (Util.isTrue(15, 100)) {
            itemMap = Util.ratiItem(this.zone, itemDoTL[randomDoTL], 1, x, y, pl.id);
        } else {
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
        textTalkMidle = new String[]{"Ta chính là đệ nhất vũ trụ cao thủ"};
    }

    @Override
    public void leaveMap() {
        Boss cumber2 = BossFactory.createBoss(BossFactory.CUMBER2);
        cumber2.zone = this.zone;
        this.setJustRestToFuture();
        super.leaveMap();
    }

}
