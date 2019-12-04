package com.example.drana_000.bluetoothyoklamasistemi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

public class MainActivity extends AppCompatActivity {

    Button btnOgretmenGiris,btnOgrenciGiris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView=(ImageView)findViewById(R.id.imageView);
        btnOgretmenGiris = (Button) findViewById(R.id.btnOgretmenGiris);
        btnOgrenciGiris = (Button) findViewById(R.id.btnOgrenciGiris);


        Float imageViewY=imageView.getPivotY();

        imageView.setAlpha(0f);
        imageView.setTranslationY(imageViewY-250);
        imageView.animate().alpha(1f).translationYBy(200).setDuration(1500).setInterpolator(new BounceInterpolator());






        btnOgrenciGiris.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {

                Drawable drawable=getResources().getDrawable(R.drawable.button_shape_click);
                v.setBackground(drawable);
                startActivity(new Intent(MainActivity.this,OgrenciActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        btnOgretmenGiris.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                Drawable drawable=getResources().getDrawable(R.drawable.button_shape_click);
                v.setBackground(drawable);
                startActivity(new Intent(MainActivity.this,OgretmenKontrolActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onStart() {
        super.onStart();

        Drawable drawable=getResources().getDrawable(R.drawable.button_shape);
        btnOgrenciGiris.setBackground(drawable);
        btnOgretmenGiris.setBackground(drawable);

    }
}

