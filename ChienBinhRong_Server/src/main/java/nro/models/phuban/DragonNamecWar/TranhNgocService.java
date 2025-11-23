/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nro.models.phuban.DragonNamecWar;

import java.util.List;
import nro.consts.ConstTranhNgocNamek;
import nro.models.item.Item;
import nro.models.map.ItemMap;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.server.io.Message;
import nro.services.InventoryService;
import nro.services.ItemMapService;
import nro.services.ItemService;
import nro.services.PlayerService;
import nro.services.Service;
import nro.utils.Util;

/**
 *
 * @Build Arriety
 */
public class TranhNgocService {

    private static TranhNgocService instance;

    public static TranhNgocService getInstance() {
        if (instance == null) {
            instance = new TranhNgocService();
        }
        return instance;
    }

    public void sendCreatePhoBan(Player pl) {
        Message msg;
        try {
            msg = new Message(20);
            msg.writer().writeByte(0);
            msg.writer().writeByte(0);
            msg.writer().writeShort(ConstTranhNgocNamek.MAP_ID);
            msg.writer().writeUTF(ConstTranhNgocNamek.CADIC); // team 1
            msg.writer().writeUTF(ConstTranhNgocNamek.FIDE); // team 2
            msg.writer().writeInt(ConstTranhNgocNamek.MAX_LIFE);
            msg.writer().writeShort(ConstTranhNgocNamek.TIME_SECOND);
            msg.writer().writeByte(ConstTranhNgocNamek.MAX_POINT);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendEndPhoBan(Zone zone, byte type, boolean isFide) {
        Message msg;
        try {
            msg = new Message(20);
            msg.writer().writeByte(0);
            msg.writer().writeByte(2);
            msg.writer().writeByte(type);
            if (zone != null) {
                List<Player> players = isFide ? zone.getPlayersFide() : zone.getPlayersCadic();
                synchronized (players) {
                    for (Player pl : players) {
                        if (pl != null) {
                            pl.sendMessage(msg);
                        }
                    }
                }
                msg.cleanup();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendUpdateTime(Player pl, short second) {
        Message msg;
        try {
            msg = new Message(20);
            msg.writer().writeByte(0);
            msg.writer().writeByte(5);
            msg.writer().writeShort(second);
            Service.gI().sendMessAllPlayerInMap(pl.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendUpdatePoint(Player pl) {
        Message msg;
        try {
            msg = new Message(20);
            msg.writer().writeByte(0);
            msg.writer().writeByte(4);
            msg.writer().writeByte(pl.zone.pointCadic); // gửi điểm Cadic
            msg.writer().writeByte(pl.zone.pointFide);  // gửi điểm Fide
            Service.gI().sendMessAllPlayerInMap(pl.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void givePrice(List<Player> players, byte type, int point) {
        switch (type) {
            case ConstTranhNgocNamek.LOSE:
//                int pointDiff = ConstTranhNgocNamek.MAX_POINT - point;
                for (Player pl : players) {
                    if (pl != null) {
//                        Item mcl = InventoryService.gI().findItemBagByTemp(pl, ConstTranhNgocNamek.ITEM_HONG_NGOC);
//                        if (mcl != null) {
//                            InventoryService.gI().subQuantityItemsBag(pl, mcl, 1);
//                            InventoryService.gI().sendItemBags(pl);
//                        }
                        if (pl.diem_tranh_ngoc > 0) {
                            pl.diem_tranh_ngoc -= 2;
                            PlayerService.gI().savePlayer(pl);
                        }
                        TranhNgoc.gI().removePlayersCadic(pl);
                        TranhNgoc.gI().removePlayersFide(pl);
                        pl.iDMark.setTranhNgoc((byte) -1);//reset trạng thái người chơi
                    }
                }
                break;
            case ConstTranhNgocNamek.WIN:
                for (Player pl : players) {
                    if (pl != null) {
//                        Item mcl = ItemService.gI().createNewItem((short) ConstTranhNgocNamek.ITEM_TRANH_NGOC);
//                        mcl.quantity = point;
//                        InventoryService.gI().addItemBag(pl, mcl, 1);
//                        InventoryService.gI().sendItemBags(pl);
                        pl.diem_tranh_ngoc += point;//Cộng điểm sk
                        PlayerService.gI().savePlayer(pl);
                        TranhNgoc.gI().removePlayersCadic(pl);
                        TranhNgoc.gI().removePlayersFide(pl);
                        pl.iDMark.setTranhNgoc((byte) -1);//reset trạng thái người chơi
                    }
                }
                break;
            case ConstTranhNgocNamek.DRAW:
                for (Player pl : players) {
                    if (pl != null) {
//                        Item mcl = ItemService.gI().createNewItem((short) ConstTranhNgocNamek.ITEM_TRANH_NGOC);
//                        mcl.quantity = point / 2;
//                        InventoryService.gI().addItemBag(pl, mcl, 1);
//                        InventoryService.gI().sendItemBags(pl);
                        pl.diem_tranh_ngoc += 2;//Cộng điểm sk
                        PlayerService.gI().savePlayer(pl);
                        TranhNgoc.gI().removePlayersCadic(pl);
                        TranhNgoc.gI().removePlayersFide(pl);
                        pl.iDMark.setTranhNgoc((byte) -1);//reset trạng thái người chơi
                    }
                }
                break;
            default:
                break;
        }
    }

    public void pickBall(Player player, ItemMap item) {
        if (player.isHoldNamecBallTranhDoat || item.typeHaveBallTranhDoat == player.iDMark.getTranhNgoc()) {
            return;
        }
        if (item.typeHaveBallTranhDoat != -1 && item.typeHaveBallTranhDoat != player.iDMark.getTranhNgoc()) {
            if (player.iDMark.getTranhNgoc() == 1) {
                player.zone.pointFide--;
            } else if (player.iDMark.getTranhNgoc() == 2) {
                player.zone.pointCadic--;
            }
            sendUpdatePoint(player);
        }
        player.tempIdNamecBallHoldTranhDoat = item.itemTemplate.id;
        player.isHoldNamecBallTranhDoat = true;
        player.isHoldNamecBall = true; // thêm dòng này
        ItemMapService.gI().removeItemMapAndSendClient(item);
        Service.gI().sendFlagBag(player);
        Service.gI().sendThongBao(player, "Bạn đang giữ viên ngọc rồng Namek");
    }

    // Khi bị đánh chết và rơi Ngọc Rồng ra map
    public void dropBall(Player player, byte a) {
        // Tắt trạng thái giữ Ngọc Tranh Đoạt
        player.isHoldNamecBallTranhDoat = false;

        // Tạo vật phẩm rơi trên map
        int x = Util.nextInt(20, player.zone.map.mapWidth);
        int y = player.zone.map.yPhysicInTop(x, player.zone.map.mapHeight / 2);
        ItemMap itemMap = new ItemMap(player.zone, player.tempIdNamecBallHoldTranhDoat, 1, x, y, -1);
        itemMap.isNamecBallTranhDoat = true;
        itemMap.typeHaveBallTranhDoat = a;
        itemMap.x = player.location.x;
        itemMap.y = player.location.y;

        // Gửi item rơi ra map
        Service.gI().dropItemMap(player.zone, itemMap);
        player.tempIdNamecBallHoldTranhDoat = -1;
        player.isHoldNamecBall = false;
        // Cập nhật cờ trên lưng
        Service.gI().sendFlagBag(player);
    }

    public void dropBallTranhNro(Player player, byte a) {
        if (player.tempIdNamecBallHoldTranhDoat != -1) {
            player.isHoldNamecBallTranhDoat = false;
        }
        player.tempIdNamecBallHoldTranhDoat = -1;
        player.isHoldNamecBall = false;
        // Cập nhật cờ trên lưng
        Service.gI().sendFlagBag(player);

        // Thông báo cho người chơi
        Service.gI().sendThongBao(player, "Tốt lắm! tiếp tục đi...");
    }
}

