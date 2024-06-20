package com.example.aircraftwar2024.activity;

import androidx.appcompat.app.AlertDialog;
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
import com.example.aircraftwar2024.game.HardGame;
import com.example.aircraftwar2024.music.MyMediaPlayer;
import com.example.aircraftwar2024.music.MySoundPool;

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
        //设置匹配中对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("匹配中");
        builder.setMessage("正在匹配");
        builder.setCancelable(false);
        AlertDialog matchingDialog = builder.create();

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
                if(msg.what == 3){
                    //TODO:跳转到联机游戏结算画面
                    Intent intent = new Intent(MainActivity.this, OnlineEndActivity.class);
                    startActivity(intent);
                }
            }
        };
        this.onlinehandler = new Handler(getMainLooper()){
            //当数据处理子线程更新数据后发送消息给UI线程，UI线程更新UI
            @Override
            public void handleMessage(Message msg){
                String[] parts = msg.obj.toString().split(",");
                //标志游戏匹配成功，开始游戏
                if(msg.what == 0 && parts[0].equals("match success")){
                    matchingDialog.dismiss();
                    HardGame game = new HardGame(MainActivity.this, mHandler);
                    setContentView(game);
                    // 如果开启游戏，那么就新开一个线程给服务端发送当前分数
                    // 如果当前玩家已经死亡，那么就给服务器传"end"信息
                    new Thread(() -> {
                        //发送当前分数
                        while (!game.isGameOverFlag()) {

                            writer.println("score,"+game.getScore());
                            Log.i(TAG,"send to server: score,"+game.getScore());
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        //死亡后发送结束标志
                        writer.println("end");
                    }).start();
                }
                if(msg.what == 0 && parts[0].equals("score")){
                    int e_score = Integer.parseInt(parts[1]);
                    BaseGame.setE_score(e_score);
                }
                if(msg.what == 0 && parts[0].equals("end")){
                    BaseGame.setOp_gameOverFlag(true);
                }

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
                //显示“匹配中”对话框
                matchingDialog.show();
                new Thread(new NetConn(onlinehandler)).start();

                BaseGame.setisOnline(true);

                // 获取选中的 RadioButton 的 ID 和文本
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(selectedId);
                String option = radioButton.getText().toString();
                if (option.equals("开启音乐")) {
                    MyMediaPlayer.music = true;
                    MySoundPool.music = true;
                } else {
                    MyMediaPlayer.music = false;
                    MySoundPool.music = false;
                }

            }

//            @Override
//            public void onClick(View view) {
//                // 创建一个 Handler 用于在主线程上执行操作
//                Handler mainHandler = new Handler(Looper.getMainLooper());
//
//                // 显示“匹配中”对话框
//                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                builder.setTitle("匹配中");
//                builder.setMessage("正在匹配");
//                builder.setCancelable(false);
//                AlertDialog matchingDialog = builder.create();
//                matchingDialog.show();
//
//
////                 启动连接服务器的线程
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        while (true) {
//                            try {
//                                // 尝试连接服务器
//                                new NetConn(onlinehandler).run();
//                                Log.i(TAG, "send message to server");
//                                writer.println("hello,server");
//
//                                // 连接成功，更新 BaseGame.isOnline 的值
//                                BaseGame.setisOnline(true);
//
//                                // 获取选中的 RadioButton 的 ID 和文本
//                                int selectedId = radioGroup.getCheckedRadioButtonId();
//                                RadioButton radioButton = (RadioButton) findViewById(selectedId);
//                                String option = radioButton.getText().toString();
//                                if (option.equals("开启音乐")) {
//                                    MyMediaPlayer.music = true;
//                                    MySoundPool.music = true;
//                                } else {
//                                    MyMediaPlayer.music = false;
//                                    MySoundPool.music = false;
//                                }
//
//                                // 关闭“匹配中”对话框并进入游戏
//                                mainHandler.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        matchingDialog.dismiss();
//                                        Intent intent = new Intent(MainActivity.this, GameActivity.class);
//                                        intent.putExtra("gameType", 3);
//                                        startActivity(intent);
//                                    }
//                                });
//                                break; // 退出循环
//                            } catch (Exception e) {
//                                // 捕获连接异常并继续尝试连接
//                                Log.e(TAG, "连接失败，重试中...");
//                                try {
//                                    Thread.sleep(200); // 休眠0.2秒后重试
//                                } catch (InterruptedException ie) {
//                                    Thread.currentThread().interrupt();
//                                }
//                            }
//                        }
//                    }
//                }).start();
//                new Thread(new NetConn(onlinehandler)).start();
//            }
        });
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
                                msg.what = 0;
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
