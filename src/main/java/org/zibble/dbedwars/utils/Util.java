package org.zibble.dbedwars.utils;

import com.cryptomorin.xseries.XMaterial;
import com.github.retrooper.packetevents.protocol.player.TextureProperty;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.pepedevs.radium.adventure.MiniMessageUtils;
import de.tr7zw.changeme.nbtapi.NBTItem;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.util.Vector;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.game.DeathCause;
import org.zibble.dbedwars.api.messaging.message.LegacyMessage;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.objects.profile.PlayerGameProfile;
import org.zibble.dbedwars.api.objects.profile.Property;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.api.util.NBTUtils;
import org.zibble.dbedwars.configuration.ConfigMessage;
import org.zibble.dbedwars.configuration.language.ConfigLang;
import org.zibble.dbedwars.configuration.translator.LegacyTranslator;
import org.zibble.dbedwars.configuration.translator.MiniMessageTranslator;
import org.zibble.dbedwars.messaging.Messaging;
import org.zibble.dbedwars.utils.reflection.bukkit.BukkitReflection;
import org.zibble.dbedwars.utils.reflection.resolver.FieldResolver;
import org.zibble.dbedwars.utils.reflection.resolver.MethodResolver;
import org.zibble.dbedwars.utils.reflection.resolver.ResolverQuery;
import org.zibble.dbedwars.utils.reflection.resolver.minecraft.CraftClassResolver;
import org.zibble.dbedwars.utils.reflection.resolver.minecraft.NMSClassResolver;
import org.zibble.dbedwars.utils.reflection.resolver.wrapper.ClassWrapper;
import org.zibble.dbedwars.utils.reflection.resolver.wrapper.FieldWrapper;
import org.zibble.dbedwars.utils.reflection.resolver.wrapper.MethodWrapper;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

public class Util {

    private static final FieldWrapper fieldFireballDirX;
    private static final FieldWrapper fieldFireballDirY;
    private static final FieldWrapper fieldFireballDirZ;

    private static final MethodWrapper<?> CRAFT_ITEM_STACK_AS_NMS_COPY;
    private static final MethodWrapper<?> NMS_ITEM_STACK_GET_ITEM;
    private static final MethodWrapper<?> NMS_ITEM_C;
    private static final MethodWrapper<?> CRAFT_ITEM_STACK_AS_BUKKIT_COPY;

    public static boolean isUnMergeable(ItemStack item) {
        return NBTUtils.hasNBTData(item, "unmerge") && new NBTItem(item).getBoolean("unmerge");
    }

    public static ItemStack setMaxStackSize(ItemStack stack, int stackSize) {
        Object nmsItemStack =
                CRAFT_ITEM_STACK_AS_NMS_COPY.invoke(null, stack);
        Object item = NMS_ITEM_STACK_GET_ITEM.invoke(nmsItemStack);
        NMS_ITEM_C.invoke(item, stackSize);
        return (ItemStack) CRAFT_ITEM_STACK_AS_BUKKIT_COPY.invoke(null, nmsItemStack);
    }

    public static boolean isBed(Block block) {
        return Arrays.asList(ItemConstant.BED.getItems())
                .contains(XMaterial.matchXMaterial(block.getType()))
                || block.getType().name().equals("BED_BLOCK");
    }

    @SafeVarargs
    public static <T> T[] mergeArray(T[]... arrays) {
        List<T> list = new ArrayList<>();
        for (T[] array : arrays) {
            list.addAll(Arrays.asList(array));
        }

        return list.toArray(arrays[0]);
    }

