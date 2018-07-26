package com.example.ygrek.mysunpos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class MySunPosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sun_pos);
        EditText editTextLatitude = (EditText) findViewById(R.id.editTextLatitude);
        EditText editTextLongitude = (EditText) findViewById(R.id.editTextLongitude);
        EditText editTextTimeZone = (EditText) findViewById(R.id.editTextTimeZone);
        TextView textViewSunAzimuth = (TextView) findViewById(R.id.textViewSunAzimuth);
        TextView textViewSunAltitude = (TextView) findViewById(R.id.textViewSunAltitude);
        TextView textViewMoonAzimuth = (TextView) findViewById(R.id.textViewMoonAzimuth);
        TextView textViewMoonAltitude = (TextView) findViewById(R.id.textViewMoonAltitude);
        TextView textViewDate = (TextView) findViewById(R.id.textViewDate);

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
    public void onClickCalc(View view){
        EditText editTextLatitude = (EditText) findViewById(R.id.editTextLatitude);
        EditText editTextLongitude = (EditText) findViewById(R.id.editTextLongitude);
        EditText editTexttimeZone = (EditText) findViewById(R.id.editTextTimeZone);
        TextView textViewSunAzimuth = (TextView) findViewById(R.id.textViewSunAzimuth);
        TextView textViewSunAltitude = (TextView) findViewById(R.id.textViewSunAltitude);
        TextView textViewMoonAzimuth = (TextView) findViewById(R.id.textViewMoonAzimuth);
        TextView textViewMoonAltitude = (TextView) findViewById(R.id.textViewMoonAltitude);

        TextView textViewDate = (TextView) findViewById(R.id.textViewDate);

        double lat = Double.parseDouble(editTextLatitude.getText().toString());
        double lng = Double.parseDouble(editTextLongitude.getText().toString());
        int tz = Integer.parseInt(editTexttimeZone.getText().toString());
        Date date = new Date();
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
}
