package com.bignerdranch.android.currentconditions;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

public class HumidityGraphActivity extends Activity {
    public interface Constants {
        String TAG = "com.example.graphactivity";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_humidity_graph);
    GraphViewSeries exampleSeries = new GraphViewSeries(new GraphViewData[] {
            new GraphViewData(0, 0),
            new GraphViewData(1, 5),
            new GraphViewData(2, 4),
    });

    GraphView graphView = new LineGraphView(HumidityGraphActivity.this, "GraphViewDemo");
    graphView.addSeries(exampleSeries);
    LinearLayout layout = (LinearLayout) findViewById(R.id.graph1);
    layout.addView(graphView);
    }
}