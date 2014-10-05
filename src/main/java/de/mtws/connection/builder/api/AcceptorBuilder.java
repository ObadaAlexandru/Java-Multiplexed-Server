package de.mtws.connection.builder.api;

import java.nio.channels.ServerSocketChannel;

import de.mtws.connection.api.Acceptor;
import de.mtws.connection.api.Injector;
import de.mtws.connection.api.Reactor;

public interface AcceptorBuilder extends Builder<Acceptor> {

	public AcceptorBuilder setReactor(Reactor reactor);

	public AcceptorBuilder setServerSocketChannel(ServerSocketChannel server);
	
	public AcceptorBuilder setInjector(Injector injector);

}
