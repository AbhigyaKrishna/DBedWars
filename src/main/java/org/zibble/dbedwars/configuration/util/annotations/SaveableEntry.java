package org.zibble.dbedwars.configuration.util.annotations;

import org.apache.commons.lang.StringUtils;
import org.zibble.dbedwars.configuration.framework.SaveActionType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SaveableEntry {

    String key();

    SaveActionType action() default SaveActionType.NORMAL;

    /**
     * Gets the name of the sub-section where this will be saved on.
     *
     * <p>
     *
     * @return Name of the sub-section where this will be saved.
     */
    String subsection() default StringUtils.EMPTY;
}
