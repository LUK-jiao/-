package com.example.aircraftwar2024.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.example.aircraftwar2024.R;

public class OnlineEndActivity extends AppCompatActivity {
    private Handler mHandler = MainActivity.getInstance().getHandler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_end);
        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(RecordActivity.this,MainActivity.class);
//                startActivity(intent);
                Message msg = Message.obtain();
                msg.what = 2; //消息的标识
                msg.obj = "A"; // 消息的存放
                // b. 通过Handler发送消息到其绑定的消息队列
                mHandler.sendMessage(msg);
            }
        });
    }


}