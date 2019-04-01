package com.entrophy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.entrophy.helper.Constants;
import com.entrophy.helper.SharedPreference;

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

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.profile_setup) {
            if(SharedPreference.getBool(this, Constants.KEY_IS_LOGGEDIN)) {
                Intent i = new Intent(this, Contacts.class);
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
}
