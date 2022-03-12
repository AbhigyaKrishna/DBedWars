package org.zibble.dbedwars.guis.component;

import org.bukkit.entity.Player;
import org.zibble.inventoryframework.menu.Menu;

import java.util.ArrayList;

@SuppressWarnings({"rawtypes", "unchecked"})
public class PaginatedGuiComponent<T extends Menu, R extends PaginatedGuiComponent> {

    protected ArrayList<GuiComponent<T, ? extends GuiComponent>> pages;
    protected Player player;
    protected int currentPage = 0;

    protected PaginatedGuiComponent() {
        this(4);
    }

    protected PaginatedGuiComponent(int capacity) {
        pages = new ArrayList<>(capacity);
    }

    public GuiComponent<T, ? extends GuiComponent> getPage(int page) {
        return this.pages.get(page);
    }

    public int getPageCount() {
        return this.pages.size();
    }

    public R setPage(int page, GuiComponent<T, ? extends GuiComponent> component) {
        this.pages.set(page, component.addCloseAction((menu, p) -> this.close()));
        return (R) this;
    }

    public R addPage(GuiComponent<T, ? extends GuiComponent> component) {
        pages.add(component.addCloseAction((menu, p) -> this.close()));
        return (R) this;
    }

    public R removePage(int page) {
        pages.remove(page);
        return (R) this;
    }

    public R clearPages() {
        pages.clear();
        return (R) this;
    }

    public R open(Player player, int page) {
        this.close();
        this.player = player;
        getPage(page).open(player);
        this.currentPage = page;
        return (R) this;
    }

    public R open(Player player) {
        return this.open(player, this.currentPage);
    }

    public R nextPage() {
        if (this.player != null) {
            if (this.currentPage < this.getPageCount() - 1) {
                this.getPage(this.currentPage).close();
                this.getPage(++this.currentPage).open(this.player);
            }
        }
        return (R) this;
    }

    public R previousPage() {
        if (this.player != null) {
            if (this.currentPage > 0) {
                this.getPage(this.currentPage).close();
                this.getPage(--this.currentPage).open(this.player);
            }
        }
        return (R) this;
    }

    public R page(int page) {
        if (this.player != null) {
            this.getPage(this.currentPage).close();
            this.getPage(page).open(this.player);
        }
        return (R) this;
    }

    public R close() {
        if (this.player != null) {
            this.getPage(this.currentPage).close();
            this.currentPage = 0;
            this.player = null;
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
        return player;
    }

}
