package com.entrophy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WelcomePage extends AppCompatActivity implements View.OnClickListener{

    private Button accept_continue;
    private TextView terms_condition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        init();


    }
    public void init() {
        accept_continue = (Button)findViewById(R.id.accept_continue);
        terms_condition = (TextView)findViewById(R.id.terms_condition);
        accept_continue.setOnClickListener(this);
        terms_condition.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.accept_continue) {
            Intent intent = new Intent(this,EnterPhoneNumber.class);
            startActivity(intent);
        }else if(view.getId() == R.id.terms_condition) {
           Intent i =new Intent(this,TermsConditions.class);
            startActivity(i);
        }
    }


}
