package com.ssja.lms;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Test {

	public static void main(String[] args) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode("1234");

		System.out.println(hashedPassword);
		System.out.println(passwordEncoder.encode("1234"));
		
	}
}
