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


public class CurrentConditionsActivity extends Activity implements SensorEventListener {

    private TextView mTimeTextView;
    private TextView mTemperatureTextView;
    private TextView mHumidityTextView;
    private TextView mPressureTextView;

    private SensorManager mSensorManager;
    private Sensor mThermometer;

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

    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        mSensorManager.registerListener(this, mThermometer, SensorManager.SENSOR_DELAY_NORMAL);
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
        if (event.sensor.getType() != Sensor.TYPE_AMBIENT_TEMPERATURE) return;

        float temperature_celcius = event.values[0];
        mTemperatureTextView.setText("" + temperature_celcius);
    }
}
