package com.example.bluetooth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button turnon,paireddevices,ScanBluetoothDevices,MakeDeviceVisible;
    BluetoothAdapter bluetoothAdapter;
    ListView listView;
    ArrayList<D_ScannedDevicesInfo> arrayList;
    MyAdapter myAdapter;
    boolean isClicked=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      turnon=findViewById(R.id.turnon);
      paireddevices=findViewById(R.id.paireddevices);
      MakeDeviceVisible=findViewById(R.id.MakeDeviceDiscoverable);
      MakeDeviceVisible.setOnClickListener(this);
      bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
     ScanBluetoothDevices=findViewById(R.id.ScanBluetoothDevices);
      turnon.setOnClickListener(this);
      ScanBluetoothDevices.setOnClickListener(this);
      paireddevices.setOnClickListener(this);
      listView=findViewById(R.id.ListviewForScannedDevices);
      arrayList=new ArrayList<>();
      myAdapter=new MyAdapter(getApplicationContext(),R.layout.singleviewforscanneddevices,arrayList);
      listView.setAdapter(myAdapter);

    }
 BroadcastReceiver broadcastReceiverTocheckOffOrOn =new BroadcastReceiver() {
     @Override
     public void onReceive(Context context, Intent intent) {
         String action=intent.getAction();
         if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action))
         {
             int state=intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,BluetoothAdapter.ERROR);
             switch (state)
             {
                 case BluetoothAdapter.STATE_ON:
                     Toast.makeText(context, "Turned on", Toast.LENGTH_SHORT).show();
                     break;
                 case BluetoothAdapter.STATE_TURNING_ON:
                     Toast.makeText(context, "Turning on", Toast.LENGTH_SHORT).show();
                     break;
                 case BluetoothAdapter.STATE_TURNING_OFF:
                     Toast.makeText(context, "Turning off", Toast.LENGTH_SHORT).show();
                     break;
                 case BluetoothAdapter.STATE_OFF:
                     Toast.makeText(context, "Turned off in broadcast", Toast.LENGTH_SHORT).show();
                     break;
             }
         }
     }
 };

    BroadcastReceiver broadcastReceiverforParieddevices =new BroadcastReceiver() {
        @Override
         public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String devicename = device.getName();
                String deviceHardwareAddress = device.getAddress();
                D_ScannedDevicesInfo d_scannedDevicesInfo = new D_ScannedDevicesInfo(devicename, deviceHardwareAddress);
                if (!arrayList.contains(d_scannedDevicesInfo))
                {
                    arrayList.add(d_scannedDevicesInfo);
                }
                myAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intent=new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(broadcastReceiverTocheckOffOrOn,intent);

        IntentFilter scanningIntent=new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(broadcastReceiverforParieddevices,scanningIntent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiverTocheckOffOrOn);
        unregisterReceiver(broadcastReceiverforParieddevices);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.turnon)
        {
            if (bluetoothAdapter==null)
                Toast.makeText(getApplicationContext(),"Device does not have bluetooth ",Toast.LENGTH_LONG).show();
            else
            {
                if (!bluetoothAdapter.isEnabled())
                {
                    Intent  intent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent,1);
                }
                if (bluetoothAdapter.isEnabled())
                {
                    Toast.makeText(getApplicationContext(),"Bluetooth turned off ",Toast.LENGTH_LONG).show();
                    bluetoothAdapter.disable(); //too turn of bluetooth
                }
            }
        }
        else if (v.getId()==R.id.paireddevices)
        {
            pariedInfo();
        }
        else if (v.getId()==R.id.ScanBluetoothDevices)
        {
            scanForBluetoothDevices();
        }
        else if (v.getId()==R.id.MakeDeviceDiscoverable)
        {
            makeDeviceDiscoverable();
        }
    }

    private void makeDeviceDiscoverable() {
     if (bluetoothAdapter.isEnabled())
     {
         Intent discoverableIntent=new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
         discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300);
         startActivityForResult(discoverableIntent,3);
     }
    }

    private void scanForBluetoothDevices() {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},2);
        if (isClicked)
        {
            arrayList.clear();
            myAdapter.notifyDataSetChanged();
        }
        else
        {
            isClicked=true;
        }

        if (bluetoothAdapter!=null && bluetoothAdapter.cancelDiscovery())
            bluetoothAdapter.startDiscovery();


    }

    private void pariedInfo() {
       if (bluetoothAdapter!=null)
       {
           Set<BluetoothDevice> pariedDevices=bluetoothAdapter.getBondedDevices(); //to get paired devices list
           if (pariedDevices.size()>0)
           {
               for (BluetoothDevice device:pariedDevices)
               {
                   String deviceName=device.getName();
                   String address=device.getAddress();
                   Toast.makeText(this, " Device name:"+deviceName+"\n Address:"+address, Toast.LENGTH_SHORT).show();
               }
           }
       }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1)
        {
             if (resultCode==RESULT_OK)
             {
                 Toast.makeText(this, "Turned on successfully", Toast.LENGTH_SHORT).show();
             }
             if (resultCode==RESULT_CANCELED)
                 Toast.makeText(this, "You have denied permission", Toast.LENGTH_SHORT).show();
        }
        else if (requestCode==3)
        {
            if (resultCode!=RESULT_CANCELED)
            {
                Toast.makeText(this, "device is visible", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this, "Device is not visible", Toast.LENGTH_SHORT).show();
        }
    }
}
