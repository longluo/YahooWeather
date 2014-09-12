package com.imlongluo.weather.apis;

/**
 * A callback when querying is completed.
 */
public interface YahooWeatherInfoListener {
    public void gotWeatherInfo(WeatherInfo weatherInfo);
}
