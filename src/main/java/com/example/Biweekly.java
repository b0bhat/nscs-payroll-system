package com.example;
import java.sql.Date;
public class Biweekly {

    // PROPERTIES
    private String clientName;
    private float workHours;
    private String workType;
    private String employeeName;
    private float workDays;

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
    public void setWorkHours(float workHours) {
        this.workHours = workHours;
    }
    public void setWorkType(String workType) {
        this.workType = workType;
    }
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
    public void setWorkDays(float workDays) {
        this.workDays = workDays;
    }

    public String getClientName() {
        return this.clientName;
    }
    public float getWorkHours() {
        return this.workHours;
    }
    public String getWorkType() {
        return this.workType;
    }
    public String getEmployeeName() {
        return this.employeeName;
    }
    public float getWorkDays() {
        return this.workDays;
    }

}
