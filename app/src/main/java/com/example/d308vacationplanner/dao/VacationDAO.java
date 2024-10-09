package com.example.d308vacationplanner.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.d308vacationplanner.entities.Vacation;

import java.util.List;

@Dao
public interface VacationDAO{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertVacation(Vacation vacation);

    @Update
    void updateVacation(Vacation vacation);

    @Delete
    void deleteVacation(Vacation vacation);

    @Query("SELECT * FROM VACATIONS ORDER BY vacationID ASC")
    List<Vacation> getAllVacations();

    @Query("SELECT * FROM vacations WHERE vacationId = :vacationID")
    Vacation getVacationByID(int vacationID);

    @Query("SELECT * FROM vacations WHERE vacationTitle LIKE '%' || :title || '%'")
    List<Vacation> searchVacationsByTitle(String title);
}

//B6 industry-appropriate security features. By utilizing parameterized queries we are preventing SQL injection.