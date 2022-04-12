package org.zibble.dbedwars.api.util;

import org.apache.commons.lang.StringUtils;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.messaging.message.Message;

public class ExceptionHandler {

    public static void throwException(Throwable throwable) throws Throwable {
        ExceptionHandler.throwException(throwable, ExceptionHandler.wrap("An error has occurred!"));
    }

    public static void throwException(Throwable throwable, Message message) throws Throwable {
        Messaging.get().getConsole().sendMessage(message);
        throw throwable;
    }

    public static void handleException(Throwable throwable) {
        ExceptionHandler.handleException(throwable, ExceptionHandler.wrap("An error has occurred!"));
    }

    public static void handleException(Throwable throwable, Message message) {
        message.addLine("");
        message.addLine("");
        message.addLine("<red>Error: " + throwable.getMessage());
        message.addLine("");
        message.addLine("Stacktrace:");
        for (StackTraceElement trace : throwable.getStackTrace()) {
            message.addLine("<red> - " + trace.toString());
        }
        Messaging.get().getConsole().sendMessage(message);
    }

    public static Message wrap(String message) {
        String wrapper = "<red>###" + StringUtils.repeat("#", message.length()) + "###";
        return AdventureMessage.from(new String[]{wrapper, "<red>## " + message + " ##", wrapper});
    }

}
