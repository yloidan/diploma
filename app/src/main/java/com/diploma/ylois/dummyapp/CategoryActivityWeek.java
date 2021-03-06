package com.diploma.ylois.dummyapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

public class CategoryActivityWeek extends ActionBarActivity {

    private View mChart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_activity_week);

        openChart();

    }

    //methodos gia xrwmatismo kai allagh megethous kathgoriwn
    private void setColor(TextView view, String fulltext, String subtext, int color) {
        view.setText(fulltext, TextView.BufferType.SPANNABLE);
        Spannable str = (Spannable) view.getText();
        int i = fulltext.indexOf(subtext);
        str.setSpan(new ForegroundColorSpan(color), i, i + subtext.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(new RelativeSizeSpan(1.2f), i, i + subtext.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }


    private void openChart() {

        // Pie Chart Section Names
        String[] code = new String[] { "Arts & Entertainment", "Business",
                "Computers & Internet", "Culture & Politics", "Gaming", "Health", "Law & Crime",
                "Religion", "Recreation","Science & Technology", "Sports", "Weather"};

        // Pie Chart Section Value
        Intent intent = getIntent();
        String message = "";


        message = intent.getStringExtra(DashboardActivityWeek.EXTRA_MESSAGE);



        String[] parts = message.split(" ");
        double[] distribution = new double[parts.length];
        int[] empty = new int[distribution.length];
        int newlength=distribution.length;

        for (int n = 0; n < parts.length; n++) {
            distribution[n] = Double.parseDouble(parts[n]);
        }
        for (int i=0; i<distribution.length; i++){
            if ((int)Math.round(distribution[i])==0){
                empty[i]=0;
                newlength-=1;
            }
            else
                empty[i]=1;
        }
        String[] codenew = new String[newlength];
        double[] distributionnew = new double[newlength];
        int z=0;


        for (int i=0; i<empty.length; i++)
            if (empty[i]==1){
                codenew[z]=code[i];
                distributionnew[z]=distribution[i];
                z+=1;
            }

//renamed      double[] distribution = {};

        // Color of each Pie Chart Sections
        int[] colors = { Color.BLUE, Color.GREEN, Color.RED, Color.MAGENTA, Color.CYAN, Color.YELLOW, Color.BLUE, Color.GREEN, Color.RED, Color.MAGENTA, Color.CYAN, Color.YELLOW};

        // Instantiating CategorySeries to plot Pie Chart
        CategorySeries distributionSeries = new CategorySeries(
                " Category Analysis");
        for (int i = 0; i < newlength; i++) {
            // Adding a slice with its values and name to the Pie Chart
            distributionSeries.add(codenew[i], distributionnew[i]);
        }

        // Instantiating a renderer for the Pie Chart
        DefaultRenderer defaultRenderer = new DefaultRenderer();
        for (int i = 0; i < newlength; i++) {
            SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
            seriesRenderer.setColor(colors[i]);
            seriesRenderer.setDisplayChartValues(true);
            //Adding colors to the chart
            defaultRenderer.setBackgroundColor(Color.WHITE);
            defaultRenderer.setApplyBackgroundColor(true);
            // Adding a renderer for a slice
            defaultRenderer.addSeriesRenderer(seriesRenderer);
            defaultRenderer.setLabelsTextSize(20);
            defaultRenderer.setLabelsColor(Color.BLACK);
            defaultRenderer.setLegendTextSize(20);
            defaultRenderer.setDisplayValues(true);


        }

        defaultRenderer
                .setChartTitle("Category Analysis ");
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
//        chartContainer.removeAllViews();
        // drawing pie chart
        mChart = ChartFactory.getPieChartView(getBaseContext(),
                distributionSeries, defaultRenderer);
        // adding the view to the linearlayout

        chartContainer.addView(mChart);




        //add textview
        parts = message.split(" ");
        int[] values = new int[parts.length];
        for (int n = 0; n < parts.length; n++) {
            values[n] = Integer.parseInt(parts[n]);
        }

        int max = values[0];
        int cnt = 0;
//        int[] cnt2={};
        //finding max value
        for (int i=0; i<values.length; i++)
        {
            if(max < values[i]) {
                max = values[i];
                cnt=i;
            }
        }

        //antistoixia dedomenwn me mhdenika, me ta antistoixa nea dedomena xwris mhdenika

        int cnt2 = 0;
        for (int i=0; i<cnt; i++){
            if (empty[i]==0){
                cnt2+=1;        //metraw posa mhdenika exei o pinakas mexri thn megisth kathgoria
            }
        }

        int categfinal = cnt - cnt2;

        //finding equal to max value; WILL NOT USE IT
/*        int m=0;

        for (int k=0; k<values.length; k++)
        {
            if (max==values[k] && k!=cnt)
            {
                cnt2[m]=k;
                m++;
            }
        }
*/
        String category="";
        StringBuilder sb = new StringBuilder();
        String url1="";
        String url2="";
        String url3="";

        switch (cnt)
        {
            case 0:
                category ="Arts & Entertainment";
                url1="http://www.forbes.com/arts-entertainment/";
                url2="http://www.cbc.ca/news/arts";
                url3="http://www.bbc.com/news/entertainment_and_arts";
                break;
            case 1:
                category ="Business";
                url1="http://www.bbc.com/news/business";
                url2="http://uk.reuters.com/business";
                url3="http://www.bloomberg.com/europe";
                break;
            case 2:
                category ="Computers & Internet";
                url1="http://www.cnet.com/news/";
                url2="http://edition.cnn.com/tech";
                url3="http://www.sciencedaily.com/news/computers_math/computers_and_internet/";
                break;
            case 3:
                category ="Culture & Politics";
                url1="http://www.bbc.com/news/politics";
                url2="http://www.huffingtonpost.com/news/politics-news/";
                url3="http://www.economist.com/topics/culture-and-lifestyle";
                break;
            case 4:
                category ="Gaming";
                url1="http://www.mmo-champion.com/content/";
                url2="http://www.gamespot.com/news/";
                url3="http://www.pcgamer.com/news/";
                break;
            case 5:
                category ="Health";
                url1="http://www.nytimes.com/pages/health/index.html";
                url2="http://edition.cnn.com/health";
                url3="http://www.foxnews.com/health/index.html";
                break;
            case 6:
                category ="Law & Crime";
                url1="http://edition.cnn.com/specials/us/crime-and-justice";
                url2="http://www.theguardian.com/law/criminal-justice";
                url3="http://legalnews.findlaw.com/crime-news.html";
                break;
            case 7:
                category ="Religion";
                url1="http://www.religionnews.com/";
                url2="http://www.huffingtonpost.com/religion/";
                url3="http://www.theguardian.com/world/religion";
                break;
            case 8:
                category ="Recreation";
                url1="http://www.sciencedaily.com/news/science_society/travel_and_recreation/";
                url2="http://recreationnews.com/";
                url3="http://greece.angloinfo.com/lifestyle/sports-and-leisure/";
                break;
            case 9:
                category ="Science & Technology";
                url1="https://www.sciencenews.org/";
                url2="http://www.huffingtonpost.com/science/";
                url3="http://www.bbc.com/news/technology";
                break;
            case 10:
                category ="Sports";
                url1="http://sports.yahoo.com/";
                url2="http://www.skysports.com/latest-news/";
                url3="http://www.bbc.com/sport/0/";
                break;
            case 11:
                category ="Weather";
                url1="http://www.accuweather.com/en/weather-news";
                url2="http://www.weather.com/";
                url3="http://www.bbc.com/weather/";
                break;
            default:
                category ="error1";
                url1="error2";
                url2="error3";
                url3="error4";
                break;
        }

        sb.append(getApplicationContext().getString(R.string.sbc_1)).append(category).append(".\n").append(getApplicationContext().getString(R.string.sbc_2)).append("\n").append(url1).append("\n").append(url2).append("\n").append(url3);

        TextView text = (TextView) findViewById(R.id.btn_chart);
        setColor(text, sb.toString(), category, colors[categfinal]);

        return;
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