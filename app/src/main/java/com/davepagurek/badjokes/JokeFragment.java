package com.davepagurek.badjokes;

import android.app.Fragment;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by dave_000 on 2014-11-21.
 */
public class JokeFragment extends Fragment {

    public View main;
    public String q = "";
    public String a = "";
    public Integer color = 0;
    Integer id = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        main = inflater.inflate(R.layout.joke_fragment, container, false);
        TextView qText = (TextView) main.findViewById(R.id.qEdit);
        qText.setText(Html.fromHtml(q).toString());
        //qText.setTextColor(color);

        TextView aText = (TextView) main.findViewById(R.id.aEdit);
        aText.setText(Html.fromHtml(a).toString());

        LinearLayout card = (LinearLayout) main.findViewById(R.id.card);
        GradientDrawable bgShape = (GradientDrawable)card.getBackground();
        bgShape.setColor(color);


        float scale = main.getContext().getResources().getDisplayMetrics().density;
        Log.e("test", ""+MainActivity.DENSITY);
        //main.setCameraDistance(0.5f * scale);
        main.setCameraDistance(MainActivity.DENSITY * 5000f);

        MainActivity.last = id;

        //card.setBackgroundColor(color);
        //aText.setTextColor(color);
        // Inflate the layout for this fragment
        return main;
    }

    public void setQA(String _q, String _a, Integer _color, Integer _id) {
        q=_q;
        a=_a;
        color = _color;
        id = _id;
    }
}
