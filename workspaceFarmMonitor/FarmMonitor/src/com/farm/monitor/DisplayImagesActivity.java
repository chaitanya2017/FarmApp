package com.farm.monitor;

import java.io.File;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/*
 * Activity for displaying images
 */
public class DisplayImagesActivity extends Activity {

	private ImageAdapter imageAdapter;
	ArrayList<String> f = new ArrayList<String>();// list of file paths
	File[] listFile;

	/*
	 * creates activity controls
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_images);
		getImages();
		GridView imagegrid = (GridView) findViewById(R.id.gridViewDiplayImages);
		imageAdapter = new ImageAdapter();
		imagegrid.setAdapter(imageAdapter);

	}

	/*
	 * read images from storage card
	 */
	public void getImages() {
		File file = new File(android.os.Environment.getExternalStorageDirectory(), "farmimages");

		if (file.isDirectory()) {
			listFile = file.listFiles();

			for (int i = 0; i < listFile.length; i++) {
				f.add(listFile[i].getAbsolutePath());
			}
		}
	}

	/*
	 * This class displays images in a grid
	 */
	class ImageAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public ImageAdapter() {
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			return f.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		/*
		 * show the image 
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.imagedisplayitem, null);
				holder.imageview = (ImageView)
				convertView.findViewById(R.id.thumbImage);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Bitmap myBitmap = BitmapFactory.decodeFile(f.get(position));
			holder.imageview.setImageBitmap(myBitmap);
			return convertView;
		}
	}

	class ViewHolder {
		ImageView imageview;
	}
}