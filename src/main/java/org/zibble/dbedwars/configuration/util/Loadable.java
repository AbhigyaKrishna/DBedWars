package org.zibble.dbedwars.configuration.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.ConfigurationSection;
import org.zibble.dbedwars.api.util.Validable;
import org.zibble.dbedwars.configuration.util.annotations.LoadableCollectionEntry;
import org.zibble.dbedwars.configuration.util.annotations.LoadableEntry;
import org.zibble.dbedwars.api.util.DataType;
import org.zibble.dbedwars.utils.reflection.general.FieldReflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Represents the classes that can be loaded from a {@link ConfigurationSection}.
 *
 * <p>
 *
 * <h1><strong>Note:</strong></h1>
 *
 * Every single class that implements this interface must to have an empty constructor whose will
 * act as 'uninitialized constructor', then the method {@link
 * Loadable#loadEntries(ConfigurationSection)} can create uninitialized instances and initialize it
 * by using the method {@link Loadable#load(ConfigurationSection)}, that may initialize that
 * instance correctly.
 */
public interface Loadable extends Validable {

    /**
     * Load the configuration of this from the given {@link ConfigurationSection}.
     *
     * <p>
     *
     * @param section Section where the configuration is located
     * @return This Object, for chaining
     */
    public Loadable load(ConfigurationSection section);

    /**
     * Load the configuration of every single field in this class from the given {@link
     * ConfigurationSection} that has the annotation {@link LoadableEntry}/{@link
     * LoadableCollectionEntry} present.
     *
     * <p>
     *
     * @param section Section where the configuration is located
     * @return This Object, for chaining
     */
    @SuppressWarnings("unchecked")
    public default Loadable loadEntries(ConfigurationSection section) {
        Validate.notNull(section, "The section cannot be null!");

        List<Field> entries = new ArrayList<>();

        // finding out entries
        Class<? extends Loadable> current = getClass().asSubclass(Loadable.class);

        while (current != null) {
            // extracting fields
            for (Field field : current.getDeclaredFields()) {
                if (entries.contains(field)) {
                    continue;
                } else {
                    entries.add(field);
                }
            }

            // finding out next loadable class from super classes
            Class<?> super_clazz = current.getSuperclass();

            if (Loadable.class.isAssignableFrom(super_clazz)) {
                current = super_clazz.asSubclass(Loadable.class);
            } else {
                current = null;
            }
        }

        // then loading
        for (Field entry : entries) {
            ConfigurationSection sub_section = null;
            if (entry.isAnnotationPresent(LoadableEntry.class)) {
                if (Modifier.isFinal(entry.getModifiers())) {
                    throw new UnsupportedOperationException(
                            "The loadable entry '" + entry.getName() + "' cannot be final!");
                }

                LoadableEntry options = entry.getAnnotation(LoadableEntry.class);
                boolean loadable = Loadable.class.isAssignableFrom(entry.getType());
                boolean blank_key = StringUtils.isBlank(options.key());
                ConfigurationSection destiny_section =
                        (!StringUtils.isBlank(options.subsection())
                                ? section.getConfigurationSection(options.subsection())
                                : null);

                if (blank_key && !loadable) {
                    throw new UnsupportedOperationException(
                            "Only the entries of type '"
                                    + Loadable.class.getName()
                                    + "' can be loadaed from a ConfigurationSection!");
                }

                Object getted = null;
                if (blank_key) {
                    if (destiny_section != null) {
                        try {
                            Constructor<?> uninitialized_constructor =
                                    entry.getType().getConstructor();
                            if (uninitialized_constructor == null) {
                                throw new UnsupportedOperationException(
                                        "A new instance of '"
                                                + entry.getType().getSimpleName()
                                                + "' couldn't be created, because there is not an"
                                                + " empty constructor within that class!");
                            }

                            uninitialized_constructor.setAccessible(true);
                            try {
                                getted =
                                        uninitialized_constructor
                                                .newInstance(); // create uninitialized instance
                                getted = ((Loadable) getted).load(destiny_section);
                            } catch (InstantiationException
                                    | IllegalAccessException
                                    | IllegalArgumentException
                                    | InvocationTargetException e) {
                                throw new UnsupportedOperationException(
                                        "A new instance of '"
                                                + entry.getType().getSimpleName()
                                                + "' couldn't be created: ",
                                        e);
                            }
                        } catch (NoSuchMethodException | SecurityException e) {
                            /* ignore */
                        }
                    }
                } else {
                    //					System.out.println ( "Loadable: load: destiny_section = " +
                    // destiny_section );
                    if (destiny_section != null) {
                        getted = destiny_section.get(options.key(), null);
                    } else {
                        getted = section.get(options.key());
                        //						System.out.println ( "Loadable: load: getted = " + getted );
                        //						System.out.println ( "Loadable: load: options.key ( ) = " +
                        // options.key ( ) );
                        //						System.out.println ( "Loadable: load: section keys = " +
                        // section.getKeys ( false ) );
                    }
                }

                if (getted != null) {
                    DataType getted_type = DataType.fromClass(getted.getClass());
                    DataType entry_type = DataType.fromClass(entry.getType());

                    //					System.out.println ( "entry.getType().isAssignableFrom(getted.getClass())
                    // = " + entry.getType().isAssignableFrom(getted.getClass()) );
                    //					System.out.println ( "DataType.fromClass(entry.getType()) = " +
                    // DataType.fromClass(entry.getType()) );
                    //					System.out.println ( "DataType.fromClass(getted.getClass()) = " +
                    // DataType.fromClass(getted.getClass()) );
                    //					System.out.println ( "DataType.fromClass(entry.getType()) ==
                    // DataType.fromClass(getted.getClass()) = " + (
                    // DataType.fromClass(entry.getType()) == DataType.fromClass(getted.getClass())
                    // ) );

                    if (entry.getType().isAssignableFrom(getted.getClass())
                            || entry_type == getted_type
                            || (getted_type.isNumber() && entry_type.isNumber())) {
                        try {
                            FieldReflection.setValue(this, entry.getName(), getted);
                        } catch (SecurityException
                                | NoSuchFieldException
                                | IllegalAccessException e) {
                            throw new IllegalStateException(
                                    "cannot load the value to the field '" + entry.getName() + "'");
                        }
                    }
                }
            }
            else if (entry.isAnnotationPresent(LoadableCollectionEntry.class)) {
                if (!(Collection.class.isAssignableFrom(entry.getType())
                        || Map.class.isAssignableFrom(entry.getType()))) {
                    throw new UnsupportedOperationException(
                            "The loadable collection entry '"
                                    + entry.getName()
                                    + "' is not a valid instance of '"
                                    + Collection.class.getName()
                                    + "' or '"
                                    + Map.class.getName()
                                    + "'!");
                }

                Object value = null;
                try {
                    value = FieldReflection.getValue(this, entry.getName());
                } catch (SecurityException
                        | NoSuchFieldException
                        | IllegalArgumentException
                        | IllegalAccessException e1) {
                    throw new IllegalStateException(
                            "Cannot get the value of the field '" + entry.getName() + "'");
                }

                if (value == null) {
                    throw new UnsupportedOperationException(
                            "The loadable collection entry '"
                                    + entry.getName()
                                    + "' must be already initialized!");
                }

                if (Collection.class.isAssignableFrom(entry.getType())) {
                    Collection<Loadable> collection = ((Collection<Loadable>) value);
                    LoadableCollectionEntry options =
                            entry.getAnnotation(LoadableCollectionEntry.class);
                    if (!Loadable.class.isAssignableFrom(
                            FieldReflection.getParameterizedTypesClasses(entry)[0])) {
                        throw new UnsupportedOperationException(
                                "The elements type of the loadable collection entry '"
                                        + entry.getName()
                                        + "' must be of the type '"
                                        + Loadable.class.getName()
                                        + "'!");
                    }

                    Class<? extends Loadable> element_type =
                            (Class<? extends Loadable>)
                                    FieldReflection.getParameterizedTypesClasses(entry)[0];
                    Constructor<?> uninitialized_constructor = null;
                    try {
                        uninitialized_constructor = element_type.getConstructor();
                        if (uninitialized_constructor == null) {
                            throw new UnsupportedOperationException(
                                    "A new instance of '"
                                            + element_type.getSimpleName()
                                            + "' couldn't be created, because there is not an empty"
                                            + " constructor within that class!");
                        }
                        uninitialized_constructor.setAccessible(true);
                    } catch (NoSuchMethodException | SecurityException e) {
                        /* ignore */
                    }

                    sub_section =
                            !StringUtils.isBlank(options.subsection())
                                    ? section.getConfigurationSection(options.subsection())
                                    : null;
                    for (String key :
                            (sub_section != null ? sub_section : section).getKeys(false)) {
                        ConfigurationSection element_section =
                                (sub_section != null ? sub_section : section)
                                        .getConfigurationSection(key);
                        if (element_section == null) {
                            continue;
                        }

                        try {
                            collection.add(
                                    ((Loadable) uninitialized_constructor.newInstance())
                                            .load(element_section)); // don't skip invalids
                            //						Loadable element = ((Loadable)
                            // uninitialized_constructor.newInstance()).load(element_section);
                            //						if (element.isValid()) { // skip invalids
                            //							collection.add(element);
                            //						}
                        } catch (Throwable t) {
                            /* ignore */
                        }
                    }
                }
                else if (Map.class.isAssignableFrom(entry.getType())) {
                    Map<String, Loadable> map = (Map<String, Loadable>) value;
                    LoadableCollectionEntry options =
                            entry.getAnnotation(LoadableCollectionEntry.class);
                    Class<?>[] arr = FieldReflection.getParameterizedTypesClasses(entry);
                    if (!String.class.isAssignableFrom(arr[0])
                            || !Loadable.class.isAssignableFrom(arr[1])) {
                        throw new UnsupportedOperationException(
                                "The key and value elements type of the loadable collection entry '"
                                        + entry.getName()
                                        + "' must be of the type '"
                                        + String.class.getName()
                                        + "' and '"
                                        + Loadable.class.getName()
                                        + "'!");
                    }

                    Class<? extends Loadable> element_type = (Class<? extends Loadable>) arr[1];
                    Constructor<?> uninitialized_constructor = null;
                    try {
                        uninitialized_constructor = element_type.getConstructor();
                        if (uninitialized_constructor == null) {
                            throw new UnsupportedOperationException(
                                    "A new instance of '"
                                            + element_type.getSimpleName()
                                            + "' couldn't be created, because there is not an empty"
                                            + " constructor within that class!");
                        }
                        uninitialized_constructor.setAccessible(true);
                    } catch (NoSuchMethodException | SecurityException e) {
                        /* ignore */
                    }

                    sub_section =
                            !StringUtils.isBlank(options.subsection())
                                    ? section.getConfigurationSection(options.subsection())
                                    : null;

                    for (String key :
                            (sub_section != null ? sub_section : section).getKeys(false)) {
                        ConfigurationSection element_section =
                                (sub_section != null ? sub_section : section)
                                        .getConfigurationSection(key);
                        if (element_section == null) {
                            continue;
                        }

                        try {
                            map.put(
                                    key,
                                    ((Loadable) uninitialized_constructor.newInstance())
                                            .load(element_section));
                        } catch (Throwable t) {
                            /* ignore */
                        }
                    }
                }
            }
        }
        return this;
    }
}
