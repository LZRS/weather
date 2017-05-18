package com.app.android.weather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.app.android.weather.events.FetchedDataEvent;
import com.app.android.weather.model.WeatherModel;
import com.app.android.weather.network.WeatherApiService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView mDateTextView;
    private TextView mCelsius;
    private TextView mFahrenheit;
    private TextView mLongitude;
    private TextView mLatitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ButterKnife.bind(this);
        mDateTextView = (TextView) findViewById(R.id.date_time);
        mCelsius = (TextView) findViewById(R.id.temp_in_celsius);
        mFahrenheit = (TextView) findViewById(R.id.temp_in_fahrenheit);
        mLongitude = (TextView) findViewById(R.id.longitude);
        mLatitude = (TextView) findViewById(R.id.latitude);
    }

    @Override
    protected void onStart() {
        super.onStart();
        WeatherApiService.getSingleWeatherApiService().fetchCurrentWeatherData();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode=ThreadMode.MAIN)
    public void doItemsFetched(FetchedDataEvent fetchedDataEvent){
        WeatherModel weatherModel = fetchedDataEvent.getWeatherModel();

        mDateTextView.setText(weatherModel.getDate_time());
        mCelsius.setText(String.format(Locale.ENGLISH, "%.2f celsius", weatherModel.getTemperatureInCelsius()));
        mFahrenheit.setText(String.format(Locale.ENGLISH, "%.2f fahrenheit",
                WeatherModel.convertTemperatureToFahrenheit(weatherModel.getTemperatureInCelsius())));
        mLongitude.setText(String.format(Locale.ENGLISH, "Lon: %.2f", weatherModel.getLongLatitude().first));
        mLatitude.setText(String.format(Locale.ENGLISH, "Lat: %.2f", weatherModel.getLongLatitude().second));
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
