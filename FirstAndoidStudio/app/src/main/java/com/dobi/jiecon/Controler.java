package com.dobi.jiecon;

import android.graphics.Bitmap;

/**
 * Created by harvey on 12/31/14.
 */
public class Controler {
    private String name;
    private String phone;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    private String userid;
    private String role;
    private String id;
    private Bitmap thumbnailUrl;

    private int contacts_status;

    public  final static int CONTACTS_JIECON_USER_YES = 1;
    public  final static int CONTACTS_JIECON_USER_NO = 2;
    public  final static int CONTACTS_IN_YOUR_FAMILIES = 3;

    //监控人的信息|  id   | userid | name | phone | role
    public Controler() {
    }

    public Controler(String id, String userid, String name, String phone, String role) {
        this.id = id;
        this.name = name;
        this.userid = userid;
        this.phone = phone;
        this.role = role;
    }

    public Controler(Bitmap thumbnailUrl, String name, String phone) {
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.phone = phone;
    }
    public int getContacts_status() {
        return this.contacts_status;
    }

    public void setContacts_status(int status) {
        this.contacts_status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(Bitmap thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
