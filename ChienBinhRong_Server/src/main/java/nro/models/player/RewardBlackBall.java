package nro.models.player;

import nro.services.Service;
import nro.utils.TimeUtil;
import nro.utils.Util;

import java.util.Date;
import nro.models.item.Item;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.RewardService;

/**
 *
 * @author 💖 Trần Lại 💖
 * @copyright 💖 GirlkuN 💖
 *
 */
public class RewardBlackBall {

    private static final int TIME_REWARD = 79200000;

    public static final int R1S = 15; // +SDG
    public static final int R2S = 20; // +HP + KI
    public static final int R3S = 5; // CN2
    public static final int R4S = 5; // BH2
    public static final int R5S = 5; // BK2
    public static final int R6S = 5; // DBV
    public static final int R7S = 20; // ruby

    public static final int TIME_WAIT = 3600000;

    private Player player;

    public long[] timeOutOfDateReward;
    public long[] lastTimeGetReward;

    public RewardBlackBall(Player player) {
        this.player = player;
        this.timeOutOfDateReward = new long[7];
        this.lastTimeGetReward = new long[7];
    }

    public void reward(byte star) {
        this.timeOutOfDateReward[star - 1] = System.currentTimeMillis() + TIME_REWARD;
        Service.getInstance().point(player);
    }

    public void getRewardSelect(byte select) {
        int index = 0;
        for (int i = 0; i < timeOutOfDateReward.length; i++) {
            if (timeOutOfDateReward[i] > System.currentTimeMillis()) {
                index++;
                if (index == select + 1) {
                    getReward(i + 1);
                    break;
                }
            }
        }
    }

    private void getReward(int star) {
        if (timeOutOfDateReward[star - 1] > System.currentTimeMillis()
                && Util.canDoWithTime(lastTimeGetReward[star - 1], TIME_WAIT)) {
            switch (star) {
                case 1:
                case 2:
                    Service.getInstance().sendThongBao(player, "Phần thưởng chỉ số tự động nhận");
                    break;
                case 3:
                    if (InventoryService.gI().getCountEmptyBag(player) >= 0) {
                        Item cn2 = ItemService.gI().createNewItem((short) 1150, R3S);
                        InventoryService.gI().addItemBag(player, cn2, 9999);
                        InventoryService.gI().sendItemBags(player);
                        lastTimeGetReward[star - 1] = System.currentTimeMillis();
                        Service.getInstance().sendThongBao(player, "Bạn đã nhận thưởng 3 sao!");
                    } else {
                        Service.getInstance().sendThongBao(player, "Hành trang đã đầy");
                    }
                    break;
                case 4:
                    if (InventoryService.gI().getCountEmptyBag(player) >= 0) {
                        Item bh2 = ItemService.gI().createNewItem((short) 1152, R4S);
                        InventoryService.gI().addItemBag(player, bh2, 9999);
                        InventoryService.gI().sendItemBags(player);
                        lastTimeGetReward[star - 1] = System.currentTimeMillis();
                        Service.getInstance().sendThongBao(player, "Bạn đã nhận thưởng 4 sao!");
                    } else {
                        Service.getInstance().sendThongBao(player, "Hành trang đã đầy");
                    }
                    break;
                case 5:
                    if (InventoryService.gI().getCountEmptyBag(player) >= 0) {
                        Item bk2 = ItemService.gI().createNewItem((short) 1151, R5S);
                        InventoryService.gI().addItemBag(player, bk2, 9999);
                        InventoryService.gI().sendItemBags(player);
                        lastTimeGetReward[star - 1] = System.currentTimeMillis();
                        Service.getInstance().sendThongBao(player, "Bạn đã nhận thưởng 5 sao!");
                    } else {
                        Service.getInstance().sendThongBao(player, "Hành trang đã đầy");
                    }
                    break;
                case 6:
                    if (InventoryService.gI().getCountEmptyBag(player) >= 0) {
                        Item dbv = ItemService.gI().createNewItem((short) 987, R6S);
                        InventoryService.gI().addItemBag(player, dbv, 9999);
                        InventoryService.gI().sendItemBags(player);
                        lastTimeGetReward[star - 1] = System.currentTimeMillis();
                        Service.getInstance().sendThongBao(player, "Bạn đã nhận thưởng 6 sao!");
                    } else {
                        Service.getInstance().sendThongBao(player, "Hành trang đã đầy");
                    }
                    break;
                case 7:
                        player.inventory.ruby += R7S;
                        Service.getInstance().sendMoney(player);
                        lastTimeGetReward[star - 1] = System.currentTimeMillis();
                        Service.getInstance().sendThongBao(player, "Bạn đã nhận thưởng 7 sao!");

                    break;
            }
        } else {
            Service.getInstance().sendThongBao(player, "Chưa thể nhận phần quà ngay lúc này, vui lòng đợi "
                    + TimeUtil.diffDate(new Date(lastTimeGetReward[star - 1]), new Date(lastTimeGetReward[star - 1] + TIME_WAIT),
                            TimeUtil.MINUTE) + " phút nữa");
        }
    }

    public void dispose() {
        this.player = null;
    }
}
