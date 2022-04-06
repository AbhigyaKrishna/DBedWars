package org.zibble.dbedwars.commands.setup;

import org.bukkit.entity.Player;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.commands.annotations.Permission;
import org.zibble.dbedwars.api.commands.annotations.PlayerOnly;
import org.zibble.dbedwars.api.commands.annotations.SubCommandNode;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.member.PlayerMember;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.configuration.language.PluginLang;
import org.zibble.dbedwars.game.arena.view.ShopInfoImpl;
import org.zibble.dbedwars.game.setup.SetupSession;
import org.zibble.dbedwars.game.setup.SetupSessionManager;

@PlayerOnly
@Permission("dbedwars.setup")
@SubCommandNode(parent = "bw.setup", value = "addshop")
public class SetupTeamShop extends SetupSessionTeamCommand {

    private final DBedwars plugin;

    public SetupTeamShop(DBedwars plugin, SetupSessionManager manager, Messaging messaging) {
        super(manager, messaging);
        this.plugin = plugin;
    }

    @Override
    protected int teamNameIndex() {
        return 1;
    }

    @Override
    protected Message invalidArgs(PlayerMember member, Player player, SetupSession setupSession, String[] args) {
        return PluginLang.SETUP_SESSION_TEAM_SHOP_USAGE.asMessage();
    }

    @Override
    protected void execute(PlayerMember member, Player player, SetupSession setupSession, Color color, String[] args) {
        if (args.length < 1) {
            member.sendMessage(this.invalidArgs(member, player, setupSession, args));
            return;
        }

        ShopInfoImpl shop = null;
        for (ShopInfoImpl shopInfo : this.plugin.getGameManager().getShops()) {
            if (shopInfo.getKey().get().equalsIgnoreCase(args[0])) {
                shop = shopInfo;
                break;
            }
        }
        if (shop == null) {
            member.sendMessage(PluginLang.SETUP_SESSION_TEAM_SHOP_NOT_FOUND.asMessage());
        } else {
            setupSession.addTeamShop(color, shop, player.getLocation());
        }
    }

}
