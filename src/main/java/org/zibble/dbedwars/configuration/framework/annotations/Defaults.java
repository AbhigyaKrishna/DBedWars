package org.zibble.dbedwars.configuration.framework.annotations;

import org.zibble.dbedwars.configuration.framework.SaveActionType;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

public class Defaults {

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

}

