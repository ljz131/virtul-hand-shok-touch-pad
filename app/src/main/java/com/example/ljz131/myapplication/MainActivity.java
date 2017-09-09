package com.example.ljz131.myapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class MainActivity extends AppCompatActivity {

    private MyService.mBinder binder = null;
    private Intent intent_startService = new Intent();
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            binder = (MyService.mBinder) iBinder;
            Log.d("SSS2","onServiceConnected" );
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("SSS2","onServiceDisconnected" );
        }
    };

    Button button1=null;
    Button button2=null;
    Button button3=null;
    Button change_ip;
    EditText edittext_ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_mode);

        intent_startService.setAction("android.myservive");
        intent_startService.setPackage("com.example.ljz131.myapplication");//双引号里填包名
        bindService(intent_startService,serviceConnection,BIND_AUTO_CREATE);
        /*Intent intent = new Intent(this,MyService.class);
        startService(intent);*/

        button1= (Button) findViewById(R.id.mode1);
        button2= (Button) findViewById(R.id.mode2);
        button3= (Button) findViewById(R.id.mode3);
        change_ip = (Button) findViewById(R.id.change_ip);
        edittext_ip = (EditText) findViewById(R.id.change_ip_text);
        edittext_ip.setText("192.168.199.");

        change_ip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binder.setHost(edittext_ip.getText().toString());
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ComplicatedHandShankView.class);
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SimpleHandShankView.class);
                startActivity(intent);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,TouchPadView.class);
                startActivity(intent);
            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
