package de.mtws.connection.api.imp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;

import de.mtws.connection.api.Acceptor;
import de.mtws.connection.api.ChannelFacade;
import de.mtws.connection.api.Injector;
import de.mtws.connection.api.InputHandler;
import de.mtws.connection.api.Protocol;
import de.mtws.connection.api.Reactor;
import de.mtws.connection.api.HandlerAdapter;
import de.mtws.connection.builder.api.AcceptorBuilder;
import de.mtws.connection.builder.api.HandlerAdapterBuilder;

public class StreamReactor implements Reactor {
	private Logger LOGGER = Logger.getLogger(this.getClass().getName());

	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	private final Selector selector;

	private final Acceptor acceptor;

	private int bindedPort;

	private volatile boolean active;

	private volatile boolean alive;

	private final Injector injector;

	private final ExecutorService handlingPool;

	private final ExecutorService acceptingPool;

	private Queue<HandlerAdapter> statusChangedQueue;

	private final Protocol protocol;

	public StreamReactor(int port, Injector injector) throws IOException {
		LOGGER.debug("Start reactor configuration");

		handlingPool = Executors.newSingleThreadExecutor();

		acceptingPool = Executors.newSingleThreadExecutor();

		this.injector = injector;

		bindedPort = port;

		ServerSocketChannel server = ServerSocketChannel.open();

		server.configureBlocking(false);

		server.bind(new InetSocketAddress(port));

		selector = Selector.open();

		statusChangedQueue = new LinkedList<>();

		alive = true;

		AcceptorBuilder acceptorBuilder = this.injector.getAcceptor();

		// TODO Probably some parameters should be set in the protocol
		protocol = injector.getProtocol().build();

		acceptor = acceptorBuilder.setReactor(this)
				.setServerSocketChannel(server).setInjector(injector).build();
		server.register(selector, SelectionKey.OP_ACCEPT, acceptor);
	}

	@Override
	public void dispatch() throws IOException {
		guardBarrier();
		reapCompletedHandlers();
		int readyChannels = selector.select();
		if (readyChannels != 0) {
			Set<SelectionKey> keys = selector.selectedKeys();
			for (SelectionKey key : keys) {
				if (key.isAcceptable()) {
					LOGGER.debug("Connection  received");
					Acceptor acceptor = (Acceptor) key.attachment();
					invokeAcceptor(acceptor);

				} else {
					LOGGER.debug("Channel ready for " + key.readyOps());
					HandlerAdapter handler = (HandlerAdapter) key.attachment();
					invokeHandler(handler);
				}
			}
			keys.clear();
		}
	}

	private void invokeHandler(HandlerAdapter handler) {
		handler.prepareToRun();
		// Stop selection on this channel
		handler.getKey().interestOps(0);
		Future<HandlerAdapter> handledResult;
		try {
			handledResult = handlingPool.submit(handler);
			this.enqueueStatusChangeAdapter(handler);
			handledResult.get();
			LOGGER.debug("Message handled");
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			LOGGER.error("Execution error, the handler will be killed");
			handler.kill();
		}
	}

	private void invokeAcceptor(Acceptor acceptor) {
		try {
			Future<Acceptor> acceptorResult = acceptingPool.submit(acceptor);
			acceptorResult.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			LOGGER.error("Error during connection");
		}
	}

	@Override
	public Reactor call() throws Exception {
		while (alive) {
			if (active) {
				dispatch();
			}
		}
		return this;
	}

	/**
	 * start dispatching
	 */
	public void start() {
		LOGGER.info("Dispatcher started");
		active = true;
	}

	public void pause() {
		active = false;
	}

	public boolean isRunning() {
		return active;
	}

	public void stop() {
		LOGGER.info("Rector stopped");
		active = false;
		alive = false;
	}

	public int getPort() {
		return bindedPort;
	}

	public Acceptor getAcceptor() {
		return acceptor;
	}

	public void guardBarrier() {
		lock.writeLock().lock();
		lock.writeLock().unlock();
	}

	public void aquireSelectionGuard() {
		lock.readLock().lock();
		selector.wakeup();
	}

	public void releaseSelectionGuard() {
		lock.readLock().unlock();
	}

	@Override
	public ChannelFacade register(InputHandler handler,
			SelectableChannel channel) throws ClosedChannelException {
		aquireSelectionGuard();
		HandlerAdapterBuilder adapterBuilder = injector.getHandlerAdapter();
		try {
			SelectionKey key = channel.register(selector, SelectionKey.OP_READ);

			// Instantiating the HandlerAdapter through a builder
			HandlerAdapter adapter = adapterBuilder.setInputHandler(handler)
					.setReactor(this).setProtocol(protocol).setChannel(channel)
					.setKey(key).build();
			key.attach(adapter);
			LOGGER.debug("Channel registered");
			return adapter;
		} finally {
			releaseSelectionGuard();
		}
	}

	@Override
	public boolean unRegister(ChannelFacade channel) {
		if (!(channel instanceof HandlerAdapter)) {
			throw new IllegalArgumentException("Cannot registrate class ="
					+ channel.getClass().getName());
		}
		aquireSelectionGuard();
		HandlerAdapter adapter = (HandlerAdapter) channel;
		SelectionKey key = adapter.getKey();
		try {
			key.cancel();
			LOGGER.debug("Channel Unregistered");
			return true;
		} finally {
			releaseSelectionGuard();
		}
	}

	private void reapCompletedHandlers() {
		HandlerAdapter adapter;
		while ((adapter = statusChangedQueue.poll()) != null) {
			if (adapter.isAlive()) {
				resumeSelection(adapter);
			} else {
				unRegister(adapter);
			}
		}
	}
	
	public void enqueueStatusChangeAdapter(HandlerAdapter adapter) {
		statusChangedQueue.add(adapter);
	}

	private void resumeSelection(HandlerAdapter adapter) {
		SelectionKey key = adapter.getKey();
		if (key.isValid()) {
			key.interestOps(adapter.getInterestOps());
		}
	}
}
