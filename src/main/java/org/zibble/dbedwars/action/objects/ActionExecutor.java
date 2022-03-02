package org.zibble.dbedwars.action.objects;

import org.zibble.dbedwars.DBedwars;

public class ActionExecutor {

    private static final DBedwars PLUGIN = DBedwars.getInstance();

    public static void execute(final ProcessedActionHolder action) {
        if (!action.shouldExecute()) return;
        PLUGIN.getThreadHandler().runTaskLater(new Runnable() {
            @Override
            public void run() {
                int runTimes = action.getRepeats() + 1;
                int i = 0;
                while (i++ < runTimes) {
                    action.getAction().execute();
                }
            }
        }, action.getDelay());
    }

}
