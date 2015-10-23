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
		private String imageUrl;
		private String title;
		private String subTitle;


		public String getImageUrl() {
			return imageUrl;
		}

		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getSubTitle() {
			return subTitle;
		}

		public void setSubTitle(String subTitle) {
			this.subTitle = subTitle;
		}
		
		

	}

}
