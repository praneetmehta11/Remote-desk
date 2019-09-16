package com.remotedesk.networking.server;

import java.net.*;
import java.util.*;

public class Server {
	private int port;
	private ServerSocket serverSocket;

	public Server(ServerSocket socket, int port) {
		this.port = port;
		try {
			this.serverSocket = socket;
			startListening();
		} catch (Exception ee) {
			//ee.printStackTrace();
			return;
		}
	}

	public void createServer() {
		try {
			Random random = new Random();
			int ran = random.nextInt(60000) + 55300;
			serverSocket = new ServerSocket(ran);
			this.port = ran;
		} catch (Exception e) {
			createServer();
		}
	}

	public int getPortNumber() {
		return this.port;
	}

	public void startListening() {
		Socket socket;
		try {
			while (true) {
				socket = serverSocket.accept();
				new RequestProcesser(serverSocket, socket);
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
}