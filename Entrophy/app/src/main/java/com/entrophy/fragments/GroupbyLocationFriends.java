package com.entrophy.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.entrophy.GroupByLocationDetail;
import com.entrophy.R;
import com.entrophy.adapters.GroupByLocAdapter;
import com.entrophy.helper.ConstantStrings;
import com.entrophy.helper.Constants;
import com.entrophy.helper.CustomToast;
import com.entrophy.helper.DataBaseHelper;
import com.entrophy.helper.Loader;
import com.entrophy.helper.SharedPreference;
import com.entrophy.model.ConnectedContactsFriend;
import com.entrophy.model.MatchedContacts;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class GroupbyLocationFriends extends Fragment {

    private View view;
    private GridView friends_list;
    private List<MatchedContacts> friends = new ArrayList<>();
    private ConnectedContactsFriend connectedContactsFriend;
    private DataBaseHelper db;
    private int listType;
    private LinearLayout no_data;
    private String sealth_mode;

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
            db = new DataBaseHelper(getActivity());
            friends_list = (GridView) view.findViewById(R.id.friends_list);
            no_data = (LinearLayout) view.findViewById(R.id.no_data);

        } catch (Exception e){
            System.out.println("LayoutInflater getMeasuredWidth Exception "+e.toString());

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        sealth_mode = SharedPreference.getString(getActivity(),Constants.KEY_SEALTH_MODE);

        if(sealth_mode.equalsIgnoreCase("Y")) {
            friends = db.getHomeConnectedBiredEyeContacts();
            setDataConnect();
        } else {
            callConnectedFriendList();
        }

    }

    public void callConnectedFriendList() {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", SharedPreference.getInt(getActivity(), Constants.KEY_USER_ID));
            jsonObject.put("access_token", SharedPreference.getString(getActivity(),Constants.KEY_ACCESS_TOKEN));
            jsonObject.put("offset", 0);
            jsonObject.put("limit", 100000);

            Loader.show(getActivity());


            Constants.invokeAPIEx(getActivity(),Constants.LOCATIONFRIENDCOUNT,jsonObject.toString(),
                    new Handler(new Handler.Callback() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public boolean handleMessage(Message message) {
                    Loader.hide();
                    if(message != null && message.obj != null) {
                        String result = (String) message.obj;
                        try {
                            connectedContactsFriend = new Gson().fromJson(result, ConnectedContactsFriend.class);

                            if (connectedContactsFriend.getStatus().equalsIgnoreCase("success")) {
                                db.deleteFromConnectedContactsBirdEye();

                                setHomeConnectiondb();
                            } else {
                                if (connectedContactsFriend.getMessage() != null) {
                                    new CustomToast().Show_Toast(getActivity(), ConstantStrings.common_msg, R.color.red);
                                } else {
                                    new CustomToast().Show_Toast(getActivity(), ConstantStrings.common_msg, R.color.red);

                                }

                            }
                        } catch (Exception e){
                            System.out.println("LetsConnectPrint callAPI Exception "+e.toString());

                        }
                    } else {
                        new CustomToast().Show_Toast(getActivity(), ConstantStrings.common_msg,R.color.red);

                    }
                    return false;
                }
            }));
        } catch (Exception e){
            System.out.println("LetsConnectPrint callAPI Exception "+e.toString());
        }
    }
    public void setHomeConnectiondb() {
        System.out.println("setHomeConnectiondb "+connectedContactsFriend.getLocation_list().size());
        for(int i =0 ;i<connectedContactsFriend.getLocation_list().size();i++) {
            MatchedContacts matchedContacts = connectedContactsFriend.getLocation_list().get(i);
            db.putHomeContactsBirdEye(db,matchedContacts.getFriend_cnt(),matchedContacts.getCity(),
                    matchedContacts.getState(),matchedContacts.getCountry(),"on",matchedContacts.getLocation());
        }
        friends = connectedContactsFriend.getLocation_list();
        setDataConnect();

    }

    private void setDataConnect() {
        if(friends.size() != 0) {
            friends_list.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.GONE);
        } else {
            friends_list.setVisibility(View.GONE);
            no_data.setVisibility(View.VISIBLE);

        }
        if(sealth_mode.equalsIgnoreCase("N")) {

            friends_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if(Constants.isValidString(connectedContactsFriend.getLocation_list().get(i).getCity())) {
                        Intent intent = new Intent(getActivity(), GroupByLocationDetail.class);
                        intent.putExtra("city_name", connectedContactsFriend.getLocation_list().get(i).getCity());
                        startActivity(intent);
                    } else {
                        new CustomToast().Show_Toast(getActivity(), "City Cannot be empty ",R.color.red);

                    }
                }
            });
        }
        friends_list.setAdapter(new GroupByLocAdapter(getActivity(),friends));
    }

    public void setListType(int listType) {
        this.listType = listType;
    }
}
