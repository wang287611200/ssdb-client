package com.opensource.ssdb_client;


import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.opensource.ssdb.SSDB;

public class SSDBTest {
	
	private static SSDB ssdb = null;
	
	private static final int seconds = 2;
	static{
		ssdb=new SSDB("10.211.55.8",8888);
	}
	@Before
	public void init(){
		ssdb.del("hello");
	}
	
	@Test
	public void setAndGetTest(){
		ssdb.set("hello", "world");
		Assert.assertEquals(ssdb.get("hello"), "world");
	}
	@Test
	public void delAndGetTest(){
		ssdb.del("hello");
		Assert.assertEquals(ssdb.get("hello"), null);
	}
	@Test
	public void setxAndGetTest() throws Exception{
		ssdb.setx("hello", "world", seconds);
		Assert.assertEquals(ssdb.get("hello"), "world");
		Thread.sleep(seconds*1000);
		Assert.assertEquals(ssdb.get("hello"), null);
	}
	@Test
	public void setnxAdnGetTest(){
		long state=ssdb.setnx("hello", "world");
		Assert.assertEquals(state, 1L);
		state=ssdb.setnx("hello", "world");
		Assert.assertEquals(state, 0L);
	}
	
	@Test
	public void getAndSetTest(){
		String data = ssdb.getAndSet("hello", "world");
		Assert.assertEquals(data,null);
		data = ssdb.getAndSet("hello", "world2");
		//before set
		Assert.assertEquals(data,"world");
		//after set
		Assert.assertEquals(ssdb.get("hello"), "world2");
	}
	
	@Test
	public void incrTest(){
		long data = ssdb.incr("hello", 10);
		Assert.assertEquals(data,10);
		data = ssdb.incr("hello", -10);
		Assert.assertEquals(data,0);
	}
	
	@Test
	public void existsTest(){
		Assert.assertEquals(ssdb.exists("hello"),false);
		ssdb.set("hello", "world");
		Assert.assertEquals(ssdb.exists("hello"),true);
	}
	@Test
	public void keysTest(){
		Set<String> set=ssdb.keys("he", "hz", 10);
		for (String key : set) {
			System.out.println(key);
		}
	}
	@Test
	public void scanTest(){
		int size =30;
		Map<String,String> map= ssdb.scan("","",10);
		System.out.println(StringUtils.rightPad("Key",size)+"Value");
		System.out.println("---------------------------------------");
		for (Entry<String, String> entry:map.entrySet()) {
			System.out.println(StringUtils.rightPad(entry.getKey() , size)+":  " +entry.getValue());
		}
	}
	@Test
	public void multi_setTest(){
		ssdb.multi_set("A","a","B","b","C","d");
	}
	
	@Test
	public void multi_getTest(){
		int size =30;
		Map<String,String> map= ssdb.multi_get("A","B","C");
		System.out.println(StringUtils.rightPad("Key",size)+"Value");
		System.out.println("---------------------------------------");
		for (Entry<String, String> entry:map.entrySet()) {
			System.out.println(StringUtils.rightPad(entry.getKey() , size)+":  " +entry.getValue());
		}
	}
	
	@Test
	public void multi_delTest(){
		ssdb.multi_del("A","B","C");
	}
	
	public static void main(String[] args) throws Exception {
		SSDB ssdb=new SSDB("10.211.55.8",8888);
		System.out.println("before set hello:"+ ssdb.get("hello"));
		ssdb.set("hello", "world1");
		System.out.println("after set hello:"+ ssdb.get("hello"));
		ssdb.del("hello");
		System.out.println("after del hello:"+ ssdb.get("hello"));
		ssdb.setx("test", "hello", 10);
		System.out.println("before setx test:"+ssdb.get("test"));
		Thread.sleep(10000);
		System.out.println("after setx  test:"+ssdb.get("test"));
		System.out.println("first setnx:"+ssdb.setnx("test2", "hello"));
		System.out.println("second setnx:"+ssdb.setnx("test2", "hello"));
//		System.out.println(ssdb);
//		Thread.sleep(100000);
//		byte b='0';
//		System.out.println("aa:" + (long)(b-'0'));
//		System.out.println("12".getBytes());
	}
}
