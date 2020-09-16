package com.example.weather;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoadUI {
    Weather weather;
    public TextView desc;
    public ImageView sky;
    public TextView temp;
    public TextView minMaxTemp;

    LoadUI(Weather weather, TextView desc, TextView temp, TextView minMaxTemp, ImageView sky){
        this.sky = sky;
        this.temp = temp;
        this.minMaxTemp = minMaxTemp;
        this.desc = desc;
        this.weather = weather;
    }

    public void Loading(){
        temp.setText(weather.temp);
        minMaxTemp.setText(weather.tempMax + " / " + weather.tempMin);
        desc.setText(weather.description + "\n" + "Wind speed: " + weather.speed);
        sky.setImageResource(R.drawable.sun);
}

}
