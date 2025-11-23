package bosscuatao;

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
 * @author 💖 Trần Lại 💖
 * @copyright 💖 GirlkuN 💖
 */
public class rongOmega extends FutureBoss {

    public rongOmega() {
        super(BossFactory.RONGOMEGA, BossData.RONG_OMEGA);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {
        pl.pointSb += 1;
        int x = this.location.x;
        int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);

        int[] itemNROD = new int[]{807, 808, 809, 810, 811, 812, 813};

        int randomNROD = new Random().nextInt(itemNROD.length);
        ItemMap itemMap = new ItemMap(this.zone, itemNROD[randomNROD], 1, x, y, pl.id);

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
    }

}
