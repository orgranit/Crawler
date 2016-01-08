/**
 * Created by ogranit on 1/6/16.
 */

import java.util.HashMap;

/**
 * This class represents the Analyzers statcs on specific crawl, it also holds all the visited Url's HasMap
 */
public class CrawlStatistics {
	private  HashMap visitedUrls;
	//TODO add more stats and methods not forget to sync all of the methods

	public CrawlStatistics() {
		this.visitedUrls = new HashMap();
	}

	// Check if a url is already in 'visitedUrls'
	public boolean urlsContain(String url){
		synchronized (visitedUrls){
			visitedUrls.notifyAll();
			return visitedUrls.containsKey(url);
		}
	}

	public void urlsAdd(String url){
		synchronized (visitedUrls){
			visitedUrls.put(url, url);
			visitedUrls.notifyAll();
		}
	}





}
