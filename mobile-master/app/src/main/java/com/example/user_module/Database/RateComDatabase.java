package com.example.user_module.Database;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.user_module.Dao.RateComDao;
import com.example.user_module.entity.RateCom;

@Database(entities = {RateCom.class}, version = 4)
public abstract class RateComDatabase extends  RoomDatabase  {
    public abstract RateComDao rateComDao();
}
