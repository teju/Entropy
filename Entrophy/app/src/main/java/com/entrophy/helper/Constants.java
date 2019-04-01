package com.entrophy.helper;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.entrophy.R;
import com.entrophy.model.ContactsDeo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tejaswini on 22/03/2019.
 */

public class Constants {
    public static int TabbedContactsRequest = 0;
    public static int GeneralContactsRequest = 1;
    public static int ListContactsRequest = 3;
    public static int TabbedContacts = 4;
    public static final String Login = "/api/web/user/login";
    public static final String OTP = "/api/web/user/otp";
    public static final String ENTROPYSIGNUP = "/api/web/user/entropy-signup";
    public static final String STEALTHMODE = "/api/web/profile/stealth-mode";
    public static final String PROFILEIMAGE = "/api/web/user/profile-image";
    public static final String SERVICE_URL = "https://entropy.bikerservice.in";
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 124;
    private static final int REQUEST_PERMISSION_CODE = 100;

    public static String KEY_USER_NAME = "KEY_USER_NAME";
    public static String KEY_USER_ID = "KEY_USER_ID";
    public static String KEY_MOBILE_NO = "KEY_MOBILE_NO";
    public static String KEY_BIO = "KEY_BIO";
    public static String KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN";
    public static String KEY_IS_LOGGEDIN = "KEY_IS_LOGGEDIN";
    public static String KEY_SEALTH_MODE = "KEY_SEALTH_MODE";
    private static String phNo = "";

