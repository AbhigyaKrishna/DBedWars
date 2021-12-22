package com.pepedevs.dbedwars.messaging;

import com.pepedevs.dbedwars.messaging.member.MessagingMember;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public abstract class MessagingUtils {

    public static final float DEFAULT_BOSS_BAR_PROGRESS = 1;
    public static final BossBar.Color DEFAULT_BOSS_BAR_COLOR = BossBar.Color.PURPLE;
    public static final BossBar.Overlay DEFAULT_BOSS_BAR_OVERLAY = BossBar.Overlay.PROGRESS;
    public static final Component DEFAULT_SUBTITLE = Component.empty();
    public static final Title.Times DEFAULT_TITLE_TIMES = Title.DEFAULT_TIMES;

    public BossBar sendBossBar(BossBar bossBar, MessagingMember... except) {
        List<MessagingMember> list = Arrays.asList(except);
        for (MessagingMember member : this.getMembers()) {
            if (!list.contains(member)) member.getAudienceMember().showBossBar(bossBar);
        }
        return bossBar;
    }

    public BossBar sendBossBar(Message message, MessagingMember... except) {
        BossBar bossBar =
                BossBar.bossBar(
                        message.asComponent(),
                        DEFAULT_BOSS_BAR_PROGRESS,
                        DEFAULT_BOSS_BAR_COLOR,
                        DEFAULT_BOSS_BAR_OVERLAY);
        return this.sendBossBar(bossBar, except);
    }

    public BossBar sendBossBar(Message message, float progress, MessagingMember... except) {
        BossBar bossBar =
                BossBar.bossBar(
                        message.asComponent(),
                        progress,
                        DEFAULT_BOSS_BAR_COLOR,
                        DEFAULT_BOSS_BAR_OVERLAY);
        return this.sendBossBar(bossBar, except);
    }

    public BossBar sendBossBar(Message message, BossBar.Color color, MessagingMember... except) {
        BossBar bossBar =
                BossBar.bossBar(
                        message.asComponent(),
                        DEFAULT_BOSS_BAR_PROGRESS,
                        color,
                        DEFAULT_BOSS_BAR_OVERLAY);
        return this.sendBossBar(bossBar, except);
    }

    public BossBar sendBossBar(
            Message message, BossBar.Overlay overlay, MessagingMember... except) {
        BossBar bossBar =
                BossBar.bossBar(
                        message.asComponent(),
                        DEFAULT_BOSS_BAR_PROGRESS,
                        DEFAULT_BOSS_BAR_COLOR,
                        overlay);
        return this.sendBossBar(bossBar, except);
    }

    public void hideBossBar(BossBar bossBar, MessagingMember... except) {
        List<MessagingMember> list = Arrays.asList(except);
        for (MessagingMember member : this.getMembers()) {
            if (!list.contains(member)) member.getAudienceMember().hideBossBar(bossBar);
        }
    }

    public void sendActionBar(Message message, MessagingMember... except) {
        List<MessagingMember> list = Arrays.asList(except);
        for (MessagingMember member : this.getMembers()) {
            if (!list.contains(member))
                member.getAudienceMember().sendActionBar(message.asComponent());
        }
    }

    public void sendTitle(Title title, MessagingMember... except) {
        List<MessagingMember> list = Arrays.asList(except);
        for (MessagingMember member : this.getMembers()) {
            if (!list.contains(member)) member.getAudienceMember().showTitle(title);
        }
    }

    public void sendTitle(Message title, MessagingMember... except) {
        this.sendTitle(
                Title.title(title.asComponent(), DEFAULT_SUBTITLE, DEFAULT_TITLE_TIMES), except);
    }

    public void sendTitle(Message title, Message subtitle, MessagingMember... except) {
        this.sendTitle(
                Title.title(title.asComponent(), subtitle.asComponent(), DEFAULT_TITLE_TIMES),
                except);
    }

    public void sendTitle(
            Message title,
            int fadeInTicks,
            int fadeOutTicks,
            int stayTicks,
            MessagingMember... except) {
        this.sendTitle(
                Title.title(
                        title.asComponent(),
                        DEFAULT_SUBTITLE,
                        this.getTimes(fadeInTicks, fadeOutTicks, stayTicks)),
                except);
    }

    public void sendTitle(
            Message title,
            Message subtitle,
            int fadeInTicks,
            int fadeOutTicks,
            int stayTicks,
            MessagingMember... except) {
        this.sendTitle(
                Title.title(
                        title.asComponent(),
                        subtitle.asComponent(),
                        this.getTimes(fadeInTicks, fadeOutTicks, stayTicks)),
                except);
    }

    public void clearTitle(MessagingMember... except) {
        List<MessagingMember> list = Arrays.asList(except);
        for (MessagingMember member : this.getMembers()) {
            if (!list.contains(member)) member.getAudienceMember().clearTitle();
        }
    }

    public void resetTitle(MessagingMember... except) {
        List<MessagingMember> list = Arrays.asList(except);
        for (MessagingMember member : this.getMembers()) {
            if (!list.contains(member)) member.getAudienceMember().resetTitle();
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
