package com.entrophy;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.entrophy.helper.ConstantStrings;
import com.entrophy.helper.Constants;
import com.entrophy.helper.CustomToast;
import com.entrophy.helper.Loader;

import org.json.JSONArray;
import org.json.JSONObject;
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

public class TermsConditions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);
        callAPITermsConditions();
        Button ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TermsConditions.super.onBackPressed();
            }
        });
    }

    public void callAPITermsConditions() {
        try {

            Loader.show(this);

            Constants.invokeAPIEx(this,Constants.CUSTOMERTERMSCONDITION,"",new Handler(new Handler.Callback() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public boolean handleMessage(Message message) {
                    Loader.hide();
                    if(message != null && message.obj != null) {
                        String result = (String) message.obj;
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if(jsonObject.getString("status").equalsIgnoreCase("success")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("terms");
                                String object = jsonArray.getString(0);
                                HtmlTextView termCon = (HtmlTextView)findViewById(R.id.terms_conditions);
                                object = object.replaceAll("\"", " ");
                                object = object.replaceAll("\\[", "").replaceAll("\\]","");

                                termCon.setHtml(object, new HtmlHttpImageGetter(termCon));

                            }
                        } catch (Exception e){

                        }

                    } else {
                        new CustomToast().Show_Toast(TermsConditions.this, ConstantStrings.common_msg,R.color.red);

                    }
                    return false;
                }
            }));
        } catch (Exception e){
            System.out.println("LetsConnectPrint callAPI Exception "+e.toString());
        }
    }

}
