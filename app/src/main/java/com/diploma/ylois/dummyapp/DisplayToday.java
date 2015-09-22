package com.diploma.ylois.dummyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class DisplayToday extends ActionBarActivity {


    

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_today);
        TextView textView = (TextView) findViewById(R.id.my_txt_view);
        textView.setMovementMethod(new ScrollingMovementMethod());


        Intent intent = getIntent();
        StringBuilder sb = new StringBuilder("");
        String message="";


        message = intent.getStringExtra(DashboardActivityToday.EXTRA_MESSAGE);




        if (!message.equals(getApplicationContext().getResources().getString(R.string.fail_flag))) {
            String message2 [] = message.split("\n");

            sb.append(getApplicationContext().getResources().getString(R.string.sbc_4)).append("\n").append("\n");
            for (int i=0; i<message2.length; i++)
            {
                sb.append(i+1).append(".").append(" ").append(message2[i]).append("\n");
            }
        }
        else {
            sb.append(getApplicationContext().getResources().getString(R.string.sb_5));
        }


        textView.setText(sb.toString());

    }

    //OPENCLOUD remove text and replace with word cloud
/*
        String segments[]=message.split(getApplicationContext().getString(R.string.sb_2));
        String substring = segments[segments.length - 1];
//                (message.substring(message.lastIndexOf(getApplicationContext().getString(R.string.sb_2))+ 1));
        message =message.replace(substring,"");
        Cloud cloud = new Cloud();
        cloud.setMaxWeight(38.0); //max font size
        cloud.setTagCase(Cloud.Case.CAPITALIZATION);
        String [] items =substring.split(",");
        for (int i=0; i<items.length; i++) {
            // creation date is 4th parameter, current time by default, weights are 3rd parameter
            Tag tag = new Tag(items[i],"http://www.google.com/search?q="+items[i] );
            cloud.addTag(tag); // adds it to the cloud
        }
        ListView listView = (ListView) findViewById(R.id.list);
        List <Tag> list = cloud.tags();
        ArrayAdapter adapter = new ArrayAdapter <Tag>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
*/




/*version2 not needed since its only debugging activity




        int count = 0;

        Pattern p0 = Pattern.compile(getApplicationContext().getString(R.string.sb_3) + "negative");
        Matcher m0 = p0.matcher(message);
        if (m0.find()){
            count =1;
        }

        if(count==0) {
            Pattern p1 = Pattern.compile(getApplicationContext().getString(R.string.sb_3) + "positive");
            Matcher m1 = p1.matcher(message);
            if (m1.find()) {
                count = 2;
            }
        }

        if (count==0)
        {
            Pattern p2 = Pattern.compile(getApplicationContext().getString(R.string.sb_3) + "neutral");
            Matcher m2 = p2.matcher(message);
            if (m2.find()){
                count =3;
            }
        }

        switch (count){
            case 0:
                textView.setText(message);
                break;
            case 1:
                setColor(textView, message, "negative", Color.MAGENTA);
                break;
            case 2:
                setColor(textView, message, "positive", Color.BLUE);
                break;
            case 3:
                setColor(textView, message, "neutral", Color.RED);
                break;
            default:
                break;
        }

    }
    */

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