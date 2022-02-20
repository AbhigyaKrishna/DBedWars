package org.zibble.dbedwars.api.version;

import org.bukkit.Bukkit;

/** An enumeration for most server versions, that implements some methods for comparing versions. */
public enum Version {

    /* legacy versions */
    v1_8_R1(181),
    v1_8_R2(182),
    v1_8_R3(183),
    v1_9_R1(191),
    v1_9_R2(192),
    v1_10_R1(1101),
    v1_11_R1(1111),
    v1_12_R1(1121),

    /* latest versions */
    v1_13_R1(1131),
    v1_13_R2(1132),
    v1_14_R1(1141),
    v1_15_R1(1151),
    v1_16_R1(1161),
    v1_16_R2(1162),
    v1_16_R3(1163),
    v1_17_R1(1171),
    v1_18_R1(1181),
    ;

    public static final String CRAFT_CLASSES_PACKAGE = "org.bukkit.craftbukkit.%s";
    public static final String NMS_CLASSES_PACKAGE = "net.minecraft.server.%s";
    public static final Version SERVER_VERSION = Version.getServerVersion();

    private final int id;

    Version(int id) {
        this.id = id;
    }

    /**
     * Gets the version of the current running server.
     *
     * <p>Note that server versions older than {@link Version#v1_8_R1} are NOT supported.
     *
     * <p>
     *
     * @return Version of this server
     */
    public static Version getServerVersion() {
        String packaje = Bukkit.getServer().getClass().getPackage().getName();
        String version = packaje.substring(packaje.lastIndexOf(".") + 1);
        return valueOf(version);
    }

    public String getNmsPackage() {
        if (SERVER_VERSION.isNewerEquals(Version.v1_17_R1)) return "net.minecraft";
        else return String.format(NMS_CLASSES_PACKAGE, SERVER_VERSION.name());
    }

    public String getObcPackage() {
        return String.format(CRAFT_CLASSES_PACKAGE, SERVER_VERSION.name());
    }

    /**
     * Gets the version's id.
     *
     * <p>
     *
     * @return Version's id
     */
    public int getID() {
        return this.id;
    }

    /**
     * Checks whether this version is older than the provided version.
     *
     * <p>
     *
     * @param other Other version
     * @return true if older
     */
    public boolean isOlder(Version other) {
        return (getID() < other.getID());
    }

    /**
     * Checks whether this version is older than or equals to the provided version.
     *
     * <p>
     *
     * @param other Other version
     * @return true if older or equals
     */
    public boolean isOlderEquals(Version other) {
        return (getID() <= other.getID());
    }

    /**
     * Checks whether this version is newer than the provided version.
     *
     * <p>
     *
     * @param other Other version
     * @return true if newer
     */
    public boolean isNewer(Version other) {
        return (getID() > other.getID());
    }

    /**
     * Checks whether this version is newer than or equals to the provided version.
     *
     * <p>
     *
     * @param other Other version
     * @return true if newer or equals
     */
    public boolean isNewerEquals(Version other) {
        return (getID() >= other.getID());
    }

    /**
     * Checks whether this has the same version as the provided version.
     *
     * <p>
     *
     * <pre><code>
     * Version.1_8_R1.equalsVersion (1_8_R3) = true
     * Version.1_9_R1.equalsVersion (1_8_R1) = false
     * </code></pre>
     *
     * <p>
     *
     * @param other Other version
     * @return true if has the same version
     */
    public boolean equalsVersion(Version other) {
        String s0 = name().substring(0, name().indexOf("_R"));
        String s1 = other.name().substring(0, other.name().indexOf("_R"));
        return s0.equals(s1);
    }

    /**
     * Checks whether this has the same revision as the provided version.
     *
     * <p>
     *
     * <pre><code>
     * Version.1_8_R3.equalsRevision (1_9_R3) = true
     * Version.1_8_R1.equalsRevision (1_8_R3) = false
     * </code></pre>
     *
     * <p>
     *
     * @param other Other version
     * @return true if has the same revision
     */
    public boolean equalsRevision(Version other) {
        String s0 = name().substring(name().indexOf("R") + 1);
        String s1 = other.name().substring(other.name().indexOf("R") + 1);
        return s0.equals(s1);
    }
}
