package org.zibble.dbedwars.database.jooq.records;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.Multimap;
import org.jooq.Field;
import org.jooq.Record9;
import org.jooq.Row9;
import org.jooq.impl.TableRecordImpl;
import org.zibble.dbedwars.api.objects.serializable.Duration;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.database.data.ArenaHistory;
import org.zibble.dbedwars.database.jooq.tables.ArenaHistoryTable;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ArenaHistoryRecord extends TableRecordImpl<ArenaHistoryRecord> implements Record9<String, String, Multimap<Color, UUID>, Color, Duration, Instant, Map<UUID, Map<XMaterial, Integer>>, Map<UUID, ArenaHistory.DeathData>, Multimap<UUID, Color>> {

    public ArenaHistoryRecord() {
        super(ArenaHistoryTable.ARENA_HISTORY);
    }

    public ArenaHistoryRecord(String id, String gameId, Multimap<Color, UUID> team, Color winnerTeam, Duration runtime, Instant timestamp, Map<UUID, Map<XMaterial, Integer>> itemPickup, Map<UUID, ArenaHistory.DeathData> deaths, Multimap<UUID, Color> bedsBroken) {
        super(ArenaHistoryTable.ARENA_HISTORY);

        setId(id);
        setGameId(gameId);
        setTeams(team);
        setWinner(winnerTeam);
        setRuntime(runtime);
        setTimestamp(timestamp);
        setItemPickup(itemPickup);
        setDeaths(deaths);
        setBedsBroken(bedsBroken);
    }

    public ArenaHistoryRecord(ArenaHistory history) {
        super(ArenaHistoryTable.ARENA_HISTORY);

        setId(history.getId());
        setGameId(history.getGameId());
        setTeams(history.getTeams());
        setWinner(history.getWinner());
        setRuntime(history.getRuntime());
        setTimestamp(history.getTimestamp());
        setItemPickup(history.getItemPickup());
        setDeaths(history.getDeaths());
        setBedsBroken(history.getBedsBroken());
    }

    public ArenaHistory toDataCache() {
        ArenaHistory data = new ArenaHistory();
        data.setId(this.getId());
        data.setGameId(this.getGameId());
        data.setTeams(this.getTeams());
        data.setWinner(this.getWinner());
        data.setRuntime(this.getRuntime());
        data.setTimestamp(this.getTimestamp());
        data.setItemPickup(this.getItemPickup());
        data.setDeaths(this.getDeaths());
        data.setBedsBroken(this.getBedsBroken());
        return data;
    }

    public void setId(String value) {
        set(0, value);
    }

    public String getId() {
        return (String) get(0);
    }

    public void setGameId(String value) {
        set(1, value);
    }

    public String getGameId() {
        return (String) get(1);
    }

    public void setTeams(Multimap<Color, UUID> value) {
        set(2, value);
    }

    public Multimap<Color, UUID> getTeams() {
        return (Multimap<Color, UUID>) get(2);
    }

    public void setWinner(Color value) {
        set(3, value);
    }

    public Color getWinner() {
        return (Color) get(3);
    }

    public void setRuntime(Duration value) {
        set(4, value);
    }

    public Duration getRuntime() {
        return (Duration) get(4);
    }

    public void setTimestamp(Instant value) {
        set(5, value);
    }

    public Instant getTimestamp() {
        return (Instant) get(5);
    }

    public void setItemPickup(Map<UUID, Map<XMaterial, Integer>> value) {
        set(6, value);
    }

    public Map<UUID, Map<XMaterial, Integer>> getItemPickup() {
        return (Map<UUID, Map<XMaterial, Integer>>) get(6);
    }

    public void setDeaths(Map<UUID, ArenaHistory.DeathData> value) {
        set(7, value);
    }

    public Map<UUID, ArenaHistory.DeathData> getDeaths() {
        return (Map<UUID, ArenaHistory.DeathData>) get(7);
    }

    public void setBedsBroken(Multimap<UUID, Color> value) {
        set(8, value);
    }

    public Multimap<UUID, Color> getBedsBroken() {
        return (Multimap<UUID, Color>) get(8);
    }

    @Override
    public Row9<String, String, Multimap<Color, UUID>, Color, Duration, Instant, Map<UUID, Map<XMaterial, Integer>>, Map<UUID, ArenaHistory.DeathData>, Multimap<UUID, Color>> fieldsRow() {
        return (Row9) super.fieldsRow();
    }

    @Override
    public Row9<String, String, Multimap<Color, UUID>, Color, Duration, Instant, Map<UUID, Map<XMaterial, Integer>>, Map<UUID, ArenaHistory.DeathData>, Multimap<UUID, Color>> valuesRow() {
        return (Row9) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return ArenaHistoryTable.ARENA_HISTORY.ID;
    }

    @Override
    public Field<String> field2() {
        return ArenaHistoryTable.ARENA_HISTORY.GAME_ID;
    }

    @Override
    public Field<Multimap<Color, UUID>> field3() {
        return ArenaHistoryTable.ARENA_HISTORY.TEAM;
    }

    @Override
    public Field<Color> field4() {
        return ArenaHistoryTable.ARENA_HISTORY.WINNER;
    }

    @Override
    public Field<Duration> field5() {
        return ArenaHistoryTable.ARENA_HISTORY.RUNTIME;
    }

    @Override
    public Field<Instant> field6() {
        return ArenaHistoryTable.ARENA_HISTORY.TIMESTAMP;
    }

    @Override
    public Field<Map<UUID, Map<XMaterial, Integer>>> field7() {
        return ArenaHistoryTable.ARENA_HISTORY.ITEM_PICKUP;
    }

    @Override
    public Field<Map<UUID, ArenaHistory.DeathData>> field8() {
        return ArenaHistoryTable.ARENA_HISTORY.DEATHS;
    }

    @Override
    public Field<Multimap<UUID, Color>> field9() {
        return ArenaHistoryTable.ARENA_HISTORY.BEDS_BROKEN;
    }

    @Override
    public String component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getGameId();
    }

    @Override
    public Multimap<Color, UUID> component3() {
        return getTeams();
    }

    @Override
    public Color component4() {
        return getWinner();
    }

    @Override
    public Duration component5() {
        return getRuntime();
    }

    @Override
    public Instant component6() {
        return getTimestamp();
    }

    @Override
    public Map<UUID, Map<XMaterial, Integer>> component7() {
        return getItemPickup();
    }

    @Override
    public Map<UUID, ArenaHistory.DeathData> component8() {
        return getDeaths();
    }

    @Override
    public Multimap<UUID, Color> component9() {
        return getBedsBroken();
    }

    @Override
    public String value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getGameId();
    }

    @Override
    public Multimap<Color, UUID> value3() {
        return getTeams();
    }

    @Override
    public Color value4() {
        return getWinner();
    }

    @Override
    public Duration value5() {
        return getRuntime();
    }

    @Override
    public Instant value6() {
        return getTimestamp();
    }

    @Override
    public Map<UUID, Map<XMaterial, Integer>> value7() {
        return getItemPickup();
    }

    @Override
    public Map<UUID, ArenaHistory.DeathData> value8() {
        return getDeaths();
    }

    @Override
    public Multimap<UUID, Color> value9() {
        return getBedsBroken();
    }

    @Override
    public ArenaHistoryRecord value1(String value) {
        setId(value);
        return this;
    }

    @Override
    public ArenaHistoryRecord value2(String value) {
        setGameId(value);
        return this;
    }

    @Override
    public ArenaHistoryRecord value3(Multimap<Color, UUID> value) {
        setTeams(value);
        return this;
    }

    @Override
    public ArenaHistoryRecord value4(Color value) {
        setWinner(value);
        return this;
    }

    @Override
    public ArenaHistoryRecord value5(Duration value) {
        setRuntime(value);
        return this;
    }

    @Override
    public ArenaHistoryRecord value6(Instant value) {
        setTimestamp(value);
        return this;
    }

    @Override
    public ArenaHistoryRecord value7(Map<UUID, Map<XMaterial, Integer>> value) {
        setItemPickup(value);
        return this;
    }

    @Override
    public ArenaHistoryRecord value8(Map<UUID, ArenaHistory.DeathData> value) {
        setDeaths(value);
        return this;
    }

    @Override
    public ArenaHistoryRecord value9(Multimap<UUID, Color> value) {
        setBedsBroken(value);
        return this;
    }

    @Override
    public ArenaHistoryRecord values(String value1, String value2, Multimap<Color, UUID> value3, Color value4, Duration value5, Instant value6, Map<UUID, Map<XMaterial, Integer>> value7, Map<UUID, ArenaHistory.DeathData> value8, Multimap<UUID, Color> value9) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        return this;
    }

}
