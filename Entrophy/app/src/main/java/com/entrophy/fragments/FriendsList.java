package com.entrophy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.entrophy.MyProfile;
import com.entrophy.R;
import com.entrophy.adapters.FrirendsListAdapter;
import com.entrophy.model.DeoFriends;

import java.util.ArrayList;
import java.util.List;


public class FriendsList extends Fragment {


    private View view;
    private GridView friends_list;
    private List<DeoFriends> friends = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_friends_list, container, false);
        initUI();
        return view;
    }

    public void initUI () {
        friends_list = (GridView)view.findViewById(R.id.friends_list);
        friends_list.setAdapter(new FrirendsListAdapter(getActivity(),friends,0));
        friends_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(),MyProfile.class);
                intent.putExtra("friends_profile",false);
                startActivity(intent);
            }
        });
    }


}
