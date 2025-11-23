package nro.models.item;

import nro.consts.ConstPlayer;
import nro.models.player.NPoint;
import nro.models.player.Player;
import nro.services.ItemTimeService;
import nro.services.PlayerService;
import nro.services.Service;
import nro.utils.Util;

/**
 * @author 💖 Trần Lại 💖
 * @copyright 💖 GirlkuN 💖
 */
public class ItemTime {

    //id item text
    public static final byte DOANH_TRAI = 0;
    public static final byte BAN_DO_KHO_BAU = 1;

    public static final byte TEXT_NHIEM_VU_HANG_NGAY = 2;

    public static final byte TEXT_NHAN_BUA_MIEN_PHI = 3;

    public static final byte KHI_GAS = 4;

    public static final int TIME_ITEM = 600000;
    public static final int TIME_ITEM_X2_DT = 1800000;
    public static final int TIME_ruotden = 900000;
    public static final int TIME_OPEN_POWER = 86400000;
    public static final int TIME_MAY_DO = 1800000;
    public static final int TIME_NHAN_MA_PHAP = 1800000;
    public static final int TIME_NHAN_LINH_GIOI = 1800000;
    public static final int TIME_EAT_MEAL = 600000;
    public static final int TIME_SMTA = 900000;

    public static final int TIME_DANH_NHAN_BAN = 300000;

    private Player player;
    public boolean isUseBoHuyet2;
    public boolean isUseBoKhi2;
    public boolean isUseGiapXen2;
    public boolean isUseCuongNo2;
    public long lastTimeBoHuyet2;
    public long lastTimeBoKhi2;
    public long lastTimeGiapXen2;
    public long lastTimeCuongNo2;

    public boolean isUseruotden;
    public long lastTimeruotden;

    public boolean isMaTroi;
    public long lastTimeMaTroi;
    public boolean isDoiNhi;
    public long lastTimeDoiNhi;
    public boolean isBoXuong;
    public long lastTimeBoXuong;
    public int iconMaTroi;

    public boolean isUseBanhChung;
    public boolean isUseBanhTet;
    public long lastTimeBanhChung;
    public long lastTimeBanhTet;
    public boolean isUseBoHuyet;
    public boolean isUseBoKhi;
    public boolean isUseGiapXen;
    public boolean isUseCuongNo;
    public boolean isUseAnDanh;
    public long lastTimeBoHuyet;
    public long lastTimeBoKhi;
    public long lastTimeGiapXen;
    public long lastTimeCuongNo;
    public long lastTimeAnDanh;
    public boolean isUsebuax5;
    public boolean isUsebuax2dt;
    public boolean isUse4la;
    public boolean isUsebinhcommeson;
    public long lastTimebuax5;
    public long lastTimebuax2dt;
    public long lastTime4la;
    public long lastTime;
    public long lastTimebinhcommeson;
    public boolean isUseMayDo;
    public long lastTimeUseMayDo;
    public boolean isUseNhanMaPhap;
    public long lastTimeUseNhanMaPhap;
    public boolean isUseNhanLinhGioi;
    public long lastTimeUseNhanLinhGioi;

    public boolean isUseCuongbao;
    public long lastTimeCuongbao;

    public boolean isUseSMTA;
    public long lastTimeSMTA;

    public boolean isOpenPower;
    public long lastTimeOpenPower;

    public boolean isUseTDLT;
    public long lastTimeUseTDLT;
    public int timeTDLT;

    public boolean isEatMeal;
    public long lastTimeEatMeal;
    public int iconMeal;

    public boolean isDanhNhanBan;
    public boolean doneDanhNhanBan = false;
    public long lasttimeDanhNhanBan;

    public ItemTime(Player player) {
        this.player = player;
    }

