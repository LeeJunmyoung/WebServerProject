package webserver;

import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServer {
	private static final Logger log = LoggerFactory.getLogger(WebServer.class);

	private static final int port = 12345;
	
	public static void main(String args[]){
		
		log.info("HELLO WEB SERVER");
		log.info("PORT {}",port);
		
		try {
			ServerSocket listenSocket = new ServerSocket(port);
			
			Socket socket;
			while((socket=listenSocket.accept())!=null){
				log.info("socket start");
				RequestHandler requestHandler = new RequestHandler(socket);
				requestHandler.start();
				
			}
			
			
		} catch (Exception e) {
			log.error("error:"+e);
		}
	}
}
