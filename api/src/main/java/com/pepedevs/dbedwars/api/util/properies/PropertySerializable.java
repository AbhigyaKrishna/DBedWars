package com.pepedevs.dbedwars.api.util.properies;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public interface PropertySerializable {

    NamedProperties toProperties();

    default NamedProperties mapToProperties() {
        Class<?> clazz = this.getClass().asSubclass(PropertySerializable.class);
        NamedProperties.Builder builder = NamedProperties.builder();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            int modifiers = field.getModifiers();
            if (Modifier.isStatic(modifiers)) continue;
            if (Modifier.isTransient(modifiers)) continue;

            String name = field.getName();
            if (field.isAnnotationPresent(PropertyName.class)) {
                PropertyName propertyName = field.getAnnotation(PropertyName.class);
                name = propertyName.value();
            }

            try {
                builder.add(name, field.get(this));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return builder.build();
    }

}
