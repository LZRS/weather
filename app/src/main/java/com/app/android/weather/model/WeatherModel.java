package com.app.android.weather.model;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import java.util.Date;

/**
 * Created by laz on 17/05/17.
 */

public final class WeatherModel {
    private final Pair<Double, Double> longLatitude;
    private final Long date_time;
    private final Double temperatureInCelsius;

    public WeatherModel(Pair<Double, Double> longLatitude, Long date_time, Double temperatureInCelsius) {
        this.longLatitude = longLatitude;
        this.date_time = date_time;
        this.temperatureInCelsius = temperatureInCelsius;
    }

    public Pair<Double, Double> getLongLatitude() {
        return longLatitude;
    }

    public String getDate_time() {
        return new Date(date_time).toString();
    }

    public Double getTemperatureInCelsius() {
        return temperatureInCelsius;
    }

    public static Double convertTemperatureToFahrenheit(@NonNull Double temperatureCelsius){
        return ((temperatureCelsius * 9/5) + 32.0);
    }
}
