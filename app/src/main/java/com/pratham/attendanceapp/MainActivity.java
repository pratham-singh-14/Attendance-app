package com.pratham.attendanceapp;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    RecyclerView recyclerView;
    LinearLayout include, btn_ll;
    AppCompatButton btn, present_btn, absent_btn;
    Toolbar toolbar;
    TextView take_tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // calendar=new MyCalendar();


        take_tv = findViewById(R.id.take_tv);
        btn_ll = findViewById(R.id.btn_ll);

        present_btn = findViewById(R.id.present_btn);
        absent_btn = findViewById(R.id.absent_btn);
        fab = findViewById(R.id.fab_main);
        recyclerView = findViewById(R.id.recyclerView);
        include = findViewById(R.id.include);
        btn = findViewById(R.id.btnadd);
        DBHelper dbHelper = new DBHelper(this);
        showclass(dbHelper);

        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        TextView save = toolbar.findViewById(R.id.save);
        title.setText("Attendance App");
        subtitle.setVisibility(View.GONE);
        back.setVisibility(View.GONE);
        save.setVisibility(View.GONE);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.performClick();
            }
        });

    }


    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.class_dialog, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        AppCompatButton cancel = view.findViewById(R.id.cancel_btn);
        AppCompatButton add = view.findViewById(R.id.add_btn);
        EditText class_edt = (EditText) view.findViewById(R.id.class_edt);
        EditText subject_edt = (EditText) view.findViewById(R.id.subject_edt);
        EditText a = (EditText) view.findViewById(R.id.st_name);
        EditText b = (EditText) view.findViewById(R.id.st_father);
        EditText c = (EditText) view.findViewById(R.id.st_phone);
        a.setVisibility(View.GONE);
        b.setVisibility(View.GONE);
        c.setVisibility(View.GONE);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String c = class_edt.getText().toString();
                String s = subject_edt.getText().toString();
                if (!c.isEmpty() && !s.isEmpty()) {
                    DBHelper dbhelper = new DBHelper(MainActivity.this);
                    dbhelper.addClass(c, s);
                    showclass(dbhelper);
                    Toast.makeText(MainActivity.this, "Class added Successfully", Toast.LENGTH_SHORT).show();

                    dialog.dismiss();
                } else {
                    Toast.makeText(MainActivity.this, "Please enter something", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    public void showclass(DBHelper dbhelper) {
        ArrayList<ClassItem> list = dbhelper.fetchClass();
        if (list.size() > 0) {

            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


            ClassAdapter adapter = new ClassAdapter(this, list);

            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            include.setVisibility(View.GONE);
        } else {
            include.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }


}
