package com.example.ylois.dummyapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

public class CategoryActivity extends ActionBarActivity {

    private View mChart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentiment);
        openChart();

        


    }

    private void openChart() {

        // Pie Chart Section Names
        String[] code = new String[] { "Arts & Entertainment", "Business",
                "Computers & Internet", "Culture & Politics", "Gaming", "Health", "Law & Crime",
                "Religion", "Recreation","Science & Technology", "Sports", "Weather"};

        // Pie Chart Section Value
        Intent intent = getIntent();
        String message = intent.getStringExtra(MyActivity.EXTRA_MESSAGE);
        String[] parts = message.split(" ");
        double[] distribution = new double[parts.length];
        for (int n = 0; n < parts.length; n++) {
            distribution[n] = Double.parseDouble(parts[n]);
        }

//renamed      double[] distribution = {};

        // Color of each Pie Chart Sections
        int[] colors = { Color.BLUE, Color.GREEN, Color.RED, Color.BLUE, Color.GREEN, Color.RED,Color.BLUE, Color.GREEN, Color.RED,Color.BLUE, Color.GREEN, Color.RED};

        // Instantiating CategorySeries to plot Pie Chart
        CategorySeries distributionSeries = new CategorySeries(
                " Category Analysis");
        for (int i = 0; i < distribution.length; i++) {
            // Adding a slice with its values and name to the Pie Chart
            distributionSeries.add(code[i], distribution[i]);
        }

        // Instantiating a renderer for the Pie Chart
        DefaultRenderer defaultRenderer = new DefaultRenderer();
        for (int i = 0; i < distribution.length; i++) {
            SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
            seriesRenderer.setColor(colors[i]);
            seriesRenderer.setDisplayChartValues(true);
            //Adding colors to the chart
            defaultRenderer.setBackgroundColor(Color.WHITE);
            defaultRenderer.setApplyBackgroundColor(true);
            // Adding a renderer for a slice
            defaultRenderer.addSeriesRenderer(seriesRenderer);
            defaultRenderer.setLabelsTextSize(40);
            defaultRenderer.setLabelsColor(Color.BLACK);
            defaultRenderer.setLegendTextSize(30);
            defaultRenderer.setDisplayValues(true);


        }

        defaultRenderer
                .setChartTitle("Category Analysis ");
        defaultRenderer.setChartTitleTextSize(40);
        defaultRenderer.setZoomButtonsVisible(false);

        // Creating an intent to plot bar chart using dataset and
        // multipleRenderer
        // Intent intent = ChartFactory.getPieChartIntent(getBaseContext(),
        // distributionSeries , defaultRenderer, "AChartEnginePieChartDemo");

        // Start Activity
        // startActivity(intent);

        // this part is used to display graph on the xml
        LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart);
        // remove any views before u paint the chart
        chartContainer.removeAllViews();
        // drawing pie chart
        mChart = ChartFactory.getPieChartView(getBaseContext(),
                distributionSeries, defaultRenderer);
        // adding the view to the linearlayout

        chartContainer.addView(mChart);

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
}

    /*

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
*/