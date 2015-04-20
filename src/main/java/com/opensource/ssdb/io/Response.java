package com.opensource.ssdb.io;

import java.io.Serializable;
import java.util.List;

import com.opensource.ssdb.util.SafeEncoder;

public class Response implements Serializable{

	private static final long serialVersionUID = 1712198157059135112L;
	
	//ok, not_found, error, fail, client_error
	public static final String OK = "ok";
	
	public static final String NOT_FOUND = "not_found";
	
	public static final String ERROR = "error";
	
	public static final String FAIL = "fail";
	
	public static final String CLIENT_ERROR = "client_error";
	
	private List<byte[]> result;
	
	public Response(List<byte[]> result) {
		this.result = result;
	}
	
	public String getStatusCode(){
		if(null!= result && result.size() >= 1){
			return SafeEncoder.encode(result.get(0));}
		else
			return null;
	}
	
	public boolean  isOk(){
		return OK.equals(getStatusCode());
	}
	
	public boolean  isNotFound(){
		return NOT_FOUND.equals(getStatusCode());
	}
	
	public String getErrorMessage(){
		if(!isOk()){
			StringBuilder sb=new StringBuilder();
			sb.append(SafeEncoder.encode(result.get(0)));
			if(result.size()>1){
				sb.append(":");
				sb.append(SafeEncoder.encode(result.get(1)));
			}
			return sb.toString();
		}
		return null;
	}
	
	public List<byte[]> getData(){
		if(isOk()){
			return result.subList(1, result.size());
		}
		return null;
	}
	
}
