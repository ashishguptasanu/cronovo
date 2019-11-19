package com.amit.cronovolibrary;

import android.util.Log;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

class LibraryMethods {
    private static double previousCT = 37;
    private static double previousVariance = 0;

    static double calculateCardiacEfficiency(Cronovo.TimePeriod timePeriod, DataBase dataBase) {
        Log.d("cronovo", "timeperiod" + timePeriod.getTimePeriod());
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
        double heart_rate_recovery;
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
        ArrayList<UserDetails> userDetailsArrayList = dataBase.getUserDetails(from, to);
        Log.d("cronovo", "list size at resting hr" + userDetailsArrayList);
        if (userDetailsArrayList.size() == 0) {
            Log.d("cronovo", "insufficient data");
            return -1;
        } else {
            return Helper.calculateRestingHr(userDetailsArrayList);
        }
    }

    static double calculateCoreTemperature(DataBase dataBase) {
        long to = Instant.now().getEpochSecond();
        long from = to - 60;
        double CoreTemp;
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

    static double calculateVO2MAX(DataBase dataBase) {
        double vo2_max;
        double hr_rest = LibraryMethods.calculateRestingHr(dataBase);
        if (hr_rest == -1) {
            vo2_max = -1;
        } else {
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

    static double calculateRRI(DataBase dataBase) {
        double result;
        ArrayList<UserDetails> userDetailsArrayList = dataBase.getUserDetails(DataBase.readQueryRRi);
        if (userDetailsArrayList.size() == 0) {
            return -1;
        } else {
            double value1 = userDetailsArrayList.get(1).getTime_sec() + (double) userDetailsArrayList.get(1).getTime_ms() / 1000;
            double value2 = userDetailsArrayList.get(0).getTime_sec() + (double) userDetailsArrayList.get(0).getTime_ms() / 1000;
            Log.d("cronovo", "" + value1 + " " + value2);
            Log.d("cronovo", "" + userDetailsArrayList.get(1).getTime_sec() + " " + userDetailsArrayList.get(1).getTime_ms());

            result = ((value1 - value2) * 1000);
            return Math.round(result);
        }
    }

    static double calculateHRV(DataBase dataBase) {
        ArrayList<UserDetails> userDetailsArrayList = dataBase.getUserDetails(DataBase.readQueryHRV);
        ArrayList<Integer> rriValues = Helper.returnRRiValues(userDetailsArrayList);
        ArrayList<Integer> rriDiff = Helper.returnRRiDiff(rriValues);
        double diffSquare = Helper.calculateDiffSquare(rriDiff);
        double result = Math.sqrt(diffSquare);
        Log.d("cronovo", "HRV" + result);
        return result;
    }

    static String calculateHeartRateZoneValues(DataBase dataBase, Integer Age, Cronovo.HeartZone heartZone) {
        Log.d("cronovo", "heartZone" + heartZone.getHeartZone());
        double maxHR = 207 - (Age * 0.7);
        double HRRest = calculateRestingHr(dataBase);
        if (HRRest == 0) {
            HRRest = 70;
        }
        double HRR = maxHR - HRRest;
        ArrayList<Double> zoneValues = Helper.calculateHeartRateAtZone(HRR, HRRest, heartZone);
        String minVal = String.valueOf(zoneValues.get(0));
        String maxVal = String.valueOf(zoneValues.get(1));
        return minVal + "-" + maxVal;
    }

    static Dictionary calculateTrainingEffect(DataBase dataBase, Integer Age, Long StartTime, Long StopTime) {
        long diff = Helper.findEpochDiff(StartTime, StopTime);
        int modCount = 0;
        int intenseCount = 0;
        ArrayList<Double> zoneValues = Helper.calculateZoneData(dataBase, Age);
        ArrayList<UserDetails> dataValues = dataBase.getUserDetails(StartTime, StopTime);
        for (int i = 0; i < dataValues.size(); i++) {
            long HR = dataValues.get(i).getHrm();
            if (HR > zoneValues.get(0) && HR < zoneValues.get(1)) {
                modCount += 1;
            } else if (HR > zoneValues.get(1)) {
                intenseCount += 1;
            }
        }
        double modPer = (modCount / dataValues.size()) * 100;
        double modTime = (modPer / 100) * diff;
        double intensePer = (intenseCount / dataValues.size()) * 100;
        double intenseTime = (intensePer / 100) * diff;
        double totalTime = modTime + (intenseTime * 2);
        Dictionary dict = new Hashtable();
        if (totalTime > 50) {
            dict.put("modTime", modTime);
            dict.put("intenseTime", intenseTime);
            dict.put("Result", "Daily Target Achieved");
        } else {
            dict.put("modTime", modTime);
            dict.put("intenseTime", intenseTime);
            dict.put("Result", "Too Lazy!!");
        }
        return dict;
    }
}
