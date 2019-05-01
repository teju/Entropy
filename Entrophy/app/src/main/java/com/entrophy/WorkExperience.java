package com.entrophy;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class WorkExperience extends Activity implements View.OnClickListener {

    private LinearLayout work_exp;
    private LayoutInflater mInflater;
    private Button done;
    private ImageView back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_experience);
        init();
    }

    private void init() {
        work_exp = (LinearLayout)findViewById(R.id.work_exp);
        done = (Button)findViewById(R.id.done);
        back_button = (ImageView)findViewById(R.id.back_button);
        back_button.setOnClickListener(this);
        done.setOnClickListener(this);
        mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        List<String> profileInfo = getIntent().getStringArrayListExtra("response");
        System.out.println("profileInfo12121 "+profileInfo.size());
//
//
        for (int i =0;i < profileInfo.size();i++ ){
            final View v = mInflater.inflate(R.layout.work_item, null, false);
             TextView work_exp_text = (TextView)v.findViewById(R.id.work_exp_text);
            work_exp_text.setText(profileInfo.get(i));
            work_exp.addView(v);
        }
    }

    @Override
    public void onClick(View view) {
        super.onBackPressed();
    }
}
