package com.pepedevs.dbedwars.api.messaging;

import java.util.List;
import java.util.Map;

public interface MessagingChannel {

    List<ChannelMember> getMembers();

    boolean addChannelMember(ChannelMember member);

    boolean removeChannelMember(ChannelMember member);

    void sendMessage(Message message);

    Map<Message, Long> getMessageHistory();

}
