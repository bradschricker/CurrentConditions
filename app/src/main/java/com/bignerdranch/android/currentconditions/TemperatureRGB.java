package com.bignerdranch.android.currentconditions;

/**
 * Created by localadmin on 8/14/14.
 */
public class TemperatureRGB {

    private int mTemperature;
    private int mRed;
    private int mGreen;
    private int mBlue;

    public TemperatureRGB() {
        this(0, 0, 0, 0);
    }

    public TemperatureRGB(int temperature, int red, int green, int blue) {
        mTemperature = temperature;
        mRed = red;
        mGreen = green;
        mBlue = blue;
    }

    public void setTemperature(int temperature) {
        mTemperature = temperature;
    }

    public int getTemperature() {
        return mTemperature;
    }

    public void setRed(int red) {
        mRed = red;
    }

    public int getRed() {
        return mRed;
    }

    public void setGreen(int green) {
        mGreen = green;
    }

    public int getGreen() {
        return mGreen;
    }

    public void setBlue(int blue) {
        mBlue = blue;
    }

    public int getBlue() {
        return mBlue;
    }

}
