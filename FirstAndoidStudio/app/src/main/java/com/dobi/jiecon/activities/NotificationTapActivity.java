package com.dobi.jiecon.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.dobi.jiecon.R;

public class NotificationTapActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_tap);
        Intent intent = getIntent();
        String msg = intent.getStringExtra("displaymsg");
        long status = intent.getLongExtra("notifitystatus", 0);
        String name = intent.getStringExtra("name");
/*
        switch ((int) status) {
            case RelationData.RELATION_FATHER_SUPERVISION_REQUEST: //10

                                        if (true == SupervisionManager.agree_supervision_request_with_activity(cxt, father_id, son_id, "")) {

                                            SupervisionManager.add_family_async(contacts_name, contacts_phone);
                                            UtilLog.showToast(cxt, getString(R.string.status_11_label), Toast.LENGTH_LONG);
                                        } else {
                                            UtilLog.showToast(cxt, getString(R.string.status_11_label_fail), Toast.LENGTH_LONG);
                                        }
                                    }



                                SupervisionManager.disagree_supervision_request(father_id, son_id, "");
                                UtilLog.showToast(cxt, getString(R.string.status_12_label), Toast.LENGTH_LONG);


                break;
            case RelationData.RELATION_FATHER_APPROVE_SUPERVISION_FREE: //21
                showSonActions(true, false, false);
                UtilLog.showToast(cxt, contacts_name + getString(R.string.status_21_label_agree), Toast.LENGTH_LONG);
                break;
            case RelationData.RELATION_FATHER_DISAPPROVE_SUPERVISION_FREE: //22
                showSonActions(false, true, false);
                UtilLog.showToast(cxt, contacts_name + getString(R.string.status_22_label), Toast.LENGTH_LONG);
                break;
            case RelationData.RELATION_FATHER_APPROVE_UNLOCK:// = 31
                SupervisionManager.setLockScreen(SupervisionManager.LOCK_SCREEN_NO);
                UtilLog.showToast(cxt, contacts_name + getString(R.string.status_31_label), Toast.LENGTH_LONG);
                break;
            case RelationData.RELATION_FATHER_DISAPPROVE_UNLOCK:// = 32;
                UtilLog.showToast(cxt, contacts_name + getString(R.string.status_32_label), Toast.LENGTH_LONG);
                break;
        }
    /*
        if (status == RelationData.RELATION_FATHER_SUPERVISION_REQUEST) {
            msg_appendix = true;
            id = R.string.status_10;
        } else if (relation.getStatus() == RelationData.RELATION_SON_AGREE_SUPERVISION) {
            id = R.string.status_11;
        } else if (relation.getStatus() == RelationData.RELATION_SON_DISAGREE_SUPERVISION) {
            id = R.string.status_12;
        } else if (relation.getStatus() == RelationData.RELATION_SON_CANCEL_SUPERVISION_REQUEST) {
            msg_appendix = true;
            id = R.string.status_20;
        } else if (relation.getStatus() == RelationData.RELATION_FATHER_APPROVE_SUPERVISION_FREE) {
            id = R.string.status_21;
        } else if (relation.getStatus() == RelationData.RELATION_FATHER_DISAPPROVE_SUPERVISION_FREE) {
            id = R.string.status_22;
        } else if (relation.getStatus() == RelationData.RELATION_SON_UNLOCK_REQUEST) {
            msg_appendix = true;
            id = R.string.status_30;
        } else if (relation.getStatus() == RelationData.RELATION_FATHER_APPROVE_UNLOCK) {
            if (relation.getTime() == -25 * 60 * 60) {
                id = R.string.status_31_2;
            } else if (relation.getTime() == 25 * 60 * 60) {
                id = R.string.status_31_3;
            } else {
                id = R.string.status_31_1;
            }
        } else if (relation.getStatus() == RelationData.RELATION_FATHER_DISAPPROVE_UNLOCK) {
            id = R.string.status_32;
        }
//        intent.putExtra("notifitystatus", status);
*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notification_tap, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
