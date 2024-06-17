package com.example.aircraftwar2024.Data;

import android.content.Context;
import android.icu.text.AlphabeticIndex;

import com.example.aircraftwar2024.activity.RecordActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GameDAOimpl {

    public static final String FILE_PATH_easy = "easy.txt";
    public static final String FILE_PATH_medium = "medium.txt";
    public static final String FILE_PATH_hard = "hard.txt";

    public static String FILE_PATH_mode;

    private Context context;
    private List<Item> items = new ArrayList<>();

    public GameDAOimpl(Context context){
        switch (RecordActivity.gameType){
            case 1:FILE_PATH_mode = FILE_PATH_easy;
                break;
            case 2:FILE_PATH_mode = FILE_PATH_medium;
                break;
            case 3:FILE_PATH_mode = FILE_PATH_hard;
                break;
            default:FILE_PATH_mode = FILE_PATH_easy;
                break;
        }
        this.context = context;
    }

    public List<Item> getItems(){
        return items;
    }
    //TODO add（String time，int score，int mode）游戏结束后自动修改文件
    public void doAddandsort(Item item) {
        items.add(item);
        Collections.sort(items, new Comparator<Item>() {
            @Override
            public int compare(Item o1,Item o2) {
                return o2.getScore() - o1.getScore();
            }
        });
        storeAllItem();
    }


    //TODO delete（）修改文件，删除记录
    public void doDelete(int row) {
        items.remove(row);
        storeAllItem();
    }

    public void storeAllItem(){
        //将所有记录存储到文件中
        try (FileOutputStream fos = context.openFileOutput(FILE_PATH_mode,Context.MODE_PRIVATE)) {
            for (Item item: items) {
                fos.write((item.getName() + "," + item.getScore() +','+item.getDate_string()+" \n").getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getAllItem(){
        //从文件中读取所有的记录
        try (
//                FileOutputStream fos = context.openFileOutput(FILE_PATH_mode,Context.MODE_PRIVATE);
                FileInputStream fis = context.openFileInput(FILE_PATH_mode);
             )
        { InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
            String line;
            items.clear();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                Item item = new Item(parts[0],Integer.parseInt(parts[1]),simpleDateFormat.parse(parts[2]));
                items.add(item);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
