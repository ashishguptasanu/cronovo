package com.amit.library;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amit.cronovolibrary.Cronovo;
import com.amit.cronovolibrary.DataBase;
import com.amit.cronovolibrary.UserDetails;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private EditText ageText, weightText, heightText;
    private Button button, button1, button2, button3, button4, button5, button6, button7, temp, hrv;
    private long signal = 100;
    private long hrm = 390;
    private long time_sec = 11213;
    private long time_ms = 123141;
    private long cadence = 20;
    private long steps = 1000;
    private long vo2 = 500;
    private long calories = 2540;
    private long entry_time;
    int inc = 0;
    private String date;
    long[][] arr = {{0, 70, 7, 880, 0, 4, 35, 0},
            {56, 69, 8, 880, 0, 4, 35, 0},
            {80, 67, 9, 800, 99, 4, 35, 0},
            {83, 62, 12, 840, 90, 4, 29, 0},
            {45, 61, 13, 760, 0, 4, 29, 0},
            {90, 60, 14, 880, 50, 4, 29, 0},
            {59, 60, 15, 800, 0, 4, 29, 0},
            {81, 60, 16, 800, 0, 4, 29, 0},
            {82, 61, 17, 800, 0, 4, 29, 0},
            {96, 61, 18, 760, 0, 4, 29, 0},
            {100, 62, 19, 720, 0, 4, 29, 0},
            {100, 62, 20, 640, 0, 4, 29, 0},
            {100, 62, 21, 640, 0, 4, 29, 0},
            {100, 62, 22, 600, 0, 4, 29, 0},
            {80, 62, 12, 840, 90, 4, 29, 0},
            {80, 61, 13, 760, 0, 4, 29, 0},
            {80, 63, 52, 600, 90, 14, 29, 0},
            {80, 65, 53, 240, 0, 16, 29, 0},
            {44, 65, 54, 0, 0, 16, 29, 0},
            {0, 62, 22, 600, 0, 4, 29, 0},
            {47, 62, 12, 840, 90, 4, 29, 0},
            {45, 61, 13, 760, 0, 4, 29, 0},
            {45, 60, 14, 880, 50, 4, 29, 0},
            {59, 60, 15, 800, 0, 4, 29, 0},
            {59, 60, 16, 800, 0, 4, 29, 0},
            {82, 61, 17, 800, 0, 4, 29, 0},
            {96, 61, 18, 760, 0, 4, 29, 0},
            {100, 62, 19, 720, 0, 4, 29, 0},
            {100, 62, 20, 640, 0, 4, 29, 0},
            {100, 62, 21, 640, 0, 4, 29, 0},
            {100, 62, 22, 600, 0, 4, 29, 0},
            {100, 62, 23, 560, 0, 4, 29, 0},
            {89, 67, 56, 80, 0, 16, 29, 0},
            {89, 67, 56, 760, 0, 16, 29, 0},
            {100, 62, 20, 640, 0, 4, 29, 0},
            {100, 62, 21, 640, 0, 4, 29, 0},
            {100, 62, 22, 600, 0, 4, 29, 0},
            {47, 62, 12, 840, 90, 4, 29, 0},
            {45, 61, 13, 760, 0, 4, 29, 0},
            {45, 60, 14, 880, 50, 4, 29, 0},
            {59, 60, 15, 800, 0, 4, 29, 0},
            {59, 60, 16, 800, 0, 4, 29, 0},
            {82, 61, 17, 800, 0, 4, 29, 0},
            {96, 61, 18, 760, 0, 4, 29, 0},
            {0, 70, 7, 880, 0, 4, 35, 0},
            {56, 69, 8, 880, 0, 4, 35, 0},
            {56, 67, 9, 800, 99, 4, 35, 0},
            {47, 62, 12, 840, 90, 4, 29, 0},
            {45, 61, 13, 760, 0, 4, 29, 0},
            {45, 60, 14, 880, 50, 4, 29, 0},
            {59, 60, 15, 800, 0, 4, 29, 0},
            {59, 60, 16, 800, 0, 4, 29, 0},
            {82, 61, 17, 800, 0, 4, 29, 0},};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ageText = findViewById(R.id.editText);
        //weightText = findViewById(R.id.editText2);
        //heightText = findViewById(R.id.editText3);
        button = findViewById(R.id.button);
        button1 = findViewById(R.id.button2);
        button2 = findViewById(R.id.button3);
        button3 = findViewById(R.id.button4);
        //button4 = findViewById(R.id.button5);
        button5 = findViewById(R.id.button6);
        button6 = findViewById(R.id.button7);
        button7 = findViewById(R.id.getRRi);
        temp = findViewById(R.id.temp);
        hrv = findViewById(R.id.getHRV);

        Log.d("cronovo", "" + arr[0][1]);
        Log.d("cronovo", "" + arr[1][2]);


        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double rri = new Cronovo().getRRI(MainActivity.this);
                Log.d("cronovo", "rri" + rri);
            }
        });

        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double tem = new Cronovo().getCoreTemperature(MainActivity.this);
                Log.d("cronovo", "temp" + tem);

            }
        });

        hrv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double hrv = new Cronovo().getHRV(MainActivity.this);
                Log.d("cronovo", "hrv" + hrv);

            }
        });
        //long now = Instant.now().getEpochSecond();
        //long timeMilli = new Date().getTime();
        //Log.d("timeEpoch", "" + now);
        //Log.d("timeSystem", "" + timeMilli);

        //save  details
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // new Cronovo().saveUserDetails(arr[inc][0], arr[inc][1], arr[inc][2], arr[inc][3], arr[inc][4], arr[inc][5], arr[inc][6], arr[inc][7], MainActivity.this);
                //++inc;
                try {
                    JSONArray jArray = new JSONArray(readJSONFromAsset());
                    for (int i = 0; i < jArray.length(); ++i) {
                        String signal = jArray.getJSONObject(i).getString("Signal");// name of the country
                        String hrm = jArray.getJSONObject(i).getString("Hrm"); // dial code of the country
                        String timsec = jArray.getJSONObject(i).getString("Timesec"); // code of the country
                        String timems = jArray.getJSONObject(i).getString("Timems"); // code of the country
                        String cadence = jArray.getJSONObject(i).getString("cadence"); // code of the country
                        String steps = jArray.getJSONObject(i).getString("steps"); // code of the country
                        String vo2 = jArray.getJSONObject(i).getString("vo2"); // code of the country
                        String calories = jArray.getJSONObject(i).getString("Calories"); // code of the country

                        long entry_time = Instant.now().getEpochSecond() + i;
                        Date date = new Date();
                        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                        String strDate = formatter.format(date);

                        new Cronovo().saveUserDetails(Long.valueOf(signal), Long.valueOf(hrm), Long.valueOf(timsec), Long.valueOf(timems), Long.valueOf(cadence), Long.valueOf(steps), Long.valueOf(vo2), Long.valueOf(calories), entry_time, strDate, MainActivity.this);

                        //UserDetails userDetails= new UserDetails(Long.valueOf(signal),Long.valueOf(hrm),Long.valueOf(timsec),Long.valueOf(timems),Long.valueOf(cadence),Long.valueOf(steps),Long.valueOf(vo2),Long.valueOf(calories));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            private String readJSONFromAsset() {
                String json = null;
                try {
                    InputStream is = getAssets().open("data.json");
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();
                    json = new String(buffer, "UTF-8");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return null;
                }
                return json;

            }

        });
        //read details
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*SharedPref.init(MainActivity.this);
                int age = SharedPref.read("age", 0);
                int height = SharedPref.read("height", 0);
                int weight = SharedPref.read("weight", 0);
                Toast.makeText(MainActivity.this,"age ="+age +"weight ="+weight +"height"+height,Toast.LENGTH_LONG).show();*/

                //User user = db.getUserDetails();
                // Toast.makeText(MainActivity.this,"age ="+user.age +"weight ="+user.weight +"height"+user.height,Toast.LENGTH_LONG).show();
                ArrayList<UserDetails> userDetailsArrayList = new Cronovo().getUserDetails(MainActivity.this);
                for (int j = 0; j < userDetailsArrayList.size(); j++) {
                    Log.d("cronovo", "" + userDetailsArrayList.get(j).getSignal());
                    Log.d("cronovo", "" + userDetailsArrayList.get(j).getHrm());
                    Log.d("cronovo", "" + userDetailsArrayList.get(j).getTime_sec());
                    Log.d("cronovo", "" + userDetailsArrayList.get(j).getTime_ms());
                    Log.d("cronovo", "" + userDetailsArrayList.get(j).getCadence());
                    Log.d("cronovo", "" + userDetailsArrayList.get(j).getSteps());
                    Log.d("cronovo", "" + userDetailsArrayList.get(j).getVo2());
                    Log.d("cronovo", "" + userDetailsArrayList.get(j).getCalories());
                    Log.d("cronovo", "" + userDetailsArrayList.get(j).getEntry_time());
                    Log.d("cronovo", "" + userDetailsArrayList.get(j).getDate());

                }


            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        //cardiac efficency
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBase db = DataBase.getInstance(MainActivity.this);
                double cardiacEfficieny = new Cronovo().getCardiacEfficieny(Cronovo.TimePeriod.DAILY, MainActivity.this);
                Log.d("cronovo", "" + cardiacEfficieny);
            }
        });


       /* button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // DataBaseHelper db = DataBaseHelper.getInstance(MainActivity.this);
                // db.minHateRate(1);
            }
        });*/


        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double heartRateRecovery = new Cronovo().getHeartRateRecovery(Cronovo.RecoveryTime.SIXTYSEC, MainActivity.this);
                Log.d("cronovo", "" + heartRateRecovery);

            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
