package org.zibble.dbedwars.api.commands.nodes;

import org.bukkit.command.CommandSender;

import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class ConditionalCommandNode extends AbstractCommandNode {

    private final Predicate<CommandSender> predicate;
    private final Consumer<CommandSender> noHandler;

    public ConditionalCommandNode(Predicate<CommandSender> condition, Consumer<CommandSender> noHandler) {
        this.predicate = condition;
        this.noHandler = noHandler;
    }

    @Override
    public final boolean accept(CommandSender sender, String[] args) {
        if (this.predicate != null && !this.predicate.test(sender)) {
            if (this.noHandler != null) this.noHandler.accept(sender);
            return false;
        }
        this.execute(sender, args);
        return true;
    }

    public abstract void execute(CommandSender sender, String[] args);

}
