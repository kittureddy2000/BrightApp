package com.brightapps.all.mDatabase;

/**
 * Created by kyadamakanti on 12/29/2017.
 */

public class TodoObject {

    private int row_id;
    private String title;
    private String Description;
    private String reminder_Date;
    private int snooze_Amount;
    private String Proirity;
    private String category_name;
    private int recurrence;
    private String location;
    private int task_Completion_status;
    private String creation_date;
    private String update_Date;

    public int getRow_id() {
        return row_id;
    }

    public void setRow_id(int row_id) {
        this.row_id = row_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getReminder_Date() {
        return reminder_Date;
    }

    public void setReminder_Date(String reminder_Date) {
        this.reminder_Date = reminder_Date;
    }

    public int getSnooze_Amount() {
        return snooze_Amount;
    }

    public void setSnooze_Amount(int snooze_Amount) {
        this.snooze_Amount = snooze_Amount;
    }

    public String getProirity() {
        return Proirity;
    }

    public void setProirity(String proirity) {
        Proirity = proirity;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public int getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(int recurrence) {
        this.recurrence = recurrence;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getTask_Completion_status() {
        return task_Completion_status;
    }

    public void setTask_Completion_status(int task_Completion_status) {
        this.task_Completion_status = task_Completion_status;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public String getUpdate_Date() {
        return update_Date;
    }

    public void setUpdate_Date(String update_Date) {
        this.update_Date = update_Date;
    }


    public TodoObject() {
    }
}
