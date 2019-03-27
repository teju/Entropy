package com.entrophy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.entrophy.adapters.ContactsAdapter;
import com.entrophy.helper.Constants;
import com.entrophy.helper.Helper;
import com.entrophy.model.ContactsDeo;

import java.util.ArrayList;
import java.util.List;

public class Contacts extends Activity implements View.OnClickListener{

    private RecyclerView contacts_list;
    private ContactsAdapter mAdapter;
    private List<ContactsDeo> contactsList = new ArrayList<>();
    private LinearLayout selected_contacts,info_view;
    private TextView skip,invite_b;
    private ImageView back_button;
    private Button select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Helper.hideKeyboard(this);
    }

    private void init() {
        try {

            skip = (TextView) findViewById(R.id.skip);
            select = (Button) findViewById(R.id.select);
            invite_b = (TextView) findViewById(R.id.invite_b);
            back_button = (ImageView) findViewById(R.id.back_button);
            info_view = (LinearLayout) findViewById(R.id.info_view);
            selected_contacts = (LinearLayout) findViewById(R.id.selected_contacts);
            back_button.setOnClickListener(this);
            select.setOnClickListener(this);
            skip.setOnClickListener(this);
            invite_b.setOnClickListener(this);
            initData();
            selectedFriends();
            contacts_list = (RecyclerView) findViewById(R.id.contacts_list);
            mAdapter = new ContactsAdapter(Contacts.this,contactsList, Constants.GeneralContactsRequest,new ContactsAdapter.ContactsAdapterListener() {
                @Override
                public void checkBoXChecked(int position,boolean b) {
                    System.out.println("Contacts1234 checkBoXChecked " + position);

                    if(b) {
                        info_view.setVisibility(View.GONE);
                        skip.setVisibility(View.GONE);
                        back_button.setVisibility(View.VISIBLE);
                        invite_b.setText("Connect");
                        invite_b.setTextColor(getResources().getColor(R.color.green));
                        selected_contacts.setVisibility(View.VISIBLE);
                    } else {
                        selected_contacts.setVisibility(View.GONE);
                        invite_b.setTextColor(getResources().getColor(R.color.dark_gray));
                        info_view.setVisibility(View.VISIBLE);
                        skip.setVisibility(View.VISIBLE);
                        back_button.setVisibility(View.GONE);
                        invite_b.setText("Invite");
                    }
                }
            });


            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            contacts_list.setLayoutManager(mLayoutManager);
            contacts_list.setItemAnimator(new DefaultItemAnimator());
            contacts_list.setAdapter(mAdapter);
            //searchView.setQueryHint("Search");

        }
         catch (Exception e){
            System.out.println("Contacts1234 Exception "+e.toString());
        }
    }

    public void selectedFriends() {
        for (int i=0;i<7;i++) {
            View layout2 = LayoutInflater.from(this).inflate(R.layout.selected_contacts, selected_contacts, false);
            selected_contacts.addView(layout2);
        }

    }

    public void initData() {
        for (int i =1;i<10;i++) {
            ContactsDeo contactsDeo = new ContactsDeo();
            contactsDeo.setName("Friend "+i);
            contactsList.add(contactsDeo);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.back_button) {
            selected_contacts.setVisibility(View.GONE);
            info_view.setVisibility(View.VISIBLE);
            skip.setVisibility(View.VISIBLE);
            back_button.setVisibility(View.GONE);
            invite_b.setText("Invite");
        } else if(view.getId() == R.id.skip) {
            Intent intent = new Intent(this,ConnectionSet.class);
            intent.putExtra("skip_button",true);
            startActivity(intent);
        }else if(view.getId() == R.id.select) {
            info_view.setVisibility(View.GONE);
            skip.setVisibility(View.GONE);
            back_button.setVisibility(View.VISIBLE);
            invite_b.setText("Connect");
            invite_b.setTextColor(getResources().getColor(R.color.green));
            selected_contacts.setVisibility(View.VISIBLE);
            mAdapter.add(true);
        } else if(view.getId() == R.id.invite_b) {
            Intent intent = new Intent(this,ConnectionSet.class);
            intent.putExtra("skip_button",false);
            startActivity(intent);
        }
    }
}
