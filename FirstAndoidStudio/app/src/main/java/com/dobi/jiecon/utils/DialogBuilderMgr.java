package com.dobi.jiecon.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dobi.jiecon.R;

/**
 * Created by rock on 15/2/14.
 */
public class DialogBuilderMgr {

    public static void CreateAlertDialog(Context cxt, final String hint_msg, final IAlarmDialog yesCallback, final IAlarmDialog noCallback, final IAlarmDialog cancelCallback) {
        if (yesCallback == null || noCallback == null) {
            return;
        }

        final Dialog dialog = new Dialog(cxt);
        dialog.setContentView(R.layout.pop_dialog_layout);
        Resources res = cxt.getResources();
        String hint_title = res.getString(R.string.family_bind_dialog_monitor_title);
        String binding_hint = res.getString(R.string.OK);
        TextView title = (TextView) dialog.findViewById(R.id.title);
        title.setText(hint_title);
        TextView msg = (TextView) dialog.findViewById(R.id.message);
        msg.setText(hint_msg+"?");

        Button pos_button = (Button) dialog.findViewById(R.id.pos_button);
        pos_button.setText(R.string.YES);
        Button nag_button = (Button) dialog.findViewById(R.id.nag_button);
        nag_button.setText(R.string.NO);
        nag_button.setVisibility(View.VISIBLE);
        pos_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yesCallback.consume();
                dialog.dismiss();
            }
        });
        nag_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noCallback.consume();
                dialog.dismiss();
            }
        });

        if (cancelCallback != null) {
        }

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
//        return builder.create();
    }


    public static AlertDialog CreateAgreeDialog(Context cxt, final String hint_msg, final IAlarmDialog agreeCallback, final IAlarmDialog disagreeCallback, final IAlarmDialog cancelCallback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(cxt);

        Resources res = cxt.getResources();
        String hint_title = res.getString(R.string.family_bind_dialog_monitor_title);
//        String binding_hint = res.getString(R.string.OK);

        builder.setTitle(hint_title);
//        builder.setMessage(binding_hint+"?");
        builder.setMessage(hint_msg);
        if (agreeCallback == null || disagreeCallback == null) {
            return null;
        }
        builder.setPositiveButton(res.getString(R.string.AGREE), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                agreeCallback.consume();
            }

        });

        builder.setNegativeButton(res.getString(R.string.DISAGREE), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                disagreeCallback.consume();
            }
        });
        builder.setNeutralButton(res.getString(R.string.WAITMONMENT), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (cancelCallback == null) {
                    return;
                }
                cancelCallback.consume();
            }
        });
        return builder.create();
    }

    public static AlertDialog CreateOKCancelDialog(Context cxt, final String hint_msg, final IAlarmDialog agreeCallback, final IAlarmDialog cancelCallback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(cxt);

        Resources res = cxt.getResources();
        String hint_title = res.getString(R.string.family_bind_dialog_monitor_title);
//        String binding_hint = res.getString(R.string.OK);

        builder.setTitle(hint_title);
//        builder.setMessage(binding_hint+"?");
        builder.setMessage(hint_msg);
        if (agreeCallback == null) {
            return null;
        }
        builder.setPositiveButton(res.getString(R.string.OK), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                agreeCallback.consume();
            }

        });

        builder.setNeutralButton(res.getString(R.string.CANCEL), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (cancelCallback == null) {
                    return;
                }
                cancelCallback.consume();
            }
        });
        return builder.create();
    }
}
