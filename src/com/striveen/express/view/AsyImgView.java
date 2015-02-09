package com.striveen.express.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.striveen.express.util.LogUtil;

public class AsyImgView extends ImageView {
	private final String TAG = getClass().getSimpleName();
	private String imgurl;
	private int defaultResource;
	private String fliesPath;

	public AsyImgView(Context context) {
		super(context);
	}

	public AsyImgView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AsyImgView(Context context, int defaultResource) {
		super(context);
		this.defaultResource = defaultResource;
	}

	public AsyImgView(Context context, AttributeSet attrs, int defaultResource) {
		super(context, attrs);
		this.defaultResource = defaultResource;
	}

	public void setDefaultResource(int defaultResource) {
		this.defaultResource = defaultResource;
	}

	public void setFliesPath(String fliesPath) {
		this.fliesPath = fliesPath;
	}

	public void setImgUrl(String imgurl) {
		synchronized (this) {
			if (imgurl.equals(this.imgurl)) {
				return;
			}

			if (this.defaultResource != 0) {
				setImageResource(this.defaultResource);
			}

			if (TextUtils.isEmpty(imgurl)) {
				return;
			}

			this.imgurl = imgurl;
			new GetDataTask(imgurl).execute(new Void[0]);
		}
	}

	public void reLoad() {
		if (TextUtils.isEmpty(this.imgurl)) {
			return;
		}
		if (this.defaultResource != 0) {
			setImageResource(this.defaultResource);
		}

		synchronized (AsyImgConfig.imgsCache) {
			Bitmap bitmap = (Bitmap) AsyImgConfig.imgsCache
					.get(this.imgurl);
			if (bitmap != null) {
				AsyImgConfig.imgsCache.remove(this.imgurl);
				bitmap.recycle();
			}
		}

		if (!TextUtils.isEmpty(AsyImgConfig.appImgsFilesPath)) {
			String imgurl = new AsyImgFileCacheUtil()
			.imgPathToFilePath_PNG(this.imgurl);
			File file = new File(AsyImgConfig.appImgsFilesPath
					+ imgurl);
			if (file.exists()) {
				file.delete();
			}
		}
		if (!TextUtils.isEmpty(this.fliesPath)) {
			String imgurl = new AsyImgFileCacheUtil()
			.imgPathToFilePath_PNG(this.imgurl);
			File file = new File(this.fliesPath + imgurl);
			if (file.exists()) {
				file.delete();
			}
		}

		new GetDataTask(this.imgurl).execute(new Void[0]);
	}

	private class GetDataTask extends AsyncTask<Void, Void, String> {
		private Bitmap bitmap;
		private String imgUrl;
		private AsyImgFileCacheUtil util;

