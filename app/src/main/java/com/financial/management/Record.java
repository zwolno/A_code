package com.financial.management;

public class Record {
    private String type;
    private String date;
    private float money;

    public Record(String type, String date, float money) {
        this.type = type;
        this.date = date;
        this.money = money;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public float getMoney() {
        return money;
    }
}
