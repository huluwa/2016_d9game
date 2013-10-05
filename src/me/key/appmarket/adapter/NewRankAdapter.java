package me.key.appmarket.adapter;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;

import com.market.d9game.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import me.key.appmarket.MarketApplication;
import me.key.appmarket.ImageNet.AsyncImageLoader;
import me.key.appmarket.ImageNet.AsyncImageLoader.ImageCallback;
import me.key.appmarket.network.AppDetailRequest;
import me.key.appmarket.network.AppDetailResponse;
import me.key.appmarket.network.HttpRequest.OnResponseListener;
import me.key.appmarket.network.HttpResponse;
import me.key.appmarket.tool.DownloadService;
import me.key.appmarket.tool.ToolHelper;
import me.key.appmarket.utils.AppInfo;
import me.key.appmarket.utils.AppUtils;
import me.key.appmarket.utils.Global;
import me.key.appmarket.utils.LogUtils;
import me.key.appmarket.widgets.ProgressView;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 排行界面
 * @author Administrator
 *
 */
public class NewRankAdapter extends BaseAdapter {
	private List<AppInfo> appInfos;
	private LayoutInflater lay;
	private File cache;
	private Context mContext;
	private AsyncImageLoader asyncImageLoader;
	// 是否异步加载图片
	public boolean isAsyn;
	// 是否暂停
	private boolean isPause;
	// 是否是下载状态
	private boolean isDownLoading;
	private static final int TYPE_1 = 0;
	private static final int TYPE_2 = 1;
	

