package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestUtill {
	
	private static final Logger log = LoggerFactory.getLogger(RequestUtill.class);
	

	public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }
	
	public static Map<String, String> readHeader(BufferedReader br) throws IOException{
		Map<String, String> map = new HashMap<String, String>();
		String line;
		while((line=br.readLine())!=null){
			log.debug(line);
			if(!line.equals("")){
			String key = line.split(":")[0];
			String value = line.split(":")[1].trim();
			map.put(key, value);
			}else{
				break;
			}
		}
		return map;
	}
	
}
