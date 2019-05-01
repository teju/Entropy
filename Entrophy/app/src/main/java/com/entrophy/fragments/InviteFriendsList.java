package com.entrophy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.entrophy.R;
import com.entrophy.adapters.ContactsAdapter;
import com.entrophy.adapters.TabbedContactsAdapter;
import com.entrophy.helper.Constants;
import com.entrophy.helper.DataBaseHelper;
import com.entrophy.model.ContactsDeo;
import com.entrophy.model.FriendRequestResult;
import com.entrophy.model.ValidateContactsFriend;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class InviteFriendsList extends Fragment {


    private View view;
    private List<ContactsDeo> contactsList  = new ArrayList<>();
    private RecyclerView contacts_list_invite;
    private TabbedContactsAdapter mAdapter;
    private LinearLayout top_view;
    private FriendRequestResult friendRequestResult;
    private LinearLayout no_data;
    private TextView contacts_coutn,mtext;
    private DataBaseHelper db;
    public List<ContactsDeo> addedContacts = new ArrayList<>();
    private JSONArray jsonArray;
    private ValidateContactsFriend validateContactsFriend;
    private GridView contacts_list_connect;
    private LinearLayout search;
    private ContactsAdapter invitemAdapter;
    InviteFriendsListListener inviteFriendsListListener;
    private Button selectall;


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
    @Override
    public void onResume() {
        super.onResume();
        mtext.setText("Invite your friends to join Entrophy");
    }

    public  void initUI() {
        db = new DataBaseHelper(getActivity());

        contacts_list_invite = (RecyclerView)view.findViewById(R.id.contacts_list);
        selectall = (Button)view.findViewById(R.id.select);
        selectall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectall.getText().toString().equalsIgnoreCase("Select All")) {
                    db.UpdateAllContacts("true","");
                    selectall.setText("UnSelect All");
                    selectAll();
                } else {
                    db.UpdateAllContacts("false","");
                    selectall.setText("Select All");
                    db.deleteFromAddedContacts("");
                    invitemAdapter.notifyadd();
                    inviteFriendsListListener.SelectAll(db.getAddedContacts(),Constants.InviteContacts);

                }
                addedContacts = db.getAddedContacts();
                if(addedContacts.size() != 0) {
                    contacts_coutn.setText(addedContacts.size() + " Contacts Selected");
                    contacts_coutn.setVisibility(View.VISIBLE);
                } else {
                    contactsList = db.getAllContacts("");
                    contacts_coutn.setText(contactsList.size()+" Contacts");
                    contacts_coutn.setVisibility(View.VISIBLE);

                }
            }
        });
        contacts_list_connect = (GridView) view.findViewById(R.id.contacts_list_connect);
        contacts_list_connect.setVisibility(View.GONE);
        contacts_list_invite.setVisibility(View.GONE);

        top_view = (LinearLayout)view.findViewById(R.id.top_view);
        search = (LinearLayout)view.findViewById(R.id.search);
        no_data = (LinearLayout)view.findViewById(R.id.no_data);
        contacts_coutn = (TextView)view.findViewById(R.id.contacts_coutn);
        contacts_coutn.setVisibility(View.VISIBLE);
        mtext = (TextView)view.findViewById(R.id.mtext);

        System.out.println("DistanceFriendsList123 listType "+listType);
        setDataInvite();

    }
    public void addedContacts(int position, boolean added) {
        addedContacts.clear();
        addedContacts = db.getAddedContacts();
        List<ContactsDeo> contactsDeos = db.getAllContacts("");
        if(added) {
            db.putAddedContacts(db,contactsDeos.get(position).getName(),
                    contactsDeos.get(position).getPhoneNumber(),"true",contactsDeos.get(position).getImage(),
                    contactsDeos.get(position).getContact_id());
        } else if(addedContacts.size() > 0 ) {
            db.deleteFromAddedContacts(contactsDeos.get(position).getContact_id());
        }
        addedContacts = db.getAddedContacts();
        if(addedContacts.size() != 0) {
            contacts_coutn.setText(addedContacts.size() + " Contacts Selected");
            contacts_coutn.setVisibility(View.VISIBLE);
        } else {
            contacts_coutn.setText(contactsList.size()+" Contacts");

            contacts_coutn.setVisibility(View.VISIBLE);
        }
        System.out.println("addedContacts "+addedContacts.size()+ " added "+added+" "+position);
        inviteFriendsListListener.SelectAll(db.getAddedContacts(),Constants.InviteContacts);

    }


    public void setDataInvite() {

        top_view.setVisibility(View.VISIBLE);
        search.setVisibility(View.VISIBLE);
        contactsList.clear();
        contactsList = db.getAllContacts("");
        if(contactsList.size() != 0){
            no_data.setVisibility(View.GONE);
        }
        contacts_list_invite.setVisibility(View.VISIBLE);
        contacts_list_connect.setVisibility(View.GONE);
        contacts_coutn.setText(contactsList.size()+" Contacts");

        invitemAdapter = new ContactsAdapter(getActivity(), contactsList, Constants.InviteContacts, new ContactsAdapter.ContactsAdapterListener() {
            @Override
            public void checkBoXChecked(int position, boolean added,String search_text) {
                addedContacts(position,added);


            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        contacts_list_invite.setLayoutManager(mLayoutManager);
        contacts_list_invite.setItemAnimator(new DefaultItemAnimator());
        contacts_list_invite.setAdapter(invitemAdapter);
    }




    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
//            Intent intent = new Intent(getActivity(),ConnectionSet.class);
//            intent.putExtra("skip_button",true);
//            startActivity(intent);
        }
    }
    public interface InviteFriendsListListener {

        void SelectAll(List<ContactsDeo> contactsList, int type);


    }

    public void selectAll() {

        contactsList.clear();
        contactsList = db.getAllContacts("");
        db.deleteFromAddedContacts("");
        for (int i =0;i<contactsList.size();i++) {
            db.putAddedContacts(db,contactsList.get(i).getName(),contactsList.get(i).getPhoneNumber(),"true",contactsList.get(i).getImage(),
                    contactsList.get(i).getContact_id());
        }

        invitemAdapter.notifyadd();
        inviteFriendsListListener.SelectAll(db.getAddedContacts(),Constants.InviteContacts);
    }



    public void setOnEventListener(InviteFriendsListListener listener) {
        inviteFriendsListListener = listener;
    }
}
