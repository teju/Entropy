package com.entrophy.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.entrophy.model.ContactsDeo;

import java.util.ArrayList;
import java.util.List;


public class DataBaseHelper extends SQLiteOpenHelper {

    private static  int database_version  = 2;
    private final Context context;
    public String Contacts =" CREATE TABLE `Contacts` (Id INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "User_Name TEXT, User_Phone TEXT, User_Selected TEXT, Image TEXT); ";
    public String AddedContacts =" CREATE TABLE `AddedContacts` (Id INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "User_Name TEXT, User_Phone TEXT, User_Selected TEXT, Image TEXT,Contact_id TEXT); ";

    private static final String DELETE_Contacts = "DROP TABLE IF EXISTS Contacts" ;
    private static final String DELETE_AddedContacts = "DROP TABLE IF EXISTS AddedContacts" ;

    public DataBaseHelper(Context context) {
        super(context, "entrophy_db",null, database_version);
        this.context=context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Contacts);
        db.execSQL(AddedContacts);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_Contacts);
        db.execSQL(DELETE_AddedContacts);
        onCreate(db);
    }


    public boolean putContacts(DataBaseHelper dbh, String User_Name,
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


    public List<ContactsDeo> getContacts() {
        List<ContactsDeo> dataListList = new ArrayList<ContactsDeo>();
        String selectQuery = "SELECT * FROM Contacts " ;

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
                contactsDeo.setImage(cursor.getString(3));
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
            where = "where  Id = "+id;
        }
        String deleteQuery = "Delete from AddedContacts "+where ;
        Log.d("settingdeletequery", deleteQuery);
        database.execSQL(deleteQuery);
    }

    public void deleteFromContacts () {
        SQLiteDatabase database = this.getWritableDatabase();
        String deleteQuery = "Delete from Contacts ";
        Log.d("settingdeletequery", deleteQuery);
        database.execSQL(deleteQuery);
    }

    public void UpdateContacts (String User_Selected,String id) {
        SQLiteDatabase database = this.getWritableDatabase();
        String where = "";
        if(id.length() > 0) {
            where = "where  Id = "+id;
        }
        String deleteQuery = "Update Contacts set User_Selected = '"+User_Selected+"'"+where;
        Log.d(" UpdateContacts", deleteQuery);
        database.execSQL(deleteQuery);
    }


}
