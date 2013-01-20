package com.csun.bookboi.utils;

public class ValidationUtil {
	
	/**
	 * Non-construct object
	 */
	private ValidationUtil() {
		
	}
	
	public static boolean isValidEmail(String email) {
		return email.contains("@");
	}
}
