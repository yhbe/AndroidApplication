package com.example.d308vacationplanner.entities;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "excursions")
public class Excursion {
    @PrimaryKey(autoGenerate = true)
    private int excursionID;
    private String excursionTitle;
    private String excursionDate;
    private int vacationID;


    public Excursion(int excursionID, String excursionTitle, String excursionDate, int vacationID) {
        this.excursionID = excursionID;
        this.excursionTitle = excursionTitle;
        this.excursionDate = excursionDate;
        this.vacationID = vacationID;
    }

    public int getExcursionID() {
        return excursionID;
    }

    public void setExcursionID(int excursionID) {
        this.excursionID = excursionID;
    }

    public String getExcursionTitle() {
        return excursionTitle;
    }

    public void setExcursionTitle(String excursionTitle) {
        this.excursionTitle = excursionTitle;
    }

    public String getExcursionDate() {
        return excursionDate;
    }

    public void setExcursionDate(String excursionDate) {
        this.excursionDate = excursionDate;
    }

    public int getVacationID() {
        return vacationID;
    }

    public void setVacationID(int vacationID) {
        this.vacationID = vacationID;
    }
}

//This code shows encapsulation because the fields marked as, "private" like excursionID, mean that they cannot be accessed from outside the class. Thus they are encapsulated.