package uk.ac.hw.group20.admin.dto;

public class LoginDto {
	String username;
	String password;
	
	public LoginDto(){
		this.password = null;
		this.username = null;
	}
	
	public LoginDto(String username, String password) {
		this.password = password;
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "LoginDto [username=" + username + ", password=" + password + "]";
	}

	

}
