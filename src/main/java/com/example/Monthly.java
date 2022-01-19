package com.example;
import java.sql.Date;
public class Monthly {

    // PROPERTIES
    private String employeeName;
    private float workHours;
    private Date workDate;
    private String workType;

    public void setWorkHours(float workHours) {
        this.workHours = workHours;
    }
    public void setWorkDate(Date workDate) {
        this.workDate = workDate;
    }
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
    public void setWorkType(String workType) {
        this.workType = workType;
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
    public String getWorkType() {
        return this.workType;
    }

}
