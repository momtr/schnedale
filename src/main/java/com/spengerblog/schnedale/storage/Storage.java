package com.spengerblog.schnedale.storage;

public interface Storage {

    /**
     * stores a string (= record) that is organized according to a specific representation
     * @param prefix the folder
     * @param tag the file catrgory
     * @param record the string
     * @param representation the string's representation (base64, string_binary, etc.)
     */
    void store(String prefix, String tag, String record, String representation);

    void errorLog(String error);

}
