package com.pepedevs.dbedwars.utils;

import com.pepedevs.corelib.utils.math.LocationUtils;
import com.pepedevs.corelib.utils.reflection.general.ClassReflection;
import com.pepedevs.corelib.utils.reflection.general.MethodReflection;
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
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Utils {

    public static boolean isUnMergeable(ItemStack item) {
        return NBTUtils.hasNBTData(item, "unmerge") && new NBTItem(item).getBoolean("unmerge");
    }

    public static ItemStack setUnStackable(ItemStack stack) {
        try {
            Class<?> cbItemStack = ClassReflection.getCraftClass("CraftItemStack", "inventory");
            Object nmsItemStack =
                    MethodReflection.get(cbItemStack, "asNMSCopy", ItemStack.class)
                            .invoke(null, stack);
            Object item = MethodReflection.invoke(nmsItemStack, "getItem");
            MethodReflection.invoke(item, "c", 1);
            return (ItemStack)
                    MethodReflection.get(cbItemStack, "asBukkitCopy", nmsItemStack.getClass())
                            .invoke(null, nmsItemStack);

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return stack;
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
}
