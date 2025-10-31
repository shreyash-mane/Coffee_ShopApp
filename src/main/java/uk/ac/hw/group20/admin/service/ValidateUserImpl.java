package uk.ac.hw.group20.admin.service;

import java.util.List;

import uk.ac.hw.group20.admin.dto.LoginResponseDto;
import uk.ac.hw.group20.admin.model.User;
import uk.ac.hw.group20.errorhandler.InvalidInputException;
import uk.ac.hw.group20.logger.Logger;
import uk.ac.hw.group20.utils.LogLevel;
import uk.ac.hw.group20.utils.Password;

public class ValidateUserImpl implements ValidateUser {

	private List<User> users = UserLoader.loadUsersFromCSV();

	public LoginResponseDto validateUser(String username, String password) {

		LoginResponseDto response = new LoginResponseDto();

		String responseMessage = null;
		String passwordMd5 = null;

		if (username == null || username.trim().isEmpty()) {
			responseMessage = "Username cannot be blank";
		} else if (password == null || password.trim().isEmpty()) {
			responseMessage = "Password cannot be blank";
		} else {
			passwordMd5 = Password.hashPassword(password);
			for (User user : users) {
				if (user.getUsername().equals(username) && user.getPassword().equals(passwordMd5)) {
					response.setStatus("Y");
					response.setUserId(user.getUserId());
					break;
				} else {
					responseMessage = "Invalid username and/or password";

				}
			}
		}

		response.setMessage(responseMessage);

		if (!"Y".equals(response.getStatus()) && username != null && password != null && !username.trim().isEmpty() && !password.trim().isEmpty()) {
			User usertoAdd = null;
			try {
				usertoAdd = new User("LOGIN000", username, passwordMd5, "ROLE00");
			} catch (InvalidInputException e) {
				Logger.logMessage(LogLevel.ERROR, e.getMessage());
			}
			Logger.logMessage(LogLevel.ERROR, "User Login failed, Adding user to log file with details: " + usertoAdd);
			UserLoader.addUserToCSV(usertoAdd);
		}

		return response;
	}

}
