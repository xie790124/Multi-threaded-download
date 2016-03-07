package main;

import download.HTTPDownloader;

public class Main {

	public static void main(String[] args) throws Exception {
		HTTPDownloader d = new HTTPDownloader("https://www.dropbox.com/s/kskt25lono5g0ri/SweetROM_V15_BOGB_5.0.1.zip?dl=1", "d://SweetROM_V15_BOGB_5.0.1.zip", 11000);
		d.setTotalThread(100);
		d.down();
	}

}
