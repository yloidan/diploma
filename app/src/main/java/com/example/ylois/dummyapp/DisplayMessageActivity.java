package com.example.ylois.dummyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class DisplayMessageActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_message);
        TextView textView = (TextView) findViewById(R.id.my_txt_view);


        Intent intent = getIntent();
        String message = intent.getStringExtra(MyActivity.EXTRA_MESSAGE);

        textView.setText(message);

    }

        /*
        String[] parts = message.split(" ");
        int[] VALUES = new int[parts.length];
        for (int n = 0; n < parts.length; n++) {
            VALUES[n] = Integer.parseInt(parts[n]);
        }
       StringBuilder sb= new StringBuilder();
        for (int i: VALUES){
            sb.append(Integer.toString(i));
            sb.append("\n");
        }
        textView.setText(sb.toString());            */



//textView.setText(result);



     //not needed
     //setContentView(R.layout.activity_display_message);


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_message, menu);
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
