package org.zibble.dbedwars.configuration.util.yaml;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfigurationOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/** Various settings for controlling the input and output of a {@link YamlConfigurationComments} */
public class YamlConfigurationCommentsOptions extends YamlConfigurationOptions {

    public static final String PATH_COMMENT_PREFIX = "## ";

    private final Map<String, Map<String, String>> comments;
    private boolean copy_comments;

    protected YamlConfigurationCommentsOptions(YamlConfigurationComments configuration) {
        super(configuration);
        this.comments = new HashMap<>();
        this.copy_comments = true;
    }

    @Override
    public YamlConfigurationComments configuration() {
        return (YamlConfigurationComments) super.configuration();
    }

    @Override
    public YamlConfigurationCommentsOptions copyDefaults(boolean value) {
        super.copyDefaults(value);
        return this;
    }

    @Override
    public YamlConfigurationCommentsOptions pathSeparator(char value) {
        super.pathSeparator(value);
        return this;
    }

    @Override
    public YamlConfigurationCommentsOptions header(String value) {
        super.header(value);
        return this;
    }

    @Override
    public YamlConfigurationCommentsOptions copyHeader(boolean value) {
        super.copyHeader(value);
        return this;
    }

    /**
     * Sets the comment of the given path that will be applied to the top of the given path.
     *
     * <p>This comment will be applied directly at the top of the given path. It is not required a
     * new line (\n) at the end of the comment as it will automatically be applied, but you may
     * include one if you wish for extra spacing.
     *
     * <p>Null is a valid value which will indicate that no comment is to be applied at the top of
     * the given path.
     *
     * <p>
     *
     * @param section Section where the value of the given path is located
     * @param path Path of the set object
     * @param comment The comment
     * @return This object, for chaining
     */
    public YamlConfigurationCommentsOptions comment(ConfigurationSection section, String path, String comment) {
        Validate.notNull(section, "The section cannot be null");
        Validate.isTrue(!StringUtils.isBlank(path), "Cannot comment an empty path");
        Validate.isTrue(
                !path.contains(String.valueOf(pathSeparator())),
                "The path cannot contains references to sub-sections (path separator '"
                        + pathSeparator()
                        + "' found)!");

        Map<String, String> sc_comments = comments.get(section.getCurrentPath());

        if (sc_comments == null) {
            comments.put(section.getCurrentPath(), sc_comments = new HashMap<String, String>());
        }

        if (StringUtils.isNotBlank(comment)) {
            sc_comments.put(path, comment);
        } else { // if the comment is blank, is the equivalent to use the
            // method: removeComment()
            sc_comments.remove(path);
        }
        return this;
    }

    /**
     * Sets the comment for the desired {@code path}.
     *
     * <p>This comment will be applied directly at the top of the given path. It is not required a
     * new line (\n) at the end of the comment as it will automatically be applied, but you may
     * include one if you wish for extra spacing.
     *
     * <p>Null/Empty comment is a valid value which will indicate that no comment is to be applied
     * at the top of the given path.
     *
     * <p>
     *
     * @param path Path to comment.
     * @param comment Text of the comment.
     * @return Object, for chaining.
     */
    public YamlConfigurationCommentsOptions comment(String path, String comment) {
        Validate.isTrue(!StringUtils.isBlank(path), "Cannot comment an empty path");

        int index = path.lastIndexOf(pathSeparator());
        if (index == -1) {
            // we're commenting a section that is at the root.
            return comment(configuration(), path, comment);
        } else {
            // we're commenting something that is within a sub configuration
            // section.
            String parent_path = path.substring(0, index);
            String path_name = path.substring(index + 1);

            return comment(
                    configuration().getConfigurationSection(parent_path), path_name, comment);
        }
    }

    /**
     * Returns true if the given path is commented.
     *
     * <p>
     *
     * @param section Section where the value of the given path is located.
     * @param path Path of the set object.
     * @return true if commented.
     */
    public boolean isCommented(ConfigurationSection section, String path) {
        boolean a = comments.get(section.getCurrentPath()) != null;
        boolean b = a && comments.get(section.getCurrentPath()).containsKey(path);

        return a && b;
    }

    /**
     * Returns the comment of the given path or null if it is not commented.
     *
     * <p>
     *
     * @param section Section where the value of the given path is located.
     * @param path Path of the set object.
     * @return Comment of the given path or null if not commented.
     */
    public String getComment(ConfigurationSection section, String path) {
        Map<String, String> parent = comments.get(section.getCurrentPath());

        return parent != null ? parent.get(path) : null;
    }

    /**
     * Return all the comments of all the path of all the {@link ConfigurationSection}.
     *
     * <p>The returned map is unmodifiable, so you should not try do modify it!
     *
     * <p>
     *
     * @return All the comments of all the path of all the {@link ConfigurationSection}.
     */
    public Map<String, Map<String, String>> getComments() {
        return Collections.unmodifiableMap(comments);
    }

    /**
     * Gets whether the comments of the path should be inserted at the top of the paths when saving
     * the configuration.
     *
     * <p>
     *
     * @return true if the comments are enabled.
     */
    public boolean copyComments() {
        return this.copy_comments;
    }

    /**
     * Sets whether or not the comments should by copied to the top of the paths when saving the
     * configuration.
     *
     * <p>
     *
     * @param copy true to enable the comments.
     * @return This object, for chaining.
     */
    public YamlConfigurationCommentsOptions copyComments(boolean copy) {
        this.copy_comments = copy;
        return this;
    }
}
