package org.zibble.dbedwars.utils.reflection.resolver.minecraft;

import org.zibble.dbedwars.api.version.Version;
import org.zibble.dbedwars.utils.reflection.resolver.ClassResolver;

public class NMSClassResolver extends ClassResolver {

    @Override
    public Class resolve(String... names) throws ClassNotFoundException {
        for (int i = 0; i < names.length; i++) {
            if (names[i].startsWith("net.minecraft")) continue;

            /* use the whole name */
            names[i] = Version.SERVER_VERSION.getNmsPackage() + "." + names[i];
        }
        return super.resolve(names);
    }
}
