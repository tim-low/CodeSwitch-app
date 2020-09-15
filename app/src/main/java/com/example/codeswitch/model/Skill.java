package com.example.codeswitch.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Skill extends BaseObject {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("group")
    @Expose
    private String group;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "Skill{" +
                "name='" + name + '\'' +
                ", group='" + group + '\'' +
                ", id=" + id +
                '}';
    }
}