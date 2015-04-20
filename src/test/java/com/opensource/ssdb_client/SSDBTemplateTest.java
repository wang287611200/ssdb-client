package com.opensource.ssdb_client;

import com.opensource.ssdb.SSDB;
import com.opensource.ssdb.SSDBTemplate;
import com.opensource.ssdb.SSDBTemplate.SSDBAction;
import com.opensource.ssdb.pool.SSDBPool;

public class SSDBTemplateTest {

	public static void main(String[] args) {
		SSDBPool ssdbPool=new SSDBPool("10.211.55.8",8888);
		
		SSDBTemplate ssdbTemplate =new SSDBTemplate(ssdbPool);
		
		
		String value=ssdbTemplate.execute(new SSDBAction<String>() {
			public String action(SSDB ssdb) {
				return ssdb.get("hello");
			}
		});
		System.out.println(value);
		
	}
}
