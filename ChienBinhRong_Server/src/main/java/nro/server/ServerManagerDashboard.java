package nro.server;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.*;
import java.util.function.Supplier;
import nro.models.boss.BossManager;
import nro.models.player.Player;
import nro.services.Service;

public class ServerManagerDashboard extends JFrame {

    // ======= Suppliers (cắm số liệu thật vào đây) =======
    private Supplier<Integer> supThreads = () -> Thread.activeCount();
    private Supplier<Integer> supSessions = () -> Client.gI().sessions.size();
    private Supplier<Integer> supPlayers = () -> {
        try {
            return Client.gI().getPlayers().size();
        } catch (Throwable t) {
            return 0;
        }
    };
    private Supplier<Integer> supBosses = () -> BossManager.gI().getBosses().size();
    private Supplier<Integer> supClans = () -> {
        try {
            int c = 0;
            for (Object cl : Manager.CLANS) {
                if (cl != null) {
                    c++;
                }
            }
            return c;
        } catch (Throwable t) {
            return 0;
        }
    };
    private Supplier<String> supOS = () -> System.getProperty("os.name") + " " + System.getProperty("os.version");
    private Supplier<String> supArch = () -> System.getProperty("os.arch");
    private Supplier<Integer> supCpuCores = () -> Runtime.getRuntime().availableProcessors();
    private Supplier<Long> supJvmUsed = () -> (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
    private Supplier<Long> supJvmMax = () -> (Runtime.getRuntime().maxMemory()) / (1024 * 1024);
    private Supplier<Integer> supCpuLoad = () -> { // % CPU của process (nếu OSBean hỗ trợ)
        try {
            com.sun.management.OperatingSystemMXBean os
                    = (com.sun.management.OperatingSystemMXBean) java.lang.management.ManagementFactory.getOperatingSystemMXBean();
            double v = os.getProcessCpuLoad();
            if (v < 0) {
                return 0;
            }
            return (int) Math.round(v * 100);
        } catch (Throwable t) {
            return 0;
        }
    };
    private Supplier<Long> supRamTotal = () -> { // RAM vật lý hệ thống (MB)
        try {
            com.sun.management.OperatingSystemMXBean os
                    = (com.sun.management.OperatingSystemMXBean) java.lang.management.ManagementFactory.getOperatingSystemMXBean();
            return os.getTotalPhysicalMemorySize() / (1024 * 1024);
        } catch (Throwable t) {
            return 0L;
        }
    };
    private Supplier<Long> supRamFree = () -> {
        try {
            com.sun.management.OperatingSystemMXBean os
                    = (com.sun.management.OperatingSystemMXBean) java.lang.management.ManagementFactory.getOperatingSystemMXBean();
            return os.getFreePhysicalMemorySize() / (1024 * 1024);
        } catch (Throwable t) {
            return 0L;
        }
    };

    // ======= UI components =======
    private final JLabel lbHeader = new JLabel("Manager Server – NRS", SwingConstants.LEFT);
    private final JTextArea taInfo = new JTextArea();
    private final JTextArea taLog = new JTextArea();

    // Control Panel
    private final JLabel lbAutoSave = new JLabel();
    private final JButton btMinus1 = new JButton("-1 Giây");
    private final JButton btPlus1 = new JButton("+1 Giây");
    private final JToggleButton tgSave = new JToggleButton("Save: OFF");
    private final JButton btBaoTri = new JButton("Bảo Trì");
    private final JButton btSave = new JButton("Save Data");
    private final JButton btExp = new JButton("Exp");
    private final JButton btThongBao = new JButton("Thông Báo");
    private final JButton btLoad = new JButton("Load Data");
    private final JButton btKickAll = new JButton("Kick all");

    // Timers & state
    private int autosaveIntervalSec = 5 * 60; // mặc định 5 phút
    private int countdownSec = autosaveIntervalSec;
    private final Timer tAutoSave;
    private final Timer tRefreshInfo;
    private final int[] baoTriCountdownHolder = new int[1];

    // ======= Maintenance schedule (định kỳ) =======
    private final DefaultListModel<String> mdlSchedules = new DefaultListModel<>();
    private JList<String> listSchedules;
    private JTextField tfHour, tfMinute;
    private JButton btnAddSch, btnRemoveSch, btnClearSch;
    private static final String SCHEDULE_FILE = "maintenanceConfig.txt";

    public ServerManagerDashboard() {
        super(" Server Manager");

        // ---- Header ----
        lbHeader.setFont(lbHeader.getFont().deriveFont(Font.BOLD, 14f));
        lbHeader.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        // ---- Server Information ----
        taInfo.setEditable(false);
        taInfo.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JPanel pnInfo = new JPanel(new BorderLayout());
        pnInfo.setBorder(new TitledBorder("Server Information"));
        pnInfo.add(new JScrollPane(taInfo), BorderLayout.CENTER);
        pnInfo.setPreferredSize(new Dimension(260, 330));

        // ---- Control Panel ----
        JPanel pnTopRow = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(4, 4, 4, 4);
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 3;
        gc.fill = GridBagConstraints.HORIZONTAL;

        JPanel boxAuto = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        boxAuto.add(new JLabel("Next Time Auto Save:"));
        boxAuto.add(lbAutoSave);
        JLabel hint = new JLabel("(5 phút)");
        hint.setForeground(new Color(120, 120, 120));
        boxAuto.add(hint);
        pnTopRow.add(boxAuto, gc);

        gc.gridwidth = 1;
        gc.gridy = 1;
        gc.gridx = 0;
        pnTopRow.add(btMinus1, gc);
        gc.gridx = 1;
        pnTopRow.add(btPlus1, gc);
        gc.gridx = 2;
        tgSave.setPreferredSize(new Dimension(90, 28));
        pnTopRow.add(tgSave, gc);

        JPanel pnButtons = new JPanel(new GridLayout(3, 3, 8, 8));
        pnButtons.add(btBaoTri);
        pnButtons.add(btSave);
        pnButtons.add(btExp);
        pnButtons.add(btThongBao);
        pnButtons.add(btLoad);
        pnButtons.add(btKickAll);
        pnButtons.add(new JLabel());
        pnButtons.add(new JLabel());
        pnButtons.add(new JLabel());

        JPanel pnControl = new JPanel(new BorderLayout(8, 8));
        pnControl.setBorder(new TitledBorder("Control Panel"));
        pnControl.add(pnTopRow, BorderLayout.NORTH);
        pnControl.add(pnButtons, BorderLayout.CENTER);
        pnControl.setPreferredSize(new Dimension(390, 330));

        // ---- Maintenance Schedule Panel ----
        JPanel pnMaint = new JPanel(new BorderLayout(8, 8));
        pnMaint.setBorder(new TitledBorder("Maintenance Schedule (định kỳ)"));

        listSchedules = new JList<>(mdlSchedules);
        listSchedules.setVisibleRowCount(6);
        pnMaint.add(new JScrollPane(listSchedules), BorderLayout.CENTER);

        JPanel pnMaintTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        pnMaintTop.add(new JLabel("Giờ:"));
        tfHour = new JTextField(2);
        pnMaintTop.add(tfHour);
        pnMaintTop.add(new JLabel("Phút:"));
        tfMinute = new JTextField(2);
        pnMaintTop.add(tfMinute);

        btnAddSch = new JButton("Thêm");
        btnRemoveSch = new JButton("Xóa");
        btnClearSch = new JButton("Xóa hết");
      
        pnMaintTop.add(btnAddSch);
        pnMaintTop.add(btnRemoveSch);
        pnMaintTop.add(btnClearSch);
        
        pnMaint.add(pnMaintTop, BorderLayout.NORTH);

        // ---- Server Log ----
        taLog.setEditable(false);
        taLog.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JPanel pnLog = new JPanel(new BorderLayout());
        pnLog.setBorder(new TitledBorder("Server Log"));
        pnLog.add(new JScrollPane(taLog), BorderLayout.CENTER);
        pnLog.setPreferredSize(new Dimension(700, 160));

        // ---- Layout tổng ----
        JPanel content = new JPanel(new BorderLayout(8, 8));
        content.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        top.add(lbHeader);
        content.add(top, BorderLayout.NORTH);

        // center dùng GridBag: pnInfo (0,0), pnControl(1,0), pnMaint(0..1,1)
        JPanel center = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.7;
        c.weighty = 1;
        center.add(pnInfo, c);
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.6;
        center.add(pnControl, c);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.weightx = 0.3;
        c.weighty = 0.5;
        center.add(pnMaint, c);

        content.add(center, BorderLayout.CENTER);
        content.add(pnLog, BorderLayout.SOUTH);

        setContentPane(content);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(760, 620);
        setLocationRelativeTo(null);

        // ---- Actions ----
        tgSave.addActionListener(e -> {
            boolean on = tgSave.isSelected();
            tgSave.setText(on ? "Save: ON" : "Save: OFF");
            appendLog("Toggle AUTO-SAVE: " + (on ? "ON" : "OFF"));
        });
        btMinus1.addActionListener(e -> changeCountdown(-1));
        btPlus1.addActionListener(e -> changeCountdown(+1));

        btBaoTri.addActionListener(this::onBaoTri);
        btSave.addActionListener(e -> {
            appendLog("Save Data requested.");
            ServerManager.gI().saveAll(false);//lưu khi player đang onl
            JOptionPane.showMessageDialog(ServerManagerDashboard.this, "Đã lưu toàn bộ dữ liệu xong.");
        });
        btExp.addActionListener(e -> onExp());
        btThongBao.addActionListener(e -> {
            String msg = JOptionPane.showInputDialog(this, "Nhập nội dung thông báo:");
            if (msg != null && !msg.isEmpty()) {
                appendLog("Broadcast: " + msg);
                Service.getInstance().sendThongBaoAllPlayer("Admin: " + msg);
            }
        });
        btLoad.addActionListener(e -> {
            appendLog("Load Data requested.");
            // TODO: load data thật
            JOptionPane.showMessageDialog(this, "Đã gọi load dữ liệu (stub).");
        });
        btKickAll.addActionListener(e -> {
            appendLog("Kick all requested.");

            int count = 0;
            for (Player pl : Client.gI().getPlayers()) {
                try {
                    if (pl != null && pl.getSession() != null) {
                        ServerManager.gI().saveAll(true);//lưu lại dữ liệu 
                        pl.getSession().disconnect();
                        count++;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            JOptionPane.showMessageDialog(this, "Đã kick toàn bộ " + count + " người chơi.");
        });

        // Maintenance actions
        btnAddSch.addActionListener(e -> addScheduleFromInput());
        btnRemoveSch.addActionListener(e -> removeSelectedSchedule());
        btnClearSch.addActionListener(e -> clearAllSchedules());
        

        // ---- Timers ----
        tAutoSave = new Timer(1000, e -> tickAutosave());
        tAutoSave.start();

        tRefreshInfo = new Timer(1000, e -> refreshInfo());
        tRefreshInfo.start();

        // hiển thị ngay lần đầu
        refreshInfo();
        updateCountdownLabel();

        // Load file lịch & start scheduler
        loadMaintenanceSchedulesFromFile();
    }

    // ======= Control Panel handlers =======
    private void onBaoTri(ActionEvent e) {
        int seconds = 60; // ví dụ đếm ngược 60s
        appendLog("Bắt đầu đếm lùi bảo trì " + seconds + "s.");
        Timer t = new Timer(1000, null);
        baoTriCountdownHolder[0] = seconds;
        t.addActionListener(ev -> {
            baoTriCountdownHolder[0]--;
            if (baoTriCountdownHolder[0] <= 0) {
                ((Timer) ev.getSource()).stop();
                appendLog("Thực thi bảo trì!");
                Maintenance.gI().start(seconds);
            }
        });
        t.start();

        // TODO: Maintenance.gI().start(seconds);
        JOptionPane.showMessageDialog(this, "Đã gọi lịch bảo trì trong " + seconds + " giây (stub).");
    }

    private void onExp() {
        String exp = JOptionPane.showInputDialog(this,
                "Bảng Exp Server\nExp Server hiện tại: " + Manager.RATE_EXP_SERVER);
        if (exp != null) {
            try {
                byte rate = Byte.parseByte(exp.trim());
                Manager.RATE_EXP_SERVER = rate;
                appendLog("Cập nhật EXP server = " + rate);
                JOptionPane.showMessageDialog(this, "Cập nhật thành công! EXP: " + rate);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Vui lòng chỉ nhập số hợp lệ!");
            }
        }
    }

    // ======= Autosave countdown =======
    private void changeCountdown(int delta) {
        autosaveIntervalSec = Math.max(5, autosaveIntervalSec + delta);
        countdownSec = Math.min(countdownSec + delta, autosaveIntervalSec);
        updateCountdownLabel();
    }

    private void tickAutosave() {
        if (!tgSave.isSelected()) {
            updateCountdownLabel();
            return;
        }
        countdownSec--;
        if (countdownSec <= 0) {
            appendLog("AUTO-SAVE chạy.");
            try {
                // gọi hàm saveAll thực sự
                ServerManager.gI().saveAll(false);
            } catch (Exception ex) {
                ex.printStackTrace();
                appendLog("Lỗi khi AUTO-SAVE: " + ex.getMessage());
            }
            countdownSec = autosaveIntervalSec;
        }
        updateCountdownLabel();
    }

    private void updateCountdownLabel() {
        String hhmmss = formatDuration(countdownSec);
        lbAutoSave.setText(hhmmss);
    }

    private static String formatDuration(int totalSec) {
        if (totalSec < 0) {
            totalSec = 0;
        }
        Duration d = Duration.ofSeconds(totalSec);
        long h = d.toHours();
        long m = d.minusHours(h).toMinutes();
        long s = d.minusHours(h).minusMinutes(m).getSeconds();
        if (h > 0) {
            return String.format("%02d:%02d:%02d", h, m, s);
        }
        return String.format("%02d:%02d", m, s);
    }

    // ======= Server info =======
    private void refreshInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("• Thread: %d%n", supThreads.get()));
        sb.append(String.format("• Session: %d%n", supSessions.get()));
        sb.append(String.format("• Player: %d%n", supPlayers.get()));
        sb.append(String.format("• Boss: %d%n", supBosses.get()));
        sb.append(String.format("• Clan: %d%n", supClans.get()));
        sb.append(String.format("• CPU cores: %d%n", supCpuCores.get()));
        sb.append(String.format("• JVM RAM used: %d MB / %d MB%n", supJvmUsed.get(), supJvmMax.get()));
        sb.append(String.format("• CPU Load: %d%%%n", supCpuLoad.get()));
        sb.append(String.format("• RAM Total: %d MB%n", supRamTotal.get()));
        sb.append(String.format("• RAM Free: %d MB%n", supRamFree.get()));

        taInfo.setText(sb.toString());
        taInfo.setCaretPosition(0);
    }

    private void appendLog(String line) {
        taLog.append(line + "\n");
        taLog.setCaretPosition(taLog.getDocument().getLength());
    }

    // ======= Maintenance helpers =======
    private void addScheduleFromInput() {
        String hStr = tfHour.getText().trim();
        String mStr = tfMinute.getText().trim();
        int h, m;
        try {
            h = Integer.parseInt(hStr);
            m = Integer.parseInt(mStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số (giờ 0–23, phút 0–59)");
            return;
        }
        if (h < 0 || h > 23 || m < 0 || m > 59) {
            JOptionPane.showMessageDialog(this, "Khoảng hợp lệ: giờ 0–23, phút 0–59");
            return;
        }
        String hhmm = String.format("%d:%02d", h, m);
        // tránh trùng
        for (int i = 0; i < mdlSchedules.size(); i++) {
            if (mdlSchedules.get(i).equals(hhmm)) {
                JOptionPane.showMessageDialog(this, "Giờ này đã có trong lịch");
                return;
            }
        }
        mdlSchedules.addElement(hhmm);
        saveMaintenanceSchedulesToFile();
        appendLog("⏰ Đã đặt lịch bảo trì: " + hhmm);

        // 👉 gọi AutoMaintenance luôn
        AutoMaintenance auto = new AutoMaintenance(h, m);
        auto.start();

        tfHour.setText("");
        tfMinute.setText("");
    }

    private void removeSelectedSchedule() {
        int idx = listSchedules.getSelectedIndex();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "Chọn 1 dòng trong danh sách để xóa");
            return;
        }
        String val = mdlSchedules.get(idx);
        mdlSchedules.remove(idx);
        saveMaintenanceSchedulesToFile();
        appendLog("Xóa lịch bảo trì: " + val);
    }

    private void clearAllSchedules() {
        if (mdlSchedules.isEmpty()) {
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Xóa toàn bộ lịch bảo trì?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            mdlSchedules.clear();
            saveMaintenanceSchedulesToFile();
            appendLog("Đã xóa tất cả lịch bảo trì");
        }
    }

  
    private void loadMaintenanceSchedulesFromFile() {
        mdlSchedules.clear();
        java.io.File f = new java.io.File(SCHEDULE_FILE);
        if (!f.exists()) {
            return;
        }
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                if (!line.matches("^\\d{1,2}:\\d{2}$")) {
                    continue;
                }
                String[] sp = line.split(":");
                int h = Integer.parseInt(sp[0]);
                int m = Integer.parseInt(sp[1]);
                if (h < 0 || h > 23 || m < 0 || m > 59) {
                    continue;
                }

                String hhmm = String.format("%d:%02d", h, m);
                mdlSchedules.addElement(hhmm);

                // 👉 chạy AutoMaintenance cho mỗi lịch trong file
                AutoMaintenance auto = new AutoMaintenance(h, m);
                auto.start();
            }
        } catch (Exception ex) {
            appendLog("Lỗi đọc maintenanceConfig.txt: " + ex.getMessage());
        }
    }

    private void saveMaintenanceSchedulesToFile() {
        try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(SCHEDULE_FILE))) {
            List<String> lst = new ArrayList<>();
            for (int i = 0; i < mdlSchedules.size(); i++) {
                lst.add(mdlSchedules.get(i));
            }
            lst.sort(Comparator.comparing(this::parseTime));
            for (String s : lst) {
                bw.write(s);
                bw.newLine();
            }
        } catch (Exception ex) {
            appendLog("Lỗi ghi maintenanceConfig.txt: " + ex.getMessage());
        }
        sortScheduleModel();
    }

    private void sortScheduleModel() {
        List<String> lst = new ArrayList<>();
        for (int i = 0; i < mdlSchedules.size(); i++) {
            lst.add(mdlSchedules.get(i));
        }
        lst.sort(Comparator.comparing(this::parseTime));
        mdlSchedules.clear();
        for (String s : lst) {
            mdlSchedules.addElement(s);
        }
    }

    private LocalTime parseTime(String hhmm) {
        String[] sp = hhmm.split(":");
        int h = Integer.parseInt(sp[0]);
        int m = Integer.parseInt(sp[1]);
        return LocalTime.of(h, m);
    }

    // ======= Main demo =======
    public static void main(String[] args) {
        try {
            // Nimbus cho cảm giác giống ảnh
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
        }
        SwingUtilities.invokeLater(() -> new ServerManagerDashboard().setVisible(true));
    }
}
