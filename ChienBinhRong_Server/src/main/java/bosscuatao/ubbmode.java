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
public class ubbmode extends FutureBoss {

    public ubbmode() {
        super(BossFactory.UBBMODE, BossData.UBBMODE);
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

        itemMap = new ItemMap(this.zone, 1522, 3, x, y, pl.id);

        if (Manager.EVENT_SEVER == 4 && itemMap == null) {
            itemMap = new ItemMap(this.zone, ConstItem.LIST_ITEM_NLSK_TET_2023[Util.nextInt(0, ConstItem.LIST_ITEM_NLSK_TET_2023.length - 1)], 1, x, y, pl.id);
            itemMap.options.add(new ItemOption(74, 0));
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
        textTalkMidle = new String[]{"Đưa ngọc rồng cho ta"};

    }

    @Override
    public synchronized int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (isDie()) {
            return 0;
        }

        // Tính sát thương theo phần trăm HP hiện tại của boss
        int currentHP = this.nPoint.hp;

        if (currentHP > 100) {
            // Nếu HP của boss lớn hơn 100, tính sát thương theo 10% của HP hiện tại
            damage = (int) (currentHP * 0.1);
        } else {
            // Nếu HP của boss nhỏ hơn hoặc bằng 100, đặt sát thương cố định là 1
            damage = 1;
        }

        // Kiểm tra xuyên giáp và giảm sát thương một nửa
        damage /= 2;

        // Giới hạn tối đa sát thương
        int maxDamage = this.nPoint.hpg / ((this.type + 1) * 20);
        if (maxDamage <= 0) {
            maxDamage = 1;
        }
        if (damage > maxDamage) {
            damage = maxDamage;
        }

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

    @Override
    public void leaveMap() {

        super.leaveMap();
    }

}
