package com.example.weather;

import android.content.Context;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CitiesList{
    ArrayList<String> data;
    ArrayList<String> cities;
    private Context context;
    private Spinner spinner;

    public CitiesList(final Context context, Spinner spinner){
        this.context = context;
        data = new ArrayList<>();
        cities = new ArrayList<>();
        cities.add("none");
        this.spinner = spinner;
        LoadCities();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {
                final int index = i;
                ExecutorService ser = Executors.newSingleThreadExecutor();
                ser.submit(new Runnable() {
                    @Override
                    public void run() {
                        WeatherLoader loader = new WeatherLoader(cities.get(index), context);
                        Weather weather = loader.GetWeather();
                        MainActivity.weather = weather;
                        String city = cities.get(i);
                        cities.remove(i);
                        cities.add(0, city);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void DisplayCity(String city){
        city = city.toUpperCase();
        System.out.println(city);
        if(data.size() != 0)
        cities = data;
        if(cities.contains(city)){
            cities.remove(city);
            cities.add(0, city);
            if(data.contains(city)) {
                data = cities;
                SaveCities();
            }
        }
        else{
            if(cities.contains("none")) cities.remove("none");
            else if (cities.contains("NONE")) cities.remove("NONE");
            cities.add(0, city);
            SetMenu();
        }
    }

    public void AddCity(){
        String city = spinner.getSelectedItem().toString().toUpperCase();
        if(data.size() != 0)
        cities = data;
        if(cities.contains(city)) Toast.makeText(context, "This city is already saved", Toast.LENGTH_LONG).show();
        else{
            data.add(0, city);
            cities = data;
            SaveCities();
        }
    }

    public void SetMenu(){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_layout, cities);
        adapter.setDropDownViewResource(R.layout.list_spinner);
        spinner.setAdapter(adapter);
    }

    public void SaveCities(){
        data = cities;
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
        if(data.size() > 0)
        cities = data;
    }
}
