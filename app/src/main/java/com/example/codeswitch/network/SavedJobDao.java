package com.example.codeswitch.network;

import com.example.codeswitch.model.Job;
import com.example.codeswitch.model.SavedJob;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SavedJobDao {
    /**
     * Get list of ALL jobs that this specific user saved.
     * It should be split by qualified or not based on Tim's algo.
     * @param id user id
     * @return list of saved jobs
     */
    @GET("saved_jobs/user/{user_id}")
    Call<List<SavedJob>> getUserSavedJobs(@Path("user_id") int id);

    /**
     * Add jobs to saved jobs.
     * @param userId
     * @param jobId
     * @return
     */
    @FormUrlEncoded
    @POST("saved_jobs")
    Call<Job> saveJob(@Field("user") int userId, @Field("job") int jobId);

    /**
     * When you click apply job, we should change is_applied=True.
     * @param id
     * @param has_applied
     * @return
     */
    @FormUrlEncoded
    @PATCH("saved_jobs/{id}")
    Call<SavedJob> applyJob(@Path("id") int id, @Field("has_applied") Boolean has_applied);
}
