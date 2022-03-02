package org.zibble.dbedwars.utils.gamerule;

import org.apache.commons.configLang.Validate;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.NumberConversions;

public class GameRuleMetadata extends FixedMetadataValue {

    protected final org.zibble.dbedwars.utils.gamerule.GameRuleType type;
    
    public GameRuleMetadata(Plugin owning_plugin, org.zibble.dbedwars.utils.gamerule.GameRuleType type, Object value) {
        super(owning_plugin, value);
        Validate.isTrue(type.isSameDataType(value));
        this.type = type;
    }
    
    public GameRuleType getType() {
        return this.type;
    }
    
    public int asInt() {
        validateNumericValue();
        return NumberConversions.toInt(this.value());
    }
    
    public float asFloat() {
        validateNumericValue();
        return NumberConversions.toFloat(this.value());
    }
    
    public double asDouble() {
        validateNumericValue();
        return NumberConversions.toDouble(this.value());
    }

    public long asLong() {
        validateNumericValue();
        return NumberConversions.toLong(this.value());
    }

    public short asShort() {
        validateNumericValue();
        return NumberConversions.toShort(this.value());
    }

    public byte asByte() {
        validateNumericValue();
        return NumberConversions.toByte(this.value());
    }

    public boolean asBoolean() {
        validateBooleanValue();
        return (Boolean) this.value();
    }

    protected void validateNumericValue() {
        Validate.isTrue(this.getType().isNumericalValue() && this.value() instanceof Number, "Wrong value type!");
    }

    protected void validateBooleanValue() {
        Validate.isTrue(this.getType().isBooleanValue() && this.value() instanceof Boolean, "Wrong value type!");
    }

}
