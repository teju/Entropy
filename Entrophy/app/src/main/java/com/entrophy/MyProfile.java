package com.entrophy;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.entrophy.helper.ConstantStrings;
import com.entrophy.helper.Constants;
import com.entrophy.helper.CustomToast;
import com.entrophy.helper.ImageFilePath;
import com.entrophy.helper.Loader;
import com.entrophy.helper.Notify;
import com.entrophy.helper.ReportsPopup;
import com.entrophy.helper.SharedPreference;
import com.entrophy.helper.VolleyMultipartRequest;
import com.entrophy.model.LoginApiResult;
import com.entrophy.model.ProfileInfo;
import com.entrophy.model.SuccessREs;
import com.google.gson.Gson;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyProfile extends AppCompatActivity implements View.OnClickListener {

    private ImageView back_button;
    private LinearLayout friend_profile;
    private LinearLayout friends_profile_header;
    private Button done;
    private Dialog AlertDialog;

    private RelativeLayout work;
    LinearLayout work_ll,education_ll;
    private String _selectedUploadMode = "";
    private int REQUEST_CAMERA = 101;
    private int REQUEST_GALLERY = 102;
    private boolean _permissionResult;
    private ImageView profile_img;
    private EditText name,phone_number,bio,home,marital,kids,education;
    private TextView edit;
    private LayoutInflater mInflater;
    private ProfileInfo profileInfo;
    ArrayList<EditText> worklist = new ArrayList<EditText>();
    ArrayList<EditText> edulist = new ArrayList<EditText>();
    private ImageView work_add,edu_add;
    private boolean isMyProfile;
    private LinearLayout contact_ll,name_ll;
    private TextView friend_contact_number,friend_name;
    private ImageView call_phone,text_message_user,whatsapp_message_user;
    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    private LinearLayout block;
    public boolean isEdit = false;
    private TextView friend_description;
    private TextView updated_on;
    private LinearLayout report;
    private TextView report_text;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        initUI () ;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initUI() {
        isMyProfile = getIntent().getBooleanExtra("friends_profile",false);
        back_button = (ImageView)findViewById(R.id.back_button);
        call_phone = (ImageView)findViewById(R.id.call_phone);
        text_message_user = (ImageView)findViewById(R.id.text_message_user);
        whatsapp_message_user = (ImageView)findViewById(R.id.whatsapp_message_user);
        profile_img = (ImageView)findViewById(R.id.profile_img);
        work_add = (ImageView)findViewById(R.id.work_add);
        edu_add = (ImageView)findViewById(R.id.edu_add);
        edu_add.setOnClickListener(this);
        work_add.setOnClickListener(this);
        profile_img = (ImageView)findViewById(R.id.profile_img);
        profile_img.setOnClickListener(this);
        done = (Button)findViewById(R.id.done);
        work = (RelativeLayout)findViewById(R.id.work);
        block = (LinearLayout)findViewById(R.id.block);
        if(!isMyProfile) {
            whatsapp_message_user.setOnClickListener(this);
            call_phone.setOnClickListener(this);
            text_message_user.setOnClickListener(this);
            work.setOnClickListener(this);
            block.setOnClickListener(this);
        }
        work_ll = (LinearLayout)findViewById(R.id.work_ll);
        report = (LinearLayout)findViewById(R.id.report);
        report.setOnClickListener(this);
        education_ll = (LinearLayout)findViewById(R.id.education_ll);
        friend_profile = (LinearLayout)findViewById(R.id.friend_profile);
        friends_profile_header = (LinearLayout)findViewById(R.id.friends_profile_header);
        contact_ll = (LinearLayout)findViewById(R.id.contact_ll);
        name_ll = (LinearLayout)findViewById(R.id.name_ll);
        back_button.setOnClickListener(this);
        done.setOnClickListener(this);
        edit = (TextView)findViewById(R.id.edit);
        report_text = (TextView)findViewById(R.id.report_text);

        if(isMyProfile) {
            isEdit = false;
            friend_profile.setVisibility(View.GONE);
            friends_profile_header.setVisibility(View.GONE);
            name_ll.setVisibility(View.VISIBLE);
            contact_ll.setVisibility(View.VISIBLE);
            edit.setVisibility(View.VISIBLE);
            edu_add.setImageDrawable(getDrawable(R.drawable.plus));
            work_add.setImageDrawable(getDrawable(R.drawable.plus));
        } else {
            edu_add.setImageDrawable(getDrawable(R.drawable.chevron_right));
            work_add.setImageDrawable(getDrawable(R.drawable.chevron_right));
            edit.setVisibility(View.GONE);
            contact_ll.setVisibility(View.GONE);
            name_ll.setVisibility(View.GONE);
            friend_profile.setVisibility(View.VISIBLE);
            friends_profile_header.setVisibility(View.VISIBLE);
        }

        friend_contact_number = (TextView)findViewById(R.id.friend_contact_number);
        updated_on = (TextView)findViewById(R.id.updated_on);
        friend_description = (TextView)findViewById(R.id.friend_description);
        friend_name = (TextView)findViewById(R.id.friend_name);
        edit.setOnClickListener(this);
        name = (EditText)findViewById(R.id.name);
        phone_number = (EditText)findViewById(R.id.phone_number);
        bio = (EditText)findViewById(R.id.bio);
        home = (EditText)findViewById(R.id.home);
        marital = (EditText)findViewById(R.id.marital);
        kids = (EditText)findViewById(R.id.kids);

        mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public void sendSms(int smstype ) {

        System.out.println("sendSms1232112 "+SharedPreference.getString(MyProfile.this,Constants.KEY_TEXTMESSAGE));

        if(smstype == 1) {
            Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + profileInfo.getUserDetails().getMobile_no()));
            smsIntent.putExtra("sms_body", SharedPreference.getString(MyProfile.this,Constants.KEY_TEXTMESSAGE));
            try {
                startActivityForResult(smsIntent,1);
            } catch (android.content.ActivityNotFoundException ex) {
                new CustomToast().Show_Toast(MyProfile.this, "SMS faild, please try again later", R.color.red);

            }
        } else if(smstype == 2) {

            String smsNumber = profileInfo.getUserDetails().getMobile_no(); // E164 format without '+' sign
            if(!smsNumber.startsWith("91")) {
                smsNumber = "91"+smsNumber;
            }
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT,  SharedPreference.getString(MyProfile.this,Constants.KEY_TEXTMESSAGE));
            sendIntent.setPackage("com.whatsapp");
            sendIntent.putExtra("jid", smsNumber + "@s.whatsapp.net"); //phone number without "+" prefix

            startActivityForResult(Intent.createChooser(sendIntent, "Share via"),1);
        } else {

            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"+profileInfo.getUserDetails().getMobile_no()));
            startActivity(intent);

        }
    }
    private boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(MyProfile.this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case MAKE_CALL_PERMISSION_REQUEST_CODE :
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(MyProfile.this, "You can call the number by clicking on the button", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    public void disableText() {
        Constants.disableEditText(name,isMyProfile);
        Constants.disableEditText(phone_number,isMyProfile);
        Constants.disableEditText(bio,isMyProfile);
        Constants.disableEditText(home,isMyProfile);
        Constants.disableEditText(marital,isMyProfile);
        Constants.disableEditText(kids,isMyProfile);
        if(!isMyProfile) {
            work_add.setVisibility(View.VISIBLE);
            edu_add.setVisibility(View.VISIBLE);
        } else {
            work_add.setVisibility(View.GONE);
            edu_add.setVisibility(View.GONE);
        }

    }
    public void enableTexts() {
        Constants.enableEditText(name,true);
        Constants.enableEditText(phone_number,true);
        Constants.enableEditText(bio,true);
        Constants.enableEditText(home,true);
        Constants.enableEditText(marital,true);
        Constants.enableEditText(kids,true);
        work_add.setVisibility(View.VISIBLE);
        edu_add.setVisibility(View.VISIBLE);
        setUPWork();
        setUPEducation();
    }

    public void setUpUSerData() {
        disableText();
        name.setText(profileInfo.getUserDetails().getFirst_name());
        friend_name.setText(profileInfo.getUserDetails().getFirst_name());
        phone_number.setText(profileInfo.getUserDetails().getMobile_no());
        friend_contact_number.setText(profileInfo.getUserDetails().getMobile_no());
        bio.setText(profileInfo.getUserDetails().getBio());
        if(profileInfo.getUserDetails().getHome_location()  != null) {
            home.setText(profileInfo.getUserDetails().getHome_location());
        }
        marital.setText(profileInfo.getUserDetails().getMarital_status());
        kids.setText(profileInfo.getUserDetails().getNo_of_kids());
        if(profileInfo.getUserDetails().getEducation().size() == 0 ) {
            educationcnt = 1;
        } else {
            educationcnt = profileInfo.getUserDetails().getEducation().size();
        }
        if(profileInfo.getUserDetails().getWork().size() == 0 || !isMyProfile) {
            workcnt = 1;
        } else {
            workcnt = profileInfo.getUserDetails().getWork().size();
        }

        try {
            if(!isMyProfile) {
                StringBuilder stringBuilder = new StringBuilder();
                if(profileInfo.getUserDetails().getLocation().getLocation()!= null){
                    stringBuilder.append(profileInfo.getUserDetails().getLocation().getLocation());

                }
                if(profileInfo.getUserDetails().getLocation().getCity()!= null){
                    stringBuilder.append(", "+profileInfo.getUserDetails().getLocation().getCity());

                }
                if(profileInfo.getUserDetails().getLocation().getState()!= null){
                    stringBuilder.append(", "+profileInfo.getUserDetails().getLocation().getState());

                }
                if(profileInfo.getUserDetails().getLocation().getCountry()!= null){
                    stringBuilder.append(", "+profileInfo.getUserDetails().getLocation().getCountry());

                }
                Picasso.with(this).load(profileInfo.getUserDetails().getProfile_image().getProfile_path())
                        .memoryPolicy(MemoryPolicy.NO_STORE)
                        .into(profile_img);
                friend_description.setText(profileInfo.getUserDetails().getFirst_name()+" was in "+ stringBuilder.toString());
                updated_on.setText(profileInfo.getUserDetails().getLocation().getUpdated_on());
            } else {
                Picasso.with(this).load(SharedPreference.getString(MyProfile.this,Constants.KEY_IMAGEPATH))
                        .memoryPolicy(MemoryPolicy.NO_STORE)
                        .into(profile_img);
            }

        } catch (Exception e){

        }

        setUPWork();
        setUPEducation();
    }

    public void setUPWork() {
        work_ll.removeAllViews();

        for (int i = 0;i< workcnt;i++){
            final View view = mInflater.inflate(R.layout.edu_work_item, null, false);
            ImageView work_minus = (ImageView)view.findViewById(R.id.edu_minus);
            work_minus.setTag(i);
            final EditText mwork = (EditText)view.findViewById(R.id.mwork);
            try {
                mwork.setText(profileInfo.getUserDetails().getWork().get(i));
            } catch (Exception e){
            }
            if(i ==0) {
                work_minus.setVisibility(View.GONE);
                //work_minus.setImageDrawable(getResources().getDrawable(R.drawable.plus));
            } else  if(isEdit){
                work_minus.setImageDrawable(getResources().getDrawable(R.drawable.minus));
                work_minus.setVisibility(View.VISIBLE);
            } else{

                work_minus.setImageDrawable(getResources().getDrawable(R.drawable.minus));
                work_minus.setVisibility(View.GONE);

            }

            final int finalI = i;
            work_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((LinearLayout) view.getParent()).removeView(view);
                    worklist.remove(mwork);
                }
            });
            if(isEdit){
                Constants.enableEditText(mwork,false);
            } else if(isMyProfile) {
                Constants.disableEditText(mwork,isMyProfile);
            } else {
                Constants.disableEditText(mwork,true);

            }
            work_ll.addView(view);
            try {
                worklist.remove(i);
            } catch (Exception e){

            }
            worklist.add(i,mwork);

        }
    }


    public void addView(boolean isWork) {
        final View v = mInflater.inflate(R.layout.edu_work_item, null, false);
        final ImageView work_minus = (ImageView)v.findViewById(R.id.edu_minus);
        work_minus.setTag(worklist.size());
        final EditText mwork = (EditText)v.findViewById(R.id.mwork);

        work_minus.setImageDrawable(getResources().getDrawable(R.drawable.minus));
        work_minus.setVisibility(View.VISIBLE);

        work_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("work_minussetOnClickListener tview");

                    ((LinearLayout) v.getParent()).removeView(v);
                    worklist.remove(mwork);

            }
        });
        if(isWork) {
            work_ll.addView(v);

            worklist.add(mwork);
            System.out.println("work_minussetOnClickListener setVisibility");
        } else {
            education_ll.addView(v);
            edulist.add(mwork);
        }
