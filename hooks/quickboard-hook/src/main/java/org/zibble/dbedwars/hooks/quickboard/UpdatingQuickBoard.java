package org.zibble.dbedwars.hooks.quickboard;

import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.DBedWarsAPI;
import org.zibble.dbedwars.api.adventure.AdventureUtils;
import org.zibble.dbedwars.api.hooks.scoreboard.UpdatingScoreboard;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.task.CancellableWorkload;
import org.zibble.dbedwars.api.util.Duration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UpdatingQuickBoard extends QuickScoreboard implements UpdatingScoreboard {

    private int titleCursor;
    private List<Integer> lineCursor;
    private Duration delay;
    private CancellableWorkload task;

    public UpdatingQuickBoard(Message title, List<Message> lines, Player player, Duration delay) {
        super(title, lines, player);
        this.titleCursor = 0;
        this.lineCursor = new ArrayList<>();
        for (Message ignored : lines) {
            this.lineCursor.add(0);
        }
        this.delay = delay;
    }

    @Override
    public void init() {
        super.init();
        this.task = new CancellableWorkload() {
            long updatedLastAt = System.currentTimeMillis();

            @Override
            public void compute() {
                this.updatedLastAt = System.currentTimeMillis();
                if (this.isCancelled()) return;

                if (!UpdatingQuickBoard.this.isShown()) return;

                if (++UpdatingQuickBoard.this.titleCursor >= UpdatingQuickBoard.this.title.getLines().size()) {
                    UpdatingQuickBoard.this.titleCursor = 0;
                }
                UpdatingQuickBoard.this.board.getTitle().clear();
                UpdatingQuickBoard.this.board.getTitle().add(AdventureUtils.toVanillaString(
                        title.asComponentWithPAPI(UpdatingQuickBoard.this.getViewer())[UpdatingQuickBoard.this.titleCursor]));

                List<String> lines = new ArrayList<>();
                int x;
                for (int i = 0; i < UpdatingQuickBoard.this.getLines().size(); i++) {
                    if (UpdatingQuickBoard.this.lineCursor.get(i) + 1 >= UpdatingQuickBoard.this.getLines().get(i).getLines().size()) {
                        x = UpdatingQuickBoard.this.lineCursor.set(i, 0);
                    } else {
                        x = UpdatingQuickBoard.this.lineCursor.set(i, UpdatingQuickBoard.this.lineCursor.get(i) + 1);
                    }
                    lines.add(AdventureUtils.toVanillaString(
                            UpdatingQuickBoard.this.getLines().get(i).asComponentWithPAPI(UpdatingQuickBoard.this.getViewer())[x]));
                }
                UpdatingQuickBoard.this.board.getList().clear();
                UpdatingQuickBoard.this.board.getList().addAll(lines);
                UpdatingQuickBoard.this.update();
            }

            @Override
            public boolean shouldExecute() {
                if (isCancelled())
                    return false;

                return System.currentTimeMillis() - this.updatedLastAt >= UpdatingQuickBoard.this.delay.toMillis();
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
        for (Message ignored : lines) {
            this.lineCursor.add(0);
        }
    }

    @Override
    public void setLine(int index, Message line) {
        super.setLine(index, line);
        this.lineCursor.set(index, 0);
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
        this.task.setCancelled(false);
        DBedWarsAPI.getApi().getThreadHandler().submitAsync(this.task);
    }

    @Override
    public void cancelUpdate() {
        this.task.setCancelled(true);
    }

    @Override
    public Duration getDelay() {
        return this.delay;
    }

    @Override
    public void setDelay(Duration delay) {
        this.delay = delay;
    }

}
