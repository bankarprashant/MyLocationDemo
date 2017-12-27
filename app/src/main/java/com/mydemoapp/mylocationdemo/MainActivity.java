package com.mydemoapp.mylocationdemo;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
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
        if (isLocationPermissionGranted()) {
            getLocation();
        }
    }

    private void getLocation() {
        if (!isGpsEnabled() && !isNetworkEnabled()) {
            showEnableServicePrompt(getString(R.string.gps));
        } else {
            startService(new Intent(this, LocationService.class));
            LocalBroadcastManager.getInstance(this).registerReceiver(locationBroadcastReceiver,
                    new IntentFilter(AppConstants.LOCATION_UPDATE_CALLBACK));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(new Intent(this, LocationService.class));
        Log.d(TAG, "onStop: ");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(locationBroadcastReceiver);
    }

    private class LocationBroadcastReceiver extends BroadcastReceiver {
        @SuppressWarnings("deprecation")
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(AppConstants.LOCATION);
            Log.d(TAG, "onReceive: " + " LAT :" + location.getLatitude() + " LONG : " + location.getLongitude());
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N)
                locationTextView.setText(Html.fromHtml(getString(R.string.lat_long_text, location.getLatitude(), location.getLongitude()), Html.FROM_HTML_MODE_LEGACY));
            else
                locationTextView.setText(Html.fromHtml(getString(R.string.lat_long_text, location.getLatitude(), location.getLongitude())));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        getLocation();
                    }
                } else finish();
        }
    }
}
