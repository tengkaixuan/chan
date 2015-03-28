package com.github.mikephil.charting;

import com.dobi.jiecon.App;
import com.dobi.jiecon.R;
import com.github.mikephil.charting.utils.ValueFormatter;

import java.text.DecimalFormat;

/**
 * Created by harvey on 3/10/15.
 */
public class MyValueFormatter implements ValueFormatter {

    private DecimalFormat mFormat;

    public MyValueFormatter() {
        mFormat = new DecimalFormat("###,###,###,##0.0");
    }

    @Override
    public String getFormattedValue(float value) { //value单位：分
        int hours = 0;
        float mins = 0;
        String format = "";
        if (value > 60) { //大于60分钟，显示小时
            hours = (int)value / 60; // min
            mins = ((value /60.0f) - hours) * 60;
//            String xformat = App.getAppContext().getResources().getString(R.string.xformathhmm);
//            format = String.format(xformat,hours, mins);
            String hh = App.getAppContext().getResources().getString(R.string.xformathh);
            String mm = App.getAppContext().getResources().getString(R.string.xformatmm);
            format = String.format("%02d%s%.0f%s", hours,hh, mins,mm);
        } else { //
            String xformat = App.getAppContext().getResources().getString(R.string.xformatmm);
            format = mFormat.format(value) + xformat;
        }
        return format;
    }
}
