package com.entrophy.adapters;

/**
 * Created by Khushvinders on 15-Nov-16.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.entrophy.MyProfile;
import com.entrophy.R;
import com.entrophy.model.ContactsDeo;

import java.util.List;


public class GroupByLocAdapter extends RecyclerView.Adapter<GroupByLocAdapter.MyViewHolder> {
    private final Context context;

    private List<ContactsDeo> contactsList;


    public class MyViewHolder extends RecyclerView.ViewHolder {


        public View viewroot;

        public MyViewHolder(View view) {
            super(view);
            viewroot = (LinearLayout)view.findViewById(R.id.friends_profile);

        }
    }


    public GroupByLocAdapter(Context context, List<ContactsDeo> moviesList) {
        this.contactsList = moviesList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contacts_group_by_loc_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
//        ContactsDeo movie = contactsList.get(position);
        holder.viewroot.setTag(position);
//
        holder.viewroot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = (Integer)view.getId();
                Intent intent = new Intent(context,MyProfile.class);
                intent.putExtra("friends_profile",false);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    public void add() {
        notifyDataSetChanged();
    }

}