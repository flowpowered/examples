package com.flowpowered.examples.networking.message;

import com.flowpowered.networking.MessageHandler;
import com.flowpowered.networking.session.Session;

public class AbstractMessageHandler implements MessageHandler<Session, AbstractMessage> {

    public void handle(Session session, AbstractMessage message) {
    }

}
