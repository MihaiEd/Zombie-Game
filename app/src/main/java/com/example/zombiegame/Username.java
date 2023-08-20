package com.example.zombiegame;

public class Username {
    String Email, Date, Name, Password, Uid;
    int Zombies;

    public Username() {

    }

    public Username(String email, String date, String name, String password, String uid, int zombies) {
        Email = email;
        Date = date;
        Name = name;
        Password = password;
        Uid = uid;
        Zombies = zombies;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public int getZombies() {
        return Zombies;
    }

    public void setZombies(int zombies) {
        Zombies = zombies;
    }
}
