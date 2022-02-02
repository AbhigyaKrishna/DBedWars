package com.pepedevs.dbedwars.nms.v1_8_R3;

import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.game.Team;
import com.pepedevs.dbedwars.api.nms.IGolem;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftIronGolem;
import org.bukkit.entity.IronGolem;

import java.util.Optional;

public class BedwarsGolem implements IGolem {

    private final EntityIronGolem golem;
    private final Team spawningPlayerTeam;
    private float radius;

    public BedwarsGolem(IronGolem golem, Team spawningPlayerTeam) {
        this.golem = ((CraftIronGolem) golem).getHandle();
        this.radius = 32;
        this.spawningPlayerTeam = spawningPlayerTeam;
    }

    @Override
    public IGolem clearDefaultPathfinding() {
        new NMSUtils().clearDefaultPathFinding(golem);
        return this;
    }

    @Override
    public IGolem addCustomDefaults() {
        golem.goalSelector.a(0, new PathfinderGoalFloat(golem));
        golem.goalSelector.a(2, new PathfinderGoalMoveTowardsTarget(golem, 0.9D, radius));

        golem.goalSelector.a(6, new PathfinderGoalRandomStroll(golem, 0.6D));
        golem.goalSelector.a(7, new PathfinderGoalLookAtPlayer(golem, EntityHuman.class, 6.0F));
        golem.goalSelector.a(8, new PathfinderGoalRandomLookaround(golem));
        return this;
    }

    @Override
    public IGolem initTargets(double reachModifier) {
        golem.goalSelector.a(1, new PathfinderGoalMeleeAttack(golem, reachModifier, true));
        golem.targetSelector.a(
                2,
                new PathFindingNearestAttackableTarget<>(
                        golem, EntityHuman.class, true, false, spawningPlayerTeam));
        return this;
    }

    @Override
    public IGolem setChaseRadius(float radius) {
        this.radius = radius;
        return this;
    }

    private static class PathFindingNearestAttackableTarget<T extends EntityLiving>
            extends PathfinderGoalNearestAttackableTarget<T> {

        private final Team spawningTeam;

        public PathFindingNearestAttackableTarget(
                EntityCreature entitycreature,
                Class<T> oclass,
                boolean flag,
                boolean flag1,
                Team spawningTeam) {
            super(entitycreature, oclass, 0, flag, flag1, null);
            this.spawningTeam = spawningTeam;
        }

        @Override
        protected boolean a(EntityLiving entityLiving, boolean b) {
            if (!(entityLiving instanceof EntityPlayer)) return false;
            Optional<ArenaPlayer> optionalPlayer =
                    spawningTeam
                            .getArena()
                            .getAsArenaPlayer(((EntityPlayer) entityLiving).getBukkitEntity());
            if (!optionalPlayer.isPresent()) return false;
            if (optionalPlayer.get().isSpectator()) return false;
            return !spawningTeam.getPlayers().contains(optionalPlayer.get());
        }
    }
}
