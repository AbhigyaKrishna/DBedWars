package org.zibble.dbedwars.api.objects.hologram;

import org.zibble.dbedwars.api.hooks.hologram.Hologram;
import org.zibble.dbedwars.api.objects.serializable.Duration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.BiFunction;

public class AnimatedHologramModel extends HologramModel {

    private HologramRotateTask.TaskEndAction endAction;
    private BiFunction<Hologram, AnimatedHologramModel, HologramRotateTask> taskFunction;
    private HologramRotateTask task;

    public AnimatedHologramModel(Collection<ModelLine> lines, Duration updateInterval, HologramRotateTask.TaskEndAction endAction, BiFunction<Hologram, AnimatedHologramModel, HologramRotateTask> taskFunction) {
        super(lines, updateInterval);
        this.endAction = endAction;
        this.taskFunction = taskFunction;
    }

    public HologramRotateTask.TaskEndAction getEndAction() {
        return endAction;
    }

    public void setEndAction(HologramRotateTask.TaskEndAction endAction) {
        this.endAction = endAction;
    }

    public BiFunction<Hologram, AnimatedHologramModel, HologramRotateTask> getTaskFunction() {
        return taskFunction;
    }

    public void setTaskFunction(BiFunction<Hologram, AnimatedHologramModel, HologramRotateTask> taskFunction) {
        this.taskFunction = taskFunction;
    }

    public HologramRotateTask startTask() {
        this.task = this.taskFunction.apply(this.hologram, this);
        this.task.start();
        return this.task;
    }

    public void stopTask() {
        this.task.cancel();
        this.task = null;
    }

    public HologramRotateTask getTask() {
        return task;
    }

    public boolean hasStarted() {
        return this.task != null;
    }

    @Override
    public AnimatedHologramModel clone() {
        return new AnimatedHologramModel(new ArrayList<>(this.lines), this.updateInterval, this.endAction, this.taskFunction);
    }

}
