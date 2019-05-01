package com.entrophy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.entrophy.helper.Constants;
import com.entrophy.helper.SharedPreference;

public class InviteOthers extends AppCompatActivity implements View.OnClickListener{

    private ImageView whats_app;
    private ImageView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_others);
        init();
    }

    private void init() {
        whats_app = (ImageView)findViewById(R.id.whats_app);
        TextView later = (TextView) findViewById(R.id.later);
        whats_app.setOnClickListener(this);
        message = (ImageView)findViewById(R.id.message);
        message.setOnClickListener(this);
        later.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.whats_app) {
            sendSms();
        } else   if(view.getId() == R.id.message) {
            Intent i = new Intent(this, Contacts.class);
            i.putExtra("ConnectionType", Constants.InviteContacts);
            startActivity(i);

        } else   if(view.getId() == R.id.later) {
            Intent intent = new Intent(this, ConnectionSet.class);
            intent.putExtra("skip_button", true);
            startActivity(intent);

        }
    }

    public void sendSms( ) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.setPackage("com.whatsapp");           // so that only Whatsapp reacts and not the chooser
        i.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        i.putExtra(Intent.EXTRA_TEXT, SharedPreference.getString(InviteOthers.this,Constants.KEY_TEXTMESSAGE));
        startActivityForResult(Intent.createChooser(i, "Share via"),1);
    }


}
