package org.zibble.dbedwars.configuration.configurable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.zibble.dbedwars.api.util.Initializable;
import org.zibble.dbedwars.configuration.framework.Configurable;
import org.zibble.dbedwars.configuration.util.YamlUtils;

import java.util.UUID;

/**
 * Represents a {@link Location} that is 'Configurable' because can be loaded from/saved on a {@link
 * ConfigurationSection}.
 */
public class ConfigurableLocation extends Location implements Configurable, Initializable {

    public static final String WORLD_UID_KEY = "world-uid";
    public static final String X_KEY = "x";
    public static final String Y_KEY = "y";
    public static final String Z_KEY = "z";
    public static final String YAW_KEY = "yaw";
    public static final String PITCH_KEY = "pitch";
    public static final String[] CONFIGURABLE_LOCATION_KEYS = {
        WORLD_UID_KEY, X_KEY, Y_KEY, Z_KEY, YAW_KEY, PITCH_KEY
    };
    /** Whether {@link #load(ConfigurationSection)} method has been called. */
    protected boolean initialized;

    /** Constructs a uninitialized {@link ConfigurableLocation}. */
    public ConfigurableLocation() { // uninitialized
        super(null, 0, 0, 0, 0, 0);
    }

    /**
     * Constructs the {@link ConfigurableLocation}.
     *
     * <p>
     *
     * @param world World
     * @param x X-axis
     * @param y Y-axis
     * @param z Z-axis
     * @param yaw Yaw
     * @param pitch Pitch
     */
    public ConfigurableLocation(World world, double x, double y, double z, float yaw, float pitch) {
        super(world, x, y, z, yaw, pitch);
        this.initialized = true;
    }

    /**
     * Constructs the {@link ConfigurableLocation}.
     *
     * <p>
     *
     * @param world World
     * @param x X-axis
     * @param y Y-axis
     * @param z Z-axis
     */
    public ConfigurableLocation(World world, double x, double y, double z) {
        this(world, x, y, z, 0.0F, 0.0F);
    }

    /**
     * Constructs the {@link ConfigurableLocation}.
     *
     * <p>
     *
     * @param copy Copy of the location
     */
    public ConfigurableLocation(Location copy) {
        this(copy.getWorld(), copy.getX(), copy.getY(), copy.getZ(), copy.getYaw(), copy.getPitch());
    }

    /**
     * Returns a {@link ConfigurableLocation} loaded from the given {@link ConfigurationSection}, or
     * null if there is no any valid {@link ConfigurableLocation} stored on the given {@link
     * ConfigurationSection}.
     *
     * <p>Note that this method checks the given configuration section calling {@link
     * #isConfigurableLocation(ConfigurationSection)}.
     *
     * <p>
     *
     * @param section Section to parse
     * @return Parsed location
     */
    public static ConfigurableLocation of(ConfigurationSection section) {
        if (isConfigurableLocation(section)) {
            ConfigurableLocation location = new ConfigurableLocation();
            location.load(section);
            return location;
        }
        return null;
    }

    /**
     * Return true if and only if there is a valid {@link ConfigurableLocation} stored on the given
     * {@link ConfigurationSection}
     *
     * <p>
     *
     * @param section {@link ConfigurationSection} where the supposed {@link ConfigurableLocation}
     *     is stored
     * @return true if is
     */
    public static boolean isConfigurableLocation(ConfigurationSection section) {
        for (String key : CONFIGURABLE_LOCATION_KEYS) {
            if (!WORLD_UID_KEY.equals(key) && !(section.get(key) instanceof Number)) {
                return false;
            }
        }

        if (section.isString(WORLD_UID_KEY)) {
            try {
                // this will thrown an exception if the UUID is invalid.
                UUID.fromString(section.getString(WORLD_UID_KEY));
                return true;
            } catch (IllegalArgumentException ex) {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void load(ConfigurationSection section) {
        this.setWorld(Bukkit.getWorld(UUID.fromString(section.getString(WORLD_UID_KEY))));
        this.setX(section.getDouble(X_KEY, 0));
        this.setY(section.getDouble(Y_KEY, 0));
        this.setZ(section.getDouble(Z_KEY, 0));
        this.setYaw((float) section.getDouble(YAW_KEY, 0));
        this.setPitch((float) section.getDouble(PITCH_KEY, 0));
        this.initialized = true;
    }

    @Override
    public int save(ConfigurationSection section) {
        return (YamlUtils.setNotEqual(section, WORLD_UID_KEY, (this.getWorld() != null ? this.getWorld().getUID().toString() : "")) ? 1 : 0)
                + (YamlUtils.setNotEqual(section, X_KEY, getX()) ? 1 : 0)
                + (YamlUtils.setNotEqual(section, Y_KEY, getY()) ? 1 : 0)
                + (YamlUtils.setNotEqual(section, Z_KEY, getZ()) ? 1 : 0)
                + (YamlUtils.setNotEqual(section, YAW_KEY, getYaw()) ? 1 : 0)
                + (YamlUtils.setNotEqual(section, PITCH_KEY, getPitch()) ? 1 : 0);
    }

    /**
     * Gets a clone of this location with the specified {@link World world}.
     *
     * <p>
     *
     * @param world New world for the location
     * @return Clone of this location with the specified {@code world}
     */
    public ConfigurableLocation withWorld(World world) {
        ConfigurableLocation location = clone();
        location.setWorld(world);
        return location;
    }

    @Override
    public boolean isInitialized() {
        return this.initialized;
    }

    @Override
    public boolean isValid() {
        return isInitialized() && this.getWorld() != null;
    }

    @Override
    public ConfigurableLocation clone() {
        return (ConfigurableLocation) super.clone();
    }
}
