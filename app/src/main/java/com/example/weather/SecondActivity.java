package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.ConsoleHandler;

public class SecondActivity extends AppCompatActivity {
    public TextView fd;
    public TextView sd;
    public TextView td;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitty_weather);

        fd = findViewById(R.id.first_date);
        sd = findViewById(R.id.second_date);
        td = findViewById(R.id.third_date);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        SimpleDateFormat curDate = new SimpleDateFormat("dd/MM");
        fd.setText(curDate.format(cal.getTime()));
        cal.add(Calendar.DATE, 1);
        sd.setText(curDate.format(cal.getTime()));
        cal.add(Calendar.DATE, 1);
        td.setText(curDate.format(cal.getTime()));

    }
}