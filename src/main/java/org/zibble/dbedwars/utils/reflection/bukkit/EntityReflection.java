package org.zibble.dbedwars.utils.reflection.bukkit;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.zibble.dbedwars.api.objects.math.BoundingBox;
import org.zibble.dbedwars.api.version.Version;
import org.zibble.dbedwars.utils.reflection.annotation.*;
import org.zibble.dbedwars.utils.reflection.resolver.FieldResolver;
import org.zibble.dbedwars.utils.reflection.resolver.wrapper.ClassWrapper;
import org.zibble.dbedwars.utils.reflection.resolver.wrapper.ConstructorWrapper;
import org.zibble.dbedwars.utils.reflection.resolver.wrapper.FieldWrapper;
import org.zibble.dbedwars.utils.reflection.resolver.wrapper.MethodWrapper;

import java.util.concurrent.atomic.AtomicInteger;

public class EntityReflection {

    @ClassRef({"{nms}.Entity", "net.minecraft.world.entity.Entity"})
    public static final ClassWrapper<?> NMS_ENTITY_CLASS = null;
    @ClassRef({"{nms}.EntityLiving", "net.minecraft.world.entity.EntityLiving"})
    public static final ClassWrapper<?> ENTITY_LIVING = null;
    @ClassRef({"{nms}.NBTTagCompound", "net.minecraft.nbt.NBTTagCompound"})
    public static final ClassWrapper<?> NBT_TAG_COMPOUND = null;
    @ClassRef({"{nms}.DamageSource", "net.minecraft.world.damagesource.DamageSource"})
    public static final ClassWrapper<?> DAMAGE_SOURCE = null;
    @ConstructorRef(className = "@Class(NBT_TAG_COMPOUND)", parameters = {})
    public static final ConstructorWrapper<?> NBT_TAG_COMPOUND_CONSTRUCTOR = null;
    @MethodRef(className = "@Class(NMS_ENTITY_CLASS)", value = "getBoundingBox()")
    public static final MethodWrapper<?> NMS_ENTITY_GET_BOUNDING_BOX = null;
    @MethodRef(className = "@Class(NMS_ENTITY_CLASS)", value = "getHeadHeight()")
    public static final MethodWrapper<Float> NMS_ENTITY_GET_HEAD_HEIGHT = null;
    @MethodRef(className = "@Class(NMS_ENTITY_CLASS)", value = {"isSilent()", "R()", "ad()"})
    public static final MethodWrapper<Boolean> NMS_ENTITY_IS_SILENT = null;
    @MethodRef(className = "@Class(NMS_ENTITY_CLASS)", value = {"setSilent(boolean)", "b(boolean)"})
    public static final MethodWrapper<?> NMS_ENTITY_SET_SILENT = null;
    @MethodRef(className = "@Class(NMS_ENTITY_CLASS)", value = "isInvulnerable(@Class(DAMAGE_SOURCE))")
    public static final MethodWrapper<Boolean> NMS_ENTITY_IS_INVULNERABLE = null;
    @MethodRef(className = "@Class(NBT_TAG_COMPOUND)", value = "setInt(java.lang.String, int)")
    public static final MethodWrapper<?> NBT_TAG_COMPOUND_SET_INT = null;
    @MethodRef(className = "@Class(ENTITY_LIVING)", value = "c(@Class(NBT_TAG_COMPOUND))")
    public static final MethodWrapper<?> ENTITY_LIVING_C = null;
    @MethodRef(className = "@Class(ENTITY_LIVING)", value = "f(@Class(NBT_TAG_COMPOUND))")
    public static final MethodWrapper<?> ENTITY_LIVING_F = null;
    @FieldRef(className = "@Class(NMS_ENTITY_CLASS)", value = "boolean invulnerable")
    public static final FieldWrapper NMS_ENTITY_INVULNERABLE = null;
    @FieldRef(className = "@Class(DAMAGE_SOURCE)", value = {"GENERIC", "n"})
    public static final FieldWrapper DAMAGE_SOURCE_GENERIC = null;
    public static FieldWrapper ENTITY_COUNTER_FIELD;

