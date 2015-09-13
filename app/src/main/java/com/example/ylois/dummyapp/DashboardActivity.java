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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DashboardActivity extends ActionBarActivity implements View.OnClickListener {

    public final static String EXTRA_MESSAGE = "com.example.ylois.dummyapp.MESSAGE";
    public String returns = "";

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

        barchart();

    }

    //methodos elegxou an einai connected sto internet
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
                    goToMPC(returns);
                } else {
                    DialogFragment alert = new NoInternetDialogFragment();
                    alert.show(getSupportFragmentManager(), "alert");
                }
                break;
        }
    }

    private void barchart() {

        int AEneg, AEpos, Busneg, Buspos, CIneg, CIpos, CPneg, CPpos, Ganeg, Gapos, Heneg, Hepos, LCneg, LCpos, Relneg, Relpos, Recneg, Recpos, STneg, STpos, Spneg, Sppos, Weneg, Wepos;
        AEneg = AEpos = Busneg = Buspos = CIneg = CIpos = CPneg = CPpos = Ganeg = Gapos = Heneg = Hepos = LCneg = LCpos = Relneg = Relpos = Recneg = Recpos = STneg = STpos = Spneg = Sppos = Weneg = Wepos = 0;

        View barChart;
        String[] mCategories = new String[]{"Arts", "Business",
                "Computers", "Politics", "Gaming", "Health", "Law & Crime",
                "Religion", "Recreation", "Science", "Sports", "Weather"};
        int[] x = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        int[] positive = new int[x.length];
        int[] negative = new int[x.length];

        Intent intent = getIntent();
        String message = intent.getStringExtra(MyActivity.EXTRA_MESSAGE);
        String[] parts = message.split("_id");

        int AEnegarr[] = new int[]{};
        int AEposarr[] = new int[]{};
        int Busnegarr[] = new int[]{};
        int Busposarr[] = new int[]{};
        int CInegarr[] = new int[]{};
        int CIposarr[] = new int[]{};
        int CPnegarr[] = new int[]{};
        int CPposarr[] = new int[]{};
        int Ganegarr[] = new int[]{};
        int Gaposarr[] = new int[]{};
        int Henegarr[] = new int[]{};
        int Heposarr[] = new int[]{};
        int LCnegarr[] = new int[]{};
        int LCposarr[] = new int[]{};
        int Relnegarr[] = new int[]{};
        int Relposarr[] = new int[]{};
        int Recnegarr[] = new int[]{};
        int Recposarr[] = new int[]{};
        int STnegarr[] = new int[]{};
        int STposarr[] = new int[]{};
        int Spnegarr[] = new int[]{};
        int Spposarr[] = new int[]{};
        int Wenegarr[] = new int[]{};
        int Weposarr[] = new int[]{};

        for (int i = 0; i < parts.length; i++) {
            if (parts[i].contains("\"arts_entertainment\"") && parts[i].contains("\"negative\"")) {
                AEnegarr[AEneg] = i;
                AEneg += 1;
            }
            if (parts[i].contains("\"arts_entertainment\"") && parts[i].contains("\"positive\"")) {
                AEposarr[AEpos] = i;
                AEpos += 1;
            }

            if (parts[i].contains("\"business\"") && parts[i].contains("\"negative\"")) {
                Busnegarr[Busneg] = i;
                Busneg += 1;
            }

            if (parts[i].contains("\"business\"") && parts[i].contains("\"positive\"")) {
                Busposarr[Buspos] = i;
                Buspos += 1;
            }


            if (parts[i].contains("\"computer_internet\"") && parts[i].contains("\"negative\"")) {
                CInegarr[CIneg] = i;
                CIneg += 1;
            }
            if (parts[i].contains("\"computer_internet\"") && parts[i].contains("\"positive\"")) {
                CIposarr[CIpos] = i;
                CIpos += 1;
            }

            if (parts[i].contains("\"culture_politics\"") && parts[i].contains("\"negative\"")) {
                CPnegarr[CPneg] = i;
                CPneg += 1;
            }
            if (parts[i].contains("\"culture_politics\"") && parts[i].contains("\"positive\"")) {
                CPposarr[CPpos] = i;
                CPpos += 1;
            }

            if (parts[i].contains("\"gaming\"") && parts[i].contains("\"negative\"")) {
                Ganegarr[Ganeg] = i;
                Ganeg += 1;
            }
            if (parts[i].contains("\"gaming\"") && parts[i].contains("\"positive\"")) {
                Gaposarr[Gapos] = i;
                Gapos += 1;
            }

            if (parts[i].contains("\"health\"") && parts[i].contains("\"negative\"")) {
                Henegarr[Heneg] = i;
                Heneg += 1;
            }
            if (parts[i].contains("\"health\"") && parts[i].contains("\"positive\"")) {
                Heposarr[Hepos] = i;
                Hepos += 1;
            }

            if (parts[i].contains("\"law_crime\"") && parts[i].contains("\"negative\"")) {
                LCnegarr[LCneg] = i;
                LCneg += 1;
            }
            if (parts[i].contains("\"law_crime\"") && parts[i].contains("\"positive\"")) {
                LCposarr[LCpos] = i;
                LCpos += 1;
            }

            if (parts[i].contains("\"religion\"") && parts[i].contains("\"negative\"")) {
                Relnegarr[Relneg] = i;
                Relneg += 1;
            }
            if (parts[i].contains("\"religion\"") && parts[i].contains("\"positive\"")) {
                Relposarr[Relpos] = i;
                Relpos += 1;
            }

            if (parts[i].contains("\"recreation\"") && parts[i].contains("\"negative\"")) {
                Recnegarr[Recneg] = i;
                Recneg += 1;
            }
            if (parts[i].contains("\"recreation\"") && parts[i].contains("\"positive\"")) {
                Recposarr[Recpos] = i;
                Recpos += 1;
            }

            if (parts[i].contains("\"science_technology\"") && parts[i].contains("\"negative\"")) {
                STnegarr[STneg] = i;
                STneg += 1;
            }
            if (parts[i].contains("\"science_technology\"") && parts[i].contains("\"positive\"")) {
                STposarr[STpos] = i;
                STpos += 1;
            }

            if (parts[i].contains("\"sports\"") && parts[i].contains("\"negative\"")) {
                Spnegarr[Spneg] = i;
                Spneg += 1;
            }
            if (parts[i].contains("\"sports\"") && parts[i].contains("\"positive\"")) {
                Spposarr[Sppos] = i;
                Sppos += 1;
            }

            if (parts[i].contains("\"weather\"") && parts[i].contains("\"negative\"")) {
                Wenegarr[Weneg] = i;
                Weneg += 1;
            }
            if (parts[i].contains("\"weather\"") && parts[i].contains("\"positive\"")) {
                Weposarr[Wepos] = i;
                Wepos += 1;
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
        int maxpos = 0;
        int mpc = 0;
        maxpos = positive[0];
        for (int i = 0; i < positive.length; i++)
            if (maxpos < positive[i]) {
                maxpos = positive[i];
                mpc = i;
            }
        TextView mpctext = (TextView) findViewById(R.id.MPosCat);
        mpctext.setText(getApplicationContext().getResources().getString(R.string.sbc_3) + mCategories[mpc]);


        StringBuilder sb = new StringBuilder("");
        switch (mpc) {
            case 0:
                for (int i=0; i<AEposarr.length; i++)
                {
                   sb.append(parts[AEposarr[i]]).append("");

                }
                break;
            case 1:
                for (int i=0; i<Busposarr.length; i++)
                {
                    sb.append(parts[Busposarr[i]]).append("");
                }
                break;
            case 2:
                for (int i=0; i<CIposarr.length; i++)
                {
                    sb.append(parts[CIposarr[i]]).append("");;
                }
                break;
            case 3:
                for (int i=0; i<CPposarr.length; i++)
                {
                    sb.append(parts[CPposarr[i]]).append("");
                }
                break;
            case 4:
                for (int i=0; i<Gaposarr.length; i++)
                {
                    sb.append(parts[Gaposarr[i]]).append("");
                }
                break;
            case 5:
                for (int i=0; i<Heposarr.length; i++)
                {
                    sb.append(parts[Heposarr[i]]).append("");
                }
                break;
            case 6:
                for (int i=0; i<LCposarr.length; i++)
                {
                    sb.append(parts[LCposarr[i]]).append("");
                }
                break;
            case 7:
                for (int i=0; i<Relposarr.length; i++)
                {
                    sb.append(parts[Relposarr[i]]).append("");
                }
                break;
            case 8:
                for (int i=0; i<Recposarr.length; i++)
                {
                    sb.append(parts[Recposarr[i]]).append("");
                }
                break;
            case 9:
                for (int i=0; i<STposarr.length; i++)
                {
                    sb.append(parts[STposarr[i]]).append("");
                }
                break;
            case 10:
                for (int i=0; i<Spposarr.length; i++)
                {
                    sb.append(parts[Spposarr[i]]).append("");
                }
                break;
            case 11:
                for (int i=0; i<Weposarr.length; i++)
                {
                    sb.append(parts[Weposarr[i]]).append("");
                }
                break;
            default:
                break;

        }
        returns = sb.toString();







    }

    private void goToMPC(String MPCmessage){

        String [] mpcarr = MPCmessage.split("urlres");
        StringBuilder sb = new StringBuilder("");
        String urlres = "";

        for (int i=0; i<mpcarr.length; i++)
        {
            try {
                JSONObject json = new JSONObject(mpcarr[i]);
                urlres = json.getString("url");
                sb.append(urlres).append("\n");

            } catch (JSONException e) {
                System.err.println("JSONException " + e.getMessage());
            }
        }
        Intent effortIntent = new Intent(DashboardActivity.this, DisplayMessageActivity.class);
        effortIntent.putExtra(EXTRA_MESSAGE, sb.toString());
        startActivity(effortIntent);


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

            Pattern p = Pattern.compile("\"positive\"");
            Matcher m = p.matcher(text);
            int count = 0;
            while (m.find()){
                count +=1;
            }
          String cpos =Integer.toString(count);

            Pattern p2 = Pattern.compile("\"negative\"");
            Matcher m2 = p2.matcher(text);
            int count2 = 0;
            while (m2.find()){
                count2 +=1;
            }
            String cneg =Integer.toString(count2);

            Pattern p3 = Pattern.compile("\"neutral\"");
            Matcher m3 = p3.matcher(text);
            int count3 = 0;
            while (m3.find()){
                count3 +=1;
            }
            String cneut =Integer.toString(count3);

            return new StringBuilder().append(cpos).append(" ").append(cneg).append(" ").append(cneut).toString();

        }

        protected void onPostExecute(String message) {

            Intent effortIntent = new Intent(DashboardActivity.this, SentimentActivity.class);
            effortIntent.putExtra(EXTRA_MESSAGE, message);
            startActivity(effortIntent);

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

            Pattern p = Pattern.compile("\"arts_entertainment\"");
            Matcher m = p.matcher(text);
            int count = 0;
            while (m.find()){
                count +=1;
            }
            String c1 =Integer.toString(count);

            Pattern p2 = Pattern.compile("\"business\"");
            Matcher m2 = p2.matcher(text);
            int count2 = 0;
            while (m2.find()){
                count2 +=1;
            }
            String c2 =Integer.toString(count2);

            Pattern p3 = Pattern.compile("\"computer_internet\"");
            Matcher m3 = p3.matcher(text);
            int count3 = 0;
            while (m3.find()){
                count3 +=1;
            }
            String c3 =Integer.toString(count3);

            Pattern p4 = Pattern.compile("\"culture_politics\"");
            Matcher m4 = p4.matcher(text);
            int count4 = 0;
            while (m4.find()){
                count4 +=1;
            }
            String c4 =Integer.toString(count4);

            Pattern p5 = Pattern.compile("\"gaming\"");
            Matcher m5 = p5.matcher(text);
            int count5 = 0;
            while (m5.find()){
                count5 +=1;
            }
            String c5 =Integer.toString(count5);

            Pattern p6 = Pattern.compile("\"health\"");
            Matcher m6 = p6.matcher(text);
            int count6 = 0;
            while (m6.find()){
                count6 +=1;
            }
            String c6 =Integer.toString(count6);

            Pattern p7 = Pattern.compile("\"law_crime\"");
            Matcher m7 = p7.matcher(text);
            int count7 = 0;
            while (m7.find()){
                count7 +=1;
            }
            String c7 =Integer.toString(count7);

            Pattern p8 = Pattern.compile("\"religion\"");
            Matcher m8 = p8.matcher(text);
            int count8 = 0;
            while (m8.find()){
                count8 +=1;
            }
            String c8 =Integer.toString(count8);

            Pattern p9 = Pattern.compile("\"recreation\"");
            Matcher m9 = p9.matcher(text);
            int count9 = 0;
            while (m9.find()){
                count9 +=1;
            }
            String c9 =Integer.toString(count9);

            Pattern pa = Pattern.compile("\"science_technology\"");
            Matcher ma = pa.matcher(text);
            int counta = 0;
            while (ma.find()){
                counta +=1;
            }
            String ca =Integer.toString(counta);

            Pattern pb = Pattern.compile("\"sports\"");
            Matcher mb = pb.matcher(text);
            int countb = 0;
            while (mb.find()){
                countb +=1;
            }
            String cb =Integer.toString(countb);

            Pattern pc = Pattern.compile("\"weather\"");
            Matcher mc = pc.matcher(text);
            int countc = 0;
            while (mc.find()){
                countc +=1;
            }
            String cc =Integer.toString(countc);

            return new StringBuilder().append(c1).append(" ").append(c2).append(" ").append(c3).append(" ").append(c4).append(" ").append(c5).append(" ")
                    .append(c6).append(" ").append(c7).append(" ").append(c8).append(" ").append(c9).append(" ").append(ca).append(" ").append(cb).append(" ").append(cc).toString();

        }

        protected void onPostExecute(String message) {

            Intent effortIntent = new Intent(DashboardActivity.this, CategoryActivity.class);
            effortIntent.putExtra(EXTRA_MESSAGE, message);
            startActivity(effortIntent);

        }
    }





}