    public static boolean contains(List<Material> materials, XMaterial... toCheck) {
        for (Material material : materials) {
            for (XMaterial xMaterial : toCheck) {
                if (material.equals(xMaterial.parseMaterial())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static <T> boolean anyMatch(List<T> list, Predicate<T> predicate) {
        List<T> temp = new ArrayList<>(list);
        temp.removeIf(predicate);
        return temp.size() >= 1;
    }

    public static LinkedHashMap<ArenaPlayer, Integer> getGameLeaderBoard(
            Collection<ArenaPlayer> players) {
        LinkedHashMap<ArenaPlayer, Integer> map = new LinkedHashMap<>();
        players.stream()
                .sorted(
                        (o1, o2) -> {
                            int p1 = calculatePoint(o1);
                            int p2 = calculatePoint(o2);

                            return Integer.compare(p1, p2) * -1;
                        })
                .forEach(
                        player -> {
                            int point = calculatePoint(player);
                            map.put(player, point);
                        });

        return map;
    }

    public static int calculatePoint(ArenaPlayer player) {
        return (player.getPoints().getCount(ArenaPlayer.PlayerPoints.KILLS).intValue() * player.getArena().getSettings().getKillPoint())
                + (player.getPoints().getCount(ArenaPlayer.PlayerPoints.FINAL_KILLS).intValue() * player.getArena().getSettings().getFinalKillPoint())
                + (player.getPoints().getCount(ArenaPlayer.PlayerPoints.DEATH).intValue() * player.getArena().getSettings().getDeathPoint())
                + (player.getPoints().getCount(ArenaPlayer.PlayerPoints.BEDS).intValue() * player.getArena().getSettings().getBedDestroyPoint());
    }

    public static void useItem(Player player) {
        if (player.getGameMode() == GameMode.CREATIVE) return;

        int amt = player.getInventory().getItemInHand().getAmount();

        if (amt == 1) {
            player.getInventory().setItemInHand(XMaterial.AIR.parseItem());
        } else {
            player.getInventory().getItemInHand().setAmount(--amt);
        }
    }

    public static void setDirection(Fireball fireball, Vector direction) {
        Object handle = BukkitReflection.getHandle(fireball);
        fieldFireballDirX.set(handle, direction.getX() * 0.10D);
        fieldFireballDirY.set(handle, direction.getY() * 0.10D);
        fieldFireballDirZ.set(handle, direction.getZ() * 0.10D);
    }

    public static String getConfigCode(Color color) {
        if (ConfigLang.getTranslator() instanceof MiniMessageTranslator) {
            return color.getMiniCode();
        } else if (ConfigLang.getTranslator() instanceof LegacyTranslator) {
            return "" + ((LegacyTranslator) ConfigLang.getTranslator()).getCHAR() + color.getChatColor().getChar();
        }
        return null;
    }

    public static String getChatColor(Message message, Color color) {
        if (message instanceof ConfigMessage) {
            return getConfigCode(color);
        } else if (message instanceof LegacyMessage) {
            return new String(new char[]{'&', color.getChatColor().getChar()});
        } else {
            return color.getMiniCode();
        }
    }

    public static <T extends Message> T convertMessage(Message message, T newMessage) {
        if (newMessage instanceof ConfigMessage) {
            newMessage.setMessage(ConfigLang.getTranslator().untranslate(message.asComponent()));
        } else if (newMessage instanceof LegacyMessage) {
            String[] msg = new String[message.size()];
            Component[] components = message.asComponent();
            for (int i = 0; i < message.size(); i++) {
                msg[i] = Messaging.getInstance().getLegacySerializer().serialize(components[i]);
            }
            newMessage.setMessage(msg);
        } else {
            newMessage.setMessage(MiniMessageUtils.untranslate(message.asComponent()));
        }
        newMessage.addPlaceholders(message.getPlaceholders());
        return newMessage;
    }

    public static Location getRandomPointAround(final Location centre, final int range, final Predicate<Location> constrain) {
        Vector v = Vector.getRandom().normalize().multiply(ThreadLocalRandom.current().nextDouble(range));
        Location loc = centre.clone().add(v);
        if (constrain.test(loc))
            return loc;
        else
            return Util.getRandomPointAround(centre, range, constrain);
    }

    public static boolean hasMetaData(Entity entity, String key, Object value) {
        if (entity.hasMetadata(key)) {
            DBedwars plugin = DBedwars.getInstance();
            for (MetadataValue v : entity.getMetadata(key)) {
                if (v.getOwningPlugin().equals(plugin) && v.value().equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static UserProfile asProtocolProfile(PlayerGameProfile profile) {
        List<TextureProperty> properties = new ArrayList<>();
        for (Property property : profile.getProperties()) {
            properties.add(new TextureProperty(property.getName(), property.getValue(), property.getSignature()));
        }
        return new UserProfile(profile.getUuid(), profile.getName(), properties);
    }

    public static boolean playerHasItem(Player player, ItemStack item) {
        ItemStack[] items = player.getInventory().getContents();
        int num = Arrays.stream(items)
                        .filter(Objects::nonNull)
                        .filter(i -> i.getType() == item.getType() && i.getDurability() == item.getDurability())
                        .filter(NBTUtils::hasPluginData)
                        .mapToInt(ItemStack::getAmount)
                        .sum();
        return num >= item.getAmount();
    }

    public static void removeItem(Player player, ItemStack item) {
        ItemStack[] items = player.getInventory().getContents();
        int amount = item.getAmount();
        for (byte b = 0; b < items.length; b++) {
            ItemStack itemStack = items[b];
            if (itemStack != null
                    && itemStack.getType() == item.getType()
                    && itemStack.getDurability() == item.getDurability()
                    && NBTUtils.hasPluginData(itemStack)) {
                if (itemStack.getAmount() <= amount) {
                    amount -= itemStack.getAmount();
                    items[b] = null;
                } else {
                    itemStack.setAmount(itemStack.getAmount() - amount);
                    amount = 0;
                }
                if (amount <= 0) break;
            }
        }
        player.getInventory().setContents(items);
    }

    public static int getMissing(Player player, Material material, int amount) {
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack != null
                    && itemStack.getType() == material
                    && NBTUtils.hasPluginData(itemStack)) amount -= itemStack.getAmount();
        }
        return amount;
    }

    public static Message getDeathMessage(DeathCause cause, Player player) {
        switch (cause) {
            case ATTACK:
                return ConfigLang.DEATH_MESSAGE_PLAYER_ATTACK.asMessage();
            case FALL:
                return player != null ? ConfigLang.DEATH_MESSAGE_FALL_BY_PLAYER.asMessage() : ConfigLang.DEATH_MESSAGE_FALL_NO_PLAYER.asMessage();
            case VELOCITY:
                return player != null ? ConfigLang.DEATH_MESSAGE_VELOCITY_BY_PLAYER.asMessage() : ConfigLang.DEATH_MESSAGE_VELOCITY_NO_PLAYER.asMessage();
            case EXPLOSION:
                return player != null ? ConfigLang.DEATH_MESSAGE_EXPLOSION_BY_PLAYER.asMessage() : ConfigLang.DEATH_MESSAGE_EXPLOSION_NO_PLAYER.asMessage();
            case SUFFOCATION:
                return player != null ? ConfigLang.DEATH_MESSAGE_SUFFOCATION_BY_PLAYER.asMessage() : ConfigLang.DEATH_MESSAGE_SUFFOCATION_NO_PLAYER.asMessage();
            case DROWN:
                return player != null ? ConfigLang.DEATH_MESSAGE_DROWN_BY_PLAYER.asMessage() : ConfigLang.DEATH_MESSAGE_DROWN_NO_PLAYER.asMessage();
            case BURNING:
                return player != null ? ConfigLang.DEATH_MESSAGE_BURNING_BY_PLAYER.asMessage() : ConfigLang.DEATH_MESSAGE_BURNING_NO_PLAYER.asMessage();
            case MAGIC:
                return player != null ? ConfigLang.DEATH_MESSAGE_MAGIC_BY_PLAYER.asMessage() : ConfigLang.DEATH_MESSAGE_MAGIC_NO_PLAYER.asMessage();
            case VOID:
                return player != null ? ConfigLang.DEATH_MESSAGE_VOID_BY_PLAYER.asMessage() : ConfigLang.DEATH_MESSAGE_VOID_NO_PLAYER.asMessage();
            case SUICIDE:
                return ConfigLang.DEATH_MESSAGE_SUICIDE.asMessage();
            case ENTITY_CRAMMED:
                return player != null ? ConfigLang.DEATH_MESSAGE_CRAMMING_BY_PLAYER.asMessage() : ConfigLang.DEATH_MESSAGE_CRAMMING_NO_PLAYER.asMessage();
            case CRUSHED:
                return player != null ? ConfigLang.DEATH_MESSAGE_CRUSHED_BY_PLAYER.asMessage() : ConfigLang.DEATH_MESSAGE_CRUSHED_NO_PLAYER.asMessage();
            case UNKNOWN:
            default:
                return ConfigLang.DEATH_MESSAGE_UNKNOWN_REASON.asMessage();
        }
    }

    static {
        NMSClassResolver NMS_CLASS_RESOLVER = new NMSClassResolver();
        CraftClassResolver CRAFT_CLASS_RESOLVER = new CraftClassResolver();

        ClassWrapper<?> ENTITY_FIREBALL_CLASS = NMS_CLASS_RESOLVER.resolveWrapper("EntityFireball", "net.minecraft.world.entity.projectile.EntityFireball");
        ClassWrapper<?> CRAFT_ITEM_STACK = CRAFT_CLASS_RESOLVER.resolveWrapper("inventory.CraftItemStack");
        ClassWrapper<?> NMS_ITEM_STACK = NMS_CLASS_RESOLVER.resolveWrapper("ItemStack", "net.minecraft.world.item.ItemStack");
        ClassWrapper<?> NMS_ITEM = NMS_CLASS_RESOLVER.resolveWrapper("Item", "net.minecraft.world.item.Item");

        CRAFT_ITEM_STACK_AS_NMS_COPY = new MethodResolver(CRAFT_ITEM_STACK.getClazz()).resolveWrapper(ResolverQuery.builder().with("asNMSCopy", ItemStack.class).build());
        NMS_ITEM_STACK_GET_ITEM = new MethodResolver(NMS_ITEM_STACK.getClazz()).resolveWrapper("getItem");
        NMS_ITEM_C = new MethodResolver(NMS_ITEM.getClazz()).resolveWrapper(ResolverQuery.builder().with("c", int.class).build());
        CRAFT_ITEM_STACK_AS_BUKKIT_COPY = new MethodResolver(CRAFT_ITEM_STACK.getClazz()).resolveWrapper(ResolverQuery.builder().with("asBukkitCopy", NMS_ITEM_STACK.getClazz()).build());

        fieldFireballDirX = new FieldResolver(ENTITY_FIREBALL_CLASS.getClazz()).resolveWrapper("dirX", "b");
        fieldFireballDirY = new FieldResolver(ENTITY_FIREBALL_CLASS.getClazz()).resolveWrapper("dirY", "c");
        fieldFireballDirZ = new FieldResolver(ENTITY_FIREBALL_CLASS.getClazz()).resolveWrapper("dirZ", "d");
    }
}
