package com.sunsun.nbapic.config;

import com.kugou.framework.component.base.NbaProjectApplication;

/**
 * 程序配置类：定义一些全局常量
 * 
 */
public class AppConfig {

	public static final float DENSITY = NbaProjectApplication.getInstance()
			.getResources().getDisplayMetrics().density;
	public static final float SCALESITY = NbaProjectApplication.getInstance()
			.getResources().getDisplayMetrics().scaledDensity;
	public static final int widthPx = NbaProjectApplication.getInstance()
			.getResources().getDisplayMetrics().widthPixels;
	public static final int heightPx = NbaProjectApplication.getInstance()
			.getResources().getDisplayMetrics().heightPixels;
	
	public static final boolean CRASHHANDLE = false;
	public static final boolean DEBUG = true;
	
	public static final String LOG_TAG = "DWYXApp";
}