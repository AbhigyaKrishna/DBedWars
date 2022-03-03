package org.zibble.dbedwars.utils.reflection.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ConstructorRefs.class)
public @interface ConstructorRef {

    Class<?> clazz() default Object.class;

    String className() default "";

    Class<?>[] parameters();

    VersionControl[] versionPolicy() default {};

    boolean ignoreExceptions() default true;

}
