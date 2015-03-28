package com.dobi.jiecon.data;


public class RelationData {
    long seq;

    //Status
    public static final int RELATION_SUPERVISION_NO = 0;
    public static final int RELATION_SUPERVISION_YES = 1;

    //Role
    public static final int RELATION_ROLE_FATHER = 0;
    public static final int RELATION_ROLE_SON = 1;

    //Father
    public static final int RELATION_FATHER_SUPERVISION_REQUEST = 10;
    public static final int RELATION_FATHER_APPROVE_SUPERVISION_FREE = 21;
    public static final int RELATION_FATHER_DISAPPROVE_SUPERVISION_FREE = 22;
    public static final int RELATION_FATHER_APPROVE_UNLOCK = 31;
    public static final int RELATION_FATHER_DISAPPROVE_UNLOCK = 32;

    //son
    public static final int RELATION_SON_AGREE_SUPERVISION = 11;
    public static final int RELATION_SON_DISAGREE_SUPERVISION = 12;
    public static final int RELATION_SON_CANCEL_SUPERVISION_REQUEST = 20;
    public static final int RELATION_SON_UNLOCK_REQUEST = 30;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    String user_id;
    String name;
    String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getRole() {
        return role;
    }

    public void setRole(long role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    long time;
    //0: father, 1: son

    long role;

    long status;

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    long type;
    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    String msg;

    public long getRead_flag() {
        return read_flag;
    }

    public void setRead_flag(long read_flag) {
        this.read_flag = read_flag;
    }

    long read_flag;
}
