package com.example.d308vacationplanner.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class VacationDetails extends AppCompatActivity {
    String title;
    String hotelTitle;
    String startDate;
    String endDate;
    int vacationID;
    TextView vacationIDTextView;
    EditText editVacationTitle;
    EditText editVacationHotel;
    EditText editVacationStartDate;
    EditText editVacationEndDate;
    Repository repository;

    DatePickerDialog.OnDateSetListener starterDate;
    DatePickerDialog.OnDateSetListener enderDate;
    final Calendar myCalendar=Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_details);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);

        editVacationTitle = findViewById(R.id.titletext);
        editVacationHotel = findViewById(R.id.hoteltext);
        editVacationStartDate = findViewById(R.id.startdate);
        editVacationEndDate = findViewById(R.id.enddate);
        vacationIDTextView = findViewById(R.id.vacationTextID);

        title = getIntent().getStringExtra("vacationTitle");
        vacationID = getIntent().getIntExtra("vacationID", -1);
        hotelTitle = getIntent().getStringExtra("vacationHotel");
        startDate = getIntent().getStringExtra("vacationStartDate");
        endDate = getIntent().getStringExtra("vacationEndDate");

        editVacationTitle.setText(title);
        editVacationHotel.setText(hotelTitle);
        editVacationStartDate.setText(startDate);
        editVacationEndDate.setText(endDate);
        vacationIDTextView.setText(String.valueOf(vacationID));

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
            intent.putExtra("VacationStartDate", startDate);
            intent.putExtra("VacationEndDate", endDate);
            intent.putExtra("ExcursionVacationID", vacationID);
            startActivity(intent);
        });

        RecyclerView recyclerView = findViewById(R.id.vacationRecyclerView);
        repository = new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion e : repository.getAllExcursions()) {
            if (e.getVacationID() == vacationID) filteredExcursions.add(e);
        }
        excursionAdapter.setExcursions(filteredExcursions);

        String dateFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);

        editVacationStartDate.setOnClickListener(view -> {
            String info = editVacationStartDate.getText().toString();
            try {
                myCalendar.setTime(Objects.requireNonNull(sdf.parse(info)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            new DatePickerDialog(VacationDetails.this, starterDate, myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        editVacationEndDate.setOnClickListener(view -> {
            String info=editVacationEndDate.getText().toString();
            try{
                myCalendar.setTime(Objects.requireNonNull(sdf.parse(info)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            new DatePickerDialog(VacationDetails.this,enderDate,myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        starterDate = (view, year, monthOfYear, dayOfMonth) -> {

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            updateStartLabel();
        };

        enderDate = (view, year, monthOfYear, dayOfMonth) -> {

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            updateEndLabel();
        };

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        repository = new Repository(getApplication());
        RecyclerView recyclerView = findViewById(R.id.vacationRecyclerView);
        final ExcursionAdapter adapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion excursion : repository.getAllExcursions()) {
            if (excursion.getVacationID() == vacationID) {
                filteredExcursions.add(excursion);
            }
        }
        adapter.setExcursions(filteredExcursions);
    }

    private void updateStartLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editVacationStartDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateEndLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editVacationEndDate.setText(sdf.format(myCalendar.getTime()));
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacationdetails, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.vacationsave) {
            handleSaveVacation();
            return true;
        } else if (itemId == R.id.vacationdelete) {
            handleDeleteVacation();
            return true;
        } else if (itemId == R.id.shareVacation) {
            handleShareVacation();
            return true;
        } else if (itemId == R.id.setVacationAlerts) {
            handleSetVacationAlerts();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private boolean handleSaveVacation() {
        String dateFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        Vacation vacation;

        String vacationTitle = editVacationTitle.getText().toString();
        String vacationHotel = editVacationHotel.getText().toString();
        String vacationStartDate = editVacationStartDate.getText().toString();
        String vacationEndDate = editVacationEndDate.getText().toString();

        try {
            Date tripStartDate = sdf.parse(vacationStartDate);
            Date tripEndDate = sdf.parse(vacationEndDate);

            if (repository.getAllVacations().size() == 0){
                vacationID = 0;
            }

            Toast.makeText(this, "Your vacation id is: " + vacationID, Toast.LENGTH_LONG).show();


            if (tripStartDate.after(tripEndDate)) {
                Toast.makeText(this, "Your vacation start date is later than your selected end date", Toast.LENGTH_LONG).show();
                return false;
            } else {
                if (vacationID == -1){
                    vacationID = repository.getAllVacations().get(repository.getAllVacations().size() - 1).getVacationID() + 1;
                    vacation = new Vacation(vacationID, vacationTitle, vacationHotel, vacationStartDate, vacationEndDate);
                    repository.insertVacation(vacation);
                    Toast.makeText(this, "Your new vacation was saved", Toast.LENGTH_LONG).show();
                    this.finish();
                }
                else if (vacationID == 0){
                    vacationID = 1;
                    vacation = new Vacation(vacationID, vacationTitle, vacationHotel, vacationStartDate, vacationEndDate);
                    repository.insertVacation(vacation);
                    Toast.makeText(this, "Your new vacation was saved", Toast.LENGTH_LONG).show();
                    this.finish();
                } else{
                    vacation = new Vacation(vacationID, vacationTitle, vacationHotel, vacationStartDate, vacationEndDate);
                    repository.updateVacation(vacation);
                    Toast.makeText(this, "Your vacation was updated", Toast.LENGTH_LONG).show();
                    this.finish();
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Invalid data entered!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return true;
    }

    private boolean handleDeleteVacation() {
        String vacationTitle = editVacationTitle.getText().toString();
        String vacationHotel = editVacationHotel.getText().toString();
        String vacationStartDate = editVacationStartDate.getText().toString();
        String vacationEndDate = editVacationEndDate.getText().toString();

        boolean hasVacations = repository.getAllVacations().size() > 0;

        if (hasVacations && vacationID != 0) {
            Vacation vacation = new Vacation(vacationID, vacationTitle, vacationHotel, vacationStartDate, vacationEndDate);
            List<Excursion> excursions = repository.getAssociatedExcursions(vacation.getVacationID());
            if (excursions.isEmpty()) {
                repository.deleteVacation(vacation);
                this.finish();
                Toast.makeText(this, "Your vacation was deleted!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Your vacation has associated excursions. Please delete them before proceeding.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Your vacation was never saved; therefore, it cannot be deleted", Toast.LENGTH_LONG).show();
        }
        return true;
    }

    private boolean handleShareVacation() {
        Intent sendIntent = new Intent();

        String vacationTitle = editVacationTitle.getText().toString();
        String vacationHotel = editVacationHotel.getText().toString();
        String vacationStartDate = editVacationStartDate.getText().toString();
        String vacationEndDate = editVacationEndDate.getText().toString();

        String text = String.format(
                "My Vacation to %s\nThe hotel: %s\nVacation start date: %s\nVacation end date: %s",
                vacationTitle,
                vacationHotel,
                vacationStartDate,
                vacationEndDate
        );

        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TITLE, "My Vacation Details");
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");

        startActivity(Intent.createChooser(sendIntent, null));
        return true;
    }

    private boolean handleSetVacationAlerts(){
        String dateFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);

        String startDate = editVacationStartDate.getText().toString();
        String endDate = editVacationEndDate.getText().toString();

        String vacationTitle = editVacationTitle.getText().toString();

        try {
            long startDateNotify = sdf.parse(startDate).getTime();
            long endDateNotify = sdf.parse(endDate).getTime();

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            setAlarm(alarmManager, startDateNotify, "ACTION_START_VACATION",
                    vacationTitle + " is starting today " + startDate);
            setAlarm(alarmManager, endDateNotify, "ACTION_END_VACATION",
                    vacationTitle + " is ending today " + endDate);

        } catch (ParseException e) {
            Toast.makeText(this, "Error! Couldn't set vacation alert", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return true;
    }

    private void setAlarm(AlarmManager alarmManager, Long l, String action, String message) {
        Intent intent = new Intent(VacationDetails.this, MyReceiver.class);
        intent.setAction(action);
        intent.putExtra("vacationTitle", message);

        PendingIntent sender = PendingIntent.getBroadcast(VacationDetails.this,
                ++MainActivity.notificationID, intent, PendingIntent.FLAG_IMMUTABLE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, l, sender);
    }


}

