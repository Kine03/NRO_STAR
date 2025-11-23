package nro.models.map.phoban;

import nro.models.boss.boss_ban_do_kho_bau.BossBanDoKhoBau;
import nro.models.clan.Clan;
import nro.models.map.TrapMap;
import nro.models.map.Zone;
import nro.models.mob.GuardRobot;
import nro.models.mob.Mob;
import nro.models.mob.Octopus;
import nro.models.player.Player;
import nro.services.ItemTimeService;
import nro.services.MobService;
import nro.services.func.ChangeMapService;
import nro.utils.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import nro.models.boss.boss_ban_do_kho_bau.TrungUyXanhLo;
import nro.models.boss.boss_khi_gas_huy_diet.BossKhiGasHuyDiet;
import nro.models.boss.boss_khi_gas_huy_diet.DrLychee;
import nro.models.boss.boss_khi_gas_huy_diet.Hatchiyack;
import nro.services.MapService;
import nro.services.Service;

/**
 *
 * @author 💖 Trần Lại 💖
 * @copyright 💖 GirlkuN 💖
 *
 */
public class KhiGas {

    public static final long POWER_CAN_GO_TO_KHI_GAS = 2000000000;

    public static final List<KhiGas> KHI_GAS;

    public static final int MAX_AVAILABLE = 50;

    public static final int TIME_KHI_GAS = 1800000; // 30 phút

    public static int TIME_DELAY_DONE_KG_SOM = 70;
    public boolean HachiyackIsDie;

    static {
        KHI_GAS = new ArrayList<>();
        for (int i = 0; i < MAX_AVAILABLE; i++) {
            KHI_GAS.add(new KhiGas(i));
        }
    }

    public int id;

    public byte level;

    public final List<Zone> zones;

    public final List<BossKhiGasHuyDiet> bosses;

    public Clan clan;

    public boolean isOpened;

    public long lastTimeOpen;

    public boolean doneKGSom = false;

    public long lasTimeDoneKhiGasSom;

    public boolean removeText = false;

    public boolean sendTextThongBaoSapKetThuc = false;

    public long timeSendThongBaoKhiGas;

    public boolean isSpawnDrLychee = false;

    public boolean isDoneSpawnBoss = false;
    
    
    
    public KhiGas(int id) {
        this.id = id;
        this.zones = new ArrayList<>();
        this.bosses = new ArrayList<>();
    }

    public void update() {
        if (this.isOpened) {
            if (isSpawnDrLychee && !isDoneSpawnBoss) {
                initBoss();
                isDoneSpawnBoss = true;
            }
            if (doneKGSom == true) {
                if (TIME_DELAY_DONE_KG_SOM > 0) {
                    for (Player pl : this.clan.membersInGame) {
                        if (removeText == false) {
                            ItemTimeService.gI().removeTextKhiGas(pl);
                            sendThanhTichKhiGas(pl);
                        }
                        pl.clan.khiGas.sendTextThongBaoSapKetThuc = true;
                        sendThongBaoKhiGas();
                        removeText = true;
                    }
                } else {
                    finish();
                }
            } else {
                if (Util.canDoWithTime(lastTimeOpen, TIME_KHI_GAS)) {
                    checkTimeKhiGas();
                }
                if (TIME_DELAY_DONE_KG_SOM == 0) {
                    finish();
                }
            }
        }
    }

    public void openKhiGasHuyDiet(Player plOpen, Clan clan, byte level) {
        this.level = level;
        this.lastTimeOpen = System.currentTimeMillis();
        this.isOpened = true;
        this.clan = clan;
        this.clan.timeOpenKhiGas = this.lastTimeOpen;
        this.clan.playerOpenKhiGas = plOpen;
        this.clan.khiGas = this;
        TIME_DELAY_DONE_KG_SOM = 60;
        HachiyackIsDie = false;
        resetKhiGas();
        ChangeMapService.gI().goToKhiGas(plOpen); // xíu sửa
        sendTextKhiGasHuyDiet();
    }

