package com.pepedevs.dbedwars.configuration.util.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface LoadableCollectionEntry {

    /**
     * Gets the name of the sub-section where this is located.
     *
     * <p>
     *
     * @return Name of the sub-section where this is located.
     */
    String subsection();
}
