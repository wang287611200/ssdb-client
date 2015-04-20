package com.opensource.ssdb.command;
/**
 * 与连接相关的SSDB 命令接口
 * @author wangcheng
 *
 */
public interface BasicCommands {

	public String info();
	
	public String auth(String password);
}
