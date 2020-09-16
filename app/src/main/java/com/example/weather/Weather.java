package com.example.weather;

public class Weather {
    public String city;
    public int temp;
    public int tempLike;
    public int tempMin;
    public int tempMax;
    public String main;
    public String description;
    public float speed;

    public void setCity(String city){this.city = city;}

    public void setTemp(int temp){this.temp = temp;}

    public void setTempLike(int tempLike){this.tempLike = tempLike;}

    public void setMain(String main){this.main = main;}

    public void setTempMin(int tempMin){this.tempMin = tempMin;}

    public void setTempMax(int tempMax){this.tempMax = tempMax;}

    public void setSpeed(float speed){this.speed = speed;}

    public void setDescription(String description){this.description = description;}
}
