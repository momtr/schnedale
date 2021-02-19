package com.spengerblog.schnedale.command;

import com.spengerblog.schnedale.metrics.MetricsAggregator;
import com.spengerblog.schnedale.pipeline.PipelineManager;
import com.spengerblog.schnedale.socket.SocketIO;
import com.spengerblog.schnedale.logging.DefaultLogger;
import com.spengerblog.schnedale.socket.model.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager {

    private final String PUBLISH_COMMAND = "PUSH";
    private final String SUBSCRIBE_COMMAND = "SUBS";
    private final String ERROR_COMMAND = "ERR";
    private final String ACK_COMMAND = "ACK";

    private PipelineManager pipelineManager;
    private MetricsAggregator metricsAggregator;

    private DefaultLogger log;

    public CommandManager(MetricsAggregator metricsAggregator) {
        this.log = new DefaultLogger(CommandManager.class);
        this.pipelineManager = new PipelineManager(metricsAggregator);
        this.metricsAggregator = metricsAggregator;
        log.info("initialized command manager");
    }

    public void process(SocketIO socketIO, String message) {
        Command command = parseCommand(message);
        switch (command.getCommand()) {
            case PUBLISH_COMMAND:
                if(command.getParams().size() != 2) {
                    sendError(socketIO,1002, "invalid_number_of_arguments: " + command.getParams().size());
                } else if (command.getParams().get(0).split(":").length != 2) {
                    sendError(socketIO,1003, "pipeline_tag_not_specified");
                } else {
                    pipelineManager.publishTo(command.getParams().get(0), command.getParams().get(1), socketIO.getIdentifier());
                    socketIO.write(ACK_COMMAND);
                }
                break;
            case SUBSCRIBE_COMMAND:
                if(command.getParams().size() != 1) {
                    sendError(socketIO,1002, "invalid_number_of_arguments: " + command.getParams().size());
                } else if (command.getParams().get(0).split(":").length != 2) {
                    sendError(socketIO,1003, "pipeline_tag_not_specified");
                } else {
                    pipelineManager.subscribeTo(command.getParams().get(0), socketIO);
                    socketIO.write(ACK_COMMAND);
                }
                break;
            default:
                sendError(socketIO, 1001, "command_not_found");
                break;
        }
    }

    public void disconnect(SocketIO socketIO) {
        pipelineManager.disconnect(socketIO);
    }

    private Command parseCommand(String message) {
        String[] splitted = message.split(" ");
        List<String> params = new ArrayList<String>(Arrays.asList(splitted));
        params.remove(0);
        return Command.builder()
                .command(splitted[0])
                .errorFlag(false)
                .params(params)
                .build();
    }

    private void sendError(SocketIO socketIO, int code, String message) {
        socketIO.write(String.format("%s %s %s", ERROR_COMMAND, code, message));
    }

}
