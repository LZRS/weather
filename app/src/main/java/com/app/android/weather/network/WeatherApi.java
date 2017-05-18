package com.app.android.weather.network;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by laz on 17/05/17.
 */

interface WeatherApi {

    @GET("data/2.5/weather/")
    Call<JsonElement> getWeatherData(@Query("id") String cityId,
                                     @Query("appid") String apiKey,
                                     @Query("units") String units);
}
