package com.pratham.attendanceapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    ArrayList<StudentItem> studentItems;
    Context context;

    public StudentAdapter(Context context, ArrayList<StudentItem> studentItems) {
        this.studentItems = studentItems;
        this.context = context;

    }


    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item, parent, false);
        return new StudentViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, @SuppressLint("RecyclerView") int position) {
        StudentItem list1 = studentItems.get(position);
        holder.roll.setText(studentItems.get(position).getRoll() + "");
        holder.name.setText(studentItems.get(position).getName());
        holder.status.setText(studentItems.get(position).getStatus());
        holder.card.setBackgroundColor(getColor(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((student_activity) context).changeStatus(position);
            }
        });

    }

    private int getColor(int position) {
        String status = studentItems.get(position).getStatus();
        if (status.equals("P")) {
            return Color.parseColor("#" + Integer.toHexString(ContextCompat.getColor(context, R.color.green)));
        } else if (status.equals("A")) {
            return Color.parseColor("#" + Integer.toHexString(ContextCompat.getColor(context, R.color.red)));
        }
        return Color.parseColor("#" + Integer.toHexString(ContextCompat.getColor(context, R.color.normal)));

    }

    @Override
    public int getItemCount() {
        return studentItems.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView roll, name, status;
        CardView card;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            roll = itemView.findViewById(R.id.roll);
            name = itemView.findViewById(R.id.name);
            status = itemView.findViewById(R.id.status);
            card = itemView.findViewById(R.id.card);


            itemView.setOnCreateContextMenuListener(this);


        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(getAdapterPosition(), 0, 0, "Edit");
            menu.add(getAdapterPosition(), 1, 0, "Delete");
        }
    }
}
