package com.opensource.ssdb.exception;

/**
 * SSDB 连接相关异常
 * @author wangcheng
 *
 */
public class SSDBConnectionException extends SSDBException {

	private static final long serialVersionUID = -4549509032028312476L;

	public SSDBConnectionException(String message) {
		super(message);
	}

	public SSDBConnectionException(Throwable cause) {
		super(cause);
	}

	public SSDBConnectionException(String message, Throwable cause) {
		super(message, cause);
	}
}
