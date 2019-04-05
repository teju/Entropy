package com.entrophy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.entrophy.helper.Constants;

public class ConnectionSet extends Activity implements View.OnClickListener{

    private TextView bottom_text;
    private Button skip;
    private TextView header_label;
    private Button done;
    private ImageView back_button;
    private boolean isSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_set);
        initUI();
    }

    public void initUI() {
        isSkip = getIntent().getBooleanExtra("skip_button",false);
        bottom_text = (TextView)findViewById(R.id.bottom_text);
        header_label = (TextView)findViewById(R.id.header_label);
        skip = (Button)findViewById(R.id.skip);
        done = (Button)findViewById(R.id.done);
        back_button = (ImageView)findViewById(R.id.back_button);
        skip.setOnClickListener(this);
        done.setOnClickListener(this);
        back_button.setOnClickListener(this);
        if(isSkip) {
            bottom_text.setVisibility(View.GONE);
            skip.setVisibility(View.GONE);
            header_label.setText("You’re All Set!");
            done.setText("Done");
        } else {
            bottom_text.setVisibility(View.VISIBLE);
            skip.setVisibility(View.VISIBLE);
            header_label.setText("Request Sent!");
            done.setText("Let's Invite");
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.back_button) {
            super.onBackPressed();
        } else if(view.getId() == R.id.done) {
            if(isSkip) {
                Intent i = new Intent(this, HomeActivity.class);
                startActivity(i);
            } else {
                Intent i = new Intent(this, Contacts.class);
                i.putExtra("ConnectionType", Constants.InviteContacts);
                startActivity(i);

            }
        }  else if(view.getId() == R.id.skip) {
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
        }
    }
}
