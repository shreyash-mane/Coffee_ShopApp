package uk.ac.hw.group20.customer.model;

import uk.ac.hw.group20.utils.CustomerType;

public class Customer {
	public String userId;
	public String firstName;
	public String middleName;
	public String lastName;
	public String postCode;
	public String address;
	public String email;
	public String phone;
	public CustomerType customerType;

	public Customer(String userId, String firstName, String middleName, String lastName, String postCode,
			String address, String email, String phone, CustomerType customerType) {
		this.userId = userId;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.postCode = postCode;
		this.address = address;
		this.email = email;
		this.phone = phone;
		this.customerType = customerType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public CustomerType getCustomerType() {
		return customerType;
	}

	public void setCustomerType(CustomerType customerType) {
		this.customerType = customerType;
	}

	@Override
	public String toString() {
		return "Customer [userId=" + userId + ", firstName=" + firstName + ", middleName=" + middleName + ", lastName="
				+ lastName + ", postCode=" + postCode + ", address=" + address + ", email=" + email + ", phone=" + phone
				+ ", customerType=" + customerType + "]";
	}

}
