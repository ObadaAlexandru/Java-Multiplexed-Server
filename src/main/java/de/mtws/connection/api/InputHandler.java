package de.mtws.connection.api;

/**
 * This interface should be implemented by the client
 * 
 * @author Alexandru Obada
 * 
 */
public interface InputHandler {

	/**
	 * returns the next message
	 * 
	 * @return
	 */
	public Message nextMessage(ChannelFacade channel);

	/**
	 * handles the input
	 * 
	 * @param msg
	 * @param adapter
	 */
	public void handleInput(Message msg, ChannelFacade channel);
}
