package com.example.codeswitch.network;

import com.example.codeswitch.model.Job;
import com.example.codeswitch.model.SavedJob;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JobDao {
    @GET("query_jobs")
    Call<List<Job>> getJobBySearch(@Query("q") String q);

    @GET("jobs/{job_id}")
    Call<Job> getJob(@Path("job_id") int id);
}
