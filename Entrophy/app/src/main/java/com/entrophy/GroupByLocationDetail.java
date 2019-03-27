package com.entrophy;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.entrophy.adapters.GroupByLocAdapter;
import com.entrophy.model.ContactsDeo;

import java.util.ArrayList;
import java.util.List;

public class GroupByLocationDetail extends Activity {
    private List<ContactsDeo> contactsList  = new ArrayList<>();
    private RecyclerView contacts_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_by_location_detail);
        initUI();
    }
    public  void initUI() {
        initData();
        contacts_list = (RecyclerView)findViewById(R.id.contacts_list);
        GroupByLocAdapter mAdapter = new GroupByLocAdapter(this,contactsList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        contacts_list.setLayoutManager(mLayoutManager);
        contacts_list.setItemAnimator(new DefaultItemAnimator());
        contacts_list.setAdapter(mAdapter);
    }

    public void initData() {
        for (int i =1;i<10;i++) {
            ContactsDeo contactsDeo = new ContactsDeo();
            contactsDeo.setName("Friend "+i);
            contactsList.add(contactsDeo);
        }
    }


}
