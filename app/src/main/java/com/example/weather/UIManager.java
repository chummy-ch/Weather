package com.example.weather;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class UIManager {
    private Activity activity;
    private Handler weatherHandler;
    private TextView temp;
    private TextView desc;
    private TextView windSpeed;
    private ImageView sky;
    private TextView pressure;
    private TextView humidity;

    public UIManager(){
        this.activity = UIActivity.activity;
        temp = activity.findViewById(R.id.temp);
        desc = activity.findViewById(R.id.disc);
        windSpeed = activity.findViewById(R.id.windTv);
        sky = activity.findViewById(R.id.weatherImage);
        pressure = activity.findViewById(R.id.pressureTv);
        humidity = activity.findViewById(R.id.waterTv);
    }



    public Handler GetWeatherHandler(){
        weatherHandler = new Handler(Looper.getMainLooper()){
            public void handleMessage(Message msg){
                Weather weather = (Weather) msg.obj;
                temp.setText(String.valueOf(weather.temp) + "°С");
                desc.setText(weather.description);
                windSpeed.setText(weather.speed + "m/s");
                sky.setBackgroundResource(weather.weatherImage);
                pressure.setText(weather.pressure + " hpa");
                humidity.setText(weather.humidity + "%");
            }
        };
        return weatherHandler;
    }
}
