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

import com.example.d308vacationplanner.R;
import com.example.d308vacationplanner.database.Repository;
import com.example.d308vacationplanner.entities.Excursion;
import com.example.d308vacationplanner.entities.Vacation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExcursionDetails extends AppCompatActivity {
    int excursionID;
    String excursionTitle;
    String excursionDate;
    int vacationID;
    TextView excursionIDTextView;
    TextView excursionTitleTextView;
    EditText excursionDateEditText;
    EditText vacationIDEditText;
    String vacationEndDate;
    String vacationStartDate;
    DatePickerDialog.OnDateSetListener starterDate;
    final Calendar myCalendar = Calendar.getInstance();
    Repository repository = new Repository(getApplication());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_excursion_details);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        excursionID = getIntent().getIntExtra("excursionID", 0);
        excursionIDTextView = findViewById(R.id.excursion_details_text_set_ID);
        excursionIDTextView.setText(String.valueOf(excursionID));

        excursionTitle = getIntent().getStringExtra("excursionTitle");
        excursionTitleTextView = findViewById(R.id.excursion_details_text_set_title);
        excursionTitleTextView.setText(excursionTitle);

        excursionDate = getIntent().getStringExtra("excursionDate");
        excursionDateEditText = findViewById(R.id.excursion_details_text_set_date);
        excursionDateEditText.setText(excursionDate);

        vacationID = getIntent().getIntExtra("ExcursionVacationID", 0);
        vacationIDEditText = findViewById(R.id.excursion_details_text_set_vacationID);
        vacationIDEditText.setText(String.valueOf(vacationID));

        vacationStartDate = getIntent().getStringExtra("VacationStartDate");
        vacationEndDate = getIntent().getStringExtra("VacationEndDate");


        Repository repository = new Repository(getApplication());
        if (vacationID != 0) {
            Vacation vacation = repository.getVacationByID(vacationID);
            vacationStartDate = vacation.getVacationStartDate();
            vacationEndDate = vacation.getVacationEndDate();
        }

        String dateFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);

        excursionDateEditText.setOnClickListener(view -> {
            String info = excursionDateEditText.getText().toString();
            try {
                myCalendar.setTime(sdf.parse(info));
            } catch (Exception e) {
                e.printStackTrace();
            }
            new DatePickerDialog(ExcursionDetails.this, starterDate, myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });


        starterDate = (view, year, monthOfYear, dayOfMonth) -> {

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            updateDateLabel();
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        String dateFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);

        if (item.getItemId() == R.id.excursionSave) {

            try {
                Date vacationStart = sdf.parse(vacationStartDate);
                Date vacationEnd = sdf.parse(vacationEndDate);
                Date excursionDate = sdf.parse(excursionDateEditText.getText().toString());

                String excursionTitleText = excursionTitleTextView.getText().toString();
                String excursionDateText = excursionDateEditText.getText().toString();

                if (excursionDate.before(vacationStart)|| excursionDate.after(vacationEnd) ) {
                    Toast.makeText(this, "Excursion date must be within the period of your vacation.", Toast.LENGTH_LONG).show();
                    return false;
                }

                if (repository.getExcursionByID(excursionID) != null) {
                    Excursion excursion = new Excursion(excursionID, excursionTitleText, excursionDateText, vacationID);
                    repository.updateExcursion(excursion);
                } else {
                    Excursion excursion = new Excursion(0, excursionTitleText, excursionDateText, vacationID);
                    repository.insertExcursion(excursion);
                }

                Toast.makeText(this, "Your excursion was saved!", Toast.LENGTH_LONG).show();
                finish();
            }
            catch (Exception e) {
                Toast.makeText(this, "There was an error adding your excursion", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            return true;
        }

        if (item.getItemId() == R.id.setExcursionAlerts) {

            String startDate = excursionDate;

            try {
                Long startDateNotify = sdf.parse(startDate).getTime();

                Intent excursionIntent = new Intent(ExcursionDetails.this, MyReceiver.class);
                excursionIntent.setAction("ACTION_START_EXCURSION");
                excursionIntent.putExtra("vacationTitle", excursionTitle + " is starting today " + startDate);
                PendingIntent excursionMessenger = PendingIntent.getBroadcast(ExcursionDetails.this, ++MainActivity.notificationID, excursionIntent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, startDateNotify, excursionMessenger);

            } catch (Exception e) {
                Toast.makeText(this, "Something went wrong when setting your alarm!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            return true;
        }

        if (item.getItemId() == R.id.excursionDelete) {
            if (excursionID == 0){
                Toast.makeText(this, "The excursion has not been saved yet, their is nothing to delete.", Toast.LENGTH_LONG).show();
                this.finish();
            }
            else {
                Excursion excursion = new Excursion(excursionID, excursionTitleTextView.getText().toString(), excursionDateEditText.getText().toString(), vacationID);
                repository.deleteExcursion(excursion);
                Toast.makeText(this, "Excursion successfully deleted", Toast.LENGTH_LONG).show();
                this.finish();
            }
            return true;
        }

        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_excursiondetails, menu);
        return true;
    }


    private void updateDateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdfa = new SimpleDateFormat(myFormat, Locale.US);
        excursionDateEditText.setText((sdfa.format(myCalendar.getTime())).toString());
    }

}