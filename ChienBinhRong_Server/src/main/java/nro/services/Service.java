package nro.services;

import java.io.DataOutputStream;
import nro.consts.Cmd;
import nro.consts.ConstNpc;
import nro.consts.ConstPlayer;
import nro.data.DataGame;
import nro.jdbc.daos.AccountDAO;
import nro.manager.TopPowerManager;
import nro.models.Part;
import nro.models.PartManager;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.map.Zone;
import nro.models.map.dungeon.zones.ZDungeon;
import nro.models.mob.Mob;
import nro.models.player.Pet;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.power.Caption;
import nro.power.CaptionManager;
import nro.server.Client;
import nro.server.Manager;
import nro.server.io.Message;
import nro.server.io.Session;
import nro.services.func.Input;
import nro.utils.FileIO;
import nro.utils.Log;
import nro.utils.TimeUtil;
import nro.utils.Util;

import java.io.IOException;
import static java.lang.System.currentTimeMillis;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import nro.consts.ConstAchive;
import nro.consts.ConstItem;
import nro.consts.ConstTask;
import nro.dialog.ConfirmDialog;
import nro.jdbc.DBService;
import nro.manager.MyClanTopBanDoKhoBau;
import nro.manager.MyClanTopKhiGas;
import nro.manager.TopBanDoKhoBau;
import nro.manager.TopKhiGas;
import nro.manager.TopKillWhisManager;
import nro.manager.TopRichManManager;
import nro.manager.TopToTask;
import nro.manager.Topmaydam;
import nro.manager.TopoHp;
import nro.manager.TopoKi;
import nro.manager.TopoSd;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.boss.mapoffline.Boss_Tau77;
import nro.models.clan.Clan;
import nro.models.npc.specialnpc.MabuEgg;
import nro.models.player.Inventory;
import nro.sendEff.SendEffect;
import nro.server.Maintenance;
import nro.server.TaiXiu;
import nro.services.func.MiniGame;
import nro.services.func.UseItem;
import nro.utils.SkillUtil;

public class Service {

    private static Service instance;

