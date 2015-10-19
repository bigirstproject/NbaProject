package com.sunsun.nbapic.Frament;

import android.os.Message;

import com.kugou.framework.component.base.BaseWorkerFragment;

public class VideoFragment extends BaseWorkerFragment {
	
	

	public static VideoFragment newInstance() {
		VideoFragment fragment = new VideoFragment();
		return fragment;
	}

	@Override
	protected void handleBackgroundMessage(Message msg) {

	}

}
