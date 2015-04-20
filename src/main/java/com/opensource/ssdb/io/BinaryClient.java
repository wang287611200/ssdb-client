package com.opensource.ssdb.io;

import com.opensource.ssdb.io.Protocol.Command;

public class BinaryClient extends Connection{
	
	private String password;
	
	//-----Constructor----//
	public BinaryClient() {
	}
	
	public BinaryClient(final String host){
		super(host);
	}
	
	public BinaryClient(final String host,final int port){
		super(host,port);
	}
	public BinaryClient(final String host,final int port,final int timeout){
		super(host,port,timeout);
	}
	
	public BinaryClient(final String host,final int port,final int timeout,String password){
		super(host,port,timeout);
		this.password = password;
	} 

	public void connect() {
		if(!isConnected()){
			super.connect();
			if(null != password){
				auth(password);
		        getStatusCodeReply();
			}
		}
	}
	
	public void auth(final String password){
		setPassword(password);
		sendCommand(Command.AUTH,password);
	}
	public void set(byte[] key, byte[] value) {
		sendCommand(Command.SET, key,value);
	}
	public void setx(byte[] key, byte[] value, int seconds) {
		sendCommand(Command.SETX, key,value,Protocol.toByteArray(seconds)); 	
	}
	
	public void setnx(final byte[] key, final byte[] value) {
		sendCommand(Command.SETNX, key, value);
	}
	public void get(byte[] key) {
		sendCommand(Command.GET, key);
	}
	public void getAndSet(byte[] key, byte[] value){
		sendCommand(Command.GETSET,key,value);
	}
	public void del(byte[] key){
		sendCommand(Command.DEL, key);
	}
	public void incr(byte[] key,long integer){
		sendCommand(Command.INCR,key,Protocol.toByteArray(integer));
	}
	public void exists(byte[] key){
		sendCommand(Command.EXISTS,key);
	}
	public void keys(byte[] start,byte[] end,int limit){
		sendCommand(Command.KEYS,start,end,Protocol.toByteArray(limit));
	}
	public void scan(byte[] start,byte[] end,int limit){
		sendCommand(Command.SCAN,start,end,Protocol.toByteArray(limit));
	}
	public void hset(byte[] key, byte[] field, byte[] value){
		sendCommand(Command.HSET,key,field,value);
	}

	public void setPassword(String password) {
		this.password = password;
	} 
	
	
	
	
}
