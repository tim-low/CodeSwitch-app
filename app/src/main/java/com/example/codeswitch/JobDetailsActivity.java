package com.example.codeswitch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.codeswitch.model.Job;
import com.example.codeswitch.model.User;
import com.example.codeswitch.network.ApiManager;
import com.example.codeswitch.network.CustomCallback;
import com.example.codeswitch.network.DaoFactory;
import com.example.codeswitch.network.SavedJobDao;

import java.util.ArrayList;
import java.util.List;

public class JobDetailsActivity extends ModifiedActivity  implements DetailsActivity {

    Job thisJob;
    String jobTitle, jobDescription, companyName, jobURL, date_posted;
    List<String> requiredSkills, unacquiredSkills, acquiredSkills, acquiredRelevantSkills;
    Boolean acquired;
    Intent intent;
    TextView jobTitleTextView, jobDescriptionTextView, companyNameTextView, jobURLTextView, picture_urlTextView, dateTextView;
    Button backButton;
    User user;
    int jobId;
    androidx.gridlayout.widget.GridLayout acquiredSkillsGridLayout, unacquiredSkillsGridLayout;
    private SavedJobDao dao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // call the super class onCreate to complete the creation of activity like
        // the view hierarchy
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_details);
        DaoFactory daoFactory = new DaoFactory();
        dao = daoFactory.getSavedJobDao();
        intent = getIntent();
        thisJob = new Job();
        TextView descriptionBox = (TextView) findViewById(R.id.jobDescriptionText);
        descriptionBox.setMovementMethod(new ScrollingMovementMethod());

        thisJob = (Job)intent.getSerializableExtra("serializedJob");
        Log.i("test", thisJob.getId()+" "+thisJob.getTitle());
        Log.d("DEBUG", Boolean.toString(thisJob==null) );
        Log.i ("Reference Num: ", thisJob.getTitle());
        getDetails();
    }

    public void getDetails(){
        requiredSkills = new ArrayList<>();
        unacquiredSkills = new ArrayList<>();
        acquiredSkills = new ArrayList<>();
        acquiredRelevantSkills = new ArrayList<>();
        jobTitle = thisJob.getTitle();
        jobDescription = thisJob.getDescription();
        companyName = thisJob.getCompany();
        jobURL = thisJob.getApplicationSrc();
        date_posted = thisJob.getDatePosted();
        requiredSkills = thisJob.getRequiredSkills();
        jobId = thisJob.getId();
        System.out.println(jobId);
        user = getUserFromPrefs();
        acquiredSkills = user.getSkills();
        //get the unacquired skills

        for(String requiredSkill : requiredSkills){
            acquired = false;
            for(String acquiredSkill: acquiredSkills){
                if(acquiredSkill.contentEquals(requiredSkill)){
                    acquired = true;
                }
            }
            if(!acquired){
                unacquiredSkills.add(requiredSkill);
            }
        }
        //get acquired skills that are relevant
        for(String requiredSkill : requiredSkills){
            acquired = true;
            for(String unacquiredSkill: unacquiredSkills){
                if(unacquiredSkill.contentEquals(requiredSkill)){
                    acquired = false;
                }
            }
            if(acquired){
                acquiredRelevantSkills.add(requiredSkill);
            }
        }

        display();

    }

    public void display() {
        jobTitleTextView = findViewById(R.id.jobTitleText);
        jobTitleTextView.setText(jobTitle);
        companyNameTextView = findViewById(R.id.companyNameText);
        companyNameTextView.setText(companyName);
        jobDescriptionTextView = findViewById(R.id.jobDescriptionText);
        jobDescriptionTextView.setText(jobDescription);
        dateTextView = findViewById(R.id.datePostedText);
        dateTextView.setText(date_posted);
        // dynamically generate requiredSkills
        // i is counter for ID generation
        int i = 0;
        acquiredSkillsGridLayout = findViewById(R.id.acquiredSkillsGridLayout);
        // set the number of columns used by the grid layout
        acquiredSkillsGridLayout.setColumnCount(2);
        //generate buttons based on the number of skills
        for (final String acquiredRelevantSkill : acquiredRelevantSkills) {
            Button btn = new Button(this);
            btn.setId(i);
            btn.setTag(acquiredRelevantSkill);
            btn.setText(acquiredRelevantSkill);

            acquiredSkillsGridLayout.addView(btn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    Intent requiredSkillIntent = new Intent(JobDetailsActivity.this, CourseSearchActivity.class);
                    requiredSkillIntent.putExtra("Skill",acquiredRelevantSkill);
                    startActivity(requiredSkillIntent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                }
            });
            i++;
        }
        //generate unacquired skills
        unacquiredSkillsGridLayout = findViewById(R.id.unacquiredSkillsGridLayout);
        // set the number of columns used by the grid layout
        unacquiredSkillsGridLayout.setColumnCount(2);
        //generate buttons based on the number of skills
        for (final String unacquiredSkill : unacquiredSkills) {
            Button btn = new Button(this);
            btn.setId(i);
            btn.setTag(unacquiredSkill);
            btn.setText(unacquiredSkill);

            unacquiredSkillsGridLayout.addView(btn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    Intent requiredSkillIntent = new Intent(JobDetailsActivity.this, CourseSearchActivity.class);
                    requiredSkillIntent.putExtra("Skill",unacquiredSkill);
                    startActivity(requiredSkillIntent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                }
            });
            i++;
        }
        backButton = findViewById(R.id.jobDetailsBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    //button to go to website
    public void onClickURL (View view){

        Log.i("Job Details", "Link Clicked: " + jobURL);

        Intent browserIntent = new Intent (Intent.ACTION_VIEW, Uri.parse(jobURL));
        Toast.makeText(this, "Redirecting to website", Toast.LENGTH_SHORT).show();
        startActivity (browserIntent);
    }
    //button to save job
    public void onClickSaveJob(View view){
        System.out.println(user.getId());
        System.out.println(jobId);
        ApiManager.callApi(dao.saveJob(user.getId(), jobId), new CustomCallback<Job>() {
            @Override
            public void onResponse(Job response) {
                if (response != null) {
                    Log.d("Debug", response.toString());
                    Toast.makeText(JobDetailsActivity.this, "successfully saved", Toast.LENGTH_LONG).show();
                }
                else {
                    Log.d("Debug", "Response was null");
                }
            }
        });
    }

    public void saveJob(){

    }




}
