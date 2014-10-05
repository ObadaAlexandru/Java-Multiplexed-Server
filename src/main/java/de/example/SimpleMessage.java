package de.example;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import de.mtws.connection.api.Message;

public class SimpleMessage implements Message {
	
	private String data;
	
	public SimpleMessage(String data) {
		this.data = data + "\n";
	}

	@Override
	public byte[] toByteArray() {
		return data.getBytes(Charset.defaultCharset());
	}

	@Override
	public ByteBuffer toByteBuffer() {
		return ByteBuffer.wrap(toByteArray());
	}
	
	@Override
	public String toString() {
		return data;
	}

}
