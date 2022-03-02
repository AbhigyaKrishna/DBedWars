package org.zibble.dbedwars.utils.reflection.resolver;

import org.zibble.dbedwars.utils.reflection.resolver.wrapper.ConstructorWrapper;

import java.lang.reflect.Constructor;

public class ConstructorResolver extends MemberResolver<Constructor> {

    public ConstructorResolver(Class<?> clazz) {
        super(clazz);
    }

    public ConstructorResolver(String className) throws ClassNotFoundException {
        super(className);
    }

    @Override
    public Constructor resolveIndex(int index) throws IndexOutOfBoundsException, ReflectiveOperationException {
        Constructor<?> constructor = this.clazz.getDeclaredConstructors()[index];
        constructor.setAccessible(true);
        return constructor;
    }

    @Override
    public Constructor resolveIndexSilent(int index) {
        try {
            return this.resolveIndex(index);
        } catch (IndexOutOfBoundsException | ReflectiveOperationException ignored) {
        }
        return null;
    }

    @Override
    public ConstructorWrapper resolveIndexWrapper(int index) {
        return new ConstructorWrapper<>(this.resolveIndexSilent(index));
    }

    public ConstructorWrapper resolveWrapper(Class<?>[]... types) {
        return new ConstructorWrapper<>(this.resolveSilent(types));
    }

    public Constructor resolveSilent(Class<?>[]... types) {
        try {
            return this.resolve(types);
        } catch (NoSuchMethodException ignored) {
        }
        return null;
    }

    public Constructor resolve(Class<?>[]... types) throws NoSuchMethodException {
        ResolverQuery.Builder builder = ResolverQuery.builder();
        for (Class<?>[] type : types) builder.with(type);
        try {
            return super.resolve(builder.build());
        } catch (ReflectiveOperationException e) {
            throw (NoSuchMethodException) e;
        }
    }

    @Override
    protected Constructor resolveObject(ResolverQuery query) throws ReflectiveOperationException {
        Constructor<?> constructor = this.clazz.getDeclaredConstructor(query.getTypes());
        constructor.setAccessible(true);
        return constructor;
    }

    public Constructor resolveFirstConstructor() throws ReflectiveOperationException {
        for (Constructor<?> constructor : this.clazz.getDeclaredConstructors()) {
            constructor.setAccessible(true);
            return constructor;
        }
        return null;
    }

    public Constructor resolveFirstConstructorSilent() {
        try {
            return this.resolveFirstConstructor();
        } catch (Exception ignored) {
        }
        return null;
    }

    public Constructor resolveLastConstructor() throws ReflectiveOperationException {
        Constructor<?> constructor = null;
        for (Constructor<?> constructor1 : this.clazz.getDeclaredConstructors()) {
            constructor = constructor1;
        }
        if (constructor != null) {
            constructor.setAccessible(true);
            return constructor;
        }
        return null;
    }

    public Constructor resolveLastConstructorSilent() {
        try {
            return this.resolveLastConstructor();
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    protected NoSuchMethodException notFoundException(String joinedNames) {
        return new NoSuchMethodException("Could not resolve constructor for " + joinedNames + " in class " + this.clazz);
    }
}
