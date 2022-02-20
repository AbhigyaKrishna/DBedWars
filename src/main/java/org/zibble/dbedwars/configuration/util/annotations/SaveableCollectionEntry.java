package org.zibble.dbedwars.configuration.util.annotations;

import org.apache.commons.lang.StringUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SaveableCollectionEntry {

    /**
     * Gets the name of the sub-section where this will be saved.
     *
     * <p>
     *
     * @return Name of the sub-section where this will be saved.
     */
    String subsection();

    /**
     * Gets the prefix for the name of sub-section where this will be saved.
     *
     * <p>
     *
     * @return Prefix for the name of sub-section where this will be saved.
     */
    String subsectionprefix() default StringUtils.EMPTY;
}
