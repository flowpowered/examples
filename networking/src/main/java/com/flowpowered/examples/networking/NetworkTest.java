package com.flowpowered.examples.networking;


import java.util.logging.Level;
import java.util.logging.Logger;

import io.netty.channel.ChannelOption;
import com.flowpowered.examples.networking.message.TestMessage;

public class NetworkTest {
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
