package com.entrophy;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.entrophy.fragments.DistanceFriendsList;
import com.entrophy.fragments.FriendsList;
import com.entrophy.fragments.GroupbyLocationFriends;
import com.entrophy.helper.ConstantStrings;
import com.entrophy.helper.Constants;
import com.entrophy.helper.CustomToast;
import com.entrophy.helper.Loader;
import com.entrophy.helper.Notify;
import com.entrophy.helper.SharedPreference;
import com.entrophy.model.LoginApiResult;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener,
        NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ImageView menu;
    private DrawerLayout drawer;
    private ImageView add_friends,notifications;
    private Switch switch_;
    private ImageView bg_view;
    private TextView sealth_text;
    private TextView user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_home);
        initUi();
    }

    public void initUi() {
        sealth_text = (TextView)findViewById(R.id.sealth_text);
        user_name = (TextView)findViewById(R.id.user_name);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        bg_view = (ImageView) findViewById(R.id.bg_view);
        switch_ = (Switch) findViewById(R.id.switch_);
        notifications = (ImageView) findViewById(R.id.notifications);
        add_friends = (ImageView) findViewById(R.id.add_friends);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        menu = (ImageView) findViewById(R.id.menu);
        LinearLayout my_profile = (LinearLayout) findViewById(R.id.my_profile);
        menu.setOnClickListener(this);
        notifications.setOnClickListener(this);
        add_friends.setOnClickListener(this);
        my_profile.setOnClickListener(this);
        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(this);
        setupTabIcons();
        
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ColorStateList myColorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_pressed},
                        new int[]{}
                },
                new int[] {
                        getResources().getColor(R.color.white),
                        getResources().getColor(R.color.black)
                }
        );
        switch_.setTextColor(myColorStateList);
        if(switch_.isActivated()) {
            sealth_text.setTextColor(Color.RED);

        } else {
            sealth_text.setTextColor(getResources().getColor(R.color.dark_gray));

        }

        switch_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(switch_.isChecked()) {
                    sealth_text.setTextColor(Color.RED);
                    bg_view.setVisibility(View.VISIBLE);

                    Notify.show(HomeActivity.this, "You are entering Stealth Mode for 24 hours.",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    bg_view.setVisibility(View.GONE);

                                    if(i == dialogInterface.BUTTON_POSITIVE) {
                                        switch_.setActivated(true);
                                    }
                                    if(i == dialogInterface.BUTTON_NEGATIVE) {
                                        switch_.setActivated(false);

                                    }
                                    callSealthUpdatelAPI();
                                }

                            },"Ok","Back");
                } else {
                    switch_.setActivated(false);
                    callSealthUpdatelAPI();
                    sealth_text.setTextColor(getResources().getColor(R.color.dark_gray));
                }
            }
        });
        updateUSerData();
    }

    public void updateUSerData() {
        user_name.setText(SharedPreference.getString(this,Constants.KEY_USER_NAME));
        String sealth_mode = SharedPreference.getString(this,Constants.KEY_SEALTH_MODE);
        System.out.println("KEY_SEALTH_MODE "+sealth_mode);
        if(sealth_mode.equalsIgnoreCase("Y")) {
            switch_.setActivated(true);
            switch_.setChecked(true);
        } else {
            switch_.setChecked(false);
            switch_.setActivated(false);
        }
    }

    private void setupTabIcons() {

        tabLayout.getTabAt(0).setIcon(R.drawable.apps);
        tabLayout.getTabAt(1).setIcon(R.drawable.format_list_bulleted_square);
        tabLayout.getTabAt(2).setIcon(R.drawable.location_green);
        int tabIconColor = ContextCompat.getColor(this, R.color.green);
        tabLayout.getTabAt(0).getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(ContextCompat.getColor(this,
                R.color.tabUnselectedIconColor), PorterDuff.Mode.SRC_IN);

    }


    private void setupViewPager() {
        try {


            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
            adapter.addFrag(new FriendsList(), "");
            adapter.addFrag(new DistanceFriendsList(), "");
            adapter.addFrag(new GroupbyLocationFriends(), "");
            viewPager.setAdapter(adapter);
        } catch (Exception e){
            System.out.println("Exception1234 setupViewPager"+e.toString());

        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        System.out.println("onTabSelected "+tab.getPosition());
        int tabIconColor = ContextCompat.getColor(this, R.color.green);
        if(tab.getPosition() == 0){
            tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        }
        if(tab.getPosition() == 1){
            tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        }
        if(tab.getPosition() == 2){
            tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        int tabIconColor = ContextCompat.getColor(this, R.color.tabUnselectedIconColor);
        tabLayout.getTabAt(0).getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
    private void setTextColorForMenuItem(MenuItem menuItem, @ColorRes int color) {
        SpannableString spanString = new SpannableString(menuItem.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, color)), 0, spanString.length(), 0);
        menuItem.setTitle(spanString);
        Drawable drawable = menuItem.getIcon();
        if(drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
         // setTextColorForMenuItem(item, R.color.colorPrimary);
        if(item.getItemId() == R.id.logout) {
            Notify.show(this, "Are you sure want to logout? ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(i == dialogInterface.BUTTON_POSITIVE) {
                        SharedPreference.clear(HomeActivity.this);
                        Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            },"OK","Cancel");

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onBackPressed() {
        Notify.show(this, "Are you sure want to exit? ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == dialogInterface.BUTTON_POSITIVE) {
                    Intent a = new Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);
                    finish();
                }
            }
        },"OK","Cancel");
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.menu) {
            if(!drawer.isDrawerOpen(GravityCompat.START))
                drawer.openDrawer(Gravity.START);
            else
                drawer.closeDrawer(Gravity.END);
        } else if(view.getId() == R.id.my_profile) {
            Intent i = new Intent(this,MyProfile.class);
            i.putExtra("friends_profile",true);
            startActivity(i);
        }else if(view.getId() == R.id.add_friends) {
            Intent i = new Intent(this,TabbedContacts.class);
            startActivity(i);
        }else if(view.getId() == R.id.notifications) {
            Intent i = new Intent(this,Notifications.class);
            startActivity(i);
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

    public void callSealthUpdatelAPI() {
        try {
            String mode = "";
            if(switch_.isActivated()) {
                mode = "Y";
            } else {
                mode = "N";

            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("stealth_mode",mode);
            jsonObject.put("user_id", String.valueOf(SharedPreference.getInt(this,Constants.KEY_USER_ID)));
            jsonObject.put("access_token",SharedPreference.getString(this,Constants.KEY_ACCESS_TOKEN));
            Loader.show(this);

            final String finalMode = mode;
            Constants.invokeAPIEx(this,Constants.STEALTHMODE,jsonObject.toString(),new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    Loader.hide();
                    try {
                        if (message != null && message.obj != null) {
                            String result = (String) message.obj;
                            LoginApiResult data = new Gson().fromJson(result, LoginApiResult.class);
                            System.out.println("HomeActivityPrint callAPI Response " + result + " status " + data.getStatus());

                            if (data.getStatus().equalsIgnoreCase("success")) {
                                new CustomToast().Show_Toast(HomeActivity.this, data.getMessage(), R.color.red);
                                SharedPreference.setString(HomeActivity.this,Constants.KEY_SEALTH_MODE, finalMode);
                            } else {
                                if (data.getMessage() != null) {
                                    new CustomToast().Show_Toast(HomeActivity.this, data.getMessage(), R.color.red);
                                } else {
                                    new CustomToast().Show_Toast(HomeActivity.this, ConstantStrings.common_msg, R.color.red);
                                }
                            }
                        } else {
                            new CustomToast().Show_Toast(HomeActivity.this, ConstantStrings.common_msg, R.color.red);

                        }
                        return false;
                    } catch (Exception e){
                        new CustomToast().Show_Toast(HomeActivity.this, ConstantStrings.common_msg, R.color.red);

                        return false;
                    }
                }
            }));
        } catch (Exception e){
            System.out.println("HomeActivityPrint callAPI Exception "+e.toString());
        }
    }

}
