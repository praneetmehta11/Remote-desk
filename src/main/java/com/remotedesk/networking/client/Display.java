package com.remotedesk.networking.client;

import com.remotedesk.networking.utility.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Graphics2D;
import java.io.*;
import java.awt.image.*;


public class Display extends Canvas implements MouseMotionListener, MouseListener, KeyListener {
	private BufferedImage currentFrame;
	private String server;
	private int port;
	private int height, remoteHeight;
	private int width, remoteWidth;

	public Display(String server, int port, Frame frame) {
		this.server = server;
		this.port = port;
		addMouseMotionListener(this);
		addMouseListener(this);
		addKeyListener(this);
		setFocusTraversalKeysEnabled(false);
		repaint();
	}

	public void setCanvasSize(int width, int height) {
		this.width = width;
		this.height = height;
		this.setSize(width, height);
	}

	public void setImageSize(int RemoteWidth, int RemoteHeight) {
		this.remoteWidth = RemoteWidth;
		this.remoteHeight = RemoteHeight;
	}

	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}

	public void paint(Graphics g) {
		super.paint(g);
		if (currentFrame != null) {
			Graphics2D g2d = (Graphics2D) g.create();
			int x = (getWidth() - currentFrame.getWidth(null)) / 2;
			int y = (getHeight() - currentFrame.getHeight(null)) / 2;
			g2d.drawImage(currentFrame, x, y, this);
		}
	}

	public void drawAgain(BufferedImage img) {
		BufferedImage onCanvas = new BufferedImage(width, height, img.getType());
		Graphics2D g2d = onCanvas.createGraphics();
		g2d.drawImage(img, 0, 0, width, height, null);
		g2d.dispose();
		currentFrame = onCanvas;
		repaint();
	}

	public void mouseClicked(MouseEvent ev) {
		try {
			Socket socket;
			OutputStream outputStream;
			byte[] b = new byte[4];
			b = Convert.intToByte(70);
			socket = new Socket(server, port);
			outputStream = socket.getOutputStream();
			outputStream.write(b);
			outputStream.flush();
			b = Convert.intToByte(ev.getButton());
			outputStream.write(b);
			outputStream.flush();
			socket.close();
		} catch (Exception ee) {
			//ee.printStackTrace();
		}
	}

	public void mousePressed(MouseEvent ev) {
		try {
			Socket socket;
			OutputStream outputStream;
			byte[] b = new byte[4];
			b = Convert.intToByte(80);
			socket = new Socket(server, port);
			outputStream = socket.getOutputStream();
			outputStream.write(b);
			outputStream.flush();
			b = Convert.intToByte(ev.getButton());
			outputStream.write(b);
			outputStream.flush();
			socket.close();
		} catch (Exception ee) {
			//ee.printStackTrace();
		}
	}

	public void mouseReleased(MouseEvent ev) {
		try {
			Socket socket;
			OutputStream outputStream;
			byte[] b = new byte[4];
			b = Convert.intToByte(90);
			socket = new Socket(server, port);
			outputStream = socket.getOutputStream();
			outputStream.write(b);
			outputStream.flush();
			b = Convert.intToByte(ev.getButton());
			outputStream.write(b);
			outputStream.flush();
			socket.close();
		} catch (Exception ee) {
			//ee.printStackTrace();
		}
	}

	public void mouseEntered(MouseEvent ev) {
	}

	public void mouseExited(MouseEvent ev) {
	}

	public void mouseDragged(MouseEvent ev) {
		try {
			Socket socket;
			OutputStream outputStream;
			byte[] b = new byte[4];
			b = Convert.intToByte(60);
			socket = new Socket(server, port);
			outputStream = socket.getOutputStream();
			outputStream.write(b);
			outputStream.flush();
			b = Convert.intToByte((int) (ev.getX() * ((double) remoteWidth / (double) width)));
			outputStream.write(b);
			outputStream.flush();
			b = Convert.intToByte((int) ((double) ev.getY() * ((double) remoteHeight / (double) height)));
			outputStream.write(b);
			outputStream.flush();
			socket.close();
		} catch (Exception ee) {
			//ee.printStackTrace();
		}
	}

	public void mouseMoved(MouseEvent ev) {
		try {
			Socket socket;
			OutputStream outputStream;
			byte[] b = new byte[4];
			b = Convert.intToByte(60);
			socket = new Socket(server, port);
			outputStream = socket.getOutputStream();
			outputStream.write(b);
			outputStream.flush();
			b = Convert.intToByte((int) (ev.getX() * ((double) remoteWidth / (double) width)));
			outputStream.write(b);
			outputStream.flush();
			b = Convert.intToByte((int) ((double) ev.getY() * ((double) remoteHeight / (double) height)));
			outputStream.write(b);
			outputStream.flush();
			socket.close();
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}

	public void keyTyped(KeyEvent ev) {
	}

	public void keyPressed(KeyEvent ev) {
		try {
			Socket socket;
			OutputStream outputStream;
			byte[] b = new byte[4];
			b = Convert.intToByte(110);
			socket = new Socket(server, port);
			outputStream = socket.getOutputStream();
			outputStream.write(b);
			outputStream.flush();
			b = Convert.intToByte(ev.getKeyCode());
			outputStream.write(b);
			outputStream.flush();
			socket.close();
		} catch (Exception ee) {
			//ee.printStackTrace();
		}
	}

	public void keyReleased(KeyEvent ev) {
		try {
			Socket socket;
			OutputStream outputStream;
			byte[] b = new byte[4];
			b = Convert.intToByte(120);
			socket = new Socket(server, port);
			outputStream = socket.getOutputStream();
			outputStream.write(b);
			outputStream.flush();
			b = Convert.intToByte(ev.getKeyCode());
			outputStream.write(b);
			outputStream.flush();
			socket.close();
		} catch (Exception ee) {
			//ee.printStackTrace();
		}
	}
}