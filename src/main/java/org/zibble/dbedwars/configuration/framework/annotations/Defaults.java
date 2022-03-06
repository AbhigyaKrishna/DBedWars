package org.zibble.dbedwars.configuration.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

public class Defaults {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Character {
        char value();
        SaveMethod saveMethod() default SaveMethod.NOT_SET;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Byte {
        byte value();
        SaveMethod saveMethod() default SaveMethod.NOT_SET;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Short {
        short value();
        SaveMethod saveMethod() default SaveMethod.NOT_SET;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Float {
        float value();
        SaveMethod saveMethod() default SaveMethod.NOT_SET;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Long {
        long value();
        SaveMethod saveMethod() default SaveMethod.NOT_SET;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Double {
        double value();
        SaveMethod saveMethod() default SaveMethod.NOT_SET;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Boolean {
        boolean value();
        SaveMethod saveMethod() default SaveMethod.NOT_SET;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Integer {
        int value();
        SaveMethod saveMethod() default SaveMethod.NOT_SET;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface String {
        java.lang.String value();
        SaveMethod saveMethod() default SaveMethod.NOT_SET;
    }

    public enum SaveMethod {
        NORMAL,
        NOT_SET,
        NOT_EQUAL
    }

}

