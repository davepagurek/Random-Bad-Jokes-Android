package com.davepagurek.badjokes;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
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
import android.content.res.Configuration;

import java.util.Random;
import java.util.ArrayList;


public class JokeActivity extends Activity {

    public static final Integer ADD_STATUS = 1;
    public static final Integer STATUS_SUCCESS = 2;

    public static Integer added_id = -1;
    public static String added_q = "";
    public static String added_a = "";

    public JokeFragment fragment;
    public ArrayList<Integer> colors = new ArrayList<Integer>();
    public View content;
    //public Integer MainActivity.LAST_BG = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        //set the transition

        setContentView(R.layout.activity_joke);

        String url = MainActivity.URL_GET_JOKE + "?last=" + MainActivity.last;

        content = getWindow().getDecorView().findViewById(android.R.id.content);

        Transition ts = new Explode();
        ts.setDuration(500);
        getWindow().setEnterTransition(ts);
        getWindow().setExitTransition(ts);

        getWindow().getDecorView().findViewById(android.R.id.content).setBackgroundColor(getResources().getColor(R.color.background));

        GetJSONTask request = new GetJSONTask();

        request.setCallbackInstance(this);

        colors.add(getResources().getColor(R.color.c1));
        colors.add(getResources().getColor(R.color.c2));
        colors.add(getResources().getColor(R.color.c3));
        colors.add(getResources().getColor(R.color.c4));
        colors.add(getResources().getColor(R.color.c5));
        colors.add(getResources().getColor(R.color.c6));
        colors.add(getResources().getColor(R.color.c7));
        colors.add(getResources().getColor(R.color.c8));
        colors.add(getResources().getColor(R.color.c9));

        MainActivity.DENSITY = getResources().getDisplayMetrics().density;


        if(savedInstanceState == null) {
            request.execute(url);
        } else {
            setJoke(MainActivity.last, MainActivity.CURRENT_Q, MainActivity.CURRENT_A, true);
        }

    }

    public void setJoke(int id, String q, String a, Boolean change) {
        MainActivity.last = id;
        /*FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations( R.animator.card_flip_right_in, R.animator.card_flip_right_out, R.animator.card_flip_right_in, R.animator.card_flip_right_out);
        fragment = new JokeFragment();
        fragment.setQA(q, a);
        fragmentTransaction.add(R.id.jokeContainer, fragment);
        fragmentTransaction.commit();*/

        Integer textColor = 0;
        if (!change) {
            do {
                textColor = (colors.get(new Random().nextInt(colors.size())));
            } while (textColor.equals(MainActivity.LAST_COLOR));
            MainActivity.LAST_COLOR = textColor;
        } else {
            textColor = MainActivity.LAST_COLOR;
        }


        fragment = new JokeFragment();
        fragment.setQA(q, a, textColor, id);

        if (change) {
            getFragmentManager()
                    .beginTransaction()

                    .replace(R.id.jokeContainer, fragment)

                            // Add this transaction to the back stack, allowing users to press Back
                            // to get to the front of the card.
                    .addToBackStack(null)

                            // Commit the transaction.
                    .commit();
        } else {
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

            MainActivity.CURRENT_Q = q;
            MainActivity.CURRENT_A = a;
        }



        ColorDrawable bgcolor = (ColorDrawable) content.getBackground();
        Integer colorFrom = bgcolor.getColor();
        Integer colorTo = getResources().getColor(R.color.background_light);

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

    public void flagResult(String result) {
        AlertDialog.Builder alert = new AlertDialog.Builder(content.getContext());
        if (result == null) {
            alert.setMessage("Sorry, we could not connect to the bad jokes database.\nPlease check your network connection and try again later.");
        } else {
            alert.setMessage(result);
        }
        alert.setPositiveButton("OK", null);
        alert.show();
    }

    public void connectionError() {
        AlertDialog.Builder alert = new AlertDialog.Builder(content.getContext());
        alert.setMessage("Sorry, we could not connect to the bad jokes database.\nPlease check your network connection and try again later.");
        alert.setPositiveButton("OK", null);
        alert.show();
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
        if (id == R.id.action_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, MainActivity.URL_VIEW_JOKE + "?joke=" + MainActivity.last);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

            return true;
        }

        if (id == R.id.action_flag) {
            final JokeActivity instance = this;
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            GetTextTask request = new GetTextTask();

                            request.setCallbackInstance(instance);
                            request.execute(MainActivity.URL_FLAG_JOKE + "?joke=" + MainActivity.last);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(content.getContext());
            builder.setMessage("Remember, flagging is intented only for things that aren't jokes, are offensive, or are spam.\nAre you sure you want to flag this?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }

        if (id == R.id.action_add) {
            AddActivity.callback_view = this;
            Intent intent = new Intent(this, AddActivity.class);
            startActivityForResult(intent, ADD_STATUS);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == ADD_STATUS) {

            if (JokeActivity.added_id != -1) {
                setJoke(JokeActivity.added_id, JokeActivity.added_q, JokeActivity.added_a, false);
                JokeActivity.added_id = -1;
                JokeActivity.added_q = "";
                JokeActivity.added_a = "";
            }
        }
    }
}

