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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        moreWeather = findViewById(R.id.moreWeather);
        spinner = findViewById(R.id.spinner);

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

        /*find.setOnClickListener(findWeather);*/

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

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.cities, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

    // handle button activities
    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.addCity) {
            CitiesList list = new CitiesList(context, spinner);
            list.AddCity();
        }
        return super.onOptionsItemSelected(item);
    }*/
}