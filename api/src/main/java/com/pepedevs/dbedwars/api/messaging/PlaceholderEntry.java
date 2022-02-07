package com.pepedevs.dbedwars.api.messaging;

import java.util.function.Supplier;

public interface PlaceholderEntry {

    static PlaceholderEntry of(String placeholder, Supplier<String> replacement) {
        return new PlaceholderEntry() {
            @Override
            public String getPlaceholder() {
                return placeholder;
            }

            @Override
            public Supplier<String> getReplacement() {
                return replacement;
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

    static PlaceholderEntry symbol(String placeholder, Supplier<String> replacement) {
        return of("<".concat(placeholder).concat(">"), replacement);
    }

    String getPlaceholder();

    Supplier<String> getReplacement();

    default String apply(String message) {
        return message.replace(this.getPlaceholder(), this.getReplacement().get());
    }
}
