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

public class CreateAccountActivity extends ModifiedActivity {

    private String TAG = "CreateAccount";

    private String newEmail;
    private String newPassword;
    private String newConfirmPassword;
    private AuthResponseDao dao;
    private Context thisContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        DaoFactory daoFactory = new DaoFactory();
        dao = daoFactory.getAuthResponseDao();
    }

    public void onRegister (View view)
    {
        newEmail = getTextInputEditText(R.id.email_login_input);
        newPassword = getTextInputEditText(R.id.password_login_input);
        newConfirmPassword = getTextInputEditText(R.id.password_login_input2);

        if (newEmail.isEmpty() || newPassword.isEmpty() || newConfirmPassword.isEmpty())
        {
            Toast.makeText(this, "Complete All Fields", Toast.LENGTH_SHORT).show();
//            Log.i(TAG, "Complete All Fields");
            return;
        }
        else if (!newPassword.equals(newConfirmPassword))
        {
            Toast.makeText(this, "Confirmed Password is different", Toast.LENGTH_SHORT).show();
//            Log.i(TAG, "Confirmed Password is different");
            return;
        }

        // TODO : Authenticate validity of new password

        authenticateRegisterNew(newEmail, newPassword);


    }

    private void authenticateRegisterNew(String email, String password) {
        ApiManager.callApi(dao.createAccount(email, password), new CustomCallback<AuthResponse>() {
            @Override
            public void onResponse(AuthResponse response) {
                if (response.getSuccess()) {

                    Log.d(TAG, response.toString());

                    try {
                        Log.d(TAG, "Successful New User");

                        saveUserToPrefs(response.getUser());
                        Intent goToEditProfileActivity = new Intent(CreateAccountActivity.this, EditProfileActivity.class);
                        startActivity(goToEditProfileActivity);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(thisContext, response.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void onLoginActivityClick(View view) {
        Intent k = new Intent(this, MainActivity.class);
        startActivity(k);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

}
