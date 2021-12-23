package com.pepedevs.dbedwars.messaging;

import com.pepedevs.dbedwars.api.messaging.member.MessagingMember;
import com.pepedevs.dbedwars.api.messaging.member.PlayerMember;
import com.pepedevs.dbedwars.api.messaging.message.AdventureMessage;
import com.pepedevs.dbedwars.api.messaging.message.Message;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

import java.util.*;
import java.util.function.Predicate;

public abstract class AbstractMessaging implements com.pepedevs.dbedwars.api.messaging.AbstractMessaging {

    public static final float DEFAULT_BOSS_BAR_PROGRESS = 1;
    public static final BossBar.Color DEFAULT_BOSS_BAR_COLOR = BossBar.Color.PURPLE;
    public static final BossBar.Overlay DEFAULT_BOSS_BAR_OVERLAY = BossBar.Overlay.PROGRESS;
    public static final Component DEFAULT_SUBTITLE = Component.empty();
    public static final Title.Times DEFAULT_TITLE_TIMES = Title.DEFAULT_TIMES;

    @Override
    public void sendMessage(Message message, boolean papiParsed) {
        for (MessagingMember member : this.getMembers()) {
            member.getAudienceMember().sendMessage(papiParsed && member.isPlayerMember() ?
                    message.asComponentWithPAPI(((PlayerMember) member).getPlayer()) : message.asComponent());
        }
    }

    @Override
    public void sendMessage(Message message, boolean papiParsed, Predicate<MessagingMember> except) {
        for (MessagingMember member : this.getMembers()) {
            if (!except.test(member)) member.getAudienceMember().sendMessage(papiParsed && member.isPlayerMember() ?
                    message.asComponentWithPAPI(((PlayerMember) member).getPlayer()) : message.asComponent());
        }
    }

    @Override
    public BossBar sendBossBar(BossBar bossBar) {
        for (MessagingMember member : this.getMembers()) {
            member.getAudienceMember().showBossBar(bossBar);
        }
        return bossBar;
    }

    @Override
    public BossBar sendBossBar(BossBar bossBar, Predicate<MessagingMember> except) {
        for (MessagingMember member : this.getMembers()) {
            if (!except.test(member)) member.getAudienceMember().showBossBar(bossBar);
        }
        return bossBar;
    }

    @Override
    public BossBar[] sendBossBar(Message message, boolean papiParsed) {
        return this.sendBossBar(message, DEFAULT_BOSS_BAR_PROGRESS, DEFAULT_BOSS_BAR_OVERLAY, DEFAULT_BOSS_BAR_COLOR, papiParsed);
    }

    @Override
    public BossBar[] sendBossBar(Message message, boolean papiParsed, Predicate<MessagingMember> except) {
        return this.sendBossBar(message, DEFAULT_BOSS_BAR_PROGRESS, DEFAULT_BOSS_BAR_OVERLAY, DEFAULT_BOSS_BAR_COLOR, papiParsed, except);
    }

    @Override
    public BossBar[] sendBossBar(Message message, float progress, BossBar.Overlay overlay, BossBar.Color color, boolean papiParsed) {
        return this.sendBossBar(message, progress, overlay, color, Collections.emptySet(), papiParsed);
    }

    @Override
    public BossBar[] sendBossBar(Message message, float progress, BossBar.Overlay overlay, BossBar.Color color, boolean papiParsed, Predicate<MessagingMember> except) {
        return this.sendBossBar(message, progress, overlay, color, Collections.emptySet(), papiParsed);
    }

    @Override
    public BossBar[] sendBossBar(Message message, float progress, BossBar.Overlay overlay, BossBar.Color color, Set<BossBar.Flag> flags, boolean papiParsed) {
        List<BossBar> bossBars = new ArrayList<>();
        for (MessagingMember member : this.getMembers()) {
            BossBar bossBar = BossBar.bossBar(papiParsed && member.isPlayerMember() ?
                    message.asComponentWithPAPI(((PlayerMember) member).getPlayer()) : message.asComponent(), progress, color, overlay, flags);
            member.getAudienceMember().showBossBar(bossBar);
            bossBars.add(bossBar);
        }
        return bossBars.toArray(new BossBar[0]);
    }

    @Override
    public BossBar[] sendBossBar(Message message, float progress, BossBar.Overlay overlay, BossBar.Color color, Set<BossBar.Flag> flags, boolean papiParsed, Predicate<MessagingMember> except) {
        List<BossBar> bossBars = new ArrayList<>();
        for (MessagingMember member : this.getMembers()) {
            if (!except.test(member)) {
                BossBar bossBar = BossBar.bossBar(papiParsed && member.isPlayerMember() ?
                        message.asComponentWithPAPI(((PlayerMember) member).getPlayer()) : message.asComponent(), progress, color, overlay, flags);
                member.getAudienceMember().showBossBar(bossBar);
                bossBars.add(bossBar);
            }
        }
        return bossBars.toArray(new BossBar[0]);
    }

