package me.abhigya.dbedwars.configuration.configurable;

import me.Abhigya.core.util.loadable.Loadable;
import me.Abhigya.core.util.loadable.LoadableEntry;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class ConfigurableCustomItems implements Loadable {

  private ConfigurableFireball fireball;
  private ConfigurableTNT TNT;
  private ConfigurablePopupTower popupTower;
  private ConfigurableBridgeEgg bridgeEgg;
  private ConfigurableWaterBucket waterBucket;
  private ConfigurableSponge sponge;
  private ConfigurableDreamDefender dreamDefender;
  private ConfigurableBedBug bedBug;
  private ConfigurableBlastProofGlass blastProofGlass;

  @Override
  public Loadable load(ConfigurationSection section) {
    fireball = new ConfigurableFireball();
    fireball.load(section.getConfigurationSection("fireball"));
    TNT = new ConfigurableTNT();
    TNT.load(section.getConfigurationSection("tnt"));
    popupTower = new ConfigurablePopupTower();
    popupTower.load(section.getConfigurationSection("popup-tower"));
    bridgeEgg = new ConfigurableBridgeEgg();
    bridgeEgg.load(section.getConfigurationSection("bridge-egg"));
    waterBucket = new ConfigurableWaterBucket();
    waterBucket.load(section.getConfigurationSection("water-bucket"));
    sponge = new ConfigurableSponge();
    sponge.load(section.getConfigurationSection("sponge"));
    dreamDefender = new ConfigurableDreamDefender();
    dreamDefender.load(section.getConfigurationSection("dream-defender"));
    bedBug = new ConfigurableBedBug();
    bedBug.load(section.getConfigurationSection("bed-bug"));
    blastProofGlass = new ConfigurableBlastProofGlass();
    blastProofGlass.load(section.getConfigurationSection("blast-proof-glass"));
    return this.loadEntries(section);
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

  public ConfigurableTNT getTNT() {
    return TNT;
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

  public static class ConfigurablePopupTower implements Loadable {

    @LoadableEntry(key = "name")
    private String name;

    @LoadableEntry(key = "lore")
    private List<String> lore;

    @LoadableEntry(key = "sound")
    private String sound;

    @LoadableEntry(key = "particle")
    private String particle;

    @LoadableEntry(key = "main-block")
    private String mainBlock;

    protected ConfigurablePopupTower() {
      name = "&bCompact Pop-up Tower";
      lore = new ArrayList<>();
      mainBlock = "WOOL%team%";
    }

    @Override
    public Loadable load(ConfigurationSection section) {
      return this.loadEntries(section);
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
  }

  public static class ConfigurableTNT implements Loadable {

    @LoadableEntry(key = "name")
    private String name;

    @LoadableEntry(key = "lore")
    private List<String> lore;

    @LoadableEntry(key = "auto-ignite-tnt")
    private boolean autoIgniteTNT;

    @LoadableEntry(key = "fix-random-explosion")
    private boolean fixRandomExplosion;

    @LoadableEntry(key = "better-tnt-ignite-animation")
    private boolean isBetterTNTAnimationEnabled;

    @LoadableEntry(key = "fuse-ticks")
    private int fuseTicks;

    private KnockBack knockBack;

    protected ConfigurableTNT() {
      name = "";
      lore = new ArrayList<>();
      autoIgniteTNT = true;
      fixRandomExplosion = true;
      fuseTicks = 52;
      knockBack = new KnockBack();
      isBetterTNTAnimationEnabled = true;
    }

    @Override
    public Loadable load(ConfigurationSection section) {
      knockBack.load(section.getConfigurationSection("knockback"));
      return this.loadEntries(section);
    }

    @Override
    public boolean isValid() {
      return true;
    }

    @Override
    public boolean isInvalid() {
      return false;
    }

    public boolean isBetterTNTAnimationEnabled() {
      return isBetterTNTAnimationEnabled;
    }

    public List<String> getLore() {
      return lore;
    }

    public boolean isAutoIgniteTNTEnabled() {
      return autoIgniteTNT;
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

    public KnockBack getKnockBack() {
      return knockBack;
    }
  }

  public static class ConfigurableFireball implements Loadable {

    @LoadableEntry(key = "name")
    private String displayName;

    @LoadableEntry(key = "lore")
    private List<String> lore;

    @LoadableEntry(key = "left-click-throw")
    private boolean leftClickThrow;

    @LoadableEntry(key = "fix-direction")
    private boolean fixDirection;

    @LoadableEntry(key = "explosion-yield")
    private float explosionYield;

    @LoadableEntry(key = "speed-multiplier")
    private double speedMultiplier;

    @LoadableEntry(key = "throw-effects")
    private List<String> potionEffects;

    @LoadableEntry(key = "explosion-fire")
    private boolean explosionFire;

    private KnockBack knockBack;

    protected ConfigurableFireball() {
      displayName = "";
      lore = new ArrayList<>();
      leftClickThrow = false;
      fixDirection = true;
      explosionYield = 4;
      speedMultiplier = 5;
      explosionFire = true;
      potionEffects = new ArrayList<>();
    }

    @Override
    public Loadable load(ConfigurationSection section) {
      knockBack = new KnockBack();
      knockBack.load(section.getConfigurationSection("knockback"));
      return this.loadEntries(section);
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

    public KnockBack getKnockBack() {
      return knockBack;
    }

    public List<String> getPotionEffects() {
      return potionEffects;
    }

    public String getDisplayName() {
      return displayName;
    }

    public boolean isExplosionFireEnabled() {
      return explosionFire;
    }
  }

  public static class ConfigurableBridgeEgg implements Loadable {

    @LoadableEntry(key = "name")
    private String displayName;

    @LoadableEntry(key = "lore")
    private List<String> lore;

    @LoadableEntry(key = "keep-alive-timeout")
    private int keepAliveTimeOut;

    @LoadableEntry(key = "min-distance-from-player-to-start-placing-blocks")
    private int minDistanceFromPlayer;

    @LoadableEntry(key = "max-distance-from-player-to-keep-placing-blocks")
    private int maxDistanceFromPlayer;

    @LoadableEntry(key = "max-down-stack")
    private int maxDownStack;

    @LoadableEntry(key = "flip-bridge")
    private boolean flipBridgeEnabled;

    public ConfigurableBridgeEgg() {
      lore = new ArrayList<>();
      flipBridgeEnabled = true;
    }

    @Override
    public Loadable load(ConfigurationSection section) {
      return this.loadEntries(section);
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
      return displayName;
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

    public boolean isFlipBridgeEnabled() {
      return flipBridgeEnabled;
    }
  }

  public static class ConfigurableWaterBucket implements Loadable {

    @LoadableEntry(key = "name")
    private String displayName;

    @LoadableEntry(key = "lore")
    private List<String> lore;

    @LoadableEntry(key = "remove-on-use")
    private boolean removeOnUse;

    public ConfigurableWaterBucket() {
      this.displayName = "Water Bucket";
      this.lore = new ArrayList<>();
      this.removeOnUse = true;
    }

    @Override
    public Loadable load(ConfigurationSection section) {
      return this.loadEntries(section);
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

    public String getDisplayName() {
      return displayName;
    }
  }

  public static class ConfigurableSponge implements Loadable {

    @LoadableEntry(key = "name")
    private String displayName;

    @LoadableEntry(key = "lore")
    private List<String> lore;

    @LoadableEntry(key = "animation-enabled")
    private boolean animationEnabled;

    @LoadableEntry(key = "radius-for-particles")
    private int radiusForParticles;

    @LoadableEntry(key = "remove-on-animation-end")
    private boolean removeSpongeOnAnimationEnd;

    @LoadableEntry(key = "sound-on-box-increase")
    private String soundBoxIncrease;

    @LoadableEntry(key = "sound-on-end")
    private String soundOnAnimationEnd;

    @LoadableEntry(key = "allow-breaking")
    private boolean allowBreaking;

    @LoadableEntry(key = "break-message")
    private String breakTryMessage;

    public ConfigurableSponge() {
      displayName = "SpongeBoy";
      lore = new ArrayList<>();
      animationEnabled = true;
      radiusForParticles = 4;
      removeSpongeOnAnimationEnd = true;
      soundBoxIncrease = "CLICK:0.2:0.5";
      soundOnAnimationEnd = "SPLASH2:1:1";
      allowBreaking = false;
      breakTryMessage = "";
    }

    @Override
    public Loadable load(ConfigurationSection section) {
      return this.loadEntries(section);
    }

    @Override
    public boolean isValid() {
      return true;
    }

    @Override
    public boolean isInvalid() {
      return false;
    }

    public String getDisplayName() {
      return displayName;
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

    public String getSoundBoxIncrease() {
      return soundBoxIncrease;
    }

    public String getSoundOnAnimationEnd() {
      return soundOnAnimationEnd;
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
  }

  public static class ConfigurableDreamDefender implements Loadable {

    @LoadableEntry(key = "name")
    private String itemName;

    @LoadableEntry(key = "lore")
    private List<String> itemLore;

    @LoadableEntry(key = "golem-display-name")
    private String golemDisplayName;

    @LoadableEntry(key = "health-symbol")
    private String healthSymbol;

    @LoadableEntry(key = "health-color-codes")
    private String healthColorCodes;

    @LoadableEntry(key = "health-indicator-count")
    private int healthIndicatorCount;

    @LoadableEntry(key = "potion-effects")
    private List<String> golemPotionEffects;

    @LoadableEntry(key = "time-until-despawn")
    private int ticksUntilDespawn;

    public ConfigurableDreamDefender() {
      itemName = "Dream Defender";
      itemLore = new ArrayList<>();
      golemDisplayName = "&f%time_left% &0[ %health_bar% &0]";
      healthSymbol = "● ";
      healthColorCodes = "f:8";
      healthIndicatorCount = 10;
      golemPotionEffects = new ArrayList<>();
      ticksUntilDespawn = 4800;
    }

    @Override
    public Loadable load(ConfigurationSection section) {
      return this.loadEntries(section);
    }

    @Override
    public boolean isValid() {
      return true;
    }

    @Override
    public boolean isInvalid() {
      return false;
    }

    public String getItemName() {
      return itemName;
    }

    public List<String> getItemLore() {
      return itemLore;
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

    public List<String> getGolemPotionEffects() {
      return golemPotionEffects;
    }

    public int getTicksUntilDespawn() {
      return ticksUntilDespawn;
    }
  }

  public static class ConfigurableBedBug implements Loadable {

    @LoadableEntry(key = "name")
    private String itemName;

    @LoadableEntry(key = "lore")
    private List<String> itemLore;

    @LoadableEntry(key = "bed-bug-display-name")
    private String bedBugDisplayName;

    @LoadableEntry(key = "health-symbol")
    private String healthSymbol;

    @LoadableEntry(key = "health-color-codes")
    private String healthColorCodes;

    @LoadableEntry(key = "health-indicator-count")
    private int healthIndicatorCount;

    @LoadableEntry(key = "potion-effects")
    private List<String> bedBugPotionEffects;

    @LoadableEntry(key = "time-until-despawn")
    private int ticksUntilDespawn;

    public ConfigurableBedBug() {
      itemName = "Snowball";
      itemLore = new ArrayList<>();
      bedBugDisplayName = "&0[ %health_bar% &0]";
      healthSymbol = "⏹ ";
      healthColorCodes = "%team_color%:8";
      healthIndicatorCount = 5;
      bedBugPotionEffects = new ArrayList<>();
      ticksUntilDespawn = 300;
    }

    @Override
    public Loadable load(ConfigurationSection section) {
      return this.loadEntries(section);
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

    public List<String> getBedBugPotionEffects() {
      return bedBugPotionEffects;
    }

    public List<String> getItemLore() {
      return itemLore;
    }

    public String getHealthColorCodes() {
      return healthColorCodes;
    }

    public String getHealthSymbol() {
      return healthSymbol;
    }

    public String getItemName() {
      return itemName;
    }
  }

  public static class ConfigurableBlastProofGlass implements Loadable {

    @LoadableEntry(key = "name")
    private String glassItemName;

    @LoadableEntry(key = "lore")
    private List<String> glassItemLore;

    public ConfigurableBlastProofGlass() {
      this.glassItemName = "";
      this.glassItemLore = new ArrayList<>();
    }

    @Override
    public Loadable load(ConfigurationSection section) {
      return this.loadEntries(section);
    }

    @Override
    public boolean isValid() {
      return true;
    }

    @Override
    public boolean isInvalid() {
      return false;
    }

    public List<String> getGlassItemLore() {
      return glassItemLore;
    }

    public String getGlassItemName() {
      return glassItemName;
    }
  }

  public static class KnockBack implements Loadable {

    @LoadableEntry(key = "enabled")
    private boolean enabled;

    @LoadableEntry(key = "radius-entities")
    private int radiusEntities;

    @LoadableEntry(key = "distance-modifier")
    private double distanceModifier;

    @LoadableEntry(key = "height-force")
    private double heightForce;

    @LoadableEntry(key = "horizontal-force")
    private double horizontalForce;

    protected KnockBack() {
      enabled = true;
      radiusEntities = 3;
      distanceModifier = 16;
      heightForce = 1.5;
      horizontalForce = 3;
    }

    @Override
    public Loadable load(ConfigurationSection section) {
      return this.loadEntries(section);
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
  }
}
