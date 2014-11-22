package com.davepagurek.badjokes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;


public class JokeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        //set the transition

        setContentView(R.layout.activity_joke);

        Intent intent = getIntent();
        String q = intent.getStringExtra(MainActivity.JOKE_Q);
        String a = intent.getStringExtra(MainActivity.JOKE_A);

        setJoke(q, a);

        Transition ts = new Explode();
        ts.setDuration(500);
        getWindow().setEnterTransition(ts);
        getWindow().setExitTransition(ts);


    }

    public void setJoke(String q, String a) {
        TextView qText = (TextView) findViewById(R.id.q);
        qText.setText(q);

        TextView aText = (TextView) findViewById(R.id.a);
        aText.setText(a);
    }

    public void randomJoke(View view) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(MainActivity.JOKE_RETURN_STATUS, MainActivity.RANDOM_JOKE);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_joke, menu);
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
