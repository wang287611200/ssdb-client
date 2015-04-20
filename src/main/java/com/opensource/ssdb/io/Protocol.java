package com.opensource.ssdb.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.opensource.ssdb.exception.SSDBConnectionException;
import com.opensource.ssdb.util.SafeEncoder;

public class Protocol {

	public static final int DEFAULT_PORT = 8888;

	public static final int DEFAULT_TIMEOUT = 2000;

	public static final String CHARSET = "UTF-8";

	private Protocol() {

	}

	public static void sendCommand(final SSDBOutputStream os,
			final Command command, final byte[]... args) {
		sendCommand(os, command.raw, args);
	}

	private static void sendCommand(final SSDBOutputStream os,
			final byte[] command, final byte[]... args) {
		try {
			os.writeIntLf(command.length);
			os.writeLf(command);
			for (byte[] arg : args) {
				os.writeIntLf(arg.length);
				os.writeLf(arg);
			}
			os.writeLf();
		} catch (IOException e) {
			throw new SSDBConnectionException(e);
		}
	}

	public static List<byte[]> read(final SSDBInputStream is) {
		return process(is);
	}

	private static List<byte[]> process(final SSDBInputStream is) {
		List<byte[]> result = new ArrayList<byte[]>();
		while (true) {
			final int len = is.readIntLf();
			if (0 == len)
				break;
			final byte[] read = new byte[len];
			int offset = 0;
			while (offset < len) {
				final int size = is.read(read, offset, (len - offset));
				if (size == -1)
					throw new SSDBConnectionException(
							"It seems like server has closed the connection.");
				offset += size;
			}
			is.readByte();// read '\n'
			result.add(read);
		}
		return result;
	}

	public static final byte[] toByteArray(final boolean value) {
		return toByteArray(value ? 1 : 0);
	}

	public static final byte[] toByteArray(final int value) {
		return SafeEncoder.encode(String.valueOf(value));
	}

	public static final byte[] toByteArray(final long value) {
		return SafeEncoder.encode(String.valueOf(value));
	}

	public static final byte[] toByteArray(final double value) {
		return SafeEncoder.encode(String.valueOf(value));
	}

	public static enum Command {
		//Simple Command
		EXISTS("exists"),AUTH("auth"),
		//String Command
		SET("set"),SETX("setx"),SETNX("setnx"),GET("get"),GETSET("getset"),DEL("del"),INCR("incr"),KEYS("keys"),SCAN("scan"),MULTI_SET("multi_set"),MULTI_GET("multi_get"),MULTI_DEL("multi_del"),
		//HashMap Command
		HSET("hset"),HGET("hget"),HDEL("hdel"),HINCR("hincr"),HEXISTS("hexists"),HSIZE("hsize"),HLIST("hlist"),HRLIST("hrlist"),HKEYS("hkeys"),HGETALL("hgetall"),HSCAN("hscan"),HCLEAR("hclear"),MULTI_HSET("multi_hset"),MULTI_HGET("multi_hget"),MULTI_HDEL("multi_hdel"),
		;

		private final String name;

		public final byte[] raw;

		private Command(final String name) {
			this.name = name;
			raw = SafeEncoder.encode(this.name);
		}
	}
}
