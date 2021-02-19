package com.spengerblog.schnedale.pipeline;

import com.spengerblog.schnedale.metrics.MetricsAggregator;
import com.spengerblog.schnedale.socket.SocketIO;
import com.spengerblog.schnedale.storage.MassFileStorage;
import com.spengerblog.schnedale.storage.StorageManager;

import java.nio.channels.Pipe;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PipelineManager {

    private StorageManager storageManager;
    private MetricsAggregator metricsAggregator;
    private Map<String, Pipeline> pipelines;

    public PipelineManager(MetricsAggregator metricsAggregator) {
        this.pipelines = new HashMap<>();
        this.metricsAggregator = metricsAggregator;
        this.storageManager = new StorageManager(new MassFileStorage());
    }

    public void publishTo(String pipeline, String data, String source) {
        String[] prefixAndStage = pipeline.split(":");
        checkAndCreate(prefixAndStage[0]);
        pipelines.get(prefixAndStage[0]).process(prefixAndStage[1], source, data);
    }

    public void subscribeTo(String pipeline, SocketIO socketIO) {
        String[] prefixAndStage = pipeline.split(":");
        checkAndCreate(prefixAndStage[0]);
        pipelines.get(prefixAndStage[0]).addSubscriber(prefixAndStage[1], socketIO);
    }

    public Set<String> getPipelines() {
        return this.pipelines.keySet();
    }

    public void disconnect(SocketIO socketIO) {
        for(Pipeline pipeline : pipelines.values()) {
            pipeline.disconnect(socketIO);
        }
    }

    private void checkAndCreate(String pipelinePrefix) {
        if(pipelines.get(pipelinePrefix) == null) {
            pipelines.put(pipelinePrefix, new Pipeline(pipelinePrefix, storageManager));
        }
    }

}
