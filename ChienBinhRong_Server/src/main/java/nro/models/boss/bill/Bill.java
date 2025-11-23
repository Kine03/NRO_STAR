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
import nro.services.TaskService;
import nro.utils.Util;


public class Bill extends Boss {

    public Bill() {
        super(BossFactory.BILL, BossData.BILL);
    }
    @Override
    public void rewards(Player pl) {
        ItemMap itemMap = null;
        int x = this.location.x;
        int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);

         if (Util.isTrue(1, 5)) {
            itemMap = new ItemMap(this.zone, 1500, 1, x, y, pl.id);
        } else if (Util.isTrue(1, 5)) {
            itemMap = new ItemMap(this.zone, Util.nextInt(807,813), 1, x, y, pl.id);
        }
        if (Manager.EVENT_SEVER == 4 && itemMap == null) {
            itemMap = new ItemMap(this.zone, ConstItem.LIST_ITEM_NLSK_TET_2023[Util.nextInt(0, ConstItem.LIST_ITEM_NLSK_TET_2023.length - 1)], 1, x, y, pl.id);
            itemMap.options.add(new ItemOption(74, 0));
        }
        if (itemMap != null) {
            Service.getInstance().dropItemMap(zone, itemMap);
        }
        generalRewards(pl);
        TaskService.gI().checkDoneTaskKillBoss(pl, this);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }
    @Override
    public void leaveMap() {// roi map
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }

    @Override
    public void idle() {
    }

    @Override
    public void checkPlayerDie(Player pl) {
    }

    @Override
    public void initTalk() {
    }
}
