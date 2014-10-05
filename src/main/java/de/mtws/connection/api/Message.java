package de.mtws.connection.api;

import java.nio.ByteBuffer;

/**
 * defines a contract for a data wrapper this objects will allow us to have a
 * generic server which is able to send any kind of data
 * 
 * @author Alexandru Obada
 * 
 * @param <T>
 */

public interface Message {

	public byte[] toByteArray();

	public ByteBuffer toByteBuffer();
}
