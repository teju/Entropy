package com.entrophy.helper;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.text.InputType;
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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tejaswini on 22/03/2019.
 */

public class Constants {
    public static int TabbedContactsRequest = 0;
    public static int ListContactsRequest = 3;
    public static int ConnectionsContacts = 5;
    public static int HomeConnectionsContacts = 1;
    public static int HomeInviteFriendList = 2;
    public static int InviteContacts = 6;


    public static final String Login = "/api/web/user/login";
    public static final String OTP = "/api/web/user/otp";
    public static final String ENTROPYSIGNUP = "/api/web/user/entropy-signup";
    public static final String STEALTHMODE = "/api/web/profile/stealth-mode";
    public static final String PROFILEIMAGE = "/api/web/user/profile-image";
    public static final String VALIDATECONTACTS = "/api/web/profile/validate-contacts";
    public static final String FRIENDREQUEST = "/api/web/profile/friend-request";
    public static final String FRIENDREQUESTLIST = "/api/web/profile/friend-request-list";
    public static final String ACCEPTFRIENDREQUEST = "/api/web/profile/accept-friend-request";
    public static final String PUSHNOTIFICATION = "/api/web/user/push-notification";
    public static final String LOCATIONUPDATE = "/api/web/profile/location-update";
    public static final String UPDATEPROFILEIMAGE = "/api/web/profile/update-profile-image";
    public static final String USERPROFILE = "/api/web/profile/user-profile";
    public static final String USERUPDATEPROFILE = "/api/web/profile/user-update-profile";
    public static final String FRIENDLIST = "/api/web/profile/friend-list";
    public static final String FRIENDPROFILEVIEW = "/api/web/profile/friend-profile-view";
    public static final String BLOCKFRIENDREQUEST = "/api/web/profile/block-friend-request";
    public static final String CUSTOMERTERMSCONDITION = "/api/web/user/customer-terms-condition";
    public static final String FAQ = "/api/web/user/faq";
    public static final String REPORTFRIEND = "/api/web/profile/report-friend";
    public static final String USERDASHBOARDCOUNT = "/api/web/profile/user-dashboard-count";
    public static final String LOCATIONFRIENDCOUNT = "/api/web/profile/location-friend-count";
    public static final String LOCATIONFRIENDLIST = "/api/web/profile/location-friend-list";
    public static final String PUSHNOTIFFICATION = "/api/web/profile/push-notiffication";
    public static final String PUSHNOTIFFICATIONUPDATE = "/api/web/profile/push-notiffication-update";
    public static final String SERVICE_URL = "https://entropy.bikerservice.in";


    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 124;
    private static final int REQUEST_PERMISSION_CODE = 100;

    public static String KEY_USER_NAME = "KEY_USER_NAME";
    public static String KEY_USER_ID = "KEY_USER_ID";
    public static String KEY_MOBILE_NO = "KEY_MOBILE_NO";
    public static String KEY_BIO = "KEY_BIO";
    public static String KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN";
    public static String KEY_FIREBASEID= "KEY_FIREBASEID";
    public static String KEY_IS_LOGGEDIN = "KEY_IS_LOGGEDIN";
    public static String KEY_SEALTH_MODE = "KEY_SEALTH_MODE";
    public static String KEY_TEXTMESSAGE = "KEY_TEXTMESSAGE";
    public static String KEY_IMAGEID = "KEY_IMAGEID";
    public static String KEY_NAME = "KEY_NAME";
    public static String KEY_IMAGEPATH = "KEY_IMAGEPATH";
    public static String KEY_NOTIFICATION_COUNT = "KEY_NOTIFICATION_COUNT";
    public static String KEY_FRIEND_COUNT = "KEY_FRIEND_COUNT";
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

    public static void disableEditText(EditText editText,boolean drawable) {
        editText.setEnabled(false);
        editText.setFocusable(false);

        editText.setBackgroundColor(Color.TRANSPARENT);
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

    }


    public static void enableEditText(EditText editText,boolean drawable) {
        editText.setEnabled(true);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        if(drawable) {
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.lead_pencil, 0);
        }
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
        int request = Request.Method.POST;

        if(postString.length()!= 0) {
            request = Request.Method.POST;

        } else {
            request = Request.Method.GET;

        }
        final String mRequestBody = postString;
        System.out.println("SYSTEMPRINT URL "+SERVICE_URL+url+" PARAMS" +mRequestBody+" ");
        if(Constants.isNetworkAvailable(context)) {

            StringRequest strReq = new StringRequest(request, SERVICE_URL+url,
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
            if(url.contains("location-update")) {

            } else if(url.contains("user-dashboard-count")) {

            } else {
                new CustomToast().Show_Toast(context, ConstantStrings.no_internet, R.color.red);
            }
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
                            phNo = phNo.trim().replaceAll("-","").
                                    replaceAll("\\s","").replaceAll("\"[-+.^:,]\",\"\"","");
                            if( phNo.startsWith("91")) {
                                phNo = phNo.replaceFirst("91","");
                            }
                            phNo = phNo.replaceAll("\\D+","");
                            c.setPhoneNumber(phNo);
                            image_uri = phoneCursor.getString(cursor.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                            if(image_uri == null) {
                                image_uri = "";
                            }
                        }
                        phoneCursor.close();
                    }


                    db.putAllContacts(db,name,phNo,"false",image_uri);
                    contactList.add(c);


                }

            }
            cursor.close();
        }
        return contactList;
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

    public static void startLocationService(Context context) {
        Intent insertLocation = new Intent(context, InsertLocationServer.class);
        AlarmManager insertLocationalarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent insertLocationalarmMgrpendingIntent = PendingIntent.getBroadcast(context, 0,
                insertLocation, PendingIntent.FLAG_CANCEL_CURRENT);
            insertLocationalarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 10000, insertLocationalarmMgrpendingIntent);
        System.out.println("InsertLocationServer startLocationService " );

    }

    public static void starGPSService(Context context) {
        Intent insertLocation = new Intent(context, GPSStatusToServer.class);
        AlarmManager insertLocationalarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent insertLocationalarmMgrpendingIntent = PendingIntent.getBroadcast(context, 0,
                insertLocation, PendingIntent.FLAG_CANCEL_CURRENT);
        insertLocationalarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(), 10000, insertLocationalarmMgrpendingIntent);
        System.out.println("starGPSService startLocationService " );

    }


}
