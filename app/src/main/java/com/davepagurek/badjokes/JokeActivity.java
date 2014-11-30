package com.davepagurek.badjokes;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.widget.TextView;

import java.util.Random;
import java.util.ArrayList;


public class JokeActivity extends Activity {

    public JokeFragment fragment;
    public ArrayList<Integer> colors = new ArrayList<Integer>();
    public View content;
    public Integer lastBg = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        colors.add(getResources().getColor(R.color.c1));
        colors.add(getResources().getColor(R.color.c2));
        colors.add(getResources().getColor(R.color.c3));
        colors.add(getResources().getColor(R.color.c4));
        colors.add(getResources().getColor(R.color.c5));
        colors.add(getResources().getColor(R.color.c6));
        colors.add(getResources().getColor(R.color.c7));
        colors.add(getResources().getColor(R.color.c8));
        colors.add(getResources().getColor(R.color.c9));

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        //set the transition

        String url = MainActivity.URL_GET_JOKE + "?last=" + MainActivity.last;

        setContentView(R.layout.activity_joke);
        getWindow().getDecorView().findViewById(android.R.id.content).setBackgroundColor(getResources().getColor(R.color.background));

        content = getWindow().getDecorView().findViewById(android.R.id.content);

        Transition ts = new Explode();
        ts.setDuration(500);
        getWindow().setEnterTransition(ts);
        getWindow().setExitTransition(ts);

        GetJSONTask request = new GetJSONTask();

        request.setCallbackInstance(this);
        request.execute(url);

    }

    public void setJoke(int id, String q, String a) {
        MainActivity.last = id;
        /*FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations( R.animator.card_flip_right_in, R.animator.card_flip_right_out, R.animator.card_flip_right_in, R.animator.card_flip_right_out);
        fragment = new JokeFragment();
        fragment.setQA(q, a);
        fragmentTransaction.add(R.id.jokeContainer, fragment);
        fragmentTransaction.commit();*/


        fragment = new JokeFragment();
        fragment.setQA(q, a);

        getFragmentManager()
                .beginTransaction()

                        // Replace the default fragment animations with animator resources representing
                        // rotations when switching to the back of the card, as well as animator
                        // resources representing rotations when flipping back to the front (e.g. when
                        // the system Back button is pressed).
                .setCustomAnimations(
                        R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in, R.animator.card_flip_left_out)

                        // Replace any fragments currently in the container view with a fragment
                        // representing the next page (indicated by the just-incremented currentPage
                        // variable).
                .replace(R.id.jokeContainer, fragment)

                        // Add this transaction to the back stack, allowing users to press Back
                        // to get to the front of the card.
                .addToBackStack(null)

                        // Commit the transaction.
                .commit();


        ColorDrawable bgcolor = (ColorDrawable) content.getBackground();
        Integer colorFrom = bgcolor.getColor();
        Integer colorTo = 0;
        do {
            colorTo = (colors.get(new Random().nextInt(colors.size())));
        } while (colorTo.equals(lastBg));
        lastBg = colorTo;

        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);

        colorAnimation.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                content.setBackgroundColor((Integer)animator.getAnimatedValue());
            }

        });
        colorAnimation.start();

    }

    public void randomJoke(View view) {
        /*FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations( R.animator.card_flip_right_in, R.animator.card_flip_right_out, R.animator.card_flip_right_in, R.animator.card_flip_right_out);
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();*/

        //content = getWindow().getDecorView().findViewById(android.R.id.content);
        ColorDrawable bgcolor = (ColorDrawable) content.getBackground();
        Integer colorFrom = bgcolor.getColor();
        Integer colorTo = getResources().getColor(R.color.background);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);

        colorAnimation.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                content.setBackgroundColor((Integer)animator.getAnimatedValue());
            }

        });
        colorAnimation.start();

        String url = MainActivity.URL_GET_JOKE + "?last=" + MainActivity.last;

        GetJSONTask request = new GetJSONTask();

        request.setCallbackInstance(this);
        request.execute(url);

        /*Intent resultIntent = new Intent();
        resultIntent.putExtra(MainActivity.JOKE_RETURN_STATUS, MainActivity.RANDOM_JOKE);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();*/
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
