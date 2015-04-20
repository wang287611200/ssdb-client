package com.opensource.ssdb.command;

import java.util.Map;
import java.util.Set;

public interface SSDBCommands {

	public void set(String key,String value); 
	
	public void setx(String key,String value,int seconds);
	
	public Long setnx(String key,String value); 
	
	public String get(String key);

	public String getAndSet(String key,String value);
	
	public void del(String key);
	
	public long incr(String key,long integer);
	
	public Boolean exists(String key);
	
	public Set<String> keys(String start, String end, int limit);
	
	public Map<String,String> scan(String start, String end, int limit);
	
	public void multi_set(String ...keyAndValues);
	
	public Map<String,String> multi_get(String ...keys);
	
	public void multi_del(String ...keys);
	
	public void hset(String key,String field,String value);
	
}
