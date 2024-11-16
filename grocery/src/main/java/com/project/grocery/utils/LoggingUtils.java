package com.project.grocery.utils;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Properties;

public class LoggingUtils {

    private static final Logger logger = LoggerFactory.getLogger(LoggingUtils.class);
    private static final Properties logMessages = new Properties();

    static {
        try {
            logMessages.load(new ClassPathResource("logerror.properties").getInputStream());
        } catch (IOException e) {
            logger.error("Failed to load logerror.properties", e);
        }
    }

    public static void logError(String key, Object... params) {
        String message = logMessages.getProperty(key, " error");
        logger.error(message, params);
    }


    


    



    


    
}
