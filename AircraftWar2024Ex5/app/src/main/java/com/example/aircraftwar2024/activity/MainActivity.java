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
import com.example.aircraftwar2024.music.MyMediaPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private static MainActivity instance;
    public Handler mHandler;

    private int layoutId = R.layout.activity_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId);
        instance = this;
        Button btn = (Button) findViewById(R.id.btn);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        };
    }

    public static MainActivity getInstance() {
        return instance;
    }

    public Handler getHandler() {
        return mHandler;
    }
}
