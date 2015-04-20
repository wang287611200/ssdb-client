package com.opensource.ssdb_client;

import org.junit.Before;
import org.junit.Test;

import com.opensource.ssdb.SSDB;

public class HashMapCommandTest {

	private  SSDB ssdb;
	
	@Before
	public void init(){
		ssdb=new SSDB("10.211.55.8",8888);
	}
	
	@Test
	public void hsetTest(){
		ssdb.hset("maptest", "A", "a");
	}
}
