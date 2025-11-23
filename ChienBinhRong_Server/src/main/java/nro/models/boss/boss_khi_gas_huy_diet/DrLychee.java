/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nro.models.boss.boss_khi_gas_huy_diet;

import nro.consts.ConstPlayer;
import nro.consts.ConstRatio;
import nro.consts.MapName;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.player.Player;
import nro.services.SkillService;
import nro.utils.Util;

import nro.models.boss.Boss;
import nro.models.boss.BossManager;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.map.phoban.KhiGas;
import nro.models.skill.Skill;
import nro.services.EffectSkillService;
import nro.services.PlayerService;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.func.ChangeMapService;
import nro.utils.Log;

/**
 *
 * @author kitakeyos - Hoàng Hữu Dũng
 */
public class DrLychee extends BossKhiGasHuyDiet {

    private boolean isDie = false;

    public DrLychee(KhiGas khigas) {
        super(BossFactory.DR_LYCHEE, BossData.DR_LYCHEE, khigas);
    }


    @Override
    public void initTalk() {
        this.textTalkBefore = new String[]{"Ta đợi các ngươi mãi", "Bọn xayda các ngươi mau đền tội đi"};
        this.textTalkMidle = new String[]{"Đại bác báo thù...", "Heyyyyyyyy yaaaaa", "Hahaha", "ai da"};
        this.textTalkAfter = new String[]{"Các ngươi khá lắm", "Hatchiyack sẽ thay ta báo thù"};
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

    @Override
    public void joinMap() {
        try {
            this.zone = khiGas.getMapById(148);
            ChangeMapService.gI().changeMap(this, this.zone, 513, 480);
        } catch (Exception e) {
        }
    }
}
