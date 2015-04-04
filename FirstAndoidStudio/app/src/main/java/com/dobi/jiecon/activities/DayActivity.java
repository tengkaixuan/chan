package com.dobi.jiecon.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.dobi.jiecon.App;
import com.dobi.jiecon.R;
import com.dobi.jiecon.TitlebarListener;
import com.dobi.jiecon.UtilLog;
import com.dobi.jiecon.database.AppUsage;
import com.dobi.jiecon.datacontroller.AppUsageManager;
import com.dobi.jiecon.datacontroller.RegistrationManager;
import com.dobi.jiecon.utils.Common;
import com.dobi.jiecon.utils.Config;
import com.dobi.jiecon.utils.MathCalculate;
import com.dobi.jiecon.utils.TimeFormat;
import com.github.mikephil.charting.MyValueFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.PercentFormatter;
import com.github.mikephil.charting.utils.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class DayActivity extends Activity implements OnChartValueSelectedListener, TitlebarListener {
    protected BarChart mChart;
    protected PieChart mPieChart;
    private Typeface mTf;
    private Typeface mPieTf;
    private String chart_user_id;
    static public TitlebarListener mTitleBarListener;
    //应用名写前面三个字母，当用点击时，bar上显示全称
    //Show activity abstraction, when click the bar show the whole name on the it.
    // this is for barchart
    protected String[] mMonths = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt"
    };

    //this is for pie char
    protected String[] mParties = new String[]{
            "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
            "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
            "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
            "Party Y", "Party Z"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_xy_chart);
        mTitleBarListener = this;

        mChart = (BarChart) findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);

        mPieChart = (PieChart) findViewById(R.id.piechart);
        mPieChart.setOnChartValueSelectedListener(this);
    }

    private void initBarChart() {
        mChart.setDrawBarShadow(true);
        mChart.setDrawValueAboveBar(true);
//        mChart.isDrawValuesForWholeStackEnabled
        mChart.setDrawValuesForWholeStack(true);
        mChart.setDescription("");

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn. 最多可显示的app name为60
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        // draw shadows for each bar that show the maximum value
        // mChart.setDrawBarShadow(true);

        // mChart.setDrawXLabels(false);

        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);

        mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTf);
        xAxis.setDrawGridLines(false);

        ValueFormatter custom = new MyValueFormatter();
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(mTf);
        leftAxis.setLabelCount(8);
        leftAxis.setValueFormatter(custom);


        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setTypeface(mTf);
        rightAxis.setLabelCount(8);
//        rightAxis.setValueFormatter(custom);

        setBarData(10, 50);
        mChart.animateY(2500);
    }

    private void initPieChart() {
        mPieChart.setUsePercentValues(true);

        // change the color of the center-hole
        // mChart.setHoleColor(Color.rgb(235, 235, 235));
        mPieChart.setHoleColorTransparent(true);

        mPieTf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        mPieChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));

        mPieChart.setHoleRadius(60f);

        String pie_description = getResources().getString(R.string.day_pie_descript);
        mPieChart.setDescription(pie_description);

        mPieChart.setDrawCenterText(true);

        mPieChart.setDrawHoleEnabled(true);

        mPieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mPieChart.setRotationEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
