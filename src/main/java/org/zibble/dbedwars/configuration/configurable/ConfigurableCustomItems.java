package org.zibble.dbedwars.configuration.configurable;

import org.bukkit.configuration.ConfigurationSection;
import org.zibble.dbedwars.configuration.framework.Loadable;
import org.zibble.dbedwars.configuration.framework.annotations.ConfigPath;
import org.zibble.dbedwars.configuration.framework.annotations.Defaults;

import java.util.ArrayList;
import java.util.List;

public class ConfigurableCustomItems implements Loadable {

    @ConfigPath
    private ConfigurableFireball fireball;

    @ConfigPath
    private ConfigurableTNT tnt;

    @ConfigPath
    private ConfigurablePopupTower popupTower;

    @ConfigPath
    private ConfigurableBridgeEgg bridgeEgg;

    @ConfigPath
    private ConfigurableWaterBucket waterBucket;

    @ConfigPath
    private ConfigurableSponge sponge;

    @ConfigPath
    private ConfigurableDreamDefender dreamDefender;

    @ConfigPath
    private ConfigurableBedBug bedBug;

    @ConfigPath
    private ConfigurableBlastProofGlass blastProofGlass;

    @Override
    public void load(ConfigurationSection section) {
        this.loadEntries(section);
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public boolean isInvalid() {
        return false;
    }

    public ConfigurableFireball getFireball() {
        return fireball;
    }

    public ConfigurableTNT getTnt() {
        return tnt;
    }

    public ConfigurablePopupTower getPopupTower() {
        return popupTower;
    }

    public ConfigurableBridgeEgg getBridgeEgg() {
        return bridgeEgg;
    }

    public ConfigurableWaterBucket getWaterBucket() {
        return waterBucket;
    }

    public ConfigurableSponge getSponge() {
        return sponge;
    }

    public ConfigurableDreamDefender getDreamDefender() {
        return dreamDefender;
    }

    public ConfigurableBedBug getBedBug() {
        return bedBug;
    }

    public ConfigurableBlastProofGlass getBlastProofGlass() {
        return blastProofGlass;
    }

    @Override
    public String toString() {
        return "ConfigurableCustomItems{" +
                "fireball=" + fireball +
                ", TNT=" + tnt +
                ", popupTower=" + popupTower +
                ", bridgeEgg=" + bridgeEgg +
                ", waterBucket=" + waterBucket +
                ", sponge=" + sponge +
                ", dreamDefender=" + dreamDefender +
                ", bedBug=" + bedBug +
                ", blastProofGlass=" + blastProofGlass +
                '}';
    }

    public static class ConfigurablePopupTower implements Loadable {

        @ConfigPath
        private String name;

        @ConfigPath
        private List<String> lore;

        @ConfigPath
        private String sound;

        @ConfigPath
        private String particle;

        @ConfigPath
        private String mainBlock;

        @Override
        public void load(ConfigurationSection section) {
            this.loadEntries(section);
        }

        @Override
        public boolean isValid() {
            return true;
        }

        @Override
        public boolean isInvalid() {
            return false;
        }

        public String getName() {
            return name;
        }

        public List<String> getLore() {
            return lore;
        }

        public String getMainBlock() {
            return mainBlock;
        }

        public String getParticle() {
            return particle;
        }

        public String getSound() {
            return sound;
        }

        @Override
        public String toString() {
            return "ConfigurablePopupTower{" +
                    "name='" + name + '\'' +
                    ", lore=" + lore +
                    ", sound='" + sound + '\'' +
                    ", particle='" + particle + '\'' +
                    ", mainBlock='" + mainBlock + '\'' +
                    '}';
        }

    }

    public static class ConfigurableTNT implements Loadable {

        @ConfigPath
        private String name;

        @ConfigPath
        private List<String> lore;

        @ConfigPath
        private boolean autoIgniteTnt;

        @ConfigPath
        private boolean fixRandomExplosion;

        @ConfigPath
        private boolean betterTntIgniteAnimation;

        @ConfigPath
        private int fuseTicks;

        @ConfigPath
        private ConfigurableKnockBack knockback;

        protected ConfigurableTNT() {
            /*name = "";
            lore = new ArrayList<>();
            autoIgniteTnt = true;
            fixRandomExplosion = true;
            fuseTicks = 52;
            knockback = new ConfigurableKnockBack();
            betterTntIgniteAnimation = true;*/
        }

        @Override
        public void load(ConfigurationSection section) {
            this.loadEntries(section);
        }

        @Override
        public boolean isValid() {
            return true;
        }

        @Override
        public boolean isInvalid() {
            return false;
        }

        public boolean isBetterTntIgniteAnimation() {
            return betterTntIgniteAnimation;
        }

        public List<String> getLore() {
            return lore;
        }

        public boolean isAutoIgniteTNTEnabled() {
            return autoIgniteTnt;
        }

        public boolean isFixRandomExplosionEnabled() {
            return fixRandomExplosion;
        }

        public int getFuseTicks() {
            return fuseTicks;
        }

        public String getName() {
            return name;
        }

        public ConfigurableKnockBack getKnockback() {
            return knockback;
        }

        @Override
        public String toString() {
            return "ConfigurableTNT{" +
                    "name='" + name + '\'' +
                    ", lore=" + lore +
                    ", autoIgniteTNT=" + autoIgniteTnt +
                    ", fixRandomExplosion=" + fixRandomExplosion +
                    ", isBetterTNTAnimationEnabled=" + betterTntIgniteAnimation +
                    ", fuseTicks=" + fuseTicks +
                    ", knockBack=" + knockback +
                    '}';
        }

    }

    public static class ConfigurableFireball implements Loadable {

        @ConfigPath
        private String name;

        @ConfigPath
        private List<String> lore;

        @ConfigPath
        private boolean leftClickThrow;

        @ConfigPath
        private boolean fixDirection;

        @ConfigPath
        private float explosionYield;

        @ConfigPath
        private double speedMultiplier;

        @ConfigPath
        private List<String> throwEffects;

        @ConfigPath
        private boolean explosionFire;

        @ConfigPath
        private ConfigurableKnockBack knockback;

        protected ConfigurableFireball() {
            name = "";
            lore = new ArrayList<>();
            leftClickThrow = false;
            fixDirection = true;
            explosionYield = 4;
            speedMultiplier = 5;
            explosionFire = true;
            throwEffects = new ArrayList<>();
        }

        @Override
        public void load(ConfigurationSection section) {
            this.loadEntries(section);
        }

        @Override
        public boolean isValid() {
            return true;
        }

        @Override
        public boolean isInvalid() {
            return false;
        }

        public List<String> getLore() {
            return lore;
        }

        public boolean isFixDirectionEnabled() {
            return fixDirection;
        }

        public boolean isLeftClickThrowEnabled() {
            return leftClickThrow;
        }

        public float getExplosionYield() {
            return explosionYield;
        }

        public double getSpeedMultiplier() {
            return speedMultiplier;
        }

        public ConfigurableKnockBack getKnockback() {
            return knockback;
        }

        public List<String> getThrowEffects() {
            return throwEffects;
        }

        public String getName() {
            return name;
        }

        public boolean isExplosionFireEnabled() {
            return explosionFire;
        }

        @Override
        public String toString() {
            return "ConfigurableFireball{" +
                    "displayName='" + name + '\'' +
                    ", lore=" + lore +
                    ", leftClickThrow=" + leftClickThrow +
                    ", fixDirection=" + fixDirection +
                    ", explosionYield=" + explosionYield +
                    ", speedMultiplier=" + speedMultiplier +
                    ", potionEffects=" + throwEffects +
                    ", explosionFire=" + explosionFire +
                    ", knockBack=" + knockback +
                    '}';
        }

    }

    public static class ConfigurableBridgeEgg implements Loadable {

        @ConfigPath
        private String name;

        @ConfigPath
        private List<String> lore;

        @ConfigPath
        private int keepAliveTimeOut;

        @ConfigPath("min-distance-from-player-to-start-placing-blocks")
        private int minDistanceFromPlayer;

        @ConfigPath("max-distance-from-player-to-keep-placing-blocks")
        private int maxDistanceFromPlayer;

        @ConfigPath
        private int maxDownStack;

        @ConfigPath("flip-bridge")
        private boolean flipBridge;

        public ConfigurableBridgeEgg() {
            lore = new ArrayList<>();
            flipBridge = true;
        }

        @Override
        public void load(ConfigurationSection section) {
            this.loadEntries(section);
        }

        @Override
        public boolean isValid() {
            return true;
        }

        @Override
        public boolean isInvalid() {
            return false;
        }

        public String getName() {
            return name;
        }

        public List<String> getLore() {
            return lore;
        }

        public int getKeepAliveTimeOut() {
            return keepAliveTimeOut;
        }

        public int getMinDistanceFromPlayer() {
            return minDistanceFromPlayer;
        }

        public int getMaxDistanceFromPlayer() {
            return maxDistanceFromPlayer;
        }

        public int getMaxDownStack() {
            return maxDownStack;
        }

        public boolean isFlipBridge() {
            return flipBridge;
        }

        @Override
        public String toString() {
            return "ConfigurableBridgeEgg{" +
                    "displayName='" + name + '\'' +
                    ", lore=" + lore +
                    ", keepAliveTimeOut=" + keepAliveTimeOut +
                    ", minDistanceFromPlayer=" + minDistanceFromPlayer +
                    ", maxDistanceFromPlayer=" + maxDistanceFromPlayer +
                    ", maxDownStack=" + maxDownStack +
                    ", flipBridgeEnabled=" + flipBridge +
                    '}';
        }

    }

    public static class ConfigurableWaterBucket implements Loadable {

        @ConfigPath
        private String name;

        @ConfigPath
        private List<String> lore;

        @ConfigPath("remove-on-use")
        private boolean removeOnUse;

        public ConfigurableWaterBucket() {
            this.name = "Water Bucket";
            this.lore = new ArrayList<>();
            this.removeOnUse = true;
        }

        @Override
        public void load(ConfigurationSection section) {
            this.loadEntries(section);
        }

        @Override
        public boolean isValid() {
            return true;
        }

        @Override
        public boolean isInvalid() {
            return false;
        }

        public boolean shouldRemoveOnUse() {
            return removeOnUse;
        }

        public List<String> getLore() {
            return lore;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "ConfigurableWaterBucket{" +
                    "displayName='" + name + '\'' +
                    ", lore=" + lore +
                    ", removeOnUse=" + removeOnUse +
                    '}';
        }

    }

    public static class ConfigurableSponge implements Loadable {

        @ConfigPath
        private String name;

        @ConfigPath
        private List<String> lore;

        @ConfigPath
        private boolean animationEnabled;

        @ConfigPath
        private int radiusForParticles;

        @ConfigPath
        private boolean removeSpongeOnAnimationEnd;

        @ConfigPath
        private String soundOnBoxIncrease;

        @ConfigPath
        private String soundOnEnd;

        @ConfigPath
        private boolean allowBreaking;

        @ConfigPath
        private String breakTryMessage;

        public ConfigurableSponge() {
            name = "SpongeBoy";
            lore = new ArrayList<>();
            animationEnabled = true;
            radiusForParticles = 4;
            removeSpongeOnAnimationEnd = true;
            soundOnBoxIncrease = "CLICK:0.2:0.5";
            soundOnEnd = "SPLASH2:1:1";
            allowBreaking = false;
            breakTryMessage = "";
        }

        @Override
        public void load(ConfigurationSection section) {
            this.loadEntries(section);
        }

        @Override
        public boolean isValid() {
            return true;
        }

        @Override
        public boolean isInvalid() {
            return false;
        }

        public String getName() {
            return name;
        }

        public List<String> getLore() {
            return lore;
        }

        public boolean shouldRemoveSpongeOnAnimationEnd() {
            return removeSpongeOnAnimationEnd;
        }

        public int getRadiusForParticles() {
            return radiusForParticles;
        }

        public String getSoundOnBoxIncrease() {
            return soundOnBoxIncrease;
        }

        public String getSoundOnEnd() {
            return soundOnEnd;
        }

        public boolean isBreakingAllowed() {
            return allowBreaking;
        }

        public String getBreakTryMessage() {
            return breakTryMessage;
        }

        public boolean isAnimationEnabled() {
            return animationEnabled;
        }

        @Override
        public String toString() {
            return "ConfigurableSponge{" +
                    "displayName='" + name + '\'' +
                    ", lore=" + lore +
                    ", animationEnabled=" + animationEnabled +
                    ", radiusForParticles=" + radiusForParticles +
                    ", removeSpongeOnAnimationEnd=" + removeSpongeOnAnimationEnd +
                    ", soundBoxIncrease='" + soundOnBoxIncrease + '\'' +
                    ", soundOnAnimationEnd='" + soundOnEnd + '\'' +
                    ", allowBreaking=" + allowBreaking +
                    ", breakTryMessage='" + breakTryMessage + '\'' +
                    '}';
        }

    }

    public static class ConfigurableDreamDefender implements Loadable {

        @ConfigPath("name")
        private String name;

        @ConfigPath("lore")
        private List<String> lore;

        @ConfigPath("golem-display-name")
        private String golemDisplayName;

        @ConfigPath("health-symbol")
        private String healthSymbol;

        @ConfigPath("health-color-codes")
        private String healthColorCodes;

        @ConfigPath("health-indicator-count")
        private int healthIndicatorCount;

        @ConfigPath("potion-effects")
        private List<String> potionEffects;

        @ConfigPath("time-until-despawn")
        private int ticksUntilDespawn;

        public ConfigurableDreamDefender() {
            name = "Dream Defender";
            lore = new ArrayList<>();
            golemDisplayName = "&f%time_left% &0[ %health_bar% &0]";
            healthSymbol = "● ";
            healthColorCodes = "f:8";
            healthIndicatorCount = 10;
            potionEffects = new ArrayList<>();
            ticksUntilDespawn = 4800;
        }

        @Override
        public void load(ConfigurationSection section) {
            this.loadEntries(section);
        }

        @Override
        public boolean isValid() {
            return true;
        }

        @Override
        public boolean isInvalid() {
            return false;
        }

        public String getName() {
            return name;
        }

        public List<String> getLore() {
            return lore;
        }

        public String getGolemDisplayName() {
            return golemDisplayName;
        }

        public String getHealthSymbol() {
            return healthSymbol;
        }

        public String getHealthColorCodes() {
            return healthColorCodes;
        }

        public int getHealthIndicatorCount() {
            return healthIndicatorCount;
        }

        public List<String> getPotionEffects() {
            return potionEffects;
        }

        public int getTicksUntilDespawn() {
            return ticksUntilDespawn;
        }

        @Override
        public String toString() {
            return "ConfigurableDreamDefender{" +
                    "itemName='" + name + '\'' +
                    ", itemLore=" + lore +
                    ", golemDisplayName='" + golemDisplayName + '\'' +
                    ", healthSymbol='" + healthSymbol + '\'' +
                    ", healthColorCodes='" + healthColorCodes + '\'' +
                    ", healthIndicatorCount=" + healthIndicatorCount +
                    ", golemPotionEffects=" + potionEffects +
                    ", ticksUntilDespawn=" + ticksUntilDespawn +
                    '}';
        }

    }

    public static class ConfigurableBedBug implements Loadable {

        @ConfigPath
        private String name;

        @ConfigPath
        private List<String> lore;

        @ConfigPath
        private String bedBugDisplayName;

        @ConfigPath
        private String healthSymbol;

        @ConfigPath
        private String healthFilledColorCode;

        @ConfigPath
        private String healthMissingColorCode;

        @ConfigPath
        private int healthIndicatorCount;

        @ConfigPath("potion-effects")
        private List<String> potionEffects;

        @ConfigPath
        private int ticksUntilDespawn;

        public ConfigurableBedBug() {
            name = "Snowball";
            lore = new ArrayList<>();
            bedBugDisplayName = "<black>[ <health_bar> ]";
            healthSymbol = "⏹ ";
            healthFilledColorCode = "<team_color>";
            healthMissingColorCode = "<dark_gray>";
            healthIndicatorCount = 5;
            potionEffects = new ArrayList<>();
            ticksUntilDespawn = 300;
        }

        @Override
        public void load(ConfigurationSection section) {
            this.loadEntries(section);
        }

        @Override
        public boolean isValid() {
            return true;
        }

        @Override
        public boolean isInvalid() {
            return false;
        }

        public int getHealthIndicatorCount() {
            return healthIndicatorCount;
        }

        public int getTicksUntilDespawn() {
            return ticksUntilDespawn;
        }

        public String getBedBugDisplayName() {
            return bedBugDisplayName;
        }

        public List<String> getPotionEffects() {
            return potionEffects;
        }

        public List<String> getLore() {
            return lore;
        }

        public String getHealthFilledColorCode() {
            return healthFilledColorCode;
        }

        public String getHealthMissingColorCode() {
            return healthMissingColorCode;
        }

        public String getHealthSymbol() {
            return healthSymbol;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "ConfigurableBedBug{" +
                    "itemName='" + name + '\'' +
                    ", itemLore=" + lore +
                    ", bedBugDisplayName='" + bedBugDisplayName + '\'' +
                    ", healthSymbol='" + healthSymbol + '\'' +
                    ", healthFilledColorCode='" + healthFilledColorCode + '\'' +
                    ", healthMissingColorCode='" + healthMissingColorCode + '\'' +
                    ", healthIndicatorCount=" + healthIndicatorCount +
                    ", bedBugPotionEffects=" + potionEffects +
                    ", ticksUntilDespawn=" + ticksUntilDespawn +
                    '}';
        }

    }

    public static class ConfigurableBlastProofGlass implements Loadable {

        @ConfigPath
        private String name;

        @ConfigPath
        private List<String> lore;

        public ConfigurableBlastProofGlass() {
            this.name = "";
            this.lore = new ArrayList<>();
        }

        @Override
        public void load(ConfigurationSection section) {
            this.loadEntries(section);
        }

        @Override
        public boolean isValid() {
            return true;
        }

        @Override
        public boolean isInvalid() {
            return false;
        }

        public List<String> getLore() {
            return lore;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "ConfigurableBlastProofGlass{" +
                    "glassItemName='" + name + '\'' +
                    ", glassItemLore=" + lore +
                    '}';
        }

    }

    public static class ConfigurableKnockBack implements Loadable {

        @Defaults.Boolean(true)
        @ConfigPath
        private boolean enabled;

        @Defaults.Integer(3)
        @ConfigPath
        private int radiusEntities;

        @Defaults.Double(16)
        @ConfigPath
        private double distanceModifier;

        @Defaults.Double(1.5)
        @ConfigPath
        private double heightForce;

        @Defaults.Double(3)
        @ConfigPath
        private double horizontalForce;

        protected ConfigurableKnockBack() {
        }

        @Override
        public void load(ConfigurationSection section) {
            this.loadEntries(section);
        }

        @Override
        public boolean isValid() {
            return true;
        }

        @Override
        public boolean isInvalid() {
            return false;
        }

        public double getDistanceModifier() {
            return distanceModifier;
        }

        public double getHeightForce() {
            return heightForce;
        }

        public double getHorizontalForce() {
            return horizontalForce;
        }

        public int getRadiusEntities() {
            return radiusEntities;
        }

        public boolean isEnabled() {
            return enabled;
        }

        @Override
        public String toString() {
            return "KnockBack{" +
                    "enabled=" + enabled +
                    ", radiusEntities=" + radiusEntities +
                    ", distanceModifier=" + distanceModifier +
                    ", heightForce=" + heightForce +
                    ", horizontalForce=" + horizontalForce +
                    '}';
        }

    }

}
