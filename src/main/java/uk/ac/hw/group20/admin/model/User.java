package uk.ac.hw.group20.admin.model;

import uk.ac.hw.group20.errorhandler.InvalidInputException;

public class User {
	private String userId;
    private String username;
    private String password;
    private String roleId;
    
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public User(String userId, String username, String password, String roleId) throws InvalidInputException {
		
		if(username == null || username.trim().isEmpty()) {
			throw new InvalidInputException("Username can not be blank");
		}
		
		if(password == null || password.trim().isEmpty()) {
			throw new InvalidInputException("Password can not be blank");
		}
		
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.roleId = roleId;
	 }
	
	@Override
	public String toString() {
		return "User [userId=" + userId + ", username=" + username + ", password=" + password + ", roleId=" + roleId + "]";
	}
    
}
