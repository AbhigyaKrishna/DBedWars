package org.zibble.dbedwars.configuration.framework.annotations;

import com.google.common.collect.ImmutableSet;
import org.zibble.dbedwars.configuration.framework.SaveActionType;

import java.lang.annotation.*;
import java.util.Set;

public class Defaults {

    public static final Set<Class<? extends Annotation>> DEFAULT_TYPES = ImmutableSet.of(Character.class, Byte.class, Short.class, String.class, Integer.class, Long.class, Double.class, Boolean.class, Variable.class);

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Character {

        char value();

        SaveActionType saveAction() default SaveActionType.NOT_SET;

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Byte {

        byte value();

        SaveActionType saveAction() default SaveActionType.NOT_SET;

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Short {

        short value();

        SaveActionType saveAction() default SaveActionType.NOT_SET;

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Float {

        float value();

        SaveActionType saveAction() default SaveActionType.NOT_SET;

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Long {

        long value();

        SaveActionType saveAction() default SaveActionType.NOT_SET;

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Double {

        double value();

        SaveActionType saveAction() default SaveActionType.NOT_SET;

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Boolean {

        boolean value();

        SaveActionType saveAction() default SaveActionType.NOT_SET;

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Integer {

        int value();

        SaveActionType saveAction() default SaveActionType.NOT_SET;

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface String {

        java.lang.String value();

        SaveActionType saveAction() default SaveActionType.NOT_SET;

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Variable {

        java.lang.String value();

        SaveActionType saveAction() default SaveActionType.NOT_SET;

    }

}

