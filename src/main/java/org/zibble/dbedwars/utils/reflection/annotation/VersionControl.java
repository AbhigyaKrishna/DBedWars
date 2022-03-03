package org.zibble.dbedwars.utils.reflection.annotation;

import org.zibble.dbedwars.api.version.Version;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface VersionControl {

    Version value();

    boolean checkEquals() default false;

    boolean checkEqualRevision() default false;

    boolean checkNewer() default false;

    boolean checkNewerEqual() default false;

    boolean checkOlder() default false;

    boolean checkOlderEqual() default false;

}
