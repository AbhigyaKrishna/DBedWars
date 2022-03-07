package org.zibble.dbedwars.configuration.framework.annotations;

import org.zibble.dbedwars.configuration.framework.SaveActionType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigPath {

    String value();

    String subSection() default "";

    ConfigType[] type() default {ConfigType.SAVEABLE, ConfigType.LOADABLE};

    SaveActionType saveAction() default SaveActionType.NORMAL;

    enum ConfigType {

        SAVEABLE,
        LOADABLE,
        ;

    }

}
