package com.example.ljz131.myapplication;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ljz131 on 2017/5/19.
 */

public class SimpleHandShankView extends AppCompatActivity {

    ImageView left_top;
    ImageView left_center;
    ImageView left_bottom;
    ImageView right_top;
    ImageView right_center;
    ImageView right_bottom;
    Button set;
    //Button change_ip;
    Switch gravity;
    //EditText editTextip;

    SensorManager smanger;
    Sensor sensor_gravity;

    boolean gravity_on_off = false;
    boolean button_flag = false;
    MyService myService = new MyService();
    //String ip = myService.getHost();

    SimpleDataPackge simpleDataPackge = new SimpleDataPackge();
    private Handler handler = new Handler();
    private Runnable runnable;

    public MyService.mBinder binder = null;
    private JSONObject jsonObject = new JSONObject();
    private Intent intent_startService = new Intent();
    public final String CONNECT = "make_a_connect";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_hand_shank);
        left_top = (ImageView) findViewById(R.id.shs_left_top);
        left_center = (ImageView) findViewById(R.id.shs_left_center);
        left_bottom = (ImageView) findViewById(R.id.shs_left_bottom);
        right_top = (ImageView) findViewById(R.id.shs_right_top);
        right_center = (ImageView) findViewById(R.id.shs_right_center);
        right_bottom = (ImageView) findViewById(R.id.shs_right_bottom);
        set = (Button) findViewById(R.id.shs_center_key);
        //change_ip = (Button) findViewById(R.id.shs_ip);
        gravity = (Switch) findViewById(R.id.simple_gravity);
        //editTextip = (EditText) findViewById(R.id.located_view);

        intent_startService.setAction("android.myservive");
        intent_startService.setPackage("com.example.ljz131.myapplication");//双引号里填包名
        bindService(intent_startService,serviceConnection,BIND_AUTO_CREATE);

        /*editTextip.setText(ip);
        change_ip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myService.setHost(editTextip.getText().toString());
                editTextip.setText(myService.getHost());
                editTextip.clearFocus();
            }
        });*/
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleDataPackge.setSet(1);
                button_flag = true;
            }
        });
        left_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleDataPackge.setLeft_top(1);
                button_flag = true;
            }
        });
        left_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleDataPackge.setLeft_center(1);
                button_flag = true;
            }
        });
        left_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleDataPackge.setLeft_bottom(1);
                button_flag = true;
            }
        });
        right_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleDataPackge.setRight_top(1);
                button_flag = true;
            }
        });
        right_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleDataPackge.setRight_center(1);
                button_flag = true;
            }
        });
        right_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleDataPackge.setRight_bottom(1);
                button_flag = true;
            }
        });

        final SensorEventListener myListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float X = sensorEvent.values[0];
                float Y = sensorEvent.values[1];
                float Z = sensorEvent.values[2];
                double sqr = Math.sqrt(X*X+Y*Y+Z*Z);
                double alpha = Math.toDegrees(Math.atan(Math.sqrt(Math.pow(X,2) + Math.pow(Z,2))/Y ));//左右旋转角
                double beta = Math.toDegrees(Math.atan(Math.sqrt(Math.pow(Y,2) + Math.pow(Z,2))/X ));//上下旋转角
                String str = "";//  "X:" + X + "，Y:" + Y + "，Z:" + Z+"\nalpha:"+alpha+"\nbeta:"+beta+"\nsqr:"+sqr;
                if(sqr<11 && gravity_on_off){
                    if( (beta<88&&beta>-88) || (alpha<88&&alpha>-88)){
                        //留出一个平放模式
                        if(alpha>-85 && alpha<0){
                            simpleDataPackge.setGravity_left(1);
                        }else {
                            simpleDataPackge.setGravity_left(0);
                        }
                        if(alpha<85 && alpha>0){
                            simpleDataPackge.setGravity_right(1);
                        }else {
                            simpleDataPackge.setGravity_right(0);
                        }
                        if(beta>50||beta<-50){
                            simpleDataPackge.setGravity_up(1);
                        }else {
                            simpleDataPackge.setGravity_up(0);
                        }
                        if(beta>-40&&beta<40){
                            simpleDataPackge.setGravity_down(1);
                        }else {
                            simpleDataPackge.setGravity_down(0);
                        }
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        gravity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    gravity.setText("重力感应:开");
                    Toast.makeText(getApplicationContext(),"请倾斜手机在45度左右",Toast.LENGTH_SHORT).show();
                    gravity_on_off = true;
                }else {
                    gravity.setText("重力感应:关");
                    gravity_on_off =false;
                    simpleDataPackge.setGravity_up(0);
                    simpleDataPackge.setGravity_down(0);
                    simpleDataPackge.setGravity_left(0);
                    simpleDataPackge.setGravity_right(0);
                }
            }
        });
        smanger = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor_gravity = smanger.getDefaultSensor(Sensor.TYPE_ACCELEROMETER );
        smanger.registerListener(myListener,sensor_gravity,100000);

        runnable = new Runnable() {
            @Override
            public void run() {

                setjs();
                binder.setJSON(jsonObject);
                Intent intent = new Intent();
                intent.setAction(CONNECT);
                sendBroadcast(intent);

                if (button_flag){
                    simpleDataPackge.setSet(0);
                    simpleDataPackge.setLeft_top(0);
                    simpleDataPackge.setLeft_center(0);
                    simpleDataPackge.setLeft_bottom(0);
                    simpleDataPackge.setRight_top(0);
                    simpleDataPackge.setRight_center(0);
                    simpleDataPackge.setRight_bottom(0);
                    button_flag =false;
                    Log.d("comData",jsonObject.toString());
                }
                handler.postDelayed(this,20);
                /*binder.setJOSN(jsonObject);

                Intent intent = new Intent();
                intent.setAction(CONNECT);
                sendBroadcast(intent);*/
                //simpleDataPackge.init();
                //Toast.makeText(getApplicationContext(),"Json",Toast.LENGTH_SHORT).show();
            }
        };
        setjs();

        Intent intent = new Intent();
        intent.setAction(CONNECT);

        //binder.setJSON(jsonObject);
        sendBroadcast(intent);
        handler.postDelayed(runnable,20);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    public void setjs(){
        Map map = new HashMap();
        Map mapData = new HashMap();
        //map.put("name", "json");
        mapData.put("gravity_down",simpleDataPackge.getGravity_down());
        mapData.put("gravity_left",simpleDataPackge.getGravity_left());
        mapData.put("gravity_right",simpleDataPackge.getGravity_right());
        mapData.put("gravity_up",simpleDataPackge.getGravity_up());

        mapData.put("left_top",simpleDataPackge.getLeft_top());
        mapData.put("left_center",simpleDataPackge.getLeft_center());
        mapData.put("left_bottom",simpleDataPackge.getLeft_bottom());

        mapData.put("right_top",simpleDataPackge.getRight_top());
        mapData.put("right_center",simpleDataPackge.getRight_center());
        mapData.put("right_bottom",simpleDataPackge.getRight_bottom());

        map.put("mode",simpleDataPackge.getMode());
        map.put("set",simpleDataPackge.getSet());
        map.put("data",mapData);

        jsonObject = new JSONObject(map);
    }

}
