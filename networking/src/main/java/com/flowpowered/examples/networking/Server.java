package com.flowpowered.examples.networking;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;

import com.flowpowered.networking.NetworkServer;
import com.flowpowered.networking.protocol.AbstractProtocol;
import com.flowpowered.networking.protocol.ProtocolRegistry;
import com.flowpowered.networking.session.BasicSession;
import com.flowpowered.networking.session.Session;

public class Server extends NetworkServer {
    private ProtocolRegistry<AbstractProtocol> pr = new ProtocolRegistry<AbstractProtocol>();
    BasicSession session;

	public Server() {
        bindAndRegister(new InetSocketAddress(65535), new TestProtocol());
	}

    private void bindAndRegister(InetSocketAddress a, AbstractProtocol p) {
        bind(a);
        pr.registerProtocol(a.getPort(), p);
    }

	@Override
	public Session newSession(Channel c) {
		System.out.println("Server session created!");
		return session = new BasicSession(c, pr.getProtocol(c.localAddress()));
	}

	@Override
	public void sessionInactivated(Session session) {
		System.out.println("Server session inactivated!");
	}
}
