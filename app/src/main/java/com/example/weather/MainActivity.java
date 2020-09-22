package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {
    public Context context;
    public TextView field;
    public Button find;
    public TextView moreWeather;
    public Spinner spinner;
    public TextView saveWeatehr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        moreWeather = findViewById(R.id.moreWeather);
        spinner = findViewById(R.id.spinner);
        field = findViewById(R.id.field);
        find = findViewById(R.id.search_button);
        saveWeatehr = findViewById(R.id.saveButton);

        UIActivity uiActivity = new UIActivity(this);

        View.OnClickListener moreW = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SecondActivity.class);
                startActivity(intent);
            }
        };

        moreWeather.setOnClickListener(moreW);

        final View.OnClickListener findWeather = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (field.getText().toString().trim().length() > 2) {
                    ExecutorService service = Executors.newSingleThreadExecutor();
                    service.submit(new Runnable() {
                        @Override
                        public void run() {
                            WeatherLoader loader = new WeatherLoader(field.getText().toString(), context);
                            loader.GetWeather();
                            CitiesList list = new CitiesList(context, spinner);
                            list.DisplayCity(field.getText().toString().toUpperCase());
                        }
                    });
                }
                else Toast.makeText(context, "Fill the field", Toast.LENGTH_LONG).show();
            }
        };

        final CitiesList list = new CitiesList(this, spinner);
        list.SetMenu();

        find.setOnClickListener(findWeather);

        Thread working = new Thread(new Runnable() {
            @Override
            public void run() {
                WeatherLoader load = new WeatherLoader(context);
                String city = load.LWeather();
                if(city != null) {
                    list.DisplayCity(city);
                }
            }
        });
        working.start();

        saveWeatehr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExecutorService ser = Executors.newSingleThreadExecutor();
                ser.submit(new Runnable() {
                    @Override
                    public void run() {
                        CitiesList list = new CitiesList(context, spinner);
                        list.AddCity();
                    }
                });
            }
        });
    }
}