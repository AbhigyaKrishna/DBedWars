package me.abhigya.dbedwars.game.arena.view.shop;

import me.Abhigya.core.menu.inventory.action.ItemClickAction;
import me.Abhigya.core.menu.inventory.item.action.ActionItem;
import me.Abhigya.core.menu.inventory.item.action.ItemAction;
import me.Abhigya.core.menu.inventory.item.action.ItemActionPriority;
import me.Abhigya.core.util.StringUtils;
import me.Abhigya.core.util.itemstack.ItemMetaBuilder;
import me.Abhigya.core.util.loadable.LoadableEntry;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.api.events.PlayerPurchaseItemEvent;
import me.abhigya.dbedwars.api.exceptions.OverrideException;
import me.abhigya.dbedwars.api.game.ArenaPlayer;
import me.abhigya.dbedwars.api.util.BwItemStack;
import me.abhigya.dbedwars.api.util.LEnchant;
import me.abhigya.dbedwars.api.util.Overridable;
import me.abhigya.dbedwars.api.util.item.PluginActionItem;
import me.abhigya.dbedwars.configuration.configurable.ConfigurableShop;
import me.abhigya.dbedwars.guis.ShopGui;
import me.abhigya.dbedwars.utils.ConfigurationUtils;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class ViewItem implements me.abhigya.dbedwars.api.game.view.ViewItem {

  private ArenaPlayer player;
  private ConfigurableShop.ConfigurablePage.BwGUIItem cfgItem;
  private String key;
  private ShopPage page;

  private BwItemStack material;
  private int amount;
  private String name;
  private List<String> lore;
  private List<LEnchant> enchants;
  private Map<AttributeType, me.abhigya.dbedwars.api.game.view.ViewItem.Attribute> attributes;
  private ActionItem actionItem;

  public ViewItem(ShopPage page) {
    this.lore = new ArrayList<>();
    this.enchants = new ArrayList<>();
    this.attributes = new ConcurrentHashMap<>();
    this.page = page;
  }

  public ViewItem(
      ArenaPlayer p, ShopPage page, ConfigurableShop.ConfigurablePage.BwGUIItem item, String key) {
    this.player = p;
    this.cfgItem = item;
    this.key = key;
    this.material = ConfigurationUtils.parseItem(p.getTeam(), item.getMaterial());
    this.amount = item.getAmount();
    this.name = item.getItemName();
    this.lore = item.getItemLore();
    this.enchants = item.getEnchant().stream().map(LEnchant::valueOf).collect(Collectors.toList());
    this.attributes = new ConcurrentHashMap<>();
    this.page = page;
    ConfigurationUtils.getAttributeTypes(item.getAttribute().getAttributeType())
        .forEach(
            t -> {
              Attribute attribute = new Attribute(t);
              attribute.load(item.getAttribute());
              this.attributes.put(t, attribute);
            });
  }

  @Override
  public ShopPage getPage() {
    return page;
  }

  @Override
  public ArenaPlayer getPlayer() {
    return this.player;
  }

  @Override
  public BwItemStack getMaterial() {
    return this.material;
  }

  @Override
  public void setMaterial(BwItemStack material) {
    this.material = material;
  }

  @Override
  public int getAmount() {
    return this.amount;
  }

  @Override
  public void setAmount(int amount) {
    this.amount = amount;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public List<String> getLore() {
    return this.lore;
  }

  @Override
  public List<LEnchant> getEnchants() {
    return this.enchants;
  }

  @Override
  public Map<AttributeType, me.abhigya.dbedwars.api.game.view.ViewItem.Attribute> getAttributes() {
    return this.attributes;
  }

  @Override
  public BwItemStack getFormatted() {
    BwItemStack stack = this.material.clone();
    ItemMetaBuilder builder =
        stack
            .getItemMetaBuilder()
            .withDisplayName(StringUtils.translateAlternateColorCodes(this.name))
            .withLore(StringUtils.translateAlternateColorCodes(this.lore));
    stack.setItemMetaBuilder(builder);
    this.enchants.forEach(stack::applyEnchant);
    return stack;
  }

  @Override
  public ActionItem getActionItem(boolean recreate) {
    if (!recreate && this.actionItem != null) return this.actionItem;

    ActionItem item =
        new ActionItem(this.getFormatted().toItemStack()) {
          @Override
          public ItemStack getDisplayIcon() {
            return this.getIcon();
          }
        };
    if (this.attributes.containsKey(AttributeType.PURCHASABLE)) {
      me.abhigya.dbedwars.api.game.view.ViewItem.Attribute purchasable =
          this.attributes.get(AttributeType.PURCHASABLE);
      Set<ItemStack> cost =
          ConfigurationUtils.parseCost((String) purchasable.getKeyEntry().get("cost"));
      Map<String, BwItemStack> items = new HashMap<>();
      boolean b = this.attributes.containsKey(AttributeType.PERMANENT);

      for (Map.Entry<String, Object> entry : purchasable.getKeyEntry().entrySet()) {
        if (entry.getKey().startsWith("item")) {
          String key = entry.getKey().replace("item-", "");
          BwItemStack i = (BwItemStack) entry.getValue();
          i.addNBT("shopKey", this.key + ":" + key);
          if (this.page != null) i.addNBT("page", this.page.getKey());
          if (b) i.addNBT("permanent", true);
          items.put(key, i);
        }
      }

      PlayerPurchaseItemEvent event =
          new PlayerPurchaseItemEvent(this.player, this.player.getArena(), cost, items.values());

      if (this.attributes.containsKey(AttributeType.AUTO_EQUIP)) {
        me.abhigya.dbedwars.api.game.view.ViewItem.Attribute equip =
            this.attributes.get(AttributeType.AUTO_EQUIP);
        Map<String, Integer> index = new HashMap<>();
        for (Map.Entry<String, Object> entry : equip.getKeyEntry().entrySet()) {
          index.put(entry.getKey().replace("item:auto-equip-", ""), (int) entry.getValue());
        }
        item.addAction(
            new ItemAction() {
              @Override
              public ItemActionPriority getPriority() {
                return ItemActionPriority.HIGH;
              }

              @Override
              public void onClick(ItemClickAction itemClickAction) {
                if (event.getCost().stream()
                    .allMatch(i -> BwItemStack.playerHas(itemClickAction.getPlayer(), i))) {
                  event.call();
                  if (event.isCancelled()) return;

                  items.forEach(
                      (s, i) -> {
                        int x = index.getOrDefault(s, -1);
                        if (x > -1) {
                          itemClickAction.getPlayer().getInventory().setItem(x, i.toItemStack());
                        } else {
                          itemClickAction.getPlayer().getInventory().addItem(i.toItemStack());
                        }
                      });
                }
              }
            });
      } else {
        item.addAction(
            new ItemAction() {
              @Override
              public ItemActionPriority getPriority() {
                return ItemActionPriority.HIGH;
              }

              @Override
              public void onClick(ItemClickAction itemClickAction) {
                if (event.getCost().stream()
                    .allMatch(i -> BwItemStack.playerHas(itemClickAction.getPlayer(), i))) {
                  event.call();
                  if (event.isCancelled()) return;

                  items
                      .values()
                      .forEach(
                          i -> itemClickAction.getPlayer().getInventory().addItem(i.toItemStack()));
                }
              }
            });
      }

      if (this.attributes.containsKey(AttributeType.UPGRADEABLE_TIER)) {
        me.abhigya.dbedwars.api.game.view.ViewItem.Attribute upgradeTier =
            this.attributes.get(AttributeType.UPGRADEABLE_TIER);
        String nextTier =
            (String)
                upgradeTier.getKeyEntry().get(AttributeType.UPGRADEABLE_TIER.getConfigKeys()[0]);
        me.abhigya.dbedwars.api.game.view.ViewItem vi = null;
        if (this.player.getShopView().getCommons().containsKey(nextTier)) {
          vi = this.player.getShopView().getCommons().get(nextTier);
        }

        for (Map.Entry<String, ShopPage> entry :
            this.player.getShopView().getShopPages().entrySet()) {
          if (entry.getValue().getItems().containsKey(nextTier)) {
            if (vi != null) {
              vi = vi.clone();
              try {
                vi.override(entry.getValue().getItems().get(nextTier));
              } catch (OverrideException e) {
                e.printStackTrace();
              }
            } else {
              vi = entry.getValue().getItems().get(nextTier);
            }
            break;
          }
        }

        if (vi != null) {
          me.abhigya.dbedwars.api.game.view.ViewItem finalVi = vi;
          ActionItem nextItem = finalVi.getActionItem(false);
          item.addAction(
              new ItemAction() {
                @Override
                public ItemActionPriority getPriority() {
                  return ItemActionPriority.NORMAL;
                }

                @Override
                public void onClick(ItemClickAction itemClickAction) {
                  if (event.getCost().stream()
                      .allMatch(i -> BwItemStack.playerHas(itemClickAction.getPlayer(), i))) {
                    if (event.isCancelled()) return;

                    itemClickAction.getMenu().setItem(itemClickAction.getSlot(), nextItem);
                    ViewItem.this.getPage()
                            .getPattern()[itemClickAction.getSlot() / 9][
                            itemClickAction.getSlot() % 9] =
                        ((ViewItem) finalVi).key;
                    itemClickAction.setUpdate(true);
                    itemClickAction.getMenu().update(itemClickAction.getPlayer());
                  }
                }
              });
        }
      }

      item.addAction(
          new ItemAction() {
            @Override
            public ItemActionPriority getPriority() {
              return ItemActionPriority.LOW;
            }

            @Override
            public void onClick(ItemClickAction itemClickAction) {
              if (event.isCancelled()) return;

              if (event.getCost().stream()
                  .allMatch(i -> BwItemStack.playerHas(itemClickAction.getPlayer(), i))) {
                for (ItemStack i : cost) {
                  BwItemStack.removeItem(itemClickAction.getPlayer(), i);
                }
              } else {
                itemClickAction
                    .getPlayer()
                    .sendMessage(
                        StringUtils.translateAlternateColorCodes(
                            "&cYou don't have the required items."));
              }
            }
          });
    }

    if (this.attributes.containsKey(AttributeType.COMMAND)) {
      me.abhigya.dbedwars.api.game.view.ViewItem.Attribute command =
          this.attributes.get(AttributeType.COMMAND);
      List<String> cmd = (List<String>) command.getKeyEntry().get("command");
      item.addAction(
          new ItemAction() {
            @Override
            public ItemActionPriority getPriority() {
              return ItemActionPriority.NORMAL;
            }

            @Override
            public void onClick(ItemClickAction itemClickAction) {
              cmd.forEach(
                  c -> {
                    if (c.startsWith("[CONSOLE]")) {
                      Bukkit.getServer()
                          .dispatchCommand(
                              Bukkit.getConsoleSender(), c.replace("[CONSOLE]", "").trim());
                    } else if (c.startsWith("[PLAYER]")) {
                      itemClickAction.getPlayer().performCommand(c.replace("[PLAYER]", "").trim());
                    }
                  });
            }
          });
    }

    if (this.attributes.containsKey(AttributeType.CHANGE_PAGE)) {
      me.abhigya.dbedwars.api.game.view.ViewItem.Attribute page =
          this.attributes.get(AttributeType.CHANGE_PAGE);
      String p = (String) page.getKeyEntry().get("page");
      item.addAction(
          new ItemAction() {
            @Override
            public ItemActionPriority getPriority() {
              return ItemActionPriority.NORMAL;
            }

            @Override
            public void onClick(ItemClickAction itemClickAction) {
              Map<String, Object> info = new HashMap<>();
              info.put("player", ViewItem.this.player);
              info.put("page", p);
              ((ShopGui) DBedwars.getInstance().getGuiHandler().getGuis().get("SHOP"))
                  .setUpMenu(itemClickAction.getPlayer(), itemClickAction, info);
              itemClickAction.getMenu().update(itemClickAction.getPlayer());
              itemClickAction.setUpdate(true);
            }
          });
    }

    this.actionItem = item;
    return item;
  }

  @Override
  public void override(Overridable override) throws OverrideException {
    if (!(override instanceof ViewItem)) throw new OverrideException("Wrong override");

    ViewItem item = (ViewItem) override;
    if (item.getPage() != null) this.page = item.getPage();
    if (item.getMaterial() != null) this.material = item.getMaterial();
    if (item.getAmount() != this.getAmount() && item.getAmount() > 1)
      this.amount = item.getAmount();
    if (item.getName() != null) this.name = item.getName();
    if (item.getLore() != null) this.lore = item.getLore();
    if (item.getEnchants() != null) this.enchants = item.getEnchants();

    this.attributes.clear();
    this.attributes = new ConcurrentHashMap<>(item.getAttributes());
    this.actionItem = null;
  }

  public List<String> getAllUpgradeTier() {
    List<String> tiers = new ArrayList<>();
    if (this.attributes.containsKey(AttributeType.UPGRADEABLE_TIER)) {
      me.abhigya.dbedwars.api.game.view.ViewItem viewItem = this;
      do {
        me.abhigya.dbedwars.api.game.view.ViewItem.Attribute upgradeTier =
            viewItem.getAttributes().get(AttributeType.UPGRADEABLE_TIER);
        String nextTier =
            (String)
                upgradeTier.getKeyEntry().get(AttributeType.UPGRADEABLE_TIER.getConfigKeys()[0]);
        if (this.page.getItems().containsKey(nextTier)) {
          tiers.add(nextTier);
          viewItem = this.page.getItems().get(nextTier);
        } else {
          break;
        }
      } while (viewItem.getAttributes().containsKey(AttributeType.UPGRADEABLE_TIER));
    }

    return tiers;
  }

  @Override
  public ViewItem clone() {
    return new ViewItem(this.player, this.page, this.cfgItem, this.key);
  }

  public class Attribute implements me.abhigya.dbedwars.api.game.view.ViewItem.Attribute {

    private ViewItem item;
    private AttributeType type;
    private Map<String, Object> keyEntry;

    public Attribute(AttributeType type) {
      this.item = ViewItem.this;
      this.type = type;
      this.keyEntry = new HashMap<>();
    }

    protected void load(ConfigurableShop.ConfigurableAttribute atr) {
      if (type.getConfigKeys() != null) {
        for (String key : type.getConfigKeys()) {
          for (Field field : atr.getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(LoadableEntry.class)) continue;

            LoadableEntry entry = field.getAnnotation(LoadableEntry.class);
            if (entry.key() != null && entry.key().equals(key)) {
              try {
                field.setAccessible(true);
                Object value = field.get(atr);
                this.keyEntry.put(key, value);
              } catch (IllegalAccessException ignored) {
              }
            }
          }
        }
      }

      if (type == AttributeType.PURCHASABLE) {
        for (Map.Entry<String, ConfigurableShop.ConfigurableAttribute.AttributeItems> atrItem :
            atr.getItemsToGive().entrySet()) {
          if (atrItem.getValue().isInvalid()) continue;
          if (atrItem.getValue().getMaterial() != null) {
            BwItemStack stack =
                ConfigurationUtils.parseItem(
                    this.item.player.getTeam(), atrItem.getValue().getMaterial());
            stack.setAmount(atrItem.getValue().getAmount());
            ItemMetaBuilder builder = stack.getItemMetaBuilder();
            if (atrItem.getValue().getName() != null)
              builder.withDisplayName(
                  StringUtils.translateAlternateColorCodes(atrItem.getValue().getName()));
            if (!atrItem.getValue().getLore().isEmpty())
              builder.withLore(
                  StringUtils.translateAlternateColorCodes(atrItem.getValue().getLore()));
            stack.setItemMetaBuilder(builder);
            if (atrItem.getValue().getEnchant() != null)
              atrItem.getValue().getEnchant().stream()
                  .map(LEnchant::valueOf)
                  .filter(Objects::nonNull)
                  .forEach(stack::applyEnchant);
            this.keyEntry.put("item-" + atrItem.getKey(), stack);
          } else if (atrItem.getValue().getCustomItem() != null) {
            try {
              PluginActionItem item =
                  DBedwars.getInstance()
                      .getCustomItemHandler()
                      .getItem(atrItem.getValue().getCustomItem());
              if (item == null) continue;
              this.keyEntry.put("item-" + atrItem.getKey(), item.toBwItemStack());
            } catch (IllegalArgumentException ignored) {
            }
          }
        }
      } else if (type == AttributeType.AUTO_EQUIP) {
        for (Map.Entry<String, ConfigurableShop.ConfigurableAttribute.AttributeItems> atrItem :
            atr.getItemsToGive().entrySet()) {
          this.keyEntry.put(
              AttributeType.AUTO_EQUIP.getConfigKeys()[0] + "-" + atrItem.getKey(),
              atrItem.getValue().getAutoEquipSlot());
        }
      }
    }

    @Override
    public AttributeType getType() {
      return this.type;
    }

    @Override
    public Map<String, Object> getKeyEntry() {
      return this.keyEntry;
    }

    @Override
    public boolean isValid() {
      Set<String> keys = this.keyEntry.keySet();
      return Arrays.stream(type.getConfigKeys())
          .allMatch(s -> keys.stream().anyMatch(k -> k.contains(s)));
    }

    @Override
    public boolean isInvalid() {
      return !this.isValid();
    }

    @Override
    public void override(Overridable override) throws OverrideException {
      if (!(override instanceof Attribute)) throw new OverrideException("Wrong override");

      Attribute attribute = (Attribute) override;
      this.type = attribute.getType();
      this.keyEntry.clear();
      this.keyEntry = new HashMap<>(attribute.getKeyEntry());
    }

    @Override
    public Attribute clone() {
      try {
        return (Attribute) super.clone();
      } catch (CloneNotSupportedException e) {
        e.printStackTrace();
      }

      return null;
    }
  }
}
