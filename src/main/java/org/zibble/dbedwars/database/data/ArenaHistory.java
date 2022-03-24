package org.zibble.dbedwars.database.data;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.zibble.dbedwars.api.game.DeathCause;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.api.util.Duration;
import org.zibble.dbedwars.api.util.EnumUtil;
import org.zibble.dbedwars.api.util.properies.NamedProperties;
import org.zibble.dbedwars.database.data.io.DataReader;
import org.zibble.dbedwars.database.data.io.DataWriter;

import java.time.Instant;
import java.util.*;

public class ArenaHistory implements DataCache {

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
        this.id = reader.readString("id");
        this.gameId = reader.readString("game_id");
        this.teams = reader.readMultiMap("teams", s -> EnumUtil.matchEnum(s, Color.VALUES), s -> {
            try {
                return reader.readList(s, UUID::fromString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new ArrayList<>();
        });
        this.winner = reader.readEnum("winner", Color.class);
        this.runtime = reader.readDuration("runtime");
        this.timestamp = reader.readInstant("timestamp");
        this.itemPickup = reader.readMap("item_pickup", UUID::fromString, s -> {
            try {
                return reader.readMap(s, str -> XMaterial.matchXMaterial(str).get(), str -> {
                    try {
                        return reader.readInt(str);
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
        this.deaths = reader.readMap("item_pickup", UUID::fromString, s -> {
            try {
                DeathData data = new DeathData();
                data.load(reader);
                return data;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new DeathData();
        });
        this.bedsBroken = reader.readMultiMap("beds_broken", UUID::fromString, s -> {
            try {
                return reader.readList(s, str -> EnumUtil.matchEnum(str, Color.VALUES));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    @Override
    public void save(DataWriter<?> writer) throws Exception {
        writer.writeString("id", this.id);
        writer.writeString("game_id", this.gameId);
        writer.writeMultiMap("teams", this.teams, Enum::name, (k, v) -> {
            try {
                writer.writeList(k, v, UUID::toString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        writer.writeEnum("winner", this.winner);
        writer.writeDuration("runtime", this.runtime);
        writer.writeInstant("timestamp", this.timestamp);
        writer.writeMap("item_pickup", this.itemPickup, UUID::toString, (k, v) -> {
            try {
                writer.writeMap(k, v, XMaterial::name, (k2, v2) -> {
                    try {
                        writer.writeInt(k2, v2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        writer.writeMap("deaths", this.deaths, UUID::toString, (k, v) -> {
            try {
                v.save(writer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        writer.writeMultiMap("beds_broken", this.bedsBroken, UUID::toString, (k, v) -> {
            try {
                writer.writeList(k, v, Color::name);
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
            this.killer = reader.readUUID("killer");
            this.cause = reader.readEnum("cause", DeathCause.class);
        }

        @Override
        public void save(DataWriter<?> writer) throws Exception {
            writer.writeUUID("killer", this.killer);
            writer.writeEnum("cause", this.cause);
        }

        @Override
        public NamedProperties toProperties() {
            return NamedProperties.builder()
                    .add("killer", this.killer)
                    .add("cause", this.cause)
                    .build();
        }

    }

}
