package com.example.aircraftwar2024.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Item {
    private String name;
    private int score;
    private Date date;

    private String date_string;

    public String getDate_string() {
        return date_string;
    }

    public void setDate_string(Date date) {
        String pattern = "MM-dd HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        this.date_string = simpleDateFormat.format(date);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public Item(String name, int score, Date date){
        this.name = name;
        this.score = score;
        this.date = date;
        setDate_string(date);
    }
}
