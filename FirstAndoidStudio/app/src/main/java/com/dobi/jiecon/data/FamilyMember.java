package com.dobi.jiecon.data;


public class FamilyMember {

    private String user_id;
    private String name;
    private String phone;
    private int role;
    private long status;

    private String type;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String  getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



}
