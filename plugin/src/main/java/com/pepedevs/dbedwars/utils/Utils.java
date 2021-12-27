package com.pepedevs.dbedwars.utils;

import com.pepedevs.corelib.utils.math.LocationUtils;
import com.pepedevs.corelib.utils.reflection.accessor.FieldAccessor;
import com.pepedevs.corelib.utils.reflection.bukkit.BukkitReflection;
import com.pepedevs.corelib.utils.reflection.general.ClassReflection;
import com.pepedevs.corelib.utils.reflection.general.MethodReflection;
import com.pepedevs.corelib.utils.reflection.resolver.FieldResolver;
import com.pepedevs.corelib.utils.reflection.resolver.MethodResolver;
import com.pepedevs.corelib.utils.reflection.resolver.ResolverQuery;
import com.pepedevs.corelib.utils.reflection.resolver.minecraft.CraftClassResolver;
import com.pepedevs.corelib.utils.reflection.resolver.minecraft.NMSClassResolver;
import com.pepedevs.corelib.utils.reflection.resolver.wrapper.ClassWrapper;
import com.pepedevs.corelib.utils.reflection.resolver.wrapper.MethodWrapper;
import com.pepedevs.corelib.utils.xseries.XMaterial;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.game.Team;
import com.pepedevs.dbedwars.api.util.BwItemStack;
import com.pepedevs.dbedwars.api.util.NBTUtils;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Utils {

    private static final FieldAccessor fieldFireballDirX;
    private static final FieldAccessor fieldFireballDirY;
    private static final FieldAccessor fieldFireballDirZ;

    private static final MethodWrapper CRAFT_ITEM_STACK_AS_NMS_COPY;
    private static final MethodWrapper NMS_ITEM_STACK_GET_ITEM;
    private static final MethodWrapper NMS_ITEM_C;
    private static final MethodWrapper CRAFT_ITEM_STACK_AS_BUKKIT_COPY;

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

    public static void setSpawnInventory(Player player, Team team) {
        BwItemStack helmet = new BwItemStack(XMaterial.LEATHER_HELMET.parseMaterial());
        BwItemStack chestPlate = new BwItemStack(XMaterial.LEATHER_CHESTPLATE.parseMaterial());
        BwItemStack leggings = new BwItemStack(XMaterial.LEATHER_LEGGINGS.parseMaterial());
        BwItemStack boots = new BwItemStack(XMaterial.LEATHER_BOOTS.parseMaterial());

        // Helmet
        LeatherArmorMeta helmetMeta = (LeatherArmorMeta) helmet.getItemMeta();
        helmetMeta.setColor(team.getColor().getColor());
        helmet.setItemMeta(helmetMeta);

        // ChestPlate
        LeatherArmorMeta chestPlateMeta = (LeatherArmorMeta) chestPlate.getItemMeta();
        chestPlateMeta.setColor(team.getColor().getColor());
        chestPlate.setItemMeta(chestPlateMeta);

        // Leggings
        LeatherArmorMeta leggingsMeta = (LeatherArmorMeta) leggings.getItemMeta();
        leggingsMeta.setColor(team.getColor().getColor());
        leggings.setItemMeta(leggingsMeta);

        // Boots
        LeatherArmorMeta bootsMeta = (LeatherArmorMeta) boots.getItemMeta();
        bootsMeta.setColor(team.getColor().getColor());
        boots.setItemMeta(bootsMeta);

        player.getInventory().setHelmet(helmet.toItemStack());
        player.getInventory().setChestplate(chestPlate.toItemStack());
        player.getInventory().setLeggings(leggings.toItemStack());
        player.getInventory().setBoots(boots.toItemStack());

        player.getInventory()
                .setItem(0, new BwItemStack(XMaterial.WOODEN_SWORD.parseItem()).toItemStack());
    }

    public static Block findBed(Location location, byte x, byte y, byte z) {
        Location corner = location.clone().add(x, y, z);
        Location corner2 = location.clone().subtract(x, y, z);
        Set<Block> blocks = LocationUtils.getBlocksBetween(corner, corner2);

        return blocks.stream().filter(Utils::isBed).findFirst().orElse(null);
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

    public static boolean containsGlass(List<Material> materials) {
        List<Material> list = new ArrayList<>(materials);
        list.removeIf(material -> !material.name().contains("GLASS"));
        return list.size() >= 1;
    }

    public static boolean containsGlassOrEndstone(List<Material> materials) {
        if (containsGlass(materials)) return true;
        List<Material> list = new ArrayList<>(materials);
        materials.removeIf(material -> XMaterial.matchXMaterial(material) != XMaterial.END_STONE);
        return list.size() >= 1;
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
        return (player.getKills() * player.getArena().getSettings().getKillPoint())
                + (player.getFinalKills() * player.getArena().getSettings().getFinalKillPoint())
                + (player.getDeath() * player.getArena().getSettings().getDeathPoint())
                + (player.getBedDestroy() * player.getArena().getSettings().getBedDestroyPoint());
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

        fieldFireballDirX = new FieldResolver(ENTITY_FIREBALL_CLASS.getClazz()).resolveAccessor("dirX", "b");
        fieldFireballDirY = new FieldResolver(ENTITY_FIREBALL_CLASS.getClazz()).resolveAccessor("dirY", "c");
        fieldFireballDirZ = new FieldResolver(ENTITY_FIREBALL_CLASS.getClazz()).resolveAccessor("dirZ", "d");
    }
}
