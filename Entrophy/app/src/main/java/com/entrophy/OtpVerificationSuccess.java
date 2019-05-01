package com.entrophy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.entrophy.helper.Constants;
import com.entrophy.helper.Loader;
import com.entrophy.helper.SharedPreference;

import org.json.JSONObject;

public class OtpVerificationSuccess extends Activity implements View.OnClickListener {

    private Button profile_setup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification_success);
        init();
    }

    private void init() {
        profile_setup = (Button)findViewById(R.id.profile_setup);
        profile_setup.setOnClickListener(this);
        callAPI();

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.profile_setup) {
            if(SharedPreference.getBool(this, Constants.KEY_IS_LOGGEDIN)) {
                Intent i = new Intent(OtpVerificationSuccess.this, Contacts.class);
                startActivity(i);
                finish();

            } else {
                Intent i = new Intent(this, LetsConnect.class);
                i.putExtra("phone_number", getIntent().getStringExtra("phone_number"));
                startActivity(i);
                finish();
            }



        }

    }

    public void callAPI() {
        try {
            JSONObject params = new JSONObject();
            JSONObject login_jsonObject = new JSONObject();
            if(SharedPreference.getBool(this, Constants.KEY_IS_LOGGEDIN)) {
                login_jsonObject.put("mobile_no",SharedPreference.getString(this,Constants.KEY_MOBILE_NO));
            }else {
                login_jsonObject.put("mobile_no",getIntent().getStringExtra("phone_number"));
            }
            SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences
                    (getString(R.string.fcm_pref), Context.MODE_PRIVATE);
            login_jsonObject.put("google_fcm_id",sharedPreferences.getString(getString(R.string.fcm_token),""));

            params.put("PushNotification",login_jsonObject);

            Loader.show(this);
            Constants.invokeAPIEx(this,Constants.PUSHNOTIFICATION,params.toString(),new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    Loader.hide();
                    if(message != null && message.obj != null) {
                        String result = (String) message.obj;
                    }
                        return false;
                }
            }));
        } catch (Exception e){
            System.out.println("OtpVerificationSuccessPrint callAPI Exception "+e.toString());
        }
    }

}
