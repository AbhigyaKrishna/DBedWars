package org.zibble.dbedwars.hooks.defaults.npc;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.TextureProperty;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfo;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnPlayer;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams;
import com.pepedevs.radium.utils.version.Version;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.future.ActionFuture;
import org.zibble.dbedwars.api.hooks.npc.NPCData;
import org.zibble.dbedwars.api.hooks.npc.PlayerNPC;
import org.zibble.dbedwars.api.objects.profile.Skin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

public class PlayerNPCImpl extends BedWarsNPCImpl implements PlayerNPC {

    private final SkinDataImpl skinData;
    private final UserProfile userProfile;
    private final WrapperPlayServerPlayerInfo.PlayerData info;

    public PlayerNPCImpl(String ID, Location location, NPCData npcData, SkinDataImpl skinData) {
        super(ID, location, npcData);
        this.skinData = skinData;
        this.userProfile = new UserProfile(this.getUUID(), "");
        this.info = new WrapperPlayServerPlayerInfo.PlayerData(
                Component.empty(),
                this.userProfile,
                GameMode.CREATIVE,
                0
        );
    }

    @Override
    public ActionFuture<PlayerNPC> setSkin(Skin skin) {
        return ActionFuture.supplyAsync(() -> {
            this.userProfile.getTextureProperties().clear();
            this.userProfile.getTextureProperties().add(new TextureProperty("textures", skin.getValue(), skin.getSignature()));
            return this;
        });
    }

    @Override
    public ActionFuture<PlayerNPC> setSkin(ActionFuture<Skin> skin) {
        return skin.thenApply(s -> {
            this.userProfile.getTextureProperties().clear();
            this.userProfile.getTextureProperties().add(new TextureProperty("textures", s.getValue(), s.getSignature()));
            return this;
        });
    }

    @Override
    public ActionFuture<PlayerNPC> hideNameTag() {
        return ActionFuture.supplyAsync(() -> {
            WrapperPlayServerTeams packet = new WrapperPlayServerTeams("NPC_0000" + this.getUUID().toString().substring(0, 8),
                    WrapperPlayServerTeams.TeamMode.REMOVE,
                    Optional.empty(),
                    this.userProfile.getName());
            for (UUID uuid : this.shown) {
                Player player = Bukkit.getPlayer(uuid);
                if (player == null) continue;
                PACKET_EVENTS_API.getPlayerManager().sendPacket(player, packet);
            }
            return this;
        });
    }

    @Override
    public ActionFuture<PlayerNPC> showNameTag() {
        return ActionFuture.supplyAsync(() -> {
            WrapperPlayServerTeams packet = new WrapperPlayServerTeams("NPC_0000" + this.getUUID().toString().substring(0, 8),
                    WrapperPlayServerTeams.TeamMode.CREATE,
                    Optional.empty(),
                    this.userProfile.getName());
            for (UUID uuid : this.shown) {
                Player player = Bukkit.getPlayer(uuid);
                if (player == null) continue;
                PACKET_EVENTS_API.getPlayerManager().sendPacket(player, packet);
            }
            return this;
        });
    }

    @Override
    public ActionFuture<PlayerNPC> showInTab() {
        return ActionFuture.supplyAsync(() -> {
            WrapperPlayServerPlayerInfo infoAddPacket = new WrapperPlayServerPlayerInfo(
                    WrapperPlayServerPlayerInfo.Action.ADD_PLAYER,
                    this.info
            );
            for (UUID uuid : this.shown) {
                Player player = Bukkit.getPlayer(uuid);
                if (player == null) continue;
                PACKET_EVENTS_API.getPlayerManager().sendPacket(player, infoAddPacket);
            }
            return this;
        });
    }

    @Override
    public ActionFuture<PlayerNPC> hideFromTab() {
        return ActionFuture.supplyAsync(() -> {
            WrapperPlayServerPlayerInfo infoRemovePlayer = new WrapperPlayServerPlayerInfo(
                    WrapperPlayServerPlayerInfo.Action.REMOVE_PLAYER,
                    this.info
            );
            for (UUID uuid : this.shown) {
                Player player = Bukkit.getPlayer(uuid);
                if (player == null) continue;
                PACKET_EVENTS_API.getPlayerManager().sendPacket(player, infoRemovePlayer);
            }
            return this;
        });
    }

    @Override
    public SkinDataImpl getSkinData() {
        return this.skinData;
    }

    @Override
    public ActionFuture<PlayerNPC> updateSkinData() {
        return ActionFuture.supplyAsync(() -> {
            EntityData entityData = new EntityData(10, EntityDataTypes.BYTE, ByteUtil.buildSkinDataByte(this.skinData));
            switch (Version.SERVER_VERSION) {
                case v1_8_R1:
                case v1_8_R2:
                case v1_8_R3: {
                    entityData.setIndex(10);
                    break;
                }
                case v1_9_R1:
                case v1_9_R2: {
                    entityData.setIndex(12);
                    break;
                }
                case v1_10_R1:
                case v1_11_R1:
                case v1_12_R1:
                case v1_13_R1:
                case v1_13_R2:
                case v1_14_R1: {
                    entityData.setIndex(13);
                    break;
                }
                case v1_15_R1: {
                    entityData.setIndex(15);
                }
                case v1_16_R1:
                case v1_16_R2:
                case v1_16_R3:{
                    entityData.setIndex(16);
                }
                case v1_17_R1:
                case v1_18_R1: {
                    entityData.setIndex(17);
                }
            }
            WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata(this.getEntityID(), Collections.singletonList(entityData));
            for (UUID uuid : this.shown) {
                Player player = Bukkit.getPlayer(uuid);
                if (player == null) continue;
                PACKET_EVENTS_API.getPlayerManager().sendPacket(player, packet);
            }
            return this;
        });
    }

    @Override
    protected void viewPacket(Player player) {
        WrapperPlayServerSpawnPlayer spawnPacket = new WrapperPlayServerSpawnPlayer(
                this.getEntityID(),
                this.getUUID(),
                super.convert(this.getLocation()),
                this.getLocation().getYaw(),
                this.getLocation().getPitch(),
                new ArrayList<>()
        );
        PACKET_EVENTS_API.getPlayerManager().sendPacket(player, spawnPacket);
    }
}
