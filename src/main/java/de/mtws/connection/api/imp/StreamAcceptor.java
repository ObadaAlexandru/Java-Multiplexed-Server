package de.mtws.connection.api.imp;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.apache.log4j.Logger;

import de.mtws.connection.api.Acceptor;
import de.mtws.connection.api.Injector;
import de.mtws.connection.api.InputHandler;
import de.mtws.connection.api.Reactor;
import de.mtws.connection.builder.api.AcceptorBuilder;

public class StreamAcceptor implements Acceptor {

	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

	private final ServerSocketChannel server;

	private final Reactor reactor;

	private final Injector injector;

	public static class StreamAcceptorBuilder implements AcceptorBuilder {

		private Reactor reactor;
		private ServerSocketChannel server;
		private Injector injector;

		public StreamAcceptorBuilder() {
		}

		@Override
		public Acceptor build() {
			try {
				return new StreamAcceptor(this);
			} catch (ClosedChannelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public AcceptorBuilder setReactor(Reactor reactor) {
			this.reactor = reactor;
			return this;
		}

		@Override
		public AcceptorBuilder setServerSocketChannel(ServerSocketChannel server) {
			this.server = server;
			return this;
		}

		@Override
		public AcceptorBuilder setInjector(Injector injector) {
			this.injector = injector;
			return this;
		}

	}

	private StreamAcceptor(StreamAcceptorBuilder builder)
			throws ClosedChannelException {
		reactor = builder.reactor;
		server = builder.server;
		injector = builder.injector;
	}

	@Override
	public Acceptor call() throws Exception {
		accept();
		return this;
	}

	@Override
	public void accept() {
		try {
			LOGGER.info("Connection received");
			SocketChannel channel = server.accept();
			channel.configureBlocking(false);
			InputHandler handler = injector.getInputHandler();
			reactor.register(handler, channel);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