    public void sendThanhTichKhiGas(Player pl) {
        long timeDoneKhiGas;
        timeDoneKhiGas = System.currentTimeMillis() - pl.clan.timeOpenKhiGas;
        int levelDoneKG;
        levelDoneKG = pl.clan.khiGas.level;
        if (levelDoneKG > pl.clan.levelDoneKhiGas) {
            pl.clan.levelDoneKhiGas = levelDoneKG;
            pl.clan.thoiGianHoanThanhKhiGas = timeDoneKhiGas;
            System.out.println("levelDoneKG: " + levelDoneKG);
            System.out.println("timeDoneKhiGas: " + timeDoneKhiGas);
        } else if (levelDoneKG == pl.clan.levelDoneKhiGas) {
            if (timeDoneKhiGas < pl.clan.thoiGianHoanThanhKhiGas) {
                pl.clan.thoiGianHoanThanhKhiGas = timeDoneKhiGas;
            }
        }
        pl.clan.updatethanhTichKG(pl.clan.id);
        pl.clan.updatethanhTichKGForLeader();
        pl.clan.updateThongTinLeader(pl.clan.id);
    }
    
    private void resetKhiGas() {
        for (Zone zone : zones) {
            for (TrapMap trap : zone.trapMaps) {
                trap.dame = this.level * 10000;
            }
        }
        for (Zone zone : zones) {
            for (Mob m : zone.mobs) {
                if (m instanceof Octopus) {
                    m.location.x = 740;
                    m.location.y = 576;
                }
                if (m instanceof GuardRobot) {
                    m.location.x = 550;
                    m.location.y = 336;
                }
                MobService.gI().initMobKhiGas(m, this.level); // xíu sửa
                MobService.gI().hoiSinhMob(m);
            }
        }
        for (BossKhiGasHuyDiet boss : bosses) {
            boss.leaveMap();
        }
        this.bosses.clear();
        initBoss();
        initBoss2();
    }

    private void initBoss() {
        this.bosses.add(new DrLychee(this));
    }
    public void initBoss2() {
        this.bosses.add(new Hatchiyack(this));
    }
    public void doneKG_byAdmin() {
        finish();
    }

    //kết thúc bản đồ kho báu
    private void finish() {
        List<Player> plOutDT = new ArrayList();
        for (Zone zone : zones) {
            List<Player> players = zone.getPlayers();
            synchronized (players) {
                for (Player pl : players) {
                    plOutDT.add(pl);
                }
            }
        }
        for (Player pl : plOutDT) {
            ChangeMapService.gI().changeMap(pl, 176, -1, 568,-1);
            ItemTimeService.gI().removeTextKhiGas(pl);
        }
        this.clan.khiGas = null;
        this.clan = null;
        this.isOpened = false;

    }

    public Zone getMapById(int mapId) {
        for (Zone zone : zones) {
            if (zone.map.mapId == mapId) {
                return zone;
            }
        }
        return null;
    }
    
    public void checkTimeKhiGas() {
        for (Player player : this.clan.membersInGame) {
            player.clan.khiGas.sendTextThongBaoSapKetThuc = true;
            sendThongBaoKhiGas();
        }
    }
    
    public void sendThongBaoKhiGas() {
        for (Player player : this.clan.membersInGame) {
            if (sendTextThongBaoSapKetThuc = true) {
                if (player.clan != null && player.clan.khiGas != null && player.clan.khiGas.isOpened && player.clan.timeOpenKhiGas != 0) {
                    if (MapService.gI().isMapKhiGas(player.zone.map.mapId)) {
                        if (Util.canDoWithTime(timeSendThongBaoKhiGas, 1000 * 10)) {
                            if (TIME_DELAY_DONE_KG_SOM == 70) {
                                Service.getInstance().sendThongBao(player, "Nơi này sắp nổ tung, mau chạy đi");
                                timeSendThongBaoKhiGas = System.currentTimeMillis();
                            }
                            TIME_DELAY_DONE_KG_SOM -= 10;
                            System.out.println("TIME_DELAY_DONE_KHI_GAS_SOM: " + TIME_DELAY_DONE_KG_SOM);
                            if (TIME_DELAY_DONE_KG_SOM > 0) {
                                Service.getInstance().sendThongBao(player, "Về làng Aru sau " + TIME_DELAY_DONE_KG_SOM + " giây nữa");
                                timeSendThongBaoKhiGas = System.currentTimeMillis();
                            }
                        }
                    }
                }
            }
        }
    }

    public static void addZone(int idBanDo, Zone zone) {
        KHI_GAS.get(idBanDo).zones.add(zone);
    }

    private void sendTextKhiGasHuyDiet() {
        for (Player pl : this.clan.membersInGame) {
            ItemTimeService.gI().sendTextKhiGas(pl);
        }
    }
}
