package com.dobi.jiecon.datacontroller;

import android.content.res.Resources;

import com.dobi.jiecon.App;
import com.dobi.jiecon.R;
import com.dobi.jiecon.data.RelationData;

/**
 * Created by rock on 15/3/5.
 */
public class FamilyRelationFormat {
    public static String getRoleString(int role, int status) {
        Resources res = App.getAppContext().getResources();
        String ret = res.getString(R.string.family_friend);
        //exclude "SUPERVISON_NO" status
        if (status != RelationData.RELATION_SUPERVISION_NO) {
            switch (role) {
                case RelationData.RELATION_ROLE_FATHER:
                    ret = res.getString(R.string.family_parent);
                    break;
                case RelationData.RELATION_ROLE_SON:
                    ret = res.getString(R.string.family_kid);
                    break;
            }
        }
        return ret;
    }

    public static String getStatusString(int status) {
        String ret = "";
        Resources res = App.getAppContext().getResources();

        switch (status) {
            //Status
            case RelationData.RELATION_SUPERVISION_NO:
                ret = res.getString(R.string.status_supervision_no);
                break;
            case RelationData.RELATION_SUPERVISION_YES:
                ret = res.getString(R.string.status_supervision_yes);
                break;
        }
        return ret;
    }
}
