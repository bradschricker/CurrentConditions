package com.bignerdranch.android.currentconditions;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;


public class CurrentConditionsActivity extends Activity implements SensorEventListener {
    private static final String KEY_TEMPERATURE = "temperature";
    private static final String KEY_HUMIDITY = "humidity";
    private static final String KEY_PRESSURE = "pressure";

    private TextView mTemperatureTextView;
    private TextView mHumidityTextView;
    private TextView mPressureTextView;

    private SensorManager mSensorManager;
    private Sensor mThermometer;
    private Sensor mHydrometer;
    private Sensor mBarometer;

    private float mTemperatureFahrenheit;
    private float mHumidity;
    private float mPressure;

    private NumberFormat mOutputFormat;

    private ArrayList<TemperatureRGB> mTemperatureRGBList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_conditions);

        mOutputFormat = NumberFormat.getNumberInstance();
        mOutputFormat.setMinimumFractionDigits(2);
        mOutputFormat.setMaximumFractionDigits(2);

        mTemperatureTextView = (TextView) findViewById(R.id.temperature_text_view);
        mTemperatureTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent temperatureIntent = new Intent(CurrentConditionsActivity.this, TemperatureGraphActivity.class);
                startActivity(temperatureIntent);
            }
        });
        mHumidityTextView = (TextView) findViewById(R.id.humidity_text_view);
        mHumidityTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent humidityIntent = new Intent(CurrentConditionsActivity.this, HumidityGraphActivity.class);
                startActivity(humidityIntent);
            }
        });
        mPressureTextView = (TextView) findViewById(R.id.pressure_text_view);
        mPressureTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pressureIntent = new Intent(CurrentConditionsActivity.this, PressureGraphActivity.class);
                startActivity(pressureIntent);
            }
        });

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mThermometer = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        mHydrometer = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        mBarometer = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);

        mTemperatureRGBList = new ArrayList<TemperatureRGB>();
        initializeTemperatureMap();

        if (savedInstanceState != null) {
            mTemperatureFahrenheit = savedInstanceState.getFloat(KEY_TEMPERATURE, 0);
            String temperature_output = mOutputFormat.format(mTemperatureFahrenheit);
            mTemperatureTextView.setText(temperature_output + (char) 0x00B0 + " F");
            int temperatureItem = getTemperatureRGBItem(mTemperatureFahrenheit);
            int red = mTemperatureRGBList.get(temperatureItem).getRed();
            int green = mTemperatureRGBList.get(temperatureItem).getGreen();
            int blue = mTemperatureRGBList.get(temperatureItem).getBlue();

            getWindow().getDecorView().setBackgroundColor(Color.rgb(red, green, blue));

            mHumidity = savedInstanceState.getFloat(KEY_HUMIDITY, 0);
            String relative_humidity_output = mOutputFormat.format(mHumidity);
            String humidity_label = getResources().getString(R.string.humidity);
            mHumidityTextView.setText(relative_humidity_output + "% " + humidity_label);

            mPressure = savedInstanceState.getFloat(KEY_PRESSURE, 0);
            String pressure_output = mOutputFormat.format(mPressure);
            String pressure_label = getResources().getString(R.string.pressure);
            mPressureTextView.setText(pressure_output + " " + pressure_label);
        }

    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        mSensorManager.registerListener(this, mThermometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mHydrometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mBarometer, SensorManager.SENSOR_DELAY_NORMAL);
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

    @Override
    public void onSaveInstanceState (Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putFloat(KEY_TEMPERATURE, mTemperatureFahrenheit);
        savedInstanceState.putFloat(KEY_HUMIDITY, mHumidity);
        savedInstanceState.putFloat(KEY_PRESSURE, mPressure);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //  Do something if the accuracy changes
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            float temperature_celcius = event.values[0];
            mTemperatureFahrenheit = temperature_celcius * 9 / 5 + 32.0f;

            String temperature_output = mOutputFormat.format(mTemperatureFahrenheit);
            mTemperatureTextView.setText(temperature_output + (char) 0x00B0 + " F");

            int temperatureItem = getTemperatureRGBItem(mTemperatureFahrenheit);
            int red = mTemperatureRGBList.get(temperatureItem).getRed();
            int green = mTemperatureRGBList.get(temperatureItem).getGreen();
            int blue = mTemperatureRGBList.get(temperatureItem).getBlue();

            getWindow().getDecorView().setBackgroundColor(Color.rgb(red, green, blue));
        }

        if (event.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
            mHumidity = event.values[0];

            String relative_humidity_output = mOutputFormat.format(mHumidity);
            String humidity_label = getResources().getString(R.string.humidity);
            mHumidityTextView.setText(relative_humidity_output + "% " + humidity_label);
        }

        if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
            mPressure = event.values[0];

            String pressure_output = mOutputFormat.format(mPressure);
            String pressure_label = getResources().getString(R.string.pressure);
            mPressureTextView.setText(pressure_output + " " + pressure_label);
        }
    }

    public void initializeTemperatureMap() {
        mTemperatureRGBList.add(0, new TemperatureRGB(10, 6, 0, 255));
        mTemperatureRGBList.add(1, new TemperatureRGB(12, 5, 0, 255));
        mTemperatureRGBList.add(2, new TemperatureRGB(14, 4, 0, 255));
        mTemperatureRGBList.add(3, new TemperatureRGB(16, 3, 0, 255));
        mTemperatureRGBList.add(4, new TemperatureRGB(18, 2, 0, 255));
        mTemperatureRGBList.add(5, new TemperatureRGB(20, 1, 0, 255));
        mTemperatureRGBList.add(6, new TemperatureRGB(22, 0, 0, 255));
        mTemperatureRGBList.add(7, new TemperatureRGB(24, 0, 2, 255));
        mTemperatureRGBList.add(8, new TemperatureRGB(26, 0, 18, 255));
        mTemperatureRGBList.add(9, new TemperatureRGB(28, 0, 34, 255));
        mTemperatureRGBList.add(10, new TemperatureRGB(30, 0, 50, 255));
        mTemperatureRGBList.add(11, new TemperatureRGB(32, 0, 68, 255));
        mTemperatureRGBList.add(12, new TemperatureRGB(34, 0, 84, 255));
        mTemperatureRGBList.add(13, new TemperatureRGB(36, 0, 100, 255));
        mTemperatureRGBList.add(14, new TemperatureRGB(38, 0, 116, 255));
        mTemperatureRGBList.add(15, new TemperatureRGB(40, 0, 132, 255));
        mTemperatureRGBList.add(16, new TemperatureRGB(42, 0, 148, 255));
        mTemperatureRGBList.add(17, new TemperatureRGB(44, 0, 164, 255));
        mTemperatureRGBList.add(18, new TemperatureRGB(46, 0, 180, 255));
        mTemperatureRGBList.add(19, new TemperatureRGB(48, 0, 196, 255));
        mTemperatureRGBList.add(20, new TemperatureRGB(50, 0, 212, 255));
        mTemperatureRGBList.add(21, new TemperatureRGB(52, 0, 228, 255));
        mTemperatureRGBList.add(22, new TemperatureRGB(54, 0, 255, 244));
        mTemperatureRGBList.add(23, new TemperatureRGB(56, 0, 255, 208));
        mTemperatureRGBList.add(24, new TemperatureRGB(58, 0, 255, 168));
        mTemperatureRGBList.add(25, new TemperatureRGB(60, 0, 255, 131));
        mTemperatureRGBList.add(26, new TemperatureRGB(62, 0, 255, 92));
        mTemperatureRGBList.add(27, new TemperatureRGB(64, 0, 255, 54));
        mTemperatureRGBList.add(28, new TemperatureRGB(66, 0, 255, 16));
        mTemperatureRGBList.add(29, new TemperatureRGB(68, 23, 255, 0));
        mTemperatureRGBList.add(30, new TemperatureRGB(70, 62, 255, 0));
        mTemperatureRGBList.add(31, new TemperatureRGB(72, 101, 255, 0));
        mTemperatureRGBList.add(32, new TemperatureRGB(74, 138, 255, 0));
        mTemperatureRGBList.add(33, new TemperatureRGB(76, 176, 255, 0));
        mTemperatureRGBList.add(34, new TemperatureRGB(78, 215, 255, 0));
        mTemperatureRGBList.add(35, new TemperatureRGB(80, 253, 255, 0));
        mTemperatureRGBList.add(36, new TemperatureRGB(82, 255, 250, 0));
        mTemperatureRGBList.add(37, new TemperatureRGB(84, 255, 240, 0));
        mTemperatureRGBList.add(38, new TemperatureRGB(86, 255, 230, 0));
        mTemperatureRGBList.add(39, new TemperatureRGB(88, 255, 220, 0));
        mTemperatureRGBList.add(40, new TemperatureRGB(90, 255, 210, 0));
        mTemperatureRGBList.add(41, new TemperatureRGB(92, 255, 200, 0));
        mTemperatureRGBList.add(42, new TemperatureRGB(94, 255, 190, 0));
        mTemperatureRGBList.add(43, new TemperatureRGB(96, 255, 180, 0));
        mTemperatureRGBList.add(44, new TemperatureRGB(98, 255, 170, 0));
        mTemperatureRGBList.add(45, new TemperatureRGB(100, 255, 160, 0));
        mTemperatureRGBList.add(46, new TemperatureRGB(102, 255, 150, 0));
        mTemperatureRGBList.add(47, new TemperatureRGB(104, 255, 140, 0));
        mTemperatureRGBList.add(48, new TemperatureRGB(106, 255, 130, 0));
        mTemperatureRGBList.add(49, new TemperatureRGB(108, 255, 120, 0));
        mTemperatureRGBList.add(50, new TemperatureRGB(110, 255, 110, 0));
        mTemperatureRGBList.add(51, new TemperatureRGB(112, 255, 100, 0));
        mTemperatureRGBList.add(52, new TemperatureRGB(114, 255, 90, 0));
        mTemperatureRGBList.add(53, new TemperatureRGB(116, 255, 80, 0));
        mTemperatureRGBList.add(54, new TemperatureRGB(118, 255, 70, 0));
        mTemperatureRGBList.add(55, new TemperatureRGB(120, 255, 60, 0));
        mTemperatureRGBList.add(56, new TemperatureRGB(122, 255, 50, 0));
        mTemperatureRGBList.add(57, new TemperatureRGB(124, 255, 40, 0));
        mTemperatureRGBList.add(58, new TemperatureRGB(126, 255, 30, 0));
        mTemperatureRGBList.add(59, new TemperatureRGB(128, 255, 20, 0));
        mTemperatureRGBList.add(60, new TemperatureRGB(130, 255, 10, 0));
        mTemperatureRGBList.add(61, new TemperatureRGB(132, 255, 0, 0));
        mTemperatureRGBList.add(62, new TemperatureRGB(134, 255, 0, 16));
        mTemperatureRGBList.add(63, new TemperatureRGB(136, 255, 0, 32));
        mTemperatureRGBList.add(64, new TemperatureRGB(138, 255, 0, 48));
        mTemperatureRGBList.add(65, new TemperatureRGB(140, 255, 0, 64));
        mTemperatureRGBList.add(66, new TemperatureRGB(142, 255, 0, 80));
        mTemperatureRGBList.add(67, new TemperatureRGB(144, 255, 0, 96));
        mTemperatureRGBList.add(68, new TemperatureRGB(146, 255, 0, 112));
        mTemperatureRGBList.add(69, new TemperatureRGB(148, 255, 0, 128));
        mTemperatureRGBList.add(70, new TemperatureRGB(150, 255, 0, 144));
        mTemperatureRGBList.add(71, new TemperatureRGB(152, 255, 0, 160));
        mTemperatureRGBList.add(72, new TemperatureRGB(154, 255, 0, 176));
        mTemperatureRGBList.add(73, new TemperatureRGB(156, 255, 0, 192));
        mTemperatureRGBList.add(74, new TemperatureRGB(158, 255, 0, 208));
        mTemperatureRGBList.add(75, new TemperatureRGB(160, 255, 0, 224));
        mTemperatureRGBList.add(76, new TemperatureRGB(162, 255, 0, 240));
        mTemperatureRGBList.add(77, new TemperatureRGB(164, 255, 1, 240));
        mTemperatureRGBList.add(78, new TemperatureRGB(166, 255, 2, 240));
        mTemperatureRGBList.add(79, new TemperatureRGB(168, 255, 3, 240));
        mTemperatureRGBList.add(80, new TemperatureRGB(170, 255, 4, 240));
        mTemperatureRGBList.add(81, new TemperatureRGB(172, 255, 5, 240));
        mTemperatureRGBList.add(82, new TemperatureRGB(174, 255, 6, 240));
        mTemperatureRGBList.add(83, new TemperatureRGB(176, 255, 7, 240));
        mTemperatureRGBList.add(84, new TemperatureRGB(178, 255, 8, 240));
        mTemperatureRGBList.add(85, new TemperatureRGB(180, 255, 9, 240));
        mTemperatureRGBList.add(86, new TemperatureRGB(182, 255, 10, 240));
        mTemperatureRGBList.add(87, new TemperatureRGB(184, 255, 11, 240));
        mTemperatureRGBList.add(88, new TemperatureRGB(186, 255, 12, 240));
        mTemperatureRGBList.add(89, new TemperatureRGB(188, 255, 13, 240));
        mTemperatureRGBList.add(90, new TemperatureRGB(190, 255, 14, 240));
    }

    public int getTemperatureRGBItem(float temperatureF) {
        for (int i = 0; i < mTemperatureRGBList.size(); i++) {
            if (temperatureF <= mTemperatureRGBList.get(0).getTemperature()) {
                return 0;
            } else if ((temperatureF > mTemperatureRGBList.get(i).getTemperature()) && (temperatureF < mTemperatureRGBList.get(i + 1).getTemperature())) {
                return i;
            } else if (temperatureF > mTemperatureRGBList.get(mTemperatureRGBList.size() - 1).getTemperature()) {
                return mTemperatureRGBList.size();
            }
        }

        return 0;
    }
}
