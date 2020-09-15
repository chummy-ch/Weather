package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    public TextView text;
    public Context context = this;
    public EditText field;
    public Button find;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.tv);
        find = findViewById(R.id.button);
        field = findViewById(R.id.field);
        final View.OnClickListener findWeather = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FindWeather();
            }
        };
        find.setOnClickListener(findWeather);
    }

    public void FindWeather(){
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {
                if (field.getText().toString().trim().length() > 2){
                    WeatherLoader weatherLoader = new WeatherLoader(field.getText().toString(), context);
                    Weather weather = weatherLoader.GetWeather();
                    Gson gson = new Gson();
                    String weatherString = gson.toJson(weather);
                    text.setText(weatherString);
                }
                else Toast.makeText(context, "Fill the field", Toast.LENGTH_LONG).show();
            }
        });
    }
}