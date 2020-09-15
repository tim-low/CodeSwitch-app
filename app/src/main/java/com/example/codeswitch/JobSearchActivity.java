package com.example.codeswitch;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.codeswitch.model.Job;
import com.example.codeswitch.model.Skill;
import com.example.codeswitch.model.User;
import com.example.codeswitch.network.ApiManager;
import com.example.codeswitch.network.CustomCallback;
import com.example.codeswitch.network.DaoFactory;
import com.example.codeswitch.network.JobDao;
import com.example.codeswitch.network.SkillDao;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.io.*;

public class JobSearchActivity extends ModifiedActivity implements SearchActivity, Serializable, JobRecyclerViewAdapter.OnJobListener {
    //set up JobItem objects
    private ArrayList<JobItem> jobItems = new ArrayList<>();
    private ArrayList<JobItem> filteredJobItems = new ArrayList<>();
    private ArrayList <String> fieldsToAdd = new ArrayList<String>();
    private List<Job> jobList = new ArrayList();
    private List<Job> filteredJobList = new ArrayList();
    private User thisUser;

    //set up RecyclerViews
    private RecyclerView jobRecyclerView;
    private JobRecyclerViewAdapter jobRecyclerAdapter;
    private RecyclerView.LayoutManager jobRecyclerManager;

    //set up button menu selection
    Button mOrder;  //skillSelectButton
    String[] menuListItems; //skillList
    boolean[] checkedItems;   //selectedSkillsBoolean
    ArrayList<Integer> mUserItems = new ArrayList<>();  //userSelectedSkills



