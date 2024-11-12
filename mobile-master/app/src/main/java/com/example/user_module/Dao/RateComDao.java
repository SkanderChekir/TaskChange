package com.example.user_module.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.user_module.entity.RateCom;

import java.util.List;

@Dao
public interface RateComDao {
    @Insert
    void insert(RateCom rateCom);

    @Update
    void update(RateCom rateCom);

    @Delete
    void delete(RateCom rateCom);

    @Query("SELECT * FROM RateCom")
    List<RateCom> getAllRateCom();

    @Query("SELECT * FROM RateCom WHERE id = :id")
    RateCom getRateComById(int id);
}
