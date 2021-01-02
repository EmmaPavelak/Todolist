package com.ynov.todolist;

import java.io.Serializable;
import java.util.HashMap;

public class Taches implements Serializable {

    String name;
    String message;
    String date;
    String idtache;
    public Taches() {

    }
    public Taches(String name, String message, String date, String idtache){
        this.name=name;
        this.message=message;
        this.date=date;
        this.idtache=idtache;
    }

    public String getIdtache() { return idtache; }
    public void setIdtache(String idtache) { this.idtache = idtache; }
    public String getDate() { return date; }
    public void setDate(String date) {
        this.date = date;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

}
