package com.example.ylois.dummyapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Browser;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class MyActivity extends ActionBarActivity implements OnClickListener{
// implements OnClickListener removed from public class MyActivity
    public final static String EXTRA_MESSAGE = "com.example.ylois.dummyapp.MESSAGE";
    public final static String b1 = "b1";
    public final static String b2 = "b2";
    public final static String b3 = "b3";


     /*      JSON Node names YAHOO
    private static final String TAG_ENTITY ="entity";
    private static final String TAG_CONTENT="content";
    private static final String TAG_TEXT="text";
    private static final String TAG_QUERY="query";
    private static final String TAG_RESULTS="results";
    private static final String TAG_ENTITIES="entities";
    //private static final String TAG_WIKI_URL="wiki_url";
    */
    private static final String TAG_DOCSENTIMENT="docSentiment";
    private static final String TAG_TYPE="type";
    //json node names for category
    private static final String TAG_CATEGORY="category";
    //json node names for alchemy term
    private static final String TAG_KEYWORDS="keywords";
    private static final String TAG_TEXT="text";
    private static final String TAG_RELEVANCE="relevance";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        Button b1 = (Button) findViewById(R.id.b1);
        Button b2 = (Button) findViewById(R.id.b2);
        Button b3 = (Button) findViewById(R.id.b3);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
//        findViewById(R.id.my_button).setOnClickListener(this);
 //       findViewById(R.id.sentiment_analysis).setOnClickListener(this);
  //      findViewById(R.id.category_analysis).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b1:
                new LongRunningGetIO().execute();
                break;
            case R.id.b2:

                break;
            case R.id.b3:
                break;

    }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
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

    /**
     * Called when the user clicks the button
     */
    private class LongRunningGetIO extends AsyncTask<Void, Void, String> {

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


            //xml  https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20contentanalysis.analyze%20where%20url%3D'http%3A%2F%2Fwww.cnn.com%2F2011%2F11%2F11%2Fworld%2Feurope%2Fgreece-main%2Findex.html'%3B&diagnostics=true

            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            String url = "";
/*
            Long start=System.currentTimeMillis()-604800000; //last week in milliseconds
            Long end=System.currentTimeMillis();
            String startdate=Long.toString(start);
            String enddate=Long.toString(end);
*/
            String[] proj = new String[]{Browser.BookmarkColumns.TITLE, Browser.BookmarkColumns.URL};
            Uri uriCustom = Uri.parse("content://com.android.chrome.browser/bookmarks");
            String sel = Browser.BookmarkColumns.BOOKMARK + " = 0" ; // 0 = history, 1 = bookmark , + " AND " + Browser.BookmarkColumns.DATE + "BETWEEN ? AND ?"
            ContentResolver cr = getContentResolver();
            Cursor mCur = cr.query(uriCustom, proj, sel, null, null); // new String[]{startdate, enddate}
            mCur.moveToFirst();
            String title = "";
            String message = "";
            String returns = "";
            JSONObject combined = null;
 //           String replaced="";

            //allagmeno se moveToLast gia to pio prosfato history item, sto while ean usaristei thelei beforeFirst

            if (mCur.moveToFirst() && mCur.getCount() > 0)

            {
                while (!mCur.isAfterLast()) {
                    title = mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.TITLE));
                    message = mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.URL));
                    Browser.deleteFromHistory(cr, mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.URL)));
