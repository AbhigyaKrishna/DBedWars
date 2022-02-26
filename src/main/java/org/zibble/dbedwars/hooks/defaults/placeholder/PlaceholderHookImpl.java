package org.zibble.dbedwars.hooks.defaults.placeholder;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.hooks.placholder.PlaceholderHook;
import org.zibble.dbedwars.api.hooks.placholder.PlaceholderRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholderHookImpl implements PlaceholderHook {

    public static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("%([^%]+)%");

    private final Map<String, PlaceholderRegistry> REGISTERED_PLACEHOLDERS = new ConcurrentHashMap<>(2);

    @Override
    public void register(PlaceholderRegistry registry) {
        REGISTERED_PLACEHOLDERS.put(registry.getId(), registry);
    }

    @Override
    public String setPlaceholders(Player player, String message) {
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(message);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String str = matcher.group(1);
            String replacement = this.resolve(player, str);
            if (replacement != null) {
                matcher.appendReplacement(buffer, Matcher.quoteReplacement(replacement));
            }
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    @Override
    public List<String> setPlaceholders(Player player, List<String> messages) {
        List<String> result = new ArrayList<>();
        for (String message : messages) {
            result.add(this.setPlaceholders(player, message));
        }
        return result;
    }

    @Override
    public String setPlaceholders(OfflinePlayer player, String message) {
        return this.setPlaceholders(player.getPlayer(), message);
    }

    @Override
    public List<String> setPlaceholders(OfflinePlayer player, List<String> messages) {
        List<String> result = new ArrayList<>();
        for (String message : messages) {
            result.add(this.setPlaceholders(player, message));
        }
        return result;
    }

    private String resolve(Player player, String text) {
        PlaceholderRegistry found = REGISTERED_PLACEHOLDERS.get(text);
        if (found != null) {
            return found.resolve(player, "");
        }

        int index = text.lastIndexOf('_');

        while (index >= 0) {
            String id = text.substring(0, index);
            String arg = text.substring(index + 1);
            found = REGISTERED_PLACEHOLDERS.get(id);
            if (found != null) {
                try {
                    return found.resolve(player, arg);
                } catch (Exception e) {
                    return null;
                }
            }
            index = text.lastIndexOf('_', index - 1);
        }
        return null;
    }

}
