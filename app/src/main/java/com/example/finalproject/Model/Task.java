package com.example.finalproject.Model;

public class Task {
    private int id;
    private String name;
    private String description;
    private String entry_date;
    private String end_date;
    private int stepid;

    public Task(int id, String name, String description, String entry_date, String end_date, int stepid) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.entry_date = entry_date;
        this.end_date = end_date;
        this.stepid = stepid;
    }

    public Task(String name, String description, String entry_date, String end_date, int stepid) {
        this.name = name;
        this.description = description;
        this.entry_date = entry_date;
        this.end_date = end_date;
        this.stepid = stepid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEntry_date() {
        return entry_date;
    }

    public void setEntry_date(String entry_date) {
        this.entry_date = entry_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public int getStepid() {
        return stepid;
    }

    public void setStepid(int stepid) {
        this.stepid = stepid;
    }
}
