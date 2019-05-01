package com.entrophy.adapters;

/**
 * Created by Khushvinders on 15-Nov-16.
 */

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.entrophy.R;
import com.entrophy.helper.Constants;
import com.entrophy.helper.DataBaseHelper;
import com.entrophy.helper.SharedPreference;
import com.entrophy.model.MatchedContacts;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GroupByLocAdapter extends BaseAdapter {

    private final DataBaseHelper db;
    private int tab_number = 0;
    private  List<MatchedContacts> friends = new ArrayList<>();
    private  Context context;

    public GroupByLocAdapter(Context context, List<MatchedContacts> friends) {
        this.friends = friends;
        this.context = context;
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        try {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final ViewHolder holder = new ViewHolder();

            view = inflater.inflate(R.layout.bird_eye_item, viewGroup, false); // inflate your_layout.xml

            holder.root_view = (ImageView) view.findViewById(R.id.root_view);
            holder.city = (TextView) view.findViewById(R.id.city);
            holder.state_country = (TextView) view.findViewById(R.id.state_country);
            holder.count = (TextView) view.findViewById(R.id.count);


            view.setTag(holder);


            MatchedContacts contactsDeo = friends.get(i);

            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

            String sealth_mode = SharedPreference.getString(context, Constants.KEY_SEALTH_MODE);
            System.out.println("KEY_SEALTH_MODE "+sealth_mode);
            if((tab_number == Constants.HomeConnectionsContacts && sealth_mode.equalsIgnoreCase("N"))
                    || (tab_number == Constants.HomeConnectionsContacts && contactsDeo.getGps().equalsIgnoreCase("on"))) {
                holder.root_view.setAlpha(1f);

            }  if((sealth_mode.equalsIgnoreCase("Y"))){
                view.setBackground(context.getResources().getDrawable(R.drawable.gray_bird_eye_bg));
            } else {
                view.setBackground(context.getResources().getDrawable(R.drawable.bird_eye_bg));

            }


            StringBuilder stringBuilder = new StringBuilder();

            if(contactsDeo.getCity() != null) {
                holder.city.setText(contactsDeo.getCity());
            }
            if(contactsDeo.getState() != null) {
                stringBuilder.append(contactsDeo.getState());
            }
            if(contactsDeo.getCountry() != null) {
                stringBuilder.append(", "+contactsDeo.getCountry());
            }

            if(contactsDeo.getFriend_cnt() != null) {
                holder.count.setText("("+contactsDeo.getFriend_cnt()+" Friends)");
            }
            if(stringBuilder.length() > 0) {
                holder.state_country.setText(stringBuilder.toString());
            }
        }catch (Exception e){
            System.out.println("LayoutInflater getMeasuredWidth Exception "+e.toString());

        }
        // inflate the layout
        return view;
    }


    static class ViewHolder {
        ImageView root_view;
        TextView city,state_country,count;
    }

}
