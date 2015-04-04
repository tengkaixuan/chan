package com.dobi.jiecon;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dobi.jiecon.utils.IAlarmDialog;

public class UtilLog {
    public static final String wapsid = "241d20127d6ec1e73ff60be7039e8931";
    public static final String WiChatAppID = "wxbcb36ac7ce9a5ba3";
    public static final String QQAppID = "801513551";
    public static final String QQAppPWD = "KEY:f0f4d766df48c23fc1af9c1ab9453e1f";
    public static final String GoogleID = "a1540362eea2ae5";
    public static final String eomobiID = "3D52E41AC26EEE035AC31788C7809694";
    public static final String midiID = "19128";
    public static final String midiPWD = "v4lmm1nzzwr9yay2";
    public static final String youmiID = "49f4ecd73eae56e2";
    public static final String youmiPWD = "3dcfdaafe287e028";

    public static final String advWapsType = "20";
    public static final String advMidiType = "21";
    public static final String advYoumitype = "4";
    public static final String snsInvateCode = "1008";
    private static final boolean DEBUG = true;
    private static Context ctx;
    private static String msg;
    private static int duration;

    static public void logD(String string) {
        if (DEBUG)
            Log.d("harvey", "" + string);
    }

    static public void logE(String string) {
        if (DEBUG)
            Log.e("harvey", "" + string);
    }

    static public void logW(String string) {
        if (DEBUG)
            Log.w("harvey", "" + string);
    }

    static public void logControlD(String string) {
        if(DEBUG) Log.d("harvey", string);
    }
    static public void logControlR(String string) {
        if(DEBUG) Log.v("Rock", string);
    }

    static public void logWithCodeInfo(String msg, String method, String cls_name) {
        if(DEBUG)
        Log.v(produceTag(method, cls_name), msg);
    }

    private static String produceTag(String method, String cls_name){
        return "Rock -- "+ cls_name + "." + method;
    }

    static public void logAdv(String string) {
        if(DEBUG) Log.d("harvey", "" + string);
    }
    static public void showToast(Context _ctx, String _msg, final int _duration) {
        ctx = _ctx;
        msg = _msg;
        duration = _duration;
        ((Activity) ctx).runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        Toast t = Toast.makeText(ctx, msg, duration);
//                        t.setMargin(50 , 50);
                        t.show();
                    }
                }
        );
    }
    static public void showAlert(Context context, String dialogtitle, String dialogmsg) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.pop_dialog_layout);

        TextView title = (TextView) dialog.findViewById(R.id.title);

        title.setText(dialogtitle);
        Button dialogButton = (Button) dialog.findViewById(R.id.pos_button);
        dialogButton.setText(android.R.string.yes);
        TextView msg = (TextView) dialog.findViewById(R.id.message);
        msg.setText(dialogmsg);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }
    static public void showAlertWithCall(Context context, String dialogtitle, String dialogmsg, IAlarmDialog callback) {
        final Dialog dialog = new Dialog(context);
        final IAlarmDialog cbk = callback;
        dialog.setContentView(R.layout.pop_dialog_layout);

        TextView title = (TextView) dialog.findViewById(R.id.title);

        title.setText(dialogtitle);
        Button dialogButton = (Button) dialog.findViewById(R.id.pos_button);
        dialogButton.setText(android.R.string.yes);
        TextView msg = (TextView) dialog.findViewById(R.id.message);
        msg.setText(dialogmsg);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    cbk.consume();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }
}
