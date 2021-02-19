package com.spengerblog.schnedale.logging;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DefaultLogger implements Logger {

    private String output;
    private String className;
    private String placeholder;

    public DefaultLogger(Class<?> classType) {
        output = "stdout";
        placeholder = "%%";
        this.className = classType.getCanonicalName();
    }

    public String getOutput() {
        return output;
    }

    public String getTime() {
        return new SimpleDateFormat("hh:mm:ss").format(new Date());
    }

    public String getInfoTemplate() {
        return LogColor.makeGreen("[INFO] ") + getTime() + " - [" + className + "]: " + LogColor.makeBlue("%s");
    }

    public String getWarningTemplate() {
        return LogColor.makeYellow("[WARNING] ") + getTime() + " - [" + className + "]: " + LogColor.makeBlue("%s");
    }

    public String getErrorTemplate() {
        return LogColor.makeRed("[ERROR] ") + getTime() + " - [" + className + "]: " + LogColor.makeBlue("%s");
    }

    public void info(String info, String... args) {
        log(String.format(getInfoTemplate(), formatLogMessage(info, args)));
    }

    public void warning(String warning, String... args) {
        log(String.format(getWarningTemplate(), formatLogMessage(warning, args)));
    }

    public void error(String error, String... args) {
        log(String.format(getErrorTemplate(), formatLogMessage(error, args)));
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public String formatLogMessage(String message, String... args) {
        String[] subs = message.split(getPlaceholder());
        if(subs.length != (args.length + 1))
            return "<error>number of arguments does not match number of placeholders</error>";
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < subs.length; i++) {
            stringBuilder.append(subs[i]);
            if(i < args.length)
                stringBuilder.append(args[i]);
        }
        return stringBuilder.toString();
    }

    public void log(String message) {
        System.out.println(message);
    }

}
