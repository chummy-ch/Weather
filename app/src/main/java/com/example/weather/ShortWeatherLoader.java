package com.example.weather;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ShortWeatherLoader {
    private String cord;
    private String lat, lon;
    private Context context;
    private String json;
    private HashMap<String, Integer> images;
    private ArrayList<ShortWeather> dailyWeather;

    public ShortWeatherLoader(Context context, String cord){
        this.context = context;
        this.cord = cord;
        dailyWeather = new ArrayList<>();
        images = new HashMap<String, Integer>(){{put("Clear", R.drawable.sun); put("Rain", R.drawable.rain); put("Snow", R.drawable.snowy); put("Clouds", R.drawable.cloud);}};
        GetCord();
    }

    private void GetCord(){
        lat = cord.substring(0, cord.indexOf('&'));
        lon = cord.substring(cord.indexOf("&") + 1, cord.length());
    }

    private void Filler(){
        ExecutorService ser = Executors.newSingleThreadExecutor();
        ser.submit(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 3; i++){
                    Extractor(i);
                }
            }
        });
    }

    private void LoadWeather(){
        RequestQueue queue = Volley.newRequestQueue(context);
        Token t = new Token();
        String url = "https://api.openweathermap.org/data/2.5/onecall?lat=" + lat + "&lon=" + lon + "&exclude=current,minutely,hourly&appid=" + t.token;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                json = response;
                Filler();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Такого города нет в базе, приносим свои извинения.", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(request);
    }

    private void Extractor(int i){
        ShortWeather weather = new ShortWeather();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("daily");
            weather.temp = jsonArray.getJSONObject(i).getJSONObject("temp").getString("day");
            weather.main = jsonArray.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("main");
            if(images.containsKey(weather.main)) weather.image = images.get(weather.main);
            else weather.image = R.drawable.cloud;
            weather.disc = jsonArray.getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("description");
            dailyWeather.add(weather);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void GetWeather(){
        UIManager ui = new UIManager();
        ExecutorService ser = Executors.newSingleThreadExecutor();
        ser.submit(new Runnable() {
            @Override
            public void run() {
                LoadWeather();
            }
        });
        while (dailyWeather.size() != 3){
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
