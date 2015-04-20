package com.opensource.ssdb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensource.ssdb.exception.SSDBConnectionException;
import com.opensource.ssdb.exception.SSDBDataException;
import com.opensource.ssdb.exception.SSDBException;
import com.opensource.ssdb.pool.SSDBPool;

public class SSDBTemplate {

	private static final Logger logger = LoggerFactory.getLogger(SSDBTemplate.class);
	
	private SSDBPool ssdbPool;
	
	public SSDBTemplate(final SSDBPool ssdbPool){
		this.ssdbPool = ssdbPool;
	}

	public SSDBTemplate(final String host,final int port){
		this.ssdbPool = new SSDBPool(host, port);
	}
	
	public <T> T execute(SSDBAction<T> ssdbAction )throws SSDBException{
		SSDB ssdb = null;
		boolean broken = false;
		try{
			ssdb = ssdbPool.getResource();
			return ssdbAction.action(ssdb);
		}catch(SSDBException e){
			broken = handleSSDBException(e);
			throw e;
		} finally {
			closeResource(ssdb, broken);
		}
	}
	
	public void execute(SSDBActionNoResult ssdbAction)throws SSDBException{
		SSDB ssdb = null;
		boolean broken = false;
		try{
			ssdb = ssdbPool.getResource();
			ssdbAction.action(ssdb);
		}catch(SSDBException e){
			broken = handleSSDBException(e);
			throw e;
		} finally {
			closeResource(ssdb, broken);
		}
	}
	
	/**
	 * Handle jedisException, write log and return whether the connection is broken.
	 */
	protected boolean handleSSDBException(SSDBException ssdbException) {
		if (ssdbException instanceof SSDBConnectionException) {
			logger.error("SSDB connection " + ssdbPool.getAddress() + " lost.", ssdbException);
		} else if (ssdbException instanceof SSDBDataException) {
			logger.error("SSDB connection " + ssdbPool.getAddress() + " data error", ssdbException);
		} else {
			logger.error("SSDB exception happen.", ssdbException);
		}
		return true;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Return jedis connection to the pool, call different return methods depends on the conectionBroken status.
	 */
	protected void closeResource(SSDB ssdb, boolean conectionBroken) {
		try {
			if (conectionBroken) {
				ssdbPool.returnBrokenResource(ssdb);
			} else {
				ssdbPool.returnResource(ssdb);
			}
		} catch (Exception e) {
			logger.error("return back jedis failed, will fore close the jedis.", e);
			destroyJedis(ssdb);
		}

	}
	/**
	 * 在Pool以外强行销毁Jedis.
	 */
	private  void destroyJedis(SSDB ssdb) {
		if ((ssdb != null) && ssdb.isConnected()) {
			try {
				ssdb.disconnect();
			} catch (Exception e) {
			}
		}
	}
	public interface SSDBAction<T>{
		T action(SSDB ssdb);
	}
	
	public interface SSDBActionNoResult{
		void action(SSDB ssdb);
	}
}
