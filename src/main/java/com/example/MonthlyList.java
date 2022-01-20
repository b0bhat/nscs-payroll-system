package com.example;
import java.sql.Date;
import java.util.ArrayList;
public class MonthlyList {

    // PROPERTIES
    private ArrayList<Monthly> records = new ArrayList<Monthly>();
    private ArrayList<Monthly> totals = new ArrayList<Monthly>();
    private String clientName;
    private float totalHours;

    public void setRecords(ArrayList<Monthly> records) {
        this.records = records;
    }
    public void setTotals(ArrayList<Monthly> totals) {
        this.totals = totals;
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
    public ArrayList<Monthly> getTotals() {
        return this.totals;
    }
    public String getClientName() {
        return this.clientName;
    }
    public float getTotalHours() {
        return this.totalHours;
    }

}
