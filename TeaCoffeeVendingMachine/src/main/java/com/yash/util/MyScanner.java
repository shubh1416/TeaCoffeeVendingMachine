package com.yash.util;

import java.util.Scanner;

public class MyScanner {
	private final Scanner scanner;

	public MyScanner() {
		this.scanner = new Scanner(System.in);
	}

	public int nextInt() {

		return scanner.nextInt();

	}
}
