package download;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;


/**
 * �U���u�{
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

		// �إ�buff
		BufferedInputStream bis = null;
		RandomAccessFile fos = null;
		// �w�İϤj�p
		byte[] buf = new byte[BUFFER_SIZE];
		URLConnection con = null;
		try {
			File file = new File(manager.getSavePath());
			// �Ы�RandomAccessFile
			fos = new RandomAccessFile(file, "rw");
			// �qstartPos�}�l
			fos.seek(startPos);
			// �Ыسs���A�o�̷|���C�ӽu�{���гy�@�ӳs��
			URL url = new URL(manager.getPage());
			con = url.openConnection();
			con.setAllowUserInteraction(true);
			curPos = startPos;
			// �]�m����귽���ƾڽd��A�qstartPos��endPos
			System.out.println("�u�{" + id + "�Ұ�\t�]�mŪ���d��\tbytes=" + startPos + "-" + endPos);
			con.setRequestProperty("Range", "bytes=" + startPos + "-" + endPos);
			// �s�s�ѨM
			con.setRequestProperty("referer", manager.getReferer() == null ? manager.getPage() : manager.getReferer());
			con.setRequestProperty("userAgent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)");
			// �U���@�q�V�ھڤ��g�J�ƾڡAcurPos����e�g�J�������A�o�̷|�P�_�O�_�p��endPos�A�p�G�W�LendPos�N�N��ӽu�{�w�g���槹���C
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
					// ������TŪ�����r�`��
					readByte += len - (curPos - endPos) + 1;
				} else {
					readByte += len;
				}
			}
			long endTime=System.currentTimeMillis();
			int cost= (int) (endTime-startTime)/1000;
			System.out.println("�`�Ӯ�:" + cost + " ��");    
			System.out.println("�u�{" + id + "�U������:" + readByte);
			bis.close();
			fos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
