package com.pepedevs.dbedwars.api.action;

import com.pepedevs.dbedwars.api.util.Key;
import com.pepedevs.dbedwars.api.util.Keyed;

public interface ActionPlaceholder<K, R> extends Keyed<K> {

    /*static <A, B> ActionPlaceholder<A ,B> create() {
        return new ActionPlaceholder<A, B>() {
            @Override
            public String forTranslator() {
                return "";
            }

            @Override
            public Key<A> getKey() {
                return null;
            }

            @Override
            public R getPlaceholder() {
                return null;
            }
        };
    }*/

    String forTranslator();

    Key<K> getKey();

    R getPlaceholder();

}
