package org.zibble.dbedwars.script.action.impl;

import org.zibble.dbedwars.api.objects.serializable.TitleST;
import org.zibble.dbedwars.api.script.action.Action;
import org.zibble.dbedwars.messaging.AbstractMessaging;

public class TitleAction implements Action {

    private final TitleST title;
    private final AbstractMessaging abstractMessaging;

    public TitleAction(TitleST title, AbstractMessaging abstractMessaging) {
        this.title = title;
        this.abstractMessaging = abstractMessaging;
    }

    @Override
    public void execute() {
        this.title.send(this.abstractMessaging);
    }

    public AbstractMessaging getMessaging() {
        return this.abstractMessaging;
    }

    public TitleST getTitle() {
        return title;
    }

}
