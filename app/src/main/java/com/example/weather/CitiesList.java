package com.example.weather;

import android.content.Context;
import android.icu.text.UnicodeSetSpanner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

public class CitiesList{
    String[] data;
    private Context context;
    private Spinner spinner;

    public CitiesList(final Context context, Spinner spinner){
        this.context = context;
        data = new String[]{};
        this.spinner = spinner;
        LoadCities();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final int index = i;
                ExecutorService ser = Executors.newSingleThreadExecutor();
                ser.submit(new Runnable() {
                    @Override
                    public void run() {
                        WeatherLoader loader = new WeatherLoader(data[index], context);
                        loader.GetWeather();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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

    public void AddCity(){
        String city = spinner.getSelectedItem().toString().toUpperCase();
        for (String c : data) {
            if(c.toLowerCase().trim().equals(city.toLowerCase().trim())) {
                Toast.makeText(context, "This city is already saved", Toast.LENGTH_LONG).show();
                return;
            }
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.spinner_layout, data);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
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
