package com.dobi.jiecon.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dobi.jiecon.App;
import com.dobi.jiecon.Controler;
import com.dobi.jiecon.R;
import com.dobi.jiecon.UtilLog;
import com.dobi.jiecon.adapter.ControlAddContactAdapter;
import com.dobi.jiecon.data.RelationData;
import com.dobi.jiecon.database.sqlite.SqliteBase;
import com.dobi.jiecon.datacontroller.RegistrationManager;
import com.dobi.jiecon.datacontroller.SupervisionManager;
import com.dobi.jiecon.utils.DialogBuilderMgr;
import com.dobi.jiecon.utils.IAlarmDialog;
import com.dobi.jiecon.utils.PhoneNumFormating;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddContactsActivity extends Activity {
    private TextView v_jiecon_id;
    private TextView v_contact_role;
    private TextView v_txt_phone_number;
    private TextView v_contact_status;
    private Button v_btn_both_request_supervision;
    private Button v_btn_father_cancel_supervision;
    private Button v_btn_son_request_cancel_supervision;
    private Button v_btn_father_lock;
    private Button v_btn_son_request_unlock;

    private ImageView v_img_title_bar_back;
    private String contacts_id;
    private String contacts_name;
    private List<RelationData> relationList = new ArrayList<RelationData>();

    private Resources res = App.getAppContext().getResources();
    private Context thisActivity;


    private ListView m_contacts_listView;
    private ControlAddContactAdapter m_contacts_adapter;
    List<Controler> m_contacts_List = new ArrayList<Controler>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.control_contacts_list);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.supervision_contacts_titlebar);
        v_img_title_bar_back = (ImageView) findViewById(R.id.contacts_title_bar_back);

        v_img_title_bar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(thisActivity, TabSample.class);
