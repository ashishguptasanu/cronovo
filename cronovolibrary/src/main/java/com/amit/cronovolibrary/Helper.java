package com.amit.cronovolibrary;

import android.util.Log;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

class Helper {

    static double calculateAverageCadence(ArrayList<UserDetails> userDetailsArrayList) {
        double total_cadence = 0;
        double average_cadence = 0;

        for (int i = 0; i < userDetailsArrayList.size(); i++) {
            total_cadence = total_cadence + userDetailsArrayList.get(i).getCadence();
        }
        try {
            average_cadence = total_cadence / userDetailsArrayList.size();
            Log.d("cronovo", "total_cadence" + total_cadence);
            Log.d("cronovo", "average_cadence" + average_cadence);


        } catch (ArithmeticException e) {
            e.printStackTrace();
        }
        return average_cadence;
    }

    static double calculateAverageHR(ArrayList<UserDetails> userDetailsArrayList) {
        double total_HR = 0;
        double average_HR = 0;

        for (int i = 0; i < userDetailsArrayList.size(); i++) {
            total_HR = total_HR + userDetailsArrayList.get(i).getHrm();
        }
        try {
            average_HR = total_HR / userDetailsArrayList.size();
            Log.d("cronovo", "total_hR" + total_HR);
            Log.d("cronovo", "average_hr" + average_HR);

        } catch (ArithmeticException e) {
            e.printStackTrace();
        }
        return average_HR;
    }

    static double calculateRestingHr(ArrayList<UserDetails> userDetailsArrayList) {
        long total_RestingHr = 0;

        for (int i = 0; i < userDetailsArrayList.size(); i++) {
            total_RestingHr = total_RestingHr + userDetailsArrayList.get(i).getHrm();
        }
        return (double) (total_RestingHr / userDetailsArrayList.size());
    }

    static public String getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        String strDate = formatter.format(date);
        System.out.println("Date Format with MM/dd/yyyy : " + strDate);
        return strDate;
    }


    static ArrayList<Integer> returnRRiValues(ArrayList<UserDetails> userDetailsArrayList) {
        // int[] result = new int[userDetailsArrayList.size()];
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < userDetailsArrayList.size() - 1; i++) {
            double value1 = userDetailsArrayList.get(i + 1).getTime_sec() + (double) userDetailsArrayList.get(i + 1).getTime_ms() / 1000;
            double value2 = userDetailsArrayList.get(i).getTime_sec() + (double) userDetailsArrayList.get(i).getTime_ms() / 1000;
            result.add((int) Math.round((value1 - value2) * 1000));
        }
        return result;
    }


    static ArrayList<Integer> returnRRiDiff(ArrayList<Integer> data) {
        ArrayList<Integer> rriDiff = new ArrayList<>();
        for (int i = 0; i < data.size() - 1; i++) {
            int value1 = data.get(i + 1);
            int value2 = data.get(i);
            int result = value1 - value2;
            rriDiff.add(result);
        }
        return rriDiff;
    }


    static double calculateDiffSquare(ArrayList<Integer> data) {
        double total = 0;
        for (int i = 0; i < data.size(); i++) {
            total = total + Math.pow(i, 2);
        }
        double diffSquare = total / data.size() - 1;
        return diffSquare;
    }

    static ArrayList<Double> calculateHeartRateAtZone(double HRR, double HRRest, Cronovo.HeartZone Zone){
        ArrayList<Double> result = new ArrayList<>();
        double zoneMin = Zone.getHeartZone() / 100;
        double zoneMax = (Zone.getHeartZone() + 10) / 100;

        double HRAtZoneMin = (zoneMin * HRR) + HRRest;
        double HRAtZoneMax = (zoneMax * HRR) + HRRest;

        result.addAll(Arrays.asList(HRAtZoneMin, HRAtZoneMax));
        return result;
    }

    static long findEpochDiff(Long Start, Long Stop){
        long minutes = Math.abs(Stop - Start) / 60;
        return minutes;
    }

    static ArrayList<Double> calculateZoneData(DataBase database, Integer Age){
        ArrayList<Double> result = new ArrayList<>();
        double maxHR = 207 - (Age * 0.7);
//        double HRRest = calculateRestingHr(dataBase);
////        if (HRRest == 0){
////            HRRest = 70;
////        }
        double HRRest = 70;
        double HRR = maxHR - HRRest;
        double zoneValueModerate = calculateHeartZone(HRR, HRRest, 50);
        double zoneValueIntense = calculateHeartZone(HRR, HRRest, 70);
        result.addAll(Arrays.asList(zoneValueModerate, zoneValueIntense));
        return result;

    }

    static double calculateHeartZone(Double HRR, Double HRRest, Integer Zone){
        int zone = Zone / 100;
        double HRAtZone = (Zone * HRR) + HRRest;
        return HRAtZone;
    }
}
