package com.example.d308vacationplanner.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d308vacationplanner.R;
import com.example.d308vacationplanner.database.Repository;
import com.example.d308vacationplanner.entities.Vacation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static int notificationID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button button=findViewById(R.id.button);
        SearchView searchView = findViewById(R.id.searchView);

        button.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, VacationList.class);
            intent.putExtra("test", "Information sent");
            startActivity(intent);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String query) {
                if (isValidQuery(query)) {
                    fetchVacations(query);
                } else {
                    Toast.makeText(MainActivity.this, "Invalid search!", Toast.LENGTH_SHORT).show();

                }
                return true;
            }

            private boolean isValidQuery(String query) {
                String regex = ".*[0-9&@#!$%^&*()_+=<>?/|{}\\[\\]:;\"',.~`].*";
                if (query.matches(regex) || query.length() > 15) {
                    return false;
                }
                return true;
            }
            //B5 added query validation functionality.


            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void fetchVacations(String query) {
        Repository repository = new Repository(getApplication());

        List<Vacation> vacations = repository.searchVacationsByTitle(query);

        if (vacations == null || vacations.isEmpty()) {
            Toast.makeText(this, "No vacations found!", Toast.LENGTH_SHORT).show();
        } else {
            displayChart(vacations);
        }
    }

    private void displayChart(List<Vacation> vacations) {
        TableLayout reportTableLayout = findViewById(R.id.reportTableLayout);
        TableRow headerRow = findViewById(R.id.headerRow);

        reportTableLayout.removeViewsInLayout(1, reportTableLayout.getChildCount() - 1);

        if (vacations == null || vacations.isEmpty()) {
            reportTableLayout.setVisibility(View.GONE);
            return;
        } else {
            reportTableLayout.setVisibility(View.VISIBLE);
            headerRow.setVisibility(View.VISIBLE);
        }


        for (Vacation vacation : vacations) {
            TableRow row = new TableRow(this);

            TextView titleTextView = new TextView(this);
            titleTextView.setText(vacation.getVacationTitle());
            titleTextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            titleTextView.setPadding(0, 8, 0, 8);
            row.addView(titleTextView);

            TextView dateTextView = new TextView(this);
            dateTextView.setText(vacation.getVacationStartDate());
            dateTextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            dateTextView.setPadding(0, 8, 0, 8);
            row.addView(dateTextView);

            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            TextView timestampTextView = new TextView(this);
            timestampTextView.setText(timestamp);
            timestampTextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            timestampTextView.setPadding(0, 8, 0, 8);
            row.addView(timestampTextView);

            reportTableLayout.addView(row);
        }
    }
}
