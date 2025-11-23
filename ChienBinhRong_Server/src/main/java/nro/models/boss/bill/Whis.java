package nro.models.boss.bill;

import nro.consts.ConstItem;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.RewardService;
import nro.services.Service;
import nro.utils.Util;

/**
 *
 * @author 💖 Trần Lại 💖
 * @copyright 💖 GirlkuN 💖
 *
 */
public class Whis extends Boss {

    public Whis() {
        super(BossFactory.WHIS, BossData.WHIS);
    }


    @Override
    public void rewards(Player pl) {

        ItemMap itemMap = null;
        int x = this.location.x;
        int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);
         if (Util.isTrue(1, 5)) {
            itemMap = new ItemMap(this.zone, 1450, Util.nextInt(2,5), x, y, pl.id);
        } else if (Util.isTrue(1, 5)) {
            itemMap = new ItemMap(this.zone, 1500, 1, x, y, pl.id);
        }
        if (itemMap != null) {
            Service.getInstance().dropItemMap(zone, itemMap);
        }
        generalRewards(pl);
    }

    @Override
    public void idle() {
    }
    @Override
    protected boolean useSpecialSkill() {
        return false;
    }
    @Override
    public void leaveMap() {// roi map
        super.leaveMap();
        BossManager.gI().getBossById(BossFactory.BILL).changeToAttack();
    }

    @Override
    public void checkPlayerDie(Player pl) {

    }
    @Override
    public void joinMap() {
        super.joinMap();
        BossFactory.createBoss(BossFactory.BILL).zone = this.zone;
    }
    @Override
    public void initTalk() {
    }

}
