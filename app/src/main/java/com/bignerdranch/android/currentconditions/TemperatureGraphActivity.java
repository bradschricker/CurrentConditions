package com.bignerdranch.android.currentconditions;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.LinearLayout;

public class TemperatureGraphActivity extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mThermometer;

    private int mSeriesEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSeriesEntry = 1;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humidity_graph);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mThermometer = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
    }
    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();

        mSensorManager.registerListener(this, mThermometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //  Do something if the accuracy changes
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            float temperature_celcius = event.values[0];
            float temperature_fahrenheit = temperature_celcius * 9 / 5 + 32.0f;
        }
    }

}