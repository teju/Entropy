package com.entrophy.adapters;

/**
 * Created by Khushvinders on 15-Nov-16.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.entrophy.R;
import com.entrophy.TabbedContacts;
import com.entrophy.helper.ConstantStrings;
import com.entrophy.helper.Constants;
import com.entrophy.helper.CustomToast;
import com.entrophy.helper.DataBaseHelper;
import com.entrophy.helper.Loader;
import com.entrophy.helper.SharedPreference;
import com.entrophy.model.NotiModel;
import com.entrophy.model.SuccessREs;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.MyViewHolder> {
    private final Context context;
    private final DataBaseHelper db;

    private List<NotiModel> contactsList = new ArrayList<>();



    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView right_arrow;
        private final RelativeLayout viewroot;
        private final LinearLayout request;
        private final ImageView friend_image;
        private final Button accept,remove;
        private final TextView address;
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
            accept = (Button) view.findViewById(R.id.accept);
            remove = (Button) view.findViewById(R.id.remove);
            address = (TextView) view.findViewById(R.id.address);

            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    System.out.println("onCheckedChanged  boolean " +b+" "+getAdapterPosition());

                }
            });
        }
    }

    public NotificationsAdapter(Context context, List<NotiModel> contactsList) {
        this.contactsList = contactsList;
        this.context = context;

        db = new DataBaseHelper(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contacts_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final NotiModel contactsDeo = contactsList.get(position);
        holder.viewroot.setTag(position);
        holder.checkbox.setTag(position);
        holder.accept.setTag(position);
        holder.remove.setTag(position);
        holder.checkbox.setVisibility(View.GONE);
        holder.accept.setVisibility(View.GONE);
        holder.remove.setVisibility(View.GONE);
        holder.address.setVisibility(View.VISIBLE);
        holder.right_arrow.setVisibility(View.VISIBLE);
        holder.name.setText(contactsDeo.getTitle());
        holder.address.setText(contactsDeo.getBody());
        holder.viewroot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i =(Integer)view.getTag();
               callAPIUpdateNotificationCount(contactsList.get(i).getNotification_id());
            }
        });

    }


    public void callAPIUpdateNotificationCount(String noti_id) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", SharedPreference.getInt(context,Constants.KEY_USER_ID));
            jsonObject.put("access_token", SharedPreference.getString(context,Constants.KEY_ACCESS_TOKEN));
            jsonObject.put("notification_id",noti_id);
            Loader.show(context);
            Constants.invokeAPIEx(context,Constants.PUSHNOTIFFICATIONUPDATE,jsonObject.toString(),
                    new Handler(new Handler.Callback() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public boolean handleMessage(Message message) {
                    Loader.hide();
                    if(message != null && message.obj != null) {
                        String result = (String) message.obj;
                        SuccessREs data = new Gson().fromJson(result, SuccessREs.class);

                        if(data.getStatus().equalsIgnoreCase("success")) {
                            Intent i = new Intent(context, TabbedContacts.class);
                            context.startActivity(i);
                            new CustomToast().Show_Toast(context, data.getMessage(),R.color.red);

                        } else if(data.getMessage() != null) {
                            new CustomToast().Show_Toast(context, data.getMessage(),R.color.red);

                        }
                    } else {
                        new CustomToast().Show_Toast(context, ConstantStrings.common_msg,R.color.red);
                    }
                    return false;
                }
            }));
        } catch (Exception e){
            System.out.println("LetsConnectPrint callAPI Exception "+e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }


}