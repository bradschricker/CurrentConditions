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

public class PressureGraphActivity extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mBarometer;

    private GraphView mGraphView;
    private GraphViewSeries mPressureSeries;
    private int mSeriesEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSeriesEntry = 1;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humidity_graph);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mBarometer = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);

        mGraphView = new LineGraphView(PressureGraphActivity.this, getResources().getString(R.string.pressure_graph_title));
    }
    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();

        mSensorManager.registerListener(this, mBarometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //  Do something if the accuracy changes
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
            float pressure = event.values[0];

            GraphViewData newData = new GraphViewData(mSeriesEntry, pressure);

            if (mPressureSeries == null) {
                mPressureSeries = new GraphViewSeries(new GraphViewData[] {new GraphViewData(0, pressure)});
            }

            mPressureSeries.appendData(newData, false, 100);

            mGraphView.addSeries(mPressureSeries);
            LinearLayout layout = (LinearLayout) findViewById(R.id.graph1);
            layout.removeAllViews();
            layout.addView(mGraphView);
            mSeriesEntry++;
        }
    }

}