package com.example.codeswitch.model;

import java.io.Serializable;
import java.util.List;

import com.example.codeswitch.model.BaseObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User extends BaseObject implements Serializable {
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("skills")
    @Expose
    private List<String> skills = null;

    public User(String email, String password, List<String> skills) {
        this.email = email;
        this.password = password;
        this.skills = skills;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", skills=" + skills +
                ", id=" + id +
                '}';
    }
}