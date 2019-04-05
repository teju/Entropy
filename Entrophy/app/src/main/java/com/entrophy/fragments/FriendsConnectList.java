package com.entrophy.fragments;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.entrophy.R;
import com.entrophy.adapters.FrirendsListAdapter;
import com.entrophy.adapters.TabbedContactsAdapter;
import com.entrophy.helper.ConstantStrings;
import com.entrophy.helper.Constants;
import com.entrophy.helper.CustomToast;
import com.entrophy.helper.DataBaseHelper;
import com.entrophy.helper.Loader;
import com.entrophy.helper.SharedPreference;
import com.entrophy.model.ContactsDeo;
import com.entrophy.model.MatchedContacts;
import com.entrophy.model.ValidateContactsFriend;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FriendsConnectList extends Fragment {


    private View view;
    private List<MatchedContacts> contactsList  = new ArrayList<>();
    private List<ContactsDeo> allcontactsList  = new ArrayList<>();
    private RecyclerView contacts_list_invite;
    private TabbedContactsAdapter mAdapter;
    private LinearLayout top_view;
    private LinearLayout no_data;
    private TextView contacts_coutn;
    private DataBaseHelper db;
    private List<ContactsDeo> addedContacts = new ArrayList<>();
    private JSONArray jsonArray;
    private GridView contacts_list_connect;
    private LinearLayout search;
    private FrirendsListAdapter mConnectiom;
    private InviteFriendsList.InviteFriendsListListener inviteFriendsListListener;
    private Button selectall;
    private ValidateContactsFriend validateContactsFriend;

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
        db = new DataBaseHelper(getActivity());

        contacts_list_invite = (RecyclerView)view.findViewById(R.id.contacts_list);
        contacts_list_connect = (GridView) view.findViewById(R.id.contacts_list_connect);
        contacts_list_connect.setVisibility(View.GONE);
        contacts_list_invite.setVisibility(View.GONE);

        top_view = (LinearLayout)view.findViewById(R.id.top_view);
        search = (LinearLayout)view.findViewById(R.id.search);
        no_data = (LinearLayout)view.findViewById(R.id.no_data);
        contacts_coutn = (TextView)view.findViewById(R.id.contacts_coutn);

        allContactsnJSon();
        selectall = (Button)view.findViewById(R.id.select);
        selectall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectall.getText().toString().equalsIgnoreCase("Select All")) {
                    db.UpdateConnectedContacts("true","");
                    selectall.setText("UnSelect All");
                    selectAll();
                } else {
                    db.UpdateConnectedContacts("false","");
                    selectall.setText("Select All");
                    db.deleteFromAddedContacts("");
                    mConnectiom.notifyadd();
                    inviteFriendsListListener.SelectAll(db.getAddedContacts(),Constants.InviteContacts);

                }
                addedContacts = db.getAddedContacts();
                if(addedContacts.size() != 0) {
                    contacts_coutn.setText(addedContacts.size() + " Contacts");
                    contacts_coutn.setVisibility(View.VISIBLE);
                } else {
                    contacts_coutn.setVisibility(View.GONE);

                }
            }
        });

    }

    public void addedContacts(int position, boolean added) {
        addedContacts.clear();
        addedContacts = db.getAddedContacts();
        List<MatchedContacts> contactsDeos = db.getConnectedContacts();
        if(added) {
            db.putAddedContacts(db,contactsDeos.get(position).getName(),
                    contactsDeos.get(position).getMobile_no(),"true",contactsDeos.get(position).getImage(),
                    contactsDeos.get(position).getContact_id());
        } else if(addedContacts.size() > 0 ) {
            db.deleteFromAddedContacts(contactsDeos.get(position).getContact_id());
        }
        addedContacts = db.getAddedContacts();
        if(addedContacts.size() != 0) {
            contacts_coutn.setText(addedContacts.size() + " Contacts");
            contacts_coutn.setVisibility(View.VISIBLE);
        } else {
            contacts_coutn.setVisibility(View.GONE);

        }
        System.out.println("addedContacts "+addedContacts.size()+ " added "+added+" "+position);
        inviteFriendsListListener.SelectAll(db.getAddedContacts(),Constants.ConnectionsContacts);

    }



    public void setDataConnect() {

        top_view.setVisibility(View.VISIBLE);
        search.setVisibility(View.GONE);
        getListFRomJSon();
        contactsList = db.getConnectedContacts();
        contacts_list_invite.setVisibility(View.GONE);
        contacts_list_connect.setVisibility(View.VISIBLE);
        if(contactsList.size() != 0) {
            no_data.setVisibility(View.GONE);
        }
        mConnectiom = new FrirendsListAdapter(getActivity(), contactsList, Constants.ConnectionsContacts, new FrirendsListAdapter.ContactsAdapterListener() {
            @Override
            public void checkBoXChecked(int position, boolean added) {
                addedContacts(position, added);
                if (addedContacts.size() > 0) {
                    contacts_coutn.setText(addedContacts.size() + " Contacts");
                    contacts_coutn.setVisibility(View.VISIBLE);
                } else {
                    contacts_coutn.setVisibility(View.GONE);
                }
            }
        });

        contacts_list_connect.setAdapter(mConnectiom);

    }
    public void allContactsnJSon() {
        jsonArray = new JSONArray();
        allcontactsList.clear();
        allcontactsList = db.getAllContacts();
        for(int i = 0;i<allcontactsList.size();i++) {
            String phone_number = allcontactsList.get(i).getPhoneNumber().trim().replaceAll("-","").
                    replaceAll("\\s","").replaceAll("\"[-+.^:,]\",\"\"","").replaceAll("91","");
            phone_number = phone_number.replaceAll("\\D+","");
            jsonArray.put(Long.parseLong(phone_number));
        }
        System.out.println("DistanceFriendsList123allContactsnJSon listType "+allcontactsList.size());

        callConnectGetFriendList();
    }

    public void getListFRomJSon() {
        try {
            System.out.println("LetsConnectPrint getListFRomJSon resp "+validateContactsFriend.getMatched_contacts());
            db.deleteFromConnectedContacts();

            //  String jsonArray = resp.getString("matched_contacts");
            List<MatchedContacts> matchedContactses = validateContactsFriend.getMatched_contacts();
            if(matchedContactses.size() > 0) {
                for (int i = 0;i<matchedContactses.size();i++) {
                    db.putConnectionContacts(db,matchedContactses.get(i).getMobile_no(),
                            matchedContactses.get(i).getMobile_no(),"false","",matchedContactses.get(i).getUser_id());
                }
            }

        } catch (Exception e){
            System.out.println("LetsConnectPrint getListFRomJSon Exception "+e.toString());
        }
    }

    public void selectAll() {

        contactsList.clear();
        contactsList = db.getConnectedContacts();
        db.deleteFromAddedContacts("");
        for (int i =0;i<contactsList.size();i++) {
            db.putAddedContacts(db,contactsList.get(i).getName(),contactsList.get(i).getMobile_no(),"true",contactsList.get(i).getImage(),
                    contactsList.get(i).getContact_id());
        }
        mConnectiom.notifyadd();
        inviteFriendsListListener.SelectAll(db.getAddedContacts(),Constants.ConnectionsContacts);

    }



    public void setOnEventListener(InviteFriendsList.InviteFriendsListListener listener) {
        inviteFriendsListListener = listener;
    }


    public void callConnectGetFriendList() {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", SharedPreference.getInt(getActivity(),Constants.KEY_USER_ID));
            jsonObject.put("access_token", SharedPreference.getString(getActivity(),Constants.KEY_ACCESS_TOKEN));
            jsonObject.put("contact_numbers",jsonArray);
            Loader.show(getActivity());

            Constants.invokeAPIEx(getActivity(),Constants.VALIDATECONTACTS,jsonObject.toString(),new Handler(new Handler.Callback() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public boolean handleMessage(Message message) {
                    Loader.hide();
                    if(message != null && message.obj != null) {
                        String result = (String) message.obj;
                        try {
                            validateContactsFriend = new Gson().fromJson(result, ValidateContactsFriend.class);

                            if (validateContactsFriend.getStatus().equalsIgnoreCase("success")) {
                                setDataConnect();
                            } else {
                                if (validateContactsFriend.getMessage() != null) {
                                    new CustomToast().Show_Toast(getActivity(), ConstantStrings.common_msg, R.color.red);
                                } else {
                                    new CustomToast().Show_Toast(getActivity(), ConstantStrings.common_msg, R.color.red);

                                }

                            }
                        } catch (Exception e){

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
