package org.zibble.dbedwars.commands.dev;

import org.bukkit.command.CommandSender;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.commands.nodes.CommandNode;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.member.MessagingMember;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class DebugBaseCommand extends CommandNode {

    private static final Pattern PATTERN = Pattern.compile("(?<class>\\w+?)\\{(?<content>.*)}");
    private static final Pattern CONTENT_PATTERN = Pattern.compile(" ?+(?<key>.+?) ?+= ?+(?<value>'.+?'|\".+?\"|\\[.+?]|\\{.+?}|\\(.+?\\)|.[^,]*) ?+,?+ ?+", Pattern.CASE_INSENSITIVE);
    private static final Pattern VALUE_PATTERN = Pattern.compile(" ?+((?<numbervalue>[+-]?((\\d+(\\.\\d*)?)|(\\.\\d+)))|'(?<stringvalue>.+?)'|(?<boolvalue>true|false)|\\{(?<listvalue>\\w[^=]+?|'.+?')}|\\{ ?+(?<mapvalue>\\w+? ?+=?+ ?+.+? ?+,?+ ?+)}|(?<classvalue>\\w+?\\{.+?})|(?<nullvalue>null)) ?+,?+ ?+", Pattern.CASE_INSENSITIVE);

    protected final DBedwars plugin;
    protected final Messaging messaging;

    public DebugBaseCommand(DBedwars plugin, Messaging messaging) {
        this.plugin = plugin;
        this.messaging = messaging;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        MessagingMember member = this.messaging.getMemberOf(sender);

        if (args.length == 0) {
            member.sendMessage(AdventureMessage.from("<red>Please provide some arguments."));
            return false;
        }

        return this.execute(member, sender, args);
    }

    public abstract boolean execute(MessagingMember member, CommandSender sender, String[] args);

    protected String formatColors(String message) {
        Matcher matcher = PATTERN.matcher(message);
        if (matcher.matches()) {
            StringBuilder builder = new StringBuilder();
            builder.append("<gold>").append(matcher.group("class")).append("</gold>").append("<gray>{ ");
            builder.append(this.formatContent(matcher.group("content"))).append("}</gray>");
            return builder.toString();
        }

        return message;
    }

    protected String formatContent(String content) {
        StringBuilder builder = new StringBuilder();
        Matcher contentMatcher = CONTENT_PATTERN.matcher(content);
        boolean matched = false;
        while (contentMatcher.find()) {
            if (matched) {
                builder.append("<yellow>")
                        .append(",")
                        .append("</yellow> ");
            }
            matched = true;
            builder.append("<aqua>")
                    .append(contentMatcher.group("key"))
                    .append("</aqua> <yellow>=</yellow> ");
            Matcher valueMatcher = VALUE_PATTERN.matcher(contentMatcher.group("value"));
            if (valueMatcher.matches()) {
                builder.append(this.formatValue(valueMatcher));
            } else {
                builder.append(contentMatcher.group("value"));
            }
        }
        if (!matched) {
            builder.append("<gray>").append(content).append("</gray>");
        }
        return builder.toString();
    }

    protected String formatValue(Matcher contentMatcher) {
        StringBuilder builder = new StringBuilder();
        if (contentMatcher.group("numbervalue") != null) {
            builder.append("<red>")
                    .append(contentMatcher.group("numbervalue"))
                    .append("</red>");
        } else if (contentMatcher.group("stringvalue") != null) {
            builder.append("<green>")
                    .append("'")
                    .append(contentMatcher.group("stringvalue"))
                    .append("'")
                    .append("</green>");
        } else if (contentMatcher.group("boolvalue") != null) {
            builder.append("<dark_red>")
                    .append(contentMatcher.group("boolvalue"))
                    .append("</dark_red>");
        } else if (contentMatcher.group("classvalue") != null) {
            builder.append(this.formatColors(contentMatcher.group("classvalue")));
        } else if (contentMatcher.group("listvalue") != null) {
            builder.append("<gray>{ ");
            String listValue = contentMatcher.group("listValue");
            Matcher matcher = VALUE_PATTERN.matcher(listValue);
            boolean matched = false;
            while (matcher.find()) {
                if (matched) {
                    builder.append("<yellow>")
                            .append(",")
                            .append("</yellow> ");
                }
                matched = true;
                builder.append(this.formatValue(matcher));
            }
            if (!matched) {
                builder.append(listValue);
            }
            builder.append(" }</gray>");
        } else if (contentMatcher.group("mapvalue") != null) {
            builder.append("<gray>{ ")
                    .append(this.formatContent(contentMatcher.group("mapvalue")))
                    .append(" }</gray>");
        } else if (contentMatcher.group("nullvalue") != null) {
            builder.append("<dark_green>null</dark_green>");
        }
        return builder.toString();
    }

}
