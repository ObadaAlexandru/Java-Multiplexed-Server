package de.mtws.connection.http.api;

import de.mtws.connection.api.Message;

/**
 * Defines a contract for an HTTPMessage Response/Request
 * 
 * @author Alexandru Obada
 * 
 */

public interface HTTPMessage extends Message {

	/**
	 * Get the HTTP version specified in the current message
	 * 
	 * @return A string containing the http version
	 */
	public String getHttpVersion();
}
