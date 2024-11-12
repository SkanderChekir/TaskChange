package com.example.user_module.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "confirmationCode")
    public String confirmationCode;

    @ColumnInfo(name = "confirmed")
    public boolean confirmed;

    @ColumnInfo(name = "resetToken")
    public String resetToken;



    public User(String email, String password, String confirmationCode) {
        this.email = email;
        this.password = password;
        this.confirmationCode = confirmationCode;
        this.confirmed = false;
        this.resetToken = null;// Default to false until confirmed
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }
    public void setPassword(String password) {
        this.password = password;
    }

}
