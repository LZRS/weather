package com.app.android.weather.network;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.util.Log;

import com.app.android.weather.BuildConfig;
import com.app.android.weather.events.FetchedDataEvent;
import com.app.android.weather.model.WeatherModel;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by laz on 17/05/17.
 */

public class WeatherApiService {
    private final String BASE_URL = "http://api.openweathermap.org/";
    private static Retrofit mRetrofit;
    private static WeatherApiService singleWeatherApiService;

    private static final String API_KEY = BuildConfig.OPEN_WEATHER_MAP_API_KEY;
    private static final String UNITS = "metric";
    private static final String CITY_ID = "184745"; // city id for Nairobi

    private WeatherApiService() {
        long cacheSize = 10 * 1024 * 1024; // 10mb
        Cache cache = new Cache(new File(Environment.getDownloadCacheDirectory(), "weather"), cacheSize);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .readTimeout(40, TimeUnit.SECONDS) // 40 seconds
                .writeTimeout(40, TimeUnit.SECONDS)
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static WeatherApiService getSingleWeatherApiService() {
        if (singleWeatherApiService == null) {
            synchronized (WeatherApiService.class) {
                singleWeatherApiService = new WeatherApiService();
            }
        }
        return singleWeatherApiService;
    }

    public void fetchCurrentWeatherData() {
        WeatherApi weatherApi = mRetrofit.create(WeatherApi.class);

        weatherApi.getWeatherData(CITY_ID, API_KEY, UNITS)
                .enqueue(new Callback<JsonElement>() {
                    @Override
                    public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {

                        JsonObject root = response.body().getAsJsonObject();
//                            JSONObject coord = root.get("coord");
                        JsonObject coord = root.getAsJsonObject("coord");
                        Double lon = coord.get("lon").getAsDouble();
                        Double lat = coord.get("lat").getAsDouble();
                        Pair<Double, Double> longLat = new Pair<>(lon, lat);

                        JsonObject main = root.getAsJsonObject("main");
                        Double temp_celsius = main.get("temp").getAsDouble();

                        Long dt = root.get("dt").getAsLong();

                        WeatherModel weatherModel = new WeatherModel(longLat, dt, temp_celsius);
                        EventBus.getDefault().post(new FetchedDataEvent(weatherModel));

                    }

                    @Override
                    public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                        if (BuildConfig.DEBUG) {
                            Log.d(WeatherApi.class.getSimpleName(), "Failed to fetch weather data..");
                            t.printStackTrace();

                        }
                    }
                });
    }
}
