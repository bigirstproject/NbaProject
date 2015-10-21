package com.sunsun.nbapic.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.kugou.framework.component.base.BaseWorkerFragmentActivity;
import com.kugou.framework.component.base.ScrollableViewPager;
import com.kugou.framework.component.base.ViewPagerAdapter;
import com.sunsun.nbapic.Frament.ChinaMainPhotosFragment;
import com.sunsun.nbapic.Frament.GalleryFragment;
import com.sunsun.nbapic.Frament.NewsFragment;
import com.sunsun.nbapic.Frament.VideoFragment;
import com.sunsun.nbapic.event.ToggleEvent;
import com.sunsun.nbapic.view.TitleBarView;
import com.sunsun.nbapic.view.TitleBarView.OnTitleBarItemClickListener;
import com.sunsun.nbaproject.R;

import de.greenrobot.event.EventBus;

public class MainActivity extends BaseWorkerFragmentActivity implements
		OnClickListener, OnPageChangeListener {

	private static final String TAG = MainActivity.class.getSimpleName();

	private long exitTime = 0;
	private boolean mScrollView = true;
	private TitleBarView mTitleBarView;
	private LinearLayout tabsLinearLayout;
	private ScrollableViewPager mCustomViewPager;
	private ViewPagerAdapter mViewPagerAdapter;
	private String[] tabResources;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
		setContentView(R.layout.main_layout);
		initTitle();
		initView();
	}

	private void initTitle() {
		tabResources = new String[] {
				getResources().getString(R.string.channel),
				getResources().getString(R.string.news),
				getResources().getString(R.string.gallery),
				getResources().getString(R.string.video) };
	}

	private void initView() {

		mTitleBarView = (TitleBarView) findViewById(R.id.title_bar_view);
		mTitleBarView.setRightButtonDrawableLeft(true);
		mTitleBarView.setLeftButtonDrawable(R.drawable.user_center_icon);
		mTitleBarView.setOnItemClickListener(titleBarItemCilckListener);

		tabsLinearLayout = (LinearLayout) findViewById(R.id.ll_tabs);
		findViewById(R.id.tab_channel).setOnClickListener(this);
		findViewById(R.id.tab_news).setOnClickListener(this);
		findViewById(R.id.tab_gallery).setOnClickListener(this);
		findViewById(R.id.tab_video).setOnClickListener(this);

		mCustomViewPager = (ScrollableViewPager) findViewById(R.id.customviewpager);
		mCustomViewPager.setOffscreenPageLimit(1);// 设置viewPager不进行预加载
		mCustomViewPager.setScanScroll(mScrollView);// 设置无法滑动

		ArrayList<Fragment> fragments = initFragments();
		mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),
				fragments);
		mCustomViewPager.setAdapter(mViewPagerAdapter);
		mCustomViewPager.setOnPageChangeListener(this);
	}

	protected ArrayList<Fragment> initFragments() {
		ArrayList<Fragment> fragments = new ArrayList<Fragment>();
		Fragment mChannelFragment = ChinaMainPhotosFragment.newInstance();
		Fragment mNewsFragment = NewsFragment.newInstance();
		Fragment mGalleryFragment = GalleryFragment.newInstance();
		Fragment mVideoFragment = VideoFragment.newInstance();
		fragments.add(mChannelFragment);
		fragments.add(mNewsFragment);
		fragments.add(mGalleryFragment);
		fragments.add(mVideoFragment);
		return fragments;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tab_channel:
			mCustomViewPager.setCurrentItem(0);
			break;
		case R.id.tab_news:
			mCustomViewPager.setCurrentItem(1);
			break;
		case R.id.tab_gallery:
			mCustomViewPager.setCurrentItem(2);
			break;
		case R.id.tab_video:
			mCustomViewPager.setCurrentItem(3);
			break;
		default:
			break;
		}
	}

	@Override
	public void onPageScrollStateChanged(int position) {
	}

	@Override
	public void onPageScrolled(int position, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int position) {
		changeTabStatus(position);
	}

	/**
	 * 切换底部tab的状态
	 * 
	 * @param index
	 */
	private void changeTabStatus(int index) {
		int childCount = tabsLinearLayout.getChildCount();
		for (int i = 0; i < childCount; i++) {
			if (index == i) {
				tabsLinearLayout.getChildAt(i).setSelected(true);
			} else {
				tabsLinearLayout.getChildAt(i).setSelected(false);
			}
		}
		mTitleBarView.setTitle(tabResources[index]);
	}

	private OnTitleBarItemClickListener titleBarItemCilckListener = new OnTitleBarItemClickListener() {

		@Override
		public void onTitleBarItemClick(int index) {
			showToast("titleBarItemCilckListener index is " + index);
		}
	};

	public void onEvent(ToggleEvent event) {
		if (event != null) {

		}
	}

	@Override
	protected void handleBackgroundMessage(Message msg) {

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				showToast("再按一次退出程序");
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				// System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

}
