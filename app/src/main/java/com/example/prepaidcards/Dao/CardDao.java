package com.example.prepaidcards.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.prepaidcards.Entity.Card;

import java.util.List;

@Dao
public interface CardDao {

    @Insert
    void insert(Card card);

    @Update
    void update(Card card);

    @Delete
    void delete(Card card);

    @Query("SELECT * FROM Card")
    List<Card> getAllCards();

    @Query("SELECT * FROM Card WHERE id = :id")
    Card getCardById(int id);
}

