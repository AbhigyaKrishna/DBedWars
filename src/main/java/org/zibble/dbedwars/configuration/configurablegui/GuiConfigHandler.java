package org.zibble.dbedwars.configuration.configurablegui;

import com.pepedevs.radium.gui.inventory.ItemMenu;
import com.pepedevs.radium.gui.inventory.item.action.ActionItem;
import com.pepedevs.radium.gui.inventory.size.ItemMenuSize;
import com.pepedevs.radium.utils.StringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.zibble.dbedwars.api.exceptions.InvalidGuiFileException;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class GuiConfigHandler {

    public static final char UNIVERSAL_AIR = 'X';

    public static final String TITLE_KEY = "title";
    public static final String PATTERN_KEY = "pattern";
    public static final String ITEM_KEY = "items";

    private File file;
    private FileConfiguration config;

    public GuiConfigHandler(File file) throws InvalidGuiFileException {
        this.file = file;
        this.config = YamlConfiguration.loadConfiguration(file);
        if (!config.contains(TITLE_KEY)
                || !config.contains(PATTERN_KEY)
                || !config.contains(ITEM_KEY)) {
            throw new InvalidGuiFileException(file.getName() + " is not a Gui File!");
        }
    }

    public ItemMenu loadGui(Plugin plugin) {
        String title = config.getString(TITLE_KEY);
        List<char[]> patternRow =
                this.parsePattern(
                        config.getStringList(PATTERN_KEY).stream()
                                .limit(6)
                                .collect(Collectors.toList()));
        Map<String, ActionItem> actionItems =
                this.getActionItems(config.getConfigurationSection(ITEM_KEY));

        ItemMenu menu =
                new ItemMenu(
                        StringUtils.translateAlternateColorCodes(title),
                        ItemMenuSize.fitOf(patternRow.size() * 9),
                        null);
        int n = 0;
        for (char[] chars : patternRow) {
            for (int i = 0; i < 9; i++) {
                if (actionItems.containsKey(String.valueOf(chars[i]))) {
                    menu.setItem(i + n * 9, actionItems.get(String.valueOf(chars[i])));
                }
            }
            n++;
        }

        menu.registerListener(plugin);

        return menu;
    }

    private List<char[]> parsePattern(List<String> pattern) {
        List<char[]> parsed = new ArrayList<>();
        for (String s : pattern) {
            char[] chars = new char[9];
            Arrays.fill(chars, UNIVERSAL_AIR);
            String[] str = s.split(" ");
            for (int j = 0; j < 9; j++) {
                try {
                    chars[j] = str[j].charAt(0);
                } catch (IndexOutOfBoundsException e) {
                    break;
                }
            }
            parsed.add(chars);
        }

        return parsed;
    }

    private Map<String, ActionItem> getActionItems(ConfigurationSection section) {
        Map<String, ActionItem> items = new HashMap<>();
        Set<String> keys = section.getKeys(false);
        ConfigurableGuiItem ci = new ConfigurableGuiItem();
        for (String s : keys) {
            if (items.containsKey(s)) continue;

            ci.load(section.getConfigurationSection(s));
            if (ci.isValid()) items.put(s, ci.toActionItem());
        }

        return items;
    }

    @Override
    public String toString() {
        return "GuiConfigHandler{" +
                "file=" + file +
                ", config=" + config +
                '}';
    }
}
