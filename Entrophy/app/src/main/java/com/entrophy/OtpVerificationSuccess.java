package com.entrophy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
            Intent i = new Intent(this, LetsConnect.class);
            startActivity(i);
        }

    }
}
