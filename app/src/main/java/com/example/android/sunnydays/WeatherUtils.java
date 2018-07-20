package com.example.android.sunnydays;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class WeatherUtils {

    // Base URL for the open weather data api
    public static final String OPEN_WEATHER_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast?";

    // Constant for query paramater
    public static final String PARAM_QUERY = "q";

    // Constant for zip parameter
    public static final String PARAM_ZIP = "zip";

    // Tells the open weather api to return the data in the json format
    public static final String PARAM_FORMAT = "format";

    public static final String JSON_FORMAT = "json";

    // Tells open weather api to return the data in farenheit
    public static final String PARAM_UNITS = "units";

    public static final String DESIRED_UNITS = "imperial";

    // Constant for country code
    public static final String PARAM_COUNTRY_CODE = "us";

    // constan for API parameter
    public static final String PARAM_API_KEY = "APPID";


    /**
     * Builds the URL used to query OpenWeatherMap.
     *
     * @param openWeatherSearchQuery The keyword that will be queried for.
     * @return The URL to use to query the weather server.
     */
     public static URL buildUrl(String openWeatherSearchQuery) {
         // build the query URI for OpenWeatherMap
         Uri builtUri = Uri.parse(OPEN_WEATHER_BASE_URL).buildUpon()
                 .appendQueryParameter(PARAM_ZIP, openWeatherSearchQuery)
                 .appendQueryParameter(PARAM_UNITS, DESIRED_UNITS)
                 .appendQueryParameter(PARAM_FORMAT, JSON_FORMAT)
                 .appendQueryParameter(PARAM_API_KEY, BuildConfig.OPEN_WEATHER_MAP_API_KEY)
                 .build();

         URL url = null;
         try {
             url = new URL(builtUri.toString());
         } catch (MalformedURLException e) {
             e.printStackTrace();
         }

         return url;
     }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}

