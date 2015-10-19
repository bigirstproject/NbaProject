package com.sunsun.nbapic.Frament;

import android.os.Message;

import com.kugou.framework.component.base.BaseWorkerFragment;

public class NewsFragment extends BaseWorkerFragment {
	
	public static NewsFragment newInstance() {
		NewsFragment fragment = new NewsFragment();
		return fragment;
	}

	@Override
	protected void handleBackgroundMessage(Message msg) {

	}

}
