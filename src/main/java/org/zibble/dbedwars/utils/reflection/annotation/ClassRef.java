package org.zibble.dbedwars.utils.reflection.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ClassRefs.class)
public @interface ClassRef {

    String[] value();

    VersionControl[] versionPolicy() default {};

    boolean ignoreExceptions() default true;

}
