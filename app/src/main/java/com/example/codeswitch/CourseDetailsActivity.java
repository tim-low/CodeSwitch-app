package com.example.codeswitch;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.codeswitch.model.Skill;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class CourseDetailsActivity extends ModifiedActivity implements DetailsActivity {

    private Context thisContext = this;
    private Intent thisIntent;
    private String referenceNumber;
    private JSONObject searchResults;
    Button backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_details);

        TextView contentBox = (TextView) findViewById(R.id.courseDescriptionText);
        contentBox.setMovementMethod(new ScrollingMovementMethod());

        thisIntent = getIntent();

        // TODO : Remove once intent added in course search

        referenceNumber = thisIntent.getStringExtra("referenceNumber");
//        referenceNumber = "NTU-200604393R-01-NC-IT1024";
        Log.i ("Reference Num: ", referenceNumber);
//
        getDetails();
    }


    public void getDetails() {
        RequestQueue ExampleRequestQueue = Volley.newRequestQueue(thisContext);

        Uri.Builder builder = new Uri.Builder();

        //https://w5fe0239ih.execute-api.us-east-1.amazonaws.com/default/CodeSwitch?searchOrDetails=details&referenceNumber=NTU-200604393R-01-NC-IT1024

        builder.scheme("https")
                .authority("w5fe0239ih.execute-api.us-east-1.amazonaws.com")
                .appendPath("default")
                .appendPath("CodeSwitch")
                .appendQueryParameter("searchOrDetails", "details")
                .appendQueryParameter("referenceNumber", referenceNumber);

        String myUrl = builder.build().toString();
        findViewById(R.id.progressBar3).setVisibility(View.VISIBLE);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, myUrl, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        searchResults = response;
//                        Log.i("Response: ", response.toString());
//                        try
//                        {
                            display();
//                            Log.i("Course Details", response.toString());
//                            response.getString("referenceNumber");
//                            printPrompt(R.id.courseName, searchResults.getString("title"));
//                            printPrompt(R.id.courseProvider, searchResults.getString("trainingProviderAlias"));
//                            printPrompt(R.id.courseDescriptionText, searchResults.getString("content"));
//                            printPrompt(R.id.courseURLButton, searchResults.getString("url"));
//                            printPrompt(R.id.phoneCourseDetails, "Phone:\n" + searchResults.getString("phoneNumber"));
//                            printPrompt(R.id.emailCourseDetails, "Email:\n" + searchResults.getString("email"));
//                            printPrompt(R.id.datePosted, "Date Posted: " + searchResults.getString("createDate").substring(0,10));
//                            printPrompt(R.id.priceCourseDetails, "Cost per Trainee: SGD " + searchResults.getString("totalCostOfTrainingPerTrainee"));

//                        }
//                        catch (JSONException e) {
//                            e.printStackTrace();
                        findViewById(R.id.progressBar3).setVisibility(View.GONE);
                        TextView pricingTitle = findViewById(R.id.pricingTitle);
                        pricingTitle.setText("Pricing");
                        TextView descriptionTitle = findViewById(R.id.descriptionTitle);
                        descriptionTitle.setText("Description");
                        TextView contactTitle = findViewById(R.id.contactTitle);
                        contactTitle.setText("Contact");


//                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("HTTPS Error: " + error.getMessage());
                    }
                });

        ExampleRequestQueue.add(jsonObjectRequest);
    }

    public void display() {
        try {
            System.out.println(searchResults.toString());

            printPrompt(R.id.courseName, searchResults.getString("title"));
            printPrompt(R.id.courseProvider, searchResults.getString("trainingProviderAlias"));
            printPrompt(R.id.courseDescriptionText, searchResults.getString("content"));

            String phone = searchResults.getJSONArray("contactPerson").getJSONObject(0).optJSONObject("telephone").getString("number");
            if (phone.equals("0"))
                phone = "Not Available";

            printPrompt(R.id.phoneCourseDetails, "Phone:\n" + phone);
            printPrompt(R.id.emailCourseDetails, "Email:\n" + searchResults.getJSONArray("contactPerson").getJSONObject(0).optJSONObject("email").getString("full"));
            printPrompt(R.id.datePosted, "Date Posted: " + searchResults.optJSONObject("meta").getString("createDate").substring(0, 10));
            printPrompt(R.id.priceCourseDetails, "Cost per Trainee: SGD " + searchResults.getString("totalCostOfTrainingPerTrainee"));

//            filteredData['phoneNumber'] = details['contactPerson'][0]['telephone']['number']
//            filteredData['email'] = details['contactPerson'][0]['email']['full']
//            filteredData['modeOfTrainings'] = details['modeOfTrainings'][0]['description']
//            filteredData['createDate'] = details['meta']['createDate']
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        backButton = findViewById(R.id.courseDetailsBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void onClickURL (View view){

        String thisUrl = null;
        try {
            thisUrl = searchResults.getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        Log.i("Course Details", "Link Clicked: " + thisUrl);

        Intent browserIntent = new Intent (Intent.ACTION_VIEW, Uri.parse(thisUrl));
        Toast.makeText(thisContext, "Redirecting to website", Toast.LENGTH_SHORT).show();
        startActivity (browserIntent);
    }
}
