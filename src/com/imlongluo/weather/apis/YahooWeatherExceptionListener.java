package com.imlongluo.weather.apis;

public interface YahooWeatherExceptionListener {
    public void onFailConnection(final Exception e);

    public void onFailParsing(final Exception e);

    public void onFailFindLocation(final Exception e);
}
