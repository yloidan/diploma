package com.example.ylois.dummyapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Browser;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class MyActivity extends ActionBarActivity {

    public final static String EXTRA_MESSAGE = "com.example.ylois.dummyapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
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
    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        //axristo    EditText editText = (EditText) findViewById(R.id.edit_message);
        //axristo    String message = editText.getText().toString();
        //   Browser historyUrl = new Browser ();
     /*   final Uri BOOKMARKS_URI_CHROME = Uri.parse("content://com.android.chrome.browser/bookmarks");
        String pinakas = HISTORY_PROJECTION.getBookmarkColumns.URL(1);
    */
        String[] proj = new String[]{Browser.BookmarkColumns.TITLE, Browser.BookmarkColumns.URL};
        Uri uriCustom = Uri.parse("content://com.android.chrome.browser/bookmarks");
        String sel = Browser.BookmarkColumns.BOOKMARK + " = 0"; // 0 = history, 1 = bookmark
        Cursor mCur = getContentResolver().query(uriCustom, proj, sel, null, null);
        mCur.moveToFirst();
        // @SuppressWarnings("unused")
        String title = "";
        // @SuppressWarnings("unused")
        String message = "";


        //allagmeno se moveToLast gia to pio prosfato history item, sto while ean usaristei thelei beforeFirst

        if (mCur.moveToLast() && mCur.getCount() > 0) {
//axristo gia mena           while (!mCur.isAfterLast()) {
            title = mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.TITLE));
            message = mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.URL));
            // Do something with title and url
            message =doInBackground(message);

            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);


//axristo gia mena                mCur.moveToNext();
//axristo gia mena           }
        }






    }

            protected String getASCIIContentFromEntity (HttpEntity entity) throws IllegalStateException, IOException
        {

                InputStream in = entity.getContent();

                StringBuilder out = new StringBuilder();
                int n = 1;
                while (n>0) {
                    byte[] b = new byte[4096];

                    n =  in.read(b);

                    if (n>0) out.append(new String(b, 0, n));

                }
                return out.toString();
            }

            protected String doInBackground(String message) {

//xml  https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20contentanalysis.analyze%20where%20url%3D'http%3A%2F%2Fwww.cnn.com%2F2011%2F11%2F11%2Fworld%2Feurope%2Fgreece-main%2Findex.html'%3B&diagnostics=true
//json https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20contentanalysis.analyze%20where%20url%3D'http%3A%2F%2Fwww.cnn.com%2F2011%2F11%2F11%2Fworld%2Feurope%2Fgreece-main%2Findex.html'%3B&format=json&diagnostics=true&callback=

                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                String url = null;
                try {url =  URLEncoder.encode(message,"UTF-8"); }
                    catch ( UnsupportedEncodingException e1){
                        return e1.getLocalizedMessage();
                    }

                HttpGet httpGet = new HttpGet("https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20contentanalysis.analyze%20where%20url%3D'" +url+ "'%3B&format=json&diagnostics=true&callback=");
                String text = null;
                try {

                    HttpResponse response = httpClient.execute(httpGet, localContext);

                    HttpEntity entity = response.getEntity();

                    text = getASCIIContentFromEntity(entity);

                } catch (Exception e) {
                    return e.getLocalizedMessage();
                }
                    return text;

            }






}
