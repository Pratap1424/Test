package com.cap.payload;

public class RegisterRequest {
	
	
	    private String firstName;
	    private String lastName;
	    private String email;
	    private String mobileNumber;
	    private String password;

	    public RegisterRequest() {}

	    public RegisterRequest(String firstName, String lastName, String email, String mobileNumber, String password) {
	        this.firstName = firstName;
	        this.lastName = lastName;
	        this.email = email;
	        this.mobileNumber = mobileNumber;
	        this.password = password;
	    }

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getMobileNumber() {
			return mobileNumber;
		}

		public void setMobileNumber(String mobileNumber) {
			this.mobileNumber = mobileNumber;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

}
