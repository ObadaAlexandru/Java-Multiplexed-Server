package de.example;

import org.apache.log4j.Logger;

import de.mtws.connection.api.ChannelFacade;
import de.mtws.connection.api.InputHandler;
import de.mtws.connection.api.Message;

public class SimpleHandler implements InputHandler {
	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

	@Override
	public Message nextMessage(ChannelFacade channel) {
		LOGGER.debug("Handle message");
		Message msg = channel.inQueue();
		if(null != msg) {
			LOGGER.debug(msg.toString());
		}
		return msg;
	}

	@Override
	public void handleInput(Message msg, ChannelFacade channel) {
		LOGGER.debug("Handle input");
		channel.outQueue(msg);
	}

}
