package com.example.drana_000.bluetoothyoklamasistemi;

import android.content.Intent;
import android.graphics.Color;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


public class DersKontrolActivity extends AppCompatActivity {
    //final String site1 = "http://192.168.1.100/dersAl.php";
    //private final String site1 = "http://bluetoothyoklamasistemi.co.nf/dersAl.php";
    private final String site1 ="http://bbrsdeneme.c1.biz/dersAl.php";


    Button btn_ders_sec;
    ListView listView;
    DersAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ders_kontrol);

        btn_ders_sec=(Button)findViewById(R.id.btn_secim_yap_ders);
        listView=(ListView)findViewById(R.id.listview1);
        JSONObject json=JsonObjeOlustur();
        String response = Post.gonder(site1,json.toString());
        DerslerAta(response);

        adapter=new DersAdapter(this,Ogretmen.getInstance().getDersler());
        listView.setAdapter(adapter);


        btn_ders_sec.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {


                if(adapter.selected == null){
                    Toast.makeText(DersKontrolActivity.this,"Önce Seçim Yapınız",
                            Toast.LENGTH_SHORT).show();

                }
                else {
                    int id = adapter.selected.getId();
                    Ogretmen.getInstance().setDersID(String.valueOf(id));
                    startActivity(new Intent(DersKontrolActivity.this, YoklamaActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }

            }
        });



    }


    private JSONObject JsonObjeOlustur()
    {
        JSONObject json = new JSONObject();

        try {
            json.put("ogretmenid",Ogretmen.getInstance().getOgretmenID());
            json.put("ogretimturu",Ogretmen.getInstance().getOgretimTürü());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }


    private void DerslerAta(String response)
    {
        if(!response.equals("null"))
        {
            int index=response.indexOf("[");
            response=response.substring(index,response.length());

            try {
                JSONArray jsonArray=new JSONArray(new JSONTokener(response));
                Ogretmen.getInstance().setDersler(jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}

