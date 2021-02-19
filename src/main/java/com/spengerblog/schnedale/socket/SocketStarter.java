package com.spengerblog.schnedale.socket;

import com.spengerblog.schnedale.command.CommandManager;
import com.spengerblog.schnedale.logging.DefaultLogger;
import com.spengerblog.schnedale.metrics.MetricsAggregator;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketStarter {

    private final int PORT = 2227;
    private ServerSocket serverSocket;
    private DefaultLogger log;
    private CommandManager commandManager;
    private MetricsAggregator metricsAggregator;

    public SocketStarter(CommandManager commandManager, MetricsAggregator metricsAggregator) {
        log = new DefaultLogger(SocketStarter.class);
        this.commandManager = commandManager;
        this.metricsAggregator = metricsAggregator;
    }

    public void startSocketServer() throws IOException {
        serverSocket = new ServerSocket(PORT);
        log.info("server-socket listening on port [%%]", PORT+"");
        while(true) {
            Socket socket = serverSocket.accept();
            log.info("new client [%%]", socket.getInetAddress().getHostAddress());
            new ServerThread(socket, commandManager, metricsAggregator).start();
        }
    }

}
