package org.zibble.dbedwars.commands.setup;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.commands.annotations.Permission;
import org.zibble.dbedwars.api.commands.annotations.PlayerOnly;
import org.zibble.dbedwars.api.commands.annotations.SubCommandNode;
import org.zibble.dbedwars.api.future.ActionFuture;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.member.PlayerMember;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.api.util.SchedulerUtils;
import org.zibble.dbedwars.configuration.language.PluginLang;
import org.zibble.dbedwars.game.setup.MissingArenaData;
import org.zibble.dbedwars.game.setup.SetupSession;
import org.zibble.dbedwars.game.setup.SetupSessionManager;
import org.zibble.dbedwars.game.setup.SetupUtil;
import org.zibble.dbedwars.game.setup.detection.BedDetection;
import org.zibble.dbedwars.game.setup.detection.GenDetection;

import java.util.Optional;

@PlayerOnly
@Permission("dbedwars.setup")
@SubCommandNode(parent = "bw.setup", value = "setspawn")
public class SetupTeamSpawn extends SetupSessionOptionalTeamCommand {

    public SetupTeamSpawn(SetupSessionManager manager, Messaging messaging) {
        super(manager, messaging);
    }

    @Override
    protected int teamNameIndex() {
        return 0;
    }

    @Override
    protected Message invalidArgs(PlayerMember member, Player player, SetupSession setupSession, String[] args) {
        return PluginLang.SETUP_SESSION_TEAM_SPAWN_USAGE.asMessage();
    }

    @Override
    protected Color findTeam(Player player, SetupSession session, Location location) {
        return SetupUtil.getNearestTeamViaHologram(location, session, 30);
    }

    @Override
    protected void execute(PlayerMember member, Player player, SetupSession setupSession, Color color, String[] args) {
        setupSession.setupTeamSpawn(color, player.getLocation());

        ActionFuture.runAsync(() -> {
            MissingArenaData missingData = new MissingArenaData(setupSession);
            if (!missingData.hasBed(color)) {
                Optional<Block> bed = BedDetection.detect(player.getLocation(), 15);
                bed.ifPresent(block -> setupSession.setupTeamBed(color, block.getLocation()));
            }

            GenDetection.detect(player.getLocation(), 15).ifPresent(block -> {
                SchedulerUtils.runTask(() -> player.teleport(block.getLocation().add(0, 1, 0)));
                member.sendMessage(PluginLang.SETUP_SESSION_FOUND_GENERATOR_LOCATION.asMessage());
            });
        });
    }

}
