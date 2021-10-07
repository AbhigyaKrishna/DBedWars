package me.abhigya.dbedwars.configuration.configurable;

import me.Abhigya.core.particle.particlelib.ParticleEffect;
import me.Abhigya.core.util.loadable.Loadable;
import me.Abhigya.core.util.loadable.LoadableEntry;
import me.Abhigya.core.util.xseries.XMaterial;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.api.game.spawner.DropType;
import me.abhigya.dbedwars.api.util.BwItemStack;
import me.abhigya.dbedwars.api.util.SoundVP;
import me.abhigya.dbedwars.utils.ConfigurationUtils;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class ConfigurableItemSpawner implements Loadable {

    private final DBedwars plugin;

    private String key;

    @LoadableEntry( key = "id" )
    private String id;

    @LoadableEntry( key = "icon" )
    private String icon;

    @LoadableEntry( key = "spawn-sound" )
    private String spawnSound;

    @LoadableEntry( key = "spawn-effect" )
    private String spawnEffect;

    @LoadableEntry( key = "radius" )
    private int radius;

    @LoadableEntry( key = "merge" )
    private boolean merge;

    @LoadableEntry( key = "split" )
    private boolean split;

    @LoadableEntry( key = "timed-upgrade" )
    private boolean timedUpgrade;

    @LoadableEntry( key = "team-spawner" )
    private boolean teamSpawner;

    @LoadableEntry( key = "hologram.enabled" )
    private boolean hologramEnabled;

    @LoadableEntry( key = "hologram.material" )
    private String hologramMaterial;

    @LoadableEntry( key = "hologram.text" )
    private List< String > hologramText;

    private Map< Integer, ConfigurableTiers > tiers;

    private DropType dropType;

    public ConfigurableItemSpawner( DBedwars plugin, String key ) {
        this.plugin = plugin;
        this.key = key;
        this.hologramText = new ArrayList<>( );
        this.tiers = new LinkedHashMap<>( );
        this.radius = 1;
    }

    @Override
    public Loadable load( ConfigurationSection section ) {
        ConfigurationSection tierSection = section.getConfigurationSection( "tiers" );
        if ( tierSection != null ) {
            for ( String key : tierSection.getKeys( false ) ) {
                ConfigurableTiers tier = new ConfigurableTiers( key );
                tier.load( tierSection.getConfigurationSection( key ) );
                if ( tier.isValid( ) )
                    this.tiers.put( Integer.parseInt( key ), tier );
            }
        }
        return this.loadEntries( section );
    }

    @Override
    public boolean isValid( ) {
        return this.id != null && !this.tiers.isEmpty( ) && this.tiers.containsKey( 1 ) && !this.tiers.get( 1 ).getActions( ).isEmpty( );
    }

    @Override
    public boolean isInvalid( ) {
        return !this.isValid( );
    }

    public String getKey( ) {
        return key;
    }

    public String getId( ) {
        return id;
    }

    public BwItemStack getIcon( ) {
        return this.icon != null ? ConfigurationUtils.parseItem( this.icon ) : new BwItemStack( XMaterial.IRON_INGOT.parseItem( ) );
    }

    public SoundVP getSpawnSound( ) {
        return this.spawnSound != null ? SoundVP.valueOf( this.spawnSound ) : null;
    }

    public ParticleEffect getSpawnEffect( ) {
        try {
            return ParticleEffect.valueOf( this.spawnEffect );
        } catch ( IllegalArgumentException ignored ) {
        }
        return null;
    }

    public int getRadius( ) {
        return this.radius;
    }

    public boolean isMerge( ) {
        return this.merge;
    }

    public boolean isSplit( ) {
        return split;
    }

    public boolean isTimedUpgrade( ) {
        return this.timedUpgrade;
    }

    public boolean isTeamSpawner( ) {
        return this.teamSpawner;
    }

    public boolean isHologramEnabled( ) {
        return this.hologramEnabled;
    }

    public BwItemStack getHologramMaterial( ) {
        return this.hologramMaterial != null ? ConfigurationUtils.parseItem( this.hologramMaterial ) : null;
    }

    public List< String > getHologramText( ) {
        return this.hologramText;
    }

    public Map< Integer, ConfigurableTiers > getTiers( ) {
        return tiers;
    }

    public DropType toDropType( ) {
        if ( this.dropType == null ) {
            this.dropType = new me.abhigya.dbedwars.game.arena.DropType( this.plugin, this );
        }

        return this.dropType;
    }

    public static class ConfigurableTiers implements Loadable {

        private String key;

        @LoadableEntry( key = "time" )
        private double seconds;

        @LoadableEntry( key = "upgrade-effect" )
        private String upgradeEffect;

        @LoadableEntry( key = "upgrade-sound" )
        private String upgradeSound;

        @LoadableEntry( key = "message" )
        private String message;

        private Map< String, ConfigurableDrop > actions;

        protected ConfigurableTiers( String key ) {
            this.key = key;
            this.seconds = -1;
            this.actions = new HashMap<>( );
        }

        @Override
        public Loadable load( ConfigurationSection section ) {
            ConfigurationSection actionSection = section.getConfigurationSection( "action" );
            if ( actionSection != null ) {
                for ( String key : actionSection.getKeys( false ) ) {
                    ConfigurableDrop action = new ConfigurableDrop( key );
                    action.load( actionSection.getConfigurationSection( key ) );
                    if ( action.isValid( ) )
                        this.actions.put( key, action );
                }
            }
            return this.loadEntries( section );
        }

        @Override
        public boolean isValid( ) {
            try {
                Integer.parseInt( this.key );
            } catch ( NumberFormatException e ) {
                return false;
            }

            return true;
        }

        @Override
        public boolean isInvalid( ) {
            return !this.isValid( );
        }

        public String getKey( ) {
            return key;
        }

        public double getSeconds( ) {
            return this.seconds;
        }

        public ParticleEffect getUpgradeEffect( ) {
            if ( this.upgradeEffect != null ) {
                try {
                    return ParticleEffect.valueOf( this.upgradeEffect );
                } catch ( IllegalArgumentException ignored ) {
                }
            }
            return null;
        }

        public SoundVP getUpgradeSound( ) {
            return this.upgradeSound != null ? SoundVP.valueOf( this.upgradeSound ) : null;
        }

        public String getMessage( ) {
            return this.message;
        }

        public Map< String, ConfigurableDrop > getActions( ) {
            return this.actions;
        }

    }

    public static class ConfigurableDrop implements Loadable {

        private String key;

        @LoadableEntry( key = "material" )
        private String material;

        @LoadableEntry( key = "delay" )
        private double delay;

        @LoadableEntry( key = "limit" )
        private int limit;

        protected ConfigurableDrop( String key ) {
            this.key = key;
            this.delay = -1;
            this.limit = -1;
        }

        @Override
        public Loadable load( ConfigurationSection section ) {
            return this.loadEntries( section );
        }

        @Override
        public boolean isValid( ) {
            return this.material != null && XMaterial.matchXMaterial( this.material.split( ":" )[0] ).isPresent( );
        }

        @Override
        public boolean isInvalid( ) {
            return !this.isValid( );
        }

        public String getKey( ) {
            return key;
        }

        public BwItemStack getMaterial( ) {
            return ConfigurationUtils.parseItem( this.material );
        }

        public double getDelay( ) {
            return this.delay;
        }

        public int getLimit( ) {
            return this.limit;
        }

    }

}
