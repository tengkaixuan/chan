package com.dobi.jiecon.activities;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.dobi.jiecon.R;
import com.dobi.jiecon.TitlebarListener;
import com.dobi.jiecon.datacontroller.RegistrationManager;
import com.dobi.jiecon.datacontroller.SupervisionManager;
import com.dobi.jiecon.service.PeekService;
import com.dobi.jiecon.utils.DebugControl;

public class TabSample extends TabActivity implements OnTabChangeListener {
    /**
     * Called when the activity is first created.
     */
     ImageView m_title_bar_back;
//    ImageView m_title_bar_refresh;
    ImageView m_title_bar_icon;
    ImageView m_title_pie_icon;
    TextView m_title_text;
    private TitlebarListener mDayTitleBar;
    private TitlebarListener mFamilyTitleBar;
    public static TabActivity mTab;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.common_titlebar);

        mTab = this;
        setTabs();

        m_title_bar_back = (ImageView) this.findViewById(R.id.title_bar_back);
        m_title_bar_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        m_title_bar_icon = (ImageView) findViewById(R.id.bar_chart);
        m_title_pie_icon = (ImageView) findViewById(R.id.pie_chart);

        m_title_text = (TextView) this.findViewById(R.id.title_bar_text);

        // TODO: This is for test purpose, which should be removed in future
        if (DebugControl.DEBUG_FLAG)
            startService(new Intent(this, PeekService.class));
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        SupervisionManager.remove_lock_screen_policy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        RegistrationManager.update_user_info_async();
    }

    private void setTabs() {
        Resources res = this.getResources();
        addTab(res.getString(R.string.tab_everyday), R.drawable.tab_earnmoney_layout, DayActivity.class);
        addTab(res.getString(R.string.tab_everymonth), R.drawable.tab_fee_icon_layout, MonthActivity.class);
        addTab(res.getString(R.string.tab_monitor), R.drawable.tab_choujian_layout, ControlViewActivity.class);
        addTab(res.getString(R.string.tab_setting), R.drawable.tab_more_layout, SettingsActivity.class);
    }

    private void addTab(String labelId, int drawableId, Class<?> c) {
        TabHost tabHost = getTabHost();
        Intent intent = new Intent(this, c);
        TabHost.TabSpec spec = tabHost.newTabSpec("" + labelId);

        View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
        TextView title = (TextView) tabIndicator.findViewById(R.id.title);

        Resources res = this.getResources();
        String res_day = res.getString(R.string.tab_everyday);
//        String res_month = res.getString(R.string.tab_everymonth);
//        String res_family = res.getString(R.string.tab_monitor);
//        String res_setting = res.getString(R.string.tab_setting);

        if(!labelId.equals(res_day)) {
            title.setTextColor(Color.parseColor("#FFFFFF"));
            title.setTextSize(14);
            title.setTypeface(null, Typeface.NORMAL);
        } else {
            title.setTextColor(Color.parseColor("#283a17"));
            title.setTextSize(18);
            title.setTypeface(null, Typeface.BOLD);
        }
        title.setText(labelId);
        ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
        icon.setImageResource(drawableId);

        spec.setIndicator(tabIndicator);
        spec.setContent(intent);
        tabHost.addTab(spec);
        tabHost.setOnTabChangedListener(this);
    }

    @Override
    public void onTabChanged(String tabId) {
        m_title_text.setText(tabId);
//        m_title_bar_refresh
        Resources res = this.getResources();
        String res_day = res.getString(R.string.tab_everyday);
        String res_month = res.getString(R.string.tab_everymonth);
        String res_family = res.getString(R.string.tab_monitor);
        String res_setting = res.getString(R.string.tab_setting);

        for (int i = 0; i < getTabWidget().getChildCount(); i++) {
            if (getTabWidget().getChildAt(i) instanceof RelativeLayout) {
                RelativeLayout relativeLayout = (RelativeLayout) getTabWidget().getChildAt(i);
                for (int j = 0; j < relativeLayout.getChildCount(); j++) {
                    View view = relativeLayout.getChildAt(j);
                    if (relativeLayout.getChildAt(j) instanceof TextView) {
                        TextView lableText = (TextView) relativeLayout.getChildAt(j);
                        String lable = lableText.getText().toString();
                        if (lable.equals(tabId)) {
                            lableText.setTextColor(Color.parseColor("#283a17"));
                            lableText.setTextSize(18);
                            lableText.setTypeface(null, Typeface.BOLD);
                        } else {
                            lableText.setTextColor(Color.parseColor("#FFFFFF"));
                            lableText.setTextSize(14);
                            lableText.setTypeface(null, Typeface.NORMAL);
                        }
                    }
                }

            }
        }

        if (tabId.equals(res_day)) {
            this.m_title_bar_back.setVisibility(View.VISIBLE);
            this.m_title_bar_icon.setImageResource(R.drawable.bar_chart_icon);
            this.m_title_bar_icon.setVisibility(View.VISIBLE);
            this.m_title_pie_icon.setVisibility(View.VISIBLE);
//            DayActivity.mTitleBarListener.onBackHome(m_title_bar_back);
            DayActivity.mTitleBarListener.showBarChart();
            DayActivity.mTitleBarListener.showPieChart();
        } else if (tabId.equals(res_month)) {
//            this.m_title_bar_back.setVisibility(View.INVISIBLE);
            this.m_title_bar_icon.setVisibility(View.GONE);
            this.m_title_pie_icon.setVisibility(View.GONE);
        } else if (tabId.equals(res_family)) {
            this.m_title_pie_icon.setVisibility(View.GONE);
            this.m_title_bar_back.setVisibility(View.INVISIBLE);
            this.m_title_bar_icon.setImageResource(R.drawable.add_contact);
            this.m_title_bar_icon.setVisibility(View.VISIBLE);
            ControlViewActivity.mFamilyTitleBarListener.addContact();
        } else if (tabId.equals(res_setting)){ //设定
//            this.m_title_bar_back.setVisibility(View.INVISIBLE);
            this.m_title_bar_icon.setVisibility(View.INVISIBLE);
            this.m_title_pie_icon.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return  super.dispatchTouchEvent(ev);

    }
    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {

            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

}