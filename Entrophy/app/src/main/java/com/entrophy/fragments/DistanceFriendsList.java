package com.entrophy.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.entrophy.R;
import com.entrophy.adapters.ContactsAdapter;
import com.entrophy.helper.Constants;
import com.entrophy.model.ContactsDeo;

import java.util.ArrayList;
import java.util.List;


public class DistanceFriendsList extends Fragment {


    private View view;
    private List<ContactsDeo> contactsList  = new ArrayList<>();
    private RecyclerView contacts_list;
    private ContactsAdapter mAdapter;
    private LinearLayout top_view;

    public void setTabbedContacts(boolean tabbedContacts) {
        isTabbedContacts = tabbedContacts;
    }

    private boolean isTabbedContacts = false;

    public void setListType(int listType) {
        this.listType = listType;
    }

    int listType = Constants.ListContactsRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_distance_friends_list, container, false);
        initUI();
        return view;
    }

    public  void initUI() {
        initData();
        contacts_list = (RecyclerView)view.findViewById(R.id.contacts_list);
        top_view = (LinearLayout)view.findViewById(R.id.top_view);
        mAdapter = new ContactsAdapter(getActivity(),contactsList, listType, new ContactsAdapter.ContactsAdapterListener() {
            @Override
            public void checkBoXChecked(int position, boolean b) {

            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        contacts_list.setLayoutManager(mLayoutManager);
        contacts_list.setItemAnimator(new DefaultItemAnimator());
        contacts_list.setAdapter(mAdapter);

        if(isTabbedContacts){
            top_view.setVisibility(View.VISIBLE);

        } else {
            top_view.setVisibility(View.GONE);
        }
    }
    public void initData() {
        for (int i =1;i<10;i++) {
            ContactsDeo contactsDeo = new ContactsDeo();
            contactsDeo.setName("Friend "+i);
            contactsList.add(contactsDeo);
        }
    }


}
