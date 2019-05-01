package com.entrophy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.entrophy.adapters.ContactsAdapter;
import com.entrophy.adapters.GridFrirendsListAdapter;
import com.entrophy.helper.ConstantStrings;
import com.entrophy.helper.Constants;
import com.entrophy.helper.CustomToast;
import com.entrophy.helper.DataBaseHelper;
import com.entrophy.helper.Helper;
import com.entrophy.helper.Loader;
import com.entrophy.helper.SharedPreference;
import com.entrophy.model.ContactsDeo;
import com.entrophy.model.MatchedContacts;
import com.entrophy.model.SuccessREs;
import com.entrophy.model.ValidateContactsFriend;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Contacts extends Activity implements View.OnClickListener,TextWatcher{

    private RecyclerView contacts_list_invite;
    private GridView contacts_list_connect;
    private ContactsAdapter invitemAdapter;
    private List<ContactsDeo> allcontactsList = new ArrayList<>();
    private LinearLayout selected_contacts,info_view,search_bar;
    private TextView skip,invite_b;
    private ImageView back_button;
    private Button select;
    private List<ContactsDeo> addedContacts = new ArrayList<>();
    private DataBaseHelper db;
    private GridFrirendsListAdapter mConnectiom;

    public boolean isSelected = false;
    private JSONArray jsonArray;
    private int type = Constants.InviteContacts;
    private TextView contacts_coutn;
    private ValidateContactsFriend validateContactsFriend;
    private List<MatchedContacts> connectedContactsList = new ArrayList<>();
    private EditText search_text;
    private TextView title_val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        db = new DataBaseHelper(this);
        type = getIntent().getIntExtra("ConnectionType",0);

            GetContacts getContacts = new GetContacts(this);
            getContacts.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Helper.hideKeyboard(this);


    }



    private void init() {
        try {
            skip = (TextView) findViewById(R.id.skip);
            title_val = (TextView) findViewById(R.id.title_val);
            contacts_coutn = (TextView) findViewById(R.id.contacts_coutn);
            contacts_coutn.setVisibility(View.GONE);
            select = (Button) findViewById(R.id.select);
            invite_b = (TextView) findViewById(R.id.invite_b);
            invite_b.setVisibility(View.GONE);
            back_button = (ImageView) findViewById(R.id.back_button);
            info_view = (LinearLayout) findViewById(R.id.info_view);
            search_bar = (LinearLayout) findViewById(R.id.search_bar);
            selected_contacts = (LinearLayout) findViewById(R.id.selected_contacts);
            search_text = (EditText) findViewById(R.id.search_text);
            search_text.addTextChangedListener(this);
            back_button.setOnClickListener(this);
            select.setOnClickListener(this);
            skip.setOnClickListener(this);
            invite_b.setOnClickListener(this);
            contacts_list_invite = (RecyclerView) findViewById(R.id.contacts_list_invite);
            contacts_list_connect = (GridView) findViewById(R.id.contacts_list_connect);
            selectedFriends();

            if(type == Constants.ConnectionsContacts) {
                invite_b.setText("Connect");
                setDataConnect();
                search_bar.setVisibility(View.GONE);
            } else {
                invite_b.setText("Invite");
                setDataInvite();
                search_bar.setVisibility(View.VISIBLE);
            }

            Constants.startLocationService(this);
            Constants.starGPSService(this);
            //searchView.setQueryHint("Search");

        }
         catch (Exception e){
            System.out.println("Contacts1234 Exception "+e.toString());
        }
    }



    public void setDataInvite() {
        invite_b.setText("Invite");
        search_bar.setVisibility(View.VISIBLE);

        type = Constants.InviteContacts;
        info_view.setVisibility(View.GONE);
        contacts_coutn.setText(allcontactsList.size()+" Contacts");

        allcontactsList.clear();
        allcontactsList = db.getAllContacts("");
        contacts_list_invite.setVisibility(View.VISIBLE);
        contacts_list_connect.setVisibility(View.GONE);
        invitemAdapter = new ContactsAdapter(Contacts.this, allcontactsList, Constants.InviteContacts,
                new ContactsAdapter.ContactsAdapterListener() {
            @Override
            public void checkBoXChecked(int position, boolean added,String search_text) {
                addedContacts(position,added,search_text);

                if (addedContacts.size() > 0) {

                    invite_b.setVisibility(View.VISIBLE);
                    contacts_coutn.setText(addedContacts.size()+" Contacts Selected");
                    contacts_coutn.setVisibility(View.VISIBLE);
                    System.out.println("onCheckedChanged Contacts1234 checkBoXChecked " + addedContacts.size());
                    skip.setVisibility(View.GONE);
                    back_button.setVisibility(View.VISIBLE);
                    selected_contacts.setVisibility(View.VISIBLE);

                } else {
                    select.setText("Select All");

                    invite_b.setVisibility(View.GONE);
                    contacts_coutn.setText(allcontactsList.size()+" Contacts");
                    contacts_coutn.setVisibility(View.VISIBLE);
                    selected_contacts.setVisibility(View.GONE);
                    skip.setVisibility(View.VISIBLE);
                    back_button.setVisibility(View.GONE);
                }
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        contacts_list_invite.setLayoutManager(mLayoutManager);
        contacts_list_invite.setItemAnimator(new DefaultItemAnimator());
        contacts_list_invite.setAdapter(invitemAdapter);

    }

    public void setDataConnect() {
        invite_b.setText("Connect");
        search_bar.setVisibility(View.GONE);

        getListFRomJSon();
        connectedContactsList.clear();
        connectedContactsList = db.getConnectedContacts();
        contacts_coutn.setText(connectedContactsList.size()+" people know you are using Knowinu");

        contacts_list_invite.setVisibility(View.GONE);
        contacts_list_connect.setVisibility(View.VISIBLE);
        mConnectiom = new GridFrirendsListAdapter(Contacts.this, connectedContactsList, Constants.ConnectionsContacts, new GridFrirendsListAdapter.ContactsAdapterListener() {
            @Override
            public void checkBoXChecked(int position, boolean added) {
                addedContacts(position, added,"");
                if (addedContacts.size() > 0) {
                    invite_b.setVisibility(View.VISIBLE);
                    contacts_coutn.setText(addedContacts.size()+" Contacts Selected");
                    contacts_coutn.setVisibility(View.VISIBLE);
                    skip.setVisibility(View.GONE);
                    back_button.setVisibility(View.VISIBLE);

                } else {
                    select.setText("Select All");
                    invite_b.setVisibility(View.GONE);
                    contacts_coutn.setText(connectedContactsList.size()+" people know you are using Knowinu");
                    contacts_coutn.setVisibility(View.VISIBLE);
                    selected_contacts.setVisibility(View.GONE);
                    info_view.setVisibility(View.GONE);
                    skip.setVisibility(View.VISIBLE);
                    back_button.setVisibility(View.GONE);
                }
            }
        });

        contacts_list_connect.setAdapter(mConnectiom);

    }

    public void getListFRomJSon() {
        try {
            //  String jsonArray = resp.getString("matched_contacts");
            List<MatchedContacts> matchedContactses = validateContactsFriend.getMatched_contacts();
            if(matchedContactses.size() > 0) {
                db.deleteFromConnectedContacts();
                for (int i = 0;i<matchedContactses.size();i++) {
                    String whereclause = " Where User_Phone = '"+matchedContactses.get(i).getMobile_no()+"'";
                    System.out.println("LetsConnectPrint getListFRomJSon resp "+whereclause);

                    List<ContactsDeo> deos = db.getAllContacts(whereclause);
                    String name = matchedContactses.get(i).getMobile_no();
                    if(deos.size() > 0) {
                        name = deos.get(0).getName();
                    }
                    db.putConnectionContacts(db,name,
                            matchedContactses.get(i).getMobile_no(),"false",
                            matchedContactses.get(i).getImage(),matchedContactses.get(i).getUser_id());
                }
            }

        } catch (Exception e){
            System.out.println("LetsConnectPrint getListFRomJSon Exception "+e.toString());
        }
    }

    public void addedContacts(int position, boolean added,String search_text) {
        addedContacts.clear();
        addedContacts = db.getAddedContacts();
        if(type == Constants.InviteContacts) {
            allcontactsList.clear();
            String whereclause ="";
            if(search_text.length() != 0) {
                whereclause = " Where User_Phone LIKE " +
                        "'%"+search_text+"%' OR User_Name LIKE '%"+search_text+"%'";

            }
            allcontactsList = db.getAllContacts(whereclause);
        } else {
            connectedContactsList.clear();
            connectedContactsList = db.getConnectedContacts();
        }

        if(added) {
            if(type == Constants.InviteContacts) {
                if (!addedContacts.contains(allcontactsList.get(position).getPhoneNumber())) {
                    db.putAddedContacts(db, allcontactsList.get(position).getName(),
                            allcontactsList.get(position).getPhoneNumber(), "true",
                            allcontactsList.get(position).getImage(), allcontactsList.get(position).getContact_id());
                }

            } else {
                if (!addedContacts.contains(connectedContactsList.get(position).getMobile_no())) {
                    db.putAddedContacts(db, connectedContactsList.get(position).getName(),
                            connectedContactsList.get(position).getMobile_no(), "true", connectedContactsList.get(position).getImage(),
                            connectedContactsList.get(position).getID());
                }
            }
        } else if(addedContacts.size() > 0 ) {
            if(type == Constants.InviteContacts) {

                db.deleteFromAddedContacts(allcontactsList.get(position).getContact_id());
            } else {
                db.deleteFromAddedContacts(connectedContactsList.get(position).getID());

            }
        }
        System.out.println("addedContacts "+addedContacts.size()+ " added "+added+" "+position);
        selectedFriends();

    }

    @Nullable
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    public void selectedFriends() {
        selected_contacts.removeAllViews();
        addedContacts.clear();
        addedContacts = db.getAddedContacts();
        for (int i=0;i<addedContacts.size();i++) {
            View layout2 = LayoutInflater.from(this).inflate(R.layout.selected_contacts, selected_contacts, false);
            ImageView remove_contact = (ImageView)layout2.findViewById(R.id.remove_contact);
            TextView name = (TextView)layout2.findViewById(R.id.name);
            name.setText(addedContacts.get(i).getName());
            remove_contact.setTag(i);
            remove_contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int rmi = (Integer) view.getTag();
                   // mAdapter.add(false,rmi);
                    db.deleteFromAddedContacts(addedContacts.get(rmi).getNewContact_id());
                    System.out.println("onCheckedChanged  boolean " + addedContacts.get(rmi).getNewContact_id());
                    if(type == Constants.InviteContacts) {
                        db.UpdateAllContacts("false", addedContacts.get(rmi).getNewContact_id());
                    } else {
                        db.UpdateConnectedContacts("false", addedContacts.get(rmi).getNewContact_id());

                    }
                    selectedFriends();
                    if(type == Constants.InviteContacts) {
                        invitemAdapter.notifyadd();
                    } else {
                        mConnectiom.notifyadd();

                    }
                    addedContacts.clear();
                    addedContacts = db.getAddedContacts();
                    if(addedContacts.size() != 0) {
                        contacts_coutn.setText(addedContacts.size() + " Contacts Selected");
                        contacts_coutn.setVisibility(View.VISIBLE);
                        if(type == Constants.InviteContacts) {
                            selected_contacts.setVisibility(View.VISIBLE);
                            info_view.setVisibility(View.GONE);

                        } else {
                            selected_contacts.setVisibility(View.GONE);
                            info_view.setVisibility(View.GONE);


                        }
                    } else {
                        contacts_coutn.setVisibility(View.VISIBLE);
                        if(type == Constants.InviteContacts) {
                            info_view.setVisibility(View.GONE);
                        } else {
                            info_view.setVisibility(View.GONE);

                        }
                        selected_contacts.setVisibility(View.GONE);

                    }

                }
            });
            selected_contacts.addView(layout2);
        }
        addedContacts.clear();

        addedContacts = db.getAddedContacts();
        if(addedContacts.size() !=0) {
            contacts_coutn.setText(addedContacts.size() + " Contacts Selected");
            contacts_coutn.setVisibility(View.VISIBLE);
            if(type == Constants.InviteContacts) {
                info_view.setVisibility(View.GONE);
            } else {
                info_view.setVisibility(View.GONE);
            }
            invite_b.setVisibility(View.VISIBLE);
        } else {
            invite_b.setVisibility(View.GONE);
            contacts_coutn.setVisibility(View.VISIBLE);
            if(type == Constants.InviteContacts) {
                contacts_coutn.setText(allcontactsList.size() + " Contacts");

            } else {
                contacts_coutn.setText(connectedContactsList.size() + "  people know you are using Knowinu");

            }
        }

        if(type == Constants.InviteContacts) {
            info_view.setVisibility(View.GONE);
            selected_contacts.setVisibility(View.VISIBLE);
        } else {
            info_view.setVisibility(View.GONE);
            selected_contacts.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.back_button) {
            if(type == Constants.InviteContacts) {
                db.UpdateAllContacts("false", "");
            } else {
                db.UpdateConnectedContacts("false", "");
            }
            select.setText("Select All");

            contacts_coutn.setVisibility(View.VISIBLE);
            if(type == Constants.InviteContacts) {
                contacts_coutn.setText(allcontactsList.size() + " Contacts");

            } else {
                contacts_coutn.setText(connectedContactsList.size() + "  people know you are using Knowinu");

            }
            selected_contacts.setVisibility(View.GONE);
            if(type == Constants.InviteContacts) {
                info_view.setVisibility(View.GONE);
            } else {
                info_view.setVisibility(View.GONE);

            }
            skip.setVisibility(View.VISIBLE);
            back_button.setVisibility(View.GONE);
            db.deleteFromAddedContacts("");

            if(type == Constants.InviteContacts) {
                invitemAdapter.notifyadd();
            } else {
                mConnectiom.notifyadd();

            }
            selectedFriends();
        } else if(view.getId() == R.id.skip) {
            System.out.println("InviteContacts122 "+type);
            if(type == Constants.InviteContacts) {
                Intent intent = new Intent(this, ConnectionSet.class);
                intent.putExtra("skip_button", true);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, InviteOthers.class);
                startActivity(intent);;
            }
        }else if(view.getId() == R.id.select) {

            if(isSelected) {

                if(type == Constants.InviteContacts) {
                    db.UpdateAllContacts("false", "");
                } else {
                    db.UpdateConnectedContacts("false", "");
                }
                if(type == Constants.InviteContacts) {
                    info_view.setVisibility(View.GONE);
                } else {
                    info_view.setVisibility(View.GONE);

                }
                skip.setVisibility(View.VISIBLE);
                back_button.setVisibility(View.GONE);
                db.deleteFromAddedContacts("");
                isSelected = false;
                select.setText("Select All");

            } else {
                if(type == Constants.InviteContacts) {

                    info_view.setVisibility(View.GONE);
                } else {
                    info_view.setVisibility(View.GONE);

                }
                skip.setVisibility(View.GONE);
                back_button.setVisibility(View.VISIBLE);
                selectAll();
                if(type == Constants.InviteContacts) {
                    db.UpdateAllContacts("true", "");
                } else {
                    db.UpdateConnectedContacts("true", "");

                }
                select.setText("Deselect All");
                isSelected = true;

            }
            if(type == Constants.InviteContacts) {
                search_text.setText("");
                invitemAdapter.notifySearchadd("");
            } else {
                mConnectiom.notifyadd();

            }

            selectedFriends();
        } else if(view.getId() == R.id.invite_b) {
            if(addedContacts.size() !=0) {
                if (type == Constants.InviteContacts) {
                    sendSms(1);
                } else {
                    addConntectionJSon();
                }
            } else {
                new CustomToast().Show_Toast(Contacts.this, "Please Select atlease 1 contact", R.color.red);
            }
//            if(invite_b.getText().toString().contains("invite")) {
//                setDataInvite();
//            } else {
//                Intent intent = new Intent(this,ConnectionSet.class);
//                intent.putExtra("skip_button",false);
//                startActivity(intent);
//            }

        }
    }


    public void sendSms(int smstype ) {
        addedContacts.clear();
        addedContacts = db.getAddedContacts();
        StringBuilder sb = new StringBuilder();
        for(int i = 0;i<addedContacts.size();i++) {
            String phone_number = addedContacts.get(i).getPhoneNumber().trim().replaceAll("-","").
                    replaceAll("\\s","").replaceAll("\"[-+.^:,]\",\"\"","");
            if(i != 0) {
                String prefix = ";";

                sb.append(prefix+phone_number);
            } else {
                sb.append(phone_number);
            }
        }
        String sel_cat = sb.toString();
        System.out.println("sendSms1232112 "+SharedPreference.getString(Contacts.this,Constants.KEY_TEXTMESSAGE));

        if(smstype == 1) {
            Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + sel_cat));
            smsIntent.putExtra("sms_body", SharedPreference.getString(Contacts.this,Constants.KEY_TEXTMESSAGE));
            try {
                startActivityForResult(smsIntent,1);
            } catch (android.content.ActivityNotFoundException ex) {
                new CustomToast().Show_Toast(Contacts.this, "SMS faild, please try again later", R.color.red);

            }
        } else {

            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra("jid", sel_cat + "@s.whatsapp.net"); //phone number without "+" prefix
            i.setPackage("com.whatsapp");           // so that only Whatsapp reacts and not the chooser
            i.putExtra(Intent.EXTRA_SUBJECT, "Subject");
            i.putExtra(Intent.EXTRA_TEXT, SharedPreference.getString(Contacts.this,Constants.KEY_TEXTMESSAGE));
            startActivityForResult(Intent.createChooser(i, "Share via"),1);
//            String smsNumber = sel_cat; // E164 format without '+' sign
//            if(!smsNumber.startsWith("91")) {
//                smsNumber = "91"+smsNumber;
//            }
//            Intent sendIntent = new Intent(Intent.ACTION_SEND);
//            sendIntent.setType("text/plain");
//            sendIntent.putExtra(Intent.EXTRA_TEXT,  SharedPreference.getString(Contacts.this,Constants.KEY_TEXTMESSAGE));
//            sendIntent.setPackage("com.whatsapp");
//            startActivityForResult(Intent.createChooser(sendIntent, "Share via"),1);

            //startActivity(sendIntent);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Intent intent = new Intent(this,ConnectionSet.class);
            intent.putExtra("skip_button",true);
            startActivity(intent);
        }}

    public void selectAll() {

        db.deleteFromAddedContacts("");

        if(type == Constants.InviteContacts) {
            allcontactsList.clear();
            allcontactsList = db.getAllContacts("");
            for (int i = 0; i < allcontactsList.size(); i++) {
                db.putAddedContacts(db, allcontactsList.get(i).getName(),
                        allcontactsList.get(i).getPhoneNumber(), "true", allcontactsList.get(i).getImage(),
                        allcontactsList.get(i).getContact_id());
            }
        } else {
            connectedContactsList.clear();
            connectedContactsList = db.getConnectedContacts();
            for (int i = 0; i < connectedContactsList.size(); i++) {
                db.putAddedContacts(db, connectedContactsList.get(i).getName(),
                        connectedContactsList.get(i).getMobile_no(), "true", connectedContactsList.get(i).getImage(),
                        connectedContactsList.get(i).getID());
            }
        }
        selectedFriends();
        if(type == Constants.InviteContacts) {
            invitemAdapter.notifyadd();
        } else {
            mConnectiom.notifyadd();

        }
        if(addedContacts.size() != 0) {
            contacts_coutn.setText(addedContacts.size() + " Contacts");
            contacts_coutn.setVisibility(View.VISIBLE);
        } else {
            contacts_coutn.setVisibility(View.VISIBLE);

        }
    }

    public void addConntectionJSon() {
        jsonArray = new JSONArray();
        List<MatchedContacts> temp = validateContactsFriend.getMatched_contacts();
        for(int i = 0;i<addedContacts.size();i++) {
            jsonArray.put(Integer.parseInt(temp.get(i).getUser_id()));
        }
        callAPConnectI();
    }


    public void callAPConnectI() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", SharedPreference.getInt(this,Constants.KEY_USER_ID));
            jsonObject.put("access_token", SharedPreference.getString(this,Constants.KEY_ACCESS_TOKEN));
            jsonObject.put("freind_request",jsonArray);
            Loader.show(this);

            Constants.invokeAPIEx(this,Constants.FRIENDREQUEST,jsonObject.toString(),new Handler(new Handler.Callback() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public boolean handleMessage(Message message) {
                    Loader.hide();
                    if(message != null && message.obj != null) {
                        String result = (String) message.obj;
                        SuccessREs data = new Gson().fromJson(result, SuccessREs.class);

                        if(data.getStatus().equalsIgnoreCase("success")) {
                            Intent intent = new Intent(Contacts.this,ConnectionSet.class);
                            intent.putExtra("skip_button",false);
                            startActivity(intent);
                        }
                    } else {
                        new CustomToast().Show_Toast(Contacts.this, ConstantStrings.common_msg,R.color.red);

                    }
                    return false;
                }
            }));
        } catch (Exception e){
            System.out.println("LetsConnectPrint callAPI Exception "+e.toString());
        }
    }

    public void callGetFriendList() {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", SharedPreference.getInt(this,Constants.KEY_USER_ID));
            jsonObject.put("access_token", SharedPreference.getString(this,Constants.KEY_ACCESS_TOKEN));
            jsonObject.put("contact_numbers",jsonArray);
            Loader.show(this);

            Constants.invokeAPIEx(this,Constants.VALIDATECONTACTS,jsonObject.toString(),new Handler(new Handler.Callback() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public boolean handleMessage(Message message) {
                    Loader.hide();
                    if(message != null && message.obj != null) {
                        String result = (String) message.obj;
                         validateContactsFriend = new Gson().fromJson(result, ValidateContactsFriend.class);

                        if(validateContactsFriend.getStatus().equalsIgnoreCase("success")) {

                            if( validateContactsFriend.getTotal_count() != 0) {
                                type = Constants.ConnectionsContacts;
                            } else {
                                Intent intent = new Intent(Contacts.this, InviteOthers.class);
                                startActivity(intent);;
                            }
                            init();

                        } else {
                            if(validateContactsFriend.getMessage() != null) {
                                new CustomToast().Show_Toast(Contacts.this, ConstantStrings.common_msg, R.color.red);
                            }  else {
                                new CustomToast().Show_Toast(Contacts.this, ConstantStrings.common_msg,R.color.red);

                            }
                        }
                    } else {
                        new CustomToast().Show_Toast(Contacts.this, ConstantStrings.common_msg,R.color.red);

                    }
                    return false;
                }
            }));
        } catch (Exception e){
            System.out.println("LetsConnectPrint callAPI Exception "+e.toString());
        }
    }

    public void allContactsnJSon() {
        jsonArray = new JSONArray();
        for(int i = 0;i<allcontactsList.size();i++) {
            String phone_number = allcontactsList.get(i).getPhoneNumber().trim().replaceAll("-","").
                    replaceAll("\\s","").replaceAll("\"[-+.^:,]\",\"\"","");
            if( phone_number.startsWith("91")) {
                phone_number = phone_number.replaceFirst("91","");
            }
            phone_number = phone_number.replaceAll("\\D+","");

            jsonArray.put(Long.parseLong(phone_number));
        }
        callGetFriendList();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(editable.toString().length() > 0){
            title_val.setText("Search");
        } else {
            title_val.setText("Contacts");

        }
        invitemAdapter.notifySearchadd(editable.toString());

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
            if(type == 0) {
                allContactsnJSon();
            } else {
                init();
            }
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


}
