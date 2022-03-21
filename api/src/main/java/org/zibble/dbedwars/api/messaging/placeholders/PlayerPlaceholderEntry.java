package org.zibble.dbedwars.api.messaging.placeholders;

import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.util.Key;

import java.util.function.Function;

public interface PlayerPlaceholderEntry extends Placeholder {

    static PlayerPlaceholderEntry of(String placeholder, Function<Player, String> replacement) {
        return new PlayerPlaceholderEntry() {
            @Override
            public Key<String> getKey() {
                return Key.of(placeholder);
            }

            @Override
            public String getPlaceholder(Player player) {
                return replacement.apply(player);
            }

        };
    }

    static PlayerPlaceholderEntry of(String placeholder, String replacement) {
        return new PlayerPlaceholderEntry() {
            @Override
            public Key<String> getKey() {
                return Key.of(placeholder);
            }

            @Override
            public String getPlaceholder(Player player) {
                return replacement;
            }

        };
    }

    static PlayerPlaceholderEntry symbol(String placeholder, String replacement) {
        return of("<".concat(placeholder).concat(">"), replacement);
    }

    static PlayerPlaceholderEntry symbol(String placeholder, Function<Player, String> replacement) {
        return of("<".concat(placeholder).concat(">"), replacement);
    }

    @Override
    Key<String> getKey();

    String getPlaceholder(Player player);

    default String apply(String message, Player player) {
        return message.replace(this.getKey().get(), this.getPlaceholder(player));
    }

}
