package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        find = findViewById(R.id.button);
        field = findViewById(R.id.edit);
        context = this;

        getSupportActionBar().setTitle("");

        UIActivity uiActivity = new UIActivity(this);

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
                        }
                    });
                }
                else Toast.makeText(context, "Fill the field", Toast.LENGTH_LONG).show();
            }
        };

        CitiesList list = new CitiesList(this, getSupportActionBar());
        list.SetMenu();

        find.setOnClickListener(findWeather);

        Thread working = new Thread(new Runnable() {
            @Override
            public void run() {
                WeatherLoader load = new WeatherLoader(context);
                load.LWeather();
            }
        });
        working.start();
    }
}