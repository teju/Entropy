package com.entrophy.adapters;

/**
 * Created by Khushvinders on 15-Nov-16.
 */

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.entrophy.MyProfile;
import com.entrophy.R;
import com.entrophy.helper.Constants;
import com.entrophy.helper.CustomToast;
import com.entrophy.helper.SharedPreference;
import com.entrophy.model.MatchedContacts;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;


public class BirdEyeContactsAdapter extends RecyclerView.Adapter<BirdEyeContactsAdapter.MyViewHolder> {
    private final Context context;
    private List<MatchedContacts> contactsList;
    private String search_text = "";



    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout viewroot;
        private final ImageView friend_image,call_phone,text_message_user,whatsapp_message_user;
        public TextView name,phone_number,description,last_update;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            phone_number = (TextView) view.findViewById(R.id.phone_number);
            description = (TextView) view.findViewById(R.id.description);
            last_update = (TextView) view.findViewById(R.id.last_update);
            viewroot = (LinearLayout) view.findViewById(R.id.viewroot);
            friend_image = (ImageView) view.findViewById(R.id.friend_image);
            call_phone = (ImageView) view.findViewById(R.id.call_phone);
            text_message_user = (ImageView) view.findViewById(R.id.text_message_user);
            whatsapp_message_user = (ImageView) view.findViewById(R.id.whatsapp_message_user);

        }

    }


    public BirdEyeContactsAdapter(Context context, List<MatchedContacts> contactsList) {
        this.contactsList = contactsList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contacts_group_by_loc_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        MatchedContacts contactsDeo = contactsList.get(position);
        holder.viewroot.setTag(position);
        holder.call_phone.setTag(position);
        holder.text_message_user.setTag(position);
        holder.whatsapp_message_user.setTag(position);
        holder.friend_image.setTag(position);
        if (contactsDeo.getName() != null && contactsDeo.getName().length() > 0) {
            holder.name.setText(contactsDeo.getName());
        } else {
            holder.name.setText(contactsDeo.getName());
        }
        StringBuilder stringBuilder = new StringBuilder();
        if(contactsDeo.getLocation()!= null){
            stringBuilder.append(contactsDeo.getLocation());

        }
        if(contactsDeo.getCity()!= null){
            stringBuilder.append(", "+contactsDeo.getCity());

        }
        if(contactsDeo.getState()!= null){
            stringBuilder.append(", "+contactsDeo.getState());

        }
        if(contactsDeo.getCountry()!= null){
            stringBuilder.append(", "+contactsDeo.getCountry());

        }
        holder.phone_number.setText(contactsDeo.getMobile_no());
        holder.last_update.setText(contactsDeo.getUpdated_on());
        holder.description.setText(contactsDeo.getName()+" was in "+stringBuilder.toString());
        holder.viewroot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = (Integer)view.getTag();
                Intent intent = new Intent(context, MyProfile.class);
                intent.putExtra("friend_id",contactsList.get(i).getFreind_id());
                intent.putExtra("friends_profile",false);
                context.startActivity(intent);
            }
        });
        Picasso.with(context).load(contactsDeo.getImage())
                .memoryPolicy(MemoryPolicy.NO_STORE)
                .into(holder.friend_image);
        holder.call_phone.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                int i = (Integer)view.getTag();

                sendSms(3,i);
            }
        });
        holder.whatsapp_message_user.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                int i = (Integer)view.getTag();

                sendSms(2,i);

            }
        });
        holder.text_message_user.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                int i = (Integer)view.getTag();
                sendSms(1,i);

            }
        });

        String sealth_mode = SharedPreference.getString(context,Constants.KEY_SEALTH_MODE);
        System.out.println("KEY_SEALTH_MODE "+sealth_mode);
        if(contactsDeo.getGps().equalsIgnoreCase("off") || sealth_mode.equalsIgnoreCase("Y")) {
            holder.friend_image.setAlpha(0.5f);
        }  else {
            holder.friend_image.setAlpha(1f);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void sendSms(int smstype,int i ) {

        System.out.println("sendSms1232112 "+ SharedPreference.getString(context, Constants.KEY_TEXTMESSAGE));

        if(smstype == 1) {
            Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + contactsList.get(i).getMobile_no()));
            smsIntent.putExtra("sms_body", SharedPreference.getString(context,Constants.KEY_TEXTMESSAGE));
            try {
                context.startActivity(smsIntent);
            } catch (ActivityNotFoundException ex) {
                new CustomToast().Show_Toast(context, "SMS faild, please try again later", R.color.red);

            }
        } else if(smstype == 2) {

            String smsNumber = contactsList.get(i).getMobile_no(); // E164 format without '+' sign
            if(!smsNumber.startsWith("91")) {
                smsNumber = "91"+smsNumber;
            }
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT,  SharedPreference.getString(context,Constants.KEY_TEXTMESSAGE));
            sendIntent.setPackage("com.whatsapp");
            sendIntent.putExtra("jid", smsNumber + "@s.whatsapp.net"); //phone number without "+" prefix

            context.startActivity(Intent.createChooser(sendIntent, "Share via"));
        } else {

            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"+contactsList.get(i).getMobile_no()));
            context.startActivity(intent);

        }
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }


}