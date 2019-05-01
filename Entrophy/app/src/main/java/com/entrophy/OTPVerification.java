package com.entrophy;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
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
import com.entrophy.helper.SharedPreference;
import com.entrophy.model.LoginApiResult;
import com.entrophy.model.OtpApiResult;
import com.google.gson.Gson;

import org.json.JSONObject;

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
        TextView resend_otp = (TextView) findViewById(R.id.resend_otp);
        verify.setOnClickListener(this);
        resend_otp.setOnClickListener(this);
        verify.setEnabled(false);
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
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onCodeReady(String code) {
                if(code.length() != 0) {
                    verify.setEnabled(true);
                    verify.setBackground(getResources().getDrawable(R.drawable.rounded_button_green));
                } else {
                    verify.setEnabled(false);
                    verify.setBackground(getResources().getDrawable(R.drawable.rounded_button_gray));
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.verify ) {
            if(!Constants.isValidOtp(otp_number.getCode())){
                new CustomToast().Show_Toast(OTPVerification.this, "Invalid OTP",R.color.red);

            } else {
                callAPI();
            }
        } else if(view.getId() == R.id.resend_otp) {
            calLoginlAPI();
        }
    }
    public void callAPI() {
        try {
            JSONObject params = new JSONObject();
            JSONObject login_jsonObject = new JSONObject();
            JSONObject otp_jsonObject = new JSONObject();
            login_jsonObject.put("username",getIntent().getStringExtra("phone_number"));
            otp_jsonObject.put("otp_code",otp_number.getCode());
            params.put("LoginForm",login_jsonObject);
            params.put("OtpForm",otp_jsonObject);

            Loader.show(this);
            Constants.invokeAPIEx(this,Constants.OTP,params.toString(),new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    Loader.hide();
                    if(message != null && message.obj != null) {
                        String result = (String) message.obj;
                        OtpApiResult data = new Gson().fromJson(result, OtpApiResult.class);
                        System.out.println("OTPVerificationPrint callAPI Response "+result);

                        if(data.getStatus().equalsIgnoreCase("success")) {
                            if(data.getAccess_token() != null) {
                                SharedPreference.setInt(OTPVerification.this,Constants.KEY_USER_ID,data.getUser_id());
                                SharedPreference.setString(OTPVerification.this,Constants.KEY_USER_NAME,data.getName());
                                SharedPreference.setString(OTPVerification.this,Constants.KEY_MOBILE_NO,getIntent().getStringExtra("phone_number"));
                                SharedPreference.setString(OTPVerification.this,Constants.KEY_ACCESS_TOKEN,data.getAccess_token());
                                SharedPreference.setString(OTPVerification.this,Constants.KEY_TEXTMESSAGE,data.getInvitemessage());
                                SharedPreference.setBool(OTPVerification.this,Constants.KEY_IS_LOGGEDIN,true);
                                if(data.getProfile_image() != null) {
                                    SharedPreference.setString(OTPVerification.this, Constants.KEY_IMAGEPATH, data.getProfile_image());
                                }
                                Intent intent = new Intent(OTPVerification.this,OtpVerificationSuccess.class);
                                intent.putExtra("phone_number",getIntent().getStringExtra("phone_number"));
                                startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(OTPVerification.this,OtpVerificationSuccess.class);
                                intent.putExtra("phone_number",getIntent().getStringExtra("phone_number"));
                                startActivity(intent);
                                finish();
                            }

                        } else {
                            if(data.getErrors() != null) {
                                new CustomToast().Show_Toast(OTPVerification.this, data.getErrors().getOtp_code().get(0),R.color.red);

                            } else {
                                new CustomToast().Show_Toast(OTPVerification.this, ConstantStrings.common_msg,R.color.red);

                            }
                        }
                    } else {
                        new CustomToast().Show_Toast(OTPVerification.this, ConstantStrings.common_msg,R.color.red);

                    }
                    return false;
                }
            }));
        } catch (Exception e){
            System.out.println("OTPVerificationPrint callAPI Exception "+e.toString());
        }
    }

    public void calLoginlAPI() {
        try {
            JSONObject params = new JSONObject();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username",getIntent().getStringExtra("phone_number"));
            params.put("LoginForm",jsonObject);
            Loader.show(this);

            Constants.invokeAPIEx(this,Constants.Login,params.toString(),new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    Loader.hide();
                    if(message != null && message.obj != null) {
                        String result = (String) message.obj;
                        LoginApiResult data = new Gson().fromJson(result, LoginApiResult.class);
                        System.out.println("OTPVerificationPrint callAPI Response "+result+" status "+data.getStatus());

                        if(data.getStatus().equalsIgnoreCase("success")) {
                            callAPI();
                        } else {
                            if(data.getErrors() != null) {
                                new CustomToast().Show_Toast(OTPVerification.this, data.getErrors().getOtp_code().get(0),R.color.red);
                            } else {
                                new CustomToast().Show_Toast(OTPVerification.this, ConstantStrings.common_msg,R.color.red);
                            }
                        }
                    } else {
                        new CustomToast().Show_Toast(OTPVerification.this, ConstantStrings.common_msg,R.color.red);

                    }
                    return false;
                }
            }));
        } catch (Exception e){
            System.out.println("OTPVerificationPrint callAPI Exception "+e.toString());
        }
    }


}
