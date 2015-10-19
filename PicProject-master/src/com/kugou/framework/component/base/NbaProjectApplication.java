package com.kugou.framework.component.base;

import com.sunsun.nbapic.Image.SwitchImageLoader;

public class NbaProjectApplication extends BaseApplication {
	private static NbaProjectApplication mInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		SwitchImageLoader.init(this);
	}
	
	public static NbaProjectApplication getInstance() {
		return mInstance;
	}
}
