package com.flowpowered.examples.networking.message;

import java.io.IOException;
import com.flowpowered.networking.Codec;
import io.netty.buffer.ByteBuf;

public class AbstractMessageCodec implements Codec<AbstractMessage> {

    public AbstractMessage decode(ByteBuf buffer) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ByteBuf encode(ByteBuf buf, AbstractMessage message) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
