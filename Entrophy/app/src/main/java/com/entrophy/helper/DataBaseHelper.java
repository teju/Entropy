package com.entrophy.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.entrophy.model.ContactsDeo;
import com.entrophy.model.MatchedContacts;

import java.util.ArrayList;
import java.util.List;


public class DataBaseHelper extends SQLiteOpenHelper {

    private static  int database_version  = 7;
    private final Context context;
    public String Contacts =" CREATE TABLE `Contacts` (Id INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "User_Name TEXT, User_Phone TEXT, User_Selected TEXT, Image TEXT); ";
    public String ConnectedContacts =" CREATE TABLE `ConnectedContacts` (Id INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "User_Name TEXT, User_Phone TEXT, User_Selected TEXT, Image TEXT,USer_id TEXT); ";
    public String AddedContacts =" CREATE TABLE `AddedContacts` (Id INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "User_Name TEXT, User_Phone TEXT, User_Selected TEXT, Image TEXT,Contact_id TEXT); ";

    public String HomeConnect =" CREATE TABLE `HomeConnect` (Id INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "freind_id TEXT, first_name TEXT, profile_photo TEXT, city TEXT,state TEXT,country TEXT,gps_status TEXT); ";

    public String HomeConnectBirdEye =" CREATE TABLE `HomeConnectBirdEye` (Id INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "friend_cnt TEXT, city TEXT,state TEXT,country TEXT,gps_status TEXT,location TEXT); ";

    private static final String DELETE_Contacts = "DROP TABLE IF EXISTS Contacts" ;
    private static final String DELETE_AddedContacts = "DROP TABLE IF EXISTS AddedContacts" ;
    private static final String DELETE_ConnectedContacts = "DROP TABLE IF EXISTS ConnectedContacts" ;
    private static final String DELETE_HomeConnect = "DROP TABLE IF EXISTS HomeConnect" ;
    private static final String DELETE_HomeConnectBirdEye = "DROP TABLE IF EXISTS HomeConnectBirdEye" ;

    public DataBaseHelper(Context context) {
        super(context, "entrophy_db",null, database_version);
        this.context=context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Contacts);
        db.execSQL(AddedContacts);
        db.execSQL(ConnectedContacts);
        db.execSQL(HomeConnect);
        db.execSQL(HomeConnectBirdEye);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_Contacts);
        db.execSQL(DELETE_AddedContacts);
        db.execSQL(DELETE_ConnectedContacts);
        db.execSQL(DELETE_HomeConnect);
        db.execSQL(DELETE_HomeConnectBirdEye);
        onCreate(db);
    }


    public boolean putAllContacts(DataBaseHelper dbh, String User_Name,
                               String User_Phone, String User_Selected, String image){
        SQLiteDatabase sq=dbh.getWritableDatabase();
        System.out.println("DataBaseHelper123 To_Node_ID " + image);

        ContentValues cv = new ContentValues();
        cv.put("User_Name", User_Name);
        cv.put("User_Phone", User_Phone);
        cv.put("User_Selected", User_Selected);
        cv.put("Image", image);
        sq.insert("Contacts", null, cv);
        return true;

    }
    public boolean putHomeContacts(DataBaseHelper dbh, String freind_id,
                               String first_name, String profile_photo, String city,String state,
                                   String country,String gps_status){
        SQLiteDatabase sq=dbh.getWritableDatabase();
        System.out.println("DataBaseHelper123 putHomeContacts freind_id"
                + first_name);

        ContentValues cv = new ContentValues();
        cv.put("freind_id", freind_id);
        cv.put("first_name", first_name);
        cv.put("profile_photo", profile_photo);
        cv.put("city", city);
        cv.put("state", state);
        cv.put("country", country);
        cv.put("gps_status", gps_status);
        sq.insert("HomeConnect", null, cv);
        return true;

    }

    public boolean putHomeContactsBirdEye(DataBaseHelper dbh,
                                   String friend_cnt,  String city,String state,
                                   String country,String gps_status,String location){
        SQLiteDatabase sq=dbh.getWritableDatabase();
        System.out.println("DataBaseHelper123 putHomeContactsBirdEye freind_id"
                + friend_cnt);

        ContentValues cv = new ContentValues();
        cv.put("friend_cnt", friend_cnt);
        cv.put("city", city);
        cv.put("state", state);
        cv.put("country", country);
        cv.put("gps_status", gps_status);
        cv.put("location", location);
        sq.insert("HomeConnectBirdEye", null, cv);
        return true;

    }

    public boolean putConnectionContacts(DataBaseHelper dbh, String User_Name,
                                  String User_Phone, String User_Selected, String image, String USer_id){
        SQLiteDatabase sq=dbh.getWritableDatabase();
        System.out.println("DataBaseHelper123 To_Node_ID " + image);

        ContentValues cv = new ContentValues();
        cv.put("User_Name", User_Name);
        cv.put("User_Phone", User_Phone);
        cv.put("User_Selected", User_Selected);
        cv.put("Image", image);
        cv.put("USer_id", USer_id);
        sq.insert("ConnectedContacts", null, cv);
        return true;

    }


    public boolean putAddedContacts(DataBaseHelper dbh, String User_Name,
                               String User_Phone, String User_Selected, String image,String Contact_id ){
        SQLiteDatabase sq=dbh.getWritableDatabase();
        System.out.println("DataBaseHelper123 To_Node_ID " + Contact_id);

        ContentValues cv = new ContentValues();
        cv.put("User_Name", User_Name);
        cv.put("User_Phone", User_Phone);
        cv.put("User_Selected", User_Selected);
        cv.put("Image", image);
        cv.put("Contact_id", Contact_id);
        sq.insert("AddedContacts", null, cv);
        return true;

    }


    public List<ContactsDeo> getAllContacts(String where) {
        List<ContactsDeo> dataListList = new ArrayList<ContactsDeo>();

        String selectQuery = "SELECT * FROM Contacts "+where ;

        SQLiteDatabase db = this.getWritableDatabase();
        System.out.println("DataBaseHelper123 getAllContacts " + selectQuery);

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
               ContactsDeo contactsDeo = new ContactsDeo();
                contactsDeo.setContact_id(cursor.getString(0));
                contactsDeo.setName(cursor.getString(1));
                contactsDeo.setPhoneNumber(cursor.getString(2));
                contactsDeo.setIsselected(cursor.getString(3));
                contactsDeo.setImage(cursor.getString(3));
                dataListList.add(contactsDeo);
            } while (cursor.moveToNext());
        }
        return dataListList;
    }

    public List<MatchedContacts> getConnectedContacts() {
        List<MatchedContacts> dataListList = new ArrayList<MatchedContacts>();
        String selectQuery = "SELECT * FROM ConnectedContacts " ;

        SQLiteDatabase db = this.getWritableDatabase();
        System.out.println("DataBaseHelper123 getDateRate " + selectQuery);

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MatchedContacts contactsDeo = new MatchedContacts();
                contactsDeo.setContact_id(cursor.getString(5));
                contactsDeo.setMobile_no(cursor.getString(2));
                contactsDeo.setName(cursor.getString(1));
                contactsDeo.setIsselected(cursor.getString(3));
                contactsDeo.setImage(cursor.getString(4));
                contactsDeo.setID(cursor.getString(0));
                System.out.println("DataBaseHelper123 getDateRate setContact_id "
                        + cursor.getString(5)+" setMobile_no "+cursor.getString(2)+
                        " setIsselected "+cursor.getString(3) +" ID "+cursor.getString(0));

                dataListList.add(contactsDeo);
            } while (cursor.moveToNext());
        }
        return dataListList;
    }

    public List<MatchedContacts> getHomeConnectedContacts() {
        List<MatchedContacts> dataListList = new ArrayList<MatchedContacts>();
        String selectQuery = "SELECT * FROM HomeConnect " ;

        SQLiteDatabase db = this.getWritableDatabase();
        System.out.println("DataBaseHelper123 getHomeConnectedContacts " + selectQuery);

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MatchedContacts contactsDeo = new MatchedContacts();
                contactsDeo.setID(cursor.getString(0));
                contactsDeo.setFreind_id(cursor.getString(1));
                contactsDeo.setName(cursor.getString(2));
                contactsDeo.setImage(cursor.getString(3));
                contactsDeo.setCity(cursor.getString(4));
                contactsDeo.setState(cursor.getString(5));
                contactsDeo.setCountry(cursor.getString(6));
                contactsDeo.setGps(cursor.getString(7));
                System.out.println("DataBaseHelper123 getHomeConnectedContacts setFreind_id "
                        + cursor.getString(1)+" setName "+cursor.getString(2)+
                        " setImage "+cursor.getString(3) +" setCity "+cursor.getString(4) +
                        " setState "+cursor.getString(5)+" setCountry "+cursor.getString(6));

                dataListList.add(contactsDeo);
            } while (cursor.moveToNext());
        }
        return dataListList;
    }

    public List<MatchedContacts> getHomeConnectedBiredEyeContacts() {
        List<MatchedContacts> dataListList = new ArrayList<MatchedContacts>();
        String selectQuery = "SELECT * FROM HomeConnectBirdEye " ;

        SQLiteDatabase db = this.getWritableDatabase();
        System.out.println("DataBaseHelper123 getHomeConnectedBiredEyeContacts " + selectQuery);

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MatchedContacts contactsDeo = new MatchedContacts();
                contactsDeo.setID(cursor.getString(0));
                contactsDeo.setFriend_cnt(cursor.getString(1));
                contactsDeo.setCity(cursor.getString(2));
                contactsDeo.setState(cursor.getString(3));
                contactsDeo.setCountry(cursor.getString(4));
                contactsDeo.setGps(cursor.getString(5));
                contactsDeo.setLocation(cursor.getString(6));
                System.out.println("DataBaseHelper123 getHomeConnectedBiredEyeContacts setFreind_id "
                        + cursor.getString(1)+" setName "+cursor.getString(2)+
                        " setImage "+cursor.getString(3) +" setCity "+cursor.getString(4) +
                        " setState ");

                dataListList.add(contactsDeo);
            } while (cursor.moveToNext());
        }
        return dataListList;
    }


    public List<ContactsDeo> getAddedContacts() {
        List<ContactsDeo> dataListList = new ArrayList<ContactsDeo>();
        String selectQuery = "SELECT * FROM AddedContacts " ;

        SQLiteDatabase db = this.getWritableDatabase();
        System.out.println("DataBaseHelper123 getDateRate " + selectQuery);

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ContactsDeo contactsDeo = new ContactsDeo();
                contactsDeo.setContact_id(cursor.getString(0));
                contactsDeo.setName(cursor.getString(1));
                contactsDeo.setPhoneNumber(cursor.getString(2));
                contactsDeo.setIsselected(cursor.getString(3));
                contactsDeo.setImage(cursor.getString(4));
                contactsDeo.setNewContact_id(cursor.getString(5));
                System.out.println("DataBaseHelper123 getDateRate " + cursor.getString(1)+" "+cursor.getString(5));

                dataListList.add(contactsDeo);
            } while (cursor.moveToNext());
        }
        return dataListList;
    }



    public void deleteFromAddedContacts (String id) {
        SQLiteDatabase database = this.getWritableDatabase();
        String where = "";
        if(id.length() > 0) {
            where = "where  Contact_id = "+id;
        }
        String deleteQuery = "Delete from AddedContacts "+where ;
        Log.d("settingdeletequery", deleteQuery);
        database.execSQL(deleteQuery);
    }

    public void deleteFromAllContacts () {
        SQLiteDatabase database = this.getWritableDatabase();
        String deleteQuery = "Delete from Contacts ";
        Log.d("settingdeletequery", deleteQuery);
        database.execSQL(deleteQuery);
    }

    public void deleteFromConnectedContacts () {
        SQLiteDatabase database = this.getWritableDatabase();
        String deleteQuery = "Delete from ConnectedContacts ";
        Log.d("settingdeletequery", deleteQuery);
        database.execSQL(deleteQuery);
    }

    public void deleteFromConnectedContactsBirdEye () {
        SQLiteDatabase database = this.getWritableDatabase();
        String deleteQuery = "Delete from HomeConnectBirdEye ";
        Log.d("settingdeletequery deleteFromConnectedContactsBirdEye", deleteQuery);
        database.execSQL(deleteQuery);
    }

    public void deleteFromHomeConnectedContacts () {
        SQLiteDatabase database = this.getWritableDatabase();
        String deleteQuery = "Delete from HomeConnect ";
        Log.d("settingdeletequery", deleteQuery);
        database.execSQL(deleteQuery);
    }


    public void UpdateAllContacts (String User_Selected,String id) {
        SQLiteDatabase database = this.getWritableDatabase();
        String where = "";
        if(id.length() > 0) {
            where = "where  Id = "+id;
        }
        String deleteQuery = "Update Contacts set User_Selected = '"+User_Selected+"' "+where;
        Log.d(" UpdateContacts", deleteQuery);
        database.execSQL(deleteQuery);
    }

    public void UpdateConnectedContacts (String User_Selected,String id) {
        SQLiteDatabase database = this.getWritableDatabase();
        String where = "";
        if(id.length() > 0) {
            where = "where  Id = "+id;
        }
        String deleteQuery = "Update ConnectedContacts set User_Selected = '"+User_Selected+"' "+where;
        Log.d(" UpdateContacts", deleteQuery);
        database.execSQL(deleteQuery);
    }

}
