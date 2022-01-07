package com.example;
import java.sql.Date;
import java.util.ArrayList;
public class MonthlyList {

    // PROPERTIES
    private ArrayList<Monthly> record = new ArrayList<Monthly>();
    private String clientName;

    public void setMonthly(ArrayList<Monthly> record) {
        this.record = record;
    }
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public ArrayList<Monthly> getMonthly() {
        return this.record;
    }
    public String getClientName() {
        return this.clientName;
    }

}
