package com.dobi.jiecon.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.dobi.jiecon.R;
import com.dobi.jiecon.UtilLog;
import com.dobi.jiecon.database.AppUsage;
import com.dobi.jiecon.datacontroller.AppUsageManager;
import com.dobi.jiecon.utils.MathCalculate;
import com.dobi.jiecon.utils.TimeFormat;
import com.github.mikephil.charting.charts.BarChartItem;
import com.github.mikephil.charting.charts.ChartItem;
import com.github.mikephil.charting.charts.LineChartItem;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MonthActivity extends Activity {
    private LinearLayout m_linearView = null;
    private HashMap<String, Map<Integer, AppUsage>> allData = new HashMap<String, Map<Integer, AppUsage>>();
    private ArrayList<AppUsage> retList = new ArrayList<AppUsage>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_month);
    }

    protected void onResume() {
        super.onResume();
        Calendar cal = Calendar.getInstance();
        int today = cal.get(Calendar.DAY_OF_MONTH);

        AppUsageManager.sum_app_duration_by_day(TimeFormat.getSecondForMonth(TimeFormat.Now(), 0), today, retList, allData);

        ArrayList<ChartItem> list = new ArrayList<ChartItem>();
        for (int i = 0; i < retList.size(); i++) {

            AppUsage app_item = retList.get(i);
            String package_name = app_item.getApp_pkgname();
            String app_name = app_item.getApp_name();

            list.add(new LineChartItem(generateDataLineByApp(allData.get(package_name), app_name, today), getApplicationContext()));
            list.add(new BarChartItem(generateDataBarByApp(allData.get(package_name), app_name, today), getApplicationContext()));
        }
        ListView lv = (ListView) findViewById(R.id.monthlist);
        ChartDataAdapter cda = new ChartDataAdapter(getApplicationContext(), list);
        lv.setAdapter(cda);
        cda.notifyDataSetChanged();
    }

    /**
     * adapter that supports 3 different item types
     */
    private class ChartDataAdapter extends ArrayAdapter<ChartItem> {

        public ChartDataAdapter(Context context, List<ChartItem> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getItem(position).getView(position, convertView, getContext());
        }

        @Override
        public int getItemViewType(int position) {
            // return the views type
            return getItem(position).getItemType();
        }

        @Override
        public int getViewTypeCount() {
            return 2; // we have 3 different item-types
        }
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    /*
    private LineData generateDataLine(int cnt) {

        ArrayList<Entry> e1 = new ArrayList<Entry>();

        for (int i = 0; i < 12; i++) {
            e1.add(new Entry((int) (Math.random() * 65) + 40, i));
        }

        LineDataSet d1 = new LineDataSet(e1, "Line New DataSet " + cnt + ", (1)");
        d1.setLineWidth(2.5f);
        d1.setCircleSize(4.5f);
        d1.setValueTextSize(14f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);

        ArrayList<Entry> e2 = new ArrayList<Entry>();

        for (int i = 0; i < 12; i++) {
            e2.add(new Entry(e1.get(i).getVal() - 30, i));
        }

        LineDataSet d2 = new LineDataSet(e2, "Line New DataSet " + cnt + ", (2)");
        d2.setLineWidth(2.5f);
        d2.setCircleSize(4.5f);
        d2.setValueTextSize(14f);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setDrawValues(false);

        ArrayList<LineDataSet> sets = new ArrayList<LineDataSet>();
        sets.add(d1);
//        sets.add(d2);

        LineData cd = new LineData(getMonths(), sets);
        return cd;
    }
*/
    /**
     * @return
     */
    private LineData generateDataLineByApp(Map<Integer, AppUsage> appMonthData, String appName, int endDay) {
        UtilLog.logWithCodeInfo("Current app name is " + appName, "generateDataLineByApp", "MonthActivity");
        ArrayList<String> xVals;
        ArrayList<Entry> yVals;
        if (appMonthData != null && appMonthData.size() > 0) {
            //intial x and y
            xVals = new ArrayList<String>(endDay);
            yVals = new ArrayList<Entry>(endDay);

            for (int i = 0; i < endDay; i++) {
                xVals.add(i, String.valueOf(i + 1));
                yVals.add(i, new Entry(0, i));
            }

            //Set Y axis
            Set<Integer> day_set = new HashSet<Integer>();
            day_set = appMonthData.keySet();
            long duration;
            for (Integer day : day_set) {
                duration = massageDur(appMonthData.get(day).getDuration());
                UtilLog.logWithCodeInfo("Original Duration = " + duration, "generateDataLineByApp", "MonthActivity");
                float f_duration = MathCalculate.float2float(duration / 60.0f, 1);
                UtilLog.logWithCodeInfo("Format Duration = " + f_duration, "generateDataLineByApp", "MonthActivity");
                UtilLog.logWithCodeInfo("Day = " + day, "generateDataLineByApp", "MonthActivity");
                yVals.set(day - 1, new Entry(f_duration, day - 1));
            }
        } else {
            xVals = getMonths();
            yVals = new ArrayList<Entry>();
            for (int i = 0; i < 12; i++) {
                yVals.add(new Entry((int) (Math.random() * 70) + 30, i));
            }
        }

        LineDataSet d1 = new LineDataSet(yVals, appName);
        d1.setLineWidth(2.5f);
        d1.setCircleSize(4.5f);
        d1.setValueTextSize(12f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);
        ArrayList<LineDataSet> sets = new ArrayList<LineDataSet>();
        sets.add(d1);

        LineData cd = new LineData(xVals, sets);
        return cd;
    }
    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
        /*
    private BarData generateDataBar(int cnt) {

        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
//        Log.d("harvey", "user_id" + user_id + ",ret=" + ret);
        if (retList != null && retList.size() > 0) {
            for (int i = 0; i < retList.size(); i++) {
                Log.d("harvey", "appname" + i + " = " + retList.get(i).getApp_name() + "BARCHART");
                xVals.add(retList.get(i).getApp_name());
                Log.d("harvey", "duration time = " + retList.get(i).getDuration());
                float min = retList.get(i).getDuration() / 60.0f;
                yVals1.add(new BarEntry(min, i));
            }
        } else { //默认数据

            xVals = getMonths();

            for (int i = 0; i < 12; i++) {
                yVals1.add(new BarEntry((int) (Math.random() * 70) + 30, i));
            }
        }

        BarDataSet d = new BarDataSet(yVals1, "Bar New DataSet " + cnt);
        d.setBarSpacePercent(20f);
        d.setColors(ColorTemplate.HARVEY_COLORS);
        d.setHighLightAlpha(255);
        d.setValueTextSize(14f);

        BarData cd = new BarData(xVals, d);

        return cd;
    }
*/
    private BarData generateDataBarByApp(Map<Integer, AppUsage> appMonthData, String appName, int endDay) {
        UtilLog.logWithCodeInfo("Current app name is " + appName, "generateDataBarByApp", "MonthActivity");
        ArrayList<String> xVals;
        ArrayList<BarEntry> yVals;
        if (appMonthData != null && appMonthData.size() > 0) {
            //intial x and y
            xVals = new ArrayList<String>(endDay);
            yVals = new ArrayList<BarEntry>(endDay);

            for (int i = 0; i < endDay; i++) {
                xVals.add(i, String.valueOf(i + 1));
                yVals.add(i, new BarEntry(0, i));
            }

            //Set Y axis
            Set<Integer> day_set = appMonthData.keySet();
            long duration;
            for (Integer day : day_set) {
                duration = massageDur(appMonthData.get(day).getDuration());
                float f_duration = MathCalculate.float2float(duration / 60.0f, 1);
                UtilLog.logWithCodeInfo("Duration = " + f_duration, "generateDataBarByApp", "MonthActivity");
                UtilLog.logWithCodeInfo("Day = " + day, "generateDataBarByApp", "MonthActivity");
                yVals.set(day - 1, new BarEntry(f_duration, day - 1));
            }
        } else { //默认数据

            xVals = getMonths();
            yVals = new ArrayList<BarEntry>();
            for (int i = 0; i < 12; i++) {
                yVals.add(new BarEntry((int) (Math.random() * 70) + 30, i));
            }
        }

        BarDataSet d = new BarDataSet(yVals, appName);
        d.setBarSpacePercent(20f);
        d.setColors(ColorTemplate.HARVEY_COLORS);
        d.setHighLightAlpha(255);
        d.setValueTextSize(12f);


        BarData cd = new BarData(xVals, d);

        return cd;
    }
    private long massageDur(long dur) {
        if (dur < 6) dur = 6;
        return dur;

    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private PieData generateDataPie(int cnt) {
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();

        if (retList != null && retList.size() > 0) {

            long totaltime = 0;
            for (int i = 0; i < retList.size(); i++) {
                totaltime += retList.get(i).getDuration();
            }
            for (int i = 0; i < retList.size(); i++) {
                xVals.add(retList.get(i).getApp_name());

                long time = retList.get(i).getDuration();
                float percent = (float) time / (float) totaltime;
                yVals1.add(new BarEntry(percent, i));
            }
        } else { //默认数据
            for (int i = 0; i < 4; i++) {
                yVals1.add(new Entry((int) (Math.random() * 70) + 30, i));
            }

            xVals = getQuarters();
        }

        PieDataSet d = new PieDataSet(yVals1, "Pie Data Set " + cnt);

        // space between slices
        d.setSliceSpace(2f);
        d.setColors(ColorTemplate.HARVEY_COLORS);
        d.setValueTextSize(14f);
        PieData cd = new PieData(xVals, d);
        return cd;
    }

    private ArrayList<String> getQuarters() {

        ArrayList<String> q = new ArrayList<String>();
        q.add("1st Quarter");
        q.add("2nd Quarter");
        q.add("3rd Quarter");
        q.add("4th Quarter");

        return q;
    }

    private ArrayList<String> getMonths() {

        ArrayList<String> m = new ArrayList<String>();
        m.add("Jan");
        m.add("Feb");
        m.add("Mar");
        m.add("Apr");
        m.add("May");
        m.add("Jun");
        m.add("Jul");
        m.add("Aug");
        m.add("Sep");
        m.add("Okt");
        m.add("Nov");
        m.add("Dec");

        return m;
    }
}
