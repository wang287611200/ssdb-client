package com.opensource.ssdb;

import java.util.Map;
import java.util.Set;

import com.opensource.ssdb.command.BasicCommands;
import com.opensource.ssdb.command.SSDBCommands;
import com.opensource.ssdb.pool.Pool;
import com.opensource.ssdb.util.BuilderFactory;
import com.opensource.ssdb.util.SafeEncoder;

public class SSDB extends BinarySSDB implements SSDBCommands, BasicCommands {

	protected Pool<SSDB> dataSource = null;

	public SSDB(String host) {
		super(host);
	}

	public SSDB(String host, int port) {
		super(host, port);
	}

	public SSDB(String host, int port, int timeout) {
		super(host, port, timeout);
	}

	public SSDB(String host, int port, int timeout, String password) {
		super(host, port, timeout, password);
	}

	public void set(String key, String value) {
		set(SafeEncoder.encode(key), SafeEncoder.encode(value));
	}

	public void setx(String key, String value, int seconds) {
		setx(SafeEncoder.encode(key), SafeEncoder.encode(value), seconds);
	}

	public Long setnx(String key, String value) {
		return setnx(SafeEncoder.encode(key), SafeEncoder.encode(value));
	}

	public String get(String key) {
		byte[] data = get(SafeEncoder.encode(key));
		return null != data ? SafeEncoder.encode(data) : null;
	}

	public String getAndSet(String key, String value) {
		byte[] data = getAndSet(SafeEncoder.encode(key),
				SafeEncoder.encode(value));
		return null != data ? SafeEncoder.encode(data) : null;
	}

	public void del(String key) {
		del(SafeEncoder.encode(key));
	}

	public long incr(String key, long integer) {
		return incr(SafeEncoder.encode(key), integer);
	}

	public Boolean exists(String key) {
		return exists(SafeEncoder.encode(key));
	}

	public Set<String> keys(String start, String end, int limit) {
		return keys(SafeEncoder.encode(start), SafeEncoder.encode(end), limit);
	}

	public Map<String, String> scan(String start, String end, int limit) {
		return scan(SafeEncoder.encode(start), SafeEncoder.encode(end), limit);
	}

	public void multi_set(String... keyAndValues) {
		client.multi_set(keyAndValues);
		client.getStatusCodeReply();
	}

	public Map<String, String> multi_get(String... keys) {
		client.multi_get(keys);
		return BuilderFactory.STRING_MAP
				.build(client.getBinaryMultiBulkReply());
	}

	public void multi_del(String... keys) {
		client.multi_del(keys);
		client.getStatusCodeReply();
	}

	public void hset(String key, String field, String value) {
		hset(SafeEncoder.encode(key), SafeEncoder.encode(field),
				SafeEncoder.encode(value));
	}

	public void setDataSource(Pool<SSDB> ssdbPool) {
		this.dataSource = ssdbPool;
	}
}
