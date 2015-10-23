package com.sunsun.nbapic.Frament;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kugou.framework.component.base.BaseEmptyFragment;
import com.sunsun.nbapic.adapter.GalleryAdapter;
import com.sunsun.nbapic.bean.ZoomPhotosTable.PicAddress;
import com.sunsun.nbapic.http.SimpleHttp;
import com.sunsun.nbapic.util.AnimationHelper;
import com.sunsun.nbaproject.R;
import com.sunsun.nbaproject.viewpager.ScrollBannerView;

public class ViewPagerZoomFragment extends BaseEmptyFragment implements
		OnPageChangeListener, OnClickListener {

	public static final String TAG = ViewPagerZoomFragment.class
			.getSimpleName();

	public final int REQUEST_CODE = 100;
	public final int REPONSE_CODE = 200;
	public final int REQUEST_MORE_CODE = 300;
	public final int REPONSE_MORE_CODE = 400;
	public final int REPONSE_NO_DATA_CODE = 500;

	public static final int FROM_ARTILCE = 1;
	public static final int FROM_NEWS_LIST = 2;

	public static final String KEY_SELECT_POS = "select_pos";
	public static final String LIST_KEY = "list_key";
	public static final String TYPE_KEY = "type_key";

	public static final int POSITION_FIRST = 0;
	public static final int POSITION_LAST = -1;
	private final int FILT_DURATION = 20;

	private ScrollBannerView mBanner;
	private GalleryAdapter mGalleryAdapter;

	private LinearLayout mBottomBar;
	private TextView mTitleTextView;
	private TextView mPageSize;
	private TextView mSubTitle;
	private ImageButton mBackImageView;

	private Animation mAnimRadioUpToDownIn;
	private Animation mAnimRadioUpToDownOut;

	private String url;
	private List<PicAddress> mDatasource;
	private int mSelectPos;

	private boolean isAnimating;
	private boolean mAllBarVisible = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bunble = getArguments();
		if (bunble != null) {
			url = bunble.getString(LIST_KEY);
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initAmin();
		updateAdapterData(mSelectPos);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.zoom_gallery_activity, container,
				false);
		mBanner = (ScrollBannerView) view.findViewById(R.id.pager);
		setContainer(mBanner);
		mBottomBar = (LinearLayout) view.findViewById(R.id.top_bar);
		mBackImageView = (ImageButton) view.findViewById(R.id.back);
		mTitleTextView = (TextView) view.findViewById(R.id.title);
		mSubTitle = (TextView) view.findViewById(R.id.sub_title);
		mPageSize = (TextView) view.findViewById(R.id.page_number);
		mBackImageView.setOnClickListener(this);
		mGalleryAdapter = new GalleryAdapter(getActivity());
		mGalleryAdapter.setOnViewTapListener(listener);
		mBanner.setListAdapter(mGalleryAdapter);
		mBanner.setOnPageChangeListener(this);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		showView(VIEW_TYPE_LOADING);
		sendEmptyBackgroundMessage(REQUEST_CODE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			getActivity().onBackPressed();
			break;
		default:
			break;
		}

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int position) {
		updateCurrentPos(position);
		mSelectPos = position;
	}

	@Override
	public void onPageScrollStateChanged(int position) {

	}

	private void initAmin() {
		mAnimRadioUpToDownIn = AnimationHelper.createAnimUpToDownIn(
				getActivity(), mAnimListener);
		mAnimRadioUpToDownOut = AnimationHelper.createAnimUpToDownOut(
				getActivity(), mAnimListener);
	}

	private static final int MSG_SHOW_RADIO = 1001;
	private static final int MSG_HIDE_RADIO = 1002;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_SHOW_RADIO: {
				showMainRadioNow();
				break;
			}
			case MSG_HIDE_RADIO: {
				hideMainRadioNow();
				break;
			}
			}
		}
	};

	private void showMainRadioNow() {
		if (isAnimating) {
			return;
		}
		mAnimRadioUpToDownIn.cancel();
		mBottomBar.startAnimation(mAnimRadioUpToDownIn);
	}

	private void hideMainRadioNow() {
		if (isAnimating) {
			return;
		}
		mAnimRadioUpToDownOut.cancel();
		mBottomBar.startAnimation(mAnimRadioUpToDownOut);
	}

	private OnViewTapListener listener = new OnViewTapListener() {

		@Override
		public void onViewTap(View view, float x, float y) {
			if (mAllBarVisible) {
				hideMainRadio(FILT_DURATION);
			} else {
				showMainRadio(FILT_DURATION);
			}
		}

	};

	private void showMainRadio(int delay) {
		if (!mHandler.hasMessages(MSG_SHOW_RADIO)) {
			mHandler.removeMessages(MSG_HIDE_RADIO);
			mHandler.sendEmptyMessageDelayed(MSG_SHOW_RADIO, delay);
		}
	}

	private void hideMainRadio(int delay) {
		if (!mHandler.hasMessages(MSG_HIDE_RADIO)) {
			mHandler.removeMessages(MSG_SHOW_RADIO);
			mHandler.sendEmptyMessageDelayed(MSG_HIDE_RADIO, delay);
		}
	}

	private AnimationListener mAnimListener = new AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {
			isAnimating = true;
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			isAnimating = false;
			if (mAllBarVisible) {
				mAllBarVisible = false;
				onHeaderFooterHide();
			} else {
				mAllBarVisible = true;
				onHeaderFooterShow();
			}
		}
	};

	protected void onHeaderFooterShow() {
		mBackImageView.setClickable(true);
	}

	protected void onHeaderFooterHide() {
		mBackImageView.setClickable(false);
	}

	private void updateAdapterData(int selectPos) {
		if (mDatasource != null) {
			if (selectPos >= mDatasource.size()) {
				selectPos = 0;
			}
			if (selectPos == POSITION_LAST) {
				selectPos = getImageSum() - 1;
			}
			mGalleryAdapter.setDataSource(mDatasource);
			mBanner.setCurrentItem(selectPos, false);
			updateCurrentPos(selectPos);
		}
	}

	private void updateCurrentPos(int position) {
		String text = String.format("%d/%d", getImagePos(position),
				getImageSum());
		SpannableString spanText = new SpannableString(text);
		int index = text.indexOf("/");
		spanText.setSpan(new AbsoluteSizeSpan(14, true), 0, index,
				Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		spanText.setSpan(new ForegroundColorSpan(Color.WHITE), 0, index,
				Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		mPageSize.setText(spanText);
		if (mGalleryAdapter != null) {
			PicAddress item = mGalleryAdapter.getItem(position);
			if (!TextUtils.isEmpty(item.getTitle())) {
				mTitleTextView.setText(item.getTitle());
			}
			if (!TextUtils.isEmpty(item.getSubTitle())) {
				mSubTitle.setText(item.getSubTitle());
			}
		}
	}

	protected int getImagePos(int pagePos) {
		return pagePos + 1;
	}

	protected int getImageSum() {
		return mGalleryAdapter.getCount();
	}

	@Override
	protected void handleBackgroundMessage(Message msg) {
		switch (msg.what) {
		case REQUEST_CODE:
			if (TextUtils.isEmpty(url)) {
				return;
			}
			try {
				byte[] requestData = SimpleHttp.RequestGet(url);
				String data = new String(requestData, "UTF-8");
				Document parse = Jsoup.parse(data, url);
				Element body = parse.body();
				List<PicAddress> listData = new ArrayList<PicAddress>();

				Elements elementsTitle = body.getElementsByClass("title");
				String title = elementsTitle.get(elementsTitle.size() - 1).text()
						.trim();

				Elements elements = body.getElementsByClass("content");
				Elements elementsList = elements.get(0).getElementsByClass(
						"image");

				for (int i = 0; i < elementsList.size(); i++) {
					PicAddress item = new PicAddress();
					Elements tags = elementsList.get(i).getElementsByTag("img");
					String imageUrl = tags.attr("src");
					String subTitle = elements.get(0)
							.getElementsByTag("p").get(i).text().trim();
					item.setId(i);
					if (!TextUtils.isEmpty(imageUrl)) {
						item.setImageUrl(imageUrl);
					}
					if (!TextUtils.isEmpty(subTitle)) {
						item.setSubTitle(subTitle);
					}
					if (!TextUtils.isEmpty(title)) {
						item.setTitle(title);
					}
					listData.add(item);
				}
				Message requsetMsg = new Message();
				requsetMsg.what = REPONSE_CODE;
				requsetMsg.obj = listData;
				sendUiMessage(requsetMsg);
			} catch (Exception e) {
				e.printStackTrace();
				sendEmptyUiMessage(REPONSE_NO_DATA_CODE);
			}
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleUiMessage(Message msg) {
		super.handleUiMessage(msg);
		switch (msg.what) {
		case REPONSE_CODE:
			if (msg != null && msg.obj instanceof List<?>) {
				List<PicAddress> data = (List<PicAddress>) msg.obj;
				if (data != null) {
					mGalleryAdapter.setDataSource(data);
					updateCurrentPos(0);
					showView(VIEW_TYPE_DATA);
				} else {
					showView(VIEW_TYPE_EMPTY);
				}
			}
			break;
		case REPONSE_NO_DATA_CODE:
			showView(VIEW_TYPE_EMPTY);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onEmptyViewClicked() {
		super.onEmptyViewClicked();
		showView(VIEW_TYPE_LOADING);
		sendEmptyBackgroundMessage(REQUEST_CODE);
	}

	protected String tag() {
		return TAG;
	}
}
