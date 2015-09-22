package com.diploma.ylois.dummyapp;

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

public class SentimentActivityWeek extends ActionBarActivity {


    private View mChart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentiment_activity_week);
        openChart();
    }

    private void openChart() {

        // Pie Chart Section Names
        String[] code = new String[] { "Positive", "Negative",
                "Neutral"};

        // Pie Chart Section Value
        Intent intent = getIntent();
        String message= "";


        message = intent.getStringExtra(DashboardActivityWeek.EXTRA_MESSAGE);



        String[] parts = message.split(" ");
        double[] distribution = new double[parts.length];
        for (int n = 0; n < parts.length; n++) {
            distribution[n] = Double.parseDouble(parts[n]);
        }

//renamed      double[] distribution = {};

        // Color of each Pie Chart Sections
        int[] colors = { Color.BLUE, Color.MAGENTA , Color.RED };

        // Instantiating CategorySeries to plot Pie Chart
        CategorySeries distributionSeries = new CategorySeries(
                " Sentiment Analysis");
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
            defaultRenderer.setBackgroundColor(Color.BLACK);
            defaultRenderer.setApplyBackgroundColor(true);
            // Adding a renderer for a slice
            defaultRenderer.addSeriesRenderer(seriesRenderer);
            defaultRenderer.setLabelsTextSize(20);
            defaultRenderer.setLegendTextSize(20);
            defaultRenderer.setDisplayValues(true);


        }

        defaultRenderer
                .setChartTitle("Sentiment Analysis ");
        defaultRenderer.setChartTitleTextSize(20);
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



// Getting reference to the button btn_chart
//        Button btnChart = (Button) findViewById(R.id.btn_chart);

// Defining click event listener for the button btn_chart
//       OnClickListener clickListener = new OnClickListener() {

//           @Override
//            public void onClick(View v) {
// Draw the pie Chart
//
//           }

