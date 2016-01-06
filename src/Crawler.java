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

	}
}
