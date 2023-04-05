package com.pratham.attendanceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SheetList extends AppCompatActivity {
    private ListView sheetList;
    private ArrayAdapter adapter;
    private ArrayList<String> listitem = new ArrayList();
    private long cid;
    private LinearLayout ll;
    private AppCompatButton btn;
    private TextView emptyy;
    private String class_names, subject_names;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_list);
        ll = findViewById(R.id.ll);
        btn = findViewById(R.id.btnadd);
        emptyy = findViewById(R.id.emptyy);
        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        TextView save = toolbar.findViewById(R.id.save);
        back.setVisibility(View.GONE);
        class_names = getIntent().getStringExtra("class_name");
        subject_names = getIntent().getStringExtra("subject_name");
        save.setVisibility(View.GONE);
        title.setText("Attendance App");
        subtitle.setText(class_names + " | " + subject_names);
        cid = getIntent().getLongExtra("cid", -1);

        sheetList = findViewById(R.id.sheetList);
        loadlistitems();



    }


    private void loadlistitems() {
        Cursor cursor = new DBHelper(this).getDistinctmonths(cid);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(DBHelper.DATE_KEY));
            listitem.add(date.substring(3));
        }
        cursor.close();
        if(listitem.size()>0)
        {
            sheetList.setVisibility(View.VISIBLE);
           ll.setVisibility(View.GONE);
            adapter = new ArrayAdapter(this, R.layout.sheet_list_item, R.id.datelist, listitem);
            sheetList.setAdapter(adapter);
            sheetList.setOnItemClickListener((parent, view, position, id) -> openSheetActivity(position));

        }
        else {
               ll.setVisibility(View.VISIBLE);
               sheetList.setVisibility(View.GONE);
               emptyy.setText("No Attendance Available");
               btn.setVisibility(View.GONE);
        }
    }


    private void openSheetActivity(int position) {
        long[] idArray = getIntent().getLongArrayExtra("idArray");
        int[] rollArray = getIntent().getIntArrayExtra("rollArray");
        String[] nameArray = getIntent().getStringArrayExtra("nameArray");

        Intent intent = new Intent(this, SheetActivityList.class);
        intent.putExtra("idArray", idArray);
        intent.putExtra("rollArray", rollArray);
        intent.putExtra("nameArray", nameArray);
        intent.putExtra("class_name", class_names);
        intent.putExtra("subject_name", subject_names);
        intent.putExtra("month", listitem.get(position));
        startActivity(intent);
    }
}