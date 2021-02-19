package com.spengerblog.schnedale;

import com.spengerblog.schnedale.command.CommandManager;
import com.spengerblog.schnedale.metrics.MetricsAggregator;
import com.spengerblog.schnedale.socket.SocketStarter;

import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException {
        MetricsAggregator metricsAggregator = new MetricsAggregator();
        CommandManager commandManager = new CommandManager(metricsAggregator);
        SocketStarter socketStarter = new SocketStarter(commandManager, metricsAggregator);
        socketStarter.startSocketServer();
    }

}
