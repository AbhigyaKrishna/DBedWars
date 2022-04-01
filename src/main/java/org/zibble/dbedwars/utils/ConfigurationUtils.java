package org.zibble.dbedwars.utils;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.future.ActionFuture;
import org.zibble.dbedwars.api.game.spawner.DropInfo;
import org.zibble.dbedwars.api.hooks.hologram.HologramPage;
import org.zibble.dbedwars.api.hooks.npc.BedwarsNPC;
import org.zibble.dbedwars.api.hooks.npc.PlayerNPC;
import org.zibble.dbedwars.api.hooks.scoreboard.ScoreboardData;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.objects.profile.Property;
import org.zibble.dbedwars.api.objects.profile.Skin;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZ;
import org.zibble.dbedwars.api.script.ScriptVariable;
import org.zibble.dbedwars.api.util.*;
import org.zibble.dbedwars.api.util.json.JSONBuilder;
import org.zibble.dbedwars.api.util.json.Json;
import org.zibble.dbedwars.configuration.ConfigMessage;
import org.zibble.dbedwars.configuration.configurable.ConfigurableNpc;
import org.zibble.dbedwars.configuration.configurable.ConfigurableScoreboard;
import org.zibble.dbedwars.game.arena.ArenaPlayerImpl;
import org.zibble.dbedwars.game.arena.view.ShopView;
import org.zibble.dbedwars.io.GameProfileFetcher;
import org.zibble.dbedwars.io.MineSkinAPI;
import org.zibble.dbedwars.io.UUIDFetcher;
import org.zibble.dbedwars.io.UUIDTypeAdaptor;
import org.zibble.dbedwars.script.action.ActionPreProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class ConfigurationUtils {

    public static String serializeSpawner(DropInfo drop, LocationXYZ location) {
        return drop.getKey().get() + "#" + location.toString();
    }

    public static String[][] parseGuiPattern(List<String> lines) {
        String[][] pattern = new String[Math.min(lines.size(), 6)][9];
        for (byte i = 0; i < lines.size(); i++) {
            pattern[i] = lines.get(i).split(" ");
        }
        return pattern;
    }

    public static Pair<DropInfo, LocationXYZ> parseSpawner(String s) {
        String[] str = s.split("#");
        for (DropInfo d : DBedwars.getInstance().getGameManager().getDropTypes()) {
            if (d.getKey().get().equals(str[0]))
                return Pair.of(d, LocationXYZ.valueOf(str[1]));
        }

        return null;
    }

    public static BwItemStack parseItem(Color color, String s) {
        String replace = color == null ? "" : String.valueOf(color.getData());
        s = s.replace("%team%", replace);
        s = s.replace("STAINED_GLASS", "GLASS");
        s = s.replace("GLASS", "STAINED_GLASS");
        return BwItemStack.valueOf(s);
    }

    public static Json createJson(ConfigurationSection section) {
        JSONBuilder builder = new JSONBuilder();
        for (String key : section.getKeys(false)) {
            if (section.isConfigurationSection(key)) {
                builder.add(key, createJson(section.getConfigurationSection(key)).getHandle());
            } else {
                builder.add(key, section.get(key));
            }
        }

        return builder.toJson();
    }

    public static BedwarsNPC createNpc(Location location, ConfigurableNpc config) {
        BedwarsNPC npc;
        if (config.getType().equalsIgnoreCase("player")) {
            npc = DBedwars.getInstance().getHookManager().getNpcFactory().createPlayerNPC(location);
            if (config.getTexture() != null) {
                ConfigurableNpc.ConfigurableTexture texture = config.getTexture();
                ActionFuture<Skin> skin = null;
                if (texture.getOwner() != null) {
                    if (texture.getOwner().length() == 32) {
                        skin = ActionFuture.supplyAsync(() -> {
                            Skin s = null;
                            for (Property property : GameProfileFetcher.getInstance().fetch(UUIDTypeAdaptor.fromString(texture.getOwner())).getProperties()) {
                                if (property.getName().equals("textures")) {
                                    s = Skin.from(property.getValue(), property.getSignature());
                                    break;
                                }
                            }
                            return s;
                        });
                    } else if (texture.getOwner().length() == 36) {
                        skin = ActionFuture.supplyAsync(() -> {
                            Skin s = null;
                            for (Property property : GameProfileFetcher.getInstance().fetch(UUID.fromString(texture.getOwner())).getProperties()) {
                                if (property.getName().equals("textures")) {
                                    s = Skin.from(property.getValue(), property.getSignature());
                                    break;
                                }
                            }
                            return s;
                        });
                    } else {
                        skin = ActionFuture.supplyAsync(() -> {
                            Skin s = null;
                            for (Property property : GameProfileFetcher.getInstance().fetch(UUIDFetcher.getInstance().getUUID(texture.getOwner())).getProperties()) {
                                if (property.getName().equals("textures")) {
                                    s = Skin.from(property.getValue(), property.getSignature());
                                    break;
                                }
                            }
                            return s;
                        });
                    }
                } else if (texture.getValue() != null) {
                    skin = ActionFuture.supplyAsync(() -> Skin.from(texture.getValue(), texture.getSignature()));
                } else if (texture.getMineskin() != null) {
                    skin = ActionFuture.supplyAsync(() -> MineSkinAPI.getInstance().getSkin(texture.getMineskin()));
                }

                if (skin != null) {
                    ((PlayerNPC) npc).setSkin(skin.thenApply(s -> s == null ? Skin.empty() : s));
                }
            }
        } else {
            EntityType type = EnumUtil.matchEnum(config.getType(), EntityType.values());
            if (type == null) {
                throw new IllegalArgumentException("No match for npc type " + config.getType() + " found!");
            }
            npc = DBedwars.getInstance().getHookManager().getNpcFactory().createEntityNPC(location, type);
        }

        HologramPage page = npc.getNameHologram().getHologramPages().get(0);
        for (ConfigMessage message : ConfigMessage.from(config.getName()).splitToLineMessage()) {
            page.addNewTextLine(message);
        }

        if (config.getShop() != null) {
            npc.addClickAction((player, clickType) -> DBedwars.getInstance().getGameManager().getArenaPlayer(player).ifPresent(arenaPlayer -> {
                ShopView shop = ((ArenaPlayerImpl) arenaPlayer).getShop(config.getShop());
                if (shop != null) {
                    shop.getGui().open(shop.getDefaultPage().getKey());
                }
            }));
        }

        if (config.getActions() != null && !config.getActions().isEmpty()) {
            npc.addClickAction((player, clickType) -> {
                for (String action : config.getActions()) {
                    ActionPreProcessor.process(action, ScriptVariable.of("player", player),
                            ScriptVariable.of("clicktype", clickType)).execute();
                }
            });
        }

        return npc;
    }

    public ScoreboardData createScoreboard(ConfigurableScoreboard config) {
        List<Message> lines = new ArrayList<>(config.getContent().size());
        for (List<String> list : config.getContent()) {
            lines.add(ConfigMessage.from(list));
        }
        return DBedwars.getInstance().getHookManager().getScoreboardHook().createScoreboardData(ScoreboardData.Type.DYNAMIC, ConfigMessage.from(config.getTitle()), lines, Duration.ofTicks(config.getUpdateTick()));
    }
}
