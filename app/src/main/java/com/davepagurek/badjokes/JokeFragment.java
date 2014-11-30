package com.davepagurek.badjokes;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by dave_000 on 2014-11-21.
 */
public class JokeFragment extends Fragment {

    public View main;
    public String q = "";
    public String a = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        main = inflater.inflate(R.layout.joke_fragment, container, false);
        TextView qText = (TextView) main.findViewById(R.id.q);
        qText.setText(Html.fromHtml(q).toString());

        TextView aText = (TextView) main.findViewById(R.id.a);
        aText.setText(Html.fromHtml(a).toString());
        // Inflate the layout for this fragment
        return main;
    }

    public void setQA(String _q, String _a) {
        q=_q;
        a=_a;
    }
}
