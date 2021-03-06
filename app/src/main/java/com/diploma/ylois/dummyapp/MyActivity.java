package com.diploma.ylois.dummyapp;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Browser;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.List;


public class MyActivity extends ActionBarActivity implements OnClickListener {

    public final static String EXTRA_MESSAGE = "com.diploma.ylois.dummyapp.MESSAGE";
//    ProgressBar pb;
    ProgressDialog progressDialog;


     /*      JSON Node names YAHOO
    private static final String TAG_ENTITY ="entity";
    private static final String TAG_CONTENT="content";
    private static final String TAG_TEXT="text";
    private static final String TAG_QUERY="query";
    private static final String TAG_RESULTS="results";
    private static final String TAG_ENTITIES="entities";
    private static final String TAG_WIKI_URL="wiki_url";
    */

    //json node names for text extraction
    private static final String TAG_DOCSENTIMENT = "docSentiment";
    private static final String TAG_TYPE = "type";
    //json node names for category
    private static final String TAG_CATEGORY = "category";
    //json node names for alchemy term
    private static final String TAG_KEYWORDS = "keywords";
    private static final String TAG_TEXT = "text";
    private static final String TAG_RELEVANCE = "relevance";

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

    //class for chrome not running warning alert dialog
    public static class NoChromeRunningFragment extends DialogFragment{
        @Override
        public Dialog onCreateDialog (Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.warning_3)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            return builder.create();
        }
    }

    //class for first time running warning
    public static class FirstTimeFragment extends DialogFragment{
        @Override
        public Dialog onCreateDialog (Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.first_time)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            return builder.create();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        //warning for first time running CHAT
        File file = new File("data/data/com.diploma.ylois.dummyapp/files/lastDate.txt");
        if (!file.exists()){
            DialogFragment alert = new FirstTimeFragment();
            alert.show(getSupportFragmentManager(), "alert");
        }

//        pb = (ProgressBar) findViewById(R.id.progressBar);
        Button b1 = (Button) findViewById(R.id.b1);
        Button b2 = (Button) findViewById(R.id.b2);
//version2 axristo        Button b3 = (Button) findViewById(R.id.b3);
        Button b4 = (Button) findViewById(R.id.b4);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
//version2 axristo        b3.setOnClickListener(this);
        b4.setOnClickListener(this);

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
            case R.id.b1:
                ActivityManager activityManager = (ActivityManager) this.getSystemService( ACTIVITY_SERVICE );
                List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
                int counterchr = 0;
                for(int i = 0; i < procInfos.size(); i++)
                {
                    if(procInfos.get(i).processName.equals("com.android.chrome"))
                    {
                        if (isNetworkAvailable()) {
                            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                            counterchr+=1;
                            new LongRunningGetIO().execute();
                        }
                     else {
                        DialogFragment alert = new NoInternetDialogFragment();
                        alert.show(getSupportFragmentManager(), "alert");
                    }
                    }
                }
                if (counterchr==0)
                {
                    DialogFragment alert = new NoChromeRunningFragment();
                    alert.show(getSupportFragmentManager(), "alert");
                }

                break;
            case R.id.b2:
                if (isNetworkAvailable())
                {
                new LongRunningGetIO2().execute();
                }
                else {
                    DialogFragment alert = new NoInternetDialogFragment();
                    alert.show(getSupportFragmentManager(), "alert");
                }
                break;
/*versio2 axristo            case R.id.b3:
                if (isNetworkAvailable())
                {
                new LongRunningGetIO3().execute();
                }
            else {
                    DialogFragment alert = new NoInternetDialogFragment();
                    alert.show(getSupportFragmentManager(), "alert");
                }
                break;
  */
            case R.id.b4:
            {
                Intent aboutIntent = new Intent(this, AboutActivity.class);
                startActivity(aboutIntent);
            }
                break;
    }
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

    /*
      Called when the user clicks the first button
     */
    private class LongRunningGetIO extends AsyncTask<Void, Integer, String> {

        protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {

            InputStream in = entity.getContent();

            StringBuilder out = new StringBuilder();
            int n = 1;
            while (n > 0) {
                byte[] b = new byte[4096];

                n = in.read(b);

                if (n > 0) out.append(new String(b, 0, n));

            }
            return out.toString();
        }

        //method for uploading to mongolab
        public  String executeHttpPost(String url, JSONObject json) throws Exception {
            BufferedReader in = null;
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost(url);

                request.setEntity(new ByteArrayEntity(json.toString().getBytes("UTF-8")));
                request.setHeader( "Content-Type", "application/json");
                HttpResponse response = client.execute(request);
                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                StringBuilder sb = new StringBuilder("");
                String line = "";
                String NL = System.getProperty("line.separator");
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                    sb.append(NL);
                }
                in.close();
                return sb.toString();

            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
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




            String url = "";
/*
            Long start=System.currentTimeMillis()-604800000L; //last week in milliseconds
            Long end=System.currentTimeMillis();
            String startdate=Long.toString(start);
            String enddate=Long.toString(end);
*/
            String[] proj = new String[]{Browser.BookmarkColumns.TITLE, Browser.BookmarkColumns.URL, Browser.BookmarkColumns.DATE};
            Uri uriCustom = Uri.parse("content://com.android.chrome.browser/bookmarks");
            String sel = Browser.BookmarkColumns.BOOKMARK + " = 0" ; // 0 = history, 1 = bookmark , + " AND " + Browser.BookmarkColumns.DATE + "BETWEEN ? AND ?"
            ContentResolver cr = getContentResolver();
            Cursor mCur = cr.query(uriCustom, proj, sel, null, null); // new String[]{startdate, enddate}


            String dateCompare = "";

            try {

                File file = new File("data/data/com.diploma.ylois.dummyapp/files/lastDate.txt");


                if ( file.exists() ) {
                    InputStream inputStream = openFileInput("lastDate.txt");
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String receiveString = "";
                    StringBuilder stringBuilder = new StringBuilder();

                    while ( (receiveString = bufferedReader.readLine()) != null ) {
                        stringBuilder.append(receiveString);
                    }

                    inputStream.close();
                    dateCompare = stringBuilder.toString();
                }
                else {

                    dateCompare = "0";
                }
            }
            catch (FileNotFoundException e) {
                Log.e("login activity", "File not found: " + e.toString());
            } catch (IOException e) {
                Log.e("login activity", "Can not read file: " + e.toString());
            }



            String title = "";
            String message = "";
//            String message2= "";
            String returns = "";
            JSONObject combined = null;
 //fffff           String text = "";
 //fffff           String text2="";
            String date="";
            String date2="";
            int control=0;

            /*
            try {
                mCur.moveToFirst();
            }
            catch (Exception e11)
            {
                returns="There is no browser history.";
                return returns;
            }


*/


            //allagi se moveToLast gia to pio prosfato history item, sto while ean usaristei thelei beforeFirst

//            int counter=1;
//            int denom =mCur.getCount();
            if (mCur.moveToFirst() && mCur.getCount() > 0)

            {
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                mCur.moveToFirst();

                while (!mCur.isAfterLast()) {
                    title = mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.TITLE));
                    message = mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.URL));
                    date = mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.DATE));
