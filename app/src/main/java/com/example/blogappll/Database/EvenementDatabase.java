package com.example.blogappll.Database;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.blogappll.Dao.EvenementDao;
import com.example.blogappll.Entity.Evenement;

@Database(entities = {Evenement.class}, version = 1)
public  abstract class EvenementDatabase extends  RoomDatabase  {
    public abstract EvenementDao EvenementDao();
}
