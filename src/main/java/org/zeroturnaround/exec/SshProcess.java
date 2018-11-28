package org.zeroturnaround.exec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSchException;

public class SshProcess extends Process {
	final Channel channel;
	OutputStream out_stream;
	InputStream in_stream;
	InputStream err_stream;
	
	public SshProcess(Channel channel) throws IOException {
		this.channel = channel;
		out_stream = channel.getOutputStream();
		in_stream = channel.getInputStream();
		err_stream = channel.getExtInputStream();
	}
	
	public void connect() throws JSchException {
		channel.connect();
	}

	@Override
	public OutputStream getOutputStream() {
		return out_stream;
	}

	@Override
	public InputStream getInputStream() {
		return in_stream;		
	}

	@Override
	public InputStream getErrorStream() {
		return err_stream;
	}

	@Override
	public int waitFor() throws InterruptedException {
		while (true) {
			if (channel.isClosed()) {
				break;
			}
			Thread.sleep(100);
		}
		destroy();
		return exitValue();
	}

	@Override
	public int exitValue() {
		return channel.getExitStatus();
	}

	@Override
	public void destroy() {
		channel.disconnect();
		try {
			
			channel.getSession().disconnect();
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
