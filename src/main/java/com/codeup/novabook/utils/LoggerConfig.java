package com.codeup.novabook.utils;

import java.io.IOException;
import java.util.logging.*;

/**
 * Utility class for configuring and managing application logging.
 */
public class LoggerConfig {
    private static final Logger LOGGER = Logger.getLogger("com.codeup.novabook");
    private static final String LOG_FILE = "app.log";
    private static boolean initialized = false;

    /**
     * Initializes the logging configuration.
     * Sets up both file and console handlers with appropriate formatters.
     */
    public static void init() {
        if (initialized) {
            return;
        }

        try {
            // Create file handler
            FileHandler fileHandler = new FileHandler(LOG_FILE, true);
            fileHandler.setFormatter(new SimpleFormatter());

            // Create console handler
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new SimpleFormatter());

            // Configure root logger
            Logger rootLogger = Logger.getLogger("");
            rootLogger.setLevel(Level.INFO);

            // Remove existing handlers
            Handler[] handlers = rootLogger.getHandlers();
            for (Handler handler : handlers) {
                rootLogger.removeHandler(handler);
            }

            // Add our handlers
            rootLogger.addHandler(fileHandler);
            rootLogger.addHandler(consoleHandler);

            LOGGER.info("Logging system initialized");
            initialized = true;

        } catch (IOException e) {
            System.err.println("Failed to initialize logging: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gets the application logger.
     *
     * @return The configured logger instance
     */
    public static Logger getLogger() {
        if (!initialized) {
            init();
        }
        return LOGGER;
    }
}