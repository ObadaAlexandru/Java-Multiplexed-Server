package de.mtws.connection.http.api;

/**
 * Contains constants used through the protocol
 * 
 * @author Alexandru Obada
 * 
 */
public enum HTTPConstants {
	CRLF("\r\n"), LF("\n");

	private String data;

	HTTPConstants(String data) {
		this.data = data;
	}

	public String getData() {
		return data;
	}

}
