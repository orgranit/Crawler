/**
 * Created by ogranit on 1/6/16.
 */

import java.util.HashMap;

/**
 * This class represents the Analyzers statcs on specific crawl, it also holds all the visited Url's HasMap
 */
public class CrawlStatistics {
	private  HashMap visitedUrls;
	private static Object lock = new Object();
	//TODO add more stats and methods

	public CrawlStatistics() {
		this.visitedUrls = new HashMap();
	}

	public boolean urlsContain(String url){
		synchronized (lock){
			lock.notifyAll();
			return visitedUrls.containsKey(url);
		}
	}

	public void urlsAdd(String url){
		synchronized (lock){
			visitedUrls.put(url, url);
			lock.notifyAll();
		}
	}





}
