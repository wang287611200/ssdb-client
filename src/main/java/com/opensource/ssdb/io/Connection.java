package com.opensource.ssdb.io;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

import com.opensource.ssdb.exception.SSDBConnectionException;
import com.opensource.ssdb.exception.SSDBDataException;
import com.opensource.ssdb.io.Protocol.Command;
import com.opensource.ssdb.util.SafeEncoder;

/**
 * 负责处理客户端与SSDB服务端socket连接
 * 
 * @author wangcheng
 *
 */
public class Connection implements Closeable {

	private String host;
	private int port = Protocol.DEFAULT_PORT;
	private int timeout = Protocol.DEFAULT_TIMEOUT;

	private Socket socket;
	private SSDBOutputStream outputStream;
	private SSDBInputStream inputStream;
	
	private boolean broken = false;

	// ----Constructor -----//
	public Connection() {
	}

	public Connection(final String host) {
		this.host = host;
	}

	public Connection(final String host, final int port) {
		this.host = host;
		this.port = port;
	}

	public Connection(final String host, final int port, final int timeout) {
		this.host = host;
		this.port = port;
		this.timeout = timeout;
	}

	public boolean isConnected() {
		return socket != null && socket.isBound() && !socket.isClosed()
				&& socket.isConnected() && !socket.isInputShutdown()
				&& !socket.isOutputShutdown();
	}

	public void connect() {
		if (!isConnected()) {
			try {
				socket = new Socket();
				// ->@wjw_add
				socket.setReuseAddress(true);
				socket.setKeepAlive(true); // Will monitor the TCP connection is
				// valid
				socket.setTcpNoDelay(true); // Socket buffer Whetherclosed, to
				// ensure timely delivery of data
				socket.setSoLinger(true, 0); // Control calls close () method,
				// the underlying socket is closed
				// immediately
				// <-@wjw_add
				socket.connect(new InetSocketAddress(host, port), timeout);
				socket.setSoTimeout(timeout);
				outputStream = new SSDBOutputStream(socket.getOutputStream());
				inputStream = new SSDBInputStream(socket.getInputStream());
			} catch (IOException ex) {
				broken = true;
				throw new SSDBConnectionException(ex);
			}
		}
	}

	public void disconnect() {
		if (isConnected()) {
			try {
				socket.getInputStream().close();
				if (!socket.isClosed()) {
					socket.getOutputStream().close();
					socket.close();
				}
			} catch (IOException ex) {
				broken = true;
				throw new SSDBConnectionException(ex);
			}
		}
	}

	public void close() throws IOException {
		disconnect();
	}

	protected Connection sendCommand(final Command cmd,final String ...args){
		final byte[][] bargs  =new byte[args.length][];
		for(int i=0;i<args.length;i++){
			bargs[i] = SafeEncoder.encode(args[i]);
		}
		return sendCommand(cmd, bargs);
	}
	protected Connection sendCommand(final Command cmd, final byte[]... args) {
		try {
			connect();
			Protocol.sendCommand(outputStream, cmd, args);
			return this;
		} catch (SSDBConnectionException ex) {
			broken = true;
			throw ex;
		}
	}

	protected Connection sendCommand(final Command cmd){
		try {
			connect();
			//TODO 发送不带带参数的请求命令
			return this;
		} catch (SSDBConnectionException ex) {
			broken = true;
			throw ex;
		}
	}
	
	public String getStatusCodeReply(){
		Response response=	this.getResponse();

		if(!response.isOk())
			throw new SSDBDataException(response.getErrorMessage());
		
		return response.getStatusCode();
	}
	
	public String getBulkReply() {
		final byte[] result = getBinaryBulkReply();
	    if (null != result) {
	      return SafeEncoder.encode(result);
	    } else {
	      return null;
	    }
	}
	public Long getIntegerReply() {
		final byte[] result = getBinaryBulkReply();
	    if (null != result) {
	      return Long.valueOf(SafeEncoder.encode(result));
	    } else {
	      return null;
	    }
	  }
	public byte[] getBinaryBulkReply() {
		Response response=	this.getResponse();
		if(response.isNotFound())
			return null;
		
		if(response.isOk()){
			if(response.getData().size() != 1){
				throw new SSDBConnectionException("Invalid response");
			}
			return response.getData().get(0);
		}
		throw new SSDBDataException(response.getErrorMessage());
	 }
	
	 public List<byte[]> getBinaryMultiBulkReply() {
		 Response response=	this.getResponse();
		 if(response.isNotFound())
				return null;
			if(response.isOk()){
				return response.getData();
			}
			throw new SSDBDataException(response.getErrorMessage());
		 
	 }
	public Response getResponse() {
	    try {
	      flush();
	      return new Response(Protocol.read(inputStream));
	    } catch (SSDBConnectionException ex) {
	      broken = true;
	      throw ex;
	    }
	  }
	protected void flush() {
		try {
			outputStream.flush();
		} catch (IOException e) {
			broken = true;
			throw new SSDBConnectionException(e);
		}
	}
	
	// --------Setter And Getter ------//
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public Socket getSocket() {
		return socket;
	}

	public boolean isBroken() {
		return broken;
	}

}
