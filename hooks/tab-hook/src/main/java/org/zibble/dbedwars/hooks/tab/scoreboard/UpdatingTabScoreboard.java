package org.zibble.dbedwars.hooks.tab.scoreboard;

import me.neznamy.tab.api.scoreboard.ScoreboardManager;
import org.zibble.dbedwars.api.DBedWarsAPI;
import org.zibble.dbedwars.api.adventure.AdventureUtils;
import org.zibble.dbedwars.api.hooks.scoreboard.UpdatingScoreboard;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.task.CancellableWorkload;
import org.zibble.dbedwars.api.util.Duration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class UpdatingTabScoreboard extends TabScoreboard implements UpdatingScoreboard {

    private int titleCursor;
    private List<Integer> lineCursor;
    private Duration delayDuration;
    private CancellableWorkload task;

    public UpdatingTabScoreboard(UUID playerID, ScoreboardManager manager, Message title, List<Message> lines, Duration delayDuration) {
        super(playerID, manager, title, lines);
        this.titleCursor = 0;
        this.lineCursor = new ArrayList<>();
        for (Message ignored : lines) {
            this.lineCursor.add(0);
        }
        this.delayDuration = delayDuration;

    }

    public void init() {
        super.init();
        this.task = new CancellableWorkload() {
            long updatedLastAt = System.currentTimeMillis();

            @Override
            public void compute() {
                updatedLastAt = System.currentTimeMillis();

                if (!UpdatingTabScoreboard.this.isShown()) return;

                if (++UpdatingTabScoreboard.this.titleCursor >= UpdatingTabScoreboard.this.title.getLines().size()) {
                    UpdatingTabScoreboard.this.titleCursor = 0;
                };
                UpdatingTabScoreboard.this.tabScoreboard.setTitle(AdventureUtils.toVanillaString(
                        title.asComponentWithPAPI(UpdatingTabScoreboard.this.getViewer())[UpdatingTabScoreboard.this.titleCursor]));

                UpdatingTabScoreboard.this.tabScoreboard.getLines().clear();
                int x;
                for (int i = 0; i < UpdatingTabScoreboard.this.getLines().size(); i++){
                    if (UpdatingTabScoreboard.this.lineCursor.get(i) + 1 >= UpdatingTabScoreboard.this.getLines().get(i).getLines().size()) {
                        x = UpdatingTabScoreboard.this.lineCursor.set(i, 0);
                    } else {
                        x = UpdatingTabScoreboard.this.lineCursor.set(i, UpdatingTabScoreboard.this.lineCursor.get(i) + 1);
                    }
                    UpdatingTabScoreboard.this.tabScoreboard.addLine(AdventureUtils.toVanillaString(
                            UpdatingTabScoreboard.this.getLines().get(i).asComponentWithPAPI(UpdatingTabScoreboard.this.getViewer())[x]));
                }
                UpdatingTabScoreboard.this.update();
            }

            @Override
            public boolean shouldExecute(){
                if(isCancelled())
                    return false;

                return System.currentTimeMillis() - updatedLastAt >= delayDuration.toMillis();
            }
        };
    }

    @Override
    public void setTitle(Message title) {
        super.setTitle(title);
        this.titleCursor = 0;
    }

    @Override
    public void addLines(Collection<Message> lines) {
        super.addLines(lines);
        for (int i = 0; i < lines.size(); i++) {
            this.lineCursor.add(0);
        }
    }

    @Override
    public void setLine(int index, Message line) {
        super.setLine(index, line);
        this.lineCursor.set(index, 0);
    }

    @Override
    public void clearLines() {
        super.clearLines();
        this.lineCursor.clear();
    }

    @Override
    public void removeLine(int index) {
        super.removeLine(index);
        this.lineCursor.remove(index);
    }

    @Override
    public void show() {
        this.init();
    }

    @Override
    public void hide() {
        this.cancelUpdate();
        super.hide();
    }

    @Override
    public void startUpdate() {
        DBedWarsAPI.getApi().getThreadHandler().submitAsync(this.task);
    }

    @Override
    public void cancelUpdate() {
        this.task.setCancelled(true);
        this.task = null;
    }

    @Override
    public Duration getDelay() {
        return this.delayDuration;
    }

    @Override
    public void setDelay(Duration delay) {
        this.delayDuration = delay;
    }

}
