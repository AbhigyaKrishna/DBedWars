package com.pepedevs.dbedwars.api.messaging;

import com.pepedevs.dbedwars.api.util.Key;

import java.util.function.Supplier;

public interface PlaceholderEntry extends Placeholder {

    static PlaceholderEntry of(String placeholder, Supplier<String> replacement) {
        return new PlaceholderEntry() {
            @Override
            public Key<String> getKey() {
                return Key.of(placeholder);
            }

            @Override
            public Supplier<String> getPlaceholder() {
                return replacement;
            }

        };
    }

    static PlaceholderEntry of(String placeholder, String replacement) {
        return new PlaceholderEntry() {
            @Override
            public Key<String> getKey() {
                return Key.of(placeholder);
            }

            @Override
            public Supplier<String> getPlaceholder() {
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

    @Override
    Key<String> getKey();

    Supplier<String> getPlaceholder();

    default String apply(String message) {
        return message.replace(this.getKey().get(), this.getPlaceholder().get());
    }
}
