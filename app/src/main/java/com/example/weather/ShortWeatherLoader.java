package com.example.weather;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ShortWeatherLoader {
    private String cord;
    private String lat, lon;
    private Context context;
    private ShortWeather weather;
    private String weatherJson;
    private ArrayList<ShortWeather> dailyWeather;

    public ShortWeatherLoader(Context context, String cord){
        this.context = context;
        this.cord = cord;
        dailyWeather = new ArrayList<>();
        GetCord();
    }

    private void GetCord(){
        lat = cord.substring(0, cord.indexOf('&'));
        lon = cord.substring(cord.indexOf("&") + 1, cord.length());
    }

    private void LoadWeather(){
        RequestQueue queue = Volley.newRequestQueue(context);
        Token t = new Token();
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+ cord + "&appid=" + t.token;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                weatherJson = response;
                Extractor();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Такого города нет в базе, приносим свои извинения.", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(request);
    }

    private void Extractor(){

    }

    public void GetWeather(){
        weather = new ShortWeather();
        UIManager ui = new UIManager();
        ExecutorService ser = Executors.newSingleThreadExecutor();
        ser.submit(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
}