// Setting event click listener for the button btn_chart of the
// MainActivity layout
//       btnChart.setOnClickListener(clickListener);









    /* starts using external library a chart engine TRY#1

    private static int[] COLORS = new int[]{Color.GREEN, Color.BLUE, Color.MAGENTA};
//    private static int[] VALUES = new int[]{}; //0 for positive, 1 for negative, 2 for neutral
    private static String[] SENTIMENT = new String[]{"positive", "negative", "neutral"};
    private CategorySeries mSeries = new CategorySeries("");
    private DefaultRenderer mRenderer = new DefaultRenderer();
    private GraphicalView mChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentiment);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MyActivity.EXTRA_MESSAGE);
        String[] parts = message.split(" ");
        int[] VALUES = new int[parts.length];
        for (int n = 0; n < parts.length; n++) {
            VALUES[n] = Integer.parseInt(parts[n]);
        }

        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
        mRenderer.setChartTitleTextSize(20);
        mRenderer.setChartTitle("Sentiment Analysis");
        mRenderer.setLabelsTextSize(15);
        mRenderer.setLegendTextSize(15);
//        mRenderer.setMargins(new int[]{20, 30, 15, 0});
        mRenderer.setZoomButtonsVisible(true);
//        mRenderer.setStartAngle(90);
//            return ChartFactory.getPieChartIntent(context, mSeries, mRenderer, "PieChart");


        for (int i = 0; i < VALUES.length; i++) {
            mSeries.add(SENTIMENT[i] + " " + VALUES[i], VALUES[i]);
            SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
            renderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);
            mRenderer.addSeriesRenderer(renderer);
        }
    }
        @Override
        protected void onResume() {
            super.onResume();
            if (mChartView == null) {
                LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
                mChartView = ChartFactory.getPieChartView(this, mSeries, mRenderer);
            }


            if (mChartView != null) {
                mChartView.repaint();
            }
        }

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_sentiment, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
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
        ends using external library a chart engine TRY#1  */

    /* USING NO EXTERNAL LIBRARY


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        LinearLayout linear = new LinearLayout(this);

        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);





//        setContentView(R.layout.activity_sentiment);
//       LinearLayout linear=(LinearLayout) findViewById(R.id.linear);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MyActivity.EXTRA_MESSAGE);
        String[] parts = message.split(" ");
        float[] values = new float[parts.length];
        for (int n = 0; n < parts.length; n++) {
            values[n] = Float.parseFloat(parts[n]);
        }

        float[] values2=calculateData(values);


        linear.addView(new MyGraphview(this,values2));

        float max = values[0];
        int counter=0;
        int countereq=0;
        int countereq2=0;

        //vriskontas to epikrates sunaisthima
        for (int i=0; i<values.length; i++)
        {
            if (max < values[i]){
                max=values[i];
                counter=i;
             }

        }
        //elegxontas gia periptwsi isothtas
        for (int k=0; k<values.length; k++) {
            if (max == values[k] && counter!=k ) {
                countereq = k;
            }
        }
        //elegxontas gia periptwsi isothtas olwn
        for (int m=0; m<values.length; m++) {
            if (max == values[m] && counter != m && countereq != m) {
                countereq2 = m;
            }
        }
        //ftiaxnontas ta outputs
        String output="";
        switch(counter){
            case 0:
                output = "Positive";
                break;
            case 1:
                output = "Negative";
                break;
            case 2:
                output = "Neutral";
                break;
            default:
                break;
        }
        String output2="";
        switch(countereq){
            case 1:
                output2= "Negative";
                break;
            case 2:
                output2= "Neutral";
                break;
            default:
                break;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Positive Sentiment is with blue color equal to: ").append(values[0]/120).append(", or in percentage: ").append((values2[0]/360)*100).append("%").append("\n");
        sb.append("Negative Sentiment is with green color equal to: ").append(values[1]/120).append(", or in percentage: ").append((values2[1]/360)*100).append("%").append("\n");
        sb.append("Neutral Sentiment is with red color equal to: ").append(values[2]/120).append(", or in percentage: ").append((values2[2]/360)*100).append("%").append("\n");
        sb.append("\n").append("Most dominant sentiment of the browser history items is the ").append(output).append(" sentiment");
        if (countereq !=0)
        {
            sb.append(", which is equal to the ").append(output2).append(" sentiment");
        }
        if (countereq2 ==1){
            sb.append(" and also to the Negative sentiment");
        }
        sb.append(".");

        TextView textView = new TextView(this);
        textView.setTextSize(18);
 //       setContentView(textView);
        textView.setText(sb.toString());
        linear.addView(textView);
 //       setContentView(linear);
        setContentView(linear, lParams);
    }
    private float[] calculateData(float[] data) {

        float total=0;
        for(int i=0;i<data.length;i++)
        {
            total+=data[i];
        }
        for(int i=0;i<data.length;i++)
        {
            data[i]=360*(data[i]/total);
        }
        return data;

    }
    public class MyGraphview extends View
    {
        private Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        private float[] value_degree;
        private int[] COLORS={Color.BLUE,Color.GREEN,Color.RED};
        RectF rectf = new RectF (80, 80, 1000, 1000);
        int temp=0;
        public MyGraphview(Context context, float[] values) {

            super(context);
            value_degree=new float[values.length];
            for(int i=0;i<values.length;i++)
            {
                value_degree[i]=values[i];
            }
        }
        @Override
        protected void onDraw(Canvas canvas) {

            super.onDraw(canvas);

            for (int i = 0; i < value_degree.length; i++) {//values2.length; i++) {
                if (i == 0) {
                    paint.setColor(COLORS[i]);
                    canvas.drawArc(rectf, 0, value_degree[i], true, paint);
                }
                else
                {
                    temp += (int) value_degree[i - 1];
                    paint.setColor(COLORS[i]);
                    canvas.drawArc(rectf, temp, value_degree[i], true, paint);
                }
            }
        }

    } USING NO EXTERNAL LIBRARY*/


