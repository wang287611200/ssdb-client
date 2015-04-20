package com.opensource.ssdb.exception;

public class SSDBException extends RuntimeException {

	private static final long serialVersionUID = -5119301338145674481L;

	public SSDBException(String message) {
		super(message);
	}

	public SSDBException(Throwable e) {
		super(e);
	}

	public SSDBException(String message, Throwable cause) {
		super(message, cause);
	}
}
