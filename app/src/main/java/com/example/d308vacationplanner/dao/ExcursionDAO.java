package com.example.d308vacationplanner.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.d308vacationplanner.entities.Excursion;

import java.util.List;

@Dao
public interface ExcursionDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertExcursion(Excursion excursion);

    @Update
    void updateExcursion(Excursion excursion);


    @Delete
    void deleteExcursion(Excursion excursion);

    @Query("SELECT * FROM excursions ORDER BY excursionID ASC")
    List<Excursion> getAllExcursions();

    @Query("SELECT * FROM excursions WHERE excursionID=:prod ORDER BY excursionID ASC")
    List<Excursion> getAssociatedExcursions(int prod);

    @Query("SELECT * FROM excursions WHERE excursionID=:excursionID")
    Excursion getExcursionByID(int excursionID);
}
