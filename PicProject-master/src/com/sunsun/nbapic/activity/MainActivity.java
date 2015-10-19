package com.sunsun.nbapic.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.kugou.framework.component.base.BaseWorkerFragmentActivity;
import com.sunsun.nbapic.Frament.MainListFragment;
import com.sunsun.nbapic.Frament.MenuBehindListFragment;
import com.sunsun.nbapic.event.ToggleEvent;
import com.sunsun.nbaproject.R;

import de.greenrobot.event.EventBus;

public class MainActivity extends BaseWorkerFragmentActivity {

	private String mCurrentFragmentTag = null;
	private static final String XINGAN_FRAGMENT = "	xinganfragment";
	private static final String SIWA_FRAGMENT = "siwafragment";
	private static final String GAOXIN_FRAGMENT = "gaoxinfragment";
	private static final String WANGLUO_FRAGMENT = "wangluofragment";
	private static final String WEIMEI_FRAGMENT = "weimeifragment";
	private static final String TIYU_FRAGMENT = "tiyufragment";
	private static final String MOTE_FRAGMENT = "motefragment";
	private static final String MINGXING_FRAGMENT = "mingxinfragment";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
		setContentView(R.layout.content_frame);
		addFragment(R.id.content_frame, new MainListFragment(), XINGAN_FRAGMENT);

	}

	private void addFragment(int resId, Fragment fg, String tag) {
		if (getSupportFragmentManager().findFragmentByTag(tag) == null) {
			mCurrentFragmentTag = tag;
			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			ft.add(resId, fg, tag);
			ft.commit();
		}
	}

	private void replaceFragment(int resId, Fragment fg, String tag) {
		if (!mCurrentFragmentTag.equals(tag)
				&& getSupportFragmentManager().findFragmentByTag(tag) == null) {
			mCurrentFragmentTag = tag;
			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			ft.replace(resId, fg, tag);
			ft.commit();
		}
	}



	public void onEvent(ToggleEvent event) {
		if (event != null) {
			if (event.getTable().getId() == 1) {
				replaceFragment(R.id.content_frame,
						new MenuBehindListFragment(), XINGAN_FRAGMENT);
			} else if (event.getTable().getId() == 2) {
				replaceFragment(R.id.content_frame,
						new MenuBehindListFragment(), SIWA_FRAGMENT);
			} else if (event.getTable().getId() == 3) {
				replaceFragment(R.id.content_frame,
						new MenuBehindListFragment(), GAOXIN_FRAGMENT);
			} else if (event.getTable().getId() == 4) {
				replaceFragment(R.id.content_frame,
						new MenuBehindListFragment(), WANGLUO_FRAGMENT);
			} else if (event.getTable().getId() == 5) {
				replaceFragment(R.id.content_frame,
						new MenuBehindListFragment(), WEIMEI_FRAGMENT);
			} else if (event.getTable().getId() == 6) {
				replaceFragment(R.id.content_frame,
						new MenuBehindListFragment(), TIYU_FRAGMENT);
			} else if (event.getTable().getId() == 7) {
				replaceFragment(R.id.content_frame,
						new MenuBehindListFragment(), MOTE_FRAGMENT);
			} else if (event.getTable().getId() == 8) {
				replaceFragment(R.id.content_frame,
						new MenuBehindListFragment(), MINGXING_FRAGMENT);
			}
		}
	}

	@Override
	protected void handleBackgroundMessage(Message msg) {
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
