package com.example.weather;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.ActionBar;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CitiesList implements  ActionBar.OnNavigationListener{
    private String[] data = new String[] { "КИЕВ", "ЗАПОРОЖЬЕ", "ХАРЬКОВ" };
    private Context context;
    private ActionBar bar;

    public CitiesList(Context context, ActionBar bar){
        this.bar = bar;
        this.context = context;
    }

    public void SetMenu(){
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, data);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        bar.setListNavigationCallbacks(adapter, this);
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        final int itempos = itemPosition;
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {
                WeatherLoader loader = new WeatherLoader(data[itempos], context);
                loader.GetWeather();
            }
        });
        return true;
    }
}
