package com.pepedevs.dbedwars.messaging;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Collection;

@SuppressWarnings("unused")
public class MessagingUtils {

    public static final float DEFAULT_BOSS_BAR_PROGRESS;
    public static final BossBar.Color DEFAULT_BOSS_BAR_COLOR;
    public static final BossBar.Overlay DEFAULT_BOSS_BAR_OVERLAY;
    public static final Component DEFAULT_SUBTITLE;
    public static final Title.Times DEFAULT_TITLE_TIMES;

    static {
        DEFAULT_BOSS_BAR_PROGRESS = 1;
        DEFAULT_BOSS_BAR_COLOR = BossBar.Color.PURPLE;
        DEFAULT_BOSS_BAR_OVERLAY = BossBar.Overlay.PROGRESS;
        DEFAULT_SUBTITLE = Component.empty();
        DEFAULT_TITLE_TIMES = Title.DEFAULT_TIMES;
    }

    public static BossBar sendBossBar(BossBar bossBar, Collection<MessagingMember> members) {
        for (MessagingMember member : members) {
            member.getAudienceMember().showBossBar(bossBar);
        }
        return bossBar;
    }

    public static BossBar sendBossBar(BossBar bossBar, MessagingChannel channel) {
        return sendBossBar(bossBar, channel.getChannelMemebers());
    }

    public static BossBar sendBossBar(BossBar bossBar, MessagingMember... members) {
        for (MessagingMember member : members) {
            member.getAudienceMember().showBossBar(bossBar);
        }
        return bossBar;
    }

    public static BossBar sendBossBar(BossBar bossBar, Player... players) {
        for (Player player : players) {
            MessagingMember.ofPlayer(player).getAudienceMember().showBossBar(bossBar);
        }
        return bossBar;
    }

    public static BossBar getBossBar(Message message) {
        return BossBar.bossBar(
                message.asComponent(),
                DEFAULT_BOSS_BAR_PROGRESS,
                DEFAULT_BOSS_BAR_COLOR,
                DEFAULT_BOSS_BAR_OVERLAY);
    }

    public static BossBar getBossBar(Message message, float progress) {
        return BossBar.bossBar(
                message.asComponent(), progress, DEFAULT_BOSS_BAR_COLOR, DEFAULT_BOSS_BAR_OVERLAY);
    }

    public static BossBar getBossBar(Message message, BossBar.Color color) {
        return BossBar.bossBar(
                message.asComponent(), DEFAULT_BOSS_BAR_PROGRESS, color, DEFAULT_BOSS_BAR_OVERLAY);
    }

    public static BossBar getBossBar(Message message, BossBar.Overlay overlay) {
        return BossBar.bossBar(
                message.asComponent(), DEFAULT_BOSS_BAR_PROGRESS, DEFAULT_BOSS_BAR_COLOR, overlay);
    }

    public static void hideBossBar(BossBar bossBar, Collection<MessagingMember> members) {
        for (MessagingMember member : members) {
            member.getAudienceMember().hideBossBar(bossBar);
        }
    }

    public static void hideBossBar(BossBar bossBar, MessagingChannel channel) {
        hideBossBar(bossBar, channel.getChannelMemebers());
    }

    public static void hideBossBar(BossBar bossBar, MessagingMember... members) {
        for (MessagingMember member : members) {
            member.getAudienceMember().hideBossBar(bossBar);
        }
    }

    public static void hideBossBar(BossBar bossBar, Player... players) {
        for (Player player : players) {
            MessagingMember.ofPlayer(player).getAudienceMember().hideBossBar(bossBar);
        }
    }

    public static void sendActionBar(Message message, Collection<MessagingMember> members) {
        for (MessagingMember member : members) {
            member.getAudienceMember().sendActionBar(message.asComponent());
        }
    }

    public static void sendActionBar(Message message, MessagingChannel channel) {
        sendActionBar(message, channel.getChannelMemebers());
    }

    public static void sendActionBar(Message message, MessagingMember... members) {
        for (MessagingMember member : members) {
            member.getAudienceMember().sendActionBar(message.asComponent());
        }
    }

    public static void sendActionBar(Message message, Player... players) {
        for (Player player : players) {
            MessagingMember.ofPlayer(player)
                    .getAudienceMember()
                    .sendActionBar(message.asComponent());
        }
    }

    public static void sendTitle(Message title, Collection<MessagingMember> members) {
        for (MessagingMember member : members) {
            member.getAudienceMember()
                    .showTitle(
                            Title.title(
                                    title.asComponent(), DEFAULT_SUBTITLE, DEFAULT_TITLE_TIMES));
        }
    }

    public static void sendTitle(Message title, MessagingChannel channel) {
        sendTitle(title, channel.getChannelMemebers());
    }

    public static void sendTitle(Message title, MessagingMember... members) {
        for (MessagingMember member : members) {
            member.getAudienceMember()
                    .showTitle(
                            Title.title(
                                    title.asComponent(), DEFAULT_SUBTITLE, DEFAULT_TITLE_TIMES));
        }
    }

    public static void sendTitle(Message title, Player... players) {
        for (Player player : players) {
            MessagingMember.ofPlayer(player)
                    .getAudienceMember()
                    .showTitle(
                            Title.title(
                                    title.asComponent(), DEFAULT_SUBTITLE, DEFAULT_TITLE_TIMES));
        }
    }

    public static void sendTitle(
            Message title, Message subtitle, Collection<MessagingMember> members) {
        for (MessagingMember member : members) {
            member.getAudienceMember()
                    .showTitle(
                            Title.title(
                                    title.asComponent(),
                                    subtitle.asComponent(),
                                    DEFAULT_TITLE_TIMES));
        }
    }

