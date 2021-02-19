package com.spengerblog.schnedale.socket;

import com.spengerblog.schnedale.command.CommandManager;
import com.spengerblog.schnedale.logging.DefaultLogger;
import com.spengerblog.schnedale.metrics.MetricsAggregator;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {

    private Socket socket;
    private DefaultLogger log;
    private CommandManager commandManager;
    private MetricsAggregator metricsAggregator;

    public ServerThread(Socket socket, CommandManager commandManager, MetricsAggregator metricsAggregator) {
        this.socket = socket;
        this.log = new DefaultLogger(ServerThread.class);
        this.commandManager = commandManager;
        this.metricsAggregator = metricsAggregator;
    }

    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            new SocketIO(this.socket.getLocalAddress().getHostAddress(), this.socket.getPort()+"", writer, reader, this.commandManager, this.metricsAggregator);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("error when receiving socket-client [%%]", e.getLocalizedMessage());
        }
    }

}
