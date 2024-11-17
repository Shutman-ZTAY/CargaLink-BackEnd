package com.ipn.mx.exeptions;

public class InvalidRequestExeption extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidRequestExeption(String message) {
        super(message);
    }

}
