package com.example.aircraftwar2024.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.aircraftwar2024.Data.GameDAOimpl;
import com.example.aircraftwar2024.Data.Item;
import com.example.aircraftwar2024.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordActivity extends AppCompatActivity {
    public static int gameType;

    private GameDAOimpl gameDAOimpl;

    private List<Item> items;

    ArrayList<Map<String, Object>> listitem = new ArrayList<Map<String, Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        //获得Layout里面的ListView
        ListView list = (ListView) findViewById(R.id.ListView01);
        TextView title = (TextView) findViewById(R.id.mode);
        switch (gameType){
            case 1:title.setText("简单模式");
            break;
            case 2:title.setText("普通模式");
            break;
            case 3:title.setText("困难模式");
            break;
        }

        gameDAOimpl = new GameDAOimpl(this);
        //生成适配器的Item和动态数组对应的元素
        SimpleAdapter listItemAdapter = new SimpleAdapter(
                this,
                getData(),
                R.layout.activity_item,
                new String[]{"排名","用户","得分","时间"},
                new int[]{R.id.排名,R.id.用户,R.id.得分,R.id.时间});

        //添加并且显示
        list.setAdapter(listItemAdapter);

        //删除记录功能
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {

                AlertDialog alertDialog = new AlertDialog.Builder(RecordActivity.this)
                        .setTitle("提示")
                        .setMessage("确认删除该条记录吗")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
//                                listitem.remove(position);

                                //更新排行榜文件
                                items.remove(position);
                                gameDAOimpl.storeAllItem();
                                listitem = getData();

                                listItemAdapter.notifyDataSetChanged();
                            }
                        })

                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .create();
                alertDialog.show();
            }
        });


        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecordActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private ArrayList<Map<String, Object>> getData() {

        //TODO 改一下原来的代码
        listitem.clear();
        gameDAOimpl.getAllItem();
        items = gameDAOimpl.getItems();
        int rank = 0;
        for(Item item:items){
            rank++;
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("排名",rank);
            map.put("用户",item.getName());
            map.put("得分",item.getScore());
            map.put("时间",item.getDate_string());
            listitem.add(map);
        }
        return listitem;
    }
}