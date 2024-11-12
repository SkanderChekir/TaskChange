package com.example.prepaidcards.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.prepaidcards.Dao.CardDao;
import com.example.prepaidcards.Entity.Card;

@Database(entities = {Card.class}, version = 6)
public  abstract class CardDatabase extends  RoomDatabase  {

    private static CardDatabase instance;

    public abstract CardDao cardDao();

    public static synchronized CardDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            CardDatabase.class, "card_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}