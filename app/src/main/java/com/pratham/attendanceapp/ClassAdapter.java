package com.pratham.attendanceapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {
    ArrayList<ClassItem> classItems;
    Context context;

    public ClassAdapter(Context context, ArrayList<ClassItem> classItems) {
        this.classItems = classItems;
        this.context = context;

    }


    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item, parent, false);
        return new ClassViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ClassItem c_item = classItems.get(position);
        holder.classname.setText(c_item.ClassName);
        holder.subjectname.setText(c_item.SubjectName);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, student_activity.class);
                intent.putExtra("class", classItems.get(position).getClassName());
                intent.putExtra("subject", classItems.get(position).getSubjectName());
                intent.putExtra("position", position);
                intent.putExtra("cid", classItems.get(position).getC_id());
                context.startActivity(intent);

            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                PopupMenu popupMenu = new PopupMenu(context, holder.itemView, Gravity.CENTER, 0, R.style.PopupMenuMoreCenteralized);
                popupMenu.inflate(R.menu.edit);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.e_c) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            View view = LayoutInflater.from(context).inflate(R.layout.class_dialog, null);
                            builder.setView(view);
                            AlertDialog dialog = builder.create();
                            dialog.setCancelable(true);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.show();
                            EditText c_n, s_n, a, b, c;
                            TextView titledialog;
                            AppCompatButton up_btn, cancelbtn;
                            cancelbtn = view.findViewById(R.id.cancel_btn);
                            titledialog = view.findViewById(R.id.titleDialog);
                            c_n = (EditText) view.findViewById(R.id.class_edt);
                            s_n = (EditText) view.findViewById(R.id.subject_edt);
                            a = (EditText) view.findViewById(R.id.st_name);
                            b = (EditText) view.findViewById(R.id.st_father);
                            c = (EditText) view.findViewById(R.id.st_phone);
                            a.setVisibility(View.GONE);
                            b.setVisibility(View.GONE);
                            c.setVisibility(View.GONE);
                            up_btn = view.findViewById(R.id.add_btn);
                            c_n.setText(c_item.ClassName);
                            s_n.setText(c_item.SubjectName);
                            titledialog.setText("Update Details");
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
                                    String t = c_n.getText().toString();
                                    String c = s_n.getText().toString();

                                    if (!t.isEmpty() && !c.isEmpty()) {
                                        DBHelper helper = new DBHelper(context);

                                        c_item.ClassName = t;
                                        c_item.SubjectName = c;
                                        helper.Update(c_item);
                                        ((MainActivity) context).showclass(helper);
                                        dialog.cancel();


                                    } else {
                                        Toast.makeText(context, "Please write something!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            dialog.show();
                        } else if (item.getItemId() == R.id.d_c) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Delete " + c_item.ClassName);
                            builder.setMessage("Are you sure you want to delete this Class");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    DBHelper d = new DBHelper(context);
                                    d.Delete(c_item);
                                    ((MainActivity) context).showclass(d);
                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            builder.show();
                            return false;

                        }
                        return false;
                    }
                });
                popupMenu.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return classItems.size();
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder {
        TextView classname, subjectname;

        public ClassViewHolder(@NonNull View itemView) {
            super(itemView);
            classname = itemView.findViewById(R.id.class_tv);
            subjectname = itemView.findViewById(R.id.subject_tv);


        }


    }

}
