package com.swayam.demo.stomp.client.broker;

import java.io.IOException;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnOpen;
import javax.websocket.Session;

@ClientEndpoint
public class SimpleClient {

    @OnOpen
    public void onOpen(Session session) {
	try {
	    String name = "Duke";
	    System.out.println("Sending message to endpoint: " + name);
	    session.getBasicRemote().sendText(name);
	} catch (IOException ex) {
	    ex.printStackTrace();
	}
    }

}