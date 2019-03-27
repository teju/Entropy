package com.entrophy.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.entrophy.R;
import com.entrophy.model.DeoFriends;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by tejaswini on 22/03/2019.
 */

public class FrirendsListAdapter extends BaseAdapter {

    private int tab_number = 0;
    private  List<DeoFriends> friends = new ArrayList<>();
    private  Context context;

    public FrirendsListAdapter(Context context, List<DeoFriends> friends,int tab_number) {
        this.friends = friends;
        this.context = context;
        this.tab_number = tab_number;
    }

    @Override
    public int getCount() {
        return 10;
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
            final int[] width = {0};
            LayoutInflater inflter = (LayoutInflater.from(context));

            view = inflter.inflate(R.layout.item_friend_list, null);
            ViewTreeObserver vto = view.getViewTreeObserver();
            final View finalView = view;
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        finalView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        finalView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    width[0] = finalView.getMeasuredWidth();

                }
            });
            System.out.println("LayoutInflater getMeasuredWidth height " + " width " + width[0]);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.height = 340;
            view.setLayoutParams(params);
            TextView mtext = (TextView) view.findViewById(R.id.mtext);
            TextView mtext2 = (TextView) view.findViewById(R.id.mtext2);
            if (tab_number == 2) {
                mtext.setVisibility(View.GONE);
                mtext2.setVisibility(View.VISIBLE);
                Random rnd = new Random();
                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                view.setBackgroundColor(color);

            }
        }catch (Exception e){
            System.out.println("LayoutInflater getMeasuredWidth Exception "+e.toString());

        }
        // inflate the layout
        return view;
    }
}
