package org.zibble.dbedwars.action;

import org.zibble.dbedwars.api.action.ActionPlaceholder;
import org.zibble.dbedwars.api.messaging.PlaceholderEntry;
import org.zibble.dbedwars.api.util.Key;

public class ActionUtil {

    public static ActionPlaceholder<String, PlaceholderEntry> getAsKeyedPlaceholder(PlaceholderEntry entry) {
        return new ActionPlaceholder<String, PlaceholderEntry>() {
            @Override
            public Key<String> getKey() {
                return Key.of("PLACEHOLDER");
            }

            @Override
            public PlaceholderEntry getValue() {
                return entry;
            }
        };
    }

    public static <T> ActionPlaceholder<Void, T> getAsVoidPlaceholder(T value) {
        return new ActionPlaceholder<Void, T>() {
            @Override
            public Key<Void> getKey() {
                return null;
            }

            @Override
            public T getValue() {
                return value;
            }
        };
    }

}
