package com.opensource.ssdb.exception;

public class SSDBDataException extends SSDBException {

	private static final long serialVersionUID = -811707219441298776L;

	public SSDBDataException(String message) {
		super(message);
	}

	public SSDBDataException(Throwable cause) {
		super(cause);
	}

	public SSDBDataException(String message, Throwable cause) {
		super(message, cause);
	}
}
