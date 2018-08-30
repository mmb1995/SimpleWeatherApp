package com.example.android.sunnydays;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Sunny Days";

    // Stores list of forecast results
    private ArrayList<WeatherData> mWeatherList;

    private EditText mSearchWeatherEditText;

    private Button mSearchButton;

    private WeatherAdapter weatherAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Gets reference to RecyclerView
        RecyclerView weatherDataRv = (RecyclerView) findViewById(R.id.weather_recycler_view);

        // Gets reference to the EditText
        mSearchWeatherEditText = (EditText) findViewById(R.id.edit_text_search);

        // Gets reference to search button
        mSearchButton = (Button) findViewById(R.id.search_button);

        // Sets onClickListener for the search button
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeOpenWeatherMapSearchQuery();
            }
        });

        // Set layout manager to position the items
        weatherDataRv.setLayoutManager(new LinearLayoutManager(this));

        // Creates new WeatherAdapter
        weatherAdapter = new WeatherAdapter();

        // Attaches the adapter to the RecyclerView to populate items with weather data
        weatherDataRv.setAdapter(weatherAdapter);
    }

    /**
     * This class creates a custom AsyncTask that fetches weather data from the OpenWeatherMap Api on a
     * background thread, and then once the background task is finished executing the retrieved
     * data is used to populate the RecyclerView that is displayed on the app
     */
    public class OpenWeatherMapQueryTask extends AsyncTask<URL, Void, ArrayList<WeatherData>> {

        @Override
        protected ArrayList<WeatherData> doInBackground(URL... params) {
            URL openWeatherMapUrl = params[0];

            // Stores JSON returned from Open Weather map
            String openWeatherMapSearchResults = null;

            // Builds openWeatherMap search url and catches exceptions
            try {
                openWeatherMapSearchResults = WeatherUtils.getResponseFromHttpUrl(openWeatherMapUrl);
                Log.i("JSON Response: ", openWeatherMapSearchResults);
                ArrayList<WeatherData> weatherJsonData =
                        OpenWeatherMapJsonHelper.getWeatherDataFromJson(openWeatherMapSearchResults);

                // Returns list of weather data
                return weatherJsonData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<WeatherData> weatherJsonData) {
            if (weatherJsonData != null) {

                // Gets references to text views for displaying information about the first occurence
                // of sun in the upcoming forecast
                TextView weatherTimeTv = (TextView) findViewById(R.id.weather_time_information_tv);
                TextView weatherDescriptionTV = (TextView) findViewById(R.id.weather_desciption_tv);
                ImageView iconImageView = (ImageView) findViewById(R.id.icon_image_view);

                // Finds the first time that the forecast calls for sun and displays that information
                // Otherwise displays a message stating there is no sun in the upcoming forecast
                for (int i = 0; i < weatherJsonData.size(); i++) {
                    WeatherData weatherItem = weatherJsonData.get(i);
                    if (weatherItem.mWeatherType.equals("Sun")) {
                        weatherDescriptionTV.setText(R.string.sun_message);
                        weatherTimeTv.setText("On " + weatherItem.mDate);
                        weatherTimeTv.setVisibility(View.VISIBLE);
                        iconImageView.setImageResource(R.drawable.baseline_wb_sunny_black_24dp);
                        iconImageView.setVisibility(View.VISIBLE);
                        break;
                    }

                    // There is no sun in the upcoming forecast
                    if (i == weatherJsonData.size() - 1) {
                        weatherDescriptionTV.setText(R.string.no_sun_message);
                        weatherTimeTv.setVisibility(View.INVISIBLE);
                        iconImageView.setImageResource(R.drawable.baseline_sentiment_very_dissatisfied_black_24dp);
                        iconImageView.setVisibility(View.VISIBLE);
                    }
                }
                weatherAdapter.setWeatherList(weatherJsonData);
            } else {
                //weatherAdapter.setWeatherList(mWeatherList);
                TextView tv = (TextView) findViewById(R.id.weather_desciption_tv);
                tv.setText("Unable to get Weather Data");
            }
        }
    }

    /**
     * This method retrieves the search text from the EditText, constructs the
     * URL (using {@link WeatherUtils}) for the Zip code the user wants weather forecasts about,
     * and finally fires off an AsyncTask to perform the GET request using
     * {@link OpenWeatherMapQueryTask}
     */
    private void makeOpenWeatherMapSearchQuery() {
        // Creates OpenWeatherMap Search Query
        String openWeatherMapQuery = mSearchWeatherEditText.getText().toString();
        URL openWeatherMapUrl = WeatherUtils.buildUrl(openWeatherMapQuery);
        new OpenWeatherMapQueryTask().execute(openWeatherMapUrl);

    }
}
