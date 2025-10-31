package uk.ac.hw.group20.main;

import javax.swing.JFrame;

import uk.ac.hw.group20.admin.view.LoginGui;
import uk.ac.hw.group20.logger.Logger;
import uk.ac.hw.group20.utils.LogLevel;

public class MainApp extends JFrame {
	public MainApp() {
	}

	private static final long serialVersionUID = 1L;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			LoginGui frame = new LoginGui();
			frame.setVisible(true);
			Logger.logMessage(LogLevel.INFO, "Coffee Application have Started Successfully");
		} catch (Exception e) {
			Logger.logMessage(LogLevel.ERROR, "Coffee Application have Started Successfully");
			throw new RuntimeException("Failed to start the application");
		}
	}


}
