package nro.models.boss.bosstraidat;

import java.util.Random;
import nro.models.boss.*;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.Util;

/**
 *
 * @author 💖 Trần Lại 💖
 * @copyright 💖 GirlkuN 💖
 *
 */
public class xeko extends FutureBoss {

    public xeko() {
        super(BossFactory.XEKO, BossData.xeko);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {
        TaskService.gI().checkDoneTaskKillBoss(pl, this);
        ItemMap itemMap = null;
        int x = this.location.x;
        int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);

        int[] itemNRO = new int[]{16,17,18};

        int randomNRO = new Random().nextInt(itemNRO.length);

        itemMap = Util.ratiItem(this.zone, itemNRO[randomNRO], 1, x, y, pl.id);
 
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
        this.textTalkBefore = new String[]{""};
        this.textTalkMidle = new String[]{""};
    }

    @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }
}
