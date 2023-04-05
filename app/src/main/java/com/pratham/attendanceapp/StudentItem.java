package com.pratham.attendanceapp;

public class StudentItem {
    public long s_id;
    public int roll;
    public String name;
    public String Status;
    public String enrollment;
    public String father;
    public String phone;

    public StudentItem(long s_id, int roll, String enrollment, String name, String father, String phone) {
        this.s_id = s_id;
        this.roll = roll;


        this.enrollment = enrollment;
        this.name = name;
        this.father = father;
        this.phone = phone;
        Status = "";
    }

    public String getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(String enrollment) {
        this.enrollment = enrollment;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public StudentItem(long sid, int roll, String name) {
        this.s_id = sid;
        this.roll = roll;
        this.name = name;
        Status = "";
    }


    public long getS_id() {
        return s_id;
    }

    public void setS_id(long s_id) {
        this.s_id = s_id;
    }

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return Status;
    }

    public String setStatus(String status) {
        Status = status;
        return Status;
    }
}
