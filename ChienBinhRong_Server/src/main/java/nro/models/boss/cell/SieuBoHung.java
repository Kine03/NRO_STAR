package nro.models.boss.cell;

import java.util.Random;
import nro.consts.ConstItem;
import nro.consts.ConstRatio;
import nro.models.boss.*;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.server.Manager;
import nro.server.ServerNotify;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.SkillService;
import nro.services.TaskService;
import nro.services.func.ChangeMapService;
import nro.utils.Log;
import nro.utils.SkillUtil;
import nro.utils.Util;

/**
 *
 * @author 💖 Trần Lại 💖
 * @copyright 💖 GirlkuN 💖
 *
 */
public class SieuBoHung extends FutureBoss {

    public static boolean tuSat;

    public SieuBoHung() {
        super(BossFactory.SIEU_BO_HUNG, BossData.SIEU_BO_HUNG);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void attack() {
        try {
            Player pl = getPlayerAttack();
            if (pl != null) {
                this.playerSkill.skillSelect = this.getSkillAttack();
                if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
                    if (Util.isTrue(15, ConstRatio.PER100) && SkillUtil.isUseSkillChuong(this)) {
                        goToXY(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 80)),
                                Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50), false);
                    }
                    SkillService.gI().useSkill(this, pl, null, null);
                    checkPlayerDie(pl);
                } else {
                    goToPlayer(pl, false);
                }
            }
        } catch (Exception ex) {
            Log.error(SieuBoHung.class, ex);
        }
    }

    @Override
    public void leaveMap() {
        BossManager.gI().getBossById(BossFactory.XEN_BO_HUNG).setJustRest();
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }

    @Override
    public void joinMap() {
        int x = Util.nextInt(50, this.zone.map.mapWidth - 50);
        ChangeMapService.gI().changeMap(this, this.zone, x, this.zone.map.yPhysicInTop(x, 0));
        ServerNotify.gI().notify("Boss " + this.name + " vừa xuất hiện tại " + this.zone.map.mapName);
    }

    @Override
    public void idle() {
    }

    @Override
    public void rewards(Player pl) {
        pl.pointSb += 1;
        TaskService.gI().checkDoneTaskKillBoss(pl, this);
        ItemMap itemMap = null;
        int x = this.location.x;
        int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);

        int[] itemDoThuong = new int[]{241, 253, 265, 277, 237, 249, 261, 273, 233, 245, 257, 269, 281};
        int[] NRs = new int[]{16, 15};
        int[] itemDoTL = new int[]{555, 556, 557, 558, 559, 560, 563, 565, 567};
        int[] gangTL = new int[]{562, 564, 566};

        int randomDoThuong = new Random().nextInt(itemDoThuong.length);
        int randomNR = new Random().nextInt(NRs.length);
        int randomDoTL = new Random().nextInt(itemDoTL.length);
        int randomGangTL = new Random().nextInt(gangTL.length);

        if (Util.isTrue(5, 100)) {
            itemMap = Util.ratiItem(this.zone, gangTL[randomGangTL], 1, x, y, pl.id);
        } else if (Util.isTrue(15, 100)) {
            itemMap = Util.ratiItem(this.zone, itemDoTL[randomDoTL], 1, x, y, pl.id);
        } else {
            if (Util.isTrue(50, 100)) {
                itemMap = new ItemMap(this.zone, NRs[randomNR], 1, x, y, pl.id);
            } else {
                itemMap = Util.ratiItemDoThuong(this.zone, itemDoThuong[randomDoThuong], 1, x, y, pl.id);

            }
        }

        Service.getInstance().dropItemMap(this.zone, itemMap);
    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {
        this.textTalkBefore = new String[]{};
        this.textTalkMidle = new String[]{"Kame Kame Haaaaa!!", "Mi khá đấy nhưng so với ta chỉ là hạng tôm tép",
            "Tất cả nhào vô hết đi", "Cứ chưởng tiếp đi. haha", "Các ngươi yếu thế này sao hạ được ta đây. haha",
            "Khi công pháo!!", "Cho mi biết sự lợi hại của ta"};
        this.textTalkAfter = new String[]{"Các ngươi được lắm", "Hãy đợi đấy thời gian tới ta sẽ quay lại.."};
    }

}
