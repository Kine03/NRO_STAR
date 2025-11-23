package nro.sendEff;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import nro.models.player.Player;
import nro.server.io.Message;
import nro.services.Service;

/**
 *
 * @author Louis Goku
 */
public class SendEffect {

    private static SendEffect instance;

    public static SendEffect getInstance() {
        if (instance == null) {
            instance = new SendEffect();
        }
        return instance;
    }

    public void sendChanThienTu(Player player, int id) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);

            int shortValue = -1; // Default value in case none of the conditions match

            switch (id) {
                case 1216:
                    shortValue = 73;
                    break;
                case 1217:
                    shortValue = 61;
                    break;
                case 1218:
                    shortValue = 62;
                    break;
                case 1219:
                    shortValue = 63;
                    break;
                case 1220:
                    shortValue = 64;
                    break;
                case 1221:
                    shortValue = 67;
                    break;
                case 1222:
                    shortValue = 70;
                    break;
                case 1223:
                    shortValue = 71;
                    break;
                case 1224:
                    shortValue = 72;
                    break;
                default:
                    break;
            }

            me.writer().writeShort(shortValue);
            me.writer().writeByte(0);
            me.writer().writeByte(-1);
            me.writer().writeShort(1);
            me.writer().writeByte(0);
            Service.getInstance().sendMessAllPlayerInMap(player, me);
            me.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendChanThienTuAll(Player player, Player p2, int id) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);
            if (id == 1216) {
                me.writer().writeShort(73);
            }
            if (id == 1217) {
                me.writer().writeShort(61);
            }
            if (id == 1218) {
                me.writer().writeShort(62);
            }
            if (id == 1219) {
                me.writer().writeShort(63);
            }
            if (id == 1220) {
                me.writer().writeShort(64);
            }
            if (id == 1221) {
                me.writer().writeShort(67);
            }
            if (id == 1222) {
                me.writer().writeShort(70);
            }
            if (id == 1223) {
                me.writer().writeShort(71);
            }
            if (id == 1224) {
                me.writer().writeShort(72);
            }
            me.writer().writeByte(0);
            me.writer().writeByte(-1);
            me.writer().writeShort(1);
            me.writer().writeByte(0);
            p2.sendMessage(me);
            me.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendDanhHieu(Player player, int id) {
        Message me = null;
        try {

            int ycongdanhhieu = 0;

            switch (id) {
                case 1:
                    if (player.DH1 = true && player.IdDanhHieu_1 > 200) {
                        me = createMessage(player, player.IdDanhHieu_1, ycongdanhhieu);
                    }
                    break;
                case 2:
//                    if (player.DH1 = true) {
//                        ycongdanhhieu -= 20;
//                    }
                    if (player.DH1 = true && player.IdDanhHieu_1 > 200) {
                        me = createMessage(player, player.IdDanhHieu_2, ycongdanhhieu);
                    }
                    break;
                case 3:
                    if (player.DH1 = true) {
                        ycongdanhhieu -= 20;
                    }
                    if (player.DH2 = true) {
                        ycongdanhhieu -= 20;
                    }
                    if (player.DH3 = true && player.IdDanhHieu_3 > 200) {
                        me = createMessage(player, player.IdDanhHieu_3, ycongdanhhieu);
                    }
                    break;
                case 4:
                    if (player.DH1 = true) {
                        ycongdanhhieu -= 20;
                    }
                    if (player.DH2 = true) {
                        ycongdanhhieu -= 20;
                    }
                    if (player.DH3 = true) {
                        ycongdanhhieu -= 20;
                    }
                    if (player.DH4 = true && player.IdDanhHieu_4 > 200) {
                        me = createMessage(player, player.IdDanhHieu_4, ycongdanhhieu);
                    }
                    break;
                case 5:
                    if (player.DH1 = true) {
                        ycongdanhhieu -= 20;
                    }
                    if (player.DH2 = true) {
                        ycongdanhhieu -= 20;
                    }
                    if (player.DH3 = true) {
                        ycongdanhhieu -= 20;
                    }
                    if (player.DH4 = true) {
                        ycongdanhhieu -= 20;
                    }
                    if (player.DH5 = true && player.IdDanhHieu_5 > 200) {
                        me = createMessage(player, player.IdDanhHieu_5, ycongdanhhieu);
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendDanhHieuAll(Player player, Player p2, int id) {
        Message me;
        try {
            int ycongdanhhieu = 0;

            switch (id) {
                case 1:
                    if (player.DH1 = true && player.IdDanhHieu_1 > 200) {
                        me = createMessage2(player, p2, player.IdDanhHieu_1, ycongdanhhieu);
                    }
                    break;
                case 2:
//                    if (player.DH1 = true) {
//                        ycongdanhhieu -= 20;
//                    }
                    if (player.DH2 = true && player.IdDanhHieu_2 > 200) {
                        me = createMessage2(player, p2, player.IdDanhHieu_2, ycongdanhhieu);
                    }
                    break;
                case 3:
//                    if (player.DH1 = true) {
//                        ycongdanhhieu -= 20;
//                    }
//                    if (player.DH2 = true) {
//                        ycongdanhhieu -= 20;
//                    }
                    if (player.DH3 = true && player.IdDanhHieu_3 > 200) {
                        me = createMessage2(player, p2, player.IdDanhHieu_3, ycongdanhhieu);
                    }
                    break;
                case 4:
//                    if (player.DH1 = true) {
//                        ycongdanhhieu -= 20;
//                    }
//                    if (player.DH2 = true) {
//                        ycongdanhhieu -= 20;
//                    }
//                    if (player.DH3 = true) {
//                        ycongdanhhieu -= 20;
//                    }
                    if (player.DH4 = true && player.IdDanhHieu_4 > 200) {
                        me = createMessage2(player, p2, player.IdDanhHieu_4, ycongdanhhieu);
                    }
                    break;
                case 5:
//                    if (player.DH1 = true) {
//                        ycongdanhhieu -= 20;
//                    }
//                    if (player.DH2 = true) {
//                        ycongdanhhieu -= 20;
//                    }
//                    if (player.DH3 = true) {
//                        ycongdanhhieu -= 20;
//                    }
//                    if (player.DH4 = true) {
//                        ycongdanhhieu -= 20;
//                    }
                    if (player.DH5 = true && player.IdDanhHieu_5 > 200) {
                        me = createMessage2(player, p2, player.IdDanhHieu_5, ycongdanhhieu);
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Message createMessage2(Player player, Player p2, int danhHieuCode, int ycongdanhhieu) throws IOException {
        Message me = new Message(-128);
        me.writer().writeByte(0);
        me.writer().writeInt((int) player.id);
        me.writer().writeShort(danhHieuCode);
        me.writer().writeByte(1);
        me.writer().writeByte(-1);
        me.writer().writeShort(50);
        me.writer().writeByte(-1);
        if (ycongdanhhieu != 0) {
            me.writer().writeByte(ycongdanhhieu);
        }
        me.writer().writeByte(-1);
        Service.getInstance().sendMessAllPlayerInMap(player.zone, me);
        me.cleanup();
        return me;
    }

//    private Message createMessage(Player player, int danhHieuCode, int ycongdanhhieu) throws IOException {
//        Message me = new Message(-128);
//        me.writer().writeByte(0);
//        me.writer().writeInt((int) player.id);
//        me.writer().writeShort(danhHieuCode);
//        me.writer().writeByte(1);
//        me.writer().writeByte(-1);
//        me.writer().writeShort(50);
//        me.writer().writeByte(-1);
//        if (ycongdanhhieu != 0) {
//            me.writer().writeByte(ycongdanhhieu);
//        }
//        me.writer().writeByte(-1);
//        Service.getInstance().sendMessAllPlayerInMap(player, me);
//        me.cleanup();
//        return me;
//    }
//
//    public void removeTitle(Player player) {
//        Message me;
//        try {
//            me = new Message(-128);
//            me.writer().writeByte(2);
//            me.writer().writeInt((int) player.id);
//            player.getSession().sendMessage(me);
//            Service.getInstance().sendMessAllPlayerInMap(player, me);
//            SendEffDanhHieu(player);
//            me.cleanup();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    public void SendEffDanhHieu(Player player) {
        for (int i = 0; i < 5; i++) {
            this.sendDanhHieu(player, i);
        }
    }

    public String Name_Danh_Hieu(int iddanhhieu) {
        switch (iddanhhieu) {
            case 645:
                return "Chiến binh rồng";
            case 218:
                return "Chiến binh rồng";
            default:
                return "Danh hiệu không xác định"; // Giá trị trả về mặc định
        }
    }

    // Hàm cấp danh hiệu
    public void send_danh_hieu(Player player, int iddanhhieu, int ngay, int chiSoHP, int chiSoKI, int chiSoSD, int titleIndex) {
        Service.getInstance().sendThongBao(player, "Bạn nhận được danh hiệu " + Name_Danh_Hieu(iddanhhieu) + " " + ngay + " ngày");

        switch (titleIndex) {
            case 1:
                applyTitle(player, iddanhhieu, ngay, chiSoHP, chiSoKI, chiSoSD, "lastTimeTitle1", "isTitleUse", "IdDanhHieu_1", "ChiSoHP_1", "ChiSoKI_1", "ChiSoSD_1");
                break;
            case 2:
                applyTitle(player, iddanhhieu, ngay, chiSoHP, chiSoKI, chiSoSD, "lastTimeTitle2", "isTitleUse2", "IdDanhHieu_2", "ChiSoHP_2", "ChiSoKI_2", "ChiSoSD_2");
                break;
            case 3:
                applyTitle(player, iddanhhieu, ngay, chiSoHP, chiSoKI, chiSoSD, "lastTimeTitle3", "isTitleUse3", "IdDanhHieu_3", "ChiSoHP_3", "ChiSoKI_3", "ChiSoSD_3");
                break;
            case 4:
                applyTitle(player, iddanhhieu, ngay, chiSoHP, chiSoKI, chiSoSD, "lastTimeTitle4", "isTitleUse4", "IdDanhHieu_4", "ChiSoHP_4", "ChiSoKI_4", "ChiSoSD_4");
                break;
            case 5:
                applyTitle(player, iddanhhieu, ngay, chiSoHP, chiSoKI, chiSoSD, "lastTimeTitle5", "isTitleUse5", "IdDanhHieu_5", "ChiSoHP_5", "ChiSoKI_5", "ChiSoSD_5");
                break;
            default:
                Service.getInstance().sendThongBao(player, "Title index không hợp lệ");
        }
    }

    // Hàm áp dụng danh hiệu
    private void applyTitle(Player player, int iddanhhieu, int ngay, int chiSoHP, int chiSoKI, int chiSoSD,
            String lastTimeField, String isTitleUseField, String idDanhHieuField,
            String chiSoHPField, String chiSoKIField, String chiSoSDField) {
        try {
            Field lastTime = Player.class.getField(lastTimeField);
            Field isTitleUse = Player.class.getField(isTitleUseField);
            Field idDanhHieu = Player.class.getField(idDanhHieuField);
            Field fChiSoHP = Player.class.getField(chiSoHPField);
            Field fChiSoKI = Player.class.getField(chiSoKIField);
            Field fChiSoSD = Player.class.getField(chiSoSDField);

            long currentTime = System.currentTimeMillis();
            long currentLastTime = lastTime.getLong(player);
            if (currentLastTime == 0 || idDanhHieu.getInt(player) != iddanhhieu) {
                lastTime.setLong(player, currentTime + (86400000L * ngay));
                isTitleUse.setBoolean(player, true);
                idDanhHieu.setInt(player, iddanhhieu);
                fChiSoHP.setInt(player, chiSoHP);
                fChiSoKI.setInt(player, chiSoKI);
                fChiSoSD.setInt(player, chiSoSD);
            } else {
                lastTime.setLong(player, currentLastTime + (86400000L * ngay));
            }
            Service.getInstance().point(player);
            sendAllTitles(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Gửi danh hiệu đang hiệu lực chồng lên đầu nhân vật
    public void sendAllTitles(Player player) {
        int yOffset = 0;
        try {
            if (player.isTitleUse && player.lastTimeTitle1 > System.currentTimeMillis()) {
                createMessage(player, player.IdDanhHieu_1, yOffset);
                yOffset += 12;
            }
            if (player.isTitleUse2 && player.lastTimeTitle2 > System.currentTimeMillis()) {
                createMessage(player, player.IdDanhHieu_2, yOffset);
                yOffset += 12;
            }
            if (player.isTitleUse3 && player.lastTimeTitle3 > System.currentTimeMillis()) {
                createMessage(player, player.IdDanhHieu_3, yOffset);
                yOffset += 12;
            }
            if (player.isTitleUse4 && player.lastTimeTitle4 > System.currentTimeMillis()) {
                createMessage(player, player.IdDanhHieu_4, yOffset);
                yOffset += 12;
            }
            if (player.isTitleUse5 && player.lastTimeTitle5 > System.currentTimeMillis()) {
                createMessage(player, player.IdDanhHieu_5, yOffset);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Tạo message gửi danh hiệu
    private Message createMessage(Player player, int danhHieuCode, int yOffset) throws IOException {
        Message me = new Message(-128);
        me.writer().writeByte(0);
        me.writer().writeInt((int) player.id);
        me.writer().writeShort(danhHieuCode);
        me.writer().writeByte(1);
        me.writer().writeByte(-1);
        me.writer().writeShort(50);
        me.writer().writeByte(-1);
        if (yOffset != 0) {
            me.writer().writeByte(yOffset);
        }
        me.writer().writeByte(-1);
        Service.getInstance().sendMessAllPlayerInMap(player, me);
        me.cleanup();
        return me;
    }

    // Xóa danh hiệu
    public void removeTitle(Player player) {
        try {
            Message me = new Message(-128);
            me.writer().writeByte(2);
            me.writer().writeInt((int) player.id);
            player.getSession().sendMessage(me);
            Service.getInstance().sendMessAllPlayerInMap(player, me);
            SendEffDanhHieu(player); // Nếu bạn có hiệu ứng danh hiệu riêng
            me.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static final ExecutorService executor = Executors.newFixedThreadPool(5);

    public void sendThreadDanhHieu(Player player) {
        executeInBackground(() -> sendDanhHieuIfTimeExists(player, (short) 1, player.lastTimeTitle1));
        executeInBackground(() -> sendDanhHieuIfTimeExists(player, (short) 2, player.lastTimeTitle2));
        executeInBackground(() -> sendDanhHieuIfTimeExists(player, (short) 3, player.lastTimeTitle3));
        executeInBackground(() -> sendDanhHieuIfTimeExists(player, (short) 4, player.lastTimeTitle4));
        executeInBackground(() -> sendDanhHieuIfTimeExists(player, (short) 5, player.lastTimeTitle5));
    }

    private void executeInBackground(Runnable task) {
        executor.submit(task);
    }

    private void sendDanhHieuIfTimeExists(Player player, short danhHieuId, long lastTime) {
        try {
            Thread.sleep(1000);
            SendEffDanhHieu(player);
        } catch (Exception e) {

        }
    }

}
