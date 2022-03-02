package org.zibble.dbedwars.utils.reflection.resolver;

import org.zibble.dbedwars.utils.reflection.resolver.wrapper.ClassWrapper;

public class ClassResolver extends ResolverAbstract<Class> {

    public ClassWrapper resolveWrapper(String... names) {
        return new ClassWrapper<>(this.resolveSilent(names));
    }

    public Class resolveSilent(String... names) {
        try {
            return this.resolve(names);
        } catch (ClassNotFoundException ignored) {
        }
        return null;
    }

    public Class resolve(String... names) throws ClassNotFoundException {
        ResolverQuery.Builder builder = ResolverQuery.builder();
        for (String name : names) builder.with(name);
        try {
            return super.resolve(builder.build());
        } catch (ReflectiveOperationException e) {
            throw (ClassNotFoundException) e;
        }
    }

    @Override
    protected Class resolveObject(ResolverQuery query) throws ReflectiveOperationException {
        return Class.forName(query.getName());
    }

    @Override
    protected ClassNotFoundException notFoundException(String joinedNames) {
        return new ClassNotFoundException("Could not resolve class for " + joinedNames);
    }
}
