package com.example.aircraftwar2024.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.aircraftwar2024.R;
import com.example.aircraftwar2024.game.BaseGame;
import com.example.aircraftwar2024.music.MyMediaPlayer;
import com.example.aircraftwar2024.music.MySoundPool;

public class OffilineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offiline);
        String str = getIntent().getStringExtra("Music");
        Button btn1 = (Button) findViewById(R.id.btn1);
        Button btn2 = (Button) findViewById(R.id.btn2);
        Button btn3 = (Button) findViewById(R.id.btn3);
        if(str.equals("开启音乐")){
            MyMediaPlayer.music = true;
            MySoundPool.music = true;
        }else {
            MyMediaPlayer.music = false;
            MySoundPool.music = false;
        }
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OffilineActivity.this,GameActivity.class);
                intent.putExtra("gameType", 1);
                RecordActivity.gameType = 1;
                startActivity(intent);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OffilineActivity.this,GameActivity.class);
                intent.putExtra("gameType", 2);
                RecordActivity.gameType = 2;
                startActivity(intent);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OffilineActivity.this,GameActivity.class);
                intent.putExtra("gameType", 3);
                RecordActivity.gameType = 3;
                startActivity(intent);
            }
        });
    }
}