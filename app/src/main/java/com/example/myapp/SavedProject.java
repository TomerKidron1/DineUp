package com.example.myapp;

public class SavedProject {
    private String project_name;
    private String Number_of_people;
    private String Category;
    private String Date;


    public SavedProject(String project_name, String number_of_people, String category, String date) {
        this.project_name = project_name;
        Number_of_people = number_of_people;
        Category = category;
        Date = date;
    }

    public SavedProject(Object value) {
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getNumber_of_people() {
        return Number_of_people;
    }

    public void setNumber_of_people(String number_of_people) {
        Number_of_people = number_of_people;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getDate() {
        return this.Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
