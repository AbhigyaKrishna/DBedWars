package org.zibble.dbedwars.commands.framework;

import com.google.common.collect.ImmutableMap;
import org.reflections.Reflections;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.DBedWarsAPI;
import org.zibble.dbedwars.api.commands.annotations.ParentCommandNode;
import org.zibble.dbedwars.api.commands.annotations.Permission;
import org.zibble.dbedwars.api.commands.annotations.SubCommandNode;
import org.zibble.dbedwars.api.commands.nodes.AbstractCommandNode;
import org.zibble.dbedwars.api.util.Pair;
import org.zibble.dbedwars.utils.reflection.resolver.wrapper.ConstructorWrapper;

import java.lang.reflect.Constructor;
import java.util.*;

public class AutoRegistryHandler {

    private static final List<String> PACKAGES = new ArrayList<>(1);

    private final Map<Class<?>, Object> availableParameters;

    public AutoRegistryHandler(DBedwars plugin) {
        this.availableParameters = ImmutableMap.<Class<?>, Object>builder()
                .put(DBedwars.class, plugin)
                .put(DBedWarsAPI.class, DBedWarsAPI.getApi())
                .build();
    }

    public Map<Pair<String, String[]>, ? extends AbstractCommandNode> baseNodes() {
        Map<Pair<String, String[]>, AbstractCommandNode> returnVal = new HashMap<>();
        Set<Class<?>> classes = new HashSet<>();
        for (String pkg : PACKAGES) {
            Reflections reflections = new Reflections(pkg);
            classes.addAll(reflections.getTypesAnnotatedWith(ParentCommandNode.class));
        }
        for (Class<?> clazz : classes) {
            for (Constructor<?> constructor : clazz.getConstructors()) {
                ConstructorWrapper<?> wrapper = new ConstructorWrapper(constructor);
                boolean canMatch = true;
                for (Class<?> type : wrapper.getParameterTypes()) {
                    if (this.availableParameters.containsKey(type)) continue;
                    canMatch = false;
                    break;
                }
                if (!canMatch) continue;
                Object[] parameters = new Class[wrapper.getParameterTypes().length];
                for (int i = 0; i < wrapper.getParameterTypes().length; i++) {
                    parameters[i] = this.availableParameters.get(wrapper.getParameterTypes()[i]);
                }
                AbstractCommandNode node = (AbstractCommandNode) wrapper.newInstance(parameters);
                ParentCommandNode parent = clazz.getAnnotation(ParentCommandNode.class);
                returnVal.put(new Pair<>(parent.value(), parent.aliases()), node);
                break;
            }
        }
        return returnVal;
    }

    public void subNodes(CommandRegistryImpl registry) {
        Set<Class<?>> classes = new HashSet<>();
        for (String aPackage : PACKAGES) {
            Reflections reflections = new Reflections(aPackage);
            classes.addAll(reflections.getTypesAnnotatedWith(SubCommandNode.class));
        }
        List<Class<?>> sortedClasses = new ArrayList<>(classes);

        sortedClasses.sort((a, b) -> {
            SubCommandNode o1 = a.getAnnotation(SubCommandNode.class);
            SubCommandNode o2 = b.getAnnotation(SubCommandNode.class);
            if (o1 == null || o2 == null) throw new IllegalArgumentException("Classes must be annotated with @SubCommandNode");
            String[] aRoute = o1.parent().split("\\.");
            String[] bRoute = o2.parent().split("\\.");
            return Integer.compare(aRoute.length, bRoute.length);
        });

        for (Class<?> clazz : sortedClasses) {
            for (Constructor<?> constructor : clazz.getConstructors()) {
                ConstructorWrapper<?> wrapper = new ConstructorWrapper(constructor);
                boolean canMatch = true;
                for (Class<?> type : wrapper.getParameterTypes()) {
                    if (this.availableParameters.containsKey(type)) continue;
                    canMatch = false;
                    break;
                }
                if (!canMatch) continue;
                Object[] parameters = new Class[wrapper.getParameterTypes().length];
                for (int i = 0; i < wrapper.getParameterTypes().length; i++) {
                    parameters[i] = this.availableParameters.get(wrapper.getParameterTypes()[i]);
                }
                AbstractCommandNode node = (AbstractCommandNode) wrapper.newInstance(parameters);
                SubCommandNode subCommandNode = clazz.getAnnotation(SubCommandNode.class);
                String parent = subCommandNode.parent();
                String[] parentRoute = parent.split("\\.");
                String[] subRoute = new String[parentRoute.length - 1];
                String masterParentID = parentRoute[0];
                System.arraycopy(parentRoute, 1, subRoute, 0, parentRoute.length - 1);
                AbstractCommandNode currentParent = registry.getBaseNode(masterParentID);
                if (currentParent == null) throw new IllegalArgumentException("Invalid parent path at " + masterParentID +  " :" + Arrays.toString(parentRoute));
                for (String s : subRoute) {
                    currentParent = registry.getChildNode(currentParent, s);
                    if (currentParent == null) throw new IllegalArgumentException("Invalid parent path at " + s +  " :" + Arrays.toString(parentRoute));
                }
                registry.addSubCommandNode(currentParent, new Pair<>(subCommandNode.value(), node));
                break;
            }
        }
    }

    public Map<AbstractCommandNode, Pair<String, String[]>> permissions(CommandRegistryImpl registry) {
        Map<AbstractCommandNode, Pair<String, String[]>> returnVal = new HashMap<>();
        registry.getBaseCommandNodes().values().forEach((v) -> {
            Permission permission = v.getClass().getAnnotation(Permission.class);
            if (permission == null) return;
            returnVal.put(v, new Pair<>(permission.value(), permission.noPerm()));
        });
        registry.getSubCommandNodes().keys().forEach((k) -> {
            System.out.println("K = " + k);
            Permission permission = k.getClass().getAnnotation(Permission.class);
            if (permission == null) return;
            returnVal.put(k, new Pair<>(permission.value(), permission.noPerm()));
        });
        return returnVal;
    }

}
