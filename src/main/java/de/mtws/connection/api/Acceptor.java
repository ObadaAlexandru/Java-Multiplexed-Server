package de.mtws.connection.api;

import java.util.concurrent.Callable;

/**
 * Defines a contract for the classes which are designed to handle incomming
 * connections and register them to the dispatcher with a handler
 * 
 * @author Alexandru Obada
 * 
 */
public interface Acceptor extends Callable<Acceptor> {

	/**
	 * This method will register channels and attach handlers to them
	 */
	public void accept();

}
