package de.mtws.connection.api.imp;

import de.mtws.connection.api.Injector;
import de.mtws.connection.api.InputHandler;
import de.mtws.connection.builder.api.AcceptorBuilder;
import de.mtws.connection.builder.api.HandlerAdapterBuilder;
import de.mtws.connection.builder.api.ProtocolBuilder;

public class StreamInjector implements Injector {

	private Class<? extends ProtocolBuilder> protocolBuilder;

	private Class<? extends HandlerAdapterBuilder> handlerAdapterBuilder;

	private Class<? extends AcceptorBuilder> acceptorBuilder;

	private Class<? extends InputHandler> inputHandler;

	@Override
	public AcceptorBuilder getAcceptor() {
		try {
			AcceptorBuilder builderInstance = acceptorBuilder.newInstance();
			return builderInstance;
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void setAcceptor(Class<? extends AcceptorBuilder> acceptorClass) {
		acceptorBuilder = acceptorClass;
	}

	@Override
	public InputHandler getInputHandler() {
		try {
			InputHandler builderInstance = inputHandler.newInstance();
			return builderInstance;
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void setInputHandler(Class<? extends InputHandler> inputHandler) {
		this.inputHandler = inputHandler;
	}

	@Override
	public HandlerAdapterBuilder getHandlerAdapter() {
		try {
			HandlerAdapterBuilder builderInstance = handlerAdapterBuilder
					.newInstance();
			return builderInstance;
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void setHandlerAdapter(
			Class<? extends HandlerAdapterBuilder> handlerAdapter) {
		handlerAdapterBuilder = handlerAdapter;
	}

	@Override
	public ProtocolBuilder getProtocol() {
		try {
			ProtocolBuilder protocolBuilder = this.protocolBuilder
					.newInstance();
			return protocolBuilder;
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void setProtocol(Class<? extends ProtocolBuilder> protocol) {
		protocolBuilder = protocol;
	}

}
