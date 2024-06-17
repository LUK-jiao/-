package com.example.aircraftwar2024.music;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

import com.example.aircraftwar2024.R;

import java.util.HashMap;

public class MySoundPool {

    public static boolean music = true;
    private SoundPool soundPool;
    private HashMap<Integer, Integer> soundPoolMap;

    public static final int bomb_explosion = 1;
    public static final int bullet_hit = 2;
    public static final int game_over = 3;
    public static final int get_supply = 4;

    public MySoundPool(Context context) {
        // 创建SoundPool对象
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(10)
                .setAudioAttributes(audioAttributes)
                .build();

        // 加载音频资源
        soundPoolMap = new HashMap<>();
        soundPoolMap.put(bomb_explosion, soundPool.load(context, R.raw.bomb_explosion, 1));
        soundPoolMap.put(bullet_hit, soundPool.load(context, R.raw.bullet_hit, 1));
        soundPoolMap.put(game_over, soundPool.load(context, R.raw.game_over, 1));
        soundPoolMap.put(get_supply, soundPool.load(context, R.raw.get_supply, 1));
    }

    // 播放音频
    public void play(int soundID) {
        if (music) {
            soundPool.play(soundPoolMap.get(soundID), 1.0f, 1.0f, 1, 0, 1.0f);
        }
    }

    // 释放资源
    public void release() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
        if (soundPoolMap != null) {
            soundPoolMap.clear();
        }
    }
}
