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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jtriemstra.quickweather.BuildConfig;
import com.example.jtriemstra.quickweather.R;
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

import org.json.JSONObject;

public class MainActivity extends Activity {

    private FusedLocationProviderClient mFusedLocationClient;
    private Location mLocation;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private AddressResultReceiver mResultReceiver;
    private String mAddressOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("Location", "test");

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //TODO: what can I do with this Handler() parameter?
        mResultReceiver = new AddressResultReceiver(new Handler());

        //TODO: under what conditions is this necessary?
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Log.d("Location", "success");
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            mLocation = location;
                            Log.d("Location", Double.toString(location.getLatitude()));
                            TextView objTextView = (TextView) findViewById(R.id.txtOutput);
                            objTextView.setText(Double.toString(location.getLatitude()));

                            startIntentService();
                        }
                    }
                });

        createLocationRequest();

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    Log.d("LocationUpdate", Double.toString(location.getLatitude()));
                    TextView objTextView = (TextView) findViewById(R.id.txtOutput);
                    objTextView.setText(Double.toString(location.getLatitude()));
                    mLocation = location;
                }
            };
        };

        startLocationUpdates();

        WundergroundFactory objFactory = new WundergroundFactory(this);
        objFactory.loadDataByZip("49001", new WeatherSuccessCallback() {
            @Override
            public void onSuccess(Wunderground objResult) {
                
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();

    }

    private void startLocationUpdates() {
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback,
                null /* Looper */);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
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
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startIntentService() {
        Log.d("x", "starting intent service");
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(FetchAddressIntentService.RECEIVER, mResultReceiver);
        intent.putExtra(FetchAddressIntentService.LOCATION_DATA_EXTRA, mLocation);
        startService(intent);
    }

    private void displayAddressOutput()
    {
        Log.d("address", mAddressOutput);
        ((TextView) findViewById(R.id.txtCityOutput)).setText(mAddressOutput);
    }

    @SuppressLint("ParcelCreator")
    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
            Log.d("x","creating AddressResultReceiver");
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            Log.d("x", "onReceiveResult");
            // Display the address string
            // or an error message sent from the intent service.
            mAddressOutput = resultData.getString(FetchAddressIntentService.RESULT_DATA_KEY);


            // Show a toast message if an address was found.
            if (resultCode == FetchAddressIntentService.SUCCESS_RESULT) {
                Log.d("x", "onReceiveResult success");
                displayAddressOutput();
            }

        }
    }
}