//        mPieChart.setOnChartValueSelectedListener(this);
        // mChart.setTouchEnabled(false);
        String daypiecenter = getResources().getString(R.string.day_pie_center);
        mPieChart.setCenterText(daypiecenter);
        mPieChart.setCenterTextColor(Color.WHITE);
        mPieChart.setCenterTextSize(18f);

        setPieData(9, 100);

        mPieChart.animateXY(1500, 1500);
        // mChart.spin(2000, 0, 360);

        Legend l = mPieChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);

    }

    private void setPieData(int count, float range) {
        float mult = range;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();
        String user_id = RegistrationManager.getUserId();
        List<AppUsage> ret = AppUsageManager.get_app_duration_day(user_id, TimeFormat.Now());
        long totaltime = AppUsageManager.get_today_app_duration_total();
        if (ret != null && ret.size() > 0) {
            for (int i = 0; i < ret.size(); i++) {


                xVals.add(Common.getAppName(this, ret.get(i)));
                long time = ret.get(i).getDuration();
                float percent = (float) time / (float) totaltime;
                float f_duration = MathCalculate.float2float(percent, 2);
                yVals1.add(new BarEntry(f_duration, i));
            }
        }/* else { //默认数据
            // IMPORTANT: In a PieChart, no values (Entry) should have the same
            // xIndex (even if from different DataSets), since no values can be
            // drawn above each other.
            for (int i = 0; i < count + 1; i++) {
                yVals1.add(new Entry((float) (Math.random() * mult) + mult / 5, i));
            }

            for (int i = 0; i < count + 1; i++) {
                xVals.add(mParties[i % mParties.length]);
            }
        }*/

        String discript = getResources().getString(R.string.day_app_percent);
        PieDataSet dataSet = new PieDataSet(yVals1, discript);
        dataSet.setSliceSpace(3f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.HARVEY_COLORS) {
            colors.add(c);
        }
        dataSet.setColors(colors);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(14f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(mPieTf);
        mPieChart.setData(data);

        // undo all highlights
        mPieChart.highlightValues(null);

        mPieChart.invalidate();
    }

    private void setBarData(int count, float range) {
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

//        String user_id = RegistrationManager.getUserId();
//        String user_id = "100003"; can quary the buddy id from Supervison
        UtilLog.logWithCodeInfo("Valid user id is " + chart_user_id, "setBarData", "DayActivity");
        List<AppUsage> ret = AppUsageManager.get_app_duration_day(chart_user_id, TimeFormat.Now());

        if (ret != null && ret.size() > 0) {
            for (int i = 0; i < ret.size(); i++) {
                xVals.add(ret.get(i).getApp_name());
                float duration = ret.get(i).getDuration() / 60.0f;
                yVals1.add(new BarEntry(duration, i));
            }
        } /*else { //默认数据
            for (int i = 0; i < count; i++) {
                xVals.add(mMonths[i % 10]);
            }

            for (int i = 0; i < count; i++) {
                float mult = (range + 1);
                float val = (float) (Math.random() * mult);
                yVals1.add(new BarEntry(val, i));
            }
        }*/ else {
            return;
        }

//        Legend l = mChart.getLegend();
//        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
//        l.setFormSize(12f);
//        l.setXEntrySpace(4f);

        String bardiscription = getResources().getString(R.string.day_bar_discript, Config.APP_COUNT());
        BarDataSet set1 = new BarDataSet(yVals1, bardiscription);
        set1.setBarSpacePercent(35f);
        set1.setColors(ColorTemplate.HARVEY_COLORS);

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
//        data.setXvals(xVals);
        data.setValueTextSize(12f);
        data.setValueTypeface(mTf);

        mChart.setData(data);

    }

    protected void onResume() {
        super.onResume();
        chart_user_id = this.getIntent().getStringExtra(App.KEY_FAMILY2DAY_USERID);
        UtilLog.logWithCodeInfo("Contacts user id is " + chart_user_id, "onResume", "DayActivity");
        if (chart_user_id == null || chart_user_id.equals("")) {
            chart_user_id = RegistrationManager.getUserId();
        }
        initBarChart();
        showBarChart();
        initPieChart();
        showPieChart();
    }

    @Override
    public void onBackPressed() {

    }

    @SuppressLint("NewApi")
    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

//        if (e == null)
//            return;
//
//        RectF bounds = mChart.getBarBounds((BarEntry) e);
//        PointF position = mChart.getPosition(e, mChart.getData().getDataSetByIndex(dataSetIndex)
//                .getAxisDependency());

//        drawActivityDiscription(mChart.getCanvas(), mChart.getPaint(2), position.x, position.y, "activity name");
//        Log.i("bounds", bounds.toString()+",canvas="+mChart.getCanvas()+", paint="+mChart.getPaint(2));
//        Log.i("position", position.toString());
    }

    public void onNothingSelected() {
        Log.i("harvey", "no nothing selected");
    }

    ;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.actionToggleValues: {
                for (DataSet<?> set : mChart.getData().getDataSets())
                    set.setDrawValues(!set.isDrawValuesEnabled());

                mPieChart.invalidate();
                break;
            }
            case R.id.actionToggleHole: {
                if (mPieChart.isDrawHoleEnabled())
                    mPieChart.setDrawHoleEnabled(false);
                else
                    mPieChart.setDrawHoleEnabled(true);
                mPieChart.invalidate();
                break;
            }
            case R.id.actionDrawCenter: {
                if (mPieChart.isDrawCenterTextEnabled())
                    mPieChart.setDrawCenterText(false);
                else
                    mPieChart.setDrawCenterText(true);
                mPieChart.invalidate();
                break;
            }
            case R.id.actionToggleXVals: {

                mPieChart.setDrawSliceText(!mPieChart.isDrawSliceTextEnabled());
                mPieChart.invalidate();
                break;
            }
            case R.id.actionSave: {
                // mChart.saveToGallery("title"+System.currentTimeMillis());
                mPieChart.saveToPath("title" + System.currentTimeMillis(), "");
                break;
            }
            case R.id.actionTogglePercent:
                mPieChart.setUsePercentValues(!mPieChart.isUsePercentValuesEnabled());
                mPieChart.invalidate();
                break;
            case R.id.animateX: {
                mPieChart.animateX(1800);
                break;
            }
            case R.id.animateY: {
                mPieChart.animateY(1800);
                break;
            }
            case R.id.animateXY: {
                mPieChart.animateXY(1800, 1800);
                break;
            }
        }
        return true;
    }

    @Override
    public void onBackHome(ImageView iv) {
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void showBarChart() {
        TabSample.mTab.findViewById(R.id.bar_chart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPieChart.setVisibility(View.GONE);

                mChart.removeAllViews();
                initBarChart();
                mChart.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void showPieChart() {
        TabSample.mTab.findViewById(R.id.pie_chart).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mChart.setVisibility(View.GONE);
                mPieChart.removeAllViews();
                initPieChart();
                mPieChart.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void addContact() {

    }
}
