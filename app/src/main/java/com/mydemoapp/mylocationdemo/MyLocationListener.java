package com.mydemoapp.mylocationdemo;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

class MyLocationListener implements LocationListener {
    private static final String TAG = MyLocationListener.class.getSimpleName();
    private Context context;
    private Intent locationUpdateIntent;

    MyLocationListener(Context context) {
        this.context = context;
        locationUpdateIntent = new Intent(AppConstants.LOCATION_UPDATE_CALLBACK);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged: ");
        locationUpdateIntent.putExtra(AppConstants.LOCATION, location);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        localBroadcastManager.sendBroadcast(locationUpdateIntent);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(TAG, "onStatusChanged: ");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG, "onProviderEnabled: ");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(TAG, "onProviderDisabled: ");
    }
}
