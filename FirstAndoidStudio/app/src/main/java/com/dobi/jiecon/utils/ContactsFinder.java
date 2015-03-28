package com.dobi.jiecon.utils;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.dobi.jiecon.App;
import com.dobi.jiecon.data.RelationData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rock on 15/1/23.
 */
public class ContactsFinder {
    private List<ContactInfo> cList = new ArrayList<ContactInfo>();

    public List<ContactInfo> getAllContacts() {
        String phoneNumber = null;
        String email = null;
        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
        Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String DATA = ContactsContract.CommonDataKinds.Email.DATA;

        ContentResolver contentResolver = App.getAppContext().getContentResolver();
        Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ContactInfo contact = new ContactInfo();
                String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
                String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                contact.setContactId(contact_id);
                contact.setName(name);
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    // Query and loop for every phone number of the contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);
                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        contact.addPhoneNum(phoneNumber);
                    }
                    phoneCursor.close();
                }
                cList.add(contact);
            }
        }
        return cList;
    }

/*    public static List<RelationData> RefineRelationList(List<RelationData> rawData) {
        HashMap<String, RelationData> hm = new HashMap<String, RelationData>();
        if (rawData != null) {

            for (RelationData newRd : rawData) {
                if (hm.get(newRd.getUser_id()) == null){
                    hm.put(newRd.getUser_id(),newRd);
                }else{
                    RelationData oldRd = hm.get(newRd.getUser_id());
                    if (newRd.getStatus()>oldRd.getStatus()){
                        hm.put(newRd.getUser_id(), newRd);
                    }
                }
            }
        }
        List<RelationData> ret = new ArrayList<RelationData>(hm.values());
        return ret;
    }*/

}
