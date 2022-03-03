package org.zibble.dbedwars.utils.reflection.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Constructor {

    java.lang.Class<?> clazz();

    String className() default "";

    java.lang.Class<?>[] parameters();

    boolean ignoreExceptions() default true;

}