    public static void sendTitle(Message title, Message subtitle, MessagingChannel channel) {
        sendTitle(title, subtitle, channel.getChannelMemebers());
    }

    public static void sendTitle(Message title, Message subtitle, MessagingMember... members) {
        for (MessagingMember member : members) {
            member.getAudienceMember()
                    .showTitle(
                            Title.title(
                                    title.asComponent(),
                                    subtitle.asComponent(),
                                    DEFAULT_TITLE_TIMES));
        }
    }

    public static void sendTitle(Message title, Message subtitle, Player... players) {
        for (Player player : players) {
            MessagingMember.ofPlayer(player)
                    .getAudienceMember()
                    .showTitle(
                            Title.title(
                                    title.asComponent(),
                                    subtitle.asComponent(),
                                    DEFAULT_TITLE_TIMES));
        }
    }

    public static void sendTitle(
            Message title,
            int fadeInTicks,
            int fadeOutTicks,
            int stayTicks,
            Collection<MessagingMember> members) {
        for (MessagingMember member : members) {
            member.getAudienceMember()
                    .showTitle(
                            Title.title(
                                    title.asComponent(),
                                    DEFAULT_SUBTITLE,
                                    getTimes(fadeInTicks, fadeOutTicks, stayTicks)));
        }
    }

    public static void sendTitle(
            Message title,
            int fadeInTicks,
            int fadeOutTicks,
            int stayTicks,
            MessagingChannel channel) {
        sendTitle(title, fadeInTicks, fadeOutTicks, stayTicks, channel.getChannelMemebers());
    }

    public static void sendTitle(
            Message title,
            int fadeInTicks,
            int fadeOutTicks,
            int stayTicks,
            MessagingMember... members) {
        for (MessagingMember member : members) {
            member.getAudienceMember()
                    .showTitle(
                            Title.title(
                                    title.asComponent(),
                                    DEFAULT_SUBTITLE,
                                    getTimes(fadeInTicks, fadeOutTicks, stayTicks)));
        }
    }

    public static void sendTitle(
            Message title, int fadeInTicks, int fadeOutTicks, int stayTicks, Player... players) {
        for (Player player : players) {
            MessagingMember.ofPlayer(player)
                    .getAudienceMember()
                    .showTitle(
                            Title.title(
                                    title.asComponent(),
                                    DEFAULT_SUBTITLE,
                                    getTimes(fadeInTicks, fadeOutTicks, stayTicks)));
        }
    }

    public static void sendTitle(
            Message title,
            Message subtitle,
            int fadeInTicks,
            int fadeOutTicks,
            int stayTicks,
            Collection<MessagingMember> members) {
        for (MessagingMember member : members) {
            member.getAudienceMember()
                    .showTitle(
                            Title.title(
                                    title.asComponent(),
                                    subtitle.asComponent(),
                                    getTimes(fadeInTicks, fadeOutTicks, stayTicks)));
        }
    }

    public static void sendTitle(
            Message title,
            Message subtitle,
            int fadeInTicks,
            int fadeOutTicks,
            int stayTicks,
            MessagingChannel channel) {
        sendTitle(
                title,
                subtitle,
                fadeInTicks,
                fadeOutTicks,
                stayTicks,
                channel.getChannelMemebers());
    }

    public static void sendTitle(
            Message title,
            Message subtitle,
            int fadeInTicks,
            int fadeOutTicks,
            int stayTicks,
            MessagingMember... members) {
        for (MessagingMember member : members) {
            member.getAudienceMember()
                    .showTitle(
                            Title.title(
                                    title.asComponent(),
                                    subtitle.asComponent(),
                                    getTimes(fadeInTicks, fadeOutTicks, stayTicks)));
        }
    }

    public static void sendTitle(
            Message title,
            Message subtitle,
            int fadeInTicks,
            int fadeOutTicks,
            int stayTicks,
            Player... players) {
        for (Player player : players) {
            MessagingMember.ofPlayer(player)
                    .getAudienceMember()
                    .showTitle(
                            Title.title(
                                    title.asComponent(),
                                    subtitle.asComponent(),
                                    getTimes(fadeInTicks, fadeOutTicks, stayTicks)));
        }
    }

    public static void clearTitle(Collection<MessagingMember> members) {
        for (MessagingMember member : members) {
            member.getAudienceMember().clearTitle();
        }
    }

    public static void clearTitle(MessagingChannel channel) {
        clearTitle(channel.getChannelMemebers());
    }

    public static void clearTitle(MessagingMember... members) {
        for (MessagingMember member : members) {
            member.getAudienceMember().clearTitle();
        }
    }

    public static void clearTitle(Player... players) {
        for (Player player : players) {
            MessagingMember.ofPlayer(player).getAudienceMember().clearTitle();
        }
    }

    public static void resetTitle(Collection<MessagingMember> members) {
        for (MessagingMember member : members) {
            member.getAudienceMember().resetTitle();
        }
    }

    public static void resetTitle(MessagingChannel channel) {
        resetTitle(channel.getChannelMemebers());
    }

    public static void resetTitle(MessagingMember... members) {
        for (MessagingMember member : members) {
            member.getAudienceMember().resetTitle();
        }
    }

    public static void resetTitle(Player... players) {
        for (Player player : players) {
            MessagingMember.ofPlayer(player).getAudienceMember().resetTitle();
        }
    }

    private static Title.Times getTimes(int fadeIn, int fadeOut, int stay) {
        return Title.Times.of(
                Duration.ofMillis(fadeIn * 50L),
                Duration.ofMillis(stay * 50L),
                Duration.ofMillis(fadeOut * 50L));
    }
}
