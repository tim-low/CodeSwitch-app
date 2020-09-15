package com.example.codeswitch.network;

import com.example.codeswitch.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface UserDao {
    /**
     * Trigger if you want to get a user's details based on their ID.
     * Details include things like skills.
     *
     * @param id user id
     * @return user object returned
     */
    @GET("users/{user_id}")
    Call<User> getUserDetail(@Path("user_id") int id);

    /**
     * Trigger whenever the user wants to update his skills.
     *
     * @param id     Pass in the user's id that you wanna update
     * @param skills Pass in his new set of skills
     * @return Updated user object. REMEMBER to save user again in SharedPrefs so that the local app knows of this change as well
     */
    @FormUrlEncoded
    @PATCH("users/{user_id}")
    Call<User> updateUserSkills(@Path("user_id") int id,
                                @Field("skills") List<String> skills);
}
