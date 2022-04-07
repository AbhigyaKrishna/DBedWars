package org.zibble.dbedwars.game.arena.traps;

import org.zibble.dbedwars.api.game.Team;
import org.zibble.dbedwars.api.game.trap.Trap;

import java.util.ArrayList;
import java.util.Collection;

public class TrapQueue extends ArrayList<Trap> {

    private final Team team;

    public TrapQueue(Team team) {
        super(team.getArena().getSettings().getTrapQueueSize());
        this.team = team;
    }

    @Override
    public boolean add(Trap trap) {
        if (this.team.getArena().getSettings().isTrapQueueEnabled() && this.size() >= this.team.getArena().getSettings().getTrapQueueSize()) {
            throw new IllegalStateException("Trap queue is full!");
        }

        return super.add(trap);
    }

    @Override
    public Trap set(int index, Trap element) {
        return null;
    }

    @Override
    public boolean addAll(Collection<? extends Trap> c) {
        if (this.team.getArena().getSettings().isTrapQueueEnabled() && this.size() + c.size() > this.team.getArena().getSettings().getTrapQueueSize()) {
            throw new IllegalStateException("Trap queue is full!");
        }
        return super.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Trap> c) {
        return false;
    }

}