		public GetDataTask(String imgUrl) {
			this.imgUrl = imgUrl;
			this.util = new AsyImgFileCacheUtil();
		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if ("true".equals(result)) {
				LogUtil.Log(TAG, "asyImgView imgUrl = " + AsyImgView.this.imgurl);
				LogUtil.Log(TAG, "GetDataTask imgUrl = " + this.imgUrl);
				if (AsyImgView.this.imgurl == this.imgUrl) {
					if (this.bitmap != null) {
						AsyImgView.this.setImageBitmap(this.bitmap);
					} else {
						AsyImgView.this
								.setImageResource(AsyImgView.this.defaultResource);
					}
				}
			} else
				AsyImgView.this
						.setImageResource(AsyImgView.this.defaultResource);
		}

		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected String doInBackground(Void[] params) {
			if (AsyImgConfig.imgsCache != null) {
				this.bitmap = ((Bitmap) AsyImgConfig.imgsCache
						.get(this.imgUrl));
				if (this.bitmap != null) {
					return "true";
				}
			}
			String bitmapName = null;
			if (!TextUtils.isEmpty(AsyImgView.this.fliesPath)) {
				bitmapName = this.util
						.imgPathToFilePath_PNG(this.imgUrl);
				try {
					File dir = new File(AsyImgView.this.fliesPath);
					this.bitmap = BitmapFactory
							.decodeFile(AsyImgView.this.fliesPath +
							bitmapName);
					return "true";
				} catch (Exception localException1) {
				}
			}
			else if (!TextUtils.isEmpty(AsyImgConfig.appImgsFilesPath)) {
				bitmapName = this.util
						.imgPathToFilePath_PNG(this.imgUrl);
				try {
					File dir = new File(AsyImgConfig.appImgsFilesPath);
					if (dir.exists()) {
						this.bitmap =
						BitmapFactory
								.decodeFile(AsyImgConfig.appImgsFilesPath +
								bitmapName);
						if (this.bitmap != null)
							return "true";
					}
				} catch (Exception localException2) {
				}
			}
			InputStream stream = null;
			try {
				URL url = new URL(this.imgUrl);
				URLConnection conn = url.openConnection();
				conn.setConnectTimeout(3000);
				conn.setReadTimeout(2000);
				conn.connect();
				int lenth = conn.getContentLength();
				Log.w(TAG, "lenth = "+lenth);
				if ((lenth > AsyImgConfig.imgFileSizeMaxValue) &&
				(AsyImgConfig.isCheckImgFileSizeMax)) {
					Log.w(AsyImgView.this.TAG,
									"image path : "
											+
											this.imgUrl
											+
											" , Picture file over 700k does not allow the download, or will likely lead outofmemory");
					return "false";
				}
				stream = conn.getInputStream();

				this.bitmap = BitmapFactory.decodeStream(stream);
				stream.close();
				if (AsyImgConfig.autoCreatScaled) {
					int width = AsyImgView.this.getWidth();
					int height = AsyImgView.this.getHeight();
					if ((width > 0) && (height > 0)) {
					this.bitmap = Bitmap.createScaledBitmap(
								this.bitmap, width,
								height, true);
					}
				}
				
				if (this.bitmap == null) {
					return "false";
				}
				if (AsyImgConfig.imgsCache != null) {
					synchronized (AsyImgConfig.imgsCache) {
						AsyImgConfig.imgsCache.put(this.imgUrl,
								this.bitmap);
					}
				}
				if (!TextUtils.isEmpty(AsyImgView.this.fliesPath)) {
					if (bitmapName == null)
						bitmapName = this.util
								.imgPathToFilePath_PNG(this.imgUrl);
					try {
						File dir = new File(AsyImgView.this.fliesPath);
						if (!dir.exists()) {
							dir.mkdirs();
						}
						File bitmapFile = new File(
								AsyImgView.this.fliesPath + bitmapName);
						if (bitmapFile.exists()) {
							bitmapFile.delete();
						}
						bitmapFile.createNewFile();

						FileOutputStream fos = new FileOutputStream(
								bitmapFile);
						this.bitmap.compress(
								Bitmap.CompressFormat.PNG, 100, fos);
						fos.close();
					} catch (Exception e) {
						Log.w(AsyImgView.this.TAG,
										"Save Image Error , save image path : " + AsyImgView.this.fliesPath + bitmapName);
					}
				}
				else if (!TextUtils
						.isEmpty(AsyImgConfig.appImgsFilesPath)) {
					if (bitmapName == null)
						bitmapName = this.util
								.imgPathToFilePath_PNG(this.imgUrl);
					try {
						File dir = new File(
								AsyImgConfig.appImgsFilesPath);
						if (!dir.exists()) {
							dir.mkdirs();
						}
						File bitmapFile = new File(
						AsyImgConfig.appImgsFilesPath + bitmapName);
						if (bitmapFile.exists()) {
							bitmapFile.delete();
						}
						bitmapFile.createNewFile();

						FileOutputStream fos = new FileOutputStream(
								bitmapFile);
						this.bitmap.compress(
								Bitmap.CompressFormat.PNG, 100, fos);
						fos.close();
					} catch (Exception e) {
						Log.w(AsyImgView.this.TAG,
								"Save Image Error , save image path : " +
								AsyImgConfig.appImgsFilesPath
										+ bitmapName);
					}
				}
			} catch (Exception e) {
				Log.w(AsyImgView.this.TAG,
						"Image path error , image path : " + this.imgUrl);
				Log.w(TAG, e.toString());
			}
			URLConnection conn;
			URL url;
			return "true";
		}
	}
}
