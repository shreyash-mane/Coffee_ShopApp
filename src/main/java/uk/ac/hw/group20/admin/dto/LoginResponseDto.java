package uk.ac.hw.group20.admin.dto;

public class LoginResponseDto {
	String status;
	String message;
	String userId;

	public LoginResponseDto() {
		this.status = "N";
		this.message = null;
		this.userId = null;
	}

	public LoginResponseDto(String status, String message, String userId) {
		this.status = status;
		this.message = message;
		this.userId = userId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "LoginResponseDto [status=" + status + ", message=" + message + ", userId=" + userId + "]";
	}

}
