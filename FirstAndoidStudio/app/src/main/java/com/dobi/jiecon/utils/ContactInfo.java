package com.dobi.jiecon.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rock on 15/1/24.
 */
public class ContactInfo {

    String contactId;
    String Name;
    List<String> phoneNum = new ArrayList<String>(); //One could have more than one sim card

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public List<String> getPhoneNum() {
        return phoneNum;
    }

    public void addPhoneNum(String phoneNum) {
        this.phoneNum.add(phoneNum);
    }
}
