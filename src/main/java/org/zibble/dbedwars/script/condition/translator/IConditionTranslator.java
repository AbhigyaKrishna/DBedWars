package org.zibble.dbedwars.script.condition.translator;

import org.zibble.dbedwars.api.script.ScriptVariable;
import org.zibble.dbedwars.api.script.condition.Condition;
import org.zibble.dbedwars.api.script.condition.ConditionTranslator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class IConditionTranslator<T, R extends Condition> implements ConditionTranslator<T, R> {

    private static final Pattern REGEX = Pattern.compile("^(?<key>.*)\\s*\\{(?<value>.*)}$");

    @Override
    public R serialize(String condition, ScriptVariable<?>... variables) {
        Matcher matcher = REGEX.matcher(condition.trim());
        if (matcher.matches()) {
            return this.serialize(matcher.group("key"), matcher.group("value"));
        }
        return null;
    }

    public abstract R serialize(String key, String value, ScriptVariable<?>... variables);

}