//remove deleting history items                    Browser.deleteFromHistory(cr, mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.URL)));
//reference later                    Long timestamp = Long.parseLong(date);
                    Long dateCompare2 = Long.parseLong(dateCompare);
                    Long dateC = Long.parseLong(date);
                    if (dateCompare2 < dateC) {

                        try {
                            url = URLEncoder.encode(message, "UTF-8");
                        } catch (UnsupportedEncodingException e1) {
                            return e1.getLocalizedMessage();
                        }
/*      YAHOO KEY EXTRACTION - REPLACED WITH ALCHEMY TERM EXTRACTION
            String resu = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20contentanalysis.analyze%20where%20url%3D%27" + url + "%27%3B&format=json&diagnostics=true&callback=";

            HttpGet httpGet = new HttpGet(resu);
            String text = "";
            try {

                HttpResponse response = httpClient.execute(httpGet, localContext);

                HttpEntity entity = response.getEntity();

                text = getASCIIContentFromEntity(entity);

            } catch (Exception e) {
                return e.getLocalizedMessage();
            }

 */
/*fffff
                        String termAlchemy = "http://access.alchemyapi.com/calls/url/URLGetRankedKeywords?apikey=7af70ab4d132580daebfd7d69d35873cb6860fc1&url=" + url + "&outputMode=json&keywordExtractMode=strict";
                        HttpGet httpGet = new HttpGet(termAlchemy);

                        try {

                            HttpResponse response = httpClient.execute(httpGet, localContext);

                            HttpEntity entity = response.getEntity();

                            text = getASCIIContentFromEntity(entity);

                        } catch (Exception e) {
                            return e.getLocalizedMessage();
                        }

                        */

                        String alchemy = "http://access.alchemyapi.com/calls/url/URLGetTextSentiment?apikey=7af70ab4d132580daebfd7d69d35873cb6860fc1&url=" + url + "&outputMode=json";
                        HttpGet httpGet2 = new HttpGet(alchemy);
                        String sentiment = "";
                        try {

                            HttpResponse response = httpClient.execute(httpGet2, localContext);

                            HttpEntity entity = response.getEntity();

                            sentiment = getASCIIContentFromEntity(entity);

                        } catch (Exception e) {
                            return e.getLocalizedMessage();
                        }


                        String CatAlchemy = "http://access.alchemyapi.com/calls/url/URLGetCategory?url=" + url + "&apikey=7af70ab4d132580daebfd7d69d35873cb6860fc1&outputMode=json";
                        HttpGet httpGet3 = new HttpGet(CatAlchemy);
                        String categories = "";
                        try {

                            HttpResponse response = httpClient.execute(httpGet3, localContext);

                            HttpEntity entity = response.getEntity();

                            categories = getASCIIContentFromEntity(entity);

                        } catch (Exception e) {
                            return e.getLocalizedMessage();
                        }
//creating json object for title

                        if (title != null && !title.isEmpty()) {
                            title = title.trim().replace('\'', ' ').replace('"', ' ');
                            try {
                                title = new JSONObject().put("title", title).toString();
                            } catch (JSONException e1) {
                                System.err.println("JSONException " + e1.getMessage());
                            }
                        } else {
                            title = getApplicationContext().getString(R.string.no_title);
                            try {
                                title = new JSONObject().put("title", title).toString();
                            } catch (JSONException e1) {
                                System.err.println("JSONException " + e1.getMessage());
                            }
                        }

/*fffff
//refining text results for 70% relativity

                        StringBuilder sb = new StringBuilder();
                        sb.append("{text:'");
                        try {
                            JSONObject json = new JSONObject(text);
                            JSONArray ja;
                            ja = json.getJSONArray(TAG_KEYWORDS);
                            boolean appendSeparator = false;
                            for (int i = 0; i < ja.length(); i++) {
                                JSONObject resultObject = ja.getJSONObject(i);
                                String name = resultObject.getString(TAG_TEXT);
                                String comparison = resultObject.getString(TAG_RELEVANCE);
                                Float foo = Float.parseFloat(comparison.trim());

                                if (foo > 0.7) {
                                    if (appendSeparator)
                                        sb.append(", ");
                                    appendSeparator = true;
                                    sb.append(name);


                                }
                            }
                            sb.append("'");
                            sb.append("}");

                            text2 = sb.toString();
                        } catch (JSONException e6) {
                            System.err.println("JSONException " + e6.getMessage());
                        }
                        */

//refining category results
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("{category:'");
                        try {
                            JSONObject json = new JSONObject(categories);
                            String categ = json.getString(TAG_CATEGORY);
                            sb3.append(categ);
                            sb3.append("'}");
                            categories = sb3.toString();
                        } catch (JSONException e7) {
                            System.err.println("JSONException " + e7.getMessage());
                        }

//refining sentiment results
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("{sentiment:'");
                        try {
                            JSONObject json = new JSONObject(sentiment);
                            JSONObject docSentiment = json.getJSONObject(TAG_DOCSENTIMENT);
                            String type = docSentiment.getString(TAG_TYPE);
                            sb2.append(type);
                            sb2.append("'}");
                            sentiment = sb2.toString();
                        } catch (JSONException e5) {
                            System.err.println("JSONException " + e5.getMessage());
                        }
//creating json object for date

                        try {
                            date2 = new JSONObject().put("date", date).toString();
                        } catch (JSONException e10) {
                            System.err.println("JSONException " + e10.getMessage());
                        }

//creating json object for url
                        try {
                            message = new JSONObject().put("url", message).toString();
                        }catch (JSONException e11) {
                            System.err.println("JSONException " + e11.getMessage());
                        }
//merge the json objects to one
                        try {
                      //      JSONObject obj1 = new JSONObject(text2);
                            JSONObject obj2 = new JSONObject(sentiment);
                            JSONObject obj3 = new JSONObject(categories);
                            JSONObject obj4 = new JSONObject(title);
                            JSONObject obj5 = new JSONObject(date2);
                            JSONObject obj6 = new JSONObject(message);
                            combined = new JSONObject();
                     //       combined.put("textres", obj1);
                            combined.put("sentimentres", obj2);
                            combined.put("categoryres", obj3);
                            combined.put("titleres", obj4);
                            combined.put("dateres", obj5);
                            combined.put("urlres", obj6);
                        } catch (JSONException e18) {
                            System.err.println("JSONException " + e18.getMessage());
                        }

//upload json obj to mongolab -> executeHttpPut

                        String myuri = "https://api.mongolab.com/api/1/databases/dummydb/collections/" + myself + "?apiKey=sWm3hnnxTlUTHiT2r45aaqQkFltSauc6";

                        try {
                            returns = executeHttpPost(myuri, combined);
                        } catch (Exception e9) {
                            return  combined.toString();
                        }

                        //PROGRESS BAR
/*                    int result = Math.round(counter*100/denom);
                    publishProgress(result);
                    counter++;

*/
                        control++;

                    }
                    mCur.moveToNext();
                }

                try {
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("lastDate.txt", Context.MODE_PRIVATE));
                    outputStreamWriter.write(date);
                    outputStreamWriter.close();
                } catch (IOException e) {
                    Log.e("Exception", "File write failed: " + e.toString());
                }
                // When HttpClient instance is no longer needed,
                // shut down the connection manager to ensure
                // immediate deallocation of all system resources
                httpClient.getConnectionManager().shutdown();

                if (control == 0) {
                    returns = getApplicationContext().getString(R.string.no_new_history);
                }
            }
        else {
            returns=getApplicationContext().getString(R.string.no_history);
            }



            mCur.close();

 /* DEBUGGING --------------------------------------------------------------------------------------------------
//adding parsing here

            String result = "";
            StringBuilder sb = new StringBuilder();
            sb.append("Your last visit was: ").append(title).append("\n").append("\n").append("The terms with more than 70% relevance were: ").append("\n");


/* YAHOO TERM TRY BLOCK            try {
                JSONObject jo = new JSONObject(text);
                JSONArray ja;
                JSONObject query = jo.getJSONObject(TAG_QUERY);
                JSONObject results = query.getJSONObject(TAG_RESULTS);
                JSONObject entities = results.getJSONObject(TAG_ENTITIES);
                ja = entities.getJSONArray(TAG_ENTITY);

                boolean appendSeparator = false;
             for (int i = 0; i < ja.length(); i++)
           {
                    JSONObject resultObject = ja.getJSONObject(i);
                    JSONObject textObj = resultObject.getJSONObject(TAG_TEXT);
                    String name = textObj.getString(TAG_CONTENT);

                    if (appendSeparator)
                        sb.append("\n");

                    appendSeparator = true;
                    sb.append(name);

             }
//prs2                 result=sb.toString();

            }
            catch ( JSONException e2){
                System.err.println("JSONException " + e2.getMessage());
            }
YAHOO TERM
            try {
                JSONObject json = new JSONObject(text);
                //add next line in parsing for new combined jsonobject and changed next line to reflect the change
                String textres = json.getString("textres");
                JSONArray ja;
                ja = textres.getJSONArray(TAG_KEYWORDS);
                boolean appendSeparator = false;
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject resultObject = ja.getJSONObject(i);
                    String name = resultObject.getString(TAG_TEXT);
                    String comparison = resultObject.getString(TAG_RELEVANCE);
                    Float foo = Float.parseFloat(comparison.trim());

                    if (foo > 0.7) {
                        if (appendSeparator)
                            sb.append("\n");
                        appendSeparator = true;
                        sb.append(name);
                    }
                }
            } catch (JSONException e2) {
                System.err.println("JSONException " + e2.getMessage());
            }

            sb.append("\n").append("\n").append("The sentiment for this url is: ");
            try {
                JSONObject json = new JSONObject(sentiment);
                //add next line in parsing for new combined jsonobject and changed next line to reflect the change
                JSONObject sentimentres = json.getJSONObject("sentimentres");
                JSONObject docSentiment = sentimentres.getJSONObject(TAG_DOCSENTIMENT);
                String type = docSentiment.getString(TAG_TYPE);
                sb.append(type);
            } catch (JSONException e3) {
                System.err.println("JSONException " + e3.getMessage());
            }


            sb.append("\n").append("\n").append("The url belongs into the category of: ");
            try {
                JSONObject json = new JSONObject(categories);
                //add next line in parsing for new combined jsonobject and changed next line to reflect the change
                String categoryres = json.getString("categoryres");
                String categ = categoryres.getString(TAG_CATEGORY);
                sb.append(categ);
            } catch (JSONException e4) {
                System.err.println("JSONException " + e4.getMessage());
            }


            result = sb.toString();

            DEBUGGING -----------------------------------------------------------------------------------------
            */

