package com.opensource.ssdb.io;

import com.opensource.ssdb.exception.SSDBDataException;
import com.opensource.ssdb.io.Protocol.Command;


public class Client extends BinaryClient  {

	//-----Constructor---//
	public Client(final String host){
		super(host);
	}
	public Client(final String host,final int port){
		super(host, port);
	}
	public Client(final String host,final int port,final int timeout){
		super(host, port,timeout);
	}
	public Client(final String host,final int port,final int timeout,final String password){
		super(host, port,timeout,password);
	}
	
	public void multi_set(String... keyAndValues) {
		if(keyAndValues.length % 2 != 0){
			throw new SSDBDataException("Invalid arguments count");
		}
		sendCommand(Command.MULTI_SET,keyAndValues);
	}
	public void multi_get(String... keys) {
		sendCommand(Command.MULTI_GET,keys);
	}
	public void multi_del(String... keys) {
		sendCommand(Command.MULTI_DEL,keys);
	}
}