	// 设置ImageLoade初始化信息
	private DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.tempicon)
			.showStubImage(R.drawable.tempicon).resetViewBeforeLoading(false)
			.delayBeforeLoading(100).cacheInMemory(true).cacheOnDisc(true)
			.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
			.bitmapConfig(Bitmap.Config.RGB_565).build();

	public NewRankAdapter(List<AppInfo> appInfos, Context context, File cache) {
		super();
		this.appInfos = appInfos;
		this.cache = cache;
		mContext = context;
		lay = LayoutInflater.from(context);

		// asyncImageLoader = new AsyncImageLoader();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return appInfos.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return appInfos.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	// 获取item类型
	@Override
	public int getItemViewType(int position) {
		switch (position) {
		case 0:
			return TYPE_2;
		case 1:
			return TYPE_2;
		case 2:
			return TYPE_2;
		default:
			return TYPE_1;
		}
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(final int position, View convertvView, ViewGroup arg2) {
		final ViewHolder viewHolder;
		final ViewHolder2 viewHolder2;
		Drawable mDrawable;
		int type = getItemViewType(position);
		if (convertvView == null) {
			switch (type) {
			case TYPE_1:
				viewHolder = new ViewHolder();
				convertvView = lay.inflate(R.layout.app_list_item, null);
				viewHolder.icon = (ImageView) convertvView
						.findViewById(R.id.icon2);
				viewHolder.name = (TextView) convertvView
						.findViewById(R.id.app_name2);
				viewHolder.size = (TextView) convertvView
						.findViewById(R.id.appsize2);
				viewHolder.tvdown = (TextView) convertvView
						.findViewById(R.id.tv_down2);
				viewHolder.progress_view = (ProgressView) convertvView
						.findViewById(R.id.progress_view2);
				viewHolder.topname = (TextView) convertvView.findViewById(R.id.topnum_applist_item_tv);
				convertvView.setTag(viewHolder);
				break;

			case TYPE_2:
				viewHolder2 = new ViewHolder2();
				convertvView = lay.inflate(R.layout.item_newrank, null);
				viewHolder2.icon = (ImageView) convertvView
						.findViewById(R.id.icon);
				viewHolder2.name = (TextView) convertvView
						.findViewById(R.id.app_name);
				viewHolder2.size = (TextView) convertvView
						.findViewById(R.id.appsize);
				viewHolder2.tvdown = (TextView) convertvView
						.findViewById(R.id.tv_down);
				viewHolder2.progress_view = (ProgressView) convertvView
						.findViewById(R.id.progress_view);
				viewHolder2.descri = (TextView) convertvView.findViewById(R.id.item_newrank_tv);
				viewHolder2.top = (ImageView) convertvView.findViewById(R.id.item_newrank_top_iv);
				convertvView.setTag(viewHolder2);
				break;
			}

		} else {
			switch (type) {
			case TYPE_1:
				
				viewHolder = (ViewHolder) convertvView.getTag();
				break;

			case TYPE_2:
				viewHolder2 = (ViewHolder2) convertvView.getTag();
				break;
			}
		}
		switch (type) {
		case TYPE_1:
			final ViewHolder v1 = ((ViewHolder) convertvView.getTag());
			fillData(position, v1);
			v1.topname.setText(position+1+"");
			break;
		case TYPE_2:
			final ViewHolder2 v2 = ((ViewHolder2) convertvView.getTag());
			fillData(position, v2);
			switch (position) {
			case 0:
				v2.top.setImageResource(R.drawable.top1);
				getDesc(0, v2);
				break;

			case 1:
				v2.top.setImageResource(R.drawable.top2);
				getDesc(1, v2);
				break;
			case 2:
				v2.top.setImageResource(R.drawable.top3);
				getDesc(2, v2);
				break;
			}
			
			
			
			LogUtils.d("NewRank", appInfos.get(position).getAppDescri()+"");
			break;
		}
	
		return convertvView;
	}

	public void getDesc(final int position, final ViewHolder2 v2) {
		new AppDetailRequest(appInfos.get(position).getIdx()).execute(new OnResponseListener(){

			@Override
			public void onGetResponse(HttpResponse resp) {
				final AppDetailResponse response = (AppDetailResponse) resp;
				if (response != null) {
					v2.descri.setText(response.getAppDes());
				}
			}
			
		});
	}

	public void fillData(final int position, final BaseHolder v1) {
		Drawable mDrawable;
		v1.name.setText(appInfos.get(position).getAppName());
		v1.size.setText(ToolHelper.Kb2Mb(appInfos.get(position)
				.getAppSize()));
		// 给view设置唯一tag
		v1.icon.setTag(appInfos.get(position).getIconUrl());
		ImageLoader.getInstance().displayImage(
				appInfos.get(position).getIconUrl(), v1.icon, options);
		// final Drawable drawable;
		// if (!isAsyn) {
		// drawable = getDrawable(asyncImageLoader, appInfos.get(position)
		// .getIconUrl(), viewHolder.icon);
		// } else {
		// String imageUrl = appInfos.get(position).getIconUrl();
		// HashMap<String, SoftReference<Drawable>> imageCache =
		// asyncImageLoader.imageCache;
		// if (imageCache.containsKey(imageUrl)) {
		// SoftReference<Drawable> softReference = imageCache
		// .get(imageUrl);
		// Drawable icon = softReference.get();
		// viewHolder.icon.setImageDrawable(icon);
		// drawable = icon;
		// } else {
		// drawable = null;
		// }
		// }
		// if (drawable != null) {
		// viewHolder.icon.setImageBitmap(DownloadService
		// .drawable2Bitmap(drawable));
		// // 如果图片为Null,则设置默认图片
		// } else {
		//
		// viewHolder.icon.setImageResource(R.drawable.tempicon);
		// }
		// TODO Auto-generated method stub

		// asyncloadImage(viewHolder.icon, appInfos.get(position).getIconUrl());

		v1.progress_view.setProgress(0);
		v1.progress_view.setVisibility(View.VISIBLE);
		File tempFile = new File(Environment.getExternalStorageDirectory(),
				"/market/" + appInfos.get(position).getAppName() + ".apk");
		SharedPreferences sp = mContext.getSharedPreferences("down",
				mContext.MODE_PRIVATE);
		boolean isDownLoaded = DownloadService.isDownLoaded(appInfos.get(
				position).getAppName());
		int idx = Integer.parseInt(appInfos.get(position).getIdx());
		isDownLoading = DownloadService.isDownLoading(idx);

		if (appInfos.get(position).isIspause()) {
		
			v1.tvdown.setText("暂停");
			v1.progress_view.setProgress(DownloadService
					.getPrecent(idx));
		} else {
			v1.tvdown.setText("下载中");

		}
		if (appInfos.get(position).isInstalled()) {
			v1.tvdown.setText("打开");

			v1.progress_view.setProgress(100);
			Drawable mDrawableicon = mContext.getResources().getDrawable(
					R.drawable.action_type_software_update);
			v1.tvdown.setCompoundDrawablesWithIntrinsicBounds(null,
					mDrawableicon, null, null);
		} else if (appInfos.get(position).isDown()) {

			v1.progress_view.setProgress(DownloadService
					.getPrecent(idx));
			LogUtils.d("ture", isDownLoading + "isDown");

			v1.tvdown.setText("下载中");
			Drawable mDrawableicon = mContext.getResources().getDrawable(
					R.drawable.downloading);
			v1.tvdown.setCompoundDrawablesWithIntrinsicBounds(null,
					mDrawableicon, null, null);
		} else if (isDownLoaded) {
			Drawable mDrawableicon = mContext.getResources().getDrawable(
					R.drawable.downloaded);
			v1.tvdown.setCompoundDrawablesWithIntrinsicBounds(null,
					mDrawableicon, null, null);
			v1.tvdown.setText("安装");
			v1.progress_view.setProgress(100);
		} else if (!isDownLoading) {
			v1.tvdown.setText("下载");
			mDrawable = mContext.getResources().getDrawable(
					R.drawable.downloading);
			v1.tvdown.setCompoundDrawablesWithIntrinsicBounds(null,
					mDrawable, null, null);
			// 获取将要下载的文件名，如果本地存在该文件，则取出该文件

			// LogUtils.d("sa", tempFile.getAbsolutePath());

			long length = sp.getLong(tempFile.getAbsolutePath(), 0);
			// LogUtils.d("sa", length+"");
			if (length != 0
					&& DownloadService.isExist(appInfos.get(position)
							.getAppName())) {
				LogUtils.d("test", "已经存在");
				v1.tvdown.setText("已下载");

				long count = sp.getLong(tempFile.getAbsolutePath() + "precent",
						0);
				v1.progress_view.setProgress(count);
			} else if (length != 0
					&& !DownloadService.isExist(appInfos.get(position)
							.getAppName())) {
				Editor edit = sp.edit();
				edit.remove(tempFile.getAbsolutePath());
				edit.commit();
			}
		}

		v1.tvdown.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (appInfos.get(position).isInstalled()) {
					AppUtils.launchApp(mContext, appInfos.get(position)
							.getAppName());
				} else if (DownloadService.isDownLoading(Integer
						.parseInt(appInfos.get(position).getIdx()))) {
					LogUtils.d("test", "暂停");
				
					File tempFile = DownloadService.CreatFileName(appInfos.get(
							position).getAppName());
					Intent intent = new Intent();
					intent.setAction(tempFile.getAbsolutePath());
					mContext.sendBroadcast(intent);
					if (!appInfos.get(position).isIspause()) {
						v1.tvdown.setText("暂停");
						LogUtils.d("暂停", appInfos.get(position).getAppName() + "");
						appInfos.get(position).setDown(false);
					} else {
						v1.tvdown.setText("下载中");
						appInfos.get(position).setDown(true);
					}
					LogUtils.d("down", appInfos.get(position).isDown() + "");
					LogUtils.d("test", appInfos.get(position).isIspause() + "1");
					appInfos.get(position).setIspause(
							!appInfos.get(position).isIspause());
					LogUtils.d("test", appInfos.get(position).isIspause() + "2");
				} else if (DownloadService.isDownLoaded(appInfos.get(position)
						.getAppName())) {
					// 已经下载
					DownloadService.Instanll(appInfos.get(position)
							.getAppName(), mContext);
				} else if (!appInfos.get(position).isInstalled()) {
					Log.e("tag",
							"appurl = " + Global.MAIN_URL
									+ appInfos.get(position).getAppUrl());
					Log.e("tag",
							"appIdx = "
									+ Integer.parseInt(appInfos.get(position)
											.getIdx()));
					/*
					 * Log.e("tag", "appname = " +
					 * appInfos.get(position).getAppName());
					 */
					SharedPreferences sp = mContext.getSharedPreferences(
							"down", mContext.MODE_PRIVATE);
					File tempFile = new File(Environment
							.getExternalStorageDirectory(), "/market/"
							+ appInfos.get(position).getAppName() + ".apk");

					long length = sp.getLong(tempFile.getAbsolutePath(), 0);
					/*
					 * DownloadService.downNewFile(appInfos.get(position)
					 * .getAppUrl(), Integer.parseInt(appInfos.get(
					 * position).getIdx()), appInfos.get(position)
					 * .getAppName(),length,0);
					 */
					DownloadService.downNewFile(appInfos.get(position), length,
							0, null);
					appInfos.get(position).setDown(true);
					Intent intent = new Intent();
					intent.setAction(MarketApplication.PRECENT);
					mContext.sendBroadcast(intent);

					Toast.makeText(mContext,
							appInfos.get(position).getAppName() + " 开始下载...",
							Toast.LENGTH_SHORT).show();
				}

			}

		});
	}

	static class ViewHolder extends BaseHolder{
	}

	static class ViewHolder2 extends BaseHolder{
	}
	
	static class BaseHolder{
		ImageView icon;
		TextView topname;
		ImageView top;
		TextView name;
		TextView size;
		TextView tvdown;
		TextView descri;
		ProgressView progress_view;
	}

	private void asyncloadImage(ImageView iv_header, String path) {
		AsyncImageTask task = new AsyncImageTask(iv_header);
		task.execute(path);
	}

	private final class AsyncImageTask extends AsyncTask<String, Integer, Uri> {

		private ImageView iv_header;

		public AsyncImageTask(ImageView iv_header) {
			this.iv_header = iv_header;
		}

		@Override
		protected Uri doInBackground(String... params) {
			try {
				return ToolHelper.getImageURI(params[0], cache);
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(Uri result) {
			super.onPostExecute(result);
			if (iv_header != null && result != null) {
				iv_header.setImageURI(result);
			}
		}
	}

	public Drawable getDrawable(AsyncImageLoader asyncImageLoader,
			String imageUrl, final ImageView imageView) {
		Drawable drawable = asyncImageLoader.loadDrawable(imageUrl,
				new ImageCallback() {
					@Override
					public void imageLoaded(Drawable imageDrawable,
							String imageUrl) {
						// 如果当前view的标记和draw的标记一致，则将图片设置
						if (imageDrawable != null
								&& imageView.getTag().equals(imageUrl))
							// imageView.setImageDrawable(imageDrawable);
							imageView.setImageBitmap(DownloadService
									.drawable2Bitmap(imageDrawable));
						/*
						 * else imageView.setImageResource(R.drawable.tempicon);
						 */
					}
				});
		return drawable;
	}
}