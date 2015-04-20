package com.opensource.ssdb.pool;

import com.opensource.ssdb.SSDB;
import com.opensource.ssdb.exception.SSDBException;
import com.opensource.ssdb.io.Protocol;
import com.opensource.ssdb.util.ConnectionInfo;
import com.opensource.ssdb.util.HostAndPort;

public class SSDBPool extends Pool<SSDB> {

	private HostAndPort hostAndPort;

	private ConnectionInfo connectionInfo;

	public SSDBPool(final String host,final int port){
		this(new SSDBPoolConfig(),host,port,Protocol.DEFAULT_TIMEOUT,null);
		this.hostAndPort = new HostAndPort(host, port);
		this.connectionInfo = new ConnectionInfo();
	}
	
	
	public SSDBPool(final SSDBPoolConfig poolConfig, final String host,
			final int port, final int timeout, final String password) {
		super(poolConfig, new SSDBFactory(host, port, timeout, password));
		this.hostAndPort = new HostAndPort(host, port);
		this.connectionInfo = new ConnectionInfo(timeout, password);
	}

	public SSDBPool(final SSDBPoolConfig poolConfig, final String host,
			final int port, final int timeout) {
		super(poolConfig, new SSDBFactory(host, port, timeout));
		this.hostAndPort = new HostAndPort(host, port);
		this.connectionInfo = new ConnectionInfo(timeout);
	}

	public SSDBPool(final SSDBPoolConfig ssdbPoolConfig,
			final HostAndPort hostAndPort, final ConnectionInfo connectionInfo) {
		this(ssdbPoolConfig, hostAndPort.getHost(), hostAndPort.getPort(),
				connectionInfo.getTimeout(), connectionInfo.getPassword());
		this.hostAndPort = hostAndPort;
		this.connectionInfo = connectionInfo;
	}

	@Override
	public SSDB getResource() {
		SSDB ssdb = super.getResource();
		ssdb.setDataSource(this);
		return ssdb;
	}

	public void returnBrokenResource(final SSDB resource) {
		if (resource != null) {
			returnBrokenResourceObject(resource);
		}
	}

	public void returnResource(final SSDB resource) {
		if (resource != null) {
			try {
				returnResourceObject(resource);
			} catch (Exception e) {
				returnBrokenResource(resource);
				throw new SSDBException(
						"Could not return the resource to the pool", e);
			}
		}
	}

	public int getNumActive() {
		if (this.internalPool == null || this.internalPool.isClosed()) {
			return -1;
		}

		return this.internalPool.getNumActive();
	}

	public HostAndPort getAddress() {
		return hostAndPort;
	}

	public void setHostAndPort(HostAndPort hostAndPort) {
		this.hostAndPort = hostAndPort;
	}

	public ConnectionInfo getConnectionInfo() {
		return connectionInfo;
	}

	public void setConnectionInfo(ConnectionInfo connectionInfo) {
		this.connectionInfo = connectionInfo;
	}
	
}
