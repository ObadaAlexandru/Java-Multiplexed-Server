package de.example;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.example.SimpleProtocol.SimpleProtocolBuilder;
import de.mtws.connection.imp.StreamInjector;
import de.mtws.connection.imp.StreamReactor;
import de.mtws.connection.imp.StreamAcceptor.StreamAcceptorBuilder;
import de.mtws.connection.imp.StreamHandlerAdapter.StreamHandlerAdapterBuilder;

public class Main {

	public static void main(String... arg) throws InstantiationException,
			IllegalAccessException {
		StreamInjector injector = new StreamInjector();
		injector.setProtocol(SimpleProtocolBuilder.class);
		injector.setAcceptor(StreamAcceptorBuilder.class);
		injector.setHandlerAdapter(StreamHandlerAdapterBuilder.class);
		injector.setInputHandler(SimpleHandler.class);
		try {
			StreamReactor reactor = new StreamReactor(9999, injector);
			reactor.start();
			ExecutorService server = Executors.newSingleThreadExecutor();
			server.submit(reactor);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
