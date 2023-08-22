package com.pratham.attendanceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class student_activity extends AppCompatActivity {
    Toolbar toolbar;
    private String classname, subjectname;
    private int position;
    private long cid1;
    public int p_st = 0;
    private MyCalendar calendar;
    private ArrayList<StudentItem> studentItems = new ArrayList<>();

    private LinearLayout ll;
    private AppCompatButton btn;
    private TextView emptyy;
    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private DBHelper dbHelper;
    private TextView subtitle, title;
    private TextView save1;
    private RecyclerView.LayoutManager layoutManager;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        toolbar = findViewById(R.id.toolbar);
        title = toolbar.findViewById(R.id.title_toolbar);
        subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        back = toolbar.findViewById(R.id.back);
        save1 = toolbar.findViewById(R.id.save);
        ll = findViewById(R.id.ll);
        btn = findViewById(R.id.btnadd);
        emptyy = findViewById(R.id.emptyy);

        emptyy.setText("No student avialable in this class !");
        btn.setText("Add new student");
        recyclerView = findViewById(R.id.student_recycler);
        dbHelper = new DBHelper(this);
        calendar = new MyCalendar();

        Intent intent = getIntent();
        classname = intent.getStringExtra("class");
        subjectname = intent.getStringExtra("subject");
        position = intent.getIntExtra("position", -1);
        cid1 = intent.getLongExtra("cid", -2);
        settoolbar();
        loadData();
    }


    public void rview() {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StudentAdapter(this, studentItems);
        recyclerView.setAdapter(adapter);


    }

    private void loadData() {
        Cursor cursor = dbHelper.getStudentTable(cid1);
        studentItems.clear();
        while (cursor.moveToNext()) {
            @SuppressLint("Range") long sid = cursor.getLong(cursor.getColumnIndex(DBHelper.S_ID));
            @SuppressLint("Range") int roll = cursor.getInt(cursor.getColumnIndex(DBHelper.STUDENT_ROLL_KEY));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DBHelper.STUDENT_NAME_KEY));
            @SuppressLint("Range") String enrollment = cursor.getString(cursor.getColumnIndex(DBHelper.STUDENT_ENROLLMENT_KEY));
            @SuppressLint("Range") String father = cursor.getString(cursor.getColumnIndex(DBHelper.STUDENT_FATHER_KEY));
            @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex(DBHelper.STUDENT_PHONE_KEY));

            studentItems.add(new StudentItem(sid, roll, enrollment, name, father, phone));
        }
        cursor.close();
        if (studentItems.size() > 0) {
            save1.setVisibility(View.VISIBLE);
            save1.setText("Total ( " + studentItems.size() + " )");
            recyclerView.setVisibility(View.VISIBLE);
            ll.setVisibility(View.GONE);


            rview();
            loadStatusdata();
            adapter.notifyDataSetChanged();

        } else {
            recyclerView.setVisibility(View.GONE);
            ll.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAddStudentDialog();
                }
            });
        }
    }

    public void changeStatus(int position) {
        String status = studentItems.get(position).getStatus();
        if (status.equals("P")) {
            status = "A";

        } else {
            status = "P";
        }
        studentItems.get(position).setStatus(status);
        adapter.notifyItemChanged(position);
    }

    private void settoolbar() {

        save1.setVisibility(View.GONE);
        title.setText(classname);
        subtitle.setText(subjectname + " | " + calendar.getDate());
        back.setVisibility(View.GONE);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });
        toolbar.inflateMenu(R.menu.student_menu);
        toolbar.setOnMenuItemClickListener(menuItem -> onMenuItemClick(menuItem));
    }

    private boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.add_student) {
            showAddStudentDialog();
        } else if (menuItem.getItemId() == R.id.calendar) {
            showCalendar();
        } else if (menuItem.getItemId() == R.id.Attendancesheet) {
            openSheetList();
        } else if (menuItem.getItemId() == R.id.save_status) {
            saveStatus();
            loadStatusdata();
        }
        return true;
    }

    private void saveStatus() {
        for (StudentItem studentItem : studentItems) {
            String status = studentItem.getStatus();
            if (status != "P") status = "A";
            long value = dbHelper.addStatus(studentItem.getS_id(), cid1, calendar.getDate(), status);
            if (value == -1)
                dbHelper.updateStatus(studentItem.getS_id(), calendar.getDate(), status);


        }
        Toast.makeText(this, "Attendance save successfully", Toast.LENGTH_SHORT).show();
    }

    private void loadStatusdata() {
        for (StudentItem studentItem : studentItems) {
            String status = dbHelper.getStatus(studentItem.getS_id(), calendar.getDate());


            if (status != null) studentItem.setStatus(status);
            else {
                studentItem.setStatus("");
            }
        }
        adapter.notifyDataSetChanged();

    }


    private void openSheetList() {
        long[] idArray = new long[studentItems.size()];
        String[] nameArray = new String[studentItems.size()];
        int[] rollArray = new int[studentItems.size()];

        for (int i = 0; i < idArray.length; i++)
            idArray[i] = studentItems.get(i).getS_id();

        for (int i = 0; i < rollArray.length; i++)
            rollArray[i] = studentItems.get(i).getRoll();

        for (int i = 0; i < nameArray.length; i++)
            nameArray[i] = studentItems.get(i).getName();

        Intent intent = new Intent(this, SheetList.class);
        intent.putExtra("cid", cid1);
        intent.putExtra("idArray", idArray);
        intent.putExtra("rollArray", rollArray);
        intent.putExtra("nameArray", nameArray);
        intent.putExtra("class_name", classname);
        intent.putExtra("subject_name", subjectname);
        startActivity(intent);
    }

    private void showCalendar() {
        MyCalendar calendar = new MyCalendar();
        calendar.show(getSupportFragmentManager(), "");
        calendar.setOnCalendarOkClickListener(this::onCalendarOkClicked);
    }

    private void onCalendarOkClicked(int year, int month, int day) {
        calendar.setDate(year, month, day);
        subtitle.setText(subjectname + " | " + calendar.getDate());
        loadStatusdata();
    }

    private void showAddStudentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.class_dialog, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.show();
        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("Add new Student");
        EditText roll_edt = view.findViewById(R.id.class_edt);
        EditText enroll_edt = view.findViewById(R.id.subject_edt);
        EditText st_name_edt = view.findViewById(R.id.st_name);
        EditText st_father_edt = view.findViewById(R.id.st_father);
        EditText st_phone_edt = view.findViewById(R.id.st_phone);
        roll_edt.setHint("Roll no.");
        enroll_edt.setHint("Enrollment");

        AppCompatButton cancel = view.findViewById(R.id.cancel_btn);
        AppCompatButton add = view.findViewById(R.id.add_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String roll = roll_edt.getText().toString();
                String enroll = enroll_edt.getText().toString();

                String st_name = st_name_edt.getText().toString();
                String st_father = st_father_edt.getText().toString();
                String st_phone = st_phone_edt.getText().toString();
                try {
                    int r = Integer.parseInt(roll);
                    roll_edt.setText(String.valueOf(r + 1));
                } catch (NumberFormatException e) {

                    Toast.makeText(student_activity.this, "Please enter roll no. is a number ", Toast.LENGTH_SHORT).show();
                }
                enroll_edt.setText("");
                st_name_edt.setText("");
                st_father_edt.setText("");
                st_phone_edt.setText("");

                if (!roll.isEmpty() && !st_name.isEmpty() && !st_father.isEmpty()) {

                    try {
                      int  rol = Integer.parseInt(roll);
                        addStudent(rol, enroll, st_name, st_father, st_phone);

                    } catch (NumberFormatException e) {

                    }


                } else {
                    Toast.makeText(student_activity.this, "Please enter something", Toast.LENGTH_SHORT).show();
                }
                //dialog.dismiss();
            }
        });


    }

    private void addStudent(int roll, String enrollment, String name, String father, String phone) {
        long sid = dbHelper.addStudent(cid1, roll, enrollment, name, father, phone);
        studentItems.add(new StudentItem(sid, roll, enrollment, name, father, phone));
        loadData();
        rview();
        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                showupdatestudent(item.getGroupId());
                break;
            case 1:
                deleteStudentss(item.getGroupId());
        }
        return super.onContextItemSelected(item);
    }

    private void showupdatestudent(int position) {
//        loadData();
        StudentItem list1 = studentItems.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.class_dialog, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        EditText roll, phone, s_name, s_father, enrollment;
        TextView titledialog;
        AppCompatButton up_btn, cancelbtn;
        cancelbtn = view.findViewById(R.id.cancel_btn);
        titledialog = view.findViewById(R.id.titleDialog);
        roll = (EditText) view.findViewById(R.id.class_edt);
        enrollment = (EditText) view.findViewById(R.id.subject_edt);
        s_name = (EditText) view.findViewById(R.id.st_name);
        s_father = (EditText) view.findViewById(R.id.st_father);
        phone = (EditText) view.findViewById(R.id.st_phone);
        up_btn = view.findViewById(R.id.add_btn);
        roll.setHint("Roll no.");
        enrollment.setHint("Enrollment no.");
        s_name.setHint("Student name");
        s_father.setHint("Student Father name");
        phone.setHint("Student Phone no.");
        roll.setText(String.valueOf(list1.getRoll()));
        enrollment.setText(list1.getEnrollment());
        s_name.setText(list1.getName());
        s_father.setText(list1.getFather());
        phone.setText(list1.getPhone());
        roll.setEnabled(false);
        titledialog.setText("Update Student");
        up_btn.setText("Update");
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String r = roll.getText().toString();
                String e = enrollment.getText().toString();
                String ne = s_name.getText().toString();
                String f = s_father.getText().toString();
                String p = phone.getText().toString();

                if (!r.isEmpty() && !ne.isEmpty() && !f.isEmpty()) {
                    DBHelper helper = new DBHelper(student_activity.this);
                    try {
                        list1.roll = Integer.parseInt(r);
                        list1.enrollment = e;
                        list1.name = ne;
                        list1.father = f;
                        list1.phone = p;


                        helper.updateStudent(studentItems.get(position).getS_id(), Integer.parseInt(r), e, ne, f, p);
                        loadData();

                        rview();
                        loadStatusdata();
                        adapter.notifyDataSetChanged();


                        dialog.cancel();
                    } catch (NumberFormatException x) {
                        Toast.makeText(student_activity.this, "Please enter roll number field a number.", Toast.LENGTH_SHORT).show();

                    }


                } else {
                    Toast.makeText(student_activity.this, "Please write something!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    private void deleteStudentss(int position) {
        dbHelper.deleteStudent(studentItems.get(position).getS_id());
        studentItems.remove(position);
        adapter.notifyDataSetChanged();
        loadData();
        rview();
    }
}