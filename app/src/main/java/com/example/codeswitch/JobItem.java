package com.example.codeswitch;

import java.util.List;

public class JobItem {
    private int jobImageResource;
    private String jobReferenceNumberText;
    private List<String> jobRequiredSkillsList;
    private String jobTitleText;
    private String jobCompanyText;
    private String jobDatePostedText;
    private boolean qualified;
    private int savedJobID;
    private boolean appliedStatus;

    public JobItem(int ImageResource, String ReferenceNumberText,
                   List<String> RequiredSkillsList,
                   String TitleText, String CompanyText, String JobDatePostedText,
                   boolean qualified, int savedJobID){
        this.appliedStatus = false;
        this.savedJobID = savedJobID;
        jobImageResource = ImageResource;
        jobRequiredSkillsList = RequiredSkillsList;
        jobReferenceNumberText = ReferenceNumberText;
        jobTitleText = TitleText;
        jobCompanyText = CompanyText;
        jobDatePostedText = JobDatePostedText;
        this.qualified = qualified;

    }

    public int getJobImageResource(){
        return jobImageResource;
    }

    public List<String> getJobRequiredSkillsList(){ return jobRequiredSkillsList; }

    public String getJobReferenceNumberText(){
        return jobReferenceNumberText;
    }

    public String getJobTitleText(){
        return this.jobTitleText;
    }

    public String getJobCompanyText(){
        return this.jobCompanyText;
    }

    public String getJobDatePostedText() { return this.jobDatePostedText; }

    public boolean isQualified() {
        return qualified;
    }

    public int getSavedJobID() {return savedJobID;}

    public Boolean getAppliedStatus() {return appliedStatus;}
    public void setAppliedStatus(Boolean appliedStatus) {this.appliedStatus = appliedStatus;}
}
