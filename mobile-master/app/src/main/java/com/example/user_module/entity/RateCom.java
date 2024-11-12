package com.example.user_module.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class RateCom implements Serializable {  // Implement Serializable
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String rate;
    public String commentaire;
    public String author;
    public String imageUri;
}
