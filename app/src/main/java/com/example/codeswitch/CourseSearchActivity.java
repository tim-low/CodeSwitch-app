package com.example.codeswitch;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class CourseSearchActivity extends ModifiedActivity implements SearchActivity, CourseRecyclerViewAdapter.OnCourseListener {

    //set up API call
    Context thisContext = this;
    JSONArray searchResults = null;
    Intent thisIntent;

    //set up CourseItem objects
    private ArrayList<CourseItem> courseItems = new ArrayList<>();

    //set up RecyclerView
    private RecyclerView courseRecyclerView;
    private CourseRecyclerViewAdapter courseRecyclerAdapter;
    private RecyclerView.LayoutManager courseRecyclerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_search);
        thisIntent = getIntent();

        //set up searchView
        SearchView searchView = findViewById(R.id.course_search_view);

        //execute search
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchDisplayItems(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //implement recyclerview
        displayItems(courseItems);

        //get skill from jobDetails
        if (thisIntent.hasExtra("Skill"))
        {
            String keyword = thisIntent.getStringExtra("Skill");
            SearchView searchView1 = (SearchView) findViewById(R.id.course_search_view);
            searchView1.setQueryHint(keyword);
            fetchDisplayItems(keyword);
        }


        //set up bottomnavigationview
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        MenuItem menuItem = bottomNavigationView.getMenu().getItem(1);
        menuItem.setChecked(true);

        //display bottomNavigationView
        displayBottomNavigationView(bottomNavigationView);
    }

    @Override
    public void displayItems(ArrayList courseItems) {
        courseRecyclerView = findViewById(R.id.recyclerView_courseSearch);
        courseRecyclerView.setHasFixedSize(true);
        courseRecyclerManager = new LinearLayoutManager(this);
        courseRecyclerAdapter = new CourseRecyclerViewAdapter(courseItems, this);   //pass the interface to the adapter

        courseRecyclerView.setLayoutManager(courseRecyclerManager);
        courseRecyclerView.setAdapter(courseRecyclerAdapter);
    }

    public void displayBottomNavigationView(BottomNavigationView bottomNavigationView){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.ic_job_search:
                        Intent intent_toJS = new Intent(CourseSearchActivity.this, JobSearchActivity.class);
                        startActivity(intent_toJS);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        break;
                    case R.id.ic_course_search:
                        //already here
                        return true;
                    case R.id.ic_saved_jobs:
                        Intent intent_toSJ = new Intent(CourseSearchActivity.this, SavedJobsActivity.class);
                        startActivity(intent_toSJ);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        return true;
                    case R.id.ic_profile:
                        Intent intent_toEP = new Intent(CourseSearchActivity.this, EditProfileActivity.class);
                        startActivity(intent_toEP);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        return true;
                }

                return false;
            }
        });
    }

    //get api courses
    @Override
    public void fetchDisplayItems(String keyword)
    {
        TextView blankText = findViewById(R.id.course_search_blank_text);
        blankText.setText("");
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        courseItems.clear();
        courseRecyclerAdapter.notifyDataSetChanged();

        RequestQueue ExampleRequestQueue = Volley.newRequestQueue(thisContext);

        Uri.Builder builder = new Uri.Builder();

        //https://w5fe0239ih.execute-api.us-east-1.amazonaws.com/default/CodeSwitch?searchOrDetails=details&referenceNumber=NTU-200604393R-01-NC-IT1024

        //http://codeswitch-rest-api.herokuapp.com/courses

        builder.scheme("https")
                .authority("w5fe0239ih.execute-api.us-east-1.amazonaws.com")
                .appendPath("default")
                .appendPath("CodeSwitch")
                .appendQueryParameter("searchOrDetails", "search")
                .appendQueryParameter("keyword" , keyword);

        String myUrl = builder.build().toString();

//        03-28 10:38:04.828 17475-17475/com.example.codeswitch I/System.out: HTTPS Error: org.json.JSONException: Value {"statusCode":200,"body":"[{\"referenceNumber\": \"SCN-200604346E-01-CRS-N-0046926\", \"trainingProviderAlias\": \"NATIONAL UNIVERSITY OF SINGAPORE\", \"title\": \"Water Quality Engineering\", \"displayImageName\": \"19_5\", \"modeOfTrainings\": \"Part Time\"}, {\"referenceNumber\": \"SP-T08GB0056A-01-SP-502439\", \"trainingProviderAlias\": \"SINGAPORE POLYTECHNIC\", \"title\": \"Water Efficiency Manager\", \"displayImageName\": \"6_6\", \"modeOfTrainings\": \"Part Time\"}, {\"referenceNumber\": \"SP-T08GB0056A-01-CRS-N-0014513\", \"trainingProviderAlias\": \"SINGAPORE POLYTECHNIC\", \"title\": \"Water Efficiency Manager\", \"displayImageName\": \"37_10\", \"modeOfTrainings\": \"Part Time\"}, {\"referenceNumber\": \"NYP-T08GB0032G-01-CL1008\", \"trainingProviderAlias\": \"NANYANG POLYTECHNIC\", \"title\": \"Introduction to Air & Water Pollution Control Analytics\", \"displayImageName\": \"35_5\", \"modeOfTrainings\": \"Part Time\"}, {\"referenceNumber\": \"SCN-T08GB0032G-01-CRS-N-0049212\", \"trainingProviderAlias\": \"NANYANG POLYTECHNIC\", \"title\": \"Introduction to Air & Water Pollution Control Analytics\", \"displayImageName\": \"25_1\", \"modeOfTrainings\": \"Full Time\"}, {\"referenceNumber\": \"ITE-T08GB0022B-01-CB1006CS\", \"trainingProviderAlias\": \"INSTITUTE OF TECHNICAL EDUCATION\", \"title\": \"CoC in Plumbing Basics\", \"displayImageName\": \"19_11\", \"modeOfTrainings\": \"Part Time\"}, {\"referenceNumber\": \"SCN-200604346E-01-CRS-N-0046930\", \"trainingProviderAlias\": \"NATIONAL UNIVERSITY OF SINGAPORE\", \"title\": \"Water Resources Engineering\", \"displayImageName\": \"19_3\", \"modeOfTrainings\": \"Part Time\"}, {\"referenceNumber\": \"SCN-200604346E-01-CRS-N-0051216\", \"trainingProviderAlias\": \"NATIONAL UNIVERSITY OF SINGAPORE\", \"title\": \"Specialist Certificate Course in Water and the Environment\", \"displayImageName\": \"19_7\", \"modeOfTrainings\": \"Part Time\"}, {\"referenceNumber\": \"SCN-200604346E-01-CRS-N-0047001\", \"trainingProviderAlias\": \"NATIONAL UNIVERSITY OF SINGAPORE\", \"title\": \"Introduction to Environmental Engineering\", \"displayImageName\": \"19_3\", \"modeOfTrainings\": \"Part Time\"}, {\"referenceNumber\": \"SCN-200604346E-01-CRS-N-0046818\", \"trainingProviderAlias\": \"NATIONAL UNIVERSITY OF SINGAPORE\", \"title\": \"Membrane Science and Engineering\", \"displayImageName\": \"19_1\", \"modeOfTrainings\": \"Part Time\"}]"} of type org.json.JSONObject cannot be converted to JSONArray


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, myUrl, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            searchResults = response;
                            Log.d("DEBUG", "Response: "+response.toString());
//                            03-28 10:44:01.250 17639-17639/com.example.codeswitch W/System.err: org.json.JSONException: Value [{"referenceNumber": "SCN-200604346E-01-CRS-N-0046926", "trainingProviderAlias": "NATIONAL UNIVERSITY OF SINGAPORE", "title": "Water Quality Engineering", "displayImageName": "19_5", "modeOfTrainings": "Part Time"}, {"referenceNumber": "SP-T08GB0056A-01-SP-502439", "trainingProviderAlias": "SINGAPORE POLYTECHNIC", "title": "Water Efficiency Manager", "displayImageName": "6_6", "modeOfTrainings": "Part Time"}, {"referenceNumber": "SP-T08GB0056A-01-CRS-N-0014513", "trainingProviderAlias": "SINGAPORE POLYTECHNIC", "title": "Water Efficiency Manager", "displayImageName": "37_10", "modeOfTrainings": "Part Time"}, {"referenceNumber": "NYP-T08GB0032G-01-CL1008", "trainingProviderAlias": "NANYANG POLYTECHNIC", "title": "Introduction to Air & Water Pollution Control Analytics", "displayImageName": "35_5", "modeOfTrainings": "Part Time"}, {"referenceNumber": "SCN-T08GB0032G-01-CRS-N-0049212", "trainingProviderAlias": "NANYANG POLYTECHNIC", "title": "Introduction to Air & Water Pollution Control Analytics", "displayImageName": "25_1", "modeOfTrainings": "Full Time"}, {"referenceNumber": "ITE-T08GB0022B-01-CB1006CS", "trainingProviderAlias": "INSTITUTE OF TECHNICAL EDUCATION", "title": "CoC in Plumbing Basics", "displayImageName": "19_11", "modeOfTrainings": "Part Time"}, {"referenceNumber": "SCN-200604346E-01-CRS-N-0046930", "trainingProviderAlias": "NATIONAL UNIVERSITY OF SINGAPORE", "title": "Water Resources Engineering", "displayImageName": "19_3", "modeOfTrainings": "Part Time"}, {"referenceNumber": "SCN-200604346E-01-CRS-N-0051216", "trainingProviderAlias": "NATIONAL UNIVERSITY OF SINGAPORE", "title": "Specialist Certificate Course in Water and the Environment", "displayImageName": "19_7", "modeOfTrainings": "Part Time"}, {"referenceNumber": "SCN-200604346E-01-CRS-N-0047001", "trainingProviderAlias": "NATIONAL UNIVERSITY OF SINGAPORE", "title": "Introduction to Environmental Engineering", "displayImageName": "19_3", "modeOfTrainings": "Part Time"}, {"referenceNumber": "SCN-200604346E-01-CRS-N-0046818", "trainingProviderAlias": "NATIONAL UNIVERSITY OF SINGAPORE", "title": "Membrane Science and Engineering", "displayImageName": "19_1", "modeOfTrainings": "Part Time"}] at body of type java.lang.String cannot be converted to JSONArray


                            courseItems.clear();
                            for (int i = 0; i<searchResults.length();i++){

                                courseItems.add(
                                        new CourseItem(
                                                R.drawable.sample_tech_image,
                                                searchResults.getJSONObject(i).getString("title"),
                                                searchResults.getJSONObject(i).getString("referenceNumber"),
                                                searchResults.getJSONObject(i).getString("trainingProviderAlias"),
                                                searchResults.getJSONObject(i).getJSONArray("modeOfTrainings").getJSONObject(0).getString("description")));
                            }

                            courseRecyclerAdapter.notifyDataSetChanged();


                            System.out.println(courseItems.toString());
                            findViewById(R.id.progressBar).setVisibility(View.GONE);

                        } catch (Exception e) {
                            e.printStackTrace();
                            TextView blankText = findViewById(R.id.course_search_blank_text);
                            blankText.setText("We did not find any Courses with that Search.");
                            findViewById(R.id.progressBar).setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("HTTPS Error: " + error.getMessage());

                        TextView blankText = findViewById(R.id.course_search_blank_text);
                        blankText.setText("We did not find any Courses with that Search.");
                        findViewById(R.id.progressBar).setVisibility(View.GONE);
                    }
                });

        ExampleRequestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onCourseClick(int position) {
        try {
            Log.d("onCourseClick", searchResults.getJSONObject(position).getString("title")+ " at Index: " + position);
            Intent goToCourseDetails = new Intent(CourseSearchActivity.this, CourseDetailsActivity.class);
            String str = searchResults.getJSONObject(position).getString("referenceNumber");
            goToCourseDetails.putExtra("referenceNumber" , str);
            startActivity(goToCourseDetails);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
