package com.oritmalki.tunityhometest;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.TextureView;
import android.widget.TextView;

/**
 * Created by user2 on 21/03/2018.
 */

public class DetailsActivity extends AppCompatActivity {

    TextView rValueTv;
    TextView gValueTv;
    TextView bValueTv;
    TextView rColorTv;
    TextView gColorTv;
    TextView bColorTv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.detail_activity);

        rValueTv = findViewById(R.id.r_value);
        gValueTv = findViewById(R.id.g_value);
        bValueTv = findViewById(R.id.b_value);
        rColorTv = findViewById(R.id.r_color);
        gColorTv = findViewById(R.id.g_color);
        bColorTv = findViewById(R.id.b_color);

        int[] rgb = getIntent().getIntArrayExtra(MainActivity.MID_PIXEL_VALUES);

        rValueTv.setText(String.valueOf(rgb[0]));
        rColorTv.setBackgroundColor(Color.rgb(rgb[0],0,0));

        gValueTv.setText(String.valueOf(rgb[1]));
        gColorTv.setBackgroundColor(Color.rgb(0,rgb[1],0));

        bValueTv.setText(String.valueOf(rgb[2]));
        bColorTv.setBackgroundColor(Color.rgb(0,0,rgb[2]));




    }
}
