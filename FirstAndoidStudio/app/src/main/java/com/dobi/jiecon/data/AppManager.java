package com.dobi.jiecon.data;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import com.dobi.jiecon.utils.DurationSlot;
import com.dobi.jiecon.utils.TimeSlot;

public class AppManager {

    private LinkedList<SingleAppRecord> appHistory = new LinkedList<SingleAppRecord>();
    private AppInfo app;
    private AppStatus sts;
    private Date st;
    private static AppManager apps= null;

    public AppManager() {

    }

    // There should be a method that can return the whole consuming time
    public DurationSlot getAllDuration() {
        DurationSlot ds = new DurationSlot();
        for (Iterator<SingleAppRecord> iter = appHistory.iterator(); iter
                .hasNext(); ) {
            ds.addDay(iter.next().getDayDuration())
                    .addHour(iter.next().getHourDuration())
                    .addMin(iter.next().getMinDuration())
                    .addSec(iter.next().getSecDuration());
        }
        return ds;
    }
    public static AppManager getInstance(){
        if(apps==null){
            apps = new AppManager();
        }
        return apps;
    }
    public void setRunning() {
        sts = AppStatus.Running;
        this.st = new Date();
    }

    public void setStop() {
        sts = AppStatus.Stop;
        MoveToHistory();
    }

    public void HistoryClean(int Remains) {
        while (Remains > appHistory.size()) {
            appHistory.removeLast();
        }
    }

    public AppStatus getStatus() {
        return this.sts;
    }

    private void MoveToHistory() {
        appHistory.add(new SingleAppRecord(st, new Date()));
    }

    public void AddHistory(Date ts, Date ed) {
        appHistory.add(new SingleAppRecord(ts, ed));
    }

    public AppInfo getApp() {
        return app;
    }

    public void setApp(AppInfo app) {
        this.app = app;
    }

    public AppManager(AppInfo a) {
        this.app = a;
    }

    // Return the item from the list
    public LinkedList<SingleAppRecord> getAllItem() {
        return appHistory;
    }

    public void addItem(SingleAppRecord rec) {
        appHistory.addFirst(rec);
    }

    // Return the item from the list
    public SingleAppRecord getItem(int location) {
        return appHistory.get(location);
    }

    // Return the item from the list
    public SingleAppRecord removeItem(int location) {
        return appHistory.remove(location);
    }

    // Return the item from the list
    public SingleAppRecord removeFirstItem() {
        return appHistory.remove();
    }

    // Return the item from the list
    public SingleAppRecord removeLastItem() {
        return appHistory.removeLast();
    }

    // Return the item from the list
    public SingleAppRecord getItem() {
        return appHistory.getFirst();
    }

    public DurationSlot getDayDuration(Date d) {
        DurationSlot ds = new DurationSlot();
        for (Iterator<SingleAppRecord> iter = appHistory.iterator(); iter.hasNext();) {
            ds.addDuration(iter.next().getOccupiedTime(d));
        }
        return ds;
    }
}