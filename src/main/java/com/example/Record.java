package com.example;
import java.sql.Date;
public class Record {

    // PROPERTIES
    private Integer recordID;
    private String clientName;
    private float workHours;
    private String workType;
    private Date workDate;
    private String employeeName;

    public void setRecordID(Integer recordID) {
        this.recordID = recordID;
    }
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
    public void setWorkHours(float workHours) {
        this.workHours = workHours;
    }
    public void setWorkType(String workType) {
        this.workType = workType;
    }
    public void setWorkDate(Date workDate) {
        this.workDate = workDate;
    }
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Integer getRecordID() {
        return this.recordID;
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
    public Date getWorkDate() {
        return this.workDate;
    }
    public String getEmployeeName() {
        return this.employeeName;
    }

}
