package com.entrophy;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.entrophy.adapters.BirdEyeContactsAdapter;
import com.entrophy.helper.ConstantStrings;
import com.entrophy.helper.Constants;
import com.entrophy.helper.CustomToast;
import com.entrophy.helper.Loader;
import com.entrophy.helper.SharedPreference;
import com.entrophy.model.ConnectedContactsFriend;
import com.entrophy.model.MatchedContacts;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GroupByLocationDetail extends Activity {
    private List<MatchedContacts> contactsList  = new ArrayList<>();
    private RecyclerView contacts_list;
    private ConnectedContactsFriend connectedContactsFriend;
    private TextView title_val;
    private ImageView back_button;
    private Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_by_location_detail);
        initUI();
    }
    public  void initUI() {
        contacts_list = (RecyclerView)findViewById(R.id.contacts_list);
        done = (Button)findViewById(R.id.done);
        title_val = (TextView)findViewById(R.id.title_val);
        back_button = (ImageView)findViewById(R.id.back_button);
        title_val.setText(getIntent().getStringExtra("city_name"));
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GroupByLocationDetail.super.onBackPressed();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GroupByLocationDetail.super.onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        callConnectedFriendList();
    }

    public void callConnectedFriendList() {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", SharedPreference.getInt(GroupByLocationDetail.this, Constants.KEY_USER_ID));
            jsonObject.put("access_token", SharedPreference.getString(GroupByLocationDetail.this,Constants.KEY_ACCESS_TOKEN));
            jsonObject.put("offset", 0);
            jsonObject.put("limit", 100000);
            jsonObject.put("city_name", getIntent().getStringExtra("city_name"));

            Loader.show(GroupByLocationDetail.this);


            Constants.invokeAPIEx(GroupByLocationDetail.this,Constants.LOCATIONFRIENDLIST,jsonObject.toString(),
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
                                        setDataConnect();
                                    } else {
                                        if (connectedContactsFriend.getMessage() != null) {
                                            new CustomToast().Show_Toast(GroupByLocationDetail.this, ConstantStrings.common_msg, R.color.red);
                                        } else {
                                            new CustomToast().Show_Toast(GroupByLocationDetail.this, ConstantStrings.common_msg, R.color.red);

                                        }

                                    }
                                } catch (Exception e){
                                    System.out.println("LetsConnectPrint callAPI Exception "+e.toString());

                                }
                            } else {
                                new CustomToast().Show_Toast(GroupByLocationDetail.this, ConstantStrings.common_msg,R.color.red);

                            }
                            return false;
                        }
                    }));
        } catch (Exception e){
            System.out.println("LetsConnectPrint callAPI Exception "+e.toString());
        }
    }

    private void setDataConnect() {

        contactsList = connectedContactsFriend.getFriend_request_list();
        System.out.println("BirdEyeContactsAdapter setDataConnect "+contactsList.size());

        if(contactsList.size() != 0) {
            contacts_list.setVisibility(View.VISIBLE);
            //no_data.setVisibility(View.GONE);
        } else {
            contacts_list.setVisibility(View.GONE);
            //no_data.setVisibility(View.VISIBLE);

        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(GroupByLocationDetail.this);
        contacts_list.setLayoutManager(mLayoutManager);
        contacts_list.setItemAnimator(new DefaultItemAnimator());
        contacts_list.setAdapter(new BirdEyeContactsAdapter(GroupByLocationDetail.this,contactsList));
    }

}
