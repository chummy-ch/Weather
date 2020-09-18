package com.example.weather;

import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActionBar;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.LocaleDisplayNames;
import android.icu.text.UnicodeSetSpanner;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.TokenWatcher;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {
    public TextView desc;
    public ImageView sky;
    public TextView temp;
    public TextView minMaxTemp;
    public Context context;
    public EditText field;
    public Button find;
    public Handler handler;
    public ConstraintLayout parent;
    public TextView city;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.acivity_weather);
        getSupportActionBar().setTitle("КИЕВ");


        /*sky = findViewById(R.id.skyImage);
        desc = findViewById(R.id.decs);
        temp = findViewById(R.id.tempField);
        minMaxTemp = findViewById(R.id.max_minField);
        find = findViewById(R.id.button);
        field = findViewById(R.id.field);
        city = findViewById(R.id.cityName);
        context = MainActivity.this;
        parent = findViewById(R.id.parent);
        final View.OnClickListener findWeather = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (field.getText().toString().trim().length() > 2)
                FindWeather();
                else Toast.makeText(context, "Fill the field", Toast.LENGTH_LONG).show();
            }
        };
        find.setOnClickListener(findWeather);
        handler = new Handler(){
            public void handleMessage(Message msg){
                Weather weather = (Weather)msg.obj;
                temp.setText(String.valueOf(weather.temp) + "°С");
                minMaxTemp.setText(String.valueOf(weather.tempMax) + "°С / " + String.valueOf(weather.tempMin) + "°С");
                desc.setText(weather.description + "\n" + "Wind speed: " + String.valueOf(weather.speed) + " m/s");
                sky.setBackgroundResource(weather.weatherImage);
                city.setText(weather.city.toUpperCase());
            }
        };
        Thread working = new Thread(new Runnable() {
            @Override
            public void run() {
                LoadWeather();
            }
        });
        working.start();*/
    }

    public void LoadWeather(){
                WeatherLoader wl = new WeatherLoader(context);
                Weather weather = wl.LWeather();
                if(weather == null) return;
                Message msg = handler.obtainMessage(0, weather);
                handler.sendMessage(msg);
                wl = new WeatherLoader(weather.city, context);
                weather = wl.GetWeather();
                msg = handler.obtainMessage(0, weather);
                handler.sendMessage(msg);
;    }

    public void FindWeather(){
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {
                WeatherLoader weatherLoader = new WeatherLoader(field.getText().toString(), context);
                Weather weather = weatherLoader.GetWeather();
                Message msg = handler.obtainMessage(0, weather);
                handler.sendMessage(msg);

            }
        });
    }
}