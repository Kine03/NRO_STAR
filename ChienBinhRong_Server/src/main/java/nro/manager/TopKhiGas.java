package nro.manager;

import lombok.Getter;
import nro.jdbc.DBService;
import nro.jdbc.daos.PlayerDAO;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.player.Player;
import nro.services.ItemService;
import nro.utils.Log;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import nro.models.clan.Clan;
import nro.models.clan.ClanMember;
import nro.server.Manager;

/**
 * @author outcast c-cute hột me 😳
 */
public class TopKhiGas {

    @Getter
    private List<Clan> list = new ArrayList<>();
    
    private static final TopKhiGas INSTANCE = new TopKhiGas();
    

    public static TopKhiGas getInstance() {
        return INSTANCE;
    }

    public void load() {
        list.clear();

        try ( Connection con = DBService.gI().getConnectionForGetPlayer();  PreparedStatement ps = con.prepareStatement("SELECT *,CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(thanhTichKhiGas, ',', 1), '[', -1) AS UNSIGNED) AS part1, CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(thanhTichKhiGas, ',', -1), ']', 1) AS UNSIGNED) AS part2 FROM clan_sv" + Manager.SERVER +" ORDER BY part1 DESC, part2 ASC LIMIT 100;");
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Clan clan = extractClanFromResultSet(rs);
                list.add(clan);
            }
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e);
        }
    }

    private Clan extractClanFromResultSet(ResultSet rs) throws SQLException {
        Clan clan = new Clan();
        JSONArray dataArray = null;
        JSONObject dataObject = null;
        JSONValue jv = new JSONValue();
        clan.id = rs.getInt("id");
        clan.name = rs.getString("name");
        dataArray = (JSONArray) JSONValue.parse(rs.getString("thanhTichKhiGas"));
        if (!dataArray.isEmpty()) {
            clan.levelDoneKhiGas = Integer.parseInt(String.valueOf(dataArray.get(0)));
            clan.thoiGianHoanThanhKhiGas = Long.parseLong(String.valueOf(dataArray.get(1)));
        }
        dataArray.clear();
                dataArray = (JSONArray) jv.parse(rs.getString("members"));
                for (int i = 0; i < dataArray.size(); i++) {
                    dataObject = (JSONObject) jv.parse(String.valueOf(dataArray.get(i)));
                    ClanMember cm = new ClanMember();
                    cm.clan = clan;
                    cm.id = Integer.parseInt(String.valueOf(dataObject.get("id")));
                    cm.name = String.valueOf(dataObject.get("name"));
                    cm.head = Short.parseShort(String.valueOf(dataObject.get("head")));
                    cm.body = Short.parseShort(String.valueOf(dataObject.get("body")));
                    cm.leg = Short.parseShort(String.valueOf(dataObject.get("leg")));
                    cm.role = Byte.parseByte(String.valueOf(dataObject.get("role")));
                    cm.donate = Integer.parseInt(String.valueOf(dataObject.get("donate")));
                    cm.receiveDonate = Integer.parseInt(String.valueOf(dataObject.get("receive_donate")));
                    cm.memberPoint = Integer.parseInt(String.valueOf(dataObject.get("member_point")));
                    cm.clanPoint = Integer.parseInt(String.valueOf(dataObject.get("clan_point")));
                    cm.joinTime = Integer.parseInt(String.valueOf(dataObject.get("join_time")));
                    cm.timeAskPea = Long.parseLong(String.valueOf(dataObject.get("ask_pea_time")));
                    try {
                        cm.powerPoint = Long.parseLong(String.valueOf(dataObject.get("power")));
                    } catch (Exception e) {
                    }
                    clan.addClanMember(cm);
                }
        return clan;
    }
}
