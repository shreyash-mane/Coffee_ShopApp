package uk.ac.hw.group20.test.order;

/**
 * @author Junaid
 * 
 */
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.ac.hw.group20.logger.Logger;
import uk.ac.hw.group20.utils.LogLevel;

public class LoggerTest {
	private static final String LOG_FILE = "logs/coffee_shop_log.log";
    private Logger logger;

    @BeforeEach
    public void setUp() {
        logger = Logger.getLoggerInstance();
    }

    @Test
    public void testSingletonInstance() {
        Logger anotherLogger = Logger.getLoggerInstance();
        assertSame(logger, anotherLogger, "Logger is a singleton instance");
    }

    @Test
    public void testLogMessage() throws IOException {
        String testMessage = "Test log entry";
        logger.logMessage(LogLevel.INFO, testMessage);
        
        // Verifying the message is written to the log file
        assertTrue(containsLogEntry(testMessage), "Log file should contain this message");
    }

    private boolean containsLogEntry(String message) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(message)) {
                    return true;
                }
            }
        }
        return false;
    }
}
