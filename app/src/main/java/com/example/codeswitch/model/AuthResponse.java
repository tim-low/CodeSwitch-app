package com.example.codeswitch.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthResponse {
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("success")
    @Expose
    protected Boolean success;
    @SerializedName("message")
    @Expose
    protected String message;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toString() {
        return "AuthResponse{" +
                "user=" + user +
                ", success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}
