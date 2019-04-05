package com.entrophy;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.entrophy.fragments.DistanceFriendsList;
import com.entrophy.fragments.FriendsConnectList;
import com.entrophy.fragments.InviteFriendsList;
import com.entrophy.helper.ConstantStrings;
import com.entrophy.helper.Constants;
import com.entrophy.helper.CustomToast;
import com.entrophy.helper.DataBaseHelper;
import com.entrophy.helper.Loader;
import com.entrophy.helper.Notify;
import com.entrophy.helper.SharedPreference;
import com.entrophy.model.ContactsDeo;
import com.entrophy.model.FriendRequestResult;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TabbedContacts extends AppCompatActivity implements InviteFriendsList.InviteFriendsListListener,View.OnClickListener{

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TextView invite_b;
    private List<ContactsDeo> selectallList = new ArrayList<>();
    private List<ContactsDeo> addedContacts = new ArrayList<>();
    private DataBaseHelper db;
    private int type = 0;
    private JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_contacts);
        initUi();
    }

    public void initUi() {
        db = new DataBaseHelper(this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        invite_b = (TextView) findViewById(R.id.invite_b);
        invite_b.setOnClickListener(this);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager() {
        try {

            DistanceFriendsList distanceFriendsList = new DistanceFriendsList();
            FriendsConnectList friendsRequestList = new FriendsConnectList();
            friendsRequestList.setOnEventListener(this);
            InviteFriendsList inviteFriendsList = new InviteFriendsList();
            inviteFriendsList.setOnEventListener(this);
            ;

            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
            adapter.addFrag(distanceFriendsList, "Requests");
            adapter.addFrag(friendsRequestList, "Connect");
            adapter.addFrag(inviteFriendsList, "Invite");
            viewPager.setAdapter(adapter);
        } catch (Exception e){
            System.out.println("Exception1234 setupViewPager"+e.toString());

        }
    }

    @Override
    public void SelectAll(List<ContactsDeo> contactsList,int type) {
        this.selectallList = contactsList;
        this.type = type;
        if(selectallList.size() > 0) {
            invite_b.setVisibility(View.VISIBLE);
        } else {
            invite_b.setVisibility(View.GONE);
        }
        System.out.println("SelectAllListner "+contactsList.size());


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
        System.out.println("sendSms1232112 "+sel_cat);
        if(smstype == 1) {
            Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + sel_cat));
            smsIntent.putExtra("sms_body", "sms message goes here");
            try {
                startActivity(smsIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                new CustomToast().Show_Toast(TabbedContacts.this, "SMS faild, please try again later", R.color.red);

            }
        } else {
//            startActivity(i);
            String smsNumber = sel_cat; // E164 format without '+' sign
            if(!smsNumber.startsWith("91")) {
                smsNumber = "91"+smsNumber;
            }
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
            sendIntent.putExtra("jid", smsNumber + "@s.whatsapp.net"); //phone number without "+" prefix
            sendIntent.setPackage("com.whatsapp");
            startActivityForResult(Intent.createChooser(sendIntent, "Share via"),1);

            //startActivity(sendIntent);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.invite_b) {
            if(selectallList.size() != 0) {
                if (type == Constants.InviteContacts) {
                    Notify.show(TabbedContacts.this, "Invite Friends through WhatsApp or send SMS", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (i == dialogInterface.BUTTON_POSITIVE) {
                                sendSms(2);

                            } else {
                                sendSms(1);
                            }
                        }
                    }, "WhatsApp", "SMS");
                } else {
                    addConntectionJSon();
                }
            } else {
                new CustomToast().Show_Toast(TabbedContacts.this, "Please Select atlease 1 contact", R.color.red);
            }
        }
    }

    public void callAPConnectI() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", SharedPreference.getInt(TabbedContacts.this,Constants.KEY_USER_ID));
            jsonObject.put("access_token", SharedPreference.getString(TabbedContacts.this,Constants.KEY_ACCESS_TOKEN));
            jsonObject.put("freind_request",jsonArray);
            Loader.show(TabbedContacts.this);

            Constants.invokeAPIEx(TabbedContacts.this,Constants.FRIENDREQUEST,jsonObject.toString(),new Handler(new Handler.Callback() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public boolean handleMessage(Message message) {
                    Loader.hide();
                    if(message != null && message.obj != null) {
                        String result = (String) message.obj;
                        FriendRequestResult data = new Gson().fromJson(result, FriendRequestResult.class);

                        if(data.getStatus().equalsIgnoreCase("success")) {
                            new CustomToast().Show_Toast(TabbedContacts.this, data.getMessage(),R.color.red);

                        }
                    } else {
                        new CustomToast().Show_Toast(TabbedContacts.this, ConstantStrings.common_msg,R.color.red);

                    }
                    return false;
                }
            }));
        } catch (Exception e){
            System.out.println("LetsConnectPrint callAPI Exception "+e.toString());
        }
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }



    public void addConntectionJSon() {
        addedContacts.clear();
        addedContacts =db.getAddedContacts();
        jsonArray = new JSONArray();
        for(int i = 0;i<addedContacts.size();i++) {
            jsonArray.put(Integer.parseInt(addedContacts.get(i).getNewContact_id()));
        }
        callAPConnectI();

    }


}
