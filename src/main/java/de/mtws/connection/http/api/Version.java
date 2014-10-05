package de.mtws.connection.http.api;

/**
 * Represents the version number, provides methods for comparing versions,
 * getting major or manor version numbers.
 * 
 * @author Alexandru Obada
 * 
 */
public interface Version extends Comparable<Version> {

	/**
	 * Returns the major version number
	 * 
	 * @return
	 */
	public int getMajor();

	/**
	 * Returns the minor version number
	 * 
	 * @return
	 */
	public int getMinor();
}
