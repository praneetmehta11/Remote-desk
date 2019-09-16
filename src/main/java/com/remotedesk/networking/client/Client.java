package com.remotedesk.networking.client;

import com.remotedesk.networking.utility.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.*;
import java.util.Timer;
import java.util.TimerTask;

public class Client extends Frame {
    private Display display;
    private BufferedImage screenFullImage;
    private int quit;

    public Client(String server, int port, int password) {
        quit = 0;
        Socket socket;
        InputStream inputStream;
        OutputStream outputStream;
        byte b[] = new byte[4];
        try {
            display = new Display(server, port, this);
            try {
                socket = new Socket(server, port);
            } catch (Exception e) {
                setQuit(4);
                return;
            }
            outputStream = socket.getOutputStream();
            outputStream.write(Convert.intToByte(password));
            outputStream.flush();
            inputStream = socket.getInputStream();
            inputStream.read(b);
            int correct = Convert.byteToInt(b);
            if (correct == 0) {
                setQuit(1);
                socket.close();
                return;
            }
            socket.close();
            new Robot().delay(500);
            socket = new Socket(server, port);
            outputStream = socket.getOutputStream();
            outputStream.write(Convert.intToByte(40));
            outputStream.flush();
            inputStream = socket.getInputStream();
            inputStream.read(b);
            int RemoteHeight = Convert.byteToInt(b);
            inputStream.read(b);
            int RemoteWidth = Convert.byteToInt(b);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int width = (int) screenSize.getWidth();
            int height = (int) screenSize.getHeight() - 50 - 40;
            double x = (double) RemoteWidth / (double) RemoteHeight * ((double) height / (double) width);
            width *= x;
            this.setSize(width + 10, height + 45);
            display.setCanvasSize(width, height);
            socket.close();
            display.setImageSize(RemoteWidth, RemoteHeight);
            this.setLayout(new FlowLayout());
            this.add(display);
            this.setLocation(0, 0);
            this.setVisible(true);
            new Reminder(server, port, this);
            this.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent ev) {
                    try {
                        Socket socket;
                        OutputStream outputStream;
                        byte b[] = new byte[4];
                        b = Convert.intToByte(30);
                        socket = new Socket(server, port);
                        outputStream = socket.getOutputStream();
                        outputStream.write(b);
                        outputStream.flush();
                        socket.close();
                        return;
                    } catch (Exception e) {
                        return;
                    } finally {
                        setVisible(false);
                        setQuit(3);
                    }
                }
            });
        } catch (Exception e) {
            setQuit(2);
            setVisible(false);
            return;
        }
    }

    class Reminder {
        Timer timer;

        public Reminder(String server, int port, Client frame) {
            timer = new Timer();
            timer.schedule(new RemindTask(server, port, frame, timer), 0, 500);
        }

        class RemindTask extends TimerTask {
            private String server;
            private int port;
            private Client mainFrame;
            private Timer timer;

            RemindTask(String server, int port, Client frame, Timer timer) {
                this.server = server;
                this.port = port;
                this.mainFrame = frame;
                this.timer = timer;
            }

            public void run() {
                try {
                    Socket socket;
                    InputStream inputStream;
                    OutputStream outputStream;
                    byte b[] = new byte[4];
                    b = Convert.intToByte(50);
                    socket = new Socket(server, port);
                    outputStream = socket.getOutputStream();
                    outputStream.write(b);
                    outputStream.flush();
                    inputStream = socket.getInputStream();
                    screenFullImage = ImageIO.read(inputStream);
                    display.drawAgain(screenFullImage);
                    socket.close();
                } catch (Exception ee) {
                    mainFrame.setVisible(false);
                    timer.cancel();
                    timer.purge();
                    mainFrame.setQuit(2);
                }
            }
        }
    }

    public void setQuit(int i) {
        this.quit = i;
    }

    public int closed() {
        return this.quit;
    }
}