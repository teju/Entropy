package com.entrophy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MyProfile extends AppCompatActivity implements View.OnClickListener {

    private ImageView back_button;
    private LinearLayout friend_profile;
    private LinearLayout friends_profile_header;
    private Button done;
    private LinearLayout work;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        initUI () ;
    }

    private void initUI() {
        boolean isMyProfile = getIntent().getBooleanExtra("friends_profile",false);
        back_button = (ImageView)findViewById(R.id.back_button);
        done = (Button)findViewById(R.id.done);
        work = (LinearLayout)findViewById(R.id.work);
        friend_profile = (LinearLayout)findViewById(R.id.friend_profile);
        friends_profile_header = (LinearLayout)findViewById(R.id.friends_profile_header);
        back_button.setOnClickListener(this);
        work.setOnClickListener(this);
        done.setOnClickListener(this);
        if(isMyProfile) {
            friend_profile.setVisibility(View.GONE);
            friends_profile_header.setVisibility(View.GONE);
        } else {
            friend_profile.setVisibility(View.VISIBLE);
            friends_profile_header.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.back_button){
            super.onBackPressed();

        } else if(view.getId() == R.id.done){
            Intent i = new Intent(this,HomeActivity.class);
            startActivity(i);

        }else if(view.getId() == R.id.work){
            Intent i = new Intent(this,WorkExperience.class);
            startActivity(i);

        }
    }
}