/*version2 not needed

            if (returns!=getApplicationContext().getString(R.string.no_history) && returns!=getApplicationContext().getString(R.string.no_new_history))  {

                StringBuilder sb = new StringBuilder();
                String titlef = "";
                String textf="";

                try {
                    JSONObject json = new JSONObject(returns);
                    //add next line in parsing for new combined jsonobject and changed next line to reflect the change
                    JSONObject titleres = json.getJSONObject("titleres");
                    titlef = titleres.getString("title");

                } catch (JSONException e8) {
                    System.err.println("JSONException " + e8.getMessage());
                }


                sb.append(getApplicationContext().getString(R.string.sb_1)).append(titlef);

                sb.append("\n").append("\n").append(getApplicationContext().getString(R.string.sb_3));
                try {
                    JSONObject json = new JSONObject(returns);
                    //add next line in parsing for new combined jsonobject and changed next line to reflect the change
                    JSONObject sentimentres = json.getJSONObject("sentimentres");
                    //       JSONObject docSentiment = sentimentres.getJSONObject(TAG_DOCSENTIMENT);
                    String type = sentimentres.getString("sentiment");
                    sb.append(type);
                } catch (JSONException e3) {
                    System.err.println("JSONException " + e3.getMessage());
                }


                sb.append("\n").append("\n").append(getApplicationContext().getString(R.string.sb_4));
                try {
                    JSONObject json = new JSONObject(returns);
                    //add next line in parsing for new combined jsonobject and changed next line to reflect the change
                    JSONObject categoryres = json.getJSONObject("categoryres");
                    String categ = categoryres.getString(TAG_CATEGORY);
                    sb.append(categ);
                } catch (JSONException e4) {
                    System.err.println("JSONException " + e4.getMessage());
                }

                sb.append("\n").append("\n").append(getApplicationContext().getString(R.string.sb_2)).append("\n");
                try {
                    JSONObject json = new JSONObject(returns);
                    JSONObject textres = json.getJSONObject("textres");
                    textf = textres.getString("text");
                    sb.append(textf);

                } catch (JSONException e2) {
                    System.err.println("JSONException " + e2.getMessage());
                }

                returns = sb.toString();
            }
            return returns;

*/
            if (returns!=getApplicationContext().getString(R.string.no_history) && returns!=getApplicationContext().getString(R.string.no_new_history))  {
                returns = String.valueOf(control);
            }
            return returns;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MyActivity.this, getApplicationContext().getString(R.string.pdone), getApplicationContext().getString(R.string.pdtwo));
 //           Toast.makeText(getApplicationContext(),"Uploading data to database...",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate (Integer... progress) {
            super.onProgressUpdate(progress);

