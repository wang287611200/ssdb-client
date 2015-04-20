package com.opensource.ssdb;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import com.opensource.ssdb.command.BasicCommands;
import com.opensource.ssdb.command.BinarySSDBCommands;
import com.opensource.ssdb.exception.SSDBDataException;
import com.opensource.ssdb.io.Client;
import com.opensource.ssdb.io.Response;
import com.opensource.ssdb.util.BuilderFactory;

public class BinarySSDB implements BinarySSDBCommands, BasicCommands, Closeable {

	protected Client client = null;

	// ----Constructor----//
	public BinarySSDB(final String host) {
		client = new Client(host);
	}

	public BinarySSDB(final String host, final int port) {
		client = new Client(host, port);
	}

	public BinarySSDB(final String host, final int port, final int timeout) {
		client = new Client(host, port, timeout);
	}

	public BinarySSDB(final String host, final int port, final int timeout,
			final String password) {
		client = new Client(host, port, timeout, password);
	}

	public String info() {
		return null;
	}

	public String set(byte[] key, byte[] value) {
		client.set(key, value);
		return client.getStatusCodeReply();

	}

	public String setx(byte[] key, byte[] value, int seconds) {
		client.setx(key, value, seconds);
		return client.getStatusCodeReply();
	}

	public Long setnx(byte[] key, byte[] value) {
		client.setnx(key, value);
		return client.getIntegerReply();
	}

	public byte[] get(byte[] key) {
		client.get(key);
		return client.getBinaryBulkReply();
	}

	@Override
	public byte[] getAndSet(byte[] key, byte[] value) {
		client.getAndSet(key, value);
		return client.getBinaryBulkReply();
	}

	public void del(byte[] key) {
		client.del(key);
		Response response = client.getResponse();
		if (!response.isOk())
			throw new SSDBDataException(response.getErrorMessage());
	}

	public Long incr(byte[] key, long integer) {
		client.incr(key, integer);
		return client.getIntegerReply();
	}

	public Boolean exists(byte[] key) {
		client.exists(key);
		return client.getIntegerReply() == 1;
	}

	public Set<String> keys(byte[] start, byte[] end, int limit) {
		client.keys(start, end, limit);
		return BuilderFactory.STRING_SET
				.build(client.getBinaryMultiBulkReply());
	}

	public Map<String, String> scan(byte[] start, byte[] end, int limit) {
		client.scan(start, end, limit);

		return BuilderFactory.STRING_MAP
				.build(client.getBinaryMultiBulkReply());
	}

	public void hset(byte[] key, byte[] field, byte[] value) {
		client.hset(key, field, value);
		client.getStatusCodeReply();
	}

	public String auth(String password) {
		client.auth(password);
		return client.getStatusCodeReply();
	}

	public void connect() {
		client.connect();
	}

	public void disconnect() {
		client.disconnect();
	}

	public boolean isConnected() {
		return client.isConnected();
	}

	public void close() throws IOException {
		client.close();
	}

	public Client getClient() {
		return client;
	}
	
	

}
