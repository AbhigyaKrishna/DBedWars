package org.zibble.dbedwars.commands.framework;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.commands.nodes.AbstractCommandNode;
import org.zibble.dbedwars.api.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Generators {

    public static CommandExecutor executor(CommandRegistryImpl registry, AbstractCommandNode node) {
        return (sender, command, label, args) -> {
            String[] currentArgs = new String[args.length];
            AbstractCommandNode currentNode = node;
            System.arraycopy(args, 0, currentArgs, 0, args.length);
            boolean canMatch = true;
            Collection<Pair<String, AbstractCommandNode>> pairs;
            while (currentArgs.length > 0 && canMatch) {
                String subNode = currentArgs[0];
                pairs = registry.getSubCommandNodes().get(currentNode);
                if (pairs == null) canMatch = false;
                else {
                    boolean anyMatch = false;
                    for (Pair<String, AbstractCommandNode> pair : pairs) {
                        if (pair.getKey().equalsIgnoreCase(subNode)) {
                            currentNode = pair.getValue();
                            String[] helper = new String[currentArgs.length - 1];
                            System.arraycopy(currentArgs, 1, helper, 0, currentArgs.length - 1);
                            currentArgs = helper;
                            anyMatch = true;
                            break;
                        }
                    }
                    if (!anyMatch) canMatch = false;
                }
            }
            currentNode.accept(sender, currentArgs);
            return true;
        };
    }

    public static CommandExecutor executor(CommandRegistryImpl registry, AbstractCommandNode node, Pair<String, String[]> permission) {
        return (sender, command, label, args) -> {
            if (!sender.isOp() && !sender.hasPermission(permission.getKey())) {
                //TODO SEND MESSAGE
                return true;
            }
            String[] currentArgs = new String[args.length];
            AbstractCommandNode currentNode = node;
            System.arraycopy(args, 0, currentArgs, 0, args.length);
            boolean canMatch = true;
            while (currentArgs.length > 0 && canMatch) {
                String subNode = currentArgs[0];
                Collection<Pair<String, AbstractCommandNode>> pairs = registry.getSubCommandNodes().get(currentNode);
                if (pairs == null) canMatch = false;
                else {
                    boolean anyMatch = false;
                    for (Pair<String, AbstractCommandNode> pair : pairs) {
                        if (pair.getKey().equalsIgnoreCase(subNode)) {
                            currentNode = pair.getValue();
                            String[] helper = new String[currentArgs.length - 1];
                            System.arraycopy(currentArgs, 1, helper, 0, currentArgs.length - 1);
                            currentArgs = helper;
                            anyMatch = true;
                            break;
                        }
                    }
                    if (!anyMatch) canMatch = false;
                }
            }
            currentNode.accept(sender, currentArgs);
            return true;
        };
    }

    public static TabCompleter completer(CommandRegistryImpl registry, AbstractCommandNode node) {
        return (sender, command, label, args) -> {
            if (!(sender instanceof Player)) return null;
            String[] currentArgs = new String[args.length];
            AbstractCommandNode currentNode = node;
            System.arraycopy(args, 0, currentArgs, 0, args.length);
            Collection<Pair<String, AbstractCommandNode>> pairs = null;
            boolean canMatch = true;
            while (currentArgs.length > 0 && canMatch) {
                String subNode = currentArgs[0];
                pairs = registry.getSubCommandNodes().get(currentNode);
                if (pairs == null) canMatch = false;
                else {
                    boolean anyMatch = false;
                    for (Pair<String, AbstractCommandNode> pair : pairs) {
                        if (pair.getKey().equalsIgnoreCase(subNode)) {
                            currentNode = pair.getValue();
                            String[] helper = new String[currentArgs.length - 1];
                            System.arraycopy(currentArgs, 1, helper, 0, currentArgs.length - 1);
                            currentArgs = helper;
                            anyMatch = true;
                            break;
                        }
                    }
                    if (!anyMatch) canMatch = false;
                }
            }
            List<String> returnVal = new ArrayList<>();
            if (pairs != null) pairs.forEach(pair -> returnVal.add(pair.getKey()));
            returnVal.addAll(Arrays.asList(currentNode.hint((Player) sender)));
            return returnVal;
        };
    }

}
