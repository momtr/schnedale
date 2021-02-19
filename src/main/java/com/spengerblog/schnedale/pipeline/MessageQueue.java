package com.spengerblog.schnedale.pipeline;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class MessageQueue {

    private ArrayDeque<Message> queue;

    public MessageQueue() {
        queue = new ArrayDeque<>();
    }

    public void addMessage(Message message) {
        queue.add(message);
    }

    public Message getFirstMessage() {
        return queue.pop();
    }

    public List<Message> getAll() {
        List<Message> messages = new ArrayList<>();
        while(!queue.isEmpty()) {
            messages.add(queue.pop());
        }
        return messages;
    }

}
