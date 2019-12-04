package com.example.drana_000.bluetoothyoklamasistemi;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;


public class OgretimKontrolActivity extends AppCompatActivity {

    Button btnSecimYap;
    RadioGroup radioGroup;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ogretim_kontrol);
        init();

        btnSecimYap.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {


                if(radioGroup.getCheckedRadioButtonId() == -1){
                    Toast.makeText(OgretimKontrolActivity.this,"Önce Seçim Yapınız",Toast.LENGTH_SHORT).show();

                }
                else{
                    int radio_btn_id=radioGroup.getCheckedRadioButtonId();

                    switch (radio_btn_id){
                        case R.id.radioButton: { Ogretmen.getInstance().setOgretimTürü("1"); break;}
                        case R.id.radioButton2: { Ogretmen.getInstance().setOgretimTürü("2"); break;}
                    }
                    startActivity(new Intent(OgretimKontrolActivity.this,DersKontrolActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });
    }

    private void init()
    {
       btnSecimYap=(Button)findViewById(R.id.btn_secim_yap_ogretim);
       radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
    }


}