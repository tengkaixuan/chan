package com.dobi.jiecon.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dobi.jiecon.App;
import com.dobi.jiecon.Controler;
import com.dobi.jiecon.R;
import com.dobi.jiecon.UtilLog;
import com.dobi.jiecon.database.sqlite.SqliteBase;
import com.dobi.jiecon.datacontroller.SupervisionManager;

import java.util.List;

/**
 * Created by harvey on 12/31/14.
 */
public class ControlAddContactAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Controler> movieItems;
//    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public ControlAddContactAdapter(Activity activity, List<Controler> movieItems) {
        this.activity = activity;
        this.movieItems = movieItems;
    }


    @Override
    public int getCount() {
        return movieItems.size();
    }

    @Override
    public Object getItem(int location) {
        return movieItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UtilLog.logWithCodeInfo("view is updated ", "getView", "ControlAddContactAdapter");
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.control_contact_add, null);


        ImageView thumbNail = (ImageView) convertView.findViewById(R.id.thumbnail);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView phone = (TextView) convertView.findViewById(R.id.phone);
//        Button detail = (Button) convertView.findViewById(R.id.contacts_detail);

        Controler m = movieItems.get(position);

        convertView.setTag(m);
        UtilLog.logWithCodeInfo("Phone is " + m.getPhone(), "getView", "ControlAddContactAdapter");
        UtilLog.logWithCodeInfo("Name is " + m.getName(), "getView", "ControlAddContactAdapter");
        Bitmap photo = m.getThumbnailUrl();
        if (photo != null) {
            thumbNail.setImageBitmap(photo);
        } else {
            thumbNail.setImageResource(R.drawable.default_contact_icon);
        }
        name.setText(m.getName());
        phone.setText(m.getPhone());
  /*      Resources res = App.getAppContext().getResources();

        if (null != SqliteBase.friend_is_existing(m.getPhone())) {

            UtilLog.logWithCodeInfo("Phone is existing in sqlite " + m.getPhone(), "getView", "ControlAddContactAdapter");
            detail.setText(res.getString(R.string.family_bind_added));
            detail.setEnabled(false);
            m.setContacts_status(Controler.CONTACTS_IN_YOUR_FAMILIES);
        } else {
            UtilLog.logWithCodeInfo("Phone does not exist in sqlite " + m.getPhone(), "getView", "ControlAddContactAdapter");
            String usrid = SupervisionManager.get_user_id_by_phone(m.getPhone());
            if (null != usrid) {
                m.setUserid(usrid);
                m.setContacts_status(Controler.CONTACTS_JIECON_USER_YES);
            } else {
//                detail.setText(res.getString(R.string.family_bind_invite));
                m.setContacts_status(Controler.CONTACTS_JIECON_USER_NO);
            }
            detail.setText(res.getString(R.string.contacts_add_as_friend));
        }*/
        return convertView;
    }
}
