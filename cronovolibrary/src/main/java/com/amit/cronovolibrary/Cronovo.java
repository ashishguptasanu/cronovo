package com.amit.cronovolibrary;

import android.content.Context;
import android.util.Log;

import java.time.Instant;
import java.util.ArrayList;

public class Cronovo {
    public double getCardiacEfficieny(Cronovo.TimePeriod timePeriod, Context context) {
        DataBase dataBase = DataBase.getInstance(context);
        return LibraryMethods.calculateCaridacEfficiency(timePeriod, dataBase);
    }

    public double getHeartRateRecovery(Cronovo.RecoveryTime recoveryTime, Context context) {
        DataBase dataBase = DataBase.getInstance(context);
        return LibraryMethods.calculateHeartRateRecovery(recoveryTime, dataBase);
    }

    public double getRestingHr(Context context) {
        DataBase dataBase = DataBase.getInstance(context);
        return LibraryMethods.calculateRestingHr(dataBase);
    }

    public double getCoreTemperature(Context context) {
        DataBase dataBase = DataBase.getInstance(context);
        return LibraryMethods.calculateCoreTemperature(dataBase);


    }

    public  double getVO2MAX(Context context){
        DataBase dataBase=DataBase.getInstance(context);
        return LibraryMethods.calculateVO2MAX(dataBase);
    }

    public double getRRI(Context context) {
        DataBase dataBase = DataBase.getInstance(context);
        return LibraryMethods.calculateRRI(dataBase);
    }

    /*public void saveUserDetails(long signal, long hrm, long time_sec, long time_milli_sec, long cadence, long steps, long vo2, long calories, Context context) {
        DataBase dataBase = DataBase.getInstance(context);
        long entry_time = Instant.now().getEpochSecond();
        if (signal < 70)
            Log.d("cronovo", "signal less than 70");
        else {
            long i = dataBase.insertUserDetails(signal, hrm, time_sec, time_milli_sec, cadence, steps, vo2, calories, entry_time, Helper.getCurrentDate());
            Log.d("cronovo", "" + i);
        }
    }*/
    public void saveUserDetails(long signal, long hrm, long time_sec, long time_milli_sec, long cadence, long steps, long vo2, long calories,long entry_time,String date, Context context) {
        DataBase dataBase = DataBase.getInstance(context);
       // long entry_time = Instant.now().getEpochSecond();
        if (signal < 70)
            Log.d("cronovo", "signal less than 70");
        else {
            long i = dataBase.insertUserDetails(signal, hrm, time_sec, time_milli_sec, cadence, steps, vo2, calories, entry_time,date);
            Log.d("cronovo", "" + i);
        }
    }

    public void saveUser(int age, int height, int weight, Context context) {
        SharedPref.init(context);
        SharedPref.write("age", age);
        SharedPref.write("height", height);
        SharedPref.write("weight", weight);
    }

    public ArrayList<UserDetails> getUserDetails(Context context) {
        DataBase dataBase = DataBase.getInstance(context);
        ArrayList<UserDetails> userDetailsArrayList = dataBase.getUserDetails();
        Log.d("cronovo", "" + userDetailsArrayList);
        return dataBase.getUserDetails();
    }

    public double  getHRV(Context context){
        DataBase dataBase = DataBase.getInstance(context);
        return LibraryMethods.calculateHRV(dataBase);

    }
    public enum TimePeriod {
        DAILY(86400), WEEKLY(604800), MONTHLY(2678400), ALLTIME(0);

        private final long i;

        TimePeriod(long i) {
            this.i = i;
        }

        public long getTimePeriod() {
            return i;
        }
    }

    public enum RecoveryTime {
        THIRTYSEC(30), SIXTYSEC(60), TWENTYSEC(120);
        private final long i;

        RecoveryTime(long i) {
            this.i = i;
        }

        public long getRecoveryTime() {
            return i;
        }
    }

}
