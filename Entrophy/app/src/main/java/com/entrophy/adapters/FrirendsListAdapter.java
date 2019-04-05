package com.entrophy.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.entrophy.R;
import com.entrophy.helper.Constants;
import com.entrophy.helper.DataBaseHelper;
import com.entrophy.model.MatchedContacts;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by tejaswini on 22/03/2019.
 */

public class FrirendsListAdapter extends BaseAdapter {

    private final ContactsAdapterListener onClickListener;
    private final DataBaseHelper db;
    private int tab_number = 0;
    private  List<MatchedContacts> friends = new ArrayList<>();
    private  Context context;

    public FrirendsListAdapter(Context context, List<MatchedContacts> friends, int tab_number, ContactsAdapterListener onClickListener) {
        this.friends = friends;
        this.context = context;
        this.tab_number = tab_number;
        this.onClickListener = onClickListener;
        db = new DataBaseHelper(context);


    }

    @Override
    public int getCount() {
        return friends.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        try {
            MatchedContacts contactsDeo = friends.get(i);

            final int[] width = {0};
            LayoutInflater inflter = (LayoutInflater.from(context));

            view = inflter.inflate(R.layout.item_friend_list, null);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.height = 340;
            view.setLayoutParams(params);

            TextView name = (TextView) view.findViewById(R.id.name);
            TextView place = (TextView) view.findViewById(R.id.place);
            final CheckBox checkbox = (CheckBox) view.findViewById(R.id.checkbox);
            checkbox.setTag(i);
            if (tab_number == 2) {
                name.setVisibility(View.GONE);
                name.setVisibility(View.VISIBLE);
                Random rnd = new Random();
                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                view.setBackgroundColor(color);
            } else {
                name.setText(contactsDeo.getName());
            }

            if(tab_number == Constants.ConnectionsContacts) {
                checkbox.setVisibility(View.VISIBLE);
            } else {
                checkbox.setVisibility(View.VISIBLE);
            }
            try {
                if (contactsDeo.getIsselected().equals("true")) {
                    checkbox.setChecked(true);
                } else {
                    checkbox.setChecked(false);

                }
            }catch (Exception e){

            }
            checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (Integer)view.getTag();
                    if(checkbox.isChecked()) {
                        db.UpdateConnectedContacts("true", friends.get(pos).getID());
                    } else {
                        db.UpdateConnectedContacts("false", friends.get(pos).getID());
                    }
                    onClickListener.checkBoXChecked(pos, checkbox.isChecked());
                    friends.clear();

                    friends = db.getConnectedContacts();
                    notifyDataSetChanged();
                }
            });


        }catch (Exception e){
            System.out.println("LayoutInflater getMeasuredWidth Exception "+e.toString());

        }
        // inflate the layout
        return view;
    }

    public void notifyadd() {
        friends.clear();
        friends = db.getConnectedContacts();
        notifyDataSetChanged();
    }
    public interface ContactsAdapterListener {

        void checkBoXChecked(int position,boolean contactsDeos);


    }
}
