package com.example.blogappll.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Reclamation implements Serializable {  // Implemente Serializable
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String description;
    public String status;
    public String type;
}
