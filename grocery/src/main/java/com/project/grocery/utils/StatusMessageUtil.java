package com.project.grocery.utils;

import java.util.ResourceBundle;

public class StatusMessageUtil {

    private static final ResourceBundle bundle;

    static {
        bundle = ResourceBundle.getBundle("constant");
    }

    public static String getStatusMessage(String key) {
        return bundle.getString(key);
    }
}
