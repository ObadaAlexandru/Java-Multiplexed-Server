package de.mtws.connection.api;

import de.mtws.connection.builder.api.AcceptorBuilder;
import de.mtws.connection.builder.api.HandlerAdapterBuilder;
import de.mtws.connection.builder.api.ProtocolBuilder;

/**
 * Defines a contract for the dependency Injectors
 * 
 * @author Alexandru Obada
 * 
 */
public interface Injector {
	/**
	 * Returns an'instance of an Acceptor
	 * 
	 * @param acceptorClass
	 */
	public AcceptorBuilder getAcceptor();

	/**
	 * Sets the class for the acceptor instantiation
	 * 
	 * @param acceptorClass
	 */
	public void setAcceptor(Class<? extends AcceptorBuilder > acceptorClass);

	/**
	 * Returns an'instance of Input Handler
	 * 
	 * @param inputHandler
	 * @return
	 */
	public InputHandler getInputHandler();

	/**
	 * Sets the class for input Handler instantiation
	 * 
	 * @param inputHandler
	 */
	public void setInputHandler(Class<? extends InputHandler> inputHandler);

	/**
	 * Returns an'instance of the handled protocol
	 * 
	 * @param protocol
	 * @return
	 */
	public ProtocolBuilder getProtocol();

	/**
	 * Sets the handled protocol
	 * 
	 * @param protocol
	 */
	public void setProtocol(Class<? extends ProtocolBuilder> protocol);

	/**
	 * returns the adapter
	 * 
	 * @return
	 */
	public HandlerAdapterBuilder getHandlerAdapter();

	/**
	 * sets the adapter class for instantiation
	 * 
	 * @param handlerAdapter
	 */
	public void setHandlerAdapter(Class<? extends HandlerAdapterBuilder> handlerAdapter);

}
