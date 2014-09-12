package com.imlongluo.weather.apis;

public class YahooWeatherConsts {

    public static final String XML_TAG_WOEID_QUALITY = "quality";
    public static final String XML_TAG_WOEID_LATITUDE = "latitude";
    public static final String XML_TAG_WOEID_LONGITUDE = "longitude";
    public static final String XML_TAG_WOEID_OFFSETLATITUDE = "offsetlat";
    public static final String XML_TAG_WOEID_OFFSETLONGITUDE = "offsetlon";
    public static final String XML_TAG_WOEID_RADIUS = "radius";
    public static final String XML_TAG_WOEID_NAME = "name";
    public static final String XML_TAG_WOEID_ADDITION = "addition";
    public static final String XML_TAG_WOEID_POSTAL = "postal";
    public static final String XML_TAG_WOEID_NEIGHBORHOOD = "neighborhood";
    public static final String XML_TAG_WOEID_CITY = "city";
    public static final String XML_TAG_WOEID_COUNTY = "county";
    public static final String XML_TAG_WOEID_COUNTRY = "country";
    public static final String XML_TAG_WOEID_COUNTRYCODE = "countrycode";
    public static final String XML_TAG_WOEID_STATE = "state";
    public static final String XML_TAG_WOEID_STATECODE = "statecode";
    public static final String XML_TAG_WOEID_WOEID = "woeid";

    public static final String[] WOEID_RESULT_TAG_LSIT = new String[] { XML_TAG_WOEID_QUALITY,
            XML_TAG_WOEID_LATITUDE, XML_TAG_WOEID_LONGITUDE, XML_TAG_WOEID_OFFSETLATITUDE,
            XML_TAG_WOEID_OFFSETLONGITUDE, XML_TAG_WOEID_RADIUS, XML_TAG_WOEID_NAME,
            XML_TAG_WOEID_ADDITION, XML_TAG_WOEID_POSTAL, XML_TAG_WOEID_NEIGHBORHOOD,
            XML_TAG_WOEID_CITY, XML_TAG_WOEID_COUNTY, XML_TAG_WOEID_COUNTRY,
            XML_TAG_WOEID_COUNTRYCODE, XML_TAG_WOEID_STATE, XML_TAG_WOEID_STATECODE,
            XML_TAG_WOEID_WOEID, };

    public static final String[] CONDITION_LIST = new String[] { "tornado", "tropical storm",
            "hurricane", "severe thunderstorms", "thunderstorms", "mixed rain and snow",
            "mixed rain and sleet", "mixed snow and sleet", "freezing drizzle", "drizzle",
            "freezing rain", "showers", "showers", "snow flurries", "light snow showers",
            "blowing snow", "snow", "hail", "sleet", "dust", "foggy", "haze", "smoky", "blustery",
            "windy", "cold", "cloudy", "mostly cloudy (night)", "mostly cloudy (day)",
            "partly cloudy (night)", "partly cloudy (day)", "clear (night)", "sunny",
            "fair (night)", "fair (day)", "mixed rain and hail", "hot", "isolated thunderstorms",
            "scattered thunderstorms", "scattered thunderstorms", "scattered showers",
            "heavy snow", "scattered snow showers", "heavy snow", "partly cloudy",
            "thundershowers", "snow showers", "isolated thundershowers", "not available", };

    public static String getConditionByCode(int code) {
        return CONDITION_LIST[code];
    }

}
