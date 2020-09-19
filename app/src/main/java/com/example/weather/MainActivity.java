package com.example.weather;

import androidx.annotation.MainThread;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DownloadManager;
import android.content.ClipData;
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
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
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


public class MainActivity extends AppCompatActivity implements  ActionBar.OnNavigationListener{
    public TextView desc;
    public ImageView sky;
    public TextView temp;
    public Context context;
    public EditText field;
    public Button find;
    public Handler weatherHandler;
    public ConstraintLayout parent;
    public TextView pressure;
    public TextView humidity;
    public TextView windSpeed;

    String[] data = new String[] { "КИЕВ", "ЗАПОРОЖЬЕ", "ХАРЬКОВ" };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        windSpeed = findViewById(R.id.windTv);
        humidity = findViewById(R.id.waterTv);
        pressure = findViewById(R.id.pressureTv);
        sky = findViewById(R.id.weatherImage);
        desc = findViewById(R.id.disc);
        temp = findViewById(R.id.temp);
        find = findViewById(R.id.button);
        field = findViewById(R.id.edit);
        context = MainActivity.this;
        parent = findViewById(R.id.parent);
        getSupportActionBar().setTitle("");
        final View.OnClickListener findWeather = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (field.getText().toString().trim().length() > 2)
                    FindWeather(field.getText().toString());
                else Toast.makeText(context, "Fill the field", Toast.LENGTH_LONG).show();
            }
        };


        find.setOnClickListener(findWeather);

        weatherHandler = new Handler(){
            public void handleMessage(Message msg){
                Weather weather = (Weather) msg.obj;
                temp.setText(String.valueOf(weather.temp) + "°С");
                desc.setText(weather.description);
                windSpeed.setText(weather.speed + "m/s");
                sky.setBackgroundResource(weather.weatherImage);
                /*getSupportActionBar().setTitle(weather.city.toUpperCase());*/
                pressure.setText(weather.pressure + " hpa");
                humidity.setText(weather.humidity + "%");
            }
        };
        Thread working = new Thread(new Runnable() {
            @Override
            public void run() {
                LoadWeather();
            }
        });
        working.start();

        SetMenu();
    }

    public void SetMenu(){
        ActionBar bar = getSupportActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, data);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        bar.setListNavigationCallbacks(adapter, this);
    }

    public void LoadWeather(){
                WeatherLoader wl = new WeatherLoader(context);
                Weather weather = wl.LWeather();
                if(weather == null) return;
                Message msg = weatherHandler.obtainMessage(0, weather);
                weatherHandler.sendMessage(msg);
                FindWeather(weather.city);
;    }

    public void FindWeather(String city){
        final String c = city;
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {
                WeatherLoader weatherLoader = new WeatherLoader(c, context);
                Weather weather = weatherLoader.GetWeather();
                Message msg = weatherHandler.obtainMessage(0, weather);
                weatherHandler.sendMessage(msg);

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        final int itempos = itemPosition;
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {
                FindWeather(data[itempos]);
            }
        });
        return true;
    }
}