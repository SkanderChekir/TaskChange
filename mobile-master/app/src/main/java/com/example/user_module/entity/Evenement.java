package com.example.user_module.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Evenement implements Serializable {  // Implement Serializable
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public String description;
    public String lieu;
    public String imageUri;  // Store the URI of the image in the database
}
