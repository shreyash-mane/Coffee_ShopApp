package uk.ac.hw.group20.admin.service;

import uk.ac.hw.group20.admin.dto.LoginResponseDto;

public interface ValidateUser {
	public LoginResponseDto validateUser(String username, String password);

}
