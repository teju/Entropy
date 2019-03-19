package com.entrophy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.bigbangbutton.editcodeview.EditCodeListener;
import com.bigbangbutton.editcodeview.EditCodeView;
import com.entrophy.helper.Helper;

public class OTPVerification extends Activity implements View.OnClickListener{

    private EditCodeView otp_number;
    private Button verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);
        init();
    }
    public void init() {
        otp_number = (EditCodeView)findViewById(R.id.otp_number);
        verify = (Button)findViewById(R.id.verify);
        verify.setOnClickListener(this);
        otp_number.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                    Helper.hideKeyboard(OTPVerification.this);
                    return true;
                }
                return false;
            }
        });
        otp_number.setEditCodeListener(new EditCodeListener() {
            @Override
            public void onCodeReady(String code) {
                if(code.length() != 0) {
                    verify.setBackground(getResources().getDrawable(R.drawable.rounded_button_green));
                } else {
                    verify.setBackground(getResources().getDrawable(R.drawable.rounded_button_gray));
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.verify) {
            Intent i = new Intent(this, OtpVerificationSuccess.class);
            startActivity(i);
        }

    }
}
