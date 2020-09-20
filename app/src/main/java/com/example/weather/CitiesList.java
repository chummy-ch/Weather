package com.example.weather;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CitiesList implements  ActionBar.OnNavigationListener{
    String[] data;
    private Context context;
    private ActionBar bar;

    public CitiesList(Context context, ActionBar bar){
        this.bar = bar;
        this.context = context;
        data = new String[]{};
        LoadCities();
    }

    public void DisplayCity(String city){
        for (String c : data) {
            if(c.toLowerCase().trim().equals(city.toLowerCase().trim())) return;
        }
        String[] copy = data;
        city = city.toUpperCase();
        data = new String[copy.length + 1];
        data[0] = city;
        for(int i = 0; i < copy.length; i++){
            data[i + 1] = copy[i];
        }
        SetMenu();
        data = copy;
    }

    public void AddCity(String city){
        for (String c : data) {
            if(c.toLowerCase().trim().equals(city.toLowerCase().trim())) return;
        }
        String[] copy = data;
        data = new String[copy.length + 1];
        data[0] = city;
        for(int i = 0; i < copy.length; i++){
            data[i + 1] = copy[i];
        }
        SetMenu();
        SaveCities();
        Toast.makeText(context, "The city is saved", Toast.LENGTH_SHORT).show();
    }

    public void SetMenu(){
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, data);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        bar.setListNavigationCallbacks(adapter, this);
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        final int itempos = itemPosition;
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {
                WeatherLoader loader = new WeatherLoader(data[itempos], context);
                loader.GetWeather();
            }
        });
        return true;
    }

    public void SaveCities(){
        String jsonString = new Gson().toJson(data);
        try{
            String filePath = context.getFilesDir().getPath().toString() + "cities.txt";
            FileWriter file = new FileWriter(filePath);
            file.write(jsonString);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void LoadCities(){
        try{
            String filePath = context.getFilesDir().getPath().toString() + "cities.txt";
            File f = new File(filePath);
            if(!f.exists()) return;
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            StringBuffer stringBuffer = new StringBuffer();
            while ((line = br.readLine()) != null) {
                stringBuffer.append(line);
            }
            data = new Gson().fromJson(stringBuffer.toString(), data.getClass());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