//                    worklist.clear();
//                    setUPWork();

    }

    int educationcnt = 1;
    int workcnt = 1;

    public void setUPEducation() {
        education_ll.removeAllViews();
        for (int i = 0;i< educationcnt;i++){
            View view = mInflater.inflate(R.layout.edu_work_item, null, false);
            ImageView edu_minus = (ImageView)view.findViewById(R.id.edu_minus);
            EditText mwork = (EditText)view.findViewById(R.id.mwork);

            if(i ==0) {
                edu_minus.setVisibility(View.GONE);
            } else  if(isEdit){
                edu_minus.setVisibility(View.VISIBLE);
            } else{
                edu_minus.setVisibility(View.GONE);

            }
            if(isEdit){
                Constants.enableEditText(mwork,false);
            }else if(isMyProfile && !isEdit) {
                Constants.disableEditText(mwork,isMyProfile);
            } else {
                Constants.disableEditText(mwork,true);

            }
            try {
                mwork.setText(profileInfo.getUserDetails().getEducation().get(i));
            } catch (Exception e){

            }
            edu_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    educationcnt = educationcnt - 1;
                    edulist.clear();
                    setUPEducation();
                }
            });
            try {
                edulist.remove(i);
            }catch (Exception e){

            }
            edulist.add(i,mwork);
            education_ll.addView(view);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.back_button){
            super.onBackPressed();

        } else if(view.getId() == R.id.done){
            if(isMyProfile) {
                edit.setVisibility(View.VISIBLE);
                if (validate() && validateEduField() && validateWorkField()) {
                    updateUSerPRofile();
                }
            } else {
                super.onBackPressed();
            }

        }else if(view.getId() == R.id.work){
            Intent i = new Intent(this,WorkExperience.class);
            List<String> list = profileInfo.getUserDetails().getWork();

            i.putStringArrayListExtra("response",(ArrayList<String>) list);
            startActivity(i);

        }else if(view.getId() == R.id.profile_img && isMyProfile){
           uploadProfilePhoto();

        } else if(view.getId() == R.id.edit && isMyProfile){
            isEdit = true;
           edit.setVisibility(View.GONE);
           enableTexts();

        } else if(view.getId() == R.id.work_add && isMyProfile){
          addView(true);

        } else if(view.getId() == R.id.edu_add && isMyProfile){
          addView(false);

        } else if(view.getId() == R.id.text_message_user && !isMyProfile){
          sendSms(1);

        } else if(view.getId() == R.id.whatsapp_message_user && !isMyProfile){
            sendSms(2);

        }else if(view.getId() == R.id.block && !isMyProfile){
           ;
            Notify.show(this, "Are you sure you want to bloack " + profileInfo.getUserDetails().getFirst_name(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(i == dialogInterface.BUTTON_POSITIVE) {
                        callAPIBlock();

                    }
                }
            },"OK","CANCEL");

        }else if(view.getId() == R.id.call_phone && !isMyProfile){
            ActivityCompat.requestPermissions(MyProfile.this, new String[]
                    {android.Manifest.permission.CALL_PHONE}, MAKE_CALL_PERMISSION_REQUEST_CODE);
            sendSms(3);

        } else if(view.getId() == R.id.report) {
            List<String> strings = new ArrayList<>();
            strings.add("Not know");
            strings.add("I don’t want to continue");
            strings.add("my reason not listed");
            strings.add("Others");

            ReportsPopup.show(this, "Reports", new ReportsPopup.ReportsOnClickListener() {
                @Override
                public void onClick(String reason,String comment) {
                    System.out.println("ReportsPopup12 reason "+reason+" comment "+comment);
                    reportFriend(reason,comment);
                }
            },strings);
        }
    }

    private void uploadProfilePhoto() {
        Notify.show(this, "Upload Your Profile Photo.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                _permissionResult = Constants.checkPermission(MyProfile.this);
                if(which == DialogInterface.BUTTON_POSITIVE) {
                    chooseFromGallery();
                }
                else if(which == DialogInterface.BUTTON_NEGATIVE) {
                    takePhoto();
                }
                profile_img.setImageResource(0);

            }
        }, "Gallery", "Camera", true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isMyProfile = getIntent().getBooleanExtra("friends_profile",false);


        if(isMyProfile) {
            getUSerPRofile();

        } else {
            getFriendsPRofile();
        }
    }


    private static int RESULT_LOAD_IMAGE = 1;

    private void chooseFromGallery() {
        _selectedUploadMode = "GALLERY";
        if(_permissionResult) {
            Intent i = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(i, REQUEST_GALLERY);
        }
    }
    private void takePhoto() {
        _selectedUploadMode = "CAMERA";
        if(_permissionResult) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CAMERA);
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_GALLERY) {
                if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK && null != data) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    onSelectFromGalleryResult(data);


                }
            } else if (requestCode == REQUEST_CAMERA)
                    onCaptureImageResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();

        String filepath = picturePath.substring(picturePath.lastIndexOf("/")+1);

        Bitmap bmImg = BitmapFactory.decodeFile(picturePath);
        BitmapDrawable background = new BitmapDrawable(bmImg);
        UploadImage uploadImage = new UploadImage(bmImg, filepath);
        uploadImage.execute();
    }

    private void onCaptureImageResult(Intent data) {
        System.out.println("LetsConnectPrint onCaptureImageResult upload_URL ");

        Bitmap bmp = (Bitmap) data.getExtras().get("data");
        String file_name = ImageFilePath.getPath(this,getImageUri(bmp));
        file_name=file_name.substring(file_name.lastIndexOf("/")+1);
        UploadImage uploadImage = new UploadImage(bmp,file_name);
        uploadImage.execute();
        // saveAndUploadImage(bmp);
    }

    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void getUSerPRofile() {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", SharedPreference.getInt(this,Constants.KEY_USER_ID));
            jsonObject.put("access_token", SharedPreference.getString(this,Constants.KEY_ACCESS_TOKEN));
            Loader.show(this);

            Constants.invokeAPIEx(this,Constants.USERPROFILE,jsonObject.toString(),new Handler(new Handler.Callback() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public boolean handleMessage(Message message) {
                    Loader.hide();
                    if(message != null && message.obj != null) {
                        String result = (String) message.obj;
                        profileInfo = new Gson().fromJson(result, ProfileInfo.class);

                        if(profileInfo.getStatus().equalsIgnoreCase("success")) {
                            setUpUSerData();
                        } else {
                            if(profileInfo.getMessage() != null) {
                                new CustomToast().Show_Toast(MyProfile.this, profileInfo.getMessage(), R.color.red);
                            }  else {
                                new CustomToast().Show_Toast(MyProfile.this, ConstantStrings.common_msg,R.color.red);

                            }
                        }
                    } else {
                        new CustomToast().Show_Toast(MyProfile.this, ConstantStrings.common_msg,R.color.red);

                    }
                    return false;
                }
            }));
        } catch (Exception e){
            System.out.println("LetsConnectPrint callAPI Exception "+e.toString());
        }
    }

    public void callAPIBlock() {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", SharedPreference.getInt(this,Constants.KEY_USER_ID));
            jsonObject.put("access_token", SharedPreference.getString(this,Constants.KEY_ACCESS_TOKEN));
            jsonObject.put("status", "Block");
            jsonObject.put("freind_id", profileInfo.getUserDetails().getId());
            Loader.show(this);

            Constants.invokeAPIEx(this,Constants.BLOCKFRIENDREQUEST,jsonObject.toString(),new Handler(new Handler.Callback() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public boolean handleMessage(Message message) {
                    Loader.hide();
                    if(message != null && message.obj != null) {
                        String result = (String) message.obj;
                        LoginApiResult blockres = new Gson().fromJson(result, LoginApiResult.class);

                        if(blockres.getStatus().equalsIgnoreCase("success")) {
                            MyProfile.super.onBackPressed();
                        } else {
                            if(blockres.getMessage() != null) {
                                new CustomToast().Show_Toast(MyProfile.this, blockres.getMessage(), R.color.red);
                            }  else {
                                new CustomToast().Show_Toast(MyProfile.this, ConstantStrings.common_msg,R.color.red);

                            }
                        }
                    } else {
                        new CustomToast().Show_Toast(MyProfile.this, ConstantStrings.common_msg,R.color.red);

                    }
                    return false;
                }
            }));
        } catch (Exception e){
            System.out.println("LetsConnectPrint callAPI Exception "+e.toString());
        }
    }


    public boolean validate(){
        if(!Constants.isValidString(name.getText().toString())){
            new CustomToast().Show_Toast(MyProfile.this, "Enter valid name",R.color.red);
            return false;
        } else if(!Constants.isValidMobile(phone_number.getText().toString())){
            new CustomToast().Show_Toast(MyProfile.this, "Enter valid mobile number",R.color.red);
            return false;
        } else if(!Constants.isValidString(bio.getText().toString())){
            new CustomToast().Show_Toast(MyProfile.this, "Enter valid bio information",R.color.red);
            return false;
        } else if(!Constants.isValidString(home.getText().toString())){
            new CustomToast().Show_Toast(MyProfile.this, "Enter valid home address",R.color.red);
            return false;
        } else if(!Constants.isValidString(marital.getText().toString())){
            new CustomToast().Show_Toast(MyProfile.this, "Enter valid maritial",R.color.red);
            return false;
        } else if(!Constants.isValidString(kids.getText().toString())){
            new CustomToast().Show_Toast(MyProfile.this, "Enter valid number of kids",R.color.red);
            return false;
        } else {

        }
        return true;
    }

    public boolean validateWorkField() {
        for(int i=0; i<worklist.size(); i++) {
            String enterdText=worklist.get(i).getText().toString();
            if(!Constants.isValidString(enterdText)) {
                new CustomToast().Show_Toast(MyProfile.this, "Work field cannot be empty",R.color.red);
                return false;
            }
        }
        return true;
    }

    public boolean validateEduField() {
        for(int i=0; i<edulist.size(); i++) {
            String enterdText=edulist.get(i).getText().toString();
            if(!Constants.isValidString(enterdText)) {
                new CustomToast().Show_Toast(MyProfile.this, "Education field cannot be empty",R.color.red);
                return false;
            }
        }
        return true;
    }

    public JSONObject getuserjsonData(){
        JSONObject params = new JSONObject();

        try {
            params.put("first_name",name.getText().toString());
            params.put("bio",bio.getText().toString());
            params.put("mobile_no",phone_number.getText().toString());
            params.put("marital_status",marital.getText().toString());
            params.put("no_of_kids",kids.getText().toString());
            params.put("home_location",home.getText().toString());
        } catch (Exception e){

        }
        return params;
    }

    public JSONArray getworkjsonData(){
        JSONArray params = new JSONArray();
        for(int i=0; i<worklist.size(); i++) {
            String enterdText=worklist.get(i).getText().toString();

            try {
            params.put(enterdText);
            } catch (Exception e){

            }
        }
        return params;
    }

    public JSONArray getedujsonData(){
        JSONArray params = new JSONArray();

        for(int i=0; i<edulist.size(); i++) {
            String enterdText=edulist.get(i).getText().toString();
            params.put(enterdText);

            try {
            } catch (Exception e){

            }
        }
        return params;
    }


    public void updateUSerPRofile() {
        try {
            isEdit = false;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", SharedPreference.getInt(this,Constants.KEY_USER_ID));
            jsonObject.put("access_token", SharedPreference.getString(this,Constants.KEY_ACCESS_TOKEN));
            jsonObject.put("UpdateProfileForm", getuserjsonData());
            jsonObject.put("Work", getworkjsonData());
            jsonObject.put("Education", getedujsonData());
            Loader.show(this);

            Constants.invokeAPIEx(this,Constants.USERUPDATEPROFILE,jsonObject.toString(),new Handler(new Handler.Callback() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public boolean handleMessage(Message message) {
                    Loader.hide();
                    if(message != null && message.obj != null) {
                        String result = (String) message.obj;
                        LoginApiResult res = new Gson().fromJson(result, LoginApiResult.class);

                        if(res.getStatus().equalsIgnoreCase("success")) {
                            worklist.clear();
                            edulist.clear();
                            getUSerPRofile();
                        } else {
                            if(profileInfo.getMessage() != null) {
                                new CustomToast().Show_Toast(MyProfile.this, profileInfo.getMessage(), R.color.red);
                            }  else {
                                new CustomToast().Show_Toast(MyProfile.this, ConstantStrings.common_msg,R.color.red);

                            }
                        }
                    } else {
                        new CustomToast().Show_Toast(MyProfile.this, ConstantStrings.common_msg,R.color.red);

                    }
                    return false;
                }
            }));
        } catch (Exception e){
            System.out.println("LetsConnectPrint callAPI Exception "+e.toString());
        }
    }

    public void getFriendsPRofile() {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", SharedPreference.getInt(this,Constants.KEY_USER_ID));
            jsonObject.put("access_token", SharedPreference.getString(this,Constants.KEY_ACCESS_TOKEN));
            jsonObject.put("friend_id", getIntent().getStringExtra("friend_id"));

            Loader.show(this);

            Constants.invokeAPIEx(this,Constants.FRIENDPROFILEVIEW,jsonObject.toString(),new Handler(new Handler.Callback() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public boolean handleMessage(Message message) {
                    Loader.hide();
                    if(message != null && message.obj != null) {
                        String result = (String) message.obj;
                        profileInfo = new Gson().fromJson(result, ProfileInfo.class);

                        if(profileInfo.getStatus().equalsIgnoreCase("success")) {
                            setUpUSerData();

                        } else {
                            if(profileInfo.getMessage() != null) {
                                new CustomToast().Show_Toast(MyProfile.this, profileInfo.getMessage(), R.color.red);
                            }  else {
                                new CustomToast().Show_Toast(MyProfile.this, ConstantStrings.common_msg,R.color.red);

                            }
                        }
                    } else {
                        new CustomToast().Show_Toast(MyProfile.this, ConstantStrings.common_msg,R.color.red);

                    }
                    return false;
                }
            }));
        } catch (Exception e){
            System.out.println("LetsConnectPrint callAPI Exception "+e.toString());
        }
    }

    public void reportFriend(String reason, String comment) {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", SharedPreference.getInt(this,Constants.KEY_USER_ID));
            jsonObject.put("access_token", SharedPreference.getString(this,Constants.KEY_ACCESS_TOKEN));
            jsonObject.put("friend_id", getIntent().getStringExtra("friend_id"));
            jsonObject.put("reason", reason);
            jsonObject.put("comment", comment);

            Loader.show(this);

            Constants.invokeAPIEx(this,Constants.REPORTFRIEND,jsonObject.toString(),new Handler(new Handler.Callback() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public boolean handleMessage(Message message) {
                    Loader.hide();
                    if(message != null && message.obj != null) {
                        String result = (String) message.obj;
                        SuccessREs successREs = new Gson().fromJson(result, SuccessREs.class);

                        if(successREs.getStatus().equalsIgnoreCase("success")) {
                            new CustomToast().Show_Toast(MyProfile.this, successREs.getMessage(),R.color.red);


                        } else {
                            if(profileInfo.getMessage() != null) {
                                new CustomToast().Show_Toast(MyProfile.this, profileInfo.getMessage(), R.color.red);
                            }  else {
                                new CustomToast().Show_Toast(MyProfile.this, ConstantStrings.common_msg,R.color.red);

                            }
                        }
                    } else {
                        new CustomToast().Show_Toast(MyProfile.this, ConstantStrings.common_msg,R.color.red);

                    }
                    return false;
                }
            }));
        } catch (Exception e){
            System.out.println("LetsConnectPrint callAPI Exception "+e.toString());
        }
    }


    public class UploadImage extends AsyncTask {

        private final Bitmap bitmap;
        private final String file_name;

        public UploadImage(Bitmap bitmap, String file_name){
            this.bitmap = bitmap;
            this.file_name = file_name;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Loader.show(MyProfile.this);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            String upload_URL = Constants.SERVICE_URL+ Constants.UPDATEPROFILEIMAGE;
            System.out.println("LetsConnectPrint onSelectFromGalleryResult upload_URL "+upload_URL
                    +" "+file_name+" "+SharedPreference.getString(MyProfile.this, Constants.KEY_USER_ID));
            //final File sourceFile = new File(bitmap);

            RequestQueue rQueue;
            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            String resultResponse = new String(response.data);
                            try {
                                final JSONObject jsonObject = new JSONObject(resultResponse);
                                SharedPreference.clearKEy(MyProfile.this,Constants.KEY_IMAGEPATH);
                                SharedPreference.setString(MyProfile.this,Constants.KEY_IMAGEPATH,jsonObject.getString("original_path"));
                                System.out.println("LetsConnectPrint onSelectFromGalleryResult response " +
                                        ""+ resultResponse +" "+SharedPreference.getString(MyProfile.this,Constants.KEY_IMAGEPATH));
                                MyProfile.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Picasso.with(MyProfile.this).load(jsonObject.getString("original_path"))
                                                    .memoryPolicy(MemoryPolicy.NO_STORE)

                                                    .into(profile_img);
                                        }catch (Exception e){

                                        }
                                    }
                                });
                            } catch (JSONException e) {
                                System.out.println("LetsConnectPrint onSelectFromGalleryResult JSONException "+e.toString());
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("LetsConnectPrint onSelectFromGalleryResult error "+error.getMessage() );
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("user_id", String.valueOf(SharedPreference.getInt(MyProfile.this,Constants.KEY_USER_ID)));
                    params.put("access_token", SharedPreference.getString(MyProfile.this, Constants.KEY_ACCESS_TOKEN));
                    System.out.println("LetsConnectPrint onSelectFromGalleryResult upload_URL "+params.toString());

                    return params;
                }


                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    System.out.println("LetsConnectPrint onSelectFromGalleryResult ");
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                    params.put("profile", new DataPart(file_name,byteArrayOutputStream.toByteArray(), "image/jpeg"));

                    return params;
                }
            };


            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            rQueue = Volley.newRequestQueue(MyProfile.this);
            rQueue.add(volleyMultipartRequest);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Loader.hide();

        }
    }


}
