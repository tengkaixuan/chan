package com.dobi.jiecon.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.dobi.jiecon.App;
import com.dobi.jiecon.Controler;
import com.dobi.jiecon.R;
import com.dobi.jiecon.TitlebarListener;
import com.dobi.jiecon.UtilLog;
import com.dobi.jiecon.adapter.ControlAddContactAdapter;
import com.dobi.jiecon.adapter.FamilyListAdapter;
import com.dobi.jiecon.data.FamilyMember;
import com.dobi.jiecon.data.RelationData;
import com.dobi.jiecon.datacontroller.RegistrationManager;
import com.dobi.jiecon.datacontroller.SupervisionManager;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//http://www.androidhive.info/2014/07/android-custom-listview-with-image-and-text-using-volley/
public class ControlViewActivity extends Activity implements TitlebarListener {
    public static TitlebarListener mFamilyTitleBarListener;


    //添加联系人
    private ListView m_contacts_listView;
    private ControlAddContactAdapter m_contacts_adapter;


    //监护人
//    public List<RelationData> m_monitor_List = new ArrayList<RelationData>();
    List<Controler> m_contacts_List = new ArrayList<Controler>();
    private List<FamilyMember> m_family_List = new ArrayList<FamilyMember>();
    private ListView m_monitor_listView;
    private FamilyListAdapter m_family_adapter;

    //绑定
    private RelativeLayout m_monitor_contain = null;
    private EditText m_phone = null;
    private EditText m_validate = null;
    private Button m_validate_get = null;
    private Button m_binding = null;

    //Supervision layout
    private RelativeLayout m_supervision_container;
    private EditText m_text_time;
    private Button m_btn_confirm;
    private Button m_btn_cancel;


    private boolean m_test_bing = false;
    private Context cxt;
    //0:all,1;father,2,son
    public int mType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.control_family_list);
        m_monitor_listView = (ListView) findViewById(R.id.list_monitor);

        mFamilyTitleBarListener = this;

        if (mType != 0) {
            return;
        }
        cxt = this;
        refreshRelationList();
        refreshFamilyMembers();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
