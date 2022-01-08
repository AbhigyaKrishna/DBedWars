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

    static PlaceholderEntry of(String placeholder, String replacement) {
        return new PlaceholderEntry() {
            @Override
            public String getPlaceholder() {
                return placeholder;
            }

            @Override
            public Supplier<String> getReplacement() {
                return new Supplier<String>() {
                    @Override
                    public String get() {
                        return replacement;
                    }
                };
            }
        };
    }

    static PlaceholderEntry symbol(String placeholder, String replacement) {
        return of("<".concat(placeholder).concat(">"), replacement);
    }

    static PlaceholderEntry symbol(String placeholder, Supplier<String> replacementSupplier) {
        return of("<".concat(placeholder).concat(">"), replacementSupplier);
    }

    String getPlaceholder();

    Supplier<String> getReplacement();
}
