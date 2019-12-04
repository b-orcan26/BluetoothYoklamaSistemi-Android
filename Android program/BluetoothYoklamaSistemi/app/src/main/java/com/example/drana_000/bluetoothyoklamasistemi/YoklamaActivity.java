package com.example.drana_000.bluetoothyoklamasistemi;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class YoklamaActivity extends AppCompatActivity {

    final String site2 = "http://bbrsdeneme.c1.biz/ogrenciAl.php";

    List<Ogrenciler> ogrenciler = new ArrayList<Ogrenciler>();
    String ogrenciNo;
    EditText et;
    Button btnYoklamaBaslat, btnYoklamaKapat;
    TextView tv;
    BluetoothAdapter mBluetoothAdapter;
    MenuItem itemBluetooth;
    IntentFilter filter;
    ListeAdapter adaptor;
    ListView liste;
    public static Boolean manuelYoklamaKontrol=false;
    protected static final int MESSAGE_READ = 1;
    private static UUID MY_UUID;
    String TAG="debugging";
    String NAME="asdsa";
    ArrayList<BluetoothDevice> yoklamasiAlinanlar;
    AcceptThread acceptThread;
    private String site="http://bbrsdeneme.c1.biz/yoklamakaydet.php";

    BroadcastReceiver br = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {

                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                    if (state == BluetoothAdapter.STATE_ON) {
                        itemBluetooth.setIcon(R.drawable.bluetooth_acik);
                    } else if (state == BluetoothAdapter.STATE_OFF) {
                        itemBluetooth.setIcon(R.drawable.bluetooth_kapali);
                    }
                    break;
            }
        }
    };

    Handler mHandler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what)
            {
                case MESSAGE_READ:
                   
                    byte[] readbuf=(byte[])msg.obj;
                    String ss=new String (readbuf);

                    Toast.makeText(YoklamaActivity.this,ss,Toast.LENGTH_SHORT).show();

                    break;

            }
        }
    };

    @Override
    protected void onStop()
    {
        unregisterReceiver(br);
        super.onStop();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yoklama);

        init();

        filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(br, filter);

        JSONObject json = JsonObjeOlustur();
        String response = Post.gonder(site2, json.toString());
        JSONArray jsonArray = jsonArrayAta(response);

        int ogrenciSayisi = Integer.parseInt(String.valueOf(jsonArray.length()));

        for (int i = 0; i < ogrenciSayisi; i++) {
            try {
                JSONObject jsonObje = jsonArray.getJSONObject(i);
                ogrenciNo = jsonObje.getString("ogrNo");
                ogrenciler.add(new Ogrenciler(ogrenciNo));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        liste = (ListView) findViewById(R.id.liste);
        adaptor = new ListeAdapter(this, ogrenciler);
        liste.setAdapter(adaptor);

        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(manuelYoklamaKontrol==true)
                {
                    adaptor.getItem(i).setDerseKatilim(true);

                    Toast.makeText(YoklamaActivity.this,String.valueOf(ogrenciler.get(i).getDerseKatilim()),Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnYoklamaBaslat_Click();
        btnYoklamaKapat_Click();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);

        itemBluetooth = menu.getItem(0);
        if (BluetoothAdapter.getDefaultAdapter().getState() == BluetoothAdapter.STATE_ON) {
            itemBluetooth.setIcon(R.drawable.bluetooth_acik);
        } else {
            itemBluetooth.setIcon(R.drawable.bluetooth_kapali);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bluetoothAc:
                if (mBluetoothAdapter == null) {
                    Toast.makeText(YoklamaActivity.this, "Blueetoth Aygıtı Bulunamadı", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (!mBluetoothAdapter.isEnabled()) {
                    Intent bluetoothBaslat = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(bluetoothBaslat, 1);
                } else if (mBluetoothAdapter.isEnabled()) {
                    mBluetoothAdapter.disable();
                }

                return true;

            case R.id.manuelYoklama:

                //clickable özelliğini etkinleştir
                if (item.isChecked() == true) {
                    manuelYoklamaKontrol=false;
                    liste.setClickable(false);
                    item.setIcon(R.drawable.manuel_kapali);
                    item.setChecked(false);
                } else {

                    manuelYoklamaKontrol=true;
                    liste.setClickable(true);
                    item.setIcon(R.drawable.manuel_acik);
                    item.setChecked(true);
                }
                //item.setIcon(R.drawable.manuel_acik);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void init() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        yoklamasiAlinanlar=new ArrayList<BluetoothDevice>();
        btnYoklamaBaslat = (Button) findViewById(R.id.btnYoklamaBaslat);
        btnYoklamaKapat = (Button) findViewById(R.id.btnYoklamaKapat);
        et = (EditText) findViewById(R.id.editText2);
        tv = (TextView) findViewById(R.id.textViewTime);
    }

    private void btnYoklamaBaslat_Click() {
        btnYoklamaBaslat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mBluetoothAdapter.isEnabled())
                {
                    Toast.makeText(YoklamaActivity.this, "Önce bluetooth'u açınız", Toast.LENGTH_SHORT).show();
                }
                else
                    {
                    if (et.getText().toString() == "" || et.getText().length()==0)
                        Toast.makeText(YoklamaActivity.this, "Şifre boş bırakılamaz", Toast.LENGTH_SHORT).show();
                    else {
                        //Acceptthread vs.oluştur...
                        String myUuid;
                        myUuid = et.getText().toString();
                        MY_UUID = UUID.nameUUIDFromBytes(myUuid.getBytes());

                        acceptThread = new AcceptThread();
                        acceptThread.start();
                        et.setVisibility(View.INVISIBLE);
                        btnYoklamaBaslat.setVisibility(View.INVISIBLE);
                        btnYoklamaKapat.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void btnYoklamaKapat_Click() {
        btnYoklamaKapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptThread.cancel();
                JSONArray jar=JsonArrayOlustur();
                String response=Post.gonder(site, jar.toString());
                //response=responseDuzenle(response);
                Toast.makeText(YoklamaActivity.this,response,Toast.LENGTH_SHORT).show();

            }
        });
    }

    private JSONObject JsonObjeOlustur() {
        JSONObject json = new JSONObject();

        try {
            json.put("dersid", Ogretmen.getInstance().getDersID());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    private JSONArray jsonArrayAta(String response) {
        JSONArray jsonArray = null;

        int index = response.indexOf("[");
        response = response.substring(index, response.length());

        try {
            jsonArray = new JSONArray(new JSONTokener(response));
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return jsonArray;

    }

    private int getIndex(List<Ogrenciler> ogrencilers, Ogrenciler ogrenci) {
        for (int i = 0; i < ogrencilers.size(); i++) {
            if (ogrencilers.get(i).getOgrenciNo().equals(ogrenci.getOgrenciNo())) {

                return i;
            }
        }
        return -1;
    }

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            // Use a temporary object that is later assigned to mmServerSocket
            // because mmServerSocket is final.
            BluetoothServerSocket tmp = null;
            try {

                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
                Log.e(TAG,"Soket çalışıyor");
            } catch (IOException e) {
                Log.e(TAG, "Socket's listen() method failed", e);
            }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned.
            while (true) {
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    Log.e(TAG, "Socket's accept() method failed", e);
                    break;
                }

                if (socket != null) {

                    BluetoothDevice device=socket.getRemoteDevice();
                    //yoklamasiAlinanların içinde bağlantı isteği yapan device varsa socket'i null'ladık.
                    if(yoklamasiAlinanlar.size()==0)
                    {
                        ConnectedThread connectedThread=new ConnectedThread(socket);
                        connectedThread.start();
                        String mesaj="Baglandi";
                        connectedThread.write(mesaj.getBytes());
                    }
                    else if(yoklamasiAlinanlar.contains(device)==true)
                    {
                        socket=null;
                    }
                    //bağlanmak isteyen cihaz listede değilse connectedThread oluşturup başlatıyoruz.
                    else
                    {
                        ConnectedThread connectedThread=new ConnectedThread(socket);
                        connectedThread.start();
                        String mesaj="Baglandi";
                        connectedThread.write(mesaj.getBytes());
                    }
                    //Önceden burada bulunan kod mmServerSocketi kapatıp birden fazla cihazın bağlantı kurmasını engelliyordu biz bu
                    //kodu kaldırdık ki birden çok öğrenci bağlanıp yoklama verebilsin.
                }
            }
        }

        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private byte[] mmBuffer; // mmBuffer store for the stream


        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            // Get the input and output streams; using temp objects because
            // member streams are final.
            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                Log.e(TAG, "İnput stream hatası", e);
            }
            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Output stream hatası", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            mmBuffer = new byte[3];
            int numBytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                try {
                    // Read from the InputStream.
                    numBytes = mmInStream.read(mmBuffer);

                    // Send the obtained bytes to the UI activity.

                    Message readMsg = mHandler.obtainMessage(
                            MESSAGE_READ, numBytes, -1,
                            mmBuffer);


                    String ss=new String(mmBuffer);
                    int numara = Integer.parseInt(ss);

                    for(int i=0 ; i<ogrenciler.size(); i++)
                    {
                        if(ogrenciler.get(i).getOgrenciNo().equals(ss))
                        {
                            if(ogrenciler.get(i).getDerseKatilim()==true)
                            {
                                String mesaj="Bu numaranin yoklamasi daha önce alindi.";
                                write(mesaj.getBytes());
                                Log.e("alinmis yoklama:",ss);
                                break;
                            }
                            else
                            {
                                ogrenciler.get(i).setDerseKatilim(true);
                                String mesaj="Yoklama alindi :"+ss;
                                write(mesaj.getBytes());
                                yoklamasiAlinanlar.add(mmSocket.getRemoteDevice());
                                mmSocket.close();
                                Log.e("yoklama alindi :",ss);
                                break;
                            }
                        }
                        else
                        {
                            if(i==ogrenciler.size()-1)
                            {
                                String mesaj="Bu numara yoklama listesine kayitli değil :"+ss;
                                write(mesaj.getBytes());
                                Log.e("listede yok :",ss);
                            }
                        }
                    }

                    Log.e(TAG,ss);
                    numBytes=0;
                    mmBuffer = new byte[3];
                    readMsg.sendToTarget();
                } catch (IOException e) {
                    Log.d(TAG, "Input stream was disconnected", e);
                    break;
                }
            }
        }



        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            }
            catch (IOException e) {  Log.e(TAG,"yazim hatasi");
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }

    private JSONArray JsonArrayOlustur()
    {
        JSONArray jsonArray=new JSONArray();
        String dersid=Ogretmen.getInstance().getDersID();

        JSONObject json = new JSONObject();
        JSONObject json1 = new JSONObject();

        try {
            json.put("dersid", dersid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsonArray.put(json);

        try {
            json1.put("kullaniciadi", Ogretmen.getInstance().getKullaniciadi());
            json1.put("sifre",Ogretmen.getInstance().getSifre());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsonArray.put(json1);

        for(int i=0 ; i<ogrenciler.size() ; i++)
        {
            JSONObject jsonObject1=new JSONObject();
            if(ogrenciler.get(i).getDerseKatilim()==true)
            {

                try {
                    jsonObject1.put("ogrNo",ogrenciler.get(i).getOgrenciNo());
                    jsonObject1.put("katilim","var");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else
            {
                try {
                    jsonObject1.put("ogrNo",ogrenciler.get(i).getOgrenciNo());
                    jsonObject1.put("katilim","yok");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            jsonArray.put(jsonObject1);
        }


        return jsonArray;
    }

    private String responseDuzenle(String response)
    {
       if(response.contains("yoklama kaydedildi"))
       {
           return "Yoklama kaydedildi";
       }
       else
           return "Yoklama kaydedilemedi";

    }

}
