package com.example.android.sunnydays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public final class OpenWeatherMapJsonHelper {

    /**
     * This method parses JSON from a web response by OpenWeatherMap and returns an array list of
     * of WeatherData objects that describe a 5 day weather forecast for a given city.
     *
     * @param openWeatherJson JSON response from server
     * @return ArrayList of WeatherData objects describing the weather forecast
     * in 3 hour increments over a five day period.
     * @throws JSONException if JSON data cannot be properly parsed
     */
    public static ArrayList<WeatherData> getWeatherDataFromJson(String openWeatherJson)
        throws JSONException {

        // Weather information. Each WeatherData objects info is an element of the "list" array
        final String OWM_LIST_PARAM = "list";

        // status code returned in JSON response
        final String OWM_CODE_PARAM = "code";

        ArrayList<WeatherData> weatherList = new ArrayList<WeatherData>();

        JSONObject weatherJson = new JSONObject(openWeatherJson);

        // Checks for error in JSON response
        if (weatherJson.has(OWM_CODE_PARAM)) {
            int errorCode = weatherJson.getInt(OWM_CODE_PARAM);

            switch(errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    // Location invalid
                    return null;
                default:
                    // Server might be down
                    return null;
            }
        }

        // Gets list of weather forecast information from the JSON response
        JSONArray weatherArray = weatherJson.getJSONArray(OWM_LIST_PARAM);

        for(int i = 0; i < weatherArray.length(); i++) {

            // Grabs next forecast object from the JSON array
            JSONObject forecastInfo = weatherArray.getJSONObject(i);

            // Creates new WeatherData object for individual item in JSON array
            WeatherData weatherItem = new WeatherData();

            // Sets the weather type value
            JSONObject weatherJsonObject = forecastInfo.getJSONArray("weather").getJSONObject(0);
            String weatherDescription = weatherJsonObject.getString("main");
            if (weatherDescription.equals("Clear")) {
                weatherDescription = "Sun";
            }
            weatherItem.mWeatherType = weatherDescription;

            // Sets the temperature field for the weatherItem
            JSONObject mainJsonObject = forecastInfo.getJSONObject("main");
            weatherItem.mTemp = mainJsonObject.getString("temp");

            // Sets the day and time field for the weatherItem
            long dt = forecastInfo.getLong("dt");
            Date date = new Date(dt * 1000L);
            SimpleDateFormat format = new SimpleDateFormat("EEE ',' K a");
            format.setTimeZone(TimeZone.getTimeZone("GMT-8"));
            String formatedDate = format.format(date);
            weatherItem.mDate = formatedDate;

            // Sets the image icon field for the weatherItem
            String iconName ="icon" + weatherJsonObject.getString("icon");
            weatherItem.iconName = iconName;

            // Add weatherItem to the list of weatherItems
            weatherList.add(weatherItem);

        }

        // Returns a list of WeatherData objects to be displayed
        return weatherList;
    }
}
