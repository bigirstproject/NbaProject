package com.sunsun.nbapic.Frament;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.kugou.framework.component.base.BaseEmptyFragment;
import com.kugou.framework.component.base.BaseWorkerFragment;
import com.sunsun.nbapic.adapter.ChinaMainPhotosAdapter;
import com.sunsun.nbapic.bean.ChinaMainPhotosTable;
import com.sunsun.nbapic.http.SimpleHttp;
import com.sunsun.nbapic.util.Constants;
import com.sunsun.nbapic.util.ToastUtil;
import com.sunsun.nbaproject.R;

public class ChinaMainPhotosFragment extends BaseEmptyFragment {
	private PullToRefreshListView mPullRefreshListView;
	private ListView mListView;
	private ChinaMainPhotosAdapter mAdapter;
	private List<ChinaMainPhotosTable> list;
	private int pagesize = 0;

	public static ChinaMainPhotosFragment newInstance() {
		ChinaMainPhotosFragment fragment = new ChinaMainPhotosFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.common_listview_layout, null,
				true);
		mPullRefreshListView = (PullToRefreshListView) view
				.findViewById(R.id.pull_refresh_list);
		setContainer(mPullRefreshListView);
		mPullRefreshListView.setMode(Mode.BOTH);
		mListView = mPullRefreshListView.getRefreshableView();
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		showView(VIEW_TYPE_LOADING);
		sendEmptyBackgroundMessage(REQUEST_CODE);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mPullRefreshListView
				.setOnRefreshListener(new CustomOnRefreshListener2());
		mAdapter = new ChinaMainPhotosAdapter(getActivity());
		mListView.setAdapter(mAdapter);
	}

	class CustomOnRefreshListener2 implements OnRefreshListener2<ListView> {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			mPullRefreshListView.setMode(Mode.BOTH);
			sendEmptyBackgroundMessage(REQUEST_CODE);
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			if (list != null) {
				if (list.size() > 0) {
					Message requsetMsg = new Message();
					requsetMsg.what = REQUEST_MORE_CODE;
					requsetMsg.arg1 = pagesize;
					sendBackgroundMessage(requsetMsg);
				} else {
					mPullRefreshListView.setMode(Mode.PULL_FROM_START);
					mPullRefreshListView.onRefreshComplete();
					ToastUtil.showToast("亲，已经加载到最后一页");
				}
			}
		}
	}

	public final int REQUEST_CODE = 0;
	public final int REPONSE_CODE = 1;
	public final int REQUEST_MORE_CODE = 2;
	public final int REPONSE_MORE_CODE = 3;

	@Override
	protected void handleBackgroundMessage(Message msg) {
		switch (msg.what) {
		case REQUEST_CODE:
			try {
				byte[] requestData = SimpleHttp.RequestGet(Constants.BASE_URL);
				String data = new String(requestData, "GBK");
				Document parse = Jsoup.parse(data, Constants.BASE_URL);
				Element body = parse.body();
				list = new ArrayList<ChinaMainPhotosTable>();
				Elements elements = body.getElementsByClass("news-list");
				int id = 0;
				for (int i = 0; i < elements.size(); i++) {
					Log.d("ChinaMainPhotosFragment", "elements.size() ="
							+ elements.size());
					Element element = elements.get(i);
					Elements elementsPic = element
							.getElementsByClass("news-pic");
					for (int j = 0; j < elementsPic.size(); j++) {
						ChinaMainPhotosTable item = new ChinaMainPhotosTable();
						Element elementPic = elementsPic.get(j);
						String jumpUrl = elementPic.getElementsByTag("a")
								.get(0).attr("href");
						String imageUrl = elementPic
								.getElementsByClass("big-news-img").get(0)
								.getElementsByTag("img").get(0).attr("src");
						String title = elementPic.getElementsByClass("news-hd")
								.get(0).text();
						String date = elementPic
								.getElementsByClass("news-time").get(0).text();
						item.setId(id);
						item.setPicUrl(imageUrl);
						item.setTitle(title);
						item.setDate(date);
						item.setJumpUrl(jumpUrl);
						list.add(item);
						id++;
					}
				}
				ArrayList<ChinaMainPhotosTable> currentList = new ArrayList<ChinaMainPhotosTable>();
				int count = 0;
				while (list.size() > 0) {
					currentList.add(list.remove(0));
					if (count >= 9) {
						break;
					}
					count++;
				}
				Message requsetMsg = new Message();
				requsetMsg.what = REPONSE_CODE;
				requsetMsg.obj = currentList;
				sendUiMessage(requsetMsg);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case REQUEST_MORE_CODE:
			if (msg != null) {
				int pagesize = (int) msg.arg1;
				if (pagesize < 0) {
					return;
				}
				try {
					List<ChinaMainPhotosTable> currentList = new ArrayList<ChinaMainPhotosTable>();
					int count = 0;
					while (list.size() > 0) {
						currentList.add(list.remove(0));
						if (count >= 9) {
							break;
						}
						count++;
					}
					Message requsetMsg = new Message();
					requsetMsg.what = REPONSE_MORE_CODE;
					requsetMsg.obj = currentList;
					sendUiMessage(requsetMsg);
				} catch (Exception e) {
					e.printStackTrace();
				}
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
				List<ChinaMainPhotosTable> list = (List<ChinaMainPhotosTable>) msg.obj;
				if (list != null) {
					mAdapter.setDataSource(list);
					mPullRefreshListView.onRefreshComplete();
					showView(VIEW_TYPE_DATA);
				}else{
					showView(VIEW_TYPE_EMPTY);
				}
			}
			break;
		case REPONSE_MORE_CODE:
			if (msg != null && msg.obj instanceof List<?>) {
				List<ChinaMainPhotosTable> list = (List<ChinaMainPhotosTable>) msg.obj;
				if (list != null) {
					mAdapter.addDataSource(list);
					mPullRefreshListView.onRefreshComplete();
					pagesize++;
				}
			}
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

}
