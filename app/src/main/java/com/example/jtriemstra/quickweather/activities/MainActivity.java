package com.example.jtriemstra.quickweather.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.jtriemstra.quickweather.R;
import com.example.jtriemstra.quickweather.data.location.IAddressSuccessCommand;
import com.example.jtriemstra.quickweather.data.weather.IWeather;
import com.example.jtriemstra.quickweather.data.weather.IWeatherFactory;
import com.example.jtriemstra.quickweather.data.location.LocationFacade;
import com.example.jtriemstra.quickweather.data.VolleySingleton;
import com.example.jtriemstra.quickweather.data.weather.IWeatherSuccessCommand;
import com.example.jtriemstra.quickweather.data.weather.WundergroundFactory;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private LocationFacade m_objLocationFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "Image width before load: " + ((NetworkImageView) findViewById(R.id.vwRadarImage)).getWidth());


        m_objLocationFacade = new LocationFacade(this);

        m_objLocationFacade.getLocation(new IAddressSuccessCommand() {
            @Override
            public void onSuccess(String strAddress, String strZip) {
                displayAddressOutput(strAddress);

                //TODO: default if the address lookup fails? show toast so user knows?
                IWeatherFactory objFactory = new WundergroundFactory(getApplicationContext());

                objFactory.loadDataByZip(strZip, new IWeatherSuccessCommand() {
                    @Override
                    public void onSuccess(IWeather objResult) {
                        displayWeatherInfo(objResult);
                    }
                });
            }
        });

        //m_objLocationFacade.startPollingForUpdates();


    }

    @Override
    protected void onResume() {
        super.onResume();
        m_objLocationFacade.startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        m_objLocationFacade.stopLocationUpdates();
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



    private void displayAddressOutput(String strAddress)
    {
        ((TextView) findViewById(R.id.txtCityOutput)).setText(strAddress);
    }

    private void displayWeatherInfo(IWeather objResult)
    {
        try {
            ((TextView) findViewById(R.id.txtTemperatureOutput)).setText(objResult.getTemperature());
            ((TextView) findViewById(R.id.txtDewPointOutput)).setText(objResult.getDewpoint());
            ((NetworkImageView) findViewById(R.id.vwRadarImage)).setImageUrl(objResult.getRadarImageUrl(), VolleySingleton.getInstance(getApplicationContext()).getImageLoader());

            Log.d(TAG, objResult.getRadarImageUrl());
            Log.d(TAG, "Image width after load: " + ((NetworkImageView) findViewById(R.id.vwRadarImage)).getWidth());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
