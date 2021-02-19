package com.spengerblog.schnedale.pipeline;

import com.spengerblog.schnedale.logging.DefaultLogger;
import com.spengerblog.schnedale.socket.SocketIO;
import com.spengerblog.schnedale.storage.StorageManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pipeline {

    private String pipelinePrefix;
    private Map<String, PipelineStage> stages;
    private StorageManager storageManager;

    private DefaultLogger log;

    public Pipeline(String pipelinePrefix, StorageManager storageManager) {
        this.log = new DefaultLogger(Pipeline.class);
        this.pipelinePrefix = pipelinePrefix;
        this.stages = new HashMap<>();
        this.storageManager = storageManager;
    }

    public void process(String stage, String source, String data) {
        checkAndCreate(stage);
        this.stages.get(stage).process(source, data);
        this.storageManager.storeRecord(pipelinePrefix, stage, source, data);
    }

    public void addSubscriber(String stage, SocketIO socketIO) {
        checkAndCreate(stage);
        this.stages.get(stage).addSubscriber(socketIO);
    }

    public void disconnect(SocketIO socketIO) {
        for(PipelineStage stage : stages.values()) {
            stage.disconnect(socketIO);
        }
    }

    private void checkAndCreate(String stage) {
        if(stages.get(stage) == null) {
            stages.put(stage, new PipelineStage(this.pipelinePrefix, stage));
        }
    }

}
