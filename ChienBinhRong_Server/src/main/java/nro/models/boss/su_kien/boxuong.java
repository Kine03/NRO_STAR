package nro.models.boss.su_kien;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nro.consts.ConstItem;
import nro.consts.ConstNpc;
import nro.consts.ConstRatio;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.FutureBoss;
import nro.models.boss.NguHanhSon.NgoKhong;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.EffectSkillService;
import nro.services.ItemTimeService;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.SkillService;
import nro.utils.Log;
import nro.utils.SkillUtil;
import nro.utils.Util;

/**
 * @author 💖 Trần Lại 💖
 * @copyright 💖 GirlkuN 💖
 */
public class boxuong extends FutureBoss {
    private static final long timeChangeMap = 1000;
    private long lastTimeJoinMap;

    public boxuong() {
        super(BossFactory.BO_XUONG, BossData.BO_XUONG);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }
    
    @Override
    public void joinMap() {
        super.joinMap();
        lastTimeJoinMap = System.currentTimeMillis() + timeChangeMap;
    }

    @Override
    public void rewards(Player pl) {
        ItemMap itemMap = null;
        int x = this.location.x;
        int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);

        itemMap = new ItemMap(this.zone, 585, 1, x, y, pl.id);

        if (Manager.EVENT_SEVER == 1 && itemMap == null) {
            itemMap = new ItemMap(this.zone, ConstItem.LIST_ITEM_NLSK_TET_2023.length - 1, 1, x, y, pl.id);
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
        textTalkMidle = new String[]{"Ta là bộ xương biết nói"};
    }

    @Override
    public void attack() {
        try {
            Player pl = getPlayerAttack();
            if (pl != null) {  // Kiểm tra pl khác null và chưa chết
                this.playerSkill.skillSelect = this.getSkillAttack();
                if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
                    if (Util.isTrue(15, ConstRatio.PER100) && SkillUtil.isUseSkillChuong(this)) {
                        goToXY(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 80)),
                                Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50), false);
                    } else if (Util.isTrue(15, ConstRatio.PER100) && SkillUtil.isUseSkillChuong(this)) {
                        goToXY(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(10, 40)),
                                Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50), false);
                    }
                }
                SkillService.gI().useSkill(this, pl, null, null);
                if (pl.isPl() || pl.isPet) {
                    if (!pl.itemTime.isBoXuong) {
                        pl.itemTime.isBoXuong = true;
                        pl.itemTime.lastTimeBoXuong = System.currentTimeMillis();
                        Service.getInstance().point(pl);
                        ItemTimeService.gI().sendAllItemTime(pl);
                        Service.gI().Send_Caitrang(pl);
                    }
                }
                checkPlayerDie(pl);
            } else if (pl != null) {  // Kiểm tra pl khác null trước khi gọi goToPlayer
                if (Util.isTrue(1, 2)) {
                    this.goToPlayer(pl, false);
                }
            }
        } catch (Exception ex) {
            Log.error(boxuong.class, ex);
        }
    }

   @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (plAtt.effectFlagBag.useCanhacquythienthan) {
            damage = 9900;
        } else {
            damage = 4900;
        }
        return super.injured(plAtt, damage, piercing, isMobAttack);
    }

    @Override
    public void leaveMap() {
        super.leaveMap();
    }

}
