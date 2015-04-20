package com.opensource.ssdb.util;

import java.io.UnsupportedEncodingException;

import com.opensource.ssdb.exception.SSDBDataException;
import com.opensource.ssdb.exception.SSDBException;
import com.opensource.ssdb.io.Protocol;

public class SafeEncoder {

	public static byte[][] encodeMany(final String... strs) {
	    byte[][] many = new byte[strs.length][];
	    for (int i = 0; i < strs.length; i++) {
	      many[i] = encode(strs[i]);
	    }
	    return many;
	  }

	  public static byte[] encode(final String str) {
	    try {
	      if (str == null) {
	        throw new SSDBDataException("value sent to ssdb cannot be null");
	      }
	      return str.getBytes(Protocol.CHARSET);
	    } catch (UnsupportedEncodingException e) {
	      throw new SSDBException(e);
	    }
	  }

	  public static String encode(final byte[] data) {
	    try {
	      return new String(data, Protocol.CHARSET);
	    } catch (UnsupportedEncodingException e) {
	      throw new SSDBException(e);
	    }
	  }
}
