package com.remotedesk.GUI;

import com.remotedesk.networking.server.*;
import com.remotedesk.networking.client.*;
import com.remotedesk.networking.utility.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.net.*;
import java.util.*;
import java.io.*;

public class MainFrame extends JFrame {
    private enum MODE {
        START_MODE, STOP_MODE
    };

    private ServerPanel serverPanel;
    private ClientPanel clientPanel;
    private JLabel titleLabel;
    private Container container;
    private JLabel statusLabel;

    public MainFrame() {
        initComponents();
        setAppearance();
    }

    private void initComponents() {
        titleLabel = new JLabel("REMOTE DESK", SwingConstants.CENTER);
        statusLabel = new JLabel();
        serverPanel = new ServerPanel();
        clientPanel = new ClientPanel();
    }

    private void setAppearance() {
        statusLabel.setForeground(Color.RED);
        statusLabel.setText("Not ready to connect");
        statusLabel.setFont(new Font("verdana", Font.PLAIN, 12));
        setTitle("Remote Desk");
        container = getContentPane();
        container.setLayout(null);
        setSize(700, 395);
        titleLabel.setFont(new Font("serif", Font.BOLD, 40));
        serverPanel.setBorder(BorderFactory.createLineBorder(new Color(122, 138, 153)));
        clientPanel.setBorder(BorderFactory.createLineBorder(new Color(122, 138, 153)));
        //int titleBarHeight = 45;
        int borderWidths = 16;
        int lm = 0;
        int tm = 0;
        titleLabel.setBounds(lm + 10, tm + 10, getWidth() - ((lm + 10) * 2) - borderWidths, 32);
        serverPanel.setBounds(lm + 10, tm + 10 + 32 + 10, (getWidth() - ((lm + 10) * 2) - borderWidths) / 2 - 5, 285);
        clientPanel.setBounds(lm + 10 + (getWidth() - ((lm + 10) * 2) - borderWidths) / 2 + 5, tm + 10 + 32 + 10,
                (getWidth() - ((lm + 10) * 2) - borderWidths) / 2 - 5, 285);
        statusLabel.setBounds(lm + 10 + 5, tm + 10 + 32 + 10 + 285 + 2, 400, 15);
        container.add(titleLabel);
        container.add(serverPanel);
        container.add(clientPanel);
        container.add(statusLabel);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(d.width / 2 - getWidth() / 2, d.height / 2 - getHeight() / 2);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                try {
                    MainFrame.this.serverPanel.serverSocket.close();
                    System.exit(0);
                } catch (Exception e) {
                    System.exit(0);
                }
            }
        });
        setVisible(true);
    }

    public void setReady() {
        this.statusLabel.setForeground(new Color(0, 102, 0));
        this.statusLabel.setText("Ready to connect. Waiting for the connection...");
    }

    public void setNotReady() {
        this.statusLabel.setForeground(Color.RED);
        this.statusLabel.setText("Not ready to connect.");
    }

    // ************************************
    class ClientPanel extends JPanel {
        private JLabel moduleTitleLabel;
        private JLabel IPCaptionLabel;
        private JLabel portCaptionLabel;
        private JTextField IPTextField;
        private JTextField portTextField;
        private JButton connectButton;
        private JLabel passwordCaptionLabel;
        private JPasswordField passwordTextField;

        ClientPanel() {
            initComponents();
            setAppearance();
            addEventListeners();
            setViewMode();
        }

        public void initComponents() {
            moduleTitleLabel = new JLabel("Control Remote Desk", SwingConstants.CENTER);
            IPCaptionLabel = new JLabel("Patner IP");
            IPTextField = new JTextField();
            portCaptionLabel = new JLabel("Port");
            portTextField = new JTextField();
            passwordCaptionLabel = new JLabel("Password");
            passwordTextField = new JPasswordField();
            connectButton = new JButton("Connect");
        }

        public void setAppearance() {
            setLayout(null);
            setSize(327, 350);
            connectButton.setBorder(BorderFactory.createLineBorder(new Color(238, 238, 238)));
            moduleTitleLabel.setFont(new Font("Verdana", Font.BOLD, 25));
            IPCaptionLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
            portCaptionLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
            IPTextField.setFont(new Font("Verdana", Font.PLAIN, 20));
            portTextField.setFont(new Font("Verdana", Font.PLAIN, 20));
            passwordCaptionLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
            passwordTextField.setFont(new Font("Verdana", Font.PLAIN, 20));
            connectButton.setFont(new Font("Verdana", Font.PLAIN, 30));
            int lm = 0;
            int tm = 0;
            moduleTitleLabel.setBounds(lm + 10, tm + 10, getWidth() - ((lm + 10) * 2), 35);
            IPCaptionLabel.setBounds(lm + 8 + 30 - 15, tm + 10 + 35 + 10, 100, 30);
            IPTextField.setBounds(lm + 8 + 130 + 3 - 15, tm + 10 + 35 + 10, 180, 30);
            portCaptionLabel.setBounds(lm + 8 + 77 - 15, tm + 10 + 35 + 10 + 30 + 10, 53, 30);
            portTextField.setBounds(lm + 8 + 130 + 3 - 15, tm + 10 + 35 + 10 + 30 + 10, 180, 30);
            passwordCaptionLabel.setBounds(lm + 8 + 10 + 13 - 15, tm + 10 + 35 + 10 + 30 + 10 + 30 + 10, 107, 30);
            passwordTextField.setBounds(lm + 8 + 130 + 3 - 15, tm + 10 + 35 + 10 + 30 + 10 + 30 + 10, 180, 30);
            connectButton.setBounds(getWidth() / 2 - 135 / 2, tm + 10 + 35 + 10 + 30 + 10 + 30 + 10 + 30 + 15, 135, 40);
            add(moduleTitleLabel);
            add(IPCaptionLabel);
            add(IPTextField);
            add(portCaptionLabel);
            add(portTextField);
            add(passwordCaptionLabel);
            add(passwordTextField);
            add(connectButton);
        }

        public void addEventListeners() {
            connectButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    String hostName = IPTextField.getText().trim();
                    String port = portTextField.getText().trim();
                    String password = (new String(passwordTextField.getPassword())).trim();
                    ArrayList<String> errors = new ArrayList<>();
                    if (hostName.length() == 0)
                        errors.add("Hostname required");
                    if (port.length() == 0)
                        errors.add("Port required");
                    if (password.length() == 0)
                        errors.add("Password required");
                    if (errors.size() > 0) {
                        StringBuffer sb = new StringBuffer();
                        for (String e : errors) {
                            sb.append(e);
                            sb.append("\n");
                        }
                        JOptionPane.showMessageDialog(MainFrame.this, sb.toString(), "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    } else {
                        try {
                            new StartRemoteClient(hostName, Integer.parseInt(port), Integer.parseInt(password));
                            //MainFrame.this.setVisible(false);
                            statusLabel.setForeground(new Color(0, 102, 0));
                            statusLabel.setText("trying to connect with "+hostName);
                            connectButton.setEnabled(false);
                        } catch (Exception e) {
                            IPTextField.setText("");
                            portTextField.setText("");
                            passwordTextField.setText("");
                            JOptionPane.showMessageDialog(MainFrame.this, "Invalid IP Address, port number or password",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            statusLabel.setForeground(Color.RED);
                            statusLabel.setText("Not ready to connect");
                            connectButton.setEnabled(true);
                        }
                    }
                }
            });
        }

        public void setViewMode() {
            IPTextField.setText("");
            portTextField.setText("");
            passwordTextField.setText("");
            connectButton.setText("Connect");
        }

        class StartRemoteClient extends Thread {
            private Client client;
            private String server;
            private int port;
            private int password;

            StartRemoteClient(String server, int port, int password) {
                this.server = server;
                this.port = port;
                this.password = password;
                start();
            }

            public void run() {
                client = new Client(server, port, password);
                java.util.Timer timer;
                timer = new java.util.Timer();
                timer.schedule(new Check(client, timer), 0, 50);
            }

            class Check extends TimerTask {
                private Client client;
                private java.util.Timer timer;

                Check(Client client, java.util.Timer timer) {
                    this.timer = timer;
                    this.client = client;
                }

                public void run() {
                    int x = client.closed();
                    if (x == 1) {
                        MainFrame.this.clientPanel.passwordTextField.setText("");
                        MainFrame.this.setVisible(true);
                        timer.cancel();
                        timer.purge();
                        JOptionPane.showMessageDialog(MainFrame.this, "worng password", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        statusLabel.setForeground(Color.RED);
                            statusLabel.setText("Not ready to connect");
                            connectButton.setEnabled(true);
                    }else if (x == 2) {
                        MainFrame.this.setVisible(true);
                        MainFrame.this.clientPanel.setViewMode();
                        timer.cancel();
                        timer.purge();
                        JOptionPane.showMessageDialog(MainFrame.this, "Connection lost.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        statusLabel.setForeground(Color.RED);
                            statusLabel.setText("Not ready to connect");
                            connectButton.setEnabled(true);
                    }else if (x == 3) {
                        MainFrame.this.setVisible(true);
                        MainFrame.this.clientPanel.setViewMode();
                        timer.cancel();
                        timer.purge();
                        statusLabel.setForeground(Color.RED);
                            statusLabel.setText("Not ready to connect");
                            connectButton.setEnabled(true);
                    }else if (x == 4) {
                        MainFrame.this.setVisible(true);
                        MainFrame.this.clientPanel.setViewMode();
                        timer.cancel();
                        timer.purge();
                        JOptionPane.showMessageDialog(MainFrame.this, "Invalid IP address or port number.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        statusLabel.setForeground(Color.RED);
                            statusLabel.setText("Not ready to connect");
                            connectButton.setEnabled(true);
                    }else
                    {
                        MainFrame.this.setVisible(false); 
                    }
                }
            }
        }
    }

    // ******************************************
    class ServerPanel extends JPanel {
        private MODE mode;
        private JLabel moduleTitleLabel;
        private JLabel IPCaptionLabel;
        private JLabel portCaptionLabel;
        private JLabel IPLabel;
        private JLabel portLabel;
        private JButton startButton;
        private JLabel passwordCaptionLabel;
        private JLabel passwordLabel;
        private int password;
        private ServerSocket serverSocket;
        private JLabel message;

        ServerPanel() {
            initComponents();
            setAppearance();
            addEventListeners();
            setViewMode();
        }

        public void initComponents() {
            moduleTitleLabel = new JLabel("This Desk", SwingConstants.CENTER);
            IPCaptionLabel = new JLabel("IP Address :");
            portCaptionLabel = new JLabel("Port :");
            portLabel = new JLabel();
            passwordCaptionLabel = new JLabel("Password :");
            passwordLabel = new JLabel();
            startButton = new JButton("Start Sharing");
            message = new JLabel(
                    "<html>*Share the above-shown IP address, port number, and password with partner whom you want to allow to remotely control your computer.</html>");
            try {
                InetAddress inetAddress = InetAddress.getLocalHost();
                IPLabel = new JLabel(inetAddress.getHostAddress());
            } catch (Exception e) {
            }
        }

        public void setAppearance() {
            setLayout(null);
            setSize(327, 350);
            startButton.setBorder(BorderFactory.createLineBorder(new Color(238, 238, 238)));
            moduleTitleLabel.setFont(new Font("Verdana", Font.BOLD, 25));
            IPCaptionLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
            portCaptionLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
            IPLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
            portLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
            passwordCaptionLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
            passwordLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
            startButton.setFont(new Font("Verdana", Font.PLAIN, 25));
            int lm = 0;
            int tm = 0;
            moduleTitleLabel.setBounds(lm + 10, tm + 10, getWidth() - ((lm + 10) * 2), 35);
            IPCaptionLabel.setBounds(lm + 8, tm + 10 + 35 + 10, 130, 30);
            IPLabel.setBounds(lm + 8 + 130 + 3, tm + 10 + 35 + 10, 180, 30);
            portCaptionLabel.setBounds(lm + 8 + 68, tm + 10 + 35 + 10 + 30 + 10, 62, 30);
            portLabel.setBounds(lm + 8 + 130 + 3, tm + 10 + 35 + 10 + 30 + 10, 100, 30);
            passwordCaptionLabel.setBounds(lm + 8 + 10 + 5, tm + 10 + 35 + 10 + 30 + 10 + 30 + 10, 115, 30);
            passwordLabel.setBounds(lm + 8 + 130 + 3, tm + 10 + 35 + 10 + 30 + 10 + 30 + 10, 100, 30);
            startButton.setBounds(getWidth() / 2 - 180 / 2, tm + 10 + 35 + 10 + 30 + 10 + 30 + 10 + 30 + 15, 180, 40);
            message.setBounds(lm + 10, tm + 10 + 35 + 10 + 30 + 10 + 30 + 10 + 30 + 15 + 40 + 10, 300, 50);
            add(moduleTitleLabel);
            add(IPCaptionLabel);
            add(IPLabel);
            add(portCaptionLabel);
            add(portLabel);
            add(passwordCaptionLabel);
            add(passwordLabel);
            add(startButton);
            add(message);
        }

        public void addEventListeners() {
            startButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    try {
                        if (mode == MODE.START_MODE) {
                            Random random = new Random();
                            password = random.nextInt(8999) + 1000;
                            passwordLabel.setText(String.valueOf(password));
                            new StartRemoteServer();
                            setShareMode();
                        } else {
                            setViewMode();
                        }
                    } catch (Exception e) {
                    }
                }
            });
        }

        public void setViewMode() {
            portCaptionLabel.setVisible(false);
            passwordCaptionLabel.setVisible(false);
            portLabel.setVisible(false);
            passwordLabel.setVisible(false);
            startButton.setText("Start Sharing");
            this.message.setVisible(false);
            MainFrame.this.setNotReady();
            mode = MODE.START_MODE;
        }

        public void setShareMode() {
            portCaptionLabel.setVisible(true);
            passwordCaptionLabel.setVisible(true);
            portLabel.setVisible(true);
            passwordLabel.setVisible(true);
            startButton.setText("Stop Sharing");
            this.message.setVisible(true);
            MainFrame.this.setReady();
            mode = MODE.STOP_MODE;
        }
    }

    class RemoteServer {
        private int port;
        private ServerSocket serverSocket;

        RemoteServer() {
            try {
                createServer();
                startListening();
            } catch (Exception ee) {
            }
        }

        public void createServer() {
            try {
                Random random = new Random();
                int ran = random.nextInt(60000) + 55300;
                this.port = ran;
                serverSocket = new ServerSocket(port);
                MainFrame.this.serverPanel.portLabel.setText(String.valueOf(port));
            } catch (Exception e) {
                createServer();
            }
        }

        public int getPortNumber() {
            return this.port;
        }

        public void startListening() {
            Socket socket;
            InputStream inputStream;
            OutputStream outputStream;
            try {
                socket = serverSocket.accept();
                inputStream = socket.getInputStream();
                byte b[] = new byte[4];
                inputStream.read(b);
                int fromClient = Convert.byteToInt(b);
                if (fromClient == MainFrame.this.serverPanel.password) {
                    MainFrame.this.setVisible(false);
                    outputStream = socket.getOutputStream();
                    outputStream.write(Convert.intToByte(1));
                    outputStream.flush();
                    socket.close();
                    MainFrame.this.serverPanel.serverSocket = serverSocket;
                    DisconnectFrame disconnectFrame = new DisconnectFrame();
                    new Server(serverSocket, port);
                    disconnectFrame.dispose();
                    MainFrame.this.setVisible(true);
                    MainFrame.this.setAlwaysOnTop(true);
                    MainFrame.this.serverPanel.setViewMode();
                    MainFrame.this.setAlwaysOnTop(false);
                } else {
                    outputStream = socket.getOutputStream();
                    outputStream.write(Convert.intToByte(0));
                    outputStream.flush();
                    startListening();
                }
            } catch (Exception e) {
            }
        }

        public void closeServer() {
            try {
                serverSocket.close();
            } catch (Exception e) {
            }
        }

        public class DisconnectFrame extends JDialog {
            private JButton button;

            public DisconnectFrame() {
                initComponents();
                setAppearance();
            }

            private void initComponents() {
                button = new JButton("Stop Sharing");
            }

            private void setAppearance() {
                setTitle("Remote Desk");
                setLayout(new BorderLayout());
                setSize(195, 75);
                button.setFont(new Font("Verdana", Font.BOLD, 20));
                add(button, BorderLayout.CENTER);
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                setLocation(d.width - 10 - getWidth(), (d.height - 50) - getHeight());
                setAlwaysOnTop(true);
                setVisible(true);
                addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent evt) {
                        quit();
                    }
                });
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ev) {
                        quit();
                    }
                });
            }

            public void quit() {
                try {
                    dispose();
                    MainFrame.this.setVisible(true);
                    MainFrame.this.setAlwaysOnTop(true);
                    MainFrame.this.serverPanel.setViewMode();
                    MainFrame.this.serverPanel.serverSocket.close();
                    MainFrame.this.setAlwaysOnTop(false);
                } catch (Exception e) {
                }
            }

        }

    }

    class StartRemoteServer extends Thread {
        private RemoteServer server;

        StartRemoteServer() {
            start();
        }

        public void run() {
            server = new RemoteServer();
        }

        public void closeServer() {
            server.closeServer();
        }
    }
}