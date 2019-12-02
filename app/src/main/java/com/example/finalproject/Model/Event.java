package com.example.finalproject.Model;

public class Event {
    private int id;
    private String name;
    private String description;
    private String entry_date;
    private String start_date;
    private String end_date;

    public Event(int id, String name, String description, String entry_date, String start_date, String end_date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.entry_date = entry_date;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public Event(String name, String description, String entry_date, String start_date, String end_date) {
        this.name = name;
        this.description = description;
        this.entry_date = entry_date;
        this.start_date = start_date;
        this.end_date = end_date;
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

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }
}
