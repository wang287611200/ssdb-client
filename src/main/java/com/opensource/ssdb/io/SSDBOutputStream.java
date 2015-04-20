package com.opensource.ssdb.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.opensource.ssdb.util.SafeEncoder;

public class SSDBOutputStream extends FilterOutputStream{

	protected final byte buffer[];
	
	protected int count;
	
	public SSDBOutputStream(OutputStream out) {
		this(out,8192);
	}
	
	public SSDBOutputStream(OutputStream out,final int capacity) {
		super(out);
		if(capacity <= 0 ){
			throw new IllegalArgumentException("Buffer capacity <= 0");
		}
		buffer = new byte[capacity];
	}
	
	public void writeIntLf(Integer i) throws IOException{
		this.write(i.toString());
		writeLf();
	}
	public void writeLf() throws IOException{
		if(1 >= buffer.length){
			flushBuffer();
		}
		buffer[count++] = '\n';
	}
	
	public void write(String s) throws IOException{
		this.write(SafeEncoder.encode(s));
	}
	public void writeLf(final byte[] b) throws IOException{
		write(b,0,b.length);
		writeLf();
	}
	public void write(final byte[] b) throws IOException{
		write(b,0,b.length);
	}
	
	public void write(final byte[] b,final int off,final int len)throws IOException {
		if(len >= buffer.length){
			//若内容大于缓冲区时，先flush掉缓冲区，然后直接写入输出流
			flushBuffer();
			out.write(b, off, len);
		}else{
			if(len >= buffer.length - count){
				//若内容大于缓冲区剩余空间时，先flush掉缓冲区，然后写入缓冲区
				flushBuffer();
			}
			System.arraycopy(b, off, buffer, count, len);
			count +=len;
		}
	}

	private void flushBuffer() throws IOException {
		if(count > 0){
			out.write(buffer,0,count);
			count=0;
		}
	}
	
	public void flush()throws IOException{
		flushBuffer();
		out.flush();
	}

}
