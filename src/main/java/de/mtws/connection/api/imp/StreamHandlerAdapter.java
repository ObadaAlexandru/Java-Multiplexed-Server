package de.mtws.connection.api.imp;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.log4j.Logger;

import de.mtws.connection.api.ChannelFacade;
import de.mtws.connection.api.HandlerAdapter;
import de.mtws.connection.api.InputHandler;
import de.mtws.connection.api.Message;
import de.mtws.connection.api.Protocol;
import de.mtws.connection.api.Reactor;
import de.mtws.connection.builder.api.HandlerAdapterBuilder;

public class StreamHandlerAdapter implements ChannelFacade, HandlerAdapter {

	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

	private final InputHandler handler;

	private final Reactor reactor;

	private final SelectionKey key;

	private final Protocol protocol;

	private final Queue<Message> inQueue;

	private final Queue<Message> outQueue;

	private int interestedOperations;

	private int readyOps;

	private final Object lock = new Object();

	private final SocketChannel channel;

	private volatile boolean running;

	private volatile boolean shutingdown;

	private volatile boolean alive;
	{
		alive = true;
	}

	public static class StreamHandlerAdapterBuilder implements
			HandlerAdapterBuilder {

		private InputHandler handler;

		private Reactor reactor;

		private SelectionKey key;

		private Protocol protocol;

		private SocketChannel channel;

		@Override
		public HandlerAdapter build() {
			return new StreamHandlerAdapter(this);
		}

		@Override
		public HandlerAdapterBuilder setInputHandler(InputHandler handler) {
			this.handler = handler;
			return this;
		}

		@Override
		public HandlerAdapterBuilder setReactor(Reactor reactor) {
			this.reactor = reactor;
			return this;
		}

		@Override
		public HandlerAdapterBuilder setProtocol(Protocol protocol) {
			this.protocol = protocol;
			return this;
		}

		@Override
		public HandlerAdapterBuilder setKey(SelectionKey key) {
			this.key = key;
			return this;
		}

		@Override
		public HandlerAdapterBuilder setChannel(SelectableChannel channel) {
			this.channel = (SocketChannel) channel;
			return this;
		}

	}

	public StreamHandlerAdapter(StreamHandlerAdapterBuilder builder) {
		handler = builder.handler;
		reactor = builder.reactor;
		protocol = builder.protocol;
		key = builder.key;
		channel = builder.channel;
		inQueue = new ArrayBlockingQueue<>(IN_QUEUE_CAPACITY);
		outQueue = new ArrayBlockingQueue<>(OUT_QUEUE_CAPACITY);
	}

	private void drainOutput() {
		try {
			LOGGER.debug("Draining Output");
			if ((readyOps & SelectionKey.OP_WRITE) != 0) {
				Message msg;
				while ((msg = outQueue.poll()) != null) {
					LOGGER.debug("Sending message " + msg.toString());
					ByteBuffer buffer = msg.toByteBuffer();
					channel.write(buffer);
				}
				if (outQueue.isEmpty()) {
					disableWriteSelection();
					if (shutingdown) {
						channel.close();
					}
				}
			}
		} catch (IOException e) {
			LOGGER.warn("remote client ended connection");
			this.kill();
		}
	}

	private void fillInput() throws IOException {
		LOGGER.debug("Filling the input");
		if ((readyOps & SelectionKey.OP_READ) != 0) {
			ByteBuffer buffer = ByteBuffer.allocate(DEFAULT_BUFFER_CAPACITY);
			channel.read(buffer);
			Message msg = protocol.convert(buffer);
			inQueue.add(msg);
		}
	}

	private void disableWriteSelection() {
		synchronized (lock) {
			interestedOperations = interestedOperations
					& ~SelectionKey.OP_WRITE;
		}
	}

	@Override
	public void outQueue(Message msg) {
		LOGGER.debug("Message enqueued. \n MSG --> " + msg);
		this.setInterestedOps(getInterestOps() | SelectionKey.OP_WRITE);
		outQueue.add(msg);
	}

	@Override
	public Message inQueue() {
		Message msg = inQueue.poll();
		LOGGER.debug("Message dequeued. \n MSG --> " + msg);
		return msg;
	}

	@Override
	public int getInterestOps() {
		return interestedOperations;
	}

	@Override
	public void setInterestedOps(int ops) {
		interestedOperations = ops;
	}

	@Override
	public StreamHandlerAdapter call() throws Exception {
		try {
			drainOutput();
			fillInput();

			Message msg;
			while ((msg = handler.nextMessage(this)) != null) {
				handler.handleInput(msg, this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			synchronized (lock) {
				running = false;
			}
		}
		return this;
	}

	@Override
	public Message processing(ByteBuffer buff) {
		return protocol.convert(buff);
	}

	@Override
	public void kill() {
		reactor.unRegister(this);
		alive = false;
	}

	@Override
	public boolean isAlive() {
		return alive;
	}

	@Override
	public SelectionKey getKey() {
		return key;
	}

	@Override
	public void prepareToRun() {
		synchronized (lock) {
			interestedOperations = key.interestOps();
			readyOps = key.readyOps();
			running = true;
		}
	}
	
	public boolean isRunning() {
		return running;
	}

}
