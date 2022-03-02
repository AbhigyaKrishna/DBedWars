package org.zibble.dbedwars.utils.reflection.annotation;

import com.pepedevs.radium.utils.version.Version;
import org.zibble.dbedwars.utils.reflection.resolver.wrapper.MethodWrapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Resolves the annotated {@link MethodWrapper} or {@link java.lang.reflect.Method} field to the
 * first matching method name.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Method {

    /**
     * Name of the class to load this method from
     *
     * @return name of the class
     */
    String className();

    /**
     * Possible names of the method. Use <code>&gt;</code> or <code>&lt;</code> as a name prefix in
     * combination with {@link #versions()} to specify versions newer- or older-than.
     *
     * @return method names
     */
    String[] value();

    /**
     * Specific versions for the names.
     *
     * @return Array of versions for the class names
     */
    Version[] versions() default {};

    /**
     * Whether to ignore any org.zibble.dbedwars.utils.reflection exceptions thrown. Defaults to
     * <code>true</code>
     *
     * @return whether to ignore exceptions
     */
    boolean ignoreExceptions() default true;
}
