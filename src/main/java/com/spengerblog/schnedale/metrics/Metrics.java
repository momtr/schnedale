package com.spengerblog.schnedale.metrics;

import com.spengerblog.schnedale.metrics.model.ConnectionMetrics;
import com.spengerblog.schnedale.metrics.model.PipelineMetrics;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
public class Metrics {

    private Map<String, ConnectionMetrics> connections;
    private Map<String, PipelineMetrics> pipelineMetrics;

    public Metrics() {
        this.connections = new HashMap<>();
        this.pipelineMetrics = new HashMap<>();
    }

    public void addConnection(String connectionUniqueIdentifier) {
        ConnectionMetrics connectionMetrics = ConnectionMetrics.builder()
                .identifier(connectionUniqueIdentifier)
                .since(System.currentTimeMillis())
                .build();
        connections.put(connectionUniqueIdentifier, connectionMetrics);
    }

    public void addPipeline(String prefix, Set<String> stages) {
        PipelineMetrics pipelineMetric = PipelineMetrics.builder()
                .messagesPublished(0)
                .prefix(prefix)
                .stages(stages)
                .build();
        pipelineMetrics.put(prefix, pipelineMetric);
    }

    public void addMessageToPipeline(String pipelinePrefix) {
        PipelineMetrics pipelineMetrics = this.pipelineMetrics.get(pipelinePrefix);
        if(pipelineMetrics != null) {
            pipelineMetrics.setMessagesPublished(pipelineMetrics.getMessagesPublished() + 1);
        }
    }

}
