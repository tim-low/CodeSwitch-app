package com.example.codeswitch.network;

import com.example.codeswitch.model.AuthResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AuthResponseDao {

    /**
     * Trigger when logging in. Server will run checks.
     *
     * @param email    User inputted email in the login screen
     * @param password User inputted password in the login screen
     * @return Whether or not login was successful. AuthResponse object is returned.
     */
    @FormUrlEncoded
    @POST("users/login")
    Call<AuthResponse> loginUser(@Field("email") String email,
                                 @Field("password") String password);

    /**
     * Trigger when creating account. Server will run checks.
     * Frontend should do the confirm password check.
     *
     * @param email    User inputted email in the register screen
     * @param password User inputted password in the register screen
     * @return Whether or not the account creation was successful. AuthResponse object returned.
     */
    @FormUrlEncoded
    @POST("users/create-account")
    Call<AuthResponse> createAccount(@Field("email") String email,
                                     @Field("password") String password);
}
