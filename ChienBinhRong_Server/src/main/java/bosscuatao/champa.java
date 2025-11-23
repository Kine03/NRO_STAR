package bosscuatao;

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
 * @author 💖 Trần Lại 💖
 * @copyright 💖 GirlkuN 💖
 */
public class champa extends FutureBoss {

    public champa() {
        super(BossFactory.CHAMPA, BossData.champa);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }


   
    @Override
    public void rewards(Player pl) {
        ItemMap itemMap = null;
        int x = this.location.x;
        int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);
        if(Util.isTrue(20, 100)){
            itemMap = new ItemMap(this.zone, 1328, 1, x, y, pl.id);
            itemMap.options.add(new ItemOption(30, 0));
        } else {
            itemMap = new ItemMap(this.zone, 1328, 1, x, y, pl.id);
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
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {
        textTalkMidle = new String[]{""};
        textTalkBefore = new String[]{""};
    }

    @Override
    public void leaveMap() {

        super.leaveMap();
    }

}