//        moveTaskToBack(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        UtilLog.logWithCodeInfo("RefreshFamilyMemeber", "onResume", "ControlViewActivity");
        refreshFamilyMembers();
        if (SupervisionManager.MSG_NEWARRIVAL.equals(SupervisionManager.getMsgFlag())) {
            UtilLog.logWithCodeInfo("Notification received", "onResume", "ControlViewActivity");
            UtilLog.logWithCodeInfo("contacts_id is " + SupervisionManager.getNotificationContactsId(), "onResume", "ControlViewActivity");
            UtilLog.logWithCodeInfo("contacts_name is " + SupervisionManager.getNotificationContactsName(), "onResume", "ControlViewActivity");
            UtilLog.logWithCodeInfo("contacts_phone is " + SupervisionManager.getNotificationContactsPhone(), "onResume", "ControlViewActivity");


            Intent intent = new Intent(this, FriendActivity.class);
            int relation = SupervisionManager.get_individual_relation(SupervisionManager.getNotificationContactsId());
            switch (relation) {
                case RelationData.RELATION_ROLE_FRIEND:
                    break;
                case RelationData.RELATION_ROLE_FATHER:
                    intent = new Intent(this, ParentActivity.class);
                    break;
                case RelationData.RELATION_ROLE_SON:
                    intent = new Intent(this, KidsActivity.class);
                    break;
            }
            intent.putExtra(App.KEY_CURRENT_USR_NAME, SupervisionManager.getNotificationContactsName());
            intent.putExtra(App.KEY_CURRENT_USR_PHONE, SupervisionManager.getNotificationContactsPhone());
            intent.putExtra(App.KEY_CURRENT_CONTACTS_ID, SupervisionManager.getNotificationContactsId());
            intent.putExtra(App.KEY_FROM_CXT, App.ID_NOTIFICATION);
            startActivity(intent);
            SupervisionManager.setNewMsgFlag(SupervisionManager.MSG_NO);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

/*    private void hideSupervisionLayout() {
        if (m_supervision_container == null) {
            showSupervisionLayout("");
        }
        m_supervision_container.setVisibility(View.GONE);
    }*/

    /*public void showSupervisionLayout(String son_userid) /*{
        m_supervision_container = (RelativeLayout) findViewById(R.id.supervision_settings);
        hidMonitor();
        m_supervision_container.setVisibility(View.VISIBLE);
        m_text_time = (EditText) findViewById(R.id.supervision_time);
        m_btn_confirm = (Button) findViewById(R.id.supervision_confirm);
        m_btn_confirm.setTag(son_userid);
        m_btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m_text_time.getText().toString().equals("")) {
                    Resources res = getResources();
                    String hint_title = res.getString(R.string.family_bind_dialog_monitor_title);
                    String binding_hint = res.getString(R.string.supervision_time_setting_warn);
                    UtilLog.showAlert(ControlViewActivity.this, hint_title, binding_hint);
                    return;
                }
                int t1 = Integer.parseInt(m_text_time.getText().toString());
                //Set the t1, father id and son id to the server
//                SupervisionManager.Supervision_Notify(t1, RegistrationManager.getUserId(), m_btn_confirm.getTag().toString());
                if (true == SupervisionManager.supervision_request(RegistrationManager.getUserId(), m_btn_confirm.getTag().toString(), t1 * 60, "")) {

                    Resources res = getResources();
                    String hint_title = res.getString(R.string.family_bind_dialog_monitor_title);
                    String binding_hint = res.getString(R.string.supervision_setting_ok);
                    UtilLog.showAlert(ControlViewActivity.this, hint_title, binding_hint);

                    //Show previous ui
                    m_supervision_container.setVisibility(View.GONE);
                    showMonitor();
                }
            }

        });


        m_btn_cancel = (Button) findViewById(R.id.supervision_cancel);
        m_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show previous ui
                m_supervision_container.setVisibility(View.GONE);
                showMonitor();
            }

        });
    }*/

    private boolean isValidPhoneNum(String phoneNum) {
        if (phoneNum.equals(""))
            return false;
        return true;
    }

     /*private boolean getBinding(){
        //check if the phone has been bound
        String flag = RegistrationManager.getBindingFlag();
        boolean binding;

        if (null != flag) {
            binding = flag.equals(RegistrationManager.BIND_YES);
        } else {
            binding = false;
        }
        //Check if the binding info is sitting on the server

        if (binding == false) {//Check if the binding info is on the server
            TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            String mPhoneNumber = tMgr.getLine1Number();
            if (mPhoneNumber == null) {
                Resources res = getResources();
                String hint_title = res.getString(R.string.family_bind_dialog_monitor_title);
                String binding_hint = res.getString(R.string.family_bind_dialog_no_sim_card);
                UtilLog.showAlert(this, hint_title, binding_hint);
            } else {
                String ret = SupervisionManager.get_user_id_by_phone(mPhoneNumber);
                if (ret != null) {
                    RegistrationManager.setBindingFlag(RegistrationManager.BIND_YES);
                    binding = true;
                }
            }

        }
        return binding;
    }*/

    private void refreshRelationList() {
        SupervisionManager.update_relation_list_async(this, RegistrationManager.getUserId(), mType);
    }

    public void showMonitor() {
        refreshFamilyMembers();
    }

    public void refreshFamilyMembers() {
        m_family_adapter = new FamilyListAdapter(this, m_family_List);
        m_monitor_listView.setAdapter(m_family_adapter);
//        List<FamilyMember> list = SqliteBase.get_unilateral_families();
        List<FamilyMember> list = SupervisionManager.get_families();
        if (list == null || list.size() == 0) {
            UtilLog.logWithCodeInfo("list is null", "refreshRelationList", "ControlViewActivity");
            return;
        }
        UtilLog.logWithCodeInfo("number of family is " + list.size(), "refreshRelationList", "ControlViewActivity");
        m_family_List.clear();
        for (int i = 0; i < list.size(); i++) {
            m_family_List.add(list.get(i));
        }

        m_family_adapter.notifyDataSetChanged();
    }

//    public void addContactOnClickHandler(View v) {
//        Controler item = (Controler) v.getTag();
//        String phone = item.getPhone();
////        String userid = SupervisionManager.get_user_id_by_phone(phone);
//        String userid = item.getUserid();
//        Resources res = getResources();
//        String title = res.getString(R.string.family_bind_dialog_monitor_title);
//        int status = item.getContacts_status();
//        switch (status) {
//            case Controler.CONTACTS_IN_YOUR_FAMILIES:
//                break;
//            case Controler.CONTACTS_JIECON_USER_NO://
//                if (DebugControl.DEBUG_FLAG) {
//                    String msg = "This will send msg to real phone";
//                    UtilLog.showAlert(this, title, msg);
//                } else {
//                    //TODO: I will be right back
//                    SmsMgr.sendSMS(phone, "");
//                }
//                break;
//            case Controler.CONTACTS_JIECON_USER_YES:
//                RelationData relation = new RelationData();
//                String name = item.getName();
//                if (name != null)
//                    relation.setName(name);
//                relation.setPhone(phone);
//                relation.setStatus(0); //Friends
//                relation.setUser_id(userid);
//                UtilLog.logControlD("addContactOnClickHandler, name=" + name + ", phone=" + phone);
////            boolean insert = SqliteDBContactsMgr.insertContact(userid, name, phone, SqliteDBContactsMgr.ROLE_COLUMN_ROLE_FRIEND);
//                List<RelationData> rl = new ArrayList<RelationData>();
//                rl.add(relation);
//                boolean insert = SqliteBase.update_relation_list(rl);
//                if (insert == false) {
//                    String msg1 = res.getString(R.string.family_bind_dialog_monitor_add_exist);
//                    UtilLog.showAlert(this, title, msg1);
//                }
//                List usrlist = new ArrayList();
//                usrlist.add(userid);
//                if (true == SupervisionManager.add_friend(RegistrationManager.getUserId(), usrlist)) {
//                    String msg2 = res.getString(R.string.family_bind_dialog_monitor_add_success);
//                    UtilLog.showAlert(this, title, msg2);
//                }
//                break;
//        }
//        hidContact();
//        showMonitor();
//    }

    public void showMonitorDetailOnClickHandler(View v) {
        UtilLog.logControlD("showMonitorDetailOnClickHandler clicked");
    }

    //http://developer.android.com/training/contacts-provider/display-contact-badge.html

    /**
     * Load a contact photo thumbnail and return it as a Bitmap,
     * resizing the image to the provided image dimensions as needed.
     *
     * @param photoData photo ID Prior to Honeycomb, the contact's _ID value.
     *                  For Honeycomb and later, the value of PHOTO_THUMBNAIL_URI.
     * @return A thumbnail Bitmap, sized to the provided width and height.
     * Returns null if the thumbnail is not found.
     */
    private Bitmap loadContactPhotoThumbnail(String photoData) {
        // Creates an asset file descriptor for the thumbnail file.
        if (photoData == null) return null;
        AssetFileDescriptor afd = null;
        // try-catch block for file not found
        try {
            // Creates a holder for the URI.
            Uri thumbUri;
            // If Android 3.0 or later
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                // Sets the URI from the incoming PHOTO_THUMBNAIL_URI
                thumbUri = Uri.parse(photoData);
            } else {
                // Prior to Android 3.0, constructs a photo Uri using _ID
                /*
                 * Creates a contact URI from the Contacts content URI
                 * incoming photoData (_ID)
                 */
                final Uri contactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, photoData);
                /*
                 * Creates a photo URI by appending the content URI of
                 */

                thumbUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
            }

            afd = this.getContentResolver().openAssetFileDescriptor(thumbUri, "r");
            FileDescriptor fileDescriptor = afd.getFileDescriptor();
            // Decode the photo file and return the result as a Bitmap
            // If the file descriptor is valid
            if (fileDescriptor != null) {
                // Decodes the bitmap
                return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, null);
            }
            // If the file isn't found
        } catch (FileNotFoundException e) {
            /*
             * Handle file not found errors
             */
        } finally {
            if (afd != null) {
                try {
                    afd.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    @Override
    public void onBackHome(ImageView iv) {

    }

    @Override
    public void showBarChart() {

    }

    @Override
    public void showPieChart() {

    }

    @Override
    public void addContact() {
        TabSample.mTab.findViewById(R.id.bar_chart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(App.getAppContext(), AddContactsActivity.class));
            }
        });
    }

}
