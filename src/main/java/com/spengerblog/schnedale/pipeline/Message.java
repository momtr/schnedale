package com.spengerblog.schnedale.pipeline;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Builder
@Data
@AllArgsConstructor
@EqualsAndHashCode
public class Message {

    private String source;
    private String pipeline;
    private String data;

    @Override
    public String toString() {
        return String.format("Message(source=%s,pipeline=%s,data=%s)", source, pipeline, data);
    }

}
