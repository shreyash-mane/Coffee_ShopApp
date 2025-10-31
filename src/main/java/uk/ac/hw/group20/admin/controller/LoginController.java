package uk.ac.hw.group20.admin.controller;

import uk.ac.hw.group20.admin.dto.LoginDto;
import uk.ac.hw.group20.admin.dto.LoginResponseDto;
import uk.ac.hw.group20.admin.service.ValidateUserImpl;

public class LoginController {
	
	public LoginResponseDto validateUser(LoginDto dto) {
		String username = dto.getUsername();
		String password = dto.getPassword();
		
		ValidateUserImpl validate = new ValidateUserImpl();
		LoginResponseDto response = validate.validateUser(username, password);
		
		return response;
	}

}
