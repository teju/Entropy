package com.entrophy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.entrophy.adapters.CustomAdapter;

public class Notifications extends Activity {

    private CustomAdapter mAdapter;
    private ListView notifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        initUI();
    }

    public void initUI(){
        notifications = (ListView)findViewById(R.id.notifications);
        mAdapter = new CustomAdapter(this);
        for (int i = 1; i < 30; i++) {
            if (i % 10 == 0 || i == 1) {
                mAdapter.addSectionHeaderItem("Section #" + i);
            } else {
                mAdapter.addItem("Row Item #" + i);

            }
        }
        notifications.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Notifications.this,TabbedContacts.class);
                startActivity(intent);
            }
        });
        notifications.setAdapter(mAdapter);
    }
}
