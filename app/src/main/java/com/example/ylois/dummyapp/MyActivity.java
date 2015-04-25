package com.example.ylois.dummyapp;

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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class MyActivity extends ActionBarActivity implements OnClickListener {

    public final static String EXTRA_MESSAGE = "com.example.ylois.dummyapp.MESSAGE";


     //       JSON Node names
    private static final String TAG_ENTITY ="entity";
    private static final String TAG_CONTENT="content";
    private static final String TAG_TEXT="text";
    private static final String TAG_QUERY="query";
    private static final String TAG_RESULTS="results";
    private static final String TAG_ENTITIES="entities";
    //private static final String TAG_WIKI_URL="wiki_url";
    private static final String TAG_DOCSENTIMENT="docSentiment";
    private static final String TAG_TYPE="type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        findViewById(R.id.my_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Button b = (Button) findViewById(R.id.my_button);
        b.setClickable(false);
        new LongRunningGetIO().execute();
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
     * Called when the user clicks the Send button
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

        @Override
        protected String doInBackground(Void... params) {



            //xml  https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20contentanalysis.analyze%20where%20url%3D'http%3A%2F%2Fwww.cnn.com%2F2011%2F11%2F11%2Fworld%2Feurope%2Fgreece-main%2Findex.html'%3B&diagnostics=true

            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            String url = null;

            String[] proj = new String[]{Browser.BookmarkColumns.TITLE, Browser.BookmarkColumns.URL};
            Uri uriCustom = Uri.parse("content://com.android.chrome.browser/bookmarks");
            String sel = Browser.BookmarkColumns.BOOKMARK + " = 0"; // 0 = history, 1 = bookmark
            Cursor mCur = getContentResolver().query(uriCustom, proj, sel, null, null);
            mCur.moveToFirst();
            String title = "";
            String message = "";


            //allagmeno se moveToLast gia to pio prosfato history item, sto while ean usaristei thelei beforeFirst

            if (mCur.moveToLast() && mCur.getCount() > 0)

            {
//axristo gia twra           while (!mCur.isAfterLast()) {
                title = mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.TITLE));
                message = mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.URL));

//axristo gia twra                mCur.moveToNext();

            }
            mCur.close();
            try {
                url = URLEncoder.encode(message, "UTF-8");
            } catch (UnsupportedEncodingException e1) {
                return e1.getLocalizedMessage();
            }

            String resu = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20contentanalysis.analyze%20where%20url%3D%27" + url + "%27%3B&format=json&diagnostics=true&callback=";

            HttpGet httpGet = new HttpGet(resu);
            String text = null;
            try {

                HttpResponse response = httpClient.execute(httpGet, localContext);

                HttpEntity entity = response.getEntity();

                text = getASCIIContentFromEntity(entity);

            } catch (Exception e) {
                return e.getLocalizedMessage();
            }
            String alchemy="http://access.alchemyapi.com/calls/url/URLGetTextSentiment?apikey=7af70ab4d132580daebfd7d69d35873cb6860fc1&url=" +url +"&outputMode=json";
            HttpGet httpGet2 = new HttpGet(alchemy);
            String sentiment="";
            try {

                HttpResponse response = httpClient.execute(httpGet2, localContext);

                HttpEntity entity = response.getEntity();

                sentiment = getASCIIContentFromEntity(entity);

            } catch (Exception e) {
                return e.getLocalizedMessage();
            }finally {
                // When HttpClient instance is no longer needed,
                // shut down the connection manager to ensure
                // immediate deallocation of all system resources
                httpClient.getConnectionManager().shutdown();
            }
//adding parsing here



            //      DEBUGGING

            String result="";
            StringBuilder sb = new StringBuilder();
            sb.append("Your last visit was: ").append(title).append(", ").append("and the terms were: ").append("\n");


            try {
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

            sb.append("\n").append("\n").append("The sentiment for this url is: ");
            try {
              JSONObject json =new JSONObject(sentiment);
                JSONObject docSentiment=json.getJSONObject(TAG_DOCSENTIMENT);
                String type=docSentiment.getString(TAG_TYPE);
                sb.append(type);
            }
            catch ( JSONException e3){
                System.err.println("JSONException " + e3.getMessage());
            }

                result=sb.toString();
                return result;

//DEBUGGING return text;

            }

        protected void onPostExecute(String message) {
            Intent intent = new Intent(MyActivity.this, DisplayMessageActivity.class);
            intent.putExtra(EXTRA_MESSAGE, message);
            Button b = (Button)findViewById(R.id.my_button);
            b.setClickable(true);
            startActivity(intent);


        }
    }
}