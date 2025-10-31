package uk.ac.hw.group20.test.order;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.ac.hw.group20.admin.dto.LoginResponseDto;
import uk.ac.hw.group20.admin.service.ValidateUserImpl;

public class ValidateUserTest {
	
	String username;
	String password;
	ValidateUserImpl validateUser;
	
	
	@BeforeEach
	void setupVariables() {
		this.username = "IMA";
		this.password = "IMA";
		this.validateUser = new ValidateUserImpl();
	}
	
	@Test
	void testValidateUsername() {
		this.username = null;
		
		LoginResponseDto response = this.validateUser.validateUser(this.username, this.password);
		
		Assertions.assertEquals("Username cannot be blank", response.getMessage());
		
	}
	
	@Test
	void testValidatePassword() {
		this.password = null;
		
		LoginResponseDto response = this.validateUser.validateUser(this.username, this.password);
		
		Assertions.assertEquals("Password cannot be blank", response.getMessage());
		
	}
	
	@Test
	void testValidateWrongCredentials() {
		
		this.password = "ima";
		
		LoginResponseDto response = this.validateUser.validateUser(this.username, this.password);
		
		Assertions.assertEquals("Invalid username and/or password", response.getMessage());
		
	}
	
	@Test
	void testValidateCorrectCredentials() {
		
		LoginResponseDto response = this.validateUser.validateUser(this.username, this.password);
		
		Assertions.assertTrue("Y".equals(response.getStatus()));
		
	}

}
