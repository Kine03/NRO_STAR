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
public class kylam extends FutureBoss {

    public kylam() {
        super(BossFactory.KYLAN, BossData.KYLAN);
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
        if (Util.isTrue(50, 100)) {
            itemMap = new ItemMap(this.zone, 1557, 1, x, y, pl.id);
        }else{
            itemMap = new ItemMap(this.zone, 17, 1, x, y, pl.id);
        }
        Service.getInstance().dropItemMap(zone, itemMap);
    }

    @Override
    public void idle() {

    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {
        textTalkMidle = new String[]{"Trung thu vui vẻ"};

    }

    @Override
    public synchronized int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (isDie()) {
            return 0;
        }

//        // Tính sát thương theo phần trăm HP hiện tại của boss
//        int currentHP = this.nPoint.hp;
//
//        if (currentHP > 100) {
//            // Nếu HP của boss lớn hơn 100, tính sát thương theo 10% của HP hiện tại
//            damage = (int) (currentHP * 0.1);
//        } else {
//            // Nếu HP của boss nhỏ hơn hoặc bằng 100, đặt sát thương cố định là 1
//            damage = 1;
//        }
//
//        // Kiểm tra xuyên giáp và giảm sát thương một nửa
//        damage /= 2;
        // Giới hạn tối đa sát thương
        int maxDamage = this.nPoint.hpg / ((this.type + 1) * 20);
//        if (maxDamage <= 0) {
//            maxDamage = 1;
//        }
        if (damage > 1) {
            damage = 1;
        }

//        // Kiểm tra miss nếu không xuyên giáp
//        if (!piercing && Util.isTrue(0, 100)) {
//            chat("Xí hụt");
//            return 0;
//        }
        // Trừ HP và kiểm tra trạng thái chết
        this.nPoint.subHP(damage);
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
