package de.mtws.connection.api;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.util.concurrent.Callable;

/**
 * Defines a contract for the handler used in the Reactor Pattern implementation
 * 
 * @author Alexandru Obada
 * 
 */
public interface HandlerAdapter extends Callable<HandlerAdapter>, ChannelFacade {

	public static int IN_QUEUE_CAPACITY = 300;

	public static int OUT_QUEUE_CAPACITY = 300;
	
	public static int DEFAULT_BUFFER_CAPACITY = 4096;

	/**
	 * sets the interested keys
	 * 
	 * @param key
	 */
	// public void setKey(SelectionKey key);

	/**
	 * returns the key used for the registration to the demultiplexer
	 * 
	 * @return
	 */
	public SelectionKey getKey();

	/**
	 * Process the received data and returns a DataSegment the conversion
	 * between the raw data and DS should be done by a protocol implemented by
	 * the client
	 * 
	 * @param buff
	 * @return
	 */
	public Message processing(ByteBuffer buff);

	/**
	 * Kill the handler
	 * 
	 * @param
	 */
	public void kill();

	/**
	 * check if the handler is steel alive
	 * 
	 * @return
	 */
	public boolean isAlive();

	/**
	 * defines the communication protocol
	 * 
	 * @param protocol
	 */
	// public void setProtocol(Protocol protocol);

	/**
	 * prepare the handler to run in a thread
	 */
	public void prepareToRun();
}
