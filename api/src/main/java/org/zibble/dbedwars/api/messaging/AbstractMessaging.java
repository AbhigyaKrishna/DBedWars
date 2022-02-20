package org.zibble.dbedwars.api.messaging;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.title.Title;
import org.zibble.dbedwars.api.messaging.member.MessagingMember;
import org.zibble.dbedwars.api.messaging.message.Message;

import java.util.Set;
import java.util.function.Predicate;

public interface AbstractMessaging {

    default void sendMessage(Message message) {
        this.sendMessage(message, true);
    }

    default void sendMessage(Message[] messages) {
        this.sendMessage(messages, true);
    }

    void sendMessage(Message message, boolean papiParsed);

    void sendMessage(Message[] messages, boolean papiParsed);

    default void sendMessage(Message message, Predicate<MessagingMember> except) {
        this.sendMessage(message, true, except);
    }

    default void sendMessage(Message[] messages, Predicate<MessagingMember> except) {
        this.sendMessage(messages, true, except);
    }

    void sendMessage(Message message, boolean papiParsed, Predicate<MessagingMember> except);

    void sendMessage(Message[] messages, boolean papiParsed, Predicate<MessagingMember> except);

    BossBar sendBossBar(BossBar bossBar);

    BossBar sendBossBar(BossBar bossBar, Predicate<MessagingMember> except);

    default BossBar[] sendBossBar(Message message) {
        return this.sendBossBar(message, true);
    }

    BossBar[] sendBossBar(Message message, boolean papiParsed);

    default BossBar[] sendBossBar(Message message, Predicate<MessagingMember> except) {
        return this.sendBossBar(message, true, except);
    }

    BossBar[] sendBossBar(Message message, boolean papiParsed, Predicate<MessagingMember> except);

    BossBar[] sendBossBar(Message message, float progress, BossBar.Overlay overlay, BossBar.Color color, boolean papiParsed);

    default BossBar[] sendBossBar(Message message, float progress, BossBar.Overlay overlay, BossBar.Color color, Predicate<MessagingMember> except) {
        return this.sendBossBar(message, progress, overlay, color, true, except);
    }

    BossBar[] sendBossBar(Message message, float progress, BossBar.Overlay overlay, BossBar.Color color, boolean papiParsed, Predicate<MessagingMember> except);

    default BossBar[] sendBossBar(Message message, float progress, BossBar.Overlay overlay, BossBar.Color color, Set<BossBar.Flag> flags) {
        return this.sendBossBar(message, progress, overlay, color, flags, true);
    }

    BossBar[] sendBossBar(Message message, float progress, BossBar.Overlay overlay, BossBar.Color color, Set<BossBar.Flag> flags, boolean papiParsed);

    default BossBar[] sendBossBar(Message message, float progress, BossBar.Overlay overlay, BossBar.Color color, Set<BossBar.Flag> flags, Predicate<MessagingMember> except) {
        return this.sendBossBar(message, progress, overlay, color, flags, true, except);
    }

    BossBar[] sendBossBar(Message message, float progress, BossBar.Overlay overlay, BossBar.Color color, Set<BossBar.Flag> flags, boolean papiParsed, Predicate<MessagingMember> except);

    void hideBossBar(BossBar bossBar);

    void hideBossBar(BossBar bossBar, Predicate<MessagingMember> except);

    default void sendActionBar(Message message) {
        this.sendActionBar(message, true);
    }

    void sendActionBar(Message message, boolean papiParsed);

    default void sendActionBar(Message message, Predicate<MessagingMember> except) {
        this.sendActionBar(message, true, except);
    }

    void sendActionBar(Message message, boolean papiParsed, Predicate<MessagingMember> except);

    void sendTitle(Title title);

    void sendTitle(Title title, Predicate<MessagingMember> except);

    default void sendTitle(Message title) {
        this.sendTitle(title, true);
    }

    void sendTitle(Message title, boolean papiParsed);

    default void sendTitle(Message title, Predicate<MessagingMember> except) {
        this.sendTitle(title, true, except);
    }

    void sendTitle(Message title, boolean papiParsed, Predicate<MessagingMember> except);

    default void sendTitle(Message title, Message subtitle) {
        this.sendTitle(title, subtitle, true);
    }

    void sendTitle(Message title, Message subtitle, boolean papiParsed);

    default void sendTitle(Message title, Message subtitle, Predicate<MessagingMember> except) {
        this.sendTitle(title, subtitle, true, except);
    }

    void sendTitle(Message title, Message subtitle, boolean papiParsed, Predicate<MessagingMember> except);

    default void sendTitle(Message title, Title.Times times) {
        this.sendTitle(title, times, true);
    }

    void sendTitle(Message title, Title.Times times, boolean papiParsed);

    default void sendTitle(Message title, Title.Times times, Predicate<MessagingMember> except) {
        this.sendTitle(title, times, true, except);
    }

    void sendTitle(Message title, Title.Times times, boolean papiParsed, Predicate<MessagingMember> except);

    default void sendTitle(Message title, Message subtitle, Title.Times times) {
        this.sendTitle(title, subtitle, times, true);
    }

    void sendTitle(Message title, Message subtitle, Title.Times times, boolean papiParsed);

    default void sendTitle(Message title, Message subtitle, Title.Times times, Predicate<MessagingMember> except) {
        this.sendTitle(title, subtitle, times, true, except);
    }

    void sendTitle(Message title, Message subtitle, Title.Times times, boolean papiParsed, Predicate<MessagingMember> except);

    void clearTitle();

    void clearTitle(Predicate<MessagingMember> except);

    void resetTitle();

    void resetTitle(Predicate<MessagingMember> except);

}
