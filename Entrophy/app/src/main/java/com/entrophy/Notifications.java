package com.entrophy;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.entrophy.adapters.NotificationsAdapter;
import com.entrophy.helper.ConstantStrings;
import com.entrophy.helper.Constants;
import com.entrophy.helper.CustomToast;
import com.entrophy.helper.Loader;
import com.entrophy.helper.SharedPreference;
import com.entrophy.model.NotificationApiResult;
import com.google.gson.Gson;

import org.json.JSONObject;

public class Notifications extends Activity {

    private NotificationsAdapter mAdapter;
    private RecyclerView notifications;
    private NotificationApiResult notificationApiResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        initUI();
    }

    public void initUI(){
        notifications = (RecyclerView)findViewById(R.id.notifications);
        ImageView back_button = (ImageView) findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Notifications.super.onBackPressed();
            }
        });

        callAPINotifications();
//        for (int i = 1; i < 30; i++) {
//            if (i % 10 == 0 || i == 1) {
//                mAdapter.addSectionHeaderItem("Section #" + i);
//            } else {
//                mAdapter.addItem("Row Item #" + i);
//
//            }
//        }

    }

    public void callAPINotifications() {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", SharedPreference.getInt(Notifications.this, Constants.KEY_USER_ID));
            jsonObject.put("access_token", SharedPreference.getString(Notifications.this,Constants.KEY_ACCESS_TOKEN));
            jsonObject.put("offset", 0);
            jsonObject.put("limit", 100000);
            jsonObject.put("city_name", getIntent().getStringExtra("city_name"));
            Loader.show(Notifications.this);


            Constants.invokeAPIEx(Notifications.this,Constants.PUSHNOTIFFICATION,jsonObject.toString(),
                    new Handler(new Handler.Callback() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public boolean handleMessage(Message message) {
                            Loader.hide();
                            if(message != null && message.obj != null) {
                                String result = (String) message.obj;
                                try {
                                    notificationApiResult = new Gson().fromJson(result, NotificationApiResult.class);

                                    if (notificationApiResult.getStatus().equalsIgnoreCase("success")) {
                                        setDataConnect();
                                    } else {
                                        if (notificationApiResult.getMessage() != null) {
                                            new CustomToast().Show_Toast(Notifications.this, ConstantStrings.common_msg, R.color.red);
                                        } else {
                                            new CustomToast().Show_Toast(Notifications.this, ConstantStrings.common_msg, R.color.red);

                                        }

                                    }
                                } catch (Exception e){
                                    System.out.println("LetsConnectPrint callAPI Exception "+e.toString());

                                }
                            } else {
                                new CustomToast().Show_Toast(Notifications.this, ConstantStrings.common_msg,R.color.red);

                            }
                            return false;
                        }
                    }));
        } catch (Exception e){
            System.out.println("LetsConnectPrint callAPI Exception "+e.toString());
        }
    }

    private void setDataConnect() {
        mAdapter = new NotificationsAdapter(this,notificationApiResult.getNotification_list());
        notifications.setAdapter(mAdapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        notifications.setLayoutManager(mLayoutManager);
        notifications.setItemAnimator(new DefaultItemAnimator());
        notifications.setAdapter(mAdapter);
    }


}