//            ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);
//            pb.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if (s ==getApplicationContext().getString(R.string.no_history)) {

                Toast.makeText(getApplicationContext(), R.string.no_history, Toast.LENGTH_SHORT).show();
            }
            else {
                if (s == getApplicationContext().getString(R.string.no_new_history)) {

                    Toast.makeText(getApplicationContext(), R.string.no_new_history, Toast.LENGTH_SHORT).show();
                } else {
                     try {
                         int ints =Integer.parseInt(s);
                         Toast.makeText(getApplicationContext(), ints + getApplicationContext().getResources().getString(R.string.upload_complete), Toast.LENGTH_SHORT).show();
                     }
                     catch (NumberFormatException e19){
                         Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                     }



                }
            }
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//            Intent intent = new Intent(MyActivity.this, DisplayMessageActivity.class);
//            intent.putExtra(EXTRA_MESSAGE, message);
            Button b = (Button)findViewById(R.id.b1);
            b.setClickable(true);
//            startActivity(intent);


        }
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

            JSONArray ja=null;
            try {
                ja = new JSONArray(text);
            }
            catch (JSONException e){
                System.err.println("JSONException " + e.getMessage());
            }

            if (ja!=null && ja.length()>0)  {
                return text;
            }
            else {
                return getApplicationContext().getResources().getString(R.string.no_data_fail);
            }


        }



/*version2 not needed
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
*/


        protected void onPostExecute(String message) {

            if (message.equals(getApplicationContext().getResources().getString(R.string.no_data_fail)))    {
                DialogFragment alert = new NoDataStored();
                alert.show(getSupportFragmentManager(), "alert");
            }

            else {
                Intent effortIntent = new Intent(MyActivity.this, DashboardActivity.class);
                effortIntent.putExtra(EXTRA_MESSAGE, message);
                startActivity(effortIntent);
            }
        }
    }


}

