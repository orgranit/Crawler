import jdk.internal.org.objectweb.asm.tree.analysis.Analyzer;

import java.util.HashMap;

/**
 * Created by ogranit on 1/5/16.
 */
public class Crawler {
	private static Crawler INSTANCE = null;
	private static Object LOCK = new Object();
	private static Object CRAWLING_LOCK= new Object();
	private final int maxAnalyzers;
	private final int maxDownloaders;
	private boolean fullPortScan = false;
	private boolean disrespectRobots = false;
	private String domain = null;
	private boolean isCrawling = false;

	private SynchronizedQueue<String> urlQueue;
	private SynchronizedQueue<String> htmlQueue;

	private Crawler(int maxDownloaders, int maxAnalyzers){
		this.maxAnalyzers = maxAnalyzers;
		this.maxDownloaders = maxDownloaders;
		int capacity = 5 * (maxAnalyzers + maxDownloaders);
		this.urlQueue = new SynchronizedQueue<>(capacity);
		this.htmlQueue = new SynchronizedQueue<>(capacity);
	}

	//[bonus]
	// lazy initialization, thread safe Singleton
	public static Crawler getInstance() {

		if (INSTANCE == null) {
			// Thread Safe. Might be costly operation in some case therefore
			// the double lock
			synchronized (LOCK) {
				if (INSTANCE == null) {
					INSTANCE = new Crawler(Utils.MAX_DOWNLOADERS, Utils.MAX_ANALAYZERS);
				}
			}
		}

		return INSTANCE;
	}


	public void setParams(HashMap<String,String> params) {
		this.fullPortScan = params.containsKey("full_port_scan");
		this.disrespectRobots = params.containsKey("disrespect_robots");
		this.domain = params.get("domain");
	}

	public boolean isCrawling() {
		synchronized (CRAWLING_LOCK) {
			CRAWLING_LOCK.notifyAll();
			return this.isCrawling;
		}
	}

	public void setCrawling(boolean crawling) {
		synchronized (CRAWLING_LOCK) {
			this.isCrawling = crawling;
			CRAWLING_LOCK.notifyAll();
		}
	}

	public void crawl() {
		Thread[] downloaders = initAndStartThreads(this.maxDownloaders, this.htmlQueue, this.urlQueue, true);
		Thread[] analayzers = initAndStartThreads(this.maxAnalyzers, this.urlQueue, this.htmlQueue, false);
		// wait for all threads to finish
		waitForAllThreadsToFinish(downloaders);
		waitForAllThreadsToFinish(analayzers);

	}

	private void waitForAllThreadsToFinish(Thread[] threads) {
		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				System.err.println("Thread interrupted");
			}
		}
	}


	private Thread[] initAndStartThreads(int numOfThreads, SynchronizedQueue<String> producerQueue,
	                                     SynchronizedQueue<String> consumerQueue, boolean isDownloder) {
		Thread[] threadsArr = new Thread[numOfThreads];
		for (int i = 0; i < numOfThreads ; i++) {
			if (isDownloder){
				threadsArr[i] = new Thread(new Downloader(producerQueue, consumerQueue));
			} else {
				threadsArr[i] = new Thread(new Analayzer(producerQueue, consumerQueue));
			}
			threadsArr[i].start();
		}

		return  threadsArr;
	}
}
