package com.example.drana_000.bluetoothyoklamasistemi;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import static android.R.attr.filter;

public class OgrenciActivity extends AppCompatActivity {

    ConnectedThread connectedThread;
    ListView listView;
    EditText et,et1;
    TextView tv;
    BluetoothSocket bs;///son eklemeler
    BluetoothAdapter bluetoothAdapter;
    Button btn_Listele,btn_Gonder;
    Set<BluetoothDevice> eslesmisCihazlar;
    ArrayList<BluetoothDevice> eslesmisCihazlar2;
    private ArrayAdapter<String> BTArrayAdapter;
    MenuItem itemBluetooth1;
    IntentFilter filter;

    private UUID MY_UUID;
    String TAG="debugging";
    protected static final int BAGLANIYOR=0;
    protected static final int MESSAGE_READ = 1;
    protected static final int BAGLANAMADI=2;
    protected static final int BAGLANTIKOPTU=4;

    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {

                case BAGLANIYOR:
                    //connectedThread=new ConnectedThread((BluetoothSocket)msg.obj);
                    Toast.makeText(OgrenciActivity.this,"Bağlanıyor",Toast.LENGTH_SHORT).show();

                    break;
                case MESSAGE_READ:
                    byte[] readbuf=(byte[])msg.obj;
                    String ss=new String (readbuf);
                    if(ss.contains("Baglandi"))
                    {
                        et1.setVisibility(View.VISIBLE);
                        btn_Gonder.setVisibility(View.VISIBLE);
                    }
                    Toast.makeText(OgrenciActivity.this,ss,Toast.LENGTH_SHORT).show();
                    break;
                case BAGLANAMADI:
                    Toast.makeText(OgrenciActivity.this,"Bağlantı kurulamadı",Toast.LENGTH_SHORT).show();
                    break;
                case BAGLANTIKOPTU:
                    Toast.makeText(OgrenciActivity.this,"Bağlantı sonlandırıldı",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    BroadcastReceiver br = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {

                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                    if (state == BluetoothAdapter.STATE_ON) {
                        itemBluetooth1.setIcon(R.drawable.bluetooth_acik);
                    } else if (state == BluetoothAdapter.STATE_OFF) {
                        itemBluetooth1.setIcon(R.drawable.bluetooth_kapali);
                    }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu1, menu);
        itemBluetooth1 = menu.getItem(0);
        if (BluetoothAdapter.getDefaultAdapter().getState() == BluetoothAdapter.STATE_ON) {
            itemBluetooth1.setIcon(R.drawable.bluetooth_acik);
        } else {
            itemBluetooth1.setIcon(R.drawable.bluetooth_kapali);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bluetoothAc1:
                if (bluetoothAdapter == null) {
                    Toast.makeText(OgrenciActivity.this, "Blueetoth Aygıtı Bulunamadı", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (!bluetoothAdapter.isEnabled()) {
                    Intent bluetoothBaslat = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(bluetoothBaslat, 1);
                } else if (bluetoothAdapter.isEnabled()) {
                    bluetoothAdapter.disable();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_ogrenci);
        init();
        filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(br, filter);
        btnListele_Click();
        item_Selected();
        btnGonder_Click();

    }

    private void init()
    {
        et1=(EditText)findViewById(R.id.editText4);
        et1.setVisibility(View.INVISIBLE);
        et=(EditText)findViewById(R.id.editText3);
        tv=(TextView)findViewById(R.id.textView);
        btn_Listele=(Button)findViewById(R.id.button5);
        btn_Gonder=(Button)findViewById(R.id.button12);
        btn_Gonder.setVisibility(View.INVISIBLE);
        listView=(ListView)findViewById(R.id.listview1);
        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        BTArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(BTArrayAdapter);
    }


    private void btnGonder_Click()
    {
        btn_Gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int b=0;
                int a=Integer.parseInt(String.valueOf(et1.getText()));
                while(a>0)
                {
                    b++;
                    a=a/10;
                }
                if(b<3)
                {
                    Toast.makeText(OgrenciActivity.this,"Ogrenci numarasi 3 haneli olmalıdır",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String s=String.valueOf(et1.getText());
                    connectedThread.write(s.getBytes());
                }
            }
        });
    }


    private void btnListele_Click()
    {
        btn_Listele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eslesmisCihazlar=bluetoothAdapter.getBondedDevices();
                eslesmisCihazlar2=new ArrayList<BluetoothDevice>(eslesmisCihazlar); //Tip DÖNÜŞÜMÜ//

                BTArrayAdapter.clear();
                if(bluetoothAdapter.isEnabled())
                {
                    for (BluetoothDevice device : eslesmisCihazlar2) {
                        BTArrayAdapter.add(device.getName() + "" + device.getAddress().toString());
                    }
                }
                else
                    Toast.makeText(OgrenciActivity.this,"Önce Bluetooth'u açınız.",Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void item_Selected()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //   Bağlanma çabası  edittext teki dersin şifresi yani uuid alınıp baglanmaya calısıyor
                //eslesmisCihazlar2.get(position).getName()
                String myUuid=et.getText().toString();
                MY_UUID=UUID.nameUUIDFromBytes(myUuid.getBytes());
                //bluetoothAdapter.getBondedDevices().remove(eslesmisCihazlar2.get(position));
                ConnectThread connectThread=new ConnectThread(eslesmisCihazlar2.get(position));
                connectThread.start();
            }
        });
    }


    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            bluetoothAdapter.cancelDiscovery();

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                mHandler.obtainMessage(BAGLANAMADI).sendToTarget();
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e(TAG, "İstemci soketi kapatılamadı", closeException);
                }
                return;
            }


            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            //****************************************************************SON
            connectedThread=new ConnectedThread(mmSocket);
            connectedThread.start();
            //mHandler.obtainMessage(BAGLANIYOR,mmSocket).sendToTarget();
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {  }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer;  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {

                    if(!mmSocket.isConnected())
                    {
                        mHandler.obtainMessage(BAGLANTIKOPTU).sendToTarget();
                    }
                    // Read from the InputStream
                    buffer = new byte[1024];
                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI activity
                    mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                            .sendToTarget();

                } catch (IOException e) {
                    mHandler.obtainMessage(BAGLANTIKOPTU).sendToTarget();
                    Log.e(TAG,"bağlantı sonlandırıldı");
                    break;
                }
            }
        }


        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }
}