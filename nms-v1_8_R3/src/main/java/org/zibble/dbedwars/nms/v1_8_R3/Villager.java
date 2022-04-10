package org.zibble.dbedwars.nms.v1_8_R3;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.zibble.dbedwars.api.nms.IVillager;
import org.zibble.dbedwars.api.nms.NMSAdaptor;

public class Villager extends EntityVillager implements IVillager {

    private final NMSAdaptor adaptor;
    private Location location;

    public Villager(NMSAdaptor adaptor, Location location) {
        super(((CraftWorld) location.getWorld()).getHandle());
        this.adaptor = adaptor;
        this.location = location;
        this.adaptor.clearDefaultPathFinding(this);
        this.persistent = true;
        this.setLocation(
                this.location.getX(),
                this.location.getY(),
                this.location.getZ(),
                this.location.getYaw(),
                this.location.getPitch());
        this.setPositionRotation(
                this.location.getX(),
                this.location.getY(),
                this.location.getZ(),
                this.location.getYaw(),
                this.location.getPitch());
    }

    @Override
    public void move(double d0, double d1, double d2) {
    }

    @Override
    public void collide(Entity entity) {
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        return false;
    }

    @Override
    public void g(double d0, double d1, double d2) {
    }

    @Override
    public void makeSound(String s, float f, float f1) {
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.0D);
    }

    @Override
    public void spawn() {
        ((CraftWorld) this.location.getWorld())
                .getHandle()
                .addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
        ((CraftLivingEntity) this.getBukkitEntity()).setRemoveWhenFarAway(false);
    }

    @Override
    public void setDisplayName(String name) {
        this.asBukkitEntity().setCustomName(name);
        this.asBukkitEntity().setCustomNameVisible(true);
    }

    @Override
    public void teleport(Location location) {
        this.location = location;
        this.teleportTo(location, false);
    }

    @Override
    public void setLookAtPlayer(boolean flag) {
        if (flag) {
            this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 0.0F));
        } else {
            this.adaptor.clearDefaultPathFinding(this);
        }
    }

    @Override
    public LivingEntity asBukkitEntity() {
        return ((CraftLivingEntity) this.getBukkitEntity());
    }

}
