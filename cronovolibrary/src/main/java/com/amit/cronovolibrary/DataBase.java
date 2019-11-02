package com.amit.cronovolibrary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DataBase extends SQLiteOpenHelper {
    private static DataBase sInstance;

    // Database Info
    private static final String DATABASE_NAME = "cronovo.db";
    private static final int DATABASE_VERSION = 2;

    // Table Name
    private static final String TABLE_USER_DETAILS = "user_details";


    // User Details Table Columns
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SIGNAL = "signal";
    private static final String COLUMN_HRM = "hrm";
    private static final String COLUMN_TIME_SEC = "timeSec";
    private static final String COLUMN_TIME_MILLI_SEC = "timeMilliSec";
    private static final String COLUMN_CADENCE = "cadence";
    private static final String COLUMN_STEPS = "steps";
    private static final String COLUMN_VO2 = "vo2";
    private static final String COLUMN_CALORIES = "calories";
    private static final String COLUMN_ENTRY_TIME = "entry_time";
    private static final String COLUMN_DATE = "date";

    // Create user table SQL query
    private static final String CREATE_USER_DETAILS_TABLE =
            "CREATE TABLE " + TABLE_USER_DETAILS + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_SIGNAL + " INTEGER,"
                    + COLUMN_HRM + " INTEGER,"
                    + COLUMN_TIME_SEC + " INTEGER,"
                    + COLUMN_TIME_MILLI_SEC + " INTEGER,"
                    + COLUMN_CADENCE + " INTEGER,"
                    + COLUMN_STEPS + " INTEGER,"
                    + COLUMN_VO2 + " INTEGER,"
                    + COLUMN_CALORIES + " INTEGER,"
                    + COLUMN_ENTRY_TIME + " INTEGER,"
                    + COLUMN_DATE + " TEXT"
                    + ")";

    // SQL query to read data for RRI calculation
    static String readQueryRRi = "SELECT * FROM (SELECT * FROM user_details ORDER BY id DESC LIMIT 2) SUB ORDER BY id ASC";

    // SQL query to read data for HRV calculation
    static String readQueryHRV = "SELECT * FROM (SELECT * FROM user_details ORDER BY id DESC LIMIT 10) sub ORDER BY id ASC";

    public static synchronized DataBase getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        if (sInstance == null) {
            sInstance = new DataBase(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_DETAILS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_DETAILS);
        onCreate(db);

    }

    //function to insert user details in database
    long insertUserDetails(long signal, long hrm, long time_sec, long time_milli_sec, long cadence, long steps, long vo2, long calories, long entry_time, String date) {
        // get writable database as we want to write data
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` will be inserted automatically.
        // no need to add them
        values.put(COLUMN_SIGNAL, signal);
        values.put(COLUMN_HRM, hrm);
        values.put(COLUMN_TIME_SEC, time_sec);
        values.put(COLUMN_TIME_MILLI_SEC, time_milli_sec);
        values.put(COLUMN_CADENCE, cadence);
        values.put(COLUMN_STEPS, steps);
        values.put(COLUMN_VO2, vo2);
        values.put(COLUMN_CALORIES, calories);
        values.put(COLUMN_ENTRY_TIME, entry_time);
        values.put(COLUMN_DATE, date);


        // insert row
        long id = db.insert(TABLE_USER_DETAILS, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    //function to read user details between two entry time stored in database
    ArrayList<UserDetails> getUserDetails(long from, long to) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<UserDetails> userDetailsArrayList = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER_DETAILS +
                " WHERE " + COLUMN_ENTRY_TIME +
                " BETWEEN '" + from + "' AND '" + to + "'", null);

        if (cursor.moveToFirst()) {
            do {
                UserDetails user_details = new UserDetails();
                // t.setId(c.getString((c.getColumnIndex(KEY_ID))));
                user_details.setSignal(cursor.getInt((cursor.getColumnIndex(COLUMN_SIGNAL))));
                user_details.setHrm(cursor.getInt((cursor.getColumnIndex(COLUMN_HRM))));
                user_details.setTime_sec(cursor.getInt((cursor.getColumnIndex(COLUMN_TIME_MILLI_SEC))));
                user_details.setTime_ms(cursor.getInt((cursor.getColumnIndex(COLUMN_TIME_SEC))));
                user_details.setCadence(cursor.getInt((cursor.getColumnIndex(COLUMN_CADENCE))));
                user_details.setSteps(cursor.getInt((cursor.getColumnIndex(COLUMN_STEPS))));
                user_details.setVo2(cursor.getInt((cursor.getColumnIndex(COLUMN_VO2))));
                user_details.setCadence(cursor.getInt((cursor.getColumnIndex(COLUMN_CADENCE))));
                user_details.setEntry_time((cursor.getLong((cursor.getColumnIndex(COLUMN_ENTRY_TIME)))));
                user_details.setDate(cursor.getString((cursor.getColumnIndex(COLUMN_DATE))));
                //19 column
                // adding to tags list
                userDetailsArrayList.add(user_details);
            } while (cursor.moveToNext());
        }
        // close the db connection
        cursor.close();
        db.close();
        return userDetailsArrayList;
    }

    //function to read all user detail stored in database
    ArrayList<UserDetails> getUserDetails() {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<UserDetails> userDetailsArrayList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER_DETAILS;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                UserDetails user_details = new UserDetails();
                // t.setId(c.getString((c.getColumnIndex(KEY_ID))));
                user_details.setSignal(cursor.getInt((cursor.getColumnIndex(COLUMN_SIGNAL))));
                user_details.setHrm(cursor.getInt((cursor.getColumnIndex(COLUMN_HRM))));
                user_details.setTime_sec(cursor.getInt((cursor.getColumnIndex(COLUMN_TIME_MILLI_SEC))));
                user_details.setTime_ms(cursor.getInt((cursor.getColumnIndex(COLUMN_TIME_SEC))));
                user_details.setCadence(cursor.getInt((cursor.getColumnIndex(COLUMN_CADENCE))));
                user_details.setSteps(cursor.getInt((cursor.getColumnIndex(COLUMN_STEPS))));
                user_details.setVo2(cursor.getInt((cursor.getColumnIndex(COLUMN_VO2))));
                user_details.setCadence(cursor.getInt((cursor.getColumnIndex(COLUMN_CADENCE))));
                user_details.setEntry_time((cursor.getLong((cursor.getColumnIndex(COLUMN_ENTRY_TIME)))));
                user_details.setDate(cursor.getString((cursor.getColumnIndex(COLUMN_DATE))));
                userDetailsArrayList.add(user_details);
            } while (cursor.moveToNext());
        }
        // close the db connection
        cursor.close();
        db.close();

        return userDetailsArrayList;
    }


    //function to delete user details table
    public void deleteUserTable() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_USER_DETAILS, null, null);
        db.close();
    }

    //function to read user details where HR is min
    ArrayList<UserDetails> getUserMinHrDetails(String date) {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<UserDetails> userDetails = new ArrayList<>();
        // "SELECT * FROM Cronovo_DB WHERE date='\(date)' AND hrm = (SELECT MIN(hrm) FROM Cronovo_DB)"
        Cursor cursor = db.rawQuery("SELECT * FROM user_details WHERE date= ? AND hrm = (SELECT MIN(hrm) FROM user_details)"
                , new String[]{date});

        if (cursor.moveToFirst()) {
            do {
                UserDetails user_details = new UserDetails();
                // t.setId(c.getString((c.getColumnIndex(KEY_ID))));
                user_details.setSignal(cursor.getInt((cursor.getColumnIndex(COLUMN_SIGNAL))));
                user_details.setHrm(cursor.getInt((cursor.getColumnIndex(COLUMN_HRM))));
                user_details.setTime_sec(cursor.getInt((cursor.getColumnIndex(COLUMN_TIME_MILLI_SEC))));
                user_details.setTime_ms(cursor.getInt((cursor.getColumnIndex(COLUMN_TIME_SEC))));
                user_details.setCadence(cursor.getInt((cursor.getColumnIndex(COLUMN_CADENCE))));
                user_details.setSteps(cursor.getInt((cursor.getColumnIndex(COLUMN_STEPS))));
                user_details.setVo2(cursor.getInt((cursor.getColumnIndex(COLUMN_VO2))));
                user_details.setCadence(cursor.getInt((cursor.getColumnIndex(COLUMN_CADENCE))));
                user_details.setEntry_time((cursor.getLong((cursor.getColumnIndex(COLUMN_ENTRY_TIME)))));
                user_details.setDate(cursor.getString((cursor.getColumnIndex(COLUMN_DATE))));
                userDetails.add(user_details);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return userDetails;
    }

    ArrayList<UserDetails> getUserMaxHrDetails(String date) {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<UserDetails> userDetails = new ArrayList<>();
        // "SELECT * FROM Cronovo_DB WHERE date='\(date)' AND hrm = (SELECT MIN(hrm) FROM Cronovo_DB)"
        Cursor cursor = db.rawQuery("SELECT * FROM user_details WHERE date= ? AND hrm = (SELECT MAX(hrm) FROM user_details)"
                , new String[]{date});

        if (cursor.moveToFirst()) {
            do {
                UserDetails user_details = new UserDetails();
                // t.setId(c.getString((c.getColumnIndex(KEY_ID))));
                user_details.setSignal(cursor.getInt((cursor.getColumnIndex(COLUMN_SIGNAL))));
                user_details.setHrm(cursor.getInt((cursor.getColumnIndex(COLUMN_HRM))));
                user_details.setTime_sec(cursor.getInt((cursor.getColumnIndex(COLUMN_TIME_MILLI_SEC))));
                user_details.setTime_ms(cursor.getInt((cursor.getColumnIndex(COLUMN_TIME_SEC))));
                user_details.setCadence(cursor.getInt((cursor.getColumnIndex(COLUMN_CADENCE))));
                user_details.setSteps(cursor.getInt((cursor.getColumnIndex(COLUMN_STEPS))));
                user_details.setVo2(cursor.getInt((cursor.getColumnIndex(COLUMN_VO2))));
                user_details.setCadence(cursor.getInt((cursor.getColumnIndex(COLUMN_CADENCE))));
                user_details.setEntry_time((cursor.getLong((cursor.getColumnIndex(COLUMN_ENTRY_TIME)))));
                user_details.setDate(cursor.getString((cursor.getColumnIndex(COLUMN_DATE))));
                userDetails.add(user_details);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userDetails;
    }

    ArrayList<UserDetails> getUserDetails(String selectQuery) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<UserDetails> userDetailsArrayList = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                UserDetails user_details = new UserDetails();
                // t.setId(c.getString((c.getColumnIndex(KEY_ID))));
                user_details.setSignal(cursor.getInt((cursor.getColumnIndex(COLUMN_SIGNAL))));
                user_details.setHrm(cursor.getInt((cursor.getColumnIndex(COLUMN_HRM))));
                user_details.setTime_sec(cursor.getInt((cursor.getColumnIndex(COLUMN_TIME_SEC))));
                user_details.setTime_ms(cursor.getInt((cursor.getColumnIndex(COLUMN_TIME_MILLI_SEC))));
                user_details.setCadence(cursor.getInt((cursor.getColumnIndex(COLUMN_CADENCE))));
                user_details.setSteps(cursor.getInt((cursor.getColumnIndex(COLUMN_STEPS))));
                user_details.setVo2(cursor.getInt((cursor.getColumnIndex(COLUMN_VO2))));
                user_details.setCadence(cursor.getInt((cursor.getColumnIndex(COLUMN_CADENCE))));
                user_details.setEntry_time((cursor.getLong((cursor.getColumnIndex(COLUMN_ENTRY_TIME)))));
                user_details.setDate(cursor.getString((cursor.getColumnIndex(COLUMN_DATE))));
                userDetailsArrayList.add(user_details);
            } while (cursor.moveToNext());
        }
        // close the db connection
        cursor.close();
        db.close();
        return userDetailsArrayList;
    }


}
