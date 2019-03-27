package com.entrophy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LetsConnect extends Activity implements View.OnClickListener{

    private Button lets_connect;
    private EditText bio,name;
    Boolean isConnected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lets_connect);
        init() ;
    }

    private void init() {
        lets_connect = (Button)findViewById(R.id.lets_connect);
        bio = (EditText)findViewById(R.id.bio);
        name = (EditText)findViewById(R.id.name);
        lets_connect.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(!isConnected) {
            lets_connect.setBackground(getResources().getDrawable(R.drawable.rounded_button_green));
            bio.setText("Professional Photographer & Blogger Working from StarBucks!");
            name.setText("Prathap D K");
            bio.setBackgroundResource(android.R.color.transparent);
            name.setBackgroundResource(android.R.color.transparent);
            isConnected = true;
        } else {
            Intent intent = new Intent(this,Contacts.class);
            startActivity(intent);
        }

    }
}
