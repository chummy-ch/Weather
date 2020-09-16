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

import java.util.Dictionary;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WeatherLoader {
    private Context context;
    private String city;
    private Weather weather;
    private String weatherJson;
    private HashMap<String, Integer> images;

    public WeatherLoader(String city, Context context){
        this.context = context;
        this.city = city;
        images = new HashMap<String, Integer>(){{put("Clear", R.drawable.sun); put("Rain", R.drawable.rain); put("Snow", R.drawable.snowy);}};
    }

    private void LoadWeather(){
        RequestQueue queue = Volley.newRequestQueue(context);
        Token t = new Token();
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+ city + "&appid=" + t.token;
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
        try {
            JSONObject jsonObject = new JSONObject(weatherJson);
            JSONArray weatherJsonArray = jsonObject.getJSONArray("weather");
            weather.setMain(weatherJsonArray.getJSONObject(0).getString("main"));
            weather.setTemp((int) (Float.parseFloat(jsonObject.getJSONObject("main").getString("temp")) - 273.15));
            weather.setTempLike((int) (Float.parseFloat(jsonObject.getJSONObject("main").getString("feels_like")) - 273.15));
            weather.setTempMax((int) (Float.parseFloat(jsonObject.getJSONObject("main").getString("temp_max")) - 273.15));
            weather.setTempMin((int) (Float.parseFloat(jsonObject.getJSONObject("main").getString("temp_min")) - 273.15));
            weather.setSpeed(Float.parseFloat(jsonObject.getJSONObject("wind").getString("speed")));
            weather.setDescription(weatherJsonArray.getJSONObject(0).getString("description"));
            weather.setCity(city);
            if(images.containsKey(weather.main)) weather.weatherImage = images.get(weather.main);
            else weather.weatherImage = R.drawable.cloud;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Weather GetWeather(){
        weather = new Weather();
        ExecutorService ser = Executors.newSingleThreadExecutor();
        ser.submit(new Runnable() {
            @Override
            public void run() {
                LoadWeather();
            }
        });
        while(weather.city == null){
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return weather;
    }
}
