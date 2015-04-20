package com.opensource.ssdb.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.opensource.ssdb.exception.SSDBConnectionException;

public class SSDBInputStream extends FilterInputStream {

	protected final byte buffer[];

	protected int count, limit;

	public SSDBInputStream(InputStream in, int capacity) {
		super(in);
		if (capacity <= 0) {
			throw new IllegalArgumentException("buffer capacity <= 0");
		}
		buffer = new byte[capacity];
	}

	protected SSDBInputStream(InputStream in) {
		this(in, 8192);
	}

	public int read(byte[] b, int off, int len) throws SSDBConnectionException {
		ensureFill();
		final int length = Math.min(limit - count, len);
		System.arraycopy(buffer, count, b, off, length);
		count += length;
		return length;
	}

	public byte readByte() throws SSDBConnectionException {
		ensureFill();
		return buffer[count++];
	}

	/**
	 * This methods assumes there are required bytes to be read. If we cannot
	 * read anymore bytes an exception is thrown to quickly ascertain that the
	 * stream was smaller than expected.
	 */
	private void ensureFill() throws SSDBConnectionException {
		if (count >= limit) {
			try {
				limit = in.read(buffer);
				count = 0;
				if (limit == -1) {
					throw new SSDBConnectionException(
							"Unexpected end of stream.");
				}
			} catch (IOException e) {
				throw new SSDBConnectionException(e);
			}
		}
	}

	public int readIntLf() {
		return (int) readLongLf();
	}

	public long readLongLf() {
		final byte[] buffer = this.buffer;

		long value = 0;
		while (true) {
			ensureFill();

			final int b = buffer[count++];
			if (b == '\n') {
				break;
			} else {
				value = value * 10 + b - '0';
			}
		}
		return value;

	}

}
