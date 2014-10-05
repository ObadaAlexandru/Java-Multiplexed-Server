package de.mtws.connection.http.api.version;

import de.mtws.connection.http.api.Version;

/**
 * represents an the version number of HTTP Messages provides methods for
 * retrieving major, minor version numbers and comparing them
 * 
 * @author Alexandru Obada
 * 
 */
public class HTTPVersion implements Version {

	/*
	 * Version numbers
	 */
	private final int minor;
	private final int major;

	public HTTPVersion(int major, int minor) {
		this.major = major;
		this.minor = minor;
	}

	/**
	 * Returns an instance of HTTPVersion
	 * 
	 * @param version
	 * @throws IllegalArgumentException
	 *             when a malformed string is passed as version number
	 */
	public HTTPVersion(String version) {
		if (validateVersion(version)) {
			// TODO here we should parse the version String for minor and major
			// version numbers
			minor = 0;
			major = 0;
		} else {
			throw new IllegalArgumentException("Invalid HTTP version number");
		}
	}

	private boolean validateVersion(String version) {
		// TODO this method should validate the input string
		return true;
	}

	@Override
	public int compareTo(Version o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMajor() {
		return minor;
	}

	@Override
	public int getMinor() {
		return major;
	}

}
