import java.io.*;
import java.net.Socket;

/**
 * Created by ogranit on 1/6/16.
 */
public class Downloader implements Runnable {
	private final SynchronizedQueue<String> htmlQueue;
	private final SynchronizedQueue<String> urlQueue;
	private final String domain;

	public Downloader(SynchronizedQueue<String> producerQueue, SynchronizedQueue<String> consumerQueue, String domain) {
		this.htmlQueue = producerQueue;
		this.urlQueue = consumerQueue;
		this.domain = domain;

	}

	@Override
	public void run() {
		this.htmlQueue.registerProducer();
		String url;
		while((url = urlQueue.dequeue()) != null){
			htmlQueue.enqueue(downloadUrl(url));

		}

		this.htmlQueue.unregisterProducer();

	}

	// this method return a string represents html page that could
	// be empty if an error occured during downloading
	private String downloadUrl(String url) {
		Socket socket = null;
		String htmlPage = "";
		try {
			//TODO we will need here some "smart" service to do the connection and to even redirect us
			// in case we get "301 moved temporirly" but this is only if the metargel says so
			// so we need to check what he answer in the forum in the mean time this works fine with this domain:
			// www.marmelada.co.il
			socket = new Socket(domain, 80);
			//socket.setSoTimeout(3000);
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out.write("GET " + url + " HTTP/1.1" + Utils.CRLF);
			out.write("Host: " +  domain + Utils.CRLF);
			out.write("Connection: close" + Utils.CRLF);
			out.write(Utils.CRLF);
			out.flush();
			StringBuilder stringBuilder = new StringBuilder();
			String line;
			System.out.println("Downloader starts downloading URL " + url);

			while((line = in.readLine()) != null){
				stringBuilder.append(line + "\n");
			}

			htmlPage = stringBuilder.toString();
			System.out.println("Downloader ends downloading URL " + url);
			if(socket != null){
				socket.close();
			}
		} catch (IOException e) {
			System.out.println("Downloader failed to download " + url);
		}

		return htmlPage;
	}
}
