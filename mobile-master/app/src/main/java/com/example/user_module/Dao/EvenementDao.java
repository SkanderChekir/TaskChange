package com.example.user_module.Dao;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.user_module.entity.Evenement;

import java.util.List;
@Dao
public interface EvenementDao {

    @Insert
    void insert(Evenement Evenement);

    @Update
    void update(Evenement Evenement);

    @Delete
    void delete(Evenement Evenement);

    @Query("SELECT * FROM Evenement")
    List<Evenement> getAllEvenements();

    @Query("SELECT * FROM Evenement WHERE id = :id LIMIT 1")
    Evenement getEvenementById(int id);

}
