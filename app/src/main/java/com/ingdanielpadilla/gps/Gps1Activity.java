package com.ingdanielpadilla.gps;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class Gps1Activity extends AppCompatActivity implements LocationListener, OnMapReadyCallback {

    private static final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private GoogleMap mMap;
    TextView vlat, vlon, tdata;
    String data;
    LocationManager mLocationManager;
    View.OnClickListener handler;
    ToggleButton tb1,tb2,tb3;
    int accuracy=Criteria.ACCURACY_LOW,power=Criteria.POWER_LOW;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps1);
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
                            accuracy=Criteria.ACCURACY_LOW;
                            power=Criteria.POWER_LOW;

                            Snackbar.make(view, "gps1", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            break;

                        case R.id.tb2:
                            tb1.setChecked(false);
                            tb3.setChecked(false);
                            accuracy=Criteria.ACCURACY_MEDIUM;
                            power=Criteria.POWER_MEDIUM;

                            Snackbar.make(view, "gps2", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            break;
                        case R.id.tb3:
                            tb1.setChecked(false);
                            tb2.setChecked(false);
                            accuracy=Criteria.ACCURACY_HIGH;
                            power=Criteria.POWER_HIGH;

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
        tdata = (TextView) findViewById(R.id.data);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

       // SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
         //       .findFragmentById(R.id.map);
       // mapFragment.getMapAsync(this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, Long.toString(System.currentTimeMillis()), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopSensor();
    }

    @Override
    public void onLocationChanged(Location location) {
        vlat.setText(location.getLatitude() + "");
        vlon.setText(location.getLongitude() + "");
        data=data+Double.toString(location.getLatitude())+" "+ Double.toString(location.getLongitude())+" \n";
        tdata.setText(data);

        //centerMap(new LatLng(location.getLatitude(), location.getLongitude()));

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

    @TargetApi(Build.VERSION_CODES.M)
    public void startGPS(View view) {

        verifyGPS();
        Snackbar.make(view, "gps1", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        mLocationManager.requestLocationUpdates(getBestProvider(), 0, 0, this);
    }

    public void stopGPS(View view) {
        stopSensor();
    }

    private boolean verifyGPS() {
        boolean enable = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enable) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Gps no esta habilitado");
            builder.setMessage("habilitar ahora??");
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setCancelable(false);
            builder.setNegativeButton("ahora no", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    return;
                }
            });
            builder = builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                    return;
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            return false;
        }
        return enable;
    }

    private String getBestProvider() {
        Criteria mCriteria = new Criteria();
        mCriteria.setHorizontalAccuracy(accuracy);
        mCriteria.setVerticalAccuracy(accuracy);
        mCriteria.setPowerRequirement(power);
        String bestProvider = mLocationManager.getBestProvider(mCriteria, true);
        Snackbar.make(findViewById(R.id.lat), "Provider: " + bestProvider, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        return bestProvider;
    }

    private void stopSensor() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        mLocationManager.removeUpdates(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        /* mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = mLocationManager.getLastKnownLocation(getBestProvider());
        if(location!=null) {
            LatLng lastLocation = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(lastLocation).title("Home?"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(lastLocation));
        }

        */
    }

    private void centerMap(LatLng mapCenter){
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mapCenter));

        CameraPosition cameraposition = CameraPosition.builder()
                .target(mapCenter)
                .zoom(10.f)
                .bearing(0f)
                .tilt(45)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraposition),1000,null);
    }
}
