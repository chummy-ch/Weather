package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {
    public Context context;
    public TextView field;
    public Button find;
    public TextView moreWeather;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        find = findViewById(R.id.button);
        field = findViewById(R.id.edit);
        context = this;
        moreWeather = findViewById(R.id.moreWeather);

        getSupportActionBar().setTitle("");

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
                            CitiesList list = new CitiesList(context, getSupportActionBar());
                            list.DisplayCity(field.getText().toString().toUpperCase());
                        }
                    });
                }
                else Toast.makeText(context, "Fill the field", Toast.LENGTH_LONG).show();
            }
        };

        final CitiesList list = new CitiesList(this, getSupportActionBar());
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.cities, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.addCity) {
            if(field.getText().toString().length() < 2) return false;
            CitiesList list = new CitiesList(context, getSupportActionBar());
            list.AddCity(field.getText().toString().toUpperCase());
        }
        return super.onOptionsItemSelected(item);
    }
}