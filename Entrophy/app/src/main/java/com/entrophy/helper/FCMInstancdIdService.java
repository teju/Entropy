package com.entrophy.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.entrophy.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by nz160 on 08-06-2017.
 */

public class    FCMInstancdIdService  extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String resent_token= FirebaseInstanceId.getInstance().getToken();
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences
                (getString(R.string.fcm_pref), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(getString(R.string.fcm_token),resent_token);
        editor.commit();
        System.out.println("FCMInstancdIdService resent_token "+sharedPreferences.getString(Constants.KEY_FIREBASEID,""));


    }

}
