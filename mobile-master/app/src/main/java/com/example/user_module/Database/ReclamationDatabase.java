package com.example.user_module.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.user_module.Dao.ReclamationDao;
import com.example.user_module.entity.Reclamation;

@Database(entities = {Reclamation.class}, version = 1)
public abstract class ReclamationDatabase extends RoomDatabase {
    public abstract ReclamationDao reclamationDao();
}
