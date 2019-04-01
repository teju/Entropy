package com.entrophy;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bigbangbutton.editcodeview.EditCodeListener;
import com.bigbangbutton.editcodeview.EditCodeView;
import com.entrophy.helper.ConstantStrings;
import com.entrophy.helper.Constants;
import com.entrophy.helper.CustomToast;
import com.entrophy.helper.Helper;
import com.entrophy.helper.Loader;
import com.entrophy.model.LoginApiResult;
import com.google.gson.Gson;

import org.json.JSONObject;

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
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onCodeReady(String code) {
                if(code.length() >= 10) {
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
            System.out.println("EnterPhoneNumberPrint isValidMobile "
                    +!Constants.isValidMobile(phone_number.getCode()) +" getCodeLength "+phone_number.getCodeLength());

            if(!Constants.isValidMobile(phone_number.getCode()) || phone_number.getCode().length() < 10){
                new CustomToast().Show_Toast(EnterPhoneNumber.this, "Invalid Mobile Number",R.color.red);
            } else {
                callAPI();
            }
        }
    }

    public void callAPI() {
        try {
            JSONObject params = new JSONObject();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username",phone_number.getCode());
            params.put("LoginForm",jsonObject);
            Loader.show(this);

            Constants.invokeAPIEx(this,Constants.Login,params.toString(),new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    Loader.hide();
                    if(message != null && message.obj != null) {
                        String result = (String) message.obj;
                        LoginApiResult data = new Gson().fromJson(result, LoginApiResult.class);
                        System.out.println("EnterPhoneNumberPrint callAPI Response "+result+" status "+data.getStatus());

                        if(data.getStatus().equalsIgnoreCase("success")) {
                            Intent intent = new Intent(EnterPhoneNumber.this, OTPVerification.class);
                            intent.putExtra("phone_number",phone_number.getCode());
                            startActivity(intent);
                        } else {
                            if(data.getErrors() != null) {
                                new CustomToast().Show_Toast(EnterPhoneNumber.this, data.getErrors().getOtp_code().get(0),R.color.red);

                            } else {
                                new CustomToast().Show_Toast(EnterPhoneNumber.this, ConstantStrings.common_msg,R.color.red);

                            }
                        }
                    } else {
                        new CustomToast().Show_Toast(EnterPhoneNumber.this, ConstantStrings.common_msg,R.color.red);

                    }
                    return false;
                }
            }));
        } catch (Exception e){
            System.out.println("EnterPhoneNumberPrint callAPI Exception "+e.toString());
        }
    }
}
