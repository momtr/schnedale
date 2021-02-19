package com.spengerblog.schnedale.storage;

import com.spengerblog.schnedale.logging.DefaultLogger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MassFileStorage implements Storage {

    private final String ERROR_LOG_FILE = "error_log_records.txt";
    private final String FILE_PATH_PREFIX = "./persistence/";

    private DefaultLogger log;

    public MassFileStorage() {
        this.log = new DefaultLogger(MassFileStorage.class);
    }

    @Override
    public void store(String prefix, String tag, String record, String representation) {
        try {
            File file = new File(getFileName(prefix, tag, representation));
            file.getParentFile().mkdirs();
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(record);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            log.error("could not save entry. attempting to save it in the error log");
        }
    }

    @Override
    public void errorLog(String record) {
        try {
            FileWriter fileWriter = new FileWriter(ERROR_LOG_FILE, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(record);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            log.error("could not save record in error log");
        }
    }

    private String getFileName(String prefix, String tag, String representation) {
        return String.format("%s%s_%s.%s", FILE_PATH_PREFIX, prefix, tag, representation);
    }

}
