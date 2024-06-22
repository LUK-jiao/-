package com.example.aircraftwar2024.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.aircraftwar2024.R;

public class OnlineEndActivity extends AppCompatActivity {
    private static int score1;
    private static int score2;

    public static void setScore1(int score1) {
        OnlineEndActivity.score1 = score1;
    }

    public static void setScore2(int score2) {
        OnlineEndActivity.score2 = score2;
    }

    private Handler mHandler = MainActivity.getInstance().getHandler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_end);
        TextView t_score1 = (TextView)findViewById(R.id.score1);
        TextView t_score2 = (TextView)findViewById(R.id.score2);
        t_score1.setText("你的分数： "+score1);
        t_score2.setText("对手分数： "+score2);

        Button btn = (Button) findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = Message.obtain();
                msg.what = 2; //消息的标识
                msg.obj = "A"; // 消息的存放
                // b. 通过Handler发送消息到其绑定的消息队列
                mHandler.sendMessage(msg);
            }
        });
    }


}