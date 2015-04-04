package com.dobi.jiecon.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dobi.jiecon.App;
import com.dobi.jiecon.R;
import com.dobi.jiecon.UtilLog;
import com.dobi.jiecon.activities.FriendActivity;
import com.dobi.jiecon.activities.KidsActivity;
import com.dobi.jiecon.activities.ParentActivity;
import com.dobi.jiecon.activities.SupervisionDetailsActivity;
import com.dobi.jiecon.data.FamilyMember;
import com.dobi.jiecon.data.RelationData;
import com.dobi.jiecon.database.sqlite.SqliteBase;
import com.dobi.jiecon.datacontroller.SupervisionManager;

import java.util.List;

public class FamilyListAdapter extends BaseAdapter {

    private Resources res = App.getAppContext().getResources();

    private Activity activity;
    private LayoutInflater inflater;
    private List<FamilyMember> relationItems;
//    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public FamilyListAdapter(Activity activity, List<FamilyMember> items) {
        this.activity = activity;
        this.relationItems = items;
    }


    @Override
    public int getCount() {
        return relationItems.size();
    }

    @Override
    public Object getItem(int location) {
        return relationItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.control_monitor_row, null);
//        convertView.setTag(position);
        ImageView thumbNail = (ImageView) convertView.findViewById(R.id.thumbnail);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView phone = (TextView) convertView.findViewById(R.id.phone);
//        Button detail = (Button) convertView.findViewById(R.id.detail);

        FamilyMember m = relationItems.get(position);
        if (m == null) {
            return convertView;
        }

//        detail.setText(FamilyRelationFormat.getRoleString(m.getRole(),m.getStatus()));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetails(relationItems.get(position).getName(), relationItems.get(position).getPhone());
            }
        });
//        Bitmap photo = m.getThumbnailUrl();
        Bitmap photo = null;
        if (photo != null) {
            thumbNail.setImageBitmap(photo);
        } else {
            thumbNail.setImageResource(R.drawable.default_contact_icon);
        }
        String name_label = m.getName();
        if (name_label.equals("") || name_label.equals("null") || name_label == null) {
            name_label = m.getUser_id();
        }
        name.setText(name_label);
        phone.setText(m.getPhone());

//        detail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDetails(relationItems.get(position).getName(), relationItems.get(position).getPhone());
//            }
//        });
        return convertView;
    }

    private void showDetails(String name, String phone) {
        Intent intent = new Intent(activity, SupervisionDetailsActivity.class);
        String contacts_id = SqliteBase.get_usrid_from_family_list(phone);
        if (null == contacts_id) {
            UtilLog.logWithCodeInfo("[PROGRESS] get contacts id from sqlite is null", "showDetails", "FamilyListAdapter");
            SupervisionManager.get_user_id_by_phone_async(phone, null);
            UtilLog.logWithCodeInfo("[PROGRESS] start to get userid from server async", "showDetails", "FamilyListAdapter");
            intent = new Intent(activity, FriendActivity.class);
        } else {
            UtilLog.logWithCodeInfo("[PROGRESS] get contacts id from sqlite is "+contacts_id, "showDetails", "FamilyListAdapter");
            int relation = SupervisionManager.get_individual_relation(contacts_id);
            switch (relation) {
                case RelationData.RELATION_ROLE_FRIEND:
                    UtilLog.logWithCodeInfo("[PROGRESS] contacts is friend", "showDetails", "FamilyListAdapter");
                    intent = new Intent(activity, FriendActivity.class);
                    break;
                case RelationData.RELATION_ROLE_FATHER:
                    UtilLog.logWithCodeInfo("[PROGRESS] contacts is parent", "showDetails", "FamilyListAdapter");
                    intent = new Intent(activity, ParentActivity.class);
                    break;
                case RelationData.RELATION_ROLE_SON:
                    UtilLog.logWithCodeInfo("[PROGRESS] contacts is kid", "showDetails", "FamilyListAdapter");
                    intent = new Intent(activity, KidsActivity.class);
                    break;
            }
        }
        intent.putExtra(App.KEY_CURRENT_USR_NAME, name);
        intent.putExtra(App.KEY_CURRENT_USR_PHONE, phone);
        intent.putExtra(App.KEY_FROM_CXT, App.ID_FAMILY_LIST_ADAPTER);
        activity.startActivity(intent);
    }
}
