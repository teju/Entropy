package com.entrophy.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import org.json.JSONObject;

public class InsertLocationServer extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        String sealth_mode = SharedPreference.getString(context,Constants.KEY_SEALTH_MODE);
        System.out.println("KEY_SEALTH_MODE sealth_mode "+sealth_mode);

        if(sealth_mode.length() == 0 || sealth_mode.equalsIgnoreCase("N")) {
            try {
                GPSTracker gpsTracker = new GPSTracker(context);
                JSONObject request = new JSONObject();
                JSONObject location = new JSONObject();
                location.put("country", gpsTracker.getCountryName(context));
                location.put("pincode", gpsTracker.getPostalCode(context));
                location.put("state", gpsTracker.getState(context));
                location.put("city", gpsTracker.getCity(context));
                location.put("location", gpsTracker.getLocality(context));
                location.put("lattitude", String.valueOf(gpsTracker.getLatitude()));
                location.put("longitude", String.valueOf(gpsTracker.getLongitude()));
                location.put("formatted_address", gpsTracker.getAddressLine(context));
//            location.put("country","Malaysia");
//            location.put("pincode","50470");
//            location.put("state","Kuala Lumpur");
//            location.put("city","Kuala Lumpur");
//            location.put("lattitude","3.1336");
//            location.put("longitude","101.6927");
//            location.put("formatted_address","Jalan Tebing, Brickfields, 50470 Kuala Lumpur, Wilayah Persekutuan Kuala Lumpur, Malaysia");
                request.put("user_id", SharedPreference.getInt(context, Constants.KEY_USER_ID));
                request.put("access_token", SharedPreference.getString(context, Constants.KEY_ACCESS_TOKEN));
                request.put("location_details", location);
                System.out.println("InsertLocationServer " + request.toString());
                if(gpsTracker.getLatitude() != 0) {
                    Constants.invokeAPIEx(context, Constants.LOCATIONUPDATE, request.toString(), new Handler(new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message message) {

                            return false;
                        }
                    }));
                }

            } catch (Exception e) {
                System.out.println("InsertLocationServer Exception " + e.toString());
            }
        }
    }

}
