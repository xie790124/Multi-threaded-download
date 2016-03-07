package download;

import java.net.URL;
import java.net.URLConnection;
/**
 * HTTP�h�u�{�U���u��
 *�@
 * @author 
 */
public class HTTPDownloader extends Thread {

	private String page;
	/**
	 * �O�s���|
	 */
	private String savePath;
	/**
	 * �u�{��
	 */
	private int threadNumber = 32;
	
	/**
	 * �`�u�{��
	 */
	private int totalThread = 32;
	
	
	/**
	 * �ӷ����}or�a�}
	 */
	private String referer;
	/**
	 * �̤p���ɮפj�p�C�p�G��󰣤W�u�{�Ƥp�E�o�ӼơA�h�|��ֽu�{��
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
	 * �U���ާ@
	 * 
	 * @throws Exception
	 */
	public void down() throws Exception {

		URL url = new URL(page); 
		//�إ߳s��
		URLConnection con = url.openConnection();
		//��o�ɮת��j�p
		int contentLen = con.getContentLength();
		if (contentLen / MIN_BLOCK + 1 < threadNumber) {
			//�վ�u�{�ƶq
			threadNumber = contentLen / MIN_BLOCK + 1;
		}
		//�u�{�̤j�W��
		if (threadNumber > totalThread) {
			threadNumber = totalThread;
		}
		System.out.println("�u�{�ơG"+threadNumber+"\t�ɮפj�p�G"+(contentLen/(1024*1024))+"MB");
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
	 * �U��
	 * 
	 * @param page �Q�U��������
	 * @param savePath �O�s�����|
	 */
	public HTTPDownloader(String page, String savePath) {
		this(page, savePath, 10);
	}
	/**
	 * �U��
	 * 
	 * @param page �Q�U��������
	 * @param savePath �O�s�����|
	 * @param threadNumber �u�{��
	 */
	public HTTPDownloader(String page, String savePath, int threadNumber) {
		this(page, page, savePath, threadNumber);
	}
	/**
	 * �U��
	 * 
	 * @param page �Q�U��������
	 * @param savePath �O�s�����|
	 * @param threadNumber �u�{��
	 * @param referer �ӷ�
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

