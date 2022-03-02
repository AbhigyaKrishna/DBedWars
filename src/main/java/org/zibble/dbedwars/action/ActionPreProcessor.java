package org.zibble.dbedwars.action;

import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.action.objects.ProcessedActionHolder;
import org.zibble.dbedwars.api.action.ActionPlaceholder;
import org.zibble.dbedwars.api.action.ActionTranslator;
import org.zibble.dbedwars.api.util.Duration;
import org.zibble.dbedwars.utils.TimeUtil;

import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActionPreProcessor {

    private static final DBedwars PLUGIN = DBedwars.getInstance();

    private static final Matcher CHANCE_MATCHER = Pattern.compile("\\[CHANCE=(?<chanceValue>\\d+)]", Pattern.CASE_INSENSITIVE).matcher("");
    private static final Matcher DELAY_MATCHER = Pattern.compile("\\[DELAY=(?<delayValue>\\d+[a-z])]", Pattern.CASE_INSENSITIVE).matcher("");
    private static final Matcher REPEAT_MATCHER = Pattern.compile("\\[REPEAT=(?<repeatValue>\\d+)]", Pattern.CASE_INSENSITIVE).matcher("");
    private static final Matcher ACTION_MATCHER = Pattern.compile("(.*) ?\\[(?<action>[A-Z]+?)] ?(?<arguments>.+)", Pattern.CASE_INSENSITIVE).matcher("");

    public static ProcessedActionHolder process(String input, ActionPlaceholder<?, ?>... placeholders) {
        ACTION_MATCHER.reset(input);
        if (!ACTION_MATCHER.matches()) throw new IllegalArgumentException("Invalid action format");
        String actionName = ACTION_MATCHER.group("action");
        ActionTranslator<?, ?> translator = PLUGIN.actionRegistry().getTranslator(actionName);
        return new ProcessedActionHolder(translator.serialize(ACTION_MATCHER.group("arguments"), placeholders), shouldExecute(input), delay(input), getRepeats(input));
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
        return Duration.zero();
    }

    private static int getRepeats(String input) {
        REPEAT_MATCHER.reset(input);
        if (REPEAT_MATCHER.matches()) {
            try {
                return Integer.parseInt(REPEAT_MATCHER.group("repeatValue"));
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

}
