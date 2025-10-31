package uk.ac.hw.group20.errorhandler;

/**
 * Checked exception that forces the programmer to handle when calling the method that throws this method
 */
public class InvalidInputException extends Exception{

	public InvalidInputException(String message) {
		super(message);
	}
}
