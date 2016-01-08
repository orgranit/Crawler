/**
 * Created by ogranit on 1/6/16.
 */
public class Analayzer implements Runnable {
	private final SynchronizedQueue<String> urlQueue;
	private final SynchronizedQueue<String> htmlQueue;
	private boolean suspicious = false;

	public Analayzer(SynchronizedQueue<String> producerQueue, SynchronizedQueue<String> consumerQueue, CrawlStatistics crawlStatistics) {
		this.urlQueue = producerQueue;
		this.htmlQueue = consumerQueue;

	}

	// we have a big dead-lock problem, the queues are cyclic:
	// in one queue the analyzers are producers and the downloaders are consumers
	// and in the other queue its opposite.
	// so in order to prevant a deadlock we define set of rules:
	// state SUSPICIOUS: the analyzer is suspicious that the downloders are idle
	// to get to SUSPICIOUS the analyzer need to satisfy the following:
	// 1. the dequeue item (html) is empty or the dequeue item (html) didnt provide additonal urls to enqueue
	// 2. both of the queues are empty
	// if we are in SUSPICIOUS state, we need to unregister this analyzer from the urlQueue, and then we are back
	// to the while loop conditon, (but not as producers) so the downloaders, if really idle will eventually unregister themselves
	// from the htmlQueue and this analyzer will fail to asnwer the while loop conditon so it will be out and done.
	// in case the downloaders arent really idle we are back inside the loop and since we are suspicois we register
	// again as producers.

	@Override
	public void run() {
		this.urlQueue.registerProducer();
		String html;
		while((html = htmlQueue.dequeue()) != null){
			//TODO analyze html and to find a way to signal that no new urls are found
			// I think we should create a html class that will be in the html queue instead of a string 9
			// you were right about that, sorry(: in the mean time i only check if the string-html is empty
			// when we will have the html class we will need two fields in it:
			// 1. a field to indicate an error occured while donloading this html so its useless
			// 2. a field to inicate no new urls are in this html



			this.suspicious = html.isEmpty() & false;

			if(this.suspicious){
				this.urlQueue.registerProducer();
				this.suspicious = false;
			}

			System.out.println(html);


		}

	}
}
