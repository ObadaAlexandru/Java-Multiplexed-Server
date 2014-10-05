package de.mtws.connection.api;

import java.nio.ByteBuffer;

/**
 * Defines a contract for the communication protocol It's purpose is to convert
 * raw data in meaningful for the client data
 * 
 * 
 * @author Alexandru Obada
 * 
 */
public interface Protocol {

	/**
	 * Converts from byte buffer to meaningful to the client data
	 * 
	 * @param buff
	 * @return
	 */
	public Message convert(ByteBuffer buff);

}
