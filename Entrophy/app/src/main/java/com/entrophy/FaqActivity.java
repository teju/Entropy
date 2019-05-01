package com.entrophy;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;

import com.entrophy.adapters.ExpandableListAdapter;
import com.entrophy.helper.ConstantStrings;
import com.entrophy.helper.Constants;
import com.entrophy.helper.CustomToast;
import com.entrophy.helper.Loader;
import com.entrophy.model.FaqResult;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FaqActivity extends AppCompatActivity {

    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private FaqResult data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        init();
    }

    private void init() {
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        callAPIFaq();

    }

    public void callAPIFaq() {
        try {

            Loader.show(this);

            Constants.invokeAPIEx(this,Constants.FAQ,"",new Handler(new Handler.Callback() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public boolean handleMessage(Message message) {
                    Loader.hide();
                    if(message != null && message.obj != null) {
                        String result = (String) message.obj;
                        try {
                            data = new Gson().fromJson(result, FaqResult.class);
                            if(data.getStatus().equalsIgnoreCase("success")) {
                                makeData();
                            }
                        } catch (Exception e){

                        }

                    } else {
                        new CustomToast().Show_Toast(FaqActivity.this, ConstantStrings.common_msg,R.color.red);

                    }
                    return false;
                }
            }));
        } catch (Exception e){
            System.out.println("LetsConnectPrint callAPI Exception "+e.toString());
        }
    }

    public void makeData(){
        final HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        for (int i =0;i<data.getFaq_list().size();i++){
            List<String> technology = new ArrayList<String>();
            technology.add(data.getFaq_list().get(i).getAnswer());
            expandableListDetail.put(data.getFaq_list().get(i).getQuestion(),technology);
        }
        final ArrayList expandableListTitle = new ArrayList(expandableListDetail.keySet());
        expandableListAdapter = new ExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {


            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {


            }
        });

    }

}
