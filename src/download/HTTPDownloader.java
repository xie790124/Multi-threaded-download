package download;

import java.net.URL;
import java.net.URLConnection;
/**
 * HTTP絬祘更ㄣ
 *
 * @author 
 */
public class HTTPDownloader extends Thread {

	private String page;
	/**
	 * 玂隔畖
	 */
	private String savePath;
	/**
	 * 絬祘计
	 */
	private int threadNumber = 32;
	
	/**
	 * 羆絬祘计
	 */
	private int totalThread = 32;
	
	
	/**
	 * ㄓ方呼or
	 */
	private String referer;
	/**
	 * 程郎狦ゅン埃絬祘计硂计玥穦搭ぶ絬祘计
	 */
	private int MIN_BLOCK = 10 * 1024;
	public void run() {
		try {
			down();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 更巨
	 * 
	 * @throws Exception
	 */
	public void down() throws Exception {

		URL url = new URL(page); 
		//ミ硈挡
		URLConnection con = url.openConnection();
		//莉眔郎
		int contentLen = con.getContentLength();
		if (contentLen / MIN_BLOCK + 1 < threadNumber) {
			//秸俱絬祘计秖
			threadNumber = contentLen / MIN_BLOCK + 1;
		}
		//絬祘程
		if (threadNumber > totalThread) {
			threadNumber = totalThread;
		}
		System.out.println("絬祘计"+threadNumber+"\t郎"+(contentLen/(1024*1024))+"MB");
		int begin = 0;
		int step = contentLen / threadNumber;
		int end = 0;
		for (int i = 0; i < threadNumber; i++) {
			end += step;
			if (end > contentLen) {
				end = contentLen;
			}
			new HTTPDownloaderThread(this, i, begin, end).start();
			begin = end;
		}
	}
	public HTTPDownloader() {
	}
	/**
	 * 更
	 * 
	 * @param page 砆更
	 * @param savePath 玂隔畖
	 */
	public HTTPDownloader(String page, String savePath) {
		this(page, savePath, 10);
	}
	/**
	 * 更
	 * 
	 * @param page 砆更
	 * @param savePath 玂隔畖
	 * @param threadNumber 絬祘计
	 */
	public HTTPDownloader(String page, String savePath, int threadNumber) {
		this(page, page, savePath, threadNumber);
	}
	/**
	 * 更
	 * 
	 * @param page 砆更
	 * @param savePath 玂隔畖
	 * @param threadNumber 絬祘计
	 * @param referer ㄓ方
	 */
	public HTTPDownloader(String page, String referer, String savePath, int threadNumber) {
		this.page = page;
		this.savePath = savePath;
		this.threadNumber = threadNumber;
		this.referer = referer;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getSavePath() {
		return savePath;
	}
	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}
	public int getThreadNumber() {
		return threadNumber;
	}
	public void setThreadNumber(int threadNumber) {
		this.threadNumber = threadNumber;
	}
	public String getReferer() {
		return referer;
	}
	public void setReferer(String referer) {
		this.referer = referer;
	}
	public int getTotalThread() {
		return totalThread;
	}
	public void setTotalThread(int totalThread) {
		this.totalThread = totalThread;
	}
}

