package org.zibble.dbedwars.script.action;

import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.script.ScriptVariable;
import org.zibble.dbedwars.api.script.action.Action;
import org.zibble.dbedwars.api.script.action.ActionTranslator;
import org.zibble.dbedwars.api.objects.serializable.Duration;
import org.zibble.dbedwars.api.util.key.Key;

import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActionPreProcessor {

    private static final DBedwars PLUGIN = DBedwars.getInstance();

    private static final Pattern CHANCE_PATTERN = Pattern.compile("\\[CHANCE=(?<chanceValue>\\d+)]", Pattern.CASE_INSENSITIVE);
    private static final Pattern DELAY_PATTERN = Pattern.compile("\\[DELAY=(?<delayValue>\\d+[a-z])]", Pattern.CASE_INSENSITIVE);
    private static final Pattern REPEAT_PATTERN = Pattern.compile("\\[REPEAT=(?<repeatValue>\\d+)]", Pattern.CASE_INSENSITIVE);
    private static final Pattern ACTION_PATTERN = Pattern.compile("(.*) ?\\[(?<action>[A-Z]+?)] ?(?<arguments>.+)", Pattern.CASE_INSENSITIVE);

    public static ActionProcessor process(String input, ScriptVariable<?>... variables) {
        Matcher matcher = ACTION_PATTERN.matcher(input);
        if (!matcher.matches()) throw new IllegalArgumentException("Invalid action format");
        String actionName = matcher.group("action");
        ActionTranslator<?, ? extends Action> translator = PLUGIN.scriptRegistry().actionRegistry().getTranslator(actionName);
        return new ActionProcessor(translator.serialize(matcher.group("arguments"), variables), shouldExecute(input), delay(input), getRepeats(input));
    }

    public static ActionProcessor process(Key registry, String input, ScriptVariable<?>... variables) {
        Matcher matcher = ACTION_PATTERN.matcher(input);
        if (!matcher.matches()) throw new IllegalArgumentException("Invalid action format");
        String actionName = matcher.group("action");
        ActionTranslator<?, ? extends Action> translator = PLUGIN.scriptRegistry().actionRegistry(registry).getTranslator(actionName);
        return new ActionProcessor(translator.serialize(matcher.group("arguments"), variables), shouldExecute(input), delay(input), getRepeats(input));
    }

    private static boolean shouldExecute(String input) {
        Matcher matcher = CHANCE_PATTERN.matcher(input);
        if (matcher.matches()) {
            int chance = Integer.parseInt(matcher.group("chanceValue"));
            return ThreadLocalRandom.current().nextInt(100) + 1 <= chance;
        }
        return true;
    }

    private static Duration delay(String input) {
        Matcher matcher = DELAY_PATTERN.matcher(input);
        if (matcher.matches()) {
            String delayValue = matcher.group("delayValue");
            return Duration.valueOf(delayValue);
        }
        return Duration.ZERO;
    }

    private static int getRepeats(String input) {
        Matcher matcher = REPEAT_PATTERN.matcher(input);
        if (matcher.matches()) {
            try {
                return Integer.parseInt(matcher.group("repeatValue"));
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

}
