package de.mtws.connection.api;


/**
 * This is the interface through which the client will interact with the
 * framework
 * 
 * @author Alexandru Obada
 * 
 */
public interface ChannelFacade {

	/**
	 * Add a message to the output buffer
	 * 
	 * @param msg
	 */
	public void outQueue(Message msg);

	/**
	 * get a message from the internal buffer
	 * 
	 * @return
	 */
	public Message inQueue();

	/**
	 * check the registered operations
	 * 
	 * @return
	 */
	public int getInterestOps();

	/**
	 * set interested operation
	 * 
	 * @param ops
	 */
	public void setInterestedOps(int ops);

}