//                cr.delete(uriCustom, mCur.getString(mCur.getPosition())  ,null);



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
                        //http://access.alchemyapi.com/calls/url/URLGetRankedKeywords
                        String termAlchemy = "http://access.alchemyapi.com/calls/url/URLGetRankedKeywords?apikey=7af70ab4d132580daebfd7d69d35873cb6860fc1&url=" + url + "&outputMode=json&keywordExtractMode=strict";
                        HttpGet httpGet = new HttpGet(termAlchemy);
                        String text = "";
                        try {

                            HttpResponse response = httpClient.execute(httpGet, localContext);

                            HttpEntity entity = response.getEntity();

                            text = getASCIIContentFromEntity(entity);

                        } catch (Exception e) {
                            return e.getLocalizedMessage();
                        }

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
 //1                       StringBuilder sbt = new StringBuilder();
 //1                       sbt.append("{title:'");
 /*        char[] arr=title.toCharArray();
                    for (int i=0; i<arr.length; i++)
                    {
                        if (arr[i]=='\'')
                            arr[i]=' ';
                    }
                    title=arr.toString();
  */
  //1                      title = title.trim().replace("'","").replace('"',' ');
    //1                    sbt.append(title);
       //1                 sbt.append("'}");
          //1              title = sbt.toString();
                        if (title!=null && !title.isEmpty()) {
                            title = title.trim().replace('\'', ' ').replace('"', ' ');
                            try {
                                title = new JSONObject().put("title", title).toString();
                            } catch (JSONException e1) {
                                System.err.println("JSONException " + e1.getMessage());
                            }
                        }
                       else {
                            title = "Browser did not have enough time to save title in history";
                            try {
                                title = new JSONObject().put("title", title).toString();
                            } catch (JSONException e1) {
                                System.err.println("JSONException " + e1.getMessage());
                            }
                        }
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

                            text = sb.toString();
                        } catch (JSONException e6) {
                            System.err.println("JSONException " + e6.getMessage());
                        }
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
//merge the json objects to one

                        try {
                            JSONObject obj1 = new JSONObject(text);
                            JSONObject obj2 = new JSONObject(sentiment);
                            JSONObject obj3 = new JSONObject(categories);
                            JSONObject obj4 = new JSONObject(title);
                            combined = new JSONObject();
                            combined.put("textres", obj1);
                            combined.put("sentimentres", obj2);
                            combined.put("categoryres", obj3);
                            combined.put("titleres", obj4);
                        } catch (Exception e) {
                            return e.getLocalizedMessage();
                        }


//upload json obj to mongolab -> executeHttpPut
                        String myuri = "https://api.mongolab.com/api/1/databases/dummydb/collections/myself?apiKey=sWm3hnnxTlUTHiT2r45aaqQkFltSauc6";
                        //unused string just for debugging reasons (returns the document from the db including ID)

                        try {
                            returns = executeHttpPost(myuri, combined);
                        } catch (Exception e9) {
                            return e9.getLocalizedMessage();
                        }
                        mCur.moveToNext();


                    }
                }
        else {
            returns="There is no browser history.";
            }

                // When HttpClient instance is no longer needed,
                // shut down the connection manager to ensure
                // immediate deallocation of all system resources
                httpClient.getConnectionManager().shutdown();

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
 //debug 2           return result;

            if (returns!="There is no browser history." ) {

                StringBuilder sb = new StringBuilder();
                String titlef = "";
                String textf = "";
                try {
                    JSONObject json = new JSONObject(returns);
                    //add next line in parsing for new combined jsonobject and changed next line to reflect the change
                    JSONObject titleres = json.getJSONObject("titleres");
                    titlef = titleres.getString("title");

                } catch (JSONException e8) {
                    System.err.println("JSONException " + e8.getMessage());
                }


                sb.append("Your last visit was: ").append(titlef).append("\n").append("\n").append("The terms with more than 70% relevance were: ").append("\n");
                try {
                    JSONObject json = new JSONObject(returns);
                    JSONObject textres = json.getJSONObject("textres");
                    textf = textres.getString("text");
                    sb.append(textf);

                } catch (JSONException e2) {
                    System.err.println("JSONException " + e2.getMessage());
                }

                sb.append("\n").append("\n").append("The sentiment for this url is: ");
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


                sb.append("\n").append("\n").append("The url belongs into the category of: ");
                try {
                    JSONObject json = new JSONObject(returns);
                    //add next line in parsing for new combined jsonobject and changed next line to reflect the change
                    JSONObject categoryres = json.getJSONObject("categoryres");
                    String categ = categoryres.getString(TAG_CATEGORY);
                    sb.append(categ);
                } catch (JSONException e4) {
                    System.err.println("JSONException " + e4.getMessage());
                }
                returns = sb.toString();
            }
            return returns;



        }

        protected void onPostExecute(String message) {
            Intent intent = new Intent(MyActivity.this, DisplayMessageActivity.class);
            intent.putExtra(EXTRA_MESSAGE, message);
            Button b = (Button)findViewById(R.id.b1);
            b.setClickable(true);
            startActivity(intent);


        }
    }

}

