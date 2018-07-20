package com.example.android.sunnydays;

import java.util.ArrayList;

public class WeatherData {
    public String mWeatherType;
    public String mDate;
    public String mTemp;
    public String iconName;

    /**
     * Creates a blank WeatherData object
     */
    public WeatherData() {}

    /**
     * Creates a WeatherData object containing dummy data for testing purposes
     * @param type dummy type
     * @param date dummy date
     * @param temp dummy temp
     * @param icon dummy icon resource name
     */
    public WeatherData(String type, String date, String temp, String icon) {
        this.mWeatherType = type;
        this.mDate = date;
        this.mTemp = temp;
        this.iconName = icon;

    }

    /**
     * This method converts the weather information to a string that is displayed in the
     * RecyclerView
     * @return a String representing the information in the weather forecast
     */
    @Override
    public String toString() {
        return this.mWeatherType + " on "  + this.mDate + " (" + this.mTemp + "°)";
    }

    // Dummy data to populate a list of weather Data
    private static String dummyWeatherType = "Sunny";
    private static String dummyDate = "Sun, 3:00 PM";
    private static String dummyTemp = "62.0";
    private static final String dummyIconName = "icon01d";

    /**
     * Generates a dummy list of WeatherData objects for testing purposes
     * @param numContacts number of dummy objects to create
     * @return an ArrayList of dummy data
     */
    public static ArrayList<WeatherData> createWeatherDataList(int numContacts) {
        ArrayList<WeatherData> weatherDataArrayList = new ArrayList<WeatherData>();

        for (int i = 1; i <= numContacts; i++) {
            weatherDataArrayList.add(new WeatherData(dummyWeatherType, dummyDate, dummyTemp, dummyIconName));
        }

        return weatherDataArrayList;
    }
}
