package com.example.drana_000.bluetoothyoklamasistemi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;



public class OgretmenKontrolActivity extends AppCompatActivity {

    TextInputEditText etKullaniciAdi;
    TextInputEditText etSifre;
    Button btnGirisYap;
    ProgressBar progressBar;
    String kullaniciadi = "kullaniciadi";
    String sifre = "sifre";
    //private final String site="http://bluetoothyoklamasistemi.co.nf/ogretmenKontrol.php";
    private final String site="http://bbrsdeneme.c1.biz/ogretmenKontrol.php";
    CheckBox checkBoxHatirla;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean hatirla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ogretmen_kontrol);

        init();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        btnGirisYap_Click();

    }
    private void init()
    {
        etKullaniciAdi =  (TextInputEditText)findViewById(R.id.etKullaniciAdi);
        etSifre = (TextInputEditText)findViewById(R.id.etSifre);
        btnGirisYap = (Button) findViewById(R.id.btnGirisYap);
        checkBoxHatirla=(CheckBox)findViewById(R.id.checkBoxHatirla);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        hatirla = loginPreferences.getBoolean("hatirla", false);
        if (hatirla == true) {
            etKullaniciAdi.setText(loginPreferences.getString("username", ""));
            etSifre.setText(loginPreferences.getString("password", ""));
            checkBoxHatirla.setChecked(true);
        }
    }

    private void btnGirisYap_Click()
    {
        btnGirisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!internetKontrol())
                {
                    Toast.makeText(OgretmenKontrolActivity.this, "İnternet bağlantınızı kontrol edin", Toast.LENGTH_SHORT).show();
                }
                else {

                    v.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    etKullaniciAdi.setClickable(false);
                    etSifre.setClickable(false);
                    etKullaniciAdi.setFocusable(false);
                    etSifre.setFocusable(false);


                    JSONObject json = JsonObjeOlustur();

                    String response = Post.gonder(site, json.toString());

                    OgretmenIDAta(response);

                    if (Ogretmen.getInstance().getOgretmenID() != "null") {
                        Ogretmen.getInstance().setKullaniciadi(etKullaniciAdi.getText().toString());
                        Ogretmen.getInstance().setSifre(etSifre.getText().toString());
                        Log.e("response_password",Ogretmen.getInstance().getSifre().toString());
                        Log.e("response_user",Ogretmen.getInstance().getKullaniciadi().toString());
                        startActivity(new Intent(OgretmenKontrolActivity.this, OgretimKontrolActivity.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }

                    if ("null".equals(Ogretmen.getInstance().getOgretmenID())) {
                        Toast.makeText(OgretmenKontrolActivity.this, "Kullanıcı adı veya şifre hatalı", Toast.LENGTH_SHORT).show();
                        v.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    else {
                        if (checkBoxHatirla.isChecked())	{
                            loginPrefsEditor.putBoolean("hatirla", true);
                            loginPrefsEditor.putString("username", etKullaniciAdi.getText().toString());
                            loginPrefsEditor.putString("password", etSifre.getText().toString());
                            loginPrefsEditor.commit();
                        } else {
                            loginPrefsEditor.clear();
                            loginPrefsEditor.commit();
                        }
                        Toast.makeText(OgretmenKontrolActivity.this, "Giriş başarılı ", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }}}
        );
    }

    private void OgretmenIDAta(String response)
    {
        if(!response.equals("null"))
        {
            int index=response.indexOf("{");
            response=response.substring(index,response.length());

            try {
                JSONObject json = new JSONObject(new JSONTokener(response));
                Ogretmen.getInstance().setOgretmenid(json.getString("ogretmenid"));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private JSONObject JsonObjeOlustur()
    {
        JSONObject json = new JSONObject();

        try {
            json.put(kullaniciadi, etKullaniciAdi.getText());
            json.put(sifre, etSifre.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("json",json.toString());
        return json;
    }

    private boolean internetKontrol() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }



}
