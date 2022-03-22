package org.zibble.dbedwars.configuration.util;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/** Class for dealing with {@link YamlConfiguration} */
public class YamlUtils {

    /**
     * Sets the specified path to the given value if and only if it is not already set.
     *
     * <p>If value is null, the entry will be removed. Any existing entry will be replaced,
     * regardless of what the new value is.
     *
     * <p>Some implementations may have limitations on what you may store. See their individual
     * javadocs for details. No implementations should allow you to store {@link Configuration} or
     * {@link ConfigurationSection}, please use {@link ConfigurationSection#createSection(String)}
     * for that.
     *
     * <p>
     *
     * @param section Configuration section to set
     * @param path Path of the object to set
     * @param value Value to set the path to
     * @return true if set successfully, else false
     */
    public static boolean setNotSet(ConfigurationSection section, String path, Object value) {
        if (section.isSet(path)) {
            return false;
        }

        section.set(path, value);
        return true;
    }

    /**
     * Sets the specified path to the given value if and only if it the value at the provided path
     * is not the same as {@code value}.
     *
     * <p>If value is null, the entry will be removed. Any existing entry will be replaced,
     * regardless of what the new value is.
     *
     * <p>Some implementations may have limitations on what you may store. See their individual
     * javadocs for details. No implementations should allow you to store {@link Configuration} or
     * {@link ConfigurationSection}, please use {@link ConfigurationSection#createSection(String)}
     * for that.
     *
     * <p>
     *
     * @param section Configuration section to set
     * @param path Path of the object to set
     * @param value Value to set the path to
     * @return true if set successfully, else false
     */
    public static boolean setNotEqual(ConfigurationSection section, String path, Object value) {
        if (setNotSet(section, path, value)) {
            return true;
        } else if (Objects.equals(value, section.get(path))) {
            return false;
        }

        section.set(path, value);
        return true;
    }

    /**
     * Creates an empty {@link ConfigurationSection} within the specified {@link
     * ConfigurationSection} if and only if there is no another section with the same name as the
     * provided.
     *
     * <p>
     *
     * @param section Section at which the new section will be created
     * @param name Name for the section
     * @return Newly created section, or the already existing section
     */
    public static ConfigurationSection createNotExisting(
            ConfigurationSection section, String name) {
        return section.isConfigurationSection(name)
                ? section.getConfigurationSection(name)
                : section.createSection(name);
    }

    /**
     * Gets the {@link ConfigurationSection} within the desired {@link ConfigurationSection}
     * (sub-sections of the desired section).
     *
     * <p>
     *
     * @param section {@link ConfigurationSection} where the sub-sections are stored
     * @return Sub-sections of the desired section
     */
    public static Set<ConfigurationSection> getSubSections(ConfigurationSection section) {
        Set<ConfigurationSection> sections = new HashSet<>();
        for (String key : section.getKeys(false)) {
            if (section.isConfigurationSection(key)) sections.add(section.getConfigurationSection(key));
        }
        return sections;
    }

    /**
     * Replaces the desired character that is used as path separator by the specified {@code
     * alt_char}.
     *
     * <p>
     *
     * @param key Key that contains the separator to replace
     * @param path_separator Path separator to replace
     * @param alt_char Path separator to use instead the current one
     * @return Key containing the new path separator
     */
    public static String alternatePathSeparator(String key, char path_separator, char alt_char) {
        return key.replace(path_separator, alt_char);
    }

    /**
     * Replaces the common path separator {@literal '.'} by the specified {@code alt_char}.
     *
     * <p>
     *
     * @param key Key that contains the common separator to replace
     * @param alt_char Path separator to use instead the common one
     * @return Key containing the new path separator
     */
    public static String alternatePathSeparator(String key, char alt_char) {
        return alternatePathSeparator(key, '.', alt_char);
    }
}
