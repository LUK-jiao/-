package com.example.aircraftwar2024.music;

import android.media.MediaPlayer;


import android.content.Context;
import android.media.MediaPlayer;

import com.example.aircraftwar2024.R;


import android.content.Context;
import android.media.MediaPlayer;

public class MyMediaPlayer {
    public static boolean music = true;
    private MediaPlayer bgMP;  // 用于播放背景音乐
    private MediaPlayer bossMP;  // 用于播放boss音乐
    private Context context;
    private int bgmPosition = 0;  // 用于存储bgm暂停时的位置

    public MyMediaPlayer(Context context) {
        this.context = context;
    }

    public MyMediaPlayer(){}

    // 初始化背景音乐播放器
    public void initializeBgPlayer() {
        if (bgMP == null) {
            bgMP = MediaPlayer.create(context,R.raw.bgm);
            bgMP.setLooping(true);  // 设置循环播放
        }
    }

    // 初始化boss音乐播放器
    public void initializeBossPlayer() {
        if (bossMP == null) {
            bossMP = MediaPlayer.create(context, R.raw.bgm_boss);
            bossMP.setLooping(true);  // 设置循环播放
        }
    }

    // 播放背景音乐
    public void playBgMusic() {
        if (bgMP != null && !bgMP.isPlaying() && music) {
            bgMP.seekTo(bgmPosition);  // 从上次暂停的位置继续播放
            bgMP.start();
        }
    }

    // 播放boss音乐
    public void playBossMusic() {
        if (bossMP != null && !bossMP.isPlaying() && music) {
            bossMP.start();
        }
    }

    // 暂停背景音乐
    public void pauseBgMusic() {
        if (bgMP != null && bgMP.isPlaying() && music) {
            bgmPosition = bgMP.getCurrentPosition();  // 记录暂停的位置
            bgMP.pause();
        }
    }

    // 暂停boss音乐
    public void pauseBossMusic() {
        if (bossMP != null && bossMP.isPlaying() && music) {
            bossMP.pause();
        }
    }

    // 停止背景音乐
    public void stopBgMusic() {
        if (bgMP != null  && music) {
            bgMP.stop();
            bgMP.release();
            bgMP = null;
        }
    }

    // 停止boss音乐
    public void stopBossMusic() {
        if (bossMP != null && music) {
            bossMP.stop();
            bossMP.release();
            bossMP = null;
        }
    }

    // 释放资源
    public void release() {
        if (bgMP != null) {
            bgMP.release();
            bgMP = null;
        }
        if (bossMP != null) {
            bossMP.release();
            bossMP = null;
        }
    }

    public static void main(String[] args){
        MyMediaPlayer myMediaPlayer = new MyMediaPlayer();
        myMediaPlayer.initializeBgPlayer();
        myMediaPlayer.playBgMusic();
    }
}
