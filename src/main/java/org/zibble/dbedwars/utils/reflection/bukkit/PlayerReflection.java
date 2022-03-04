package org.zibble.dbedwars.utils.reflection.bukkit;

import org.bukkit.entity.Player;
import org.zibble.dbedwars.utils.reflection.annotation.ClassRef;
import org.zibble.dbedwars.utils.reflection.annotation.FieldRef;
import org.zibble.dbedwars.utils.reflection.annotation.MethodRef;
import org.zibble.dbedwars.utils.reflection.annotation.ReflectionAnnotations;
import org.zibble.dbedwars.utils.reflection.resolver.wrapper.ClassWrapper;
import org.zibble.dbedwars.utils.reflection.resolver.wrapper.FieldWrapper;
import org.zibble.dbedwars.utils.reflection.resolver.wrapper.MethodWrapper;

public class PlayerReflection {

    @ClassRef("{obc}.entity.CraftPlayer")
    public static final ClassWrapper<?> CRAFT_PLAYER_CLASS = null;
    @ClassRef({"{nms}.EntityPlayer", "net.minecraft.server.level.EntityPlayer"})
    public static final ClassWrapper<?> ENTITY_PLAYER_CLASS = null;
    @ClassRef({"{nms}.PlayerConnection", "net.minecraft.server.network.PlayerConnection"})
    public static final ClassWrapper<?> PLAYER_CONNECTION_CLASS = null;
    @ClassRef({"{nms}.NetworkManager", "net.minecraft.network.NetworkManager"})
    public static final ClassWrapper<?> NETWORK_MANAGER_CLASS = null;
    @FieldRef(className = "@var(ENTITY_PLAYER_CLASS)", value = {"@var(PLAYER_CONNECTION_CLASS) playerConnection", "@var(PLAYER_CONNECTION_CLASS) b"})
    public static final FieldWrapper PLAYER_CONNECTION_FIELD = null;
    @FieldRef(className = "@var(PLAYER_CONNECTION_CLASS)", value = {"@var(NETWORK_MANAGER_CLASS) networkManager", "@var(NETWORK_MANAGER_CLASS) a"})
    public static final FieldWrapper NETWORK_MANAGER_FIELD = null;
    @FieldRef(className = "@var(NETWORK_MANAGER_CLASS)", value = {"io.netty.channel.Channel channel", "io.netty.channel.Channel i", "io.netty.channel.Channel k"})
    public static final FieldWrapper CHANNEL_FIELD = null;
    @MethodRef(className = "@var(CRAFT_PLAYER_CLASS)", value = "getHandle()")
    public static final MethodWrapper<?> CRAFT_PLAYER_GET_HANDLE = null;
    @MethodRef(className = "@var(PLAYER_CONNECTION_CLASS)", value = "sendPacket")
    public static final MethodWrapper<?> SEND_PACKET = null;

    static {
        ReflectionAnnotations.INSTANCE.load(PlayerReflection.class);
    }

    public static Object getHandle(Player player) {
        return CRAFT_PLAYER_GET_HANDLE.invoke(player);
    }

    public static Object getPlayerConnection(Player player) {
        return PLAYER_CONNECTION_FIELD.get(PlayerReflection.getHandle(player));
    }

    public static Object getNetworkManager(Player player) {
        return NETWORK_MANAGER_FIELD.get(PlayerReflection.getPlayerConnection(player));
    }

    public static void sendPacket(Player player, Object packet) {
        Object connection = PlayerReflection.getPlayerConnection(player);
        SEND_PACKET.invoke(connection, packet);
    }

}
