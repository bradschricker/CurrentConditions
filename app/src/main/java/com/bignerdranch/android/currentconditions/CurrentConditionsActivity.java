package com.bignerdranch.android.currentconditions;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.NumberFormat;


public class CurrentConditionsActivity extends Activity implements SensorEventListener {

    private TextView mTimeTextView;
    private TextView mTemperatureTextView;
    private TextView mHumidityTextView;
    private TextView mPressureTextView;

    private SensorManager mSensorManager;
    private Sensor mThermometer;
    private Sensor mHydrometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_conditions);

        mTimeTextView = (TextView) findViewById(R.id.time_text_view);
        mTemperatureTextView = (TextView) findViewById(R.id.temperature_text_view);
        mHumidityTextView = (TextView) findViewById(R.id.humidity_text_view);
        mPressureTextView = (TextView) findViewById(R.id.pressure_text_view);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mThermometer = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        mHydrometer = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);

    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        mSensorManager.registerListener(this, mThermometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mHydrometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.current_conditions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //  Do something if the accuracy changes
    }

    public void onSensorChanged(SensorEvent event) {
        NumberFormat output_format = NumberFormat.getNumberInstance();
        output_format.setMinimumFractionDigits(2);
        output_format.setMaximumFractionDigits(2);

        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            float temperature_celcius = event.values[0];
            float temperature_fahrenheit = temperature_celcius * 9 / 5 + 32.0f;

            String temperature_output = output_format.format(temperature_fahrenheit);
            mTemperatureTextView.setText(temperature_output + (char) 0x00B0 + " F");
        }

        if (event.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
            float relative_humidity = event.values[0];

            String relative_humidity_output = output_format.format(relative_humidity);
            String humidity_label = getResources().getString(R.string.humidity);
            mHumidityTextView.setText(relative_humidity_output + "% " + humidity_label);
        }
    }
}
