package com.entrophy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.entrophy.GroupByLocationDetail;
import com.entrophy.R;
import com.entrophy.model.DeoFriends;

import java.util.ArrayList;
import java.util.List;


public class GroupbyLocationFriends extends Fragment {

    private View view;
    private GridView friends_list;
    private List<DeoFriends> friends = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_groupby_location_friends, container, false);
        initUI();
        return view;
    }
    public void initUI () {
        try {
            friends_list = (GridView) view.findViewById(R.id.friends_list);
            friends_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getActivity(),GroupByLocationDetail.class);
                    startActivity(intent);
                }
            });
//            friends_list.setAdapter(new FrirendsListAdapter(getActivity(), friends, 2, new FrirendsListAdapter.ContactsAdapterListener() {
//            }));
        } catch (Exception e){
            System.out.println("LayoutInflater getMeasuredWidth Exception "+e.toString());

        }
    }

}
