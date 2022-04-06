package org.zibble.dbedwars.commands.setup;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.commands.annotations.Permission;
import org.zibble.dbedwars.api.commands.annotations.PlayerOnly;
import org.zibble.dbedwars.api.commands.annotations.SubCommandNode;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.member.PlayerMember;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.configuration.language.PluginLang;
import org.zibble.dbedwars.game.setup.SetupSession;
import org.zibble.dbedwars.game.setup.SetupSessionManager;
import org.zibble.dbedwars.game.setup.SetupUtil;
import org.zibble.dbedwars.game.setup.detection.BedDetection;

import java.util.Optional;

@PlayerOnly
@Permission("dbedwars.setup")
@SubCommandNode(parent = "bw.setup", value = "setbed")
public class SetupTeamBed extends SetupSessionOptionalTeamCommand {

    public SetupTeamBed(SetupSessionManager manager, Messaging messaging) {
        super(manager, messaging);
    }

    @Override
    protected int teamNameIndex() {
        return 0;
    }

    @Override
    protected Message invalidArgs(PlayerMember member, Player player, SetupSession setupSession, String[] args) {
        return PluginLang.SETUP_SESSION_TEAM_BED_USAGE.asMessage();
    }

    @Override
    protected Color findTeam(Player player, SetupSession session, Location location) {
        return SetupUtil.getNearestTeamViaHologram(location, session, 30);
    }

    @Override
    protected void execute(PlayerMember member, Player player, SetupSession setupSession, Color color, String[] args) {
        Optional<Block> bed = BedDetection.detect(player.getLocation(), 5);
        if (bed.isPresent()) {
            setupSession.setupTeamBed(color, bed.get().getLocation());
        } else {
            member.sendMessage(PluginLang.SETUP_SESSION_TEAM_BED_NOT_FOUND.asMessage());
        }
    }

}
