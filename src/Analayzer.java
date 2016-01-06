/**
 * Created by ogranit on 1/6/16.
 */
public class Analayzer implements Runnable {
	private final SynchronizedQueue<String> urlQueue;
	private final SynchronizedQueue<String> htmlQueue;

	public Analayzer(SynchronizedQueue<String> producerQueue, SynchronizedQueue<String> consumerQueue) {
		this.urlQueue = producerQueue;
		this.htmlQueue = consumerQueue;

	}

	@Override
	public void run() {
		this.urlQueue.registerProducer();
		String html;
		while((html = htmlQueue.dequeue()) != null){
			//TODO
		}

	}
}
