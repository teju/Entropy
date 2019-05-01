package com.entrophy.helper;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.entrophy.R;

import java.util.ArrayList;
import java.util.List;


public class ReportsPopup {
    private static Dialog AlertDialog;
    static List<String> strings = new ArrayList<>();
    private static LinearLayout reports_list;
    private static LayoutInflater mInflater;
    public static ReportsOnClickListener callback;
    private static Context context;
    private static String selected_option = "";
    private static int checkedPos = -1;
    public static void show(final Context context, final String message, final ReportsOnClickListener
            callback, final List<String> items) {
            new Handler(context.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    if(ReportsPopup.AlertDialog != null) {
                        ReportsPopup.AlertDialog.dismiss();
                        ReportsPopup.AlertDialog = null;
            }
                mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    ReportsPopup.callback = callback;
                    ReportsPopup.context = context;

                ReportsPopup.AlertDialog = new Dialog(context);
                ReportsPopup.AlertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                ReportsPopup.AlertDialog.setCancelable(true);
                ReportsPopup.AlertDialog.setContentView(R.layout.view_select_dialog);
                reports_list = (LinearLayout) ReportsPopup.AlertDialog.findViewById(R.id.reports_list);
                setupPopup(mInflater,items);
                    Button btn_positive = (Button) ReportsPopup.AlertDialog.findViewById(R.id.btn_positive);
                    Button btn_negative = (Button) ReportsPopup.AlertDialog.findViewById(R.id.btn_negative);
                    final EditText comments = (EditText) ReportsPopup.AlertDialog.findViewById(R.id.comments);

                //callback.onClick(strings);
                    btn_positive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!Constants.isValidString(comments.getText().toString())) {
                                new CustomToast().Show_Toast(context, "Please enter a reason", R.color.red);
                            } else if(!Constants.isValidString(selected_option)) {
                                new CustomToast().Show_Toast(context, "Please select a reason", R.color.red);
                            } else {
                                callback.onClick(selected_option , comments.getText().toString());
                                hide();

                            }
                        }
                    });
                    btn_negative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            hide();
                        }
                    });

                ReportsPopup.AlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                ReportsPopup.AlertDialog.show();
                ReportsPopup.AlertDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });
    }

    public static void setupPopup(final LayoutInflater mInflater, final List<String> reports_list_items) {
        reports_list.removeAllViews();
        for (int i = 0;i< reports_list_items.size();i++){
            View view = mInflater.inflate(R.layout.view_select_dialog_item, null);
            view.setTag(i);
            view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 150));
            final TextView vw_text = (TextView)view.findViewById(R.id.vw_text);

            RadioButton rb_button = (RadioButton)view.findViewById(R.id.rb_button);
            rb_button.setTag(i);
            if(i == checkedPos ) {
                rb_button.setChecked(true);
            } else {
                rb_button.setChecked(false);

            }
            rb_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RadioButton view1 = ((RadioButton) v);
                    boolean  checked = ((RadioButton) v).isChecked();
                    if(checked) {
                        selected_option = reports_list_items.get((Integer)v.getTag());
                        checkedPos = (Integer)v.getTag();
                    } else {
                        checkedPos = -1;
                        selected_option = "";
                    }
                    setupPopup(mInflater,reports_list_items);
                }
            });
            vw_text.setText(reports_list_items.get(i));
            reports_list.addView(view);
        }

    }

    public static void hide() {
        if(ReportsPopup.AlertDialog != null) {
            new Handler(ReportsPopup.AlertDialog.getContext().getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    ReportsPopup.AlertDialog.dismiss();
                    ReportsPopup.AlertDialog = null;
                }
            });
        }
    }

    public interface ReportsOnClickListener {

        void onClick(String reason,String comment);


    }
}
