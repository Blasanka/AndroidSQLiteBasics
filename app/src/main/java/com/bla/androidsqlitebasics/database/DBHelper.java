package com.bla.androidsqlitebasics.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bla.androidsqlitebasics.model.User;

import java.util.ArrayList;
import java.util.List;

import static com.bla.androidsqlitebasics.database.UserProfile.User.COLUMN_DOB;
import static com.bla.androidsqlitebasics.database.UserProfile.User.COLUMN_GENDER;
import static com.bla.androidsqlitebasics.database.UserProfile.User.COLUMN_PASSWORD;
import static com.bla.androidsqlitebasics.database.UserProfile.User.COLUMN_USERNAME;
import static com.bla.androidsqlitebasics.database.UserProfile.User.TABLE_NAME;
import static com.bla.androidsqlitebasics.database.UserProfile.User._ID;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "users.db";
    public static final int DATABASE_VERSION = 3;

    public static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME +"( "+
            _ID + " INTEGER PRIMARY KEY, " + COLUMN_USERNAME +" TEXT, "+ COLUMN_PASSWORD +" TEXT, "+
            COLUMN_DOB +" DATE, " + COLUMN_GENDER + ")";

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "+ TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    //Supposed if question asked to implement add, update, read methods in this class
    //to check user when login
    public long checkUser(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();

        //check if entered username and password exists in the users table
        String[] selectionArgs = {username, password};
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE "+ COLUMN_USERNAME + " = ? AND " +
                COLUMN_PASSWORD + " = ?";

        Cursor cursor = db.rawQuery(query, selectionArgs);

        //if username and password match and row count is greater than 1 get that userId or else assign -1 to @useerId
        long userId = -1;
        if (cursor.moveToFirst()) {
            userId = (cursor.getCount() >= 1) ? cursor.getLong(0) : -1;
        }
        //if the user count greater than(shoul be equal to if properly check) 1 user exists and return true
        //return cursor.getCount() >= 1;

        //to login user from primary key
        return userId;
    }

    //register/add user to db table
    public long addInfo(String username, String password) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERNAME, username);
        cv.put(COLUMN_PASSWORD, password);

        return db.insert(TABLE_NAME, null, cv);
    }

    public boolean updateInfo(long userId, String username, String password, String dob, String gender) {
        SQLiteDatabase db = getWritableDatabase();

        //to check for the user to update
        String[] selectionArgs = {userId+""};
        String whereClause = _ID + " = ?";

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERNAME, username);
        cv.put(COLUMN_PASSWORD, password);
        cv.put(COLUMN_DOB, dob);
        cv.put(COLUMN_GENDER, gender);

        return db.update(TABLE_NAME, cv, whereClause, selectionArgs) > 0;
    }

    public List<User> readAllInfor() {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                _ID,
                COLUMN_USERNAME,
                COLUMN_PASSWORD,
                COLUMN_DOB,
                COLUMN_GENDER
        };

        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        List<User> users = new ArrayList();
        while (cursor.moveToNext()) {
            User user = new User(cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4));
            users.add(user);
        }
        cursor.close();
        return users;
    }

    public User readAllInfor(long userId) {
        SQLiteDatabase db = getReadableDatabase();

        //retrieve the user using primary key
        String[] selectionArgs = {userId+""};
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE "+ _ID + " = ? ";

        Cursor cursor = db.rawQuery(query, selectionArgs);
        User u = new User();
        if (cursor.moveToFirst()) {
            u = new User(cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4));
        }
        //if the user count greater than(shoul be equal to if properly check) 1 user exists and return true
        return u;
    }

    public boolean deleteInfo(long userId) {
        SQLiteDatabase db = getWritableDatabase();

        //delete user from db table
        String[] selectionArgs = {userId+""};
        String whereClause = _ID + " = ?";

        return db.delete(TABLE_NAME, whereClause, selectionArgs) > 0;
    }
}
