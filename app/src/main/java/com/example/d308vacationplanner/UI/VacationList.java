package com.example.d308vacationplanner.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308vacationplanner.R;
import com.example.d308vacationplanner.database.Repository;
import com.example.d308vacationplanner.entities.Excursion;
import com.example.d308vacationplanner.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class VacationList extends AppCompatActivity {
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_list);
        FloatingActionButton fab=findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VacationList.this, VacationDetails.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        repository = new Repository(getApplication());
        List<Vacation> allVacations=repository.getAllVacations();
        final VacationAdapter vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(allVacations);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);
        return true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        List<Vacation> allVacations=repository.getAllVacations();
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final VacationAdapter vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(allVacations);
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.sample){

            List<Vacation> vacations = repository.getAllVacations();
            int vacationID;

            if (vacations.isEmpty()) {
               vacationID = 1;
            } else {
                vacationID = vacations.get(vacations.size() - 1).getVacationID() + 1;
            }

            Vacation vacation = new Vacation(vacationID, "London Trip", "London City Hotel", "09/01/24", "09/15/24 ");
            repository.insertVacation(vacation);

            Excursion excursion = new Excursion(0, "Bus Tour", "09/28/24", vacationID);
            repository.insertExcursion(excursion);

            excursion = new Excursion(0, "Cooking Lesson", "09/30/24", vacationID);
            repository.insertExcursion(excursion);

            vacations = repository.getAllVacations();
            vacationID =  vacations.get(vacations.size() - 1).getVacationID() + 1;
            vacation = new Vacation(vacationID, "New York Trip", "Plaza Hotel", "09/28/24", "10/13/24 ");
            repository.insertVacation(vacation);

            onResume();
            Toast.makeText(VacationList.this, "New Sample Vacation & Excursion data added!", Toast.LENGTH_LONG).show();
            return true;
        }
       if(item.getItemId() == android.R.id.home){
           this.finish();
           return true;
       }
       return true;
    }
}