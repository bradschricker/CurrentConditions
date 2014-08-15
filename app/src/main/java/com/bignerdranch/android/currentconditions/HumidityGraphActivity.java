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

public class HumidityGraphActivity extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mHydrometer;

    private GraphView mGraphView;
    private GraphViewSeries mHumiditySeries;
    private int mSeriesEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSeriesEntry = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humidity_graph);

        mHumiditySeries = new GraphViewSeries(new GraphViewData[] {new GraphViewData(0, 50)});

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mHydrometer = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);

        mGraphView = new LineGraphView(HumidityGraphActivity.this, getResources().getString(R.string.humidity_graph_title));
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

            GraphViewData newData = new GraphViewData(mSeriesEntry, relative_humidity);

            mHumiditySeries.appendData(newData, false, 500);


            mGraphView.addSeries(mHumiditySeries);
            LinearLayout layout = (LinearLayout) findViewById(R.id.graph1);
            layout.removeAllViews();
            layout.addView(mGraphView);
            mSeriesEntry++;
        }
    }

}