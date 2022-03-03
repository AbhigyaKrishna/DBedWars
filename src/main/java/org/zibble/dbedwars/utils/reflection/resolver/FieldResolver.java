package org.zibble.dbedwars.utils.reflection.resolver;

import org.zibble.dbedwars.utils.reflection.resolver.wrapper.FieldWrapper;

import java.lang.reflect.Field;

public class FieldResolver extends MemberResolver<Field> {

    public FieldResolver(Class<?> clazz) {
        super(clazz);
    }

    public FieldResolver(String className) throws ClassNotFoundException {
        super(className);
    }

    @Override
    public Field resolveIndex(int index) throws IndexOutOfBoundsException, ReflectiveOperationException {
        Field field = this.clazz.getDeclaredFields()[index];
        field.setAccessible(true);
        return field;
    }

    @Override
    public Field resolveIndexSilent(int index) {
        try {
            return this.resolveIndex(index);
        } catch (IndexOutOfBoundsException | ReflectiveOperationException ignored) {
        }
        return null;
    }

    @Override
    public FieldWrapper resolveIndexWrapper(int index) {
        return new FieldWrapper(this.resolveIndexSilent(index));
    }

    public FieldWrapper resolveWrapper(String... names) {
        return new FieldWrapper(this.resolveSilent(names));
    }

    public FieldWrapper resolveWrapper(ResolverQuery... queries) {
        return new FieldWrapper(this.resolveSilent(queries));
    }

    public Field resolveSilent(String... names) {
        try {
            return resolve(names);
        } catch (NoSuchFieldException ignored) {
        }
        return null;
    }

    public Field resolve(String... names) throws NoSuchFieldException {
        ResolverQuery.Builder builder = ResolverQuery.builder();
        for (String name : names) builder.with(name);
        try {
            return super.resolve(builder.build());
        } catch (ReflectiveOperationException e) {
            throw (NoSuchFieldException) e;
        }
    }

    public Field resolveSilent(ResolverQuery... queries) {
        try {
            return this.resolve(queries);
        } catch (NoSuchFieldException ignored) {
        }
        return null;
    }

    public Field resolve(ResolverQuery... queries) throws NoSuchFieldException {
        try {
            return super.resolve(queries);
        } catch (ReflectiveOperationException e) {
            throw (NoSuchFieldException) e;
        }
    }

    @Override
    protected Field resolveObject(ResolverQuery query) throws ReflectiveOperationException {
        if (query.getTypes() == null || query.getTypes().length == 0) {
            Field field;
            try {
                field = this.clazz.getDeclaredField(query.getName());
            } catch (NoSuchFieldException e) {
                field = this.clazz.getField(query.getName());
            }
            field.setAccessible(true);
            return field;
        } else {
            for (Field field : this.clazz.getDeclaredFields()) {
                if (field.getName().equals(query.getName())) {
                    for (Class<?> type : query.getTypes()) {
                        if (field.getType().equals(type)) {
                            return field;
                        }
                    }
                }
            }
        }
        throw new NoSuchFieldException("No field found with the name `" + query.getName() + "`");
    }

    /**
     * Attempts to find the first field of the specified type
     *
     * @param type Type to find
     * @return the Field
     * @throws ReflectiveOperationException (usually never)
     * @see #resolveByLastType(Class)
     */
    public Field resolveByFirstType(Class<?> type) throws ReflectiveOperationException {
        for (Field field : this.clazz.getDeclaredFields()) {
            if (field.getType().equals(type)) {
                field.setAccessible(true);
                return field;
            }
        }
        throw new NoSuchFieldException("Could not resolve field of type '" + type.toString() + "' in class " + this.clazz);
    }

    public FieldWrapper resolveByFirstTypeAccessor(Class<?> type) {
        return new FieldWrapper(this.resolveByFirstTypeSilent(type));
    }

    /**
     * Attempts to find the first field of the specified type
     *
     * @param type Type to find
     * @return the Field
     * @see #resolveByLastTypeSilent(Class)
     */
    public Field resolveByFirstTypeSilent(Class<?> type) {
        try {
            return this.resolveByFirstType(type);
        } catch (ReflectiveOperationException ignored) {
        }
        return null;
    }

    public Field resolveByFirstExtendingType(Class<?> type) throws ReflectiveOperationException {
        for (Field field : this.clazz.getDeclaredFields()) {
            if (type.isAssignableFrom(field.getType())) {
                field.setAccessible(true);
                return field;
            }
        }
        throw new NoSuchFieldException("Could not resolve field of type '" + type.toString() + "' in class " + this.clazz);
    }

    public Field resolveByFirstExtendingTypeSilent(Class<?> type) {
        try {
            return this.resolveByFirstExtendingType(type);
        } catch (ReflectiveOperationException ignored) {
        }
        return null;
    }

    public FieldWrapper resolveByFirstExtendingTypeAccessor(Class<?> type) {
        return new FieldWrapper(this.resolveByFirstExtendingTypeSilent(type));
    }

    public Field resolveByLastType(Class<?> type) throws ReflectiveOperationException {
        Field field = null;
        for (Field field1 : this.clazz.getDeclaredFields()) {
            if (field1.getType().equals(type)) {
                field = field1;
            }
        }
        if (field == null) {
            throw new NoSuchFieldException("Could not resolve field of type '" + type.toString() + "' in class " + this.clazz);
        }
        field.setAccessible(true);
        return field;
    }

    public Field resolveByLastTypeSilent(Class<?> type) {
        try {
            return this.resolveByLastType(type);
        } catch (ReflectiveOperationException ignored) {
        }
        return null;
    }

    public FieldWrapper resolveByLastTypeAccessor(Class<?> type) {
        return new FieldWrapper(resolveByLastTypeSilent(type));
    }

    public Field resolveByLastExtendingType(Class<?> type) throws ReflectiveOperationException {
        Field field = null;
        for (Field field1 : this.clazz.getDeclaredFields()) {
            if (type.isAssignableFrom(field1.getType())) {
                field = field1;
            }
        }
        if (field == null) {
            throw new NoSuchFieldException("Could not resolve field of type '" + type.toString() + "' in class " + this.clazz);
        }
        field.setAccessible(true);
        return field;
    }

    public Field resolveByLastExtendingTypeSilent(Class<?> type) {
        try {
            return this.resolveByLastExtendingType(type);
        } catch (ReflectiveOperationException ignored) {
        }
        return null;
    }

    public FieldWrapper resolveByLastExtendingTypeAccessor(Class<?> type) {
        return new FieldWrapper(this.resolveByLastExtendingTypeSilent(type));
    }

    @Override
    protected NoSuchFieldException notFoundException(String joinedNames) {
        return new NoSuchFieldException("Could not resolve field for " + joinedNames + " in class " + this.clazz);
    }

}
