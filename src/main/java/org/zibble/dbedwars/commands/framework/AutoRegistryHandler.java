package org.zibble.dbedwars.commands.framework;

import org.reflections.Reflections;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.commands.annotations.ParentCommandNode;
import org.zibble.dbedwars.api.commands.annotations.Permission;
import org.zibble.dbedwars.api.commands.annotations.SubCommandNode;
import org.zibble.dbedwars.api.commands.nodes.AbstractCommandNode;
import org.zibble.dbedwars.api.util.Pair;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class AutoRegistryHandler {

    private static final String[] PACKAGES = new String[]{""};
    private final DBedwars plugin;

    public AutoRegistryHandler(DBedwars commandNodes) {
        this.plugin = commandNodes;
    }

    public Map<Pair<String, String[]>, ? extends AbstractCommandNode> baseNodes() {
        Map<Pair<String, String[]>, AbstractCommandNode> returnVal = new HashMap<>();
        Set<Class<?>> classes = new HashSet<>();
        for (String pkg : PACKAGES) {
            Reflections reflections = new Reflections(pkg);
            classes.addAll(reflections.getTypesAnnotatedWith(ParentCommandNode.class));
        }
        for (Class<?> aClass : classes) {
            Constructor<?> constructor;
            try {
                constructor = aClass.getConstructor(DBedwars.class);
                constructor.setAccessible(true);
                Object o = constructor.newInstance(this.plugin);
                ParentCommandNode node = o.getClass().getAnnotation(ParentCommandNode.class);
                Pair<String, String[]> pair = new Pair<>(node.value(), node.aliases());
                returnVal.put(pair, (AbstractCommandNode) o);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
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
        List<Class<?>> sortedClasses = this.sort(classes);
        System.out.println(sortedClasses);
        for (Class<?> aClass : sortedClasses) {
            try {
                Constructor<?> constructor = aClass.getConstructor(DBedwars.class);
                constructor.setAccessible(true);
                Object o = constructor.newInstance(this.plugin);
                SubCommandNode subCommandNode = aClass.getAnnotation(SubCommandNode.class);
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
                registry.addSubCommandNode(currentParent, new Pair<>(subCommandNode.value(), (AbstractCommandNode) o));
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
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

    private List<Class<?>> sort(Set<Class<?>> input) {
        List<Class<?>> classes = new ArrayList<>(input);
        classes.sort((a, b) -> {
            SubCommandNode o1 = a.getAnnotation(SubCommandNode.class);
            SubCommandNode o2 = b.getAnnotation(SubCommandNode.class);
            if (o1 == null || o2 == null) throw new IllegalArgumentException("Classes must be annotated with @SubCommandNode");
            String[] aRoute = o1.parent().split("\\.");
            String[] bRoute = o2.parent().split("\\.");
            return Integer.compare(aRoute.length, bRoute.length);
        });
        return classes;
    }

}
