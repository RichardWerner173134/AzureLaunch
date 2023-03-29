package com.werner.bl.exception;

public class InvalidArgumentsException extends Exception {
	public InvalidArgumentsException(String argument) {
		super(argument);
	}
}
