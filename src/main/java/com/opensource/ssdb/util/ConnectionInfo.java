package com.opensource.ssdb.util;

import com.opensource.ssdb.io.Protocol;

public class ConnectionInfo {

	private static final String DEFAULT_PASSWORD = null;
	
	private String password = DEFAULT_PASSWORD;
	private int timeout = Protocol.DEFAULT_TIMEOUT;
	
	public ConnectionInfo() {
	}
	public ConnectionInfo(final int timeout){
		this.timeout = timeout;
	}
	public ConnectionInfo(final int timeout,final String password) {
		this.timeout = timeout;
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
}
