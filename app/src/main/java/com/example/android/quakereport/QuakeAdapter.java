package com.example.android.quakereport;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.graphics.drawable.GradientDrawable;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class QuakeAdapter extends ArrayAdapter<EarthQuake> {

    private static final String LOG_TAG = QuakeAdapter.class.getSimpleName();

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists.
     *
     * @param context The current context. Used to inflate the layout file.
     * @param quakes  A List of Quakes objects to display in a list
     */
    public QuakeAdapter(Context context, ArrayList<EarthQuake> quakes) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, quakes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.quake_list_item, parent, false);
        }

        // Get the {@link Word} object located at this position in the list
        EarthQuake currentQuake = getItem(position);

        // Find the view in the list_item.xml layout with the ID
        TextView magnitudeTextView = (TextView) listItemView.findViewById(R.id.magnitud_tv);
        TextView locationDistanceTextView = listItemView.findViewById(R.id.distance_tv);
        TextView locationTextView = listItemView.findViewById(R.id.location_tv);
        TextView dateTextView = listItemView.findViewById(R.id.date_tv);

        // Setting the value to display
        dateTextView.setText(currentQuake.getmDate());
        magnitudeTextView.setText(currentQuake.getmMagnitudeFormatted());
        String location = currentQuake.getmLocation();

        try {
            String[] locArr = location.split("of");
            locationDistanceTextView.setText(locArr[0] + " OF");
            locationTextView.setText(locArr[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            // In case doesn't have 'distance of'
            locationDistanceTextView.setVisibility(View.GONE);
            locationTextView.setText(location);
        }

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeTextView.getBackground();
        // Get the appropriate background color based on the current earthquake magnitude
        String stringMagnitudeColor = getMagnitudeColorName(currentQuake.getmMagnitude());
        int resourceIdFromStringName = getResId(stringMagnitudeColor, R.color.class);

        magnitudeCircle.setColor(ContextCompat.getColor(getContext(), resourceIdFromStringName));

        return listItemView;

    }

    private String getMagnitudeColorName(double doubleMag) {
        int intMag = (int) doubleMag;
        return "magnitude" + intMag;
    }

    // https://stackoverflow.com/questions/4427608/android-getting-resource-id-from-string
    // also...
    //https://stackoverflow.com/questions/15488238/using-android-getidentifier
    private static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

}