package com.entrophy;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.entrophy.helper.SharedPreference;
import com.entrophy.helper.VolleyMultipartRequest;
import com.entrophy.model.ImageUploadResult;
import com.entrophy.model.RegisterUser;
import com.google.gson.Gson;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class LetsConnect extends Activity implements View.OnClickListener{

    private Button lets_connect;
    private EditText bio,name;
    Boolean isConnected = false;
    private boolean _permissionResult;
    private String _selectedUploadMode = "";
    private int REQUEST_CAMERA = 101;
    private int REQUEST_GALLERY = 102;
    private ImageView profile_img;
    private ImageUploadResult imageUploadResult;
    private TextView plus_sign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lets_connect);
        init() ;
    }

    private void init() {
        lets_connect = (Button)findViewById(R.id.lets_connect);
        profile_img = (ImageView)findViewById(R.id.profile_img);
        bio = (EditText)findViewById(R.id.bio);
        name = (EditText)findViewById(R.id.name);
        plus_sign = (TextView) findViewById(R.id.plus_sign);
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
                if (!Constants.isValidString(name.getText().toString()) || name.getText().toString().length() < 4) {
                    new CustomToast().Show_Toast(LetsConnect.this, "Please enter valid name ", R.color.red);

                } else if (!Constants.isValidString(bio.getText().toString()) || bio.getText().toString().length() < 10) {
                    new CustomToast().Show_Toast(LetsConnect.this, "Please enter you bio information", R.color.red);

                } else if (!Constants.isValidString(SharedPreference.getString(LetsConnect.this,Constants.KEY_IMAGEID))) {
                    new CustomToast().Show_Toast(LetsConnect.this, "Please upload your image", R.color.red);

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

    }

    public void callAPI() {
        try {
            JSONObject params = new JSONObject();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("first_name",name.getText().toString());
            jsonObject.put("email","");
            jsonObject.put("mobile_no",getIntent().getStringExtra("phone_number"));
            jsonObject.put("bio",bio.getText().toString());
            jsonObject.put("image_id",SharedPreference.getString(LetsConnect.this,Constants.KEY_IMAGEID));
            jsonObject.put("file_name",SharedPreference.getString(LetsConnect.this,Constants.KEY_NAME));
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
                            if(data.getProfile_image() != null) {
                                SharedPreference.setString(LetsConnect.this, Constants.KEY_IMAGEPATH, data.getProfile_image().getOriginal_path());
                            }
                            System.out.println("LetsConnectPrint callAPI Response "+result+" status " +
                                    ""+SharedPreference.getInt(LetsConnect.this,Constants.KEY_USER_ID));

                            // SharedPreference.setBool(LetsConnect.this,Constants.KEY_IS_LOGGEDIN,true);
                            name.setText(data.getName());
                            bio.setText(data.getBio());
                            lets_connect.setBackground(getResources().getDrawable(R.drawable.rounded_button_green));
                            isConnected = true;
                            Constants.disableEditText(name,true);
                            Constants.disableEditText(bio,true);
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
            if (requestCode == REQUEST_GALLERY)
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
            else if (requestCode == REQUEST_CAMERA)
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
            String filename=picturePath.substring(picturePath.lastIndexOf("/")+1);
            Bitmap bmImg = BitmapFactory.decodeFile(picturePath);
            BitmapDrawable background = new BitmapDrawable(bmImg);
            plus_sign.setVisibility(View.GONE);
            UploadImage uploadImage = new UploadImage(bmImg,filename);
            uploadImage.execute();
    }

    private void onCaptureImageResult(Intent data) {

        Bitmap bmp = (Bitmap) data.getExtras().get("data");
        String file_name = ImageFilePath.getPath(this,getImageUri(bmp));
        file_name=file_name.substring(file_name.lastIndexOf("/")+1);

        UploadImage uploadImage = new UploadImage(bmp, file_name);
        uploadImage.execute();
       // saveAndUploadImage(bmp);
    }

    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public class UploadImage extends AsyncTask {

        private final Bitmap bitmap;
        private final String picturePath;

        public UploadImage(Bitmap bitmap, String picturePath){
            this.bitmap = bitmap;
            this.picturePath = picturePath;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Loader.show(LetsConnect.this);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            String upload_URL = Constants.SERVICE_URL+ Constants.PROFILEIMAGE;
            System.out.println("LetsConnectPrint onSelectFromGalleryResult upload_URL "+upload_URL);
            //final File sourceFile = new File(bitmap);

            RequestQueue rQueue;
            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            String resultResponse = new String(response.data);
                            try {
                                final JSONObject jsonObject = new JSONObject(resultResponse);
                                System.out.println("profile_path3233232 "+jsonObject.getString("profile_path"));
                                SharedPreference.setString(LetsConnect.this,Constants.KEY_IMAGEID,jsonObject.getString("image_id"));
                                SharedPreference.setString(LetsConnect.this,Constants.KEY_NAME,jsonObject.getString("file_name"));
                                LetsConnect.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Picasso.with(LetsConnect.this).load(jsonObject.getString("profile_path"))
                                                    .memoryPolicy(MemoryPolicy.NO_STORE)
                                                    .error(R.drawable.face_profile)
                                                    .placeholder(R.drawable.face_profile)
                                                    .into(profile_img);

                                        } catch (Exception e){

                                        }
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            System.out.println("LetsConnectPrint onSelectFromGalleryResult response "+ resultResponse);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("LetsConnectPrint onSelectFromGalleryResult error "+error.getMessage() );
                        }
                    }) {

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    System.out.println("LetsConnectPrint onSelectFromGalleryResult ");
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);

                    params.put("profile", new DataPart(picturePath,byteArrayOutputStream.toByteArray(), "image/jpeg"));

                    return params;
                }
            };


            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            rQueue = Volley.newRequestQueue(LetsConnect.this);
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
