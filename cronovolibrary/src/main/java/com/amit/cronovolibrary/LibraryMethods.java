package com.amit.cronovolibrary;

import android.util.Log;

import java.time.Instant;
import java.util.ArrayList;

public class LibraryMethods {
    private static double previousCT = 37;
    private static double previousVariance = 0;

    static double calculateCaridacEfficiency(Cronovo.TimePeriod timePeriod, DataBase dataBase) {
        Log.d("cronovo", "timeperiod" + timePeriod.getTimePeriod());
        // DataBase dataBaseHelper = DataBase.getInstance(context);
        double cardiacEfficiency = 0;
        ArrayList<UserDetails> userDetailsArrayList = dataBase.getUserDetails(Instant.now().getEpochSecond() - timePeriod.getTimePeriod(), Instant.now().getEpochSecond());
        if (userDetailsArrayList.size() == 0) {
            cardiacEfficiency = -1;
        } else {
            double average_cadence = Helper.calculateAverageCadence(userDetailsArrayList);
            double average_Hr = Helper.calculateAverageHR(userDetailsArrayList);
            try {
                cardiacEfficiency = average_cadence / average_Hr;
            } catch (ArithmeticException e) {
                e.printStackTrace();
            }
        }
        return cardiacEfficiency;
    }

    static double calculateHeartRateRecovery(Cronovo.RecoveryTime recoveryTime, DataBase dataBase) {
        Log.d("cronovo", "time" + recoveryTime.getRecoveryTime());
        String currentDate = Helper.getCurrentDate();
        double heart_rate_recovery = 0;
        /* DataBase dataBase = DataBase.getInstance(context); */
        ArrayList<UserDetails> minHRArrayList = dataBase.getUserMinHrDetails(currentDate);
        ArrayList<UserDetails> maxHRArrayList = dataBase.getUserMaxHrDetails(currentDate);
        if (maxHRArrayList.size() == 0) {
            Log.d("cronovo", "insufficient data");
            heart_rate_recovery = -1;
        } else {
            long from = maxHRArrayList.get(0).getEntry_time() + recoveryTime.getRecoveryTime();
            long to = from + 5;
            Log.d("cronovo", "hRR" + from);
            Log.d("cronovo", "hRR" + to);

            ArrayList<UserDetails> userDetailsArrayList = dataBase.getUserDetails(from, to);
            if (userDetailsArrayList.size() == 0) {
                Log.d("cronovo", "insufficent data");
                heart_rate_recovery = -1;
                ;
            } else {
                long hrRest = minHRArrayList.get(0).getHrm();
                Log.d("cronovo", "hRR hrRest" + hrRest);

                long hrPeak = maxHRArrayList.get(0).getHrm();
                Log.d("cronovo", "hRR hrPeak" + hrPeak);

                long hrAt = userDetailsArrayList.get(0).getHrm();
                Log.d("cronovo", "hRR hrAt" + hrAt);

                long hrrAt = hrPeak - hrAt;
                Log.d("cronovo", "hRR hrrAt" + hrrAt);

                heart_rate_recovery = hrrAt / (double) hrPeak * 180;
            }
        }
        return heart_rate_recovery;
    }

    static double calculateRestingHr(DataBase dataBase) {
        long to = Instant.now().getEpochSecond();
        long from = to - 60;
        // DataBase dataBase = DataBase.getInstance(context);
        ArrayList<UserDetails> userDetailsArrayList = dataBase.getUserDetails(from, to);
        Log.d("cronovo", "list size at resting hr" + userDetailsArrayList);
        if (userDetailsArrayList.size() == 0) {
            Log.d("cronovo", "insufficient data");
            return -1;
        } else
            return Helper.calculateRestingHr(userDetailsArrayList);

    }

    static double calculateCoreTemperature(DataBase dataBase) {
        long to = Instant.now().getEpochSecond();
        long from = to - 60;
        double CoreTemp = 0;
        ArrayList<UserDetails> userDetailsArrayList = dataBase.getUserDetails(from, to);
        Log.d("cronovo", "userDtl" + userDetailsArrayList);
        if (userDetailsArrayList.size() == 0) {
            Log.d("cronovo", "insufficient data");
            CoreTemp = -1;

        } else {
            double averageHR = Helper.calculateAverageHR(userDetailsArrayList);
            Log.d("cronovo", "hr" + averageHR);

            double currentCT = LibraryMethods.previousCT;
            Log.d("cronovo", "previous ct" + currentCT);

            double currentVariance = LibraryMethods.previousVariance + 0.000484;
            Log.d("cronovo", "previous v" + currentVariance);

            double Mt = (-9.1428 * currentCT) + 384.4286;
            Log.d("cronovo", "mt" + Mt);

            double Kt = (currentVariance * Mt) / (((Math.pow(Mt, 2.0)) * currentVariance) + 356.4544);
            Log.d("cronovo", "kt" + Kt);

            CoreTemp = currentCT + Kt * (averageHR - (-4.5714 * (Math.pow(currentCT, 2.0)) + 384.4286 * currentCT - 7887.1));
            Log.d("cronovo", "coretemp" + CoreTemp);

            double variance = (1 - Kt * Mt) * currentVariance;
            LibraryMethods.previousCT = CoreTemp;
            LibraryMethods.previousVariance = variance;
            Log.d("cronovo", "previousCT" + LibraryMethods.previousCT);
            Log.d("cronovo", "previousVr" + LibraryMethods.previousVariance);
        }
        return CoreTemp;
    }

    public static double calculateVO2MAX(DataBase dataBase) {
        double vo2_max;
        double hr_rest = LibraryMethods.calculateRestingHr(dataBase);
        if (hr_rest == -1)
            vo2_max = -1;
        else {
            String currentDate = Helper.getCurrentDate();
            ArrayList<UserDetails> maxHRArrayList = dataBase.getUserMaxHrDetails(currentDate);
            if (maxHRArrayList.size() == 0)
                vo2_max = -1;
            else {
                double hr_max = maxHRArrayList.get(0).getHrm();
                vo2_max = 15.3 * (hr_max / hr_rest);
            }

        }
        return vo2_max;
    }

    public static double calculateRRI(DataBase dataBase) {
        double result;
        ArrayList<UserDetails> userDetailsArrayList = dataBase.getUserDetails(DataBase.readQueryRRi);
        if (userDetailsArrayList.size() == 0)
            return -1;
        else {
            double value1 = userDetailsArrayList.get(1).getTime_sec() + (double) userDetailsArrayList.get(1).getTime_ms() / 1000;
            double value2 = userDetailsArrayList.get(0).getTime_sec() + (double) userDetailsArrayList.get(0).getTime_ms() / 1000;
            Log.d("cronovo", "" + value1 + " " + value2);
            Log.d("cronovo", "" + userDetailsArrayList.get(1).getTime_sec()+" "+userDetailsArrayList.get(1).getTime_ms());

            result = ((value1 - value2) * 1000);
            return Math.round(result);
        }
    }

    public static double calculateHRV(DataBase dataBase) {
        ArrayList<UserDetails> userDetailsArrayList = dataBase.getUserDetails(DataBase.readQueryHRV);
        ArrayList<Integer> rriValues = Helper.returnRRiValues(userDetailsArrayList);
        ArrayList<Integer> rriDiff = Helper.returnRRiDiff(rriValues);
        double diffSquare = Helper.calculateDiffSquare(rriDiff);
        double result = Math.sqrt(diffSquare);
        Log.d("cronovo","HRV"+result);
        return result;
    }
}
