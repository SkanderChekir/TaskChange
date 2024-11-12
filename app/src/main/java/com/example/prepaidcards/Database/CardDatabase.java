package com.example.prepaidcards.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.prepaidcards.Dao.CardDao;
import com.example.prepaidcards.Entity.Card;

@Database(entities = {Card.class}, version = 5)
public  abstract class CardDatabase extends  RoomDatabase  {
    public abstract CardDao cardDao();
}