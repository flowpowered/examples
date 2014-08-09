package com.flowpowered.examples.networking.message;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import com.flowpowered.networking.Codec;
import com.flowpowered.networking.MessageHandler;

import com.flowpowered.examples.networking.DynamicSession;

public class TestMessageCodec implements Codec<TestMessage>, MessageHandler<DynamicSession, TestMessage> {

	@Override
	public TestMessage decode(ByteBuf buffer) throws IOException {
		return new TestMessage(buffer.readInt());
	}

	@Override
	public ByteBuf encode(ByteBuf buf, TestMessage message) throws IOException {
		buf.writeInt(message.getNumber());
        return buf;
	}

    public void handle(DynamicSession session, TestMessage message) {
        System.out.println("Received TestMessage with number: " + message.getNumber());
    }
}
