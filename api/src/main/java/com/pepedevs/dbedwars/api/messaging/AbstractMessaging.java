package com.pepedevs.dbedwars.api.messaging;

import com.pepedevs.dbedwars.api.messaging.member.MessagingMember;
import com.pepedevs.dbedwars.api.messaging.message.Message;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.title.Title;

import java.util.function.Predicate;

public interface AbstractMessaging {

    BossBar sendBossBar(BossBar bossBar);

    BossBar sendBossBar(BossBar bossBar, Predicate<MessagingMember> except);

    BossBar sendBossBar(Message message);

    BossBar sendBossBar(Message message, Predicate<MessagingMember> except);

    BossBar sendBossBar(Message message, float progress);

    BossBar sendBossBar(Message message, float progress, Predicate<MessagingMember> except);

    BossBar sendBossBar(Message message, BossBar.Color color);

    BossBar sendBossBar(Message message, BossBar.Color color, Predicate<MessagingMember> except);

    BossBar sendBossBar(Message message, BossBar.Overlay overlay);

    BossBar sendBossBar(Message message, BossBar.Overlay overlay, Predicate<MessagingMember> except);

    void hideBossBar(BossBar bossBar);

    void hideBossBar(BossBar bossBar, Predicate<MessagingMember> except);

    void sendActionBar(Message message);

    void sendActionBar(Message message, Predicate<MessagingMember> except);

    void sendTitle(Title title);

    void sendTitle(Title title, Predicate<MessagingMember> except);

    void sendTitle(Message title);

    void sendTitle(Message title, Predicate<MessagingMember> except);

    void sendTitle(Message title, Message subtitle);

    void sendTitle(Message title, Message subtitle, Predicate<MessagingMember> except);

    void sendTitle(Message title, int fadeInTicks, int fadeOutTicks, int stayTicks);

    void sendTitle(Message title, int fadeInTicks, int fadeOutTicks, int stayTicks, Predicate<MessagingMember> except);

    void sendTitle(Message title, Message subtitle, int fadeInTicks, int fadeOutTicks, int stayTicks);

    void sendTitle(Message title, Message subtitle, int fadeInTicks, int fadeOutTicks, int stayTicks, Predicate<MessagingMember> except);

    void clearTitle();

    void clearTitle(Predicate<MessagingMember> except);

    void resetTitle();

    void resetTitle(Predicate<MessagingMember> except);

}