//                intent.putExtra(App.KEY_TAB_HOST_ID, 2);
                startActivity(intent);
                finish();
            }
        });
        thisActivity = this;
        showContacts();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void addContactOnClickHandler2(View v) {
        UtilLog.logWithCodeInfo("Catch button clicking", "addContactOnClickHandler", "AddContactsActivity");
        Controler item = (Controler) v.getTag();
        UtilLog.logWithCodeInfo("Add family: Phone = " + item.getPhone(), "addContactOnClickHandler", "AddContactsActivity");
        UtilLog.logWithCodeInfo("Add family: Name = " + item.getName(), "addContactOnClickHandler", "AddContactsActivity");
        String phone = item.getPhone();
        String userid = item.getUserid();
        int status = item.getContacts_status();
        switch (status) {
            case Controler.CONTACTS_IN_YOUR_FAMILIES:
                break;
            case Controler.CONTACTS_JIECON_USER_YES:
                RelationData relation = new RelationData();
                String name = item.getName();
                if (name != null)
                    relation.setName(name);
                relation.setPhone(phone);
                relation.setStatus(0); //Friends
                relation.setUser_id(userid);
                UtilLog.logWithCodeInfo("addContactOnClickHandler, name=" + name + ", phone=" + phone, "addContactOnClickHandler", "AddContactsActivity");
                List usrlist = new ArrayList();
                usrlist.add(userid);
                if (false == SupervisionManager.add_friend(RegistrationManager.getUserId(), usrlist)) {
                    UtilLog.logWithCodeInfo("Add Friend return false", "addContactOnClickHandler", "AddContactsActivity");
                }
                //omit break, also need to add to family
            case Controler.CONTACTS_JIECON_USER_NO://


                if (false == SupervisionManager.add_family(item.getName(), item.getPhone())) {
                    UtilLog.logWithCodeInfo("Add family return false", "addContactOnClickHandler", "AddContactsActivity");
                } else {
                    UtilLog.showAlert(this, getString(R.string.family_bind_dialog_monitor_title), getString(R.string.family_bind_dialog_monitor_add_success));
                }
                break;
        }
        startActivity(new Intent(this, TabSample.class));
    }

    public void addContactOnClickHandler(View v) {
        UtilLog.logWithCodeInfo("Catch button clicking", "addContactOnClickHandler", "AddContactsActivity");
        Controler item = (Controler) v.getTag();
        UtilLog.logWithCodeInfo("Add family: Phone = " + item.getPhone(), "addContactOnClickHandler", "AddContactsActivity");
        UtilLog.logWithCodeInfo("Add family: Name = " + item.getName(), "addContactOnClickHandler", "AddContactsActivity");
        final String phone = item.getPhone();
        final String name = item.getName();
        int status = item.getContacts_status();

        if (null != SqliteBase.friend_is_existing(item.getPhone())) {

            UtilLog.logWithCodeInfo("Phone is existing in sqlite " + item.getPhone(), "addContactOnClickHandler", "AddContactsActivity");
            Intent intent = new Intent(this, SupervisionDetailsActivity.class);
            intent.putExtra(App.KEY_CURRENT_USR_NAME, name);
            intent.putExtra(App.KEY_CURRENT_USR_PHONE, phone);
            intent.putExtra(App.KEY_FROM_CXT, App.ID_FAMILY_LIST_ADAPTER);
            startActivity(intent);
        } else {
            UtilLog.logWithCodeInfo("Phone does not exist in sqlite " + phone, "addContactOnClickHandler", "AddContactsActivity");
            DialogBuilderMgr.CreateAlertDialog(
                    this,
                    getString(R.string.family_add_buddy),
                    new IAlarmDialog() {
                        @Override
                        public void consume() {
                            if (false == SupervisionManager.add_family(name, phone)) {
                                UtilLog.logWithCodeInfo("Add family return false", "addContactOnClickHandler", "AddContactsActivity");
                            } else {
                                SupervisionManager.update_user_id_by_phone_async(phone);
                                Intent intent = new Intent(thisActivity, SupervisionDetailsActivity.class);
                                intent.putExtra(App.KEY_CURRENT_USR_NAME, name);
                                intent.putExtra(App.KEY_CURRENT_USR_PHONE, phone);
                                intent.putExtra(App.KEY_FROM_CXT, App.ID_FAMILY_LIST_ADAPTER);
                                startActivity(intent);
                            }
                        }
                    },
                    new IAlarmDialog() {
                        @Override
                        public void consume() {
                            return;
                        }
                    },
                    null);
        }
    }

    private void showContacts() {
        m_contacts_listView = (ListView) findViewById(R.id.contacts_list);
        m_contacts_adapter = new ControlAddContactAdapter(this, m_contacts_List);
        m_contacts_listView.setAdapter(m_contacts_adapter);

        m_contacts_List.clear();
        String order = ContactsContract.CommonDataKinds.Phone._ID + " ASC";
        //read data from sqlite database
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, order);

        if (cursor.getCount() == 0) {
            Resources res = getResources();
            String hint_title = res.getString(R.string.family_bind_dialog_monitor_title);
            String binding_hint = res.getString(R.string.family_bind_empty_contact_list);
//            UtilLog.showAlertWithCall(this, hint_title, binding_hint, );
        } else {
            while (cursor.moveToNext()) {
                String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phoneNumber = PhoneNumFormating.telNumberRefine(phoneNumber);
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                int mThumbnailColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_THUMBNAIL_URI);
                Bitmap mThumbnail = null;
                if (mThumbnailColumn >= 0) {
                    String mThumbnailUri = cursor.getString(mThumbnailColumn);
//            UtilLog.logControlD("mThumbnailUri="+mThumbnailUri);
                    mThumbnail = loadContactPhotoThumbnail(mThumbnailUri);
                }

                Controler controler = new Controler();
                controler.setThumbnailUrl(mThumbnail);
                controler.setName(name);
                controler.setPhone(phoneNumber);
                m_contacts_List.add(controler);
            }
        }
        m_contacts_adapter.notifyDataSetChanged();
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
}
