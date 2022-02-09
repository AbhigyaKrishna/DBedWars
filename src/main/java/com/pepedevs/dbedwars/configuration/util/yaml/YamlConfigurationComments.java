package com.pepedevs.dbedwars.configuration.util.yaml;

import com.pepedevs.dbedwars.configuration.util.yaml.YamlConfigurationCommentsOptions;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConstructor;
import org.bukkit.configuration.file.YamlRepresenter;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.representer.Representer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.logging.Level;

/**
 * An implementation of {@link Configuration} which saves all files in Yaml, and allows to comments
 * the keys by using the method {@link #options()}.comment().
 *
 * <p>Note that this implementation is not synchronized.
 */
public class YamlConfigurationComments extends YamlConfiguration {

    protected static final String LINE_SEPARATOR = "\n";
    protected static final String VALUE_INDICATOR = ":";
    private final DumperOptions yaml_options = new DumperOptions();
    private final Representer yaml_representer = new YamlRepresenter();
    private final Yaml yaml = new Yaml(new YamlConstructor(), yaml_representer, yaml_options);

    /**
     * Creates a new {@link YamlConfigurationComments}, loading from the given file.
     *
     * <p>Any errors loading the Configuration will be logged and then ignored. If the specified
     * input is not a valid config, a blank config will be returned.
     *
     * <p>The encoding used may follow the system dependent default.
     *
     * <p>
     *
     * @param file Input file
     * @return Resulting configuration
     * @throws IllegalArgumentException Thrown if file is null
     */
    public static YamlConfigurationComments loadConfiguration(File file) {
        Validate.notNull(file, "File cannot be null");

        YamlConfigurationComments config = new YamlConfigurationComments();
        try {
            config.load(file);
        } catch (FileNotFoundException ignored) {
        } catch (IOException | InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, ex);
        }
        return config;
    }

    /**
     * Creates a new {@link YamlConfigurationComments}, loading from the given reader.
     *
     * <p>Any errors loading the Configuration will be logged and then ignored. If the specified
     * input is not a valid config, a blank config will be returned.
     *
     * <p>
     *
     * @param reader Input
     * @return Resulting configuration
     * @throws IllegalArgumentException Thrown if stream is null
     */
    public static YamlConfigurationComments loadConfiguration(Reader reader) {
        Validate.notNull(reader, "Stream cannot be null");

        YamlConfigurationComments config = new YamlConfigurationComments();
        try {
            config.load(reader);
        } catch (IOException | InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load configuration from stream", ex);
        }
        return config;
    }

    @Override
    public String saveToString() {
        yaml_options.setIndent(options().indent());
        yaml_options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        yaml_representer.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        String header = buildHeader();
        String dump = insertComments(yaml.dump(getValues(false)));

        if (dump.equals(BLANK_CONFIG)) {
            dump = "";
        }

        dump = dump.substring(0, dump.lastIndexOf(LINE_SEPARATOR)); // clear
        // last
        // line
        return header + dump;
    }

    //	protected String insertComments ( String dump ) {
    //		StringBuilder result = new StringBuilder ( );
    //		String [ ]    lines  = dump.split ( "\r?\n" , -1 );
    //
    //		for ( int i = 0 ; i < lines.length ; i ++ ) {
    //			String line = lines [ i ];
    //
    //
    //		}
    //		return dump;
    //	}

    protected String insertComments(String dump) {
        StringBuilder builder = new StringBuilder();
        String[] lines = dump.split("\r?\n", -1);
        String last_node = "";

        for (int i = 0; i < lines.length; i++) {
            /* calculate line node */
            String line = lines[i];
            String line_path = "";
            int indent = indentCount(line);
            String node = "";

            //			System.out.println ( ">>>>> i = " + i );

            if (i > 0 && indent >= options().indent() && line.indexOf(VALUE_INDICATOR) != -1) {
                for (int j = i; j >= 0; j--) {
                    String line_back = lines[j];
                    String line_back_path = "";
                    int line_back_indent = indentCount(line_back);

                    if ((indent - options().indent()) < 0) {
                        break;
                    }

                    if (line_back_indent >= indent) {
                        continue;
                    }

                    if (line_back.trim().startsWith(COMMENT_PREFIX)
                            || line_back
                                    .trim()
                                    .startsWith(
                                            com.pepedevs.dbedwars.configuration.util.yaml.YamlConfigurationCommentsOptions.PATH_COMMENT_PREFIX)) {
                        continue;
                    }

                    /* father section found */
                    if (line_back_indent == (indent - options().indent())) {
                        line_path = line.trim().substring(0, line.trim().indexOf(VALUE_INDICATOR));
                        line_back_path =
                                line_back
                                        .trim()
                                        .substring(0, line_back.trim().indexOf(VALUE_INDICATOR));
                        node = line_back_path + options().pathSeparator() + line_path;
                        if (last_node.endsWith(options().pathSeparator() + line_back_path)) {
                            node = last_node + options().pathSeparator() + line_path;
                        }

                        last_node = node;
                        break;
                    }
                }
            }

            //			System.out.println ( ">>>> node = " + node );
            //			System.out.println ( ">>>> line_path = " + line_path );

            //			String node_parent_path = node.isEmpty ( ) ? this.getCurrentPath ( )
            //					: node.substring ( 0 , node.lastIndexOf ( options ( ).pathSeparator ( ) ) );

            //			System.out.println ( ">>>> node_parent_path = " + node_parent_path );

            ConfigurationSection section =
                    node.isEmpty()
                            ? this
                            : getConfigurationSection(
                                    node.substring(0, node.lastIndexOf(options().pathSeparator())));

            //			System.out.println ( ">>>> node parent section = " + ( section != null ?
            // section.getCurrentPath ( ) : "null" ) );

            //			System.out.println ( ">>>>>>> node = " + node );
            //			System.out.println ( ">>>>>>> line_path = " + line_path );

            if (section != null) {
                //				System.out.println ( ">>>> 0" );
                if (line_path.trim().isEmpty() && line.trim().indexOf(VALUE_INDICATOR) != -1) {
                    //					System.out.println ( ">>>> 1" );
                    line_path = line.trim().substring(0, line.trim().indexOf(VALUE_INDICATOR));
                }

                //				System.out.println ( ">>>> 2: section path = " + section.getCurrentPath ( ) );

                String comment = options().getComment(section, line_path);

                Map<String, String> comments =
                        options().getComments().get(section.getCurrentPath());

                if (comments != null) {
                    for (String key : comments.keySet()) {
                        if (line_path.equalsIgnoreCase(key)) {
                            //							System.out.println ( ">>>> - (FOUND) " + key + ": " +
                            // comments.get ( key ) );
                        } else {
                            //							System.out.println ( ">>>> - " + key + ": " + comments.get (
                            // key ) );
                        }
                    }
                }

                if (comment != null) {
                    builder.append(
                            getIndent(indent)
                                    + com.pepedevs.dbedwars.configuration.util.yaml.YamlConfigurationCommentsOptions.PATH_COMMENT_PREFIX
                                    + comment);
                    builder.append(LINE_SEPARATOR);
                }

                //				if ( options ( ).isCommented ( section , line_path ) ) {
                ////					System.out.println ( ">>>> 3" );
                //					builder.append ( getIndent ( indent ) +
                // YamlConfigurationCommentsOptions.PATH_COMMENT_PREFIX
                //							+ options ( ).getComment ( section , line_path ) );
                //					builder.append ( LINE_SEPARATOR );
                //				}
            }

            //			System.out.println (  );
            //			System.out.println ( "------------------" );
            //			System.out.println (  );

            builder.append(line);
            builder.append(LINE_SEPARATOR);
        }
        return builder.toString();
    }

    //	protected String insertComments ( String dump ) {
    //		StringBuilder builder   = new StringBuilder ( );
    //		String [ ]    lines     = dump.split ( "\r?\n" , -1 );
    //		String        last_node = "";
    //
    //		System.out.println ( "dump = " + dump );
    //
    //		for ( int i = 0 ; i < lines.length ; i ++ ) {
    //			/* calculate line node */
    //			String line      = lines [ i ];
    //			String line_path = "";
    //			int    indent    = indentCount ( line );
    //			String node      = "";
    //
    //			System.out.println ( ">>>>> i = " + i );
    //
    //			if ( i > 0 && indent >= options ( ).indent ( ) && line.indexOf ( VALUE_INDICATOR ) != -1 )
    // {
    //				for ( int j = i ; j >= 0 ; j -- ) {
    //					String line_back        = lines [ j ];
    //					String line_back_path   = "";
    //					int    line_back_indent = indentCount ( line_back );
    //
    //					if ( ( indent - options ( ).indent ( ) ) < 0 ) {
    //						break;
    //					}
    //
    //					if ( line_back_indent >= indent ) {
    //						continue;
    //					}
    //
    //					if ( line_back.trim ( ).startsWith ( COMMENT_PREFIX ) || line_back.trim ( )
    //							.startsWith ( YamlConfigurationCommentsOptions.PATH_COMMENT_PREFIX ) ) {
    //						continue;
    //					}
    //
    //					/* father section found */
    //					if ( line_back_indent == ( indent - options ( ).indent ( ) ) ) {
    //						line_path      = line.trim ( ).substring ( 0 , line.trim ( ).indexOf ( VALUE_INDICATOR )
    // );
    //						line_back_path = line_back.trim ( ).substring ( 0 ,
    //								line_back.trim ( ).indexOf ( VALUE_INDICATOR ) );
    //						node           = line_back_path + options ( ).pathSeparator ( ) + line_path;
    //						if ( last_node.endsWith ( options ( ).pathSeparator ( ) + line_back_path ) ) {
    //							node = last_node + options ( ).pathSeparator ( ) + line_path;
    //						}
    //
    //						last_node = node;
    //						break;
    //					}
    //				}
    //			}
    //
    //			ConfigurationSection section = node.isEmpty ( ) ? this : getConfigurationSection (
    //					node.substring ( 0 , node.lastIndexOf ( options ( ).pathSeparator ( ) ) ) );
    //
    //			if ( section != null ) {
    //				if ( line_path.trim ( ).isEmpty ( ) && line.trim ( ).indexOf ( VALUE_INDICATOR ) != -1 ) {
    //					line_path = line.trim ( ).substring ( 0 , line.trim ( ).indexOf ( VALUE_INDICATOR ) );
    //				}
    //
    //				if ( options ( ).isCommented ( section , line_path ) ) {
    //					builder.append ( getIndent ( indent ) +
    // YamlConfigurationCommentsOptions.PATH_COMMENT_PREFIX
    //							+ options ( ).getComment ( section , line_path ) );
    //					builder.append ( LINE_SEPARATOR );
    //				}
    //			}
    //
    //			builder.append ( line );
    //			builder.append ( LINE_SEPARATOR );
    //		}
    //		return builder.toString ( );
    //	}

    /**
     * Gets the equivalent of the given indent represented on a String.
     *
     * <p>Examples: - getIndent(2) = " " - getIndent(4) = " " - getIndent(6) = " "
     *
     * <p>
     *
     * @param indent_count Indent count
     * @return String representation of the given indent
     */
    protected String getIndent(int indent_count) {
        StringBuilder indent = new StringBuilder();

        for (int i = 0; i < indent_count; i++) {
            indent.append(" ");
        }

        return indent.toString();
    }

    @Override
    public void loadFromString(String contents) throws InvalidConfigurationException {
        Validate.notNull(contents, "Contents cannot be null");

        Map<?, ?> input;
        try {
            input = (Map<?, ?>) yaml.load(contents);
        } catch (YAMLException e) {
            throw new InvalidConfigurationException(e);
        } catch (ClassCastException e) {
            throw new InvalidConfigurationException("Top level is not a Map.");
        }

        String header = parseHeader(contents);
        if (header.length() > 0) {
            options().header(header);
        }

        if (input != null) {
            convertMapsToSections(input, this);
        }

        loadComments(contents);
    }

    protected void convertMapsToSections(Map<?, ?> input, ConfigurationSection section) {
        for (Map.Entry<?, ?> entry : input.entrySet()) {
            String key = entry.getKey().toString();
            Object value = entry.getValue();

            if (value instanceof Map) {
                convertMapsToSections((Map<?, ?>) value, section.createSection(key));
            } else {
                section.set(key, value);
            }
        }
    }

    protected void loadComments(String input) {
        String[] lines = input.split("\r?\n", -1);
        String last_node = "";
        for (int i = 0; i < lines.length; i++) {
            /* calculating line node */
            String line = lines[i];
            String line_path = "";
            int indent = indentCount(line);
            String node = "";
            if (i > 0 && line.indexOf(VALUE_INDICATOR) != -1) {
                if (indent >= options().indent()) {
                    for (int j = i - 1; j >= 0; j--) {
                        String line_back = lines[j];
                        String line_back_path = "";
                        int line_back_indent = indentCount(line_back);
                        if (line_back_indent >= indent) {
                            continue;
                        }

                        if (line_back.trim().startsWith(COMMENT_PREFIX)
                                || line_back
                                        .trim()
                                        .startsWith(
                                                com.pepedevs.dbedwars.configuration.util.yaml.YamlConfigurationCommentsOptions
                                                        .PATH_COMMENT_PREFIX)) {
                            continue;
                        }

                        /* father section found */
                        if (line_back_indent == (indent - options().indent())) {
                            line_path =
                                    line.trim().substring(0, line.trim().indexOf(VALUE_INDICATOR));
                            line_back_path =
                                    line_back
                                            .trim()
                                            .substring(
                                                    0, line_back.trim().indexOf(VALUE_INDICATOR));
                            node = line_back_path + options().pathSeparator() + line_path;
                            if (last_node.contains(String.valueOf(options().pathSeparator()))) {
                                if (last_node.endsWith(
                                        options().pathSeparator() + line_back_path)) {
                                    node = last_node + options().pathSeparator() + line_path;
                                }

                                if (last_node
                                        .substring(
                                                0, last_node.lastIndexOf(options().pathSeparator()))
                                        .endsWith(options().pathSeparator() + line_back_path)) {
                                    node =
                                            last_node.substring(
                                                            0,
                                                            last_node.lastIndexOf(
                                                                    options().pathSeparator()))
                                                    + options().pathSeparator()
                                                    + line_path;
                                }
                            }

                            last_node = node;
                            break;
                        }
                    }
                } else {
                    line_path = line.trim().substring(0, line.trim().indexOf(VALUE_INDICATOR));
                    node = line_path;
                    last_node = node;
                }
            }

            if ((i - 1) < 0) {
                continue;
            }

            String comment_line = lines[i - 1];
            if (!comment_line
                            .trim()
                            .startsWith(com.pepedevs.dbedwars.configuration.util.yaml.YamlConfigurationCommentsOptions.PATH_COMMENT_PREFIX)
                    || comment_line.trim().length()
                            == com.pepedevs.dbedwars.configuration.util.yaml.YamlConfigurationCommentsOptions.PATH_COMMENT_PREFIX.length()) {
                continue;
            }

            String comment =
                    comment_line
                            .trim()
                            .substring(
                                    com.pepedevs.dbedwars.configuration.util.yaml.YamlConfigurationCommentsOptions.PATH_COMMENT_PREFIX.length())
                            .trim();
            if (node.lastIndexOf(options().pathSeparator()) == -1) { // root
                // section
                options().comment(this, line_path, comment);
                continue;
            }

            String parent_node = node.substring(0, node.lastIndexOf(options().pathSeparator()));
            if (isConfigurationSection(parent_node) && !isConfigurationSection(node)) { // sub
                // section
                options().comment(getConfigurationSection(parent_node), line_path, comment);
            }
        }
    }

    /**
     * Counts the amount of indent at the start of a line.
     *
     * <p>
     *
     * @param line Line to count
     * @return Amount of indent
     */
    protected int indentCount(String line) {
        int count = 0;
        while (line.startsWith(" ", count)) {
            count++;
        }
        return count;
    }

    @Override
    public com.pepedevs.dbedwars.configuration.util.yaml.YamlConfigurationCommentsOptions options() {
        return (com.pepedevs.dbedwars.configuration.util.yaml.YamlConfigurationCommentsOptions)
                (options == null
                        ? (options = new YamlConfigurationCommentsOptions(this))
                        : options);
    }
}
