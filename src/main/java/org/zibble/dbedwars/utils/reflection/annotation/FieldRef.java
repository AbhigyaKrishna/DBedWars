package org.zibble.dbedwars.utils.reflection.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(FieldRefs.class)
public @interface FieldRef {

    Class<?> clazz() default Object.class;

    String className() default "";

    String[] value();

    VersionControl[] versionPolicy() default {};

    boolean ignoreExceptions() default true;
}
