package com.dobi.jiecon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.dobi.jiecon.R;

public class MainListViewAdapter extends ArrayAdapter<String> {

	private final Context context;
	private final String[] values;

	public MainListViewAdapter(Context context, String[] values) {
		super(context, R.layout.row_layout, values);
		this.context = context;
		this.values = values;		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.row_layout, parent, false);
		
		ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
		TextView textView1 = (TextView) rowView.findViewById(R.id.firstLine);		
		textView1.setText(values[position]);
//		textView1.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Toast.makeText(context, "The textView " + "is clicked!",Toast.LENGTH_LONG).show();
//				;
//			}
//		});
		
		imageView.setImageResource(R.drawable.wechat);
		return rowView;
	}
	
	
}
