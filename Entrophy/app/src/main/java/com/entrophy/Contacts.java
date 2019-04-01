package com.entrophy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.entrophy.adapters.ContactsAdapter;
import com.entrophy.helper.Constants;
import com.entrophy.helper.DataBaseHelper;
import com.entrophy.helper.Helper;
import com.entrophy.helper.Loader;
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
    private List<ContactsDeo> addedContacts = new ArrayList<>();
    private DataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        db = new DataBaseHelper(this);

        GetContacts getContacts = new GetContacts(this);
        getContacts.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Helper.hideKeyboard(this);
    }

    private void init() {
        try {
            skip = (TextView) findViewById(R.id.skip);
            TextView contacts_coutn = (TextView) findViewById(R.id.contacts_coutn);
            contacts_coutn.setText(contactsList.size()+" Contacts");
            select = (Button) findViewById(R.id.select);
            invite_b = (TextView) findViewById(R.id.invite_b);
            back_button = (ImageView) findViewById(R.id.back_button);
            info_view = (LinearLayout) findViewById(R.id.info_view);
            selected_contacts = (LinearLayout) findViewById(R.id.selected_contacts);
            back_button.setOnClickListener(this);
            select.setOnClickListener(this);
            skip.setOnClickListener(this);
            invite_b.setOnClickListener(this);
            selectedFriends();
            if(contactsList.size() != 0) {
                contacts_list = (RecyclerView) findViewById(R.id.contacts_list);
                mAdapter = new ContactsAdapter(Contacts.this, contactsList, Constants.GeneralContactsRequest, new ContactsAdapter.ContactsAdapterListener() {
                    @Override
                    public void checkBoXChecked(int position, boolean added) {
                        addedContacts(position,added);
                        if (addedContacts.size() > 0) {
                            System.out.println("onCheckedChanged Contacts1234 checkBoXChecked " + addedContacts.size());

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
            }
            //searchView.setQueryHint("Search");

        }
         catch (Exception e){
            System.out.println("Contacts1234 Exception "+e.toString());
        }
    }

    public void addedContacts(int position, boolean added) {
        addedContacts.clear();
        contactsList.clear();
        addedContacts = db.getAddedContacts();
        contactsList = db.getContacts();
        if(added) {
            db.putAddedContacts(db,contactsList.get(position).getName(),
                    contactsList.get(position).getPhoneNumber(),"true",contactsList.get(position).getImage(),
                    contactsList.get(position).getContact_id());
        } else if(addedContacts.size()>0 && addedContacts.size() > position) {
           db.deleteFromAddedContacts(addedContacts.get(position).getContact_id());
        }
        selectedFriends();
    }

    @Nullable
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    public void selectedFriends() {
        selected_contacts.removeAllViews();
        addedContacts.clear();
        addedContacts = db.getAddedContacts();
        for (int i=0;i<addedContacts.size();i++) {
            View layout2 = LayoutInflater.from(this).inflate(R.layout.selected_contacts, selected_contacts, false);
            ImageView remove_contact = (ImageView)layout2.findViewById(R.id.remove_contact);
            remove_contact.setTag(i);
            remove_contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int rmi = (Integer) view.getTag();
                   // mAdapter.add(false,rmi);
                    db.deleteFromAddedContacts(addedContacts.get(rmi).getContact_id());
                    System.out.println("onCheckedChanged  boolean " + addedContacts.get(rmi).getNewContact_id());

                    db.UpdateContacts("false", addedContacts.get(rmi).getNewContact_id());

                    selectedFriends();
                    mAdapter.notifyadd();
                }
            });
            selected_contacts.addView(layout2);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.back_button) {
            db.UpdateContacts("false","");

            selected_contacts.setVisibility(View.GONE);
            info_view.setVisibility(View.VISIBLE);
            skip.setVisibility(View.VISIBLE);
            back_button.setVisibility(View.GONE);
            invite_b.setText("Invite");
            db.deleteFromAddedContacts("");

            mAdapter.notifyadd();
            selectedFriends();
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
            db.UpdateContacts("true","");
            selectAll();
            mAdapter.notifyadd();

            selectedFriends();
        } else if(view.getId() == R.id.invite_b) {
            Intent intent = new Intent(this,ConnectionSet.class);
            intent.putExtra("skip_button",false);
            startActivity(intent);
        }
    }

    public void selectAll() {
        contactsList.clear();
        contactsList = db.getContacts();

        for (int i =0;i<contactsList.size();i++) {
            db.putAddedContacts(db,contactsList.get(i).getName(),contactsList.get(i).getPhoneNumber(),"true",contactsList.get(i).getImage(),
                    contactsList.get(i).getContact_id());
        }
    }

    public class GetContacts extends AsyncTask{

        private final Context context;

        public GetContacts(Context context) {
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Loader.show(context);
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Loader.hide();
            init();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            db.deleteFromContacts();
            db.deleteFromAddedContacts("");
            if(Constants.checkContactsPermission(context)) {
                Constants.getContact(context);
                contactsList = db.getContacts();
            }

            return null;
        }
    }
}
