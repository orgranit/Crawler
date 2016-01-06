/**
 * Created by ogranit on 1/6/16.
 */
public class Downloader implements Runnable {
	private final SynchronizedQueue<String> htmlQueue;
	private final SynchronizedQueue<String> urlQueue;

	public Downloader(SynchronizedQueue<String> producerQueue, SynchronizedQueue<String> consumerQueue) {
		this.htmlQueue = producerQueue;
		this.urlQueue = consumerQueue;

	}

	@Override
	public void run() {
		this.htmlQueue.registerProducer();
		String url;
		while((url = urlQueue.dequeue()) != null){
			//TODO
		}

		this.htmlQueue.unregisterProducer();

	}
}