    public static boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }
    public static boolean isValidOtp(String phone) {
        if(phone.length() >= 6) {
            return true;
        }
        return false;
    }
    public static boolean isValidString(String str) {

        if(str != null && !str.isEmpty()) {
            return true;
        }
        return false;
    }
    public static void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }

    public static boolean isNetworkAvailable(Context context) {
        boolean available = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        catch(Exception ex) {
        }
        return available;
    }

    public static void invokeAPIEx(final Context context,String url, final String postString,
                            final Handler callback) {

        RequestQueue queue = Volley.newRequestQueue(context);

        final String mRequestBody = postString;
        System.out.println("SYSTEMPRINT URL "+SERVICE_URL+url+" PARAMS" +mRequestBody+" ");
        if(Constants.isNetworkAvailable(context)) {

            StringRequest strReq = new StringRequest(Request.Method.POST, SERVICE_URL+url,
                    new Response.Listener<String>() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onResponse(final String response) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        System.out.println("SYSTEMPRINT onResponse "+response);

                                        if (callback != null) {
                                            callback.sendMessage(callback.obtainMessage(0, response));
                                        }
                                    }
                                }).start();


                            try {
                                System.gc();
                                Runtime.getRuntime().gc();
                            } catch (Exception e) {

                                e.printStackTrace();
                                System.out.println("SYSTEMPRINT postsync " + "  Exception  "+ e.toString());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onErrorResponse(final VolleyError error) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    System.out.println("SYSTEMPRINT onErrorResponse "+error);

                                    if (callback != null) {
                                        callback.sendMessage(callback.obtainMessage(0, error.toString()));
                                    }
                                }
                            }).start();
                            new CustomToast().Show_Toast(context, ConstantStrings.common_msg, R.color.red);

                        }
                    }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {

                        System.out.println("SYSTEMPRINT error  Unsupported Encoding while trying to get " +
                                "the bytes of %s using %s"+ mRequestBody+"utf-8");

                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s " +
                                "using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }

            };
            strReq.setRetryPolicy(new DefaultRetryPolicy(
                    60000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(strReq);
        } else {
            new CustomToast().Show_Toast(context, ConstantStrings.no_internet,R.color.red);
        }
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    alertBuilder.create().show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkContactsPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                        }
                    });
                    alertBuilder.create().show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static String uploadFile(Context context,String sourceFileUri,String file_name) {


        String fileName = sourceFileUri;
        int serverResponseCode = 0;
        String serverResponse = "";
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);
        System.out.println("LetsConnectPrint onSelectFromGalleryResult sourceFile "+!sourceFile.isFile());

        if (!sourceFile.isFile()) {
            return null;

        }
        else
        {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(Constants.SERVICE_URL+Constants.PROFILEIMAGE);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("profile", file_name);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
               // dos.writeBytes("Content-Disposition: form-data; name="+uploaded_file+";filename="" + fileName + """ + lineEnd);

                        dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
                StringBuilder response  = new StringBuilder();

                if(serverResponseCode == 200){
                    BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()),8192);
                    String strLine = null;
                    while ((strLine = input.readLine()) != null)
                    {
                        response.append(strLine);
                    }
                    input.close();
                    System.out.println("LetsConnectPrint serverResponseMessage  "+ response.toString()
                            +" serverResponseCode "+serverResponseCode);
                    serverResponse = response.toString();
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();
                return  serverResponse;
            } catch (MalformedURLException ex) {
                System.out.println("LetsConnectPrint MalformedURLException  "+ ex.toString());

                ex.printStackTrace();


                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {
                System.out.println("LetsConnectPrint Exception  "+ e.toString());

                e.printStackTrace();


            }
            return serverResponse;

        } // End else block
    }
    public static List<ContactsDeo> getContact(Context context) {
        DataBaseHelper db = new DataBaseHelper(context);

        List<ContactsDeo> contactList = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = ContactsContract.Contacts.CONTENT_URI;

        String[] projection = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.PhoneLookup._ID,
                ContactsContract.Contacts.HAS_PHONE_NUMBER
        };

        Cursor cursor = contentResolver.query(
                uri,
                null,
                ContactsContract.Contacts.HAS_PHONE_NUMBER + " > ?",
                new String[]{String.valueOf(0)},
                ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        );

        int totalContactsCount = cursor.getCount();
        System.out.println("Contact1234 totalContactsCount "+totalContactsCount);
        if (cursor != null && cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {

                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                            new String[]{id},
                            null
                    );
                    ContactsDeo c =new ContactsDeo();
                    c.setName(name);
                    c.setContact_id(id);
                    String image_uri = "";
                    if (phoneCursor != null && phoneCursor.getCount() > 0) {

                        while (phoneCursor.moveToNext()) {
                            String phId = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));

                            String customLabel = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL));

                            String label = (String) ContactsContract.CommonDataKinds.Phone.getTypeLabel(context.getResources(),
                                    phoneCursor.getInt(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)),
                                    customLabel
                            );

                            phNo = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            phNo = phNo.replaceAll("\\+","");
                            c.setPhoneNumber(phNo);
                            image_uri = phoneCursor.getString(cursor.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                            if(image_uri == null) {
                                image_uri = "";
                            }
                        }
                        phoneCursor.close();
                    }


                    db.putContacts(db,name,phNo,"false",image_uri);
                    contactList.add(c);


                }

            }
            cursor.close();
        }
        return contactList;
    }

    public void checkPermissions(Activity context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissionList = new ArrayList<>();
            permissionList.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            permissionList.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
            permissionList.add(android.Manifest.permission.READ_CONTACTS);
            permissionList.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (Constants.checkRuntimePermission(context, permissionList, REQUEST_PERMISSION_CODE)) {
            }
        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    public static boolean checkRuntimePermission(Activity activity, List<String> permissionNeededList, int requestCode) {
        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        for (String permission : permissionNeededList) {
            if (!addPermission(activity, permissionsList, permission))
                permissionsNeeded.add(permission);
        }
        if (permissionsList.size() > 0) {
            activity.requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), requestCode);
            return false;
        } else
            return true;
    }
    @TargetApi(Build.VERSION_CODES.M)
    public static boolean addPermission(Activity activity, List<String> permissionsList, String permission) {
        if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!activity.shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }
}
