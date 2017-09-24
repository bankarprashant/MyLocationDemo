package com.mydemoapp.mylocationdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private LocationBroadcastReceiver locationBroadcastReceiver;
    @BindView(R.id.locationTextView)
    TextView locationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Log.d(TAG, "onCreate: ");
        locationBroadcastReceiver = new LocationBroadcastReceiver();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        startService(new Intent(this, LocationService.class));
        LocalBroadcastManager.getInstance(this).registerReceiver(locationBroadcastReceiver,
                new IntentFilter(AppConstants.LOCATION_UPDATE_CALLBACK));
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(new Intent(this, LocationService.class));
        Log.d(TAG, "onStop: ");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(locationBroadcastReceiver);
    }

    private class LocationBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(AppConstants.LOCATION);
            Log.d(TAG, "onReceive: " + " LAT :" + location.getLatitude() + " LONG : " + location.getLongitude());
            locationTextView.setText(Html.fromHtml(getString(R.string.lat_long_text, location.getLatitude(), location.getLongitude())));
        }
    }
}
