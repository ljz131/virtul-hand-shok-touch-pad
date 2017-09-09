package com.example.ljz131.myapplication;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

//import net.sf.json.JSONObject;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Delayed;

/**
 * Created by ljz131 on 2017/5/17.
 */

public class ComplicatedHandShankView extends AppCompatActivity {

    Button left_first ;
    Button left_second ;
    Button right_first ;
    Button right_second ;
    Button set;
    //Button check_ip;
    TextView View1=null;
    //EditText editTextIp;
    Switch gravity;
    Switch eight_four;

    boolean eight_or_four = false;
    boolean gravity_on_off = false;

    double alpha_of_yxl ;
    double alpha_of_yxr ;

    private RockerView rocker_view_left;
    private RockerView rocker_view_right;
    SensorManager smanger;
    Sensor sensor_gravity;

    String[] text={"","","","","","","","","","","",""};
    String text2;
    //MyService myService = new MyService();
    //String ip = myService.getHost();

    ComplicatedDataPackge complicatedDataPackge = new ComplicatedDataPackge();
    ComplicatedDataPackge complicatedDataPackge_save = new ComplicatedDataPackge();
    int sent_flag = 0 ;
    int button_flag = 0;

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
        setContentView(R.layout.complicated_hand_shank);

        //editTextIp = (EditText) findViewById(R.id.chs_ip_ed);
        View1 = (TextView) findViewById(R.id.chs_text);
        left_first = (Button) findViewById(R.id.chs_left_first);
        left_second = (Button) findViewById(R.id.chs_left_second);
        right_first = (Button) findViewById(R.id.chs_right_first);
        right_second = (Button) findViewById(R.id.chs_right_second);
        set = (Button) findViewById(R.id.chs_center_key);
        //check_ip = (Button) findViewById(R.id.chs_IP);

        rocker_view_left = (RockerView) findViewById(R.id.rockerView_left);
        rocker_view_right = (RockerView) findViewById(R.id.rockerView_right);

        gravity = (Switch) findViewById(R.id.complicated_gravity);
        eight_four = (Switch) findViewById(R.id.complicated_EightorFour);

        intent_startService.setAction("android.myservive");
        intent_startService.setPackage("com.example.ljz131.myapplication");//双引号里填包名
        bindService(intent_startService,serviceConnection,BIND_AUTO_CREATE);

