package com.dobi.jiecon.database;


public class AppUsage {
    private String app_pkgname;

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    private String app_name;
    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getApp_pkgname() {
        return app_pkgname;
    }

    public void setApp_pkgname(String app_pkgname) {
        this.app_pkgname = app_pkgname;
    }

    private long duration;

    public long getTimes() {
        return times;
    }

    public void setTimes(long times) {
        this.times = times;
    }

    private long times;
}
