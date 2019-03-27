package com.entrophy;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.entrophy.fragments.DistanceFriendsList;
import com.entrophy.helper.Constants;

import java.util.ArrayList;
import java.util.List;

public class TabbedContacts extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_contacts);
        initUi();
    }

    public void initUi() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager() {
        try {

            DistanceFriendsList fragment1 = new DistanceFriendsList();
            fragment1.setListType(Constants.TabbedContactsRequest);
            fragment1.setTabbedContacts(false);
            DistanceFriendsList fragment2 = new DistanceFriendsList();
            fragment2.setListType(Constants.GeneralContactsRequest);
            fragment2.setTabbedContacts(true);
            DistanceFriendsList fragment3 = new DistanceFriendsList();
            fragment3.setListType(Constants.GeneralContactsRequest);
            fragment3.setTabbedContacts(true);

            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
            adapter.addFrag(fragment1, "Requests");
            adapter.addFrag(fragment2, "Connect");
            adapter.addFrag(fragment3, "Invite");
            viewPager.setAdapter(adapter);
        } catch (Exception e){
            System.out.println("Exception1234 setupViewPager"+e.toString());

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


}
