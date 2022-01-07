package com.example;
import java.sql.Date;
import java.util.ArrayList;
public class MonthlyList {

    // PROPERTIES
    private ArrayList<Monthly> records = new ArrayList<Monthly>();
    private String clientName;

    public void setRecords(ArrayList<Monthly> records) {
        this.records = records;
    }
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public ArrayList<Monthly> getRecords() {
        return this.records;
    }
    public String getClientName() {
        return this.clientName;
    }

}
