package com.sunsun.nbapic.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kugou.framework.component.base.ImageAdapter;
import com.sunsun.nbapic.activity.ViewPagerZoomActivity;
import com.sunsun.nbapic.bean.ChinaMainPhotosTable;
import com.sunsun.nbaproject.R;

public class ChinaMainPhotosAdapter extends ImageAdapter<ChinaMainPhotosTable> {

	public final int STYLE_ONE = 0;

	public ChinaMainPhotosAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		int itemViewType = getItemViewType(position);
		if (convertView == null) {
			convertView = getConvertView(itemViewType);
		}
		holder = (ViewHolder) convertView.getTag();
		ChinaMainPhotosTable item = getItem(position);
		updateCorner(holder, item, convertView);
		return convertView;
	}

	private void updateCorner(ViewHolder holder, ChinaMainPhotosTable item,
			View convertView) {
		if (item == null) {
			return;
		}
		displayImage(item.getPicUrl(), holder.imageview);
		if (!TextUtils.isEmpty(item.getTitle())) {
			holder.title.setText(item.getTitle());
		}
		if (!TextUtils.isEmpty(item.getDate())) {
			holder.date.setText(item.getDate());
		}
		holder.view.setOnClickListener(new OnClickItem(item));
	}

	class OnClickItem implements OnClickListener {
		ChinaMainPhotosTable item;

		public OnClickItem(ChinaMainPhotosTable item) {
			this.item = item;
		}

		@Override
		public void onClick(View v) {
			// Toast.makeText(mContext, item.getJumpUrl(), Toast.LENGTH_SHORT)
			// .show();
			if(item!=null&&!TextUtils.isEmpty(item.getJumpUrl())){
				ViewPagerZoomActivity.startViewPagerZoomActivity(mContext,
						item.getJumpUrl());
			}
		}
	}

	private View getConvertView(int itemViewType) {
		ViewHolder holder = null;
		View convertView = null;
		switch (itemViewType) {
		case STYLE_ONE:
			convertView = mInflater.inflate(R.layout.china_main_photos_layout,
					null);
			holder = new ViewHolder();
			holder.view = convertView;
			holder.imageview = (ImageView) convertView
					.findViewById(R.id.imageview);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.date = (TextView) convertView.findViewById(R.id.date);
			break;
		default:
			break;
		}
		if (convertView != null) {
			convertView.setTag(holder);
		}
		return convertView;
	}

	@Override
	public int getItemViewType(int position) {
		return 0;
	}

	public int getViewTypeCount() {
		return 1;
	}

	private static class ViewHolder {
		View view;
		ImageView imageview;
		TextView title;
		TextView date;
	}

}
