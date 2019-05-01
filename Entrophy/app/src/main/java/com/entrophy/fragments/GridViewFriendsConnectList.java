package com.entrophy.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.entrophy.MyProfile;
import com.entrophy.R;
import com.entrophy.adapters.GridFrirendsListAdapter;
import com.entrophy.adapters.TabbedContactsAdapter;
import com.entrophy.helper.ConstantStrings;
import com.entrophy.helper.Constants;
import com.entrophy.helper.CustomToast;
import com.entrophy.helper.DataBaseHelper;
import com.entrophy.helper.Loader;
import com.entrophy.helper.SharedPreference;
import com.entrophy.model.ConnectedContactsFriend;
import com.entrophy.model.ContactsDeo;
import com.entrophy.model.MatchedContacts;
import com.entrophy.model.ValidateContactsFriend;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class GridViewFriendsConnectList extends Fragment {


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
    private GridFrirendsListAdapter mConnectiom;
    private InviteFriendsList.InviteFriendsListListener inviteFriendsListListener;
    private Button selectall;
    private ValidateContactsFriend validateContactsFriend;
    private TextView mtext;

    int listType = Constants.ConnectionsContacts;
    private ConnectedContactsFriend connectedContactsFriend;
    private String sealth_mode = "";

    public void setListType(int listType) {
        this.listType = listType;
    }


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
        contacts_coutn.setVisibility(View.VISIBLE);

        mtext = (TextView)view.findViewById(R.id.mtext);

        if(listType == Constants.ConnectionsContacts) {

            GetContacts getContacts = new GetContacts(getActivity());
            getContacts.execute();

        } else {
            sealth_mode = SharedPreference.getString(getActivity(),Constants.KEY_SEALTH_MODE);
            if(sealth_mode.equalsIgnoreCase("Y")) {
                setDataConnect();
            } else {
                callConnectedFriendList();
            }
        }
        selectall = (Button)view.findViewById(R.id.select);
        selectall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectall.getText().toString().equalsIgnoreCase("Select All")) {
                    db.UpdateConnectedContacts("true","");
                    selectall.setText("DeSelect All");
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
                    contacts_coutn.setText(addedContacts.size() + " Contacts Selected");
                    contacts_coutn.setVisibility(View.VISIBLE);
                } else {
                    contactsList = db.getConnectedContacts();
                    contacts_coutn.setText(contactsList.size()+" people know you are using Knowinu");
                    contacts_coutn.setVisibility(View.VISIBLE);

                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        mtext.setText("Select whom you want to meet in person");
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
            contacts_coutn.setText(addedContacts.size() + " Contacts Selected");
            contacts_coutn.setVisibility(View.VISIBLE);
        } else {
            contacts_coutn.setText(validateContactsFriend.getMatched_contacts().size()+" people know you are using Knowinu");
            contacts_coutn.setVisibility(View.VISIBLE);
        }
        System.out.println("addedContacts "+addedContacts.size()+ " added "+added+" "+position);
        inviteFriendsListListener.SelectAll(db.getAddedContacts(),Constants.ConnectionsContacts);

    }



    public void setDataConnect() {
        if(listType == Constants.ConnectionsContacts) {
            top_view.setVisibility(View.VISIBLE);
        } else {
            top_view.setVisibility(View.GONE);
        }
        search.setVisibility(View.GONE);
        if(listType == Constants.ConnectionsContacts) {
            getListFRomJSon();
            contactsList = db.getConnectedContacts();
        } else {
            if(sealth_mode.equalsIgnoreCase("Y")) {
                contactsList = db.getHomeConnectedContacts();
            } else {
                contactsList = connectedContactsFriend.getFriend_request_list();
            }
        }
        contacts_list_invite.setVisibility(View.GONE);
        contacts_list_connect.setVisibility(View.VISIBLE);
        if(contactsList.size() != 0) {
            no_data.setVisibility(View.GONE);
        }
        contacts_coutn.setText(contactsList.size()+" people know you are using Knowinu");
        mConnectiom = new GridFrirendsListAdapter(getActivity(), contactsList, listType, new GridFrirendsListAdapter.ContactsAdapterListener() {
            @Override
            public void checkBoXChecked(int position, boolean added) {
                addedContacts(position, added);
                if (addedContacts.size() > 0) {
                    contacts_coutn.setText(addedContacts.size() + " Contacts Selected");
                    contacts_coutn.setVisibility(View.VISIBLE);
                } else {
                    contacts_coutn.setText(contactsList.size()+" people know you are using Knowinu");
                    contacts_coutn.setVisibility(View.VISIBLE);
                }

            }
        });
        String sealth_mode = SharedPreference.getString(getActivity(),Constants.KEY_SEALTH_MODE);
        System.out.println("KEY_SEALTH_MODE "+sealth_mode);
        if(listType == Constants.HomeConnectionsContacts && sealth_mode.equalsIgnoreCase("N")) {
            contacts_list_connect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if(contactsList.get(i).getGps().equalsIgnoreCase("on")) {
                        Intent intent = new Intent(getActivity(), MyProfile.class);
                        intent.putExtra("friend_id", contactsList.get(i).getFreind_id());
                        intent.putExtra("friends_profile", false);
                        startActivity(intent);
                    }
                }
            });
        }
        contacts_list_connect.setAdapter(mConnectiom);

    }
    public void allContactsnJSon() {
        jsonArray = new JSONArray();
        allcontactsList = db.getAllContacts("");
        for(int i = 0;i<allcontactsList.size();i++) {
            String phone_number = allcontactsList.get(i).getPhoneNumber().trim().replaceAll("-","").
                    replaceAll("\\s","").replaceAll("\"[-+.^:,]\",\"\"","");
            if( phone_number.startsWith("91")) {
                phone_number = phone_number.replaceFirst("91","");
            }
            phone_number = phone_number.replaceAll("\\D+","");

            jsonArray.put(Long.parseLong(phone_number));
        }
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
                    db.putConnectionContacts(db,matchedContactses.get(i).getName(),
                            matchedContactses.get(i).getMobile_no(),"false",
                            matchedContactses.get(i).getImage(),matchedContactses.get(i).getUser_id());
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


    public void callConnectedFriendList() {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", SharedPreference.getInt(getActivity(),Constants.KEY_USER_ID));
            jsonObject.put("access_token", SharedPreference.getString(getActivity(),Constants.KEY_ACCESS_TOKEN));
            jsonObject.put("offset", 0);
            jsonObject.put("limit", 100000);

            Loader.show(getActivity());


            Constants.invokeAPIEx(getActivity(),Constants.FRIENDLIST,jsonObject.toString(),new Handler(new Handler.Callback() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public boolean handleMessage(Message message) {
                    Loader.hide();
                    if(message != null && message.obj != null) {
                        String result = (String) message.obj;
                        try {
                            connectedContactsFriend = new Gson().fromJson(result, ConnectedContactsFriend.class);

                            if (connectedContactsFriend.getStatus().equalsIgnoreCase("success")) {
                                if(listType == Constants.HomeConnectionsContacts){
                                    db.deleteFromHomeConnectedContacts();
                                }
                                setHomeConnectiondb();

                            } else {
                                if (connectedContactsFriend.getMessage() != null) {
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

    public class GetContacts extends AsyncTask {

        private final Context context;

        public GetContacts(Context context) {
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Loader.show(context,"Contact Syncing");
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Loader.hide();
            allContactsnJSon();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            db.deleteFromAddedContacts("");

            if(Constants.checkContactsPermission(context)) {
                db.deleteFromAllContacts();
                Constants.getContact(context);
                allcontactsList = db.getAllContacts("");

            }

            return null;
        }
    }

    public void setHomeConnectiondb() {
        for(int i =0 ;i<connectedContactsFriend.getFriend_request_list().size();i++) {
            MatchedContacts matchedContacts = connectedContactsFriend.getFriend_request_list().get(i);
            System.out.println("setHomeConnectiondb1233 "+matchedContacts.getName());
            db.putHomeContacts(db,matchedContacts.getFreind_id(),matchedContacts.getName(),
                    matchedContacts.getImage(),matchedContacts.getCity(),matchedContacts.getState(),
                    matchedContacts.getCountry(),matchedContacts.getGps());
        }
        setDataConnect();

    }
}
