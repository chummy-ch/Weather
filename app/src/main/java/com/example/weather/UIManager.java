package com.example.weather;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Contacts;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class UIManager {
    private Activity activity;
    private TextView temp;
    private TextView desc;
    private TextView windSpeed;
    private ImageView sky;
    private TextView pressure;
    private TextView humidity;

    public UIManager(){

    }

    public void UpdateUI(ImageView view, TextView temp, TextView disc, ShortWeather weather){
        view.setBackgroundResource(weather.image);
        temp.setText((int)Math.ceil(Float.parseFloat(weather.temp) - 273.15) + "°С");
        disc.setText(weather.disc);
    }

    public void UI(ArrayList<ShortWeather> weathers){
        final Activity a = UIActivity.weatherActivity;
        final ArrayList<ShortWeather> w = weathers;
        Handler shortWeatherHandler = new Handler(Looper.getMainLooper()){
            public void handleMessage(Message msg){
                temp = a.findViewById(R.id.temp_fd);
                desc = a.findViewById(R.id.d_fd);
                sky = a.findViewById(R.id.fIV);
                UpdateUI(sky, temp, desc, w.get(0));
                temp = a.findViewById(R.id.temp_sd);
                desc = a.findViewById(R.id.d_sd);
                sky = a.findViewById(R.id.sIV);
                UpdateUI(sky, temp, desc, w.get(1));
                temp = a.findViewById(R.id.temp_thd);
                desc = a.findViewById(R.id.d_thd);
                sky = a.findViewById(R.id.tIV);
                UpdateUI(sky, temp, desc, w.get(2));
            }
        };
        shortWeatherHandler.sendEmptyMessage(0);
    }

    public Handler GetWeatherHandler(){
        activity = UIActivity.mianActivity;
        temp = activity.findViewById(R.id.temp);
        desc = activity.findViewById(R.id.disc);
        windSpeed = activity.findViewById(R.id.windTv);
        sky = activity.findViewById(R.id.weatherImage);
        pressure = activity.findViewById(R.id.pressureTv);
        humidity = activity.findViewById(R.id.waterTv);
        Handler weatherHandler = new Handler(Looper.getMainLooper()){
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