    @Override
    public void hideBossBar(BossBar bossBar) {
        for (MessagingMember member : this.getMembers()) {
            member.getAudienceMember().hideBossBar(bossBar);
        }
    }

    @Override
    public void hideBossBar(BossBar bossBar, Predicate<MessagingMember> except) {
        for (MessagingMember member : this.getMembers()) {
            if (!except.test(member)) member.getAudienceMember().hideBossBar(bossBar);
        }
    }

    @Override
    public void sendActionBar(Message message, boolean papiParsed) {
        for (MessagingMember member : this.getMembers()) {
            member.getAudienceMember().sendActionBar(papiParsed && member.isPlayerMember() ?
                    message.asComponentWithPAPI(((PlayerMember) member).getPlayer()) : message.asComponent());
        }
    }

    @Override
    public void sendActionBar(Message message, boolean papiParsed, Predicate<MessagingMember> except) {
        for (MessagingMember member : this.getMembers()) {
            if (!except.test(member)) member.getAudienceMember().sendActionBar(papiParsed && member.isPlayerMember() ?
                    message.asComponentWithPAPI(((PlayerMember) member).getPlayer()) : message.asComponent());
        }
    }

    @Override
    public void sendTitle(Title title) {
        for (MessagingMember member : this.getMembers()) {
            member.getAudienceMember().showTitle(title);
        }
    }

    @Override
    public void sendTitle(Title title, Predicate<MessagingMember> except) {
        for (MessagingMember member : this.getMembers()) {
            if (!except.test(member)) member.getAudienceMember().showTitle(title);
        }
    }

    @Override
    public void sendTitle(Message title, boolean papiParsed) {
        this.sendTitle(title, AdventureMessage.from(DEFAULT_SUBTITLE), DEFAULT_TITLE_TIMES, papiParsed);
    }

    @Override
    public void sendTitle(Message title, boolean papiParsed, Predicate<MessagingMember> except) {
        this.sendTitle(title, AdventureMessage.from(DEFAULT_SUBTITLE), DEFAULT_TITLE_TIMES, papiParsed, except);
    }

    @Override
    public void sendTitle(Message title, Message subtitle, boolean papiParsed) {
        this.sendTitle(title, subtitle, DEFAULT_TITLE_TIMES, papiParsed);
    }

    @Override
    public void sendTitle(Message title, Message subtitle, boolean papiParsed, Predicate<MessagingMember> except) {
        this.sendTitle(title, subtitle, DEFAULT_TITLE_TIMES, papiParsed, except);
    }

    @Override
    public void sendTitle(Message title, Title.Times times, boolean papiParsed) {
        this.sendTitle(title, AdventureMessage.from(DEFAULT_SUBTITLE), times, papiParsed);
    }

    @Override
    public void sendTitle(Message title, Title.Times times, boolean papiParsed, Predicate<MessagingMember> except) {
        this.sendTitle(title, AdventureMessage.from(DEFAULT_SUBTITLE), times, papiParsed, except);
    }

    @Override
    public void sendTitle(Message title, Message subtitle, Title.Times times, boolean papiParsed) {
        for (MessagingMember member : this.getMembers()) {
            Title t = Title.title(papiParsed && member.isPlayerMember() ?
                            title.asComponentWithPAPI(((PlayerMember) member).getPlayer()) : title.asComponent(),
                    papiParsed && member.isPlayerMember() ?
                            subtitle.asComponentWithPAPI(((PlayerMember) member).getPlayer()) : subtitle.asComponent(),
                    times);
            member.getAudienceMember().showTitle(t);
        }
    }

    @Override
    public void sendTitle(Message title, Message subtitle, Title.Times times, boolean papiParsed, Predicate<MessagingMember> except) {
        for (MessagingMember member : this.getMembers()) {
            Title t = Title.title(papiParsed && member.isPlayerMember() ?
                    title.asComponentWithPAPI(((PlayerMember) member).getPlayer()) : title.asComponent(),
                    papiParsed && member.isPlayerMember() ?
                            subtitle.asComponentWithPAPI(((PlayerMember) member).getPlayer()) : subtitle.asComponent(),
                    times);
            if (!except.test(member)) member.getAudienceMember().showTitle(t);
        }
    }

    @Override
    public void clearTitle() {
        for (MessagingMember member : this.getMembers()) {
            member.getAudienceMember().clearTitle();
        }
    }

    @Override
    public void clearTitle(Predicate<MessagingMember> except) {
        for (MessagingMember member : this.getMembers()) {
            if (!except.test(member)) member.getAudienceMember().clearTitle();
        }
    }

    @Override
    public void resetTitle() {
        for (MessagingMember member : this.getMembers()) {
            member.getAudienceMember().resetTitle();
        }
    }

    @Override
    public void resetTitle(Predicate<MessagingMember> except) {
        for (MessagingMember member : this.getMembers()) {
            if (!except.test(member)) member.getAudienceMember().resetTitle();
        }
    }

    public abstract Collection<MessagingMember> getMembers();

}