    static {
        ReflectionAnnotations.INSTANCE.load(EntityReflection.class);

        FieldResolver resolver = new FieldResolver(EntityReflection.NMS_ENTITY_CLASS.getClazz());
        ENTITY_COUNTER_FIELD = resolver.resolveWrapper("entityCount");
        if (!ENTITY_COUNTER_FIELD.exists()) {
            ENTITY_COUNTER_FIELD = resolver.resolveByFirstExtendingTypeAccessor(AtomicInteger.class);
        }
    }

    public static int getFreeEntityId() {
        Object entityCount = ENTITY_COUNTER_FIELD.get(null);
        if (entityCount instanceof AtomicInteger) {
            return ((AtomicInteger) entityCount).incrementAndGet();
        } else {
            ENTITY_COUNTER_FIELD.set(null, (int) entityCount + 1);
        }
        return (int) entityCount;
    }

    public static BoundingBox getBoundingBox(Entity entity, float height) {
        final Object handle = BukkitReflection.getHandle(entity);
        final Object nms_bb = NMS_ENTITY_GET_BOUNDING_BOX.invoke(handle);

        int i = 0;

        if (Version.SERVER_VERSION.isNewerEquals(Version.v1_17_R1)) i = 1;

        FieldResolver resolver = new FieldResolver(nms_bb.getClass());
        final double min_x = resolver.resolveIndexWrapper(i++).get(nms_bb);
        final double min_y = (double) resolver.resolveIndexWrapper(i++).get(nms_bb) - height;
        final double min_z = resolver.resolveIndexWrapper(i++).get(nms_bb);

        final double max_x = resolver.resolveIndexWrapper(i++).get(nms_bb);
        final double max_y = (double) resolver.resolveIndexWrapper(i++).get(nms_bb) - height;
        final double max_z = resolver.resolveIndexWrapper(i++).get(nms_bb);

        return new BoundingBox(new Vector(min_x, min_y, min_z), new Vector(max_x, max_y, max_z));
    }

    public static BoundingBox getBoundingBox(Entity entity) {
        final Object handle = BukkitReflection.getHandle(entity);
        final float head_height = NMS_ENTITY_GET_HEAD_HEIGHT.invoke(handle);

        return EntityReflection.getBoundingBox(entity, head_height);
    }

    public static void setAI(LivingEntity entity, boolean ai) {
        if (Version.SERVER_VERSION.isOlder(Version.v1_9_R2)) {

            Object handle = BukkitReflection.getHandle(entity);
            Object nbt = NBT_TAG_COMPOUND_CONSTRUCTOR.newInstance();

            ENTITY_LIVING_C.invoke(handle, nbt);
            NBT_TAG_COMPOUND_SET_INT.invoke(nbt, "NoAI", (ai ? 0 : 1));
            ENTITY_LIVING_F.invoke(handle, nbt);
        } else {
            entity.setAI(ai);
        }
    }

    public static void setCollidable(LivingEntity entity, boolean collidable) {
        if (Version.SERVER_VERSION.isNewerEquals(Version.v1_9_R2)) {
            entity.setCollidable(collidable);
        }
    }

    public static boolean isSilent(Entity entity) {
        Object handle = BukkitReflection.getHandle(entity);
        return NMS_ENTITY_IS_SILENT.invoke(handle);
    }

    public static void setSilent(Entity entity, boolean silent) {
        Object handle = BukkitReflection.getHandle(entity);
        NMS_ENTITY_SET_SILENT.invoke(handle, silent);
    }

    public static boolean isInvulnerable(Entity entity) {
        if (Version.SERVER_VERSION.isOlderEquals(Version.v1_9_R2)) {
            Object handle = BukkitReflection.getHandle(entity);
            Object generic_damage = DAMAGE_SOURCE_GENERIC.get(null);

            return NMS_ENTITY_IS_INVULNERABLE.invoke(handle, generic_damage);
        } else {
            return entity.isInvulnerable();
        }
    }

    public static void setInvulnerable(Entity entity, boolean invulnerable) {
        if (Version.SERVER_VERSION.isOlderEquals(Version.v1_9_R2)) {
            Object handle = BukkitReflection.getHandle(entity);
            NMS_ENTITY_INVULNERABLE.set(handle, invulnerable);
        } else {
            entity.setInvulnerable(invulnerable);
        }
    }

}
