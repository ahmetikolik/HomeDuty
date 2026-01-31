package com.homeduty.model;

import java.sql.Date;

public class DutyViewRow {
    public int dutyId;
    public String family;
    public String task;
    public String assignedTo;
    public Date dueDate;
    public String status;

    public DutyViewRow(int dutyId, String family, String task, String assignedTo, Date dueDate, String status) {
        this.dutyId = dutyId;
        this.family = family;
        this.task = task;
        this.assignedTo = assignedTo;
        this.dueDate = dueDate;
        this.status = status;
    }
}
