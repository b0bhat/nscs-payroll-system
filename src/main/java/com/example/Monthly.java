package com.example;
import java.sql.Date;
public class Monthly {

    // PROPERTIES
    private String employeeName;
    private float workHours;
    private Date workDate;

    public void setWorkHours(float workHours) {
        this.workHours = workHours;
    }
    public void setWorkDate(Date workDate) {
        this.workDate = workDate;
    }
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public float getWorkHours() {
        return this.workHours;
    }
    public Date getWorkDate() {
        return this.workDate;
    }
    public String getEmployeeName() {
        return this.employeeName;
    }

}
