package com.remotedesk.networking.server;

import com.remotedesk.networking.utility.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.*;

public class RequestProcesser extends Thread {
	private Socket socket;
	private ServerSocket serverSocket;

	public RequestProcesser(ServerSocket serverSocket, Socket socket) {
		this.socket = socket;
		this.serverSocket = serverSocket;
		start();
	}

	public void run() {
		OutputStream outputStream;
		InputStream inputStream;
		try {
			byte b[] = new byte[4];
			outputStream = socket.getOutputStream();
			inputStream = socket.getInputStream();
			inputStream.read(b);
			int ch;
			ch = Convert.byteToInt(b);
			if (ch == 30)
				quit();
			if (ch == 40)
				sendSize(outputStream);
			if (ch == 50)
				sendImage(outputStream);
			if (ch == 60)
				moveMouse(inputStream);
			if (ch == 70)
				mouseClicked(inputStream);
			if (ch == 80)
				mousePressed(inputStream);
			if (ch == 90)
				mouseReleased(inputStream);
			if (ch == 100)
				mouseDragged(inputStream);
			if (ch == 110)
				keyPressed(inputStream);
			if (ch == 120)
				keyReleased(inputStream);
			socket.close();
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}

	public void quit() {
		try {
			serverSocket.close();
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return;
	}

	public void sendSize(OutputStream outputStream) {
		try {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			int w = (int) screenSize.getWidth();
			int h = (int) screenSize.getHeight();
			outputStream.write(Convert.intToByte(h));
			outputStream.flush();
			outputStream.write(Convert.intToByte(w));
			outputStream.flush();
		} catch (Exception e) {
			//e.printStackTrace();
			return;
		}
	}

	public void sendImage(OutputStream outputStream) {
		try {
			Robot robot = new Robot();
			Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			BufferedImage screenFullImage;
			screenFullImage = robot.createScreenCapture(screenRect);
			ImageIO.write(screenFullImage, "png", outputStream);
			outputStream.flush();
		} catch (Exception e) {
			//e.printStackTrace();
			return;
		}
	}

	public void moveMouse(InputStream inputStream) {
		try {
			byte[] b = new byte[4];
			inputStream.read(b);
			int x = Convert.byteToInt(b);
			inputStream.read(b);
			int y = Convert.byteToInt(b);
			new Robot().mouseMove(x, y);
		} catch (Exception e) {
			//e.printStackTrace();
			return;
		}
	}

	public void mouseClicked(InputStream inputStream) {
		try {
			byte[] b = new byte[4];
			inputStream.read(b);
			int button = Convert.byteToInt(b);
			new Robot().mousePress(InputEvent.getMaskForButton(button));
			new Robot().mouseRelease(InputEvent.getMaskForButton(button));
		} catch (Exception e) {
			//e.printStackTrace();
			return;
		}
	}

	public void mousePressed(InputStream inputStream) {
		try {
			byte[] b = new byte[4];
			inputStream.read(b);
			int button = Convert.byteToInt(b);
			new Robot().mousePress(InputEvent.getMaskForButton(button));
		} catch (Exception e) {
			//e.printStackTrace();
			return;
		}
	}

	public void mouseReleased(InputStream inputStream) {
		try {
			byte[] b = new byte[4];
			inputStream.read(b);
			int button = Convert.byteToInt(b);
			new Robot().mouseRelease(InputEvent.getMaskForButton(button));
		} catch (Exception e) {
			//e.printStackTrace();
			return;
		}
	}

	public void mouseDragged(InputStream inputStream) {
		try {
			byte[] b = new byte[4];
			inputStream.read(b);
			int x = Convert.byteToInt(b);
			inputStream.read(b);
			int y = Convert.byteToInt(b);
			new Robot().mouseMove(x, y);
		} catch (Exception e) {
			//e.printStackTrace();
			return;
		}
	}

	public void keyPressed(InputStream inputStream) {
		try {
			byte[] b = new byte[4];
			inputStream.read(b);
			int key = Convert.byteToInt(b);
			new Robot().keyPress(key);
		} catch (Exception e) {
			//e.printStackTrace();
			return;
		}
	}

	public void keyReleased(InputStream inputStream) {
		try {
			byte[] b = new byte[4];
			inputStream.read(b);
			int key = Convert.byteToInt(b);
			new Robot().setAutoDelay(50);
			new Robot().keyRelease(key);
		} catch (Exception e) {
			//e.printStackTrace();
			return;
		}
	}
}