package com.entrophy.adapters;

/**
 * Created by Khushvinders on 15-Nov-16.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.entrophy.R;
import com.entrophy.helper.Constants;
import com.entrophy.helper.DataBaseHelper;
import com.entrophy.model.ContactsDeo;

import java.util.List;


public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> {
    private final int listType;
    private final Context context;
    private final DataBaseHelper db;
    public ContactsAdapterListener onClickListener;

    private List<ContactsDeo> contactsList;
    private String search_text = "";

    public void notifySearchadd(String search_text) {
        this.search_text = search_text;
        notifyadd();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView right_arrow;
        private final RelativeLayout viewroot;
        private final LinearLayout request;
        private final ImageView friend_image;
        public TextView name;
        private final CheckBox checkbox;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            checkbox = (CheckBox) view.findViewById(R.id.checkbox);
            viewroot = (RelativeLayout) view.findViewById(R.id.viewroot);
            request = (LinearLayout) view.findViewById(R.id.request);
            right_arrow = (ImageView) view.findViewById(R.id.right_arrow);
            friend_image = (ImageView) view.findViewById(R.id.friend_image);

            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    System.out.println("onCheckedChanged  boolean " +b+" "+getAdapterPosition());

                }
            });
        }

    }


    public ContactsAdapter(Context context,List<ContactsDeo> contactsList, int listType, ContactsAdapterListener onClickListener) {
        this.contactsList = contactsList;
        this.context = context;
        this.listType = listType;
        this.onClickListener = onClickListener;
        db = new DataBaseHelper(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contacts_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ContactsDeo contactsDeo = contactsList.get(position);
        holder.viewroot.setTag(position);
        holder.checkbox.setTag(position);
        holder.friend_image.setTag(position);
        if (contactsDeo.getName() != null && contactsDeo.getName().length() > 0) {
            holder.name.setText(contactsDeo.getName());
        } else {
            holder.name.setText(contactsDeo.getPhoneNumber());
        }
        try {
            if (contactsDeo.getIsselected().equals("true")) {
                holder.checkbox.setChecked(true);
            } else {
                holder.checkbox.setChecked(false);

            }
        }catch (Exception e){

        }
        if(listType == Constants.ListContactsRequest) {
            holder.checkbox.setVisibility(View.GONE);
            holder.right_arrow.setVisibility(View.VISIBLE);
            holder.request.setVisibility(View.GONE);

        } else if(listType == Constants.TabbedContactsRequest) {
            holder.checkbox.setVisibility(View.GONE);
            holder.right_arrow.setVisibility(View.GONE);
            holder.request.setVisibility(View.VISIBLE);

        } else {
            holder.right_arrow.setVisibility(View.GONE);
            holder.request.setVisibility(View.GONE);
            holder.checkbox.setVisibility(View.VISIBLE);


        }
//        if(listType == Constants.InviteContacts) {
//            holder.city.setVisibility(View.GONE);
//            holder.country.setVisibility(View.GONE);
//        } else {
//            holder.city.setVisibility(View.VISIBLE);
//            holder.country.setVisibility(View.VISIBLE);
//        }
        holder.viewroot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int i = (Integer)view.getId();
//                Intent intent = new Intent(context,MyProfile.class);
//                intent.putExtra("friends_profile",false);
//                context.startActivity(intent);
            }
        });
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (Integer)view.getTag();
                System.out.println("setOnClickListener122221 "+holder.checkbox.isChecked());

                if(holder.checkbox.isChecked()) {
                    db.UpdateAllContacts("true", contactsList.get(pos).getContact_id());
                } else {
                    db.UpdateAllContacts("false", contactsList.get(pos).getContact_id());
                }
                onClickListener.checkBoXChecked(pos, holder.checkbox.isChecked(),search_text);
                contactsList.clear();
                String whereclause = "";
                if(search_text.length() != 0) {
                    whereclause = " Where User_Phone LIKE " +
                            "'%"+search_text+"%' OR User_Name LIKE '%"+search_text+"%'";

                }
                contactsList = db.getAllContacts(whereclause);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    public void notifyadd() {
        String whereclause = "";
        if(search_text.length() != 0) {
            whereclause = " Where User_Phone LIKE " +
                    "'%"+search_text+"%' OR User_Name LIKE '%"+search_text+"%'";

        }
        contactsList.clear();
        contactsList = db.getAllContacts(whereclause);
        notifyDataSetChanged();
    }
    public interface ContactsAdapterListener {

        void checkBoXChecked(int position,boolean contactsDeos,String search_text);


    }

}