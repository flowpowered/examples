package com.flowpowered.examples.networking.message.delegated;

import java.io.IOException;
import com.flowpowered.networking.Codec;
import com.flowpowered.networking.Message;
import io.netty.buffer.ByteBuf;

public class DelegatedMessageCodec implements Codec<Message> {

    public Message decode(ByteBuf buffer) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ByteBuf encode(ByteBuf buf, Message message) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
