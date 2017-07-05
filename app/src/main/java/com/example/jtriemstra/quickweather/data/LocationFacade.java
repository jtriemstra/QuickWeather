package com.example.jtriemstra.quickweather.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

import com.example.jtriemstra.quickweather.services.FetchAddressIntentService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * Created by JTriemstra on 7/4/2017.
 */
public class LocationFacade {
    private FusedLocationProviderClient m_objFusedLocationClient;
    private Location m_objLocation;
    private LocationRequest m_objLocationRequest;
    private LocationCallback m_objLocationCallback;
    private AddressResultReceiver m_objResultReceiver;
    private String m_objAddressOutput;
    private static final String TAG = "LocationFacade";
    private boolean m_blnEnableUpdates = false;
    private Context m_objActivityContext;
    private IAddressSuccessCommand m_objSuccessCommand;

    public LocationFacade(Context objActivityContext)
    {
        m_objActivityContext = objActivityContext;
    }

    public void getLocation(IAddressSuccessCommand objSuccessCommand)
    {
        m_objSuccessCommand = objSuccessCommand;

        //TODO: how can I test against the validity of this context, eg when app rotates or goes out of focus?
        m_objFusedLocationClient = LocationServices.getFusedLocationProviderClient(m_objActivityContext);

        //TODO: what can I do with this Handler() parameter?
        m_objResultReceiver = new AddressResultReceiver(new Handler());

        //TODO: under what conditions is this necessary if we subscribe to updates?
        //TODO: removed the activity parameter when going to facade - what does the activity parameter do?
        m_objFusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Log.d(TAG, "fusedLocationClient success");

                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            m_objLocation = location;
                            Log.d(TAG, Double.toString(location.getLatitude()));

                            startIntentService();
                        }
                    }
                });
    }

    public void startPollingForUpdates()
    {
        m_blnEnableUpdates = true;

        createLocationRequest();

        m_objLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    Log.d(TAG, Double.toString(location.getLatitude()));

                    m_objLocation = location;
                }
            };
        };

        startLocationUpdates();
    }

    private void createLocationRequest() {
        m_objLocationRequest = new LocationRequest();
        m_objLocationRequest.setInterval(10000);
        m_objLocationRequest.setFastestInterval(5000);
        m_objLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void startLocationUpdates() {
        if (m_blnEnableUpdates) {
            m_objFusedLocationClient.requestLocationUpdates(m_objLocationRequest,
                    m_objLocationCallback,
                    null /* Looper */);
        }
    }

    public void stopLocationUpdates() {
        if (m_blnEnableUpdates) {
            m_objFusedLocationClient.removeLocationUpdates(m_objLocationCallback);
        }
    }

    private void startIntentService() {
        Log.d(TAG, "starting intent service");
        Intent intent = new Intent(m_objActivityContext, FetchAddressIntentService.class);
        intent.putExtra(FetchAddressIntentService.RECEIVER, m_objResultReceiver);
        intent.putExtra(FetchAddressIntentService.LOCATION_DATA_EXTRA, m_objLocation);

        //TODO: i think there's another way I could do this...PendingIntent?
        m_objActivityContext.startService(intent);
    }

    @SuppressLint("ParcelCreator")
    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
            Log.d(TAG,"creating AddressResultReceiver");
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            Log.d(TAG, "onReceiveResult");

            m_objAddressOutput = resultData.getString(FetchAddressIntentService.RESULT_DATA_KEY);

            if (resultCode == FetchAddressIntentService.SUCCESS_RESULT) {
                Log.d(TAG, "onReceiveResult success");
                m_objSuccessCommand.onSuccess(m_objAddressOutput);
            }
        }
    }
}
