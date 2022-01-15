package com.example;
import java.sql.Date;
import java.util.ArrayList;
public class MonthlyList {

    // PROPERTIES
    private ArrayList<Monthly> records = new ArrayList<Monthly>();
    private String clientName;
    private float totalHours;

    public void setRecords(ArrayList<Monthly> records) {
        this.records = records;
    }
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
    public void setTotalHours(float totalHours) {
        this.totalHours = totalHours;
    }

    public ArrayList<Monthly> getRecords() {
        return this.records;
    }
    public String getClientName() {
        return this.clientName;
    }
    public float getTotalHours() {
        return this.totalHours;
    }

}
