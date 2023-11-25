package com.example.noteapp.Model;

import java.io.Serializable;

public class Folder implements Serializable {
    private int id;
    private String name;
    private String statusG;

    public Folder(){

    }
    public Folder(int id, String name, String statusG) {
        this.id = id;
        this.name = name;
        this.statusG = statusG;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatusG() {
        return statusG;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatusG(String statusG) {
        this.statusG = statusG;
    }
}
