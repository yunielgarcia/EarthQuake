/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<EarthQuake>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    ArrayList<EarthQuake> earthquakes = null;


    // Create a new {@link ArrayAdapter} of earthquakes
    QuakeAdapter mQuakeAdapter;

    ListView mEarthquakeListView;

    TextView mEmptyView;

    ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);


        // Find a reference to the {@link ListView} in the layout
        mEarthquakeListView = findViewById(R.id.list);

        // List for empty state
        mEmptyView = findViewById(R.id.empty_view);
        mEarthquakeListView.setEmptyView(mEmptyView);


        // Create a new adapter that takes an empty list of earthquakes as input
        mQuakeAdapter = new QuakeAdapter(this, new ArrayList<EarthQuake>());


        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        mEarthquakeListView.setAdapter(mQuakeAdapter);

        // Progress Indicator
        mProgressBar = findViewById(R.id.progress_indicator);


        mEarthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Uri webpage = Uri.parse(earthquakes.get(i).getmUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        if (QueryUtils.isConnected(this)) {
            getLoaderManager().initLoader(1, null, EarthquakeActivity.this);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mEmptyView.setText("No internet found.");
        }


    }

    @Override
    public Loader<List<EarthQuake>> onCreateLoader(int i, Bundle bundle) {
        return new EarthquakeLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<EarthQuake>> loader, List<EarthQuake> earthQuakes) {

        mProgressBar.setVisibility(View.GONE);

        if (earthQuakes != null || earthQuakes.size() != 0) {
            mQuakeAdapter.clear();
            earthquakes = (ArrayList<EarthQuake>) earthQuakes;
            mQuakeAdapter.addAll(earthQuakes);
        } else {
            mEmptyView.setText("No earthquakes found.");
        }
    }

    @Override
    public void onLoaderReset(Loader<List<EarthQuake>> loader) {
        mQuakeAdapter.addAll(new ArrayList<EarthQuake>());
    }


}
