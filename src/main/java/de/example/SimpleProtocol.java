package de.example;

import java.nio.ByteBuffer;

import de.mtws.connection.api.Message;
import de.mtws.connection.api.Protocol;
import de.mtws.connection.builder.api.ProtocolBuilder;

public class SimpleProtocol implements Protocol {

	public static class SimpleProtocolBuilder implements ProtocolBuilder {

		@Override
		public Protocol build() {
			return new SimpleProtocol();
		}

	}

	@Override
	public Message convert(ByteBuffer buff) {
		String string = "received";
		System.out.println(string);
		buff.flip();
		while (buff.hasRemaining()) {
			System.out.print((char) buff.get());
		}
		return new SimpleMessage(string);
	}

}
