package com.entrophy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bigbangbutton.editcodeview.EditCodeListener;
import com.bigbangbutton.editcodeview.EditCodeView;
import com.entrophy.helper.Helper;

public class EnterPhoneNumber extends Activity implements View.OnClickListener{

    private EditCodeView phone_number;
    private TextView country_code;
    private Button send_otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_phone_number);
        init();
    }
    public void init(){

        phone_number = (EditCodeView)findViewById(R.id.phone_number);
        send_otp = (Button)findViewById(R.id.send_otp);
        send_otp.setOnClickListener(this);
        send_otp.setEnabled(false);
        phone_number.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                    Helper.hideKeyboard(EnterPhoneNumber.this);
                    return true;
                }
                return false;
            }
        });
        country_code = (TextView)findViewById(R.id.country_code);
        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.NORMAL);
        country_code.setTypeface(boldTypeface);
        phone_number.setEditCodeListener(new EditCodeListener() {
            @Override
            public void onCodeReady(String code) {
                if(code.length() != 0) {
                    send_otp.setEnabled(true);
                    send_otp.setBackground(getResources().getDrawable(R.drawable.rounded_button_green));
                } else {
                    send_otp.setEnabled(false);
                    send_otp.setBackground(getResources().getDrawable(R.drawable.rounded_button_gray));
                }
            }
        });

    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.send_otp) {
            Intent intent = new Intent(this,OTPVerification.class);
            startActivity(intent);
        }
    }
}
