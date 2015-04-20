package com.opensource.ssdb.pool;

import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import com.opensource.ssdb.BinarySSDB;
import com.opensource.ssdb.SSDB;
import com.opensource.ssdb.util.HostAndPort;

public class SSDBFactory implements PooledObjectFactory<SSDB>{

	private final AtomicReference<HostAndPort> hostAndPort = new AtomicReference<HostAndPort>();
	private final int timeout;
	private final String password;
	
	public SSDBFactory(final String host,final int port,final int timeout){
		this(host,port,timeout,null);
	}
	public SSDBFactory(final String host,final int port,final int timeout,final String password) {
		this.hostAndPort.set(new HostAndPort(host, port));
		this.timeout = timeout;
		this.password = password;
	}
	
	public PooledObject<SSDB> makeObject() throws Exception {
		final HostAndPort hostAndPort = this.hostAndPort.get();
		final SSDB ssdb = new SSDB(hostAndPort.getHost(), hostAndPort.getPort(), this.timeout); 
		ssdb.connect();
		if(null != this.password){
			ssdb.auth(password);
		}
		
		return new DefaultPooledObject<SSDB>(ssdb);
	}

	public void destroyObject(PooledObject<SSDB> pooledObject) throws Exception {
		final SSDB ssdb=pooledObject.getObject();
		if(ssdb.isConnected()){
			try{
				ssdb.disconnect();
			}catch(Exception e){
			}
		}
	}

	public boolean validateObject(PooledObject<SSDB> pooledObject) {
		final BinarySSDB ssdb = pooledObject.getObject();
		
		try{
			HostAndPort hostAndPort = this.hostAndPort.get();
			
			String connectionHost = ssdb.getClient().getHost();
			int connectionPort = ssdb.getClient().getPort();
			
			 return hostAndPort.getHost().equals(connectionHost)
			          && hostAndPort.getPort() == connectionPort && ssdb.isConnected();
			
		}catch(Exception e){
			return false;
		}
	}

	public void activateObject(PooledObject<SSDB> pooledObject) throws Exception {
		
	}
	public void passivateObject(PooledObject<SSDB> pooledObject) throws Exception {
		
	}

}
