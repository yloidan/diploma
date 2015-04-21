package com.example.ylois.dummyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.TextView;



public class DisplayMessageActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Get the message from the intent

        Intent intent = getIntent();
        String message = intent.getStringExtra(MyActivity.EXTRA_MESSAGE);

        // Create the text view

        TextView textView = new TextView(this);
        textView.setTextSize(14);

        /* DEBUGGING

        // Construct a JSONObject from a source JSON text string.

        // A JSONObject is an unordered collection of name/value pairs. Its external

        // form is a string wrapped in curly braces with colons between the names

        // and values, and commas between the values and names.

        try {JSONObject jo = new JSONObject(message);
            JSONArray ja;
 //           jo = jo.getJSONObject("ResultSet");
            ja = jo.getJSONArray("entity");
            int resultCount = ja.length();
 //           String  myArray[]=new String[resultCount];
            StringBuilder sb = new StringBuilder();
            boolean appendSeparator = false;
            for (int i = 0; i < resultCount; i++)

            {
                JSONObject resultObject = ja.getJSONObject(i);
               String name = resultObject.getString("content");

                if (appendSeparator)
                    sb.append(','); // a comma
                appendSeparator = true;
                sb.append(name);


//               sb.append(ja.get(i));
//               myArray[i]=name;
//               TextView.append(myArray[i]);
//               TextView.append("\n");


            }

            textView.setText(sb.toString());


        }
        catch ( JSONException e2){
            System.err.println("JSONException " + e2.getMessage());
        }

DEBUGGING   */
      textView.setText(message);


        // A JSONArray is an ordered sequence of values. Its external form is a

        // string wrapped in square brackets with commas between the values.



// Get the JSONObject value associated with the search result key.



// Get the JSONArray value associated with the Result key


// Get the number of search results in this set



// Loop over each result and print the title, summary, and URL









        //setting textview as the root view of the activity’s layout
        setContentView(textView);

     //not needed
     //setContentView(R.layout.activity_display_message);
    }

/* not needed for this example
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_message, menu);
        return true;
    }
*/
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
