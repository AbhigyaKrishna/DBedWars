package org.zibble.dbedwars.api.guis.component;

import org.bukkit.entity.Player;
import org.zibble.inventoryframework.menu.Menu;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

@SuppressWarnings({"rawtypes", "unchecked"})
public class PaginatedGuiComponent<K, T extends Menu, R extends PaginatedGuiComponent> {

    protected final Player player;
    protected TreeMap<K, GuiComponent<T, ? extends GuiComponent>> pages;
    protected K currentPage;

    protected PaginatedGuiComponent(Player player) {
        this(player, null);
    }

    protected PaginatedGuiComponent(Player player, Comparator<? super K> comparator) {
        this.player = player;
        this.pages = new TreeMap<>(comparator);
    }

    public GuiComponent<T, ? extends GuiComponent> getPage(K key) {
        return this.pages.get(key);
    }

    public int getPageCount() {
        return this.pages.size();
    }

    public R addPage(K key, GuiComponent<T, ? extends GuiComponent> component) {
        this.pages.put(key, component.addCloseAction((menu, p) -> this.close()));
        return (R) this;
    }

    public R removePage(int page) {
        this.pages.remove(page);
        return (R) this;
    }

    public R clearPages() {
        this.pages.clear();
        return (R) this;
    }

    public R open(K key) {
        this.close();
        this.getPage(key).open(player);
        this.currentPage = key;
        return (R) this;
    }

    public R open() {
        return this.open(this.pages.firstKey());
    }

    public R nextPage() {
        if (this.player != null) {
            Map.Entry<K, GuiComponent<T, ? extends GuiComponent>> entry = this.pages.higherEntry(this.currentPage);
            if (entry != null) {
                this.open(entry.getKey());
            }
        }
        return (R) this;
    }

    public R previousPage() {
        if (this.player != null) {
            Map.Entry<K, GuiComponent<T, ? extends GuiComponent>> entry = this.pages.lowerEntry(this.currentPage);
            if (entry != null) {
                this.open(entry.getKey());
            }
        }
        return (R) this;
    }

    public R page(K key) {
        if (this.player != null) {
            this.open(key);
        }
        return (R) this;
    }

    public R close() {
        if (this.player != null) {
            if (this.currentPage != null)
                this.getPage(this.currentPage).close();
            this.currentPage = this.pages.firstKey();
        }
        return (R) this;
    }

    public R update() {
        if (this.player != null) {
            this.getPage(this.currentPage).update();
        }
        return (R) this;
    }

    public R updateSlot(int slot) {
        if (this.player != null) {
            this.getPage(this.currentPage).updateSlot(slot);
        }
        return (R) this;
    }

    public Player getViewer() {
        return this.player;
    }

}
