package com.dobi.jiecon.database;

public class AppUsageOrg {
    private String app_pkgname ="";

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    private String app_name ="";
    private long start_time;
    private long end_time;

    public String getApp_pkgname() {
        return app_pkgname;
    }

    public void setApp_pkgname(String app_pkgname) {
        this.app_pkgname = app_pkgname;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private long id;
}
