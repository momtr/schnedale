package com.spengerblog.schnedale.metrics.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ConnectionMetrics {
    private String identifier;
    private long since;
}
