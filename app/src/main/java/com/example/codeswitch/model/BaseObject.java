package com.example.codeswitch.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Abstraction of a typical object
 * Usage of strategy pattern
 */
public abstract class BaseObject implements Serializable {
    @SerializedName("id")
    @Expose
    protected int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public abstract String toString();
}