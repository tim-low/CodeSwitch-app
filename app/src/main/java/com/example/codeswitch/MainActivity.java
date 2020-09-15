package com.example.codeswitch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.codeswitch.model.AuthResponse;
import com.example.codeswitch.network.ApiManager;
import com.example.codeswitch.network.AuthResponseDao;
import com.example.codeswitch.network.CustomCallback;
import com.example.codeswitch.network.DaoFactory;

public class MainActivity extends ModifiedActivity {
    private String email;
    private String password;
    private AuthResponseDao dao;

    Context thisContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // This code must be here if you want to use the API.
        DaoFactory daoFactory = new DaoFactory();
        dao = daoFactory.getAuthResponseDao();

        Log.d("Debug", "Hello!");
//        ApiTest.testGetJobs();
    }

    public void onLoginClick(View view) {
//        email = "cal@example.com";
        email = getEditText(R.id.email_login_input);
//        password = "Cal12345";
        password = getEditText(R.id.password_login_input);

        authenticateLogin(email, password);
    }

    public void onRegisterNewClick(View view) {
        Intent k = new Intent(this, CreateAccountActivity.class);
        startActivity(k);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    /**
     * Logs the user into the app. Validity check is done by server.
     * @param email User's inputted email
     * @param password User's inputted password
     */
    private void authenticateLogin(String email, String password) {
        ApiManager.callApi(dao.loginUser(email, password), new CustomCallback<AuthResponse>() {
            @Override
            public void onResponse(AuthResponse response) {
                if (response.getSuccess()) {
                    Log.d("Debug", response.toString());

                    //go to job search - tim, yh details
                    try {
                        saveUserToPrefs(response.getUser());
                        Intent k = new Intent(MainActivity.this, JobSearchActivity.class);
                        startActivity(k);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    //end tim

                } else {
                    Toast.makeText(thisContext, response.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
