package org.zibble.dbedwars.hooks.defaults.hologram;

import com.pepedevs.radium.utils.itemstack.ItemStackUtils;
import org.zibble.dbedwars.utils.reflection.general.EnumReflection;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.zibble.dbedwars.api.hooks.hologram.HologramEntityType;
import org.zibble.dbedwars.api.hooks.hologram.HologramLine;
import org.zibble.dbedwars.configuration.language.ConfigLang;

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
            return new HologramLineImpl.Text(page, ConfigLang.getTranslator().translate(text));
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
            //TODO REWORK GETTING ENTITY TYPE
            HologramEntityType mapped = EnumReflection.getEnumConstant(HologramEntityType.class, entity);
            if (mapped == null) return null;
            return new HologramLineImpl.Entity(page, mapped);
        }

        return null;
    }

    //TODO AVENGER AK
    private static ItemStack getItemStack(final String item) {
        final String[] split = item.split(":");

        if(split.length < 1)
            return null;

        if(split[0].startsWith("PLAYER_HEAD")){
            if(split.length < 2)
                return null;

            int amount = 1;

            if(split.length == 3) {
                amount = Integer.parseInt(split[2]);
                amount = amount <= 0 ? 1 : amount;
            }

            final ItemStack skull = ItemStackUtils.getSkull(split[1]);
            skull.setAmount(amount);
            return skull;
        }else {
            Material material = Material.getMaterial(split[0]);
            if(material == null)
                return null;

            int amount = 1;

            if(split.length == 2) {
                amount = Integer.parseInt(split[1]);
                amount = amount <= 0 ? 1 : amount;
            }

            return new ItemStack(material,amount);
        }
    }


}
