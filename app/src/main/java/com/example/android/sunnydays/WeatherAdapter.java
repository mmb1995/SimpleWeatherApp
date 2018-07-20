package com.example.android.sunnydays;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    // A list containing weather forecast information to be displayed
    private List<WeatherData> mWeatherList;

    /**
     * Constructor for a new empty WeatherAdapter
     */
    public WeatherAdapter() {}

    /**
     * Constructor for a new WeatherAdapter containing dummy data for testing purposes
     * @param weatherList a list of dummy weather data
     */
    public WeatherAdapter(List<WeatherData> weatherList) {
        mWeatherList = weatherList;
    }


    /**
     *  This class creates a custom ViewHolder to store weather forecast information.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Reference for views inside viewHolder
        public ImageView iconImageView;
        public TextView weatherItemTextView;

        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            // Gets reference to member views
            iconImageView = (ImageView) itemView.findViewById(R.id.weather_item_icon);
            weatherItemTextView = (TextView) itemView.findViewById(R.id.weather_item_tv);
        }
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param parent The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (like ours does) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new WeatherAdapter.ViewHolder that holds the View for each list item
     */
    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.weather_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, the contents of the ViewHolder are updated to display the weather
     * details for this particular position, using the "position" argument that is passed in.
     *
     * @param holder The ViewHolder which should be updated to represent the
     * contents of the item at the given position in the data set.
     * @param position  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(WeatherAdapter.ViewHolder holder, int position) {
        // Get the weather data based on its position
        WeatherData mWeatherItem = mWeatherList.get(position);

        // Sets icon to be displayed in the image view
        ImageView weatherIconImageView = holder.iconImageView;
        Context context = weatherIconImageView.getContext();
        int iconId = context.getResources().getIdentifier(mWeatherItem.iconName, "drawable",
                context.getPackageName());
        weatherIconImageView.setImageResource(iconId);

        // Sets weather info text for weather description text view to display
        TextView weatherItemTv = holder.weatherItemTextView;
        weatherItemTv.setText(mWeatherItem.toString());

    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout Views and for animations.
     *
     * @return The number of items contained in the weather list
     */
    @Override
    public int getItemCount() {
        if (null == mWeatherList) return 0;
        return mWeatherList.size();
    }

    /**
     * Updates the mWeatherList variable used by the adapter and notifies the recycler view that the data
     * to be displayed has changed
     */
    public void setWeatherList(ArrayList<WeatherData> weatherList) {
        mWeatherList = weatherList;
        notifyDataSetChanged();
    }
}
