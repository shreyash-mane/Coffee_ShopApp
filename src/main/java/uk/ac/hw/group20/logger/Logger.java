package uk.ac.hw.group20.logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.ac.hw.group20.utils.LogLevel;

/**
 * Logger Singleton Class
 * Method logMessage to accept Sting message and log to a file
 * @author Ima Mahenge
 */

public class Logger {
	
	private static Logger loggerInstance;
	private static StringBuilder logs = new StringBuilder();
	private static String eventTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	private static String logFileAppender = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	private static final String LOG_FILE = "logs/coffee-shop-log-" + logFileAppender + ".log";
    private static final Object lock = new Object();
	
	private Logger() {
		
	}
	
	public static synchronized Logger getLoggerInstance() {
		
		if(loggerInstance == null) {
			loggerInstance = new Logger();
			System.out.println(eventTime + " - Logger new Instance Created.");
		}
		
		return loggerInstance;
	}
	
	
	public static synchronized void logMessage(LogLevel level, String message) {
	    getLoggerInstance();

	    String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	    String threadName = Thread.currentThread().getName();
	    String fullLog = String.format("[%s] [%-5s] [%-15s] - %s",
	            timestamp,
	            level,
	            threadName.length() > 15 ? threadName.substring(0, 15) : threadName,
	            message);

	    // Keep current log in memory
	    logs.append(fullLog).append("\n");

	    // Write to file with thread safety
	    synchronized (lock) {
	        try (FileWriter fw = new FileWriter(LOG_FILE, true);
	             PrintWriter pw = new PrintWriter(fw)) {

	            pw.println(fullLog);
	            System.out.println(fullLog);

	        } catch (IOException e) {
	            throw new RuntimeException("Failed to write to log file due to: " + e.getMessage());
	        }
	    }
	}

}
