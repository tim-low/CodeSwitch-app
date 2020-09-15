package com.example.codeswitch.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Job extends BaseObject {
    @SerializedName("required_skills")
    @Expose
    private List<String> requiredSkills;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("date_posted")
    @Expose
    private String datePosted;
    @SerializedName("application_src")
    @Expose
    private String applicationSrc;

    public List<String> getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(List<String> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public String getApplicationSrc() {
        return applicationSrc;
    }

    public void setApplicationSrc(String applicationSrc) {
        this.applicationSrc = applicationSrc;
    }

    @Override
    public String toString() {
        return "Job{" +
                "requiredSkills=" + requiredSkills +
                ", title='" + title + '\'' +
                ", company='" + company + '\'' +
                ", description='" + description + '\'' +
                ", datePosted='" + datePosted + '\'' +
                ", applicationSrc='" + applicationSrc + '\'' +
                ", id=" + id +
                '}';
    }
}