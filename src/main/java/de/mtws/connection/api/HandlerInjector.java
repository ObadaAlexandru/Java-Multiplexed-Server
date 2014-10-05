package de.mtws.connection.api;

/**
 * the HandlerInjector will be responsible for injecting the right
 * implementation of the handler
 * 
 * @author Alexandru Obada
 * 
 */
public interface HandlerInjector {

	public HandlerAdapter inject();

}
