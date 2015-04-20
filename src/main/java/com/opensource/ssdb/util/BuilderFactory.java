package com.opensource.ssdb.util;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BuilderFactory {

	 public static final Builder<Map<String,String>> STRING_MAP =new Builder<Map<String,String>>() {
		
		@SuppressWarnings("unchecked")
		public Map<String, String> build(Object data) {
			if (null == data) {
		        return null;
		      }
		      List<byte[]> l = (List<byte[]>) data;
		      final Map<String,String> result=new LinkedHashMap<String, String>(l.size()/2);
		      
		      for(int i=0;i<l.size();i+=2){
		    	  result.put(SafeEncoder.encode(l.get(i)), SafeEncoder.encode(l.get(i+1)));
		      }
		     
		      return result;
		    }
		

	    public String toString() {
	      return "Map<String,String>";
	    }
	 };
	
	 public static final Builder<Set<String>> STRING_SET = new Builder<Set<String>>() {
		    @SuppressWarnings("unchecked")
		    public Set<String> build(Object data) {
		      if (null == data) {
		        return null;
		      }
		      List<byte[]> l = (List<byte[]>) data;
		      final Set<String> result = new HashSet<String>(l.size());
		      for (final byte[] barray : l) {
		        if (barray == null) {
		          result.add(null);
		        } else {
		          result.add(SafeEncoder.encode(barray));
		        }
		      }
		      return result;
		    }

		    public String toString() {
		      return "Set<String>";
		    }

		  };
}
