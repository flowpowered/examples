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
