package com.spengerblog.schnedale.logging;

public interface Logger {

    /**
     * get the output type of the logger (file, stdout)
     * @return string that represents output type
     */
    String getOutput();


    String getInfoTemplate();
    String getWarningTemplate();
    String getErrorTemplate();

    void info(String s, String... args);
    void warning(String warning, String... args);
    void error(String error, String... args);

    /**
     * returns the placeholder that is then used for arguments to build the String
     * @return the placeholder
     */
    String getPlaceholder();

    String formatLogMessage(String message, String... args);
    void log(String message);

}
