package hu.otpmobil.logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerService {
    private static final Logger LOGGER = Logger.getLogger(LoggerService.class.getName());

    static {
        try {
            var fileHandler = new FileHandler("application.log", true);
            var formatter = new SimpleFormatter();

            fileHandler.setFormatter(formatter);

            LOGGER.addHandler(fileHandler);
        } catch (IOException e) {
            LOGGER.warning(e.getMessage());
        }
    }

    public static void warning(String message) {
        LOGGER.warning(message);
    }

    public static void severe(String message) {
        LOGGER.severe(message);
    }
}
