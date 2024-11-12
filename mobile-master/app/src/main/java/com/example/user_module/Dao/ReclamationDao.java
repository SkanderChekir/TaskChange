package com.example.user_module.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.user_module.entity.Reclamation;

import java.util.List;

@Dao
public interface ReclamationDao {

    @Insert
    void insert(Reclamation reclamation);

    @Update
    void update(Reclamation reclamation);

    @Delete
    void delete(Reclamation reclamation);

    @Query("SELECT * FROM Reclamation")
    List<Reclamation> getAllReclamations();

    @Query("SELECT * FROM Reclamation WHERE id = :id LIMIT 1")
    Reclamation getReclamationById(int id);

}
