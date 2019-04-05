package com.entrophy.fragments;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.entrophy.R;
import com.entrophy.adapters.TabbedContactsAdapter;
import com.entrophy.helper.ConstantStrings;
import com.entrophy.helper.Constants;
import com.entrophy.helper.CustomToast;
import com.entrophy.helper.DataBaseHelper;
import com.entrophy.helper.Loader;
import com.entrophy.helper.SharedPreference;
import com.entrophy.model.ContactsDeo;
import com.entrophy.model.FriendRequestResult;
import com.entrophy.model.ValidateContactsFriend;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DistanceFriendsList extends Fragment {


    private View view;
    private List<ContactsDeo> contactsList  = new ArrayList<>();
    private RecyclerView contacts_list_invite;
    private TabbedContactsAdapter mAdapter;
    private LinearLayout top_view;
    private FriendRequestResult friendRequestResult;
    private LinearLayout no_data;
    private TextView contacts_coutn;
    private DataBaseHelper db;
    private List<ContactsDeo> addedContacts = new ArrayList<>();
    private JSONArray jsonArray;
    private ValidateContactsFriend validateContactsFriend;
    private GridView contacts_list_connect;
    private LinearLayout search;

    public void setTabbedContacts(boolean tabbedContacts) {
        isTabbedContacts = tabbedContacts;
    }

    private boolean isTabbedContacts = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_distance_friends_list, container, false);
        initUI();
        return view;
    }

    public  void initUI() {
        db = new DataBaseHelper(getActivity());

        contacts_list_invite = (RecyclerView)view.findViewById(R.id.contacts_list);
        contacts_list_connect = (GridView) view.findViewById(R.id.contacts_list_connect);
        contacts_list_connect.setVisibility(View.GONE);
        contacts_list_invite.setVisibility(View.GONE);

        top_view = (LinearLayout)view.findViewById(R.id.top_view);
        search = (LinearLayout)view.findViewById(R.id.search);
        no_data = (LinearLayout)view.findViewById(R.id.no_data);
        contacts_coutn = (TextView)view.findViewById(R.id.contacts_coutn);

        top_view.setVisibility(View.GONE);

        callGetFriendList();

    }
    public  void setDataFriendList(){
        if (friendRequestResult.getFriend_request_list().size() != 0) {
            contacts_list_invite.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.GONE);
            mAdapter = new TabbedContactsAdapter(getActivity(),
                    friendRequestResult.getFriend_request_list(),
                    Constants.TabbedContactsRequest);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            contacts_list_invite.setLayoutManager(mLayoutManager);
            contacts_list_invite.setItemAnimator(new DefaultItemAnimator());
            contacts_list_invite.setAdapter(mAdapter);
        } else {
            contacts_list_invite.setVisibility(View.GONE);
            no_data.setVisibility(View.VISIBLE);

        }
    }





    public void callGetFriendList() {
        Loader.show(getActivity());
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", SharedPreference.getInt(getActivity(),Constants.KEY_USER_ID));
            jsonObject.put("access_token", SharedPreference.getString(getActivity(),Constants.KEY_ACCESS_TOKEN));
            jsonObject.put("offset","0");
            jsonObject.put("limit","1000");
            Loader.show(getActivity());

            Constants.invokeAPIEx(getActivity(),Constants.FRIENDREQUESTLIST,jsonObject.toString(),new Handler(new Handler.Callback() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public boolean handleMessage(Message message) {
                    Loader.hide();
                    if(message != null && message.obj != null) {
                        String result = (String) message.obj;
                        friendRequestResult = new Gson().fromJson(result, FriendRequestResult.class);

                        if(friendRequestResult.getStatus().equalsIgnoreCase("success")) {
                            setDataFriendList();

                        } else {
                            if(friendRequestResult.getMessage() != null) {
                                new CustomToast().Show_Toast(getActivity(), ConstantStrings.common_msg, R.color.red);
                            }  else {
                                new CustomToast().Show_Toast(getActivity(), ConstantStrings.common_msg,R.color.red);

                            }
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

}
