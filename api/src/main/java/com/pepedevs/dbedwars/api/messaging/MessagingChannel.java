package com.pepedevs.dbedwars.api.messaging;

import java.util.List;
import java.util.Map;

public interface MessagingChannel {

    List<MessagingMember> getMembers();

    boolean addChannelMember(MessagingMember member);

    boolean removeChannelMember(MessagingMember member);

    void sendMessage(Message message);

    Map<Message, Long> getMessageHistory();
}
