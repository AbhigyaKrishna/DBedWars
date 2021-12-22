package com.pepedevs.dbedwars.api.messaging;

import java.util.function.Supplier;

public interface PlaceholderEntry {

    static PlaceholderEntry of(String placeholder, Supplier<String> replacementSupplier) {
        return new PlaceholderEntry() {
            @Override
            public String getPlaceholder() {
                return placeholder;
            }

            @Override
            public Supplier<String> getReplacement() {
                return replacementSupplier;
            }
        };
    }

    String getPlaceholder();

    Supplier<String> getReplacement();
}
