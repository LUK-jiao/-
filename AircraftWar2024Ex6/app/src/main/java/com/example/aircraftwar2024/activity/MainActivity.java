package com.example.aircraftwar2024.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.aircraftwar2024.R;
import com.example.aircraftwar2024.game.BaseGame;
import com.example.aircraftwar2024.music.MyMediaPlayer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private Handler onlinehandler;

    private static MainActivity instance;
    public Handler mHandler;

    private Socket socket;
    private PrintWriter writer;

    private int layoutId = R.layout.activity_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                // 处理接收到的消息，根据消息执行相应的操作
                if (msg.what == 1) {
                    Intent intent = new Intent(MainActivity.this,RecordActivity.class);
                    startActivity(intent);
                }
                if(msg.what == 2){
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        this.onlinehandler = new Handler(getMainLooper()){
            //当数据处理子线程更新数据后发送消息给UI线程，UI线程更新UI
            @Override
            public void handleMessage(Message msg){
               //TODO:编写处理事件
            }
        };
        super.onCreate(savedInstanceState);
        setContentView(layoutId);
        instance = this;
        Button offline_btn = (Button) findViewById(R.id.offlinegame);
        Button online_btn = (Button) findViewById(R.id.onlinegame);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        offline_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //更新BaseGame.isOnline的值
                BaseGame.setisOnline(false);
                int selectedId = radioGroup.getCheckedRadioButtonId(); // 获取选中的 RadioButton 的 ID
                RadioButton radioButton = (RadioButton) findViewById(selectedId); // 根据 ID 获取 RadioButton 对象
                String option = radioButton.getText().toString(); // 获取选中 RadioButton 的文本
                Intent intent = new Intent(MainActivity.this,OffilineActivity.class);
                intent.putExtra("Music", option);
                startActivity(intent);
            }
        });
        online_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new Thread(new NetConn(onlinehandler)).start();
                //更新BaseGame.isOnline的值
                BaseGame.setisOnline(true);
                int selectedId = radioGroup.getCheckedRadioButtonId(); // 获取选中的 RadioButton 的 ID
                RadioButton radioButton = (RadioButton) findViewById(selectedId); // 根据 ID 获取 RadioButton 对象
                String option = radioButton.getText().toString(); // 获取选中 RadioButton 的文本
                Intent intent = new Intent(MainActivity.this,OffilineActivity.class);
                intent.putExtra("Music", option);
                startActivity(intent);
            }
        });
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                // 处理接收到的消息，根据消息执行相应的操作
                if (msg.what == 1) {
                    Intent intent = new Intent(MainActivity.this,RecordActivity.class);
                    startActivity(intent);
                }
                if(msg.what == 2){
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                if(msg.what == 3){
                    //TODO:跳转到联机游戏结算画面
                    Intent intent = new Intent(MainActivity.this, OnlineEndActivity.class);
                    startActivity(intent);
                }
            }
        };
    }

    public static MainActivity getInstance() {
        return instance;
    }

    public Handler getHandler() {
        return mHandler;
    }

    protected class NetConn extends Thread{
        private BufferedReader in;
        private Handler toClientHandler;
        public NetConn(Handler myHandler){
            this.toClientHandler = myHandler;
        }
        @Override
        public void run(){
            try{
                socket = new Socket();

                socket.connect(new InetSocketAddress
                        ("10.0.2.2",9999),5000);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
                writer = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(
                                socket.getOutputStream(),"utf-8")),true);
                Log.i(TAG,"connect to server");

                //接收服务器返回的数据
                Thread receiveServerMsg =  new Thread(){
                    @Override
                    public void run(){
                        String fromserver = null;
                        try{
                            while((fromserver = in.readLine())!=null)
                            {
                                //发送消息给UI线程
                                Message msg = new Message();
                                msg.what = 1;
                                msg.obj = fromserver;
                                toClientHandler.sendMessage(msg);
                            }
                        }catch (IOException ex){
                            ex.printStackTrace();
                        }
                    }
                };
                receiveServerMsg.start();
            }catch(UnknownHostException ex){
                ex.printStackTrace();
            }catch(IOException ex){
                ex.printStackTrace();
            }
        }
    }
}
