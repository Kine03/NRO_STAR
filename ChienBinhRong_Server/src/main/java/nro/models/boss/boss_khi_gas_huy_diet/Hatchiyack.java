/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nro.models.boss.boss_khi_gas_huy_diet;

import nro.models.boss.boss_ban_do_kho_bau.*;
import nro.consts.ConstRatio;
import nro.consts.MapName;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.map.phoban.BanDoKhoBau;
import nro.models.player.Player;
import nro.services.SkillService;
import nro.services.func.ChangeMapService;
import nro.utils.Util;

import java.util.List;
import nro.consts.ConstPlayer;
import nro.models.boss.Boss;
import nro.models.boss.BossManager;
import nro.models.boss.mabu_war.BossMabuWar;
import nro.models.boss.mapoffline.Boss_ThanMeo;
import nro.models.boss.mapoffline.Boss_Yanjiro;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.map.phoban.KhiGas;
import nro.server.ServerNotify;
import nro.services.PlayerService;
import nro.services.Service;
import nro.utils.Log;


public class Hatchiyack extends BossKhiGasHuyDiet {

    private boolean activeAttack;

    public Hatchiyack(KhiGas khiGas) {
        super(BossFactory.HATCHIJACK, BossData.HATCHIJACK, khiGas);
    }

    @Override
    public void attack() {
        try {
            if (!useSpecialSkill()) {
                Player pl = getPlayerAttack();
                this.playerSkill.skillSelect = this.getSkillAttack();
                if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
                    if (Util.isTrue(10, ConstRatio.PER100)) {
                        goToXY(pl.location.x + Util.nextInt(-20, 20), Util.nextInt(pl.location.y - 80, this.zone.map.yPhysicInTop(pl.location.x, 0)), false);
                    }
                    SkillService.gI().useSkill(this, pl, null, null);
                    checkPlayerDie(pl);
                } else {
                    goToPlayer(pl, false);
                }
            }
        } catch (Exception ex) {
        }
    }

    @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }

    @Override
    public void rewards(Player pl) {
        pl.clan.khiGas.initBoss2();
        int[] nro = {17, 18, 19, 20, 539};
        ItemMap itemMap = new ItemMap(this.zone, nro[Util.nextInt(0, nro.length - 1)], 1,
                this.location.x, this.zone.map.yPhysicInTop(this.location.x, 100), -1);
        itemMap.options.add(new ItemOption(73, 0));
        Service.getInstance().dropItemMap(this.zone, itemMap);

        int[] dns = {674, 1450};
        ItemMap itemMap2 = new ItemMap(this.zone, dns[Util.nextInt(0, dns.length - 1)], Util.nextInt(1, 5),
                this.location.x, this.zone.map.yPhysicInTop(this.location.x, 100), -1);
        Service.getInstance().dropItemMap(this.zone, itemMap2);
    }
}
