package download;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;


/**
 * 下載線程
 * 
 * @author 
 */
class HTTPDownloaderThread extends Thread {
	HTTPDownloader manager;
	int startPos;
	int endPos;
	int id;
	int curPos;
	int BUFFER_SIZE = 1024;
	int readByte = 0;
	HTTPDownloaderThread(HTTPDownloader manager, int id, int startPos, int endPos) {
		this.id = id;
		this.manager = manager;
		this.startPos = startPos;
		this.endPos = endPos;
	}
	public void run() {

		// 建立buff
		BufferedInputStream bis = null;
		RandomAccessFile fos = null;
		// 緩衝區大小
		byte[] buf = new byte[BUFFER_SIZE];
		URLConnection con = null;
		try {
			File file = new File(manager.getSavePath());
			// 創建RandomAccessFile
			fos = new RandomAccessFile(file, "rw");
			// 從startPos開始
			fos.seek(startPos);
			// 創建連接，這裡會為每個線程都創造一個連結
			URL url = new URL(manager.getPage());
			con = url.openConnection();
			con.setAllowUserInteraction(true);
			curPos = startPos;
			// 設置獲取資源的數據範圍，從startPos到endPos
			System.out.println("線程" + id + "啟動\t設置讀取範圍\tbytes=" + startPos + "-" + endPos);
			con.setRequestProperty("Range", "bytes=" + startPos + "-" + endPos);
			// 盜連解決
			con.setRequestProperty("referer", manager.getReferer() == null ? manager.getPage() : manager.getReferer());
			con.setRequestProperty("userAgent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)");
			// 下面一段向根據文件寫入數據，curPos為當前寫入的未知，這裡會判斷是否小於endPos，如果超過endPos就代表該線程已經執行完畢。
			bis = new BufferedInputStream(con.getInputStream());
			long startTime=System.currentTimeMillis();
			while (curPos < endPos) {
				int len = bis.read(buf, 0, BUFFER_SIZE);
				if (len == -1) {
					break;
				}
				fos.write(buf, 0, len);
				curPos = curPos + len;
				if (curPos > endPos) {
					// 獲取正確讀取的字節數
					readByte += len - (curPos - endPos) + 1;
				} else {
					readByte += len;
				}
			}
			long endTime=System.currentTimeMillis();
			int cost= (int) (endTime-startTime)/1000;
			System.out.println("總耗時:" + cost + " 秒");    
			System.out.println("線程" + id + "下載完畢:" + readByte);
			bis.close();
			fos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