        //editTextIp.setText(ip);
        complicatedDataPackge.init();
        /*check_ip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myService.setHost(editTextIp.getText().toString());
                editTextIp.setText(myService.getHost());
                editTextIp.clearFocus();
                //editTextIp.setFocusable(false);
            }
        });*/
        left_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complicatedDataPackge.setLeft_first(1);
                button_flag = 1;
            }
        });
        left_second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complicatedDataPackge.setLeft_second(1);
                button_flag = 1;
            }
        });
        right_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complicatedDataPackge.setRight_first(1);
                button_flag = 1;
            }
        });
        right_second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complicatedDataPackge.setRight_second(1);
                button_flag = 1;
            }
        });
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complicatedDataPackge.setSet(1);
                button_flag = 1;
            }
        });
        rocker_view_left.setRockerChangeListener(new RockerView.RockerChangeListener() {

            @Override
            public void report(float x, float y) {
                // TODO Auto-generated method stub
                // doLog(x + "/" + y);
                // setLayout(rockerView2, (int)x, (int)y);
                alpha_of_yxl = Math.toDegrees (Math.atan (y/x));
                boolean left_or_right_L;
                boolean up_or_down_L;

                if(eight_or_four){
                    left_or_right_L = (-45 <= alpha_of_yxl && alpha_of_yxl <= 45);
                    up_or_down_L = (-45 > alpha_of_yxl || alpha_of_yxl > 45);
                }else {
                    left_or_right_L = (-67.5 < alpha_of_yxl && alpha_of_yxl < 67.5);
                    up_or_down_L = (-22.5 > alpha_of_yxl || alpha_of_yxl > 22.5);
                }
                if(left_or_right_L){
                    if ( x < 0 ) {
                        //if (complicatedDataPackge.getLeftHS_left()==0){
                            complicatedDataPackge.setLeftHS_left(1);
                       // }
                        complicatedDataPackge.setLeftHS_right(0);
                        text[0] = "A";
                        text[1] = "";
                    }if (x > 0) {
                        //if (complicatedDataPackge.getLeftHS_right()==0){
                            complicatedDataPackge.setLeftHS_left(0);
                       // }
                        complicatedDataPackge.setLeftHS_right(1);
                        text[0] = "A";
                        text[1] = "";
                    }
                }else {
                    complicatedDataPackge.setLeftHS_right(0);
                    complicatedDataPackge.setLeftHS_left(0);
                    text[0] = "";
                    text[1] = "";
                }
                if (up_or_down_L) {
                    if (y < 0) {
                        //if (complicatedDataPackge.getLeftHS_up()==0){
                            complicatedDataPackge.setLeftHS_up(1);
                       // }
                        complicatedDataPackge.setLeftHS_down(0);
                        text[2] = "W";
                        text[3] = "";
                    }
                    if (y > 0) {
                        //if (complicatedDataPackge.getLeftHS_down()==0){
                            complicatedDataPackge.setLeftHS_down(1);
                        //}
                        complicatedDataPackge.setLeftHS_up(0);
                        text[3] = "S";
                        text[2] = "";
                    }
                } else {
                    complicatedDataPackge.setLeftHS_down(0);
                    complicatedDataPackge.setLeftHS_up(0);
                    text[2] = "";
                    text[3] = "";
                }
                text2="左摇杆:";
                for (int i=0;i<=11;i++){
                    text2=text2+text[i];
                    if(i==3){
                        text2 = text2 + "\n右摇杆:";
                    }
                    if(i==7 && gravity_on_off){
                        text2 = text2 + "\n重力感应";
                    }
                }
                View1.setText(text2);
                /*y往上为负，往下为正；x往右为正往左为负
                * 右上角与左下角alpha_of_yx为负
                * 左上角与右下角alpha_of_yx为正
                * 则有如下判断条件（分离的四键）
                * alpha_of_yx从-45到45
                *   x小于0 为左
                * alpha_of_yx从-45到45
                *   x大于0 为右
                * alpha_of_yx大于45||小于-45
                *   y小于0 为上
                * alpha_of_yx大于45||小于-45
                *   y大于0 为下
                *
                * 八键逻辑则是均分为8块
                * 每块45度
                *
                * 切记要用全局数组把命令取出来
                * */
            }
        });

        rocker_view_right.setRockerChangeListener(new RockerView.RockerChangeListener() {

            @Override
            public void report(float x, float y) {
                // TODO Auto-generated method stub
                // doLog(x + "/" + y);
                // setLayout(rockerView2, (int)x, (int)y);

                alpha_of_yxr = Math.toDegrees (Math.atan (y/x));
                boolean left_or_right_R;
                boolean up_or_down_R;

                if(eight_or_four){
                    left_or_right_R = (-45 <= alpha_of_yxr && alpha_of_yxr <= 45);
                    up_or_down_R = (-45 > alpha_of_yxr || alpha_of_yxr > 45);
                }else {
                    left_or_right_R = (-67.5 < alpha_of_yxr && alpha_of_yxr < 67.5);
                    up_or_down_R = (-22.5 > alpha_of_yxr || alpha_of_yxr > 22.5);
                }
                if (left_or_right_R) { //判断左右根据组合逻辑或者分离逻辑
                    if (x < 0) {
                        complicatedDataPackge.setRightHS_left(1);
                        text[4] = "A";
                        text[5] = "";
                        complicatedDataPackge.setRightHS_right(0);
                    }
                    if (x > 0) {
                        complicatedDataPackge.setRightHS_right(1);
                        text[5] = "D";
                        text[4] = "";
                        complicatedDataPackge.setRightHS_left(0);
                    }
                } else {
                    text[4] = "";
                    text[5] = "";
                    complicatedDataPackge.setRightHS_left(0);
                    complicatedDataPackge.setRightHS_right(0);
                }
                if (up_or_down_R) {
                    if (y < 0) {
                        complicatedDataPackge.setRightHS_up(1);
                        text[6] = "W";
                        text[7] = "";
                        complicatedDataPackge.setRightHS_down(0);
                    }
                    if (y > 0) {
                        complicatedDataPackge.setRightHS_down(1);
                        text[7] = "S";
                        text[6] = "";
                        complicatedDataPackge.setRightHS_up(0);
                    }
                } else {
                    text[7] = "";
                    text[6] = "";
                    complicatedDataPackge.setRightHS_up(0);
                    complicatedDataPackge.setRightHS_down(0);
                }
                text2="左摇杆:";
                for (int i=0;i<=11;i++){
                    text2=text2+text[i];
                    if(i==3){
                        text2 = text2 + "\n右摇杆:";
                    }
                    if(i==7 && gravity_on_off){
                        text2 = text2 + "\n重力感应";
                    }
                }
                View1.setText(text2);
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
                            text[8] = "A";
                            complicatedDataPackge.setGravity_left(1);
                        } else{
                            text[8] = "";
                            complicatedDataPackge.setGravity_left(0);
                        }
                        if(alpha<85 && alpha>0){
                            text[9] = "D";
                            complicatedDataPackge.setGravity_right(1);
                        } else{
                            text[9] = "";
                            complicatedDataPackge.setGravity_right(0);
                        }
                        if(beta>50||beta<-50){
                            text[10] = "W";
                            complicatedDataPackge.setGravity_up(1);
                        } else{
                            text[10] = "";
                            complicatedDataPackge.setGravity_up(0);
                        }
                        if(beta>-40&&beta<40){
                            text[11] = "S";
                            complicatedDataPackge.setGravity_down(1);
                        } else{
                            text[11] = "";
                            complicatedDataPackge.setGravity_down(0);
                        }
                    }
                }
                text2="左摇杆:";
                for (int i=0;i<=11;i++){
                    text2=text2+text[i];
                    if(i==3){
                        text2 = text2 + "\n右摇杆:";
                    }
                    if(i==7 && gravity_on_off){
                        text2 = text2 + "\n重力感应:";
                    }
                }
                View1.setText(text2);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        smanger = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor_gravity = smanger.getDefaultSensor(Sensor.TYPE_ACCELEROMETER );
        smanger.registerListener(myListener,sensor_gravity,100000);

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
                    complicatedDataPackge.setGravity_up(0);
                    complicatedDataPackge.setGravity_down(0);
                    complicatedDataPackge.setGravity_left(0);
                    complicatedDataPackge.setGravity_right(0);
                    for(int i=8;i<=11;i++){
                        text[i] = "";
                    }
                }
            }
        });

        eight_four.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    eight_four.setText("摇杆键分离");//分离意味着上下左右只能同一时间内按下一个
                    Toast.makeText(getApplicationContext(),"分离意味着上下左右只能同一时间内按下一个",Toast.LENGTH_SHORT).show();
                    eight_or_four = true;
                }else {
                    eight_four.setText("摇杆键组合");//组合意味着上下左右同一时间内可以按下两个,例如同时有左和右
                    Toast.makeText(getApplicationContext(),"组合意味着上下左右同一时间内可以按下两个,例如同时有左和右",Toast.LENGTH_SHORT).show();
                    eight_or_four = false;
                }
            }
        });



        runnable = new Runnable() {
            @Override
            public void run() {

                //View1.setText(jsonObject.toString());
                /*if(complicatedDataPackge.toString().equals(complicatedDataPackge_save.toString())){
                    Log.d("comData","set flag0000000");
                    sent_flag = 0;
                }else {
                    Log.d("comData","flag1111");
                    sent_flag = 1;
                }*/
                //if(sent_flag == 1){
                    setjs();
                    binder.setJSON(jsonObject);
                    Intent intent = new Intent();
                    intent.setAction(CONNECT);
                    sendBroadcast(intent);
                    sent_flag = 0;
                    complicatedDataPackge_save = complicatedDataPackge;
                //}

                if (button_flag == 1){
                    complicatedDataPackge.setSet(0);
                    complicatedDataPackge.setLeft_second(0);
                    complicatedDataPackge.setLeft_first(0);
                    complicatedDataPackge.setRight_first(0);
                    complicatedDataPackge.setRight_second(0);
                    button_flag = 0;
                    Log.d("comData",jsonObject.toString());
                }
                handler.postDelayed(this,20);
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
        mapData.put("gravity_down",complicatedDataPackge.getGravity_down());
        mapData.put("gravity_left",complicatedDataPackge.getGravity_left());
        mapData.put("gravity_right",complicatedDataPackge.getGravity_right());
        mapData.put("gravity_up",complicatedDataPackge.getGravity_up());

        mapData.put("leftHS_right",complicatedDataPackge.getLeftHS_right());
        mapData.put("leftHS_left",complicatedDataPackge.getLeftHS_left());
        mapData.put("leftHS_up",complicatedDataPackge.getLeftHS_up());
        mapData.put("leftHS_down",complicatedDataPackge.getLeftHS_down());

        mapData.put("rightHS_left",complicatedDataPackge.getRightHS_left());
        mapData.put("rightHS_right",complicatedDataPackge.getRightHS_right());
        mapData.put("rightHS_up",complicatedDataPackge.getRightHS_up());
        mapData.put("rightHS_down",complicatedDataPackge.getRightHS_down());

        mapData.put("left_first",complicatedDataPackge.getLeft_first());
        mapData.put("left_second",complicatedDataPackge.getLeft_second());
        mapData.put("right_first",complicatedDataPackge.getRight_first());
        mapData.put("right_second",complicatedDataPackge.getRight_second());

        map.put("set",complicatedDataPackge.getSet());
        map.put("mode",complicatedDataPackge.getMode());
        map.put("data",mapData);

        jsonObject = new JSONObject(map);
    }
}
