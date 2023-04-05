package com.pratham.attendanceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Calendar;

public class SheetActivityList extends AppCompatActivity {
    private Toolbar toolbar;
    private String class_names, subject_names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_list2);
        showTable();
        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        TextView save = toolbar.findViewById(R.id.save);
        back.setVisibility(View.GONE);
        save.setVisibility(View.GONE);
        title.setText("Attendance App");
        subtitle.setText(class_names + " | " + subject_names);

    }

    private void showTable() {
        DBHelper dbHelper = new DBHelper(this);
        TableLayout tableLayout = findViewById(R.id.tableLayout);
        class_names = getIntent().getStringExtra("class_name");
        subject_names = getIntent().getStringExtra("subject_name");
        long[] idArray = getIntent().getLongArrayExtra("idArray");
        int[] rollArray = getIntent().getIntArrayExtra("rollArray");
        String[] nameArray = getIntent().getStringArrayExtra("nameArray");
        String month = getIntent().getStringExtra("month");

        int DAY_IN_MONTH = getDAYINMONTH(month);

        int rowsize = idArray.length + 1;

        TableRow[] tableRows = new TableRow[rowsize];
        TextView[] tv_rolls = new TextView[rowsize];
        TextView[] tv_names = new TextView[rowsize];
        TextView[][] tv_status = new TextView[rowsize][DAY_IN_MONTH + 1];
        for (int i = 0; i < rowsize; i++) {
            tv_rolls[i] = new TextView(this);
            tv_names[i] = new TextView(this);
            for (int j = 1; j <= DAY_IN_MONTH; j++) {
                tv_status[i][j] = new TextView(this);

            }
        }

        //header
        tv_rolls[0].setText("Roll");
        tv_rolls[0].setTypeface(tv_rolls[0].getTypeface(), Typeface.BOLD);

        tv_names[0].setText("Name");
        for (int i = 1; i <= DAY_IN_MONTH; i++) {
            tv_status[0][i].setText(String.valueOf(i));
            tv_status[0][i].setTypeface(tv_status[0][i].getTypeface(), Typeface.BOLD);
        }


        for (int i = 1; i < rowsize; i++) {
            tv_rolls[i].setText(String.valueOf(rollArray[i - 1]));
            tv_names[i].setText(nameArray[i - 1]);

            for (int j = 1; j <= DAY_IN_MONTH; j++) {
                String day = String.valueOf(j);
                if (day.length() == 1) day = "0" + day;
                String date = day + "." + month;
                String status = dbHelper.getStatus(idArray[i - 1], date);
                tv_status[i][j].setText(status);
            }
        }

        for (int i = 0; i < rowsize; i++) {
            tableRows[i] = new TableRow(this);

//            if(i%2 == 0)
//                tableRows[i].setBackgroundColor(Color.parseColor("#FFFFFF"));
//
//            else tableRows[i].setBackgroundColor(Color.parseColor("#FFFFFF"));

            tv_rolls[i].setPadding(16, 16, 16, 16);
            tv_names[i].setPadding(16, 16, 16, 16);

            tableRows[i].addView(tv_rolls[i]);
            tableRows[i].addView(tv_names[i]);

            for (int j = 1; j <= DAY_IN_MONTH; j++) {

                tv_status[i][j].setPadding(16, 16, 16, 16);
                tableRows[i].addView(tv_status[i][j]);

            }
            tableLayout.addView(tableRows[i]);
        }
        tableLayout.setShowDividers(TableLayout.SHOW_DIVIDER_MIDDLE);


    }

    private int getDAYINMONTH(String month) {
        int monthIndex = Integer.valueOf(month.substring(0, 1));
        int year = Integer.valueOf(month.substring(4));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, monthIndex);
        calendar.set(Calendar.YEAR, year);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}