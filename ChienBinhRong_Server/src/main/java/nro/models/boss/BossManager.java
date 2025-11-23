package nro.models.boss;

import nro.utils.Log;
import java.util.ArrayList;
import java.util.List;
import nro.models.boss.boss_ban_do_kho_bau.BossBanDoKhoBau;
import nro.models.boss.boss_doanh_trai.BossDoanhTrai;
import nro.models.boss.cdrd.CBoss;
import nro.models.boss.dhvt.BossDHVT;
import nro.models.boss.event.SantaClaus;
import nro.models.boss.mabu_war.BossMabuWar;
import nro.models.player.Player;
import nro.server.io.Message;
import nro.utils.Util;

/**
 *
 * @author 💖 Trần Lại 💖
 * @copyright 💖 GirlkuN 💖
 *
 */
public class BossManager {

    public static final List<Boss> BOSSES_IN_GAME = new ArrayList<>();
    private static BossManager intance;
    

    public static void initBoss() {
        new Thread(() -> {
            try {
                // gọi từ BossFactory để tạo boss
                for (int i = 0; i < 3; i++) {
                    BossFactory.createBoss(BossFactory.BROLY);
                }
                BossFactory.createBoss(BossFactory.KUKU);
                BossFactory.createBoss(BossFactory.MAP_DAU_DINH);
                BossFactory.createBoss(BossFactory.RAMBO);
                BossFactory.createBoss(BossFactory.TIEU_DOI_TRUONG);
                BossFactory.createBoss(BossFactory.FIDE_DAI_CA_1);
                BossFactory.createBoss(BossFactory.ANDROID_20);
                BossFactory.createBoss(BossFactory.ANDROID_13);
                BossFactory.createBoss(BossFactory.KINGKONG);
                BossFactory.createBoss(BossFactory.XEN_BO_HUNG_1);

                for (int i = 0; i < 3; i++) {
                    BossFactory.createBoss(BossFactory.BLACKGOKU);
                }

                BossFactory.createBoss(BossFactory.PIKKON);
                BossFactory.createBoss(BossFactory.BILLCON);
                BossFactory.createBoss(BossFactory.CHILL);
                BossFactory.createBoss(BossFactory.COOLER);
                BossFactory.createBoss(BossFactory.XEN_BO_HUNG);
                BossFactory.createBoss(BossFactory.XIN);
                BossFactory.createBoss(BossFactory.YAM);

                BossFactory.createBoss(BossFactory.CUMBER);
                BossFactory.createBoss(BossFactory.DOREMON);
                BossFactory.createBoss(BossFactory.MABU);
                BossFactory.createBoss(BossFactory.RONGOMEGA);

                for (int i = 0; i < 10; i++) {
                    BossFactory.createBoss(BossFactory.DOGDIANGUC);
                    BossFactory.createBoss(BossFactory.KYLAN);
                }

                Log.success("✅ BossManager: Khởi tạo boss thành công.");

            } catch (Exception e) {
                Log.error("❌ Lỗi khi init boss", e);
            }
        }).start();
    }
    public void updateAllBoss() {
        for (int i = BOSSES_IN_GAME.size() - 1; i >= 0; i--) {
            try {
                Boss boss = BOSSES_IN_GAME.get(i);
                if (boss != null) {
                    boss.update();
                }
            } catch (Exception e) {
                Log.error(BossManager.class, e);
            }
        }

    }

    private BossManager() {

    }

    public static BossManager gI() {
        if (intance == null) {
            intance = new BossManager();
        }
        return intance;
    }

    public List<Boss> getBosses() {
        return BossManager.BOSSES_IN_GAME;
    }

//    public Boss getBossTau77ByPlayer(Player player) {
//        for (int i = BOSSES_IN_GAME.size() - 1; i >= 0; i--) {
//            if (BOSSES_IN_GAME.get(i).id == (-251003 - player.id - 2000)) {
//                return BOSSES_IN_GAME.get(i);
//            }
//        }
//        return null;
//    }

    public Boss getBossById(int bossId) {
        for (int i = BOSSES_IN_GAME.size() - 1; i >= 0; i--) {
            if (BOSSES_IN_GAME.get(i).id == bossId) {
                return BOSSES_IN_GAME.get(i);
            }
        }
        return null;
    }

    public void addBoss(Boss boss) {
        boolean have = false;
        for (Boss b : BOSSES_IN_GAME) {
            if (boss.equals(b)) {
                have = true;
                break;
            }
        }
        if (!have) {
            BOSSES_IN_GAME.add(boss);

        }
    }

    public void removeBoss(Boss boss) {
        BOSSES_IN_GAME.remove(boss);
        boss.dispose();
    }

    public void showListBoss(Player player) {
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Boss (SL: " + BOSSES_IN_GAME.size() + ")");
            long aliveBossCount = BOSSES_IN_GAME.stream().filter(boss -> boss != null).filter(boss
                    -> !BossFactory.isYar((byte) boss.id)
                    && !BossFactory.isbossphoban((int) boss.id))
                    .count();
            msg.writer().writeByte((int) aliveBossCount);
            for (int i = 0; i < BOSSES_IN_GAME.size(); i++) {
                Boss boss = BossManager.BOSSES_IN_GAME.get(i);
                if (BossFactory.isYar((byte) boss.id)
                        || BossFactory.isbossphoban((int) boss.id)) {
                    continue;
                }
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeShort(boss.getHead());
                if (player.isVersionAbove(220)) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(boss.getBody());
                msg.writer().writeShort(boss.getLeg());
                msg.writer().writeUTF(boss.name);
                if (boss.zone != null) {
                    msg.writer().writeUTF("Đã xuất hiện");
                    msg.writer().writeUTF(boss.zone.map.mapName + "(" + boss.zone.map.mapId + ")" + "\nkhu vực: " + boss.zone.zoneId);
                } else {
                    msg.writer().writeUTF("Chết");
                    msg.writer().writeUTF("Chưa xuất hiện");
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showListBossMember(Player player) {
        Message msg = new Message(-96);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("List BOSS");
            long aliveBossCount = BOSSES_IN_GAME.stream().filter(boss -> boss != null).filter(boss
                    -> !BossFactory.isYar((byte) boss.id)
                    && !BossFactory.isbossphoban((int) boss.id))
                    .count();
            msg.writer().writeByte((int) aliveBossCount);
            for (int i = 0; i < BOSSES_IN_GAME.size(); i++) {
                Boss boss = BossManager.BOSSES_IN_GAME.get(i);
                if (BossFactory.isYar((byte) boss.id)
                        || BossFactory.isbossphoban((int) boss.id)) {
                    continue;
                }
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeShort(boss.getHead());
                if (player.isVersionAbove(220)) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(boss.getBody());
                msg.writer().writeShort(boss.getLeg());
                msg.writer().writeUTF(boss.name);
                if (boss.zone != null) {
                    msg.writer().writeUTF("Đã xuất hiện");
                    msg.writer().writeUTF(boss.zone.map.mapName + "(" + boss.zone.map.mapId + ")");
                } else {
                    msg.writer().writeUTF("Chết");
                    msg.writer().writeUTF("Chưa xuất hiện");
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
