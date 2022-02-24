package org.zibble.dbedwars.hooks.defaults.hologram;

import org.zibble.dbedwars.api.hooks.hologram.HologramEntityType;
import org.zibble.dbedwars.api.hooks.hologram.HologramLine;
import org.zibble.dbedwars.configuration.Lang;
import com.pepedevs.radium.utils.reflection.general.EnumReflection;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HologramUtil {

    private static final Matcher TEXT_MATCHER = Pattern.compile("\\[TEXT=(?<text>\\d+)]", Pattern.CASE_INSENSITIVE).matcher("");
    private static final Matcher HEAD_MATCHER = Pattern.compile("\\[HEAD=(?<head>\\d+)]", Pattern.CASE_INSENSITIVE).matcher("");
    private static final Matcher SMALL_HEAD_MATCHER = Pattern.compile("\\[SMALL_HEAD=(?<smallHead>\\d+)]", Pattern.CASE_INSENSITIVE).matcher("");
    private static final Matcher ICON_MATCHER = Pattern.compile("\\[ICON=(?<icon>\\d+)]", Pattern.CASE_INSENSITIVE).matcher("");
    private static final Matcher ENTITY_MATCHER = Pattern.compile("\\[ENTITY=(?<entity>\\d+)]", Pattern.CASE_INSENSITIVE).matcher("");

    public static HologramLine<?> getHologram(final HologramPageImpl page, final String string) {

        TEXT_MATCHER.reset(string);
        if (TEXT_MATCHER.matches()) {
            String text = TEXT_MATCHER.group("text");
            return new HologramLineImpl.Text(page, Lang.getTranslator().translate(text));
        }

        HEAD_MATCHER.reset(string);
        if (HEAD_MATCHER.matches()) {
            String head = HEAD_MATCHER.group("head");
            ItemStack itemStack = getItemStack(head);
            if (itemStack == null) return null;
            return new HologramLineImpl.Head(page, itemStack);
        }

        SMALL_HEAD_MATCHER.reset(string);
        if (SMALL_HEAD_MATCHER.matches()) {
            String smallHead = SMALL_HEAD_MATCHER.group("smallHead");
            ItemStack itemStack = getItemStack(smallHead);
            if (itemStack == null) return null;
            return new HologramLineImpl.SmallHead(page, itemStack);
        }

        ICON_MATCHER.reset(string);
        if (ICON_MATCHER.matches()) {
            String icon = ICON_MATCHER.group("icon");
            ItemStack itemStack = getItemStack(icon);
            if (itemStack == null) return null;
            return new HologramLineImpl.Icon(page, itemStack);
        }

        ENTITY_MATCHER.reset(string);
        if (ENTITY_MATCHER.matches()) {
            String entity = ENTITY_MATCHER.group("entity");
            HologramEntityType mapped = EnumReflection.getEnumConstant(HologramEntityType.class, entity);
            if (mapped == null) return null;
            return new HologramLineImpl.Entity(page, mapped);
        }

        return null;
    }

    private static ItemStack getItemStack(final String item) {
        return null;
    }

}