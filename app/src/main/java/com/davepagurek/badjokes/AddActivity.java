package com.davepagurek.badjokes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class AddActivity extends Activity {
    public View main;
    public EditText qEdit;
    public EditText aEdit;
    public Button submit;

    public Integer qLength = 0;
    public Integer aLength = 0;

    public static JokeActivity callback_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        main = getWindow().getDecorView().findViewById(android.R.id.content);
        qEdit = (EditText) main.findViewById(R.id.qEdit);
        aEdit = (EditText) main.findViewById(R.id.aEdit);
        submit = (Button) main.findViewById(R.id.submit);

        qEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                qLength = s.length();
                submit.setEnabled(qLength>0 && aLength>0);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        aEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                aLength = s.length();
                submit.setEnabled(qLength>0 && aLength>0);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    public void cancel(View view) {
        finish();
    }

    public void submit(View view) {
        PostJSONTask request = new PostJSONTask();

        request.setCallbackInstance(this);
        request.execute(MainActivity.URL_ADD_JOKE, qEdit.getText().toString(), aEdit.getText().toString());

        qEdit.setEnabled(false);
        aEdit.setEnabled(false);
        submit.setEnabled(false);
    }

    public void success(Integer id, String q, String a) {
        JokeActivity.added_id = id;
        JokeActivity.added_q = q;
        JokeActivity.added_a = a;
        finish();
    }

    public void error(String status) {
        AlertDialog.Builder alert = new AlertDialog.Builder(main.getContext());

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            finish();
            }
        };

        if (status.equals("exceeded")) {
            alert.setMessage("Sorry, you have submitted too many jokes today.");
        } else if (status.equals("spam")) {
            alert.setMessage("Sorry, this looks like you are trying to post spam.");
        } else if (status.equals("identical")) {
            alert.setMessage("Sorry, an identical joke already exists in the database.");
        } else {
            alert.setMessage("Sorry, your joke was not able to be posted.");
        }
        alert.setPositiveButton("OK", dialogClickListener);
        alert.show();
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_add, menu);
        //return true;
    }*/

    /*
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
    }*/
}
