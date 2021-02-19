package com.spengerblog.schnedale.metrics;

import com.spengerblog.schnedale.logging.DefaultLogger;
import com.spengerblog.schnedale.pipeline.PipelineManager;

public class MetricsAggregator {

    private Metrics metrics;
    private DefaultLogger log;

    public MetricsAggregator() {
        this.metrics = new Metrics();
        this.log = new DefaultLogger(MetricsAggregator.class);
        log.info("initialized metrics aggregator");
    }

    public void update(PipelineManager pipelineManager) {

    }

}