    SkillDao skillDao;
    JobDao jobDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_search);

        thisUser = getUserFromPrefs();

        //Side Menu Button
        mOrder = findViewById(R.id.job_search_side_menu);

        DaoFactory daoFactory = new DaoFactory();
        skillDao = daoFactory.getSkillDao();
        jobDao = daoFactory.getJobDao();



        //Get List of Selected Skills
        ApiManager.callApi(skillDao.getSkillList(), new CustomCallback<List<Skill>>() {
                    @Override
                    public void onResponse(List<Skill> response) {
                        if (response != null){
                            menuListItems = new String[response.size()];
                            for(int i=0; i<response.size(); i++){
                                menuListItems[i] = response.get(i).getName();
                            }
                            checkedItems = new boolean[menuListItems.length];
                        }
                        else{
                            Log.d("Debug", "Response was null");
                        }
                    }
        });

        //Implement side menu
        implementSkillSelectMenu();

        //Set up SearchView
        SearchView searchView = findViewById(R.id.job_search_view);

        //Implement Search
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                findViewById(R.id.progressBar2).setVisibility(View.VISIBLE);
                TextView blankText = findViewById(R.id.job_search_blank_text);
                blankText.setText("");

                fetchDisplayItems(query);
                jobRecyclerAdapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        //recyclerview
        displayItems(filteredJobItems);


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        MenuItem menuItem = bottomNavigationView.getMenu().getItem(0);
        menuItem.setChecked(true);

        displayBottomNavigationView(bottomNavigationView);

    }

    @Override
    public void displayItems(ArrayList filteredJobItems) {
        jobRecyclerView = findViewById(R.id.recyclerView_jobSearch);
        jobRecyclerView.setHasFixedSize(true);
        jobRecyclerManager = new LinearLayoutManager(this);
        jobRecyclerAdapter = new JobRecyclerViewAdapter(filteredJobItems, this);   //pass the interface to the adapter

        jobRecyclerView.setLayoutManager(jobRecyclerManager);
        jobRecyclerView.setAdapter(jobRecyclerAdapter);
    }

    private void displayBottomNavigationView(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.ic_job_search:
                        //already here
                        break;
                    case R.id.ic_course_search:
                        Intent intent_toCS = new Intent(JobSearchActivity.this, CourseSearchActivity.class);
                        startActivity(intent_toCS);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        break;
                    case R.id.ic_saved_jobs:
                        Intent intent_toSJ = new Intent(JobSearchActivity.this, SavedJobsActivity.class);
                        startActivity(intent_toSJ);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        break;
                    case R.id.ic_profile:
                        Intent intent_toEP = new Intent(JobSearchActivity.this, EditProfileActivity.class);
                        startActivity(intent_toEP);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        break;
                }

                return false;
            }
        });
    }

    private void implementSkillSelectMenu() {
        mOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set up side menu
                AlertDialog.Builder skillSelectBuilder = new AlertDialog.Builder(JobSearchActivity.this);
                skillSelectBuilder.setTitle("Select Skills to Filter Search By");
                //Create a menu with multiple choice items. Pass in the list of items, and an array to track if they have been checked.
                skillSelectBuilder.setMultiChoiceItems(menuListItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        //implemented checkboxes
                        if (isChecked) {
                            Log.d("SideMenu", position + " was checked");
                            if (!mUserItems.contains(position)) {
                                mUserItems.add(position);
                            }
                        } else if (mUserItems.contains(position)) {
                            mUserItems.remove((Integer.valueOf(position)));
                        }
                    }
                });

                //set up buttons
                skillSelectBuilder.setCancelable(false);

                //Implement OK Button
                skillSelectBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!mUserItems.isEmpty()) {
                            //Log.d("DEBUG", "mUserItems is not Empty");
                            filteredJobItems.clear();
                            filteredJobList.clear();
                            fieldsToAdd.clear();
                            for (int i = 0; i < mUserItems.size(); i++) {
                                fieldsToAdd.add(menuListItems[mUserItems.get(i)]);

                                Log.d("DEBUG", "Menu Items Selected:" + menuListItems[mUserItems.get(i)]);

                            }

                            //filter Job Items by comparing two lists
                            Log.d("DEBUG", "JobList SizeB: " + jobList.size());
                            for (int i = 0; i < jobItems.size(); i++) {
                                Set<String> intersection = new HashSet<String>(jobItems.get(i).getJobRequiredSkillsList());
                                intersection.retainAll(fieldsToAdd);
                                if (intersection.size() > 0) {
                                    filteredJobItems.add(jobItems.get(i));
                                    filteredJobList.add(jobList.get(i));
                                }

                            }
                            jobRecyclerAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("DEBUG", "Checkboxes are empty");
                            filteredJobItems.clear();
                            filteredJobItems.addAll(jobItems);
                            filteredJobList.clear();
                            filteredJobList.addAll(jobList);
                            jobRecyclerAdapter.notifyDataSetChanged();
                        }

                    }
                });

                //implement Cancel Button
                skillSelectBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                //implement Clear All Button
                skillSelectBuilder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < checkedItems.length; i++) {
                            checkedItems[i] = false;
                            Log.d("Debug", "checkedItems Length:" + checkedItems.length);
                            mUserItems.clear();
                            //restore full list of jobItems


                        }
                        filteredJobItems.clear();
                        filteredJobItems.addAll(jobItems);
                        filteredJobList.clear();
                        filteredJobList.addAll(jobList);
                        jobRecyclerAdapter.notifyDataSetChanged();
                    }

                });

                //Create the Dialog Box
                AlertDialog mDialog = skillSelectBuilder.create();
                //Display Dialog Box
                mDialog.show();
            }
        });
    }

    private void applyCheckboxMenuFilter() {
/*        filteredJobItems.clear();
        filteredJobList.clear();*/

        if (!mUserItems.isEmpty()) {
            Log.d("DEBUG", "mUserItems is not Empty");

            filteredJobItems.clear();
            filteredJobList.clear();
            fieldsToAdd.clear();
            for (int i = 0; i < mUserItems.size(); i++) {
                fieldsToAdd.add(menuListItems[mUserItems.get(i)]);  //fieldsToAdd contains Strings of selected skills
            }
            Log.d("DEBUG", "JobItems.size: "+jobItems.size());
            //filter Job Items by comparing two lists (does nothing right now)
            for (int i = 0; i < jobItems.size(); i++) {
                Set<String> intersection = new HashSet<String>(jobItems.get(i).getJobRequiredSkillsList());
                intersection.retainAll(fieldsToAdd);
                if (intersection.size() > 0){
                    Log.d("intersection", "Job Item contains " + fieldsToAdd + ", adding to filteredList");
                    filteredJobItems.add(jobItems.get(i));
                    Log.d("howManyItems", "item " + i + " of " + jobItems.size() + " added;");
                    filteredJobList.add(jobList.get(i));
                }
            }
            jobRecyclerAdapter.notifyDataSetChanged();
        }
        else{
            Log.d("DEBUG", "Checkboxes are empty");
            filteredJobItems.clear();
            filteredJobItems.addAll(jobItems);
            filteredJobList.clear();
            filteredJobList.addAll(jobList);
            jobRecyclerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void fetchDisplayItems(String query) {

        //blankText.setText("Now Searching...");
        TextView blankText = findViewById(R.id.job_search_blank_text);

        blankText.setText("");

        jobItems.clear();
        jobList.clear();
        //actual
        ApiManager.callApi(jobDao.getJobBySearch(query), new CustomCallback<List<Job>>() {
            @Override
            public void onResponse(List<Job> response) {

                jobList.addAll(response);
                filteredJobList.addAll(response);
                if (response != null) {
                    int i = 0;
                    for (Job job: response)
                    {
                        //Log.d("Debug", job.toString());
                        jobItems.add(
                                new JobItem(
                                        R.drawable.job_img,
                                        String.valueOf(job.getId()),
                                        job.getRequiredSkills(),
                                        job.getTitle(),
                                        job.getCompany(),
                                        job.getDatePosted(),
                                        checkIfQualified(job.getRequiredSkills()),
                                        -1
                                )
                        );
                    }
                    filteredJobItems.addAll(jobItems);

                    applyCheckboxMenuFilter();

                    TextView resultText = findViewById(R.id.job_search_blank_text);
                    if(filteredJobItems.size() > 0)

                        resultText.setText("");
                    else
                        resultText.setText("We did not find any Jobs with that Search.");

                    jobRecyclerAdapter.notifyDataSetChanged();
                    findViewById(R.id.progressBar2).setVisibility(View.GONE);
                }
                else {
                    Log.d("Debug", "Response was null");

                }

            }
        });


    }

    private boolean checkIfQualified(List<String> jobSkills)
    {
        Set<String> jobSkillsSet = new HashSet<String>(jobSkills);
        Log.d("DEBUG", "jobSkills: " + jobSkills.toString());
        Set<String> userSkills = new HashSet<String>(thisUser.getSkills());
        Log.d("DEBUG", "userSkills: " + thisUser.getSkills().toString());

        jobSkillsSet.removeAll(userSkills);
        if (jobSkillsSet.size()==0)
        {

            Log.d("DEBUG", "qualified");
            return true;
        }

        else {
            Log.d("DEBUG", "not qualified");
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onJobClick(int position) throws IOException {
        //when you click on a job
        //try {

            Intent goToJobDetails = new Intent(JobSearchActivity.this, JobDetailsActivity.class);
            Job serializableJob = filteredJobList.get(position);
            Log.i("Cal", serializableJob.getId()+"");
            goToJobDetails.putExtra("serializedJob",serializableJob);
            Log.d("DEBUG Before", Boolean.toString(serializableJob==null) );
            startActivity(goToJobDetails);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        /*}
        //when json has been gotten from cal
        catch (JSONException e) {
            e.printStackTrace();
        }*/
    }


}
