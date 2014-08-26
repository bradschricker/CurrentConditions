package com.bignerdranch.android.currentconditions;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.LinearLayout;

public class HydrometerActivity extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mHydrometer;

    private int mSeriesEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSeriesEntry = 1;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humidity_graph);

        //mHumiditySeries = new GraphViewSeries(new GraphViewData[] {new GraphViewData(0, 50)});

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mHydrometer = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);

    }
    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();

        mSensorManager.registerListener(this, mHydrometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //  Do something if the accuracy changes
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
            float relative_humidity = event.values[0];

        }
    }

}