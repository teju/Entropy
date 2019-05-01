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
import com.entrophy.model.MatchedContacts;
import com.entrophy.model.ValidateContactsFriend;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ListViewFriendsList extends Fragment {


    private View view;
    private List<MatchedContacts> contactsList  = new ArrayList<>();
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
    private int type = Constants.InviteContacts;
    private String sealth_mode;


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


    }

    @Override
    public void onResume() {
        super.onResume();
        if(type == Constants.HomeInviteFriendList) {
            sealth_mode = SharedPreference.getString(getActivity(),Constants.KEY_SEALTH_MODE);
            if(sealth_mode.equalsIgnoreCase("Y")) {
                setDataFriendList();
            } else {
                callGetFriendList();
            }
        } else {
            callGetFriendList();
        }

    }

    public  void setDataFriendList(){
        sealth_mode = SharedPreference.getString(getActivity(),Constants.KEY_SEALTH_MODE);
        if(sealth_mode.equalsIgnoreCase("Y") && type == Constants.HomeInviteFriendList) {
            contactsList = db.getHomeConnectedContacts();
        } else {
            contactsList = friendRequestResult.getFriend_request_list();
        }
        if (contactsList.size() != 0) {
            contacts_list_invite.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.GONE);

            mAdapter = new TabbedContactsAdapter(getActivity(),contactsList,
                    type, new TabbedContactsAdapter.RefreshAdapterListener() {
                @Override
                public void refreshAdapterListener(int listtype) {
                    type = listtype;
                    System.out.println("setDataFriendList2121 "+type);

                    if(type != Constants.HomeInviteFriendList) {
                        callGetFriendList();
                    }
                }
            });
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

            String url = "";
            if(type == Constants.HomeInviteFriendList) {
                url = Constants.FRIENDLIST;

            } else {
                url = Constants.FRIENDREQUESTLIST;
            }
            Constants.invokeAPIEx(getActivity(),url,jsonObject.toString(),new Handler(new Handler.Callback() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public boolean handleMessage(Message message) {
                    Loader.hide();
                    if(message != null && message.obj != null) {
                        String result = (String) message.obj;
                        friendRequestResult = new Gson().fromJson(result, FriendRequestResult.class);

                        if(friendRequestResult.getStatus().equalsIgnoreCase("success")) {
                            if(type == Constants.HomeInviteFriendList){
                                db.deleteFromHomeConnectedContacts();
                                setHomeConnectiondb();
                            } else {
                                setDataFriendList();
                            }
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
    public void setHomeConnectiondb() {
        for(int i =0 ;i<friendRequestResult.getFriend_request_list().size();i++) {
            MatchedContacts matchedContacts = friendRequestResult.getFriend_request_list().get(i);
            System.out.println("setHomeConnectiondb1233 "+matchedContacts.getName());
            db.putHomeContacts(db,matchedContacts.getFreind_id(),matchedContacts.getName(),
                    matchedContacts.getImage(),matchedContacts.getCity(),matchedContacts.getState(),
                    matchedContacts.getCountry(),matchedContacts.getGps());
        }
        setDataFriendList();

    }


    public void setType(int type) {
        this.type = type;
    }
}
