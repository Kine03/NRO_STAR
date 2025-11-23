package nro.models.npc.specialnpc;

import java.io.IOException;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.player.Player;
import nro.server.io.Message;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.Service;
import nro.services.func.InventoryServiceNew;
import nro.utils.Log;
import nro.utils.Util;

public class TrungLinhThu {

    private static final long DEFAULT_TIME_DONE = 432000000L;
    private Player player;
    public long lastTimeCreate;
    public long timeDone;

    private final short id = 51;

    public TrungLinhThu(Player player, long lastTimeCreate, long timeDone) {
        this.player = player;
        this.lastTimeCreate = lastTimeCreate;
        this.timeDone = timeDone;
    }

    public static void createLinhThuEgg(Player player) {
        player.LinhThuEgg = new TrungLinhThu(player, System.currentTimeMillis(), DEFAULT_TIME_DONE);
    }

    public void sendLinhThuEgg() {
        Message msg;
        try {
            msg = new Message(-122);
            msg.writer().writeShort(this.id);
            msg.writer().writeByte(1);
            msg.writer().writeShort(14011);
            msg.writer().writeByte(0);
            msg.writer().writeInt(this.getSecondDone());
            this.player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
            Log.error(TrungLinhThu.class, e);
        }
    }

    public void openEgg() {
        try {
            destroyEgg();
            Thread.sleep(1000);
            int[] idpet = {2073, 2075, 2063, 2060, 2059, 1973, 1974,2053, 2054};
            int[] option = {50, 77, 103, 5, 14, 5, 14,77,103};
            Item Pet = ItemService.gI().createNewItem((short) idpet[Util.nextInt(0, idpet.length - 1)]);
            Pet.itemOptions.add(new ItemOption((short) option[Util.nextInt(0, idpet.length - 1)], Util.nextInt(6, 17)));
            InventoryService.gI().addItemBag(player, Pet,9999);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được linh thú");
            InventoryService.gI().sendItemBags(player);
            player.LinhThuEgg = null;
        } catch (InterruptedException e) {
        }

    }

    public void destroyEgg() {
        try {
            Message msg = new Message(-117);
            msg.writer().writeByte(101);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
        }
        this.player.LinhThuEgg = null;
    }

    public int getSecondDone() {
        int seconds = (int) ((lastTimeCreate + timeDone - System.currentTimeMillis()) / 1000);
        return Math.max(seconds, 0);
    }

    public void subTimeDone(int d, int h, int m, int s) {
        long timeToRemove = (d * 24 * 60 * 60 * 1000L) + (h * 60 * 60 * 1000L) + (m * 60 * 1000L) + (s * 1000L);
        this.timeDone -= timeToRemove;
        this.sendLinhThuEgg();
    }

    public void dispose() {
        this.player = null;
    }
}
