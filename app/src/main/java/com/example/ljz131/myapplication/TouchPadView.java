package com.example.ljz131.myapplication;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ljz131 on 2017/5/19.
 */

public class TouchPadView extends AppCompatActivity {

    //TextView View1=null;
    TouchPad touchPad=null;
    Button left_click;
    Button right_click;
    Button slide_up;
    Button slide_down;
    //Button change_ip;
    //EditText editTextip;

    TouchPadDataPackge touchPadDataPackge = new TouchPadDataPackge();
    private Handler handler = new Handler();
    private Runnable runnable;
    //MyService myService = new MyService();

    float distance = 0;
    float old_distance = 0;
    float maxdetay = 0;
    JSONObject jsonObject = new JSONObject();
    //String ip = myService.getHost();

    private MyService.mBinder binder = null;
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
        setContentView(R.layout.touch_pad);

        intent_startService.setAction("android.myservive");
        intent_startService.setPackage("com.example.ljz131.myapplication");//双引号里填包名
        bindService(intent_startService,serviceConnection,BIND_AUTO_CREATE);

        left_click = (Button) findViewById(R.id.tp_left_click);
        right_click = (Button) findViewById(R.id.tp_right_click);
        slide_up = (Button) findViewById(R.id.tp_silde_up);
        slide_down = (Button) findViewById(R.id.tp_silde_down);
        //change_ip = (Button) findViewById(R.id.touch_change_ip);
        //View1 = (TextView) findViewById(R.id.tp_text);
        touchPad = (TouchPad) findViewById(R.id.tp_the_touch_pad);
        //editTextip = (EditText) findViewById(R.id.touch_ip_ed);

        //View1.setText("test");
        /*editTextip.setText(ip);
        change_ip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myService.setHost(editTextip.getText().toString());
                editTextip.setText(myService.getHost());
                editTextip.clearFocus();
            }
        });*/
        left_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchPadDataPackge.setLeft_click(1);
            }
        });
        right_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchPadDataPackge.setRight_click(1);
            }
        });
        slide_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchPadDataPackge.setSlide(2);
            }
        });
        slide_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchPadDataPackge.setSlide(-2);
            }
        });
        touchPad.setTouchPadListner(new TouchPad.TouchPadChangeListner() {
            @Override
            public void report(float x1, float y1, float x2, float y2, float detax1, float detay1, float detax2, float detay2) {
                distance = (float) Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
                if((x1 == 0 && y1 == 0)||(x2 == 0 && y2 ==0)){//单指滑动
                    touchPadDataPackge.setDetax((int) detax1);
                    touchPadDataPackge.setDetay((int) detay1);
                }else{
                    if(detay1>0 && detay2>0){//双指下滑
                        maxdetay = (detay1>detay2)?detay1:detay2;
                        touchPadDataPackge.setSlide((int) Math.pow(1.3,Math.log(maxdetay)/Math.log(2)));
                    }
                    if(detay1<0 && detay2<0){//双指上滑
                        maxdetay = Math.abs((detay1<detay2)?detay1:detay2);
                        touchPadDataPackge.setSlide((int) -Math.pow(1.3,Math.log(maxdetay)/Math.log(2)));
                    }
                    if((detay1<0 && detay2>0)||(detay1>0 && detay2<0)){//缩放
                        touchPadDataPackge.setCtrl(1);
                        if(distance < old_distance){
                            touchPadDataPackge.setSlide((int) Math.pow(1.3,Math.log(Math.abs(detay1)+Math.abs(detay2))/Math.log(2)));
                        }else {
                            touchPadDataPackge.setSlide((int) -Math.pow(1.3,Math.log(Math.abs(detay1)+Math.abs(detay2))/Math.log(2)));
                        }
                    }
                }
                old_distance = distance;
            }
        });



        runnable = new Runnable() {
            @Override
            public void run() {

                //editTextip.setText(jsonObject.toString());
                setJs();
                binder.setJSON(jsonObject);
                Intent intent = new Intent();
                intent.setAction(CONNECT);
                sendBroadcast(intent);
                touchPadDataPackge.init();
                handler.postDelayed(this,20);
                //Toast.makeText(getApplicationContext(),"Json",Toast.LENGTH_SHORT).show();
            }
        };
        setJs();

        handler.postDelayed(runnable,20);
        Intent intent = new Intent();
        intent.setAction(CONNECT);
        sendBroadcast(intent);
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

    public void setJs(){
        Map map = new HashMap();
        Map mapData = new HashMap();
        //map.put("name", "json");
        mapData.put("detax",touchPadDataPackge.getDetax());
        mapData.put("detay",touchPadDataPackge.getDetay());

        mapData.put("left_click",touchPadDataPackge.getLeft_click());
        mapData.put("right_click",touchPadDataPackge.getRight_click());
        mapData.put("slide",touchPadDataPackge.getSlide());

        mapData.put("ctrl",touchPadDataPackge.getCtrl());

        map.put("mode",touchPadDataPackge.getMode());
        map.put("set",touchPadDataPackge.getSet());
        map.put("data",mapData);

         jsonObject = new JSONObject(map);
    }
}
