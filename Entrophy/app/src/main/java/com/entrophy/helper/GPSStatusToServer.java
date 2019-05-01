package com.entrophy.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.entrophy.model.DAshboardRes;
import com.google.gson.Gson;

import org.json.JSONObject;

public class GPSStatusToServer extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        String sealth_mode = SharedPreference.getString(context,Constants.KEY_SEALTH_MODE);
        System.out.println("KEY_SEALTH_MODE sealth_mode "+sealth_mode);

        try {
            GPSTracker gpsTracker = new GPSTracker(context);
            String gpsStatus = "Y";
            if(gpsTracker.getLatitude() == 0) {
                gpsStatus = "off";
            } else {
                gpsStatus = "on";
            }
            JSONObject request = new JSONObject();
            request.put("user_id", SharedPreference.getInt(context, Constants.KEY_USER_ID));
            request.put("access_token", SharedPreference.getString(context, Constants.KEY_ACCESS_TOKEN));
            request.put("gps", gpsStatus);
            System.out.println("starGPSService " + request.toString());
            Constants.invokeAPIEx(context, Constants.USERDASHBOARDCOUNT, request.toString(), new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    String result = (String) message.obj;
                    try {
                        DAshboardRes data = new Gson().fromJson(result, DAshboardRes.class);
                        if(data.getStatus().equalsIgnoreCase("success")) {
                            SharedPreference.setString(context,Constants.KEY_SEALTH_MODE,data.getStealth_mode());
                            SharedPreference.setString(context,Constants.KEY_NOTIFICATION_COUNT,data.getNotification_count());
                            SharedPreference.setString(context,Constants.KEY_FRIEND_COUNT,data.getFriend_count());
                        }
                    } catch (Exception e){

                    }
                    return false;
                }
            }));


        } catch (Exception e) {
            System.out.println("InsertLocationServer Exception " + e.toString());
        }
    }
}
