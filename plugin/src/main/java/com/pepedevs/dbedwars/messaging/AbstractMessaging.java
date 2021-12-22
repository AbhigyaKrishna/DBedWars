package com.pepedevs.dbedwars.messaging;

import com.pepedevs.dbedwars.api.messaging.member.MessagingMember;
import com.pepedevs.dbedwars.api.messaging.message.Message;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

import java.time.Duration;
import java.util.Collection;
import java.util.function.Predicate;

public abstract class AbstractMessaging implements com.pepedevs.dbedwars.api.messaging.AbstractMessaging {

    public static final float DEFAULT_BOSS_BAR_PROGRESS = 1;
    public static final BossBar.Color DEFAULT_BOSS_BAR_COLOR = BossBar.Color.PURPLE;
    public static final BossBar.Overlay DEFAULT_BOSS_BAR_OVERLAY = BossBar.Overlay.PROGRESS;
    public static final Component DEFAULT_SUBTITLE = Component.empty();
    public static final Title.Times DEFAULT_TITLE_TIMES = Title.DEFAULT_TIMES;

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
    public BossBar sendBossBar(Message message) {
        BossBar bossBar = BossBar.bossBar(
                message.asComponent(),
                DEFAULT_BOSS_BAR_PROGRESS,
                DEFAULT_BOSS_BAR_COLOR,
                DEFAULT_BOSS_BAR_OVERLAY);
        return this.sendBossBar(bossBar);
    }

    @Override
    public BossBar sendBossBar(Message message, Predicate<MessagingMember> except) {
        BossBar bossBar = BossBar.bossBar(
                message.asComponent(),
                DEFAULT_BOSS_BAR_PROGRESS,
                DEFAULT_BOSS_BAR_COLOR,
                DEFAULT_BOSS_BAR_OVERLAY);
        return this.sendBossBar(bossBar, except);
    }

    @Override
    public BossBar sendBossBar(Message message, float progress) {
        BossBar bossBar = BossBar.bossBar(
                message.asComponent(), progress, DEFAULT_BOSS_BAR_COLOR, DEFAULT_BOSS_BAR_OVERLAY);
        return this.sendBossBar(bossBar);
    }

    @Override
    public BossBar sendBossBar(Message message, float progress, Predicate<MessagingMember> except) {
        BossBar bossBar = BossBar.bossBar(
                message.asComponent(), progress, DEFAULT_BOSS_BAR_COLOR, DEFAULT_BOSS_BAR_OVERLAY);
        return this.sendBossBar(bossBar, except);
    }

    @Override
    public BossBar sendBossBar(Message message, BossBar.Color color) {
        BossBar bossBar = BossBar.bossBar(
                message.asComponent(), DEFAULT_BOSS_BAR_PROGRESS, color, DEFAULT_BOSS_BAR_OVERLAY);
        return this.sendBossBar(bossBar);
    }

    @Override
    public BossBar sendBossBar(Message message, BossBar.Color color, Predicate<MessagingMember> except) {
        BossBar bossBar = BossBar.bossBar(
                message.asComponent(), DEFAULT_BOSS_BAR_PROGRESS, color, DEFAULT_BOSS_BAR_OVERLAY);
        return this.sendBossBar(bossBar, except);
    }

    @Override
    public BossBar sendBossBar(Message message, BossBar.Overlay overlay) {
        BossBar bossBar = BossBar.bossBar(
                message.asComponent(), DEFAULT_BOSS_BAR_PROGRESS, DEFAULT_BOSS_BAR_COLOR, overlay);
        return this.sendBossBar(bossBar);
    }

    @Override
    public BossBar sendBossBar(Message message, BossBar.Overlay overlay, Predicate<MessagingMember> except) {
        BossBar bossBar = BossBar.bossBar(
                message.asComponent(), DEFAULT_BOSS_BAR_PROGRESS, DEFAULT_BOSS_BAR_COLOR, overlay);
        return this.sendBossBar(bossBar, except);
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
    public void sendActionBar(Message message) {
        for (MessagingMember member : this.getMembers()) {
            member.getAudienceMember().sendActionBar(message.asComponent());
        }
    }

    @Override
    public void sendActionBar(Message message, Predicate<MessagingMember> except) {
        for (MessagingMember member : this.getMembers()) {
            if (!except.test(member)) member.getAudienceMember().sendActionBar(message.asComponent());
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
    public void sendTitle(Message title) {
        this.sendTitle(Title.title(title.asComponent(), DEFAULT_SUBTITLE, DEFAULT_TITLE_TIMES));
    }

    @Override
    public void sendTitle(Message title, Predicate<MessagingMember> except) {
        this.sendTitle(Title.title(title.asComponent(), DEFAULT_SUBTITLE, DEFAULT_TITLE_TIMES), except);
    }

    @Override
    public void sendTitle(Message title, Message subtitle) {
        this.sendTitle(Title.title(title.asComponent(), subtitle.asComponent(), DEFAULT_TITLE_TIMES));
    }

    @Override
    public void sendTitle(Message title, Message subtitle, Predicate<MessagingMember> except) {
        this.sendTitle(Title.title(title.asComponent(), subtitle.asComponent(), DEFAULT_TITLE_TIMES), except);
    }

    @Override
    public void sendTitle(Message title, int fadeInTicks, int fadeOutTicks, int stayTicks) {
        this.sendTitle(Title.title(title.asComponent(), DEFAULT_SUBTITLE, this.getTimes(fadeInTicks, fadeOutTicks, stayTicks)));
    }

    @Override
    public void sendTitle(Message title, int fadeInTicks, int fadeOutTicks, int stayTicks, Predicate<MessagingMember> except) {
        this.sendTitle(Title.title(title.asComponent(), DEFAULT_SUBTITLE, this.getTimes(fadeInTicks, fadeOutTicks, stayTicks)), except);
    }

    @Override
    public void sendTitle(Message title, Message subtitle, int fadeInTicks, int fadeOutTicks, int stayTicks) {
        this.sendTitle(Title.title(title.asComponent(), subtitle.asComponent(), this.getTimes(fadeInTicks, fadeOutTicks, stayTicks)));
    }

    @Override
    public void sendTitle(Message title, Message subtitle, int fadeInTicks, int fadeOutTicks, int stayTicks, Predicate<MessagingMember> except) {
        this.sendTitle(Title.title(title.asComponent(), subtitle.asComponent(), this.getTimes(fadeInTicks, fadeOutTicks, stayTicks)), except);
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

    private Title.Times getTimes(int fadeIn, int fadeOut, int stay) {
        return Title.Times.of(
                Duration.ofMillis(fadeIn * 50L),
                Duration.ofMillis(stay * 50L),
                Duration.ofMillis(fadeOut * 50L));
    }

    public abstract Collection<MessagingMember> getMembers();

}
