package org.zibble.dbedwars.database.data;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.zibble.dbedwars.api.game.DeathCause;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.api.objects.serializable.Duration;
import org.zibble.dbedwars.api.util.EnumUtil;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.api.util.properies.NamedProperties;
import org.zibble.dbedwars.database.data.io.DataReader;
import org.zibble.dbedwars.database.data.io.DataWriter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ArenaHistory implements DataCache {

    public static final Key ID = Key.of("id");
    public static final Key GAME_ID = Key.of("game_id");
    public static final Key TEAMS = Key.of("teams");
    public static final Key WINNER = Key.of("winner");
    public static final Key RUNTIME = Key.of("runtime");
    public static final Key TIMESTAMP = Key.of("timestamp");
    public static final Key ITEM_PICKUP = Key.of("item_pickup");
    public static final Key DEATHS = Key.of("deaths");
    public static final Key BED_BROKEN = Key.of("bed_broken");

    private String id;
    private String gameId;
    private Multimap<Color, UUID> teams;
    private Color winner;
    private Duration runtime;
    private Instant timestamp;
    private Map<UUID, Map<XMaterial, Integer>> itemPickup;
    private Map<UUID, DeathData> deaths;
    private Multimap<UUID, Color> bedsBroken;

    public ArenaHistory() {
        this.teams = ArrayListMultimap.create();
        this.itemPickup = new HashMap<>();
        this.deaths = new HashMap<>();
        this.bedsBroken = ArrayListMultimap.create();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public Multimap<Color, UUID> getTeams() {
        return teams;
    }

    public void setTeams(Multimap<Color, UUID> teams) {
        this.teams = teams;
    }

    public Color getWinner() {
        return winner;
    }

    public void setWinner(Color winner) {
        this.winner = winner;
    }

    public Duration getRuntime() {
        return runtime;
    }

    public void setRuntime(Duration runtime) {
        this.runtime = runtime;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Map<UUID, Map<XMaterial, Integer>> getItemPickup() {
        return itemPickup;
    }

    public void setItemPickup(Map<UUID, Map<XMaterial, Integer>> itemPickup) {
        this.itemPickup = itemPickup;
    }

    public Map<UUID, DeathData> getDeaths() {
        return deaths;
    }

    public void setDeaths(Map<UUID, DeathData> deaths) {
        this.deaths = deaths;
    }

    public Multimap<UUID, Color> getBedsBroken() {
        return bedsBroken;
    }

    public void setBedsBroken(Multimap<UUID, Color> bedsBroken) {
        this.bedsBroken = bedsBroken;
    }

    @Override
    public void load(DataReader<?> reader) throws Exception {
        this.id = reader.readString(ID);
        this.gameId = reader.readString(GAME_ID);
        this.teams = reader.readMultiMap(TEAMS, s -> EnumUtil.matchEnum(s, Color.VALUES), s -> {
            try {
                return reader.readList(Key.of(s), UUID::fromString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new ArrayList<>();
        });
        this.winner = reader.readEnum(WINNER, Color.class);
        this.runtime = reader.readDuration(RUNTIME);
        this.timestamp = reader.readInstant(TIMESTAMP);
        this.itemPickup = reader.readMap(ITEM_PICKUP, UUID::fromString, s -> {
            try {
                return reader.readMap(Key.of(s), str -> XMaterial.matchXMaterial(str).get(), str -> {
                    try {
                        return reader.readInt(Key.of(str));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return 0;
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new HashMap<>();
        });
        this.deaths = reader.readMap(DEATHS, UUID::fromString, s -> {
            try {
                DeathData data = new DeathData();
                data.load(reader);
                return data;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new DeathData();
        });
        this.bedsBroken = reader.readMultiMap(BED_BROKEN, UUID::fromString, s -> {
            try {
                return reader.readList(Key.of(s), str -> EnumUtil.matchEnum(str, Color.VALUES));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    @Override
    public void save(DataWriter<?> writer) throws Exception {
        writer.writeString(ID, this.id);
        writer.writeString(GAME_ID, this.gameId);
        writer.writeMultiMap(TEAMS, this.teams, Enum::name, (k, v) -> {
            try {
                writer.writeList(Key.of(k), v, UUID::toString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        writer.writeEnum(WINNER, this.winner);
        writer.writeDuration(RUNTIME, this.runtime);
        writer.writeInstant(TIMESTAMP, this.timestamp);
        writer.writeMap(ITEM_PICKUP, this.itemPickup, UUID::toString, (k, v) -> {
            try {
                writer.writeMap(Key.of(k), v, XMaterial::name, (k2, v2) -> {
                    try {
                        writer.writeInt(Key.of(k2), v2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        writer.writeMap(DEATHS, this.deaths, UUID::toString, (k, v) -> {
            try {
                v.save(writer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        writer.writeMultiMap(BED_BROKEN, this.bedsBroken, UUID::toString, (k, v) -> {
            try {
                writer.writeList(Key.of(k), v, Color::name);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public ArenaHistory copy() {
        ArenaHistory history = new ArenaHistory();
        history.id = this.id;
        history.teams = ArrayListMultimap.create(this.teams);
        history.winner = this.winner;
        history.runtime = this.runtime;
        history.timestamp = this.timestamp;
        history.itemPickup = new HashMap<>(this.itemPickup);
        history.deaths = new HashMap<>(this.deaths);
        history.bedsBroken = ArrayListMultimap.create(this.bedsBroken);
        return history;
    }

    @Override
    public NamedProperties toProperties() {
        return NamedProperties.builder()
                .add("id", this.id)
                .add("teams", this.teams)
                .add("winner", this.winner)
                .add("runtime", this.runtime)
                .add("timestamp", this.timestamp)
                .add("itemPickup", this.itemPickup)
                .add("deaths", this.deaths)
                .add("bedsBroken", this.bedsBroken)
                .build();
    }

    public static class DeathData implements DataCache {

        public static final Key KILLER = Key.of("killer");
        public static final Key CAUSE = Key.of("cause");

        private UUID killer;
        private DeathCause cause = DeathCause.UNKNOWN;

        public DeathData() {
        }

        public DeathData(UUID killer, DeathCause cause) {
            this.killer = killer;
            this.cause = cause;
        }

        public UUID getKiller() {
            return killer;
        }

        public void setKiller(UUID killer) {
            this.killer = killer;
        }

        public DeathCause getCause() {
            return cause;
        }

        public void setCause(DeathCause cause) {
            this.cause = cause;
        }

        @Override
        public DeathData copy() {
            return new DeathData(this.killer, this.cause);
        }

        @Override
        public void load(DataReader<?> reader) throws Exception {
            this.killer = reader.readUUID(KILLER);
            this.cause = reader.readEnum(CAUSE, DeathCause.class);
        }

        @Override
        public void save(DataWriter<?> writer) throws Exception {
            writer.writeUUID(KILLER, this.killer);
            writer.writeEnum(CAUSE, this.cause);
        }

        @Override
        public NamedProperties toProperties() {
            return NamedProperties.builder()
                    .add(KILLER, this.killer)
                    .add(CAUSE, this.cause)
                    .build();
        }

    }

}