    public void update() {
        boolean update = false;
        if (isEatMeal) {
            if (Util.canDoWithTime(lastTimeEatMeal, TIME_EAT_MEAL)) {
                isEatMeal = false;
                update = true;
            }
        }
        if (isDanhNhanBan) {
            if (Util.canDoWithTime(lasttimeDanhNhanBan, TIME_DANH_NHAN_BAN)) {
                isDanhNhanBan = false;
            }
        }
        if (isUseBoHuyet) {
            if (Util.canDoWithTime(lastTimeBoHuyet, TIME_ITEM)) {
                isUseBoHuyet = false;
                update = true;
            }
        }
        if (isUseBoKhi) {
            if (Util.canDoWithTime(lastTimeBoKhi, TIME_ITEM)) {
                isUseBoKhi = false;
                update = true;
            }
        }
        if (isUseGiapXen) {
            if (Util.canDoWithTime(lastTimeGiapXen, TIME_ITEM)) {
                isUseGiapXen = false;
            }
        }
        if (isUseCuongNo) {
            if (Util.canDoWithTime(lastTimeCuongNo, TIME_ITEM)) {
                isUseCuongNo = false;
                update = true;
            }
        }
        if (isUseAnDanh) {
            if (Util.canDoWithTime(lastTimeAnDanh, TIME_ITEM)) {
                isUseAnDanh = false;
            }
        }
        if (isUseBanhChung) {
            if (Util.canDoWithTime(lastTimeBanhChung, TIME_ITEM)) {
                isUseBanhChung = false;
            }
        }
        if (isUseBanhTet) {
            if (Util.canDoWithTime(lastTimeBanhTet, TIME_ITEM)) {
                isUseBanhTet = false;
            }
        }
        if (isUsebuax5) {
            if (Util.canDoWithTime(lastTimebuax5, TIME_ITEM)) {
                isUsebuax5 = false;
            }
        }
        if (isUsebuax2dt) {
            if (Util.canDoWithTime(lastTimebuax2dt, TIME_ITEM_X2_DT)) {
                isUsebuax2dt = false;
            }
        }
        if (isUse4la) {
            if (Util.canDoWithTime(lastTime, TIME_ITEM_X2_DT)) {
                isUse4la = false;
            }
        }
        if (isUsebinhcommeson) {
            if (Util.canDoWithTime(lastTimebinhcommeson, TIME_ITEM)) {
                isUsebinhcommeson = false;
            }
        }
        if (isUseBoHuyet2) {
            if (Util.canDoWithTime(lastTimeBoHuyet2, TIME_ITEM)) {
                isUseBoHuyet2 = false;
                update = true;
            }
        }
        if (isUseBoKhi2) {
            if (Util.canDoWithTime(lastTimeBoKhi2, TIME_ITEM)) {
                isUseBoKhi2 = false;
                update = true;
            }
        }
        if (isUseGiapXen2) {
            if (Util.canDoWithTime(lastTimeGiapXen2, TIME_ITEM)) {
                isUseGiapXen2 = false;
            }
        }
        if (isUseCuongNo2) {
            if (Util.canDoWithTime(lastTimeCuongNo2, TIME_ITEM)) {
                isUseCuongNo2 = false;
                update = true;
            }
        }
        if (isUseruotden) {
            if (Util.canDoWithTime(lastTimeruotden, TIME_ruotden)) {
                isUseruotden = false;
                update = true;
                player.inventory.ruby += 3000;
                player.event.addEventPoint(30);
                Service.getInstance().sendMoney(player);
                Service.getInstance().sendThongBao(player, "Bạn vừa nhận được 3000 hồng ngọc và 30 điểm sự kiện");
            }
        }
        if (isUseCuongbao) {
            if (Util.canDoWithTime(lastTimeCuongbao, TIME_MAY_DO)) {
                isUseCuongbao = false;
                update = true;
//                PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.NON_PK);
            }
        }
        if (isOpenPower) {
            if (Util.canDoWithTime(lastTimeOpenPower, TIME_OPEN_POWER)) {
                player.nPoint.limitPower++;
                if (player.nPoint.limitPower > NPoint.MAX_LIMIT) {
                    player.nPoint.limitPower = NPoint.MAX_LIMIT;
                }
                player.nPoint.initPowerLimit();
                Service.getInstance().sendThongBao(player, "Giới hạn sức mạnh của bạn đã được tăng lên 1 bậc");
                isOpenPower = false;
            }
        }
        if (isUseMayDo) {
            if (Util.canDoWithTime(lastTimeUseMayDo, TIME_MAY_DO)) {
                isUseMayDo = false;
            }
        }
        if (isUseTDLT) {
            if (Util.canDoWithTime(lastTimeUseTDLT, timeTDLT)) {
                this.isUseTDLT = false;
                ItemTimeService.gI().sendCanAutoPlay(this.player);
            }
        }
        if (isUseBanhChung) {
            if (Util.canDoWithTime(lastTimeBanhChung, TIME_MAY_DO)) {
                isUseBanhChung = false;
                update = true;
            }
        }
        if (isUseBanhTet) {
            if (Util.canDoWithTime(lastTimeBanhTet, TIME_ITEM)) {
                isUseBanhTet = false;
                update = true;
            }
        }
        if (isMaTroi) {
            if (Util.canDoWithTime(lastTimeMaTroi, TIME_ITEM)) {
                isMaTroi = false;
                Service.getInstance().Send_Caitrang(player);
                update = false;
            }
        }
        if (isDoiNhi) {
            if (Util.canDoWithTime(lastTimeDoiNhi, TIME_ITEM)) {
                isDoiNhi = false;
                Service.getInstance().Send_Caitrang(player);
                update = false;
            }
        }
        if (isBoXuong) {
            if (Util.canDoWithTime(lastTimeBoXuong, TIME_ITEM)) {
                isBoXuong = false;
                Service.getInstance().Send_Caitrang(player);
                update = false;
            }
        }
        if (isUseNhanMaPhap) {
            if (Util.canDoWithTime(lastTimeUseNhanMaPhap, TIME_NHAN_MA_PHAP)) {
                isUseNhanMaPhap = false;
                update = true;
            }
        }
        if (isUseNhanLinhGioi) {
            if (Util.canDoWithTime(lastTimeUseNhanLinhGioi, TIME_NHAN_LINH_GIOI)) {
                isUseNhanLinhGioi = false;
                update = true;
            }
        }
        if (isUseSMTA) {
            if (Util.canDoWithTime(lastTimeSMTA, TIME_SMTA)) {
                isUseSMTA = false;
                update = true;
            }
        }
        if (update) {
            Service.getInstance().point(player);
        }
    }

    public void dispose() {
        this.player = null;
    }
}
