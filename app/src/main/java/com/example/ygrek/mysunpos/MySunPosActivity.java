package com.example.ygrek.mysunpos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
//import java.util.TimeZone;

public class MySunPosActivity extends AppCompatActivity {

    TextView tvEnabledGPS;
    TextView tvStatusGPS;
    TextView tvLocationGPS;
    TextView tvEnabledNet;
    TextView tvStatusNet;
    TextView tvLocationNet;

    EditText editTextLatitude;
    EditText editTextLongitude;
    EditText editTextTimeZone;
    TextView textViewSunAzimuth;
    TextView textViewSunAltitude;
    TextView textViewMoonAzimuth;
    TextView textViewMoonAltitude;

    TextView textViewDate;


    private LocationManager locationManager;
    //StringBuilder sbGPS = new StringBuilder();
    //StringBuilder sbNet = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sun_pos);

        tvEnabledGPS = (TextView) findViewById(R.id.tvEnabledGPS);
        tvStatusGPS = (TextView) findViewById(R.id.tvStatusGPS);
        tvLocationGPS = (TextView) findViewById(R.id.tvLocationGPS);
        tvEnabledNet = (TextView) findViewById(R.id.tvEnabledNet);
        tvStatusNet = (TextView) findViewById(R.id.tvStatusNet);
        tvLocationNet = (TextView) findViewById(R.id.tvLocationNet);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        editTextLatitude = (EditText) findViewById(R.id.editTextLatitude);
        editTextLongitude = (EditText) findViewById(R.id.editTextLongitude);
        editTextTimeZone = (EditText) findViewById(R.id.editTextTimeZone);
        textViewSunAzimuth = (TextView) findViewById(R.id.textViewSunAzimuth);
        textViewSunAltitude = (TextView) findViewById(R.id.textViewSunAltitude);
        //TextView textViewMoonAzimuth = (TextView) findViewById(R.id.textViewMoonAzimuth);
        //TextView textViewMoonAltitude = (TextView) findViewById(R.id.textViewMoonAltitude);
        textViewDate = (TextView) findViewById(R.id.textViewDate);

        editTextLatitude.setText("46.8445");
        editTextLongitude.setText("29.671");
        editTextTimeZone.setText("3");

        textViewSunAzimuth.setText("empty");
        textViewSunAltitude.setText("empty");
        textViewDate.setText("no date");

        TextView textViewYear = (TextView) findViewById(R.id.textViewYear);
        TextView textViewMonth = (TextView) findViewById(R.id.textViewMonth);
        TextView textViewDay = (TextView) findViewById(R.id.textViewDay);
        TextView textViewHour = (TextView) findViewById(R.id.textViewHour);
        textViewYear.setText("Year: ");
        textViewMonth.setText("Month: ");
        textViewDay.setText("Day: ");
        textViewHour.setText("Hour: ");

    }
    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000 * 10, 10, locationListener);
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 1000 * 10, 10,
                locationListener);
        checkEnabled();
    }
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }
    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            checkEnabled();
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onProviderEnabled(String provider) {
            checkEnabled();
            showLocation(locationManager.getLastKnownLocation(provider));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (provider.equals(LocationManager.GPS_PROVIDER)) {
                tvStatusGPS.setText("Status: " + String.valueOf(status));
            } else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
                tvStatusNet.setText("Status: " + String.valueOf(status));
            }
        }
    };
    private void checkEnabled() {
        tvEnabledGPS.setText("Enabled: "
                + locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER));
        tvEnabledNet.setText("Enabled: "
                + locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }

    public void onClickCalc(View view){
        editTextLatitude = (EditText) findViewById(R.id.editTextLatitude);
        editTextLongitude = (EditText) findViewById(R.id.editTextLongitude);
        editTextTimeZone = (EditText) findViewById(R.id.editTextTimeZone);
        textViewSunAzimuth = (TextView) findViewById(R.id.textViewSunAzimuth);
        textViewSunAltitude = (TextView) findViewById(R.id.textViewSunAltitude);
        textViewMoonAzimuth = (TextView) findViewById(R.id.textViewMoonAzimuth);
        textViewMoonAltitude = (TextView) findViewById(R.id.textViewMoonAltitude);

        textViewDate = (TextView) findViewById(R.id.textViewDate);

        double lat = Double.parseDouble(editTextLatitude.getText().toString());
        double lng = Double.parseDouble(editTextLongitude.getText().toString());
        int tz = Integer.parseInt(editTextTimeZone.getText().toString());
        //Date date = new Date();
        Calendar calendar = GregorianCalendar.getInstance();
        //calendar.setTimeZone((TimeZone.getTimeZone("+2")));
        //calendar.add(Calendar.HOUR,2);
        Astronomy.CalculateSunPosition(calendar,lat,lng,tz);
        Astronomy.CalculateMoonPosition(calendar,lat,lng);

        //Sun Position
        textViewSunAzimuth.setText("Sun azi: " + Double.valueOf(Astronomy.sunazimuth*Astronomy.Rad2Deg).toString());
        textViewSunAltitude.setText("Sun alt: " + Double.valueOf(Astronomy.sunaltitude*Astronomy.Rad2Deg).toString());

        //Moon Position
        textViewMoonAzimuth.setText("Moon azi: " + Double.valueOf(Astronomy.moonazimuth*Astronomy.Rad2Deg).toString());
        textViewMoonAltitude.setText("Moon alt: " + Double.valueOf(Astronomy.moonelevation*Astronomy.Rad2Deg).toString());
        //textViewDate.setText(date.toString());
        textViewDate.setText(calendar.getTime().toString());

        TextView textViewYear = (TextView) findViewById(R.id.textViewYear);
        TextView textViewMonth = (TextView) findViewById(R.id.textViewMonth);
        TextView textViewDay = (TextView) findViewById(R.id.textViewDay);
        TextView textViewHour = (TextView) findViewById(R.id.textViewHour);
        textViewYear.setText("Year: " + calendar.get(Calendar.YEAR));
        textViewMonth.setText("Month: " + calendar.get(Calendar.MONTH));
        textViewDay.setText("Day: " + calendar.get(Calendar.DAY_OF_MONTH));
        textViewHour.setText("Hour: " + calendar.get(Calendar.HOUR_OF_DAY));


    }
    private void showLocation(Location location) {
        if (location == null)
            return;
        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            tvLocationGPS.setText(formatLocation(location));
            editTextLatitude.setText(String.valueOf(location.getLatitude()));
            editTextLongitude.setText(String.valueOf(location.getLongitude()));
        } else if (location.getProvider().equals(
                LocationManager.NETWORK_PROVIDER)) {
            tvLocationNet.setText(formatLocation(location));
        }
    }
    private String formatLocation(Location location) {
        if (location == null)
            return "";
        return String.format(
                "Coordinates: lat = %1$.4f, lon = %2$.4f, time = %3$tF %3$tT",
                location.getLatitude(), location.getLongitude(), new Date(
                        location.getTime()));
    }
    public void onClickLocationSettings(View view) {
        startActivity(new Intent(
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    };
}
