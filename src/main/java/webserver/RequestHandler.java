package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread{
	
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }
	
	public void run(){
		
		log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),connection.getPort());
		
		
		
		try(InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
			
			BufferedReader br = new BufferedReader( new InputStreamReader(in,"UTF-8") );
			
			String firstLine      = br.readLine();
			String arrFirstLine[] = firstLine.split(" ");
			if(arrFirstLine.length==3){
			String httpMethod     = arrFirstLine[0];
			String httpUrl        = arrFirstLine[1].equals("/")?"/index.html":arrFirstLine[1];
			String httpProtocol   = arrFirstLine[2];
			
			log.info("Http Method   : {}",httpMethod);
			log.info("Http Url      : {}",httpUrl);
			log.info("Http Protocol : {}",httpProtocol);
			
			
			
			
			Map<String,String> headerMap = RequestUtill.readHeader(br);
			
			if(!httpMethod.equals("GET")){
				log.info("Content-Length : {}",Integer.valueOf(headerMap.get("Content-Length")));
				String requestBody=RequestUtill.readData(br,Integer.valueOf(headerMap.get("Content-Length")));
				log.info(requestBody.trim());                  
			}
			
			DataOutputStream dos = new DataOutputStream(out);
			
			File file = new File("./webapp"+httpUrl);
			byte[] body;
			if(file.exists()){
				body = Files.readAllBytes(file.toPath());
			}else{
				body = Files.readAllBytes(new File("./webapp/404page.html").toPath());
			}
			
            
			ResponseUtill.response200Header(dos, body.length);
			ResponseUtill.responseBody(dos, body);
			}else{
				throw new Exception("INVALID FORMAT");
			}
			
		} catch (IOException e) {
			log.error("error:"+e);
		} catch (Exception e){
			log.error("error:"+e);
		} finally {
			try {
				connection.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error("error:"+e);
			}
		}
	}
	
	
    
}
