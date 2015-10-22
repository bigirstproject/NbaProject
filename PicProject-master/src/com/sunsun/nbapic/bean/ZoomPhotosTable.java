package com.sunsun.nbapic.bean;

import java.util.List;

public class ZoomPhotosTable extends commonTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String title;

	private List<PicAddress> mListPic;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<PicAddress> getmListPic() {
		return mListPic;
	}

	public void setmListPic(List<PicAddress> mListPic) {
		this.mListPic = mListPic;
	}

	public static class PicAddress extends commonTable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String url;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

	}

}
