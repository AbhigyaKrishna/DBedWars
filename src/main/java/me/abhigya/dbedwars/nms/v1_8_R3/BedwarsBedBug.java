package me.abhigya.dbedwars.nms.v1_8_R3;

import me.abhigya.dbedwars.api.game.ArenaPlayer;
import me.abhigya.dbedwars.api.game.Team;
import me.abhigya.dbedwars.nms.IBedBug;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftSilverfish;
import org.bukkit.entity.Silverfish;

import java.util.Optional;

public class BedwarsBedBug implements IBedBug {

    private final EntitySilverfish bedBug;
    private final Team spawningTeam;

    public BedwarsBedBug(Silverfish bedBug, Team spawningTeam) {
        this.bedBug = ((CraftSilverfish) bedBug).getHandle();
        this.spawningTeam = spawningTeam;
    }

    @Override
    public IBedBug clearDefaultPathfinding() {
        new NMSUtils().clearDefaultPathFinding(bedBug);
        return this;
    }

    @Override
    public IBedBug addCustomDefaults() {
        bedBug.goalSelector.a(1, new PathfinderGoalFloat(bedBug));
        return this;
    }

    @Override
    public IBedBug initTargets(double reachModifier) {
        bedBug.goalSelector.a(
                4, new PathfinderGoalMeleeAttack(bedBug, EntityHuman.class, reachModifier, false));
        bedBug.targetSelector.a(
                2,
                new PathFindingNearestAttackableTarget<>(
                        bedBug, EntityHuman.class, true, false, spawningTeam));
        return this;
    }

    @Override
    public IBedBug setChaseRadius(float radius) {
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
