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

import java.util.logging.Level;
import java.util.logging.Logger;

import io.netty.channel.ChannelOption;

import com.flowpowered.examples.networking.message.TestMessage;

public class NetworkExample {
    public static void main(String[] args) throws InterruptedException {
        final Server server = new Server();
        final Client client = new Client();
        client.run = new Runnable() {
            public void run() {
                server.session.getChannel().config().setOption(ChannelOption.AUTO_READ, false);
                client.session.send(new TestMessage(42));
                client.session.send(new TestMessage(21));
                server.session.getChannel().read();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                }
                System.out.println("Read");
                client.session.send(new TestMessage(22));
                client.session.send(new TestMessage(100040));
                client.session.setProtocol(new TestSecondProtocol());
                client.session.send(new TestMessage(100));
                System.out.println("All messages sent.");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                }
                client.shutdown();
                server.shutdown();
            }
        };
        System.out.println("Client and server created");
    }
}
