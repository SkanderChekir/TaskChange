package com.example.user_module.Database;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.user_module.Dao.EvenementDao;
import com.example.user_module.entity.Evenement;

@Database(entities = {Evenement.class}, version = 1)
public  abstract class EvenementDatabase extends  RoomDatabase  {
    public abstract EvenementDao EvenementDao();
}
