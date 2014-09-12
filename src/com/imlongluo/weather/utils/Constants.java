package com.imlongluo.weather.utils;

public class Constants {

    public static final String YAHOO_URL = "http://weather.yahooapis.com/forecastrss";

    public static final String WOEID_SHENZHEN = "2161853";

    public static String getURL() {
        StringBuilder urlBuilder = new StringBuilder();

        urlBuilder.append(YAHOO_URL).append("&w=").append(WOEID_SHENZHEN);

        return urlBuilder.toString();
    }


}
