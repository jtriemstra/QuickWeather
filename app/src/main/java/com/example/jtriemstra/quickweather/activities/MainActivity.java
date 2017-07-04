package com.example.jtriemstra.quickweather.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.jtriemstra.quickweather.R;
import com.example.jtriemstra.quickweather.data.VolleySingleton;
import com.example.jtriemstra.quickweather.data.WeatherSuccessCallback;
import com.example.jtriemstra.quickweather.data.Wunderground;
import com.example.jtriemstra.quickweather.data.WundergroundFactory;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import com.example.jtriemstra.quickweather.services.FetchAddressIntentService;


public class MainActivity extends Activity {

    private FusedLocationProviderClient m_objFusedLocationClient;
    private Location m_objLocation;
    private LocationRequest m_objLocationRequest;
    private LocationCallback m_objLocationCallback;
    private AddressResultReceiver m_objResultReceiver;
    private String m_objAddressOutput;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_objFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //TODO: what can I do with this Handler() parameter?
        m_objResultReceiver = new AddressResultReceiver(new Handler());

        //TODO: under what conditions is this necessary?
        m_objFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
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

        WundergroundFactory objFactory = new WundergroundFactory(getApplicationContext());
        objFactory.loadDataByZip("49001", new WeatherSuccessCallback() {
            @Override
            public void onSuccess(Wunderground objResult) {
                try
                {
                    ((TextView) findViewById(R.id.txtTemperatureOutput)).setText(objResult.getTemperature());
                    ((TextView) findViewById(R.id.txtDewPointOutput)).setText(objResult.getDewpoint());
                    ((NetworkImageView) findViewById(R.id.vwRadarImage)).setImageUrl(objResult.getRadarImageUrl(), VolleySingleton.getInstance(getApplicationContext()).getImageLoader());

                    Log.d(TAG, objResult.getRadarImageUrl());
                }
                catch (Exception e)
                {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();

    }

    private void startLocationUpdates() {
        m_objFusedLocationClient.requestLocationUpdates(m_objLocationRequest,
                m_objLocationCallback,
                null /* Looper */);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        m_objFusedLocationClient.removeLocationUpdates(m_objLocationCallback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void createLocationRequest() {
        m_objLocationRequest = new LocationRequest();
        m_objLocationRequest.setInterval(10000);
        m_objLocationRequest.setFastestInterval(5000);
        m_objLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startIntentService() {
        Log.d(TAG, "starting intent service");
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(FetchAddressIntentService.RECEIVER, m_objResultReceiver);
        intent.putExtra(FetchAddressIntentService.LOCATION_DATA_EXTRA, m_objLocation);
        startService(intent);
    }

    private void displayAddressOutput()
    {
        Log.d(TAG, m_objAddressOutput);
        ((TextView) findViewById(R.id.txtCityOutput)).setText(m_objAddressOutput);
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
                displayAddressOutput();
            }
        }
    }
}
