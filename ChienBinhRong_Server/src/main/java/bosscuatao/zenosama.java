package bosscuatao;

import java.util.Random;
import nro.consts.ConstItem;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.FutureBoss;
import nro.models.boss.mabu_war.BossMabuWar;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.MapService;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.TaskService;
import nro.services.func.ChangeMapService;
import nro.utils.Util;

/**
 * @author 💖 Trần Lại 💖
 * @copyright 💖 GirlkuN 💖
 */
public class zenosama extends BossZenoWar {

    public zenosama(int mapID, int zoneId) {
        super(BossFactory.ZENOSAMA, BossData.ZENO_SAMA);
        this.mapID = mapID;
        this.zoneId = zoneId;
        this.zoneHold = zoneId;
        this.isMabuBoss = true;
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {
        pl.pointSb += 3;
        TaskService.gI().checkDoneTaskKillBoss(pl, this);
        ItemMap itemMap = null;
        int x = this.location.x;
        int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);

        int[] itemDoTL = new int[]{555, 556, 557, 558, 559, 560, 563, 565, 567};
        int[] gangTL = new int[]{562, 564, 566};

        int randomDoTL = new Random().nextInt(itemDoTL.length);
        int randomGangTL = new Random().nextInt(gangTL.length);

        if (Util.isTrue(10, 100)) {
            itemMap = Util.ratiItem(this.zone, 561, 1, x, y, pl.id);
        } else if (Util.isTrue(30, 100)) {
            itemMap = Util.ratiItem(this.zone, gangTL[randomGangTL], 1, x, y, pl.id);
        } else {
            itemMap = Util.ratiItem(this.zone, itemDoTL[randomDoTL], 1, x, y, pl.id);
        }
        int dx1 = Util.nextInt(-30, 30);
        int dx2 = Util.nextInt(-30, 30);
        int dx3 = Util.nextInt(-30, 30);

        int x1 = x + dx1;
        int y1 = this.zone.map.yPhysicInTop(x1, y - 24);
        int x2 = x + dx2;
        int y2 = this.zone.map.yPhysicInTop(x2, y - 24);
        int x3 = x + dx3;
        int y3 = this.zone.map.yPhysicInTop(x3, y - 24);

        // Drop các item
        Service.getInstance().dropItemMap(this.zone, new ItemMap(this.zone, 14, 1, x1, y1, pl.id));
        Service.getInstance().dropItemMap(this.zone, new ItemMap(this.zone, 15, 1, x2, y2, pl.id));
        Service.getInstance().dropItemMap(this.zone, new ItemMap(this.zone, 1503, 1, x3, y3, pl.id));  //update sau rơi đá ngũ sắc pro
        Service.getInstance().dropItemMap(this.zone, itemMap);
    }

    @Override
    public void joinMap() {
        this.zone = getMapCanJoin(mapID);
        int x = Util.nextInt(50, this.zone.map.mapWidth - 50);
        ChangeMapService.gI().changeMap(this, this.zone, x, this.zone.map.yPhysicInTop(x, 0));
    }

    @Override
    public Zone getMapCanJoin(int mapId) {
        Zone map = MapService.gI().getZoneJoinByMapIdAndZoneId(this, mapId, zoneId);
        if (map.isBossCanJoin(this)) {
            return map;
        } else {
            return getMapCanJoin(mapJoin[Util.nextInt(0, mapJoin.length - 1)]);
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
//        textTalkMidle = new String[]{"Đưa ngọc rồng cho ta"};

    }

    @Override
    public synchronized int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (isDie()) {
            return 0;
        }

        // Kiểm tra xuyên giáp và giảm sát thương một nửa
        damage /= 3;

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
        this.changeToIdle();
    }

}
