import java.io.*;
import java.net.Socket;

/**
 * Created by ogranit on 1/5/16.
 */
public class Main {
	public static void main(String argv[]) throws IOException {
		boolean isParsed = Utils.parseConfigFile();
		if(isParsed){
			WebServer webServer = new WebServer();
			webServer.startSession();
		} else {
			System.out.println("There was Problem parsing 'config.ini'.");
		}

		System.out.println("bye bye...");
	}
}
