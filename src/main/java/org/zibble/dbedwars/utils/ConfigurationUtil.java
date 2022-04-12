package org.zibble.dbedwars.utils;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.future.ActionFuture;
import org.zibble.dbedwars.api.game.spawner.DropInfo;
import org.zibble.dbedwars.api.game.trap.Target;
import org.zibble.dbedwars.api.hooks.hologram.Hologram;
import org.zibble.dbedwars.api.hooks.hologram.HologramEntityType;
import org.zibble.dbedwars.api.hooks.hologram.HologramPage;
import org.zibble.dbedwars.api.hooks.npc.BedwarsNPC;
import org.zibble.dbedwars.api.hooks.npc.PlayerNPC;
import org.zibble.dbedwars.api.hooks.scoreboard.ScoreboardData;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.messaging.placeholders.Placeholder;
import org.zibble.dbedwars.api.objects.hologram.AnimatedHologramModel;
import org.zibble.dbedwars.api.objects.hologram.HologramModel;
import org.zibble.dbedwars.api.objects.hologram.HologramRotateTask;
import org.zibble.dbedwars.api.objects.profile.Property;
import org.zibble.dbedwars.api.objects.profile.Skin;
import org.zibble.dbedwars.api.objects.serializable.BwItemStack;
import org.zibble.dbedwars.api.objects.serializable.Duration;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZ;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZYP;
import org.zibble.dbedwars.api.script.ScriptVariable;
import org.zibble.dbedwars.api.util.*;
import org.zibble.dbedwars.api.util.json.JSONBuilder;
import org.zibble.dbedwars.api.util.json.Json;
import org.zibble.dbedwars.configuration.ConfigMessage;
import org.zibble.dbedwars.configuration.configurable.ConfigurableHologram;
import org.zibble.dbedwars.configuration.configurable.ConfigurableNpc;
import org.zibble.dbedwars.configuration.configurable.ConfigurableScoreboard;
import org.zibble.dbedwars.configuration.configurable.ConfigurableTrap;
import org.zibble.dbedwars.configuration.language.ConfigLang;
import org.zibble.dbedwars.configuration.translator.LegacyTranslator;
import org.zibble.dbedwars.configuration.translator.MiniMessageTranslator;
import org.zibble.dbedwars.game.arena.ArenaPlayerImpl;
import org.zibble.dbedwars.game.arena.traps.TrapImpl;
import org.zibble.dbedwars.game.arena.traps.TriggerType;
import org.zibble.dbedwars.game.arena.view.ShopViewImpl;
import org.zibble.dbedwars.io.GameProfileFetcher;
import org.zibble.dbedwars.io.MineSkinAPI;
import org.zibble.dbedwars.io.UUIDFetcher;
import org.zibble.dbedwars.io.UUIDTypeAdaptor;
import org.zibble.dbedwars.script.action.ActionPreProcessor;
import org.zibble.dbedwars.task.implementations.HologramBabyRotateTask;
import org.zibble.dbedwars.task.implementations.HologramExpertRotateTask;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigurationUtil {

    private static final Pattern HEAD_PATTERN = Pattern.compile("\\[HEAD=(?<head>\\d+)]", Pattern.CASE_INSENSITIVE);
    private static final Pattern SMALL_HEAD_PATTERN = Pattern.compile("\\[SMALL_HEAD=(?<smallHead>\\d+)]", Pattern.CASE_INSENSITIVE);
    private static final Pattern ICON_PATTERN = Pattern.compile("\\[ICON=(?<icon>\\d+)]", Pattern.CASE_INSENSITIVE);
    private static final Pattern ENTITY_PATTERN = Pattern.compile("\\[ENTITY=(?<entity>\\d+)]", Pattern.CASE_INSENSITIVE);
    private static final Pattern FRAME_PATTERN = Pattern.compile("^\\[(?<ticks>\\d+)] ?+(?<loc>.+?)$", Pattern.CASE_INSENSITIVE);

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

    public static String getConfigCode(Color color) {
        if (ConfigLang.getTranslator() instanceof MiniMessageTranslator) {
            return color.getMiniCode();
        } else if (ConfigLang.getTranslator() instanceof LegacyTranslator) {
            return "" + ((LegacyTranslator) ConfigLang.getTranslator()).getCHAR() + color.getChatColor().getChar();
        }
        return "";
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
                ShopViewImpl shop = ((ArenaPlayerImpl) arenaPlayer).getShop(config.getShop());
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

    public static ScoreboardData createScoreboard(ConfigurableScoreboard config) {
        List<Message> lines = new ArrayList<>(config.getContent().size());
        for (List<String> list : config.getContent()) {
            lines.add(ConfigMessage.from(list));
        }
        return DBedwars.getInstance().getHookManager().getScoreboardHook().createScoreboardData(ScoreboardData.Type.DYNAMIC, ConfigMessage.from(config.getTitle()), lines, Duration.ofTicks(config.getUpdateTick()));
    }

    public static AnimatedHologramModel createHologram(ConfigurableHologram.HologramConfig config, Placeholder... placeholders) {
        return new AnimatedHologramModel(createLines(config.getContent(), placeholders),
                Duration.ofTicks(config.getUpdateDelay()),
                config.getAnimationEndTask(),
                (hologram, model) -> createHologramTask(hologram, model, config));

    }

    private static HologramRotateTask createHologramTask(Hologram hologram, AnimatedHologramModel model, ConfigurableHologram.HologramConfig config) {
        HologramRotateTask task;
        if (config instanceof ConfigurableHologram.ConfigurableBabyHologram) {
            ConfigurableHologram.ConfigurableBabyHologram baby = (ConfigurableHologram.ConfigurableBabyHologram) config;
            task = new HologramBabyRotateTask(DBedwars.getInstance(),
                    hologram,
                    model.getEndAction(),
                    baby.getDegreeRotatedPerCycle(),
                    baby.getTicksPerAnimationCycle(),
                    baby.isSlowAtEndEnabled(),
                    baby.getVerticalDisplacement());
        } else {
            ConfigurableHologram.ConfigurableAdvancedHologram advanced = (ConfigurableHologram.ConfigurableAdvancedHologram) config;
            LinkedHashMap<LocationXYZYP, Integer> frames = new LinkedHashMap<>();
            for (String frame : advanced.getFrames()) {
                Matcher matcher = FRAME_PATTERN.matcher(frame);
                if (matcher.matches()) {
                    LocationXYZYP location = LocationXYZYP.valueOf(matcher.group("loc"));
                    int delay = NumberUtils.toInt(matcher.group("ticks"));
                    frames.put(location, delay);
                }
            }
            task = new HologramExpertRotateTask(DBedwars.getInstance(),
                    hologram,
                    model.getEndAction(),
                    frames,
                    advanced.getFrameTickDelay());

        }
        return task;
    }

    private static List<HologramModel.ModelLine> createLines(List<String> content, Placeholder... placeholders) {
        List<HologramModel.ModelLine> lines = new ArrayList<>(content.size());
        for (String s : content) {
            Matcher matcher = HEAD_PATTERN.matcher(s);
            if (matcher.matches()) {
                String head = matcher.group("head");
                BwItemStack itemStack = BwItemStack.valueOf(head, placeholders);
                if (itemStack != null) {
                    lines.add(HologramModel.ModelLine.ofHead(itemStack));
                    continue;
                }
            }

            matcher = SMALL_HEAD_PATTERN.matcher(s);
            if (matcher.matches()) {
                String smallHead = matcher.group("smallHead");
                BwItemStack itemStack = BwItemStack.valueOf(smallHead, placeholders);
                if (itemStack != null) {
                    lines.add(HologramModel.ModelLine.ofSmallHead(itemStack));
                    continue;
                }
            }

            matcher = ICON_PATTERN.matcher(s);
            if (matcher.matches()) {
                String icon = matcher.group("icon");
                BwItemStack itemStack = BwItemStack.valueOf(icon, placeholders);
                if (itemStack != null) {
                    lines.add(HologramModel.ModelLine.ofItem(itemStack));
                    continue;
                }
            }

            matcher = ENTITY_PATTERN.matcher(s);
            if (matcher.matches()) {
                String entity = matcher.group("entity");
                //TODO REWORK GETTING ENTITY TYPE
                HologramEntityType mapped = EnumUtil.matchEnum(entity, HologramEntityType.values());
                if (mapped != null) {
                    lines.add(HologramModel.ModelLine.ofEntity(mapped));
                    continue;
                }
            }

            lines.add(HologramModel.ModelLine.ofText(ConfigMessage.from(s, placeholders)));
        }

        return lines;
    }

    public static TrapImpl createTrap(ConfigurableTrap config, ArenaPlayerImpl buyer) {
        TriggerType triggerType = EnumUtil.matchEnum(config.getTrigger(), TriggerType.values());
        if (triggerType == null)
            return null;
        TrapImpl trap = new TrapImpl(config.getId(), buyer, triggerType);
        for (ConfigurableTrap.ConfigurableTrapAction cfg : config.getTrapActions()) {
            Target target = DBedwars.getInstance().getGameManager().getTargetRegistry().get(cfg.getTarget());
            if (target == null) continue;
            for (String executable : cfg.getExecutables()) {
                TrapImpl.TrapActionImpl action = trap.new TrapActionImpl(target, executable);
                trap.addAction(action);
            }
        }
        return trap;
    }

}
