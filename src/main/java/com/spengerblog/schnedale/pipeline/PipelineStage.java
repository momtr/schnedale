package com.spengerblog.schnedale.pipeline;

import com.spengerblog.schnedale.logging.DefaultLogger;
import com.spengerblog.schnedale.socket.SocketIO;

import java.util.ArrayList;
import java.util.List;

public class PipelineStage {

    private String stage;
    private String pipelinePrefix;
    private List<SocketIO> subscribers;
    private MessageQueue messageQueue;

    private DefaultLogger log;

    public PipelineStage(String pipelinePrefix, String stage) {
        this.pipelinePrefix = pipelinePrefix;
        this.stage = stage;
        this.messageQueue = new MessageQueue();
        this.subscribers = new ArrayList<>();
        log = new DefaultLogger(PipelineStage.class);
    }

    public void process(String source, String data) {
        Message message = Message.builder()
                .data(data)
                .pipeline(getIdentifier())
                .source(source)
                .build();
        messageQueue.addMessage(message);
        log.info("received message in pipeline [%%]", stage);
        this.sendMessages();
    }

    public void addSubscriber(SocketIO socketIO) {
        this.subscribers.add(socketIO);
        log.info("socket [%%] subscribed to pipeline [%%]", socketIO.getIdentifier(), stage);
    }

    public void sendMessages() {
        List<Message> messages = messageQueue.getAll();
        for(Message message : messages) {
            for(SocketIO socketIO : subscribers) {
                notify(socketIO, message);
            }
        }
        log.info("sent [%%] messages to [%%] subscribers in pipeline [%%]", messages.size()+"", subscribers.size()+"", stage);
    }

    public void disconnect(SocketIO socketIO) {
        if(subscribers.remove(socketIO)) {
            log.info("client [%%] disconnected from pipeline [%%:%%]", socketIO.getIdentifier(), pipelinePrefix, stage);
        }
    }

    private void notify(SocketIO socketIO, Message message) {
        socketIO.write(String.format("NOTI %s", message.toString()));
    }

    private String getIdentifier() {
        return this.pipelinePrefix + ":" + this.stage;
    }

}
