package com.example;
import java.sql.Date;
public class Record {

    // PROPERTIES
    private String recordID;
    private String clientName;
    private float workHours;
    private String workType;
    private Date workDate;
    private String employeeName;
    private String notes;

    public void setRecordID(String recordID) {
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
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getRecordID() {
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
    public String getNotes() {
        return this.notes;
    }

}
