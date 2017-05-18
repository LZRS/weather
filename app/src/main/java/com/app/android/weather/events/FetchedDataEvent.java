package com.app.android.weather.events;

import com.app.android.weather.model.WeatherModel;

/**
 * Created by laz on 17/05/17.
 */

public final class FetchedDataEvent {
    private final WeatherModel mWeatherModel;

    public FetchedDataEvent(WeatherModel weatherModel) {
        mWeatherModel = weatherModel;
    }

    public WeatherModel getWeatherModel() {
        return mWeatherModel;
    }
}
