package com.example.ylois.dummyapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DashboardActivity extends ActionBarActivity implements View.OnClickListener {

    public final static String EXTRA_MESSAGE = "com.example.ylois.dummyapp.MESSAGE";

    //class for not having any data stored alert dialog
    public static class NoDataStored extends DialogFragment{
        @Override
        public Dialog onCreateDialog (Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.warning_2)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            return builder.create();
        }
    }




    //class for not connected to internet warning alert dialog
    public static class NoInternetDialogFragment extends DialogFragment{
        @Override
        public Dialog onCreateDialog (Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.warning)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            return builder.create();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Button b5 = (Button) findViewById(R.id.b5);
        b5.setOnClickListener(this);
        Button b6 = (Button) findViewById(R.id.b6);
        b6.setOnClickListener(this);
        Button b7 = (Button) findViewById(R.id.b7);
        b7.setOnClickListener(this);
        Button b8 = (Button) findViewById(R.id.b8);
        b8.setOnClickListener(this);
        Button b9 = (Button) findViewById(R.id.b9);
        b9.setOnClickListener(this);
        Button b10 = (Button) findViewById(R.id.b10);
        b10.setOnClickListener(this);
        Button b11 = (Button) findViewById(R.id.b11);
        b11.setOnClickListener(this);
        Button b12 = (Button) findViewById(R.id.b12);
        b12.setOnClickListener(this);

        barchart();

    }

    //methodos elegxou an einai connected sto internet
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b5:
                if (isNetworkAvailable()) {
                    new LongRunningGetIO3().execute();
                } else {
                    DialogFragment alert = new NoInternetDialogFragment();
                    alert.show(getSupportFragmentManager(), "alert");
                }
                break;
            case R.id.b6:
                if (isNetworkAvailable()) {
                    new LongRunningGetIO2().execute();
                } else {
                    DialogFragment alert = new NoInternetDialogFragment();
                    alert.show(getSupportFragmentManager(), "alert");
                }
                break;
            case R.id.b7:
                if (isNetworkAvailable()) {
                    new LongRunningGetIO4().execute();
                } else {
                    DialogFragment alert = new NoInternetDialogFragment();
                    alert.show(getSupportFragmentManager(), "alert");
                }
                break;
            case R.id.b8:
                if (isNetworkAvailable()) {
                    new LongRunningGetIO5().execute();
                } else {
                    DialogFragment alert = new NoInternetDialogFragment();
                    alert.show(getSupportFragmentManager(), "alert");
                }
                break;
            case R.id.b9:
                if (isNetworkAvailable()) {
                    new LongRunningGetIO6().execute();
                } else {
                    DialogFragment alert = new NoInternetDialogFragment();
                    alert.show(getSupportFragmentManager(), "alert");
                }
                break;
            case R.id.b10:
                if (isNetworkAvailable()) {
                    new LongRunningGetIO7().execute();
                } else {
                    DialogFragment alert = new NoInternetDialogFragment();
                    alert.show(getSupportFragmentManager(), "alert");
                }
                break;
            case R.id.b11:
                if (isNetworkAvailable()) {
                    new LongRunningGetIOToday().execute();
                } else {
                    DialogFragment alert = new NoInternetDialogFragment();
                    alert.show(getSupportFragmentManager(), "alert");
                }
                break;
            case R.id.b12:
                if (isNetworkAvailable()) {
                    new LongRunningGetIOWeek().execute();
                } else {
                    DialogFragment alert = new NoInternetDialogFragment();
                    alert.show(getSupportFragmentManager(), "alert");
                }
                break;

            default:
                break;
        }
    }

    private void barchart() {

        int AEneg, AEpos, Busneg, Buspos, CIneg, CIpos, CPneg, CPpos, Ganeg, Gapos, Heneg, Hepos, LCneg, LCpos, Relneg, Relpos, Recneg, Recpos, STneg, STpos, Spneg, Sppos, Weneg, Wepos;
        AEneg = AEpos = Busneg = Buspos = CIneg = CIpos = CPneg = CPpos = Ganeg = Gapos = Heneg = Hepos = LCneg = LCpos = Relneg = Relpos = Recneg = Recpos = STneg = STpos = Spneg = Sppos = Weneg = Wepos = 0;

        StringBuilder dateexists=new StringBuilder("");
        StringBuilder ondate = new StringBuilder("");

        View barChart;
        String[] mCategories = new String[]{"Arts", "Business",
                "Computers", "Politics", "Gaming", "Health", "Law & Crime",
                "Religion", "Recreation", "Science", "Sports", "Weather"};
        int[] x = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        int[] positive = new int[x.length];
        int[] negative = new int[x.length];

        Intent intent = getIntent();
        String message = intent.getStringExtra(MyActivity.EXTRA_MESSAGE);

        String parts [] = null;



        try {
            JSONArray ja = new JSONArray(message);
            parts = new String[ja.length()];

            for (int i=0; i<ja.length(); i++)
            {
                JSONObject json = ja.getJSONObject(i);
                parts[i]=json.toString();
            }
        }
        catch (Exception e){
            System.err.println("JSONException " + e.getMessage());
        }







//        String[] parts = message.split("_id");



/* array FAILURE
        int[] AEnegarr = new int[]{};
        int[] AEposarr = new int[]{};
        int[] Busnegarr = new int[]{};
        int[] Busposarr = new int[]{};
        int[] CInegarr = new int[]{};
        int[] CIposarr = new int[]{};
        int[] CPnegarr = new int[]{};
        int[] CPposarr = new int[]{};
        int[] Ganegarr = new int[]{};
        int[] Gaposarr = new int[]{};
        int[] Henegarr = new int[]{};
        int[] Heposarr = new int[]{};
        int[] LCnegarr = new int[]{};
        int[] LCposarr = new int[]{};
        int[] Relnegarr = new int[]{};
        int[] Relposarr = new int[]{};
        int[] Recnegarr = new int[]{};
        int[] Recposarr = new int[]{};
        int[] STnegarr = new int[]{};
        int[] STposarr = new int[]{};
        int[] Spnegarr = new int[]{};
        int[] Spposarr = new int[]{};
        int[] Wenegarr = new int[]{};
        int[] Weposarr = new int[]{};
        */
        JSONObject category=null;
        JSONObject sentiment=null;

        for (int i = 0; i < parts.length; i++) {
            try {
                JSONObject json = new JSONObject(parts[i]);
                category = json.getJSONObject("categoryres");
                sentiment = json.getJSONObject("sentimentres");

            }
            catch (JSONException e){
                System.err.println("JSONException " + e.getMessage());
            }

            if (category.toString().contains("\"arts_entertainment\"") && sentiment.toString().contains("\"negative\"")) {
                //               AEnegarr.append(i).append("");
                AEneg += 1;
            }
            if (category.toString().contains("\"arts_entertainment\"") && sentiment.toString().contains("\"positive\"")) {
                //              AEposarr.append(i).append("");
                AEpos += 1;
            }

            if (category.toString().contains("\"business\"") && sentiment.toString().contains("\"negative\"")) {
                //              Busnegarr.append(i).append("");
                Busneg += 1;
            }

            if (category.toString().contains("\"business\"") && sentiment.toString().contains("\"positive\"")) {
                //            Busposarr.append(i).append("");
                Buspos += 1;
            }


            if (category.toString().contains("\"computer_internet\"") && sentiment.toString().contains("\"negative\"")) {
                //               CInegarr.append(i).append("");
                CIneg += 1;
            }
            if (category.toString().contains("\"computer_internet\"") && sentiment.toString().contains("\"positive\"")) {
//                CIposarr.append(i).append("");
                CIpos += 1;
            }

            if (category.toString().contains("\"culture_politics\"") && sentiment.toString().contains("\"negative\"")) {
//                CPnegarr.append(i).append("");
                CPneg += 1;
            }
            if (category.toString().contains("\"culture_politics\"") && sentiment.toString().contains("\"positive\"")) {
//                CPposarr.append(i).append("");
                CPpos += 1;
            }

            if (category.toString().contains("\"gaming\"") && sentiment.toString().contains("\"negative\"")) {
                //               Ganegarr.append(i).append("");
                Ganeg += 1;
            }
            if (category.toString().contains("\"gaming\"") && sentiment.toString().contains("\"positive\"")) {
                //               Gaposarr.append(i).append("");
                Gapos += 1;
            }

            if (category.toString().contains("\"health\"") && sentiment.toString().contains("\"negative\"")) {
//                Henegarr.append(i).append("");
                Heneg += 1;
            }
            if (category.toString().contains("\"health\"") && sentiment.toString().contains("\"positive\"")) {
                //               Heposarr.append(i).append("");
                Hepos += 1;
            }

            if (category.toString().contains("\"law_crime\"") && sentiment.toString().contains("\"negative\"")) {
                //              LCnegarr.append(i).append("");
                LCneg += 1;
            }
            if (category.toString().contains("\"law_crime\"") && sentiment.toString().contains("\"positive\"")) {
                //              LCposarr.append(i).append("");
                LCpos += 1;
            }

            if (category.toString().contains("\"religion\"") && sentiment.toString().contains("\"negative\"")) {
//                Relnegarr.append(i).append("");
                Relneg += 1;
            }
            if (category.toString().contains("\"religion\"") && sentiment.toString().contains("\"positive\"")) {
                //              Relposarr.append(i).append("");
                Relpos += 1;
            }

            if (category.toString().contains("\"recreation\"") && sentiment.toString().contains("\"negative\"")) {
//                Recnegarr.append(i).append("");
                Recneg += 1;
            }
            if (category.toString().contains("\"recreation\"") && sentiment.toString().contains("\"positive\"")) {
                //               Recposarr.append(i).append("");
                Recpos += 1;
            }

            if (category.toString().contains("\"science_technology\"") && sentiment.toString().contains("\"negative\"")) {
                //              STnegarr.append(i).append("");
                STneg += 1;
            }
            if (category.toString().contains("\"science_technology\"") && sentiment.toString().contains("\"positive\"")) {
//                STposarr.append(i).append("");
                STpos += 1;
            }

            if (category.toString().contains("\"sports\"") && sentiment.toString().contains("\"negative\"")) {
//                Spnegarr.append(i).append("");
                Spneg += 1;
            }
            if (category.toString().contains("\"sports\"") && sentiment.toString().contains("\"positive\"")) {
                //               Spposarr.append(i).append("");
                Sppos += 1;
            }

            if (category.toString().contains("\"weather\"") && sentiment.toString().contains("\"negative\"")) {
                //               Wenegarr.append(i).append("");
                Weneg += 1;
            }
            if (category.toString().contains("\"weather\"") && sentiment.toString().contains("\"positive\"")) {
                //               Weposarr.append(i).append("");
                Wepos += 1;
            }
            if (parts[i].contains("\"dateres\"") ) {
                dateexists.append(i).append("SPLITTING");
                try {
                    JSONObject json=new JSONObject(parts[i]);
                    JSONObject dateres =json.getJSONObject("dateres");

                    Date date = new Date();
                    date.setTime(Long.parseLong(dateres.getString("date")));
                    String test=new SimpleDateFormat("dd/M/yyyy", Locale.ENGLISH).format(date);

                    /*
                    int dayspassed = safeLongToInt(TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis()-(Long.parseLong(dateres.getString("date")))));

                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_MONTH, -dayspassed);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy", Locale.ENGLISH);
                    String test = sdf.format(calendar.getTime());
                    */

                    ondate.append(test).append("SPLITTING");
                }
                catch (Exception e1)
                {
                    System.err.println("JSONException " + e1.getMessage());
                }


            }

        }


//AEneg,AEpos,Busneg,Buspos,CIneg,CIpos,CPneg,CPpos,Ganeg,Gapos,Heneg,Hepos,LCneg,LCpos,Relneg,Relpos,Recneg,Recpos,STneg,STpos,Spneg,Sppos,Weneg,Wepos
        positive[0] = AEpos;
        positive[1] = Buspos;
        positive[2] = CIpos;
        positive[3] = CPpos;
        positive[4] = Gapos;
        positive[5] = Hepos;
        positive[6] = LCpos;
        positive[7] = Relpos;
        positive[8] = Recpos;
        positive[9] = STpos;
        positive[10] = Sppos;
        positive[11] = Wepos;

        negative[0] = AEneg;
        negative[1] = Busneg;
        negative[2] = CIneg;
        negative[3] = CPneg;
        negative[4] = Ganeg;
        negative[5] = Heneg;
        negative[6] = LCneg;
        negative[7] = Relneg;
        negative[8] = Recneg;
        negative[9] = STneg;
        negative[10] = Spneg;
        negative[11] = Weneg;

        int[] maximum = new int[x.length];
        maximum[0] = AEpos + AEneg;
        maximum[1] = Busneg + Buspos;
        maximum[2] = CIneg + CIpos;
        maximum[3] = CPneg + CPpos;
        maximum[4] = Ganeg + Gapos;
        maximum[5] = Heneg + Hepos;
        maximum[6] = LCneg + LCpos;
        maximum[7] = Relneg + Relpos;
        maximum[8] = Recneg + Recpos;
        maximum[9] = STneg + STpos;
        maximum[10] = Spneg + Sppos;
        maximum[11] = Weneg + Wepos;

        int max = maximum[0];
        int counter = 0;
        for (int i = 0; i < maximum.length; i++) {
            if (max < maximum[i]) {
                max = maximum[i];
                counter = i;
            }
        }

        XYSeries positiveSeries = new XYSeries("Positive");
        XYSeries negativeSeries = new XYSeries("Negative");
        for (int i = 0; i < x.length; i++) {

            positiveSeries.add(i, positive[i]);
            negativeSeries.add(i, negative[i]);
        }
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(positiveSeries);
        dataset.addSeries(negativeSeries);
        XYSeriesRenderer positiveRenderer = new XYSeriesRenderer();
        positiveRenderer.setColor(Color.BLUE);
        positiveRenderer.setFillPoints(true);
        positiveRenderer.setLineWidth(2);
        positiveRenderer.setDisplayChartValues(true);
        positiveRenderer.setDisplayChartValuesDistance(8);

        XYSeriesRenderer negativeRenderer = new XYSeriesRenderer();
        negativeRenderer.setColor(Color.MAGENTA);
        negativeRenderer.setFillPoints(true);
        negativeRenderer.setLineWidth(2);
        negativeRenderer.setDisplayChartValues(true);

        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
        multiRenderer.setXLabels(0);
        multiRenderer.setChartTitle("Categories with stacked Sentiments chart");
        multiRenderer.setXTitle("Categories");
        multiRenderer.setYTitle("Stacked sentiment score");

        multiRenderer.setChartTitleTextSize(28);
        multiRenderer.setAxisTitleTextSize(24);
        multiRenderer.setLabelsTextSize(24);
        multiRenderer.setZoomButtonsVisible(false);
        multiRenderer.setPanEnabled(false, false);
        multiRenderer.setClickEnabled(false);
        multiRenderer.setZoomEnabled(false, false);
        multiRenderer.setShowGridY(false);
        multiRenderer.setShowGridX(false);
        multiRenderer.setFitLegend(true);
        multiRenderer.setShowGrid(false);
        multiRenderer.setZoomEnabled(false);
        multiRenderer.setExternalZoomEnabled(false);
        multiRenderer.setAntialiasing(true);
        multiRenderer.setInScroll(false);
        multiRenderer.setLegendHeight(30);
        multiRenderer.setLegendTextSize(24);
        multiRenderer.setXLabelsAlign(Paint.Align.CENTER); //diff
        multiRenderer.setYLabelsAlign(Paint.Align.LEFT);    //diff
        multiRenderer.setTextTypeface("sans_serif", Typeface.NORMAL);
        multiRenderer.setYLabels(10);
        // if you use dynamic values then get the max y value and set here
        multiRenderer.setYAxisMax(max);
        multiRenderer.setXAxisMin(-0.5);
        multiRenderer.setXAxisMax(12);
        multiRenderer.setBarSpacing(0.5);
//        multiRenderer.setBackgroundColor(Color.TRANSPARENT);
//error        multiRenderer.setMarginsColor(getResources().getColor(R.color.transparent_background));
//        multiRenderer.setApplyBackgroundColor(true);
        //setting the margin size for the graph in the order top, left, bottom, right
        multiRenderer.setMargins(new int[]{30, 30, 30, 30});

        for (int i = 0; i < x.length; i++) {
            multiRenderer.addXTextLabel(i, mCategories[i]);

        }

        multiRenderer.addSeriesRenderer(positiveRenderer);
        multiRenderer.addSeriesRenderer(negativeRenderer);

        LinearLayout chartContainer = (LinearLayout) findViewById(R.id.barchart);

        barChart = ChartFactory.getBarChartView(DashboardActivity.this, dataset, multiRenderer, BarChart.Type.STACKED);
        chartContainer.addView(barChart);

        //textview for most visited category
        TextView mvctext = (TextView) findViewById(R.id.MVCat);
        mvctext.setText(getApplicationContext().getResources().getString(R.string.sbc_1) + mCategories[counter]);

        //finding main sentiment
        int sumpos = 0;
        for (int i = 0; i < positive.length; i++) {
            sumpos += positive[i];
        }
        int sumneg = 0;
        for (int i = 0; i < negative.length; i++) {
            sumneg += negative[i];
        }

        ImageView mImageView;
        mImageView = (ImageView) findViewById(R.id.sentImage);
        if (sumpos > sumneg) {
            mImageView.setImageResource(R.drawable.happy);
        } else {
            mImageView.setImageResource(R.drawable.sad);
        }

        ///most positive category

        int mpc = 0;
        int nullcounter = 0;
        int maxpos = positive[0];
        for (int i = 0; i < positive.length; i++) {
            if (maxpos < positive[i]) {
                maxpos = positive[i];
                mpc = i;
                nullcounter+=1;
            }
        }


        TextView mpctext = (TextView) findViewById(R.id.MPosCat);

        if(nullcounter!=0) {
            mpctext.setText(getApplicationContext().getResources().getString(R.string.sbc_3) + mCategories[mpc]);
        }
        else {
            mpctext.setText(getApplicationContext().getResources().getString(R.string.sbc3_fail));
        }

        //most negative category

        int mnc= 0;
        int nullcounter2 = 0;
        int maxneg=negative[0];
        for (int i=0; i <negative.length; i++) {
            if (maxneg < negative[i]) {
                maxneg=negative[i];
                mnc = i;
                nullcounter2+=1;
            }
        }

        TextView mnctext = (TextView) findViewById(R.id.MNegCat);
        if(nullcounter2!=0) {
            mnctext.setText(getApplicationContext().getResources().getString(R.string.sbc_5) + mCategories[mnc]);
        }
        else {
            mnctext.setText(getApplicationContext().getResources().getString(R.string.sbc3_fail));
        }

        //day with best sentiment and day with worst sentiment

        if(dateexists!=null && !dateexists.toString().isEmpty()) {

            String[] datescnt = dateexists.toString().split("SPLITTING");
            int[] dateint = new int[datescnt.length];
            for (int i = 0; i < datescnt.length; i++) {
                dateint[i] = Integer.parseInt(datescnt[i]);
            }

            String[] dates = ondate.toString().split("SPLITTING");

            String[] datesexperiment = new String[dates.length];
            System.arraycopy(dates, 0, datesexperiment, 0, dates.length);

            int morepositive = 0;
            int morenegative = 0;

            StringBuilder sbd = new StringBuilder("");


            for (int k = 0; k < datesexperiment.length; k++) {

                if (!datesexperiment[k].equals("SKIP")) {
                    String test = datesexperiment[k];

                    for (int i = 0; i < datesexperiment.length; i++) {
                        if (datesexperiment[i].equals(test)) {
                            sbd.append(i).append("SPLITTING");
                            datesexperiment[i] = "SKIP";
                        }

                    }
                    sbd.append("NEXT");

                }
            }

            String[] date2 = sbd.toString().split("NEXT");

            int[] totalpos = new int[dates.length];
            int[] totalneg = new int[dates.length];

            /*
            String[] results= null;

            for (int i=0; i<date2.length; i++) {
                results = date2[i].split("SPLITTING");
            }

            */

            for (int i = 0; i < date2.length; i++) {
                String[] date3 = date2[i].split("SPLITTING");
                int[] date4= new int[date3.length];

                for (int k = 0; k < date3.length; k++) {

                    date4[k] = Integer.parseInt(date3[k]);

                    try {
                        JSONObject json = new JSONObject(parts[dateint[date4[k]]]);
                        JSONObject sent = json.getJSONObject("sentimentres");
                        String sents = sent.getString("sentiment");
                        if (sents.contains("positive"))
                            morepositive += 1;
                        else {
                            morenegative += 1;
                        }

                    } catch (Exception e2) {
                        System.err.println("JSONException " + e2.getMessage());
                    }
                }

                totalneg[i] = morenegative;
                totalpos[i] = morepositive;
                morenegative=0;
                morepositive=0;

            }

            int totalnegmax = totalneg[0];
            int totalnegcnt = 0;


            for (int i = 0; i < totalneg.length; i++) {
                if (totalnegmax < totalneg[i]) {
                    totalnegmax = totalneg[i];
                    totalnegcnt = i;
                }

            }

            int totalposmax = totalpos[0];
            int totalposcnt = 0;


            for (int i = 0; i < totalpos.length; i++) {
                if (totalposmax < totalpos[i]) {
                    totalposmax = totalpos[i];
                    totalposcnt = i;
                }

            }

            String[] resultneg = date2[totalnegcnt].split("SPLITTING");
            int result2 =Integer.parseInt(resultneg[0]);

            String[] resultpos = date2[totalposcnt].split("SPLITTING");
            int result3 =Integer.parseInt(resultpos[0]);

            TextView dbstext = (TextView) findViewById(R.id.DBS);
            dbstext.setText(getApplicationContext().getResources().getString(R.string.sbc_6) + dates[result2] + " with a score of: " + totalposmax + ".");

            TextView dwstext = (TextView) findViewById(R.id.DWS);
            dwstext.setText(getApplicationContext().getResources().getString(R.string.sbc_8) + dates[result3] + " with a score of: " + totalnegmax + ".");
        }
        else {
            TextView dbstext = (TextView) findViewById(R.id.DBS);
            dbstext.setText(getApplicationContext().getResources().getString(R.string.sbc_7));

            TextView dwstext = (TextView) findViewById(R.id.DWS);
            dwstext.setText(getApplicationContext().getResources().getString(R.string.sbc_7));
        }













    }








    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
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


    private class LongRunningGetIO2 extends AsyncTask<Void, Void, String> {

        public String executeHttpGet(String url) throws Exception {
            BufferedReader in = null;
            String data = null;

            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(url));
                HttpResponse response = client.execute(request);
                response.getStatusLine().getStatusCode();

                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuilder sb = new StringBuilder("");
                String l = "";
                String nl = System.getProperty("line.separator");
                while ((l = in.readLine()) !=null){
                    sb.append(l + nl);
                }
                in.close();
                data = sb.toString();
                return data;
            } finally{
                if (in != null){
                    try{
                        in.close();
                        return data;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
        @Override
        protected String doInBackground(Void... params) {

            String myself = "";
            if(!Settings.Secure.ANDROID_ID.equals("android_id"))
                myself=Settings.Secure.ANDROID_ID;

            if(Settings.Secure.ANDROID_ID.equals("android_id"))
                myself = android.os.Build.SERIAL;

            String text="https://api.mongolab.com/api/1/databases/dummydb/collections/" + myself + "?apiKey=sWm3hnnxTlUTHiT2r45aaqQkFltSauc6";
            try {
                text = executeHttpGet(text);
            } catch (Exception e9) {
                return e9.getLocalizedMessage();
            }

            if (text!=null && !text.isEmpty()) {


                Pattern p = Pattern.compile("\"positive\"");
                Matcher m = p.matcher(text);
                int count = 0;
                while (m.find()) {
                    count += 1;
                }
                String cpos = Integer.toString(count);

                Pattern p2 = Pattern.compile("\"negative\"");
                Matcher m2 = p2.matcher(text);
                int count2 = 0;
                while (m2.find()) {
                    count2 += 1;
                }
                String cneg = Integer.toString(count2);

                Pattern p3 = Pattern.compile("\"neutral\"");
                Matcher m3 = p3.matcher(text);
                int count3 = 0;
                while (m3.find()) {
                    count3 += 1;
                }
                String cneut = Integer.toString(count3);

                return new StringBuilder().append(cpos).append(" ").append(cneg).append(" ").append(cneut).toString();
            }
            else {
                return getApplicationContext().getResources().getString(R.string.no_data_fail);
            }
        }

        protected void onPostExecute(String message) {

            if (message.equals(getApplicationContext().getResources().getString(R.string.no_data_fail))) {
                DialogFragment alert = new NoDataStored();
                alert.show(getSupportFragmentManager(), "alert");
            }
            else {
                Intent effortIntent = new Intent(DashboardActivity.this, SentimentActivity.class);
                effortIntent.putExtra(EXTRA_MESSAGE, message);
                startActivity(effortIntent);
            }
        }
    }

    private class LongRunningGetIO3 extends AsyncTask<Void, Void, String> {

        public String executeHttpGet(String url) throws Exception {
            BufferedReader in = null;
            String data = null;

            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(url));
                HttpResponse response = client.execute(request);
                response.getStatusLine().getStatusCode();

                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuilder sb = new StringBuilder("");
                String l = "";
                String nl = System.getProperty("line.separator");
                while ((l = in.readLine()) !=null){
                    sb.append(l + nl);
                }
                in.close();
                data = sb.toString();
                return data;
            } finally{
                if (in != null){
                    try{
                        in.close();
                        return data;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected String doInBackground(Void... params) {

            String myself = "";
            if(!Settings.Secure.ANDROID_ID.equals("android_id"))
                myself=Settings.Secure.ANDROID_ID;

            if(Settings.Secure.ANDROID_ID.equals("android_id"))
                myself = android.os.Build.SERIAL;

            String text="https://api.mongolab.com/api/1/databases/dummydb/collections/" + myself + "?apiKey=sWm3hnnxTlUTHiT2r45aaqQkFltSauc6";
            try {
                text = executeHttpGet(text);
            } catch (Exception e9) {
                return e9.getLocalizedMessage();
            }

            if (text!=null && !text.isEmpty()) {

                Pattern p = Pattern.compile("\"arts_entertainment\"");
                Matcher m = p.matcher(text);
                int count = 0;
                while (m.find()) {
                    count += 1;
                }
                String c1 = Integer.toString(count);

                Pattern p2 = Pattern.compile("\"business\"");
                Matcher m2 = p2.matcher(text);
                int count2 = 0;
                while (m2.find()) {
                    count2 += 1;
                }
                String c2 = Integer.toString(count2);

                Pattern p3 = Pattern.compile("\"computer_internet\"");
                Matcher m3 = p3.matcher(text);
                int count3 = 0;
                while (m3.find()) {
                    count3 += 1;
                }
                String c3 = Integer.toString(count3);

                Pattern p4 = Pattern.compile("\"culture_politics\"");
                Matcher m4 = p4.matcher(text);
                int count4 = 0;
                while (m4.find()) {
                    count4 += 1;
                }
                String c4 = Integer.toString(count4);

                Pattern p5 = Pattern.compile("\"gaming\"");
                Matcher m5 = p5.matcher(text);
                int count5 = 0;
                while (m5.find()) {
                    count5 += 1;
                }
                String c5 = Integer.toString(count5);

                Pattern p6 = Pattern.compile("\"health\"");
                Matcher m6 = p6.matcher(text);
                int count6 = 0;
                while (m6.find()) {
                    count6 += 1;
                }
                String c6 = Integer.toString(count6);

                Pattern p7 = Pattern.compile("\"law_crime\"");
                Matcher m7 = p7.matcher(text);
                int count7 = 0;
                while (m7.find()) {
                    count7 += 1;
                }
                String c7 = Integer.toString(count7);

                Pattern p8 = Pattern.compile("\"religion\"");
                Matcher m8 = p8.matcher(text);
                int count8 = 0;
                while (m8.find()) {
                    count8 += 1;
                }
                String c8 = Integer.toString(count8);

                Pattern p9 = Pattern.compile("\"recreation\"");
                Matcher m9 = p9.matcher(text);
                int count9 = 0;
                while (m9.find()) {
                    count9 += 1;
                }
                String c9 = Integer.toString(count9);

                Pattern pa = Pattern.compile("\"science_technology\"");
                Matcher ma = pa.matcher(text);
                int counta = 0;
                while (ma.find()) {
                    counta += 1;
                }
                String ca = Integer.toString(counta);

                Pattern pb = Pattern.compile("\"sports\"");
                Matcher mb = pb.matcher(text);
                int countb = 0;
                while (mb.find()) {
                    countb += 1;
                }
                String cb = Integer.toString(countb);

                Pattern pc = Pattern.compile("\"weather\"");
                Matcher mc = pc.matcher(text);
                int countc = 0;
                while (mc.find()) {
                    countc += 1;
                }
                String cc = Integer.toString(countc);

                return new StringBuilder().append(c1).append(" ").append(c2).append(" ").append(c3).append(" ").append(c4).append(" ").append(c5).append(" ")
                        .append(c6).append(" ").append(c7).append(" ").append(c8).append(" ").append(c9).append(" ").append(ca).append(" ").append(cb).append(" ").append(cc).toString();

            }
            else {
                return getApplicationContext().getResources().getString(R.string.no_data_fail);
            }

        }

        protected void onPostExecute(String message) {

            if (message.equals(getApplicationContext().getResources().getString(R.string.no_data_fail)))   {
                DialogFragment alert = new NoDataStored();
                alert.show(getSupportFragmentManager(), "alert");
            }
            else {

                Intent effortIntent = new Intent(DashboardActivity.this, CategoryActivity.class);
                effortIntent.putExtra(EXTRA_MESSAGE, message);
                startActivity(effortIntent);
            }
        }
    }



    private class LongRunningGetIO4 extends AsyncTask<Void, Void, String> {

        public String executeHttpGet(String url) throws Exception {
            BufferedReader in = null;
            String data = null;

            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(url));
                HttpResponse response = client.execute(request);
                response.getStatusLine().getStatusCode();

                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuilder sb = new StringBuilder("");
                String l = "";
                String nl = System.getProperty("line.separator");
                while ((l = in.readLine()) !=null){
                    sb.append(l + nl);
                }
                in.close();
                data = sb.toString();
                return data;
            } finally{
                if (in != null){
                    try{
                        in.close();
                        return data;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
        //methodos retrieve Most Positive Category
        private String mpcSwitch (String message, String[] parts) {

            String[] strarr = (message.split("SPLITHERE"));
            int[] intarr = new int [strarr.length];
            StringBuilder sb = new StringBuilder("[");

            for (int i=0; i<strarr.length; i++) {
                if (strarr[i] != null && !strarr[i].isEmpty()) {
                    intarr[i] = Integer.parseInt(strarr[i]);

                        sb.append(parts[intarr[i]]).append(" ").append(",").append(" ");

                }
            }
            return sb.append("]").toString();
        }

        //methodos retrieve Most Positive Category URL
        private String goToMPC(String MPCmessage){
//"recreation"
            StringBuilder sb = new StringBuilder("");
            String message = "";

            try {
                JSONArray ja = new JSONArray(MPCmessage);

                for (int i = 0; i < ja.length(); i++) {
                    JSONObject json = ja.getJSONObject(i);
                    if (json.toString().contains("urlres")){
                    JSONObject urlress = json.getJSONObject("urlres");
                    String urlres = urlress.getString("url");
                    if (urlres!=null && !urlres.isEmpty()) {
                        sb.append(urlres).append("\n");
                    }
                }
                }
            }
            catch (JSONException e){
                System.err.println("JSONException " + e.getMessage());
            }

            if (sb.toString()!=null && !sb.toString().isEmpty()) {
                message = sb.toString();
            }
            else {
                message = getApplicationContext().getResources().getString(R.string.fail_flag);
            }
            return message;




        }
        @Override
        protected String doInBackground(Void... params) {

            String returns = "";

            String myself = "";
            if(!Settings.Secure.ANDROID_ID.equals("android_id"))
                myself=Settings.Secure.ANDROID_ID;

            if(Settings.Secure.ANDROID_ID.equals("android_id"))
                myself = android.os.Build.SERIAL;

            String text="https://api.mongolab.com/api/1/databases/dummydb/collections/" + myself + "?apiKey=sWm3hnnxTlUTHiT2r45aaqQkFltSauc6";
            try {
                text = executeHttpGet(text);
            } catch (Exception e9) {
                return e9.getLocalizedMessage();
            }

            if (text!=null && !text.isEmpty()) {

                String[] parts =null; // text.split("_id");
                try {
                    JSONArray ja = new JSONArray(text);
                    parts = new String[ja.length()];

                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject json = ja.getJSONObject(i);
                        parts[i] = json.toString();
                    }
                } catch (Exception e) {
                    System.err.println("JSONException " + e.getMessage());
                }

                int AEpos, Buspos, CIpos, CPpos, Gapos, Hepos, LCpos, Relpos, Recpos, STpos, Sppos, Wepos;
                AEpos = Buspos = CIpos = CPpos = Gapos = Hepos = LCpos = Relpos = Recpos = STpos = Sppos = Wepos = 0;


                StringBuilder AEposarr = new StringBuilder("");

                StringBuilder Busposarr = new StringBuilder("");

                StringBuilder CIposarr = new StringBuilder("");

                StringBuilder CPposarr = new StringBuilder("");

                StringBuilder Gaposarr = new StringBuilder("");

                StringBuilder Heposarr = new StringBuilder("");

                StringBuilder LCposarr = new StringBuilder("");

                StringBuilder Relposarr = new StringBuilder("");

                StringBuilder Recposarr = new StringBuilder("");

                StringBuilder STposarr = new StringBuilder("");

                StringBuilder Spposarr = new StringBuilder("");

                StringBuilder Weposarr = new StringBuilder("");

                JSONObject category=null;
                JSONObject sentiment=null;

                for (int i = 0; i < parts.length; i++) {

                    try {
                        JSONObject json = new JSONObject(parts[i]);
                        category = json.getJSONObject("categoryres");
                        sentiment = json.getJSONObject("sentimentres");

                    }
                    catch (JSONException e){
                        System.err.println("JSONException " + e.getMessage());
                    }

                    if (category.toString().contains("\"arts_entertainment\"") && sentiment.toString().contains("\"positive\"")) {
                        AEposarr.append(i).append("SPLITHERE");
                        AEpos += 1;
                    }


                    if (category.toString().contains("\"business\"") && sentiment.toString().contains("\"positive\"")) {
                        Busposarr.append(i).append("SPLITHERE");
                        Buspos += 1;
                    }


                    if (category.toString().contains("\"computer_internet\"") &&sentiment.toString().contains("\"positive\"")) {
                        CIposarr.append(i).append("SPLITHERE");
                        CIpos += 1;
                    }

                    if (category.toString().contains("\"culture_politics\"") && sentiment.toString().contains("\"positive\"")) {
                        CPposarr.append(i).append("SPLITHERE");
                        CPpos += 1;
                    }

                    if (category.toString().contains("\"gaming\"") && sentiment.toString().contains("\"positive\"")) {
                        Gaposarr.append(i).append("SPLITHERE");
                        Gapos += 1;
                    }

                    if (category.toString().contains("\"health\"") && sentiment.toString().contains("\"positive\"")) {
                        Heposarr.append(i).append("SPLITHERE");
                        Hepos += 1;
                    }

                    if (category.toString().contains("\"law_crime\"") && sentiment.toString().contains("\"positive\"")) {
                        LCposarr.append(i).append("SPLITHERE");
                        LCpos += 1;
                    }

                    if (category.toString().contains("\"religion\"") && sentiment.toString().contains("\"positive\"")) {
                        Relposarr.append(i).append("SPLITHERE");
                        Relpos += 1;
                    }

                    if (category.toString().contains("\"recreation\"") && sentiment.toString().contains("\"positive\"")) {
                        Recposarr.append(i).append("SPLITHERE");
                        Recpos += 1;
                    }

                    if (category.toString().contains("\"science_technology\"") && sentiment.toString().contains("\"positive\"")) {
                        STposarr.append(i).append("SPLITHERE");
                        STpos += 1;
                    }

                    if (category.toString().contains("\"sports\"") &&sentiment.toString().contains("\"positive\"")) {
                        Spposarr.append(i).append("SPLITHERE");
                        Sppos += 1;
                    }

                    if (category.toString().contains("\"weather\"") && sentiment.toString().contains("\"positive\"")) {
                        Weposarr.append(i).append("SPLITHERE");
                        Wepos += 1;
                    }

                }
                int[] positive = new int[12];

                positive[0] = AEpos;
                positive[1] = Buspos;
                positive[2] = CIpos;
                positive[3] = CPpos;
                positive[4] = Gapos;
                positive[5] = Hepos;
                positive[6] = LCpos;
                positive[7] = Relpos;
                positive[8] = Recpos;
                positive[9] = STpos;
                positive[10] = Sppos;
                positive[11] = Wepos;


                int mpc = 0;
                int maxpos = positive[0];
                for (int i = 0; i < positive.length; i++) {
                    if (maxpos < positive[i]) {
                        maxpos = positive[i];
                        mpc = i;
                    }
                }

                switch (mpc) {
                    case 0:
                        returns = mpcSwitch(AEposarr.toString(), parts);
                        break;
                    case 1:
                        returns = mpcSwitch(Busposarr.toString(), parts);
                        break;
                    case 2:
                        returns = mpcSwitch(CIposarr.toString(), parts);
                        break;
                    case 3:
                        returns = mpcSwitch(CPposarr.toString(), parts);
                        break;
                    case 4:
                        returns = mpcSwitch(Gaposarr.toString(), parts);
                        break;
                    case 5:
                        returns = mpcSwitch(Heposarr.toString(), parts);
                        break;
                    case 6:
                        returns = mpcSwitch(LCposarr.toString(), parts);
                        break;
                    case 7:
                        returns = mpcSwitch(Relposarr.toString(), parts);
                        break;
                    case 8:
                        returns = mpcSwitch(Recposarr.toString(), parts);
                        break;
                    case 9:
                        returns = mpcSwitch(STposarr.toString(), parts);
                        break;
                    case 10:
                        returns = mpcSwitch(Spposarr.toString(), parts);
                        break;
                    case 11:
                        returns = mpcSwitch(Weposarr.toString(), parts);
                        break;
                    default:
                        break;

                }
                return goToMPC(returns);
            }
            else {
                return getApplicationContext().getResources().getString(R.string.no_data_fail);
            }

        }

        protected void onPostExecute(String message) {

            if (message.equals(getApplicationContext().getResources().getString(R.string.no_data_fail)))    {
                DialogFragment alert = new NoDataStored();
                alert.show(getSupportFragmentManager(), "alert");
            }
            else {

                Intent effortIntent = new Intent(DashboardActivity.this, DisplayMessageActivity.class);
                effortIntent.putExtra(EXTRA_MESSAGE, message);
                startActivity(effortIntent);
            }
        }
    }

    private class LongRunningGetIO5 extends AsyncTask<Void, Void, String> {

        public String executeHttpGet(String url) throws Exception {
            BufferedReader in = null;
            String data = null;

            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(url));
                HttpResponse response = client.execute(request);
                response.getStatusLine().getStatusCode();

                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuilder sb = new StringBuilder("");
                String l = "";
                String nl = System.getProperty("line.separator");
                while ((l = in.readLine()) !=null){
                    sb.append(l + nl);
                }
                in.close();
                data = sb.toString();
                return data;
            } finally{
                if (in != null){
                    try{
                        in.close();
                        return data;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
        //methodos retrieve Most Negative Category
        private String mncSwitch (String message, String[] parts) {

            String[] strarr = (message.split("SPLITHERE"));
            int[] intarr = new int [strarr.length];
            StringBuilder sb = new StringBuilder("[");

            for (int i=0; i<strarr.length; i++) {
                if (strarr[i] != null && !strarr[i].isEmpty()) {
                    intarr[i] = Integer.parseInt(strarr[i]);
                    sb.append(parts[intarr[i]]).append(" ").append(",").append(" ");
                }
            }

            return sb.append("]").toString();
        }

        //methodos retrieve Most Negative Category URL
        private String goToMNC(String MNCmessage){
//"recreation"
            StringBuilder sb = new StringBuilder("");
            String message = "";

            try {
                JSONArray ja = new JSONArray(MNCmessage);

                for (int i = 0; i < ja.length(); i++) {
                    JSONObject json = ja.getJSONObject(i);
                    if (json.toString().contains("urlres")){
                    JSONObject urlress = json.getJSONObject("urlres");
                    String urlres = urlress.getString("url");
                    if (urlres!=null && !urlres.isEmpty()) {
                        sb.append(urlres).append("\n");
                    }
                }
                }
            }
            catch (JSONException e){
                System.err.println("JSONException " + e.getMessage());
            }

            if (sb.toString()!=null && !sb.toString().isEmpty()) {
                message = sb.toString();
            }
            else {
                message = getApplicationContext().getResources().getString(R.string.fail_flag);
            }
            return message;




        }
        @Override
        protected String doInBackground(Void... params) {

            String returns = "";

            String myself = "";
            if(!Settings.Secure.ANDROID_ID.equals("android_id"))
                myself=Settings.Secure.ANDROID_ID;

            if(Settings.Secure.ANDROID_ID.equals("android_id"))
                myself = android.os.Build.SERIAL;

            String text="https://api.mongolab.com/api/1/databases/dummydb/collections/" + myself + "?apiKey=sWm3hnnxTlUTHiT2r45aaqQkFltSauc6";
            try {
                text = executeHttpGet(text);
            } catch (Exception e9) {
                return e9.getLocalizedMessage();
            }

            if (text!=null && !text.isEmpty()) {


                String[] parts =null; // text.split("_id");
                try {
                    JSONArray ja = new JSONArray(text);
                    parts = new String[ja.length()];

                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject json = ja.getJSONObject(i);
                        parts[i] = json.toString();
                    }
                } catch (Exception e) {
                    System.err.println("JSONException " + e.getMessage());
                }

                int AEneg, Busneg, CIneg, CPneg, Ganeg, Heneg, LCneg, Relneg, Recneg, STneg, Spneg, Weneg;
                AEneg = Busneg = CIneg = CPneg = Ganeg = Heneg = LCneg = Relneg = Recneg = STneg = Spneg = Weneg = 0;

                StringBuilder AEnegarr = new StringBuilder("");

                StringBuilder Busnegarr = new StringBuilder("");

                StringBuilder CInegarr = new StringBuilder("");

                StringBuilder CPnegarr = new StringBuilder("");

                StringBuilder Ganegarr = new StringBuilder("");

                StringBuilder Henegarr = new StringBuilder("");

                StringBuilder LCnegarr = new StringBuilder("");

                StringBuilder Relnegarr = new StringBuilder("");

                StringBuilder Recnegarr = new StringBuilder("");

                StringBuilder STnegarr = new StringBuilder("");

                StringBuilder Spnegarr = new StringBuilder("");

                StringBuilder Wenegarr = new StringBuilder("");

                JSONObject category=null;
                JSONObject sentiment=null;

                for (int i = 0; i < parts.length; i++) {

                    try {
                        JSONObject json = new JSONObject(parts[i]);
                        category = json.getJSONObject("categoryres");
                        sentiment = json.getJSONObject("sentimentres");

                    }
                    catch (JSONException e){
                        System.err.println("JSONException " + e.getMessage());
                    }

                    if (category.toString().contains("\"arts_entertainment\"") && sentiment.toString().contains("\"negative\"")) {
                        AEnegarr.append(i).append("SPLITHERE");
                        AEneg += 1;
                    }

                    if (category.toString().contains("\"business\"") && sentiment.toString().contains("\"negative\"")) {
                        Busnegarr.append(i).append("SPLITHERE");
                        Busneg += 1;
                    }

                    if (category.toString().contains("\"computer_internet\"") && sentiment.toString().contains("\"negative\"")) {
                        CInegarr.append(i).append("SPLITHERE");
                        CIneg += 1;
                    }

                    if (category.toString().contains("\"culture_politics\"") && sentiment.toString().contains("\"negative\"")) {
                        CPnegarr.append(i).append("SPLITHERE");
                        CPneg += 1;
                    }

                    if (category.toString().contains("\"gaming\"") && sentiment.toString().contains("\"negative\"")) {
                        Ganegarr.append(i).append("SPLITHERE");
                        Ganeg += 1;
                    }

                    if (category.toString().contains("\"health\"") && sentiment.toString().contains("\"negative\"")) {
                        Henegarr.append(i).append("SPLITHERE");
                        Heneg += 1;
                    }

                    if (category.toString().contains("\"law_crime\"") && sentiment.toString().contains("\"negative\"")) {
                        LCnegarr.append(i).append("SPLITHERE");
                        LCneg += 1;
                    }

                    if (category.toString().contains("\"religion\"") && sentiment.toString().contains("\"negative\"")) {
                        Relnegarr.append(i).append("SPLITHERE");
                        Relneg += 1;
                    }

                    if (category.toString().contains("\"recreation\"") && sentiment.toString().contains("\"negative\"")) {
                        Recnegarr.append(i).append("SPLITHERE");
                        Recneg += 1;
                    }

                    if (category.toString().contains("\"science_technology\"") && sentiment.toString().contains("\"negative\"")) {
                        STnegarr.append(i).append("SPLITHERE");
                        STneg += 1;
                    }


                    if (category.toString().contains("\"sports\"") && sentiment.toString().contains("\"negative\"")) {
                        Spnegarr.append(i).append("SPLITHERE");
                        Spneg += 1;
                    }

                    if (category.toString().contains("\"weather\"") && sentiment.toString().contains("\"negative\"")) {
                        Wenegarr.append(i).append("SPLITHERE");
                        Weneg += 1;
                    }


                }
                int[] negative = new int[12];

                negative[0] = AEneg;
                negative[1] = Busneg;
                negative[2] = CIneg;
                negative[3] = CPneg;
                negative[4] = Ganeg;
                negative[5] = Heneg;
                negative[6] = LCneg;
                negative[7] = Relneg;
                negative[8] = Recneg;
                negative[9] = STneg;
                negative[10] = Spneg;
                negative[11] = Weneg;


                int mnc = 0;
                int maxneg = negative[0];
                for (int i = 0; i < negative.length; i++) {
                    if (maxneg < negative[i]) {
                        maxneg = negative[i];
                        mnc = i;
                    }
                }

                switch (mnc) {
                    case 0:
                        returns = mncSwitch(AEnegarr.toString(), parts);
                        break;
                    case 1:
                        returns = mncSwitch(Busnegarr.toString(), parts);
                        break;
                    case 2:
                        returns = mncSwitch(CInegarr.toString(), parts);
                        break;
                    case 3:
                        returns = mncSwitch(CPnegarr.toString(), parts);
                        break;
                    case 4:
                        returns = mncSwitch(Ganegarr.toString(), parts);
                        break;
                    case 5:
                        returns = mncSwitch(Henegarr.toString(), parts);
                        break;
                    case 6:
                        returns = mncSwitch(LCnegarr.toString(), parts);
                        break;
                    case 7:
                        returns = mncSwitch(Relnegarr.toString(), parts);
                        break;
                    case 8:
                        returns = mncSwitch(Recnegarr.toString(), parts);
                        break;
                    case 9:
                        returns = mncSwitch(STnegarr.toString(), parts);
                        break;
                    case 10:
                        returns = mncSwitch(Spnegarr.toString(), parts);
                        break;
                    case 11:
                        returns = mncSwitch(Wenegarr.toString(), parts);
                        break;
                    default:
                        break;

                }

                return goToMNC(returns);
            }
            else {
                return getApplicationContext().getResources().getString(R.string.no_data_fail);
            }

        }

        protected void onPostExecute(String message) {

            if (message.equals(getApplicationContext().getResources().getString(R.string.no_data_fail)))    {
                DialogFragment alert = new NoDataStored();
                alert.show(getSupportFragmentManager(), "alert");
            }
            else {

                Intent effortIntent = new Intent(DashboardActivity.this, DisplayMessageActivity.class);
                effortIntent.putExtra(EXTRA_MESSAGE, message);
                startActivity(effortIntent);
            }

        }
    }

    private class LongRunningGetIO6 extends AsyncTask<Void, Void, String> {

        public String executeHttpGet(String url) throws Exception {
            BufferedReader in = null;
            String data = null;

            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(url));
                HttpResponse response = client.execute(request);
                response.getStatusLine().getStatusCode();

                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuilder sb = new StringBuilder("");
                String l = "";
                String nl = System.getProperty("line.separator");
                while ((l = in.readLine()) !=null){
                    sb.append(l + nl);
                }
                in.close();
                data = sb.toString();
                return data;
            } finally{
                if (in != null){
                    try{
                        in.close();
                        return data;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }



        @Override
        protected String doInBackground(Void... params) {


            StringBuilder dateexists = new StringBuilder("");
            StringBuilder ondate = new StringBuilder("");

            String myself = "";
            if (!Settings.Secure.ANDROID_ID.equals("android_id"))
                myself = Settings.Secure.ANDROID_ID;

            if (Settings.Secure.ANDROID_ID.equals("android_id"))
                myself = android.os.Build.SERIAL;

            String text = "https://api.mongolab.com/api/1/databases/dummydb/collections/" + myself + "?apiKey=sWm3hnnxTlUTHiT2r45aaqQkFltSauc6";
            try {
                text = executeHttpGet(text);
            } catch (Exception e9) {
                return e9.getLocalizedMessage();
            }

            if (text != null && !text.isEmpty()) {

                String parts[] = null;

                try {
                    JSONArray ja = new JSONArray(text);
                    parts = new String[ja.length()];

                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject json = ja.getJSONObject(i);
                        parts[i] = json.toString();
                    }
                } catch (Exception e) {
                    System.err.println("JSONException " + e.getMessage());
                }

                for (int i = 0; i < parts.length; i++) {
                    if (parts[i].contains("\"dateres\"")) {
                        dateexists.append(i).append("SPLITTING");
                        try {
                            JSONObject json = new JSONObject(parts[i]);
                            JSONObject dateres = json.getJSONObject("dateres");

                            Date date = new Date();
                            date.setTime(Long.parseLong(dateres.getString("date")));
                            String test = new SimpleDateFormat("dd/M/yyyy", Locale.ENGLISH).format(date);

                    /*
                    int dayspassed = safeLongToInt(TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis()-(Long.parseLong(dateres.getString("date")))));

                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_MONTH, -dayspassed);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy", Locale.ENGLISH);
                    String test = sdf.format(calendar.getTime());
                    */

                            ondate.append(test).append("SPLITTING");
                        } catch (Exception e1) {
                            System.err.println("JSONException " + e1.getMessage());
                        }
                    }
                }

                if (dateexists != null && !dateexists.toString().isEmpty()) {

                    String[] datescnt = dateexists.toString().split("SPLITTING");
                    int[] dateint = new int[datescnt.length];
                    for (int i = 0; i < datescnt.length; i++) {
                        dateint[i] = Integer.parseInt(datescnt[i]);
                    }

                    String[] dates = ondate.toString().split("SPLITTING");

                    String[] datesexperiment = new String[dates.length];
                    System.arraycopy(dates, 0, datesexperiment, 0, dates.length);

                    int morepositive = 0;


                    StringBuilder sbd = new StringBuilder("");


                    for (int k = 0; k < datesexperiment.length; k++) {

                        if (!datesexperiment[k].equals("SKIP")) {
                            String test = datesexperiment[k];

                            for (int i = 0; i < datesexperiment.length; i++) {
                                if (datesexperiment[i].equals(test)) {
                                    sbd.append(i).append("SPLITTING");
                                    datesexperiment[i] = "SKIP";
                                }

                            }
                            sbd.append("NEXT");

                        }
                    }

                    String[] date2 = sbd.toString().split("NEXT");

                    int[] totalpos = new int[dates.length];


            /*
            String[] results= null;

            for (int i=0; i<date2.length; i++) {
                results = date2[i].split("SPLITTING");
            }

            */

                    for (int i = 0; i < date2.length; i++) {
                        String[] date3 = date2[i].split("SPLITTING");
                        int[] date4 = new int[date3.length];

                        for (int k = 0; k < date3.length; k++) {

                            date4[k] = Integer.parseInt(date3[k]);

                            try {
                                JSONObject json = new JSONObject(parts[dateint[date4[k]]]);
                                JSONObject sent = json.getJSONObject("sentimentres");
                                String sents = sent.getString("sentiment");
                                if (sents.contains("positive"))
                                    morepositive += 1;


                            } catch (Exception e2) {
                                System.err.println("JSONException " + e2.getMessage());
                            }
                        }


                        totalpos[i] = morepositive;

                        morepositive = 0;

                    }


                    int totalposmax = totalpos[0];
                    int totalposcnt = 0;


                    for (int i = 0; i < totalpos.length; i++) {
                        if (totalposmax < totalpos[i]) {
                            totalposmax = totalpos[i];
                            totalposcnt = i;
                        }

                    }


                    String[] resultpos2 = date2[totalposcnt].split("SPLITTING");
                    StringBuilder sbp = new StringBuilder("");
                    int[] positarr = new int[resultpos2.length];


                    for (int i = 0; i < resultpos2.length; i++) {
                        try {

                            positarr [i] =Integer.parseInt(resultpos2[i]);
                            JSONObject json = new JSONObject(parts[dateint[positarr[i]]]);

                            if (json.getJSONObject("urlres") != null && !json.getJSONObject("urlres").toString().isEmpty()) {
                                JSONObject sentres = json.getJSONObject("sentimentres");
                                if (sentres.toString().contains("\"positive\"")) {

                                    JSONObject urlres = json.getJSONObject("urlres");
                                    sbp.append(urlres.getString("url")).append("\n");


                                }
                            }
                        } catch (Exception e) {
                            System.err.println("JSONException " + e.getMessage());
                        }
                    }

                    if (sbp.toString() != null && !sbp.toString().isEmpty()) {
                        return sbp.toString();

                    } else {
                        return getApplicationContext().getResources().getString(R.string.fail_flag);
                    }
                } else {
                    return getApplicationContext().getResources().getString(R.string.fail_flag);
                }
            }

        else {
            return getApplicationContext().getResources().getString(R.string.no_data_fail);
        }
        }

        protected void onPostExecute(String message) {

            if (message.equals(getApplicationContext().getResources().getString(R.string.no_data_fail)))    {
                DialogFragment alert = new NoDataStored();
                alert.show(getSupportFragmentManager(), "alert");
            }
            else {

                Intent effortIntent = new Intent(DashboardActivity.this, DisplayMessageActivity.class);
                effortIntent.putExtra(EXTRA_MESSAGE, message);
                startActivity(effortIntent);
            }

        }
    }


    private class LongRunningGetIO7 extends AsyncTask<Void, Void, String> {

        public String executeHttpGet(String url) throws Exception {
            BufferedReader in = null;
            String data = null;

            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(url));
                HttpResponse response = client.execute(request);
                response.getStatusLine().getStatusCode();

                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuilder sb = new StringBuilder("");
                String l = "";
                String nl = System.getProperty("line.separator");
                while ((l = in.readLine()) !=null){
                    sb.append(l + nl);
                }
                in.close();
                data = sb.toString();
                return data;
            } finally{
                if (in != null){
                    try{
                        in.close();
                        return data;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }



        @Override
        protected String doInBackground(Void... params) {


            StringBuilder dateexists = new StringBuilder("");
            StringBuilder ondate = new StringBuilder("");

            String myself = "";
            if (!Settings.Secure.ANDROID_ID.equals("android_id"))
                myself = Settings.Secure.ANDROID_ID;

            if (Settings.Secure.ANDROID_ID.equals("android_id"))
                myself = android.os.Build.SERIAL;

            String text = "https://api.mongolab.com/api/1/databases/dummydb/collections/" + myself + "?apiKey=sWm3hnnxTlUTHiT2r45aaqQkFltSauc6";
            try {
                text = executeHttpGet(text);
            } catch (Exception e9) {
                return e9.getLocalizedMessage();
            }

            if (text != null && !text.isEmpty()) {

                String parts[] = null;

                try {
                    JSONArray ja = new JSONArray(text);
                    parts = new String[ja.length()];

                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject json = ja.getJSONObject(i);
                        parts[i] = json.toString();
                    }
                } catch (Exception e) {
                    System.err.println("JSONException " + e.getMessage());
                }

                for (int i = 0; i < parts.length; i++) {
                    if (parts[i].contains("\"dateres\"")) {
                        dateexists.append(i).append("SPLITTING");
                        try {
                            JSONObject json = new JSONObject(parts[i]);
                            JSONObject dateres = json.getJSONObject("dateres");

                            Date date = new Date();
                            date.setTime(Long.parseLong(dateres.getString("date")));
                            String test = new SimpleDateFormat("dd/M/yyyy", Locale.ENGLISH).format(date);

                    /*
                    int dayspassed = safeLongToInt(TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis()-(Long.parseLong(dateres.getString("date")))));

                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_MONTH, -dayspassed);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy", Locale.ENGLISH);
                    String test = sdf.format(calendar.getTime());
                    */

                            ondate.append(test).append("SPLITTING");
                        } catch (Exception e1) {
                            System.err.println("JSONException " + e1.getMessage());
                        }
                    }
                }

                if (dateexists != null && !dateexists.toString().isEmpty()) {

                    String[] datescnt = dateexists.toString().split("SPLITTING");
                    int[] dateint = new int[datescnt.length];
                    for (int i = 0; i < datescnt.length; i++) {
                        dateint[i] = Integer.parseInt(datescnt[i]);
                    }

                    String[] dates = ondate.toString().split("SPLITTING");

                    String[] datesexperiment = new String[dates.length];
                    System.arraycopy(dates, 0, datesexperiment, 0, dates.length);


                    int morenegative = 0;

                    StringBuilder sbd = new StringBuilder("");


                    for (int k = 0; k < datesexperiment.length; k++) {

                        if (!datesexperiment[k].equals("SKIP")) {
                            String test = datesexperiment[k];

                            for (int i = 0; i < datesexperiment.length; i++) {
                                if (datesexperiment[i].equals(test)) {
                                    sbd.append(i).append("SPLITTING");
                                    datesexperiment[i] = "SKIP";
                                }

                            }
                            sbd.append("NEXT");

                        }
                    }

                    String[] date2 = sbd.toString().split("NEXT");

                    int[] totalneg = new int[dates.length];

            /*
            String[] results= null;

            for (int i=0; i<date2.length; i++) {
                results = date2[i].split("SPLITTING");
            }

            */

                    for (int i = 0; i < date2.length; i++) {
                        String[] date3 = date2[i].split("SPLITTING");
                        int[] date4 = new int[date3.length];

                        for (int k = 0; k < date3.length; k++) {

                            date4[k] = Integer.parseInt(date3[k]);

                            try {
                                JSONObject json = new JSONObject(parts[dateint[date4[k]]]);
                                JSONObject sent = json.getJSONObject("sentimentres");
                                String sents = sent.getString("sentiment");
                                if (sents.contains("negative"))
                                    morenegative += 1;

                            } catch (Exception e2) {
                                System.err.println("JSONException " + e2.getMessage());
                            }
                        }

                        totalneg[i] = morenegative;

                        morenegative = 0;


                    }

                    int totalnegmax = totalneg[0];
                    int totalnegcnt = 0;


                    for (int i = 0; i < totalneg.length; i++) {
                        if (totalnegmax < totalneg[i]) {
                            totalnegmax = totalneg[i];
                            totalnegcnt = i;
                        }

                    }



                    String[] resultneg2 = date2[totalnegcnt].split("SPLITTING");
                    StringBuilder sbn = new StringBuilder("");
                    int[] negatarr = new int[resultneg2.length];


                    for (int i = 0; i < resultneg2.length; i++) {
                        try {

                            negatarr [i] =Integer.parseInt(resultneg2[i]);
                            JSONObject json = new JSONObject(parts[dateint[negatarr[i]]]);

                            if (json.getJSONObject("urlres") != null && !json.getJSONObject("urlres").toString().isEmpty()) {
                                JSONObject sentres = json.getJSONObject("sentimentres");
                                if (sentres.toString().contains("\"negative\"")) {

                                    JSONObject urlres = json.getJSONObject("urlres");
                                    sbn.append(urlres.getString("url")).append("\n");


                                }
                            }
                        } catch (Exception e) {
                            System.err.println("JSONException " + e.getMessage());
                        }
                    }

                    if (sbn.toString() != null && !sbn.toString().isEmpty()) {
                        return sbn.toString();

                    } else {
                        return getApplicationContext().getResources().getString(R.string.fail_flag);
                    }
                } else {
                    return getApplicationContext().getResources().getString(R.string.fail_flag);
                }
            }

            else {
                return getApplicationContext().getResources().getString(R.string.no_data_fail);
            }
        }

        protected void onPostExecute(String message) {

            if (message.equals(getApplicationContext().getResources().getString(R.string.no_data_fail)))    {
                DialogFragment alert = new NoDataStored();
                alert.show(getSupportFragmentManager(), "alert");
            }

            else {
                Intent effortIntent = new Intent(DashboardActivity.this, DisplayMessageActivity.class);
                effortIntent.putExtra(EXTRA_MESSAGE, message);
                startActivity(effortIntent);
            }

        }
    }

    private class LongRunningGetIOToday extends AsyncTask<Void, Void, String> {

        public String executeHttpGet(String url) throws Exception {
            BufferedReader in = null;
            String data = null;

            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(url));
                HttpResponse response = client.execute(request);
                response.getStatusLine().getStatusCode();

                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuilder sb = new StringBuilder("");
                String l = "";
                String nl = System.getProperty("line.separator");
                while ((l = in.readLine()) !=null){
                    sb.append(l + nl);
                }
                in.close();
                data = sb.toString();
                return data;
            } finally{
                if (in != null){
                    try{
                        in.close();
                        return data;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
        @Override
        protected String doInBackground(Void... params) {

            String myself = "";
            if(!Settings.Secure.ANDROID_ID.equals("android_id"))
                myself=Settings.Secure.ANDROID_ID;

            if(Settings.Secure.ANDROID_ID.equals("android_id"))
                myself = android.os.Build.SERIAL;

//https://api.mongolab.com/api/1/databases/dummydb/collections/099d79de06aa2350?q={%22dateres%22:%20{%22$exists%22:%20true}}&apiKey=sWm3hnnxTlUTHiT2r45aaqQkFltSauc6
            // =%3D  {%7B  }%7D
            String text="https://api.mongolab.com/api/1/databases/dummydb/collections/" + myself + "?apiKey=sWm3hnnxTlUTHiT2r45aaqQkFltSauc6";

            try {
                text = executeHttpGet(text);
            } catch (Exception e9) {
                return e9.getLocalizedMessage();
            }
            StringBuilder sbtoday = new StringBuilder("[ ");
            int null_counter=0;

            if (text!=null && !text.isEmpty()) {

                try {
                    JSONArray ja = new JSONArray(text);
                    boolean flag = false;


                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject json = ja.getJSONObject(i);
                        if (json.toString().contains("dateres")) {

                            JSONObject dateres = json.getJSONObject("dateres");
                            Long tocheck = Long.parseLong(dateres.getString("date"));

                            if (DateUtils.isToday(tocheck)) {
                                if (flag) {
                                    sbtoday.append(",");
                                }
                                sbtoday.append(json.toString()).append(" ");
                                null_counter += 1;
                                flag = true;
                            }
                        }
                    }
                    sbtoday.append("]");
                }
                catch (Exception e) {
                    System.err.println("JSONException " + e.getMessage());
                }
                if (null_counter!=0) {
                    return sbtoday.toString();
                }
                else {
                    return getApplicationContext().getResources().getString(R.string.no_data_fail);
                }

            }
            else {
                return getApplicationContext().getResources().getString(R.string.no_data_fail);
            }

        }

        protected void onPostExecute(String message) {

            if (message.equals(getApplicationContext().getResources().getString(R.string.no_data_fail))) {
                DialogFragment alert = new NoDataStored();
                alert.show(getSupportFragmentManager(), "alert");
            }

            else {
                Intent effortIntent = new Intent(DashboardActivity.this, DashboardActivityToday.class);
                effortIntent.putExtra(EXTRA_MESSAGE, message);
                startActivity(effortIntent);
            }
        }
    }


    private class LongRunningGetIOWeek extends AsyncTask<Void, Void, String> {

        public String executeHttpGet(String url) throws Exception {
            BufferedReader in = null;
            String data = null;

            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(url));
                HttpResponse response = client.execute(request);
                response.getStatusLine().getStatusCode();

                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuilder sb = new StringBuilder("");
                String l = "";
                String nl = System.getProperty("line.separator");
                while ((l = in.readLine()) !=null){
                    sb.append(l + nl);
                }
                in.close();
                data = sb.toString();
                return data;
            } finally{
                if (in != null){
                    try{
                        in.close();
                        return data;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
        @Override
        protected String doInBackground(Void... params) {

            String myself = "";
            if(!Settings.Secure.ANDROID_ID.equals("android_id"))
                myself=Settings.Secure.ANDROID_ID;

            if(Settings.Secure.ANDROID_ID.equals("android_id"))
                myself = android.os.Build.SERIAL;

//https://api.mongolab.com/api/1/databases/dummydb/collections/099d79de06aa2350?q={%22dateres%22:%20{%22$exists%22:%20true}}&apiKey=sWm3hnnxTlUTHiT2r45aaqQkFltSauc6
            // =%3D  {%7B  }%7D
            String text="https://api.mongolab.com/api/1/databases/dummydb/collections/" + myself + "?apiKey=sWm3hnnxTlUTHiT2r45aaqQkFltSauc6";

            try {
                text = executeHttpGet(text);
            } catch (Exception e9) {
                return e9.getLocalizedMessage();
            }
            StringBuilder sbtoday = new StringBuilder("[ ");
            int null_counter=0;

            if (text!=null && !text.isEmpty()) {

                try {
                    JSONArray ja = new JSONArray(text);
                    boolean flag = false;


                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject json = ja.getJSONObject(i);
                        if (json.toString().contains("dateres")) {

                            JSONObject dateres = json.getJSONObject("dateres");
                            Long tocheck = Long.parseLong(dateres.getString("date"));

                            long now = System.currentTimeMillis();
                            long checkpoint = now - DateUtils.WEEK_IN_MILLIS;

                            if (tocheck > checkpoint) {
                                if (flag) {
                                    sbtoday.append(",");
                                }
                                sbtoday.append(json.toString()).append(" ");
                                null_counter += 1;
                                flag = true;
                            }
                        }
                    }
                    sbtoday.append("]");
                }
                catch (Exception e) {
                    System.err.println("JSONException " + e.getMessage());
                }
                if (null_counter!=0) {
                    return sbtoday.toString();
                }
                else {
                    return getApplicationContext().getResources().getString(R.string.no_data_fail);
                }

            }
            else {
                return getApplicationContext().getResources().getString(R.string.no_data_fail);
            }

        }

        protected void onPostExecute(String message) {

            if (message.equals(getApplicationContext().getResources().getString(R.string.no_data_fail))) {
                DialogFragment alert = new NoDataStored();
                alert.show(getSupportFragmentManager(), "alert");
            }

            else {
                Intent effortIntent = new Intent(DashboardActivity.this, DashboardActivityWeek.class);
                effortIntent.putExtra(EXTRA_MESSAGE, message);
                startActivity(effortIntent);
            }
        }
    }


}
