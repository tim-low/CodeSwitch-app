package com.example.codeswitch.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// Schema is still subject to change
public class SavedJob extends BaseObject {
    @SerializedName("user")
    @Expose
    private Integer userId;
    @SerializedName("job")
    @Expose
    private Job job;
    @SerializedName("has_applied")
    @Expose
    private Boolean hasApplied;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Boolean getHasApplied() {
        return hasApplied;
    }

    public void setHasApplied(Boolean hasApplied) {
        this.hasApplied = hasApplied;
    }

    @Override
    public String toString() {
        return "UserJob{" +
                "userId=" + userId +
                ", jobId=" + job.toString() +
                ", hasApplied=" + hasApplied +
                ", id=" + id +
                '}';
    }
}