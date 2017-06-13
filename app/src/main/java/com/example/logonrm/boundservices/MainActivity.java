package com.example.logonrm.boundservices;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    BoundService boundService;
    boolean mServiceBound = false;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BoundService.MyBinder myBinder = (BoundService.MyBinder) service;
            boundService = myBinder.getService();
            mServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView tvTime = (TextView) findViewById(R.id.tvTempo);
        Button btStopService = (Button) findViewById(R.id.btPararServico);
        Button btPrintTempo = (Button) findViewById(R.id.btTempo);
        Button btIniciarServico = (Button) findViewById(R.id.btIniciarServico);
        btIniciarServico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mServiceBound){
                    mServiceBound = true;
                    startServices();
                }
            }
        });
        btPrintTempo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mServiceBound) {
                    tvTime.setText(boundService.getTimestamp());
                }
            }
        });
        btStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mServiceBound) {
                    unbindService(mServiceConnection);
                    mServiceBound = false;
                }
                tvTime.setText("");
                Intent intent = new Intent(MainActivity.this, BoundService.class);
                stopService(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        startServices();
    }

    public void startServices() {
        Intent intent = new Intent(this, BoundService.class);
        startService(intent);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mServiceBound) {
            unbindService(mServiceConnection);
            mServiceBound = false;
        }
    }
}
