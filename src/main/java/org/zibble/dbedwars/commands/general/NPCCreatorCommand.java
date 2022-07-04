package org.zibble.dbedwars.commands.general;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.commands.annotations.Permission;
import org.zibble.dbedwars.api.commands.annotations.PlayerOnly;
import org.zibble.dbedwars.api.commands.annotations.SubCommandNode;
import org.zibble.dbedwars.api.commands.nodes.CommandNode;
import org.zibble.dbedwars.guis.configcreator.npc.NPCCreatorTypeGui;

@PlayerOnly
@Permission("dbedwars.setup")
@SubCommandNode(parent = "bw", value = "npccreator")
public class NPCCreatorCommand extends CommandNode {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        NPCCreatorTypeGui.show((Player) sender);
        return true;
    }

}
