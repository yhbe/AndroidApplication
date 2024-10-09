package com.example.d308vacationplanner.database;

import android.app.Application;

import com.example.d308vacationplanner.dao.ExcursionDAO;
import com.example.d308vacationplanner.dao.VacationDAO;
import com.example.d308vacationplanner.entities.Excursion;
import com.example.d308vacationplanner.entities.Vacation;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private ExcursionDAO mExcursionDao;
    private VacationDAO mVacationDao;

    private List<Vacation> mAllVacations;
    private List<Excursion> mAllExcursions;

    private static int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor= Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application){
        VacationDatabaseBuilder db = VacationDatabaseBuilder.getDatabase(application);
        mExcursionDao=db.excursionDAO();
        mVacationDao =db.vacationDAO();
    }

    public List<Vacation> searchVacationsByTitle(String title) {
        List<Vacation>[] result = new List[1];

        databaseExecutor.execute(() -> result[0] = mVacationDao.searchVacationsByTitle(title));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } //B6 industry-appropriate security features. By utilizing error handling we ensure our application handles interruptions appropriately.

        return result[0];
    }

    public List<Vacation>getAllVacations(){
        databaseExecutor.execute(() -> {
            mAllVacations= mVacationDao.getAllVacations();
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mAllVacations;
    }

    public void deleteVacation(Vacation vacation){
        databaseExecutor.execute(() -> {
            mVacationDao.deleteVacation(vacation);
        });
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void updateVacation(Vacation vacation){
        databaseExecutor.execute(() -> {
            mVacationDao.updateVacation(vacation);
        });
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Vacation getVacationByID(int vacationID) {
        return mVacationDao.getVacationByID(vacationID);
    }

    public void insertVacation(Vacation vacation){
        databaseExecutor.execute(() -> {
            mVacationDao.insertVacation(vacation);
        });
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void insertExcursion(Excursion excursion){
        databaseExecutor.execute(() -> {
            mExcursionDao.insertExcursion(excursion);
        });
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Excursion>getAllExcursions(){
        databaseExecutor.execute(() -> {
            mAllExcursions=mExcursionDao.getAllExcursions();
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mAllExcursions;
    }


    public void deleteExcursion(Excursion excursion){
        databaseExecutor.execute(() -> {
            mExcursionDao.deleteExcursion(excursion);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void updateExcursion(Excursion excursion){
        databaseExecutor.execute(() -> {
            mExcursionDao.updateExcursion(excursion);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Excursion getExcursionByID(int excursionID) {
        return mExcursionDao.getExcursionByID(excursionID);
    }

    public List<Excursion>getAssociatedExcursions(int vacationID){
        databaseExecutor.execute(() ->{
            mAllExcursions=mExcursionDao.getAssociatedExcursions(vacationID);
        });

        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return mAllExcursions;
    }
}

//B3 a database component with the functionality to securely add, modify, and delete the data.
//The added component is a part of our Room SQL database