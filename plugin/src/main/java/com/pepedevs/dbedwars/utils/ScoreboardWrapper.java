package com.pepedevs.dbedwars.utils;

import com.pepedevs.radium.placeholders.PlaceholderUtil;
import com.pepedevs.radium.scoreboard.Scoreboard;
import com.pepedevs.radium.task.Workload;
import com.pepedevs.radium.utils.Initializable;
import com.pepedevs.radium.utils.Tickable;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.messaging.PlaceholderEntry;
import com.pepedevs.dbedwars.configuration.Lang;
import com.pepedevs.dbedwars.task.UpdateTask;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ScoreboardWrapper extends Scoreboard implements Initializable, Tickable {

    public static ScoreboardWrapper from(String title, List<String> content, PlaceholderEntry... entries) {
        return from(title, content, Arrays.asList(entries));
    }

    public static ScoreboardWrapper from(String title, List<String> content, List<PlaceholderEntry> entries) {
        return new ScoreboardWrapper(title, content, entries);
    }

    private static final DBedwars INSTANCE = DBedwars.getInstance();

    private boolean initialized;

    private short updateInterval = 5;
    private long lastUpdate;

    private String rawTitle;
    private List<String> rawContent;
    private List<PlaceholderEntry> placeholderEntries;

    public ScoreboardWrapper(String title, List<String> content, List<PlaceholderEntry> entries) {
        super(Lang.getTranslator().translate(title));
        this.placeholderEntries = Collections.synchronizedList(entries);
        this.rawTitle = title;
        this.rawContent = Collections.synchronizedList(content);
    }

    public void init() {
        this.addLines(Lang.getTranslator().translate(this.rawContent));
        this.setPlaceholderFunction(new PlaceholderFunction() {
            @Override
            public Component apply(Player player, Component component) {
                String s = Lang.getTranslator().untranslate(component);
                s = PlaceholderUtil.placeholder(player, s);
                for (PlaceholderEntry placeholderEntry : placeholderEntries) {
                    s = s.replace(placeholderEntry.getPlaceholder(), placeholderEntry.getReplacement().get());
                }
                return Lang.getTranslator().translate(s);
            }
        });
        UpdateTask.addTickable(this);
        initialized = true;
    }

    @Override
    public void tick() {
        if (!this.isInitialized())
            return;

        if (System.currentTimeMillis() - lastUpdate > updateInterval * 1000) {
            lastUpdate = System.currentTimeMillis();

            INSTANCE.getThreadHandler().submitAsync(new Workload() {
                @Override
                public void compute() {
                    ScoreboardWrapper.this.updateAll(true);
                }
            });
        }
    }

    public void destroy() {
        UpdateTask.removeTickable(this);
        this.hideAll();
    }

    @Override
    public boolean isInitialized() {
        return this.initialized;
    }

    public void setUpdateInterval(short updateInterval) {
        this.updateInterval = updateInterval;
    }

    public short getUpdateInterval() {
        return this.updateInterval;
    }

}
