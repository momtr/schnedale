package com.spengerblog.schnedale.socket.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Command {
    private String command;
    private List<String> params;
    private boolean errorFlag;
}
