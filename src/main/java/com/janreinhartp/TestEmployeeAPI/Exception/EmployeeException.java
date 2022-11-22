package com.janreinhartp.TestEmployeeAPI.Exception;

public class EmployeeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String INVALID_NAME = "ERROR : Invalid Name - Name is null";
	public static final String SHORT_NAME = "ERROR : Invalid Name - Name is too short";
	public static final String LONG_NAME = "ERROR : Invalid Name - Name is too long";
	public static final String INVALID_CHAR_NAME = "ERROR : Invalid Name - Invalid Character in name";
	public static final String INVALID_EMAIL = "ERROR : Invalid Email";
	public static final String INVALID_NUMBER = "ERROR : Invalid Number";
	public static final String INVALID_AGE = "ERROR : Age must be 10 to 100";
	public static final String INVALID_GENDER = "ERROR : Invalid Gender";

	public EmployeeException() {

	}

	public EmployeeException(String message) {
		super(message, null, false, false);
	}
}
