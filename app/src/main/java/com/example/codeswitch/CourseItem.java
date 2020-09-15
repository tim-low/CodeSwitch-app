package com.example.codeswitch;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CourseItem {
    private int courseImageResource;
    private String courseTitleText;
    private String courseReferenceNumberText;
    private String courseOrganizationText;
    private String courseModeOfTrainingText;

    public CourseItem(int ImageResource, String TitleText, String ReferenceNumberText, String OrganizationText, String ModeOfTrainingText){
        courseImageResource = ImageResource;
        courseReferenceNumberText = ReferenceNumberText;
        courseTitleText = TitleText;
        courseOrganizationText = OrganizationText;
        courseModeOfTrainingText = ModeOfTrainingText;
    }

    public int getCourseImageResource(){
        return courseImageResource;
    }

    public String getReferenceNumberText(){
        return this.courseTitleText;
    }

    public String getCourseTitleText(){
        return this.courseTitleText;
    }

    public String getCourseOrganizationText(){
        return this.courseOrganizationText;
    }

    public String getCourseModeText(){
        return this.courseModeOfTrainingText;
    }
}
/*
System.out.println("referenceNumber: " + currentObject.getString("referenceNumber"));
        System.out.println("Organisation: " + currentObject.getString("trainingProviderAlias"));
        System.out.println("Title: " + currentObject.getString("title"));
        System.out.println("Mode: " + currentObject.getString("modeOfTrainings"));*/
