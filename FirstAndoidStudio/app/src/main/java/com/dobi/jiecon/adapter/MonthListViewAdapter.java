package com.dobi.jiecon.adapter;

import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dobi.jiecon.R;
import com.dobi.jiecon.UtilLog;
import com.dobi.jiecon.database.AppUsage;

public class MonthListViewAdapter extends BaseAdapter {

	protected List<AppUsage> Apps;
	LayoutInflater inflater;
	private final Context context;

	public MonthListViewAdapter(Context context, List<AppUsage> Apps) {
		this.Apps = Apps;
		this.inflater = LayoutInflater.from(context);
		this.context = context;
	}

	public int getCount() {
		return this.Apps.size();
	}

	public AppUsage getItem(int position) {
		return this.Apps.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {		

		String appPkgName = Apps.get(position).getApp_pkgname();
		String appName = getAppName(appPkgName);
		
//		 inflater = (LayoutInflater) context
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.row_layout_new, parent, false);

		ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
		
		TextView appNameView = (TextView) rowView.findViewById(R.id.appName);
		
		TextView appPercentageView = (TextView) rowView.findViewById(R.id.appPercentage);
		
		//Set Icon
		try {		
			Drawable icon = context.getPackageManager().getApplicationIcon(
					Apps.get(position).getApp_pkgname());
			imageView.setImageDrawable(icon);
		} catch (PackageManager.NameNotFoundException e) {
			UtilLog.logControlR("the position is "+position);
			UtilLog.logControlR(Apps.get(position).getApp_pkgname());
			e.printStackTrace();
			
		}
		//Set app name
		appNameView.setText(appName);
		
		//Set the duration		
		appPercentageView.setText(""+Apps.get(position).getDuration());
		
		// textView1.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Toast.makeText(context, "The textView " +
		// "is clicked!",Toast.LENGTH_LONG).show();
		// ;
		// }
		// });
//		imageView.setImageResource(R.drawable.wechat);
		return rowView;
	}

	private String getAppName(String packageName) {
		ApplicationInfo ai;
		PackageManager pm = context.getPackageManager();
		try {
			ai = pm.getApplicationInfo(packageName, 0);
		} catch (final PackageManager.NameNotFoundException e) {

			ai = null;
		}
		final String applicationName = (String) (ai != null ? pm
				.getApplicationLabel(ai) : "(unknown)");
		return applicationName;
	}
}
