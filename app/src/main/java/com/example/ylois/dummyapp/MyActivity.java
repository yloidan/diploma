package com.example.ylois.dummyapp;

import android.content.Intent;
import android.net.Uri;
import android.provider.Browser;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.database.Cursor;
import android.widget.TextView;


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
        String[] proj = new String[] { Browser.BookmarkColumns.TITLE,Browser.BookmarkColumns.URL };
        Uri uriCustom = Uri.parse("content://com.android.chrome.browser/bookmarks");
        String sel = Browser.BookmarkColumns.BOOKMARK + " = 0"; // 0 = history, 1 = bookmark
        Cursor mCur = getContentResolver().query(uriCustom, proj, sel, null, null);
        mCur.moveToFirst();
        @SuppressWarnings("unused")
        String title = "";
        @SuppressWarnings("unused")
        String message = "";

        if (mCur.moveToFirst() && mCur.getCount() > 0) {
            boolean cont = true;
            while (mCur.isAfterLast() == false && cont) {
                title = mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.TITLE));
                message = mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.URL));
                // Do something with title and url
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);


//axristo gia mena                mCur.moveToNext();
            }
        }



/* se alli thesi pio panw
       intent.putExtra(EXTRA_MESSAGE, message);
       startActivity(intent);
*/


    }

}
