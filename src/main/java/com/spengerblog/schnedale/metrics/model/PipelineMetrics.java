package com.spengerblog.schnedale.metrics.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class PipelineMetrics {
    private String prefix;
    private Set<String> stages;
    private long messagesPublished;
}
