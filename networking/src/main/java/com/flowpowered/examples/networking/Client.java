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

import io.netty.channel.Channel;

import java.net.InetSocketAddress;

import com.flowpowered.networking.NetworkClient;
import com.flowpowered.networking.session.Session;

public class Client extends NetworkClient {
    public DynamicSession session;
    public Runnable run;

    public Client() {
        connect(new InetSocketAddress("127.0.0.1", 65535));
    }

    @Override
    public Session newSession(Channel c) {
        System.out.println("Client session created!");
        session = new DynamicSession(c, new TestProtocol()) {
            @Override
            public void onReady() {
                if (run != null) run.run();
            }
        };
        return session;
    }

    @Override
    public void sessionInactivated(Session session) {
        System.out.println("Client session inactivated!");
    }
}
