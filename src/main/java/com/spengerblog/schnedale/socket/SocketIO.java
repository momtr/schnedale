package com.spengerblog.schnedale.socket;

import com.spengerblog.schnedale.command.CommandManager;
import com.spengerblog.schnedale.logging.DefaultLogger;
import com.spengerblog.schnedale.metrics.MetricsAggregator;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@Getter
@EqualsAndHashCode
public class SocketIO {

    private String ip;
    private String port;
    private PrintWriter writer;
    private BufferedReader reader;
    private CommandManager commandManager;
    private MetricsAggregator metricsAggregator;

    private DefaultLogger log;

    public SocketIO(String ip, String port, PrintWriter writer, BufferedReader reader, CommandManager commandManager, MetricsAggregator metricsAggregator) throws IOException {
        this.ip = ip;
        this.port = port;
        this.writer = writer;
        this.reader = reader;
        this.log = new DefaultLogger(SocketIO.class);
        this.commandManager = commandManager;
        this.metricsAggregator = metricsAggregator;
        this.start();
    }

    public void start() {
        try {
            String line = reader.readLine();
            while(line != null) {
                read(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            log.info("client [%%] disconnected (no proper disconnect)", getIdentifier());
        } finally {
            log.info("remove client from pipelines");
            this.commandManager.disconnect(this);
        }
    }

    public void write(String message) {
        this.writer.println(message);
        this.writer.flush();
    }

    public void read(String line) {
        log.info("CLIENT: [%%]", line);
        this.commandManager.process(this, line);
    }

    public String getIdentifier() {
        return this.getIp() + ":" + this.getPort();
    }

}
