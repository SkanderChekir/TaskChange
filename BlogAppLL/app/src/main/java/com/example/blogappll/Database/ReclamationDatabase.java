package com.example.blogappll.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.blogappll.Dao.ReclamationDao;
import com.example.blogappll.Entity.Reclamation;

@Database(entities = {Reclamation.class}, version = 1)
public abstract class ReclamationDatabase extends RoomDatabase {
    public abstract ReclamationDao reclamationDao();
}
