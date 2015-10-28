package com.ingdanielpadilla.gps;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class Gps2Activity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener,com.google.android.gms.location.LocationListener {

    private GoogleApiClient mGoogleApiClient;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS=10000,FASTED_UPDATE_INTERVAL_IN_MILLISECONDS=UPDATE_INTERVAL_IN_MILLISECONDS/2;
    LocationRequest mLocationRequest;
    TextView vlat,vlon, tdata;
    String data;
    Integer priority=LocationRequest.PRIORITY_LOW_POWER;
    View.OnClickListener handler;
    ToggleButton tb1,tb2,tb3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tb1 = (ToggleButton)findViewById(R.id.tb1);
        tb2 = (ToggleButton)findViewById(R.id.tb2);
        tb3 = (ToggleButton)findViewById(R.id.tb3);

        tb1.setChecked(true);

        handler = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToggleButton tg = (ToggleButton) view;
                if (tg.isChecked()) {
                    switch (view.getId()) {
                        case R.id.tb1:
                            tb2.setChecked(false);
                            tb3.setChecked(false);
                            priority=LocationRequest.PRIORITY_LOW_POWER;

                            Snackbar.make(view, "gps1", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            break;

                        case R.id.tb2:
                            tb1.setChecked(false);
                            tb3.setChecked(false);
                            priority=LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;

                            Snackbar.make(view, "gps2", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            break;
                        case R.id.tb3:
                            tb1.setChecked(false);
                            tb2.setChecked(false);
                            priority=LocationRequest.PRIORITY_HIGH_ACCURACY;

                            Snackbar.make(view, "gps2", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            break;

                        default:
                            break;
                    }

                }
            }
        };

        tb1.setOnClickListener(handler);
        tb2.setOnClickListener(handler);
        tb3.setOnClickListener(handler);

        data="";

        vlat = (TextView) findViewById(R.id.lat);
        vlon = (TextView) findViewById(R.id.lon);
        tdata= (TextView) findViewById(R.id.data);

        mGoogleApiClient=new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest=new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTED_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(priority);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        Snackbar.make(findViewById(R.id.fab), "Connecting" , Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Snackbar.make(findViewById(R.id.fab), "Connected" , Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        vlat.setText(location.getLatitude() + "");
        vlon.setText(location.getLongitude() + "");
        data=data+Double.toString(location.getLatitude())+" "+ Double.toString(location.getLongitude())+" \n";
        tdata.setText(data);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void startGPS(View view) {
        if(!mGoogleApiClient.isConnected())
        {
           mGoogleApiClient.connect();
        }
       LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
    }

    public void stopGPS(View view) {
        stopLocationUpdates();
    }
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
    }
}
