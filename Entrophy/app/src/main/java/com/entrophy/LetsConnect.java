package com.entrophy;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.entrophy.helper.ConstantStrings;
import com.entrophy.helper.Constants;
import com.entrophy.helper.CustomToast;
import com.entrophy.helper.ImageFilePath;
import com.entrophy.helper.Loader;
import com.entrophy.helper.Notify;
import com.entrophy.helper.SharedPreference;
import com.entrophy.model.ImageUploadResult;
import com.entrophy.model.RegisterUser;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class LetsConnect extends Activity implements View.OnClickListener{

    private Button lets_connect;
    private EditText bio,name;
    Boolean isConnected = false;
    private boolean _permissionResult;
    private String _selectedUploadMode = "";
    private int REQUEST_CAMERA = 101;
    private int REQUEST_GALLERY = 102;
    private LinearLayout profile_img;
    private ImageUploadResult imageUploadResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lets_connect);
        init() ;
    }

    private void init() {
        lets_connect = (Button)findViewById(R.id.lets_connect);
        profile_img = (LinearLayout)findViewById(R.id.profile_img);
        bio = (EditText)findViewById(R.id.bio);
        name = (EditText)findViewById(R.id.name);
        lets_connect.setOnClickListener(this);
        profile_img.setOnClickListener(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.lets_connect) {
            if(imageUploadResult != null) {
                System.out.println("LetsConnectPrint conClick " + imageUploadResult.getFile_name());
            }

            if (!isConnected) {
                if (!Constants.isValidString(name.getText().toString()) && name.getText().toString().length() < 4) {
                    new CustomToast().Show_Toast(LetsConnect.this, ConstantStrings.common_msg, R.color.red);

                } else if (!Constants.isValidString(bio.getText().toString()) && bio.getText().toString().length() < 10) {
                    new CustomToast().Show_Toast(LetsConnect.this, ConstantStrings.common_msg, R.color.red);

                } else {
                    callAPI();
                }
            } else {
                Intent intent = new Intent(this, Contacts.class);
                startActivity(intent);
            }
        } else if(view.getId() == R.id.profile_img) {
            uploadProfilePhoto();
        }
//        if(!isConnected) {
//            lets_connect.setBackground(getResources().getDrawable(R.drawable.rounded_button_green));
//            bio.setText("Professional Photographer & Blogger Working from StarBucks!");
//            name.setText("Prathap D K");
//            bio.setBackgroundResource(android.R.color.transparent);
//            name.setBackgroundResource(android.R.color.transparent);
//            isConnected = true;
//        } else {
//            Intent intent = new Intent(this,Contacts.class);
//            startActivity(intent);
//        }

    }

    public void callAPI() {
        try {
            JSONObject params = new JSONObject();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("first_name",name.getText().toString());
            jsonObject.put("email","");
            jsonObject.put("mobile_no",getIntent().getStringExtra("phone_number"));
            jsonObject.put("bio",bio.getText().toString());
            jsonObject.put("image_id","");
            jsonObject.put("file_name","");
            params.put("ApiSignupForm",jsonObject);
            Loader.show(this);

            Constants.invokeAPIEx(this,Constants.ENTROPYSIGNUP,params.toString(),new Handler(new Handler.Callback() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public boolean handleMessage(Message message) {
                    Loader.hide();
                    if(message != null && message.obj != null) {
                        String result = (String) message.obj;
                        RegisterUser data = new Gson().fromJson(result, RegisterUser.class);

                        if(data.getStatus().equalsIgnoreCase("success")) {
                            SharedPreference.setInt(LetsConnect.this,Constants.KEY_USER_ID,data.getUser_id());
                            SharedPreference.setString(LetsConnect.this,Constants.KEY_USER_NAME,data.getName());
                            SharedPreference.setString(LetsConnect.this,Constants.KEY_MOBILE_NO,data.getMobile_no());
                            SharedPreference.setString(LetsConnect.this,Constants.KEY_ACCESS_TOKEN,data.getAccess_token());
                            SharedPreference.setBool(LetsConnect.this,Constants.KEY_IS_LOGGEDIN,true);
                            System.out.println("LetsConnectPrint callAPI Response "+result+" status " +
                                    ""+SharedPreference.getInt(LetsConnect.this,Constants.KEY_USER_ID));

                            // SharedPreference.setBool(LetsConnect.this,Constants.KEY_IS_LOGGEDIN,true);
                            name.setText(data.getName());
                            bio.setText(data.getBio());
                            lets_connect.setBackground(getResources().getDrawable(R.drawable.rounded_button_green));
                            isConnected = true;
                            Constants.disableEditText(name);
                            Constants.disableEditText(bio);
//                            Intent intent = new Intent(LetsConnect.this, OTPVerification.class);
//                            intent.putExtra("phone_number",phone_number.getCode());
//                            startActivity(intent);
                        } else {
                            if(data.getErrors() != null) {
                                if(data.getErrors().getFirst_name().size() != 0) {
                                    new CustomToast().Show_Toast(LetsConnect.this, data.getErrors().getFirst_name().get(0),R.color.red);
                                } else if(data.getErrors().getMobile_no().size() != 0) {
                                    new CustomToast().Show_Toast(LetsConnect.this, data.getErrors().getMobile_no().get(0),R.color.red);

                                } else if(data.getErrors().getMobile_no().size() != 0) {
                                    new CustomToast().Show_Toast(LetsConnect.this, data.getErrors().getMobile_no().get(0),R.color.red);

                                }

                            } else {
                                new CustomToast().Show_Toast(LetsConnect.this, ConstantStrings.common_msg,R.color.red);

                            }
                        }
                    } else {
                        new CustomToast().Show_Toast(LetsConnect.this, ConstantStrings.common_msg,R.color.red);

                    }
                    return false;
                }
            }));
        } catch (Exception e){
            System.out.println("LetsConnectPrint callAPI Exception "+e.toString());
        }
    }

    private void uploadProfilePhoto() {
        Notify.show(this, "Upload Your Profile Photo.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                _permissionResult = Constants.checkPermission(LetsConnect.this);
                if(which == DialogInterface.BUTTON_POSITIVE) {
                    chooseFromGallery();
                }
                else if(which == DialogInterface.BUTTON_NEGATIVE) {
                    takePhoto();
                }
            }
        }, "Gallery", "Camera", true);
    }

    private void chooseFromGallery() {
        _selectedUploadMode = "GALLERY";
        if(_permissionResult) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_GALLERY);
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
            if (requestCode == REQUEST_GALLERY)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        String realPath = ImageFilePath.getPath(LetsConnect.this, data.getData());

        System.out.println("LetsConnectPrint onSelectFromGalleryResult "+realPath);
        UploadImage uploadImage = new UploadImage(realPath);
        uploadImage.execute();
        //Constants.uploadFile(this,realPath,ImageFilePath.fileName(realPath));
       // saveAndUploadImage(bmp);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap photo = (Bitmap) data.getExtras().get("data");
        Uri tempUri = getImageUri(getApplicationContext(), photo);

        String realPath = ImageFilePath.getPath(LetsConnect.this, tempUri);
        System.out.println("LetsConnectPrint onCaptureImageResult "+realPath);
        UploadImage uploadImage = new UploadImage(realPath);
        uploadImage.execute();
//        Bitmap bmp = (Bitmap) data.getExtras().get("data");
        //saveAndUploadImage(bmp);
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public class UploadImage extends AsyncTask {

        private final String url;

        public UploadImage(String url){
            this.url = url;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Loader.show(LetsConnect.this);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            String resp = Constants.uploadFile(LetsConnect.this,url,ImageFilePath.fileName(url));
            try {
                if (resp != null) {
                    new CustomToast().Show_Toast(LetsConnect.this, ConstantStrings.upload_success,R.color.red);
                    imageUploadResult = new Gson().fromJson(resp, ImageUploadResult.class);
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new CustomToast().Show_Toast(LetsConnect.this, ConstantStrings.upload_failed,R.color.red);

                        }
                    });
                }
            } catch (Exception e){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new CustomToast().Show_Toast(LetsConnect.this, ConstantStrings.upload_failed,R.color.red);

                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Loader.hide();
        }
    }
}
