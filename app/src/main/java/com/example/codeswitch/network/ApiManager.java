package com.example.codeswitch.network;

import android.util.Log;
import android.widget.Toast;

import com.example.codeswitch.model.BaseObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {
    private static Retrofit instance;
    private static final String BASE_URL = "https://codeswitch-rest-api.herokuapp.com/";
//    private static final String BASE_URL = "http://10.0.2.2:8000/";

    public static Retrofit getInstance() {
        if (instance == null) {
            instance = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return instance;
    }

    public static <T> void callApi(Call<T> call, final CustomCallback callback) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (callback != null) {
                    callback.onResponse(response.body());
                }
            }
            @Override
            public void onFailure(Call<T> call, Throwable t) {
                Log.d("Debug", "Failure");
                t.printStackTrace();
            }
        });
    }
}