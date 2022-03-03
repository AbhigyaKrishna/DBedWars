package org.zibble.dbedwars.utils.reflection.bukkit;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.version.Version;
import org.zibble.dbedwars.utils.reflection.annotation.ClassRef;
import org.zibble.dbedwars.utils.reflection.annotation.FieldRef;
import org.zibble.dbedwars.utils.reflection.annotation.MethodRef;
import org.zibble.dbedwars.utils.reflection.annotation.ReflectionAnnotations;
import org.zibble.dbedwars.utils.reflection.general.MethodReflection;
import org.zibble.dbedwars.utils.reflection.resolver.wrapper.ClassWrapper;
import org.zibble.dbedwars.utils.reflection.resolver.wrapper.FieldWrapper;
import org.zibble.dbedwars.utils.reflection.resolver.wrapper.MethodWrapper;

import java.lang.reflect.InvocationTargetException;

public class BukkitReflection {

    @ClassRef({"{nms}.MinecraftServer", "net.minecraft.server.MinecraftServer"})
    public static final ClassWrapper<?> MINECRAFT_SERVER = null;
    @ClassRef({"{nms}.PlayerList", "net.minecraft.server.players.PlayerList"})
    public static final ClassWrapper<?> PLAYER_LIST = null;
    @ClassRef({"{nms}.DimensionManager", "net.minecraft.world.level.dimension.DimensionManager"})
    public static final ClassWrapper<?> DIMENSION_MANAGER = null;
    @ClassRef("{obc}.CraftWorld")
    public static final ClassWrapper<?> CRAFT_WORLD = null;
    @ClassRef({"{nms}.World", "net.minecraft.world.level.World"})
    public static final ClassWrapper<?> NMS_WORLD = null;
    @ClassRef({"{nms}.ResourceKey", "net.minecraft.resources.ResourceKey"})
    public static final ClassWrapper<?> RESOURCE_KEY = null;
    @MethodRef(className = "@Class(MINECRAFT_SERVER)", value = "getServer()")
    public static final MethodWrapper<?> MINECRAFT_SERVER_GET_SERVER = null;
    @MethodRef(className = "@Class(MINECRAFT_SERVER)", value = "setMotd(java.lang.String)")
    public static final MethodWrapper<?> MINECRAFT_SERVER_SET_MOTD = null;
    @MethodRef(className = "@Class(MINECRAFT_SERVER)", value = "getPlayerList()")
    public static final MethodWrapper<?> MINECRAFT_SERVER_GET_PLAYER_LIST = null;
    @MethodRef(className = "@Class(PLAYER_LIST)", value = "sendPacketNearby")
    public static final MethodWrapper<?> SEND_PACKET_NEARBY = null;
    @FieldRef(className = "@Class(CRAFT_WORLD)", value = "worldBorder")
    public static final FieldWrapper WORLD_BORDER_FIELD = null;
    @FieldRef(className = "@Class(CRAFT_WORLD)", value = "world")
    public static final FieldWrapper WORLD_WORLD_SERVER = null;

    static {
        ReflectionAnnotations.INSTANCE.load(BukkitReflection.class);
    }

    public static Object getHandle(Object object) {
        try {
            return object.getClass().getMethod("getHandle").invoke(object);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            throw new UnsupportedOperationException(
                    "cannot get the handle of the provided object!");
        }
    }

    public static void sendPacketNearby(Player player, double x, double y, double z, double range, World world, Object packet) {
        Object handle = null;
        if (player != null) {
            handle = PlayerReflection.getHandle(player);
        }
        Object minecraft_server = MINECRAFT_SERVER_GET_SERVER.invoke(null);
        Object player_list = MINECRAFT_SERVER_GET_PLAYER_LIST.invoke(minecraft_server);

        Object world_server = WORLD_WORLD_SERVER.get(world);

        Object dimension;
        try {
            if (Version.SERVER_VERSION.isNewerEquals(Version.v1_16_R1)) {
                dimension = MethodReflection.get(NMS_WORLD.getClazz(), "getDimensionKey").invoke(world_server);
            } else if (Version.SERVER_VERSION.isNewerEquals(Version.v1_13_R2)) {
                Object world_provider = MethodReflection.get(NMS_WORLD.getClazz(), "getWorldProvider").invoke(world_server);
                dimension = MethodReflection.get(world_provider.getClass(), "getDimensionManager").invoke(world_provider);
            } else {
                dimension = world_server.getClass().getField("dimension").getInt(world_server);
            }

            SEND_PACKET_NEARBY.invoke(player_list, handle, x, y, z, range, dimension, packet);
        } catch (NoSuchFieldException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void setMotd(String motd) {
        Object server = MINECRAFT_SERVER_GET_SERVER.invokeSilent(null);
        MINECRAFT_SERVER_SET_MOTD.invokeSilent(server, motd);
    }

    public static void clearBorder(World world) {
        world.getWorldBorder().reset();
        WORLD_BORDER_FIELD.set(world, null);
    }

}
