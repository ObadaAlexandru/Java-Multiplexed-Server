package de.mtws.connection.api;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.util.concurrent.Callable;

/**
 * Defines a contract for the Dispatcher classes The dispatcher for the Reactor
 * pattern implementation It should contain an internal selector in order to be
 * able to demultiplex managed connections
 * 
 * @author Alexandru Obada
 * 
 */

public interface Reactor extends Callable<Reactor> {

	/**
	 * Here the demultiplexing I/O should be done and right handlers called
	 * 
	 * @throws IOException
	 * 
	 */
	public void dispatch() throws IOException;

	/**
	 * Register a channel with a handler and return a ChannelFacade to interact
	 * with it
	 * 
	 * @param handler
	 * @param channel
	 * @return
	 * @throws ClosedChannelException
	 */
	public ChannelFacade register(InputHandler handler,
			SelectableChannel channel) throws ClosedChannelException;

	/**
	 * Unregister the a channel from the selector
	 * 
	 * @param facade
	 * @return
	 */
	public boolean unRegister(ChannelFacade facade);

}
