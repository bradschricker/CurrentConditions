package com.bignerdranch.android.currentconditions;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.LinearLayout;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

public class TemperatureGraphActivity extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mThermometer;

    private GraphView mGraphView;
    private GraphViewSeries mTemperatureSeries;
    private int mSeriesEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSeriesEntry = 1;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humidity_graph);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mThermometer = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        mGraphView = new LineGraphView(TemperatureGraphActivity.this, getResources().getString(R.string.temperature_graph_title));
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

            GraphViewData newData = new GraphViewData(mSeriesEntry, temperature_fahrenheit);

            if (mTemperatureSeries == null) {
                mTemperatureSeries = new GraphViewSeries(new GraphViewData[] {new GraphViewData(0, temperature_fahrenheit)});
            }

            mTemperatureSeries.appendData(newData, false, 100);

            mGraphView.addSeries(mTemperatureSeries);
            LinearLayout layout = (LinearLayout) findViewById(R.id.graph1);
            layout.removeAllViews();
            layout.addView(mGraphView);
            mSeriesEntry++;
        }
    }

}