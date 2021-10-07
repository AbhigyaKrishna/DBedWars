package me.abhigya.dbedwars.configuration.configurable;

import me.Abhigya.core.util.loadable.Loadable;
import me.Abhigya.core.util.loadable.LoadableEntry;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class ConfigurableCustomItems implements Loadable {

    private ConfigurableFireball fireball;
    private ConfigurableTNT TNT;
    private ConfigurablePopupTower popupTower;

    @Override
    public Loadable load( ConfigurationSection section ) {
        fireball = new ConfigurableFireball( );
        fireball.load( section.getConfigurationSection( "fireball" ) );
        TNT = new ConfigurableTNT( );
        TNT.load( section.getConfigurationSection( "tnt" ) );
        popupTower = new ConfigurablePopupTower( );
        popupTower.load( section.getConfigurationSection( "popup-tower" ) );
        return this.loadEntries( section );
    }

    @Override
    public boolean isValid( ) {
        return true;
    }

    @Override
    public boolean isInvalid( ) {
        return false;
    }

    public ConfigurableFireball getFireball( ) {
        return fireball;
    }

    public ConfigurableTNT getTNT( ) {
        return TNT;
    }

    public ConfigurablePopupTower getPopupTower( ) {
        return popupTower;
    }

    public static class ConfigurablePopupTower implements Loadable {

        @LoadableEntry( key = "name" )
        private String name;

        @LoadableEntry( key = "lore" )
        private List< String > lore;

        @LoadableEntry( key = "sound" )
        private String sound;

        @LoadableEntry( key = "particle" )
        private String particle;

        @LoadableEntry( key = "main-block" )
        private String mainBlock;

        protected ConfigurablePopupTower( ) {
            name = "&bCompact Pop-up Tower";
            lore = new ArrayList<>( );
            mainBlock = "WOOL%team%";
        }

        @Override
        public Loadable load( ConfigurationSection section ) {
            return this.loadEntries( section );
        }

        @Override
        public boolean isValid( ) {
            return true;
        }

        @Override
        public boolean isInvalid( ) {
            return false;
        }

        public String getName( ) {
            return name;
        }

        public List< String > getLore( ) {
            return lore;
        }

        public String getMainBlock( ) {
            return mainBlock;
        }

        public String getParticle( ) {
            return particle;
        }

        public String getSound( ) {
            return sound;
        }

    }

    public static class ConfigurableTNT implements Loadable {

        @LoadableEntry( key = "name" )
        private String name;

        @LoadableEntry( key = "lore" )
        private List< String > lore;

        @LoadableEntry( key = "auto-ignite-tnt" )
        private boolean autoIgniteTNT;

        @LoadableEntry( key = "fix-random-explosion" )
        private boolean fixRandomExplosion;

        @LoadableEntry( key = "better-tnt-ignite-animation" )
        private boolean isBetterTNTAnimationEnabled;

        @LoadableEntry( key = "fuse-ticks" )
        private int fuseTicks;

        private KnockBack knockBack;

        protected ConfigurableTNT( ) {
            name = "";
            lore = new ArrayList<>( );
            autoIgniteTNT = true;
            fixRandomExplosion = true;
            fuseTicks = 52;
            knockBack = new KnockBack( );
            isBetterTNTAnimationEnabled = true;
        }

        @Override
        public Loadable load( ConfigurationSection section ) {
            knockBack.load( section.getConfigurationSection( "knockback" ) );
            return this.loadEntries( section );
        }

        @Override
        public boolean isValid( ) {
            return true;
        }

        @Override
        public boolean isInvalid( ) {
            return false;
        }

        public boolean isBetterTNTAnimationEnabled( ) {
            return isBetterTNTAnimationEnabled;
        }

        public List< String > getLore( ) {
            return lore;
        }

        public boolean isAutoIgniteTNTEnabled( ) {
            return autoIgniteTNT;
        }

        public boolean isFixRandomExplosionEnabled( ) {
            return fixRandomExplosion;
        }

        public int getFuseTicks( ) {
            return fuseTicks;
        }

        public String getName( ) {
            return name;
        }

        public KnockBack getKnockBack( ) {
            return knockBack;
        }

    }

    public static class ConfigurableFireball implements Loadable {

        @LoadableEntry( key = "name" )
        private String displayName;

        @LoadableEntry( key = "lore" )
        private List< String > lore;

        @LoadableEntry( key = "left-click-throw" )
        private boolean leftClickThrow;

        @LoadableEntry( key = "fix-direction" )
        private boolean fixDirection;

        @LoadableEntry( key = "explosion-yield" )
        private float explosionYield;

        @LoadableEntry( key = "speed-multiplier" )
        private double speedMultiplier;

        @LoadableEntry( key = "throw-effects" )
        private List< String > potionEffects;

        @LoadableEntry( key = "explosion-fire" )
        private boolean explosionFire;

        private KnockBack knockBack;

        protected ConfigurableFireball( ) {
            displayName = "";
            lore = new ArrayList<>( );
            leftClickThrow = false;
            fixDirection = true;
            explosionYield = 4;
            speedMultiplier = 5;
            explosionFire = true;
            potionEffects = new ArrayList<>( );
        }

        @Override
        public Loadable load( ConfigurationSection section ) {
            knockBack = new KnockBack( );
            knockBack.load( section.getConfigurationSection( "knockback" ) );
            return this.loadEntries( section );
        }

        @Override
        public boolean isValid( ) {
            return true;
        }

        @Override
        public boolean isInvalid( ) {
            return false;
        }

        public List< String > getLore( ) {
            return lore;
        }

        public boolean isFixDirectionEnabled( ) {
            return fixDirection;
        }

        public boolean isLeftClickThrowEnabled( ) {
            return leftClickThrow;
        }

        public float getExplosionYield( ) {
            return explosionYield;
        }

        public double getSpeedMultiplier( ) {
            return speedMultiplier;
        }

        public KnockBack getKnockBack( ) {
            return knockBack;
        }

        public List< String > getPotionEffects( ) {
            return potionEffects;
        }

        public String getDisplayName( ) {
            return displayName;
        }

        public boolean isExplosionFireEnabled( ) {
            return explosionFire;
        }

    }

    public static class KnockBack implements Loadable {

        @LoadableEntry( key = "radius-entities" )
        private int radiusEntities;

        @LoadableEntry( key = "distance-modifier" )
        private double distanceModifier;

        @LoadableEntry( key = "height-force" )
        private double heightForce;

        @LoadableEntry( key = "horizontal-force" )
        private double horizontalForce;

        protected KnockBack( ) {
            radiusEntities = 3;
            distanceModifier = 16;
            heightForce = 1.5;
            horizontalForce = 3;
        }

        @Override
        public Loadable load( ConfigurationSection section ) {
            return this.loadEntries( section );
        }

        @Override
        public boolean isValid( ) {
            return true;
        }

        @Override
        public boolean isInvalid( ) {
            return false;
        }

        public double getDistanceModifier( ) {
            return distanceModifier;
        }

        public double getHeightForce( ) {
            return heightForce;
        }

        public double getHorizontalForce( ) {
            return horizontalForce;
        }

        public int getRadiusEntities( ) {
            return radiusEntities;
        }

    }

}
