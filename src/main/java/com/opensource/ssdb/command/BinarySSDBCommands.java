package com.opensource.ssdb.command;

import java.util.Map;
import java.util.Set;

public interface BinarySSDBCommands {
	
	public String set(byte[] key, byte[] value);
	
	public String setx(byte[] key, byte[] value,int seconds);
	
	public Long   setnx(byte[] key, byte[] value);
	
	public byte[] get(byte[] key);
	
	public byte[] getAndSet(byte[] key, byte[] value);
	
	public void del(byte[] key);
	
	public Long incr( byte[] key,long integer);
	
	public Boolean exists(byte[] key);
	
	public Set<String> keys(byte[] start,byte[] end,int limit);
	
	public Map<String,String> scan(byte[] start,byte[] end,int limit);
	
	public void hset(byte[] key, byte[] field, byte[] value);
	
}
