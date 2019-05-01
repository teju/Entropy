package com.entrophy.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.entrophy.R;
import com.entrophy.helper.Constants;
import com.entrophy.helper.DataBaseHelper;
import com.entrophy.helper.SharedPreference;
import com.entrophy.model.MatchedContacts;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by tejaswini on 22/03/2019.
 */

public class GridFrirendsListAdapter extends BaseAdapter {

    private final ContactsAdapterListener onClickListener;
    private final DataBaseHelper db;
    private int tab_number = 0;
    private  List<MatchedContacts> friends = new ArrayList<>();
    private  Context context;

    public GridFrirendsListAdapter(Context context, List<MatchedContacts> friends, int tab_number,
                                   ContactsAdapterListener onClickListener) {
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
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final ViewHolder holder = new ViewHolder();

                view = inflater.inflate(R.layout.item_friend_list, viewGroup, false); // inflate your_layout.xml

                //initialize your your_layout.xml view
                holder.name = (TextView) view.findViewById(R.id.name);
                holder.place = (TextView) view.findViewById(R.id.place);
                holder.root_view = (ImageView) view.findViewById(R.id.root_view);
                holder.checkbox = (CheckBox) view.findViewById(R.id.checkbox);

                view.setTag(holder);


            MatchedContacts contactsDeo = friends.get(i);


            int width= context.getResources().getDisplayMetrics().widthPixels;

            String sealth_mode = SharedPreference.getString(context,Constants.KEY_SEALTH_MODE);
            System.out.println("KEY_SEALTH_MODE "+sealth_mode);
            if (tab_number == Constants.HomeConnectionsContacts && sealth_mode.equalsIgnoreCase("N"))
            {
                if (contactsDeo.getGps().equalsIgnoreCase("on")) {
                    holder.root_view.setAlpha(1f);
                } else {
                    holder.root_view.setAlpha(0.5f);
                }
            } else  if(tab_number == Constants.HomeConnectionsContacts && sealth_mode.equalsIgnoreCase("Y")){
                holder.root_view.setAlpha(0.5f);
            } else {
                holder.root_view.setAlpha(1f);
            }

            //view.setLayoutParams(params);

            holder.checkbox.setTag(i);
            holder.root_view.setTag(i);

            if (tab_number == 2) {
                holder.name.setVisibility(View.GONE);
                holder.name.setVisibility(View.VISIBLE);
                Random rnd = new Random();
                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                view.setBackgroundColor(color);
            } else {
                holder.name.setText(contactsDeo.getName());
            }

            if(tab_number == Constants.ConnectionsContacts) {
                holder.checkbox.setVisibility(View.VISIBLE);
            } else {
                holder.checkbox.setVisibility(View.GONE);
            }
            try {
                if (contactsDeo.getIsselected().equals("true")) {
                    holder.checkbox.setChecked(true);
                } else {
                    holder.checkbox.setChecked(false);

                }
            }catch (Exception e){

            }

            Picasso.with(context).load(contactsDeo.getImage())
                    .memoryPolicy(MemoryPolicy.NO_STORE)
                    .centerCrop().resize(width/3,width/3)
                    .into(holder.root_view);
            if(tab_number == Constants.ConnectionsContacts) {
                final ViewHolder finalHolder = holder;
                holder.checkbox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = (Integer) view.getTag();
                        if (finalHolder.checkbox.isChecked()) {
                            db.UpdateConnectedContacts("true", friends.get(pos).getID());
                        } else {
                            db.UpdateConnectedContacts("false", friends.get(pos).getID());
                        }
                        onClickListener.checkBoXChecked(pos, finalHolder.checkbox.isChecked());
                        friends.clear();

                        friends = db.getConnectedContacts();
                        notifyDataSetChanged();
                    }
                });
                final ViewHolder finalHolder1 = holder;
                holder.root_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = (Integer) v.getTag();
                        System.out.println("setOnClickListener122221 " + holder.checkbox.isChecked());
                        if (holder.checkbox.isChecked()) {
                            holder.checkbox.setChecked(false);
                            db.UpdateConnectedContacts("false", friends.get(pos).getID());
                        } else {
                            holder.checkbox.setChecked(true);
                            db.UpdateConnectedContacts("true", friends.get(pos).getID());
                        }
                        onClickListener.checkBoXChecked(pos, holder.checkbox.isChecked());
                        friends.clear();
                        friends = db.getConnectedContacts();
                        notifyDataSetChanged();
                    }
                });
            }
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
    static class ViewHolder {
        ImageView root_view;
        TextView name,place;
        CheckBox checkbox;
    }

}
