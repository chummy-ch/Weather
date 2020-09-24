package com.example.weather;

import android.app.Activity;

public class UIActivity {

    public static Activity mianActivity;
    public static Activity weatherActivity;

    public UIActivity(Activity activity){UIActivity.mianActivity = activity;}

    public UIActivity(Activity activity, int none){UIActivity.weatherActivity = activity;}
}
