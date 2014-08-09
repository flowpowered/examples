package com.flowpowered.examples.networking;

import com.flowpowered.networking.protocol.AbstractProtocol;
import com.flowpowered.networking.session.BasicSession;

import io.netty.channel.Channel;

public class DynamicSession extends BasicSession {

    public DynamicSession(Channel channel, AbstractProtocol bootstrapProtocol) {
        super(channel, bootstrapProtocol);
    }

    @Override
    public void setProtocol(AbstractProtocol protocol) {
        super.setProtocol(protocol);
    }
}
