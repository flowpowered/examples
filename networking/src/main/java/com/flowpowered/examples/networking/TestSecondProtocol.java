package com.flowpowered.examples.networking;

import io.netty.buffer.ByteBuf;

import com.flowpowered.networking.Codec;
import com.flowpowered.networking.exception.IllegalOpcodeException;
import com.flowpowered.networking.exception.UnknownPacketException;
import com.flowpowered.networking.protocol.simple.SimpleProtocol;

import com.flowpowered.examples.networking.message.AbstractMessageCodec;
import com.flowpowered.examples.networking.message.AbstractMessageHandler;
import com.flowpowered.examples.networking.message.NotAbstractMessage;


public class TestSecondProtocol extends SimpleProtocol {

	public TestSecondProtocol() {
		super("TestProtocol2", 1);
		registerMessage(NotAbstractMessage.class, AbstractMessageCodec.class, AbstractMessageHandler.class, null);
	}

	@Override
	public Codec<?> readHeader(ByteBuf buf) throws UnknownPacketException {
		int id = buf.readUnsignedShort();
		int length = buf.readInt();
        try {
            return getCodecLookupService().find(id);
        } catch (IllegalOpcodeException e) {
            throw new UnknownPacketException("Packet not found!", id, length);
        }
	}

	@Override
	public ByteBuf writeHeader(ByteBuf header, Codec.CodecRegistration codec, ByteBuf data) {
		header.writeShort(codec.getOpcode());
		header.writeInt(data.writerIndex());
        return header;
	}
}
