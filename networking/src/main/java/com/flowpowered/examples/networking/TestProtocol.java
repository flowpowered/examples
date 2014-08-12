/*
 * This file is part of Flow Networking Example, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2014 Spout LLC <https://spout.org/>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.flowpowered.examples.networking;

import io.netty.buffer.ByteBuf;

import com.flowpowered.networking.Codec;
import com.flowpowered.networking.Codec.CodecRegistration;
import com.flowpowered.networking.exception.IllegalOpcodeException;
import com.flowpowered.networking.exception.UnknownPacketException;
import com.flowpowered.networking.protocol.simple.SimpleProtocol;

import com.flowpowered.examples.networking.message.TestMessage;
import com.flowpowered.examples.networking.message.TestMessageCodec;

public class TestProtocol extends SimpleProtocol {
    public TestProtocol() {
        super("TestProtocol", 1);
        registerMessage(TestMessage.class, TestMessageCodec.class, TestMessageCodec.class, null);
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
    public ByteBuf writeHeader(ByteBuf header, CodecRegistration codec, ByteBuf data) {
        header.writeShort(codec.getOpcode());
        header.writeInt(data.writerIndex());
        return header;
    }
}
