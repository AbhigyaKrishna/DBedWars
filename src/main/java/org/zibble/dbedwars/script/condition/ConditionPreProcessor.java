package org.zibble.dbedwars.script.condition;

import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.script.ScriptVariable;
import org.zibble.dbedwars.api.script.condition.Condition;
import org.zibble.dbedwars.api.script.condition.ConditionTranslator;
import org.zibble.dbedwars.api.util.key.Key;

import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConditionPreProcessor {

    private static final DBedwars PLUGIN = DBedwars.getInstance();

    private static final Pattern CHANCE_MATCHER = Pattern.compile("\\[CHANCE=(?<chanceValue>\\d+)]", Pattern.CASE_INSENSITIVE);
    private static final Pattern CONDITION_PATTERN = Pattern.compile("(.*) ?\\[(?<condition>[A-Z]+?)] ?(?<arguments>.+)", Pattern.CASE_INSENSITIVE);

    public static Condition process(String input, ScriptVariable<?>... variables) {
        Matcher matcher = CONDITION_PATTERN.matcher(input);
        if (!matcher.matches()) throw new IllegalArgumentException("Invalid action format");
        String conditionName = matcher.group("condition");
        ConditionTranslator<? extends Condition> translator = PLUGIN.scriptRegistry().conditionRegistry().getTranslator(conditionName);
        return translator != null && shouldExecute(input) ? translator.serialize(matcher.group("arguments").trim(), variables) : null;
    }

    public static Condition process(Key key, String input, ScriptVariable<?>... variables) {
        Matcher matcher = CONDITION_PATTERN.matcher(input);
        if (!matcher.matches()) throw new IllegalArgumentException("Invalid action format");
        String conditionName = matcher.group("condition");
        ConditionTranslator<? extends Condition> translator = PLUGIN.scriptRegistry().conditionRegistry(key).getTranslator(conditionName);
        return translator != null && shouldExecute(input) ? translator.serialize(matcher.group("arguments").trim(), variables) : null;
    }

    private static boolean shouldExecute(String input) {
        Matcher matcher = CHANCE_MATCHER.matcher(input);
        if (matcher.matches()) {
            int chance = Integer.parseInt(matcher.group("chanceValue"));
            return ThreadLocalRandom.current().nextInt(100) + 1 <= chance;
        }
        return true;
    }

}
