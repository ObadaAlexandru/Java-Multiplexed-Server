package de.mtws.connection.builder.api;

import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;

import de.mtws.connection.api.HandlerAdapter;
import de.mtws.connection.api.InputHandler;
import de.mtws.connection.api.Protocol;
import de.mtws.connection.api.Reactor;

public interface HandlerAdapterBuilder extends Builder<HandlerAdapter> {

	public HandlerAdapterBuilder setInputHandler(InputHandler handler);

	public HandlerAdapterBuilder setReactor(Reactor reactor);
	
	public HandlerAdapterBuilder setProtocol(Protocol protocol);
	
	public HandlerAdapterBuilder setKey(SelectionKey key);
	
	public HandlerAdapterBuilder setChannel(SelectableChannel channel);

}