    public static Service getInstance() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }

    public static Service gI() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }

    public void removeTitle(Player player) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(2);
            me.writer().writeInt((int) player.id);
            player.getSession().sendMessage(me);
            this.sendMessAllPlayerInMap(player, me);
            me.cleanup();
            if (player.titleitem) {
                Service.getInstance().sendTitle(player, player.partDanhHieu);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTitle(Player playerReveice, Player playerInfo) {
        if (playerInfo.inventory.itemsBody.size() > 14) {
            Item item = playerInfo.inventory.itemsBody.get(13);
            if (item.isNotNullItem()) {
                short part = item.template.part;
                Message me;
                try {
                    me = new Message(-128);
                    me.writer().writeByte(0);
                    me.writer().writeInt((int) playerInfo.id);
                    if (playerInfo.titleitem) {
                        me.writer().writeShort(part);
                    }
                    me.writer().writeShort(part);
                    me.writer().writeByte(1);
                    me.writer().writeByte(-1);
                    me.writer().writeShort(50);
                    me.writer().writeByte(-1);
                    me.writer().writeByte(-1);
                    playerReveice.sendMessage(me);
                    me.cleanup();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void releaseCooldownSkill(Player pl) {
        Message msg;
        try {
            msg = new Message(-94);
            for (Skill skill : pl.playerSkill.skills) {
                skill.coolDown = 0;
                msg.writer().writeShort(skill.skillId);
                int leftTime = (int) (skill.lastTimeUseThisSkill + skill.coolDown - System.currentTimeMillis());
                if (leftTime < 0) {
                    leftTime = 0;
                }
                msg.writer().writeInt(leftTime);
            }
            pl.sendMessage(msg);
            pl.nPoint.setMp(pl.nPoint.mpMax);
            PlayerService.gI().sendInfoHpMpMoney(pl);
            msg.cleanup();

        } catch (Exception e) {
        }
    }

    public void Mabu14hAttack(Boss mabu, Player plAttack, int x, int y, byte skillId) {
        mabu.isUseSpeacialSkill = true;
        mabu.lastTimeUseSpeacialSkill = System.currentTimeMillis();
        try {
            Message msg = new Message(51);
            msg.writer().writeInt((int) mabu.id);
            msg.writer().writeByte(skillId);
            msg.writer().writeShort(x);
            msg.writer().writeShort(y);
            if (skillId == 1) {
                msg.writer().writeByte(1);
                int dame = plAttack.injured(mabu, (int) (mabu.nPoint.getDameAttack(false) * (skillId == 1 ? 1.5 : 1)), false, false);
                msg.writer().writeInt((int) plAttack.id);
                msg.writer().writeInt(dame);
            } else if (skillId == 0) {
                List<Player> listAttack = mabu.getListPlayerAttack(70);
                msg.writer().writeByte(listAttack.size());
                for (int i = 0; i < listAttack.size(); i++) {
                    Player pl = listAttack.get(i);
                    int dame = pl.injured(mabu, mabu.nPoint.getDameAttack(false), false, false);
                    msg.writer().writeInt((int) pl.id);
                    msg.writer().writeInt(dame);
                }
                listAttack.clear();
            }
            sendMessAllPlayerInMap(mabu.zone, msg);
            mabu.isUseSpeacialSkill = false;
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void eatPlayer(Boss mabu, Player plHold) {
        mabu.isUseSpeacialSkill = true;
        mabu.lastTimeUseSpeacialSkill = System.currentTimeMillis();
        plHold.effectSkill.isTaskHoldMabu = 1;
        plHold.effectSkill.lastTimeHoldMabu = System.currentTimeMillis();
        try {
            Message msg = new Message(52);
            msg.writer().writeByte(2);
            msg.writer().writeInt((int) mabu.id);
            msg.writer().writeInt((int) plHold.id);
            sendMessAllPlayerInMap(mabu.zone, msg);
            mabu.isUseSpeacialSkill = false;
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void removeMabuEat(Player plHold) {
        PlayerService.gI().changeAndSendTypePK(plHold, ConstPlayer.NON_PK);
        plHold.effectSkill.isHoldMabu = false;
        plHold.effectSkill.isTaskHoldMabu = -1;
        Message msg;
        try {
            msg = new Message(52);
            msg.writer().writeByte(0);
            msg.writer().writeInt((int) plHold.id);
            sendMessAllPlayerInMap(plHold.zone, msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void sendTitle(Player player) {
        if (player.inventory.itemsBody.size() > 11) {
            Item item = player.inventory.itemsBody.get(12);
            if (item.isNotNullItem()) {
                sendTitle(player, item.template.part);
            }
        }
    }

    public void sendTitle(Player player, int part) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);
            if (player.titleitem) {
                me.writer().writeShort(part);
            }
            me.writer().writeShort(part);
            me.writer().writeByte(1);
            me.writer().writeByte(-1);
            me.writer().writeShort(50);
            me.writer().writeByte(-1);
            me.writer().writeByte(-1);
            this.sendMessAllPlayerInMap(player, me);
            me.cleanup();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPetFollow(Player player, short smallId) {
        Message msg;
        try {
            if (player != null) {
                msg = new Message(31);
                msg.writer().writeInt((int) player.id);
                if (smallId == 0) {
                    msg.writer().writeByte(0);// type 0
                } else {
                    msg.writer().writeByte(1);// type 1
                    msg.writer().writeShort(smallId);
                    msg.writer().writeByte(1);
                    int[] fr = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
                    msg.writer().writeByte(fr.length);
                    for (int i = 0; i < fr.length; i++) {
                        msg.writer().writeByte(fr[i]);
                    }
                    msg.writer().writeShort(smallId == 15067 ? 65 : 75);
                    msg.writer().writeShort(smallId == 15067 ? 65 : 75);
                }
                sendMessAllPlayerInMap(player, msg);
                msg.cleanup();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendEffAllPlayerMapToMe(Player pl) {
        try {
            for (Player plM : pl.zone.getPlayers()) {
                if (plM.isPl() && plM.inventory.itemsBody.size() >= 12) {

                    Item chanmenh = plM.inventory.itemsBody.get(12);
                    if (chanmenh.isNotNullItem()) {
                        Service.getInstance().sendEffPlayer(plM, pl, chanmenh.template.part, 0, -1, 1);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public void sendEffPlayer(Player pl, Player plReceive, int idEff, int layer, int loop, int loopCount) {
        Message msg = null;
        try {
            msg = new Message(-128);
            msg.writer().writeByte(0);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeShort(idEff);
            msg.writer().writeByte(layer);
            msg.writer().writeByte(loop);
            msg.writer().writeShort(loopCount);
            msg.writer().writeByte(0);
            plReceive.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendEffPlayer(Player pl) {
//        if (pl.isPl()) {
//            Item danhhieu = pl.inventory.itemsBody.get(11);
//            if (danhhieu.isNotNullItem()) {
//                Service.getInstance().sendEffAllPlayer(pl, danhhieu.template.part, 1, -1, 1);
//            }
        if (pl.inventory.itemsBody.size() > 10) {
            Item chanMenh = pl.inventory.itemsBody.get(11);
            if (chanMenh.isNotNullItem()) {
                Service.getInstance().sendEffAllPlayer(pl, chanMenh.template.part, 0, -1, 1);
            }
        }
//        else {
//            // Xử lý khi danh sách không đủ phần tử
//            System.out.println("itemsBody does not have enough elements.");
//            // Hoặc ghi log, gửi thông báo lỗi, v.v.
//        }

//        }
    }

    public void removeEffPlayer(Player pl, int idEff) {
        Message msg = null;
        try {
            msg = new Message(-128);
            msg.writer().writeByte(1);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeShort(idEff);
            sendMessAllPlayerInMap(pl.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendEffAllPlayer(Player pl, int idEff, int layer, int loop, int loopCount) {
        Message msg = null;
        try {
            msg = new Message(-128);
            msg.writer().writeByte(0);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeShort(idEff);
            msg.writer().writeByte(layer);
            msg.writer().writeByte(loop);
            msg.writer().writeShort(loopCount);
            msg.writer().writeByte(0);
            sendMessAllPlayerInMap(pl.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessAllPlayer(Message msg) {
        msg.transformData();
        PlayerService.gI().sendMessageAllPlayer(msg);
    }

    public void sendMessAllPlayerIgnoreMe(Player player, Message msg) {
        msg.transformData();
        PlayerService.gI().sendMessageIgnore(player, msg);
    }

    public void sendMessAllPlayerInMap(Zone zone, Message msg) {
        msg.transformData();
        if (zone != null) {
            List<Player> players = zone.getPlayers();
            synchronized (players) {
                for (Player pl : players) {
                    if (pl != null) {
                        pl.sendMessage(msg);
                    }
                }
            }
            msg.cleanup();
        }
    }

    public void showListBoss(Player pl) {
//        List<Zone> list = null;
//        switch (pl.iDMark.getTypeChangeMap()) {
//        }
        Message msg;
        try {
            msg = new Message(-91);
            msg.writer().writeByte(BossManager.BOSSES_IN_GAME.size());
            for (int i = 0; i < BossManager.BOSSES_IN_GAME.size(); i++) {
                Boss boss = BossManager.BOSSES_IN_GAME.get(i);
                if (boss.zone != null) {
                    msg.writer().writeUTF("Boss: " + boss.name + " ( HP:" + Util.numberToMoney(boss.nPoint.hp) + ")");
                    msg.writer().writeUTF(boss.zone.map.mapName + " (" + boss.zone.map.mapId + ")" + " khu vực: " + boss.zone.zoneId);
                } else {
                    msg.writer().writeUTF("Boss: " + boss.name);
                    msg.writer().writeUTF("Chưa xuất hiện");
                }
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendPopUpMultiLine(Player pl, int tempID, int avt, String text) {
        Message msg = null;
        try {
            msg = new Message(-218);
            msg.writer().writeShort(tempID);
            msg.writer().writeUTF(text);
            msg.writer().writeShort(avt);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
//            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }

    public void sendMessAllPlayerInMap(Player player, Message msg) {
        if (player == null || player.zone == null) {
            return; // Tránh lỗi NullPointerException
        }

        msg.transformData();

        if (player.zone.map.isMapOffline) {
            if (player.isPet) {
                ((Pet) player).master.sendMessage(msg);
            } else {
                List<Player> players = player.zone.getPlayers();
                synchronized (players) {
                    for (Player pl : players) {
                        if (pl != null) {
                            pl.sendMessage(msg);
                        }
                    }
                }
                msg.cleanup();
            }
        } else {
            List<Player> players = player.zone.getPlayers();
            synchronized (players) {
                for (Player pl : players) {
                    if (pl != null) {
                        pl.sendMessage(msg);
                    }
                }
            }
            msg.cleanup();
        }
    }

    public void sendMessAnotherNotMeInMap(Player player, Message msg) {
        if (player.zone != null) {
            List<Player> players = player.zone.getPlayers();
            synchronized (players) {
                for (Player pl : players) {
                    if (pl != null && !pl.equals(player)) {
                        pl.sendMessage(msg);
                    }
                }
            }

            msg.cleanup();
        }
    }

    public void Send_Info_NV(Player pl) {
        Message msg;
        try {
            msg = Service.getInstance().messageSubCommand((byte) 14);//Cập nhật máu
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeInt(pl.nPoint.hp);
            msg.writer().writeByte(0);//Hiệu ứng Ăn Đậu
            msg.writer().writeInt(pl.nPoint.hpMax);
            sendMessAnotherNotMeInMap(pl, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void sendInfoPlayerEatPea(Player pl) {
        Message msg;
        try {
            msg = Service.getInstance().messageSubCommand((byte) 14);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeInt(pl.nPoint.hp);
            msg.writer().writeByte(1);
            msg.writer().writeInt(pl.nPoint.hpMax);
            sendMessAnotherNotMeInMap(pl, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void loginDe(Session session, short second) {
        Message msg;
        try {
            msg = new Message(122);
            msg.writer().writeShort(second);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void resetPoint(Player player, int x, int y) {
        Message msg;
        try {
            player.location.x = x;
            player.location.y = y;
            msg = new Message(46);
            msg.writer().writeShort(x);
            msg.writer().writeShort(y);
            player.sendMessage(msg);
            msg.cleanup();

        } catch (Exception e) {
        }
    }

//    public void SendImgSkill9(Player player) {
//        Message msg;
//        try {
//            msg = new Message(62);
//            msg.writer().writeShort(24);
//            msg.writer().writeByte(1);
//            msg.writer().writeByte(3);
//            player.sendMessage(msg);
//            msg.cleanup();
//
//        } catch (Exception e) {
//        }
//    }
    public void SendImgSkill9(short SkillId, int IdAnhSKill) {
        Message msg = new Message(62);
        DataOutputStream ds = msg.writer();
        try {
            ds.writeShort(SkillId);
            ds.writeByte(1);
            ds.writeByte(IdAnhSKill);
            ds.flush();
            Service.getInstance().sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

//    public void SendImgSkill9_TD(short SkillId) {
//        Message msg = new Message(62);
//        DataOutputStream ds = msg.writer();
//        try {
//            ds.writeShort(SkillId);
//            ds.writeByte(1);
//            ds.writeByte(3);
//            System.out.println("OK");
//            ds.flush();
//            Service.getInstance().sendMessAllPlayer(msg);
//            msg.cleanup();
//        } catch (Exception e) {
//        }
//    }
    public void regisAccount(Session session, Message _msg) {
        try {
            _msg.reader().readUTF();
            _msg.reader().readUTF();
            _msg.reader().readUTF();
            _msg.reader().readUTF();
            _msg.reader().readUTF();
            _msg.reader().readUTF();
            _msg.reader().readUTF();
            String user = _msg.reader().readUTF();;
            String pass = _msg.reader().readUTF();;
            if (!(user.length() >= 4 && user.length() <= 18)) {
                sendThongBaoOK(session, "Tài khoản phải có độ dài 4-18 ký tự");
                return;
            }
            if (!(pass.length() >= 5 && pass.length() <= 18)) {
                sendThongBaoOK(session, "Mật khẩu phải có độ dài 5-18 ký tự");
                return;
            }
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;
            try {
                connection = DBService.gI().getConnectionForGame();
                String sql = "SELECT * FROM `account` WHERE username = ?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, user);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.first()) {
                    sendThongBaoOK(session, "Tài khoản đã tồn tại");
                } else {
                    Connection con = DBService.gI().getConnectionForGame();
                    PreparedStatement ps = con.prepareStatement("INSERT INTO account (username, password) VALUES (?, ?)");
                    ps.setString(1, user);
                    ps.setString(2, pass);
                    ps.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void login2(Session session, String user) {
        Message msg;
        try {
            msg = new Message(62);
            DataOutputStream ds = msg.writer();
            ds.writeUTF(user);
            ds.writeByte(1);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            System.err.println("Error Login 2");
        }
    }

//    public void callTau77(Player player) {
//        if (TaskService.gI().getIdTask(player) > ConstTask.TASK_9_0 && TaskService.gI().getIdTask(player) < ConstTask.TASK_10_2 && BossManager.gI().getBossTau77ByPlayer(player) == null) {
//            try {
//                Boss_Tau77 dt = new Boss_Tau77(Util.createIdDuongTank((int) player.id), BossData.TAU_7_7, player.zone, -1, -1, (int) player.id);
//            } catch (Exception ex) {
//
//            }
//        }
//    }

    public void clearMap(Player player) {
        Message msg;
        try {
            msg = new Message(-22);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void send_HieuUng_ThanhCong(Player player, int iconsuccess) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(7);
            msg.writer().writeShort(iconsuccess);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    int test = 0;

    public void chat(Player player, String text) {
        if (player == null) {
            return;
        }
        if (player.getSession() != null && player.isAdmin()) {
            if (text.equals("logskill")) {
                Service.getInstance().sendThongBao(player, player.playerSkill.skillSelect.coolDown + "");
                return;
            }
//            if (text.startsWith("i")) {
//                String[] parts = text.split(" ");
//                if (parts.length >= 3) {
//                    short id = Short.parseShort(parts[1]);
//                    int quantity = Integer.parseInt(parts[2]);
//                    Item item = ItemService.gI().createNewItem(id, quantity);
//                    InventoryService.gI().addItemBag(player, item, quantity);
//                    InventoryService.gI().sendItemBags(player);
//                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + item.template.name + " số lượng: " + quantity);
//                    return;
//                } else {
//                    Service.getInstance().sendThongBao(player, "Lỗi");
//                    return;
//                }
//            }
            if (text.equals("loadshop")) {
                Manager.reloadShop();
                this.sendThongBao(player, "Load Shop Success");
                return;
            }
            if (text.equals("client")) {
                Client.gI().show(player);
                return;
            }
            if (text.equals("baotri_")) {
                int giay = Integer.getInteger(text.replaceAll("baotri_", ""));
                try {
                    Maintenance.gI().start(giay * 60);
                } catch (Exception e) {
                }
                return;
            }
            if (text.startsWith("thongbao")) {
                String a = text.replace("thongbao ", "");
                Service.gI().sendThongBaoAllPlayer(a);
            }
            if (text.equals("info")) {
                NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_PLAYER, 3789,
                        "|1|Nhân vật: " + player.name + "\n"
                        + "|7|Chỉ số hiện tại"
                        + "\nHành Tinh " + player.gender
                        + "\nSức đánh " + player.nPoint.dame
                        + "\nHp " + player.nPoint.hp + "/" + player.nPoint.hpMax
                        + "\nKi " + player.nPoint.mp + "/" + player.nPoint.mpMax
                        + "\n|7| Đệ Tử " + player.pet.name
                        + "\nHành Tinh " + player.pet.gender
                        + "\nSức Đánh " + player.pet.nPoint.dame
                        + "\nHp " + player.pet.nPoint.hp + "/" + player.pet.nPoint.hpMax
                        + "\nKi " + player.pet.nPoint.mp + "/" + player.pet.nPoint.hpMax, "Đóng");
                return;
            } else if (text.equals("shb")) {
                BossManager.gI().showListBoss(player);
                return;
            } else if (text.equals("player")) {
                showListPlayer(player);
                return;
            } else if (text.startsWith("i")) {
                try {
                    String[] item = text.replace("i", "").trim().split("\\s+");
                    short itemId = Short.parseShort(item[0]);

                    if (itemId <= 2128) {
                        Item it = ItemService.gI().createNewItem(itemId);
                        if (it == null) {
                            Service.gI().sendThongBao(player, "Không tạo được item");
                            return;
                        }

                        int quantity = 1;
                        String targetPlayerName = null;

                        if (item.length >= 2) {
                            try {
                                quantity = Integer.parseInt(item[1]);
                            } catch (NumberFormatException e) {
                                Service.gI().sendThongBao(player, "Số lượng không hợp lệ");
                                return;
                            }
                        }

                        if (item.length == 3) {
                            targetPlayerName = item[2];
                        } else if (item.length == 2) {
                            targetPlayerName = Client.gI().getPlayer(item[1]) == null ? null : item[1];
                        }

                        it.quantity = quantity;

                        if (targetPlayerName == null) {
                            InventoryService.gI().addItemBag(player, it, quantity);
                            InventoryService.gI().sendItemBags(player);
                            Service.gI().sendThongBao(player, "Đã nhận được x" + quantity + " " + it.template.name);
                        } else {
                            Player targetPlayer = Client.gI().getPlayer(targetPlayerName);
                            if (targetPlayer != null) {
                                InventoryService.gI().addItemBag(player, it, quantity);
                                InventoryService.gI().sendItemBags(targetPlayer);
                                Service.gI().sendThongBao(player, "Đã buff x" + quantity + " " + it.template.name + " đến player " + targetPlayerName);
                                Service.gI().sendThongBao(targetPlayer, "Đã nhận được x" + quantity + " " + it.template.name);
                            } else {
                                Service.gI().sendThongBao(player, "Không tìm thấy player: " + targetPlayerName);
                            }
                        }
                    } else {
                        Service.gI().sendThongBao(player, "Không tìm thấy item");
                    }
                } catch (NumberFormatException e) {
                    Service.gI().sendThongBao(player, "ID hoặc số lượng không hợp lệ");
                }
                return;
            } else if (text.startsWith("danhhieu ")) {
                int sodanhhieu = Integer.parseInt(text.replace("danhhieu ", ""));
                if (player.lastTimeTitle1 == 0 && player.IdDanhHieu_1 != sodanhhieu) {
                    if (player.lastTimeTitle1 == 0) {
                        player.lastTimeTitle1 += System.currentTimeMillis() + (1000 * 60 * 60 * 24);
                    }
                    player.isTitleUse = true;
                    player.IdDanhHieu_1 = sodanhhieu;
                    Service.getInstance().point(player);
                    SendEffect.getInstance().removeTitle(player);
                    Service.getInstance().sendMoney(player);
                    return;
                } else if (player.lastTimeTitle1 != 0 && player.lastTimeTitle2 == 0 && player.IdDanhHieu_1 != sodanhhieu && player.IdDanhHieu_2 != sodanhhieu) {
                    if (player.lastTimeTitle2 == 0) {
                        player.lastTimeTitle2 += System.currentTimeMillis() + (1000 * 60 * 60 * 24);
                    }
                    player.isTitleUse2 = true;
                    player.IdDanhHieu_2 = sodanhhieu;
                    Service.getInstance().point(player);
                    SendEffect.getInstance().removeTitle(player);
                    Service.getInstance().sendMoney(player);
                    return;
                } else if (player.lastTimeTitle1 != 0 && player.lastTimeTitle2 != 0 && player.lastTimeTitle3 == 0 && player.IdDanhHieu_1 != sodanhhieu && player.IdDanhHieu_2 != sodanhhieu && player.IdDanhHieu_3 != sodanhhieu) {
                    if (player.lastTimeTitle3 == 0) {
                        player.lastTimeTitle3 += System.currentTimeMillis() + (1000 * 60 * 60 * 24);
                    }
                    player.isTitleUse3 = true;
                    player.IdDanhHieu_3 = sodanhhieu;
                    Service.getInstance().point(player);
                    SendEffect.getInstance().removeTitle(player);
                    Service.getInstance().sendMoney(player);
                    return;
                } else if (player.lastTimeTitle1 != 0 && player.lastTimeTitle2 != 0 && player.lastTimeTitle3 != 0 && player.lastTimeTitle4 == 0 && player.IdDanhHieu_1 != sodanhhieu && player.IdDanhHieu_2 != sodanhhieu && player.IdDanhHieu_3 != sodanhhieu && player.IdDanhHieu_4 != sodanhhieu) {

                    if (player.lastTimeTitle4 == 0) {
                        player.lastTimeTitle4 += System.currentTimeMillis() + (1000 * 60 * 60 * 24);
                    }
                    player.IdDanhHieu_4 = sodanhhieu;
                    player.isTitleUse4 = true;
                    Service.getInstance().point(player);
                    SendEffect.getInstance().removeTitle(player);
                    Service.getInstance().sendMoney(player);
                    return;
                } else if (player.lastTimeTitle1 != 0 && player.lastTimeTitle2 != 0 && player.lastTimeTitle3 != 0 && player.lastTimeTitle4 != 0 && player.lastTimeTitle5 == 0 && player.IdDanhHieu_1 != sodanhhieu && player.IdDanhHieu_2 != sodanhhieu && player.IdDanhHieu_3 != sodanhhieu && player.IdDanhHieu_4 != sodanhhieu && player.IdDanhHieu_5 != sodanhhieu) {
                    if (player.lastTimeTitle5 == 0) {
                        player.lastTimeTitle5 += System.currentTimeMillis() + (1000 * 60 * 60 * 24);
                    }
                    player.IdDanhHieu_5 = sodanhhieu;
                    player.isTitleUse5 = true;
                    Service.getInstance().point(player);
                    SendEffect.getInstance().removeTitle(player);
                    Service.getInstance().sendMoney(player);
                    return;
                }
                return;
            }
            if (text.equals("skillnm")) {
                StringBuffer sb = new StringBuffer();
                int skillID = player.gender == 0 ? 24
                        : player.gender == 1 ? 26 : 25;
                Skill newSkill = SkillUtil.createSkill(skillID, 1);
                sb.append("|2|Ta sẽ dạy ngươi tuyệt kỹ ")
                        .append(newSkill.template.name).append("\n")
                        .append("Bí ki tuyệt kỹ ").append("/9999\n")
                        .append("Giá vàng : 500.000.000 \n")
                        .append("Giá hồng ngọc: 200");
                ConfirmDialog confirmDialog = new ConfirmDialog(sb.toString(),
                        () -> {
                            Inventory inv = player.inventory;
                            SkillUtil.setSkill(player, newSkill);
                            try {
                                Message msg = Service.getInstance()
                                        .messageSubCommand((byte) 23);
                                msg.writer().writeShort(newSkill.skillId);
                                player.sendMessage(msg);
                                msg.cleanup();
                            } catch (IOException e) {
                            }
                        });
                confirmDialog.show(player);
            }
            if (text.equals("dau_buoi")) {

            }
            if (text.equals("hoi_skill")) {
                Skill skill;
                for (int i = 0; i < player.playerSkill.skills.size(); i++) {
                    skill = player.playerSkill.skills.get(i);
                    skill.lastTimeUseThisSkill = System.currentTimeMillis() - (long) skill.coolDown;
                }
                Service.getInstance().sendTimeSkill(player);
            }
            if (text.equals("test_thu")) {
                Message msg;
                try {
                    msg = new Message(29);
                    msg.writer().writeByte(player.zone.map.zones.size());
                    for (Zone zone : player.zone.map.zones) {
                        msg.writer().writeByte(zone.zoneId);
                        int numPlayers = zone.getNumOfPlayers();
                        msg.writer().writeByte((numPlayers < 5 ? 0 : (numPlayers < 8 ? 1 : 2)));
                        msg.writer().writeByte(numPlayers);
                        msg.writer().writeByte(zone.maxPlayer);
                        msg.writer().writeByte(1);
                        msg.writer().writeUTF("test03");
                        msg.writer().writeInt(1);
                        msg.writer().writeUTF("test04");
                        msg.writer().writeInt(2);
                    }
                    sendMessAllPlayer(msg);
                    msg.cleanup();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (text.equals("csmm")) {
                Service.gI().sendThongBao(player, "Kết quả con số may mắn tiếp theo là: " + MiniGame.gI().MiniGame_S1.result_next);
                return;
            }
            if (text.equals("q")) {
                showListBoss(player);
                return;
            }
            if (text.equals("get_mabuegg")) {
                MabuEgg.createMabuEgg(player);
                Service.getInstance().sendThongBao(player, "Đã get thành công một quả trứng mabư");
                return;
            }
            if (text.equals("done_bdkb")) {
                player.clan.banDoKhoBau.doneBDKBSom = true;
                return;
            }
            if (text.equals("done_kg")) {
                player.clan.khiGas.doneKGSom = true;
                return;
            }
            if (text.equals("battu")) {
                player.isBatTu = true;
                Service.getInstance().sendThongBao(player, "Bất tử");
                return;
            }
            if (text.startsWith("dmg")) {
                try {
                    int dameg = Integer.parseInt(text.replaceAll("dmg", ""));
                    player.nPoint.dameg = dameg;
                    this.point(player);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (text.startsWith("hpg")) {
                try {
                    int hpg = Integer.parseInt(text.replaceAll("hpg", ""));
                    player.nPoint.hpg = hpg;
                    this.point(player);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (text.startsWith("mpg")) {
                try {
                    int mpg = Integer.parseInt(text.replaceAll("mpg", ""));
                    player.nPoint.mpg = mpg;
                    this.point(player);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (text.startsWith("defg")) {
                try {
                    int defg = Integer.parseInt(text.replaceAll("defg", ""));
                    player.nPoint.defg = defg;
                    this.point(player);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (text.startsWith("crg")) {
                try {
                    int critg = Integer.parseInt(text.replaceAll("crg", ""));
                    player.nPoint.critg = critg;
                    this.point(player);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (text.equals("admin")) {
                String str = "";
                NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_ADMIN, -1, "Count " + Manager.DOMAIN + " Server.\n" + "Số lượng người chơi hiện tại: " + Util.mumberToLouis(Client.gI().getPlayers().size()) + ".\n" + "Số lượng thread hiện tại: " + Thread.activeCount() + ".\n" + "Số lượng Session hiện tại: " + Client.gI().sessions.size(), "Ngọc rồng", "Đệ tử", "Bảo trì", "Tìm kiếm\nngười chơi", "Goi dien\nCho nguoi than", "Đóng");
                return;
            } else if (text.equals("toado")) {
                Service.getInstance().sendThongBao(player, player.location.x + " - " + player.location.y);
                return;
            } else if (text.equals("tn")) {
                Input.gI().createFormTangRuby(player);
                return;
            } else if (text.equals("buff")) {
                Input.gI().createFormAddItem(player);
                return;
            } else if (text.startsWith("upp")) {
                try {
                    long power = Long.parseLong(text.replaceAll("upp", ""));
                    addSMTN(player.pet, (byte) 2, power, false);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (text.startsWith("exp")) {
                try {
                    addSMTN(player.pet, (byte) 2, 28_000_000_000L, false);
                    System.out.println("truong ngu");
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (text.startsWith("up")) {
                try {
                    long power = Long.parseLong(text.replaceAll("up", ""));
                    addSMTN(player, (byte) 2, power, false);
                    return;
                } catch (Exception e) {
                }
            } else if (text.startsWith("m")) {
                try {
                    int mapId = Integer.parseInt(text.replace("m", ""));
                    Zone zone = MapService.gI().getZoneJoinByMapIdAndZoneId(player, mapId, 0);
                    if (zone != null) {
                        player.location.x = 500;
                        player.location.y = zone.map.yPhysicInTop(500, 100);
                        MapService.gI().goToMap(player, zone);
                        Service.getInstance().clearMap(player);
                        zone.mapInfo(player);
                        player.zone.loadAnotherToMe(player);
                        player.zone.load_Me_To_Another(player);
                    }
                    return;
                } catch (Exception e) {
//                    e.printStackTrace();
                    Service.gI().sendThongBaoOK(player, "Cú pháp: m_MapId ( _ là dấu cách )");
                }
            }
        }
        if (text.startsWith("ten con la ")) {
            PetService.gI().changeNamePet(player, text.replaceAll("ten con la ", ""));
        }
        if (text.equals("fixhanhtrang")) {
            Service.getInstance().player(player);
            Service.getInstance().Send_Caitrang(player);
        }
        if (player.pet != null) {
            if (text.equals("di theo") || text.equals("follow")) {
                if (!player.pet.isBoss) {
                    player.pet.changeStatus(Pet.FOLLOW);
                }

            } else if (text.equals("bao ve") || text.equals("protect")) {
                if (!player.pet.isBoss) {
                    player.pet.changeStatus(Pet.PROTECT);
                }

            } else if (text.equals("tan cong") || text.equals("attack")) {
                if (!player.pet.isBoss) {
                    player.pet.changeStatus(Pet.ATTACK);
                }

            } else if (text.equals("ve nha") || text.equals("go home")) {
                if (!player.pet.isBoss) {
                    player.pet.changeStatus(Pet.GOHOME);
                }
            } else if (text.equals("bien hinh")) {
                if (!player.pet.isBoss) {
                    player.pet.transform();

                }
            } else if (text.startsWith("m ")) {

                int mapId = Integer.parseInt(text.replace("m ", ""));
                Zone zone = MapService.gI().getZoneJoinByMapIdAndZoneId(player, mapId, 0);
                if (zone != null) {
                    player.location.x = 500;
                    player.location.y = zone.map.yPhysicInTop(500, 100);
                    MapService.gI().goToMap(player, zone);
                    Service.getInstance().clearMap(player);
                    zone.mapInfo(player);
                    player.zone.loadAnotherToMe(player);
                    player.zone.load_Me_To_Another(player);
                }
                return;
            }
        }

        if (text.length() > 100) {
            text = text.substring(0, 100);
        }
        chatMap(player, text);
    }

    public void chatMap(Player player, String text) {
        Message msg;
        try {
            msg = new Message(44);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeUTF(text);
            sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void ServerMessageVip(String text) {
        Message msg;
        try {
            msg = new Message(24);
            msg.writer().writeByte(4);
            msg.writer().writeUTF(text);
            Service.getInstance().sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void SendDanhHieu_Teamobi(Player player) {
        Message msg;
        try {
            msg = new Message(24);
            msg.writer().writeByte(2);
            msg.writer().writeInt(0);
            msg.writer().writeByte(0);
            Service.getInstance().sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void sendTimeWaitLogin(Session session, short second) {
        Message msg = null;
        try {
            msg = new Message(122);
            msg.writer().writeShort(second);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void chatJustForMe(Player me, Player plChat, String text) {
        Message msg;
        try {
            msg = new Message(44);
            msg.writer().writeInt((int) plChat.id);
            msg.writer().writeUTF(text);
            me.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void point(Player player) {
        player.nPoint.calPoint();
        Send_Info_NV(player);
        if (!player.isPet && !player.isBoss) {
            Message msg;
            try {
                msg = new Message(-42);
                msg.writer().writeInt(player.nPoint.hpg);
                msg.writer().writeInt(player.nPoint.mpg);
                msg.writer().writeInt(player.nPoint.dameg);
                msg.writer().writeInt(player.nPoint.hpMax);// hp full
                msg.writer().writeInt(player.nPoint.mpMax);// mp full
                msg.writer().writeInt(player.nPoint.hp);// hp
                msg.writer().writeInt(player.nPoint.mp);// mp
                msg.writer().writeByte(player.nPoint.speed);// speed
                msg.writer().writeByte(20);
                msg.writer().writeByte(20);
                msg.writer().writeByte(1);
                msg.writer().writeInt(player.nPoint.dame);// dam base
                msg.writer().writeInt(player.nPoint.def);// def full
                msg.writer().writeByte(player.nPoint.crit);// crit full
                msg.writer().writeLong(player.nPoint.tiemNang);
                msg.writer().writeShort(100);
                msg.writer().writeShort(player.nPoint.defg);
                msg.writer().writeByte(player.nPoint.critg);
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
                Log.error(Service.class, e);
            }
        }
        if (player.clone != null) {
            player.clone.nPoint.calPoint();
            player.clone.nPoint.setFullHpMp();
            point(player.clone);
        }
    }

    public void player(Player pl) {
        if (pl == null) {
            return;
        }
        Message msg;
        try {
            msg = messageSubCommand((byte) 0);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(pl.playerTask.taskMain.id);
            msg.writer().writeByte(pl.gender);
            msg.writer().writeShort(pl.head);
            msg.writer().writeUTF(pl.name);
            msg.writer().writeByte(0); //cPK
            msg.writer().writeByte(pl.typePk);
            msg.writer().writeLong(pl.nPoint.power);
            msg.writer().writeShort(0);
            msg.writer().writeShort(0);
            msg.writer().writeByte(pl.gender);
            //--------skill---------

            ArrayList<Skill> skills = (ArrayList<Skill>) pl.playerSkill.skills;

            msg.writer().writeByte(pl.playerSkill.getSizeSkill());

            for (Skill skill : skills) {
                if (skill.skillId != -1) {
                    msg.writer().writeShort(skill.skillId);
                }
            }

            //---vang---luong--luongKhoa
            long gold = pl.inventory.getGoldDisplay();
            if (pl.isVersionAbove(214)) {
                msg.writer().writeLong(gold);
            } else {
                msg.writer().writeInt((int) gold);
            }
            msg.writer().writeInt(pl.inventory.ruby);
            msg.writer().writeInt(pl.inventory.gem);

            //--------itemBody---------
            ArrayList<Item> itemsBody = (ArrayList<Item>) pl.inventory.itemsBody;
            msg.writer().writeByte(itemsBody.size());
            for (Item item : itemsBody) {
                if (!item.isNotNullItem()) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    List<ItemOption> itemOptions = item.getDisplayOptions();
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }
                }

            }

            //--------itemBag---------
            ArrayList<Item> itemsBag = (ArrayList<Item>) pl.inventory.itemsBag;
            msg.writer().writeByte(itemsBag.size());
            for (int i = 0; i < itemsBag.size(); i++) {
                Item item = itemsBag.get(i);
                if (!item.isNotNullItem()) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    List<ItemOption> itemOptions = item.getDisplayOptions();
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }
                }

            }

            //--------itemBox---------
            ArrayList<Item> itemsBox = (ArrayList<Item>) pl.inventory.itemsBox;
            msg.writer().writeByte(itemsBox.size());
            for (int i = 0; i < itemsBox.size(); i++) {
                Item item = itemsBox.get(i);
                if (!item.isNotNullItem()) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    List<ItemOption> itemOptions = item.getDisplayOptions();
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }
                }
            }
            //-----------------
            DataGame.sendHeadAvatar(msg);
            //-----------------
            msg.writer().writeShort(514); //char info id - con chim thông báo
            msg.writer().writeShort(515); //char info id
            msg.writer().writeShort(537); //char info id
            msg.writer().writeByte(pl.fusion.typeFusion != ConstPlayer.NON_FUSION ? 1 : 0); //nhập thể
//            msg.writer().writeInt(1632811835); //deltatime
            msg.writer().writeInt(333); //deltatime
            msg.writer().writeByte(pl.isNewMember ? 1 : 0); //is new member

//            if (pl.isAdmin()) {
            msg.writer().writeShort(pl.getAura()); //idauraeff
            msg.writer().writeByte(pl.getEffFront());
//            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public Message messageNotLogin(byte command) throws IOException {
        Message ms = new Message(-29);
        ms.writer().writeByte(command);
        return ms;
    }

    public Message messageNotMap(byte command) throws IOException {
        Message ms = new Message(-28);
        ms.writer().writeByte(command);
        return ms;
    }

    public Message messageSubCommand(byte command) throws IOException {
        Message ms = new Message(-30);
        ms.writer().writeByte(command);
        return ms;
    }

    public void addSMTN(Player player, byte type, long param, boolean isOri) {
        if (player.isPet) {
            if (player.nPoint.power > player.nPoint.getPowerLimit()) {
                return;
            }
            player.nPoint.powerUp(param);
            player.nPoint.tiemNangUp(param);
            Player master = ((Pet) player).master;

            param = master.nPoint.calSubTNSM(param);
            if (master.nPoint.power < master.nPoint.getPowerLimit()) {
                master.nPoint.powerUp(param);
            }
            master.nPoint.tiemNangUp(param);
            addSMTN(master, type, param, true);
        } else {
            if (player.nPoint.power > player.nPoint.getPowerLimit()) {
                return;
            }
            switch (type) {
                case 1:
                    player.nPoint.tiemNangUp(param);
                    break;
                case 2:
                    player.nPoint.powerUp(param);
                    player.nPoint.tiemNangUp(param);
                    break;
                default:
                    player.nPoint.powerUp(param);
                    break;
            }
            PlayerService.gI().sendTNSM(player, type, param);
            if (isOri) {
                if (player.clan != null) {
                    player.clan.addSMTNClan(player, param);
                }
            }
        }
    }

    //    public void congTiemNang(Player pl, byte type, int tiemnang) {
//        Message msg;
//        try {
//            msg = new Message(-3);
//            msg.writer().writeByte(type);// 0 là cộng sm, 1 cộng tn, 2 là cộng cả 2
//            msg.writer().writeInt(tiemnang);// số tn cần cộng
//            if (!pl.isPet) {
//                pl.sendMessage(msg);
//            } else {
//                ((Pet) pl).master.nPoint.powerUp(tiemnang);
//                ((Pet) pl).master.nPoint.tiemNangUp(tiemnang);
//                ((Pet) pl).master.sendMessage(msg);
//            }
//            msg.cleanup();
//            switch (type) {
//                case 1:
//                    pl.nPoint.tiemNangUp(tiemnang);
//                    break;
//                case 2:
//                    pl.nPoint.powerUp(tiemnang);
//                    pl.nPoint.tiemNangUp(tiemnang);
//                    break;
//                default:
//                    pl.nPoint.powerUp(tiemnang);
//                    break;
//            }
//        } catch (Exception e) {
//
//        }
//    }
    public String get_HanhTinh(int hanhtinh) {
        switch (hanhtinh) {
            case 0:
                return "Trái Đất";
            case 1:
                return "Namếc";
            case 2:
                return "Xayda";
            default:
                return "";
        }
    }

    public String getCurrStrLevel(Player pl) {
        long sucmanh = pl.nPoint.power;
        if (sucmanh < 3000) {
            return "Tân thủ";
        } else if (sucmanh < 15000) {
            return "Tập sự sơ cấp";
        } else if (sucmanh < 40000) {
            return "Tập sự trung cấp";
        } else if (sucmanh < 90000) {
            return "Tập sự cao cấp";
        } else if (sucmanh < 170000) {
            return "Tân binh";
        } else if (sucmanh < 340000) {
            return "Chiến binh";
        } else if (sucmanh < 700000) {
            return "Chiến binh cao cấp";
        } else if (sucmanh < 1500000) {
            return "Vệ binh";
        } else if (sucmanh < 15000000) {
            return "Vệ binh hoàng gia";
        } else if (sucmanh < 150000000) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 1";
        } else if (sucmanh < 1500000000) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 2";
        } else if (sucmanh < 5000000000L) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 3";
        } else if (sucmanh < 10000000000L) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 4";
        } else if (sucmanh < 40000000000L) {
            return "Thần " + get_HanhTinh(pl.gender) + " cấp 1";
        } else if (sucmanh < 50010000000L) {
            return "Thần " + get_HanhTinh(pl.gender) + " cấp 2";
        } else if (sucmanh < 60010000000L) {
            return "Thần " + get_HanhTinh(pl.gender) + " cấp 3";
        } else if (sucmanh < 70010000000L) {
            return "Giới Vương Thần cấp 1";
        } else if (sucmanh < 80010000000L) {
            return "Giới Vương Thần cấp 2";
        } else if (sucmanh < 100010000000L) {
            return "Giới Vương Thần cấp 3";
        } else if (sucmanh < 110010000000L) {
            return "Thần hủy diệt cấp 1";
        } else if (sucmanh < 120010000000L) {
            return "Thần hủy diệt cấp 2";
        } else if (sucmanh < 130010000000L) {
            return "Thần hủy diệt cấp 3";
        } else if (sucmanh < 140010000000L) {
            return "Thần hủy diệt cấp 4";
        } else if (sucmanh < 150010000000L) {
            return "Thần hủy diệt cấp 5";
        } else if (sucmanh < 160010000000L) {
            return "Thần hủy diệt cấp 6";
        } else if (sucmanh < 170010000000L) {
            return "Thần Huỷ Diệt cấp 7";
        }
        return "Thần Huỷ Diệt cấp 2";
    }

    public void hsChar(Player pl, int hp, int mp) {
        Message msg;
        try {
            pl.setJustRevivaled();
            pl.nPoint.setHp(hp);
            pl.nPoint.setMp(mp);
            if (!pl.isPet) {
                msg = new Message(-16);
                pl.sendMessage(msg);
                msg.cleanup();
                PlayerService.gI().sendInfoHpMpMoney(pl);
            }

            msg = messageSubCommand((byte) 15);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeInt(hp);
            msg.writer().writeInt(mp);
            msg.writer().writeShort(pl.location.x);
            msg.writer().writeShort(pl.location.y);
            sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();

            Send_Info_NV(pl);
            PlayerService.gI().sendInfoHpMp(pl);
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void charDie(Player pl) {
        Message msg;
        try {
            if (!pl.isPet) {
                msg = new Message(-17);
                msg.writer().writeByte((int) pl.id);
                msg.writer().writeShort(pl.location.x);
                msg.writer().writeShort(pl.location.y);
                pl.sendMessage(msg);
                msg.cleanup();
            } else {
                ((Pet) pl).lastTimeDie = System.currentTimeMillis();
            }

            msg = new Message(-8);
            msg.writer().writeShort((int) pl.id);
            msg.writer().writeByte(0); //cpk
            msg.writer().writeShort(pl.location.x);
            msg.writer().writeShort(pl.location.y);
            sendMessAnotherNotMeInMap(pl, msg);
            msg.cleanup();
            Send_Info_NV(pl);
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void attackMob(Player pl, int mobId) {
        if (pl != null && pl.zone != null) {
            for (Mob mob : pl.zone.mobs) {
                if (mob.id == mobId) {
                    SkillService.gI().useSkill(pl, null, mob, null);
                    break;
                }
            }
        }
    }

    public void Send_Caitrang(Player player) {
        if (player != null) {
            Message msg;
            try {
                msg = new Message(-90);
                msg.writer().writeByte(1);// check type
                msg.writer().writeInt((int) player.id); //id player
                short head = player.getHead();
                short body = player.getBody();
                short leg = player.getLeg();

                msg.writer().writeShort(head);//set head
                msg.writer().writeShort(body);//setbody
                msg.writer().writeShort(leg);//set leg
                msg.writer().writeByte((player.effectSkill.isMonkey) ? 1 : 0);// thêm biến hình
                sendMessAllPlayerInMap(player, msg);
                msg.cleanup();
                if (player.clone != null) {
                    Send_Caitrang(player.clone);
                }
            } catch (Exception e) {
                Log.error(Service.class, e);
            }
        }
    }

    public void setNotMonkey(Player player) {
        Message msg;
        try {
            msg = new Message(-90);
            msg.writer().writeByte(-1);
            msg.writer().writeInt((int) player.id);
            Service.getInstance().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void setNotBienhinh(Player player) {
        Message msg;
        try {
            msg = new Message(-90);
            msg.writer().writeByte(-1);
            msg.writer().writeInt((int) player.id);
            Service.getInstance().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void sendFlagBag(Player playerReveice, Player playerInfo) {
        Message msg;
        try {
            msg = new Message(-64);
            msg.writer().writeInt((int) playerInfo.id);
            msg.writer().writeByte(playerInfo.getFlagBag());
            playerReveice.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendFlagBag(Player pl) {
        Message msg;
        try {
            int Flagbag = pl.getFlagBag();
//            if (pl.effectSkill.isBienHinh) {
//                Flagbag = ConstPlayer.FLAGBIENHINH[pl.gender][pl.effectSkill.levelBienHinh - 1];
//            }
            if (pl.clone != null) {
                sendFlagBag(pl.clone);
            }
            msg = new Message(-64);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(Flagbag);
            sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void Transport(Player pl) {
        Message msg = null;
        try {
            msg = new Message(-105);
            msg.writer().writeShort(pl.maxTime);
            msg.writer().writeByte(pl.type);
            pl.sendMessage(msg);
        } catch (Exception e) {
        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }

    public void sendThongBaoOK(Player pl, String text) {
        if (pl.isPet) {
            return;
        }
        Message msg;
        try {
            msg = new Message(-26);
            msg.writer().writeUTF(text);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void minigame_taixiu(Player player) {
        String time = ((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
        int ketqua = TaiXiu.gI().z + TaiXiu.gI().y + TaiXiu.gI().x;

        if (!TaiXiu.gI().baotri) {
            NpcService.gI().createMenuConMeo(player, ConstNpc.TAIXIU, 564,
                    "\n|7|TRÒ CHƠI MAY THÌ THẮNG\n\n"
                    + "|6|Kết quả kì trước: " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                    + (ketqua >= 10 ? "  --> 'Tài'" : "  --> 'Xỉu'")
                    + "\n|1|Lịch sử kì trước:\n|3| " + TaiXiu.gI().tongHistoryString
                    + "\n\n|2|Tổng cược TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Thỏi vàng"
                    + "\nTổng cược XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Thỏi vàng"
                    + "\n\nĐếm ngược: " + time,
                    "Cập nhật",
                    "Cược\n'Tài'",
                    "Cược\n'Xỉu'",
                    "Đóng"
            );
        }
    }

    public void sendThongBaoOK(Session session, String text) {
        Message msg;
        try {
            msg = new Message(-26);
            msg.writer().writeUTF(text);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendThongBaoAllPlayer(String thongBao) {
        Message msg;
        try {
            msg = new Message(-25);
            msg.writer().writeUTF(thongBao);
            this.sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendBigMessage(Player player, int iconId, String text) {
        try {
            Message msg;
            msg = new Message(-70);
            msg.writer().writeShort(iconId);
            msg.writer().writeUTF(text);
            msg.writer().writeByte(0);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void sendThongBaoFromAdmin(Player player, String text) {
        sendBigMessage(player, 1139, text);
    }

    public void sendBigMessAllPlayer(int iconId, String text) {
        try {
            Message msg;
            msg = new Message(-70);
            msg.writer().writeShort(iconId);
            msg.writer().writeUTF(text);
            msg.writer().writeByte(0);
            this.sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void sendThongBao(Player pl, String thongBao) {
        Message msg;
        try {
            msg = new Message(-25);
            msg.writer().writeUTF(thongBao);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void showListPlayer(Player player) {
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(1);
            msg.writer().writeUTF("(" + TimeUtil.getTimeNow("dd/MM/yyyy HH:mm:ss") + ")");
            msg.writer().writeByte(Client.gI().getPlayers().size());
            for (int i = 0; i < Client.gI().getPlayers().size(); i++) {
                Player pl = Client.gI().getPlayers().get(i);
                if (pl == null) {
                    pl = player;
                }
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.getSession().version > 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF(pl.isAdmin() ? "" : "Member");
                msg.writer().writeUTF("SM: " + Util.powerToString(pl.nPoint.power)
                        + "\nTN: " + Util.powerToString(pl.nPoint.tiemNang)
                        + "\nHP: " + Util.powerToString(pl.nPoint.hpMax)
                        + "\nKI: " + Util.powerToString(pl.nPoint.mpMax)
                        + "\nSD: " + Util.powerToString(pl.nPoint.dame)
                        + "\nDEF: " + Util.powerToString(pl.nPoint.def)
                        + "\nCM: " + pl.nPoint.crit + "%"
                        + "\n|7|[Map: " + pl.zone.map.mapName + "(" + pl.zone.map.mapId + ") " + "Khu: " + pl.zone.zoneId + "]");
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void sendMoney(Player pl) {
        Message msg;
        try {
            msg = new Message(6);
            long gold = pl.inventory.getGoldDisplay();
            if (pl.isVersionAbove(214)) {
                msg.writer().writeLong(gold);
            } else {
                msg.writer().writeInt((int) gold);
            }
            msg.writer().writeInt(pl.inventory.gem);
            msg.writer().writeInt(pl.inventory.ruby);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void showYourNumber(Player player, String Number, String result, String finish, int type) {
        Message msg = null;
        try {
            msg = new Message(-126);
            msg.writer().writeByte(type); // 1 = RESET GAME | 0 = SHOW CON SỐ CỦA PLAYER
            if (type == 0) {
                msg.writer().writeUTF(Number);
            } else if (type == 1) {
                msg.writer().writeByte(type);
                msg.writer().writeUTF(result); // 
                msg.writer().writeUTF(finish);
            }
            player.sendMessage(msg);
        } catch (Exception e) {
        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }

    public void sendToAntherMePickItem(Player player, int itemMapId) {
        Message msg;
        try {
            msg = new Message(-19);
            msg.writer().writeShort(itemMapId);
            msg.writer().writeInt((int) player.id);
            sendMessAnotherNotMeInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public boolean isItemMoney(int type) {
        return type == 9 || type == 10 || type == 34;
    }

    public void useSkillNotFocus(Player pl, Message m) throws IOException {
        byte status = m.reader().readByte();
        if (status == 20) {
            byte SkillID = m.reader().readByte();
            short xPlayer = m.reader().readShort();
            short yPlayer = m.reader().readShort();
            byte dir = m.reader().readByte();
            short x = m.reader().readShort();
            short y = m.reader().readShort();
            SkillService.gI().useSKillNotFocus(pl, SkillID, xPlayer, yPlayer, dir, x, y);
        } else {
            SkillService.gI().useSkill(pl, null, null, null);
        }
    }

    public void chatGlobal(Player pl, String text) {
        if (pl.inventory.getGem() >= 5) {
            if (pl.isAdmin() || Util.canDoWithTime(pl.lastTimeChatGlobal, 180000)) {
                if (pl.isAdmin() || pl.nPoint.power > 2000000000) {
                    pl.inventory.subGem(5);
                    sendMoney(pl);
                    pl.lastTimeChatGlobal = System.currentTimeMillis();
                    Message msg;
                    try {
                        msg = new Message(92);
                        msg.writer().writeUTF(pl.name);
                        msg.writer().writeUTF("|5|" + text);
                        msg.writer().writeInt((int) pl.id);
                        msg.writer().writeShort(pl.getHead());
                        msg.writer().writeShort(pl.getBody());
                        msg.writer().writeShort(pl.getFlagBag()); //bag
                        msg.writer().writeShort(pl.getLeg());
                        msg.writer().writeByte(0);
                        sendMessAllPlayer(msg);
                        msg.cleanup();
                    } catch (Exception e) {
                    }
                } else {
                    sendThongBao(pl, "Sức mạnh phải ít nhất 2tỷ mới có thể chat thế giới");
                }
            } else {
                sendThongBao(pl, "Không thể chat thế giới lúc này, vui lòng đợi " + TimeUtil.getTimeLeft(pl.lastTimeChatGlobal, 120));
            }
        } else {
            sendThongBao(pl, "Không đủ ngọc chat thế giới");
        }
    }

    private int tiLeXanhDo = 3;

    public int xanhToDo(int n) {
        return n * tiLeXanhDo;
    }

    public int doToXanh(int n) {
        return (int) n / tiLeXanhDo;
    }

    public static final int[] flagTempId = {363, 364, 365, 366, 367, 368, 369, 370, 371, 519, 520, 747};
    public static final int[] flagIconId = {2761, 2330, 2323, 2327, 2326, 2324, 2329, 2328, 2331, 4386, 4385, 2325};

    public void openFlagUI(Player pl) {
        Message msg;
        try {
            msg = new Message(-103);
            msg.writer().writeByte(0);
            msg.writer().writeByte(flagTempId.length);
            for (int i = 0; i < flagTempId.length; i++) {
                msg.writer().writeShort(flagTempId[i]);
                msg.writer().writeByte(1);
                switch (flagTempId[i]) {
                    case 363:
                        msg.writer().writeByte(73);
                        msg.writer().writeShort(0);
                        break;
                    case 371:
                        msg.writer().writeByte(88);
                        msg.writer().writeShort(10);
                        break;
                    default:
                        msg.writer().writeByte(88);
                        msg.writer().writeShort(5);
                        break;
                }
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void changeFlag(Player pl, int index) {
        Message msg;
        try {
            pl.cFlag = (byte) index;
            msg = new Message(-103);
            msg.writer().writeByte(1);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(index);
            Service.getInstance().sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();

            msg = new Message(-103);
            msg.writer().writeByte(2);
            msg.writer().writeByte(index);
            msg.writer().writeShort(flagIconId[index]);
            Service.getInstance().sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();

            if (pl.pet != null) {
                pl.pet.cFlag = (byte) index;
                msg = new Message(-103);
                msg.writer().writeByte(1);
                msg.writer().writeInt((int) pl.pet.id);
//                System.out.println("pl.pet.id: " + pl.pet.id);
                msg.writer().writeByte(index);
                Service.getInstance().sendMessAllPlayerInMap(pl.pet, msg);
                msg.cleanup();

                msg = new Message(-103);
                msg.writer().writeByte(2);
                msg.writer().writeByte(index);
                msg.writer().writeShort(flagIconId[index]);
                Service.getInstance().sendMessAllPlayerInMap(pl.pet, msg);
                msg.cleanup();
            }
            pl.lastTimeChangeFlag = System.currentTimeMillis();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void sendFlagPlayerToMe(Player me, Player pl) {
        Message msg;
        try {
            msg = new Message(-103);
            msg.writer().writeByte(2);
            msg.writer().writeByte(pl.cFlag);
            msg.writer().writeShort(flagIconId[pl.cFlag]);
            me.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void chooseFlag(Player pl, int index) {
        if (Util.canDoWithTime(pl.lastTimeChangeFlag, 60000)) {
            if (!MapService.gI().isMapBlackBallWar(pl.zone.map.mapId) && !MapService.gI().isMapMabuWar(pl.zone.map.mapId) && !pl.isHoldBlackBall && !MapService.gI().isMapBangHoi(pl.zone.map.mapId)) {
                changeFlag(pl, index);
            } else {
                sendThongBao(pl, "Không thể đổi cờ ở khu vực này");
            }
        } else {
            sendThongBao(pl, "Không thể đổi cờ lúc này! Vui lòng đợi " + TimeUtil.getTimeLeft(pl.lastTimeChangeFlag, 60) + " nữa!");
        }
    }

    public void attackPlayer(Player pl, int idPlAnPem) {
        if (pl != null) {
            SkillService.gI().useSkill(pl, pl.zone.getPlayerInMap(idPlAnPem), null, null);
        }
    }

    public void openZoneUI(Player pl) {
        if (!pl.isAdmin() && (pl.zone == null || pl.zone.map.isMapOffline || MapService.gI().isMapOfflineNe(pl.zone.map.mapId))) {
            sendThongBaoOK(pl, "Không thể đổi khu vực trong map này");
            return;
        }
        int mapid = pl.zone.map.mapId;
        if (!pl.isAdmin() && (MapService.gI().isMapDoanhTrai(mapid) || MapService.gI().isMapBanDoKhoBau(mapid) || MapService.gI().isMapKhiGas(mapid) || mapid == 120 || MapService.gI().isMapVS(mapid) || mapid == 126 || pl.zone instanceof ZDungeon)) {
            sendThongBaoOK(pl, "Không thể đổi khu vực trong map này");
            return;
        }
        Message msg;
        try {
            msg = new Message(29);
            msg.writer().writeByte(pl.zone.map.zones.size());
            for (Zone zone : pl.zone.map.zones) {
                msg.writer().writeByte(zone.zoneId);
                int numPlayers = zone.getNumOfPlayers();
                msg.writer().writeByte((numPlayers < 5 ? 0 : (numPlayers < 8 ? 1 : 2)));
                msg.writer().writeByte(numPlayers);
                msg.writer().writeByte(zone.maxPlayer);
                msg.writer().writeByte(0);
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void UpdateCoolDown(Player pl) {
        Message msg;
        try {
            msg = new Message(-94);
            for (Skill skill : pl.playerSkill.skills) {
                skill.coolDown = 0;
                msg.writer().writeShort(skill.skillId);
                int leftTime = (int) (skill.lastTimeUseThisSkill + skill.coolDown - System.currentTimeMillis());
                if (leftTime < 0) {
                    leftTime = 0;
                }
                msg.writer().writeInt(leftTime);
            }
            pl.sendMessage(msg);
            pl.nPoint.setMp(pl.nPoint.mpMax);
            PlayerService.gI().sendInfoHpMpMoney(pl);
            msg.cleanup();

        } catch (Exception e) {
        }
    }

    public void sendTimeSkill(Player pl) {
        Message msg;
        try {
            msg = new Message(-94);
            for (Skill skill : pl.playerSkill.skills) {
                msg.writer().writeShort(skill.skillId);
                int timeLeft = (int) (skill.lastTimeUseThisSkill + skill.coolDown - System.currentTimeMillis());
                if (timeLeft < 0) {
                    timeLeft = 0;
                }
                msg.writer().writeInt(timeLeft);
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void dropItemMap(Zone zone, ItemMap item) {
        Message msg;
        try {
            msg = new Message(68);
            msg.writer().writeShort(item.itemMapId);
            msg.writer().writeShort(item.itemTemplate.id);
            msg.writer().writeShort(item.x);
            msg.writer().writeShort(item.y);
            msg.writer().writeInt((int) item.playerId);//
            if (item.playerId == -2) {
                msg.writer().writeShort(item.range);
            }
            sendMessAllPlayerInMap(zone, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void dropItemMapForMe(Player player, ItemMap item) {
        Message msg;
        try {
            msg = new Message(68);
            msg.writer().writeShort(item.itemMapId);
            msg.writer().writeShort(item.itemTemplate.id);
            msg.writer().writeShort(item.x);
            msg.writer().writeShort(item.y);
            msg.writer().writeInt((int) item.playerId);//
            if (item.playerId == -2) {
                msg.writer().writeShort(item.range);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void showInfoPet(Player pl) {
        if (pl != null && pl.pet != null) {
            Message msg;
            try {
                msg = new Message(-107);
                msg.writer().writeByte(2);
                msg.writer().writeShort(pl.pet.getAvatar());
                msg.writer().writeByte(pl.pet.inventory.itemsBody.size());

                for (Item item : pl.pet.inventory.itemsBody) {
                    if (!item.isNotNullItem()) {
                        msg.writer().writeShort(-1);
                    } else {
                        msg.writer().writeShort(item.template.id);
                        msg.writer().writeInt(item.quantity);
                        msg.writer().writeUTF(item.getInfo());
                        msg.writer().writeUTF(item.getContent());

                        List<ItemOption> itemOptions = item.getDisplayOptions();
                        int countOption = itemOptions.size();
                        msg.writer().writeByte(countOption);
                        for (ItemOption iop : itemOptions) {
                            msg.writer().writeByte(iop.optionTemplate.id);
                            msg.writer().writeShort(iop.param);
                        }
                    }
                }

                msg.writer().writeInt(pl.pet.nPoint.hp); //hp
                msg.writer().writeInt(pl.pet.nPoint.hpMax); //hpfull
                msg.writer().writeInt(pl.pet.nPoint.mp); //mp
                msg.writer().writeInt(pl.pet.nPoint.mpMax); //mpfull
                msg.writer().writeInt(pl.pet.nPoint.dame); //damefull
                msg.writer().writeUTF(pl.pet.name); //name
                msg.writer().writeUTF(getCurrStrLevel(pl.pet)); //curr level
                msg.writer().writeLong(pl.pet.nPoint.power); //power
                msg.writer().writeLong(pl.pet.nPoint.tiemNang); //tiềm năng
                msg.writer().writeByte(pl.pet.getStatus()); //status
                msg.writer().writeShort(pl.pet.nPoint.stamina); //stamina
                msg.writer().writeShort(pl.pet.nPoint.maxStamina); //stamina full
                msg.writer().writeByte(pl.pet.nPoint.crit); //crit
                msg.writer().writeShort(pl.pet.nPoint.def); //def
                int sizeSkill = pl.pet.playerSkill.skills.size();
                msg.writer().writeByte(5); //counnt pet skill
                for (int i = 0; i < pl.pet.playerSkill.skills.size(); i++) {
                    if (pl.pet.playerSkill.skills.get(i).skillId != -1) {
                        msg.writer().writeShort(pl.pet.playerSkill.skills.get(i).skillId);
                    } else {
                        switch (i) {
                            case 1:
                                msg.writer().writeShort(-1);
                                msg.writer().writeUTF("Cần 150 Tr sức mạnh để mở");
                                break;
                            case 2:
                                msg.writer().writeShort(-1);
                                msg.writer().writeUTF("Cần 1,5 Tỉ sức mạnh để mở");
                                break;
                            case 3:
                                msg.writer().writeShort(-1);
                                msg.writer().writeUTF("Cần 20 Tỉ sức mạnh để mở");
                                break;
                            case 4:
                                msg.writer().writeShort(-1);
                                msg.writer().writeUTF("Cần 80 Tỉ sức mạnh để mở");
                                break;
                            default:
                                msg.writer().writeShort(-1);
                                msg.writer().writeUTF("Cần đạt sức mạnh 120tỷ\nđể mở");
                                break;
                        }
                    }
                }
                pl.sendMessage(msg);
                msg.cleanup();

            } catch (Exception e) {
                Log.error(Service.class, e);
            }
        }
    }

    //    public void sendItemTime(Player pl, int itemId, int time) {
//        Message msg;
//        try {
//            msg = new Message(-106);
//            msg.writer().writeShort(itemId);
//            msg.writer().writeShort(time);
//            pl.sendMessage(msg);
//        } catch (Exception e) {
//        }
//    }
//    public void removeItemTime(Player pl, int itemTime) {
//        sendItemTime(pl, itemTime, 0);
//    }
    public void sendSpeedPlayer(Player pl, int speed) {
        Message msg;
        try {
            msg = Service.getInstance().messageSubCommand((byte) 8);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(speed != -1 ? speed : pl.nPoint.speed);
            pl.sendMessage(msg);
//            Service.getInstance().sendMessAllPlayerInMap(pl.map, msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void setPos(Player player, int x, int y) {
        player.location.x = x;
        player.location.y = y;
        Message msg;
        try {
            msg = new Message(123);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(x);
            msg.writer().writeShort(y);
            msg.writer().writeByte(1);
            sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void getPlayerMenu(Player player, int playerId) {
        Message msg;
        try {
            msg = new Message(-79);
            Player pl = player.zone.getPlayerInMap(playerId);
            if (pl != null) {
                msg.writer().writeInt(playerId);
                msg.writer().writeLong(pl.nPoint.power);
                msg.writer().writeUTF(Service.getInstance().getCurrStrLevel(pl));
                player.sendMessage(msg);
            }
            msg.cleanup();
            if (player.isAdmin()) {
                SubMenuService.gI().showMenuForAdmin(player);
            }
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void subMenuPlayer(Player player) {
        Message msg;
        try {
            msg = messageSubCommand((byte) 63);
            msg.writer().writeByte(1);
            msg.writer().writeUTF("String 1");
            msg.writer().writeUTF("String 2");
            msg.writer().writeShort(550);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void hideWaitDialog(Player pl) {
        Message msg;
        try {
            msg = new Message(-99);
            msg.writer().writeByte(-1);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void chatPrivate(Player plChat, Player plReceive, String text) {
        Message msg;
        try {
            msg = new Message(Cmd.CHAT_THEGIOI_SERVER);
            msg.writer().writeUTF(plChat.name);
            msg.writer().writeUTF("|5|" + text);
            msg.writer().writeInt((int) plChat.id);
            msg.writer().writeShort(plChat.getHead());
            Part part = PartManager.getInstance().find(plChat.getHead());
            msg.writer().writeShort(part.getIcon(0));
            msg.writer().writeShort(plChat.getBody());
            msg.writer().writeShort(plChat.getFlagBag()); //bag
            msg.writer().writeShort(plChat.getLeg());
            msg.writer().writeByte(1);
            plChat.sendMessage(msg);
            plReceive.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void changePassword(Player player, String oldPass, String newPass, String rePass) {
        if (player.getSession().pp.equals(oldPass)) {
            if (newPass.length() >= 6) {
                if (newPass.equals(rePass)) {
                    player.getSession().pp = newPass;
                    AccountDAO.updateAccount(player.getSession());
                    Service.getInstance().sendThongBao(player, "Đổi mật khẩu thành công!");
                } else {
                    Service.getInstance().sendThongBao(player, "Mật khẩu nhập lại không đúng!");
                }
            } else {
                Service.getInstance().sendThongBao(player, "Mật khẩu ít nhất 6 ký tự!");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Mật khẩu cũ không đúng!");
        }
    }

    public void switchToCreateChar(Session session) {
        Message msg;
        try {
            msg = new Message(2);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendCaption(Session session, byte gender) {
        Message msg;
        try {
            List<Caption> captions = CaptionManager.getInstance().getCaptions();
            msg = new Message(-41);
            msg.writer().writeByte(captions.size());
            for (Caption caption : captions) {
                msg.writer().writeUTF(caption.getCaption(gender));
            }
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendHavePet(Player player) {
        Message msg;
        try {
            msg = new Message(-107);
            msg.writer().writeByte(player.pet == null ? 0 : 1);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendWaitToLogin(Session session, int secondsWait) {
        Message msg;
        try {
            msg = new Message(122);
            msg.writer().writeShort(secondsWait);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void sendMessage(Session session, int cmd, String path) {
        Message msg;
        try {
            msg = new Message(cmd);
            msg.writer().write(FileIO.readFile(path));
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendTopRank(Player pl) {
        Message msg;
        try {
            msg = new Message(Cmd.THELUC);
            msg.writer().writeInt(1);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void createItemMap(Player player, int tempId) {
        ItemMap itemMap = new ItemMap(player.zone, tempId, 1, player.location.x, player.location.y, player.id);
        dropItemMap(player.zone, itemMap);
    }

    public void sendNangDong(Player player) {
        Message msg;
        try {
            msg = new Message(-97);
            msg.writer().writeInt(100);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendPowerInfo(Player pl, String info, short point) {
        Message m = null;
        try {
            m = new Message(-115);
            m.writer().writeUTF(info);
            m.writer().writeShort(point);
            m.writer().writeShort(20);
            m.writer().writeShort(10);
            m.writer().flush();
            if (pl != null && pl.getSession() != null) {
                pl.sendMessage(m);
            }
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public void setMabuHold(Player pl, byte type) {
        Message m = null;
        try {
            m = new Message(52);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public void sendPercentMabuEgg(Player player, byte percent) {
        try {
            Message msg = new Message(-117);
            msg.writer().writeByte(percent);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendPlayerInfo(Player player) {
        try {
            Message msg = messageSubCommand((byte) 7);
            msg.writer().writeInt((int) player.id);
            if (player.clan != null) {
                msg.writer().writeInt(player.clan.id);
            } else {
                msg.writer().writeInt(-1);
            }
            int level = CaptionManager.getInstance().getLevel(player);
            level = player.isInvisible ? 0 : level;
            msg.writer().writeByte(level);
            msg.writer().writeBoolean(player.isInvisible);
            msg.writer().writeByte(player.typePk);
            msg.writer().writeByte(player.gender);
            msg.writer().writeByte(player.gender);
            msg.writer().writeShort(player.getHead());
            msg.writer().writeUTF(player.name);
            msg.writer().writeInt(player.nPoint.hp);
            msg.writer().writeInt(player.nPoint.hpMax);
            msg.writer().writeShort(player.getBody());
            msg.writer().writeShort(player.getLeg());
            msg.writer().writeByte(player.getFlagBag());
            msg.writer().writeByte(-1);
            msg.writer().writeShort(player.location.x);
            msg.writer().writeShort(player.location.y);
            msg.writer().writeShort(0);
            msg.writer().writeShort(0);
            msg.writer().writeByte(0);
            sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendThongBaoXamLon(Player player) {
        Message msg;
        try {
            msg = new Message(-57);
            msg.writer().writeUTF("Đây chỉ là một chức năng rất là xàm lồn :v");
            msg.writer().writeInt(-1);
            msg.writer().writeInt(-1); //code
            player.isChangeMap = true;
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void getCurrLevel(Player pl) {

    }

    public int getWidthHeightImgPetFollow(int id) {
        if (id == 15067) {
            return 65;
        }
        return 75;
    }

    public void showTopPower(Player player) {
        TopPowerManager.getInstance().load();
        List<Player> list = TopPowerManager.getInstance().getList();
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("TOP 100 Sức Mạnh");
            msg.writer().writeByte(list.size());
            for (int i = 0; i < list.size(); i++) {
                Player pl = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.isVersionAbove(220)) {
                    Part part = PartManager.getInstance().find(pl.getHead());
                    msg.writer().writeShort(part.getIcon(0));
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                String inputDateString = pl.lastimelogin.toString();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                try {
                    Date inputDate = dateFormat.parse(inputDateString);
                    Date currentDate = new Date();
                    long timeDifferenceInMillis = currentDate.getTime() - inputDate.getTime();
                    long giây = timeDifferenceInMillis / 1000;
                    msg.writer().writeUTF(Util.numberToMoney(pl.nPoint.power) + " (" + Util.convertSecondsToTime(giây) + ")");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                msg.writer().writeUTF("Sức mạnh: " + Util.numberToMoney(pl.nPoint.power));
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTopSD(Player player) {
        TopoSd.getInstance().load();
        List<Player> list = TopoSd.getInstance().getList();
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("TOP 100 Sức Đánh");
            msg.writer().writeByte(list.size());
            for (int i = 0; i < list.size(); i++) {
                Player pl = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                Part part = PartManager.getInstance().find(pl.getHead());
                msg.writer().writeShort(part.getIcon(0));
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF("Sức đánh: " + Util.numberToMoney(pl.nPoint.dame));
//                msg.writer().writeUTF("NRO9.PRO");
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTopHP(Player player) {
        TopoHp.getInstance().load();
        List<Player> list = TopoHp.getInstance().getList();
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("TOP 100 HP");
            msg.writer().writeByte(list.size());
            for (int i = 0; i < list.size(); i++) {
                Player pl = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                Part part = PartManager.getInstance().find(pl.getHead());
                msg.writer().writeShort(part.getIcon(0));
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF("HP: " + Util.numberToMoney(pl.nPoint.hpMax));
//                msg.writer().writeUTF("NRO9.PRO");
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTopKI(Player player) {
        TopoKi.getInstance().load();
        List<Player> list = TopoKi.getInstance().getList();
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("TOP 100 HP");
            msg.writer().writeByte(list.size());
            for (int i = 0; i < list.size(); i++) {
                Player pl = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                Part part = PartManager.getInstance().find(pl.getHead());
                msg.writer().writeShort(part.getIcon(0));
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF("Ki: " + Util.numberToMoney(pl.nPoint.mpMax));
//                msg.writer().writeUTF("NRO9.PRO");
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTopTask(Player player) {
        List<Player> list = TopToTask.getInstance().load();
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top Nhiệm Vụ");
            msg.writer().writeByte(list.size());
            for (int i = 0; i < list.size(); i++) {
                Player pl = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.isVersionAbove(220)) {
                    Part part = PartManager.getInstance().find(pl.getHead());
                    msg.writer().writeShort(part.getIcon(0));
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);

                msg.writer().writeUTF(Client.gI().getPlayer(pl.id) != null ? "Online" : "Offline");
//                msg.writer().writeUTF("Sức mạnh: " + Util.numberToMoney(pl.nPoint.power));
                msg.writer().writeUTF("Nhiệm Vụ");
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTopClanBDKB(Player player) {
        TopBanDoKhoBau.getInstance().load();
        List<Clan> list = TopBanDoKhoBau.getInstance().getList();
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top 100");
            msg.writer().writeByte(list.size());
            for (int i = 0; i < list.size(); i++) {
                Clan clan = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) clan.id);
                msg.writer().writeShort(clan.getLeader().head);
                Part part = PartManager.getInstance().find(clan.getLeader().head);
                msg.writer().writeShort(part.getIcon(0));
                msg.writer().writeShort(clan.getLeader().body);
                msg.writer().writeShort(clan.getLeader().leg);
                msg.writer().writeUTF(clan.name);

                msg.writer().writeUTF("Lv: " + clan.levelDoneBanDoKhoBau + " Trong " + Util.convertMillisecondsToSeconds(clan.thoiGianHoanThanhBDKB) + " giây");

                msg.writer().writeUTF("Bang chủ " + clan.getLeader().name);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showMyTopClanBDKB(Player player) {
        MyClanTopBanDoKhoBau.getInstance().load2(player.clan.getLeader().id);
        List<Player> list = MyClanTopBanDoKhoBau.getInstance().getList();
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Thành tích bang");
            msg.writer().writeByte(list.size());
            for (int i = 0; i < list.size(); i++) {
                Player pl = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                Part part = PartManager.getInstance().find(pl.getHead());
                msg.writer().writeShort(part.getIcon(0));
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.nameClan);

                msg.writer().writeUTF("Lv: " + pl.levelBDKBDone + " (" + Util.convertSecondsToTime(pl.lastTimeUpdateTopBDKB) + ")");

                msg.writer().writeUTF("Bang chủ: " + pl.name + "\n[" + Util.convertMilliseconds(pl.timeBDKBDone) + "]");
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTopClanKhiGas(Player player) {
        TopKhiGas.getInstance().load();
        List<Clan> list = TopKhiGas.getInstance().getList();
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top 100");
            msg.writer().writeByte(list.size());
            for (int i = 0; i < list.size(); i++) {
                Clan clan = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) clan.id);
                msg.writer().writeShort(clan.getLeader().head);
                Part part = PartManager.getInstance().find(clan.getLeader().head);
                msg.writer().writeShort(part.getIcon(0));
                msg.writer().writeShort(clan.getLeader().body);
                msg.writer().writeShort(clan.getLeader().leg);
                msg.writer().writeUTF(clan.name);

                msg.writer().writeUTF("Lv: " + clan.levelDoneKhiGas + " Trong " + Util.convertMillisecondsToSeconds(clan.thoiGianHoanThanhKhiGas) + " giây");

                msg.writer().writeUTF("Bang chủ " + clan.getLeader().name);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showMyTopClanKhiGas(Player player) {
        if (player.clan != null) {
            MyClanTopKhiGas.getInstance().load2(player.clan.getLeader().id);
            List<Player> list = MyClanTopKhiGas.getInstance().getList();
            Message msg = new Message(Cmd.TOP);
            try {
                msg.writer().writeByte(0);
                msg.writer().writeUTF("Thành tích bang");
                msg.writer().writeByte(list.size());
                for (int i = 0; i < list.size(); i++) {
                    Player pl = list.get(i);
                    msg.writer().writeInt(i + 1);
                    msg.writer().writeInt((int) pl.id);
                    msg.writer().writeShort(pl.getHead());
                    Part part = PartManager.getInstance().find(pl.getHead());
                    msg.writer().writeShort(part.getIcon(0));
                    msg.writer().writeShort(pl.getBody());
                    msg.writer().writeShort(pl.getLeg());
                    msg.writer().writeUTF(pl.nameClan);

                    msg.writer().writeUTF("Lv: " + pl.levelKhiGasDone + " (" + Util.convertSecondsToTime(pl.lastTimeUpdateTopKhiGas) + ")");

                    msg.writer().writeUTF("Bang chủ: " + pl.name + "\n[" + Util.convertMilliseconds(pl.timeKhiGasDone) + "]");
                }
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Message msg = new Message(Cmd.TOP);
            try {
                msg.writer().writeByte(0);
                msg.writer().writeUTF("Thành tích bang");
                msg.writer().writeByte(0);
                msg.writer().writeInt(0);
                msg.writer().writeInt(0);
                msg.writer().writeShort(-1);
                Part part = PartManager.getInstance().find(-1);
                msg.writer().writeShort(part.getIcon(0));
                msg.writer().writeShort(-1);
                msg.writer().writeShort(-1);
                msg.writer().writeUTF("Chưa có");

                msg.writer().writeUTF("Chưa có");

                msg.writer().writeUTF("Chưa có");

                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void showToplevelWhis(Player player) {
        TopKillWhisManager.getInstance().load();
        List<Player> list = TopKillWhisManager.getInstance().getList();

        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("TOP 100");
            msg.writer().writeByte(list.size());
            for (int i = 0; i < list.size(); i++) {
                Player pl = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                Part part = PartManager.getInstance().find(pl.getHead());
                msg.writer().writeShort(part.getIcon(0));
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                int playerRank = pl.nPoint.getPlayerRank(list, pl);
                int thuong = 0;
                if (playerRank == 1) {
                    thuong = 20;
                } else if (playerRank >= 2 && playerRank < 6) {
                    thuong = 10;
                } else if (playerRank >= 6 && playerRank <= 15) {
                    thuong = 5;
                } else {
                    thuong = 0;
                }
                if (playerRank <= 15) {
                    msg.writer().writeUTF("+" + thuong + "% HP,KI,SD");
                } else {
                    msg.writer().writeUTF("");
                }
                msg.writer().writeUTF("LV: " + pl.levelKillWhisDone + " với " + Util.convertMillisecondsToSeconds(pl.timeKillWhis) + " giây");

                String inputDateString = pl.lastimelogin.toString();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                try {
                    Date inputDate = dateFormat.parse(inputDateString);
                    Date currentDate = new Date();
                    long timeDifferenceInMillis = currentDate.getTime() - inputDate.getTime();
                    long giây = timeDifferenceInMillis / 1000;
//                    msg.writer().writeUTF(Util.convertSecondsToTime2(giây));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTopRichMan(Player player) {
        TopRichManManager.getInstance().load();
        List<Player> list1 = TopRichManManager.getInstance().getList();
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("TOP 100 Đại Gia");
            msg.writer().writeByte(list1.size());
            for (int i = 0; i < list1.size(); i++) {
                Player pl = list1.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.isVersionAbove(220)) {
                    Part part = PartManager.getInstance().find(pl.getHead());
                    msg.writer().writeShort(part.getIcon(0));
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);

                String inputDateString = pl.lastimelogin.toString();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                try {
                    Date inputDate = dateFormat.parse(inputDateString);
                    Date currentDate = new Date();
                    long timeDifferenceInMillis = currentDate.getTime() - inputDate.getTime();
                    long giây = timeDifferenceInMillis / 1000;
                    msg.writer().writeUTF(Util.numberToMoney(pl.tongnap) + " (" + Util.convertSecondsToTime(giây) + ")");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                msg.writer().writeUTF("Tổng nạp: " + Util.numberToMoney(pl.tongnap));
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Topmaydam(Player player) {
        Topmaydam.getInstance().load();
        List<Player> list2 = Topmaydam.getInstance().getList();
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("TOP 100 Đại Gia");
            msg.writer().writeByte(list2.size());
            for (int i = 0; i < list2.size(); i++) {
                Player pl = list2.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.isVersionAbove(220)) {
                    Part part = PartManager.getInstance().find(pl.getHead());
                    msg.writer().writeShort(part.getIcon(0));
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                String inputDateString = pl.lastimelogin.toString();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date inputDate = dateFormat.parse(inputDateString);
                    Date currentDate = new Date();
                    long timeDifferenceInMillis = currentDate.getTime() - inputDate.getTime();
                    long giây = timeDifferenceInMillis / 1000;
                    msg.writer().writeUTF(Util.numberToMoney(pl.diemmaydam) + " (" + Util.convertSecondsToTime(giây) + ")");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                msg.writer().writeUTF(Util.numberToMoney(pl.diemmaydam) + "Điểm");
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
