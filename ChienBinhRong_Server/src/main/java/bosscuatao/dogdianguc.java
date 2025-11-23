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
public class dogdianguc extends FutureBoss {

    public dogdianguc() {
        super(BossFactory.DOGDIANGUC, BossData.DOGDIANGUC);
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

        int[] itemNROB = new int[]{925, 926, 927, 928, 929, 930, 931};

        int randomTV = new Util().nextInt(1, 2);
        int randomHN = new Util().nextInt(5, 10);

        int randomNROB = new Random().nextInt(itemNROB.length);
        if (Util.isTrue(50, 100)) {
            Service.getInstance().dropItemMap(this.zone, new ItemMap(this.zone, itemNROB[randomNROB], 1, x, y, pl.id));
        } else {
            if (Util.isTrue(50, 100)) {
                // Rơi item 457 rải đều
                for (int i = 0; i < randomTV; i++) {
                    int dx = Util.nextInt(-40, 40);  // Dịch trái/phải
                    int dy = Util.nextInt(-10, 10);  // Dịch lên/xuống nhẹ
                    int xDrop = x + dx;
                    int yDrop = this.zone.map.yPhysicInTop(xDrop, y - 24);
                    Service.getInstance().dropItemMap(this.zone, new ItemMap(this.zone, 457, 1, xDrop, yDrop, pl.id));
                }
            } else {
                // Rơi item 861 rải đều
                for (int i = 0; i < randomHN; i++) {
                    int dx = Util.nextInt(-60, 60);
                    int dy = Util.nextInt(-10, 10);
                    int xDrop = x + dx;
                    int yDrop = this.zone.map.yPhysicInTop(xDrop, y - 24);
                    Service.getInstance().dropItemMap(this.zone, new ItemMap(this.zone, 861, 10, xDrop, yDrop, -1));
                }
            }
        }
    }

    @Override
    public void idle() {

    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {
        textTalkMidle = new String[]{"GÂU GÂU"};
        textTalkBefore = new String[]{"ẲNG ẲNG"};
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        // Giới hạn damage tối đa
        if (damage > 5000) {
            damage = 5000;
        }

        // Trừ máu
        this.nPoint.subHP(damage);

        // Kiểm tra boss chết
        if (this.isDie()) {
            rewards(plAtt);
            die();
        }

        return damage;
    }

    @Override
    public void leaveMap() {

        super.leaveMap();
    }

}
