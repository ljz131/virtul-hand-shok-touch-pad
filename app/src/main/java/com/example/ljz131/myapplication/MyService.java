/*注意事项v2
将socket连接放在‘发送广播’中，而非之前的oncreate方法中
可以先使用绑定之后返回的binder作为中介使用 setHost方法
则会在第一次发送时修改标志位，根据islink创建连接
在112行附近的备注中，可以在建立连接时略作延迟，防止连接建立太久，目前没有问题 */


package com.example.ljz131.myapplication;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


//import net.sf.json.JSONObject;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MyService extends Service {

    private Socket clientSocket = null;

    private SendThread sendThread = null;
    private ScoketLinkThread scoketLinkThread = null;

    private boolean isLink = false;
    //true means sending and false means receiving;

    public final String BRO_TO_SERVICE = "broadcast_to_service";
    public final String CONNECT = "make_a_connect";
    public final String SEND = "send_sth";

    private  mBinder binder = new mBinder();
    private SocketReceiver socketReceiver;
    private JSONObject mJosnobject = new JSONObject();
    //private String mString = "what";
    final int PORT = 12345;
    private String host="192.168.199.1501";//同一个局域网内作为服务端的IP，使用端口1041 192.168.199.122

    /*public void setHost(String host){
        this.host = host;
    }
    *//*public String getHost(){
        return host;
    }*/

    public MyService() {
    }

    public class mBinder extends Binder{
        public void setJSON(JSONObject jsonObject){
            //SSS 备注一个，如果替换josn的时间过长，可能在发送之前要等一下
            mJosnobject = jsonObject;
            Log.d("SSS", "setJOSN");
        }
        public JSONObject getJOSN(){
            Log.d("SSS", "getJOSN");
            return mJosnobject;
        }
        public void setHost(String puthost){
            host = puthost;
        }
        public String getHost(){
            return host;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("SSS MyService service created1");

//        方便ip地址的修改，不在create里就连接，改在广播接收
//        scoketLinkThread = new ScoketLinkThread() ;
//        scoketLinkThread.start();

        //进行动态绑定
        System.out.println("SSS set brocast");
        socketReceiver = new SocketReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(CONNECT);
        registerReceiver(socketReceiver, filter);
        //根据标志位 isSending 来决定是发送或接收
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return binder;
    }

    public class SocketReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

//            try {
//                Log.d("SSS","getStringExtra TRY");
//                //mString = intent.getStringExtra("data");
//                //Log.d("SSS",mString);
//            }catch (Exception e) {
//                Log.d("SSS","EEE");
//                e.printStackTrace();
//            }
            if (!isLink){
                Log.d("SSS isLink","0");
                scoketLinkThread = new ScoketLinkThread();
                scoketLinkThread.start();
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                isLink = true;
            }

            String action = intent.getAction();
            Log.d("SSS","SocketReciver " );
            Log.d("SSS ", action);
            sendThread = new SendThread(clientSocket);
            sendThread.run();
        }
    }

    public class SendThread extends Thread{

        Socket mSocket;
        protected DataOutputStream out;

        @Override
        public void run() {
//            super.run();
//            try {
//                Log.d("SSS DataOutputStream ","new");
//                out = new DataOutputStream(mSocket.getOutputStream());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            Log.d("SSS SendThread ","sending9");

            if (out != null) {
                try {
//                    out.write(mString.getBytes());
                    //接收端的要求要UTF
                    out.writeUTF(mJosnobject.toString());
                    out.flush();
//                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {Log.d("SSS SendThread","out is null");}

//            Log.d("SSS SendThread ",mString);
            Log.d("SSS SendThread ","END");
        }

        SendThread(Socket socket){
            Log.d("SSS SendThread","Socket socket");
            try {
                Log.d("SSS DataOutputStream ","new");
                out = new DataOutputStream(socket.getOutputStream());
                mSocket = socket;
            } catch (Exception e) {
                Log.d("SSS DataOutputStream ","eee");
                e.printStackTrace();
            }
        }
    }

    private class ScoketLinkThread extends Thread{

//        protected Socket socket;
//        protected InetAddress addr;

        @Override
        public void run() {
            Log.d("SSS","ScoketLink");
            //        进行 Socket链接
            try {
                Log.d("SSS","clientSocket try18");
                clientSocket = new Socket(host,PORT);
                Log.d("SSS","clientSocket try19");
                System.out.println("SSS clientSocket succeed");
            } catch (IOException e) {
                Log.d("SSS","clientSocket EEE");
                Toast.makeText(getApplicationContext(),"MDZZ,wrong IP!!",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            Log.d("SSS","TRY END");

            //sendThread = new SendThread(clientSocket);
            //sendThread.run();
        }
    }



    @Override
    public void onDestroy() {
//        try {
//            clientSocket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        super.onDestroy();
    }
}
