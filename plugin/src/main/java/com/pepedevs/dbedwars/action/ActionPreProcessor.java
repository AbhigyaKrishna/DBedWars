package com.pepedevs.dbedwars.action;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.action.objects.ProcessedActionHolder;
import com.pepedevs.dbedwars.api.action.ActionPlaceholder;
import com.pepedevs.dbedwars.api.action.ActionTranslator;
import com.pepedevs.dbedwars.utils.TimeUtil;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActionPreProcessor {

    private static final DBedwars PLUGIN = DBedwars.getInstance();

    private static final Matcher CHANCE_MATCHER = Pattern.compile("\\[CHANCE=(?<chanceValue>\\d+)]", Pattern.CASE_INSENSITIVE).matcher("");
    private static final Matcher DELAY_MATCHER = Pattern.compile("\\[DELAY=(?<delayValue>\\d+[a-z])]", Pattern.CASE_INSENSITIVE).matcher("");
    private static final Matcher ACTION_MATCHER = Pattern.compile("(.*) ?\\[(?<action>[A-Z]+?)] ?(?<arguments>.+)", Pattern.CASE_INSENSITIVE).matcher("");

    public static ProcessedActionHolder<?> process(String input, ActionPlaceholder<?, ?>... placeholders) {
        ACTION_MATCHER.reset(input);
        if (!ACTION_MATCHER.matches()) throw new IllegalArgumentException("Invalid action format");
        String actionName = ACTION_MATCHER.group("action");
        ActionTranslator<?, ?> translator = PLUGIN.actionRegistry().getTranslator(actionName);
        List<ActionPlaceholder<?, ?>> translatorPlaceholders = new ArrayList<>();
        for (ActionPlaceholder<?, ?> placeholder : placeholders) {
            if (translator.getKey().get().equalsIgnoreCase(placeholder.forTranslator())) translatorPlaceholders.add(placeholder);
        }
        return new ProcessedActionHolder(translator.serialize(ACTION_MATCHER.group("arguments"), translatorPlaceholders.toArray(new ActionPlaceholder[0])), shouldExecute(input), delay(input));
    }

    private static boolean shouldExecute(String input) {
        CHANCE_MATCHER.reset(input);
        if (CHANCE_MATCHER.matches()) {
            int chance = Integer.parseInt(CHANCE_MATCHER.group("chanceValue"));
            return ThreadLocalRandom.current().nextInt(100) + 1 <= chance;
        }
        return true;
    }

    private static Duration delay(String input) {
        DELAY_MATCHER.reset(input);
        if (DELAY_MATCHER.matches()) {
            String delayValue = DELAY_MATCHER.group("delayValue");
            return TimeUtil.parse(delayValue);
        }
        return Duration.ZERO;
    }

}
