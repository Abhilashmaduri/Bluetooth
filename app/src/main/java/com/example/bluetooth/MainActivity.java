package com.example.bluetooth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button turnon;
    BluetoothAdapter bluetoothAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      turnon=findViewById(R.id.turnon);

      bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

      turnon.setOnClickListener(this);
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
    }
}
