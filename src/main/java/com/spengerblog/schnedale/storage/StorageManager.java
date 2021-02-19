package com.spengerblog.schnedale.storage;

import com.spengerblog.schnedale.logging.DefaultLogger;

import java.util.Base64;
import java.util.List;

public class StorageManager {

    private DefaultLogger log;
    private Storage storage;

    public StorageManager(Storage storage) {
        this.log = new DefaultLogger(StorageManager.class);
        this.storage = storage;
    }

    public void storeRecord(String pipelinePrefix, String stage, String source, String data) {
        this.storage.store(pipelinePrefix, stage, formatRecord(source, data), "txt");
    }

    private String formatRecord(String source, String data) {
        return String.format("[%s]: %s", source, fromBase64(data));
    }

    private String fromBase64(String base64string) {
        try {
            return new String(Base64.getDecoder().decode(base64string));
        } catch (Exception e) {
            return base64string;
        }
    }

}
